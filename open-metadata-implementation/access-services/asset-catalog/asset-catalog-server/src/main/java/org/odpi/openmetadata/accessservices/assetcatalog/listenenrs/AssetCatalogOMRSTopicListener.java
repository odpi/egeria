/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.listenenrs;

import org.odpi.openmetadata.accessservices.assetcatalog.auditlog.AssetCatalogAuditCode;
import org.odpi.openmetadata.accessservices.assetcatalog.builders.AssetConverter;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.publishers.AssetCatalogSearchPublisher;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
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
 * AssetCatalogOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes indexing events to the publisher.
 */
public class AssetCatalogOMRSTopicListener extends OMRSTopicListenerBase
{
    private static final Logger log = LoggerFactory.getLogger( AssetCatalogOMRSTopicListener.class );
    private static final String ASSET_TYPE                         = "Asset";

    private OMRSRepositoryHelper        repositoryHelper;
    private OMRSRepositoryValidator     repositoryValidator;
    private String                      serverName;
    private List<String>                supportedZones;
    private List<String>                supportedTypesForSearch;
    private AssetCatalogSearchPublisher publisher;
    private AssetConverter              assetConverter;

    public AssetCatalogOMRSTopicListener(String serviceName,
                                         AuditLog auditLog,
                                         OpenMetadataTopicConnector outTopicConnector,
                                         OMRSRepositoryHelper repositoryHelper,
                                         OMRSRepositoryValidator repositoryValidator,
                                         String serverName,
                                         List<String> supportedZones,
                                         List<String> supportedTypesForSearch
                                         )
            throws OMAGConfigurationErrorException {
            super(serviceName, auditLog);
            this.publisher = new AssetCatalogSearchPublisher(outTopicConnector);
            this.serverName = serverName;
            this.supportedZones = supportedZones;
            this.repositoryHelper = repositoryHelper;
            this.repositoryValidator = repositoryValidator;
            this.assetConverter = new AssetConverter(serverName,repositoryHelper);
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
        if (log.isDebugEnabled()) {
            log.debug("Processing instance event: " + instanceEvent);
        }

        if (instanceEvent == null) {
            log.debug("Ignored instance event - null originator");
            return;
        }

        OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();

        if (instanceEventOriginator == null) {
            return;
        }

        OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
        EntityDetail entityDetail = instanceEvent.getEntity();
        try{

            String assetType = getAssetType(entityDetail);

            if ( assetType != null ) {
                org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.AssetConverter assetConverter
                        = new org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.AssetConverter(entityDetail, null, repositoryHelper, serviceName, serverName);
                Asset assetBean = assetConverter.getAssetBean();

                if (assetBean == null || !this.inTheZone(assetBean.getZoneMembership())) {
                    log.debug("Ignored instance event - Asset not in the supported zones!");
                    return;
                }
                publisher.publishEvent(assetBean);
            }
            else if (  instanceEventType != null && supportedTypesForSearch!=null
                    && supportedTypesForSearch.contains(instanceEventType.getName()))
            {
                AssetDescription assetDescription = assetConverter.getAssetDescription(entityDetail);
                publisher.publishEvent(assetDescription);
            }

        } catch (Exception e) {
            log.error("An exception occurred while processing OMRSTopic event: \n " + instanceEvent, e);
            logExceptionToAudit(instanceEvent, e);
        }
    }

    /**
     * Return the name of the Asset type if this entity has a type that inherits from Asset.
     *
     * @param entity  entity to test
     * @return String containing Asset type name, or null if not an Asset.
     */
    private String getAssetType(EntityDetail  entity)
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
     * @return boolean flag
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
                AssetCatalogAuditCode.EVENT_PROCESSING_EXCEPTION.getMessageDefinition(e.getMessage(), serverName),
                instanceEvent.toString(), e);
    }
}