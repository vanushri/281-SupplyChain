public interface UserService {
	
	User findUserById(TenantId tenantId, UserId userId);

	ListenableFuture<User> findUserByIdAsync(TenantId tenantId, UserId userId);

	User findUserByEmail(TenantId tenantId, String email);

	User saveUser(User user);

	UserCredentials findUserCredentialsByUserId(TenantId tenantId, UserId userId);
	
	UserCredentials findUserCredentialsByActivateToken(TenantId tenantId, String activateToken);

	UserCredentials findUserCredentialsByResetToken(TenantId tenantId, String resetToken);

	UserCredentials saveUserCredentials(TenantId tenantId, UserCredentials userCredentials);
	
	UserCredentials activateUserCredentials(TenantId tenantId, String activateToken, String password);
	
	UserCredentials requestPasswordReset(TenantId tenantId, String email);

	void deleteUser(TenantId tenantId, UserId userId);
	
	TextPageData<User> findTenantAdmins(TenantId tenantId, TextPageLink pageLink);
	
	void deleteTenantAdmins(TenantId tenantId);
	
	TextPageData<User> findCustomerUsers(TenantId tenantId, CustomerId customerId, TextPageLink pageLink);
	    
	void deleteCustomerUsers(TenantId tenantId, CustomerId customerId);
	
}
