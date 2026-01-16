/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SoftwareCapabilityClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CapabilityAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.CapabilityAssetUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.metadatarepositories.MetadataCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.DataAccessManagerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.InventoryCatalogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Provides the base class for connector code that processes a single catalog target.  When converting an
 * integration connector implementation that does not support catalog targets to one that does, move
 * the core working code into a subclass of this class and implement CatalogTargetIntegrator (and optionally
 * CatalogTargetChangeListener if you need notifications.  Ensure you override the getNewRequestedCatalogTargetSkeleton
 * method in CatalogTargetIntegrator to return a new instance of your CatalogTargetProcessorBase class.
 */
public abstract class CatalogTargetProcessorBase extends RequestedCatalogTarget
{
    protected final String         connectorName;
    protected final AuditLog       auditLog;
    protected final PropertyHelper propertyHelper = new PropertyHelper();
    /*
     * These values are extracted from the server definition's server capabilities.
     * The metadata collection information comes from the metadata collection that is linked off of the
     * InventoryCatalog software capability.
     */
    protected String metadataCollectionGUID = null;
    protected String metadataCollectionName = null;


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public CatalogTargetProcessorBase(CatalogTarget        template,
                                      CatalogTargetContext catalogTargetContext,
                                      Connector            connectorToTarget,
                                      String               connectorName,
                                      AuditLog             auditLog)
    {
        super(template, catalogTargetContext, connectorToTarget);

        this.connectorName = connectorName;
        this.auditLog = auditLog;
    }


    /**
     * Indicates that the catalog target processor is completely configured and can begin processing.
     * This call can be used to register with non-blocking services, or extract useful metadata
     * values from the catalog target element, connector etc.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        integrationContext.validateIsActive(methodName);

        super.start();
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected so stop refresh processing
     */
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        integrationContext.validateIsActive(methodName);
    }


    /**
     * Free up any resources held since the catalog target processor is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    public void disconnect() throws ConnectorCheckedException
    {

    }



    /**
     * Ensure that the metadata collection identifiers are properly set up.
     *
     * @throws ConnectorCheckedException a problem retrieving the server definitions.
     */
    protected void setUpMetadataSource() throws ConnectorCheckedException
    {
        final String methodName = "setUpMetadataSource";

        ReferenceableProperties serverProperties;

        if (super.getCatalogTargetElement().getProperties() instanceof ReferenceableProperties referenceableProperties)
        {
            serverProperties = referenceableProperties;
        }
        else
        {
            serverProperties = new ReferenceableProperties();
        }

        String inventoryCatalogGUID = null;

        /*
         * Extract details of the software capabilities currently attached to the server.
         */
        if (super.getCatalogTargetElement().getCapabilities() != null)
        {
            for (RelatedMetadataElementSummary softwareCapability : super.getCatalogTargetElement().getCapabilities())
            {
                if (softwareCapability != null)
                {
                    if (propertyHelper.isTypeOf(softwareCapability.getRelatedElement().getElementHeader(), OpenMetadataType.INVENTORY_CATALOG.typeName))
                    {
                        inventoryCatalogGUID = softwareCapability.getRelatedElement().getElementHeader().getGUID();

                        SoftwareCapabilityClient softwareCapabilityClient = integrationContext.getSoftwareCapabilityClient(OpenMetadataType.INVENTORY_CATALOG.typeName);

                        try
                        {
                            OpenMetadataRootElement inventoryCatalog = softwareCapabilityClient.getSoftwareCapabilityByGUID(softwareCapability.getRelatedElement().getElementHeader().getGUID(), null);

                            if (inventoryCatalog.getCapabilityConsumedAssets() != null)
                            {
                                for (RelatedMetadataElementSummary asset : inventoryCatalog.getCapabilityConsumedAssets())
                                {
                                    if ((asset != null) && (propertyHelper.isTypeOf(asset.getRelatedElement().getElementHeader(), OpenMetadataType.METADATA_COLLECTION.typeName)))
                                    {
                                        if (asset.getRelatedElement().getProperties() instanceof MetadataCollectionProperties metadataCollectionProperties)
                                        {
                                            metadataCollectionGUID = metadataCollectionProperties.getManagedMetadataCollectionId();
                                            metadataCollectionName = metadataCollectionProperties.getQualifiedName();
                                        }
                                    }
                                }
                            }
                        }
                        catch (Exception error)
                        {
                            if (auditLog != null)
                            {
                                auditLog.logException(methodName,
                                                      OIFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                            error.getClass().getName(),
                                                                                                            methodName,
                                                                                                            error.getMessage()),
                                                      error);
                            }

                            throw new ConnectorCheckedException(OIFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                      error.getClass().getName(),
                                                                                                                      methodName,
                                                                                                                      error.getMessage()),
                                                                this.getClass().getName(),
                                                                methodName,
                                                                error);
                        }
                    }
                }
            }
        }

        /*
         * If no metadata collection set up then create one
         */
        if (metadataCollectionGUID == null)
        {
            /*
             * No explicit metadata collection has been set up for this server - which is typical.
             * Has in InventoryCatalog software capability been set up?
             */
            if (inventoryCatalogGUID == null)
            {
                SoftwareCapabilityClient softwareCapabilityClient = integrationContext.getSoftwareCapabilityClient(OpenMetadataType.INVENTORY_CATALOG.typeName);

                InventoryCatalogProperties inventoryCatalogProperties = new InventoryCatalogProperties();

                inventoryCatalogProperties.setQualifiedName(serverProperties.getQualifiedName() + "_inventoryCatalog");
                inventoryCatalogProperties.setDisplayName("Inventory Catalog for " + serverProperties.getDisplayName());
                inventoryCatalogProperties.setDescription("Catalog of assets that are synchronized by the " + connectorName + " integration connector");

                NewElementOptions newElementOptions = new NewElementOptions(softwareCapabilityClient.getMetadataSourceOptions());

                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setAnchorGUID(super.getCatalogTargetElement().getElementHeader().getGUID());
                newElementOptions.setParentGUID(super.getCatalogTargetElement().getElementHeader().getGUID());
                newElementOptions.setParentAtEnd1(true);
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName);

                try
                {
                    inventoryCatalogGUID = softwareCapabilityClient.createSoftwareCapability(newElementOptions,
                                                                                             null,
                                                                                             inventoryCatalogProperties,
                                                                                             null);
                }
                catch (Exception error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                              OIFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                    error.getClass().getName(),
                                                                                                    methodName,
                                                                                                    error.getMessage()),
                                              error);
                    }

                    throw new ConnectorCheckedException(OIFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                              error.getClass().getName(),
                                                                                                              methodName,
                                                                                                              error.getMessage()),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        error);
                }
            }

            metadataCollectionGUID = UUID.randomUUID().toString();
            metadataCollectionName = serverProperties.getQualifiedName() + "_metadataCollection";

            AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.METADATA_COLLECTION.typeName);

            MetadataCollectionProperties metadataCollectionProperties = new MetadataCollectionProperties();

            metadataCollectionProperties.setQualifiedName(metadataCollectionName);
            metadataCollectionProperties.setDisplayName("Metadata Collection for " + serverProperties.getDisplayName());
            metadataCollectionProperties.setDescription("Collection of metadata describing assets that are synchronized by the " + connectorName + " integration connector");
            metadataCollectionProperties.setManagedMetadataCollectionId(metadataCollectionGUID);


            NewElementOptions newElementOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(super.getCatalogTargetElement().getElementHeader().getGUID());
            newElementOptions.setParentGUID(inventoryCatalogGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName);

            CapabilityAssetUseProperties capabilityAssetUseProperties = new CapabilityAssetUseProperties();

            capabilityAssetUseProperties.setUseType(CapabilityAssetUseType.OWNS);

            try
            {
                assetClient.createAsset(newElementOptions,
                                        null,
                                        metadataCollectionProperties,
                                        capabilityAssetUseProperties);
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          OIFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                error.getClass().getName(),
                                                                                                methodName,
                                                                                                error.getMessage()),
                                          error);
                }

                throw new ConnectorCheckedException(OIFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
            }
        }
    }


    /**
     * Ensure that the named software capability type is properly set up.
     *
     * @throws ConnectorCheckedException a problem retrieving the server definitions.
     */
    protected String setUpSoftwareCapability(String softwareCapabilityTypeName) throws ConnectorCheckedException
    {
        final String methodName = "setUpSoftwareCapability";

        ReferenceableProperties serverProperties;

        if (super.getCatalogTargetElement().getProperties() instanceof ReferenceableProperties referenceableProperties)
        {
            serverProperties = referenceableProperties;
        }
        else
        {
            serverProperties = new ReferenceableProperties();
        }

        String softwareCapabilityGUID = null;

        /*
         * Extract details of the software capabilities currently attached to the server.
         */
        if (super.getCatalogTargetElement().getCapabilities() != null)
        {
            for (RelatedMetadataElementSummary softwareCapability : super.getCatalogTargetElement().getCapabilities())
            {
                if (softwareCapability != null)
                {
                    if (propertyHelper.isTypeOf(softwareCapability.getRelatedElement().getElementHeader(), softwareCapabilityTypeName))
                    {
                        softwareCapabilityGUID = softwareCapability.getRelatedElement().getElementHeader().getGUID();
                    }
                }
            }
        }


        /*
         * If no DataAccessManager then create one
         */
        if (softwareCapabilityGUID == null)
        {
            SoftwareCapabilityClient softwareCapabilityClient = integrationContext.getSoftwareCapabilityClient(OpenMetadataType.DATA_ACCESS_MANAGER.typeName);

            DataAccessManagerProperties dataAccessManagerProperties = new DataAccessManagerProperties();

            dataAccessManagerProperties.setQualifiedName(serverProperties.getQualifiedName() + "_" + softwareCapabilityTypeName);
            dataAccessManagerProperties.setDisplayName("Asset Manager for " + serverProperties.getDisplayName());
            dataAccessManagerProperties.setDescription("Manager of assets that are synchronized by the " + connectorName + " integration connector");

            NewElementOptions newElementOptions = new NewElementOptions(softwareCapabilityClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(super.getCatalogTargetElement().getElementHeader().getGUID());
            newElementOptions.setParentGUID(super.getCatalogTargetElement().getElementHeader().getGUID());
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName);

            try
            {
                softwareCapabilityGUID = softwareCapabilityClient.createSoftwareCapability(newElementOptions,
                                                                                           null,
                                                                                           dataAccessManagerProperties,
                                                                                           null);
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          OIFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                error.getClass().getName(),
                                                                                                methodName,
                                                                                                error.getMessage()),
                                          error);
                }

                throw new ConnectorCheckedException(OIFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
            }
        }

        return softwareCapabilityGUID;
    }


    /**
     * Log that the connector can not process the type of catalog target it has been passed.
     *
     * @param expectedTypeName desired type of the catalog target
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwWrongTypeOfCatalogTarget(String    expectedTypeName,
                                                 String    methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(OIFErrorCode.INVALID_CATALOG_TARGET_TYPE.getMessageDefinition(super.getCatalogTargetElement().getElementHeader().getGUID(),
                                                                                                          super.getCatalogTargetElement().getElementHeader().getType().getTypeName(),
                                                                                                          connectorName,
                                                                                                          expectedTypeName),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Log that the connector can not process the type of catalog target it has been passed.
     *
     * @param expectedClassName desired class of the catalog target's resource connector
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwWrongTypeOfResourceConnector(String    expectedClassName,
                                                     String    methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(OIFErrorCode.INVALID_CONNECTOR_TYPE.getMessageDefinition(super.getCatalogTargetElement().getElementHeader().getGUID(),
                                                                                                     super.connectorToTarget.getClass().getName(),
                                                                                                     connectorName,
                                                                                                     expectedClassName),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Log that the connector can not process the catalog target because of an incomplete connection.
     *
     * @param missingParameterName desired type of the catalog target
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwMissingConnectionInfo(String    missingParameterName,
                                              String    methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(OIFErrorCode.INVALID_CATALOG_TARGET_CONNECTION.getMessageDefinition(super.getCatalogTargetElement().getElementHeader().getGUID(),
                                                                                                                missingParameterName,
                                                                                                                connectorName),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Throw an exception because a necessary property is missing.  The description portrays this a s logic error.
     *
     * @param missingPropertyName missing property name
     * @param methodName calling method
     * @param element element in error
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwMissingPropertyValue(String                  missingPropertyName,
                                             String                  methodName,
                                             OpenMetadataRootElement element) throws ConnectorCheckedException
    {
        String elementString = "<null>";

        if (element != null)
        {
            elementString = element.toString();
        }

        if (auditLog != null)
        {
            auditLog.logMessage(methodName, OIFAuditCode.BAD_OM_VALUE.getMessageDefinition(connectorName,
                                                                                           missingPropertyName,
                                                                                           methodName,
                                                                                           elementString));
        }

        throw new ConnectorCheckedException(OIFErrorCode.BAD_OM_VALUE.getMessageDefinition(connectorName,
                                                                                           missingPropertyName,
                                                                                           methodName,
                                                                                           elementString),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Indicate that an element does not have properties of the expected bean class.  This is documented as a logic error.
     *
     * @param expectedType open metadata type information
     * @param methodName calling method
     * @param element element in error
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwBadBeanClass(OpenMetadataType        expectedType,
                                     String                  methodName,
                                     OpenMetadataRootElement element) throws ConnectorCheckedException
    {
        String elementString = "<null>";
        String elementGUID = "<null>";
        String actualBeanPropertyType = "<null>";


        if (element != null)
        {
            elementString = element.toString();
            elementGUID = element.getElementHeader().getGUID();
            if (element.getProperties() != null)
            {
                actualBeanPropertyType = element.getProperties().getClass().getName();
            }
        }

        if (auditLog != null)
        {
            auditLog.logMessage(methodName, OIFAuditCode.BAD_OM_PROPERTY_TYPE.getMessageDefinition(connectorName,
                                                                                                   elementGUID,
                                                                                                   expectedType.typeName,
                                                                                                   actualBeanPropertyType,
                                                                                                   expectedType.beanClass.getName(),
                                                                                                   methodName,
                                                                                                   elementString));
        }

        throw new ConnectorCheckedException(OIFErrorCode.BAD_OM_PROPERTY_TYPE.getMessageDefinition(connectorName,
                                                                                                   elementGUID,
                                                                                                   expectedType.typeName,
                                                                                                   actualBeanPropertyType,
                                                                                                   expectedType.beanClass.getName(),
                                                                                                   methodName,
                                                                                                   elementString),
                                            this.getClass().getName(),
                                            methodName);
    }



    /**
     * Extract the host identifier (eg coco.com) from the network address.
     *
     * @param networkAddress network address from the endpoint
     * @return host identifier
     */
    protected String getHostIdentifier(String networkAddress)
    {
        String noHttps = networkAddress.replaceAll("https://", "");
        String noHttp = noHttps.replaceAll("http://", "");

        return noHttp.split(":")[0];
    }


    /**
     * Retrieves the port number from a network address string.
     *
     * @param networkAddress the network address from the endpoint
     * @return the port number extracted from the network address, or null if not found
     */
    protected String getPortNumber(String networkAddress)
    {
        String noHttps = networkAddress.replaceAll("https://", "");
        String noHttp = noHttps.replaceAll("http://", "");

        if (noHttp.contains(":"))
        {
            return noHttp.split(":")[1];
        }
        else
        {
            return null;
        }
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
        if (configurationProperties != null)
        {
            if (configurationProperties.get(propertyName) != null)
            {
                return configurationProperties.get(propertyName).toString();
            }
        }

        return null;
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
}
