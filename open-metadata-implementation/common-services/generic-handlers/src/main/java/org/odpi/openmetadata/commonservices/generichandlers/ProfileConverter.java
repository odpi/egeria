/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ContactMethodElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ProfileElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.UserIdentityElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContactMethodProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * ProfileConverter generates a ProfileElement bean from a ActorProfileProperties entity.
 */
public class ProfileConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ProfileConverter(OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * a connection bean which made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted if they do not have any properties.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewComplexBean(Class<B>           beanClass,
                               EntityDetail       primaryEntity,
                               List<EntityDetail> supplementaryEntities,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ProfileElement bean)
            {
                ActorProfileProperties profileProperties = new ActorProfileProperties();

                if (primaryEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, primaryEntity.getClassifications(), methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    profileProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    profileProperties.setKnownName(this.removeName(instanceProperties));
                    profileProperties.setDescription(this.removeDescription(instanceProperties));
                    profileProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    profileProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    profileProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setProfileProperties(profileProperties);

                    if (supplementaryEntities != null)
                    {
                        List<UserIdentityElement>  userIdentities = new ArrayList<>();
                        List<ContactMethodElement> contactMethods = new ArrayList<>();

                        for (EntityDetail entity : supplementaryEntities)
                        {
                            if ((entity != null) && (entity.getType() != null))
                            {
                                String entityTypeName = entity.getType().getTypeDefName();

                                if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataType.USER_IDENTITY.typeName))
                                {
                                    UserIdentityElement    userBean       = new UserIdentityElement();
                                    UserIdentityProperties userProperties = new UserIdentityProperties();

                                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, entity.getClassifications(), methodName));

                                    InstanceProperties entityProperties = new InstanceProperties(entity.getProperties());

                                    userProperties.setQualifiedName(this.removeQualifiedName(entityProperties));
                                    userProperties.setUserId(this.removeUserId(entityProperties));
                                    userProperties.setDistinguishedName(this.removeDistinguishedName(entityProperties));
                                    userProperties.setAdditionalProperties(this.removeAdditionalProperties(entityProperties));

                                    userProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                                    userProperties.setExtendedProperties(this.getRemainingExtendedProperties(entityProperties));

                                    userBean.setProperties(userProperties);

                                    userIdentities.add(userBean);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataType.CONTACT_DETAILS.typeName))
                                {
                                    ContactMethodElement    contactMethodBean       = new ContactMethodElement();
                                    ContactMethodProperties contactMethodProperties = new ContactMethodProperties();

                                    contactMethodBean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, entity.getClassifications(), methodName));

                                    InstanceProperties entityProperties = new InstanceProperties(entity.getProperties());

                                    contactMethodProperties.setName(this.removeName(entityProperties));
                                    contactMethodProperties.setContactType(this.removeContactType(entityProperties));
                                    contactMethodProperties.setContactMethodType(this.getContactMethodTypeFromProperties(entityProperties));
                                    contactMethodProperties.setContactMethodService(this.removeContactMethodService(entityProperties));
                                    contactMethodProperties.setContactMethodValue(this.removeContactMethodValue(entityProperties));

                                    contactMethodProperties.setEffectiveFrom(entityProperties.getEffectiveFromTime());
                                    contactMethodProperties.setEffectiveTo(entityProperties.getEffectiveToTime());

                                    contactMethodProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                                    contactMethodProperties.setExtendedProperties(this.getRemainingExtendedProperties(entityProperties));

                                    contactMethodBean.setProperties(contactMethodProperties);

                                    contactMethods.add(contactMethodBean);
                                }
                            }
                            else
                            {
                                handleBadEntity(beanClass.getName(), entity, methodName);
                            }
                        }

                        if (! userIdentities.isEmpty())
                        {
                            bean.setUserIdentities(userIdentities);
                        }

                        if (! contactMethods.isEmpty())
                        {
                            bean.setContactMethods(contactMethods);
                        }
                    }
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
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
