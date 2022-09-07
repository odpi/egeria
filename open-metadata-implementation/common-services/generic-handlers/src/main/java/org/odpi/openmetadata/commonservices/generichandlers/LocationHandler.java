/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * LocationHandler manages Location objects.  It runs server-side in
 * the OMAG Server Platform and retrieves Location entities through the OMRSRepositoryConnector.
 * It supports effectivity dates on entities and relationships but not on classifications (since these tent to be
 * innate properties of the location) and all locations are local cohort.
 */
public class LocationHandler<B> extends ReferenceableHandler<B>
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
    public LocationHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create the location.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param qualifiedName unique name for the location - used in other configuration
     * @param identifier code value or symbol used to identify the location - typically unique.
     * @param displayName short display name for the location
     * @param description description of the governance location
     * @param additionalProperties additional properties for a location
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a governance location subtype
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new location object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createLocation(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              qualifiedName,
                                 String              identifier,
                                 String              displayName,
                                 String              description,
                                 Map<String, String> additionalProperties,
                                 String              suppliedTypeName,
                                 Map<String, Object> extendedProperties,
                                 Date                effectiveFrom,
                                 Date                effectiveTo,
                                 Date                effectiveTime,
                                 String              methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.LOCATION_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        LocationBuilder locationBuilder = new LocationBuilder(qualifiedName,
                                                              identifier,
                                                              displayName,
                                                              description,
                                                              additionalProperties,
                                                              typeGUID,
                                                              typeName,
                                                              extendedProperties,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        locationBuilder.setEffectivityDates(effectiveFrom, effectiveTo);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           typeGUID,
                                           typeName,
                                           locationBuilder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Create a new metadata element to represent a location using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new location.
     *
     * All categories and terms are linked to a single location.  They are owned by this location and if the
     * location is deleted, any linked terms and categories are deleted as well.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param templateGUID unique identifier of the metadata element to copy
     * @param identifier code value or symbol used to identify the location - typically unique.
     * @param qualifiedName unique name for the location - used in other configuration
     * @param displayName short display name for the location
     * @param description description of the governance location
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createLocationFromTemplate(String userId,
                                             String externalSourceGUID,
                                             String externalSourceName,
                                             String templateGUID,
                                             String qualifiedName,
                                             String identifier,
                                             String displayName,
                                             String description,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        LocationBuilder locationBuilder = new LocationBuilder(qualifiedName,
                                                              identifier,
                                                              displayName,
                                                              description,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        return this.createBeanFromTemplate(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           locationBuilder,
                                           supportedZones,
                                           methodName);
    }


    /**
     * Update the location.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationGUID unique identifier of the location to update
     * @param locationGUIDParameterName parameter passing the locationGUID
     * @param qualifiedName unique name for the location - used in other configuration
     * @param identifier code value or symbol used to identify the location - typically unique.
     * @param displayName short display name for the location
     * @param description description of the governance location
     * @param additionalProperties additional properties for a governance location
     * @param suppliedTypeName type of location
     * @param extendedProperties  properties for a governance location subtype
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateLocation(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              locationGUID,
                                 String              locationGUIDParameterName,
                                 String              qualifiedName,
                                 String              identifier,
                                 String              displayName,
                                 String              description,
                                 Map<String, String> additionalProperties,
                                 String              suppliedTypeName,
                                 Map<String, Object> extendedProperties,
                                 boolean             isMergeUpdate,
                                 Date                effectiveFrom,
                                 Date                effectiveTo,
                                 boolean             forLineage,
                                 boolean             forDuplicateProcessing,
                                 Date                effectiveTime,
                                 String              methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, locationGUIDParameterName, methodName);

        if (!isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }

        String typeName = OpenMetadataAPIMapper.LOCATION_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        LocationBuilder locationBuilder = new LocationBuilder(qualifiedName,
                                                              identifier,
                                                              displayName,
                                                              description,
                                                              additionalProperties,
                                                              typeGUID,
                                                              typeName,
                                                              extendedProperties,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        locationBuilder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    locationGUID,
                                    locationGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    locationBuilder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Mark the location as a Fixed Location.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationGUID unique identifier of location
     * @param locationGUIDParameterName parameter name supplying locationGUID
     * @param coordinates coordinate location
     * @param mapProjection scheme used for the coordinates
     * @param postalAddress postal address of the location
     * @param timeZone time zone of the location
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addFixedLocationClassification(String  userId,
                                                String  externalSourceGUID,
                                                String  externalSourceName,
                                                String  locationGUID,
                                                String  locationGUIDParameterName,
                                                String  coordinates,
                                                String  mapProjection,
                                                String  postalAddress,
                                                String  timeZone,
                                                Date    effectiveFrom,
                                                Date    effectiveTo,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, locationGUIDParameterName, methodName);

        LocationBuilder builder = new LocationBuilder(repositoryHelper, serviceName, serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           locationGUID,
                                           locationGUIDParameterName,
                                           OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                           OpenMetadataAPIMapper.FIXED_LOCATION_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.FIXED_LOCATION_CLASSIFICATION_TYPE_NAME,
                                           builder.getFixedLocationProperties(coordinates, 
                                                                              mapProjection,
                                                                              postalAddress,
                                                                              timeZone,
                                                                              methodName),
                                           false,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the Fixed Location designation from a location.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationGUID unique identifier of location
     * @param locationGUIDParameterName parameter name supplying locationGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeFixedLocationClassification(String  userId,
                                                   String  externalSourceGUID,
                                                   String  externalSourceName,
                                                   String  locationGUID,
                                                   String  locationGUIDParameterName,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, locationGUIDParameterName, methodName);

        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                locationGUID,
                                                locationGUIDParameterName,
                                                OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                OpenMetadataAPIMapper.FIXED_LOCATION_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.FIXED_LOCATION_CLASSIFICATION_TYPE_GUID,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Mark the location as a Secure Location.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationGUID unique identifier of location
     * @param locationGUIDParameterName parameter name supplying locationGUID
     * @param description description of security
     * @param level level of security
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addSecureLocationClassification(String  userId,
                                                 String  externalSourceGUID,
                                                 String  externalSourceName,
                                                 String  locationGUID,
                                                 String  locationGUIDParameterName,
                                                 String  description,
                                                 String  level,
                                                 Date    effectiveFrom,
                                                 Date    effectiveTo,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, locationGUIDParameterName, methodName);

        LocationBuilder builder = new LocationBuilder(repositoryHelper, serviceName, serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           locationGUID,
                                           locationGUIDParameterName,
                                           OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                           OpenMetadataAPIMapper.SECURE_LOCATION_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.SECURE_LOCATION_CLASSIFICATION_TYPE_NAME,
                                           builder.getSecureLocationProperties(description, level, methodName),
                                           false,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the Secure Location designation from a location.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationGUID unique identifier of location
     * @param locationGUIDParameterName parameter name supplying locationGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeSecureLocationClassification(String  userId,
                                                    String  externalSourceGUID,
                                                    String  externalSourceName,
                                                    String  locationGUID,
                                                    String  locationGUIDParameterName,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing,
                                                    Date    effectiveTime,
                                                    String  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                locationGUID,
                                                locationGUIDParameterName,
                                                OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                OpenMetadataAPIMapper.SECURE_LOCATION_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.SECURE_LOCATION_CLASSIFICATION_TYPE_GUID,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Mark the location as a Cyber Location.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationGUID unique identifier of location
     * @param locationGUIDParameterName parameter name supplying locationGUID
     * @param networkAddress network address of the location
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addCyberLocationClassification(String  userId,
                                                String  externalSourceGUID,
                                                String  externalSourceName,
                                                String  locationGUID,
                                                String  locationGUIDParameterName,
                                                String  networkAddress,
                                                Date    effectiveFrom,
                                                Date    effectiveTo,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, locationGUIDParameterName, methodName);

        LocationBuilder builder = new LocationBuilder(repositoryHelper, serviceName, serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           locationGUID,
                                           locationGUIDParameterName,
                                           OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                           OpenMetadataAPIMapper.CYBER_LOCATION_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.CYBER_LOCATION_CLASSIFICATION_TYPE_NAME,
                                           builder.getCyberLocationProperties(networkAddress, methodName),
                                           false,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the Cyber Location designation from a location.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationGUID unique identifier of location
     * @param locationGUIDParameterName parameter name supplying locationGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeCyberLocationClassification(String  userId,
                                                   String  externalSourceGUID,
                                                   String  externalSourceName,
                                                   String  locationGUID,
                                                   String  locationGUIDParameterName,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                locationGUID,
                                                locationGUIDParameterName,
                                                OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                OpenMetadataAPIMapper.CYBER_LOCATION_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.CYBER_LOCATION_CLASSIFICATION_TYPE_GUID,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Create a parent-child relationship between two locations.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationParentGUID unique identifier of the parent location
     * @param locationParentGUIDParameterName parameter supplying the parent
     * @param locationChildGUID unique identifier of the child location
     * @param locationChildGUIDParameterName parameter supplying the child
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupNestedLocation(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  locationParentGUID,
                                    String  locationParentGUIDParameterName,
                                    String  locationChildGUID,
                                    String  locationChildGUIDParameterName,
                                    Date    effectiveFrom,
                                    Date    effectiveTo,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  locationParentGUID,
                                  locationParentGUIDParameterName,
                                  OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                  locationChildGUID,
                                  locationChildGUIDParameterName,
                                  OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.NESTED_LOCATION_TYPE_GUID,
                                  OpenMetadataAPIMapper.NESTED_LOCATION_TYPE_NAME,
                                  setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a parent-child relationship between two locations.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationParentGUID unique identifier of the parent location
     * @param locationParentGUIDParameterName parameter supplying the parent
     * @param locationChildGUID unique identifier of the child location
     * @param locationChildGUIDParameterName parameter supplying the child
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearNestedLocation(String userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  locationParentGUID,
                                    String  locationParentGUIDParameterName,
                                    String  locationChildGUID,
                                    String  locationChildGUIDParameterName,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      locationParentGUID,
                                      locationParentGUIDParameterName,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                      locationChildGUID,
                                      locationChildGUIDParameterName,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataAPIMapper.NESTED_LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.NESTED_LOCATION_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Create a peer relationship between two locations.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationOneGUID unique identifier of the first location
     * @param locationOneGUIDParameterName parameter supplying the first location
     * @param locationTwoGUID unique identifier of the second location
     * @param locationTwoGUIDParameterName parameter supplying the second location
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupPeerLocations(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  locationOneGUID,
                                   String  locationOneGUIDParameterName,
                                   String  locationTwoGUID,
                                   String  locationTwoGUIDParameterName,
                                   Date    effectiveFrom,
                                   Date    effectiveTo,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  locationOneGUID,
                                  locationOneGUIDParameterName,
                                  OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                  locationTwoGUID,
                                  locationTwoGUIDParameterName,
                                  OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.ADJACENT_LOCATION_TYPE_GUID,
                                  OpenMetadataAPIMapper.ADJACENT_LOCATION_TYPE_NAME,
                                  setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a peer relationship between two locations.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationOneGUID unique identifier of the first location
     * @param locationOneGUIDParameterName parameter supplying the first location
     * @param locationTwoGUID unique identifier of the second location
     * @param locationTwoGUIDParameterName parameter supplying the second location
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearPeerLocations(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  locationOneGUID,
                                   String  locationOneGUIDParameterName,
                                   String  locationTwoGUID,
                                   String  locationTwoGUIDParameterName,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      locationOneGUID,
                                      locationOneGUIDParameterName,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                      locationTwoGUID,
                                      locationTwoGUIDParameterName,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataAPIMapper.ADJACENT_LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.ADJACENT_LOCATION_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }



    /**
     * Create a relationship between an actor profile and an associated location.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param actorProfileGUID unique identifier of the first location
     * @param actorProfileGUIDParameterName parameter supplying the first location
     * @param locationGUID unique identifier of the second location
     * @param locationGUIDParameterName parameter supplying the second location
     * @param associationType type of association with the location
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProfileLocation(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  actorProfileGUID,
                                     String  actorProfileGUIDParameterName,
                                     String  locationGUID,
                                     String  locationGUIDParameterName,
                                     String  associationType,
                                     Date    effectiveFrom,
                                     Date    effectiveTo,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null, OpenMetadataAPIMapper.ASSOCIATION_TYPE_PROPERTY_NAME, associationType, methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  actorProfileGUID,
                                  actorProfileGUIDParameterName,
                                  OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_NAME,
                                  locationGUID,
                                  locationGUIDParameterName,
                                  OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.PROFILE_LOCATION_TYPE_GUID,
                                  OpenMetadataAPIMapper.PROFILE_LOCATION_TYPE_NAME,
                                  setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a relationship between an actor profile and an associated location.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param actorProfileGUID unique identifier of the first location
     * @param actorProfileGUIDParameterName parameter supplying the first location
     * @param locationGUID unique identifier of the second location
     * @param locationGUIDParameterName parameter supplying the second location
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProfileLocation(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  actorProfileGUID,
                                     String  actorProfileGUIDParameterName,
                                     String  locationGUID,
                                     String  locationGUIDParameterName,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      actorProfileGUID,
                                      actorProfileGUIDParameterName,
                                      OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_NAME,
                                      locationGUID,
                                      locationGUIDParameterName,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataAPIMapper.PROFILE_LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.PROFILE_LOCATION_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Create a relationship between a location and an asset.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationGUID unique identifier of the location
     * @param locationGUIDParameterName parameter supplying the location
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName parameter supplying the asset
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAssetLocation(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  locationGUID,
                                   String  locationGUIDParameterName,
                                   String  assetGUID,
                                   String  assetGUIDParameterName,
                                   Date    effectiveFrom,
                                   Date    effectiveTo,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  locationGUID,
                                  locationGUIDParameterName,
                                  OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                  assetGUID,
                                  assetGUIDParameterName,
                                  OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_GUID,
                                  OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_NAME,
                                  setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a relationship between a location and an asset.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationGUID unique identifier of the location
     * @param locationGUIDParameterName parameter supplying the location
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName parameter supplying the asset
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAssetLocation(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  locationGUID,
                                   String  locationGUIDParameterName,
                                   String  assetGUID,
                                   String  assetGUIDParameterName,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      locationGUID,
                                      locationGUIDParameterName,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                      assetGUID,
                                      assetGUIDParameterName,
                                      OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                      OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Remove the metadata element representing a location.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param locationGUID unique identifier of the metadata element to remove
     * @param locationGUIDParameterName parameter supplying the locationGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeLocation(String  userId,
                               String  externalSourceGUID,
                               String  externalSourceName,
                               String  locationGUID,
                               String  locationGUIDParameterName,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime,
                               String  methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    locationGUID,
                                    locationGUIDParameterName,
                                    OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                                    OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of adjacent location metadata elements linked to locationGUID.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the location is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B> getAdjacentLocations(String       userId,
                                        String       elementGUID,
                                        String       elementGUIDParameterName,
                                        String       elementTypeName,
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
                                        OpenMetadataAPIMapper.ADJACENT_LOCATION_TYPE_GUID,
                                        OpenMetadataAPIMapper.ADJACENT_LOCATION_TYPE_NAME,
                                        OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                        null,
                                        null,
                                        0,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the list of nested location metadata elements linked to locationGUID.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the location is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B> getNestedLocations(String       userId,
                                      String       elementGUID,
                                      String       elementGUIDParameterName,
                                      String       elementTypeName,
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
                                        OpenMetadataAPIMapper.NESTED_LOCATION_TYPE_GUID,
                                        OpenMetadataAPIMapper.NESTED_LOCATION_TYPE_NAME,
                                        OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the list of location metadata elements that has the location identifier with locationGUID nested inside it.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the location is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B> getGroupingLocations(String       userId,
                                        String       elementGUID,
                                        String       elementGUIDParameterName,
                                        String       elementTypeName,
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
                                        OpenMetadataAPIMapper.NESTED_LOCATION_TYPE_GUID,
                                        OpenMetadataAPIMapper.NESTED_LOCATION_TYPE_NAME,
                                        OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                        null,
                                        null,
                                        1,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }



    /**
     * Count the number of locations attached to an entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countKnownLocations(String  userId,
                                   String  elementGUID,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      elementGUID,
                                      OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                      OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_NAME,
                                      1,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Return the locations attached to an asset.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the location is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B> getAssetLocations(String       userId,
                                     String       elementGUID,
                                     String       elementGUIDParameterName,
                                     String       elementTypeName,
                                     int          startingFrom,
                                     int          pageSize,
                                     boolean      forLineage,
                                     boolean      forDuplicateProcessing,
                                     Date         effectiveTime,
                                     String       methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        return this.getAssetLocations(userId, elementGUID, elementGUIDParameterName, elementTypeName, supportedZones, startingFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Return the locations attached to an asset.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the location is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B> getAssetLocations(String       userId,
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
                                        OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_GUID,
                                        OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_NAME,
                                        OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                        null,
                                        null,
                                        1,
                                        forLineage,
                                        forDuplicateProcessing,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the locations attached to an actor profile.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the location is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B> getProfileLocations(String       userId,
                                       String       elementGUID,
                                       String       elementGUIDParameterName,
                                       String       elementTypeName,
                                       int          startingFrom,
                                       int          pageSize,
                                       boolean      forLineage,
                                       boolean      forDuplicateProcessing,
                                       Date         effectiveTime,
                                       String       methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        return this.getProfileLocations(userId, elementGUID, elementGUIDParameterName, elementTypeName, supportedZones, startingFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Return the locations attached to an actor profile.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the location is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B> getProfileLocations(String       userId,
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
                                        OpenMetadataAPIMapper.PROFILE_LOCATION_TYPE_GUID,
                                        OpenMetadataAPIMapper.PROFILE_LOCATION_TYPE_NAME,
                                        OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        forLineage,
                                        forDuplicateProcessing,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the list of community metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findLocations(String  userId,
                                 String  searchString,
                                 String  searchStringParameterName,
                                 int     startFrom,
                                 int     pageSize,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                              OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                              null,
                              startFrom,
                              pageSize,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }


    /**
     * Retrieve the list of location metadata elements with a matching qualified name, identifier or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)

     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getLocationsByName(String  userId,
                                        String  name,
                                        String  nameParameterName,
                                        int     startFrom,
                                        int     pageSize,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.IDENTIFIER_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                                    OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of location metadata elements.
     *
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)

     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getLocations(String  userId,
                                  int     startFrom,
                                  int     pageSize,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        return this.getBeansByType(userId,
                                    OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                                    OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }
}
