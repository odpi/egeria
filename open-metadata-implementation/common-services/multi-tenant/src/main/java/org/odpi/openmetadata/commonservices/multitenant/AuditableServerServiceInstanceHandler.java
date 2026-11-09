/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.requestid.ActiveRequests;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadataobservability.ffdc.OpenMetadataObservabilityAuditCode;

/**
 * AuditableServerServiceInstanceHandler supports registered services that have an audit log.
 */
public abstract class AuditableServerServiceInstanceHandler extends OMAGServerServiceInstanceHandler
{
    /**
     * Constructor passes the service name that is used on all calls to this instance.
     *
     * @param serviceName unique identifier for this service with a human meaningful value
     */
    AuditableServerServiceInstanceHandler(String       serviceName)
    {
        super(serviceName);
    }


    /**
     * Return the audit log for this access service and server.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return audit log
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public AuditLog getAuditLog(String userId,
                                String serverName,
                                String serviceOperationName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String actionDescription = "userMonitoring";

        AuditableServerServiceInstance instance = (AuditableServerServiceInstance) super.getServerServiceInstance(userId,
                                                                                                                  serverName,
                                                                                                                  serviceOperationName);

        AuditLog auditLog = instance.getAuditLog();

        reportUserRequest(auditLog, actionDescription, userId, serviceOperationName, serverName);

        return auditLog;
    }


    /**
     * Return the audit log for this access service and server.
     *
     * @param userId calling userId
     * @param delegatingUserId external userId making request
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return audit log
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public AuditLog getAuditLog(String userId,
                                String delegatingUserId,
                                String serverName,
                                String serviceOperationName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String actionDescription = "userMonitoring";

        AuditableServerServiceInstance instance = (AuditableServerServiceInstance) super.getServerServiceInstance(userId,
                                                                                                                  delegatingUserId,
                                                                                                                  serverName,
                                                                                                                  serviceOperationName);

        AuditLog auditLog = instance.getAuditLog();

        reportUserRequest(auditLog, actionDescription, (delegatingUserId != null) ? delegatingUserId : userId, serviceOperationName, serverName);

        return auditLog;
    }


    /**
     * Record that a user has called a REST API operation.  A call made by another server on this platform on behalf
     * of a request it is already processing is not recorded, so each external request is recorded once.
     *
     * @param auditLog audit log for the service
     * @param actionDescription calling activity
     * @param userId user making the request
     * @param serviceOperationName name of the REST API call
     * @param serverName name of the server
     */
    private void reportUserRequest(AuditLog auditLog,
                                   String   actionDescription,
                                   String   userId,
                                   String   serviceOperationName,
                                   String   serverName)
    {
        if (! ActiveRequests.isNestedRequest())
        {
            auditLog.logMessage(actionDescription, OpenMetadataObservabilityAuditCode.USER_REQUEST_ACTIVITY.getMessageDefinition(userId,
                                                                                                                                 serviceOperationName,
                                                                                                                                 serviceName,
                                                                                                                                 serverName));
        }
    }
}
