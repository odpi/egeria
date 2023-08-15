/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas;

import org.odpi.openmetadata.accessservices.assetmanager.api.AssetManagerEventListener;
import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.ApacheAtlasAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.ApacheAtlasErrorCode;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.modules.ApacheKafkaIntegrationModule;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.modules.AtlasGlossaryIntegrationModule;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.modules.ApacheHiveIntegrationModule;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.modules.AtlasLineageIntegrationModule;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.modules.RDBMSIntegrationModule;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.modules.RegisteredIntegrationModule;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasAttributeDef;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasCardinality;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasEntityDef;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasPropagateTags;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasRelationshipCategory;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasRelationshipDef;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasRelationshipEndDef;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasTypesDef;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorConnector;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ApacheAtlasIntegrationConnector exchanges glossary terms between Apache Atlas and the open metadata ecosystem.
 */
public class ApacheAtlasIntegrationConnector extends CatalogIntegratorConnector implements AssetManagerEventListener
{
    private String                        targetRootURL            = null;
    private CatalogIntegratorContext      myContext                = null;
    private ApacheAtlasRESTClient         atlasClient              = null;
    private AtlasLineageIntegrationModule lineageIntegrationModule = null;

    private final Map<String, List<RegisteredIntegrationModule>> moduleMap  = new HashMap<>();
    private final List<RegisteredIntegrationModule>              moduleList = new ArrayList<>();


    /**
     * The type name for the entity that holds the correlation values for corresponding open metadata entity.
     */
    public static final String OPEN_METADATA_CORRELATION_TYPE_NAME      = "OpenMetadataCorrelation";

    /**
     * The type name for the relationship that links the correlation entity to its associated Apache Atlas entity.
     */
    public static final String OPEN_METADATA_CORRELATION_LINK_TYPE_NAME = "OpenMetadataCorrelationLink";

    /**
     * The type name for the relationship that links the correlation entity to its associated Apache Atlas glossary member.
     */
    public static final String GLOSSARY_CORRELATION_LINK_TYPE_NAME      = "OpenMetadataGlossaryCorrelationLink";


    /* ==============================================================================
     * Standard methods that trigger activity.
     */

    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        myContext = super.getContext();

        if ((connectionProperties.getUserId() == null) || (connectionProperties.getClearPassword() == null))
        {
            throw new ConnectorCheckedException(ApacheAtlasErrorCode.NULL_USER.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * Retrieve the configuration
         */
        EndpointProperties  endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            targetRootURL = endpoint.getAddress();
        }

        if (targetRootURL == null)
        {
            throw new ConnectorCheckedException(ApacheAtlasErrorCode.NULL_URL.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }


        if (myContext.getAssetManagerName() == null)
        {
            throw new ConnectorCheckedException(ApacheAtlasErrorCode.NULL_ASSET_MANAGER.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }


        try
        {
            /*
             * Create the client that calls Apache Atlas.
             */
            atlasClient = new ApacheAtlasRESTClient(connectorName,
                                                    "Apache Atlas",
                                                    targetRootURL,
                                                    connectionProperties.getUserId(),
                                                    connectionProperties.getClearPassword(),
                                                    auditLog);

            /*
             * Ensure the correlation types are properly defined in Apache Atlas.
             */
            setupCorrelationTypes(atlasClient);

            /*
             * Set up the processing modules.  This is currently static, but the intention is that the modules can be plug-in extensions too
             * to support privately defined types.
             */
            this.registerSupportedModule(new AtlasGlossaryIntegrationModule(connectorName,
                                                                            connectionProperties,
                                                                            auditLog,
                                                                            this.getContext(),
                                                                            targetRootURL,
                                                                            atlasClient,
                                                                            embeddedConnectors));

            this.registerSupportedModule(new ApacheHiveIntegrationModule(connectorName,
                                                                         connectionProperties,
                                                                         auditLog,
                                                                         this.getContext(),
                                                                         targetRootURL,
                                                                         atlasClient,
                                                                         embeddedConnectors));

            this.registerSupportedModule(new RDBMSIntegrationModule(connectorName,
                                                                    connectionProperties,
                                                                    auditLog,
                                                                    this.getContext(),
                                                                    targetRootURL,
                                                                    atlasClient,
                                                                    embeddedConnectors));

            this.registerSupportedModule(new ApacheKafkaIntegrationModule(connectorName,
                                                                          connectionProperties,
                                                                          auditLog,
                                                                          this.getContext(),
                                                                          targetRootURL,
                                                                          atlasClient,
                                                                          embeddedConnectors));

            this.lineageIntegrationModule = new AtlasLineageIntegrationModule(connectorName,
                                                                              connectionProperties,
                                                                              auditLog,
                                                                              this.getContext(),
                                                                              targetRootURL,
                                                                              atlasClient,
                                                                              embeddedConnectors);
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      ApacheAtlasAuditCode.BAD_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                  error.getClass().getName(),
                                                                                                  targetRootURL,
                                                                                                  methodName,
                                                                                                  error.getMessage()),
                                      error);
            }

            throw new ConnectorCheckedException(ApacheAtlasErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
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
                                    ApacheAtlasAuditCode.SYNC_INTEGRATION_MODULE.getMessageDefinition(connectorName,
                                                                                                      registeredModule.getModuleName()));
            }

            registeredModule.refresh();
        }

        /*
         * Synchronize the lineage information.  This connector will create the discovered processes and connect them to the associated
         * assets. It will fill out the lineage graph with any DataSet entities that have not been synchronized by the registered modules.
         */
        lineageIntegrationModule.synchronizeLineage();

        /*
         * Once the connector has completed a single refresh, it registered a listener with open metadata
         * to handle updates.  The delay in registering the listener is for efficiency-sake in that it
         * reduces the number of events coming in from updates to the open metadata ecosystem when the connector
         * is performing its first synchronization from Apache Atlas to Egeria.
         *
         * A listener is registered only if metadata is flowing from the open metadata ecosystem to Apache Atlas.
         */
        if ((! myContext.isListenerRegistered()) &&
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
                                          ApacheAtlasAuditCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(connectorName,
                                                                                                                error.getClass().getName(),
                                                                                                                error.getMessage()),
                                          error);
                }

                throw new ConnectorCheckedException(ApacheAtlasErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(),
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
    public void processEvent(AssetManagerOutTopicEvent event)
    {
        /*
         * Only process events if refresh() is not running because the refresh() process creates lots of events and proceeding with event processing
         * at this time causes elements to be processed multiple times.
         */
        if (! myContext.isRefreshInProgress())
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
    private void setupCorrelationTypes(ApacheAtlasRESTClient atlasClient) throws PropertyServerException
    {
        final String serviceType                     = "open_metadata_ecosystem";
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
            entityDef.setServiceType(serviceType);
            entityDef.setTypeVersion(typeVersion);
            entityDef.setVersion(1);

            attributeDefs.add(getStringAttributeDef(egeriaQualifiedNamePropertyName, egeriaQualifiedNameDescription));
            attributeDefs.add(getStringAttributeDef(egeriaTypeNamePropertyName, egeriaTypeNameDescription));
            attributeDefs.add(getStringAttributeDef(egeriaGUIDPropertyName, egeriaGUIDDescription));
            attributeDefs.add(getBooleanAttributeDef(egeriaOwnedPropertyName, egeriaOwnedDescription));

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
            relationshipDef.setServiceType(serviceType);
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
            relationshipDef.setServiceType(serviceType);
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
     * Create an attribute of type string
     *
     * @param propertyName name of the attribute
     * @param propertyDescription description of the attribute
     * @return attribute definition
     */
    private AtlasAttributeDef getStringAttributeDef(String propertyName,
                                                    String propertyDescription)
    {
        AtlasAttributeDef  attributeDef  = new AtlasAttributeDef();

        attributeDef.setName(propertyName);
        attributeDef.setDescription(propertyDescription);
        attributeDef.setTypeName("string");
        attributeDef.setOptional(false);
        attributeDef.setCardinality(AtlasCardinality.SINGLE);
        attributeDef.setValuesMinCount(1);
        attributeDef.setValuesMaxCount(1);
        attributeDef.setUnique(false);
        attributeDef.setIndexable(true);
        attributeDef.setIncludeInNotification(false);
        attributeDef.setSearchWeight(10);

        return attributeDef;
    }


    /**
     * Create an attribute of type boolean
     *
     * @param propertyName name of the attribute
     * @param propertyDescription description of the attribute
     * @return attribute definition
     */
    private AtlasAttributeDef getBooleanAttributeDef(String propertyName,
                                                     String propertyDescription)
    {
        AtlasAttributeDef  attributeDef  = new AtlasAttributeDef();

        attributeDef.setName(propertyName);
        attributeDef.setDescription(propertyDescription);
        attributeDef.setTypeName("boolean");
        attributeDef.setOptional(false);
        attributeDef.setCardinality(AtlasCardinality.SINGLE);
        attributeDef.setValuesMinCount(1);
        attributeDef.setValuesMaxCount(1);
        attributeDef.setUnique(false);
        attributeDef.setIndexable(false);
        attributeDef.setIncludeInNotification(false);
        attributeDef.setSearchWeight(10);

        return attributeDef;
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
            auditLog.logMessage(methodName, ApacheAtlasAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName, targetRootURL));
        }

        super.disconnect();
    }
}
