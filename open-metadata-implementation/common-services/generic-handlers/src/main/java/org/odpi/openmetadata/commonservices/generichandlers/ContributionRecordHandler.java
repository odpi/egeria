/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * ContributionRecordHandler manages the ContributionRecord entity that records the karma points for an individual.
 * There is on 1-1 relationship between the Person entity and the ContributionRecord and so the profile GUID is passed
 * on the parameters.  ContributionRecords are always maintainable by the local cohort.
 */
public class ContributionRecordHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    private static String contributionRecordGUIDParameterName = "contributionRecordGUID";

    private int karmaPointPlateau;

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
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public B getContributionRecord(String userId,
                                   String profileGUID,
                                   String profileGUIDParameterName,
                                   String methodName) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        return this.getAttachedElement(userId,
                                       profileGUID,
                                       profileGUIDParameterName,
                                       OpenMetadataAPIMapper.PERSON_TYPE_NAME,
                                       OpenMetadataAPIMapper.PERSONAL_CONTRIBUTION_RELATIONSHIP_TYPE_GUID,
                                       OpenMetadataAPIMapper.PERSONAL_CONTRIBUTION_RELATIONSHIP_TYPE_NAME,
                                       OpenMetadataAPIMapper.CONTRIBUTION_RECORD_TYPE_NAME,
                                       false,
                                       false,
                                       supportedZones,
                                       null,
                                       methodName);
    }


    /**
     * Return the ContributionRecord attached to a supplied person profile.
     *
     * @param userId     calling user
     * @param profileGUID identifier for the entity that the contribution record is attached to
     * @param profileGUIDParameterName name of parameter supplying the GUID
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    EntityDetail getContributionRecordEntity(String userId,
                                             String profileGUID,
                                             String profileGUIDParameterName,
                                             String methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        return this.getAttachedEntity(userId,
                                      profileGUID,
                                      profileGUIDParameterName,
                                      OpenMetadataAPIMapper.PERSON_TYPE_NAME,
                                      OpenMetadataAPIMapper.PERSONAL_CONTRIBUTION_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.PERSONAL_CONTRIBUTION_RELATIONSHIP_TYPE_NAME,
                                      OpenMetadataAPIMapper.CONTRIBUTION_RECORD_TYPE_NAME,
                                      false,
                                      false,
                                      supportedZones,
                                      null,
                                      methodName);
    }


    /**
     * Increment a person's karma points.  If this is their first award, a contribution record will be
     * created automatically.
     *
     * @param userId calling user
     * @param profileGUID unique identifier of personal profile
     * @param profileGUIDParameterName parameter supply profileGUID
     * @param qualifiedName unique name for this personal profile
     * @param karmaPointIncrement number of points to add to the contribution record.
     * @param methodName calling method
     * @return updated record or null if not found
     * @throws InvalidParameterException the userId, qualified name or guid is null
     * @throws PropertyServerException the metadata repository is not available
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public B  incrementKarmaPoints(String   userId,
                                   String   profileGUID,
                                   String   profileGUIDParameterName,
                                   String   qualifiedName,
                                   int      karmaPointIncrement,
                                   String   methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        EntityDetail contributionRecord = this.getAttachedEntity(userId,
                                                                 profileGUID,
                                                                 profileGUIDParameterName,
                                                                 OpenMetadataAPIMapper.PERSON_TYPE_NAME,
                                                                 OpenMetadataAPIMapper.PERSONAL_CONTRIBUTION_RELATIONSHIP_TYPE_GUID,
                                                                 OpenMetadataAPIMapper.PERSONAL_CONTRIBUTION_RELATIONSHIP_TYPE_NAME,
                                                                 OpenMetadataAPIMapper.CONTRIBUTION_RECORD_TYPE_NAME,
                                                                 false,
                                                                 false,
                                                                 supportedZones,
                                                                 null,
                                                                 methodName);

        if ((contributionRecord != null) && (karmaPointIncrement > 0))
        {
            int newKarmaPoints = repositoryHelper.getIntProperty(serviceName,
                                                                 OpenMetadataAPIMapper.KARMA_POINTS_PROPERTY_NAME,
                                                                 contributionRecord.getProperties(),
                                                                 methodName) + karmaPointIncrement;

            saveContributionRecord(userId,
                                   profileGUID,
                                   profileGUIDParameterName,
                                   qualifiedName + "_ContributionRecord",
                                   newKarmaPoints,
                                   null,
                                   null,
                                   null,
                                   methodName);

        }

        return this.getContributionRecord(userId, profileGUID, profileGUIDParameterName, methodName);
    }


    /**
     * Set up the contribution values in a Person profile.
     *
     * @param userId      userId of user making request.
     * @param profileGUID   unique identifier for the connected Person entity.
     * @param profileGUIDParameterName parameter supplying the profileGUID
     * @param qualifiedName unique name for this contribution record
     * @param karmaPoints  contribution points for the individual
     * @param additionalProperties additional properties for the contribution record
     * @param extendedProperties additional properties from defined subtypes
     * @param suppliedTypeName name of subtype or null
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
                                       Map<String, String> additionalProperties,
                                       String              suppliedTypeName,
                                       Map<String, Object> extendedProperties,
                                       String              methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        EntityDetail contributionRecordEntity = this.getContributionRecordEntity(userId, profileGUID, profileGUIDParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.CONTRIBUTION_RECORD_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.CONTRIBUTION_RECORD_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        ContributionRecordBuilder builder = new ContributionRecordBuilder(qualifiedName,
                                                                          karmaPoints,
                                                                          additionalProperties,
                                                                          typeGUID,
                                                                          typeName,
                                                                          extendedProperties,
                                                                          repositoryHelper,
                                                                          serviceName,
                                                                          serverName);

        builder.setAnchors(userId, profileGUID, methodName);

        if (contributionRecordEntity == null)
        {
            String contributionRecordGUID = this.createBeanInRepository(userId,
                                                                        null,
                                                                        null,
                                                                        OpenMetadataAPIMapper.CONTRIBUTION_RECORD_TYPE_GUID,
                                                                        OpenMetadataAPIMapper.CONTRIBUTION_RECORD_TYPE_NAME,
                                                                        null,
                                                                        null,
                                                                        builder,
                                                                        methodName);

            if ((contributionRecordGUID != null) && (profileGUID != null))
            {
                this.linkElementToElement(userId,
                                          null,
                                          null,
                                          profileGUID,
                                          profileGUIDParameterName,
                                          OpenMetadataAPIMapper.PERSON_TYPE_NAME,
                                          contributionRecordGUID,
                                          contributionRecordGUIDParameterName,
                                          OpenMetadataAPIMapper.CONTRIBUTION_RECORD_TYPE_NAME,
                                          false,
                                          false,
                                          supportedZones,
                                          OpenMetadataAPIMapper.PERSONAL_CONTRIBUTION_RELATIONSHIP_TYPE_GUID,
                                          OpenMetadataAPIMapper.PERSONAL_CONTRIBUTION_RELATIONSHIP_TYPE_NAME,
                                          null,
                                          methodName);
            }
        }
        else
        {
            this.updateBeanInRepository(userId,
                                        null,
                                        null,
                                        contributionRecordEntity.getGUID(),
                                        contributionRecordGUIDParameterName,
                                        OpenMetadataAPIMapper.CONTRIBUTION_RECORD_TYPE_GUID,
                                        OpenMetadataAPIMapper.CONTRIBUTION_RECORD_TYPE_NAME,
                                        false,
                                        false,
                                        supportedZones,
                                        builder.getInstanceProperties(methodName),
                                        false,
                                        new Date(),
                                        methodName);
        }

    }


    /**
     * Remove the requested contribution record.
     *
     * @param userId       calling user
     * @param profileGUID   unique identifier for the connected Person entity.
     * @param profileGUIDParameterName parameter supplying the profileGUID
     * @param methodName   calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void removeContributionRecord(String userId,
                                         String profileGUID,
                                         String profileGUIDParameterName,
                                         String methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        EntityDetail contributionRecordEntity = this.getContributionRecordEntity(userId, profileGUID, profileGUIDParameterName, methodName);

        if (contributionRecordEntity != null)
        {
            this.deleteBeanInRepository(userId,
                                        null,
                                        null,
                                        contributionRecordEntity.getGUID(),
                                        contributionRecordGUIDParameterName,
                                        OpenMetadataAPIMapper.CONTRIBUTION_RECORD_TYPE_GUID,
                                        OpenMetadataAPIMapper.CONTRIBUTION_RECORD_TYPE_NAME,
                                        null,
                                        null,
                                        false,
                                        false,
                                        new Date(),
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
