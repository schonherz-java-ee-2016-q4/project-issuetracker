package hu.schonherz.javatraining.issuetracker.client.api.shared;

public class AdminJNDIConstants {
    private static final String JNDI_GLOBAL_PREFIX = "java:global/";
    private static final String JNDI_ADMIN_MODULE = "admin-ear-0.0.1-SNAPSHOT/admin-service-0.0.1-SNAPSHOT/";

    public static final String JNDI_LOGIN_SERVICE = JNDI_GLOBAL_PREFIX + JNDI_ADMIN_MODULE
        + "RemoteLoginServiceBean!hu.schonherz.project.remote.admin.api.rpc.issuetracker.RemoteLoginService";
    
    public static final String JNDI_QOUTAS_SERVICE = JNDI_GLOBAL_PREFIX + JNDI_ADMIN_MODULE
        + "RemoteQuotasServiceBean!hu.schonherz.project.remote.admin.api.rpc.issuetracker.RemoteQuotasService";

    private AdminJNDIConstants() {}
}
