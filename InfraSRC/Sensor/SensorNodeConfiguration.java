
package org.rule.engine.aws.sns;

import lombok.Data;

@Data
public class SensorNodeConfiguration implements NodeConfiguration<TbSnsNodeConfiguration> {

    private String topicArnPattern;
    private String accessKeyId;
    private String secretAccessKey;
    private String region;

    @Override
    public SensorNodeConfiguration defaultConfiguration() {
        TbSnsNodeConfiguration configuration = new TbSnsNodeConfiguration();
        configuration.setTopicArnPattern("arn:aws:sns:us-east-1:123456789012:MyNewTopic");
        configuration.setRegion("us-east-1");
        return configuration;
    }
}
