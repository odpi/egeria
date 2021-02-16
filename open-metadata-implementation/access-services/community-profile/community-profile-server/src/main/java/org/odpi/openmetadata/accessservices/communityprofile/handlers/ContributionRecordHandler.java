/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.handlers;

import org.odpi.openmetadata.accessservices.communityprofile.builders.ContributionRecordBuilder;
import org.odpi.openmetadata.accessservices.communityprofile.converters.ContributionRecordConverter;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.accessservices.communityprofile.mappers.ContributionRecordMapper;
import org.odpi.openmetadata.accessservices.communityprofile.mappers.PersonalProfileMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContributionRecord;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * ContributionRecordHandler manages the record of a person's contribution to the open metadata ecosystem.
 * The contribution record is stored as a ContributionRecord entity linked to the Person
 * entity for the individual via a PersonalContribution relationship
 */
public class ContributionRecordHandler
{
    private String                  serviceName;
    private String                  serverName;
    private RepositoryHandler       repositoryHandler;
    private OMRSRepositoryHelper    repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;
    private int                     karmaPointPlateau;


    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName name of the consuming service
     * @param serverName name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper helper used by the converters
     * @param repositoryHandler handler for calling the repository services
     * @param karmaPointPlateau the number of karma points to reach a plateau
     */
    public ContributionRecordHandler(String                  serviceName,
                                     String                  serverName,
                                     InvalidParameterHandler invalidParameterHandler,
                                     OMRSRepositoryHelper    repositoryHelper,
                                     RepositoryHandler       repositoryHandler,
                                     int                     karmaPointPlateau)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.karmaPointPlateau = karmaPointPlateau;
    }


    /**
     * Return the karma point plateau for an individual.
     *
     * @param karmaPointTotal current points
     * @return current plateau
     */
    private int getKarmaPointPlateau(int karmaPointTotal)
    {
        return karmaPointTotal / this.karmaPointPlateau;
    }


    /**
     * Return the appropriate contribution record for the identified person.
     *
     * @param userId calling user
     * @param personalProfileGUID unique identifier of personal profile
     * @param qualifiedName qualified name of their personal profile.
     * @param methodName calling method
     *
     * @return contribution record or null if does not exist
     * @throws InvalidParameterException the userId or guid is null
     * @throws PropertyServerException the metadata repository is not available
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public ContributionRecord getContributionRecord(String   userId,
                                                    String   personalProfileGUID,
                                                    String   qualifiedName,
                                                    String   methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String        guidParameterName          = "personalProfileGUID";
        final String        qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(personalProfileGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * Retrieve the contribution record entity by following the PersonalContribution relationship.
         */
        EntityDetail  entity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                              personalProfileGUID,
                                                                              PersonalProfileMapper.PERSONAL_PROFILE_TYPE_NAME,
                                                                              ContributionRecordMapper.PERSONAL_CONTRIBUTION_TYPE_GUID,
                                                                              ContributionRecordMapper.PERSONAL_CONTRIBUTION_TYPE_NAME,
                                                                              methodName);

        if (entity == null)
        {
            String contributionRecordGUID = addContributionRecord(userId,
                                                                  personalProfileGUID,
                                                                  qualifiedName,
                                                                  methodName);

            entity = repositoryHandler.getEntityByGUID(userId,
                                                       contributionRecordGUID,
                                                       "new contribution guid",
                                                       ContributionRecordMapper.CONTRIBUTION_RECORD_TYPE_NAME,
                                                       methodName);

        }

        /*
         * If the contribution entity is returned, convert the entity to a ContributionRecord bean and return
         */
        if (entity != null)
        {
            ContributionRecordConverter converter = new ContributionRecordConverter(entity,
                                                                                    repositoryHelper,
                                                                                    serviceName);

            ContributionRecord contributionRecord = converter.getBean();

            if ((contributionRecord != null) && (contributionRecord.getKarmaPoints() > 0))
            {
                contributionRecord.setKarmaPointPlateau(this.getKarmaPointPlateau(contributionRecord.getKarmaPoints()));
            }

            return contributionRecord;
        }
        else
        {
            /*
             * Throw logic error because if there is a problem with the repository server or security,
             * an exception should already have been thrown.
             */
            throw new PropertyServerException(CommunityProfileErrorCode.UNABLE_TO_CREATE_CONTRIBUTION_RECORD.getMessageDefinition(methodName,
                                                                                                                                  serverName,
                                                                                                                                  personalProfileGUID,
                                                                                                                                  qualifiedName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Creates an empty contribution record in the repository.
     *
     * @param userId calling user
     * @param personalProfileGUID unique identifier for person's profile
     * @param qualifiedName qualified name of person's profile
     * @param methodName calling method
     *
     * @return unique identifier of new contribution record.
     *
     * @throws PropertyServerException the metadata repository is not available
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
    */
    private String addContributionRecord(String               userId,
                                         String               personalProfileGUID,
                                         String               qualifiedName,
                                         String               methodName) throws PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        ContributionRecordBuilder builder = new ContributionRecordBuilder(qualifiedName,
                                                                          repositoryHelper,
                                                                          serviceName,
                                                                          serverName);

        InstanceProperties properties = builder.getQualifiedNameInstanceProperties(methodName);


        String contributionRecordGUID = repositoryHandler.createEntity(userId,
                                                                       ContributionRecordMapper.CONTRIBUTION_RECORD_TYPE_GUID,
                                                                       ContributionRecordMapper.CONTRIBUTION_RECORD_TYPE_NAME,
                                                                       null,
                                                                       null,
                                                                       properties,
                                                                       methodName);

        repositoryHandler.createRelationship(userId,
                                             ContributionRecordMapper.PERSONAL_CONTRIBUTION_TYPE_GUID,
                                             null,
                                             null,
                                             personalProfileGUID,
                                             contributionRecordGUID,
                                             null,
                                             methodName);

        return contributionRecordGUID;
    }


    /**
     * Save an update to an existing contribution record.
     *
     * @param userId calling user
     * @param contributionRecord contribution record with updated properties.
     * @param methodName calling method
     *
     * @throws InvalidParameterException bad property
     * @throws PropertyServerException the metadata repository is not available
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    private void  saveContributionRecord(String              userId,
                                         ContributionRecord  contributionRecord,
                                         String              methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        ContributionRecordBuilder builder = new ContributionRecordBuilder(contributionRecord.getQualifiedName(),
                                                                          repositoryHelper,
                                                                          serviceName,
                                                                          serverName);

        InstanceProperties properties = builder.getContributionRecordProperties(contributionRecord.getKarmaPoints(),
                                                                                contributionRecord.getExtendedProperties(),
                                                                                contributionRecord.getAdditionalProperties());

        repositoryHandler.updateEntityProperties(userId,
                                                 null,
                                                 null,
                                                 contributionRecord.getGUID(),
                                                 ContributionRecordMapper.CONTRIBUTION_RECORD_TYPE_GUID,
                                                 ContributionRecordMapper.CONTRIBUTION_RECORD_TYPE_NAME,
                                                 properties,
                                                 methodName);
    }


    /**
     * Update the ancillary properties of a contribution record
     *
     * @param userId calling user
     * @param personalProfileGUID unique identifier of personal profile
     * @param qualifiedName qualified name of their personal profile
     * @param extendedProperties subtype properties
     * @param additionalProperties additional properties
     * @param methodName calling method
     *
     * @throws InvalidParameterException the userId, qualified name or guid is null
     * @throws PropertyServerException the metadata repository is not available
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void updateContributionRecord(String               userId,
                                         String               personalProfileGUID,
                                         String               qualifiedName,
                                         Map<String, Object>  extendedProperties,
                                         Map<String, String>  additionalProperties,
                                         String               methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        ContributionRecord contributionRecord = getContributionRecord(userId, personalProfileGUID, qualifiedName, methodName);

        if (contributionRecord != null)
        {
            contributionRecord.setExtendedProperties(extendedProperties);
            contributionRecord.setAdditionalProperties(additionalProperties);

            saveContributionRecord(userId, contributionRecord, methodName);
        }
    }


    /**
     * Increment a person's karma points.  If this is their first award, a contribution record will be
     * created automatically.
     *
     * @param userId calling user
     * @param personalProfileGUID unique identifier of personal profile
     * @param qualifiedName qualified name of their personal profile
     * @param karmaPointIncrement number of points to add to the contribution record.
     * @param methodName calling method
     * @return updated record or null if not found
     * @throws InvalidParameterException the userId, qualified name or guid is null
     * @throws PropertyServerException the metadata repository is not available
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public ContributionRecord  incrementKarmaPoints(String   userId,
                                                    String   personalProfileGUID,
                                                    String   qualifiedName,
                                                    int      karmaPointIncrement,
                                                    String   methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        ContributionRecord contributionRecord = this.getContributionRecord(userId, personalProfileGUID, qualifiedName, methodName);

        if ((contributionRecord != null) && (karmaPointIncrement > 0))
        {
            int newKarmaPoints = contributionRecord.getKarmaPoints() + karmaPointIncrement;

            contributionRecord.setKarmaPoints(newKarmaPoints);
            saveContributionRecord(userId, contributionRecord, methodName);

            contributionRecord.setKarmaPointPlateau(this.getKarmaPointPlateau(newKarmaPoints));
        }

        return contributionRecord;
    }


    /**
     * Delete the contribution record for a person.
     *
     * @param userId calling user
     * @param personalProfileGUID unique identifier of personal profile
     * @param qualifiedName qualified name of their personal profile.
     * @param methodName calling method
     *
     * @throws InvalidParameterException the userId of qualified name is null
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void deleteContributionRecord(String   userId,
                                         String   personalProfileGUID,
                                         String   qualifiedName,
                                         String   methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String guidParameterName = "profileGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        ContributionRecord contributionRecord = this.getContributionRecord(userId, personalProfileGUID, qualifiedName, methodName);

        if (contributionRecord != null)
        {
            repositoryHandler.removeEntity(userId,
                                           null,
                                           null,
                                           contributionRecord.getGUID(),
                                           guidParameterName,
                                           ContributionRecordMapper.CONTRIBUTION_RECORD_TYPE_GUID,
                                           ContributionRecordMapper.CONTRIBUTION_RECORD_TYPE_NAME,
                                           qualifiedNameParameterName,
                                           qualifiedName,
                                           methodName);
        }
    }
}
