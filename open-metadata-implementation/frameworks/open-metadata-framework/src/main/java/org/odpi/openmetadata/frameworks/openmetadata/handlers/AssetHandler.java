/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.controls.CSVFileConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ToDoProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ImpactedResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.IncidentReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.AssetGraphMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.AssetLineageGraphMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.APIEndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.CSVFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FolderHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.NestedFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.DeployedOnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.metadatarepositories.MetadataCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionRequesterProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.LineageRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.TypeEmbeddedAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularFileColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SupportedSoftwareCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.AssignmentType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.FileType;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * AssetHandler provides methods to define all types of assets and their relationships
 */
public class AssetHandler extends OpenMetadataHandlerBase
{
    private final ConnectionHandler      connectionHandler;
    private final EndpointHandler        endpointHandler;
    private final SchemaTypeHandler      schemaTypeHandler;
    private final SchemaAttributeHandler schemaAttributeHandler;

    private final static String folderDivider = "/";
    private final static String fileSystemDivider    = "://";
    private final static String fileExtensionDivider = "\\.";

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

        connectionHandler      = new ConnectionHandler(localServerName, auditLog, localServiceName, openMetadataClient);
        endpointHandler        = new EndpointHandler(localServerName, auditLog, localServiceName, openMetadataClient);
        schemaTypeHandler      = new SchemaTypeHandler(localServerName, auditLog, localServiceName, openMetadataClient);
        schemaAttributeHandler = new SchemaAttributeHandler(localServerName, auditLog, localServiceName, openMetadataClient);
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

        connectionHandler      = new ConnectionHandler(localServerName, auditLog, localServiceName, openMetadataClient);
        endpointHandler        = new EndpointHandler(localServerName, auditLog, localServiceName, openMetadataClient);
        schemaTypeHandler      = new SchemaTypeHandler(localServerName, auditLog, localServiceName, openMetadataClient);
        schemaAttributeHandler = new SchemaAttributeHandler(localServerName, auditLog, localServiceName, openMetadataClient);
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

        connectionHandler      = new ConnectionHandler(localServerName, auditLog, localServiceName, openMetadataClient);
        endpointHandler        = new EndpointHandler(localServerName, auditLog, localServiceName, openMetadataClient);
        schemaTypeHandler      = new SchemaTypeHandler(localServerName, auditLog, localServiceName, openMetadataClient);
        schemaAttributeHandler = new SchemaAttributeHandler(localServerName, auditLog, localServiceName, openMetadataClient);
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId calling user
     * @param displayName display name for the file in the catalog
     * @param description description of the file in the catalog
     * @param pathName full path of the file - used to access the file through the connector
     * @param columnHeaders does the first line of the file contain the column names. If not pass the list of column headers.
     * @param delimiterCharacter what is the delimiter character - null for default of comma
     * @param quoteCharacter what is the character to group a field that contains delimiter characters
     * @param versionIdentifier version identifier to use in the metadata elements
     * @param connectorTypeGUID optional connector type to indicate that a connection should be created
     * @param newElementOptions options to control the create process
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public  String  addCSVFileToCatalog(String            userId,
                                        String            displayName,
                                        String            description,
                                        String            pathName,
                                        List<String>      columnHeaders,
                                        Character         delimiterCharacter,
                                        Character         quoteCharacter,
                                        String            versionIdentifier,
                                        String            connectorTypeGUID,
                                        NewElementOptions newElementOptions) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "addCSVFileToCatalog";

        final String pathParameterName = "pathName";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(pathName, pathParameterName, methodName);

        String fileType = FileType.CSV_FILE.getFileTypeName();
        String fileName = this.getFileName(pathName);
        String fileExtension = this.getFileExtension(pathName);

        if (delimiterCharacter == null)
        {
            delimiterCharacter = ',';
        }

        if (quoteCharacter == null)
        {
            quoteCharacter = '\"';
        }

        CSVFileProperties csvFileProperties = new CSVFileProperties();

        csvFileProperties.setQualifiedName(this.createFileQualifiedName(OpenMetadataType.CSV_FILE.typeName,
                                                                        null,
                                                                        pathName,
                                                                        "V1.0"));
        csvFileProperties.setDisplayName(displayName);
        csvFileProperties.setDescription(description);
        csvFileProperties.setResourceName(pathName);
        csvFileProperties.setVersionIdentifier(versionIdentifier);
        csvFileProperties.setPathName(pathName);
        csvFileProperties.setFileName(fileName);
        csvFileProperties.setFileExtension(fileExtension);
        csvFileProperties.setFileType(fileType);
        csvFileProperties.setDelimiterCharacter(delimiterCharacter.toString());
        csvFileProperties.setQuoteCharacter(quoteCharacter.toString());


        String fileAssetGUID = this.createAsset(userId,
                                                newElementOptions,
                                                null,
                                                csvFileProperties,
                                                null);

        NewElementOptions newRelatedElementOptions = new NewElementOptions(newElementOptions);

        newRelatedElementOptions.setIsOwnAnchor(false);
        if (newRelatedElementOptions.getAnchorGUID() == null)
        {
            newRelatedElementOptions.setAnchorGUID(fileAssetGUID);
        }


        if (connectorTypeGUID != null)
        {
            newRelatedElementOptions.setParentGUID(fileAssetGUID);
            newRelatedElementOptions.setParentAtEnd1(true);
            newRelatedElementOptions.setParentRelationshipTypeName(OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName);

            ConnectionProperties connectionProperties = new ConnectionProperties();

            connectionProperties.setQualifiedName(csvFileProperties.getQualifiedName() + "_connection");
            connectionProperties.setDisplayName("Connection for CSV File: " + displayName);
            connectionProperties.setVersionIdentifier(versionIdentifier);

            Map<String, Object>  configurationProperties = new HashMap<>();

            configurationProperties.put(CSVFileConfigurationProperty.DELIMITER_CHARACTER.getName(), delimiterCharacter);
            configurationProperties.put(CSVFileConfigurationProperty.QUOTE_CHARACTER.getName(), quoteCharacter);

            if (columnHeaders != null)
            {
                configurationProperties.put(CSVFileConfigurationProperty.COLUMN_NAMES.getName(), columnHeaders);
            }

            connectionProperties.setConfigurationProperties(configurationProperties);

            String connectionGUID = connectionHandler.createConnection(userId,
                                                                       newRelatedElementOptions,
                                                                       null,
                                                                       connectionProperties,
                                                                       null);

            connectionHandler.linkConnectionConnectorType(userId,
                                                          connectionGUID,
                                                          connectorTypeGUID,
                                                          new MakeAnchorOptions(newRelatedElementOptions),
                                                          null);

            newRelatedElementOptions.setParentGUID(connectionGUID);
            newRelatedElementOptions.setParentAtEnd1(true);
            newRelatedElementOptions.setParentRelationshipTypeName(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName);

            EndpointProperties endpointProperties = new EndpointProperties();

            endpointProperties.setQualifiedName(csvFileProperties.getQualifiedName() + "_endpoint");
            endpointProperties.setDisplayName("Endpoint for CSV File: " + displayName);
            endpointProperties.setVersionIdentifier(versionIdentifier);
            endpointProperties.setNetworkAddress(pathName);

            endpointHandler.createEndpoint(userId,
                                           newRelatedElementOptions,
                                           null,
                                           endpointProperties,
                                           null);
        }

        if ((columnHeaders != null) && (! columnHeaders.isEmpty()))
        {
            newRelatedElementOptions.setParentGUID(fileAssetGUID);
            newRelatedElementOptions.setParentAtEnd1(true);
            newRelatedElementOptions.setParentRelationshipTypeName(OpenMetadataType.SCHEMA_RELATIONSHIP.typeName);

            TabularSchemaTypeProperties rootSchemaTypeProperties = new TabularSchemaTypeProperties();

            rootSchemaTypeProperties.setQualifiedName(csvFileProperties.getQualifiedName() + "_schemaType");
            rootSchemaTypeProperties.setDisplayName("SchemaType for CSV File: " + displayName);
            rootSchemaTypeProperties.setVersionIdentifier(versionIdentifier);

            String rootSchemaTypeGUID = schemaTypeHandler.createSchemaType(userId,
                                                                           newRelatedElementOptions,
                                                                           null,
                                                                           rootSchemaTypeProperties,
                                                                           null);

            newRelatedElementOptions.setParentGUID(rootSchemaTypeGUID);
            newRelatedElementOptions.setParentAtEnd1(true);
            newRelatedElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);

            int columnCount = 0;
            for (String columnName : columnHeaders)
            {
                if (columnName != null)
                {
                    String columnQualifiedName = pathName + "::" + columnName + "::" + columnCount;
                    String columnDisplayName = columnName + "::" + columnCount;

                    TabularFileColumnProperties columnProperties = new TabularFileColumnProperties();

                    columnProperties.setQualifiedName(columnQualifiedName);
                    columnProperties.setDisplayName(columnDisplayName);
                    columnProperties.setVersionIdentifier(versionIdentifier);

                    TypeEmbeddedAttributeProperties columnType = new TypeEmbeddedAttributeProperties();

                    columnType.setQualifiedName(columnQualifiedName + ":columnType");
                    columnType.setSchemaTypeName(OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName);
                    columnType.setDataType(DataType.STRING.getName());

                    Map<String, ClassificationProperties> initialClassifications = new HashMap<>();

                    initialClassifications.put(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName, columnType);



                    schemaAttributeHandler.createSchemaAttribute(userId,
                                                                 newRelatedElementOptions,
                                                                 initialClassifications,
                                                                 columnProperties,
                                                                 null);

                    columnCount ++;
                }
            }
        }

        return fileAssetGUID;
    }


    /**
     * Construct the qualified name for a file resource.
     *
     * @param typeName type of element
     * @param qualifiedName supplied qualified name
     * @param pathName pathname in file system
     * @param versionIdentifier version identifier
     * @return qualified name
     */
    private String createFileQualifiedName(String typeName,
                                           String qualifiedName,
                                           String pathName,
                                           String versionIdentifier)
    {
        if (qualifiedName != null)
        {
            return qualifiedName;
        }

        if (versionIdentifier == null)
        {
            return typeName + ":" + pathName;
        }
        else
        {
            return typeName + ":" + pathName + ":" + versionIdentifier;
        }
    }


    /**
     * Return the file extension of the file from the path name.
     *
     * @param pathName path name of a file
     * @return file type or null if no file type
     */
    private String getFileExtension(String pathName)
    {
        String result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(fileExtensionDivider);

            if (tokens.length > 1)
            {
                result = tokens[tokens.length - 1];
            }
        }

        return result;
    }



    /**
     * Return the name of the file from the path name.
     *
     * @param pathName path name of a file
     * @return file name (with type) or null
     */
    private String getFileName(String pathName)
    {
        String result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(folderDivider);

            result = tokens[tokens.length - 1];
        }

        return result;
    }


    /**
     * Create an incident report to capture the situation detected by the caller.
     * This incident report will be processed by other governance activities.
     *
     * @param userId caller's userId
     * @param initialClassifications initial classifications to add to the incident report
     * @param properties unique identifier to give this new incident report and description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param originatorGUID the unique identifier of the person or process that created the incident
     *
     * @return unique identifier of the resulting incident report
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException a problem with the metadata store
     */
    public  String createIncidentReport(String                                  userId,
                                        Map<String, ClassificationProperties>   initialClassifications,
                                        IncidentReportProperties                properties,
                                        Map<String, ImpactedResourceProperties> impactedResources,
                                        Map<String, ReportDependencyProperties> previousIncidents,
                                        String                                  originatorGUID) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "createIncidentReport";
        final String propertiesParameterName = "properties";
        final String originatorParameterName = "originatorGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(originatorGUID, originatorParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);
        propertyHelper.validateMandatoryName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        /*
         * Set up the API options
         */
        MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions();
        makeAnchorOptions.setEffectiveTime(new Date());
        makeAnchorOptions.setForLineage(true);
        makeAnchorOptions.setMakeAnchor(false);


        /*
         * Create the incident report entity
         */
        ElementProperties elementProperties = elementBuilder.getElementProperties(properties);

        NewElementOptions newElementOptions = new NewElementOptions(makeAnchorOptions);

        newElementOptions.setIsOwnAnchor(true);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentGUID(originatorGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.REPORT_ORIGINATOR.typeName);

        String incidentReportGUID = openMetadataClient.createMetadataElementInStore(userId,
                                                                                    OpenMetadataType.INCIDENT_REPORT.typeName,
                                                                                    newElementOptions,
                                                                                    classificationBuilder.getInitialClassifications(initialClassifications),
                                                                                    new NewElementProperties(elementProperties),
                                                                                    null);

        if (incidentReportGUID != null)
        {
            if (impactedResources != null)
            {
                for (String resourceGUID : impactedResources.keySet())
                {
                    if (resourceGUID != null)
                    {
                        ImpactedResourceProperties relationshipProperties = impactedResources.get(resourceGUID);

                        openMetadataClient.createRelatedElementsInStore(userId,
                                                                        OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName,
                                                                        resourceGUID,
                                                                        incidentReportGUID,
                                                                        makeAnchorOptions,
                                                                        new NewElementProperties(relationshipBuilder.getElementProperties(relationshipProperties)));
                    }
                }
            }

            if (previousIncidents != null)
            {
                for (String previousIncidentGUID : previousIncidents.keySet())
                {
                    if (previousIncidentGUID != null)
                    {
                        ReportDependencyProperties relationshipProperties = previousIncidents.get(previousIncidentGUID);

                        openMetadataClient.createRelatedElementsInStore(userId,
                                                                        OpenMetadataType.REPORT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                        previousIncidentGUID,
                                                                        incidentReportGUID,
                                                                        makeAnchorOptions,
                                                                        new NewElementProperties(relationshipBuilder.getElementProperties(relationshipProperties)));
                    }
                }
            }
        }

        return incidentReportGUID;
    }




    /**
     * Retrieve the metadata source's unique identifier (GUID) or if it is not defined, create the software server capability
     * for this service.
     *
     * @param userId                    calling user
     * @param metadataSourceQualifiedName unique name of the software capability that represents this integration service
     * @param ownerGUID unique identifier of the owner of the metadata collection
     * @param ownerName name of owner from config
     * @param ownerUserId userId for the owner
     *
     * @return unique identifier of the metadata source
     *
     * @throws InvalidParameterException one of the parameters passed (probably on initialize) is invalid
     * @throws UserNotAuthorizedException the integration daemon's userId does not have access to the partner OMAS
     * @throws PropertyServerException a problem in the remote server running the partner OMAS
     */
    public String setUpMetadataSource(String                userId,
                                      String                metadataSourceQualifiedName,
                                      String                ownerGUID,
                                      String                ownerName,
                                      String                ownerUserId,
                                      ElementOriginCategory originCategory) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        if (metadataSourceQualifiedName != null)
        {
            final String methodName = "setUpMetadataSource";
            final String ownerGUIDParameterName = "ownerGUID";
            final String ownerNameParameterName = "ownerName";

            propertyHelper.validateGUID(ownerGUID, ownerGUIDParameterName, methodName);
            propertyHelper.validateMandatoryName(ownerGUID, ownerNameParameterName, methodName);

            GetOptions getOptions = new GetOptions();

            getOptions.setMetadataElementTypeName(OpenMetadataType.METADATA_COLLECTION.typeName);

            OpenMetadataRootElement metadataCollection = this.getAssetByUniqueName(userId, metadataSourceQualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, getOptions);

            if (metadataCollection == null)
            {
                NewElementOptions newElementOptions = new NewElementOptions();

                newElementOptions.setIsOwnAnchor(true);

                MetadataCollectionProperties metadataCollectionProperties = new MetadataCollectionProperties();

                metadataCollectionProperties.setQualifiedName(OpenMetadataType.METADATA_COLLECTION.typeName + "::" +  ownerName + " [" + ownerGUID + "]");
                if (originCategory != null)
                {
                    metadataCollectionProperties.setDeployedImplementationType(originCategory.getName());
                }
                else
                {
                    metadataCollectionProperties.setDeployedImplementationType(ElementOriginCategory.EXTERNAL_SOURCE.getName());
                }

                metadataCollectionProperties.setDisplayName("Metadata collection for " + ownerName);

                if (ownerUserId != null)
                {
                    metadataCollectionProperties.setDescription("This is the metadata belonging to connector " + ownerName + " that is running with userId " + ownerUserId + ".");
                }
                else
                {
                    metadataCollectionProperties.setDescription("This is the metadata belonging to connector " + ownerName + ".");
                }

                return this.createAsset(userId,
                                        newElementOptions,
                                        null,
                                        metadataCollectionProperties,
                                        null);
            }
            else
            {
                return metadataCollection.getElementHeader().getGUID();
            }
        }

        return null;
    }


    /**
     * Create a new action and link it to the supplied role and targets (if applicable).
     *
     * @param userId                    calling user
     * @param initialClassifications    map of classifications to add to the new action
     * @param properties                properties of the  action
     * @param originatorGUID            optional originator element (such as a person or Governance Service)
     * @param actionSponsorGUID         optional element that maintains the action on their list
     * @param assignToActorGUID         optional actor to assign the action to
     * @param anchorOptions             how should the new action be anchored?
     * @param newActionTargets optional list of elements that the action is to target
     * @return unique identifier of the action
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public String createAction(String                                userId,
                               Map<String, ClassificationProperties> initialClassifications,
                               ActionProperties                      properties,
                               String                                originatorGUID,
                               String                                actionSponsorGUID,
                               String                                assignToActorGUID,
                               AnchorOptions                         anchorOptions,
                               List<NewActionTarget>                 newActionTargets) throws InvalidParameterException,
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

            newElementOptions.setParentGUID(originatorGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName);
        }

        String actionGUID = openMetadataClient.createMetadataElementInStore(userId,
                                                                            metadataElementTypeName,
                                                                            newElementOptions,
                                                                            classificationBuilder.getInitialClassifications(initialClassifications),
                                                                            elementBuilder.getNewElementProperties(properties),
                                                                            parentRelationshipProperties);

        if (actionGUID != null)
        {
            MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions(newElementOptions);

            if (assignToActorGUID != null)
            {
                openMetadataClient.createRelatedElementsInStore(userId,
                                                                OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                assignToActorGUID,
                                                                actionGUID,
                                                                makeAnchorOptions,
                                                                relationshipBuilder.getNewElementProperties(new AssignmentScopeProperties()));
            }

            if (actionSponsorGUID != null)
            {
                openMetadataClient.createRelatedElementsInStore(userId,
                                                                OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                actionSponsorGUID,
                                                                actionGUID,
                                                                makeAnchorOptions,
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
                                                                        actionGUID,
                                                                        newActionTarget.getActionTargetGUID(),
                                                                        makeAnchorOptions,
                                                                        relationshipBuilder.getNewElementProperties(actionTargetProperties));
                    }
                }
            }
        }

        return actionGUID;
    }




    /**
     * Create an action request for someone to work on.
     *
     * @param userId caller's userId
     * @param openMetadataTypeName type of action to create
     * @param properties properties of the action
     * @param initialClassifications classification to add to the action
     * @param actionSourceGUID unique identifier of the source of the action
     * @param actionCauseGUIDs unique identifiers of the cause for the action to be raised
     * @param assignToGUID unique identifier of the Actor element for the recipient
     * @param assignmentType the assignment type for the AssignmentScope relationship
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new action element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException a problem connecting to (or inside) the metadata store
     */
    public String createActorAction(String                                userId,
                                    String                                openMetadataTypeName,
                                    Map<String, ClassificationProperties> initialClassifications,
                                    ActionProperties                      properties,
                                    String                                actionSourceGUID,
                                    List<String>                          actionCauseGUIDs,
                                    String                                assignToGUID,
                                    AssignmentType                        assignmentType,
                                    List<NewActionTarget>                 actionTargets) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        return this.createAction(userId,
                                 openMetadataTypeName,
                                 initialClassifications,
                                 properties,
                                 actionSourceGUID,
                                 actionCauseGUIDs,
                                 assignToGUID,
                                 OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                 assignmentType,
                                 actionTargets);
    }


    /**
     * Create an entry in a note log.
     *
     * @param userId caller's userId
     * @param openMetadataTypeName type of action to create
     * @param properties properties of the action
     * @param actionSourceGUID unique identifier of the source of the action
     * @param actionCauseGUIDs unique identifiers of the cause for the action to be raised
     * @param noteLogGUID unique identifier of the note log
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new action element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException a problem connecting to (or inside) the metadata store
     */
    public String createNoteLogEntry(String                                userId,
                                     String                                openMetadataTypeName,
                                     Map<String, ClassificationProperties> initialClassifications,
                                     ActionProperties                      properties,
                                     String                                actionSourceGUID,
                                     List<String>                          actionCauseGUIDs,
                                     String                                noteLogGUID,
                                     List<NewActionTarget>                 actionTargets) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return this.createAction(userId,
                                 openMetadataTypeName,
                                 initialClassifications,
                                 properties,
                                 actionSourceGUID,
                                 actionCauseGUIDs,
                                 noteLogGUID,
                                 OpenMetadataType.ATTACHED_NOTE_LOG_ENTRY_RELATIONSHIP.typeName,
                                 null,
                                 actionTargets);
    }



    /**
     * Create an action request for someone to work on.
     *
     * @param userId caller's userId
     * @param openMetadataTypeName type of action to create
     * @param initialClassifications classification to add to the action
     * @param properties properties of the action
     * @param actionSourceGUID unique identifier of the source of the action
     * @param actionCauseGUIDs unique identifiers of the cause for the action to be raised
     * @param assignToGUID unique identifier of the Actor element for the recipient
     * @param assignToRelationshipTypeName typeName of the relationship
     * @param assignmentType type of assignment for the action (when assignToRelationshipType is AssignmentScope)
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new action element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException a problem connecting to (or inside) the metadata store
     */
    private String createAction(String                                userId,
                                String                                openMetadataTypeName,
                                Map<String, ClassificationProperties> initialClassifications,
                                ActionProperties                      properties,
                                String                                actionSourceGUID,
                                List<String>                          actionCauseGUIDs,
                                String                                assignToGUID,
                                String                                assignToRelationshipTypeName,
                                AssignmentType                        assignmentType,
                                List<NewActionTarget>                 actionTargets) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "createAction";
        final String propertiesParameterName = "properties";

        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        /*
         * Set up the API options
         */
        MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions();
        makeAnchorOptions.setEffectiveTime(new Date());
        makeAnchorOptions.setForLineage(true);
        makeAnchorOptions.setMakeAnchor(false);

        /*
         * Create the action entity
         */

        NewElementOptions newElementOptions = new NewElementOptions(makeAnchorOptions);

        if (actionSourceGUID != null)
        {
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(actionSourceGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentGUID(actionSourceGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName);
        }
        else
        {
            newElementOptions.setIsOwnAnchor(true);
        }

        String actionGUID = openMetadataClient.createMetadataElementInStore(userId,
                                                                            openMetadataTypeName,
                                                                            newElementOptions,
                                                                            classificationBuilder.getInitialClassifications(initialClassifications),
                                                                            elementBuilder.getNewElementProperties(properties),
                                                                            null);

        if (actionGUID != null)
        {
            if (actionTargets != null)
            {
                /*
                 * Link the action to the items to work on
                 */
                for (NewActionTarget actionTarget : actionTargets)
                {
                    if ((actionTarget != null) && (actionTarget.getActionTargetGUID() != null))
                    {
                        ElementProperties relationshipProperties = propertyHelper.addStringProperty(null,
                                                                                                    OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                                                    actionTarget.getActionTargetName());

                        openMetadataClient.createRelatedElementsInStore(userId,
                                                                        OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                        actionGUID,
                                                                        actionTarget.getActionTargetGUID(),
                                                                        makeAnchorOptions,
                                                                        new NewElementProperties(relationshipProperties));
                    }
                }
            }

            if (assignToGUID != null)
            {
                /*
                 * Link the action and the person/element assigned to complete the work
                 */
                NewElementProperties relationshipProperties = null;

                if ((OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName.equals(assignToRelationshipTypeName)) && (assignmentType != null))
                {
                    relationshipProperties = new NewElementProperties(propertyHelper.addStringProperty(null,
                                                                                                       OpenMetadataProperty.ASSIGNMENT_TYPE.name,
                                                                                                       assignmentType.getName()));
                }

                openMetadataClient.createRelatedElementsInStore(userId,
                                                                assignToRelationshipTypeName,
                                                                assignToGUID,
                                                                actionGUID,
                                                                makeAnchorOptions,
                                                                relationshipProperties);
            }

            if (actionCauseGUIDs != null)
            {
                for (String actionCauseGUID : actionCauseGUIDs)
                {
                    if (actionCauseGUID != null)
                    {
                        /*
                         * Link the action and its cause.
                         */
                        openMetadataClient.createRelatedElementsInStore(userId,
                                                                        OpenMetadataType.ACTIONS_RELATIONSHIP.typeName,
                                                                        actionCauseGUID,
                                                                        actionGUID,
                                                                        makeAnchorOptions,
                                                                        null);
                    }
                }
            }
        }

        return actionGUID;
    }


    /**
     * Create a "To-Do" request for someone to work on.
     *
     * @param userId caller's userId
     * @param initialClassifications classification to add to the action
     * @param properties unique name for the to do plus additional  properties
     * @param assignToGUID unique identifier of the Actor element for the recipient
     * @param sponsorGUID unique identifier of the element that describes the rule, project that this is on behalf of
     * @param originatorGUID unique identifier of the source of the to do
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new to do element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException a problem connecting to (or inside) the metadata store
     */
    public String openToDo(String                                userId,
                           Map<String, ClassificationProperties> initialClassifications,
                           ToDoProperties                        properties,
                           String                                assignToGUID,
                           String                                sponsorGUID,
                           String                                originatorGUID,
                           List<NewActionTarget>                 actionTargets) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                 = "openToDo";
        final String assignToParameterName      = "assignToGUID";
        final String propertiesParameterName    = "properties";
        final String originatorParameterName    = "originatorGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(originatorGUID, originatorParameterName, methodName);
        propertyHelper.validateMandatoryName(assignToGUID, assignToParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);
        propertyHelper.validateMandatoryName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);


        /*
         * Set up the API options
         */
        MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions();
        makeAnchorOptions.setEffectiveTime(new Date());
        makeAnchorOptions.setForLineage(true);
        makeAnchorOptions.setMakeAnchor(false);


        /*
         * Create the to do entity
         */
        NewElementOptions newElementOptions = new NewElementOptions(makeAnchorOptions);

        newElementOptions.setIsOwnAnchor(true);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentGUID(assignToGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName);

        String toDoGUID = openMetadataClient.createMetadataElementInStore(userId,
                                                                          OpenMetadataType.TO_DO.typeName,
                                                                          newElementOptions,
                                                                          classificationBuilder.getInitialClassifications(initialClassifications),
                                                                          new NewElementProperties(elementBuilder.getElementProperties(properties)),
                                                                          null);

        if (toDoGUID != null)
        {
            if (actionTargets != null)
            {
                for (NewActionTarget actionTarget : actionTargets)
                {
                    if ((actionTarget != null) && (actionTarget.getActionTargetGUID() != null))
                    {
                        ElementProperties relationshipProperties = propertyHelper.addStringProperty(null,
                                                                                                    OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                                                    actionTarget.getActionTargetName());

                        openMetadataClient.createRelatedElementsInStore(userId,
                                                                        OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                        toDoGUID,
                                                                        actionTarget.getActionTargetGUID(),
                                                                        makeAnchorOptions,
                                                                        new NewElementProperties(relationshipProperties));
                    }
                }
            }

            if (sponsorGUID != null)
            {
                /*
                 * Link the "to do" and the sponsor
                 */
                openMetadataClient.createRelatedElementsInStore(userId,
                                                                OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                sponsorGUID,
                                                                toDoGUID,
                                                                makeAnchorOptions,
                                                                new NewElementProperties(propertyHelper.addStringProperty(null,
                                                                                                                          OpenMetadataProperty.ASSIGNMENT_TYPE.name,
                                                                                                                          AssignmentType.SPONSOR.getName())));
            }

            if (originatorGUID != null)
            {
                /*
                 * Link the "to do" and the originator
                 */
                openMetadataClient.createRelatedElementsInStore(userId,
                                                                OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName,
                                                                originatorGUID,
                                                                toDoGUID,
                                                                makeAnchorOptions,
                                                                null);
            }
        }

        return toDoGUID;
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
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createAssetFromTemplate(String                 userId,
                                          TemplateOptions        templateOptions,
                                          String                 templateGUID,
                                          EntityProperties       replacementProperties,
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
     * Add an element to an action's workload.
     *
     * @param userId                userId of user making request.
     * @param actionGUID        unique identifier of the integration connector.
     * @param makeAnchorOptions options to control access to open metadata
     * @param actionTargetProperties  properties describing the relationship characteristics.
     * @param elementGUID           unique identifier of the target element.
     * @return relationship GUID
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addActionTarget(String                  userId,
                                   String                 actionGUID,
                                   String                 elementGUID,
                                   MakeAnchorOptions      makeAnchorOptions,
                                   ActionTargetProperties actionTargetProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName               = "addActionTarget";
        final String assetGUIDParameterName   = "actionGUID";
        final String elementGUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(actionGUID, assetGUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                               actionGUID,
                                                               elementGUID,
                                                               makeAnchorOptions,
                                                               relationshipBuilder.getNewElementProperties(actionTargetProperties));
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
     * Retrieve a specific action target associated with an action.
     *
     * @param userId identifier of calling user.
     * @param relationshipGUID unique identifier of the relationship.
     * @param getOptions options to control the retrieve
     *
     * @return details of the integration connector and the elements it is to catalog
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public OpenMetadataRelationship getActionTarget(String     userId,
                                                    String     relationshipGUID,
                                                    GetOptions getOptions) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                = "getActionTarget";
        final String relationshipGUIDParameter = "relationshipGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(relationshipGUID, relationshipGUIDParameter, methodName);

        return openMetadataClient.getRelationshipByGUID(userId, relationshipGUID, getOptions);
    }


    /**
     * Return a list of elements that are target elements for an action.
     *
     * @param userId         userId of user making request.
     * @param actionGUID unique identifier of the integration connector.
     * @param queryOptions   options for query
     * @return list of member details
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getActionTargets(String         userId,
                                                          String         actionGUID,
                                                          ActivityStatus activityStatus,
                                                          QueryOptions   queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName             = "getActionTargets";
        final String assetGUIDParameterName = "actionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(actionGUID, assetGUIDParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<OpenMetadataRootElement> actionTargets = super.getRelatedRootElements(userId,
                                                                                   actionGUID,
                                                                                   assetGUIDParameterName,
                                                                                   1,
                                                                                   OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                                   OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                                   queryOptions,
                                                                                   methodName);

        return filterActionTargets(actionTargets, activityStatus);
    }


    /**
     * Retrieve the actions that are chained from an action target element.
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

        List<OpenMetadataRootElement> relatedMetadataElements = super.getRelatedRootElements(userId,
                                                                                             elementGUID,
                                                                                             guidParameterName,
                                                                                             2,
                                                                                             OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                                             OpenMetadataType.ACTION.typeName,
                                                                                             queryOptions,
                                                                                             methodName);

        return this.filterActionTargets(relatedMetadataElements, activityStatus);
    }

    /**
     * Assign an action to an actor.
     *
     * @param userId    calling user
     * @param actionGUID  unique identifier of the action
     * @param actorGUID actor to assign the action to
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties the properties of the relationship
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public void assignAction(String                    userId,
                             String                    actionGUID,
                             String                    actorGUID,
                             MakeAnchorOptions         makeAnchorOptions,
                             AssignmentScopeProperties relationshipProperties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName              = "assignAction";
        final String actionGUIDParameterName   = "actionGUID";
        final String parentGUIDParameterName = "actorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(actionGUID, actionGUIDParameterName, methodName);
        propertyHelper.validateGUID(actorGUID, parentGUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                        actorGUID,
                                                        actionGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Assign an action to a new actor - removing all other assignees.
     *
     * @param userId    calling user
     * @param actionGUID  unique identifier of the action
     * @param actorGUID actor to assign the action to
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties the properties of the relationship
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public void reassignAction(String                    userId,
                               String                    actionGUID,
                               String                    actorGUID,
                               MakeAnchorOptions         makeAnchorOptions,
                               AssignmentScopeProperties relationshipProperties) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName              = "reassignAction";
        final String actionGUIDParameterName   = "actionGUID";
        final String parentGUIDParameterName = "actorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(actionGUID, actionGUIDParameterName, methodName);
        propertyHelper.validateGUID(actorGUID, parentGUIDParameterName, methodName);

        RelatedMetadataElementList assignedActors = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                  actionGUID,
                                                                                                  2,
                                                                                                  OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                                                  new QueryOptions(makeAnchorOptions));

        if ((assignedActors != null) && (assignedActors.getElementList() != null))
        {
            for (RelatedMetadataElement assignedActor : assignedActors.getElementList())
            {
                openMetadataClient.deleteRelationshipInStore(userId,
                                                             assignedActor.getRelationshipGUID(),
                                                             new DeleteOptions(makeAnchorOptions));
            }
        }

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                        actorGUID,
                                                        actionGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Remove an action from an actor.
     *
     * @param userId    calling user
     * @param actionGUID  unique identifier of the action
     * @param actorGUID actor to assign the action to
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public void unassignAction(String        userId,
                               String        actionGUID,
                               String        actorGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName              = "unassignAction";
        final String actionGUIDParameterName = "actionGUID";
        final String parentGUIDParameterName = "actorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(actionGUID, actionGUIDParameterName, methodName);
        propertyHelper.validateGUID(actorGUID, parentGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                        actorGUID,
                                                        actionGUID,
                                                        deleteOptions);
    }


    /**
     * Update the properties of an asset.
     *
     * @param userId                 userId of user making request.
     * @param assetGUID       unique identifier of the asset (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateAsset(String          userId,
                               String          assetGUID,
                               UpdateOptions   updateOptions,
                               AssetProperties properties) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName        = "updateAsset";
        final String guidParameterName = "assetGUID";

        return super.updateElement(userId,
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
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deployITAsset(String                userId,
                              String                assetGUID,
                              String                destinationGUID,
                              MakeAnchorOptions     makeAnchorOptions,
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
                                                        makeAnchorOptions,
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
                                                        assetGUID,
                                                        destinationGUID,
                                                        deleteOptions);
    }



    /**
     * Create a relationship that links a software capability to an infrastructure asset like a software server.
     *
     * @param userId                 userId of user making request
     * @param assetGUID       unique identifier of the asset
     * @param capabilityGUID           unique identifier of the destination asset
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSoftwareCapability(String                                userId,
                                       String                                assetGUID,
                                       String                                capabilityGUID,
                                       MakeAnchorOptions                     makeAnchorOptions,
                                       SupportedSoftwareCapabilityProperties relationshipProperties) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName            = "linkSoftwareCapability";
        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "capabilityGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(capabilityGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName,
                                                        assetGUID,
                                                        capabilityGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Remove a relationship that links a software capability to an infrastructure asset like a software server.
     *
     * @param userId                 userId of user making request.
     * @param assetGUID       unique identifier of the asset
     * @param capabilityGUID           unique identifier of the destination asset
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSoftwareCapability(String        userId,
                                         String        assetGUID,
                                         String        capabilityGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "unDeployITAsset";

        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "capabilityGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(capabilityGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName,
                                                        assetGUID,
                                                        capabilityGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a data set to another asset (typically a data store) that is supplying the data.
     *
     * @param userId                 userId of user making request
     * @param dataSetGUID          unique identifier of the data set
     * @param dataContentAssetGUID          unique identifier of the data asset supplying the data
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDataSetContent(String                   userId,
                                   String                   dataSetGUID,
                                   String                   dataContentAssetGUID,
                                   MakeAnchorOptions        makeAnchorOptions,
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
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a data set from another asset that was supplying the data and is no more.
     *
     * @param userId                 userId of user making request.
     * @param dataSetGUID          unique identifier of the data set
     * @param dataContentAssetGUID          unique identifier of the data asset supplying the data
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * Add an element to an integration connector's workload.
     *
     * @param userId                userId of user making request.
     * @param integrationConnectorGUID        unique identifier of the integration connector.
     * @param makeAnchorOptions options to control access to open metadata
     * @param catalogTargetProperties  properties describing the relationship characteristics.
     * @param elementGUID           unique identifier of the target element.
     * @return relationship GUID
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCatalogTarget(String                  userId,
                                   String                  integrationConnectorGUID,
                                   String                  elementGUID,
                                   MakeAnchorOptions       makeAnchorOptions,
                                   CatalogTargetProperties catalogTargetProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName               = "addCatalogTarget";
        final String assetGUIDParameterName   = "integrationConnectorGUID";
        final String elementGUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(integrationConnectorGUID, assetGUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName,
                                                               integrationConnectorGUID,
                                                               elementGUID,
                                                               makeAnchorOptions,
                                                               relationshipBuilder.getNewElementProperties(catalogTargetProperties));
    }


    /**
     * Update the properties of a catalog target relationship.
     *
     * @param userId               calling user
     * @param relationshipGUID     unique identifier of the relationship
     * @param updateOptions        provides a structure for the additional options when updating a relationship.
     * @param catalogTargetProperties properties describing the catalog target processing characteristics.
     */
    public void updateCatalogTarget(String                  userId,
                                    String                  relationshipGUID,
                                    UpdateOptions           updateOptions,
                                    CatalogTargetProperties catalogTargetProperties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        openMetadataClient.updateRelationshipInStore(userId,
                                                     relationshipGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getElementProperties(catalogTargetProperties));
    }


    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param userId identifier of calling user.
     * @param relationshipGUID unique identifier of the relationship.
     * @param getOptions options to control the retrieve
     *
     * @return details of the integration connector and the elements it is to catalog
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public OpenMetadataRelationship getCatalogTarget(String     userId,
                                                     String     relationshipGUID,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                        = "getCatalogTarget";
        final String integrationConnectorGUIDParameter = "relationshipGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(relationshipGUID, integrationConnectorGUIDParameter, methodName);

        return openMetadataClient.getRelationshipByGUID(userId, relationshipGUID, getOptions);
    }


    /**
     * Return a list of elements that are target elements for an integration connector.
     *
     * @param userId         userId of user making request.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param queryOptions   options for query
     * @return list of member details
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getCatalogTargets(String       userId,
                                                           String       integrationConnectorGUID,
                                                           QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName             = "getCatalogTargets";
        final String assetGUIDParameterName = "integrationConnectorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(integrationConnectorGUID, assetGUIDParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        return super.getRelatedRootElements(userId,
                                            integrationConnectorGUID,
                                            assetGUIDParameterName,
                                            1,
                                            OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName,
                                            OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                            queryOptions,
                                            methodName);
    }



    /**
     * Remove an element from an integration connector's workload.
     *
     * @param userId         userId of user making request.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param elementGUID    unique identifier of the element.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeCatalogTarget(String        userId,
                                    String        integrationConnectorGUID,
                                    String        elementGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName               = "removeCatalogTarget";
        final String assetGUIDParameterName   = "integrationConnectorGUID";
        final String elementGUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(integrationConnectorGUID, assetGUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName,
                                                        integrationConnectorGUID,
                                                        elementGUID,
                                                        deleteOptions);
    }


    /**
     * Remove an element from an integration connector's workload.
     *
     * @param userId         userId of user making request.
     * @param relationshipGUID unique identifier of the relationship.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeCatalogTarget(String        userId,
                                    String        relationshipGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName                    = "removeCatalogTarget";
        final String relationshipGUIDParameterName = "relationshipGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);

        openMetadataClient.deleteRelationshipInStore(userId, relationshipGUID, deleteOptions);
    }


    /**
     * Attach an API to an endpoint
     *
     * @param userId                 userId of user making request
     * @param deployedAPIGUID          unique identifier of the super team
     * @param endpointGUID            unique identifier of the subteam
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAPIEndpoint(String                 userId,
                                String                 deployedAPIGUID,
                                String                 endpointGUID,
                                MakeAnchorOptions      makeAnchorOptions,
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
                                                        makeAnchorOptions,
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkProcessHierarchy(String                     userId,
                                     String                     parentProcessGUID,
                                     String                     childProcessGUID,
                                     MakeAnchorOptions          makeAnchorOptions,
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
                                                        makeAnchorOptions,
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
                                                        parentProcessGUID,
                                                        childProcessGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a file to a folder.
     *
     * @param userId                 userId of user making request
     * @param folderGUID               unique identifier of the folder
     * @param fileGUID         unique identifier of the associated file
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedFiles(String                userId,
                                String                folderGUID,
                                String                fileGUID,
                                MakeAnchorOptions     makeAnchorOptions,
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
                                                        makeAnchorOptions,
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkLinkedFiles(String                userId,
                                String                folderGUID,
                                String                fileGUID,
                                MakeAnchorOptions     makeAnchorOptions,
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
                                                        makeAnchorOptions,
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkFolderHierarchy(String                    userId,
                                    String                    parentFolderGUID,
                                    String                    childFolderGUID,
                                    MakeAnchorOptions         makeAnchorOptions,
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
                                                        makeAnchorOptions,
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @throws PropertyServerException a problem in the property server
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
     * Retrieve the actions that are chained off a sponsor's element.
     *
     * @param userId      calling user
     * @param sponsorGUID unique identifier of the element to start with
     * @param activityStatus  optional activity status
     * @param queryOptions           multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> getActionsForSponsor(String         userId,
                                                              String         sponsorGUID,
                                                              ActivityStatus activityStatus,
                                                              QueryOptions   queryOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName        = "getActionsForSponsor";
        final String guidParameterName = "sponsorGUID";

        return getFilteredActions(userId,
                                  sponsorGUID,
                                  guidParameterName,
                                  activityStatus,
                                  OpenMetadataType.ACTIONS_RELATIONSHIP.typeName,
                                  1,
                                  queryOptions,
                                  methodName);
    }


    /**
     * Retrieve the actions that are chained off a sponsor's element.
     *
     * @param userId      calling user
     * @param requesterGUID unique identifier of the element to start with
     * @param activityStatus  optional activity status
     * @param queryOptions           multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> getActionsFromRequester(String         userId,
                                                                 String         requesterGUID,
                                                                 ActivityStatus activityStatus,
                                                                 QueryOptions   queryOptions) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName        = "getActionsForSponsor";
        final String guidParameterName = "requesterGUID";

        return getFilteredActions(userId,
                                  requesterGUID,
                                  guidParameterName,
                                  activityStatus,
                                  OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName,
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
     * @param queryOptions           multiple options to control the query
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
                                                             QueryOptions   queryOptions,
                                                             String         methodName) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        List<OpenMetadataRootElement> relatedMetadataElements = super.getRelatedRootElements(userId,
                                                                                             elementGUID,
                                                                                             guidParameterName,
                                                                                             startingAtEnd,
                                                                                             relationshipTypeName,
                                                                                             OpenMetadataType.ACTION.typeName,
                                                                                             queryOptions,
                                                                                             methodName);

        return this.filterProcesses(relatedMetadataElements, activityStatus);
    }


    /**
     * Retrieve the Processes that match the search string.
     *
     * @param userId       calling user
     * @param searchString string to search for (may include RegExs)
     * @param activityStatus   optional  status
     * @param suppliedSearchOptions           multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> findProcesses(String         userId,
                                                       String         searchString,
                                                       ActivityStatus activityStatus,
                                                       SearchOptions  suppliedSearchOptions) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName = "findProcesses";

        SearchOptions searchOptions = new SearchOptions(suppliedSearchOptions);

        if (searchOptions.getMetadataElementTypeName() == null)
        {
            searchOptions.setMetadataElementTypeName(OpenMetadataType.PROCESS.typeName);
        }

        List<OpenMetadataRootElement> openMetadataElements = this.findRootElements(userId,
                                                                                   searchString,
                                                                                   searchOptions,
                                                                                   methodName);

        return filterProcesses(openMetadataElements, activityStatus);
    }


    /**
     * Retrieve the processes that match the category name and status.
     *
     * @param userId     calling user
     * @param category   type to search for
     * @param activityStatus optional status
     * @param suppliedQueryOptions multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> getProcessesByCategory(String         userId, 
                                                                String         category, 
                                                                ActivityStatus activityStatus, 
                                                                QueryOptions   suppliedQueryOptions) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getProcessesByCategory";

        QueryOptions queryOptions = new QueryOptions(suppliedQueryOptions);

        if (queryOptions.getMetadataElementTypeName() == null)
        {
            queryOptions.setMetadataElementTypeName(OpenMetadataType.PROCESS.typeName);
        }

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
     * Filter process objects by activity status.
     *
     * @param openMetadataRootElements retrieved elements
     * @param activityStatus           optional activity status
     * @return list of process elements
     */
    private List<OpenMetadataRootElement> filterActionTargets(List<OpenMetadataRootElement> openMetadataRootElements,
                                                              ActivityStatus                activityStatus)
    {
        if (openMetadataRootElements != null)
        {
            List<OpenMetadataRootElement> rootElements = new ArrayList<>();

            for (OpenMetadataRootElement openMetadataRootElement : openMetadataRootElements)
            {
                if ((openMetadataRootElement != null) && (openMetadataRootElement.getRelatedBy() != null) && (openMetadataRootElement.getRelatedBy().getRelationshipProperties() instanceof ActionTargetProperties actionTargetProperties))
                {
                    if ((activityStatus == null) || (activityStatus == actionTargetProperties.getActivityStatus()))
                    {
                        rootElements.add(openMetadataRootElement);
                    }
                }
            }

            return rootElements;
        }

        return null;
    }


    /**
     * Retrieve the data assets that match the search string and optional content status.
     *
     * @param userId       calling user
     * @param searchString string to search for (may include RegExs)
     * @param contentStatus   optional  status
     * @param suppliedSearchOptions   multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> findDataAssets(String         userId,
                                                        String         searchString,
                                                        ContentStatus  contentStatus,
                                                        SearchOptions  suppliedSearchOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "findDataAssets";

        SearchOptions searchOptions = new SearchOptions(suppliedSearchOptions);

        if (searchOptions.getMetadataElementTypeName() == null)
        {
            searchOptions.setMetadataElementTypeName(OpenMetadataType.DATA_ASSET.typeName);
        }

        List<OpenMetadataRootElement> openMetadataElements = this.findRootElements(userId,
                                                                                   searchString,
                                                                                   searchOptions,
                                                                                   methodName);

        return filterDataSets(openMetadataElements, contentStatus);
    }


    /**
     * Retrieve the data assets that match the category name and status.
     *
     * @param userId     calling user
     * @param category   type to search for
     * @param contentStatus optional status
     * @param suppliedQueryOptions multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> getDataAssetsByCategory(String        userId,
                                                                 String        category,
                                                                 ContentStatus contentStatus,
                                                                 QueryOptions  suppliedQueryOptions) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        final String methodName = "getDataAssetsByCategory";

        QueryOptions queryOptions = new QueryOptions(suppliedQueryOptions);

        if (queryOptions.getMetadataElementTypeName() == null)
        {
            queryOptions.setMetadataElementTypeName(OpenMetadataType.DATA_ASSET.typeName);
        }

        List<OpenMetadataRootElement> openMetadataElements = super.getRootElementsByName(userId,
                                                                                         category,
                                                                                         List.of(OpenMetadataProperty.CATEGORY.name),
                                                                                         queryOptions,
                                                                                         methodName);

        return filterDataSets(openMetadataElements, contentStatus);
    }


    /**
     * Filter data set objects by content status.
     *
     * @param openMetadataRootElements retrieved elements
     * @param contentStatus           optional  status
     * @return list of process elements
     */
    private List<OpenMetadataRootElement> filterDataSets(List<OpenMetadataRootElement> openMetadataRootElements,
                                                         ContentStatus                 contentStatus)
    {
        if (openMetadataRootElements != null)
        {
            List<OpenMetadataRootElement> rootElements = new ArrayList<>();

            for (OpenMetadataRootElement openMetadataRootElement : openMetadataRootElements)
            {
                if ((openMetadataRootElement != null) &&
                        (openMetadataRootElement.getProperties() instanceof DataAssetProperties dataAssetProperties))
                {
                    if ((contentStatus == null) || (contentStatus == dataAssetProperties.getContentStatus()))
                    {
                        rootElements.add(openMetadataRootElement);
                    }
                }
            }

            return rootElements;
        }

        return null;
    }


    /**
     * Retrieve the data sets that match the search string and optional content status.
     *
     * @param userId       calling user
     * @param searchString string to search for (may include RegExs)
     * @param deploymentStatus   optional  status
     * @param searchOptions   multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> findInfrastructure(String           userId,
                                                            String           searchString,
                                                            DeploymentStatus deploymentStatus,
                                                            SearchOptions    searchOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "findInfrastructure";

        List<OpenMetadataRootElement> openMetadataElements = this.findRootElements(userId,
                                                                                   searchString,
                                                                                   searchOptions,
                                                                                   methodName);

        return filterInfrastructure(openMetadataElements, deploymentStatus);
    }


    /**
     * Retrieve the infrastructure elements that match the category name and status.
     *
     * @param userId     calling user
     * @param category   type to search for
     * @param deploymentStatus optional status
     * @param queryOptions multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> getInfrastructureByCategory(String           userId,
                                                                     String           category,
                                                                     DeploymentStatus deploymentStatus,
                                                                     QueryOptions     queryOptions) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName = "getInfrastructureByCategory";

        List<OpenMetadataRootElement> openMetadataElements = super.getRootElementsByName(userId,
                                                                                         category,
                                                                                         List.of(OpenMetadataProperty.CATEGORY.name),
                                                                                         queryOptions,
                                                                                         methodName);

        return filterInfrastructure(openMetadataElements, deploymentStatus);
    }


    /**
     * Filter infrastructure objects by deployment status.
     *
     * @param openMetadataRootElements retrieved elements
     * @param deploymentStatus           optional  status
     * @return list of process elements
     */
    private List<OpenMetadataRootElement> filterInfrastructure(List<OpenMetadataRootElement> openMetadataRootElements,
                                                               DeploymentStatus              deploymentStatus)
    {
        if (openMetadataRootElements != null)
        {
            List<OpenMetadataRootElement> rootElements = new ArrayList<>();

            for (OpenMetadataRootElement openMetadataRootElement : openMetadataRootElements)
            {
                if ((openMetadataRootElement != null) &&
                        (openMetadataRootElement.getProperties() instanceof InfrastructureProperties infrastructureProperties))
                {
                    if ((deploymentStatus == null) || (deploymentStatus == infrastructureProperties.getDeploymentStatus()))
                    {
                        rootElements.add(openMetadataRootElement);
                    }
                }
            }

            return rootElements;
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
                                            OpenMetadataType.ASSET.typeName,
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
                                            OpenMetadataType.REFERENCEABLE.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Returns the list of assets providing data to the data set.
     *
     * @param userId                 userId of user making request
     * @param elementGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSupportedDataSets(String       userId,
                                                              String       elementGUID,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getSupportedDataSets";
        final String guidPropertyName = "elementGUID";

        return super.getRelatedRootElements(userId,
                                            elementGUID,
                                            guidPropertyName,
                                            2,
                                            OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                            OpenMetadataType.ASSET.typeName,
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
                                            OpenMetadataType.DATA_FILE.typeName,
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
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
     * @throws PropertyServerException a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<OpenMetadataRootElement> findAssetsInDomain(String        userId,
                                                       String        searchString,
                                                       SearchOptions searchOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "findAssetsInDomain";
        final String nameParameter = "searchString";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateSearchString(searchString, nameParameter, methodName);

        List<AnchorSearchMatches> matches = openMetadataClient.findElementsInAnchorDomain(userId,
                                                                                           searchString,
                                                                                           OpenMetadataType.ASSET.typeName,
                                                                                           searchOptions);

        if (matches != null)
        {
            List<OpenMetadataRootElement> assetSearchMatchesList = new ArrayList<>();

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
     * @throws PropertyServerException - a problem retrieving the connected asset properties from the property server or
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
            lineageRelationshipTypeNames.add(OpenMetadataType.REQUEST_FOR_ACTION_TARGET_RELATIONSHIP.typeName);
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
     * @throws PropertyServerException a problem access in the property server
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
            Map<String, String> receivedRelationshipStartingElementGUIDs = new HashMap<>();

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
                            receivedRelationshipStartingElementGUIDs.put(relationship.getRelationshipGUID(), assetGUID);
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
                                                                                                 null,
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
                                    receivedRelationshipStartingElementGUIDs.put(relationship.getRelationshipGUID(), metadataElement.getElementGUID());

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
                        RelatedMetadataNodeSummary metadataRelationship = propertyHelper.getRelatedNodeSummary(receivedRelationshipStartingElementGUIDs.get(relationshipGUID),
                                                                                                               receivedRelationships.get(relationshipGUID));

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

            assetGraph.setAnchorMermaidGraph(graphBuilder.getMermaidGraph());
            assetGraph.setInformationSupplyChainMermaidGraph(graphBuilder.getInformationSupplyChainMermaidGraph());
            assetGraph.setFieldLevelLineageGraph(graphBuilder.getFieldLevelLineageGraph());
            assetGraph.setActionMermaidGraph(graphBuilder.getActionGraph());
            assetGraph.setLocalLineageGraph(graphBuilder.getLocalLineageGraph());

            return assetGraph;
        }


        return null;
    }
}
