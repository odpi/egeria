/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The ConnectorBase is an implementation of the Connector interface.
 * <br><br>
 * Connectors are client-side interfaces to assets such as data stores, data sets, APIs, analytical functions.
 * They handle the communication with the server that hosts the assets, along with the communication with the
 * metadata server to serve up metadata about the assets, and support for an audit log for the caller to log its
 * activity.
 * <br><br>
 * Each connector implementation is paired with a connector provider.  The connector provider is the factory for
 * connector instances.
 * <br><br>
 * The Connector interface defines that a connector instance should be able to return a unique
 * identifier, a connection object and a metadata properties object for its connected asset.
 * These are supplied to the connector during its initialization.
 * <br><br>
 * The ConnectorBase base class implements all the methods required by the Connector interface.
 * Each specific implementation of a connector then extends this interface to add the methods to work with the
 * particular type of asset it supports.  For example, a JDBC connector would add the standard JDBC SQL interface, the
 * OMRS Connectors add the metadata repository management APIs...
 */
public abstract class ConnectorBase extends Connector implements SecureConnectorExtension,
                                                                 VirtualConnectorExtension
{
    protected String            connectorInstanceId = null;
    protected Connection        connectionBean      = null;
    protected Map<String, SecretsStoreConnector> secretsStoreConnectorMap = new HashMap<>();
    protected List<Connector>                    embeddedConnectors       = null;

    /*
     * For connectors to log with or without an audit log.
     */
    protected final MessageFormatter messageFormatter    = new MessageFormatter();
    protected AuditLog auditLog = null;

    private volatile boolean           isActive                 = false;

    private static final Logger   log = LoggerFactory.getLogger(ConnectorBase.class);

    private        final int      hashCode = UUID.randomUUID().hashCode();

    /**
     * Typical Constructor: Connectors should always have a constructor requiring no parameters and perform
     * initialization in the initialize method.
     */
    public  ConnectorBase()
    {
        /*
         * Nothing to do real initialization happens in the "initialize()" method.
         * This pattern is used to make it possible for ConnectorBrokerBase to support the dynamic loading and
         * instantiation of arbitrary connector instances without needing to know the specifics of their constructor
         * methods
         */
        log.debug("New Connector Requested");
    }


    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connectionDetails   POJO for the configuration used to create the connector.
     * @throws ConnectorCheckedException An issue occurred during initialization
     */
    @Override
    public void initialize(String     connectorInstanceId,
                           Connection connectionDetails) throws ConnectorCheckedException
    {
        this.connectorInstanceId = connectorInstanceId;
        this.connectionBean      = connectionDetails;

        log.debug("New Connector initialized: {}, {},{}", connectorInstanceId, connectionDetails.getQualifiedName(), connectionDetails.getDisplayName());
    }


    /**
     * Combine the connector's configuration properties with the supplied additional configuration
     * properties into a new map.
     *
     * @param additionalConfigurationProperties additional properties (can be null)
     * @return combination of the connectors properties and the additional properties.  Null is returned if both are null/empty.
     */
    protected Map<String,Object> combineConfigurationProperties(Map<String, Object> additionalConfigurationProperties)
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        if (connectionBean.getConfigurationProperties() != null)
        {
            configurationProperties.putAll(connectionBean.getConfigurationProperties());
        }

        if (additionalConfigurationProperties != null)
        {
            configurationProperties.putAll(additionalConfigurationProperties);
        }

        if (configurationProperties.isEmpty())
        {
            return null;
        }

        return configurationProperties;
    }


    /**
     * Log an audit log record for an event, decision, error, or exception detected by the OMRS.
     *
     * @param messageDefinition description of the audit log record including specific resources involved
     * @param actionDescription calling method
     */
    protected void logRecord(String                    actionDescription,
                             AuditLogMessageDefinition messageDefinition)
    {
        if (auditLog != null)
        {
            auditLog.logMessage(actionDescription, messageDefinition);
        }
        else
        {
            System.out.println(messageDefinition.getSeverity().getName() + " " + messageFormatter.getFormattedMessage(messageDefinition));
        }
    }


    /**
     * Log an audit log record for an event, decision, error, or exception detected by the OMRS.
     *
     * @param messageDefinition description of the audit log record including specific resources involved
     * @param actionDescription calling method
     */
    protected void logRecord(String                    actionDescription,
                             AuditLogMessageDefinition messageDefinition,
                             String                    additionalInformation)
    {
        if (auditLog != null)
        {
            auditLog.logMessage(actionDescription, messageDefinition, additionalInformation);
        }
        else
        {
            System.out.println(messageDefinition.getSeverity().getName() + " " + messageFormatter.getFormattedMessage(messageDefinition));
            System.out.println(additionalInformation);
        }
    }


    /**
     * Log an audit log record for an event, decision, error, or exception detected by the OMRS.
     *
     * @param messageDefinition description of the audit log record including specific resources involved
     * @param actionDescription calling method
     */
    protected void logExceptionRecord(String                    actionDescription,
                                      AuditLogMessageDefinition messageDefinition,
                                      Exception                 exception)
    {
        if (auditLog != null)
        {
            auditLog.logException(actionDescription, messageDefinition, exception);
        }
        else
        {
            System.out.println(messageDefinition.getSeverity().getName() + " " + messageFormatter.getFormattedMessage(messageDefinition));

            StringWriter stackTrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stackTrace));

            System.out.println(stackTrace);
        }
    }


    /**
     * Set up the list of connectors that this virtual connector will use to support its interface.
     * The connectors are initialized waiting to start.  When start() is called on the
     * virtual connector, it needs to pass start() to each of the embedded connectors.
     * Similar processing is needed for the disconnect() method.
     *
     * @param embeddedConnectors list of connectors
     */
    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        this.embeddedConnectors = embeddedConnectors;
    }


    /**
     * Returns the unique connector instance id that assigned to the connector instance when it was created.
     * It is useful for error and audit messages.
     *
     * @return guid for the connector instance
     */
    @Override
    public String getConnectorInstanceId()
    {
        return connectorInstanceId;
    }


    /**
     * Returns the connection object that was used to create the connector instance.  Its contents are never refreshed
     * during the lifetime of the connector instance, even if the connection information is updated or removed
     * from the originating metadata repository.
     *
     * @return connection properties object
     */
    @Override
    public Connection getConnection()
    {
        return connectionBean;
    }


    /**
     * Set up information about an embedded connector that this connector can use to support secure access to its resources.
     *
     * @param displayName name of the secrets store
     * @param secretsStoreConnector an embedded secrets store connector
     */
    @Override
    public void initializeSecretsStoreConnector(String                displayName,
                                                SecretsStoreConnector secretsStoreConnector)
    {
        if (displayName != null)
        {
            secretsStoreConnectorMap.put(displayName, secretsStoreConnector);
        }
        else
        {
            secretsStoreConnectorMap.put(String.valueOf(secretsStoreConnectorMap.size()), secretsStoreConnector);
        }
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        setIsActive(true);
    }


    /**
     * Return the name of the configured secrets collection name.
     *
     * @param secretStorePurpose which secrets store to use
     * @return string name
     */
    public String getSecretsCollectionName(String secretStorePurpose)
    {
        if (secretsStoreConnectorMap != null)
        {
            SecretsStoreConnector secretsStoreConnector = secretsStoreConnectorMap.get(secretStorePurpose);
            if (secretsStoreConnector != null)
            {
                return secretsStoreConnector.getSecretsCollectionName();
            }
        }

        return null;
    }


    /**
     * Return the location of the secrets store. (Endpoint network address of requested secrets store.)
     *
     * @param secretStorePurpose which secrets store to use
     * @return string
     */
    public String getSecretsLocation(String secretStorePurpose)
    {
        if (secretsStoreConnectorMap != null)
        {
            SecretsStoreConnector secretsStoreConnector = secretsStoreConnectorMap.get(secretStorePurpose);
            if (secretsStoreConnector != null)
            {
                return secretsStoreConnector.getSecretsLocation();
            }
        }

        return null;
    }


    /**
     * Return the class name of this connector's provider.
     *
     * @param secretStorePurpose which secrets store to use
     * @return string
     */
    public String getSecretsProvider(String secretStorePurpose)
    {
        if (secretsStoreConnectorMap != null)
        {
            SecretsStoreConnector secretsStoreConnector = secretsStoreConnectorMap.get(secretStorePurpose);
            if (secretsStoreConnector != null)
            {
                return secretsStoreConnector.getSecretsProvider();
            }
        }

        return null;
    }


    /**
     * Pass the configuration properties as placeholder properties.  This allows the caller to supply additional
     * properties to a template beyond those envisaged in the connector implementation.
     * The templating process is not affected if properties not use in the template are supplied
     *
     * @param configurationProperties supplied configuration properties
     * @return placeholder properties map
     */
    protected Map<String, String> getSuppliedPlaceholderProperties(Map<String, Object> configurationProperties)
    {
        if (configurationProperties != null)
        {
            Map<String, String> placeholderProperties = new HashMap<>();

            for (String configurationPropertyName : configurationProperties.keySet())
            {
                if (configurationProperties.get(configurationPropertyName) == null)
                {
                    placeholderProperties.put(configurationPropertyName, null);
                }
                else
                {
                    placeholderProperties.put(configurationPropertyName, configurationProperties.get(configurationPropertyName).toString());
                }
            }

            if (! placeholderProperties.isEmpty())
            {
                return placeholderProperties;
            }
        }

        return null;
    }


    /**
     * Retrieve a configuration property that is a comma-separated list of strings.
     *
     * @param propertyName name of property
     * @param configurationProperties configuration properties
     * @param defaultValue value to use if the property is not specified.
     * @return list of strings or null if not set
     */
    protected List<String> getArrayConfigurationProperty(String              propertyName,
                                                         Map<String, Object> configurationProperties,
                                                         List<String>        defaultValue)
    {
        if (configurationProperties != null)
        {
            if (configurationProperties.containsKey(propertyName))
            {
                Object arrayOption = configurationProperties.get(propertyName);

                String[] options = arrayOption.toString().split(",");

                return new ArrayList<>(Arrays.asList(options));
            }
        }

        return defaultValue;
    }


    /**
     * Retrieve a configuration property that is a comma-separated list of strings.
     *
     * @param propertyName name of property
     * @param configurationProperties configuration properties
     * @param defaultValue value to use if the property is not specified.
     * @return list of strings or null if not set
     */
    protected List<String> getArrayConfigurationProperty(String              propertyName,
                                                         Map<String, Object> configurationProperties,
                                                         String              defaultValue)
    {
        if (configurationProperties != null)
        {
            if (configurationProperties.containsKey(propertyName))
            {
                Object arrayOption = configurationProperties.get(propertyName);

                String[] options = arrayOption.toString().split(",");

                return new ArrayList<>(Arrays.asList(options));
            }
        }

        if (defaultValue != null)
        {
            String[] options = defaultValue.split(",");

            return new ArrayList<>(Arrays.asList(options));
        }

        return null;
    }


    /**
     * Retrieve a configuration property that is a comma-separated list of strings.
     *
     * @param propertyName name of property
     * @param configurationProperties configuration properties
     * @return list of strings or null if not set
     */
    protected List<String> getArrayConfigurationProperty(String              propertyName,
                                                         Map<String, Object> configurationProperties)
    {
        if (configurationProperties != null)
        {
            if (configurationProperties.containsKey(propertyName))
            {
                Object arrayOption = configurationProperties.get(propertyName);

                String[] options = arrayOption.toString().split(",");

                return new ArrayList<>(Arrays.asList(options));
            }
        }

        return null;
    }



    /**
     * Retrieve a configuration property that is a boolean.  If any non-null value is set it returns true unless
     * the value is set to FALSE, False or false.
     *
     * @param propertyName name of property
     * @param configurationProperties configuration properties
     * @return boolean flag or false if not set
     */
    protected boolean getBooleanConfigurationProperty(String              propertyName,
                                                      Map<String, Object> configurationProperties)
    {
        if (configurationProperties != null)
        {
            if (configurationProperties.containsKey(propertyName))
            {
                Object booleanOption = configurationProperties.get(propertyName);

                return ((! "FALSE".equals(booleanOption)) && (! "false".equals(booleanOption)) && (! "False".equals(booleanOption)));
            }
        }

        return false;
    }


    /**
     * Retrieve a configuration property that is an integer.
     *
     * @param propertyName name of property
     * @param configurationProperties configuration properties
     * @return integer value or zero if not supplied
     */
    protected int getIntConfigurationProperty(String              propertyName,
                                              Map<String, Object> configurationProperties)
    {
        if (configurationProperties != null)
        {
            if (configurationProperties.get(propertyName) != null)
            {
                Object integerOption = configurationProperties.get(propertyName);

                if (integerOption != null)
                {
                    return Integer.parseInt(integerOption.toString());
                }
            }
        }

        return 0;
    }


    /**
     * Retrieve a configuration property that is an integer.
     *
     * @param propertyName name of property
     * @param configurationProperties configuration properties
     * @return integer value or zero if not supplied
     */
    protected long getLongConfigurationProperty(String              propertyName,
                                                Map<String, Object> configurationProperties)
    {
        if (configurationProperties != null)
        {
            if (configurationProperties.get(propertyName) != null)
            {
                Object integerOption = configurationProperties.get(propertyName);

                if (integerOption != null)
                {
                    return Long.parseLong(integerOption.toString());
                }
            }
        }

        return 0L;
    }


    /**
     * Retrieve a configuration property that is a string or null if not set.
     *
     * @param propertyName name of property
     * @param configurationProperties configuration properties
     * @return string value of property or null if not supplied
     */
    protected String getStringConfigurationProperty(String              propertyName,
                                                    Map<String, Object> configurationProperties)
    {
        return this.getStringConfigurationProperty(propertyName, configurationProperties, null);
    }



    /**
     * Retrieve a configuration property that is a string or null if not set.
     *
     * @param propertyName name of property
     * @param configurationProperties configuration properties
     * @param defaultValue value to use if the property is not in the configuration properties
     * @return string value of property or null if not supplied
     */
    protected String getStringConfigurationProperty(String              propertyName,
                                                    Map<String, Object> configurationProperties,
                                                    String              defaultValue)
    {
        if (configurationProperties != null)
        {
            if (configurationProperties.get(propertyName) != null)
            {
                return configurationProperties.get(propertyName).toString();
            }
        }

        return defaultValue;
    }


    /**
     * Retrieve a configuration property that is a string formatted date or null if not set.
     *
     * @param propertyName name of property
     * @param configurationProperties configuration properties
     * @return string value of property or null if not supplied
     */
    protected Date getDateConfigurationProperty(String              propertyName,
                                                Map<String, Object> configurationProperties) throws InvalidParameterException
    {
        final String methodName = "getDateConfigurationProperty";

        if (configurationProperties != null)
        {
            if (configurationProperties.get(propertyName) != null)
            {
                String dateInString = configurationProperties.get(propertyName).toString();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                try
                {
                    return formatter.parse(dateInString);
                }
                catch (ParseException error)
                {
                    throw new InvalidParameterException(OCFErrorCode.MALFORMED_DATE_CONFIGURATION_PROPERTY.getMessageDefinition(propertyName, dateInString),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        error,
                                                        propertyName);
                }
            }
        }

        return null;
    }

    /* ==========================================================================================================
     * Connectors may need to convert names between different naming conventions.  Standards such as the
     * Tabular Data Source exchanges names using a canonical format to allow connectors that support different
     * technologies with their own naming conventions to exchange schema information.
     * The methods below provide mechanisms to convert between standard naming conventions and the canonical format.
     *
     * The canonical format has a space between each word and the first character in each word is capitalized -
     * eg "My Table Name".
     *
     * Snake case is all lower case with underscores between the words - eg "my_table_name".
     *
     * Camel case has the spaces removed from the canonical format.
     * The first character of the first word is lower case; the first character of
     * subsequent works is upper case - eg myTableName.
     *
     * Kebab case is like snake case but uses dashes rather than underscores = eg "my-table-name".
     *
     * Pascal case is like camel case except that the first character of the first word is also upper case - eg "MyTableName".
     */

    /**
     * Convert a canonical name to a name in snake case.  Snake case is all in lower case with dashes between
     * the words.
     *
     * @param name string to convert
     * @return converted string
     */
    public String fromCanonicalToSnakeCase(String name)
    {
        if (name == null || name.isEmpty())
        {
            return name;
        }

        return name.replace(' ', '_').toLowerCase();
    }


    /**
     * Return the supplied name in a canonical format used for this data source.  Each word in the name should be capitalized, with spaces
     * between the words to allow translation between different naming conventions.
     *
     * @param name string to convert
     * @return string
     */
    public String fromSnakeToCanonicalCase(String name)
    {
        if (name == null || name.isEmpty())
        {
            return name;
        }

        StringBuilder converted = new StringBuilder();
        boolean convertNext = true;

        for (Character ch : name.toCharArray())
        {
            if (ch.equals('_'))
            {
                ch = ' ';
                convertNext = true; // next letter should be in upper case
            }
            else if (convertNext)
            {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            }
            else
            {
                ch = Character.toLowerCase(ch);
            }

            converted.append(ch);
        }

        return converted.toString();
    }


    /**
     * Return the camel case form of a canonical name.
     *
     * @param name string to convert
     * @return string
     */
    public String fromCanonicalToCamelCase(String name)
    {
        if (name == null || name.isEmpty())
        {
            return name;
        }

        String newName = name.replaceAll("\\s", "");

        return Character.toLowerCase(newName.charAt(0)) + newName.substring(1);
    }


    /**
     * Return the canonical form of a name in camel case.
     *
     * @param name string to convert
     * @return string
     */
    public String fromCamelToCanonicalCase(String name)
    {
        if (name == null || name.isEmpty())
        {
            return name;
        }

        StringBuilder stringBuilder = new StringBuilder();
        char[] c = name.toCharArray();

        for (char value : c)
        {
            /*
             * Skip space characters
             */
            if (! Character.isSpaceChar(value))
            {
                if (stringBuilder.isEmpty())
                {
                    /*
                     * Make sure first character is lower case
                     */
                    stringBuilder.append(Character.toLowerCase(value));
                }
                else
                {
                    stringBuilder.append(value);
                }
            }
        }

        return stringBuilder.toString();
    }



    /**
     * Convert a canonical name to a name in kebab case.  Kebab case is all in lower case with dashes between
     * the words.
     *
     * @param name string to convert
     * @return converted string
     */
    public String fromCanonicalToKebabCase(String name)
    {
        if (name == null || name.isEmpty())
        {
            return name;
        }

        return name.replace(' ', '-').toLowerCase();
    }


    /**
     * Return the supplied name in a canonical format used for this data source.
     * Each word in the name should be capitalized, with spaces
     * between the words to allow translation between different naming conventions.
     *
     * @param name string to convert
     * @return string
     */
    public String fromKebabToCanonicalCase(String name)
    {
        if (name == null || name.isEmpty())
        {
            return name;
        }

        StringBuilder converted = new StringBuilder();
        boolean convertNext = true;

        for (Character ch : name.toCharArray())
        {
            if (ch.equals('-'))
            {
                ch = ' ';
                convertNext = true; // next letter should be in upper case
            }
            else if (convertNext)
            {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            }
            else
            {
                ch = Character.toLowerCase(ch);
            }

            converted.append(ch);
        }

        return converted.toString();
    }

    /**
     * Log that no asset has been returned to the connector.  It is unable to proceed without this basic information.
     *
     * @param assetGUID the unique identifier of the asset from the connection context
     * @param connectorName name of the connector
     * @param methodName calling method
     *
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwNoAsset(String    assetGUID,
                                String    connectorName,
                                String    methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(OCFErrorCode.NO_ASSET.getMessageDefinition(assetGUID,
                                                                                       connectorName),
                                            this.getClass().getName(),
                                            methodName);
    }

    /**
     * Log that the connector can not process the type of asset it has been passed.
     *
     * @param assetGUID identifier of the asset
     * @param assetType type of the asset
     * @param supportedAssetType supported asset types
     * @param connectorName name of the connector
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwWrongTypeOfAsset(String    assetGUID,
                                         String    assetType,
                                         String    supportedAssetType,
                                         String    connectorName,
                                         String    methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(OCFErrorCode.INVALID_ASSET_TYPE.getMessageDefinition(assetGUID,
                                                                                                 assetType,
                                                                                                 connectorName,
                                                                                                 supportedAssetType),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Log that the connector can not process the type of asset it has been passed.
     *
     * @param assetGUID identifier of the asset
     * @param assetTypeName type of the asset
     * @param assetResourceName name of the resource
     * @param assetResourceType type of the resource
     * @param supportedResourceType supported resource types
     * @param connectorName name of the connector
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwWrongTypeOfResource(String assetGUID,
                                            String assetTypeName,
                                            String assetResourceName,
                                            String assetResourceType,
                                            String supportedResourceType,
                                            String connectorName,
                                            String methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(OCFErrorCode.INVALID_RESOURCE.getMessageDefinition(assetTypeName,
                                                                                               assetGUID,
                                                                                               assetResourceName,
                                                                                               assetResourceType,
                                                                                               connectorName,
                                                                                               supportedResourceType),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Log that the connector can not process the type of asset it has been passed.
     *
     * @param assetGUID identifier of the asset
     * @param assetTypeName type of the asset
     * @param assetResourceName name of the resource
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwMissingResource(String assetGUID,
                                        String assetTypeName,
                                        String assetResourceName,
                                        String methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(OCFErrorCode.NO_RESOURCE.getMessageDefinition(assetTypeName,
                                                                                          assetGUID,
                                                                                          assetResourceName),
                                            this.getClass().getName(),
                                            methodName);
    }



    /**
     * Log that the connector can not process the named resource because a key configuration property is missing.
     *
     * @param connectorName name of the connector
     * @param resourceName source of the configuration properties
     * @param propertyName name of the missing property
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwMissingConfigurationProperty(String connectorName,
                                                     String resourceName,
                                                     String propertyName,
                                                     String methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(OCFErrorCode.MISSING_CONFIGURATION_PROPERTY.getMessageDefinition(connectorName,
                                                                                                             resourceName,
                                                                                                             propertyName),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Log that the connector does not have an endpoint address.
     *
     * @param connectorName name of the connector
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwMissingEndpointAddress(String connectorName,
                                               String methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(OCFErrorCode.MISSING_ENDPOINT_ADDRESS.getMessageDefinition(connectorName),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Log that the survey action service can not process the type of root schema it has been passed.
     *
     * @param assetGUID identifier of the asset
     * @param rootSchemaType type of the root schema
     * @param supportedRootSchemaType supported root schema types
     * @param connectorName name of the connector
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwWrongTypeOfRootSchema(String    assetGUID,
                                              String    rootSchemaType,
                                              String    supportedRootSchemaType,
                                              String    connectorName,
                                              String    methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(OCFErrorCode.INVALID_ROOT_SCHEMA_TYPE.getMessageDefinition(assetGUID,
                                                                                                       rootSchemaType,
                                                                                                       connectorName,
                                                                                                       supportedRootSchemaType),
                                            this.getClass().getName(),
                                            methodName);
    }



    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        setIsActive(false);
        disconnectConnectors(embeddedConnectors);
    }


    /**
     * Disconnect all the connectors in the supplied list.  Any failures are ignored.
     * This method is typically used by virtual connectors to disconnect their embedded connectors.
     *
     * @param activeConnectors connectors to disconnect.
     */
    protected void disconnectConnectors(List<Connector> activeConnectors)
    {
        if (activeConnectors != null)
        {
            for (Connector embeddedConnector : activeConnectors)
            {
                if (embeddedConnector != null)
                {
                    try
                    {
                        embeddedConnector.disconnect();
                    }
                    catch (Exception error)
                    {
                        // keep going
                    }
                }
            }
        }
    }


    /**
     * Return a flag indicating whether the connector is active.  This means it has been started and not yet
     * disconnected.
     *
     * @return isActive flag
     */
    public synchronized boolean isActive()
    {
        return isActive;
    }


    /**
     * Set up the is active flag.
     *
     * @param flag value
     */
    private synchronized void setIsActive(boolean flag)
    {
        this.isActive = flag;
    }


    /**
     * Provide a common implementation of hashCode for all OCF Connector objects.  The UUID is unique and
     * is randomly assigned and so its hashCode is as good as anything to describe the hash code of the connector
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

        ConnectorBase that = (ConnectorBase) object;

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
        return "ConnectorBase{" +
                "connectorInstanceId='" + connectorInstanceId + '\'' +
                ", connectionDetails=" + connectionBean +
                ", isActive=" + isActive() +
                ", hashCode=" + hashCode +
                '}';
    }

    /**
     * ProtectedConnection provides a subclass to Connection in order to extract protected values from the
     * connection in order to supply them to the Connector implementation.
     */
    protected static class ProtectedConnection extends Connection
    {
        /**
         * Copy/clone connector.
         *
         * @param templateConnection connection to copy
         */
        ProtectedConnection(Connection templateConnection)
        {
            super(templateConnection);
        }
    }
}