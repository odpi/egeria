/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreCollectionProperty;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFRuntimeException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ConnectorBroker is a generic factory for Open Connector Framework (OCF) Connectors.
 * The OCF provides a default implementation because all the implementation that is specific to a
 * particular type of connector is delegated to the connector provider specified in the connection.
 */
public class ConnectorBroker
{
    private static final Logger log      = LoggerFactory.getLogger(ConnectorBroker.class);

    private AuditLog auditLog = null;


    /**
     * Typical constructor
     */
    public ConnectorBroker()
    {
        /* Nothing to do */
    }


    /**
     * Constructor to supply the audit log to all connectors that implement the AuditLoggingConnector interface.
     *
     * @param auditLog audit log to pass on to the connector providers
     */
    public ConnectorBroker(AuditLog   auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Validate that the connection (or the embedded connections if this is a virtual connection)
     * are not null.
     *
     * @param connection connection properties
     * @throws ConnectionCheckedException null connection detected
     */
    private void  validateConnectionNotNull(Connection connection) throws ConnectionCheckedException
    {
        final String methodName = "validateConnectionNotNull";

        if (connection == null)
        {
            /*
             * It is not possible to create a connector without a connection.
             */
            throw new ConnectionCheckedException(OCFErrorCode.NULL_CONNECTION.getMessageDefinition(),
                                                 this.getClass().getName(),
                                                 methodName);
        }

        if (connection instanceof VirtualConnection virtualConnection)
        {
            List<EmbeddedConnection> embeddedConnections = virtualConnection.getEmbeddedConnections();

            if ((embeddedConnections == null) || (embeddedConnections.isEmpty()))
            {
                throw new ConnectionCheckedException(OCFErrorCode.INVALID_VIRTUAL_CONNECTION.getMessageDefinition(connection.getDisplayName()),
                                                     this.getClass().getName(),
                                                     methodName);
            }
        }
    }


    /**
     * Return the connector type from the supplied connection.
     *
     * @param connection connection properties
     * @param methodName calling method
     * @return ConnectorType object
     * @throws ConnectionCheckedException connector type not defined in connection
     */
    private ConnectorType getConnectorType(Connection connection,
                                           String     methodName) throws ConnectionCheckedException
    {
        ConnectorType requestedConnectorType = connection.getConnectorType();

        if (requestedConnectorType == null)
        {
            /*
             * It is not possible to create a connector without a connector type since it
             * holds the name of the connector provider's Java class.  Build an exception.
             */
            throw new ConnectionCheckedException(OCFErrorCode.NULL_CONNECTOR_TYPE.getMessageDefinition(connection.getDisplayName()),
                                                 this.getClass().getName(),
                                                 methodName);
        }

        return requestedConnectorType;
    }


    /**
     * Create a connector provider object based on the content of the supplied connector type properties.
     *
     * @param requestedConnectorType  connector type properties
     * @param connectionName  name of the connection (for error handling)
     * @param methodName calling method (for error handling)
     * @return ConnectorProvider object
     * @throws ConnectionCheckedException unable to create the connector provider from the supplied information
     */
    private ConnectorProvider  getConnectorProvider(ConnectorType requestedConnectorType,
                                                    String        connectionName,
                                                    String        methodName) throws ConnectionCheckedException
    {
        /*
         * The class name for the connector provider is in the connectorType properties
         */
        String    connectorProviderClassName = requestedConnectorType.getConnectorProviderClassName();

        if (connectorProviderClassName == null)
        {
            /*
             * The connector provider class name is blank, so it is not possible to create the
             * connector provider.  Throw an exception.
             */
            throw new ConnectionCheckedException(OCFErrorCode.NULL_CONNECTOR_PROVIDER.getMessageDefinition(connectionName),
                                                 this.getClass().getName(),
                                                 methodName);
        }


        /*
         * Extract the class for the connector provider and then create a connector provider object.
         * These actions may reveal that the class is not known to local JVM (ClassNotFound) or
         * the class is there but its dependencies are not (LinkageError).  Each of these error conditions
         * results in a connection error exception that hopefully guides the consumer to correct
         * the config and/or setup error.
         */
        ConnectorProvider     connectorProvider;

        try
        {
            Class<?>   connectorProviderClass = Class.forName(connectorProviderClassName);
            Object     potentialConnectorProvider = connectorProviderClass.getDeclaredConstructor().newInstance();

            connectorProvider = (ConnectorProvider)potentialConnectorProvider;
        }
        catch (ClassNotFoundException classException)
        {
            /*
             * Wrap exception in the ConnectionCheckedException with a suitable message
             */
            throw new ConnectionCheckedException(OCFErrorCode.UNKNOWN_CONNECTOR_PROVIDER.getMessageDefinition(connectorProviderClassName,
                                                                                                              connectionName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 classException);
        }
        catch (ClassCastException  castException)
        {
            /*
             * Wrap class cast exception in a connection exception with error message to say that
             */
            throw new ConnectionCheckedException(OCFErrorCode.NOT_CONNECTOR_PROVIDER.getMessageDefinition(connectorProviderClassName,
                                                                                                          connectionName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 castException);
        }
        catch (Exception unexpectedSomething)
        {
            /*
             * Wrap exception in a connection exception with error message to say that there was a problem with
             * the connector provider.
             */
            throw new ConnectionCheckedException(OCFErrorCode.INVALID_CONNECTOR_PROVIDER.getMessageDefinition(connectorProviderClassName,
                                                                                                              connectionName,
                                                                                                              unexpectedSomething.getClass().getName(),
                                                                                                              unexpectedSomething.getMessage()),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 unexpectedSomething);
        }

        return connectorProvider;
    }


    /**
     * Extract the connection from the embedded connection and push any arguments into the
     * ConfigurationProperties for the connection.
     *
     * @param embeddedConnection embedded connection object.
     * @return connection object augmented with the arguments from the embedded connection.
     */
    private Connection getConnection(EmbeddedConnection embeddedConnection)
    {
        if (embeddedConnection != null)
        {
            Connection updatedConnection = embeddedConnection.getEmbeddedConnection();

            updatedConnection.setConfigurationProperties(this.addArgumentsToConfigurationProperties(embeddedConnection.getArguments(),
                                                                                                    updatedConnection.getConfigurationProperties()));

            return updatedConnection;
        }

        return null;
    }


    /**
     * Combine arguments from the embedded connection with the embedded configuration properties
     *
     * @param arguments arguments
     * @param configurationProperties configuration properties
     * @return combination
     */
    private Map<String, Object> addArgumentsToConfigurationProperties(Map<String, Object>  arguments,
                                                                      Map<String, Object>  configurationProperties)
    {
        if (configurationProperties == null)
        {
            configurationProperties = new HashMap<>();
        }

        if (arguments != null)
        {
            for (String argumentName : arguments.keySet())
            {
                configurationProperties.put(argumentName, arguments.get(argumentName).toString());
            }
        }

        if (configurationProperties.isEmpty())
        {
            configurationProperties = null;
        }

        return configurationProperties;
    }


    /**
     * Validate that the connection has sufficient properties to attempt to create a connector.
     * Any problems found are expressed as a ConnectionCheckedException.
     *
     * @param connection connection properties
     * @throws ConnectionCheckedException an error with the connection.
     */
    public void validateConnection(Connection connection) throws ConnectionCheckedException
    {
        final String   methodName = "validateConnection";

        log.debug("==> ConnectorBroker." + methodName);

        validateConnectionNotNull(connection);

        ConnectorType requestedConnectorType = this.getConnectorType(connection, methodName);

        getConnectorProvider(requestedConnectorType, connection.getDisplayName(), methodName);

        log.debug("<== ConnectorBroker." + methodName);
    }




    /**
     * Creates a new instance of a connector using the name of the connector provider in the supplied connection.
     *
     * @param connection   properties for the connector and connector provider.
     * @return new connector instance.
     * @throws ConnectionCheckedException an error with the connection.
     * @throws ConnectorCheckedException an error initializing the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    public Connector getConnector(Connection connection) throws ConnectionCheckedException,
                                                                ConnectorCheckedException,
                                                                UserNotAuthorizedException
    {
        final String         methodName = "getConnector";
        String               connectionName;

        log.debug("==> ConnectorBroker." + methodName);

        this.validateConnection(connection);
        connectionName = connection.getDisplayName();


        /*
         * Within the connection is a structure called the connector type.  This defines the factory for a new
         * connector instance.  This factory is called the Connector Provider.
         */
        ConnectorType requestedConnectorType = this.getConnectorType(connection, methodName);


        /*
         * The connection has a valid connector type so the next step is to extract the class name for the
         * connector provider.  This is the specialized factory for the connector.
         * Then create an instance of this class.
         */
        ConnectorProvider connectorProvider = this.getConnectorProvider(requestedConnectorType,
                                                                        connectionName,
                                                                        methodName);

        /*
         * If the connector provider or connector is capable of using an audit log, an audit log is passed to the connector provider if available.
         */
        if (connectorProvider instanceof AuditLoggingComponent)
        {
            ((AuditLoggingComponent) connectorProvider).setAuditLog(auditLog);
        }

        List<Connector>                    embeddedConnectors       = new ArrayList<>();
        Map<String, SecretsStoreConnector> secretsStoreConnectorMap = new HashMap<>();

        /*
         * If a virtual connection was passed to the connector broker then the connector broker needs to create
         * connectors for its embedded connections.
         */
        if (connection instanceof VirtualConnection virtualConnectionDetails)
        {
            /*
             * All ok so create the embedded connectors.  Note: one of these connectors may itself be
             * a virtual connector with embedded connectors of its own.  So this method may be called
             * recursively as the tree of connectors is explored.
             */
            log.debug("Creating embedded connectors for connection name: " + connectionName);

            List<EmbeddedConnection> embeddedConnections = virtualConnectionDetails.getEmbeddedConnections();

            for (EmbeddedConnection embeddedConnection : embeddedConnections)
            {
                Connector embeddedConnector = getConnector(this.getConnection(embeddedConnection));

                /*
                 * If the embedded connector is a secrets store connector, extract the standard secrets and add them to the new
                 * connector's connection.  It first tries to retrieve the secrets directly from the store, or via any names specified in the
                 * securedProperties.
                 */
                if (embeddedConnector instanceof SecretsStoreConnector secretsStoreConnector)
                {
                    secretsStoreConnector.start();

                    if (connection.getUserId() == null)
                    {
                        connection.setUserId(secretsStoreConnector.getSecret(SecretsStoreCollectionProperty.USER_ID.getName()));

                        if (connection.getUserId() == null)
                        {
                            if ((connection.getSecuredProperties() != null) && (connection.getSecuredProperties().get(SecretsStoreCollectionProperty.USER_ID.getName()) != null))
                            {
                                connection.setUserId(secretsStoreConnector.getSecret(connection.getSecuredProperties().get(SecretsStoreCollectionProperty.USER_ID.getName())));
                            }
                        }
                    }
                    if (connection.getClearPassword() == null)
                    {
                        connection.setClearPassword(secretsStoreConnector.getSecret(SecretsStoreCollectionProperty.CLEAR_PASSWORD.getName()));

                        if (connection.getClearPassword() == null)
                        {
                            if ((connection.getSecuredProperties() != null) && (connection.getSecuredProperties().get(SecretsStoreCollectionProperty.CLEAR_PASSWORD.getName()) != null))
                            {
                                connection.setClearPassword(secretsStoreConnector.getSecret(connection.getSecuredProperties().get(SecretsStoreCollectionProperty.CLEAR_PASSWORD.getName())));
                            }
                        }
                    }
                    if (connection.getEncryptedPassword() == null)
                    {
                        connection.setEncryptedPassword(secretsStoreConnector.getSecret(SecretsStoreCollectionProperty.ENCRYPTED_PASSWORD.getName()));

                        if (connection.getEncryptedPassword() == null)
                        {
                            if ((connection.getSecuredProperties() != null) && (connection.getSecuredProperties().get(SecretsStoreCollectionProperty.ENCRYPTED_PASSWORD.getName()) != null))
                            {
                                connection.setEncryptedPassword(secretsStoreConnector.getSecret(connection.getSecuredProperties().get(SecretsStoreCollectionProperty.ENCRYPTED_PASSWORD.getName())));
                            }
                        }
                    }

                    if (embeddedConnection.getDisplayName() != null)
                    {
                        secretsStoreConnectorMap.put(embeddedConnection.getDisplayName(), secretsStoreConnector);
                    }
                    else
                    {
                        secretsStoreConnectorMap.put(String.valueOf(secretsStoreConnectorMap.size()), secretsStoreConnector);
                    }
                }

                embeddedConnectors.add(embeddedConnector);
            }
        }



        /*
         * At this point we hopefully have a valid connector provider so all that is left to do is call
         * it to get the connector instance.  This is done in a different try ... catch block from the
         * instantiation of the connector provider, so we can separate errors in the Connection from
         * errors generated in the Connector Provider, since both classes are
         * potentially code from a source outside of Egeria.
         */
        Connector    connectorInstance;

        try
        {
            /*
             * The connector is initialized status at this point.
             */
            connectorInstance = connectorProvider.getConnector(connection);
        }
        catch (ConnectionCheckedException | ConnectorCheckedException ocfError)
        {
            /*
             * The connector provider has already provided first failure data capture in an OCF Exception.
             * This exception is rethrown to the caller.
             */
            throw ocfError;
        }
        catch (Exception  unexpectedSomething)
        {
            /*
             * The connector provider threw an unexpected runtime exception or error.  This is wrapped in a
             * ConnectorError and thrown to caller.
             */
            throw new ConnectorCheckedException(OCFErrorCode.CAUGHT_EXCEPTION.getMessageDefinition(),
                                                this.getClass().getName(),
                                                "getConnector",
                                                unexpectedSomething);
        }


        /*
         * If the connector provider has returned a null connector with no exception then raise a generic exception
         * since something has gone wrong.
         */
        if (connectorInstance == null)
        {
            /*
             * Build and throw exception.
             */
            throw new OMFRuntimeException(OCFErrorCode.NULL_CONNECTOR.getMessageDefinition(requestedConnectorType.getConnectorProviderClassName(),
                                                                                           connection.getDisplayName()),
                                          this.getClass().getName(),
                                          methodName);
        }


        /*
         * If a virtual connection was passed to the connector broker then pass
         * connectors for its embedded connections.
         */
        if (connectorInstance instanceof VirtualConnectorExtension virtualConnectorExtension)
        {
            virtualConnectorExtension.initializeEmbeddedConnectors(embeddedConnectors);
        }

        /*
         * Add the secrets connectors to the new connector.
         */
        if (connectorInstance instanceof SecureConnectorExtension secureConnectorExtension)
        {
            for (String displayName : secretsStoreConnectorMap.keySet())
            {
                secureConnectorExtension.initializeSecretsStoreConnector(displayName, secretsStoreConnectorMap.get(displayName));
            }
        }

        /*
         * If we reach this point the connector provider has returned a connector that can be passed to the caller.
         */

        log.debug("New connector returned: " + connectorInstance.getConnectorInstanceId());
        log.debug("Using Connection: " + connectorInstance.getConnection().getQualifiedName() + "(" + connectorInstance.getConnection().getDisplayName() + ")");
        log.debug("<== ConnectorBroker." + methodName);

        return connectorInstance;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConnectorBroker{}";
    }
}