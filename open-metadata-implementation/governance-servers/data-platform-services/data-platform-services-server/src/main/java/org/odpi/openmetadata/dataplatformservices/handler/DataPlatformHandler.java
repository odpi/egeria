package org.odpi.openmetadata.dataplatformservices.handler;

import org.odpi.openmetadata.accessservices.dataplatform.client.DataPlatformClient;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

public class DataPlatformHandler {

    private String                       serverName;               /* Initialized in constructor */
    private String                       serverUserId;             /* Initialized in constructor */
    private OMRSAuditLog                 auditLog;                 /* Initialized in constructor */
    private DataPlatformClient           dataPlatformClient;       /* Initialized in constructor */


    public DataPlatformHandler(String serverName, String serverUserId, OMRSAuditLog auditLog, DataPlatformClient dataPlatformClient) {
        this.serverName = serverName;
        this.serverUserId = serverUserId;
        this.auditLog = auditLog;
        this.dataPlatformClient = dataPlatformClient;
    }


}
