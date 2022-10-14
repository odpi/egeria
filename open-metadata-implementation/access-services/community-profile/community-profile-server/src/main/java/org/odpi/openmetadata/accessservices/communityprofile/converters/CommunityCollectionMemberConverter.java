/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;

import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.CommunityCollectionMember;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CommunityProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * CommunityCollectionMemberConverter generates an CommunityCollectionMember bean from an CommunityProperties entity
 * and a CollectionMembership relationship to it.
 */
public class CommunityCollectionMemberConverter<B> extends CommunityProfileOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public CommunityCollectionMemberConverter(OMRSRepositoryHelper repositoryHelper,
                                              String               serviceName,
                                              String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
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
    @Override
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

            if (returnBean instanceof CommunityCollectionMember)
            {
                CommunityCollectionMember bean                = (CommunityCollectionMember) returnBean;
                CommunityProperties       communityProperties = new CommunityProperties();

                InstanceProperties instanceProperties;

                /*
                 * The initial set of values come from the entity.
                 */
                if (entity != null)
                {
                    bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                    instanceProperties = new InstanceProperties(entity.getProperties());

                    communityProperties.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                    communityProperties.setName(repositoryHelper.removeStringProperty(serviceName, OpenMetadataAPIMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                    communityProperties.setDescription(repositoryHelper.removeStringProperty(serviceName, OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                    communityProperties.setMission(repositoryHelper.removeStringProperty(serviceName, OpenMetadataAPIMapper.MISSION_PROPERTY_NAME, instanceProperties, methodName));
                    communityProperties.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, OpenMetadataAPIMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                    communityProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
                    communityProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());

                    communityProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    communityProperties.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }

                if (relationship != null)
                {
                    instanceProperties = new InstanceProperties(relationship.getProperties());

                    bean.setMembershipRationale(this.removeMembershipRationale(instanceProperties));
                    bean.setDateAddedToCollection(relationship.getCreateTime());
                }

                bean.setProperties(communityProperties);
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
