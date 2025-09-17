/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.AssetGraphMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.AssetLineageGraphMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.APIEndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FolderHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.NestedFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.DeployedOnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionRequesterProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.LineageRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.AssignmentType;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * AssetHandler provides methods to define all types of assets and their relationships
 */
public class AssetHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public AssetHandler(String             localServerName,
                        AuditLog           auditLog,
                        String             localServiceName,
                        OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.ASSET.typeName);
    }


    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     * @param assetTypeName          subtype of asset to control handler
     */
    public AssetHandler(String             localServerName,
                        AuditLog           auditLog,
                        String             localServiceName,
                        OpenMetadataClient openMetadataClient,
                        String             assetTypeName)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              assetTypeName);
    }


    /**
     * Create a new handler.
     *
     * @param template        properties to copy
     * @param specificTypeName   subtype to control handler
     */
    public AssetHandler(AssetHandler template,
                        String       specificTypeName)
    {
        super(template, specificTypeName);
    }


    /**
     * Create a new asset.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createAsset(String                                userId,
                              NewElementOptions                     newElementOptions,
                              Map<String, ClassificationProperties> initialClassifications,
                              AssetProperties                       properties,
                              RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "createAsset";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Add a simple asset description linked to a connection object for a CSV file.
     *
     * @param displayName display name for the file in the catalog
     * @param description description of the file in the catalog
     * @param pathName full path of the file - used to access the file through the connector
     * @param columnHeaders does the first line of the file contain the column names. If not pass the list of column headers.
     * @param delimiterCharacter what is the delimiter character - null for default of comma
     * @param quoteCharacter what is the character to group a field that contains delimiter characters
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public  String  addCSVFileToCatalog(String       userId,
                                        String       displayName,
                                        String       description,
                                        String       pathName,
                                        List<String> columnHeaders,
                                        Character    delimiterCharacter,
                                        Character    quoteCharacter) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        // todo
        return null;
    }


    /**
     * Create a new  action and link it to the supplied role and targets (if applicable).
     *
     * @param userId                    calling user
     * @param originatorGUID            optional originator element (such as a person or Governance Service)
     * @param actionSponsorGUID         optional element that maintains the action on their list
     * @param assignToActorGUID         optional actor to assign the action to
     * @param newActionTargets optional list of elements that the action is to target
     * @param properties                properties of the  action
     * @return unique identifier of the action
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public String createAction(String                            userId,
                               String                            originatorGUID,
                               String                            actionSponsorGUID,
                               String                            assignToActorGUID,
                               AnchorOptions                     anchorOptions,
                               Map<String, NewElementProperties> initialClassifications,
                               List<NewActionTarget>             newActionTargets,
                               ActionProperties                  properties) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName                 = "createAction";
        final String toDoPropertiesName         = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateObject(properties, toDoPropertiesName, methodName);
        propertyHelper.validateMandatoryName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        NewElementOptions newElementOptions = new NewElementOptions(anchorOptions);

        if (properties.getRequestedTime() == null)
        {
            properties.setRequestedTime(new Date());
        }

        NewElementProperties parentRelationshipProperties = null;

        if (originatorGUID != null)
        {
            parentRelationshipProperties = relationshipBuilder.getNewElementProperties(new ActionRequesterProperties());

            newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
            newElementOptions.setParentGUID(originatorGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName);
        }
        String toDoGUID = openMetadataClient.createMetadataElementInStore(userId,
                                                                          metadataElementTypeName,
                                                                          newElementOptions,
                                                                          initialClassifications,
                                                                          elementBuilder.getNewElementProperties(properties),
                                                                          parentRelationshipProperties);

        if (toDoGUID != null)
        {
            if (assignToActorGUID != null)
            {
                openMetadataClient.createRelatedElementsInStore(userId,
                                                                OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                assignToActorGUID,
                                                                toDoGUID,
                                                                anchorOptions,
                                                                relationshipBuilder.getNewElementProperties(new AssignmentScopeProperties()));
            }

            if (actionSponsorGUID != null)
            {
                openMetadataClient.createRelatedElementsInStore(userId,
                                                                OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                actionSponsorGUID,
                                                                toDoGUID,
                                                                anchorOptions,
                                                                new NewElementProperties(propertyHelper.addStringProperty(null,
                                                                                                                          OpenMetadataProperty.ASSIGNMENT_TYPE.name,
                                                                                                                          AssignmentType.SPONSOR.getName())));
            }

            if (newActionTargets != null)
            {
                for (NewActionTarget newActionTarget : newActionTargets)
                {
                    if (newActionTarget != null)
                    {
                        ActionTargetProperties actionTargetProperties = new ActionTargetProperties();

                        actionTargetProperties.setActionTargetName(newActionTarget.getActionTargetName());

                        openMetadataClient.createRelatedElementsInStore(userId,
                                                                        OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                        toDoGUID,
                                                                        newActionTarget.getActionTargetGUID(),
                                                                        anchorOptions,
                                                                        relationshipBuilder.getNewElementProperties(actionTargetProperties));
                    }
                }
            }
        }

        return toDoGUID;
    }

    
    /**
     * Assign an action to a new actor.
     *
     * @param userId    calling user
     * @param actionGUID  unique identifier of the action
     * @param actorGUID actor to assign the action to
     * @param updateOptions  options to control access to open metadata
     * @param relationshipProperties the properties of the relationship
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public void reassignAction(String                    userId,
                               String                    actionGUID,
                               String                    actorGUID,
                               UpdateOptions             updateOptions,
                               AssignmentScopeProperties relationshipProperties) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName              = "reassignAction";
        final String toDoGUIDParameterName   = "actionGUID";
        final String parentGUIDParameterName = "actorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(actionGUID, toDoGUIDParameterName, methodName);
        propertyHelper.validateGUID(actorGUID, parentGUIDParameterName, methodName);

        RelatedMetadataElementList assignedActors = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                  actionGUID,
                                                                                                  2,
                                                                                                  OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                                                  new QueryOptions(updateOptions));

        if ((assignedActors != null) && (assignedActors.getElementList() != null))
        {
            for (RelatedMetadataElement assignedActor : assignedActors.getElementList())
            {
                openMetadataClient.deleteRelationshipInStore(userId,
                                                             assignedActor.getRelationshipGUID(),
                                                             new DeleteOptions(updateOptions));
            }
        }

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                        actorGUID,
                                                        actionGUID,
                                                        updateOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Update the properties associated with an Action Target.
     *
     * @param userId                 calling user
     * @param actionTargetGUID       unique identifier of the action target relationship
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param actionTargetProperties properties to change
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public void updateActionTargetProperties(String                 userId,
                                             String                 actionTargetGUID,
                                             UpdateOptions          updateOptions,
                                             ActionTargetProperties actionTargetProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName     = "updateActionTargetProperties";
        final String propertiesName = "actionTargetProperties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateObject(actionTargetProperties, propertiesName, methodName);

        openMetadataClient.updateRelationshipInStore(userId,
                                                     actionTargetGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getNewElementProperties(actionTargetProperties));
    }


    /**
     * Create a new metadata element to represent an asset using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAssetFromTemplate(String                 userId,
                                          TemplateOptions        templateOptions,
                                          String                 templateGUID,
                                          ElementProperties      replacementProperties,
                                          Map<String, String>    placeholderProperties,
                                          RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of an asset.
     *
     * @param userId                 userId of user making request.
     * @param assetGUID       unique identifier of the asset (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateAsset(String          userId,
                            String          assetGUID,
                            UpdateOptions   updateOptions,
                            AssetProperties properties) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String methodName        = "updateAsset";
        final String guidParameterName = "assetGUID";

        super.updateElement(userId,
                            assetGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Create a relationship that represents the deployment of an IT infrastructure asset to a specific deployment destination (another asset).
     *
     * @param userId                 userId of user making request
     * @param assetGUID       unique identifier of the asset
     * @param destinationGUID           unique identifier of the destination asset
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deployITAsset(String                userId,
                              String                assetGUID,
                              String                destinationGUID,
                              MetadataSourceOptions metadataSourceOptions,
                              DeployedOnProperties  relationshipProperties) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName            = "deployITAsset";
        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "destinationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(destinationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName,
                                                        assetGUID,
                                                        destinationGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Remove a DeployedOn relationship.
     *
     * @param userId                 userId of user making request.
     * @param assetGUID       unique identifier of the asset
     * @param destinationGUID           unique identifier of the destination asset
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void unDeployITAsset(String        userId,
                                String        assetGUID,
                                String        destinationGUID,
                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String methodName = "unDeployITAsset";

        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "destinationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(destinationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName,
                                                        destinationGUID,
                                                        assetGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a data set to another asset (typically a data store) that is supplying the data.
     *
     * @param userId                 userId of user making request
     * @param dataSetGUID          unique identifier of the first person profile
     * @param dataContentAssetGUID          unique identifier of the second person profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDataSetContent(String                   userId,
                                   String                   dataSetGUID,
                                   String                   dataContentAssetGUID,
                                   MetadataSourceOptions    metadataSourceOptions,
                                   DataSetContentProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName            = "linkDataSetContent";
        final String end1GUIDParameterName = "dataSetGUID";
        final String end2GUIDParameterName = "dataContentAssetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataSetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataContentAssetGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                        dataSetGUID,
                                                        dataContentAssetGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a data set from another asset that was supplying the data and is no more.
     *
     * @param userId                 userId of user making request.
     * @param dataSetGUID          unique identifier of the first person profile
     * @param dataContentAssetGUID          unique identifier of the second person profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDataSetContent(String        userId,
                                     String        dataSetGUID,
                                     String        dataContentAssetGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachDataSetContent";
        final String end1GUIDParameterName = "dataSetGUID";
        final String end2GUIDParameterName = "dataContentAssetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataSetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataContentAssetGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                        dataSetGUID,
                                                        dataContentAssetGUID,
                                                        deleteOptions);
    }


    /**
     * Attach an API to an endpoint
     *
     * @param userId                 userId of user making request
     * @param deployedAPIGUID          unique identifier of the super team
     * @param endpointGUID            unique identifier of the subteam
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAPIEndpoint(String                 userId,
                                String                 deployedAPIGUID,
                                String                 endpointGUID,
                                MetadataSourceOptions  metadataSourceOptions,
                                APIEndpointProperties  relationshipProperties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName            = "linkAPIEndpoint";
        final String end1GUIDParameterName = "deployedAPIGUID";
        final String end2GUIDParameterName = "endpointGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(deployedAPIGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(endpointGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.API_ENDPOINT_RELATIONSHIP.typeName,
                                                        deployedAPIGUID,
                                                        endpointGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an API from an endpoint
     *
     * @param userId                 userId of user making request.
     * @param deployedAPIGUID          unique identifier of the API
     * @param endpointGUID            unique identifier of the endpoint
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAPIEndpoint(String        userId,
                                  String        deployedAPIGUID,
                                  String        endpointGUID,
                                  DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String methodName = "detachAPIEndpoint";
        final String end1GUIDParameterName = "deployedAPIGUID";
        final String end2GUIDParameterName = "endpointGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(deployedAPIGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(endpointGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.API_ENDPOINT_RELATIONSHIP.typeName,
                                                        deployedAPIGUID,
                                                        endpointGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a child process to its parent.
     *
     * @param userId                 userId of user making request
     * @param parentProcessGUID       unique identifier of the parent process
     * @param childProcessGUID            unique identifier of the child process
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkProcessHierarchy(String                     userId,
                                     String                     parentProcessGUID,
                                     String                     childProcessGUID,
                                     MetadataSourceOptions      metadataSourceOptions,
                                     ProcessHierarchyProperties relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName            = "linkProcessHierarchy";
        final String end1GUIDParameterName = "parentProcessGUID";
        final String end2GUIDParameterName = "childProcessGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentProcessGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(childProcessGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName,
                                                        parentProcessGUID,
                                                        childProcessGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a child process from its parent.
     *
     * @param userId                 userId of user making request.
     * @param parentProcessGUID              unique identifier of the parent process
     * @param childProcessGUID          unique identifier of the child process
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachProcessHierarchy(String        userId,
                                       String        parentProcessGUID,
                                       String        childProcessGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName = "detachProcessHierarchy";
        final String end1GUIDParameterName = "parentProcessGUID";
        final String end2GUIDParameterName = "childProcessGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentProcessGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(childProcessGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName,
                                                        childProcessGUID,
                                                        childProcessGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a file to a folder.
     *
     * @param userId                 userId of user making request
     * @param folderGUID               unique identifier of the folder
     * @param fileGUID         unique identifier of the associated file
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedFiles(String                userId,
                                String                folderGUID,
                                String                fileGUID,
                                MetadataSourceOptions metadataSourceOptions,
                                NestedFileProperties  relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName            = "linkNestedFiles";
        final String end1GUIDParameterName = "folderGUID";
        final String end2GUIDParameterName = "fileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(folderGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(fileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName,
                                                        folderGUID,
                                                        fileGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a file from its folder.
     *
     * @param userId                 userId of user making request.
     * @param folderGUID               unique identifier of the folder
     * @param fileGUID         unique identifier of the associated file
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNestedFile(String        userId,
                                 String        folderGUID,
                                 String        fileGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "detachNestedFile";
        final String end1GUIDParameterName = "folderGUID";
        final String end2GUIDParameterName = "fileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(folderGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(fileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName,
                                                        folderGUID,
                                                        fileGUID,
                                                        deleteOptions);
    }



    /**
     * Attach a file to a folder.
     *
     * @param userId                 userId of user making request
     * @param folderGUID               unique identifier of the folder
     * @param fileGUID         unique identifier of the associated file
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkLinkedFiles(String                userId,
                                String                folderGUID,
                                String                fileGUID,
                                MetadataSourceOptions metadataSourceOptions,
                                NestedFileProperties  relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName            = "linkLinkedFiles";
        final String end1GUIDParameterName = "folderGUID";
        final String end2GUIDParameterName = "fileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(folderGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(fileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.LINKED_FILE_RELATIONSHIP.typeName,
                                                        folderGUID,
                                                        fileGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a file from its folder.
     *
     * @param userId                 userId of user making request.
     * @param folderGUID               unique identifier of the folder
     * @param fileGUID         unique identifier of the associated file
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachLinkedFile(String        userId,
                                 String        folderGUID,
                                 String        fileGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "detachLinkedFile";
        final String end1GUIDParameterName = "folderGUID";
        final String end2GUIDParameterName = "fileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(folderGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(fileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.LINKED_FILE_RELATIONSHIP.typeName,
                                                        folderGUID,
                                                        fileGUID,
                                                        deleteOptions);
    }


    /**
     * Attach folders in a hierarchy.
     *
     * @param userId                 userId of user making request
     * @param parentFolderGUID               unique identifier of the parent folder
     * @param childFolderGUID         unique identifier of the associated child folder
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkFolderHierarchy(String                    userId,
                                    String                    parentFolderGUID,
                                    String                    childFolderGUID,
                                    MetadataSourceOptions     metadataSourceOptions,
                                    FolderHierarchyProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName            = "linkFolderHierarchy";
        final String end1GUIDParameterName = "parentFolderGUID";
        final String end2GUIDParameterName = "childFolderGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentFolderGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(childFolderGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName,
                                                        parentFolderGUID,
                                                        childFolderGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a child folder from its parent.
     *
     * @param userId                 userId of user making request.
     * @param parentFolderGUID               unique identifier of the parent folder
     * @param childFolderGUID         unique identifier of the associated child folder
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachFolderHierarchy(String        userId,
                                      String        parentFolderGUID,
                                      String        childFolderGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "detachFolderHierarchy";
        final String end1GUIDParameterName = "parentFolderGUID";
        final String end2GUIDParameterName = "childFolderGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentFolderGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(childFolderGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName,
                                                        parentFolderGUID,
                                                        childFolderGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a asset.
     *
     * @param userId                 userId of user making request.
     * @param assetGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteAsset(String        userId,
                            String        assetGUID,
                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String methodName        = "deleteAsset";
        final String guidParameterName = "assetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, assetGUID, deleteOptions);
    }


    /**
     * Returns the list of assets with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAssetsByName(String       userId,
                                                         String       name,
                                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "getAssetsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.RESOURCE_NAME.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Returns the list of assets with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   deployedImplementationType name of the element to return - match is full text match
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAssetsByDeployedImplementationType(String       userId,
                                                                               String       name,
                                                                               QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "getAssetsByDeployedImplementationType";

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }



    /**
     * Return a list of assets with the requested name.  The name must match exactly.
     *
     * @param userId calling user
     * @param metadataCollectionId unique identifier of the metadata collection to search for
     * @param queryOptions options to control the query
     *
     * @return list of unique identifiers of assets with matching name.
     *
     * @throws InvalidParameterException the name is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<OpenMetadataRootElement> getAssetsByMetadataCollectionId(String       userId,
                                                                         String       metadataCollectionId,
                                                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName = "getAssetsByMetadataCollectionId";

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.METADATA_COLLECTION_ID.name);

        return super.getRootElementsByName(userId,
                                           metadataCollectionId,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Retrieve the actions that are chained off of an action target element.
     *
     * @param userId      calling user
     * @param elementGUID unique identifier of the element to start with
     * @param activityStatus  optional activity status
     * @param queryOptions           multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> getActionsForActionTarget(String         userId,
                                                                   String         elementGUID,
                                                                   ActivityStatus activityStatus,
                                                                   QueryOptions   queryOptions) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName        = "getActionsForActionTarget";
        final String guidParameterName = "elementGUID";

        return getFilteredActions(userId,
                                  elementGUID,
                                  guidParameterName,
                                  activityStatus,
                                  OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                  2,
                                  queryOptions,
                                  methodName);
    }




    /**
     * Retrieve the actions that are chained off of a sponsor's element.
     *
     * @param userId      calling user
     * @param elementGUID unique identifier of the element to start with
     * @param activityStatus  optional activity status
     * @param queryOptions           multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> getActionsForSponsor(String         userId,
                                                              String         elementGUID,
                                                              ActivityStatus activityStatus,
                                                              QueryOptions   queryOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName        = "getActionsForSponsor";
        final String guidParameterName = "elementGUID";

        return getFilteredActions(userId,
                                  elementGUID,
                                  guidParameterName,
                                  activityStatus,
                                  OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                  1,
                                  queryOptions,
                                  methodName);
    }


    /**
     * Retrieve the actions for a particular actor.
     *
     * @param userId     calling user
     * @param actorGUID  unique identifier of the role
     * @param activityStatus optional activity status
     * @param requestedQueryOptions           multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> getAssignedActions(String         userId,
                                                            String         actorGUID,
                                                            ActivityStatus activityStatus,
                                                            QueryOptions   requestedQueryOptions) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName        = "getAssignedActions";
        final String guidParameterName = "actorGUID";

        return getFilteredActions(userId,
                                  actorGUID,
                                  guidParameterName,
                                  activityStatus,
                                  OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                  1,
                                  requestedQueryOptions,
                                  methodName);
    }


    /**
     * Retrieve the actions for a particular actor.
     *
     * @param userId     calling user
     * @param elementGUID  unique identifier of the role
     * @param guidParameterName name of the guid
     * @param relationshipTypeName relationship to query
     * @param startingAtEnd retrieval end
     * @param activityStatus optional activity status
     * @param requestedQueryOptions           multiple options to control the query
     * @param methodName calling method
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    private List<OpenMetadataRootElement> getFilteredActions(String         userId,
                                                             String         elementGUID,
                                                             String         guidParameterName,
                                                             ActivityStatus activityStatus,
                                                             String         relationshipTypeName,
                                                             int            startingAtEnd,
                                                             QueryOptions   requestedQueryOptions,
                                                             String         methodName) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        QueryOptions queryOptions = new QueryOptions(requestedQueryOptions);

        if (queryOptions.getMetadataElementTypeName() == null)
        {
            queryOptions.setMetadataElementTypeName(OpenMetadataType.ACTION.typeName);
        }

        List<OpenMetadataRootElement> relatedMetadataElements = super.getRelatedRootElements(userId,
                                                                                             elementGUID,
                                                                                             guidParameterName,
                                                                                             startingAtEnd,
                                                                                             relationshipTypeName,
                                                                                             requestedQueryOptions,
                                                                                             methodName);

        return this.filterProcesses(relatedMetadataElements, activityStatus);
    }


    /**
     * Retrieve the Processes that match the search string.
     *
     * @param userId       calling user
     * @param searchString string to search for (may include RegExs)
     * @param activityStatus   optional activity status
     * @param searchOptions           multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> findProcesses(String         userId,
                                                       String         searchString,
                                                       ActivityStatus activityStatus,
                                                       SearchOptions  searchOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "findProcesses";

        List<OpenMetadataRootElement> openMetadataElements = this.findRootElements(userId,
                                                                                   searchString,
                                                                                   searchOptions,
                                                                                   methodName);

        return filterProcesses(openMetadataElements, activityStatus);
    }


    /**
     * Retrieve the actions that match the type name and status.
     *
     * @param userId     calling user
     * @param category   type to search for
     * @param activityStatus optional activity status
     * @param queryOptions multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> getProcessesByCategory(String         userId, 
                                                                String         category, 
                                                                ActivityStatus activityStatus, 
                                                                QueryOptions   queryOptions) throws InvalidParameterException, 
                                                                                                    PropertyServerException, 
                                                                                                    UserNotAuthorizedException
    {
        final String methodName = "getProcessesByCategory";
        
        List<OpenMetadataRootElement> openMetadataElements = super.getRootElementsByName(userId,
                                                                                         category,
                                                                                         List.of(OpenMetadataProperty.CATEGORY.name),
                                                                                         queryOptions,
                                                                                         methodName);

        return filterProcesses(openMetadataElements, activityStatus);
    }


    /**
     * Filter process objects by activity status.
     *
     * @param openMetadataRootElements retrieved elements
     * @param activityStatus           optional activity status
     * @return list of process elements
     */
    private List<OpenMetadataRootElement> filterProcesses(List<OpenMetadataRootElement> openMetadataRootElements,
                                                          ActivityStatus                activityStatus)
    {
        if (openMetadataRootElements != null)
        {
            List<OpenMetadataRootElement> processes = new ArrayList<>();

            for (OpenMetadataRootElement openMetadataRootElement : openMetadataRootElements)
            {
                if ((openMetadataRootElement != null) && 
                        (openMetadataRootElement.getProperties() instanceof ProcessProperties processProperties))
                {
                    if ((activityStatus == null) || (activityStatus == processProperties.getActivityStatus()))
                    {
                        processes.add(openMetadataRootElement);
                    }
                }
            }

            return processes;
        }

        return null;
    }


    /**
     * Returns the list of assets with a particular assetGUID.
     *
     * @param userId                 userId of user making request
     * @param assetGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getDeployedITAssets(String       userId,
                                                             String       assetGUID,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "getDeployedITAssets";
        final String guidPropertyName = "assetGUID";

        return super.getRelatedRootElements(userId,
                                           assetGUID,
                                           guidPropertyName,
                                           1,
                                           OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName,
                                           queryOptions,
                                           methodName);
    }



    /**
     * Returns the list of assets providing data to the data set.
     *
     * @param userId                 userId of user making request
     * @param assetGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getDataSetContents(String       userId,
                                                            String       assetGUID,
                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "getDataSetContents";
        final String guidPropertyName = "assetGUID";

        return super.getRelatedRootElements(userId,
                                            assetGUID,
                                            guidPropertyName,
                                            1,
                                            OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Returns the list of assets providing data to the data set.
     *
     * @param userId                 userId of user making request
     * @param assetGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSupportedDataSets(String       userId,
                                                              String       assetGUID,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getSupportedDataSets";
        final String guidPropertyName = "assetGUID";

        return super.getRelatedRootElements(userId,
                                            assetGUID,
                                            guidPropertyName,
                                            2,
                                            OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                            queryOptions,
                                            methodName);
    }



    /**
     * Returns the list of file assets catalogued under the folder asset.
     *
     * @param userId                 userId of user making request
     * @param folderGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getFilesInFolder(String       userId,
                                                          String       folderGUID,
                                                          QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "getFilesInFolder";
        final String guidPropertyName = "folderGUID";

        return super.getRelatedRootElements(userId,
                                            folderGUID,
                                            guidPropertyName,
                                            1,
                                            OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Return the properties of a specific asset.
     *
     * @param userId                 userId of user making request
     * @param assetGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getAssetByGUID(String     userId,
                                                  String     assetGUID,
                                                  GetOptions getOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "getAssetByGUID";

        return super.getRootElementByGUID(userId, assetGUID, getOptions, methodName);
    }


    /**
     * Return the properties of a specific governance definition.
     *
     * @param userId      userId of user making request
     * @param name unique name of the required element
     * @param propertyName name of the property to query (default is qualifiedName)
     * @param getOptions  multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getAssetByUniqueName(String       userId,
                                                        String       name,
                                                        String       propertyName,
                                                        GetOptions   getOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getAssetByUniqueName";

        return super.getRootElementByUniqueName(userId, name, propertyName, getOptions, methodName);
    }


    /**
     * Retrieve the list of assets metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findAssets(String        userId,
                                                    String        searchString,
                                                    SearchOptions searchOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "findAssets";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Locate string value in elements that are anchored to assets.  The search string may be a regEx.
     *
     * @param userId calling user
     * @param searchString value to search for (maybe regEx)
     * @param searchOptions options to control the search
     * @return found elements organized by asset
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<AssetSearchMatches> findAssetsInDomain(String        userId,
                                                       String        searchString,
                                                       SearchOptions searchOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "findAssetsInDomain";
        final String nameParameter = "searchString";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateSearchString(searchString, nameParameter, methodName);

        List <AnchorSearchMatches> matches = openMetadataClient.findElementsInAnchorDomain(userId,
                                                                                           searchString,
                                                                                           OpenMetadataType.ASSET.typeName,
                                                                                           searchOptions);

        if (matches != null)
        {
            List<AssetSearchMatches> assetSearchMatchesList = new ArrayList<>();

            for (AnchorSearchMatches anchorSearchMatches : matches)
            {
                if (anchorSearchMatches != null)
                {
                    AssetSearchMatches assetSearchMatches = new AssetSearchMatches(super.convertRootElement(userId,
                                                                                                            anchorSearchMatches,
                                                                                                            searchOptions,
                                                                                                            methodName));

                    if (anchorSearchMatches.getMatchingElements() != null)
                    {
                        List <MetadataElementSummary> matchingAssetElements = new ArrayList<>();

                        for (OpenMetadataElement matchingElement : anchorSearchMatches.getMatchingElements())
                        {
                            if (matchingElement != null)
                            {
                                matchingAssetElements.add(propertyHelper.getMetadataElementSummary(matchingElement));
                            }
                        }

                        assetSearchMatches.setMatchingElements(matchingAssetElements);
                    }

                    assetSearchMatchesList.add(assetSearchMatches);
                }
            }

            return assetSearchMatchesList;
        }

        return null;
    }


    /**
     * Return all the elements that are linked to an asset using lineage relationships.  The relationships are
     * retrieved both from the asset, and the anchored schema elements
     *
     * @param userId the userId of the requesting user
     * @param assetGUID  unique identifier for the asset
     * @param limitToISCQualifiedName  Return whether the returned results should just show a particular information supply chain.
     * This supply chain has to be connected to the starting asset to show.
     * @param highlightISCQualifiedName Return whether a particular information supply chain should be highlighted.
     * @param suppliedQueryOptions various other options to control the query
     *
     * @return graph of elements or
     * @throws InvalidParameterException - one of the parameters is null or invalid or
     * @throws PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public AssetLineageGraph getAssetLineageGraph(String       userId,
                                                  String       assetGUID,
                                                  String       limitToISCQualifiedName,
                                                  String       highlightISCQualifiedName,
                                                  QueryOptions suppliedQueryOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName    = "getAssetLineageGraph";
        final String guidParameter = "assetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, guidParameter, methodName);

        OpenMetadataRootElement openMetadataRootElement = this.getAssetByGUID(userId, assetGUID, suppliedQueryOptions);

        if (openMetadataRootElement != null)
        {
            List<AssetLineageGraphNode>         linkedAssets              = new ArrayList<>();
            List<AssetLineageGraphRelationship> lineageRelationships      = new ArrayList<>();
            Set<String>                         upstreamProcessedAssets   = new HashSet<>();
            Set<String>                         downstreamProcessedAssets = new HashSet<>();

            QueryOptions queryOptions = new QueryOptions(suppliedQueryOptions);

            queryOptions.setIncludeOnlyRelationships(getLineageRelationshipTypeNames(queryOptions.getIncludeOnlyRelationships()));

            this.getAssetLineageGraphNodes(userId,
                                           assetGUID,
                                           0,
                                           queryOptions.getIncludeOnlyRelationships(),
                                           limitToISCQualifiedName,
                                           queryOptions,
                                           linkedAssets,
                                           lineageRelationships,
                                           upstreamProcessedAssets,
                                           downstreamProcessedAssets);

            if (! linkedAssets.isEmpty())
            {
                AssetLineageGraph assetLineageGraph = new AssetLineageGraph(linkedAssets.get(0));

                if (linkedAssets.size() > 1)
                {
                    assetLineageGraph.setLinkedAssets(new ArrayList<>(linkedAssets.subList(1, linkedAssets.size())));
                }

                assetLineageGraph.setLineageRelationships(this.deDupLineageRelationships(lineageRelationships));

                AssetLineageGraphMermaidGraphBuilder graphBuilder = new AssetLineageGraphMermaidGraphBuilder(assetLineageGraph, highlightISCQualifiedName);
                assetLineageGraph.setMermaidGraph(graphBuilder.getMermaidGraph());
                assetLineageGraph.setEdgeMermaidGraph(graphBuilder.getEdgeMermaidGraph());

                return assetLineageGraph;
            }
        }


        return null;
    }



    /**
     * Remove any duplicated relationships.
     *
     * @param lineageRelationships derived relationships
     * @return deduplicated list
     */
    private List<AssetLineageGraphRelationship> deDupLineageRelationships(List<AssetLineageGraphRelationship> lineageRelationships)
    {
        Map<String, AssetLineageGraphRelationship> relationshipMap = new HashMap<>();

        for (AssetLineageGraphRelationship lineageRelationship : lineageRelationships)
        {
            relationshipMap.put(lineageRelationship.getEnd1AssetGUID() + lineageRelationship.getEnd2AssetGUID(),
                                lineageRelationship);
        }

        return new ArrayList<>(relationshipMap.values());
    }


    /**
     * Construct a list of the lineage relationship types to search for,
     *
     * @param relationshipTypes relationship type names supplied by the user.
     * @return list of relationship type names
     */
    private List<String> getLineageRelationshipTypeNames(List<String> relationshipTypes)
    {
        List<String> lineageRelationshipTypeNames = relationshipTypes;

        if ((lineageRelationshipTypeNames == null) || (lineageRelationshipTypeNames.isEmpty()))
        {
            lineageRelationshipTypeNames = new ArrayList<>();

            lineageRelationshipTypeNames.add(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName);

            /*
             * Pick up requests for actions and ToDos
             */
            lineageRelationshipTypeNames.add(OpenMetadataType.REQUEST_FOR_ACTION_TARGET.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName);
        }

        return lineageRelationshipTypeNames;
    }


    /**
     * Structure for capturing lineage relationships  and their supply chains.
     *
     * @param relationshipTypes names of the different types of relationships between two specific assets
     * @param informationSupplyChains the information supply chains that are associated with these relationships.
     */
    record LineageLink(Set<String> relationshipTypes,
                       Set<String> informationSupplyChains)
    {}


    /**
     * Retrieve the asset and its lineage relationships.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the asset
     * @param direction is this asset upstream or downstream of the asset (or either direction, 0, to start)
     * @param lineageRelationshipTypeNames list of requested type names
     * @param limitToInformationSupplyChain qualified name to control retrieval
     * @param queryOptions options for the query
     * @param linkedAssets set of assets that are upstream of this asset
     * @param lineageRelationships relationships that link the assets together in the lineage graph
     * @param upstreamProcessedAssets list of assets covered so far upstream of the asset
     * @param downstreamProcessedAssets list of assets covered so far downstream of the asset
     * @throws InvalidParameterException invalid parameter - not expected
     * @throws PropertyServerException problem accessing the repository
     * @throws UserNotAuthorizedException security problem
     */
    void getAssetLineageGraphNodes(String                                                userId,
                                   String                                                elementGUID,
                                   int                                                   direction,
                                   List<String>                                          lineageRelationshipTypeNames,
                                   String                                                limitToInformationSupplyChain,
                                   QueryOptions                                          queryOptions,
                                   List<AssetLineageGraphNode>                           linkedAssets,
                                   List<AssetLineageGraphRelationship>                   lineageRelationships,
                                   Set<String>                                           upstreamProcessedAssets,
                                   Set<String>                                           downstreamProcessedAssets) throws InvalidParameterException,
                                                                                                                           PropertyServerException,
                                                                                                                           UserNotAuthorizedException
    {
        Map<String, LineageLink> upstreamAssets   = new HashMap<>();
        Map<String, LineageLink> downstreamAssets = new HashMap<>();

        AssetLineageGraphNode asset = this.getAssetLineageGraphNode(userId,
                                                                    elementGUID,
                                                                    direction,
                                                                    lineageRelationshipTypeNames,
                                                                    limitToInformationSupplyChain,
                                                                    queryOptions,
                                                                    upstreamAssets,
                                                                    downstreamAssets);

        if (asset != null)
        {
            if ((direction == 0) || (direction == 1))
            {
                downstreamProcessedAssets.add(asset.getElementHeader().getGUID());
            }
            if ((direction == 0) || (direction == 2))
            {
                upstreamProcessedAssets.add(asset.getElementHeader().getGUID());
            }

            linkedAssets.add(asset);

            /*
             * Process upstream assets
             */
            for (String linkedAssetGUID : upstreamAssets.keySet())
            {
                AssetLineageGraphRelationship assetLineageGraphRelationship = new AssetLineageGraphRelationship();

                LineageLink lineageLink = upstreamAssets.get(linkedAssetGUID);

                if (lineageLink != null)
                {
                    if (lineageLink.relationshipTypes != null)
                    {
                        assetLineageGraphRelationship.setRelationshipTypes(new ArrayList<>(lineageLink.relationshipTypes));
                    }
                    if (lineageLink.informationSupplyChains != null)
                    {
                        assetLineageGraphRelationship.setInformationSupplyChains(new ArrayList<>(lineageLink.informationSupplyChains));
                    }
                }

                assetLineageGraphRelationship.setEnd1AssetGUID(linkedAssetGUID);
                assetLineageGraphRelationship.setEnd2AssetGUID(asset.getElementHeader().getGUID());

                lineageRelationships.add(assetLineageGraphRelationship);

                if (! upstreamProcessedAssets.contains(linkedAssetGUID))
                {
                    this.getAssetLineageGraphNodes(userId,
                                                   linkedAssetGUID,
                                                   2,
                                                   lineageRelationshipTypeNames,
                                                   limitToInformationSupplyChain,
                                                   queryOptions,
                                                   linkedAssets,
                                                   lineageRelationships,
                                                   upstreamProcessedAssets,
                                                   downstreamProcessedAssets);
                }
            }

            /*
             * Process downstream assets
             */
            for (String linkedAssetGUID : downstreamAssets.keySet())
            {
                AssetLineageGraphRelationship assetLineageGraphRelationship = new AssetLineageGraphRelationship();

                LineageLink lineageLink = downstreamAssets.get(linkedAssetGUID);

                if (lineageLink != null)
                {
                    if (lineageLink.relationshipTypes != null)
                    {
                        assetLineageGraphRelationship.setRelationshipTypes(new ArrayList<>(lineageLink.relationshipTypes));
                    }
                    if (lineageLink.informationSupplyChains != null)
                    {
                        assetLineageGraphRelationship.setInformationSupplyChains(new ArrayList<>(lineageLink.informationSupplyChains));
                    }
                }

                assetLineageGraphRelationship.setEnd1AssetGUID(asset.getElementHeader().getGUID());
                assetLineageGraphRelationship.setEnd2AssetGUID(linkedAssetGUID);

                lineageRelationships.add(assetLineageGraphRelationship);

                if (! downstreamProcessedAssets.contains(linkedAssetGUID))
                {
                    this.getAssetLineageGraphNodes(userId,
                                                   linkedAssetGUID,
                                                   1,
                                                   lineageRelationshipTypeNames,
                                                   limitToInformationSupplyChain,
                                                   queryOptions,
                                                   linkedAssets,
                                                   lineageRelationships,
                                                   upstreamProcessedAssets,
                                                   downstreamProcessedAssets);
                }
            }
        }
    }



    /**
     * Retrieve the asset and its lineage relationships.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param direction is this asset upstream or downstream of the asset (or either direction, 0, to start)
     * @param lineageRelationshipTypeNames list of requested type names
     * @param limitToInformationSupplyChain qualified name to control retrieval
     * @param queryOptions options for query
     * @param upstreamAssets set of assets that are upstream of this asset
     * @param downstreamAssets set of assets that are downstream of this asset
     * @return asset lineage node
     * @throws InvalidParameterException invalid parameter - not expected
     * @throws PropertyServerException problem accessing the repository
     * @throws UserNotAuthorizedException security problem
     */
    AssetLineageGraphNode getAssetLineageGraphNode(String                    userId,
                                                   String                    assetGUID,
                                                   int                       direction,
                                                   List<String>              lineageRelationshipTypeNames,
                                                   String                    limitToInformationSupplyChain,
                                                   QueryOptions              queryOptions,
                                                   Map<String, LineageLink>  upstreamAssets,
                                                   Map<String, LineageLink>  downstreamAssets) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        OpenMetadataRootElement asset = this.getAssetByGUID(userId, assetGUID, queryOptions);

        if (asset != null)
        {
            AssetLineageGraphNode assetLineageGraphNode = new AssetLineageGraphNode(asset);

            /*
             * Retrieve all the lineage relationships for the asset.
             */
            List<RelatedMetadataNodeSummary> relationships = this.getAssetLineageRelationships(userId,
                                                                                               asset.getElementHeader().getGUID(),
                                                                                               direction,
                                                                                               lineageRelationshipTypeNames,
                                                                                               limitToInformationSupplyChain,
                                                                                               queryOptions);

            if (relationships != null)
            {
                List<RelatedMetadataNodeSummary> upstreamRelationships   = new ArrayList<>();
                List<RelatedMetadataNodeSummary> downstreamRelationships = new ArrayList<>();
                List<RelatedMetadataNodeSummary> internalRelationship    = new ArrayList<>();

                for (RelatedMetadataNodeSummary relationship : relationships)
                {
                    if (relationship != null)
                    {
                        if (lineageRelationshipTypeNames.contains(relationship.getRelationshipHeader().getType().getTypeName()))
                        {
                            if (! assetGUID.equals(relationship.getStartingElementGUID()))
                            {
                                internalRelationship.add(relationship);
                            }
                            else if (relationship.getRelatedElementAtEnd1())
                            {
                                // Upstream asset
                                setupLineageAsset(relationship, upstreamAssets, upstreamRelationships);
                            }
                            else
                            {
                                // Downstream asset
                                setupLineageAsset(relationship, downstreamAssets, downstreamRelationships);
                            }
                        }
                    }
                }

                assetLineageGraphNode.setUpstreamRelationships(upstreamRelationships);
                assetLineageGraphNode.setDownstreamRelationships(downstreamRelationships);
                assetLineageGraphNode.setInternalRelationships(internalRelationship);
            }

            return assetLineageGraphNode;
        }

        return null;
    }


    /**
     * Assemble details from the relationship into the various parts of the lineage graph.
     *
     * @param relationship relationship to read
     * @param lineageAssets map of linked assets
     * @param relationshipList list of processed relationships
     */
    private void setupLineageAsset(RelatedMetadataNodeSummary       relationship,
                                   Map<String, LineageLink>         lineageAssets,
                                   List<RelatedMetadataNodeSummary> relationshipList)
    {
        String relationshipName        = this.getRelationshipName(relationship);
        String relationshipSupplyChain = null;

        if (relationship.getRelationshipProperties() instanceof LineageRelationshipProperties lineageRelationshipProperties)
        {
            relationshipSupplyChain = lineageRelationshipProperties.getISCQualifiedName();
        }

        LineageLink currentLineageLinks = lineageAssets.get(relationship.getRelatedElement().getElementHeader().getGUID());

        if (currentLineageLinks == null)
        {
            currentLineageLinks = new LineageLink(null, null);
        }

        Set<String> currentRelationshipNames = currentLineageLinks.relationshipTypes;

        if (currentRelationshipNames == null)
        {
            currentRelationshipNames = new HashSet<>();
        }

        currentRelationshipNames.add(relationshipName);

        Set<String> currentInformationSupplyChains = currentLineageLinks.informationSupplyChains;

        if (relationshipSupplyChain != null)
        {
            if (currentInformationSupplyChains == null)
            {
                currentInformationSupplyChains = new HashSet<>();
            }

            currentInformationSupplyChains.add(relationshipSupplyChain);
        }

        lineageAssets.put(relationship.getRelatedElement().getElementHeader().getGUID(), new LineageLink(currentRelationshipNames, currentInformationSupplyChains));
        relationshipList.add(relationship);
    }


    /**
     * Extract the name of the relationship from a relationship.
     *
     * @param relationship relationship to query
     * @return relationship name
     */
    private String getRelationshipName(RelatedMetadataNodeSummary relationship)
    {
        /*
         * The default name is the type name
         */
        String relationshipName = relationship.getRelationshipHeader().getType().getTypeName();

        /*
         * If the relationship has a label then this is used to embellish the relationship name.
         */
        String label = null;

        if (relationship.getRelationshipProperties() instanceof LabeledRelationshipProperties labeledRelationshipProperties)
        {
            label = labeledRelationshipProperties.getLabel();
        }

        if (label != null)
        {
            relationshipName = label + " [" + relationshipName + "]";
        }

        return relationshipName;
    }


    /**
     * Retrieve all the relevant lineage relationships for an asset.  This considers relationships from the
     * asst itself and any of its schema elements.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param direction is this asset upstream or downstream of the asset (or either direction, 0, to start)
     * @param lineageRelationshipTypeNames list of requested type names
     * @param limitToInformationSupplyChain qualified name to control retrieval
     * @param queryOptions options for the query
     * @return list for relationships - may be empty
     * @throws InvalidParameterException invalid parameter - not expected
     * @throws PropertyServerException problem accessing the repository
     * @throws UserNotAuthorizedException security problem
     */
    private List<RelatedMetadataNodeSummary> getAssetLineageRelationships(String       userId,
                                                                          String       assetGUID,
                                                                          int          direction,
                                                                          List<String> lineageRelationshipTypeNames,
                                                                          String       limitToInformationSupplyChain,
                                                                          QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        List<RelatedMetadataNodeSummary> lineageRelationships = new ArrayList<>();

       /*
        * Begin by retrieving all the relationships for the asset and filtering out the lineage relationships.
        */
        List<RelatedMetadataElement> assetRelationships = this.getLineageRelationshipsForElement(userId,
                                                                                                 assetGUID,
                                                                                                 OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                                                 direction,
                                                                                                 lineageRelationshipTypeNames,
                                                                                                 limitToInformationSupplyChain,
                                                                                                 queryOptions);

        if (assetRelationships != null)
        {
            lineageRelationships.addAll(propertyHelper.getRelatedNodeSummaries(assetGUID, assetRelationships));
        }

        /*
         * Now find the SchemaElements that belong to the asset.
         */
        SearchClassifications         searchClassifications    = new SearchClassifications();
        List<ClassificationCondition> classificationConditions = new ArrayList<>();
        ClassificationCondition       classificationCondition  = new ClassificationCondition();
        SearchProperties              searchProperties         = new SearchProperties();
        List<PropertyCondition>       propertyConditions       = new ArrayList<>();
        PropertyCondition             propertyCondition        = new PropertyCondition();
        PrimitiveTypePropertyValue    primitivePropertyValue   = new PrimitiveTypePropertyValue();

        primitivePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(assetGUID);
        primitivePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        propertyCondition.setProperty(OpenMetadataProperty.ANCHOR_GUID.name);
        propertyCondition.setOperator(PropertyComparisonOperator.EQ);
        propertyCondition.setValue(primitivePropertyValue);
        propertyConditions.add(propertyCondition);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);
        searchProperties.setConditions(propertyConditions);

        classificationCondition.setName(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);
        classificationCondition.setSearchProperties(searchProperties);
        classificationConditions.add(classificationCondition);
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);
        searchClassifications.setConditions(classificationConditions);

        QueryOptions schemaQueryOptions = new QueryOptions(queryOptions);

        schemaQueryOptions.setMetadataElementTypeName(OpenMetadataType.SCHEMA_ELEMENT.typeName);

        List<OpenMetadataElement> anchoredElements = openMetadataClient.findMetadataElements(userId,
                                                                                             null,
                                                                                             searchClassifications,
                                                                                             schemaQueryOptions);

        /*
         * For each schema element in the asset, retrieve its lineage relationships
         */
        if (anchoredElements != null)
        {
            for (OpenMetadataElement anchoredElement : anchoredElements)
            {
                List<RelatedMetadataElement> schemaRelationships = this.getLineageRelationshipsForElement(userId,
                                                                                                          anchoredElement.getElementGUID(),
                                                                                                          OpenMetadataType.SCHEMA_ELEMENT.typeName,
                                                                                                          direction,
                                                                                                          lineageRelationshipTypeNames,
                                                                                                          limitToInformationSupplyChain,
                                                                                                          queryOptions);

                if (schemaRelationships != null)
                {
                    lineageRelationships.addAll(propertyHelper.getRelatedNodeSummaries(assetGUID, schemaRelationships));
                }
            }
        }

        if (! lineageRelationships.isEmpty())
        {
            return lineageRelationships;
        }

        return null;
    }


    /**
     * Return the lineage relationships for a particular element.
     *
     * @param userId calling user
     * @param elementGUID starting element
     * @param elementTypeName type name of element
     * @param direction is this asset upstream or downstream of the asset (or either direction, 0, to start)
     * @param lineageRelationshipTypeNames list of requested type names
     * @param limitToInformationSupplyChain qualified name to control retrieval
     * @param queryOptions options for the query
     * @return list for relationships - may be empty
     * @throws InvalidParameterException invalid parameter - not expected
     * @throws PropertyServerException problem accessing the repository
     * @throws UserNotAuthorizedException security problem
     */
    List<RelatedMetadataElement> getLineageRelationshipsForElement(String       userId,
                                                                   String       elementGUID,
                                                                   String       elementTypeName,
                                                                   int          direction,
                                                                   List<String> lineageRelationshipTypeNames,
                                                                   String       limitToInformationSupplyChain,
                                                                   QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "getLineageRelationshipsForElement";

        List<RelatedMetadataElement> lineageRelationships = new ArrayList<>();

        /*
         * Begin by retrieving all the relationships for the asset and filtering out the lineage relationships.
         */
        RelatedMetadataElementList relationships = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                 elementGUID,
                                                                                                 direction,
                                                                                                 elementTypeName,
                                                                                                 queryOptions);

        if ((relationships != null) && (relationships.getElementList() != null))
        {
            for (RelatedMetadataElement relationship : relationships.getElementList())
            {
                if ((relationship != null) &&
                        (lineageRelationshipTypeNames.contains(relationship.getType().getTypeName())))
                {
                    if (limitToInformationSupplyChain != null)
                    {
                        String relationshipSupplyChain = propertyHelper.getStringProperty(localServiceName,
                                                                                          OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                                          relationship.getRelationshipProperties(),
                                                                                          methodName);

                        if (limitToInformationSupplyChain.equals(relationshipSupplyChain))
                        {
                            lineageRelationships.add(relationship);
                        }
                    }
                    else
                    {
                        lineageRelationships.add(relationship);
                    }
                }
            }
        }

        return lineageRelationships;
    }


    /**
     * Return all the elements that are anchored to an asset plus relationships between these elements and to other elements.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param queryOptions options to control the query
     * @return graph of elements
     * @throws InvalidParameterException the name is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public AssetGraph getAssetGraph(String       userId,
                                    String       assetGUID,
                                    QueryOptions queryOptions) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String methodName    = "getAssetGraph";
        final String guidParameter = "assetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, guidParameter, methodName);

        OpenMetadataRootElement asset = this.getAssetByGUID(userId, assetGUID, queryOptions);

        if (asset != null)
        {
            /*
             * Save the asset details into the asset graph bean.
             */
            AssetGraph assetGraph = new AssetGraph(asset);

            /*
             * This map will hold all of the relationships retrieved for the graph.  It initially,
             * holds the relationships connected to the starting asset - however, as the related
             * elements are extracted, their relationships are added.
             */
            Map<String, RelatedMetadataElement> receivedRelationships = new HashMap<>();

            /*
             * This list holds the qualified names of information supply chains listed in the lineage relationships.
             */
            List<String> iscQualifiedNames = new ArrayList<>();

            RelatedMetadataElementList relationships = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                     assetGUID,
                                                                                                     0,
                                                                                                     null,
                                                                                                     queryOptions);

            if ((relationships != null) && (relationships.getElementList() != null))
            {
                for (RelatedMetadataElement relationship : relationships.getElementList())
                {
                    if (relationship != null)
                    {
                        /*
                         * Save the relationship if it is structural.  The relationships relating to ongoing dynamic activity are ignored.
                         */
                        if ((! propertyHelper.isTypeOf(relationship, OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName)) &&
                                (! propertyHelper.isTypeOf(relationship, OpenMetadataType.REPORT_SUBJECT.typeName)))
                        {
                            receivedRelationships.put(relationship.getRelationshipGUID(), relationship);
                        }

                        /*
                         * The information supply chain is extracted from all relationships to build up the list of
                         * information supply chains that the asset is involved with.
                         */
                        String iscQualifiedName = propertyHelper.getStringProperty(localServiceName,
                                                                                   OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                                   relationship.getRelationshipProperties(),
                                                                                   methodName);

                        if ((iscQualifiedName != null) && (! iscQualifiedNames.contains(iscQualifiedName)))
                        {
                            iscQualifiedNames.add(iscQualifiedName);
                        }
                    }
                }
            }


            /*
             * Now retrieve all the entities anchored to the starting asset.  This is done as a single
             * query for performance reasons (each call to the repository costs).  Notice the
             * results are limited by the paging values set by the caller.
             */
            SearchClassifications         searchClassifications    = new SearchClassifications();
            List<ClassificationCondition> classificationConditions = new ArrayList<>();
            ClassificationCondition       classificationCondition  = new ClassificationCondition();
            SearchProperties              searchProperties         = new SearchProperties();
            List<PropertyCondition>       propertyConditions       = new ArrayList<>();
            PropertyCondition             propertyCondition        = new PropertyCondition();
            PrimitiveTypePropertyValue    primitivePropertyValue   = new PrimitiveTypePropertyValue();

            primitivePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
            primitivePropertyValue.setPrimitiveValue(asset.getElementHeader().getGUID());
            primitivePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

            propertyCondition.setProperty(OpenMetadataProperty.ANCHOR_GUID.name);
            propertyCondition.setOperator(PropertyComparisonOperator.EQ);
            propertyCondition.setValue(primitivePropertyValue);
            propertyConditions.add(propertyCondition);
            searchProperties.setMatchCriteria(MatchCriteria.ALL);
            searchProperties.setConditions(propertyConditions);

            classificationCondition.setName(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);
            classificationCondition.setSearchProperties(searchProperties);
            classificationConditions.add(classificationCondition);
            searchClassifications.setMatchCriteria(MatchCriteria.ALL);
            searchClassifications.setConditions(classificationConditions);

            List<OpenMetadataElement> anchoredElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 searchProperties,
                                                                                                 searchClassifications,
                                                                                                 queryOptions);

            assetGraph.setAnchoredElements(propertyHelper.getMetadataElementSummaries(anchoredElements));

            /*
             * For each anchored element, retrieve all of its relationships.  After this we have the information
             * to create a graph.
             */
            if (anchoredElements != null)
            {
                for (OpenMetadataElement metadataElement : anchoredElements)
                {
                    if (metadataElement != null)
                    {
                        relationships = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                      metadataElement.getElementGUID(),
                                                                                      0,
                                                                                      null,
                                                                                      queryOptions);
                        if ((relationships != null) && (relationships.getElementList() != null))
                        {
                            for (RelatedMetadataElement relationship : relationships.getElementList())
                            {
                                if (relationship != null)
                                {
                                    receivedRelationships.put(relationship.getRelationshipGUID(), relationship);



                                    String iscQualifiedName = propertyHelper.getStringProperty(localServiceName,
                                                                                               OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                                               relationship.getRelationshipProperties(),
                                                                                               methodName);

                                    if ((iscQualifiedName != null) && (!iscQualifiedNames.contains(iscQualifiedName)))
                                    {
                                        iscQualifiedNames.add(iscQualifiedName);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /*
             * Convert relationships
             */
            if (! receivedRelationships.isEmpty())
            {
                List<RelatedMetadataNodeSummary> metadataRelationships = new ArrayList<>();

                for (String relationshipGUID : receivedRelationships.keySet())
                {
                    if (relationshipGUID != null)
                    {
                        RelatedMetadataNodeSummary metadataRelationship = propertyHelper.getRelatedNodeSummary(relationshipGUID, receivedRelationships.get(relationshipGUID));

                        metadataRelationships.add(metadataRelationship);
                    }
                }

                assetGraph.setRelationships(metadataRelationships);
            }

            /*
             * This list contains the information supply chains associated with the asset.
             */
            List<MetadataElementSummary> informationSupplyChains = new ArrayList<>();

            if (! iscQualifiedNames.isEmpty())
            {
                GetOptions iscGetOptions = new GetOptions(queryOptions);

                iscGetOptions.setMetadataElementTypeName(OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName);

                for (String iscQualifiedName : iscQualifiedNames)
                {
                    OpenMetadataElement informationSupplyChain = openMetadataClient.getMetadataElementByUniqueName(userId,
                                                                                                                   iscQualifiedName,
                                                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                   iscGetOptions);
                    if (informationSupplyChain != null)
                    {
                        informationSupplyChains.add(propertyHelper.getMetadataElementSummary(informationSupplyChain));
                    }
                }
            }

            if (! informationSupplyChains.isEmpty())
            {
                assetGraph.setPartOfInformationSupplyChains(informationSupplyChains);
            }

            /*
             * Build the various mermaid graphs that show different aspects of the asset graph.
             */
            AssetGraphMermaidGraphBuilder graphBuilder = new AssetGraphMermaidGraphBuilder(assetGraph);

            assetGraph.setMermaidGraph(graphBuilder.getMermaidGraph());
            assetGraph.setInformationSupplyChainMermaidGraph(graphBuilder.getInformationSupplyChainMermaidGraph());
            assetGraph.setFieldLevelLineageGraph(graphBuilder.getFieldLevelLineageGraph());
            assetGraph.setActionMermaidGraph(graphBuilder.getActionGraph());
            assetGraph.setLocalLineageGraph(graphBuilder.getLocalLineageGraph());

            return assetGraph;
        }


        return null;
    }
}
