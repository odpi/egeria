/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.StorageVolumeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.AttachedStorageProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.StorageVolumeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.StoredOnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with storage volume elements.
 */
public class StorageVolumeClient extends ConnectorContextClientBase
{
    private final StorageVolumeHandler storageVolumeHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public StorageVolumeClient(ConnectorContextBase     parentContext,
                               String                   localServerName,
                               String                   localServiceName,
                               String                   connectorUserId,
                               String                   connectorGUID,
                               String                   externalSourceGUID,
                               String                   externalSourceName,
                               OpenMetadataClient       openMetadataClient,
                               AuditLog                 auditLog,
                               int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.storageVolumeHandler = new StorageVolumeHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new storage volume.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createStorageVolume(NewElementOptions                     newElementOptions,
                                      Map<String, ClassificationProperties> initialClassifications,
                                      StorageVolumeProperties               properties,
                                      RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        String elementGUID = storageVolumeHandler.createStorageVolume(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a storage volume using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new storage volume.
     *
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing storage volume to copy
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createStorageVolumeFromTemplate(TemplateOptions                       templateOptions,
                                                  String                                templateGUID,
                                                  EntityProperties                      replacementProperties,
                                                  Map<String, ClassificationProperties> replacementClassifications,
                                                  Map<String, String>                   placeholderProperties,
                                                  RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                             UserNotAuthorizedException,
                                                                                                                             PropertyServerException
    {
        String elementGUID = storageVolumeHandler.createStorageVolumeFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, replacementClassifications, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a storage volume.
     *
     * @param storageVolumeGUID unique identifier of the storage volume (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateStorageVolume(String                  storageVolumeGUID,
                                       UpdateOptions           updateOptions,
                                       StorageVolumeProperties properties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        boolean updateOccurred = storageVolumeHandler.updateStorageVolume(connectorUserId, storageVolumeGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getActivityReportWriter() != null))
        {
            parentContext.getActivityReportWriter().reportElementUpdate(storageVolumeGUID);
        }

        return updateOccurred;
    }


    /**
     * Attach a storage volume to the IT infrastructure that it provides storage for.
     *
     * @param itInfrastructureGUID   unique identifier of the element that the storage volume is attached to
     * @param storageVolumeGUID      unique identifier of the storage volume
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAttachedStorage(String                    itInfrastructureGUID,
                                    String                    storageVolumeGUID,
                                    MakeAnchorOptions         makeAnchorOptions,
                                    AttachedStorageProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        storageVolumeHandler.linkAttachedStorage(connectorUserId, itInfrastructureGUID, storageVolumeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a storage volume from the IT infrastructure that it provided storage for.
     *
     * @param itInfrastructureGUID   unique identifier of the element that the storage volume is attached to
     * @param storageVolumeGUID      unique identifier of the storage volume
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAttachedStorage(String        itInfrastructureGUID,
                                      String        storageVolumeGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        storageVolumeHandler.detachAttachedStorage(connectorUserId, itInfrastructureGUID, storageVolumeGUID, deleteOptions);
    }


    /**
     * Attach a data store to the storage volume that its data is stored on.
     *
     * @param dataStoreGUID          unique identifier of the data store
     * @param storageVolumeGUID      unique identifier of the storage volume
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkStoredOn(String             dataStoreGUID,
                             String             storageVolumeGUID,
                             MakeAnchorOptions  makeAnchorOptions,
                             StoredOnProperties relationshipProperties) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        storageVolumeHandler.linkStoredOn(connectorUserId, dataStoreGUID, storageVolumeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a data store from the storage volume that its data was stored on.
     *
     * @param dataStoreGUID          unique identifier of the data store
     * @param storageVolumeGUID      unique identifier of the storage volume
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachStoredOn(String        dataStoreGUID,
                               String        storageVolumeGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        storageVolumeHandler.detachStoredOn(connectorUserId, dataStoreGUID, storageVolumeGUID, deleteOptions);
    }


    /**
     * Delete a storage volume.
     *
     * @param storageVolumeGUID unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteStorageVolume(String        storageVolumeGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        storageVolumeHandler.deleteStorageVolume(connectorUserId, storageVolumeGUID, deleteOptions);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementDelete(storageVolumeGUID);
        }
    }


    /**
     * Returns the list of storage volumes with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getStorageVolumesByName(String       name,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        return storageVolumeHandler.getStorageVolumesByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific storage volume.
     *
     * @param storageVolumeGUID unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getStorageVolumeByGUID(String     storageVolumeGUID,
                                                          GetOptions getOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        return storageVolumeHandler.getStorageVolumeByGUID(connectorUserId, storageVolumeGUID, getOptions);
    }


    /**
     * Retrieve the list of storage volume metadata elements that contain the search string.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findStorageVolumes(String        searchString,
                                                            SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        return storageVolumeHandler.findStorageVolumes(connectorUserId, searchString, searchOptions);
    }
}
