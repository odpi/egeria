/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.odpi.openmetadata.accessservices.subjectarea.assetconsumer.properties.Asset;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;


/**
 * SubjectAreaPublisher is responsible for publishing org.odpi.openmetadata.accessservices.subjectarea.common.events about glossary artifacts.  It is called
 * when an interesting OMRS Event is added to the Enterprise OMRS Topic.  It adds org.odpi.openmetadata.accessservices.subjectarea.common.events to the Subject Area OMAS
 * out topic.
 */
public class SubjectAreaProducer
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaProducer.class);

    //    private Connection assetConsumerOutTopic;
    private OMRSRepositoryHelper repositoryHelper;
    private OMRSRepositoryValidator repositoryValidator;
    private String                  componentName;


    /*
     *
     * @param subjectAreaOutTopic - connection to the out topic
     * @param repositoryHelper - provides methods for working with metadata instances
     * @param repositoryValidator - provides validation of metadata instance
     * @param componentName - name of component
     */
    public SubjectAreaProducer(
//            Connection                 subjectAreaOutTopic,
            OMRSRepositoryHelper repositoryHelper,
            OMRSRepositoryValidator repositoryValidator,
            String                  componentName)
    {
//        this.assetConsumerOutTopic = assetConsumerOutTopic;
        this.repositoryHelper = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.componentName = componentName;
    }


    /**
     * Determine whether a new entity is a type we are interested in.  If it is then publish a subject area Event relating to it.
     * We are interested in any entity that is connected to glossary, glossaryCategory or glossaryTerm (including via Referencable
     * relationships) via a relationship.
     *
     * If there is a new comment associated with a Node then have an event for
     * Node changes
     * One for associated entities passing through the glossary term identifier.
     *
     * @param entity - entity object that has just been created.
     */
    public void processNewEntity(EntityDetail entity)
    {
        // TODO
    }


    /**
     *
     * @param entity - entity object that has just been updated.
     */
    public void processUpdatedEntity(EntityDetail   entity)
    {
        // TODO
    }


    /**
     *
     * @param entity - entity object that has just been deleted.
     */
    public void processDeletedEntity(EntityDetail   entity)
    {
        // TODO
    }


    /**
     *
     * @param entity - entity object that has just been restored.
     */
    public void processRestoredEntity(EntityDetail   entity)
    {
        // TODO
    }


    /**
     *
     * @param relationship - relationship object that has just been created.
     */
    public void processNewRelationship(Relationship relationship)
    {
        // TODO
    }


    /**
     *
     * @param relationship - relationship object that has just been updated.
     */
    public void processUpdatedRelationship(Relationship   relationship)
    {
        // TODO
    }


    /**
     *
     * @param relationship - relationship object that has just been deleted.
     */
    public void processDeletedRelationship(Relationship   relationship)
    {
        // todo
    }


    /**
     *
     * @param relationship - relationship object that has just been restored.
     */
    public void processRestoredRelationship(Relationship   relationship)
    {
        // todo
    }


//    /**
//     * Return the name of the Asset type if this entity has a type that inherits from Asset.
//     *
//     * @param entity - entity to test
//     * @return String containing Asset type name, or null if not an Asset.
//     */
//    private String getAssetType(EntityDetail  entity)
//    {
//        final   String   methodName = "getAssetType";
//
//        if (repositoryValidator.isATypeOf(componentName, entity, assetTypeName, methodName))
//        {
//            InstanceType entityType = entity.getType();
//
//            if (entityType != null)
//            {
//                return entityType.getTypeDefName();
//            }
//        }
//
//        return null;
//    }


    /**
     * Return an Asset object extracted from the supplied entity object
     * @param entity - entity describing the asset
     * @return Asset object
     */
//    private Asset getAsset(EntityDetail   entity)
//    {
//        Asset   asset = new Asset();
//
//        if (entity != null)
//        {
//            asset.setURL(entity.getInstanceURL());
//            asset.setGUID(entity.getGUID());
//
//            InstanceType  instanceType = entity.getType();
//            if (instanceType != null)
//            {
//                asset.setTypeId(instanceType.getTypeDefGUID());
//                asset.setTypeName(instanceType.getTypeDefName());
//                asset.setTypeVersion(instanceType.getTypeDefVersion());
//                asset.setTypeDescription(instanceType.getTypeDefDescription());
//            }
//
//            InstanceProperties instanceProperties = entity.getProperties();
//
//            if (instanceProperties != null)
//            {
//                InstancePropertyValue instancePropertyValue;
//
//                instancePropertyValue = instanceProperties.getPropertyValue(assetPropertyNameQualifiedName);
//
//                if (instancePropertyValue != null)
//                {
//                    PrimitivePropertyValue  primitivePropertyValue = (PrimitivePropertyValue)instancePropertyValue;
//                    asset.setQualifiedName(primitivePropertyValue.toString());
//                }
//
//                instancePropertyValue = instanceProperties.getPropertyValue(assetPropertyNameDisplayName);
//
//                if (instancePropertyValue != null)
//                {
//                    PrimitivePropertyValue  primitivePropertyValue = (PrimitivePropertyValue)instancePropertyValue;
//                    asset.setDisplayName(primitivePropertyValue.toString());
//                }
//
//                instancePropertyValue = instanceProperties.getPropertyValue(assetPropertyNameOwner);
//
//                if (instancePropertyValue != null)
//                {
//                    PrimitivePropertyValue  primitivePropertyValue = (PrimitivePropertyValue)instancePropertyValue;
//                    asset.setOwner(primitivePropertyValue.toString());
//                }
//
//                instancePropertyValue = instanceProperties.getPropertyValue(assetPropertyNameDescription);
//
//                if (instancePropertyValue != null)
//                {
//                    PrimitivePropertyValue  primitivePropertyValue = (PrimitivePropertyValue)instancePropertyValue;
//                    asset.setDescription(primitivePropertyValue.toString());
//                }
//            }
//        }
//
//        return asset;
//    }


    /**
     * Publish event about a new asset.
     *
     * @param asset - asset to report on.
     */
//    private void processNewAsset(Asset   asset)
//    {
//        log.info("Asset Consumer Event => New Asset: " + asset.toString());
//    }


    /**
     * Publish event about an updated asset.
     *
     * @param asset - asset to report on.
     */
//    private void processUpdatedAsset(Asset   asset)
//    {
//        log.info("Asset Consumer Event => Updated Asset: " + asset.toString());
//    }


    /**
     * Publish event about a deleted asset.
     *
     * @param asset - asset to report on.
     */
//    private void processDeletedAsset(Asset   asset)
//    {
//        log.info("Asset Consumer Event => Deleted Asset: " + asset.toString());
//    }


    /**
     * Publish event about a restored asset.
     *
     * @param asset - asset to report on.
     */
//    private void processRestoredAsset(Asset   asset)
//    {
//        log.info("Asset Consumer Event => Restored Asset: " + asset.toString());
//    }
}

