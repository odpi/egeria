/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;


/**
 * RatingHandler manages the Rating entity.  The Rating entity describes the star rating and review text
 * type of feedback.  Ratings do not support effectivity dates and are always anchored to a referenceable.
 */
public class RatingHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
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
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public RatingHandler(OpenMetadataAPIGenericConverter<B> converter,
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
    }


    /**
     * Return the Ratings attached to a supplied entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the feedback is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getRatings(String       userId,
                               String       elementGUID,
                               String       elementGUIDParameterName,
                               String       elementTypeName,
                               List<String> serviceSupportedZones,
                               int          startingFrom,
                               int          pageSize,
                               boolean      forLineage,
                               boolean      forDuplicateProcessing,
                               Date         effectiveTime,
                               String       methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName,
                                        OpenMetadataType.RATING.typeName,
                                        (String)null,
                                        null,
                                        0,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Add or replace an existing Rating for this user.
     *
     * @param userId      userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param elementGUID   unique identifier for the connected entity (Referenceable).
     * @param elementGUIDParameterName parameter supplying the elementGUID
     * @param starRating  StarRating ordinal for enumeration for not recommended, one to five stars.
     * @param review      user review of asset.  This can be null.
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return unique identifier of the rating
     *
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String saveRating(String  userId,
                             String  externalSourceGUID,
                             String  externalSourceName,
                             String  elementGUID,
                             String  elementGUIDParameterName,
                             int     starRating,
                             String  review,
                             boolean isPublic,
                             boolean forLineage,
                             boolean forDuplicateProcessing,
                             Date    effectiveTime,
                             String  methodName) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        try
        {
            this.removeRating(userId,
                              externalSourceGUID,
                              externalSourceName,
                              elementGUID,
                              elementGUIDParameterName,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
        }
        catch (Exception error)
        {
            /*
             * Exception means this is the first rating from user.
             */
        }

        RatingBuilder builder = new RatingBuilder(starRating,
                                                  review,
                                                  isPublic,
                                                  repositoryHelper,
                                                  serviceName,
                                                  serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    elementGUID,
                                    elementGUIDParameterName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    supportedZones,
                                    builder,
                                    methodName);

        String ratingGUID = this.createBeanInRepository(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        OpenMetadataType.RATING.typeGUID,
                                                        OpenMetadataType.RATING.typeName,
                                                        builder,
                                                        effectiveTime,
                                                        methodName);

        if (ratingGUID != null)
        {
            final String ratingGUIDParameterName = "ratingGUID";

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               elementGUID,
                                               elementGUIDParameterName,
                                               ratingGUID,
                                               ratingGUIDParameterName,
                                               OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeGUID,
                                               builder.getRelationshipInstanceProperties(methodName),
                                               methodName);
        }

        return ratingGUID;
    }


    /**
     * Remove the requested rating.
     *
     * @param userId       calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param elementGUID   unique identifier for the connected entity (Referenceable).
     * @param elementGUIDParameterName parameter supplying the elementGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName   calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public  void removeRating(String  userId,
                              String  externalSourceGUID,
                              String  externalSourceName,
                              String  elementGUID,
                              String  elementGUIDParameterName,
                              boolean forLineage,
                              boolean forDuplicateProcessing,
                              Date    effectiveTime,
                              String  methodName) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        String ratingGUID = this.unlinkConnectedElement(userId,
                                                        true,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        elementGUID,
                                                        elementGUIDParameterName,
                                                        OpenMetadataType.REFERENCEABLE.typeName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        supportedZones,
                                                        OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeGUID,
                                                        OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName,
                                                        OpenMetadataType.RATING.typeName,
                                                        effectiveTime,
                                                        methodName);

        if (ratingGUID != null)
        {
            final String ratingGUIDParameterName = "ratingGUID";

            this.deleteBeanInRepository(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        ratingGUID,
                                        ratingGUIDParameterName,
                                        OpenMetadataType.RATING.typeGUID,
                                        OpenMetadataType.RATING.typeName,
                                        false,
                                        null,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);
        }
    }
}
