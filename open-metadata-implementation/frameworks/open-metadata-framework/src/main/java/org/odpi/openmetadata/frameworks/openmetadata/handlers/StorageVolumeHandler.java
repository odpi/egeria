/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.AttachedStorageProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.StorageVolumeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.StoredOnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * StorageVolumeHandler provides methods to define storage volumes and their relationships.
 */
public class StorageVolumeHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public StorageVolumeHandler(String             localServerName,
                                AuditLog           auditLog,
                                String             localServiceName,
                                OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.STORAGE_VOLUME.typeName);
    }


    /**
     * Create a new storage volume.
     *
     * @param userId                       userId of the user making the request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createStorageVolume(String                                userId,
                                      NewElementOptions                     newElementOptions,
                                      Map<String, ClassificationProperties> initialClassifications,
                                      StorageVolumeProperties               properties,
                                      RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "createStorageVolume";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a storage volume using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new storage volume.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing element to copy
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createStorageVolumeFromTemplate(String                                userId,
                                                  TemplateOptions                       templateOptions,
                                                  String                                templateGUID,
                                                  EntityProperties                      replacementProperties,
                                                  Map<String, ClassificationProperties> replacementClassifications,
                                                  Map<String, String>                   placeholderProperties,
                                                  RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                             UserNotAuthorizedException,
                                                                                                                             PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               replacementClassifications,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of a storage volume.
     *
     * @param userId                 userId of the user making the request.
     * @param storageVolumeGUID unique identifier of the storage volume (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateStorageVolume(String                  userId,
                                       String                  storageVolumeGUID,
                                       UpdateOptions           updateOptions,
                                       StorageVolumeProperties properties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName        = "updateStorageVolume";
        final String guidParameterName = "storageVolumeGUID";

        return super.updateElement(userId,
                                   storageVolumeGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Attach a storage volume to the IT infrastructure that it provides storage for.
     *
     * @param userId                 userId of the user making the request
     * @param itInfrastructureGUID   unique identifier of the element that the storage volume is attached to
     * @param storageVolumeGUID      unique identifier of the storage volume
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAttachedStorage(String                    userId,
                                    String                    itInfrastructureGUID,
                                    String                    storageVolumeGUID,
                                    MakeAnchorOptions         makeAnchorOptions,
                                    AttachedStorageProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName            = "linkAttachedStorage";
        final String end1GUIDParameterName = "itInfrastructureGUID";
        final String end2GUIDParameterName = "storageVolumeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(itInfrastructureGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(storageVolumeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ATTACHED_STORAGE_RELATIONSHIP.typeName,
                                                        itInfrastructureGUID,
                                                        storageVolumeGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a storage volume from the IT infrastructure that it provided storage for.
     *
     * @param userId                 userId of the user making the request.
     * @param itInfrastructureGUID   unique identifier of the element that the storage volume is attached to
     * @param storageVolumeGUID      unique identifier of the storage volume
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAttachedStorage(String        userId,
                                      String        itInfrastructureGUID,
                                      String        storageVolumeGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName            = "detachAttachedStorage";
        final String end1GUIDParameterName = "itInfrastructureGUID";
        final String end2GUIDParameterName = "storageVolumeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(itInfrastructureGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(storageVolumeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.ATTACHED_STORAGE_RELATIONSHIP.typeName,
                                                        itInfrastructureGUID,
                                                        storageVolumeGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a data store to the storage volume that its data is stored on.
     *
     * @param userId                 userId of the user making the request
     * @param dataStoreGUID          unique identifier of the data store
     * @param storageVolumeGUID      unique identifier of the storage volume
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkStoredOn(String             userId,
                             String             dataStoreGUID,
                             String             storageVolumeGUID,
                             MakeAnchorOptions  makeAnchorOptions,
                             StoredOnProperties relationshipProperties) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName            = "linkStoredOn";
        final String end1GUIDParameterName = "dataStoreGUID";
        final String end2GUIDParameterName = "storageVolumeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataStoreGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(storageVolumeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.STORED_ON_RELATIONSHIP.typeName,
                                                        dataStoreGUID,
                                                        storageVolumeGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a data store from the storage volume that its data was stored on.
     *
     * @param userId                 userId of the user making the request.
     * @param dataStoreGUID          unique identifier of the data store
     * @param storageVolumeGUID      unique identifier of the storage volume
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachStoredOn(String        userId,
                               String        dataStoreGUID,
                               String        storageVolumeGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName            = "detachStoredOn";
        final String end1GUIDParameterName = "dataStoreGUID";
        final String end2GUIDParameterName = "storageVolumeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataStoreGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(storageVolumeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.STORED_ON_RELATIONSHIP.typeName,
                                                        dataStoreGUID,
                                                        storageVolumeGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a storage volume.
     *
     * @param userId                 userId of the user making the request.
     * @param storageVolumeGUID unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteStorageVolume(String        userId,
                                    String        storageVolumeGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName        = "deleteStorageVolume";
        final String guidParameterName = "storageVolumeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(storageVolumeGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, storageVolumeGUID, deleteOptions);
    }


    /**
     * Returns the list of storage volumes with a particular name.
     *
     * @param userId                 userId of the user making the request
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getStorageVolumesByName(String       userId,
                                                                 String       name,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getStorageVolumesByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Return the properties of a specific storage volume.
     *
     * @param userId                 userId of the user making the request
     * @param storageVolumeGUID unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getStorageVolumeByGUID(String     userId,
                                                          String     storageVolumeGUID,
                                                          GetOptions getOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getStorageVolumeByGUID";

        return super.getRootElementByGUID(userId,
                                          storageVolumeGUID,
                                          getOptions,
                                          methodName);
    }


    /**
     * Retrieve the list of storage volume metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findStorageVolumes(String        userId,
                                                            String        searchString,
                                                            SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName  = "findStorageVolumes";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
