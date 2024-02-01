/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.listeners;

import org.odpi.openmetadata.accessservices.assetcatalog.auditlog.AssetCatalogAuditCode;
import org.odpi.openmetadata.accessservices.assetcatalog.converters.AssetCatalogConverter;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogBean;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogEvent;
import org.odpi.openmetadata.accessservices.assetcatalog.publishers.AssetCatalogSearchPublisher;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.AssetConverter;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * AssetCatalogOMRSTopicListener receives details of each OMRS event from the cohorts that the local server
 * is connected to. It passes indexing events to the publisher.
 */
public class AssetCatalogOMRSTopicListener extends OMRSTopicListenerBase
{
    private static final Logger log = LoggerFactory.getLogger( AssetCatalogOMRSTopicListener.class );
    private static final String ASSET_TYPE                        = "Asset";

    private final OMRSRepositoryHelper                            repositoryHelper;
    private final OMRSRepositoryValidator                         repositoryValidator;
    private final String                                          serverName;
    private final List<String>                                    supportedZones;
    private final List<String>                                    supportedTypesForSearch;
    private final AssetCatalogSearchPublisher                     publisher;
    private final AssetCatalogConverter<AssetCatalogBean>         converter;

    /**
     * Instantiates a new Asset catalog OMRS topic listener.
     *
     * @param serviceName             the service name
     * @param auditLog                the audit log
     * @param outTopicConnector       the out topic connector
     * @param repositoryHelper        the repository helper
     * @param repositoryValidator     the repository validator
     * @param serverName              the server name
     * @param supportedZones          the supported zones
     * @param supportedTypesForSearch the supported types for search
     */
    public AssetCatalogOMRSTopicListener(String serviceName,
                                         AuditLog auditLog,
                                         OpenMetadataTopicConnector outTopicConnector,
                                         OMRSRepositoryHelper repositoryHelper,
                                         OMRSRepositoryValidator repositoryValidator,
                                         String serverName,
                                         List<String> supportedZones,
                                         List<String> supportedTypesForSearch
                                         ) {
            super(serviceName, auditLog);
            this.publisher = new AssetCatalogSearchPublisher(outTopicConnector);
            this.serverName = serverName;
            this.supportedZones = supportedZones;
            this.repositoryHelper = repositoryHelper;
            this.repositoryValidator = repositoryValidator;
            this.converter = new AssetCatalogConverter<>(repositoryHelper, serviceName, serverName);
            this.supportedTypesForSearch = supportedTypesForSearch;
    }

    /**
     * Unpack and deliver an instance event to the InstanceEventProcessor
     *
     * @param instanceEvent event to unpack
     */
    @Override
    public void processInstanceEvent(OMRSInstanceEvent instanceEvent)
    {
        log.debug("Processing instance event: {}", instanceEvent);

        if (instanceEvent == null) {
            log.debug("Ignored instance event - null OMRSInstanceEvent");
            return;
        }

        OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();

        if (instanceEventOriginator == null) {
            log.debug("Ignored instance event - null OMRSEventOriginator");
            auditLog.logMessage(
                    "No instance origin. Event IGNORED!",
                    AssetCatalogAuditCode.EVENT_NOT_PROCESSING.getMessageDefinition("no instance origin"));
            return;
        }

        OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
        EntityDetail entityDetail = instanceEvent.getEntity();
        Relationship relationship = instanceEvent.getRelationship();

        try{

            switch (instanceEventType) {
                case UPDATED_ENTITY_EVENT:
                case NEW_ENTITY_EVENT:
                case DELETED_ENTITY_EVENT:
                case CLASSIFIED_ENTITY_EVENT:
                case RECLASSIFIED_ENTITY_EVENT:
                case DECLASSIFIED_ENTITY_EVENT:
                    if (entityDetail != null) {
                        processEntityDetail(entityDetail);
                    }
                    break;
                case NEW_RELATIONSHIP_EVENT :
                case UPDATED_RELATIONSHIP_EVENT:
                case DELETED_RELATIONSHIP_EVENT:
//                    processRelationshipEvent(relationship);
                    break;
                default: break;
            }

        } catch (Exception e) {
            log.error("An exception occurred while processing OMRSTopic event: \n " + instanceEvent, e);
            logExceptionToAudit(instanceEvent, e);
        }

    }

    /**
     * Publishes the relationship event.
     * @param relationship the relationship to be processed
     */
    private void processRelationshipEvent(Relationship relationship){
        publisher.publishEvent(relationship);
    }

    /**
     * Publishes the entity event.
     * @param entityDetail the entityDetail to be processed
     */
    private void processEntityDetail(EntityDetail entityDetail){
            String assetType = getAssetType(entityDetail);

            if ( assetType != null ) {
                AssetConverter assetConverter
                        = new AssetConverter(entityDetail, null, repositoryHelper, serviceName, serverName);
                Asset assetBean = assetConverter.getAssetBean();
                AssetCatalogEvent assetCatalogEvent = new AssetCatalogEvent();
                assetCatalogEvent.setAsset(assetBean);
                if (assetBean == null || !this.inTheZone(assetBean.getZoneMembership())) {
                    log.debug("Ignored instance event - Asset not in the supported zones!");
                    auditLog.logMessage(
                            "Ignored instance event - Asset not in the supported zones!!",
                            AssetCatalogAuditCode.EVENT_NOT_PROCESSING.getMessageDefinition("Asset not in the supported zones!"));
                    return;
                }
                publisher.publishEvent(assetCatalogEvent);
            }else if (  supportedTypesForSearch!=null
                    && supportedTypesForSearch.contains(entityDetail.getType().getTypeDefName()))
            {
                AssetCatalogBean assetDescription = converter.getAssetCatalogBean(entityDetail);
                publisher.publishEvent(assetDescription);
            }

    }

    /**
     * Return the name of the Asset type if this entity has a type that inherits from Asset.
     *
     * @param entity  entity to test
     * @return String containing Asset type name, or null if not an Asset.
     */
    private String getAssetType(EntityDetail entity)
    {
        final   String   methodName = "getAssetType";

        if (repositoryValidator.isATypeOf(serviceName, entity, ASSET_TYPE, methodName))
        {
            InstanceType entityType = entity.getType();

            if (entityType != null)
            {
                return entityType.getTypeDefName();
            }
        }

        return null;
    }

    /**
     * Determines whether an Asset is in the supported zones.
     *
     * @param assetZones list of zones for the asset.
     * @return boolean true if at least one of the assetZones is in the supportedZones, otherwise false
     */
    private boolean inTheZone(List<String>   assetZones)
    {
        if (supportedZones == null || assetZones == null)
        {
            /*
             * If supported zones is null then all zones are supported
             * or there are no zones set up in the asset then it is a member of all zones.
             */
            return true;
        }

        return  supportedZones.stream().anyMatch(assetZones::contains);

    }

    /**
     * Log exceptions using Audit log
     *
     * @param instanceEvent the event that has been received
     * @param e             the exception object
     */
    private void logExceptionToAudit(OMRSInstanceEvent instanceEvent, Exception e) {
        String actionDescription = "Asset Lineage OMAS is unable to process an OMRSTopic event.";

        auditLog.logException(actionDescription,
                AssetCatalogAuditCode.EVENT_PROCESSING_EXCEPTION.getMessageDefinition(e.getMessage(), instanceEvent.toString()),
                instanceEvent.toString(), e);
    }
}