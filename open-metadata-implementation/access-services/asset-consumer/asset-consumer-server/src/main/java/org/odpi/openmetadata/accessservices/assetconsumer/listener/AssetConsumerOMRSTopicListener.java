/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.listener;

import org.odpi.openmetadata.accessservices.assetconsumer.elements.AssetElement;
import org.odpi.openmetadata.accessservices.assetconsumer.events.AssetConsumerEventType;
import org.odpi.openmetadata.accessservices.assetconsumer.events.NewAssetEvent;
import org.odpi.openmetadata.accessservices.assetconsumer.events.UpdatedAssetEvent;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.AssetConverter;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.accessservices.assetconsumer.outtopic.AssetConsumerPublisher;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.events.*;

import java.util.List;


/**
 * AssetConsumerOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes NEW_ENTITY_EVENTs to the publisher.
 */
public class AssetConsumerOMRSTopicListener extends OMRSTopicListenerBase
{
    private static final String assetTypeName = "Asset";

    private static final Logger log = LoggerFactory.getLogger(AssetConsumerOMRSTopicListener.class);

    private OMRSRepositoryHelper       repositoryHelper;
    private OMRSRepositoryValidator    repositoryValidator;
    private String                     componentName;
    private String                     serverName;
    private String                     serverUserId;
    private List<String>               supportedZones;
    private AssetHandler<AssetElement> assetHandler;
    private AssetConsumerPublisher     publisher;


    /**
     * The constructor is given the connection to the out topic for Asset Consumer OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetHandler provides access to assets
     * @param eventPublisher  publisher for the out topic
     * @param repositoryHelper  provides methods for working with metadata instances
     * @param repositoryValidator  provides validation of metadata instance
     * @param componentName  name of component
     * @param serverName local server name
     * @param serverUserId userId for this server
     * @param supportedZones list of zones covered by this instance of the access service.
     * @param auditLog log for errors and information messages
     */
    public AssetConsumerOMRSTopicListener(AssetHandler<AssetElement> assetHandler,
                                          AssetConsumerPublisher     eventPublisher,
                                          OMRSRepositoryHelper       repositoryHelper,
                                          OMRSRepositoryValidator    repositoryValidator,
                                          String                     componentName,
                                          String                     serverName,
                                          String                     serverUserId,
                                          List<String>               supportedZones,
                                          AuditLog                   auditLog)
    {
        super(componentName, auditLog);

        this.repositoryHelper = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.componentName = componentName;
        this.serverName = serverName;
        this.serverUserId = serverUserId;
        this.supportedZones = supportedZones;
        this.assetHandler = assetHandler;
        this.publisher = eventPublisher;
    }

    /**
     * Unpack and deliver an instance event to the InstanceEventProcessor
     *
     * @param instanceEvent event to unpack
     */
    @Override
    public void processInstanceEvent(OMRSInstanceEvent  instanceEvent)
    {
        final String methodName = "processInstanceEvent";

        log.debug("Processing instance event: " + instanceEvent);

        if (instanceEvent == null)
        {
            log.debug("Null instance event - ignoring event");
        }
        else
        {
            OMRSInstanceEventType instanceEventType       = instanceEvent.getInstanceEventType();
            OMRSEventOriginator   instanceEventOriginator = instanceEvent.getEventOriginator();

            if (instanceEventOriginator != null)
            {
                if (instanceEventType == OMRSInstanceEventType.NEW_ENTITY_EVENT)
                {
                    this.processNewEntity(instanceEvent.getEntity());
                }
                else if (instanceEventType == OMRSInstanceEventType.UPDATED_ENTITY_EVENT)
                {
                    this.processUpdatedEntity(instanceEvent.getEntity(),
                                              instanceEvent.getOriginalEntity());
                }
                else if ((instanceEventType == OMRSInstanceEventType.CLASSIFIED_ENTITY_EVENT) ||
                         (instanceEventType == OMRSInstanceEventType.RECLASSIFIED_ENTITY_EVENT))
                {
                    if (instanceEvent.getEntity() != null)
                    {
                        this.processUpdatedEntity(instanceEvent.getEntity(), null);
                    }
                    else if (instanceEvent.getEntityProxy() != null)
                    {
                        final String parameterName = "entityProxy.getGUID";

                        try
                        {
                            EntityDetail entity = assetHandler.getEntityFromRepository(serverUserId,
                                                                                       instanceEvent.getEntityProxy().getGUID(),
                                                                                       parameterName,
                                                                                       OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                                       null,
                                                                                       null,
                                                                                       false,
                                                                                       false,
                                                                                       supportedZones,
                                                                                       null,
                                                                                       methodName);

                            if (entity != null)
                            {
                                this.processUpdatedEntity(entity, null);
                            }
                        }
                        catch (Exception error)
                        {
                            log.debug("No access to asset - probably belongs to another cohort");
                        }
                    }
                }
            }
            else
            {
                log.debug("Ignored instance event - null originator");
            }
        }
    }


    /**
     * Determine whether a new entity is an Asset.  If it is then publish an Asset Consumer Event about it
     * if the Asset is within the supported zones.
     *
     * @param entity  entity object that has just been created.
     */
    private void processNewEntity(EntityDetail entity)
    {
        String assetType = getAssetType(entity);

        if (assetType != null)
        {
            AssetConverter assetConverter = new AssetConverter(entity, null, repositoryHelper, componentName, serverName);
            Asset          assetBean      = assetConverter.getAssetBean();

            if ((assetBean != null) && (this.inTheZone(assetBean.getZoneMembership())))
            {
                NewAssetEvent event = new NewAssetEvent();

                event.setEventType(AssetConsumerEventType.NEW_ASSET_EVENT);
                event.setAsset(assetConverter.getAssetBean());
                event.setCreationTime(entity.getCreateTime());

                publisher.publishNewAssetEvent(event);
            }
        }
    }


    /**
     * Determine whether an updated entity is an Asset.  If it is then publish an Asset Consumer Event about it
     * if the Asset is within the supported zones.
     *
     * @param entity  entity object that has just been created.
     * @param originalEntity original entity
     */
    private void processUpdatedEntity(EntityDetail entity,
                                      EntityDetail originalEntity)
    {
        String assetType = getAssetType(entity);

        if (assetType != null)
        {
            AssetConverter    assetConverter            = new AssetConverter(entity,
                                                                             null,
                                                                             repositoryHelper,
                                                                             componentName,
                                                                             serverName);
            Asset             assetBean                 = assetConverter.getAssetBean();

            if ((assetBean != null) && (this.inTheZone(assetBean.getZoneMembership())))
            {
                AssetConverter    assetConverterForOriginal = new AssetConverter(originalEntity,
                                                                                 null,
                                                                                 repositoryHelper,
                                                                                 componentName,
                                                                                 serverName);
                UpdatedAssetEvent event                     = new UpdatedAssetEvent();

                event.setEventType(AssetConsumerEventType.UPDATED_ASSET_EVENT);
                event.setAsset(assetBean);
                event.setOriginalAsset(assetConverterForOriginal.getAssetBean());
                if (entity.getUpdateTime() != null)
                {
                    event.setUpdateTime(entity.getUpdateTime());
                }
                else
                {
                    /*
                     * A classification was updated - need a dedicated classification message
                     */
                    event.setUpdateTime(entity.getCreateTime());
                }

                publisher.publishUpdatedAssetEvent(event);
            }
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

        if (repositoryValidator.isATypeOf(componentName, entity, assetTypeName, methodName))
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
        if (supportedZones == null)
        {
            /*
             * If supported zones is null then all zones are supported.
             */
            return true;
        }
        else if (assetZones == null)
        {
            /*
             * If there are no zones set up in the asset then it is a member of all zones.
             */
            return true;
        }
        else
        {
            for (String supportedZoneName : supportedZones)
            {
                for (String assetZoneName : assetZones)
                {
                    if (supportedZoneName.equals(assetZoneName))
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
