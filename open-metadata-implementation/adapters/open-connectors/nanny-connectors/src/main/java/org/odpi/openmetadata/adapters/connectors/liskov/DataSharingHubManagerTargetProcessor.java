/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.liskov;


import org.odpi.openmetadata.adapters.connectors.liskov.controls.LiskovConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.liskov.ffdc.LiskovAuditCode;
import org.odpi.openmetadata.adapters.connectors.liskov.ffdc.LiskovErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataHierarchySummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DeployedDatabaseSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.CSVFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FileFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.EngineActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DataSharingHubProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.TargetForGovernanceActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.TypeEmbeddedAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * Maintains the data dictionary for a single data sharing hub.  On each refresh it works through the data stores
 * that are members of the hub and, for each one, ensures that the contents of the member are being catalogued,
 * requests a fresh survey of it, and then abstracts the data fields and data structures found in its schema into
 * the hub's data dictionary.  The cataloguing and survey requests are made by starting the governance action types
 * that the content packs register against the member's technology type, so they run asynchronously in a governance
 * engine and their results are picked up by a later refresh.
 */
public class DataSharingHubManagerTargetProcessor extends CatalogTargetProcessorBase
{
    /**
     * These are the statuses of an engine action that mean it is either running, or is on its way to running.
     * They are used to detect that a cataloguing or survey request that Liskov made on a previous refresh is
     * still outstanding, so that a duplicate request is not made.
     */
    private static final List<ActivityStatus> liveActivityStatuses = List.of(ActivityStatus.REQUESTED,
                                                                            ActivityStatus.APPROVED,
                                                                            ActivityStatus.WAITING,
                                                                            ActivityStatus.ACTIVATING,
                                                                            ActivityStatus.IN_PROGRESS,
                                                                            ActivityStatus.PAUSED);

    /*
     * These caches are rebuilt on each refresh.  They ensure that each element is only considered once, and that
     * the supporting definitions are only retrieved once, no matter how many members of the data sharing hub use them.
     */
    private final Set<String>                          governedElements     = new HashSet<>();
    private final Map<String, OpenMetadataRootElement> technologyTypeCache  = new HashMap<>();
    private final Map<String, OpenMetadataRootElement> actionTypeCache      = new HashMap<>();
    private final Map<String, Set<String>>             catalogTargetCache   = new HashMap<>();

    /*
     * The surveys that the connector has been configured not to run.  When this is empty - which is the default -
     * every survey registered for a member's technology type is run.
     */
    private Set<String> excludedSurveys = new HashSet<>();

    /**
     * Constructor
     *
     * @param catalogTarget catalog target information
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public DataSharingHubManagerTargetProcessor(CatalogTarget            catalogTarget,
                                                CatalogTargetContext     catalogTargetContext,
                                                Connector                connectorToTarget,
                                                String                   connectorName,
                                                AuditLog                 auditLog)
    {
        super(catalogTarget, catalogTargetContext, connectorToTarget, connectorName, auditLog);
    }




    /* ==============================================================================
     * Standard methods that trigger activity.
     */


    /**
     * Work through the members of the data sharing hub, ensuring that each one is being catalogued and surveyed,
     * and adding the data fields and data structures found in their schemas to the hub's data dictionary.  The
     * data dictionary and its two standard collection folders are created if they are missing.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is unable to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected so stop refresh processing
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        super.refresh();

        /*
         * The caches that support the cataloguing and surveying of the data sharing hub's members only apply to a
         * single refresh so that changes made by other processes are picked up next time around.
         */
        governedElements.clear();
        technologyTypeCache.clear();
        actionTypeCache.clear();
        catalogTargetCache.clear();

        excludedSurveys = this.getExcludedSurveys();

        try
        {
            /*
             * This is a lookup table for data fields and data structures found in the data sharing hub.
             * They are used to match schema attributes to the data fields.
             */
            Map<String, Set<String>> dataStructures = new HashMap<>();

            OpenMetadataRootElement dataSharingHubElement = this.getCatalogTargetElement();

            /*
             * Notice that members that are not data sharing hubs are skipped.
             */
            if ((dataSharingHubElement != null) &&
                    (propertyHelper.isTypeOf(dataSharingHubElement.getElementHeader(), OpenMetadataType.DATA_SHARING_HUB.typeName)) &&
                    (dataSharingHubElement.getProperties() instanceof DataSharingHubProperties dataSharingHubProperties))
            {
                String dataSharingHubGUID = dataSharingHubElement.getElementHeader().getGUID();
                String dataDictionaryGUID = null;

                if (dataSharingHubElement.getDataDescription() != null)
                {
                    for (RelatedMetadataElementSummary dataDescription : dataSharingHubElement.getDataDescription())
                    {
                        if ((dataDescription != null) &&
                                (propertyHelper.isTypeOf(dataDescription.getRelatedElement().getElementHeader(),
                                                         OpenMetadataType.DATA_DICTIONARY_COLLECTION.typeName)))
                        {
                            dataDictionaryGUID = dataDescription.getRelatedElement().getElementHeader().getGUID();
                            break;
                        }
                    }
                }

                CollectionClient collectionClient = integrationContext.getCollectionClient(OpenMetadataType.DATA_DICTIONARY_COLLECTION.typeName);

                /*
                 * Create the data dictionary if it is missing
                 */
                if (dataDictionaryGUID == null)
                {
                    DataDictionaryProperties dataDictionaryProperties = new DataDictionaryProperties();

                    dataDictionaryProperties.setQualifiedName(dataSharingHubProperties.getQualifiedName() + "_DataDictionary");
                    dataDictionaryProperties.setDisplayName("Data dictionary for " + dataSharingHubProperties.getDisplayName());

                    NewElementOptions newElementOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());

                    newElementOptions.setIsOwnAnchor(false);
                    newElementOptions.setAnchorGUID(dataSharingHubGUID);
                    newElementOptions.setParentGUID(dataSharingHubGUID);
                    newElementOptions.setParentAtEnd1(true);
                    newElementOptions.setParentRelationshipTypeName(OpenMetadataType.DATA_DESCRIPTION_RELATIONSHIP.typeName);

                    DataDescriptionProperties dataDescriptionProperties = new DataDescriptionProperties();

                    dataDescriptionProperties.setLabel("Data dictionary");
                    dataDescriptionProperties.setDescription("Details of the data fields found in the data sharing hub.");

                    dataDictionaryGUID = collectionClient.createCollection(newElementOptions,
                                                                           null,
                                                                           dataDictionaryProperties,
                                                                           dataDescriptionProperties);

                    auditLog.logMessage(methodName, LiskovAuditCode.NEW_DATA_DICTIONARY.getMessageDefinition(connectorName,
                                                                                                             dataSharingHubProperties.getDisplayName(),
                                                                                                             dataSharingHubGUID));
                }

                OpenMetadataRootElement dataDictionary = collectionClient.getCollectionByGUID(dataDictionaryGUID, collectionClient.getGetOptions());

                if (dataDictionary != null)
                {
                    auditLog.logMessage(methodName, LiskovAuditCode.RETRIEVING_DATA_FIELDS.getMessageDefinition(connectorName,
                                                                                                                dataSharingHubProperties.getDisplayName(),
                                                                                                                dataSharingHubGUID));

                    /*
                     * Extract the existing data fields into the common collection folders.
                     * The owner of the data sharing hub may add new organizing folders to the data dictionary,
                     * but these 2 special folders are always used for new elements.
                     */
                    CollectionFolderProperties collectionFolderProperties = new CollectionFolderProperties();

                    collectionFolderProperties.setQualifiedName(OpenMetadataType.COLLECTION_FOLDER.typeName + "::" + dataSharingHubGUID + "_DataFields");
                    collectionFolderProperties.setDisplayName("Data Fields");
                    collectionFolderProperties.setDescription("Data fields found in the " + dataSharingHubProperties.getDisplayName() + " data sharing hub.");

                    String dataFieldsFolderGUID = getCollectionFolder(dataSharingHubGUID, dataDictionaryGUID, collectionFolderProperties);

                    collectionFolderProperties = new CollectionFolderProperties();

                    collectionFolderProperties.setQualifiedName(OpenMetadataType.COLLECTION_FOLDER.typeName + "::" + dataSharingHubGUID + "::DataStructures");
                    collectionFolderProperties.setDisplayName("Data Structures");
                    collectionFolderProperties.setDescription("Data structures found in the " + dataSharingHubProperties.getDisplayName() + " data sharing hub.");

                    String dataStructureFolderGUID = getCollectionFolder(dataSharingHubGUID, dataDictionaryGUID, collectionFolderProperties);

                    /*
                     * Process the members of the data sharing hub
                     */
                    if (dataSharingHubElement.getCollectionMembers() != null)
                    {
                        for (RelatedMetadataElementSummary member : dataSharingHubElement.getCollectionMembers())
                        {
                            if ((member != null) && (member.getRelatedElement().getProperties() instanceof DataStoreProperties dataStoreProperties))
                            {
                                auditLog.logMessage(methodName, LiskovAuditCode.REFRESHING_DATA_HUB_STORE.getMessageDefinition(connectorName,
                                                                                                                               member.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                                                                                               dataStoreProperties.getDisplayName(),
                                                                                                                               member.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                               dataSharingHubProperties.getDisplayName(),
                                                                                                                               dataSharingHubGUID));

                                refreshDataStore(dataFieldsFolderGUID, dataStructureFolderGUID, member.getRelatedElement().getElementHeader().getGUID(), dataSharingHubGUID, dataSharingHubProperties.getQualifiedName(), dataStructures);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LiskovAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                            error.getClass().getName(),
                                                                                            methodName,
                                                                                            error.getMessage()),
                                  error);


            throw new ConnectorCheckedException(LiskovErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }

    /**
     * Retrieves or creates a new collection folder for the data dictionary.
     *
     * @param dataSharingHubGUID Unique identifier of the DataSharingHub where the DataField resides or will be created.
     * @param dataDictionaryGUID unique identifier from the data dictionary.
     * @param collectionFolderProperties The properties of the DataField to retrieve or create.
     *
     * @return The unique identifier (GUID) of the retrieved or newly created DataField.
     *
     * @throws UserNotAuthorizedException If the user is not authorized to perform the operation.
     * @throws InvalidParameterException If any of the provided parameters are invalid.
     * @throws PropertyServerException If there is an error with the metadata server while performing the operation.
     */
    private String getCollectionFolder(String                     dataSharingHubGUID,
                                       String                     dataDictionaryGUID,
                                       CollectionFolderProperties collectionFolderProperties) throws UserNotAuthorizedException,
                                                                                                     InvalidParameterException,
                                                                                                     PropertyServerException
    {
        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();
        CollectionClient  collectionClient  = integrationContext.getCollectionClient();

        String collectionGUID = openMetadataStore.getMetadataElementGUIDByUniqueName(collectionFolderProperties.getQualifiedName(), OpenMetadataProperty.QUALIFIED_NAME.name);

        if (collectionGUID != null)
        {
            collectionClient.updateCollection(collectionGUID, collectionClient.getUpdateOptions(true), collectionFolderProperties);
        }
        else
        {
            NewElementOptions newElementOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(dataSharingHubGUID);
            newElementOptions.setParentGUID(dataDictionaryGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

            collectionGUID = collectionClient.createCollection(newElementOptions, null, collectionFolderProperties, null);
        }

        return collectionGUID;
    }

    /**
     * Loads the existing content of the data dictionary into the supplied maps to use when refreshing each data store.
     *
     * @param member         retrieved member of the data dictionary
     * @param dataFields     map of data fields
     * @param dataStructures map of data structures
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void retrieveDataDictionaryElements(RelatedMetadataElementSummary member,
                                                Map<String, String>           dataFields,
                                                Map<String, Set<String>>      dataStructures) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        if ((member != null) && (member.getRelatedElement().getProperties() instanceof ReferenceableProperties dataDictionaryElementProperties))
        {
            if (propertyHelper.isTypeOf(member.getRelatedElement().getElementHeader(), OpenMetadataType.DATA_FIELD.typeName))
            {
                dataFields.put(dataDictionaryElementProperties.getQualifiedName(), member.getRelatedElement().getElementHeader().getGUID());
            }
            else if (propertyHelper.isTypeOf(member.getRelatedElement().getElementHeader(), OpenMetadataType.DATA_STRUCTURE.typeName))
            {
                Set<String> dataFieldMembers = new HashSet<>();

                if ((member instanceof RelatedMetadataHierarchySummary hierarchyMember) && (hierarchyMember.getNestedElements() != null))
                {
                    for (RelatedMetadataElementSummary nestedDataField : hierarchyMember.getNestedElements())
                    {
                        if ((nestedDataField != null) && (propertyHelper.isTypeOf(nestedDataField.getRelatedElement().getElementHeader(), OpenMetadataType.DATA_FIELD.typeName)))
                        {
                            dataFieldMembers.add(nestedDataField.getRelatedElement().getElementHeader().getGUID());
                        }
                    }
                }

                dataStructures.put(dataDictionaryElementProperties.getQualifiedName(), dataFieldMembers);
            }
            else if (propertyHelper.isTypeOf(member.getRelatedElement().getElementHeader(), OpenMetadataType.COLLECTION_FOLDER.typeName))
            {
                /*
                 * Need to process the subfolders.
                 */
                CollectionClient collectionClient = integrationContext.getCollectionClient(OpenMetadataType.COLLECTION_FOLDER.typeName);

                OpenMetadataRootElement subFolder = collectionClient.getCollectionByGUID(member.getRelatedElement().getElementHeader().getGUID(), collectionClient.getGetOptions());

                if (subFolder != null)
                {
                    if (subFolder.getCollectionMembers() != null)
                    {
                        for (RelatedMetadataElementSummary subMember : subFolder.getCollectionMembers())
                        {
                            retrieveDataDictionaryElements(subMember, dataFields, dataStructures);
                        }
                    }
                }
            }
        }
    }


    /**
     * Refreshes the metadata for a specific data store based on the provided parameters. This method
     * interacts with the OpenMetadata framework to retrieve and process the data store's associated schema
     * and other attributes necessary for the refresh operation.
     *
     * @param dataFieldsFolderGUID        Globally unique identifier (GUID) for the folder containing data fields.
     * @param dataStructuresFolderGUID    Globally unique identifier (GUID) for the folder containing data structures.
     * @param dataStoreGUID               Globally unique identifier (GUID) of the data store to be refreshed.
     * @param dataSharingHubQualifiedName        Globally unique name of the data sharing hub associated with the data store.
     * @param dataSharingHubGUID                 Globally unique identifier (GUID) of the data sharing hub associated with the data store.
     * @param dataStructures              Map of identifiers for dataFieldGUIDs in the data structures used during the refresh process.
     */
    private void refreshDataStore(String                   dataFieldsFolderGUID,
                                  String                   dataStructuresFolderGUID,
                                  String                   dataStoreGUID,
                                  String                   dataSharingHubGUID,
                                  String                   dataSharingHubQualifiedName,
                                  Map<String, Set<String>> dataStructures)
    {
        String methodName = "refreshDataStore(" + dataStoreGUID + ")";

        try
        {
            AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.DATA_STORE.typeName);

            OpenMetadataRootElement dataStoreElement = assetClient.getAssetByGUID(dataStoreGUID, assetClient.getGetOptions());

            if (dataStoreElement != null)
            {
                /*
                 * Make sure that the contents of this member are being catalogued, and request a new survey so that
                 * the description of its contents is as complete and as up-to-date as possible.  Both of these run
                 * asynchronously in a governance engine, so their results are picked up on a subsequent refresh.
                 */
                refreshAssetGovernance(dataStoreElement);

                if (propertyHelper.isTypeOf(dataStoreElement.getElementHeader(), OpenMetadataType.FILE_FOLDER.typeName))
                {
                    refreshFileFolder(dataFieldsFolderGUID, dataStructuresFolderGUID, dataStoreElement, dataSharingHubGUID, dataSharingHubQualifiedName, dataStructures);
                }
                else if (propertyHelper.isTypeOf(dataStoreElement.getElementHeader(), OpenMetadataType.CSV_FILE.typeName))
                {
                    refreshCSVFile(dataFieldsFolderGUID, dataStructuresFolderGUID, dataStoreElement, dataSharingHubGUID, dataSharingHubQualifiedName, dataStructures);
                }
                else if (propertyHelper.isTypeOf(dataStoreElement.getElementHeader(), OpenMetadataType.DATABASE.typeName))
                {
                    refreshRelationalDatabase(dataFieldsFolderGUID, dataStructuresFolderGUID, dataStoreElement, dataSharingHubGUID, dataSharingHubQualifiedName, dataStructures);
                }
                else if (propertyHelper.isTypeOf(dataStoreElement.getElementHeader(), OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName))
                {
                    refreshRelationalDatabaseSchema(dataFieldsFolderGUID, dataStructuresFolderGUID, dataStoreElement, dataSharingHubGUID, dataSharingHubQualifiedName, dataStructures);
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LiskovAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                            error.getClass().getName(),
                                                                                            methodName,
                                                                                            error.getMessage()),
                                  error);
        }
    }


    /**
     * Refreshes the metadata for a file folder based on the provided parameters. This method hunts for files
     * containing tabular data sets withing the folder structure.
     *
     * @param dataFieldsFolderGUID        Globally unique identifier (GUID) for the folder containing data fields.
     * @param dataStructuresFolderGUID    Globally unique identifier (GUID) for the folder containing data structures.
     * @param dataStoreElement            The data store to be refreshed.
     * @param dataSharingHubGUID                 Globally unique identifier (GUID) of the data sharing hub associated with the data store.
     * @param dataSharingHubQualifiedName        Globally unique name of the data sharing hub associated with the data store.
     * @param dataStructures              Map of identifiers for dataFieldGUIDs in the data structures used during the refresh process.
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void refreshFileFolder(String                   dataFieldsFolderGUID,
                                   String                   dataStructuresFolderGUID,
                                   OpenMetadataRootElement  dataStoreElement,
                                   String                   dataSharingHubGUID,
                                   String                   dataSharingHubQualifiedName,
                                   Map<String, Set<String>> dataStructures) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        if (dataStoreElement.getProperties() instanceof FileFolderProperties fileFolderProperties)
        {
            ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient(OpenMetadataType.CSV_FILE.typeName);

            int startFrom = 0;
            SearchOptions searchOptions = classificationExplorerClient.getSearchOptions(startFrom, classificationExplorerClient.getMaxPagingSize());

            List<OpenMetadataRootElement> csvFiles = classificationExplorerClient.findRootElements(fileFolderProperties.getPathName(), searchOptions);

            while (csvFiles != null)
            {
                for (OpenMetadataRootElement csvFile : csvFiles)
                {
                    /*
                     * These files are revealed by the cataloguing of the folder.  Each of them has its own
                     * file type specific survey.
                     */
                    refreshAssetGovernance(csvFile);

                    refreshCSVFile(dataFieldsFolderGUID, dataStructuresFolderGUID, csvFile, dataSharingHubGUID, dataSharingHubQualifiedName, dataStructures);
                }

                startFrom += classificationExplorerClient.getMaxPagingSize();
                searchOptions = classificationExplorerClient.getSearchOptions(startFrom, classificationExplorerClient.getMaxPagingSize());
                csvFiles = classificationExplorerClient.findRootElements(fileFolderProperties.getPathName(), searchOptions);
            }
        }
    }


    /**
     * Refreshes the metadata for a CSV File based on the provided parameters. This method extracts the data structure and
     * data fields in a CSV File asset.
     *
     * @param dataFieldsFolderGUID        Globally unique identifier (GUID) for the folder containing data fields.
     * @param dataStructuresFolderGUID    Globally unique identifier (GUID) for the folder containing data structures.
     * @param dataStoreElement            The data store to be refreshed.
     * @param dataSharingHubGUID          Globally unique identifier (GUID) of the data sharing hub associated with the data store.
     * @param dataSharingHubQualifiedName        Globally unique name of the data sharing hub associated with the data store.
     * @param dataStructures              Map of identifiers for dataFieldGUIDs in the data structures used during the refresh process.
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void refreshCSVFile(String                   dataFieldsFolderGUID,
                                String                   dataStructuresFolderGUID,
                                OpenMetadataRootElement  dataStoreElement,
                                String                   dataSharingHubGUID,
                                String                   dataSharingHubQualifiedName,
                                Map<String, Set<String>> dataStructures) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        String methodName = "refreshCSVFile(" + dataStoreElement.getElementHeader().getGUID() + ")";

        if (dataStoreElement.getProperties() instanceof CSVFileProperties csvFileProperties)
        {
            auditLog.logMessage(methodName,
                                LiskovAuditCode.REFRESHING_CSV_FILE.getMessageDefinition(connectorName,
                                                                                         csvFileProperties.getFileName(),
                                                                                         dataStoreElement.getElementHeader().getGUID()));

            if ((dataStoreElement.getSchemaType() instanceof RelatedMetadataHierarchySummary schemaType) && (schemaType.getNestedElements() != null))
            {
                refreshDataStructure(dataFieldsFolderGUID,
                                     dataStructuresFolderGUID,
                                     dataStoreElement.getSchemaType().getRelatedElement().getElementHeader().getGUID(),
                                     csvFileProperties.getFileName(),
                                     schemaType.getNestedElements(),
                                     dataSharingHubGUID,
                                     dataSharingHubQualifiedName,
                                     dataStructures);
            }
        }
    }


    /**
     * Refreshes the metadata for a Relational Database based on the provided parameters. This method extracts the data structure and
     * data fields for each database schema attached to the data store.
     *
     * @param dataFieldsFolderGUID     Globally unique identifier (GUID) for the folder containing data fields.
     * @param dataStructuresFolderGUID Globally unique identifier (GUID) for the folder containing data structures.
     * @param dataStoreElement         The data store to be refreshed.
     * @param dataSharingHubGUID       Globally unique identifier (GUID) of the data sharing hub associated with the data store.
     * @param dataSharingHubQualifiedName     Globally unique name of the data sharing hub associated with the data store.
     * @param dataStructures           Map of identifiers for dataFieldGUIDs in the data structures used during the refresh process.
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void refreshRelationalDatabase(String                   dataFieldsFolderGUID,
                                           String                   dataStructuresFolderGUID,
                                           OpenMetadataRootElement  dataStoreElement,
                                           String                   dataSharingHubGUID,
                                           String                   dataSharingHubQualifiedName,
                                           Map<String, Set<String>> dataStructures) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        if (dataStoreElement.getSupportedDataSets() != null)
        {
            for (RelatedMetadataElementSummary dataSet : dataStoreElement.getSupportedDataSets())
            {
                if (propertyHelper.isTypeOf(dataSet.getRelatedElement().getElementHeader(), OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName))
                {
                    ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient(OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName);

                    OpenMetadataRootElement databaseSchema = classificationExplorerClient.getRootElementByGUID(dataSet.getRelatedElement().getElementHeader().getGUID(), classificationExplorerClient.getGetOptions());

                    if (databaseSchema != null)
                    {
                        refreshAssetGovernance(databaseSchema);

                        refreshRelationalDatabaseSchema(dataFieldsFolderGUID, dataStructuresFolderGUID, databaseSchema, dataSharingHubGUID, dataSharingHubQualifiedName, dataStructures);
                    }
                }
            }
        }
    }


    /**
     * Refreshes the metadata for a Relational Database Schema based on the provided parameters. This method extracts the data structure and
     * data fields for each database table attached to the data store.
     *
     * @param dataFieldsFolderGUID     Globally unique identifier (GUID) for the folder containing data fields.
     * @param dataStructuresFolderGUID Globally unique identifier (GUID) for the folder containing data structures.
     * @param dataStoreElement         The data store to be refreshed.
     * @param dataSharingHubGUID              Globally unique identifier (GUID) of the data sharing hub associated with the data store.
     * @param dataSharingHubQualifiedName     Globally unique name of the data sharing hub associated with the data store.
     * @param dataStructures              Map of identifiers for dataFieldGUIDs in the data structures used during the refresh process.
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void refreshRelationalDatabaseSchema(String                   dataFieldsFolderGUID,
                                                 String                   dataStructuresFolderGUID,
                                                 OpenMetadataRootElement  dataStoreElement,
                                                 String                   dataSharingHubGUID,
                                                 String                   dataSharingHubQualifiedName,
                                                 Map<String, Set<String>> dataStructures) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        if ((dataStoreElement.getProperties() instanceof DeployedDatabaseSchemaProperties databaseSchemaProperties) &&
                (dataStoreElement.getSchemaType() instanceof RelatedMetadataHierarchySummary schemaType) &&
                (schemaType.getNestedElements() != null))
        {
            refreshDataStructure(dataFieldsFolderGUID,
                                 dataStructuresFolderGUID,
                                 dataStoreElement.getSchemaType().getRelatedElement().getElementHeader().getGUID(),
                                 databaseSchemaProperties.getDisplayName(),
                                 schemaType.getNestedElements(),
                                 dataSharingHubGUID,
                                 dataSharingHubQualifiedName,
                                 dataStructures);
        }
    }


    /**
     * Refreshes the metadata for a Relational Database Schema based on the provided parameters. This method extracts the data structure and
     * data fields for each database table attached to the data store.
     *
     * @param dataFieldsFolderGUID     Globally unique identifier (GUID) for the folder containing data fields.
     * @param dataStructuresFolderGUID Globally unique identifier (GUID) for the folder containing data structures.
     * @param schemaGUID               unique identifier of the schema being refreshed.
     * @param schemaName               display name of the schema being refreshed.
     * @param schemaAttributes         list of schema attributes.
     * @param dataSharingHubGUID              Globally unique identifier (GUID) of the data sharing hub associated with the data store.
     * @param dataSharingHubQualifiedName     Globally unique name of the data sharing hub associated with the data store.
     * @param dataStructures           Map of identifiers for dataFieldGUIDs in the data structures used during the refresh process.
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void refreshDataStructure(String                              dataFieldsFolderGUID,
                                      String                              dataStructuresFolderGUID,
                                      String                              schemaGUID,
                                      String                              schemaName,
                                      List<RelatedMetadataElementSummary> schemaAttributes,
                                      String                              dataSharingHubGUID,
                                      String                              dataSharingHubQualifiedName,
                                      Map<String, Set<String>>            dataStructures) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        Set<String> dataFieldGUIDs = new HashSet<>();

        /*
         * Process all the data fields.
         */
        for (RelatedMetadataElementSummary schemaAttribute : schemaAttributes)
        {
            if ((schemaAttribute != null) && (schemaAttribute.getRelatedElement().getProperties() instanceof SchemaAttributeProperties schemaAttributeProperties))
            {
                String dataFieldName = normalizeName(schemaAttributeProperties.getDisplayName());

                DataFieldProperties dataFieldProperties = new DataFieldProperties();

                dataFieldProperties.setQualifiedName(OpenMetadataType.DATA_FIELD.typeName + "::" + dataSharingHubGUID + "::" + dataFieldName);
                dataFieldProperties.setDisplayName(dataFieldName);
                dataFieldProperties.setDescription(schemaAttributeProperties.getDescription());

                if ((schemaAttribute.getRelatedElement().getElementHeader().getSchemaType() != null) && (schemaAttribute.getRelatedElement().getElementHeader().getSchemaType().getClassificationProperties() instanceof TypeEmbeddedAttributeProperties typeEmbeddedAttributeProperties))
                {
                    dataFieldProperties.setDataType(typeEmbeddedAttributeProperties.getDataType());
                }
                else
                {
                    dataFieldProperties.setDataType(DataType.STRING.getDisplayName());
                }

                dataFieldProperties.setLength(schemaAttributeProperties.getLength());
                dataFieldProperties.setPrecision(schemaAttributeProperties.getPrecision());

                dataFieldGUIDs.add(getDataField(dataSharingHubGUID,
                                                dataSharingHubQualifiedName,
                                                schemaAttribute.getRelatedElement().getElementHeader().getGUID(),
                                                dataFieldsFolderGUID,
                                                dataFieldProperties));
            }
        }

        String dataStructureGUID = this.getMatchingDataStructure(dataFieldGUIDs, dataStructures);

        if (dataStructureGUID == null)
        {
            String dataStructureName = normalizeName(schemaName);

            DataStructureProperties dataStructureProperties = new DataStructureProperties();

            dataStructureProperties.setQualifiedName(OpenMetadataType.DATA_STRUCTURE.typeName + "::" + dataSharingHubGUID + "::" + dataStructureName);
            dataStructureProperties.setDisplayName(normalizeName(schemaName));
            dataStructureProperties.setDescription("Data structure for " + schemaName + " in the data sharing hub " + dataSharingHubQualifiedName);

            dataStructureGUID = getDataStructure(dataSharingHubGUID,
                                                 dataSharingHubQualifiedName,
                                                 schemaGUID,
                                                 dataStructuresFolderGUID,
                                                 dataStructureProperties);

            dataStructures.put(dataStructureGUID, dataFieldGUIDs);

            DataStructureClient dataStructureClient = integrationContext.getDataStructureClient();

            for (String dataFieldGUID : dataFieldGUIDs)
            {
                dataStructureClient.linkMemberDataField(dataStructureGUID, dataFieldGUID, dataStructureClient.getMakeAnchorOptions(false), null);
            }
        }
    }


    /**
     * Return the GUID of the matching data structure. This method is used to ensure that the data structure is only created once.
     *
     * @param dataFields     data fields
     * @param dataStructures data structures
     * @return data structure GUID
     */
    private String getMatchingDataStructure(Set<String> dataFields,
                                            Map<String, Set<String>> dataStructures)
    {
        for (String dataStructureGUID : dataStructures.keySet())
        {
            if (dataFields.equals(dataStructures.get(dataStructureGUID)))
            {
                return dataStructureGUID;
            }
        }

        return null;
    }

    /**
     * Normalizes the provided name by converting it to canonical case.
     *
     * @param name the name to be normalized
     * @return the normalized name
     */
    private String normalizeName(String name)
    {
        //return name.replaceAll("[^a-zA-Z0-9]", "_");
        /*
         * Conversion is step-by=step to make it easier to check in the debugger.
         */
        String uncameledName = integrationContext.fromCamelToCanonicalCase(name);
        String unkebabedName = integrationContext.fromKebabToCanonicalCase(uncameledName);
        return integrationContext.fromCanonicalToSnakeCase(unkebabedName);
    }


    /**
     * Retrieves or creates a new DataField based on the provided properties within a specified DataSharingHub and its collection.
     *
     * @param dataSharingHubGUID Unique identifier of the DataSharingHub where the DataField resides or will be created.
     * @param dataSharingHubQualifiedName Qualified name of the DataSharingHub where the DataField resides or will be created.
     * @param schemaGUID unique identifier from the data store's schema.
     * @param dataFieldsCollectionGUID Unique identifier of the DataFields collection where the DataField resides or will be created.
     * @param dataFieldProperties The properties of the DataField to retrieve or create.
     *
     * @return The unique identifier (GUID) of the retrieved or newly created DataField.
     *
     * @throws UserNotAuthorizedException If the user is not authorized to perform the operation.
     * @throws InvalidParameterException If any of the provided parameters are invalid.
     * @throws PropertyServerException If there is an error with the metadata server while performing the operation.
     */
    private String getDataField(String              dataSharingHubGUID,
                                String              dataSharingHubQualifiedName,
                                String              schemaGUID,
                                String              dataFieldsCollectionGUID,
                                DataFieldProperties dataFieldProperties) throws UserNotAuthorizedException,
                                                                                InvalidParameterException,
                                                                                PropertyServerException
    {
        String methodName = "getDataField(" + dataFieldProperties.getQualifiedName() + ")";

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();
        DataFieldClient dataFieldClient = integrationContext.getDataFieldClient();

        String dataFieldGUID = openMetadataStore.getMetadataElementGUIDByUniqueName(dataFieldProperties.getQualifiedName(), OpenMetadataProperty.QUALIFIED_NAME.name);

        if (dataFieldGUID != null)
        {
            dataFieldClient.updateDataField(dataFieldGUID, dataFieldClient.getUpdateOptions(true), dataFieldProperties);
        }
        else
        {
            NewElementOptions newElementOptions = new NewElementOptions(dataFieldClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(dataSharingHubGUID);
            newElementOptions.setParentGUID(dataFieldsCollectionGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

            dataFieldGUID = dataFieldClient.createDataField(newElementOptions, null, dataFieldProperties, null);

            auditLog.logMessage(methodName,
                                LiskovAuditCode.NEW_DATA_FIELD.getMessageDefinition(connectorName,
                                                                                    dataFieldProperties.getDisplayName(),
                                                                                    dataFieldGUID,
                                                                                    dataSharingHubQualifiedName,
                                                                                    dataSharingHubGUID));
        }

        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();

        governanceDefinitionClient.linkDesignToImplementation(dataFieldGUID, schemaGUID, governanceDefinitionClient.getMakeAnchorOptions(false), null);

        return dataFieldGUID;
    }


    /**
     * Retrieves or creates a new DataStructure based on the provided properties within a specified DataSharingHub and its collection.
     *
     * @param dataSharingHubGUID Unique identifier of the DataSharingHub where the DataStructure resides or will be created.
     * @param dataSharingHubQualifiedName Qualified name of the DataSharingHub where the DataStructure resides or will be created.
     * @param schemaGUID unique identifier from the data store's schema.
     * @param dataStructuresCollectionGUID Unique identifier of the DataStructures collection where the DataStructure resides or will be created.
     * @param dataStructuresProperties The properties of the DataStructure to retrieve or create.
     *
     * @return The unique identifier (GUID) of the retrieved or newly created DataStructure.
     *
     * @throws UserNotAuthorizedException If the user is not authorized to perform the operation.
     * @throws InvalidParameterException If any of the provided parameters are invalid.
     * @throws PropertyServerException If there is an error with the metadata server while performing the operation.
     */
    private String getDataStructure(String                  dataSharingHubGUID,
                                    String                  dataSharingHubQualifiedName,
                                    String                  schemaGUID,
                                    String                  dataStructuresCollectionGUID,
                                    DataStructureProperties dataStructuresProperties) throws UserNotAuthorizedException,
                                                                                             InvalidParameterException,
                                                                                             PropertyServerException
    {
        String methodName = "getDataStructure(" + dataStructuresProperties.getQualifiedName() + ")";

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();
        DataStructureClient dataStructureClient = integrationContext.getDataStructureClient();

        String dataStructureGUID = openMetadataStore.getMetadataElementGUIDByUniqueName(dataStructuresProperties.getQualifiedName(), OpenMetadataProperty.QUALIFIED_NAME.name);

        if (dataStructureGUID != null)
        {
            dataStructureClient.updateDataStructure(dataStructureGUID, dataStructureClient.getUpdateOptions(true), dataStructuresProperties);
        }
        else
        {
            NewElementOptions newElementOptions = new NewElementOptions(dataStructureClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(dataSharingHubGUID);
            newElementOptions.setParentGUID(dataStructuresCollectionGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

            dataStructureGUID = dataStructureClient.createDataStructure(newElementOptions, null, dataStructuresProperties, null);

            auditLog.logMessage(methodName,
                                LiskovAuditCode.NEW_DATA_STRUCTURE.getMessageDefinition(connectorName,
                                                                                        dataStructuresProperties.getDisplayName(),
                                                                                        dataStructureGUID,
                                                                                        dataSharingHubQualifiedName,
                                                                                        dataSharingHubGUID));
        }

        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();

        governanceDefinitionClient.linkDesignToImplementation(dataStructureGUID, schemaGUID, governanceDefinitionClient.getMakeAnchorOptions(false), null);

        return dataStructureGUID;
    }


    /* ==============================================================================
     * Methods that ensure the members of the data sharing hub are catalogued and surveyed.
     * Egeria's content packs define governance action types for cataloguing and surveying each
     * type of technology.  They are linked to the technology type (deployedImplementationType) that
     * they support with a ResourceList relationship, qualified by the resource use.
     */


    /**
     * Ensure that the contents of the supplied element are being catalogued, and request a new survey so that
     * the description of its contents is as complete and as up-to-date as possible.  Cataloguing is only requested
     * if it is not already set up.  Both types of request run asynchronously in a governance engine, so their
     * results are picked up on a subsequent refresh.  Each element is only considered once per refresh.
     *
     * @param assetElement element to work with
     */
    private void refreshAssetGovernance(OpenMetadataRootElement assetElement)
    {
        final String methodName = "refreshAssetGovernance";

        try
        {
            if ((assetElement == null) || (! (assetElement.getProperties() instanceof AssetProperties assetProperties)))
            {
                return;
            }

            if (! governedElements.add(assetElement.getElementHeader().getGUID()))
            {
                /*
                 * This element has already been processed during this refresh.
                 */
                return;
            }

            OpenMetadataRootElement technologyType = getTechnologyType(assetProperties.getDeployedImplementationType());

            if (technologyType == null)
            {
                auditLog.logMessage(methodName,
                                    LiskovAuditCode.NO_TECHNOLOGY_TYPE.getMessageDefinition(connectorName,
                                                                                            assetProperties.getDeployedImplementationType(),
                                                                                            assetElement.getElementHeader().getType().getTypeName(),
                                                                                            assetProperties.getDisplayName(),
                                                                                            assetElement.getElementHeader().getGUID()));
                return;
            }

            if (technologyType.getResourceList() != null)
            {
                /*
                 * Requests made on an earlier refresh may not have completed yet.  They must not be repeated.
                 */
                Set<String> runningActionTypeGUIDs = getRunningActionTypeGUIDs(assetElement.getElementHeader().getGUID());

                for (RelatedMetadataElementSummary resource : technologyType.getResourceList())
                {
                    if ((resource != null) &&
                            (propertyHelper.isTypeOf(resource.getRelatedElement().getElementHeader(), OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName)) &&
                            (resource.getRelationshipProperties() instanceof ResourceListProperties resourceListProperties) &&
                            (! runningActionTypeGUIDs.contains(resource.getRelatedElement().getElementHeader().getGUID())))
                    {
                        if (ResourceUse.CATALOG_RESOURCE.getResourceUse().equals(resourceListProperties.getResourceUse()))
                        {
                            enableCataloguing(assetElement, assetProperties, resource.getRelatedElement());
                        }
                        else if (ResourceUse.SURVEY_RESOURCE.getResourceUse().equals(resourceListProperties.getResourceUse()))
                        {
                            requestSurvey(assetElement, assetProperties, resource.getRelatedElement());
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LiskovAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                            error.getClass().getName(),
                                                                                            methodName,
                                                                                            error.getMessage()),
                                  error);
        }
    }


    /**
     * Retrieve the technology type definition for a particular deployedImplementationType value.  The governance
     * action types that catalog and survey this type of technology are linked to it with a ResourceList relationship.
     *
     * @param deployedImplementationType value from the asset
     * @return technology type definition or null
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private OpenMetadataRootElement getTechnologyType(String deployedImplementationType) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        if (deployedImplementationType == null)
        {
            return null;
        }

        if (technologyTypeCache.containsKey(deployedImplementationType))
        {
            return technologyTypeCache.get(deployedImplementationType);
        }

        ValidValueDefinitionClient validValueDefinitionClient = integrationContext.getValidValueDefinitionClient(OpenMetadataType.TECHNOLOGY_TYPE.typeName);

        QueryOptions queryOptions = validValueDefinitionClient.getQueryOptions(OpenMetadataType.TECHNOLOGY_TYPE.typeName,
                                                                              0,
                                                                              validValueDefinitionClient.getMaxPagingSize());

        List<OpenMetadataRootElement> technologyTypes = validValueDefinitionClient.getValidValueDefinitionsByName(deployedImplementationType, queryOptions);

        OpenMetadataRootElement technologyType = null;

        if (technologyTypes != null)
        {
            /*
             * The search matches on a number of properties.  Only an exact match on the preferred value describes
             * the technology type used in the asset's deployedImplementationType property.
             */
            for (OpenMetadataRootElement matchingTechnologyType : technologyTypes)
            {
                if ((matchingTechnologyType != null) &&
                        (matchingTechnologyType.getProperties() instanceof ValidValueDefinitionProperties validValueDefinitionProperties) &&
                        (deployedImplementationType.equals(validValueDefinitionProperties.getPreferredValue())))
                {
                    technologyType = matchingTechnologyType;
                    break;
                }
            }
        }

        technologyTypeCache.put(deployedImplementationType, technologyType);

        return technologyType;
    }


    /**
     * Retrieve the full definition of a governance action type - including its predefined action targets.
     *
     * @param governanceActionTypeGUID unique identifier of the governance action type
     * @return governance action type or null
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private OpenMetadataRootElement getGovernanceActionType(String governanceActionTypeGUID) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        if (actionTypeCache.containsKey(governanceActionTypeGUID))
        {
            return actionTypeCache.get(governanceActionTypeGUID);
        }

        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient(OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName);

        OpenMetadataRootElement governanceActionType = classificationExplorerClient.getRootElementByGUID(governanceActionTypeGUID,
                                                                                                        classificationExplorerClient.getGetOptions());

        actionTypeCache.put(governanceActionTypeGUID, governanceActionType);

        return governanceActionType;
    }


    /**
     * Request that the contents of the supplied element are catalogued, if this is not already happening.  The
     * cataloguing governance action type links the element to an integration connector that maintains the
     * description of its contents.  Therefore, cataloguing is already enabled if the element is a catalog target
     * of that integration connector.
     *
     * @param assetElement      element to catalog
     * @param assetProperties   properties of the element to catalog
     * @param actionTypeSummary summary of the cataloguing governance action type
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void enableCataloguing(OpenMetadataRootElement assetElement,
                                   AssetProperties         assetProperties,
                                   MetadataElementSummary  actionTypeSummary) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "enableCataloguing";

        OpenMetadataRootElement governanceActionType = getGovernanceActionType(actionTypeSummary.getElementHeader().getGUID());

        if ((governanceActionType == null) || (! (governanceActionType.getProperties() instanceof GovernanceActionTypeProperties governanceActionTypeProperties)))
        {
            return;
        }

        String assetGUID = assetElement.getElementHeader().getGUID();

        /*
         * The cataloguing governance action types have the integration connector that will do the cataloguing
         * set up as a predefined action target.  If the element is already one of its catalog targets then
         * cataloguing is already enabled.
         */
        if (governanceActionType.getPredefinedTargetForAction() != null)
        {
            for (RelatedMetadataElementSummary predefinedTarget : governanceActionType.getPredefinedTargetForAction())
            {
                if ((predefinedTarget != null) &&
                        (predefinedTarget.getRelationshipProperties() instanceof TargetForGovernanceActionProperties targetForGovernanceActionProperties) &&
                        (ActionTarget.INTEGRATION_CONNECTOR.getName().equals(targetForGovernanceActionProperties.getActionTargetName())) &&
                        (getCatalogTargetGUIDs(predefinedTarget.getRelatedElement().getElementHeader().getGUID()).contains(assetGUID)))
                {
                    return;
                }
            }
        }

        String engineActionGUID = initiateGovernanceActionType(governanceActionTypeProperties.getQualifiedName(), assetGUID);

        auditLog.logMessage(methodName,
                            LiskovAuditCode.ENABLING_CATALOGUING.getMessageDefinition(connectorName,
                                                                                      engineActionGUID,
                                                                                      assetElement.getElementHeader().getType().getTypeName(),
                                                                                      assetProperties.getDisplayName(),
                                                                                      assetGUID,
                                                                                      governanceActionTypeProperties.getQualifiedName()));
    }


    /**
     * Request a new survey of the supplied element so that the description of its contents is as complete and as
     * up-to-date as possible.  A new survey is requested on each refresh unless the survey requested on a previous
     * refresh is still running.
     *
     * @param assetElement      element to survey
     * @param assetProperties   properties of the element to survey
     * @param actionTypeSummary summary of the survey governance action type
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void requestSurvey(OpenMetadataRootElement assetElement,
                               AssetProperties         assetProperties,
                               MetadataElementSummary  actionTypeSummary) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "requestSurvey";

        OpenMetadataRootElement governanceActionType = getGovernanceActionType(actionTypeSummary.getElementHeader().getGUID());

        if ((governanceActionType == null) || (! (governanceActionType.getProperties() instanceof GovernanceActionTypeProperties governanceActionTypeProperties)))
        {
            return;
        }

        if (isExcludedSurvey(governanceActionTypeProperties.getQualifiedName()))
        {
            return;
        }

        String assetGUID = assetElement.getElementHeader().getGUID();

        String engineActionGUID = initiateGovernanceActionType(governanceActionTypeProperties.getQualifiedName(), assetGUID);

        auditLog.logMessage(methodName,
                            LiskovAuditCode.STARTING_SURVEY.getMessageDefinition(connectorName,
                                                                                 engineActionGUID,
                                                                                 assetElement.getElementHeader().getType().getTypeName(),
                                                                                 assetProperties.getDisplayName(),
                                                                                 assetGUID,
                                                                                 governanceActionTypeProperties.getQualifiedName()));
    }


    /**
     * Start an engine action from the requested governance action type, passing the element as the "newAsset"
     * action target.  The data sharing hub that Liskov is managing is recorded as the source of the request.
     *
     * @param governanceActionTypeQualifiedName unique name of the governance action type to run
     * @param assetGUID unique identifier of the element to pass as the action target
     * @return unique identifier of the new engine action
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private String initiateGovernanceActionType(String governanceActionTypeQualifiedName,
                                                String assetGUID) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        NewActionTarget newActionTarget = new NewActionTarget();

        newActionTarget.setActionTargetName(ActionTarget.NEW_ASSET.getName());
        newActionTarget.setActionTargetGUID(assetGUID);

        return integrationContext.getStewardshipAction().initiateGovernanceActionType(governanceActionTypeQualifiedName,
                                                                                      Collections.singletonList(this.getCatalogTargetElement().getElementHeader().getGUID()),
                                                                                      null,
                                                                                      Collections.singletonList(newActionTarget),
                                                                                      null,
                                                                                      null,
                                                                                      connectorName,
                                                                                      null,
                                                                                      null);
    }


    /**
     * Return the surveys that this connector has been configured not to run.  The values are supplied as a
     * comma-separated list of either the survey's request type (for example, survey-folder) or the qualified name
     * of its governance action type (for example, FileSurvey::survey-folder).  The default is to exclude nothing.
     *
     * @return set of configured values - empty if all registered surveys are to run
     */
    private Set<String> getExcludedSurveys()
    {
        Set<String> surveys = new HashSet<>();

        List<String> configuredSurveys = super.getArrayConfigurationProperty(LiskovConfigurationProperty.EXCLUDED_SURVEY_REQUEST_TYPES.getName(),
                                                                             this.getConfigurationProperties(),
                                                                             (List<String>) null);

        if (configuredSurveys != null)
        {
            for (String configuredSurvey : configuredSurveys)
            {
                if ((configuredSurvey != null) && (! configuredSurvey.trim().isEmpty()))
                {
                    surveys.add(configuredSurvey.trim());
                }
            }
        }

        return surveys;
    }


    /**
     * Return whether the requested survey is one that this connector has been configured not to run.  A survey's
     * governance action type is qualified by the name of the survey engine that runs it - for example,
     * FileSurvey::survey-folder - so the configured value may be either the whole qualified name or just the
     * request type on the end.
     *
     * @param governanceActionTypeQualifiedName unique name of the survey's governance action type
     * @return boolean flag
     */
    private boolean isExcludedSurvey(String governanceActionTypeQualifiedName)
    {
        if ((excludedSurveys.isEmpty()) || (governanceActionTypeQualifiedName == null))
        {
            return false;
        }

        if (excludedSurveys.contains(governanceActionTypeQualifiedName))
        {
            return true;
        }

        int requestTypeStart = governanceActionTypeQualifiedName.lastIndexOf("::");

        return (requestTypeStart >= 0) && (excludedSurveys.contains(governanceActionTypeQualifiedName.substring(requestTypeStart + 2)));
    }


    /**
     * Return the unique identifiers of the elements that are catalog targets for the requested integration connector.
     *
     * @param integrationConnectorGUID unique identifier of the integration connector
     * @return set of unique identifiers - may be empty but not null
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private Set<String> getCatalogTargetGUIDs(String integrationConnectorGUID) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        Set<String> catalogTargetGUIDs = catalogTargetCache.get(integrationConnectorGUID);

        if (catalogTargetGUIDs != null)
        {
            return catalogTargetGUIDs;
        }

        catalogTargetGUIDs = new HashSet<>();

        AssetClient assetClient = integrationContext.getAssetClient();
        int         startFrom   = 0;

        List<OpenMetadataRootElement> catalogTargets = assetClient.getCatalogTargets(integrationConnectorGUID,
                                                                                     assetClient.getQueryOptions(startFrom, assetClient.getMaxPagingSize()));

        while ((catalogTargets != null) && (! catalogTargets.isEmpty()))
        {
            for (OpenMetadataRootElement catalogTarget : catalogTargets)
            {
                if (catalogTarget != null)
                {
                    catalogTargetGUIDs.add(catalogTarget.getElementHeader().getGUID());
                }
            }

            startFrom      = startFrom + assetClient.getMaxPagingSize();
            catalogTargets = assetClient.getCatalogTargets(integrationConnectorGUID,
                                                           assetClient.getQueryOptions(startFrom, assetClient.getMaxPagingSize()));
        }

        catalogTargetCache.put(integrationConnectorGUID, catalogTargetGUIDs);

        return catalogTargetGUIDs;
    }


    /**
     * Return the unique identifiers of the governance action types that already have an engine action running
     * (or waiting to run) against the requested element.  These are the requests that Liskov made on an earlier
     * refresh that have not completed yet - they must not be requested again.
     *
     * @param assetGUID unique identifier of the element that is the action target
     * @return set of governance action type unique identifiers - may be empty but not null
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private Set<String> getRunningActionTypeGUIDs(String assetGUID) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        Set<String> runningActionTypeGUIDs = new HashSet<>();

        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.ENGINE_ACTION.typeName);
        int         startFrom   = 0;

        List<OpenMetadataRootElement> engineActions = assetClient.getActionsForActionTarget(assetGUID,
                                                                                            liveActivityStatuses,
                                                                                            assetClient.getQueryOptions(startFrom, assetClient.getMaxPagingSize()));

        while ((engineActions != null) && (! engineActions.isEmpty()))
        {
            for (OpenMetadataRootElement engineAction : engineActions)
            {
                if ((engineAction != null) &&
                        (engineAction.getProperties() instanceof EngineActionProperties engineActionProperties) &&
                        (engineActionProperties.getGovernanceActionTypeGUID() != null))
                {
                    runningActionTypeGUIDs.add(engineActionProperties.getGovernanceActionTypeGUID());
                }
            }

            startFrom     = startFrom + assetClient.getMaxPagingSize();
            engineActions = assetClient.getActionsForActionTarget(assetGUID,
                                                                  liveActivityStatuses,
                                                                  assetClient.getQueryOptions(startFrom, assetClient.getMaxPagingSize()));
        }

        return runningActionTypeGUIDs;
    }
}
