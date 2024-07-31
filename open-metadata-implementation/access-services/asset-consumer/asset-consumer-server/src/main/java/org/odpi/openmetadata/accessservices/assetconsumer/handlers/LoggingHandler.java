/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.handlers;

import org.odpi.openmetadata.accessservices.assetconsumer.api.AssetConsumerLoggingInterface;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;


/**
 * LoggingHandler manages the logging of audit records for the asset.
 */
public class LoggingHandler implements AssetConsumerLoggingInterface
{

    private final AuditLog auditLog;



    /**
     * Construct the audit log handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param auditLog  connector to the property server.
     */
    public LoggingHandler(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Creates an Audit log record for the asset.  This log record is stored in the Asset's Audit Log.
     *
     * @param userId  String - userId of user making request.
     * @param assetGUID  String - unique id for the asset.
     * @param connectorInstanceId  String - (optional) id of connector in use (if any).
     * @param connectionName  String - (optional) name of the connection (extracted from the connector).
     * @param connectorType  String - (optional) type of connector in use (if any).
     * @param contextId  String - (optional) function name, or processId of the activity that the caller is performing.
     * @param message  log record content.
     */
    public void  addLogMessageToAsset(String      userId,
                                      String      assetGUID,
                                      String      connectorInstanceId,
                                      String      connectionName,
                                      String      connectorType,
                                      String      contextId,
                                      String      message)
    {
        final String        methodName = "addLogMessageToAsset";

        String              additionalInformation = "User: " + userId +
                                                    "ContextId: " + contextId +
                                                    "Connector Instance Id: " + connectorInstanceId +
                                                    "Connection Name: " + connectionName +
                                                    "Connector Type: " + connectorType;

        auditLog.logMessage(methodName,
                            AssetConsumerAuditCode.ASSET_AUDIT_LOG.getMessageDefinition(assetGUID, message),
                            additionalInformation);
    }
}
