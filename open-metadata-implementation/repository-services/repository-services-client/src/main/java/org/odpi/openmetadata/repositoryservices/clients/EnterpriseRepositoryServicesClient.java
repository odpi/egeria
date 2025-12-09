/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.clients;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicRepositoryEventListener;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.rest.properties.ConnectionResponse;

import java.util.Map;

/**
 * EnterpriseRepositoryServicesClient provides a client interface for calling the enterprise repository
 * services in a remote server.
 */
public class EnterpriseRepositoryServicesClient extends MetadataCollectionServicesClient
{
    private OMRSTopicConnector remoteEnterpriseTopicConnector = null;
    private String             callerId;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param repositoryName name of the server to connect to
     * @param restURLRoot the network address of the server running the repository services.  This is of the form
     *                    serverURLroot + "/servers/" + serverName.
     * @param secretsStoreConnectorMap map from authentication type to supplied secrets store
     * @param maxPageSize pre-initialized parameter limit
     * @param callerId unique identifier of the caller
     *
     * @throws InvalidParameterException bad input parameters
     */
    public EnterpriseRepositoryServicesClient(String                             repositoryName,
                                              String                             restURLRoot,
                                              Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                              int                                maxPageSize,
                                              String                             callerId) throws InvalidParameterException
    {
        super(repositoryName, restURLRoot, "/enterprise/", secretsStoreConnectorMap);

        this.callerId = callerId;
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param repositoryName name of the server to connect to
     * @param restURLRoot the network address of the server running the repository services.  This is of the form
     *                    serverURLroot + "/servers/" + serverName.
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param auditLog audit log
     * @param maxPageSize pre-initialized parameter limit
     * @param callerId unique identifier of the caller
     *
     * @throws InvalidParameterException bad input parameters
     */
    public EnterpriseRepositoryServicesClient(String   repositoryName,
                                              String   restURLRoot,
                                              String   localServerSecretsStoreProvider,
                                              String   localServerSecretsStoreLocation,
                                              String   localServerSecretsStoreCollection,
                                              AuditLog auditLog,
                                              int      maxPageSize,
                                              String   callerId) throws InvalidParameterException
    {
        super(repositoryName, restURLRoot, "/enterprise/", localServerSecretsStoreProvider, localServerSecretsStoreLocation, localServerSecretsStoreCollection, auditLog);

        this.callerId = callerId;
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
    }


    /**
     * Register a listener object that will be passed each of the events published by
     * the Asset Manager OMAS.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws RepositoryErrorException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(String                           userId,
                                 OMRSTopicRepositoryEventListener listener) throws InvalidParameterException,
                                                                                   ConnectionCheckedException,
                                                                                   ConnectorCheckedException,
                                                                                   RepositoryErrorException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "registerListener";
        final String nameParameter = "listener";
        final String callerIdParameter = "callerId";

        final String urlTemplate = "/open-metadata/repository-services/users/{0}/enterprise/remote-topic-connection";

        try
        {
            invalidParameterHandler.validateUserId(userId, methodName);
            invalidParameterHandler.validateObject(listener, nameParameter, methodName);
            invalidParameterHandler.validateName(callerId, callerIdParameter, methodName);
        }
        catch (org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException error)
        {
            throw new InvalidParameterException(error.getReportedHTTPCode(),
                                                error.getReportingClassName(),
                                                error.getReportingActionDescription(),
                                                error.getReportedErrorMessage(),
                                                error.getReportedErrorMessageId(),
                                                error.getReportedErrorMessageParameters(),
                                                error.getReportedSystemAction(),
                                                error.getReportedUserAction(),
                                                error.getClass().getName(),
                                                error.getParameterName(),
                                                error.getRelatedProperties());
        }

        if (remoteEnterpriseTopicConnector == null)
        {
            try
            {
                /*
                 * The connector is only created if/when a listener is registered to prevent unnecessary load on the
                 * event bus.
                 */
                ConnectionResponse restResult = restClient.callGetRESTCall(methodName, ConnectionResponse.class, restURLRoot + urlTemplate, userId);

                super.detectAndThrowInvalidParameterException(methodName, restResult);
                super.detectAndThrowUserNotAuthorizedException(methodName, restResult);
                super.detectAndThrowRepositoryErrorException(methodName, restResult);

                Connection      topicConnection = restResult.getConnection();
                ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);
                Connector       connector       = connectorBroker.getConnector(topicConnection);

                if (connector == null)
                {
                    throw new ConnectorCheckedException(OMRSErrorCode.NULL_CONNECTOR_RETURNED.getMessageDefinition(topicConnection.getQualifiedName(),
                                                                                                                   repositoryName,
                                                                                                                   restURLRoot),
                                                        this.getClass().getName(),
                                                        methodName);
                }

                if (connector instanceof OMRSTopicConnector)
                {
                    remoteEnterpriseTopicConnector = (OMRSTopicConnector) connector;
                    remoteEnterpriseTopicConnector.start();
                }
                else
                {
                    throw new ConnectorCheckedException(OMRSErrorCode.WRONG_TYPE_OF_CONNECTOR.getMessageDefinition(topicConnection.getQualifiedName(),
                                                                                                                   repositoryName,
                                                                                                                   restURLRoot,
                                                                                                                   OMRSTopicConnector.class.getName()),
                                                        this.getClass().getName(),
                                                        methodName);
                }

                remoteEnterpriseTopicConnector.registerListener(listener, repositoryName);
            }
            catch (ConnectorCheckedException | ConnectionCheckedException | RepositoryErrorException  error)
            {
                throw error;
            }
            catch (Exception error)
            {
                throw new RepositoryErrorException(OMRSErrorCode.REMOTE_REPOSITORY_ERROR.getMessageDefinition(methodName,
                                                                                                              repositoryName,
                                                                                                              error.getClass().getSimpleName(),
                                                                                                              error.getMessage()),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   error);
            }
        }
    }


    /**
     * Disconnect from the topic connector - events will no longer be passed to the registered listeners.
     *
     * @throws ConnectorCheckedException problem with the topic connector
     */
    public void disconnectFromEnterpriseTopic() throws ConnectorCheckedException
    {
        if (remoteEnterpriseTopicConnector != null)
        {
            remoteEnterpriseTopicConnector.disconnect();
        }
    }
}
