/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.listener;

import org.odpi.openmetadata.accessservices.assetconsumer.events.NewAssetEvent;
import org.odpi.openmetadata.accessservices.assetconsumer.events.UpdatedAssetEvent;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.AssetConverter;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.accessservices.assetconsumer.outtopic.AssetConsumerPublisher;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
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
    private static final String assetTypeName                         = "Asset";

    private static final Logger log = LoggerFactory.getLogger(AssetConsumerOMRSTopicListener.class);

    private OMRSRepositoryHelper    repositoryHelper;
    private OMRSRepositoryValidator repositoryValidator;
    private String                  componentName;
    private String                  serverName;
    private List<String>            supportedZones;
    private AssetConsumerPublisher  publisher;


    /**
     * The constructor is given the connection to the out topic for Asset Consumer OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetConsumerOutTopic  connection to the out topic
     * @param repositoryHelper  provides methods for working with metadata instances
     * @param repositoryValidator  provides validation of metadata instance
     * @param componentName  name of component
     * @param serverName local server name
     * @param supportedZones list of zones covered by this instance of the access service.
     * @param auditLog log for errors and information messages
     * @throws OMAGConfigurationErrorException problems creating the connector for the outTopic
     */
    public AssetConsumerOMRSTopicListener(Connection              assetConsumerOutTopic,
                                          OMRSRepositoryHelper    repositoryHelper,
                                          OMRSRepositoryValidator repositoryValidator,
                                          String                  componentName,
                                          String                  serverName,
                                          List<String>            supportedZones,
                                          AuditLog                auditLog) throws OMAGConfigurationErrorException
    {
        super(componentName, auditLog);

        this.repositoryHelper = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.componentName = componentName;
        this.serverName = serverName;
        this.supportedZones = supportedZones;

        publisher = new AssetConsumerPublisher(assetConsumerOutTopic, auditLog);
    }

    /**
     * Unpack and deliver an instance event to the InstanceEventProcessor
     *
     * @param instanceEvent event to unpack
     */
    @Override
    public void processInstanceEvent(OMRSInstanceEvent  instanceEvent)
    {
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

                event.setAsset(assetBean);
                event.setOriginalAsset(assetConverterForOriginal.getAssetBean());
                event.setUpdateTime(entity.getUpdateTime());

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
