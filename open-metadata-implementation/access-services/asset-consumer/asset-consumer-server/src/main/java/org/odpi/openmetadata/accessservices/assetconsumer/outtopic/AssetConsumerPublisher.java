/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.outtopic;

import org.apache.log4j.Logger;
import org.odpi.openmetadata.frameworks.connectors.properties.Connection;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.Asset;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

/**
 * AssetConsumerPublisher is responsible for publishing events about assets.  It is called
 * when an interesting OMRS Event is added to the Enterprise OMRS Topic.  It adds events to the Asset Consumer OMAS
 * out topic.
 */
public class AssetConsumerPublisher
{
    private static final String assetTypeName                  = "Asset";
    private static final String assetPropertyNameQualifiedName = "qualifiedName";
    private static final String assetPropertyNameDisplayName   = "name";
    private static final String assetPropertyNameOwner         = "owner";
    private static final String assetPropertyNameDescription   = "description";

    private static final Logger log = Logger.getLogger(AssetConsumerPublisher.class);

    private Connection              assetConsumerOutTopic;
    private OMRSRepositoryHelper    repositoryHelper;
    private OMRSRepositoryValidator repositoryValidator;
    private String                  componentName;


    /**
     * The constructor is given the connection to the out topic for Asset Consumer OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetConsumerOutTopic - connection to the out topic
     * @param repositoryHelper - provides methods for working with metadata instances
     * @param repositoryValidator - provides validation of metadata instance
     * @param componentName - name of component
     */
    public AssetConsumerPublisher(Connection              assetConsumerOutTopic,
                                  OMRSRepositoryHelper    repositoryHelper,
                                  OMRSRepositoryValidator repositoryValidator,
                                  String                  componentName)
    {
        this.assetConsumerOutTopic = assetConsumerOutTopic;
        this.repositoryHelper = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.componentName = componentName;
    }


    /**
     * Determine whether a new entity is an Asset.  If it is then publish an Asset Consumer Event about it.
     *
     * @param entity - entity object that has just been created.
     */
    public void processNewEntity(EntityDetail entity)
    {
        String assetType = getAssetType(entity);

        if (assetType != null)
        {
            this.processNewAsset(this.getAsset(entity));
        }
    }


    /**
     * Determine whether an updated entity is an Asset.  If it is then publish an Asset Consumer Event about it.
     *
     * @param entity - entity object that has just been updated.
     */
    public void processUpdatedEntity(EntityDetail   entity)
    {
        String assetType = getAssetType(entity);

        if (assetType != null)
        {
            this.processUpdatedAsset(this.getAsset(entity));
        }
    }


    /**
     * Determine whether an updated entity is an Asset.  If it is then publish an Asset Consumer Event about it.
     *
     * @param originalEntity - original values for entity object - available when basic property updates have
     *                       occurred on the entity.
     * @param newEntity - entity object that has just been updated.
     */
    public void processUpdatedEntity(EntityDetail   originalEntity,
                                     EntityDetail   newEntity)
    {
        String assetType = getAssetType(newEntity);

        if (assetType != null)
        {
            this.processUpdatedAsset(this.getAsset(originalEntity),
                                     this.getAsset(newEntity));
        }
    }


    /**
     * Determine whether a deleted entity is an Asset.  If it is then publish an Asset Consumer Event about it.
     *
     * @param entity - entity object that has just been deleted.
     */
    public void processDeletedEntity(EntityDetail   entity)
    {
        String assetType = getAssetType(entity);

        if (assetType != null)
        {
            this.processDeletedAsset(this.getAsset(entity));
        }
    }


    /**
     * Determine whether a restored entity is an Asset.  If it is then publish an Asset Consumer Event about it.
     *
     * @param entity - entity object that has just been restored.
     */
    public void processRestoredEntity(EntityDetail   entity)
    {
        String assetType = getAssetType(entity);

        if (assetType != null)
        {
            this.processRestoredAsset(this.getAsset(entity));
        }
    }


    /**
     * Determine whether a new relationship is related to an Asset.
     * If it is then publish an Asset Consumer Event about it.
     *
     * @param relationship - relationship object that has just been created.
     */
    public void processNewRelationship(Relationship relationship)
    {
        // todo
    }


    /**
     * Determine whether an updated relationship is related to an Asset.
     * If it is then publish an Asset Consumer Event about it.
     *
     * @param relationship - relationship object that has just been updated.
     */
    public void processUpdatedRelationship(Relationship   relationship)
    {
        // todo
    }


    /**
     * Determine whether an updated relationship is related to an Asset.
     * If it is then publish an Asset Consumer Event about it.
     *
     * @param originalRelationship  - original values of relationship.
     * @param newRelationship - relationship object that has just been updated.
     */
    public void processUpdatedRelationship(Relationship   originalRelationship,
                                           Relationship   newRelationship)
    {
        // todo
    }


    /**
     * Determine whether a deleted relationship is related to an Asset.
     * If it is then publish an Asset Consumer Event about it.
     *
     * @param relationship - relationship object that has just been deleted.
     */
    public void processDeletedRelationship(Relationship   relationship)
    {
        // todo
    }


    /**
     * Determine whether a restored relationship is related to an Asset.
     * If it is then publish an Asset Consumer Event about it.
     *
     * @param relationship - relationship object that has just been restored.
     */
    public void processRestoredRelationship(Relationship   relationship)
    {
        // todo
    }


    /**
     * Return the name of the Asset type if this entity has a type that inherits from Asset.
     *
     * @param entity - entity to test
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
     * Return an Asset object extracted from the supplied entity object
     * @param entity - entity describing the asset
     * @return Asset object
     */
    private Asset getAsset(EntityDetail   entity)
    {
        Asset   asset = new Asset();

        if (entity != null)
        {
            asset.setURL(entity.getInstanceURL());
            asset.setGUID(entity.getGUID());

            InstanceType  instanceType = entity.getType();
            if (instanceType != null)
            {
                asset.setTypeId(instanceType.getTypeDefGUID());
                asset.setTypeName(instanceType.getTypeDefName());
                asset.setTypeVersion(instanceType.getTypeDefVersion());
                asset.setTypeDescription(instanceType.getTypeDefDescription());
            }

            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                InstancePropertyValue instancePropertyValue;

                instancePropertyValue = instanceProperties.getPropertyValue(assetPropertyNameQualifiedName);

                if (instancePropertyValue != null)
                {
                    PrimitivePropertyValue  primitivePropertyValue = (PrimitivePropertyValue)instancePropertyValue;
                    asset.setQualifiedName(primitivePropertyValue.toString());
                }

                instancePropertyValue = instanceProperties.getPropertyValue(assetPropertyNameDisplayName);

                if (instancePropertyValue != null)
                {
                    PrimitivePropertyValue  primitivePropertyValue = (PrimitivePropertyValue)instancePropertyValue;
                    asset.setDisplayName(primitivePropertyValue.toString());
                }

                instancePropertyValue = instanceProperties.getPropertyValue(assetPropertyNameOwner);

                if (instancePropertyValue != null)
                {
                    PrimitivePropertyValue  primitivePropertyValue = (PrimitivePropertyValue)instancePropertyValue;
                    asset.setOwner(primitivePropertyValue.toString());
                }

                instancePropertyValue = instanceProperties.getPropertyValue(assetPropertyNameDescription);

                if (instancePropertyValue != null)
                {
                    PrimitivePropertyValue  primitivePropertyValue = (PrimitivePropertyValue)instancePropertyValue;
                    asset.setDescription(primitivePropertyValue.toString());
                }
            }
        }

        return asset;
    }


    /**
     * Publish event about a new asset.
     *
     * @param asset - asset to report on.
     */
    private void processNewAsset(Asset   asset)
    {
        log.info("Asset Consumer Event => New Asset: " + asset.toString());
    }


    /**
     * Publish event about an updated asset.
     *
     * @param asset - asset to report on.
     */
    private void processUpdatedAsset(Asset   asset)
    {
        log.info("Asset Consumer Event => Updated Asset: " + asset.toString());
    }


    /**
     * Publish event about an updated asset.
     *
     * @param originalAsset - original values.
     * @param newAsset - asset to report on.
     */
    private void processUpdatedAsset(Asset   originalAsset,
                                     Asset   newAsset)
    {
        log.info("Asset Consumer Event => Original Asset: " + originalAsset.toString());
        log.info("Asset Consumer Event => Updated Asset: "  + newAsset.toString());
    }


    /**
     * Publish event about a deleted asset.
     *
     * @param asset - asset to report on.
     */
    private void processDeletedAsset(Asset   asset)
    {
        log.info("Asset Consumer Event => Deleted Asset: " + asset.toString());
    }


    /**
     * Publish event about a restored asset.
     *
     * @param asset - asset to report on.
     */
    private void processRestoredAsset(Asset   asset)
    {
        log.info("Asset Consumer Event => Restored Asset: " + asset.toString());
    }
}
