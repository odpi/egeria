/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * ContributionRecordHandler manages the ContributionRecord entity that records the karma points for an individual.
 * There is on 1-1 relationship between the ActorProfile entity and the ContributionRecord and so the profile GUID is passed
 * on the parameters.  ContributionRecords are always maintainable by the local cohort.
 */
public class ContributionRecordHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    private final static String contributionRecordGUIDParameterName = "contributionRecordGUID";

    private final int karmaPointPlateau;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from
     * @param defaultZones list of zones that the access service should set in all new Asset instances
     * @param publishZones list of zones that the access service sets up in published Asset instances
     * @param karmaPointPlateau number of karma points to a plateau
     * @param auditLog destination for audit log events
     */
    public ContributionRecordHandler(OpenMetadataAPIGenericConverter<B> converter,
                                     Class<B>                           beanClass,
                                     String                             serviceName,
                                     String                             serverName,
                                     InvalidParameterHandler            invalidParameterHandler,
                                     RepositoryHandler                  repositoryHandler,
                                     OMRSRepositoryHelper               repositoryHelper,
                                     String                             localServerUserId,
                                     OpenMetadataServerSecurityVerifier securityVerifier,
                                     List<String>                       supportedZones,
                                     List<String>                       defaultZones,
                                     List<String>                       publishZones,
                                     int                                karmaPointPlateau,
                                     AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);

        this.karmaPointPlateau = karmaPointPlateau;
    }


    /**
     * Return the ContributionRecord attached to a supplied person profile.
     *
     * @param userId     calling user
     * @param profileGUID identifier for the entity that the contribution record is attached to
     * @param profileGUIDParameterName name of parameter supplying the GUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public B getContributionRecord(String  userId,
                                   String  profileGUID,
                                   String  profileGUIDParameterName,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return this.getAttachedElement(userId,
                                       profileGUID,
                                       profileGUIDParameterName,
                                       OpenMetadataType.ACTOR_PROFILE.typeName,
                                       OpenMetadataType.CONTRIBUTION_RELATIONSHIP.typeGUID,
                                       OpenMetadataType.CONTRIBUTION_RELATIONSHIP.typeName,
                                       OpenMetadataType.CONTRIBUTION_RECORD.typeName,
                                       0,
                                       null,
                                       null,
                                       SequencingOrder.CREATION_DATE_RECENT,
                                       null,
                                       forLineage,
                                       forDuplicateProcessing,
                                       supportedZones,
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Return the ContributionRecord attached to a supplied person profile.
     *
     * @param userId     calling user
     * @param profileGUID identifier for the entity that the contribution record is attached to
     * @param profileGUIDParameterName name of parameter supplying the GUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    EntityDetail getContributionRecordEntity(String  userId,
                                             String  profileGUID,
                                             String  profileGUIDParameterName,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return this.getAttachedEntity(userId,
                                      profileGUID,
                                      profileGUIDParameterName,
                                      OpenMetadataType.ACTOR_PROFILE.typeName,
                                      OpenMetadataType.CONTRIBUTION_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.CONTRIBUTION_RELATIONSHIP.typeName,
                                      OpenMetadataType.CONTRIBUTION_RECORD.typeName,
                                      2,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Set up the contribution values in a Person profile.
     *
     * @param userId      userId of user making request.
     * @param profileGUID   unique identifier for the connected Person entity.
     * @param profileGUIDParameterName parameter supplying the profileGUID
     * @param qualifiedName unique name for the profile
     * @param karmaPoints  contribution points for the individual
     * @param isPublic  can this information be shared with colleagues
     * @param additionalProperties additional properties for the contribution record
     * @param extendedProperties additional properties from defined subtypes
     * @param suppliedTypeName name of subtype or null
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void saveContributionRecord(String              userId,
                                       String              profileGUID,
                                       String              profileGUIDParameterName,
                                       String              qualifiedName,
                                       long                karmaPoints,
                                       boolean             isPublic,
                                       Map<String, String> additionalProperties,
                                       String              suppliedTypeName,
                                       Map<String, Object> extendedProperties,
                                       boolean             isMergeUpdate,
                                       boolean             forLineage,
                                       boolean             forDuplicateProcessing,
                                       Date                effectiveTime,
                                       String              methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        EntityDetail contributionRecordEntity = this.getContributionRecordEntity(userId,
                                                                                 profileGUID,
                                                                                 profileGUIDParameterName,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 effectiveTime,
                                                                                 methodName);

        String typeName = OpenMetadataType.CONTRIBUTION_RECORD.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.CONTRIBUTION_RECORD.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);


        if (contributionRecordEntity == null)
        {
            ContributionRecordBuilder builder = new ContributionRecordBuilder(qualifiedName + "_ContributionRecord",
                                                                              karmaPoints,
                                                                              isPublic,
                                                                              additionalProperties,
                                                                              typeGUID,
                                                                              typeName,
                                                                              extendedProperties,
                                                                              repositoryHelper,
                                                                              serviceName,
                                                                              serverName);

            this.addAnchorGUIDToBuilder(userId,
                                        profileGUID,
                                        profileGUIDParameterName,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        supportedZones,
                                        builder,
                                        methodName);

            String contributionRecordGUID = this.createBeanInRepository(userId,
                                                                        null,
                                                                        null,
                                                                        OpenMetadataType.CONTRIBUTION_RECORD.typeGUID,
                                                                        OpenMetadataType.CONTRIBUTION_RECORD.typeName,
                                                                        builder,
                                                                        effectiveTime,
                                                                        methodName);

            if ((contributionRecordGUID != null) && (profileGUID != null))
            {
                this.uncheckedLinkElementToElement(userId,
                                                   null,
                                                   null,
                                                   profileGUID,
                                                   profileGUIDParameterName,
                                                   contributionRecordGUID,
                                                   contributionRecordGUIDParameterName,
                                                   OpenMetadataType.CONTRIBUTION_RELATIONSHIP.typeGUID,
                                                   null,
                                                   methodName);
            }
        }
        else
        {
            ContributionRecordBuilder builder = new ContributionRecordBuilder(null,
                                                                              karmaPoints,
                                                                              isPublic,
                                                                              additionalProperties,
                                                                              typeGUID,
                                                                              typeName,
                                                                              extendedProperties,
                                                                              repositoryHelper,
                                                                              serviceName,
                                                                              serverName);
            this.updateBeanInRepository(userId,
                                        null,
                                        null,
                                        contributionRecordEntity.getGUID(),
                                        contributionRecordGUIDParameterName,
                                        OpenMetadataType.CONTRIBUTION_RECORD.typeGUID,
                                        OpenMetadataType.CONTRIBUTION_RECORD.typeName,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        builder.getInstanceProperties(methodName),
                                        isMergeUpdate,
                                        effectiveTime,
                                        methodName);
        }

    }


    /**
     * Remove the requested contribution record.
     *
     * @param userId       calling user
     * @param profileGUID   unique identifier for the connected Person entity.
     * @param profileGUIDParameterName parameter supplying the profileGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName   calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void removeContributionRecord(String  userId,
                                         String  profileGUID,
                                         String  profileGUIDParameterName,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        EntityDetail contributionRecordEntity = this.getContributionRecordEntity(userId,
                                                                                 profileGUID,
                                                                                 profileGUIDParameterName,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 effectiveTime,
                                                                                 methodName);

        if (contributionRecordEntity != null)
        {
            this.deleteBeanInRepository(userId,
                                        null,
                                        null,
                                        contributionRecordEntity.getGUID(),
                                        contributionRecordGUIDParameterName,
                                        OpenMetadataType.CONTRIBUTION_RECORD.typeGUID,
                                        OpenMetadataType.CONTRIBUTION_RECORD.typeName,
                                        null,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);
        }
    }


    /**
     * Return the karma point plateau for an individual.
     *
     * @param karmaPointTotal current points
     * @return current plateau
     */
    public int getKarmaPointPlateau(int karmaPointTotal)
    {
        return karmaPointTotal / this.karmaPointPlateau;
    }

}
