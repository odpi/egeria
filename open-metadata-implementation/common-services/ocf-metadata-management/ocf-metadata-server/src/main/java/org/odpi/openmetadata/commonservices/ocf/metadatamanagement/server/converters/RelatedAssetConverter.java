/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.server.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OCFConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.RelatedAsset;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


/**
 * RelatedAssetConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Referenceable bean.
 */
public class RelatedAssetConverter<B> extends OCFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public RelatedAssetConverter(OMRSRepositoryHelper repositoryHelper,
                                 String               serviceName,
                                 String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied entity, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the entity supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        return this.getNewBean(beanClass, entity, null, methodName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof RelatedAsset)
            {
                RelatedAsset bean = (RelatedAsset) returnBean;

                /*
                 * Check that the entity is of the correct type.
                 */
                this.setUpElementHeader(bean, entity, OpenMetadataAPIMapper.ASSET_TYPE_NAME, methodName);

                /*
                 * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
                 * properties, leaving any subclass properties to be stored in extended properties.
                 */
                InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                bean.setName(this.removeName(instanceProperties));
                bean.setVersionIdentifier(this.removeVersionIdentifier(instanceProperties));
                bean.setDescription(this.removeDescription(instanceProperties));

                /* Note this value should be in the classification */
                bean.setOwner(this.removeOwner(instanceProperties));
                /* Note this value should be in the classification */
                bean.setOwnerType(this.removeOwnerTypeFromProperties(instanceProperties));
                /* Note this value should be in the classification */
                bean.setZoneMembership(this.removeZoneMembership(instanceProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                /*
                 * The values in the classifications override the values in the main properties of the Asset's entity.
                 * Having these properties in the main entity is deprecated.
                 */
                instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME, entity);

                bean.setZoneMembership(this.getZoneMembership(instanceProperties));

                instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME, entity);

                bean.setOwner(this.getOwner(instanceProperties));
                bean.setOwnerType(this.getOwnerTypeFromProperties(instanceProperties));

                instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME, entity);

                Map<String, String> originMap = this.getOtherOriginValues(instanceProperties);

                String orgOriginValue = this.getOriginOrganizationGUID(instanceProperties);
                String bizOriginValue = this.getOriginBusinessCapabilityGUID(instanceProperties);

                if ((orgOriginValue != null) || (bizOriginValue != null))
                {
                    if (originMap == null)
                    {
                        originMap = new HashMap<>();
                    }

                    if (orgOriginValue != null)
                    {
                        originMap.put(OpenMetadataAPIMapper.ORGANIZATION_PROPERTY_NAME, orgOriginValue);
                    }

                    if (bizOriginValue != null)
                    {
                        originMap.put(OpenMetadataAPIMapper.BUSINESS_CAPABILITY_PROPERTY_NAME, bizOriginValue);
                    }
                }

                bean.setAssetOrigin(originMap);

                // todo set up SecurityTags and the governance classifications - needs some common methods with the AssetConverter


                if (relationship != null)
                {
                    bean.setRelationshipName(relationship.getType().getTypeDefName());

                    boolean relatedAssetAtEndOne = true;

                    if (relationship.getEntityTwoProxy().getGUID().equals(entity.getGUID()))
                    {
                        relatedAssetAtEndOne = false;
                    }

                    TypeDef typeDef = repositoryHelper.getTypeDefByName(methodName, relationship.getType().getTypeDefName());

                    if (typeDef instanceof RelationshipDef)
                    {
                        RelationshipDef relationshipDef = (RelationshipDef)typeDef;

                        RelationshipEndDef endDef;

                        if (relatedAssetAtEndOne)
                        {
                            endDef = relationshipDef.getEndDef1();
                        }
                        else
                        {
                            endDef = relationshipDef.getEndDef2();
                        }

                        bean.setAttributeName(endDef.getAttributeName());
                    }
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
