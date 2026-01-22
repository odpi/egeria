/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.client;


import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.OCFConnectionResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworkservices.omf.connectors.outtopic.OMFOutTopicClientConnector;
import org.odpi.openmetadata.frameworkservices.omf.ffdc.OMFServicesAuditCode;
import org.odpi.openmetadata.frameworkservices.omf.ffdc.OMFServicesErrorCode;


/**
 * EgeriaOpenMetadataEventClient provides the implementation to manage the interaction with the server to
 * set up a listener to support the receipt of inbound events from the Open Metadata Store Out Topic.
 */
public class EgeriaOpenMetadataEventClient extends OpenMetadataEventClient
{
    private static final String  serviceName = AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceName();
    private final OCFRESTClient restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private OMFOutTopicClientConnector configurationEventTopicConnector = null;



    /**
     * Create a new client that is to be used within an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId this server's userId
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @param callerId unique identifier of the caller
     * @throws InvalidParameterException a problem with the information about the remote OMAS
     */
    public EgeriaOpenMetadataEventClient(String   serverName,
                                         String   serverPlatformURLRoot,
                                         String   serverUserId,
                                         String   secretsStoreProvider,
                                         String   secretsStoreLocation,
                                         String   secretsStoreCollection,
                                         int      maxPageSize,
                                         AuditLog auditLog,
                                         String   callerId) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serverUserId, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, serviceName, maxPageSize, auditLog, callerId);

        final String methodName = "Constructor (with REST Client)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = new OCFRESTClient(serverName, serverPlatformURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, auditLog);
    }


    /**
     * Register a listener object that will be passed the events published by
     * the Open Metadata Store.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void registerListener(String                    userId,
                                 OpenMetadataEventListener listener) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "registerListener";
        final String nameParameter = "listener";
        final String callerIdParameter = "callerId";

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/topics/out-topic-connection/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(listener, nameParameter, methodName);
        invalidParameterHandler.validateName(callerId, callerIdParameter, methodName);

        if (configurationEventTopicConnector == null)
        {
            try
            {
                /*
                 * The connector is only created if/when a listener is registered to prevent unnecessary load on the
                 * event bus.
                 */
                OCFConnectionResponse restResult = restClient.callOCFConnectionGetRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           callerId);

                Connection      topicConnection = restResult.getConnection();
                ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);
                Connector       connector       = connectorBroker.getConnector(topicConnection);

                if (connector == null)
                {
                    throw new PropertyServerException(OMFServicesErrorCode.NULL_CONNECTOR_RETURNED.getMessageDefinition(topicConnection.getQualifiedName(),
                                                                                                                        serviceName,
                                                                                                                        serverName,
                                                                                                                        serverPlatformURLRoot),
                                                      this.getClass().getName(),
                                                      methodName);
                }

                if (connector instanceof OMFOutTopicClientConnector)
                {
                    configurationEventTopicConnector = (OMFOutTopicClientConnector) connector;
                    configurationEventTopicConnector.start();
                }
                else
                {
                    throw new PropertyServerException(OMFServicesErrorCode.WRONG_TYPE_OF_CONNECTOR.getMessageDefinition(topicConnection.getQualifiedName(),
                                                                                                                        serviceName,
                                                                                                                        serverName,
                                                                                                                        serverPlatformURLRoot,
                                                                                                                        OMFOutTopicClientConnector.class.getName()),
                                                      this.getClass().getName(),
                                                      methodName);
                }
            }
            catch (Exception connectorError)
            {
                throw new PropertyServerException(OMFServicesErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorError.getClass().getName(),
                                                                                                                 methodName,
                                                                                                                 serviceName,
                                                                                                                 connectorError.getMessage()),
                                                  this.getClass().getName(),
                                                  methodName);
            }
        }

        configurationEventTopicConnector.registerListener(userId, listener);
    }


    /**
     * Shutdown the event listener.
     */
    public void disconnect()
    {
        final String methodName = "disconnect";

        if (configurationEventTopicConnector != null)
        {
            try
            {
                configurationEventTopicConnector.disconnect();
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                   auditLog.logException(methodName,
                                         OMFServicesAuditCode.EVENT_SHUTDOWN_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                        error.getMessage()),
                                         error);

                }
            }
        }
    }
}
