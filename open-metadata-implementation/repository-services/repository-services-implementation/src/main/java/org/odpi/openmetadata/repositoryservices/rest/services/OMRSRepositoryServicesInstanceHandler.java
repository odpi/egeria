/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.services;

import org.odpi.openmetadata.commonservices.multitenant.AuditableServerServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.requestid.ActiveRequests;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.metadataobservability.ffdc.OpenMetadataObservabilityAuditCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * OMRSRepositoryServicesInstanceHandler provides the mapping for inbound REST requests to the appropriate instances
 * for the requested server.  The map is maintained in a static, so it is scoped to the class loader.
 * Instances of this class call the synchronized static methods to work with the map.
 */
public class OMRSRepositoryServicesInstanceHandler extends OMAGServerServiceInstanceHandler
{

    /**
     * Constructor
     *
     * @param serviceName name of this service for error logging
     */
    public OMRSRepositoryServicesInstanceHandler(String   serviceName)
    {
        super(serviceName);
    }


    /**
     * Get the object containing the properties for this server.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return specific service instance
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws RepositoryErrorException the service name is not known - indicating a logic error
     */
    public OMRSRepositoryServicesInstance getInstance(String   userId,
                                                      String   serverName,
                                                      String   serviceOperationName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            RepositoryErrorException
    {
        try
        {
            return (OMRSRepositoryServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);
        }
        catch (PropertyServerException error)
        {
            throw new org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException(error);
        }
    }


    /**
     * Return the audit log or null (if the instance is not available).
     *
     * @param userId calling user
     * @param serverName requested server
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return audit log or null
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws RepositoryErrorException the service name is not know - indicating a logic error
     */
    public AuditLog getAuditLog(String userId,
                                String serverName,
                                String serviceOperationName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    RepositoryErrorException
    {
        final String actionDescription = "userMonitoring";

        OMRSRepositoryServicesInstance instance = this.getInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            AuditLog auditLog = instance.getAuditLog();

            reportUserRequest(auditLog, actionDescription, userId, serviceOperationName, serverName);

            return auditLog;
        }

        return null;
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

        OMRSRepositoryServicesInstance instance = (OMRSRepositoryServicesInstance) super.getServerServiceInstance(userId,
                                                                                                                  delegatingUserId,
                                                                                                                  serverName,
                                                                                                                  serviceOperationName);

        if (instance != null)
        {
            AuditLog auditLog = instance.getAuditLog();

            reportUserRequest(auditLog, actionDescription, (delegatingUserId != null) ? delegatingUserId : userId, serviceOperationName, serverName);

            return auditLog;
        }

        return null;
    }


    /**
     * Remove the instance for this server.
     *
     * @param serverName name of the server
     */
    public void removeInstance(String   serverName)
    {
        super.removeServerServiceInstance(serverName);
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
