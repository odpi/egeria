/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogTemplateType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CapabilityAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.CapabilityAssetUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * OSSUnityCatalogServerCatalogTargetProcessor supports the handoff between the UC Server Connector and
 * a catalog target that identifies a UC Server.  The key task is to ensure that the metadata collection
 * for the UC catalogs it synchronizes is set up properly so that elements created to represent UC resources
 * are in the appropriate metadata collection.
 */
public class OSSUnityCatalogServerSyncCatalogTargetProcessor extends CatalogTargetProcessorBase implements OpenMetadataEventListener
{
    /*
     * This is the integration connector that synchronizes individual catalogs on Unity Catalog.
     */
    String defaultFriendshipGUID  = null;

    /*
     * These values are extracted from the server definition's server capabilities.
     * Data Access Manager is the software capability that all of the Unity catalog elements are linked off of.
     * The metadata collection information comes from the metadata collection that is linked off of the
     * InventoryCatalog software capability.
     */
    String dataAccessManagerGUID = null;
    String metadataCollectionGUID = null;
    String metadataCollectionName = null;

    /*
     * This is the connector to access the Unity Catalog services.
     */
    OSSUnityCatalogResourceConnector ucResourceConnector = null;

    /*
     * This controls the catalogues that should be catalogued (or not).  The values are used to control
     * which catalogues are configured as catalog targets for the friendship integration connector.
     */
    List<String> defaultExcludeCatalogs = new ArrayList<>();
    List<String> defaultIncludeCatalogs = new ArrayList<>();

    /*
     * This time helps to coordinate the synchronization process.
     */
    private Date lastRefreshCompleteTime = null;


    /**
     * Primary constructor
     *
     * @param template object to copy
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public OSSUnityCatalogServerSyncCatalogTargetProcessor(CatalogTarget        template,
                                                           CatalogTargetContext catalogTargetContext,
                                                           Connector            connectorToTarget,
                                                           String               connectorName,
                                                           AuditLog             auditLog)
    {
        super(template, catalogTargetContext, connectorToTarget, connectorName, auditLog);
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        /*
         * These configuration properties are default values for all catalog targets.
         */
        if (super.getConfigurationProperties() != null)
        {
            defaultFriendshipGUID = this.getFriendshipGUID(super.getConfigurationProperties());
            defaultExcludeCatalogs = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_CATALOG_NAMES.getName(),
                                                                         super.getConfigurationProperties());
            defaultIncludeCatalogs = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_CATALOG_NAMES.getName(),
                                                                         super.getConfigurationProperties());
        }

        if (defaultFriendshipGUID != null)
        {
            /*
             * Individual catalogs will not be synchronized unless the friendship connector is set up in the
             * catalog target's configuration properties.
             */
            auditLog.logMessage(methodName,
                                UCAuditCode.FRIENDSHIP_GUID.getMessageDefinition(connectorName,
                                                                                 defaultFriendshipGUID));
        }


        /*
         * just check we have the right type of resource connector.
         */
        if (super.connectorToTarget instanceof OSSUnityCatalogResourceConnector ossUnityCatalogResourceConnector)
        {
            this.ucResourceConnector = ossUnityCatalogResourceConnector;
        }
        else
        {
            throwWrongTypeOfResourceConnector(OSSUnityCatalogResourceConnector.class.getName(), methodName);
        }

        /*
         * There is no guarantee that the caller set up the metadataSourceQualifiedName property
         * in the catalog target - nor created the server definition using its metadata collection's
         * identifiers.  Therefore, this function validates/corrects the definition of the server and
         * sets up the metadata collection information.
         */
        this.setUpMetadataSource();

        /*
         * The data access manager manages requests to the assets stored in UC.
         */
        dataAccessManagerGUID = this.setUpSoftwareCapability(OpenMetadataType.DATA_ACCESS_MANAGER.typeName);

        /*
         * Add the UC templates to the supplied templates.
         */
        super.setTemplates(this.getTemplates(super.getTemplates()));
    }



    /**
     * Merge the templates supplied on the catalog target relationship with the default templates.
     *
     * @param catalogTargetTemplates supplied templates
     * @return template properties
     */
    private Map<String, String> getTemplates(Map<String, String> catalogTargetTemplates)
    {
        Map<String, String> templateProperties = new HashMap<>();

        /*
         * Add the default templates first
         */
        for (UnityCatalogTemplateType templateType : UnityCatalogTemplateType.values())
        {
            templateProperties.put(templateType.getTemplateName(), templateType.getTemplateGUID());
        }

        /*
         * Override templates supplied for the catalog target
         */
        if (catalogTargetTemplates != null)
        {
            templateProperties.putAll(catalogTargetTemplates);
        }

        return templateProperties;
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

        if (propertyHelper.isTypeOf(super.getCatalogTargetElement().getElementHeader(), UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName()))
        {
            try
            {
                /*
                 * perform a single refresh sweep for the server.
                 */
                OSSUnityCatalogServerSyncCatalog syncCatalog = new OSSUnityCatalogServerSyncCatalog(connectorName,
                                                                                                    integrationContext,
                                                                                                    super.getCatalogTargetName(),
                                                                                                    super.getCatalogTargetElement(),
                                                                                                    dataAccessManagerGUID,
                                                                                                    metadataCollectionGUID,
                                                                                                    metadataCollectionName,
                                                                                                    defaultFriendshipGUID,
                                                                                                    super.getPermittedSynchronization(),
                                                                                                    ucResourceConnector,
                                                                                                    this.getNetworkAddress(),
                                                                                                    super.getTemplates(),
                                                                                                    super.getConfigurationProperties(),
                                                                                                    defaultExcludeCatalogs,
                                                                                                    defaultIncludeCatalogs,
                                                                                                    auditLog);

                CapabilityAssetUseProperties capabilityAssetUseProperties = new CapabilityAssetUseProperties();
                capabilityAssetUseProperties.setUseType(CapabilityAssetUseType.OWNS);

                syncCatalog.refresh(dataAccessManagerGUID,
                                    OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                    capabilityAssetUseProperties);
            }
            catch (Exception exception)
            {
                auditLog.logException(methodName,
                                      UCAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                            exception.getClass().getName(),
                                                                                            methodName,
                                                                                            exception.getMessage()),
                                      exception);
            }
        }
        else
        {
            super.throwWrongTypeOfCatalogTarget(UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
                                                methodName);
        }

        lastRefreshCompleteTime = new Date();
    }


    /**
     * Extract the friendship GUID from the configuration properties - or use the default.
     *
     * @param configurationProperties configuration properties for connection to UC
     * @return friendship GUID or null
     */
    private String getFriendshipGUID(Map<String, Object> configurationProperties)
    {
        String friendshipGUID = defaultFriendshipGUID;

        if ((configurationProperties != null) &&
                (configurationProperties.get(UnityCatalogConfigurationProperty.FRIENDSHIP_GUID.getName()) != null))
        {
            friendshipGUID = super.getConfigurationProperties().get(UnityCatalogConfigurationProperty.FRIENDSHIP_GUID.getName()).toString();
        }

        return friendshipGUID;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }


    /**
     * If the event is for a database and this database is in the server's metadata collection
     * then process it.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        final String methodName = "processEvent";

        /*
         * Only process events if refresh() is not running because the refresh() process creates lots of events and proceeding with event processing
         * at this time causes elements to be processed multiple times.
         */
        if (integrationContext.noRefreshInProgress())
        {
            /*
             * Call the appropriate registered module that matches the type.  Notice that multiple modules can be registered for the same type.
             */
            ElementHeader elementHeader = event.getElementHeader();

            Date   lastUpdateTime = elementHeader.getVersions().getUpdateTime();
            String lastUpdateUser = elementHeader.getVersions().getUpdatedBy();
            String homeMetadataCollectionId = elementHeader.getOrigin().getHomeMetadataCollectionId();

            if (lastUpdateTime == null)
            {
                lastUpdateTime = elementHeader.getVersions().getCreateTime();
                lastUpdateUser = elementHeader.getVersions().getCreatedBy();
            }

            try
            {
                integrationContext.validateIsActive(methodName);

                if ((lastUpdateTime.after(lastRefreshCompleteTime)) &&
                    (! lastUpdateUser.equals(integrationContext.getMyUserId())) &&
                    (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.DATABASE.typeName)) &&
                        (homeMetadataCollectionId.equals(integrationContext.getMetadataSourceGUID())))
                {
                    OSSUnityCatalogServerSyncCatalog syncCatalog = new OSSUnityCatalogServerSyncCatalog(connectorName,
                                                                                                        integrationContext,
                                                                                                        super.getCatalogTargetName(),
                                                                                                        super.getCatalogTargetElement(),
                                                                                                        dataAccessManagerGUID,
                                                                                                        metadataCollectionGUID,
                                                                                                        metadataCollectionName,
                                                                                                        defaultFriendshipGUID,
                                                                                                        super.getPermittedSynchronization(),
                                                                                                        ucResourceConnector,
                                                                                                        this.getNetworkAddress(),
                                                                                                        super.getTemplates(),
                                                                                                        super.getConfigurationProperties(),
                                                                                                        defaultExcludeCatalogs,
                                                                                                        defaultIncludeCatalogs,
                                                                                                        auditLog);

                    CapabilityAssetUseProperties capabilityAssetUseProperties = new CapabilityAssetUseProperties();
                    capabilityAssetUseProperties.setUseType(CapabilityAssetUseType.OWNS);

                    syncCatalog.refresh(dataAccessManagerGUID,
                                        OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                        capabilityAssetUseProperties);
                }
            }
            catch (Exception exception)
            {
                auditLog.logException(methodName,
                                      UCAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                            exception.getClass().getName(),
                                                                                            methodName,
                                                                                            exception.getMessage()),
                                      exception);
            }
        }
    }
}
