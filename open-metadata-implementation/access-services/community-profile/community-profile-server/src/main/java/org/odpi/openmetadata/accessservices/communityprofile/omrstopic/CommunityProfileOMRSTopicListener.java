/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.omrstopic;

import org.odpi.openmetadata.accessservices.communityprofile.converters.CommunityProfileOMASConverter;
import org.odpi.openmetadata.accessservices.communityprofile.events.CommunityProfileOutboundEventType;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileAuditCode;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ContributionRecordElement;
import org.odpi.openmetadata.accessservices.communityprofile.outtopic.CommunityProfileOutTopicPublisher;
import org.odpi.openmetadata.accessservices.communityprofile.server.CommunityProfileServicesInstance;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * CommunityProfileOMRSTopicListener examines each OMRS Topic event to (1) determine if karma points should
 * be awarded to an individual who has contributed to open metadata (2) determine if the event relates to
 * metadata instances that should result in a Community Profile OMAS event on its Out Topic.
 * If karma points need to be awarded, it calls the ContributionRecordHandler to update the person's
 * contribution record.  If an event needs to be sent, it calls the CommunityProfileOutTopicPublisher.
 */
public class CommunityProfileOMRSTopicListener extends OMRSTopicListenerBase
{
    private static final Logger log = LoggerFactory.getLogger(CommunityProfileOMRSTopicListener.class);

    private final CommunityProfileOutTopicPublisher          publisher;
    private final OMRSRepositoryHelper                       repositoryHelper;
    private final int                                        karmaPointIncrement;
    private final int                                        karmaPointPlateauThreshold;
    private final String                                     serverUserId;
    private final CommunityProfileOMASConverter<ElementStub> converter;
    private final CommunityProfileServicesInstance           instance;


    /**
     * The constructor is given the connection to the out topic for Community Profile OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param karmaPointIncrement increment for each personal contribution
     * @param karmaPointPlateauThreshold number of karma points in a plateau
     * @param publisher object that can set events to listening destinations
     * @param serverUserId userId for requests originated by this server
     * @param auditLog  logging destination
     * @param repositoryHelper repository helper
     * @param serviceName  name of this component
     * @param serverName  name of this server instance
     * @param instance handlers for this server instance
     */
    public CommunityProfileOMRSTopicListener(int                               karmaPointIncrement,
                                             int                               karmaPointPlateauThreshold,
                                             CommunityProfileOutTopicPublisher publisher,
                                             String                            serverUserId,
                                             AuditLog                          auditLog,
                                             OMRSRepositoryHelper              repositoryHelper,
                                             String                            serviceName,
                                             String                            serverName,
                                             CommunityProfileServicesInstance  instance)
    {
        super(serviceName, auditLog);

        this.repositoryHelper = repositoryHelper;
        this.publisher = publisher;
        this.karmaPointIncrement = karmaPointIncrement;
        this.karmaPointPlateauThreshold = karmaPointPlateauThreshold;
        this.serverUserId = serverUserId;

        this.converter = new CommunityProfileOMASConverter<>(repositoryHelper, serviceName, serverName);
        this.instance = instance;
    }


    /**
     * Increment the karma points for an individual if this OMAS is configured to collect karma points.
     *
     * @param contribution open metadata element that has changed
     */
    private void awardKarmaPoints(InstanceHeader contribution)
    {
        final String methodName = "awardKarmaPoints";
        final String userParameterName = "contribution.getUpdatedBy";

        if (karmaPointIncrement > 0)
        {
            if (contribution != null)
            {
                String contributingUserId = contribution.getUpdatedBy();

                if (contributingUserId == null)
                {
                    contributingUserId = contribution.getCreatedBy();
                }

                try
                {
                    ActorProfileElement personalProfile = instance.getActorProfileHandler().getActorProfileForUser(serverUserId,
                                                                                                                   contributingUserId,
                                                                                                                   userParameterName,
                                                                                                                   OpenMetadataType.PERSON_TYPE_NAME,
                                                                                                                   false,
                                                                                                                   false,
                                                                                                                   new Date(),
                                                                                                                   methodName);

                    if (personalProfile != null)
                    {
                        final String profileGUIDParameterName = "personalProfiles.get(0).getElementHeader().getGUID()";

                        ContributionRecordElement contributionRecord = personalProfile.getContributionRecord();

                        long currentPoints = 0;
                        boolean isPublic = false;

                        if ((contributionRecord != null) && (contributionRecord.getProperties() != null))
                        {
                            currentPoints = contributionRecord.getProperties().getKarmaPoints();
                            isPublic = contributionRecord.getProperties().getIsPublic();
                        }

                        long newPoints = currentPoints + karmaPointIncrement;

                        instance.getContributionRecordHandler().saveContributionRecord(serverUserId,
                                                                                       personalProfile.getElementHeader().getGUID(),
                                                                                       profileGUIDParameterName,
                                                                                       personalProfile.getProfileProperties().getQualifiedName(),
                                                                                       newPoints,
                                                                                       false,
                                                                                       null,
                                                                                       null,
                                                                                       null,
                                                                                       true,
                                                                                       false,
                                                                                       false,
                                                                                       new Date(),
                                                                                       methodName);

                        if (karmaPointPlateauThreshold != 0)
                        {
                            long currentPlateau = currentPoints / karmaPointPlateauThreshold;
                            long newPlateau     = newPoints / karmaPointPlateauThreshold;


                            log.debug("Karma points updated: " + contributingUserId);

                            if (newPlateau > currentPlateau)
                            {
                                if (isPublic)
                                {
                                    auditLog.logMessage(methodName,
                                                        CommunityProfileAuditCode.KARMA_PLATEAU_AWARD.getMessageDefinition(contributingUserId,
                                                                                                                           Long.toString(newPlateau),
                                                                                                                           Long.toString(newPoints)));
                                }

                                ElementStub elementStub = new ElementStub(personalProfile.getElementHeader());

                                elementStub.setUniqueName(personalProfile.getProfileProperties().getQualifiedName());
                                publisher.sendKarmaPointPlateauEvent(elementStub,
                                                                     contributingUserId,
                                                                     isPublic,
                                                                     newPlateau,
                                                                     newPoints);
                            }
                        }
                    }
                }
                catch (Exception  error)
                {
                    auditLog.logException(methodName,
                                          CommunityProfileAuditCode.KARMA_POINT_EXCEPTION.getMessageDefinition(contributingUserId,
                                                                                                               error.getClass().getName(),
                                                                                                               error.getMessage()),
                                          error);
                }
            }
        }
    }



    /**
     * An entity has been changed.
     *
     * @param eventType                      type of change to the entity
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the new entity
     * @param classificationName             potential classification change
     * @param methodName                     calling method
     */
    private void processEntityEvent(CommunityProfileOutboundEventType eventType,
                                    String                            sourceName,
                                    String                            originatorMetadataCollectionId,
                                    String                            originatorServerName,
                                    String                            originatorServerType,
                                    String                            originatorOrganizationName,
                                    EntityDetail                      entity,
                                    String                            classificationName,
                                    String                            methodName)
    {
        this.awardKarmaPoints(entity);

        String instanceTypeName = this.getInstanceTypeName(sourceName,
                                                           originatorMetadataCollectionId,
                                                           originatorServerName,
                                                           originatorServerType,
                                                           originatorOrganizationName,
                                                           entity,
                                                           methodName);

        if (instanceTypeName != null)
        {
            try
            {
                ElementStub elementStub = converter.getElementStub(ElementStub.class, entity, methodName);

                if ((repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.ACTOR_PROFILE_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.USER_IDENTITY_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.PERSON_ROLE_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.CONTACT_DETAILS_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.COMMUNITY_TYPE_NAME)))
                {

                    publisher.sendEntityEvent(eventType, entity.getGUID(), instanceTypeName, classificationName, elementStub);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      CommunityProfileAuditCode.OUTBOUND_EVENT_EXCEPTION.getMessageDefinition(entity.getGUID(),
                                                                                                              instanceTypeName,
                                                                                                              error.getClass().getName(),
                                                                                                              error.getMessage()),
                                      error);
            }
        }
    }




    /**
     * An entity has been changed - only the proxy is available.
     *
     * @param eventType                      type of change to the entity
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the new entity
     * @param classificationName             potential classification change
     * @param methodName                     calling method
     */
    private void processEntityEvent(CommunityProfileOutboundEventType eventType,
                                    String                            sourceName,
                                    String                            originatorMetadataCollectionId,
                                    String                            originatorServerName,
                                    String                            originatorServerType,
                                    String                            originatorOrganizationName,
                                    EntityProxy                       entity,
                                    String                            classificationName,
                                    String                            methodName)
    {
        this.awardKarmaPoints(entity);

        String instanceTypeName = this.getInstanceTypeName(sourceName,
                                                           originatorMetadataCollectionId,
                                                           originatorServerName,
                                                           originatorServerType,
                                                           originatorOrganizationName,
                                                           entity,
                                                           methodName);

        if (instanceTypeName != null)
        {
            try
            {
                ElementStub elementStub = converter.getElementStub(ElementStub.class, entity, methodName);

                if ((repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.ACTOR_PROFILE_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.USER_IDENTITY_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.PERSON_ROLE_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.CONTACT_DETAILS_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.COMMUNITY_TYPE_NAME)))
                {

                    publisher.sendEntityEvent(eventType, entity.getGUID(), instanceTypeName, classificationName, elementStub);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      CommunityProfileAuditCode.OUTBOUND_EVENT_EXCEPTION.getMessageDefinition(entity.getGUID(),
                                                                                                              instanceTypeName,
                                                                                                              error.getClass().getName(),
                                                                                                              error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * A relationship has changed.
     *
     * @param eventType                      type of change to the relationship
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   details of the changed relationship
     * @param methodName                     calling method
     */
    private void processRelationshipEvent(CommunityProfileOutboundEventType eventType,
                                          String                            sourceName,
                                          String                            originatorMetadataCollectionId,
                                          String                            originatorServerName,
                                          String                            originatorServerType,
                                          String                            originatorOrganizationName,
                                          Relationship                      relationship,
                                          String                            methodName)
    {
        this.awardKarmaPoints(relationship);

        String instanceTypeName = this.getInstanceTypeName(sourceName,
                                                           originatorMetadataCollectionId,
                                                           originatorServerName,
                                                           originatorServerType,
                                                           originatorOrganizationName,
                                                           relationship,
                                                           methodName);

        if (instanceTypeName != null)
        {
            try
            {
                ElementStub relationshipElementStub = converter.getElementStub(ElementStub.class, relationship, methodName);
                ElementStub endOneElementStub = converter.getElementStub(ElementStub.class, relationship.getEntityOneProxy(), methodName);
                ElementStub endTwoElementStub = converter.getElementStub(ElementStub.class, relationship.getEntityTwoProxy(), methodName);

                if ((repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.PEER_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.TEAM_LEADER_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.TEAM_MEMBER_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.COMMUNITY_MEMBERSHIP_TYPE_NAME)))
                {
                    publisher.sendRelationshipEvent(eventType, relationship.getGUID(), instanceTypeName, relationshipElementStub, endOneElementStub, endTwoElementStub);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      CommunityProfileAuditCode.OUTBOUND_EVENT_EXCEPTION.getMessageDefinition(relationship.getGUID(),
                                                                                                              instanceTypeName,
                                                                                                              error.getClass().getName(),
                                                                                                              error.getMessage()),
                                      error);
            }
        }
    }


    /*
     * Instance events
     */



    /**
     * A new entity has been created.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the new entity
     */
    @Override
    public void processNewEntityEvent(String       sourceName,
                                      String       originatorMetadataCollectionId,
                                      String       originatorServerName,
                                      String       originatorServerType,
                                      String       originatorOrganizationName,
                                      EntityDetail entity)
    {
        final String methodName = "processNewEntityEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.NEW_ELEMENT_CREATED;

        log.debug("Processing new Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                methodName);
    }


    /**
     * A remote repository in the cohort has sent entity details in response to a refresh request.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the requested entity
     */
    @Override
    public void processRefreshEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        final String methodName = "processRefreshEntityEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.REFRESH_ELEMENT_EVENT;

        log.debug("Processing refresh Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                methodName);
    }


    /**
     * An existing entity has been updated.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param oldEntity                      original values for the entity.
     * @param newEntity                      details of the new version of the entity.
     */
    @Override
    public void processUpdatedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail oldEntity,
                                          EntityDetail newEntity)
    {
        final String methodName = "processUpdatedEntityEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_UPDATED;

        log.debug("Processing updated Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                newEntity,
                                null,
                                methodName);
    }


    /**
     * A new classification has been added to an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity with the new classification added. No guarantee this is all the classifications.
     * @param classification new classification
     */
    @Override
    public void processClassifiedEntityEvent(String         sourceName,
                                             String         originatorMetadataCollectionId,
                                             String         originatorServerName,
                                             String         originatorServerType,
                                             String         originatorOrganizationName,
                                             EntityDetail   entity,
                                             Classification classification)
    {
        final String methodName = "processClassifiedEntityEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_CLASSIFIED;

        log.debug("Processing classified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                classification.getName(),
                                methodName);
    }


    /**
     * A new classification has been added to an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity with the new classification added. No guarantee this is all the classifications.
     * @param classification new classification
     */
    @Override
    public void processClassifiedEntityEvent(String         sourceName,
                                             String         originatorMetadataCollectionId,
                                             String         originatorServerName,
                                             String         originatorServerType,
                                             String         originatorOrganizationName,
                                             EntityProxy    entity,
                                             Classification classification)
    {
        final String methodName = "processClassifiedEntityEvent(proxy)";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_CLASSIFIED;

        log.debug("Processing classified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                classification.getName(),
                                methodName);
    }


    /**
     * A classification has been removed from an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been removed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     */
    @Override
    public void processDeclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity,
                                               Classification originalClassification)
    {
        final String methodName = "processDeclassifiedEntityEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_DECLASSIFIED;

        log.debug("Processing declassified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                originalClassification.getName(),
                                methodName);
    }


    /**
     * A classification has been removed from an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been removed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     */
    @Override
    public void processDeclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityProxy    entity,
                                               Classification originalClassification)
    {
        final String methodName = "processDeclassifiedEntityEvent(proxy)";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_DECLASSIFIED;

        log.debug("Processing declassified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                originalClassification.getName(),
                                methodName);
    }


    /**
     * An existing classification has been changed on an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been changed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     * @param classification new classification
     */
    @Override
    public void processReclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity,
                                               Classification originalClassification,
                                               Classification classification)
    {
        final String methodName = "processReclassifiedEntityEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_RECLASSIFIED;

        log.debug("Processing reclassified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                originalClassification.getName(),
                                methodName);
    }


    /**
     * An existing classification has been changed on an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been changed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     * @param classification new classification
     */
    @Override
    public void processReclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityProxy    entity,
                                               Classification originalClassification,
                                               Classification classification)
    {
        final String methodName = "processReclassifiedEntityEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_RECLASSIFIED;

        log.debug("Processing reclassified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                originalClassification.getName(),
                                methodName);
    }


    /**
     * An existing entity has been deleted.  This is a soft delete. This means it is still in the repository,
     * but it is no longer returned on queries.
     * <p>
     * All relationships to the entity are also soft-deleted and will no longer be usable.  These deleted relationships
     * will be notified through separate events.
     * <p>
     * Details of the TypeDef are included with the entity's unique id (guid) to ensure the right entity is deleted in
     * the remote repositories.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         deleted entity
     */
    @Override
    public void processDeletedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        final String methodName = "processDeletedEntityEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_DELETED;

        log.debug("Processing deleted Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                methodName);
    }


    /**
     * An existing entity has been deleted and purged in a single action.
     * All relationships to the entity are also deleted and purged and will no longer be usable.  These deleted relationships
     * will be notified through separate events.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  deleted entity
     */
    @Override
    public void processDeletePurgedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        final String methodName = "processDeletePurgedEntityEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_DELETED;

        log.debug("Processing delete-purge entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                methodName);
    }


    /**
     * The guid of an existing entity has been changed to a new value.  This is used if two different
     * entities are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalEntityGUID             the existing identifier for the entity.
     * @param entity                         new values for this entity, including the new guid.
     */
    @Override
    public void processReIdentifiedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               String       originalEntityGUID,
                                               EntityDetail entity)
    {
        final String methodName = "processReIdentifiedEntityEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_GUID_CHANGED;

        log.debug("Processing re-identified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                methodName);
    }


    /**
     * An existing entity has had its type changed.  Typically, this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalTypeDefSummary         original details of this entity's TypeDef.
     * @param entity                         new values for this entity, including the new type information.
     */
    @Override
    public void processReTypedEntityEvent(String         sourceName,
                                          String         originatorMetadataCollectionId,
                                          String         originatorServerName,
                                          String         originatorServerType,
                                          String         originatorOrganizationName,
                                          TypeDefSummary originalTypeDefSummary,
                                          EntityDetail   entity)
    {
        final String methodName = "processReTypedEntityEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_TYPE_CHANGED;

        log.debug("Processing re-typed Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                methodName);
    }


    /**
     * An existing entity has changed home repository.  This action is taken for example, if a repository
     * becomes permanently unavailable, or if the user community updating this entity move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param sourceName                       name of the source of the event.  It may be the cohort name for incoming events or the
     *                                         local repository, or event mapper name.
     * @param originatorMetadataCollectionId   unique identifier for the metadata collection hosted by the server that
     *                                         sent the event.
     * @param originatorServerName             name of the server that the event came from.
     * @param originatorServerType             type of server that the event came from.
     * @param originatorOrganizationName       name of the organization that owns the server that sent the event.
     * @param originalHomeMetadataCollectionId unique identifier for the original home repository.
     * @param entity                           new values for this entity, including the new home information.
     */
    @Override
    public void processReHomedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          String       originalHomeMetadataCollectionId,
                                          EntityDetail entity)
    {
        final String methodName = "processReHomedEntityEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_HOME_CHANGED;

        log.debug("Processing re-homed Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                methodName);
    }


    /**
     * A new relationship has been created.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   details of the new relationship
     */
    @Override
    public void processNewRelationshipEvent(String       sourceName,
                                            String       originatorMetadataCollectionId,
                                            String       originatorServerName,
                                            String       originatorServerType,
                                            String       originatorOrganizationName,
                                            Relationship relationship)
    {
        final String methodName = "processNewRelationshipEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.NEW_ELEMENT_CREATED;

        log.debug("Processing new relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      relationship,
                                      methodName);
    }


    /**
     * An existing relationship has been updated.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param oldRelationship                original details of the relationship.
     * @param newRelationship                details of the new version of the relationship.
     */
    @Override
    public void processUpdatedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship oldRelationship,
                                                Relationship newRelationship)
    {
        final String methodName = "processUpdatedRelationshipEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_UPDATED;

        log.debug("Processing updated relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      newRelationship,
                                      methodName);
    }


    /**
     * An existing relationship has been deleted.  This is a soft delete. This means it is still in the repository,
     * but it is no longer returned on queries.
     * <p>
     * Details of the TypeDef are included with the relationship's unique id (guid) to ensure the right
     * relationship is deleted in the remote repositories.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   deleted relationship
     */
    @Override
    public void processDeletedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship relationship)
    {
        final String methodName = "processDeletedRelationshipEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_DELETED;

        log.debug("Processing deleted relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      relationship,
                                      methodName);
    }


    /**
     * An active relationship has been deleted and purged from the repository.  This request can not be undone.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param relationship  deleted relationship
     */
    @Override
    public void processDeletePurgedRelationshipEvent(String       sourceName,
                                                     String       originatorMetadataCollectionId,
                                                     String       originatorServerName,
                                                     String       originatorServerType,
                                                     String       originatorOrganizationName,
                                                     Relationship relationship)
    {
        final String methodName = "processUpdatedRelationshipEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_DELETED;

        log.debug("Processing delete-purge relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      relationship,
                                      methodName);
    }


    /**
     * The guid of an existing relationship has changed.  This is used if two different
     * relationships are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalRelationshipGUID       the existing identifier for the relationship.
     * @param relationship                   new values for this relationship, including the new guid.
     */
    @Override
    public void processReIdentifiedRelationshipEvent(String       sourceName,
                                                     String       originatorMetadataCollectionId,
                                                     String       originatorServerName,
                                                     String       originatorServerType,
                                                     String       originatorOrganizationName,
                                                     String       originalRelationshipGUID,
                                                     Relationship relationship)
    {
        final String methodName = "processReIdentifiedRelationshipEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_GUID_CHANGED;

        log.debug("Processing re-identified relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      relationship,
                                      methodName);
    }


    /**
     * An existing relationship has had its type changed.  Typically, this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalTypeDefSummary         original details of this relationship's TypeDef.
     * @param relationship                   new values for this relationship, including the new type information.
     */
    @Override
    public void processReTypedRelationshipEvent(String         sourceName,
                                                String         originatorMetadataCollectionId,
                                                String         originatorServerName,
                                                String         originatorServerType,
                                                String         originatorOrganizationName,
                                                TypeDefSummary originalTypeDefSummary,
                                                Relationship   relationship)
    {
        final String methodName = "processReTypedRelationshipEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_TYPE_CHANGED;

        log.debug("Processing re-typed relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      relationship,
                                      methodName);
    }


    /**
     * An existing relationship has changed home repository.  This action is taken for example, if a repository
     * becomes permanently unavailable, or if the user community updating this relationship move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalHomeMetadataCollection unique identifier for the original home repository.
     * @param relationship                   new values for this relationship, including the new home information.
     */
    @Override
    public void processReHomedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                String       originalHomeMetadataCollection,
                                                Relationship relationship)
    {
        final String methodName = "processReHomedRelationshipEvent";

        final CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.ELEMENT_HOME_CHANGED;

        log.debug("Processing re-homed relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      relationship,
                                      methodName);
    }
}
