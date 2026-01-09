/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationAuditCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationErrorCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules.*;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTProvider;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.*;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefGallery;

import java.util.*;


/**
 * ApacheAtlasIntegrationConnector exchanges glossary terms between Apache Atlas and the open metadata ecosystem.
 */
public class ApacheAtlasIntegrationConnector extends IntegrationConnectorBase implements OpenMetadataEventListener
{
    private String                                         targetRootURL                             = null;
    private IntegrationContext                             myContext                                 = null;
    private ApacheAtlasRESTConnector                       atlasClient                               = null;
    private AtlasLineageIntegrationModule                  lineageIntegrationModule                  = null;
    private AtlasInformalTagsIntegrationModule             informalTagsIntegrationModule             = null;
    private AtlasRelatedElementsIntegrationModule          relatedElementsIntegrationModule          = null;
    private AtlasReferenceClassificationsIntegrationModule referenceClassificationsIntegrationModule = null;



    private final Map<String, List<RegisteredIntegrationModule>> moduleMap     = new HashMap<>();
    private final List<RegisteredIntegrationModule>              moduleList    = new ArrayList<>();


    /**
     * The type name for the entity that holds the correlation values for corresponding open metadata entity.
     */
    public static final String OPEN_METADATA_CORRELATION_TYPE_NAME = ApacheAtlasRESTConnector.OPEN_METADATA_TYPE_PREFIX + "Correlation";

    /**
     * The type name for the relationship that links the correlation entity to its associated Apache Atlas entity.
     */
    public static final String OPEN_METADATA_CORRELATION_LINK_TYPE_NAME = ApacheAtlasRESTConnector.OPEN_METADATA_TYPE_PREFIX + "CorrelationLink";

    /**
     * The type name for the relationship that links the correlation entity to its associated Apache Atlas glossary member.
     */
    public static final String GLOSSARY_CORRELATION_LINK_TYPE_NAME = ApacheAtlasRESTConnector.OPEN_METADATA_TYPE_PREFIX + "GlossaryCorrelationLink";

    public static final String[] openMetadataTypesPolicyValidValues  = new String[]{ApacheAtlasIntegrationProvider.ALL_OPEN_METADATA_TYPES_CONFIGURATION_PROPERTY_VALUE,
            ApacheAtlasIntegrationProvider.ON_DEMAND_OPEN_METADATA_TYPES_CONFIGURATION_PROPERTY_VALUE};
    public static final String   openMetadataTypesPolicyDefaultValue = ApacheAtlasIntegrationProvider.ON_DEMAND_OPEN_METADATA_TYPES_CONFIGURATION_PROPERTY_VALUE;


    public static final String[] informalTagsMappingValidValues  = new String[]{ApacheAtlasIntegrationProvider.INFORMAL_TAGS_MAP_TO_ENTITIES_CONFIGURATION_PROPERTY_VALUE,
            ApacheAtlasIntegrationProvider.INFORMAL_TAGS_MAP_TO_CLASSIFICATIONS_CONFIGURATION_PROPERTY_VALUE,
            ApacheAtlasIntegrationProvider.INFORMAL_TAGS_MAP_TO_LABELS_CONFIGURATION_PROPERTY_VALUE,
            ApacheAtlasIntegrationProvider.INFORMAL_TAGS_NO_MAPPING_CONFIGURATION_PROPERTY_VALUE};
    public static final String   informalTagsMappingDefaultValue = ApacheAtlasIntegrationProvider.INFORMAL_TAGS_NO_MAPPING_CONFIGURATION_PROPERTY_VALUE;

    public static final String[] classificationReferenceSetPolicyValidValues  = new String[]{ApacheAtlasIntegrationProvider.CLASSIFICATION_REFERENCE_SET_TO_ATLAS_CONFIGURATION_PROPERTY_VALUE,
            ApacheAtlasIntegrationProvider.CLASSIFICATION_REFERENCE_SET_FROM_ATLAS_CONFIGURATION_PROPERTY_VALUE,
            ApacheAtlasIntegrationProvider.CLASSIFICATION_REFERENCE_SET_BOTH_WAYS_CONFIGURATION_PROPERTY_VALUE};
    public static final String   classificationReferenceSetPolicyDefaultValue = ApacheAtlasIntegrationProvider.CLASSIFICATION_REFERENCE_SET_FROM_ATLAS_CONFIGURATION_PROPERTY_VALUE;

    public static final String[] relatedElementPolicyValidValues  = new String[]{ApacheAtlasIntegrationProvider.ALL_RELATED_ELEMENT_CONFIGURATION_PROPERTY_VALUE,
            ApacheAtlasIntegrationProvider.SELECTED_RELATED_ELEMENT_CONFIGURATION_PROPERTY_VALUE,
            ApacheAtlasIntegrationProvider.NONE_RELATED_ELEMENT_CONFIGURATION_PROPERTY_VALUE};
    public static final String   relatedElementPolicyDefaultValue = ApacheAtlasIntegrationProvider.NONE_RELATED_ELEMENT_CONFIGURATION_PROPERTY_VALUE;


    /* ==============================================================================
     * Standard methods that trigger activity.
     */

    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException  the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        myContext = integrationContext;

        if ((connectionBean.getUserId() == null) || (connectionBean.getClearPassword() == null))
        {
            throw new ConnectorCheckedException(AtlasIntegrationErrorCode.NULL_USER.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * Retrieve the configuration
         */
        Endpoint endpoint = connectionBean.getEndpoint();

        if (endpoint != null)
        {
            targetRootURL = endpoint.getNetworkAddress();
        }

        if (targetRootURL == null)
        {
            throw new ConnectorCheckedException(AtlasIntegrationErrorCode.NULL_URL.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }


        if (myContext.getMetadataSourceQualifiedName() == null)
        {
            throw new ConnectorCheckedException(AtlasIntegrationErrorCode.NULL_ASSET_MANAGER.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        try
        {
            /*
             * Create the client that calls Apache Atlas.
             */
            Connection atlasConnection = new Connection(connectionBean);

            atlasConnection.setConnectorType(new ApacheAtlasRESTProvider().getConnectorType());
            ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);

            Connector newConnector = connectorBroker.getConnector(atlasConnection);

            if (newConnector instanceof ApacheAtlasRESTConnector apacheAtlasRESTConnector)
            {
                this.atlasClient = apacheAtlasRESTConnector;
                this.atlasClient.start();
            }
            else
            {
                throw new ConnectorCheckedException(AtlasIntegrationErrorCode.NULL_ATLAS_CLIENT.getMessageDefinition(connectorName),
                                                    this.getClass().getName(),
                                                    methodName);
            }

            /*
             * Ensure the correlation type definitions are properly defined in Apache Atlas.
             */
            // todo setupCorrelationTypes(atlasClient);

            /*
             * Set up the configuration properties that control the synchronizing of types.
             */
            /* todo
            if (connectionBean.getConfigurationProperties() != null)
            {
                String openMetadataTypesPolicy = this.getEnumConfigurationProperty(ApacheAtlasIntegrationProvider.OPEN_METADATA_TYPES_POLICY_CONFIGURATION_PROPERTY,
                                                                                   connectionBean.getConfigurationProperties().get(ApacheAtlasIntegrationProvider.OPEN_METADATA_TYPES_POLICY_CONFIGURATION_PROPERTY),
                                                                                   Arrays.asList(openMetadataTypesPolicyValidValues),
                                                                                   openMetadataTypesPolicyDefaultValue);

                if (ApacheAtlasIntegrationProvider.ALL_OPEN_METADATA_TYPES_CONFIGURATION_PROPERTY_VALUE.equals(openMetadataTypesPolicy))
                {
                    setupOpenMetadataTypes(atlasClient,
                                           getConfigurationPropertyList(ApacheAtlasIntegrationProvider.IGNORE_OPEN_METADATA_TYPES_CONFIGURATION_PROPERTY,
                                                                        connectionBean.getConfigurationProperties().get(ApacheAtlasIntegrationProvider.OPEN_METADATA_TYPES_POLICY_CONFIGURATION_PROPERTY)));
                }
            }

             */

            /*
             * Set up the processing modules.  This is currently static, but the intention is that the modules can be plug-in extensions too
             * to support privately defined types.
             */
            this.registerSupportedModule(new AtlasGlossaryIntegrationModule(connectorName,
                                                                            connectionBean,
                                                                            auditLog,
                                                                            integrationContext,
                                                                            targetRootURL,
                                                                            atlasClient,
                                                                            embeddedConnectors));

            this.registerSupportedModule(new ApacheHiveIntegrationModule(connectorName,
                                                                         connectionBean,
                                                                         auditLog,
                                                                         integrationContext,
                                                                         targetRootURL,
                                                                         atlasClient,
                                                                         embeddedConnectors));

            this.registerSupportedModule(new RDBMSIntegrationModule(connectorName,
                                                                    connectionBean,
                                                                    auditLog,
                                                                    integrationContext,
                                                                    targetRootURL,
                                                                    atlasClient,
                                                                    embeddedConnectors));

            this.registerSupportedModule(new ApacheKafkaIntegrationModule(connectorName,
                                                                          connectionBean,
                                                                          auditLog,
                                                                          integrationContext,
                                                                          targetRootURL,
                                                                          atlasClient,
                                                                          embeddedConnectors));

            this.lineageIntegrationModule = new AtlasLineageIntegrationModule(connectorName,
                                                                              connectionBean,
                                                                              auditLog,
                                                                              integrationContext,
                                                                              targetRootURL,
                                                                              atlasClient,
                                                                              embeddedConnectors);

            String informalTagsMappingPolicy = informalTagsMappingDefaultValue;

            if (connectionBean.getConfigurationProperties() != null)
            {
                informalTagsMappingPolicy = this.getEnumConfigurationProperty(ApacheAtlasIntegrationProvider.INFORMAL_TAGS_MAPPING_POLICY_CONFIGURATION_PROPERTY,
                                                                              connectionBean.getConfigurationProperties().get(ApacheAtlasIntegrationProvider.INFORMAL_TAGS_MAPPING_POLICY_CONFIGURATION_PROPERTY),
                                                                              Arrays.asList(informalTagsMappingValidValues),
                                                                              informalTagsMappingDefaultValue);
            }

            this.informalTagsIntegrationModule = new AtlasInformalTagsIntegrationModule(connectorName,
                                                                                        connectionBean,
                                                                                        auditLog,
                                                                                        integrationContext,
                                                                                        targetRootURL,
                                                                                        atlasClient,
                                                                                        embeddedConnectors,
                                                                                        informalTagsMappingPolicy);


            String                   relatedClassificationPolicy = null;
            List<String>             ignoreClassificationList = null;
            String                   relatedEntityPolicy = null;
            List<String>             ignoreRelationshipList = null;

            if (connectionBean.getConfigurationProperties() != null)
            {
                relatedClassificationPolicy = this.getEnumConfigurationProperty(ApacheAtlasIntegrationProvider.RELATED_CLASSIFICATION_POLICY_CONFIGURATION_PROPERTY,
                                                                                connectionBean.getConfigurationProperties().get(ApacheAtlasIntegrationProvider.RELATED_CLASSIFICATION_POLICY_CONFIGURATION_PROPERTY),
                                                                                Arrays.asList(relatedElementPolicyValidValues),
                                                                                relatedElementPolicyDefaultValue);

                ignoreClassificationList = this.getConfigurationPropertyList(ApacheAtlasIntegrationProvider.RELATED_CLASSIFICATION_IGNORE_LIST_CONFIGURATION_PROPERTY,
                                                                             connectionBean.getConfigurationProperties().get(ApacheAtlasIntegrationProvider.RELATED_CLASSIFICATION_IGNORE_LIST_CONFIGURATION_PROPERTY));

                relatedEntityPolicy = this.getEnumConfigurationProperty(ApacheAtlasIntegrationProvider.RELATED_ENTITY_POLICY_CONFIGURATION_PROPERTY,
                                                                        connectionBean.getConfigurationProperties().get(ApacheAtlasIntegrationProvider.RELATED_ENTITY_POLICY_CONFIGURATION_PROPERTY),
                                                                        Arrays.asList(relatedElementPolicyValidValues),
                                                                        relatedElementPolicyDefaultValue);

                ignoreRelationshipList = this.getConfigurationPropertyList(ApacheAtlasIntegrationProvider.RELATED_RELATIONSHIP_IGNORE_LIST_CONFIGURATION_PROPERTY,
                                                                           connectionBean.getConfigurationProperties().get(ApacheAtlasIntegrationProvider.RELATED_RELATIONSHIP_IGNORE_LIST_CONFIGURATION_PROPERTY));
            }

            this.relatedElementsIntegrationModule = new AtlasRelatedElementsIntegrationModule(connectorName,
                                                                                              connectionBean,
                                                                                              auditLog,
                                                                                              integrationContext,
                                                                                              targetRootURL,
                                                                                              atlasClient,
                                                                                              embeddedConnectors,
                                                                                              informalTagsMappingPolicy,
                                                                                              relatedClassificationPolicy,
                                                                                              ignoreClassificationList,
                                                                                              relatedEntityPolicy,
                                                                                              ignoreRelationshipList);


            String classificationReferenceSetName = null;
            String classificationReferenceSetPolicy = classificationReferenceSetPolicyDefaultValue;

            if (connectionBean.getConfigurationProperties() != null)
            {
                classificationReferenceSetName = this.getStringConfigurationProperty(ApacheAtlasIntegrationProvider.CLASSIFICATION_REFERENCE_SET_NAME_CONFIGURATION_PROPERTY,
                                                                                     connectionBean.getConfigurationProperties().get(ApacheAtlasIntegrationProvider.CLASSIFICATION_REFERENCE_SET_NAME_CONFIGURATION_PROPERTY),
                                                                                     null);

                classificationReferenceSetPolicy = this.getEnumConfigurationProperty(ApacheAtlasIntegrationProvider.CLASSIFICATION_REFERENCE_SET_POLICY_CONFIGURATION_PROPERTY,
                                                                                     connectionBean.getConfigurationProperties().get(ApacheAtlasIntegrationProvider.CLASSIFICATION_REFERENCE_SET_POLICY_CONFIGURATION_PROPERTY),
                                                                                     Arrays.asList(classificationReferenceSetPolicyValidValues),
                                                                                     classificationReferenceSetPolicyDefaultValue);
            }

            this.referenceClassificationsIntegrationModule = new AtlasReferenceClassificationsIntegrationModule(connectorName,
                                                                                                                connectionBean,
                                                                                                                auditLog,
                                                                                                                integrationContext,
                                                                                                                targetRootURL,
                                                                                                                atlasClient,
                                                                                                                embeddedConnectors,
                                                                                                                classificationReferenceSetName,
                                                                                                               classificationReferenceSetPolicy);
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      AtlasIntegrationAuditCode.BAD_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       targetRootURL,
                                                                                                       methodName,
                                                                                                       error.getMessage()),
                                      error);
            }

            throw new ConnectorCheckedException(AtlasIntegrationErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                    error.getClass().getName(),
                                                                                                                    methodName,
                                                                                                                    error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Extract the list of string values from a configuration property.
     *
     * @param propertyName  name of the property
     * @param propertyValue value that the property is set to
     * @return null or the value parsed into a list of strings
     */

    private List<String> getConfigurationPropertyList(String propertyName,
                                                      Object propertyValue)
    {
        final String methodName = "getConfigurationPropertyList";

        List<String> result = null;

        if (propertyValue != null)
        {
            result = new ArrayList<>();

            String propertyValuesString = propertyValue.toString().replace('[', ' ');
            propertyValuesString = propertyValuesString.replace(']', ' ');

            StringTokenizer stringTokenizer = new StringTokenizer(propertyValuesString, ",");

            while (stringTokenizer.hasMoreTokens())
            {
                String value        = stringTokenizer.nextToken();
                String trimmedValue = value.trim();

                if (!trimmedValue.isEmpty())
                {
                    result.add(trimmedValue);
                }
            }
            if (result.isEmpty())
            {
                result = null;
            }
        }

        if (auditLog != null)
        {
            if (result == null)
            {
                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CONFIGURATION_PROPERTY_NOT_SET.getMessageDefinition(connectorName,
                                                                                                                  "null",
                                                                                                                  propertyName));
            }
            else
            {
                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.LIST_CONFIGURATION_PROPERTY_SET.getMessageDefinition(propertyName,
                                                                                                                   Integer.toString(result.size()),
                                                                                                                   result.toString(),
                                                                                                                   connectorName));
            }
        }

        return result;

    }


    /**
     * Extract the string value from a configuration property.
     *
     * @param propertyName  name of the property
     * @param propertyValue value that the property is set to
     * @param defaultValue  default value if it is not set
     * @return null or the value converted into a string
     */
    private String getStringConfigurationProperty(String propertyName,
                                                  Object propertyValue,
                                                  String defaultValue)
    {
        final String methodName = "getStringConfigurationProperty";

        String result = null;

        if (propertyValue != null)
        {
            result = propertyValue.toString();
        }

        if (auditLog != null)
        {
            if (result == null)
            {
                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CONFIGURATION_PROPERTY_NOT_SET.getMessageDefinition(connectorName,
                                                                                                                  defaultValue,
                                                                                                                  propertyName));
            }
            else
            {
                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CONFIGURATION_PROPERTY_SET.getMessageDefinition(propertyName,
                                                                                                              result,
                                                                                                              connectorName));
            }
        }

        if (result == null)
        {
            return defaultValue;
        }

        return result;
    }


    /**
     * Extract and validate the string value from a configuration property.
     *
     * @param propertyName  name of the property
     * @param propertyValue value that the property is set to
     * @param validValues   list of valid values for this property
     * @param defaultValue  default value if it is not set
     * @return null or the value converted into a string
     */
    private String getEnumConfigurationProperty(String propertyName,
                                                Object propertyValue,
                                                List<String> validValues,
                                                String defaultValue)
    {
        final String methodName = "getEnumConfigurationProperty";

        String result = null;

        if (propertyValue != null)
        {
            result = propertyValue.toString();

            if (!validValues.contains(result))
            {
                result = null;
            }
        }

        if (auditLog != null)
        {
            if (result == null)
            {
                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CONFIGURATION_PROPERTY_NOT_SET.getMessageDefinition(connectorName,
                                                                                                                  defaultValue,
                                                                                                                  propertyName));
            }
            else
            {
                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CONFIGURATION_PROPERTY_SET.getMessageDefinition(propertyName,
                                                                                                              result,
                                                                                                              connectorName));
            }
        }

        if (result == null)
        {
            return defaultValue;
        }

        return result;
    }


    /**
     * Populate the moduleList and moduleMap with details of a supported module.
     *
     * @param supportedModule module
     */
    private void registerSupportedModule(RegisteredIntegrationModule supportedModule)
    {
        moduleList.add(supportedModule);

        if (supportedModule.getListenForTypes() != null)
        {
            for (String supportedType : supportedModule.getListenForTypes())
            {
                if (supportedType != null)
                {
                    List<RegisteredIntegrationModule> modulesForTypeName = moduleMap.get(supportedType);

                    if (modulesForTypeName == null)
                    {
                        modulesForTypeName = new ArrayList<>();
                    }

                    modulesForTypeName.add(supportedModule);

                    moduleMap.put(supportedType, modulesForTypeName);
                }
            }
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        /*
         * Step through each registered module - each responsible for synchronizing a collection of Asset types from Atlas.
         */
        for (RegisteredIntegrationModule registeredModule : moduleList)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.SYNC_INTEGRATION_MODULE.getMessageDefinition(connectorName,
                                                                                                           registeredModule.getModuleName()));
            }

            registeredModule.refresh();
        }

        /*
         * Synchronize the lineage information.  This connector will create the discovered processes and connect them to the associated
         * assets. It will fill out the lineage graph with any DataSet entities that have not been synchronized by the registered modules.
         */
        // todo lineageIntegrationModule.synchronizeLineage();

        /*
         * Look for InformalTags attached to the open metadata ecosystem version of the Atlas entities and decide
         * how they should be represented in Apache Atlas.  Choice is as classifications, labels or entities.
         */
        // todo informalTagsIntegrationModule.synchronizeInformalTags();

        /*
         * Set up classification types as defined by the classification reference set.
         */
        // todo Map<String, ValidValueDefinitionElement> referenceClassifications = referenceClassificationsIntegrationModule.synchronizeClassificationDefinitions();

        /*
         * Handle additional related classifications and relationships/entities.
         */
        // todo relatedElementsIntegrationModule.synchronizeRelatedElements(referenceClassifications);

        /*
         * Once the connector has completed a single refresh, it registered a listener with open metadata
         * to handle updates.  The delay in registering the listener is for efficiency-sake in that it
         * reduces the number of events coming in from updates to the open metadata ecosystem when the connector
         * is performing its first synchronization from Apache Atlas to Egeria.
         *
         * A listener is registered only if metadata is flowing from the open metadata ecosystem to Apache Atlas.
         */
        if ((myContext.noListenerRegistered()) &&
                (myContext.getPermittedSynchronization() == PermittedSynchronization.BOTH_DIRECTIONS) ||
                (myContext.getPermittedSynchronization() == PermittedSynchronization.TO_THIRD_PARTY))
        {
            try
            {
                /*
                 * This request registers this connector to receive events from the open metadata ecosystem.  When an event occurs,
                 * the processEvent() method is called.
                 */
                myContext.registerListener(this);
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          AtlasIntegrationAuditCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(connectorName,
                                                                                                                     error.getClass().getName(),
                                                                                                                     error.getMessage()),
                                          error);
                }

                throw new ConnectorCheckedException(AtlasIntegrationErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * Process an event that was published by the Asset Manager OMAS.  This connector is only interested in
     * glossaries, glossary categories and glossary terms.   The listener is only registered if metadata is flowing
     * from the open metadata ecosystem to Apache Atlas.
     *
     * @param event event object
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        /*
         * Only process events if refresh() is not running because the refresh() process creates lots of events and proceeding with event processing
         * at this time causes elements to be processed multiple times.
         */
        if (myContext.noRefreshInProgress())
        {
            /*
             * Call the appropriate registered module that matches the type.  Notice that multiple modules can be registered for the same type.
             */
            ElementHeader elementHeader = event.getElementHeader();

            for (String supportedType : moduleMap.keySet())
            {
                if (myContext.isTypeOf(elementHeader, supportedType))
                {
                    List<RegisteredIntegrationModule> modulesForTypeName = moduleMap.get(supportedType);

                    if (modulesForTypeName != null)
                    {
                        for (RegisteredIntegrationModule registeredModule : modulesForTypeName)
                        {
                            registeredModule.processEvent(event);
                        }
                    }
                }
            }
        }
    }


    /**
     * This connector uses an entity type called OpenMetadataCorrelation to store information about the
     * equivalent open metadata entity.  It is linked to the corresponding Apache Atlas entity using the
     * OpenMetadataCorrelationLink relationship.  These types are not part of the standard Apache Atlas Type system.
     * This method queries to see if these types are defined.  If they are not then they are created in Apache Atlas.
     *
     * @param atlasClient client for Apache Atlas
     * @throws PropertyServerException unable to connect to Apache Atlas
     */
    private void setupCorrelationTypes(ApacheAtlasRESTConnector atlasClient) throws PropertyServerException
    {
        final String entityDescription               = "Information used in synchronizing Apache Atlas metadata with external catalogs via the Open Metadata Ecosystem supported by Egeria.";
        final String egeriaQualifiedNamePropertyName = "egeriaQualifiedName";
        final String egeriaQualifiedNameDescription  = "Unique name of equivalent open metadata instance in the open metadata ecosystem.";
        final String egeriaTypeNamePropertyName      = "egeriaTypeName";
        final String egeriaTypeNameDescription       = "Type name of equivalent open metadata instance in the open metadata ecosystem.";
        final String egeriaGUIDPropertyName          = "egeriaGUID";
        final String egeriaGUIDDescription           = "Unique identifier of equivalent open metadata instance in the open metadata ecosystem.";
        final String egeriaOwnedPropertyName         = "egeriaOwned";
        final String egeriaOwnedDescription          = "Boolean flag indicating whether the instance originated in the open metadata ecosystem (true) or Apache Atlas (false).";
        final String relationshipDescription         = "Relationship to connect a referenceable to correlation information from the open metadata ecosystem.";
        final String correlationEndLabel             = "openMetadataCorrelation";
        final String associatedElementEndLabel       = "associatedElement";
        final String associatedGlossaryEndLabel      = "associatedMeaning";
        final String referenceableTypeName           = "Referenceable";
        final String internalTypeName                = "__internal";
        final String typeVersion                     = "V1.0";

        /*
         * Check that Apache Atlas is running.  Any exceptions are returned.
         */
        atlasClient.getAllTypes();

        /*
         * Retrieve the OpenMetadataCorrelation entity type.  If an exception occurs then the type is not defined, and it is added.
         */
        try
        {
            atlasClient.getEntityType(OPEN_METADATA_CORRELATION_TYPE_NAME);
        }
        catch (PropertyServerException notFound)
        {
            AtlasTypesDef           typesDef      = new AtlasTypesDef();
            List<AtlasEntityDef>    entityDefs    = new ArrayList<>();
            AtlasEntityDef          entityDef     = new AtlasEntityDef();
            List<AtlasAttributeDef> attributeDefs = new ArrayList<>();

            entityDef.setName(OPEN_METADATA_CORRELATION_TYPE_NAME);
            entityDef.setDescription(entityDescription);
            entityDef.setServiceType(ApacheAtlasRESTConnector.SERVICE_TYPE);
            entityDef.setTypeVersion(typeVersion);
            entityDef.setVersion(1);

            attributeDefs.add(atlasClient.getStringAttributeDef(egeriaQualifiedNamePropertyName, egeriaQualifiedNameDescription));
            attributeDefs.add(atlasClient.getStringAttributeDef(egeriaTypeNamePropertyName, egeriaTypeNameDescription));
            attributeDefs.add(atlasClient.getStringAttributeDef(egeriaGUIDPropertyName, egeriaGUIDDescription));
            attributeDefs.add(atlasClient.getBooleanAttributeDef(egeriaOwnedPropertyName, egeriaOwnedDescription));

            entityDef.setAttributeDefs(attributeDefs);
            entityDefs.add(entityDef);
            typesDef.setEntityDefs(entityDefs);

            atlasClient.addNewTypes(typesDef);
        }


        /*
         * Retrieve the OpenMetadataCorrelationLink relationship type.  If an exception occurs then the type is not defined, and it is added.
         */
        try
        {
            atlasClient.getRelationshipType(OPEN_METADATA_CORRELATION_LINK_TYPE_NAME);
        }
        catch (PropertyServerException notFound)
        {
            AtlasTypesDef              typesDef         = new AtlasTypesDef();
            List<AtlasRelationshipDef> relationshipDefs = new ArrayList<>();
            AtlasRelationshipDef       relationshipDef  = new AtlasRelationshipDef();
            AtlasRelationshipEndDef    end1             = new AtlasRelationshipEndDef();
            AtlasRelationshipEndDef    end2             = new AtlasRelationshipEndDef();

            relationshipDef.setName(OPEN_METADATA_CORRELATION_LINK_TYPE_NAME);
            relationshipDef.setDescription(relationshipDescription);
            relationshipDef.setServiceType(ApacheAtlasRESTConnector.SERVICE_TYPE);
            relationshipDef.setTypeVersion(typeVersion);
            relationshipDef.setVersion(1);
            relationshipDef.setRelationshipCategory(AtlasRelationshipCategory.ASSOCIATION);
            relationshipDef.setPropagateTags(AtlasPropagateTags.NONE);
            end1.setType(referenceableTypeName);
            end1.setName(correlationEndLabel);
            end1.setContainer(false);
            end1.setCardinality(AtlasCardinality.SINGLE);
            end1.setLegacyAttribute(false);

            end2.setType(OPEN_METADATA_CORRELATION_TYPE_NAME);
            end2.setName(associatedElementEndLabel);
            end2.setContainer(false);
            end2.setCardinality(AtlasCardinality.SINGLE);
            end2.setLegacyAttribute(false);

            relationshipDef.setEndDef1(end1);
            relationshipDef.setEndDef2(end2);
            relationshipDefs.add(relationshipDef);
            typesDef.setRelationshipDefs(relationshipDefs);

            atlasClient.addNewTypes(typesDef);
        }


        /*
         * Retrieve the OpenMetadataCorrelationLink relationship type.  If an exception occurs then the type is not defined, and it is added.
         */
        try
        {
            atlasClient.getRelationshipType(GLOSSARY_CORRELATION_LINK_TYPE_NAME);
        }
        catch (PropertyServerException notFound)
        {
            AtlasTypesDef              typesDef         = new AtlasTypesDef();
            List<AtlasRelationshipDef> relationshipDefs = new ArrayList<>();
            AtlasRelationshipDef       relationshipDef  = new AtlasRelationshipDef();
            AtlasRelationshipEndDef    end1             = new AtlasRelationshipEndDef();
            AtlasRelationshipEndDef    end2             = new AtlasRelationshipEndDef();

            relationshipDef.setName(GLOSSARY_CORRELATION_LINK_TYPE_NAME);
            relationshipDef.setDescription(relationshipDescription);
            relationshipDef.setServiceType(ApacheAtlasRESTConnector.SERVICE_TYPE);
            relationshipDef.setTypeVersion(typeVersion);
            relationshipDef.setVersion(1);
            relationshipDef.setRelationshipCategory(AtlasRelationshipCategory.ASSOCIATION);
            relationshipDef.setPropagateTags(AtlasPropagateTags.NONE);
            end1.setType(internalTypeName);
            end1.setName(correlationEndLabel);
            end1.setContainer(false);
            end1.setCardinality(AtlasCardinality.SINGLE);
            end1.setLegacyAttribute(false);

            end2.setType(OPEN_METADATA_CORRELATION_TYPE_NAME);
            end2.setName(associatedGlossaryEndLabel);
            end2.setContainer(false);
            end2.setCardinality(AtlasCardinality.SINGLE);
            end2.setLegacyAttribute(false);

            relationshipDef.setEndDef1(end1);
            relationshipDef.setEndDef2(end2);
            relationshipDefs.add(relationshipDef);
            typesDef.setRelationshipDefs(relationshipDefs);

            atlasClient.addNewTypes(typesDef);
        }
    }


    /**
     * Add definitions for all open metadata types, except the types to ignore, in Apache Atlas.
     * This method is called if the "openMetadataTypesPolicy"=ALL.
     *
     * @param atlasClient   client for Apache Atlas
     * @param typesToIgnore null or list of types to ignore
     * @throws PropertyServerException    unable to connect to the open metadata ecosystem or Apache Atlas
     * @throws UserNotAuthorizedException security problem in connecting to open metadata ecosystem
     * @throws InvalidParameterException  probably a logic error
     */
    private void setupOpenMetadataTypes(ApacheAtlasRESTConnector atlasClient,
                                        List<String> typesToIgnore) throws PropertyServerException,
                                                                           InvalidParameterException,
                                                                           UserNotAuthorizedException
    {
        final String methodName = "setupOpenMetadataTypes";

        List<String> exclusionList = typesToIgnore;

        if (exclusionList == null)
        {
            exclusionList = new ArrayList<>();
        }

        OpenMetadataTypeDefGallery openMetadataTypeDefGallery = myContext.getOpenMetadataTypesClient().getAllTypes();

        if (openMetadataTypeDefGallery != null)
        {
            /*
             * Note that open metadata enums are added to Apache Atlas as strings
             */
            if (openMetadataTypeDefGallery.getTypeDefs() != null)
            {
                for (OpenMetadataTypeDef typeDef : openMetadataTypeDefGallery.getTypeDefs())
                {
                    if ((typeDef != null) && (!exclusionList.contains(typeDef.getName())))
                    {
                        try
                        {
                            atlasClient.addOpenMetadataType(typeDef);
                        }
                        catch (Exception error)
                        {
                            if (auditLog != null)
                            {
                                auditLog.logException(methodName,
                                                      AtlasIntegrationAuditCode.UNABLE_TO_DEFINE_TYPE_IN_ATLAS.getMessageDefinition(connectorName,
                                                                                                                                    error.getClass().getName(),
                                                                                                                                    error.getMessage()),
                                                      error);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Shutdown Apache Atlas monitoring
     *
     * @throws ConnectorCheckedException something failed in the super class
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        if (auditLog != null)
        {
            auditLog.logMessage(methodName, AtlasIntegrationAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName, targetRootURL));
        }

        if (atlasClient != null)
        {
            atlasClient.disconnect();
        }

        super.disconnect();
    }
}
