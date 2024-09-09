/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
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
 * ActorProfileConverter generates a ActorProfileElement bean from a ActorProfile entity.
 */
public class ActorProfileConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ActorProfileConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof ActorProfileElement bean)
            {
                ActorProfileProperties profileProperties = new ActorProfileProperties();

                if (primaryEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    profileProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    profileProperties.setKnownName(this.removeName(instanceProperties));
                    profileProperties.setDescription(this.removeDescription(instanceProperties));
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

                                if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataType.USER_IDENTITY.typeName))
                                {
                                    UserIdentityElement    userBean       = new UserIdentityElement();
                                    UserIdentityProperties userProperties = new UserIdentityProperties();

                                    userBean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

                                    InstanceProperties entityProperties = new InstanceProperties(entity.getProperties());

                                    userProperties.setQualifiedName(this.removeQualifiedName(entityProperties));
                                    userProperties.setUserId(this.removeUserId(entityProperties));
                                    userProperties.setDistinguishedName(this.removeDistinguishedName(entityProperties));
                                    userProperties.setAdditionalProperties(this.removeAdditionalProperties(entityProperties));

                                    userProperties.setEffectiveFrom(entityProperties.getEffectiveFromTime());
                                    userProperties.setEffectiveTo(entityProperties.getEffectiveToTime());

                                    userProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                                    userProperties.setExtendedProperties(this.getRemainingExtendedProperties(entityProperties));

                                    userBean.setProperties(userProperties);

                                    userIdentities.put(entity.getGUID(), userBean);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataType.CONTRIBUTION_RECORD_TYPE_NAME))
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
                                else if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataType.CONTACT_DETAILS.typeName))
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
                        List<ElementStub> peers     = new ArrayList<>();
                        ElementStub       superTeam = null;
                        List<ElementStub>            subTeams             = new ArrayList<>();
                        List<ElementStub>            teamLeaders          = new ArrayList<>();
                        List<ElementStub>            teamMembers          = new ArrayList<>();
                        List<ProfileIdentityElement> profileIdentities    = new ArrayList<>();
                        List<ProfileLocationElement> locations            = new ArrayList<>();
                        List<ElementStub>            roles                = new ArrayList<>();
                        List<RelatedElement>         businessCapabilities   = new ArrayList<>();
                        List<ElementStub>            linkedInfrastructure = new ArrayList<>();

                        for (Relationship relationship : relationships)
                        {
                            if ((relationship != null) && (relationship.getType() != null))
                            {
                                String relationshipTypeName = relationship.getType().getTypeDefName();

                                if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataType.PEER_RELATIONSHIP_TYPE_NAME))
                                {
                                    EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);

                                    ElementStub elementStub = super.getElementStub(beanClass, entityProxy, methodName);

                                    peers.add(elementStub);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataType.TEAM_MEMBERSHIP_RELATIONSHIP_TYPE_NAME))
                                {
                                    EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);

                                    ElementStub elementStub = super.getElementStub(beanClass, entityProxy, methodName);

                                    teamMembers.add(elementStub);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataType.TEAM_LEADERSHIP_RELATIONSHIP_TYPE_NAME))
                                {
                                    EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);

                                    ElementStub elementStub = super.getElementStub(beanClass, entityProxy, methodName);

                                    teamLeaders.add(elementStub);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP_TYPE_NAME))
                                {
                                    EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);

                                    ElementStub elementStub = super.getElementStub(beanClass, entityProxy, methodName);

                                    roles.add(elementStub);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataType.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP_TYPE_NAME))
                                {
                                    EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);

                                    ElementStub elementStub = super.getElementStub(beanClass, entityProxy, methodName);

                                    linkedInfrastructure.add(elementStub);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataType.ORGANIZATIONAL_CAPABILITY_TYPE_NAME))
                                {
                                    EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);

                                    RelatedElement relatedElement = super.getRelatedElement(beanClass, relationship, entityProxy, methodName);

                                    businessCapabilities.add(relatedElement);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataType.PROFILE_LOCATION_RELATIONSHIP.typeName))
                                {
                                    EntityProxy entityProxy = relationship.getEntityTwoProxy();

                                    ElementStub elementStub = super.getElementStub(beanClass, entityProxy, methodName);

                                    ProfileLocationElement    locationElement    = new ProfileLocationElement();
                                    ProfileLocationProperties locationProperties = new ProfileLocationProperties();

                                    locationProperties.setAssociationType(this.removeAssociationType(relationship.getProperties()));

                                    locationElement.setLocation(elementStub);
                                    locationElement.setProperties(locationProperties);
                                    locations.add(locationElement);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName))
                                {
                                    EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);

                                    ProfileIdentityElement    profileIdentityElement    = new ProfileIdentityElement();
                                    ProfileIdentityProperties profileIdentityProperties = new ProfileIdentityProperties();

                                    InstanceProperties relationshipProperties = relationship.getProperties();

                                    profileIdentityProperties.setRoleTypeName(this.removeDescription(relationshipProperties));
                                    profileIdentityProperties.setRoleGUID(this.removeDescription(relationshipProperties));
                                    profileIdentityProperties.setDescription(this.removeDescription(relationshipProperties));

                                    profileIdentityElement.setProfileIdentity(profileIdentityProperties);
                                    profileIdentityElement.setUserIdentity(userIdentities.get(entityProxy.getGUID()));

                                    profileIdentities.add(profileIdentityElement);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP_TYPE_NAME))
                                {
                                    EntityProxy entityProxy = relationship.getEntityOneProxy();

                                    if (primaryEntity.getGUID().equals(entityProxy.getGUID()))
                                    {
                                        /*
                                         * The primary entity is the super team - save subteam
                                         */
                                        ElementStub elementStub = super.getElementStub(beanClass, relationship.getEntityTwoProxy(), methodName);
                                        subTeams.add(elementStub);
                                    }
                                    else
                                    {
                                        superTeam = super.getElementStub(beanClass, entityProxy, methodName);
                                    }
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

                        bean.setSuperTeam(superTeam);
                        if (! subTeams.isEmpty())
                        {
                            bean.setSubTeams(subTeams);
                        }

                        if (! peers.isEmpty())
                        {
                            bean.setPeers(peers);
                        }

                        if (! teamLeaders.isEmpty())
                        {
                            bean.setTeamLeaderRoles(teamLeaders);
                        }

                        if (! teamMembers.isEmpty())
                        {
                            bean.setTeamMemberRoles(teamMembers);
                        }

                        if (! roles.isEmpty())
                        {
                            bean.setPersonRoles(roles);
                        }

                        if (! businessCapabilities.isEmpty())
                        {
                            bean.setBusinessCapability(businessCapabilities);
                        }

                        if (! locations.isEmpty())
                        {
                            bean.setLocations(locations);
                        }

                        if (! linkedInfrastructure.isEmpty())
                        {
                            bean.setLinkedInfrastructure(linkedInfrastructure);
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
