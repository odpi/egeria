/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.converters;

import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.ContactMethodElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.ProfileElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.UserIdentityElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ActorProfileProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ContactMethodProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ContactMethodType;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.UserIdentityProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
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
public class ProfileConverter<B> extends GovernanceProgramOMASConverter<B>
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

            if (returnBean instanceof ProfileElement)
            {
                ProfileElement         bean                    = (ProfileElement) returnBean;
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

                                if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME))
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
                                else if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.CONTACT_DETAILS_TYPE_NAME))
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


    /**
     * Retrieve the ContactMethodType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return ContactMethodType  enum value
     */
    private ContactMethodType getContactMethodTypeFromProperties(InstanceProperties   properties)
    {
        final String methodName = "getContactMethodTypeFromProperties";

        ContactMethodType contactMethodType = ContactMethodType.OTHER;

        if (properties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName, OpenMetadataAPIMapper.CONTACT_METHOD_TYPE_PROPERTY_NAME, properties, methodName);

            switch (ordinal)
            {
                case 0:
                    contactMethodType = ContactMethodType.EMAIL;
                    break;

                case 1:
                    contactMethodType = ContactMethodType.PHONE;
                    break;

                case 2:
                    contactMethodType = ContactMethodType.CHAT;
                    break;

                case 3:
                    contactMethodType = ContactMethodType.PROFILE;
                    break;

                case 4:
                    contactMethodType = ContactMethodType.ACCOUNT;
                    break;

                case 99:
                    contactMethodType = ContactMethodType.OTHER;
                    break;
            }
        }

        return contactMethodType;
    }
}
