/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OMFConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.RelatedAsset;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;



/**
 * RelatedAssetConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Referenceable bean.
 */
public class RelatedAssetConverter<B> extends OMFConverter<B>
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

            if (returnBean instanceof RelatedAsset bean)
            {
                /*
                 * Check that the entity is of the correct type.
                 */
                this.setUpElementHeader(bean, entity, OpenMetadataType.ASSET.typeName, methodName);

                /*
                 * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
                 * properties, leaving any subclass properties to be stored in extended properties.
                 */
                InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                bean.setResourceName(this.removeName(instanceProperties));
                bean.setVersionIdentifier(this.removeVersionIdentifier(instanceProperties));
                bean.setResourceDescription(this.removeDescription(instanceProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                if (relationship != null)
                {
                    bean.setRelationshipName(relationship.getType().getTypeDefName());

                    boolean relatedAssetAtEndOne = true;

                    if (relationship.getEntityTwoProxy().getGUID().equals(entity.getGUID()))
                    {
                        relatedAssetAtEndOne = false;
                    }

                    TypeDef typeDef = repositoryHelper.getTypeDefByName(methodName, relationship.getType().getTypeDefName());

                    if (typeDef instanceof RelationshipDef relationshipDef)
                    {
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
