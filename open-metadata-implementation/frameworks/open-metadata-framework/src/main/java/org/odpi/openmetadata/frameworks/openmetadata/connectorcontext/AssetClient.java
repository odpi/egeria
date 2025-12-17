/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.APIEndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FolderHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.NestedFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.DeployedOnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SupportedSoftwareCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with Asset objects: Data Assets, Processes and Infrastructure.
 */
public class AssetClient extends ConnectorContextClientBase
{
    private final AssetHandler   assetHandler;


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
    public AssetClient(ConnectorContextBase     parentContext,
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

        this.assetHandler = new AssetHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template client to copy
     * @param specificTypeName type name override
     */
    public AssetClient(AssetClient template,
                       String      specificTypeName)
    {
        super(template);

        this.assetHandler = new AssetHandler(template.assetHandler, specificTypeName);
    }


    /**
     * Create a new asset.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createAsset(NewElementOptions                     newElementOptions,
                              Map<String, ClassificationProperties> initialClassifications,
                              AssetProperties                       properties,
                              RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        String assetGUID = assetHandler.createAsset(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(assetGUID);
        }

        return assetGUID;
    }


    /**
     * Create a new action and link it to the supplied role and targets (if applicable).
     *
     * @param originatorGUID            optional originator element (such as a person or Governance Service)
     * @param actionSponsorGUID         optional element that maintains the action on their list
     * @param assignToActorGUID         optional actor to assign the action to
     * @param anchorOptions             how should the new action be anchored?
     * @param initialClassifications    map of classifications to add to the new action
     * @param newActionTargets optional list of elements that the action is to target
     * @param properties                properties of the  action
     * @return unique identifier of the action
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public String createAction(String                                originatorGUID,
                               String                                actionSponsorGUID,
                               String                                assignToActorGUID,
                               AnchorOptions                         anchorOptions,
                               Map<String, ClassificationProperties> initialClassifications,
                               List<NewActionTarget>                 newActionTargets,
                               ActionProperties                      properties) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        return assetHandler.createAction(connectorUserId, originatorGUID, actionSponsorGUID, assignToActorGUID, anchorOptions, initialClassifications, newActionTargets, properties);
    }


    /**
     * Create a new metadata element to represent an asset using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAssetFromTemplate(TemplateOptions        templateOptions,
                                          String                 templateGUID,
                                          EntityProperties       replacementProperties,
                                          Map<String, String>    placeholderProperties,
                                          RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        String assetGUID = assetHandler.createAssetFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(assetGUID);
        }

        return assetGUID;
    }


    /**
     * Retrieve the metadata source's unique identifier (GUID) or if it is not defined, create the software server capability
     * for this service.
     *
     * @param metadataSourceQualifiedName unique name of the software capability that represents this integration service
     * @param ownerGUID unique identifier of the owner of the metadata collection
     * @param ownerName name of owner from config
     * @param ownerUserId userId for the owner
     *
     * @return unique identifier of the metadata source
     *
     * @throws InvalidParameterException one of the parameters passed (probably on initialize) is invalid
     * @throws UserNotAuthorizedException the integration daemon's userId does not have access to the partner OMAS
     * @throws PropertyServerException there is a problem in the remote server running the partner OMAS
     */
    public String setUpMetadataSource(String                metadataSourceQualifiedName,
                                      String                ownerGUID,
                                      String                ownerName,
                                      String                ownerUserId,
                                      ElementOriginCategory originCategory) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return assetHandler.setUpMetadataSource(connectorUserId, metadataSourceQualifiedName, ownerGUID, ownerName, ownerUserId, originCategory);
    }


    /**
     * Update the properties of an asset.
     *
     * @param assetGUID       unique identifier of the asset (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateAsset(String          assetGUID,
                               UpdateOptions   updateOptions,
                               AssetProperties properties) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        boolean updateOccurred = assetHandler.updateAsset(connectorUserId, assetGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(assetGUID);
        }

        return updateOccurred;
    }


    /**
     * Create a relationship that represents the deployment of an IT infrastructure asset to a specific deployment destination (another asset).
     *
     * @param assetGUID       unique identifier of the asset
     * @param destinationGUID           unique identifier of the destination asset
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deployITAsset(String                assetGUID,
                              String                destinationGUID,
                              MakeAnchorOptions     metadataSourceOptions,
                              DeployedOnProperties  relationshipProperties) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        assetHandler.deployITAsset(connectorUserId, assetGUID, destinationGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Remove a DeployedOn relationship.
     *
     * @param assetGUID       unique identifier of the asset
     * @param destinationGUID           unique identifier of the destination asset
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void unDeployITAsset(String        assetGUID,
                                String        destinationGUID,
                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        assetHandler.unDeployITAsset(connectorUserId, assetGUID, destinationGUID, deleteOptions);
    }


    /**
     * Create a relationship that links a software capability to an infrastructure asset like a software server.
     *
     * @param assetGUID       unique identifier of the asset
     * @param capabilityGUID           unique identifier of the destination asset
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSoftwareCapability(String                                assetGUID,
                                       String                                capabilityGUID,
                                       MakeAnchorOptions                     makeAnchorOptions,
                                       SupportedSoftwareCapabilityProperties relationshipProperties) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        assetHandler.linkSoftwareCapability(connectorUserId, assetGUID, capabilityGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Remove a relationship that links a software capability to an infrastructure asset like a software server.
     *
     * @param assetGUID       unique identifier of the asset
     * @param capabilityGUID           unique identifier of the destination asset
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSoftwareCapability(String        assetGUID,
                                         String        capabilityGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        assetHandler.detachSoftwareCapability(connectorUserId, assetGUID, capabilityGUID, deleteOptions);
    }


    /**
     * Attach a data set to another asset (typically a data store) that is supplying the data.
     *
     * @param dataSetGUID          unique identifier of the first person profile
     * @param dataContentAssetGUID          unique identifier of the second person profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDataSetContent(String                   dataSetGUID,
                                   String                   dataContentAssetGUID,
                                   MakeAnchorOptions        metadataSourceOptions,
                                   DataSetContentProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        assetHandler.linkDataSetContent(connectorUserId, dataSetGUID, dataContentAssetGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a data set from another asset that was supplying the data and is no more.
     *
     * @param dataSetGUID          unique identifier of the first person profile
     * @param dataContentAssetGUID          unique identifier of the second person profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDataSetContent(String        dataSetGUID,
                                     String        dataContentAssetGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        assetHandler.detachDataSetContent(connectorUserId, dataSetGUID, dataContentAssetGUID, deleteOptions);
    }


    /**
     * Attach an API to an endpoint
     *
     * @param deployedAPIGUID          unique identifier of the super team
     * @param endpointGUID            unique identifier of the subteam
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAPIEndpoint(String                deployedAPIGUID,
                                String                endpointGUID,
                                MakeAnchorOptions     makeAnchorOptions,
                                APIEndpointProperties relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        assetHandler.linkAPIEndpoint(connectorUserId, deployedAPIGUID, endpointGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an API from an endpoint
     *
     * @param deployedAPIGUID          unique identifier of the API
     * @param endpointGUID            unique identifier of the endpoint
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAPIEndpoint(String        deployedAPIGUID,
                                  String        endpointGUID,
                                  DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        assetHandler.detachAPIEndpoint(connectorUserId, deployedAPIGUID, endpointGUID, deleteOptions);
    }


    /**
     * Attach a child process to its parent.
     *
     * @param parentProcessGUID       unique identifier of the parent process
     * @param childProcessGUID            unique identifier of the child process
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkProcessHierarchy(String                     parentProcessGUID,
                                     String                     childProcessGUID,
                                     MakeAnchorOptions          makeAnchorOptions,
                                     ProcessHierarchyProperties relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        assetHandler.linkProcessHierarchy(connectorUserId, parentProcessGUID, childProcessGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a child process from its parent.
     *
     * @param parentProcessGUID              unique identifier of the parent process
     * @param childProcessGUID          unique identifier of the child process
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachProcessHierarchy(String        parentProcessGUID,
                                       String        childProcessGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        assetHandler.detachProcessHierarchy(connectorUserId, parentProcessGUID, childProcessGUID, deleteOptions);
    }


    /**
     * Attach a file to a folder.
     *
     * @param folderGUID               unique identifier of the folder
     * @param fileGUID         unique identifier of the associated file
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedFiles(String                folderGUID,
                                String                fileGUID,
                                MakeAnchorOptions     metadataSourceOptions,
                                NestedFileProperties relationshipProperties) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        assetHandler.linkNestedFiles(connectorUserId, folderGUID, fileGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a file from its folder.
     *
     * @param folderGUID               unique identifier of the folder
     * @param fileGUID         unique identifier of the associated file
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNestedFile(String        folderGUID,
                                 String        fileGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        assetHandler.detachNestedFile(connectorUserId, folderGUID, fileGUID, deleteOptions);
    }



    /**
     * Attach a file to a folder.
     *
     * @param folderGUID               unique identifier of the folder
     * @param fileGUID         unique identifier of the associated file
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkLinkedFiles(String                folderGUID,
                                String                fileGUID,
                                MakeAnchorOptions     metadataSourceOptions,
                                NestedFileProperties  relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        assetHandler.linkLinkedFiles(connectorUserId, folderGUID, fileGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a file from its folder.
     *
     * @param folderGUID               unique identifier of the folder
     * @param fileGUID         unique identifier of the associated file
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachLinkedFile(String        folderGUID,
                                 String        fileGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        assetHandler.detachLinkedFile(connectorUserId, folderGUID, fileGUID, deleteOptions);
    }


    /**
     * Attach folders in a hierarchy.
     *
     * @param parentFolderGUID               unique identifier of the parent folder
     * @param childFolderGUID         unique identifier of the associated child folder
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkFolderHierarchy(String                    parentFolderGUID,
                                    String                    childFolderGUID,
                                    MakeAnchorOptions         makeAnchorOptions,
                                    FolderHierarchyProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        assetHandler.linkFolderHierarchy(connectorUserId, parentFolderGUID, childFolderGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a child folder from its parent.
     *
     * @param parentFolderGUID               unique identifier of the parent folder
     * @param childFolderGUID         unique identifier of the associated child folder
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachFolderHierarchy(String        parentFolderGUID,
                                      String        childFolderGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        assetHandler.detachFolderHierarchy(connectorUserId, parentFolderGUID, childFolderGUID, deleteOptions);
    }


    /**
     * Delete a asset.
     *
     * @param assetGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteAsset(String        assetGUID,
                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        assetHandler.deleteAsset(connectorUserId, assetGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(assetGUID);
        }
    }


    /**
     * Returns the list of assets with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAssetsByName(String       name,
                                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        return assetHandler.getAssetsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Returns the list of assets with a particular deployedImplementationType.
     *
     * @param name                   deployedImplementationType of the element to return - match is full text match
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAssetsByDeployedImplementationType(String       name,
                                                                               QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        return assetHandler.getAssetsByDeployedImplementationType(connectorUserId, name, queryOptions);
    }


    /**
     * Returns the list of assets with a particular assetGUID.
     *
     * @param assetGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query

     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getDeployedITAssets(String       assetGUID,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        return assetHandler.getDeployedITAssets(connectorUserId, assetGUID, queryOptions);
    }




    /**
     * Returns the list of assets providing data to the data set.
     *
     * @param assetGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getDataSetContents(String       assetGUID,
                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        return assetHandler.getDataSetContents(connectorUserId, assetGUID, queryOptions);
    }


    /**
     * Returns the list of assets providing data to the data set.
     *
     * @param assetGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSupportedDataSets(String       assetGUID,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        return assetHandler.getSupportedDataSets(connectorUserId, assetGUID, queryOptions);
    }


    /**
     * Returns the list of file assets catalogued under the folder asset.
     *
     * @param folderGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getFilesInFolder(String       folderGUID,
                                                          QueryOptions queryOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        return assetHandler.getFilesInFolder(connectorUserId, folderGUID, queryOptions);
    }



    /**
     * Returns the list of file assets catalogued under the folder asset.
     *
     * @param folderGUID              unique identifier of the starting element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getFilesInFolder(String folderGUID,
                                                          int    startFrom,
                                                          int    pageSize) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        return assetHandler.getFilesInFolder(connectorUserId, folderGUID, this.getQueryOptions(startFrom, pageSize));
    }



    /**
     * Return the properties of a specific asset.
     *
     * @param assetGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getAssetByGUID(String     assetGUID,
                                                  GetOptions getOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return assetHandler.getAssetByGUID(connectorUserId, assetGUID, getOptions);
    }


    /**
     * Return the properties of a specific governance definition.
     *
     * @param name unique name of the required element
     * @param propertyName name of the property to query (default is qualifiedName)
     * @param getOptions  multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getAssetByUniqueName(String       name,
                                                        String       propertyName,
                                                        GetOptions   getOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        return assetHandler.getAssetByUniqueName(connectorUserId, name, propertyName, getOptions);
    }


    /**
     * Retrieve the list of assets metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned assets include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findAssets(String        searchString,
                                                    SearchOptions searchOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return assetHandler.findAssets(connectorUserId, searchString, searchOptions);
    }



    /**
     * Add an element to an integration connector's workload.
     *
     * @param integrationConnectorGUID        unique identifier of the integration connector.
     * @param makeAnchorOptions options to control access to open metadata
     * @param catalogTargetProperties  properties describing the relationship characteristics.
     * @param elementGUID           unique identifier of the target element.
     * @return relationship GUID
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCatalogTarget(String                  integrationConnectorGUID,
                                   String                  elementGUID,
                                   MakeAnchorOptions       makeAnchorOptions,
                                   CatalogTargetProperties catalogTargetProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        return assetHandler.addCatalogTarget(connectorUserId,
                                             integrationConnectorGUID,
                                             elementGUID,
                                             makeAnchorOptions,
                                             catalogTargetProperties);
    }


    /**
     * Update the properties of a catalog target relationship.
     *
     * @param relationshipGUID     unique identifier of the relationship
     * @param updateOptions        provides a structure for the additional options when updating a relationship.
     * @param catalogTargetProperties properties describing the catalog target processing characteristics.
     */
    public void updateCatalogTarget(String                  relationshipGUID,
                                    UpdateOptions           updateOptions,
                                    CatalogTargetProperties catalogTargetProperties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        assetHandler.updateCatalogTarget(connectorUserId, relationshipGUID, updateOptions, catalogTargetProperties);
    }


    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param relationshipGUID unique identifier of the relationship.
     * @param getOptions options to control the retrieve
     *
     * @return details of the integration connector and the elements it is to catalog
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public OpenMetadataRelationship getCatalogTarget(String     relationshipGUID,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return assetHandler.getCatalogTarget(connectorUserId, relationshipGUID, getOptions);
    }


    /**
     * Return a list of elements that are target elements for an integration connector.
     *
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param queryOptions   options for query
     * @return list of member details
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getCatalogTargets(String       integrationConnectorGUID,
                                                           QueryOptions queryOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        return assetHandler.getCatalogTargets(connectorUserId, integrationConnectorGUID, queryOptions);
    }


    /**
     * Remove an element from an integration connector's workload.
     *
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param elementGUID    unique identifier of the element.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeCatalogTarget(String        integrationConnectorGUID,
                                    String        elementGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        assetHandler.removeCatalogTarget(connectorUserId, integrationConnectorGUID, elementGUID, deleteOptions);
    }


    /**
     * Remove an element from an integration connector's workload.
     *
     * @param relationshipGUID unique identifier of the relationship.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeCatalogTarget(String        relationshipGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        assetHandler.removeCatalogTarget(connectorUserId, relationshipGUID, deleteOptions);
    }


}
