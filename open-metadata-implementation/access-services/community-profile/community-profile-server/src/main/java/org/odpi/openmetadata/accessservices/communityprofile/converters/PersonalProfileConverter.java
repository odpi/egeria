/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.*;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethodProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContributionRecord;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfileProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ProfileIdentityProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentityProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PersonalProfileConverter generates a PersonalProfileProperties bean from a PersonalProfileProperties entity.
 */
public class PersonalProfileConverter<B> extends CommunityProfileOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public PersonalProfileConverter(OMRSRepositoryHelper repositoryHelper,
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
     * @param primaryEntity entity that is the root of the cluster of entities that make up the content of the bean
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

            if (returnBean instanceof PersonalProfileUniverse)
            {
                PersonalProfileUniverse   bean              = (PersonalProfileUniverse) returnBean;
                PersonalProfileProperties profileProperties = new PersonalProfileProperties();

                if (primaryEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    profileProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    profileProperties.setKnownName(this.removeName(instanceProperties));
                    profileProperties.setPronouns(this.removePronouns(instanceProperties));
                    profileProperties.setDescription(this.removeDescription(instanceProperties));
                    profileProperties.setTitle(this.removeTitle(instanceProperties));
                    profileProperties.setInitials(this.removeInitials(instanceProperties));
                    profileProperties.setGivenNames(this.removeGivenNames(instanceProperties));
                    profileProperties.setSurname(this.removeSurname(instanceProperties));
                    profileProperties.setFullName(this.removeFullName(instanceProperties));
                    profileProperties.setPreferredLanguage(this.removePreferredLanguage(instanceProperties));
                    profileProperties.setJobTitle(this.removeJobTitle(instanceProperties));
                    profileProperties.setEmployeeNumber(this.removeEmployeeNumber(instanceProperties));
                    profileProperties.setEmployeeType(this.removeEmployeeType(instanceProperties));
                    profileProperties.setIsPublic(this.removeIsPublic(instanceProperties));
                    profileProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    profileProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
                    profileProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    profileProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    profileProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setProfileProperties(profileProperties);

                    Map<String, UserIdentityElement> userIdentities = new HashMap<>();

                    if (supplementaryEntities != null)
                    {
                        List<ContactMethodElement> contactMethods = new ArrayList<>();

                        for (EntityDetail entity : supplementaryEntities)
                        {
                            if ((entity != null) && (entity.getType() != null))
                            {
                                String entityTypeName = entity.getType().getTypeDefName();

                                if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME))
                                {
                                    UserIdentityElement    userBean = new UserIdentityElement();
                                    UserIdentityProperties userProperties = new UserIdentityProperties();

                                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

                                    InstanceProperties entityProperties = new InstanceProperties(entity.getProperties());

                                    userProperties.setQualifiedName(this.removeQualifiedName(entityProperties));
                                    userProperties.setUserId(this.removeUserId(instanceProperties));
                                    userProperties.setDistinguishedName(this.removeDistinguishedName(instanceProperties));
                                    userProperties.setAdditionalProperties(this.removeAdditionalProperties(entityProperties));

                                    userProperties.setEffectiveFrom(entityProperties.getEffectiveFromTime());
                                    userProperties.setEffectiveTo(entityProperties.getEffectiveToTime());

                                    userProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                                    userProperties.setExtendedProperties(this.getRemainingExtendedProperties(entityProperties));

                                    userBean.setProperties(userProperties);

                                    userIdentities.put(entity.getGUID(), userBean);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.CONTRIBUTION_RECORD_TYPE_NAME))
                                {
                                    ContributionRecordElement contributionBean   = new ContributionRecordElement();
                                    ContributionRecord        contributionRecord = new ContributionRecord();

                                    contributionBean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                                    InstanceProperties entityProperties = new InstanceProperties(entity.getProperties());

                                    contributionRecord.setQualifiedName(this.removeQualifiedName(entityProperties));
                                    contributionRecord.setAdditionalProperties(this.removeAdditionalProperties(entityProperties));
                                    contributionRecord.setKarmaPoints(this.removeKarmaPoints(entityProperties));
                                    if ((contributionRecord.getKarmaPoints() > 0) && (karmaPointPlateau > 0))
                                    {
                                        contributionRecord.setKarmaPointPlateau(contributionRecord.getKarmaPoints() / karmaPointPlateau);
                                    }
                                    contributionRecord.setIsPublic(this.removeIsPublic(entityProperties));
                                    contributionRecord.setTypeName(bean.getElementHeader().getType().getTypeName());
                                    contributionRecord.setExtendedProperties(this.getRemainingExtendedProperties(entityProperties));

                                    contributionBean.setProperties(contributionRecord);

                                    bean.setContributionRecord(contributionBean);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.CONTACT_DETAILS_TYPE_NAME))
                                {
                                    ContactMethodElement    contactMethodBean       = new ContactMethodElement();
                                    ContactMethodProperties contactMethodProperties = new ContactMethodProperties();

                                    contactMethodBean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

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

                        if (! contactMethods.isEmpty())
                        {
                            bean.setContactMethods(contactMethods);
                        }
                    }

                    if (relationships != null)
                    {
                        List<ElementStub>            peers = new ArrayList<>();
                        List<ElementStub>            roles = new ArrayList<>();
                        List<ProfileIdentityElement> profileIdentities = new ArrayList<>();

                        for (Relationship relationship : relationships)
                        {
                            if ((relationship != null) && (relationship.getType() != null))
                            {
                                String relationshipTypeName = relationship.getType().getTypeDefName();

                                if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataAPIMapper.PEER_RELATIONSHIP_TYPE_NAME))
                                {
                                    EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);

                                    ElementStub elementStub = super.getElementStub(beanClass, entityProxy, methodName);

                                    peers.add(elementStub);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataAPIMapper.PERSON_ROLE_APPOINTMENT_RELATIONSHIP_TYPE_NAME))
                                {
                                    EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);

                                    ElementStub elementStub = super.getElementStub(beanClass, entityProxy, methodName);

                                    roles.add(elementStub);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataAPIMapper.PROFILE_IDENTITY_RELATIONSHIP_TYPE_NAME))
                                {
                                    EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);

                                    ProfileIdentityElement    profileIdentityElement    = new ProfileIdentityElement();
                                    ProfileIdentityProperties profileIdentityProperties = new ProfileIdentityProperties();

                                    InstanceProperties relationshipProperties = relationship.getProperties();

                                    profileIdentityProperties.setRoleTypeName(this.removeDescription(relationshipProperties));
                                    profileIdentityProperties.setRoleGUID(this.removeDescription(relationshipProperties));
                                    profileIdentityProperties.setDescription(this.removeDescription(relationshipProperties));

                                    profileIdentityElement.setProfileIdentity(profileIdentityProperties);
                                    profileIdentityElement.setProperties(userIdentities.get(entityProxy.getGUID()));

                                    profileIdentities.add(profileIdentityElement);
                                }
                            }
                            else
                            {
                                handleBadRelationship(beanClass.getName(), relationship, methodName);
                            }
                        }

                        if (!profileIdentities.isEmpty())
                        {
                            bean.setUserIdentities(profileIdentities);
                        }

                        if (! peers.isEmpty())
                        {
                            bean.setPeers(peers);
                        }

                        if (! roles.isEmpty())
                        {
                            bean.setRoles(roles);
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
