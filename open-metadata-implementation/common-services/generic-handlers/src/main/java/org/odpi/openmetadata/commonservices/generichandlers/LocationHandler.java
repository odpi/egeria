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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * LocationHandler manages Location objects.  It runs server-side in
 * the OMAG Server Platform and retrieves Location entities through the OMRSRepositoryConnector.
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
     * @param qualifiedName unique name for the location - used in other configuration
     * @param displayName short display name for the location
     * @param description description of the governance location
     * @param additionalProperties additional properties for a location
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a governance location subtype
     * @param methodName calling method
     *
     * @return unique identifier of the new location object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createLocation(String              userId,
                                 String              qualifiedName,
                                 String              displayName,
                                 String              description,
                                 Map<String, String> additionalProperties,
                                 String              suppliedTypeName,
                                 Map<String, Object> extendedProperties,
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
                                                              displayName,
                                                              description,
                                                              additionalProperties,
                                                              typeGUID,
                                                              typeName,
                                                              extendedProperties,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        return this.createBeanInRepository(userId,
                                           null,
                                           null,
                                           typeGUID,
                                           typeName,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           locationBuilder,
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
     * @param templateGUID unique identifier of the metadata element to copy
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
                                             String templateGUID,
                                             String qualifiedName,
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
                                                              displayName,
                                                              description,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        return this.createBeanFromTemplate(userId,
                                           null,
                                           null,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           locationBuilder,
                                           methodName);
    }


    /**
     * Update the location.
     *
     * @param userId calling user
     * @param locationGUID unique identifier of the location to update
     * @param locationGUIDParameterName parameter passing the locationGUID
     * @param qualifiedName unique name for the location - used in other configuration
     * @param displayName short display name for the location
     * @param description description of the governance location
     * @param additionalProperties additional properties for a governance location
     * @param suppliedTypeName type of location
     * @param extendedProperties  properties for a governance location subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateLocation(String              userId,
                                 String              locationGUID,
                                 String              locationGUIDParameterName,
                                 String              qualifiedName,
                                 String              displayName,
                                 String              description,
                                 Map<String, String> additionalProperties,
                                 String              suppliedTypeName,
                                 Map<String, Object> extendedProperties,
                                 String              methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, locationGUIDParameterName, methodName);
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
                                                              displayName,
                                                              description,
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
                                    locationGUID,
                                    locationGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    locationBuilder.getInstanceProperties(methodName),
                                    false,
                                    methodName);
    }


    /**
     * Mark the location as a Fixed Location.
     *
     * @param userId calling user
     * @param locationGUID unique identifier of location
     * @param locationGUIDParameterName parameter name supplying locationGUID
     * @param coordinates coordinate location
     * @param mapProjection scheme used for the coordinates
     * @param postalAddress postal address of the location
     * @param timeZone time zone of the location
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addFixedLocationClassification(String userId,
                                                String locationGUID,
                                                String locationGUIDParameterName,
                                                String coordinates,
                                                String mapProjection,
                                                String postalAddress,
                                                String timeZone,
                                                String methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, locationGUIDParameterName, methodName);

        LocationBuilder builder = new LocationBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
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
                                           methodName);
    }


    /**
     * Remove the Fixed Location designation from a location.
     *
     * @param userId calling user
     * @param locationGUID unique identifier of location
     * @param locationGUIDParameterName parameter name supplying locationGUID
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeFixedLocationClassification(String userId,
                                                   String locationGUID,
                                                   String locationGUIDParameterName,
                                                   String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, locationGUIDParameterName, methodName);

        this.removeClassificationFromRepository(userId,
                                                locationGUID,
                                                locationGUIDParameterName,
                                                OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                OpenMetadataAPIMapper.FIXED_LOCATION_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.FIXED_LOCATION_CLASSIFICATION_TYPE_GUID,
                                                methodName);
    }


    /**
     * Mark the location as a Secure Location.
     *
     * @param userId calling user
     * @param locationGUID unique identifier of location
     * @param locationGUIDParameterName parameter name supplying locationGUID
     * @param description description of security
     * @param level level of security
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addSecureLocationClassification(String userId,
                                                 String locationGUID,
                                                 String locationGUIDParameterName,
                                                 String description,
                                                 String level,
                                                 String methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, locationGUIDParameterName, methodName);

        LocationBuilder builder = new LocationBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           locationGUID,
                                           locationGUIDParameterName,
                                           OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                           OpenMetadataAPIMapper.SECURE_LOCATION_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.SECURE_LOCATION_CLASSIFICATION_TYPE_NAME,
                                           builder.getSecureLocationProperties(description, level, methodName),
                                           methodName);
    }


    /**
     * Remove the Secure Location designation from a location.
     *
     * @param userId calling user
     * @param locationGUID unique identifier of location
     * @param locationGUIDParameterName parameter name supplying locationGUID
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeSecureLocationClassification(String userId,
                                                    String locationGUID,
                                                    String locationGUIDParameterName,
                                                    String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                locationGUID,
                                                locationGUIDParameterName,
                                                OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                OpenMetadataAPIMapper.SECURE_LOCATION_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.SECURE_LOCATION_CLASSIFICATION_TYPE_GUID,
                                                methodName);
    }


    /**
     * Mark the location as a Cyber Location.
     *
     * @param userId calling user
     * @param locationGUID unique identifier of location
     * @param locationGUIDParameterName parameter name supplying locationGUID
     * @param networkAddress network address of the location
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addCyberLocationClassification(String userId,
                                                String locationGUID,
                                                String locationGUIDParameterName,
                                                String networkAddress,
                                                String methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, locationGUIDParameterName, methodName);

        LocationBuilder builder = new LocationBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           locationGUID,
                                           locationGUIDParameterName,
                                           OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                           OpenMetadataAPIMapper.CYBER_LOCATION_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.CYBER_LOCATION_CLASSIFICATION_TYPE_NAME,
                                           builder.getCyberLocationProperties(networkAddress, methodName),
                                           methodName);
    }


    /**
     * Remove the Cyber Location designation from a location.
     *
     * @param userId calling user
     * @param locationGUID unique identifier of location
     * @param locationGUIDParameterName parameter name supplying locationGUID
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeCyberLocationClassification(String userId,
                                                   String locationGUID,
                                                   String locationGUIDParameterName,
                                                   String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                locationGUID,
                                                locationGUIDParameterName,
                                                OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                OpenMetadataAPIMapper.CYBER_LOCATION_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.CYBER_LOCATION_CLASSIFICATION_TYPE_GUID,
                                                methodName);
    }


    /**
     * Create a parent-child relationship between two locations.
     *
     * @param userId calling user
     * @param locationParentGUID unique identifier of the parent location
     * @param locationParentGUIDParameterName parameter supplying the parent
     * @param locationChildGUID unique identifier of the child location
     * @param locationChildGUIDParameterName parameter supplying the child
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupNestedLocation(String userId,
                                    String locationParentGUID,
                                    String locationParentGUIDParameterName,
                                    String locationChildGUID,
                                    String locationChildGUIDParameterName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.linkElementToElement(userId,
                                  null,
                                  null,
                                  locationParentGUID,
                                  locationParentGUIDParameterName,
                                  OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                  locationChildGUID,
                                  locationChildGUIDParameterName,
                                  OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                  OpenMetadataAPIMapper.NESTED_LOCATION_TYPE_GUID,
                                  OpenMetadataAPIMapper.NESTED_LOCATION_TYPE_NAME,
                                  null,
                                  methodName);
    }


    /**
     * Remove a parent-child relationship between two locations.
     *
     * @param userId calling user
     * @param locationParentGUID unique identifier of the parent location
     * @param locationParentGUIDParameterName parameter supplying the parent
     * @param locationChildGUID unique identifier of the child location
     * @param locationChildGUIDParameterName parameter supplying the child
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearNestedLocation(String userId,
                                    String locationParentGUID,
                                    String locationParentGUIDParameterName,
                                    String locationChildGUID,
                                    String locationChildGUIDParameterName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      null,
                                      null,
                                      locationParentGUID,
                                      locationParentGUIDParameterName,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                      locationChildGUID,
                                      locationChildGUIDParameterName,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                      OpenMetadataAPIMapper.NESTED_LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.NESTED_LOCATION_TYPE_NAME,
                                      methodName);
    }


    /**
     * Create a peer relationship between two locations.
     *
     * @param userId calling user
     * @param locationOneGUID unique identifier of the first location
     * @param locationOneGUIDParameterName parameter supplying the first location
     * @param locationTwoGUID unique identifier of the second location
     * @param locationOneGUIDParameterName parameter supplying the second location
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupPeerLocations(String userId,
                                   String locationOneGUID,
                                   String locationOneGUIDParameterName,
                                   String locationTwoGUID,
                                   String locationTwoGUIDParameterName,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        this.linkElementToElement(userId,
                                  null,
                                  null,
                                  locationOneGUID,
                                  locationOneGUIDParameterName,
                                  OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                  locationTwoGUID,
                                  locationTwoGUIDParameterName,
                                  OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                  OpenMetadataAPIMapper.ADJACENT_LOCATION_TYPE_GUID,
                                  OpenMetadataAPIMapper.ADJACENT_LOCATION_TYPE_NAME,
                                  null,
                                  methodName);
    }


    /**
     * Remove a peer relationship between two locations.
     *
     * @param userId calling user
     * @param locationOneGUID unique identifier of the first location
     * @param locationOneGUIDParameterName parameter supplying the first location
     * @param locationTwoGUID unique identifier of the second location
     * @param locationTwoGUIDParameterName parameter supplying the second location
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearPeerLocations(String userId,
                                   String locationOneGUID,
                                   String locationOneGUIDParameterName,
                                   String locationTwoGUID,
                                   String locationTwoGUIDParameterName,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      null,
                                      null,
                                      locationOneGUID,
                                      locationOneGUIDParameterName,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                      locationTwoGUID,
                                      locationTwoGUIDParameterName,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                      OpenMetadataAPIMapper.ADJACENT_LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.ADJACENT_LOCATION_TYPE_NAME,
                                      methodName);
    }


    /**
     * Create a relationship between a location and an asset.
     *
     * @param userId calling user
     * @param locationGUID unique identifier of the location
     * @param locationGUIDParameterName parameter supplying the location
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName parameter supplying the asset
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAssetLocation(String userId,
                                   String locationGUID,
                                   String locationGUIDParameterName,
                                   String assetGUID,
                                   String assetGUIDParameterName,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        this.linkElementToElement(userId,
                                  null,
                                  null,
                                  locationGUID,
                                  locationGUIDParameterName,
                                  OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                  assetGUID,
                                  assetGUIDParameterName,
                                  OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                  OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_GUID,
                                  OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_NAME,
                                  null,
                                  methodName);
    }


    /**
     * Remove a relationship between a location and an asset.
     *
     * @param userId calling user
     * @param locationGUID unique identifier of the location
     * @param locationGUIDParameterName parameter supplying the location
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName parameter supplying the asset
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAssetLocation(String userId,
                                   String locationGUID,
                                   String locationGUIDParameterName,
                                   String assetGUID,
                                   String assetGUIDParameterName,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      null,
                                      null,
                                      locationGUID,
                                      locationGUIDParameterName,
                                      OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                      assetGUID,
                                      assetGUIDParameterName,
                                      OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                      OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                      OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_NAME,
                                      methodName);
    }


    /**
     * Remove the metadata element representing a location.
     *
     * @param userId calling user
     * @param locationGUID unique identifier of the metadata element to remove
     * @param locationGUIDParameterName parameter supplying the locationGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeLocation(String userId,
                               String locationGUID,
                               String locationGUIDParameterName,
                               String methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    null,
                                    null,
                                    locationGUID,
                                    locationGUIDParameterName,
                                    OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                                    OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                    null,
                                    null,
                                    methodName);
    }


    /**
     * Count the number of locations attached to an entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countKnownLocations(String   userId,
                                   String   elementGUID,
                                   String   methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      elementGUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                      OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_GUID,
                                      OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_NAME,
                                      methodName);
    }


    /**
     * Return the locations attached to an entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the feedback is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getLocations(String       userId,
                                 String       elementGUID,
                                 String       elementGUIDParameterName,
                                 String       elementTypeName,
                                 int          startingFrom,
                                 int          pageSize,
                                 String       methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        return this.getLocations(userId, elementGUID, elementGUIDParameterName, elementTypeName, supportedZones, startingFrom, pageSize, methodName);
    }


    /**
     * Return the locations attached to an entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the feedback is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getLocations(String       userId,
                                 String       elementGUID,
                                 String       elementGUIDParameterName,
                                 String       elementTypeName,
                                 List<String> serviceSupportedZones,
                                 int          startingFrom,
                                 int          pageSize,
                                 String       methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_GUID,
                                        OpenMetadataAPIMapper.ASSET_LOCATION_TYPE_NAME,
                                        OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
                                        methodName);
    }
}
