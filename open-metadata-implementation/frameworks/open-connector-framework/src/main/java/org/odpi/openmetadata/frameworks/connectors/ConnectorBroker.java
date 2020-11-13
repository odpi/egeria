/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.odpi.openmetadata.frameworks.connectors.properties.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;

import java.util.*;

/**
 * The ConnectorBroker is a generic factory for Open Connector Framework (OCF) Connectors.
 * The OCF provides a default implementation because all of the implementation that is specific to a
 * particular type of connector is delegated to the connector provider specified in the connection.
 */
public class ConnectorBroker
{
    private static final Logger log      = LoggerFactory.getLogger(ConnectorBroker.class);
    private final        int    hashCode = UUID.randomUUID().hashCode();


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
     * @param auditLog auditlog to pass on
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
     * @param methodName calling method
     * @throws ConnectionCheckedException null connection detected
     */
    private void  validateConnectionNotNull(ConnectionProperties  connection,
                                            String                methodName) throws ConnectionCheckedException
    {
        if (connection == null)
        {
            /*
             * It is not possible to create a connector without a connection.
             */
            throw new ConnectionCheckedException(OCFErrorCode.NULL_CONNECTION.getMessageDefinition(),
                                                 this.getClass().getName(),
                                                 methodName);
        }

        if (connection instanceof VirtualConnectionProperties)
        {
            VirtualConnectionProperties         virtualConnection = (VirtualConnectionProperties)connection;
            List<EmbeddedConnectionProperties>  embeddedConnections = virtualConnection.getEmbeddedConnections();

            if ((embeddedConnections == null) || (embeddedConnections.isEmpty()))
            {
                throw new ConnectionCheckedException(OCFErrorCode.INVALID_VIRTUAL_CONNECTION.getMessageDefinition(connection.getConnectionName()),
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
    private  ConnectorTypeProperties   getConnectorType(ConnectionProperties  connection,
                                                        String                methodName) throws ConnectionCheckedException
    {
        ConnectorTypeProperties requestedConnectorType = connection.getConnectorType();

        if (requestedConnectorType == null)
        {
            /*
             * It is not possible to create a connector without a connector type since it
             * holds the name of the connector provider's Java class.  Build an exception.
             */
            throw new ConnectionCheckedException(OCFErrorCode.NULL_CONNECTOR_TYPE.getMessageDefinition(connection.getConnectionName()),
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
    private ConnectorProvider  getConnectorProvider(ConnectorTypeProperties   requestedConnectorType,
                                                    String                    connectionName,
                                                    String                    methodName) throws ConnectionCheckedException
    {
        /*
         * The class name for the connector provider is in the connectorType properties
         */
        String    connectorProviderClassName = requestedConnectorType.getConnectorProviderClassName();

        if (connectorProviderClassName == null)
        {
            /*
             * The connector provider class name is blank so it is not possible to create the
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
            Object     potentialConnectorProvider = connectorProviderClass.newInstance();

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
        catch (Throwable unexpectedSomething)
        {
            /*
             * Wrap throwable in a connection exception with error message to say that there was a problem with
             * the connector provider.
             */
            throw new ConnectionCheckedException(OCFErrorCode.INVALID_CONNECTOR_PROVIDER.getMessageDefinition(connectorProviderClassName,
                                                                                                              connectionName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 unexpectedSomething);
        }

        return connectorProvider;
    }


    /**
     * Extract the connection from the embedded connection and push any arguments into the
     * AdditionalProperties for the connection.
     *
     * @param embeddedConnection embedded connection object.
     * @return connection object augmented with the arguments from the embedded connection.
     */
    private ConnectionProperties   getConnection(EmbeddedConnectionProperties   embeddedConnection)
    {
        ConnectionProperties connection = null;

        if (embeddedConnection != null)
        {
            AccessibleConnection accessibleConnection = new AccessibleConnection(embeddedConnection.getConnectionProperties());
            Connection           connectionBean       = accessibleConnection.getConnectionBean();

            int                  position             = embeddedConnection.getPosition();
            String               displayName          = embeddedConnection.getDisplayName();
            Map<String, Object>  arguments            = embeddedConnection.getArguments();

            if (arguments != null)
            {
                Map<String, Object>  configurationProperties = connectionBean.getConfigurationProperties();
                if (configurationProperties == null)
                {
                    configurationProperties = new HashMap<>();
                }

                for (String argumentName : arguments.keySet())
                {
                    configurationProperties.put(argumentName, arguments.get(argumentName).toString());
                }

                if (configurationProperties.isEmpty())
                {
                    configurationProperties = null;
                }

                connectionBean.setConfigurationProperties(configurationProperties);
            }

            connection = new ConnectionProperties(connectionBean);
        }

        return connection;
    }


    /**
     * Validate that the connection has sufficient properties to attempt to create a connector.
     * Any problems found are expressed as a ConnectionCheckedException.
     *
     * @param connection connection properties
     * @throws ConnectionCheckedException an error with the connection.
     */
    public void validateConnection(Connection    connection) throws ConnectionCheckedException
    {
        if (connection == null)
        {
            this.validateConnection((ConnectionProperties)null);
        }
        else
        {
            this.validateConnection(new ConnectionProperties(connection));
        }
    }


    /**
     * Validate that the connection has sufficient properties to attempt to create a connector.
     * Any problems found are expressed as a ConnectionCheckedException.
     *
     * @param connection connection properties
     * @throws ConnectionCheckedException an error with the connection.
     */
    public void validateConnection(ConnectionProperties    connection) throws ConnectionCheckedException
    {
        final String   methodName = "validateConnection";

        log.debug("==> ConnectorBroker." + methodName);

        validateConnectionNotNull(connection, methodName);

        ConnectorTypeProperties requestedConnectorType = this.getConnectorType(connection, methodName);

        getConnectorProvider(requestedConnectorType, connection.getConnectionName(), methodName);

        log.debug("<== ConnectorBroker." + methodName);
    }


    /**
     * Creates a new instance of a connector using the name of the connector provider in the supplied connection.
     *
     * @param connection   properties for the connector and connector provider.
     * @return new connector instance.
     * @throws ConnectionCheckedException an error with the connection.
     * @throws ConnectorCheckedException an error initializing the connector.
     */
    public Connector getConnector(Connection connection) throws ConnectionCheckedException, ConnectorCheckedException
    {
        if (connection == null)
        {
            return this.getConnector((ConnectionProperties)null);
        }
        else if (connection instanceof VirtualConnection)
        {
            return this.getConnector(new VirtualConnectionProperties((VirtualConnection)connection));
        }
        else
        {
            return this.getConnector(new ConnectionProperties(connection));
        }
    }


    /**
     * Creates a new instance of a connector using the name of the connector provider in the supplied connection.
     *
     * @param connection   properties for the connector and connector provider.
     * @return new connector instance.
     * @throws ConnectionCheckedException an error with the connection.
     * @throws ConnectorCheckedException an error initializing the connector.
     */
    public Connector getConnector(ConnectionProperties connection) throws ConnectionCheckedException,
                                                                          ConnectorCheckedException
    {
        final String         methodName = "getConnector";
        String               connectionName;

        log.debug("==> ConnectorBroker." + methodName);

        this.validateConnection(connection);
        connectionName = connection.getConnectionName();


        /*
         * Within the connection is a structure called the connector type.  This defines the factory for a new
         * connector instance.  This factory is called the Connector Provider.
         */
        ConnectorTypeProperties requestedConnectorType = this.getConnectorType(connection, methodName);


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


        /*
         * At this point we hopefully have a valid connector provider so all that is left to do is call
         * it to get the connector instance.  This is done in a different try ... catch block from the
         * instantiation of the connector provider so we can separate errors in the Connection from
         * errors generated in the Connector Provider, since both classes are
         * potentially code from a source outside of Egeria.
         */
        Connector    connectorInstance;

        try
        {
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
        catch (Throwable  unexpectedSomething)
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
            throw new OCFRuntimeException(OCFErrorCode.NULL_CONNECTOR.getMessageDefinition(requestedConnectorType.getConnectorProviderClassName(),
                                                                                           connection.getConnectionName()),
                                          this.getClass().getName(),
                                          methodName);
        }


        /*
         * If a virtual connection was passed to the connector broker then (1) the connector provider
         * should have created a VirtualConnector and (2) the connector broker needs to create
         * connectors for its embedded connections.
         */
        if (connection instanceof VirtualConnectionProperties)
        {
            if (connectorInstance instanceof VirtualConnectorExtension)
            {
                VirtualConnectorExtension virtualConnectorExtension = (VirtualConnectorExtension)connectorInstance;

                /*
                 * All ok so create the embedded connectors.  Note: one of these connectors may itself be
                 * a virtual connector with embedded connectors of its own.  So this method may be called
                 * recursively as the tree of connectors is explored.
                 */
                log.debug("Creating embedded connectors for connection name: " + connectionName + "; connectorId: " + connectorInstance.getConnectorInstanceId());

                VirtualConnectionProperties        virtualConnectionProperties = (VirtualConnectionProperties) connection;
                List<EmbeddedConnectionProperties> embeddedConnections         = virtualConnectionProperties.getEmbeddedConnections();
                List<Connector>                    embeddedConnectors          = new ArrayList<>();

                for (EmbeddedConnectionProperties  embeddedConnection : embeddedConnections)
                {
                    embeddedConnectors.add(getConnector(this.getConnection(embeddedConnection)));
                }

                virtualConnectorExtension.initializeEmbeddedConnectors(embeddedConnectors);
            }
            else
            {
                /*
                 * Build and throw exception.
                 */
                throw new OCFRuntimeException(OCFErrorCode.NOT_VIRTUAL_CONNECTOR.getMessageDefinition(requestedConnectorType.getConnectorProviderClassName(),
                                                                                                      connection.getConnectionName()),
                                              this.getClass().getName(),
                                              methodName);
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
     * Provide an implementation of hashCode for all OCF Connector Broker objects.  The UUID is unique and
     * is randomly assigned and so its hashCode is as good as anything to describe the hash code of the connector
     * broker object.
     */
    public int hashCode()
    {
        return hashCode;
    }


    /**
     * Provide a common implementation of equals for all OCF Connector Broker objects.  The UUID is unique and
     * is randomly assigned and so its hashCode is as good as anything to evaluate the equality of the connector
     * broker object.
     *
     * @param object   object to test
     * @return boolean flag
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }

        ConnectorBroker that = (ConnectorBroker) object;

        return hashCode == that.hashCode;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConnectorBroker{" +
                "hashCode=" + hashCode +
                '}';
    }


    /**
     * ProtectedConnection provides a subclass to Connection in order to extract protected values from the
     * connection in order to supply them to the Connector implementation.
     */
    private static class AccessibleConnection extends ConnectionProperties
    {
        private static final long     serialVersionUID = 1L;

        AccessibleConnection(ConnectionProperties templateConnection)
        {
            super(templateConnection);
        }

        /**
         * Return a copy of the ConnectionBean.
         *
         * @return Connection bean
         */
        protected Connection getConnectionBean()
        {
            return super.getConnectionBean();
        }
    }
}