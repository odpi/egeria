/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ConnectorProviderBase is a base class for a connector provider.  It manages all the class loading
 * for subclass implementations of the connector provider along with the generation of new connector guids.
 * ConnectorProviderBase creates a connector instance with the class name from the private variable called
 * connectorClassName.  This class name is initialized to null.  If the getConnector method is called when
 * the connectorClassName is null, it throws ConnectorCheckedException.
 * This is its default behaviour.
 * To use the ConnectorProviderBase, create a new class that extends the ConnectorProviderBase class
 * and in the constructor call super.setConnectorClassName("your connector's class name");
 */
public abstract class ConnectorProviderBase extends ConnectorProvider implements AuditLoggingComponent
{
    private String               connectorClassName            = null;
    protected List<String>       connectorInterfaces           = new ArrayList<>();
    private ComponentDescription connectorComponentDescription = null;

    protected AuditLog      auditLog            = null;
    protected ConnectorType connectorTypeBean   = null;

    private final int     hashCode = UUID.randomUUID().hashCode();

    private static final Logger     log = LoggerFactory.getLogger(ConnectorProviderBase.class);

    /**
     * Typical constructor
     */
    public ConnectorProviderBase ()
    {
        connectorInterfaces.add(Connector.class.getName());
    }


    /**
     * Each connector has a guid to make it easier to correlate log messages from the various components that
     * serve it.  It uses a type 4 (pseudo randomly generated) UUID.
     * The UUID is generated using a cryptographically strong pseudo random number generator.
     *
     * @return guid for a new connector instance
     */
    protected String  getNewConnectorGUID()
    {
        UUID     newUUID = UUID.randomUUID();

        return newUUID.toString();
    }


    /**
     * Return the class name for the connector that the connector provider generates.
     *
     * @return connectorClassName will be null initially.
     */
    public  String   getConnectorClassName()
    {
        return connectorClassName;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    public ComponentDescription getConnectorComponentDescription()
    {
        return connectorComponentDescription;
    }


    /**
     * Update the class name for this connector provider.
     *
     * @param newConnectorClassName   this must be a valid Java class name for a class that implements the
     *                              org.odpi.openmetadata.Connector interface.
     */
    protected  void setConnectorClassName(String   newConnectorClassName)
    {
        log.debug("Connector class name set: " + newConnectorClassName);

        connectorClassName = newConnectorClassName;
    }


    /**
     * Update the component name to use in the creation of the connector's audit log.
     *
     * @param connectorComponentDescription   component description.
     */
    protected  void setConnectorComponentDescription(ComponentDescription   connectorComponentDescription)
    {
        log.debug("Connector audit logging component description: " + connectorComponentDescription.toString());

        this.connectorComponentDescription = connectorComponentDescription;
    }


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Returns the properties about the type of connector that this Connector Provider supports.
     *
     * @return properties including the name of the connector type, the connector provider class
     * and any specific connection properties that are recognized by this connector.
     */
    @Override
    public ConnectorTypeProperties getConnectorTypeProperties()
    {
        if (connectorTypeBean == null)
        {
            return null;
        }
        else
        {
            return new ConnectorTypeProperties(this.getConnectorType());
        }
    }


    /**
     * Returns the properties about the type of connector that this ConnectorProvider supports.
     *
     * @return properties including the name of the connector type, the connector provider class
     * and any specific connection properties that are recognized by this connector.
     */
    @Override
    public synchronized ConnectorType getConnectorType()
    {
        final String javaLanguageName = "Java";
        final String ocfFrameworkName = "Open Connector Framework (OCF)";

        if (connectorTypeBean == null)
        {
            connectorTypeBean = new ConnectorType();
        }

        if (connectorTypeBean.getType() == null)
        {
            connectorTypeBean.setType(ConnectorType.getConnectorTypeType());
        }

        if (connectorTypeBean.getQualifiedName() == null)
        {
            connectorTypeBean.setQualifiedName(this.getClass().getName());
        }

        if (connectorTypeBean.getDisplayName() == null)
        {
            if (connectorComponentDescription != null)
            {
                connectorTypeBean.setDisplayName(connectorComponentDescription.getComponentName());
            }
        }

        if (connectorTypeBean.getDescription() == null)
        {
            if (connectorComponentDescription != null)
            {
                connectorTypeBean.setDescription(connectorComponentDescription.getComponentDescription());
            }
        }

        if (connectorTypeBean.getConnectorProviderClassName() == null)
        {
            connectorTypeBean.setConnectorProviderClassName(this.getClass().getName());
        }

        if (connectorTypeBean.getConnectorInterfaceLanguage() == null)
        {
            connectorTypeBean.setConnectorInterfaceLanguage(javaLanguageName);
        }

        if (connectorTypeBean.getConnectorFrameworkName() == null)
        {
            connectorTypeBean.setConnectorFrameworkName(ocfFrameworkName);
        }

        if (connectorTypeBean.getConnectorInterfaces() == null || connectorTypeBean.getConnectorInterfaces().isEmpty())
        {
            if (connectorClassName != null)
            {
                List<String>  connectorInterfaces = new ArrayList<>();

                try
                {
                    Class<?> nextConnectorClass = Class.forName(connectorClassName);

                    while (nextConnectorClass != null)
                    {
                        Class<?>[] interfaces = nextConnectorClass.getInterfaces();

                        for (Class<?> implementedInterface : interfaces)
                        {
                            if (implementedInterface != null)
                            {
                                connectorInterfaces.add(0, implementedInterface.getName());
                            }
                        }

                        nextConnectorClass = nextConnectorClass.getSuperclass();
                    }
                }
                catch (Exception error)
                {
                    // ignore
                }

                if (! connectorInterfaces.isEmpty())
                {
                    connectorTypeBean.setConnectorInterfaces(connectorInterfaces);
                }
            }
        }

        return new ConnectorType(connectorTypeBean);
    }


    /**
     * Setter method to enable a subclass to set up the connector type properties that are
     * added to a connection properties object.  The connector type properties guide the
     * ConnectorBroker and ConnectorProvider on how to create and configure a Connector instance.
     *
     * @param connectorTypeBean default properties for this type of connector
     */
    protected void setConnectorTypeProperties(ConnectorType  connectorTypeBean)
    {
        this.connectorTypeBean = connectorTypeBean;
    }


    /**
     * Creates a new instance of a connector using the name of the connector provider in the supplied connection.
     *
     * @param connection   properties for the connector and connector provider.
     * @return new connector instance.
     * @throws ConnectionCheckedException an error with the connection.
     * @throws ConnectorCheckedException an error initializing the connector.
     */
    @Override
    public Connector getConnector(Connection connection) throws ConnectionCheckedException, ConnectorCheckedException
    {
        return this.getConnector(new ConnectionProperties(connection));
    }


    /**
     * Creates a new instance of a connector based on the information in the supplied connection.
     *
     * @param connection   connection that should have all the properties needed by the Connector Provider
     *                     to create a connector instance.
     * @return Connector   instance of the connector.
     * @throws ConnectionCheckedException if there are missing or invalid properties in the connection
     * @throws ConnectorCheckedException if there are issues instantiating or initializing the connector
     */
    @Override
    public Connector getConnector(ConnectionProperties connection) throws ConnectionCheckedException, ConnectorCheckedException
    {
        final String             methodName = "getConnector";

        Connector                connector;
        String                   guid;

        log.debug("getConnector called");

        if (connection == null)
        {
            /*
             * It is not possible to create a connector without a connection.
             */
            throw new ConnectionCheckedException(OCFErrorCode.NULL_CONNECTION.getMessageDefinition(),
                                                 this.getClass().getName(),
                                                 methodName);
        }


        /*
         * Validate that a subclass (or external class) has set up the class name of the connector.
         */
        if (connectorClassName == null)
        {
            /*
             * This instance of the connector provider is not initialised with the connector's class name so
             * throw an exception because a connector can not be generated.
             */
            throw new ConnectionCheckedException(OCFErrorCode.NULL_CONNECTOR_CLASS.getMessageDefinition(),
                                                 this.getClass().getName(),
                                                 methodName);
        }


        /*
         * Generate a new GUID for the connector.
         */
        guid = getNewConnectorGUID();


        /*
         * Create a new instance of the connector and initialize it with the guid and connection.
         */
        try
        {
            Class<?>   connectorClass = getClassForConnector();
            Object     potentialConnector = connectorClass.getDeclaredConstructor().newInstance();

            connector = (Connector)potentialConnector;
            connector.initialize(guid, connection);

            if (connector instanceof AuditLoggingComponent)
            {
                if (auditLog != null)
                {
                    if (connectorComponentDescription == null)
                    {
                        ((AuditLoggingComponent) connector).setAuditLog(auditLog);
                    }
                    else
                    {
                        ((AuditLoggingComponent) connector).setAuditLog(auditLog.createNewAuditLog(connectorComponentDescription.getComponentId(),
                                                                                                   connectorComponentDescription.getComponentDevelopmentStatus(),
                                                                                                   connectorComponentDescription.getComponentName() + ":" + guid,
                                                                                                   connectorComponentDescription.getComponentDescription(),
                                                                                                   connectorComponentDescription.getComponentWikiURL()));
                    }
                }
            }
        }
        catch (ClassNotFoundException classException)
        {
            /*
             * Wrap exception in the ConnectionCheckedException with a suitable message
             */
            throw new ConnectionCheckedException(OCFErrorCode.UNKNOWN_CONNECTOR.getMessageDefinition(connectorClassName,
                                                                                                     connection.getConnectionName()),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 classException);
        }
        catch (ClassCastException  castException)
        {
            /*
             * Wrap class cast exception in a connection exception with error message to say that
             */
            throw new ConnectionCheckedException(OCFErrorCode.NOT_CONNECTOR.getMessageDefinition(connectorClassName,
                                                                                                 connection.getConnectionName()),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 castException);
        }
        catch (Exception unexpectedSomething)
        {
            /*
             * Wrap throwable in a connection exception with error message to say that there was a problem with
             * the connector implementation.
             */
            throw new ConnectionCheckedException(OCFErrorCode.INVALID_CONNECTOR.getMessageDefinition(connectorClassName,
                                                                                                     connection.getConnectionName(),
                                                                                                     unexpectedSomething.getClass().getName(),
                                                                                                     unexpectedSomething.getMessage()),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 unexpectedSomething);
        }


        /*
         * Return the initialized connector ready for use.
         */

        log.debug("getConnector returns: " + connector.getConnectorInstanceId() + ", " + connection.getConnectionName());

        return connector;
    }


    /**
     * Use the standard class loader to retrieve the class for the connector.
     *
     * @return class
     * @throws ClassNotFoundException unable to locate a class by that name on the class path
     */
    protected Class<?> getClassForConnector() throws ClassNotFoundException
    {
        return Class.forName(connectorClassName);
    }


    /**
     * Provide a common implementation of hashCode for all OCF Connector Provider objects.  The UUID is unique and
     * is randomly assigned and so its hashCode is as good as anything to describe the hash code of the properties
     * object.
     *
     * @return random UUID as hashcode
     */
    @Override
    public int hashCode()
    {
        return hashCode;
    }


    /**
     * Provide a common implementation of equals for all OCF Connector Provider objects.  The UUID is unique and
     * is randomly assigned and so its hashCode is as good as anything to evaluate the equality of the connector
     * provider object.
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

        ConnectorProviderBase that = (ConnectorProviderBase) object;

        return (hashCode == that.hashCode);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConnectorProviderBase{" +
                "connectorClassName='" + connectorClassName + '\'' +
                ", connectorType=" + connectorTypeBean +
                ", hashCode=" + hashCode +
                '}';
    }
}