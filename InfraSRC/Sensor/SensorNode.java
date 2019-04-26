
package org.rule.engine.aws.sns;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;


import java.util.concurrent.ExecutionException;

import static org.rule.engine.api.util.DonAsynchron.withCallback;


@RuleNode(
        type = ComponentType.EXTERNAL,
        name = "aws sns",
        configClazz = SensorNodeConfiguration.class,
        nodeDescription = "Publish message to the AWS SNS",
        nodeDetails = "Will publish message payload to the AWS SNS topic. Outbound message will contain response fields " +
                "(<code>messageId</code>, <code>requestId</code>) in the Message Metadata from the AWS SNS. " +
                "For example <b>requestId</b> field can be accessed with <code>metadata.requestId</code>.",
        uiResources = {"static/rulenode/rulenode-core-config.js"},
        configDirective = "tbActionNodeSnsConfig",
        
)
public class SensorNode implements TbNode {

    private static final String MESSAGE_ID = "messageId";
    private static final String REQUEST_ID = "requestId";
    private static final String ERROR = "error";

    private SensorNodeConfiguration config;
    private AmazonSNS snsClient;

    @Override
    public void init(TbContext ctx, TbNodeConfiguration configuration) throws TbNodeException {
        this.config = TbNodeUtils.convert(configuration, SensorNodeConfiguration.class);
        AWSCredentials awsCredentials = new BasicAWSCredentials(this.config.getAccessKeyId(), this.config.getSecretAccessKey());
        AWSStaticCredentialsProvider credProvider = new AWSStaticCredentialsProvider(awsCredentials);
        try {
            this.snsClient = AmazonSNSClient.builder()
                    .withCredentials(credProvider)
                    .withRegion(this.config.getRegion())
                    .build();
        } catch (Exception e) {
            throw new TbNodeException(e);
        }
    }

    @Override
    public void onMsg(TbContext ctx, TbMsg msg) throws ExecutionException, InterruptedException, TbNodeException {
        withCallback(publishMessageAsync(ctx, msg),
                m -> ctx.tellNext(m, TbRelationTypes.SUCCESS),
                t -> {
                    TbMsg next = processException(ctx, msg, t);
                    ctx.tellFailure(next, t);
                });
    }

    private ListenableFuture<TbMsg> publishMessageAsync(TbContext ctx, TbMsg msg) {
        return ctx.getExternalCallExecutor().executeAsync(() -> publishMessage(ctx, msg));
    }

    private TbMsg publishMessage(TbContext ctx, TbMsg msg) {
        String topicArn = TbNodeUtils.processPattern(this.config.getTopicArnPattern(), msg.getMetaData());
        PublishRequest publishRequest = new PublishRequest()
                .withTopicArn(topicArn)
                .withMessage(msg.getData());
        PublishResult result = this.snsClient.publish(publishRequest);
        return processPublishResult(ctx, msg, result);
    }

    private TbMsg processPublishResult(TbContext ctx, TbMsg origMsg, PublishResult result) {
        TbMsgMetaData metaData = origMsg.getMetaData().copy();
        metaData.putValue(MESSAGE_ID, result.getMessageId());
        metaData.putValue(REQUEST_ID, result.getSdkResponseMetadata().getRequestId());
        return ctx.transformMsg(origMsg, origMsg.getType(), origMsg.getOriginator(), metaData, origMsg.getData());
    }

    private TbMsg processException(TbContext ctx, TbMsg origMsg, Throwable t) {
        TbMsgMetaData metaData = origMsg.getMetaData().copy();
        metaData.putValue(ERROR, t.getClass() + ": " + t.getMessage());
        return ctx.transformMsg(origMsg, origMsg.getType(), origMsg.getOriginator(), metaData, origMsg.getData());
    }

    @Override
    public void destroy() {
        if (this.snsClient != null) {
            try {
                this.snsClient.shutdown();
            } catch (Exception e) {
                log.error("Failed to shutdown SNS client during destroy()", e);
            }
        }
    }
}
