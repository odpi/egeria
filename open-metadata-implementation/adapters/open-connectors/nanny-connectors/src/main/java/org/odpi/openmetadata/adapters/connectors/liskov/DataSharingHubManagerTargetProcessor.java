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
import org.odpi.openmetadata.frameworks.openmetadata.enums.CapabilityAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataHierarchySummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DeployedDatabaseSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FileFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.EngineActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.SurveyReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DataSharingHubProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDuplicateLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionExecutorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.TargetForGovernanceActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.TypeEmbeddedAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.StatusIdentifier;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Maintains the data dictionary for a single data sharing hub.  On each refresh it works through the data stores
 * that are members of the hub and, for each one, ensures that the contents of the member are being catalogued,
 * requests a survey of it when one is due, and then abstracts the data fields and data structures found in its
 * schema into the hub's data dictionary.  The cataloguing and survey requests are made by starting the governance action types
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
     * The survey reports retrieved are filtered by the survey that produced them.  This is the page size used to
     * work back through an asset's reports, most recent first, until enough of the right survey are found.
     */
    private static final int surveyReportPageSize = 20;

    /*
     * The results of comparing two survey reports.  The key is the GUID of the newer report followed by the GUID
     * of the older one, and the value is true if the newer report found a change.  Survey reports do not change
     * once the survey is complete, so a result holds for the life of this processor, and each pair of reports is
     * only compared once however many refreshes ask about it.
     */
    private final Map<String, Boolean> surveyReportComparisons = new HashMap<>();

    /*
     * The number of different annotation types reported by each completed survey report, keyed by the report's
     * GUID.  Like the comparisons above, these do not change once the survey is complete.
     */
    private final Map<String, Integer> surveyReportAnnotationTypeCounts = new HashMap<>();

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

    /*
     * Decides when each member is next surveyed - see SurveySchedule.  It is rebuilt on each refresh from the
     * minimumSurveyIntervalDays and maximumSurveyIntervalDays configuration properties.
     */
    private SurveySchedule surveySchedule = new SurveySchedule(SurveySchedule.DEFAULT_MINIMUM_INTERVAL_DAYS,
                                                               SurveySchedule.DEFAULT_MAXIMUM_INTERVAL_DAYS);

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
        surveySchedule  = new SurveySchedule(this.getSurveyIntervalDays(LiskovConfigurationProperty.MINIMUM_SURVEY_INTERVAL_DAYS.getName(),
                                                                        SurveySchedule.DEFAULT_MINIMUM_INTERVAL_DAYS),
                                             this.getSurveyIntervalDays(LiskovConfigurationProperty.MAXIMUM_SURVEY_INTERVAL_DAYS.getName(),
                                                                        SurveySchedule.DEFAULT_MAXIMUM_INTERVAL_DAYS));

        try
        {
            /*
             * This is a lookup table for data fields and data structures found in the data sharing hub.
             * They are used to match schema attributes to the data fields.
             */
            Map<String, Set<String>> dataStructures = new HashMap<>();

            /*
             * The hub's data dictionary and its members are all that is used from its graph.
             */
            GetOptions hubOptions = integrationContext.getCollectionClient().getGetOptions();

            hubOptions.setGraphQueryDepth(1);
            hubOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.DATA_DESCRIPTION_RELATIONSHIP.typeName,
                                                           OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName));

            OpenMetadataRootElement dataSharingHubElement = this.getCatalogTargetElement(hubOptions);

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

                    logRecord(methodName, LiskovAuditCode.NEW_DATA_DICTIONARY.getMessageDefinition(connectorName,
                                                                                                   dataSharingHubProperties.getDisplayName(),
                                                                                                   dataSharingHubGUID));
                }

                GetOptions dataDictionaryOptions = collectionClient.getGetOptions();

                dataDictionaryOptions.setGraphQueryDepth(0); // Only checking that the data dictionary exists.

                OpenMetadataRootElement dataDictionary = collectionClient.getCollectionByGUID(dataDictionaryGUID, dataDictionaryOptions);

                if (dataDictionary != null)
                {
                    logRecord(methodName, LiskovAuditCode.RETRIEVING_DATA_FIELDS.getMessageDefinition(connectorName,
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

                    seedDataStructures(dataStructureFolderGUID, dataStructures);

                    /*
                     * Process the members of the data sharing hub
                     */
                    if (dataSharingHubElement.getCollectionMembers() != null)
                    {
                        for (RelatedMetadataElementSummary member : dataSharingHubElement.getCollectionMembers())
                        {
                            if ((member != null) && (member.getRelatedElement().getProperties() instanceof DataAssetProperties dataAssetProperties))
                            {
                                logRecord(methodName, LiskovAuditCode.REFRESHING_DATA_HUB_STORE.getMessageDefinition(connectorName,
                                                                                                                     member.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                                                                                     dataAssetProperties.getDisplayName(),
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
            logExceptionRecord(methodName,
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
     * Load the data structures already in the data dictionary, along with the data fields that are members of
     * each one.  Data structures are shared by every schema with the same set of data fields, and seeding the map
     * means that sharing survives from one refresh to the next, whatever order the schemas are processed in.
     *
     * @param dataStructuresFolderGUID unique identifier of the folder containing the data structures
     * @param dataStructures           map of data structure GUID to the GUIDs of its member data fields
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void seedDataStructures(String                   dataStructuresFolderGUID,
                                    Map<String, Set<String>> dataStructures) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        CollectionClient    collectionClient    = integrationContext.getCollectionClient(OpenMetadataType.COLLECTION_FOLDER.typeName);
        DataStructureClient dataStructureClient = integrationContext.getDataStructureClient();

        /*
         * Only the folder's direct members, and the data fields that are members of each data structure, are used.
         */
        GetOptions folderOptions = collectionClient.getGetOptions();

        folderOptions.setGraphQueryDepth(1);
        folderOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName));

        GetOptions dataStructureOptions = dataStructureClient.getGetOptions();

        dataStructureOptions.setGraphQueryDepth(1);
        dataStructureOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName));

        OpenMetadataRootElement dataStructuresFolder = collectionClient.getCollectionByGUID(dataStructuresFolderGUID, folderOptions);

        if ((dataStructuresFolder != null) && (dataStructuresFolder.getCollectionMembers() != null))
        {
            for (RelatedMetadataElementSummary member : dataStructuresFolder.getCollectionMembers())
            {
                if ((member != null) && (propertyHelper.isTypeOf(member.getRelatedElement().getElementHeader(), OpenMetadataType.DATA_STRUCTURE.typeName)))
                {
                    String                  dataStructureGUID = member.getRelatedElement().getElementHeader().getGUID();
                    OpenMetadataRootElement dataStructure     = dataStructureClient.getDataStructureByGUID(dataStructureGUID, dataStructureOptions);
                    Set<String>             dataFieldGUIDs    = new HashSet<>();

                    if ((dataStructure != null) && (dataStructure.getContainsDataFields() != null))
                    {
                        for (RelatedMetadataElementSummary dataField : dataStructure.getContainsDataFields())
                        {
                            if (dataField != null)
                            {
                                dataFieldGUIDs.add(dataField.getRelatedElement().getElementHeader().getGUID());
                            }
                        }
                    }

                    dataStructures.put(dataStructureGUID, dataFieldGUIDs);
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
            AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.DATA_ASSET.typeName);

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
                else if ((propertyHelper.isTypeOf(dataStoreElement.getElementHeader(), OpenMetadataType.CSV_FILE.typeName)) ||
                         (propertyHelper.isTypeOf(dataStoreElement.getElementHeader(), OpenMetadataType.PARQUET_FILE.typeName)))
                {
                    refreshTabularFile(dataFieldsFolderGUID, dataStructuresFolderGUID, dataStoreElement, dataSharingHubGUID, dataSharingHubQualifiedName, dataStructures);
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
            logExceptionRecord(methodName,
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
            final String methodName = "refreshFileFolder";

            /*
             * The folder cataloguer records each file under its canonical path name - that is, with the local
             * mount point replaced by the canonical mount point - so that files catalogued by different runtimes
             * line up.  Folders and files may also have been catalogued by hand with their local path names.
             */
            String folderPathName      = fileFolderProperties.getPathName();
            String localMountPoint     = null;
            String canonicalMountPoint = null;
            String canonicalPathName;

            try
            {
                /*
                 * The mount points are the ones used to catalogue this folder's files, where that is known.
                 */
                MountPoints mountPoints = this.getMountPoints(dataStoreElement.getElementHeader().getGUID());

                localMountPoint     = mountPoints.localMountPoint();
                canonicalMountPoint = mountPoints.canonicalMountPoint();

                canonicalPathName = getCanonicalPathName(folderPathName, localMountPoint, canonicalMountPoint);

                if (canonicalPathName != null)
                {
                    /*
                     * The folder, and every folder and file catalogued beneath it, is given its canonical path
                     * name.  The qualified names and connections are not changed, so each of them is still
                     * known by the same unique name and is still reached through its local address.
                     */
                    correctPathName(dataStoreElement, canonicalPathName);
                    correctPathNamesInFolder(OpenMetadataType.FILE_FOLDER.typeName, folderPathName, canonicalPathName, localMountPoint, canonicalMountPoint);
                    correctPathNamesInFolder(OpenMetadataType.DATA_FILE.typeName, folderPathName, canonicalPathName, localMountPoint, canonicalMountPoint);

                    /*
                     * Giving everything its canonical path name can reveal that the same folder, or the same
                     * file, has been catalogued more than once - by hand under its local name, and by a
                     * cataloguer under its canonical name, for example.
                     */
                    linkDuplicatesInFolder(OpenMetadataType.FILE_FOLDER.typeName, canonicalPathName);
                    linkDuplicatesInFolder(OpenMetadataType.DATA_FILE.typeName, canonicalPathName);
                }
            }
            catch (IOException error)
            {
                /*
                 * The path name can not be resolved on this machine, so it is used as it is.
                 */
                canonicalPathName = folderPathName;

                logExceptionRecord(methodName,
                                   LiskovAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                             error.getClass().getName(),
                                                                                             methodName,
                                                                                             error.getMessage()),
                                   error);
            }

            if (canonicalPathName == null)
            {
                return;
            }

            /*
             * These are the types of file that have a tabular schema, and so can be described by a data structure.
             */
            refreshFilesInFolder(OpenMetadataType.CSV_FILE.typeName, canonicalPathName, dataFieldsFolderGUID, dataStructuresFolderGUID, dataSharingHubGUID, dataSharingHubQualifiedName, dataStructures);
            refreshFilesInFolder(OpenMetadataType.PARQUET_FILE.typeName, canonicalPathName, dataFieldsFolderGUID, dataStructuresFolderGUID, dataSharingHubGUID, dataSharingHubQualifiedName, dataStructures);
        }
    }


    /**
     * Give the folders, or the files, that are catalogued beneath a folder their canonical path names.  They
     * are found by looking for the local forms of the folder's path name, since an element that already has
     * its canonical path name needs no correction.
     *
     * @param typeName                open metadata type name of the elements to look for
     * @param folderPathName          path name of the folder as it was catalogued
     * @param canonicalFolderPathName canonical path name of the folder
     * @param localMountPoint         root part of the path name on this machine
     * @param canonicalMountPoint     value that replaces the local mount point in the catalog
     * @throws IOException                the file system is not able to resolve a path name
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void correctPathNamesInFolder(String typeName,
                                          String folderPathName,
                                          String canonicalFolderPathName,
                                          String localMountPoint,
                                          String canonicalMountPoint) throws IOException,
                                                                             InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        Set<String> localPathNames = new LinkedHashSet<>();

        localPathNames.add(folderPathName.startsWith("file://") ? folderPathName.substring(7) : folderPathName);
        localPathNames.add(getLocalPathName(canonicalFolderPathName, localMountPoint, canonicalMountPoint));
        localPathNames.remove(canonicalFolderPathName);

        /*
         * The elements are collected before any of them is updated so that the updates do not disturb the paging.
         */
        Map<String, OpenMetadataRootElement> elementsToCorrect   = new HashMap<>();
        Map<String, String>                  canonicalPathNames = new HashMap<>();

        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient(typeName);

        for (String localPathName : localPathNames)
        {
            int startFrom = 0;

            List<OpenMetadataRootElement> elements = classificationExplorerClient.findRootElements(localPathName,
                                                                                                   classificationExplorerClient.getSearchOptions(startFrom, classificationExplorerClient.getMaxPagingSize()));

            while (elements != null)
            {
                for (OpenMetadataRootElement element : elements)
                {
                    if ((element != null) && (element.getProperties() instanceof DataStoreProperties dataStoreProperties))
                    {
                        String canonicalPathName = getCanonicalPathName(dataStoreProperties.getPathName(), localMountPoint, canonicalMountPoint);

                        /*
                         * The search matches any property, so check that the element really is in the folder.
                         */
                        if ((canonicalPathName != null) && (canonicalPathName.startsWith(canonicalFolderPathName + "/")))
                        {
                            elementsToCorrect.put(element.getElementHeader().getGUID(), element);
                            canonicalPathNames.put(element.getElementHeader().getGUID(), canonicalPathName);
                        }
                    }
                }

                startFrom += classificationExplorerClient.getMaxPagingSize();

                elements = classificationExplorerClient.findRootElements(localPathName,
                                                                         classificationExplorerClient.getSearchOptions(startFrom, classificationExplorerClient.getMaxPagingSize()));
            }
        }

        for (String elementGUID : elementsToCorrect.keySet())
        {
            correctPathName(elementsToCorrect.get(elementGUID), canonicalPathNames.get(elementGUID));
        }
    }


    /**
     * Look for folders, or files, in a folder (and the folder itself) that have the same canonical path name
     * and link them as potential duplicates.  The link is a PeerDuplicateLink with a status of DISCOVERED,
     * which leaves the decision on whether they really are duplicates to the duplicate manager or a steward.
     * Elements that are already linked, whatever the status of the link, are left alone - so a decision that
     * has been made is not reopened.
     *
     * @param typeName                open metadata type name of the elements to look for
     * @param canonicalFolderPathName canonical path name of the folder
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void linkDuplicatesInFolder(String typeName,
                                        String canonicalFolderPathName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "linkDuplicatesInFolder";

        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient(typeName);

        List<String> elementGUIDs     = new ArrayList<>();
        List<String> elementPathNames = new ArrayList<>();

        int startFrom = 0;

        List<OpenMetadataRootElement> elements = classificationExplorerClient.findRootElements(canonicalFolderPathName,
                                                                                               classificationExplorerClient.getSearchOptions(startFrom, classificationExplorerClient.getMaxPagingSize()));

        while (elements != null)
        {
            for (OpenMetadataRootElement element : elements)
            {
                if ((element != null) && (element.getProperties() instanceof DataStoreProperties dataStoreProperties))
                {
                    String pathName = dataStoreProperties.getPathName();

                    /*
                     * The search matches any property, so check that the element really is the folder, or in it.
                     */
                    if ((pathName != null) &&
                            ((pathName.equals(canonicalFolderPathName)) || (pathName.startsWith(canonicalFolderPathName + "/"))) &&
                            (! elementGUIDs.contains(element.getElementHeader().getGUID())))
                    {
                        elementGUIDs.add(element.getElementHeader().getGUID());
                        elementPathNames.add(pathName);
                    }
                }
            }

            startFrom += classificationExplorerClient.getMaxPagingSize();

            elements = classificationExplorerClient.findRootElements(canonicalFolderPathName,
                                                                     classificationExplorerClient.getSearchOptions(startFrom, classificationExplorerClient.getMaxPagingSize()));
        }

        Map<String, List<String>> duplicateGroups = getDuplicateGroups(elementGUIDs, elementPathNames);

        for (String pathName : duplicateGroups.keySet())
        {
            List<String> duplicateGUIDs = duplicateGroups.get(pathName);

            /*
             * The first element is linked to each of the others, which is enough to make them one cluster.
             */
            String      firstGUID     = duplicateGUIDs.get(0);
            Set<String> existingPeers = getPeerDuplicateGUIDs(firstGUID);

            for (int i = 1; i < duplicateGUIDs.size(); i++)
            {
                String duplicateGUID = duplicateGUIDs.get(i);

                if (existingPeers.contains(duplicateGUID))
                {
                    continue;
                }

                PeerDuplicateLinkProperties peerDuplicateLinkProperties = new PeerDuplicateLinkProperties();

                peerDuplicateLinkProperties.setStatusIdentifier(StatusIdentifier.DISCOVERED.getOrdinal());
                peerDuplicateLinkProperties.setSource(connectorName);
                peerDuplicateLinkProperties.setNotes("Discovered automatically: the two elements are catalogued with the same canonical path name (" + pathName + ").");

                classificationExplorerClient.linkElementsAsPeerDuplicates(firstGUID,
                                                                          duplicateGUID,
                                                                          peerDuplicateLinkProperties,
                                                                          classificationExplorerClient.getMakeAnchorOptions(false));

                logRecord(methodName,
                          LiskovAuditCode.POTENTIAL_DUPLICATES_LINKED.getMessageDefinition(connectorName,
                                                                                           typeName,
                                                                                           firstGUID,
                                                                                           duplicateGUID,
                                                                                           pathName));
            }
        }
    }


    /**
     * Group the elements that have the same path name.
     *
     * @param elementGUIDs unique identifiers of the elements
     * @param pathNames    path name of each of the elements, in the same order
     * @return map of path name to the unique identifiers of the elements that have it, in order of unique
     * identifier, for each path name that more than one element has
     */
    static Map<String, List<String>> getDuplicateGroups(List<String> elementGUIDs,
                                                        List<String> pathNames)
    {
        Map<String, List<String>> groups = new TreeMap<>();

        for (int i = 0; i < elementGUIDs.size(); i++)
        {
            if ((elementGUIDs.get(i) != null) && (pathNames.get(i) != null))
            {
                List<String> group = groups.get(pathNames.get(i));

                if (group == null)
                {
                    group = new ArrayList<>();
                    groups.put(pathNames.get(i), group);
                }

                if (! group.contains(elementGUIDs.get(i)))
                {
                    group.add(elementGUIDs.get(i));
                }
            }
        }

        Map<String, List<String>> duplicateGroups = new TreeMap<>();

        for (String pathName : groups.keySet())
        {
            List<String> group = groups.get(pathName);

            if (group.size() > 1)
            {
                Collections.sort(group);
                duplicateGroups.put(pathName, group);
            }
        }

        return duplicateGroups;
    }


    /**
     * Return the elements that an element is already linked to as a peer duplicate, whatever the status of the link.
     *
     * @param elementGUID unique identifier of the element
     * @return unique identifiers of its peer duplicates
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private Set<String> getPeerDuplicateGUIDs(String elementGUID) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        Set<String> peerGUIDs = new HashSet<>();

        int startFrom = 0;

        while (true)
        {
            QueryOptions queryOptions = new QueryOptions();

            /*
             * The links are needed as they are stored, rather than combined with the duplicates they describe.
             */
            queryOptions.setForDuplicateProcessing(true);
            queryOptions.setStartFrom(startFrom);
            queryOptions.setPageSize(integrationContext.getMaxPageSize());

            RelatedMetadataElementList peers = openMetadataStore.getRelatedMetadataElements(elementGUID,
                                                                                            0,
                                                                                            OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
                                                                                            queryOptions);

            if ((peers == null) || (peers.getElementList() == null) || (peers.getElementList().isEmpty()))
            {
                return peerGUIDs;
            }

            for (RelatedMetadataElement peer : peers.getElementList())
            {
                if ((peer != null) && (peer.getElement() != null))
                {
                    peerGUIDs.add(peer.getElement().getElementGUID());
                }
            }

            startFrom += integrationContext.getMaxPageSize();
        }
    }


    /**
     * Set the path name of a folder or file to its canonical path name, if it is not already.  Nothing else
     * about the element is changed.
     *
     * @param element           folder or file
     * @param canonicalPathName its canonical path name
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void correctPathName(OpenMetadataRootElement element,
                                 String                  canonicalPathName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "correctPathName";

        if ((element.getProperties() instanceof DataStoreProperties dataStoreProperties) &&
                (! canonicalPathName.equals(dataStoreProperties.getPathName())))
        {
            String elementTypeName = element.getElementHeader().getType().getTypeName();

            DataStoreProperties canonicalProperties = new DataStoreProperties();

            canonicalProperties.setTypeName(elementTypeName);
            canonicalProperties.setPathName(canonicalPathName);

            AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.DATA_STORE.typeName);

            assetClient.updateAsset(element.getElementHeader().getGUID(), assetClient.getUpdateOptions(true), canonicalProperties);

            logRecord(methodName,
                      LiskovAuditCode.CANONICAL_PATH_NAME_SET.getMessageDefinition(connectorName,
                                                                                   elementTypeName,
                                                                                   dataStoreProperties.getDisplayName(),
                                                                                   element.getElementHeader().getGUID(),
                                                                                   dataStoreProperties.getPathName(),
                                                                                   canonicalPathName));
        }
    }


    /**
     * Convert a canonical path name back into the path name that the file system on this machine knows it by.
     *
     * @param canonicalPathName   canonical path name
     * @param localMountPoint     root part of the path name on this machine
     * @param canonicalMountPoint value that replaces the local mount point in the catalog
     * @return local path name; this is the canonical path name if it does not start with the canonical mount point
     * @throws IOException the file system is not able to resolve the local mount point
     */
    static String getLocalPathName(String canonicalPathName,
                                   String localMountPoint,
                                   String canonicalMountPoint) throws IOException
    {
        if ((canonicalPathName == null) || (localMountPoint == null) || (canonicalMountPoint == null))
        {
            return canonicalPathName;
        }

        if ((canonicalPathName.equals(canonicalMountPoint)) || (canonicalPathName.startsWith(canonicalMountPoint + "/")))
        {
            return new File(localMountPoint).getCanonicalPath() + canonicalPathName.substring(canonicalMountPoint.length());
        }

        return canonicalPathName;
    }


    /**
     * The mount points used to convert between the local and canonical forms of a path name.  Either may be null,
     * in which case path names are not converted.
     *
     * @param localMountPoint     root part of the path name on this machine
     * @param canonicalMountPoint value that replaces the local mount point in the catalog
     */
    record MountPoints(String localMountPoint, String canonicalMountPoint) {}


    /**
     * Work out the mount points that apply to a folder that is a member of a data sharing hub.  They need to be
     * the ones that the folder cataloguer uses when it catalogues the folder's files, so they are taken from
     * the same places, in the same order:
     * <ul>
     *     <li>The file system that the folder, or one of the folders above it, is linked to - see
     *     {@link #getFileSystemMountPoints(String)}.</li>
     *     <li>The file system named as the metadata source in the catalog target relationship that makes the
     *     folder a target of the folder cataloguer.</li>
     *     <li>The configuration properties of that catalog target relationship.</li>
     *     <li>This connector's own configuration properties - used when the folder is not yet a catalog target.</li>
     *     <li>The defaults.</li>
     * </ul>
     *
     * @param folderGUID unique identifier of the folder
     * @return mount points
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private MountPoints getMountPoints(String folderGUID) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String methodName = "getMountPoints";

        MountPoints fileSystemMountPoints = this.getFileSystemMountPoints(folderGUID);

        if (fileSystemMountPoints != null)
        {
            return fileSystemMountPoints;
        }

        MountPoints mountPoints = new MountPoints(this.getMountPoint(LiskovConfigurationProperty.LOCAL_MOUNT_POINT),
                                                  this.getMountPoint(LiskovConfigurationProperty.CANONICAL_MOUNT_POINT));

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setStartFrom(0);
        queryOptions.setPageSize(integrationContext.getMaxPageSize());

        RelatedMetadataElementList catalogTargets = openMetadataStore.getRelatedMetadataElements(folderGUID,
                                                                                                 0,
                                                                                                 OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName,
                                                                                                 queryOptions);

        if ((catalogTargets == null) || (catalogTargets.getElementList() == null))
        {
            return mountPoints;
        }

        for (RelatedMetadataElement catalogTarget : catalogTargets.getElementList())
        {
            if ((catalogTarget == null) || (catalogTarget.getRelationshipProperties() == null))
            {
                continue;
            }

            Map<String, Object> targetConfigurationProperties = propertyHelper.getMapFromProperty(connectorName,
                                                                                                  OpenMetadataProperty.CONFIGURATION_PROPERTIES.name,
                                                                                                  catalogTarget.getRelationshipProperties(),
                                                                                                  methodName);

            String metadataSourceQualifiedName = propertyHelper.getStringProperty(connectorName,
                                                                                  OpenMetadataProperty.METADATA_SOURCE_QUALIFIED_NAME.name,
                                                                                  catalogTarget.getRelationshipProperties(),
                                                                                  methodName);

            boolean fileSystemFound           = false;
            String  fileSystemLocalMountPoint = null;
            String  fileSystemCanonicalMountPoint = null;

            if (metadataSourceQualifiedName != null)
            {
                OpenMetadataElement metadataSource = openMetadataStore.getMetadataElementByUniqueName(metadataSourceQualifiedName,
                                                                                                      OpenMetadataProperty.QUALIFIED_NAME.name);

                if ((metadataSource != null) && (propertyHelper.isTypeOf(metadataSource, OpenMetadataType.FILE_SYSTEM.typeName)))
                {
                    fileSystemFound               = true;
                    fileSystemLocalMountPoint     = propertyHelper.getStringProperty(connectorName,
                                                                                     OpenMetadataProperty.LOCAL_MOUNT_POINT.name,
                                                                                     metadataSource.getElementProperties(),
                                                                                     methodName);
                    fileSystemCanonicalMountPoint = propertyHelper.getStringProperty(connectorName,
                                                                                     OpenMetadataProperty.CANONICAL_MOUNT_POINT.name,
                                                                                     metadataSource.getElementProperties(),
                                                                                     methodName);
                }
            }

            MountPoints targetMountPoints = resolveMountPoints(mountPoints,
                                                               targetConfigurationProperties,
                                                               fileSystemFound,
                                                               fileSystemLocalMountPoint,
                                                               fileSystemCanonicalMountPoint);

            if (! targetMountPoints.equals(mountPoints))
            {
                /*
                 * This is the catalog target that says how the folder's files are catalogued.
                 */
                return targetMountPoints;
            }
        }

        return mountPoints;
    }


    /**
     * A link from a file or folder to a file system that has its mount points set.
     *
     * @param fileSystemGUID      unique identifier of the file system
     * @param uses                is the link one that says the file system merely uses the file or folder?
     * @param localMountPoint     local mount point of the file system
     * @param canonicalMountPoint canonical mount point of the file system
     */
    record FileSystemLink(String fileSystemGUID, boolean uses, String localMountPoint, String canonicalMountPoint) {}


    /**
     * Look for the file system that a file or folder belongs to, and return its mount points.  The file system
     * is a FileSystem software capability that is linked, with a CapabilityAssetUse relationship, to the file or
     * folder itself or to one of the folders above it in the file hierarchy (found by following the NestedFile
     * and FolderHierarchy relationships upwards).  Only a file system that has both of its mount points set
     * counts.  The nearest one is used: the search stops at the first level of the hierarchy that is linked to
     * such a file system, and anything further up is not considered.
     * <p>
     * If that level is linked to more than one such file system, the ones that only use the file or folder
     * (useType=USES) are set aside.  If that does not leave exactly one, there is no telling which is right, so
     * the file systems are reported in the audit log and ignored - including any further up.
     *
     * @param elementGUID unique identifier of the file or folder
     * @return mount points, or null if there is no file system to take them from
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private MountPoints getFileSystemMountPoints(String elementGUID) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "getFileSystemMountPoints";

        List<FileSystemLink> fileSystemLinks = new ArrayList<>();
        Set<String>          visitedGUIDs    = new HashSet<>();
        List<String>         currentLevel    = new ArrayList<>();

        currentLevel.add(elementGUID);

        /*
         * Each level is the element itself, then its parent folders, then their parent folders, and so on.
         * The first level that is linked to a file system is the nearest, and the search stops there.
         */
        while ((! currentLevel.isEmpty()) && (fileSystemLinks.isEmpty()))
        {
            List<String> parentLevel = new ArrayList<>();

            for (String currentGUID : currentLevel)
            {
                if (visitedGUIDs.add(currentGUID))
                {
                    this.addFileSystemLinks(currentGUID, fileSystemLinks);

                    parentLevel.addAll(this.getParentFolderGUIDs(currentGUID, OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName));
                    parentLevel.addAll(this.getParentFolderGUIDs(currentGUID, OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName));
                }
            }

            currentLevel = parentLevel;
        }

        if (fileSystemLinks.isEmpty())
        {
            return null;
        }

        FileSystemLink fileSystemLink = chooseFileSystem(fileSystemLinks);

        if (fileSystemLink == null)
        {
            Set<String> fileSystemGUIDs = new TreeSet<>();

            for (FileSystemLink candidate : fileSystemLinks)
            {
                fileSystemGUIDs.add(candidate.fileSystemGUID());
            }

            logRecord(methodName,
                      LiskovAuditCode.AMBIGUOUS_FILE_SYSTEMS.getMessageDefinition(connectorName,
                                                                                  elementGUID,
                                                                                  Integer.toString(fileSystemGUIDs.size()),
                                                                                  fileSystemGUIDs.toString()));
            return null;
        }

        return new MountPoints(fileSystemLink.localMountPoint(), fileSystemLink.canonicalMountPoint());
    }


    /**
     * Choose the file system to take the mount points from.  If the links lead to one file system, that is it.
     * If they lead to several, the file systems that are only linked with useType=USES are set aside, and if
     * that leaves one file system, that is it.
     *
     * @param fileSystemLinks links to file systems with mount points; there is at least one
     * @return a link to the chosen file system, or null if there is no single file system to choose
     */
    static FileSystemLink chooseFileSystem(List<FileSystemLink> fileSystemLinks)
    {
        /*
         * A file system may be linked more than once - to a folder and to one of its parents, for example.
         * It only uses the files if every one of its links says so.
         */
        Map<String, FileSystemLink> fileSystems = new TreeMap<>();

        for (FileSystemLink fileSystemLink : fileSystemLinks)
        {
            FileSystemLink existingLink = fileSystems.get(fileSystemLink.fileSystemGUID());

            if ((existingLink == null) || ((existingLink.uses()) && (! fileSystemLink.uses())))
            {
                fileSystems.put(fileSystemLink.fileSystemGUID(), fileSystemLink);
            }
        }

        if (fileSystems.size() == 1)
        {
            return fileSystems.values().iterator().next();
        }

        FileSystemLink chosenFileSystem = null;

        for (FileSystemLink fileSystemLink : fileSystems.values())
        {
            if (! fileSystemLink.uses())
            {
                if (chosenFileSystem != null)
                {
                    return null;
                }

                chosenFileSystem = fileSystemLink;
            }
        }

        return chosenFileSystem;
    }


    /**
     * Add the links from a file or folder to the file systems that have their mount points set.
     *
     * @param elementGUID     unique identifier of the file or folder
     * @param fileSystemLinks list to add to
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void addFileSystemLinks(String               elementGUID,
                                    List<FileSystemLink> fileSystemLinks) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "addFileSystemLinks";

        for (RelatedMetadataElement capability : this.getAllRelatedElements(elementGUID, OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName))
        {
            /*
             * The capability is at end 1 of the relationship.
             */
            if ((capability.getElementAtEnd1()) && (propertyHelper.isTypeOf(capability.getElement(), OpenMetadataType.FILE_SYSTEM.typeName)))
            {
                String localMountPoint     = propertyHelper.getStringProperty(connectorName,
                                                                              OpenMetadataProperty.LOCAL_MOUNT_POINT.name,
                                                                              capability.getElement().getElementProperties(),
                                                                              methodName);
                String canonicalMountPoint = propertyHelper.getStringProperty(connectorName,
                                                                              OpenMetadataProperty.CANONICAL_MOUNT_POINT.name,
                                                                              capability.getElement().getElementProperties(),
                                                                              methodName);

                if ((localMountPoint != null) && (! localMountPoint.isBlank()) && (canonicalMountPoint != null) && (! canonicalMountPoint.isBlank()))
                {
                    String useType = null;

                    if ((capability.getRelationshipProperties() != null) && (capability.getRelationshipProperties().getPropertiesAsStrings() != null))
                    {
                        useType = capability.getRelationshipProperties().getPropertiesAsStrings().get(OpenMetadataProperty.USE_TYPE.name);
                    }

                    fileSystemLinks.add(new FileSystemLink(capability.getElement().getElementGUID(),
                                                           CapabilityAssetUseType.USES.name().equalsIgnoreCase(useType),
                                                           localMountPoint,
                                                           canonicalMountPoint));
                }
            }
        }
    }


    /**
     * Return the folders that a file or folder is nested in.
     *
     * @param elementGUID          unique identifier of the file or folder
     * @param relationshipTypeName the type of relationship that links it to its parent folder
     * @return unique identifiers of its parent folders
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private List<String> getParentFolderGUIDs(String elementGUID,
                                              String relationshipTypeName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        List<String> parentFolderGUIDs = new ArrayList<>();

        for (RelatedMetadataElement relatedElement : this.getAllRelatedElements(elementGUID, relationshipTypeName))
        {
            /*
             * The parent folder is at end 1 of the relationship.  The folders nested in this one are at end 2.
             */
            if (relatedElement.getElementAtEnd1())
            {
                parentFolderGUIDs.add(relatedElement.getElement().getElementGUID());
            }
        }

        return parentFolderGUIDs;
    }


    /**
     * Return all the elements linked to an element by a type of relationship, at either end.
     *
     * @param elementGUID          unique identifier of the element
     * @param relationshipTypeName the type of relationship
     * @return related elements
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private List<RelatedMetadataElement> getAllRelatedElements(String elementGUID,
                                                               String relationshipTypeName) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        List<RelatedMetadataElement> allRelatedElements = new ArrayList<>();

        int startFrom = 0;

        while (true)
        {
            QueryOptions queryOptions = new QueryOptions();

            queryOptions.setStartFrom(startFrom);
            queryOptions.setPageSize(integrationContext.getMaxPageSize());

            RelatedMetadataElementList relatedElements = openMetadataStore.getRelatedMetadataElements(elementGUID,
                                                                                                      0,
                                                                                                      relationshipTypeName,
                                                                                                      queryOptions);

            if ((relatedElements == null) || (relatedElements.getElementList() == null) || (relatedElements.getElementList().isEmpty()))
            {
                return allRelatedElements;
            }

            for (RelatedMetadataElement relatedElement : relatedElements.getElementList())
            {
                if ((relatedElement != null) && (relatedElement.getElement() != null))
                {
                    allRelatedElements.add(relatedElement);
                }
            }

            startFrom += integrationContext.getMaxPageSize();
        }
    }


    /**
     * Apply the settings of a catalog target to the mount points, in the way that the folder cataloguer does.
     * A mount point in the catalog target's configuration properties replaces the default.  If the catalog target
     * names a file system as its metadata source, then both mount points come from the file system - even if it
     * does not set them, which means that path names are not converted.
     *
     * @param defaultMountPoints            mount points to use where the catalog target does not say
     * @param targetConfigurationProperties configuration properties from the catalog target relationship
     * @param fileSystemFound               does the catalog target name a file system as its metadata source?
     * @param fileSystemLocalMountPoint     local mount point from the file system
     * @param fileSystemCanonicalMountPoint canonical mount point from the file system
     * @return mount points
     */
    static MountPoints resolveMountPoints(MountPoints         defaultMountPoints,
                                          Map<String, Object> targetConfigurationProperties,
                                          boolean             fileSystemFound,
                                          String              fileSystemLocalMountPoint,
                                          String              fileSystemCanonicalMountPoint)
    {
        if (fileSystemFound)
        {
            return new MountPoints(fileSystemLocalMountPoint, fileSystemCanonicalMountPoint);
        }

        String localMountPoint     = defaultMountPoints.localMountPoint();
        String canonicalMountPoint = defaultMountPoints.canonicalMountPoint();

        if (targetConfigurationProperties != null)
        {
            if (targetConfigurationProperties.get(LiskovConfigurationProperty.LOCAL_MOUNT_POINT.getName()) != null)
            {
                localMountPoint = targetConfigurationProperties.get(LiskovConfigurationProperty.LOCAL_MOUNT_POINT.getName()).toString();
            }

            if (targetConfigurationProperties.get(LiskovConfigurationProperty.CANONICAL_MOUNT_POINT.getName()) != null)
            {
                canonicalMountPoint = targetConfigurationProperties.get(LiskovConfigurationProperty.CANONICAL_MOUNT_POINT.getName()).toString();
            }
        }

        return new MountPoints(localMountPoint, canonicalMountPoint);
    }


    /**
     * Return one of the mount point configuration properties.  The default is the one used by the folder cataloguer.
     *
     * @param mountPointProperty which mount point
     * @return configured value or default
     */
    private String getMountPoint(LiskovConfigurationProperty mountPointProperty)
    {
        String mountPoint = super.getStringConfigurationProperty(mountPointProperty.getName(), this.getConfigurationProperties());

        if ((mountPoint == null) || (mountPoint.isBlank()))
        {
            return mountPointProperty.getExample();
        }

        return mountPoint;
    }


    /**
     * Convert the path name of a folder into the canonical path name that the folder cataloguer records for the
     * files it finds.  The path name is resolved by the file system, and then the local mount point at its start
     * is replaced by the canonical mount point.  A path name that already starts with the canonical mount point
     * is returned unchanged.
     *
     * @param pathName            path name from the folder asset
     * @param localMountPoint     root part of the path name on this machine
     * @param canonicalMountPoint value that replaces the local mount point in the catalog
     * @return canonical path name, or null if there is no path name
     * @throws IOException the file system is not able to resolve the path name or the local mount point
     */
    static String getCanonicalPathName(String pathName,
                                       String localMountPoint,
                                       String canonicalMountPoint) throws IOException
    {
        if ((pathName == null) || (pathName.isBlank()))
        {
            return null;
        }

        String fileSystemPathName = pathName;

        if (fileSystemPathName.startsWith("file://"))
        {
            fileSystemPathName = fileSystemPathName.substring(7);
        }

        if ((localMountPoint == null) || (canonicalMountPoint == null))
        {
            return fileSystemPathName;
        }

        if ((fileSystemPathName.equals(canonicalMountPoint)) || (fileSystemPathName.startsWith(canonicalMountPoint + "/")))
        {
            return fileSystemPathName;
        }

        return new File(fileSystemPathName).getCanonicalPath().replace(new File(localMountPoint).getCanonicalPath(), canonicalMountPoint);
    }


    /**
     * Refreshes the metadata for the files of a particular type that are catalogued under a folder.
     *
     * @param fileTypeName                open metadata type name of the files to look for
     * @param folderPathName              canonical path name of the folder
     * @param dataFieldsFolderGUID        Globally unique identifier (GUID) for the folder containing data fields.
     * @param dataStructuresFolderGUID    Globally unique identifier (GUID) for the folder containing data structures.
     * @param dataSharingHubGUID                 Globally unique identifier (GUID) of the data sharing hub associated with the data store.
     * @param dataSharingHubQualifiedName        Globally unique name of the data sharing hub associated with the data store.
     * @param dataStructures              Map of identifiers for dataFieldGUIDs in the data structures used during the refresh process.
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void refreshFilesInFolder(String                   fileTypeName,
                                      String                   folderPathName,
                                      String                   dataFieldsFolderGUID,
                                      String                   dataStructuresFolderGUID,
                                      String                   dataSharingHubGUID,
                                      String                   dataSharingHubQualifiedName,
                                      Map<String, Set<String>> dataStructures) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient(fileTypeName);

        int startFrom = 0;
        SearchOptions searchOptions = classificationExplorerClient.getSearchOptions(startFrom, classificationExplorerClient.getMaxPagingSize());

        List<OpenMetadataRootElement> files = classificationExplorerClient.findRootElements(folderPathName, searchOptions);

        while (files != null)
        {
            for (OpenMetadataRootElement file : files)
            {
                /*
                 * These files are revealed by the cataloguing of the folder.  Each of them has its own
                 * file type specific survey.
                 */
                refreshAssetGovernance(file);
                refreshTabularFile(dataFieldsFolderGUID, dataStructuresFolderGUID, file, dataSharingHubGUID, dataSharingHubQualifiedName, dataStructures);
            }

            startFrom += classificationExplorerClient.getMaxPagingSize();
            searchOptions = classificationExplorerClient.getSearchOptions(startFrom, classificationExplorerClient.getMaxPagingSize());

            files = classificationExplorerClient.findRootElements(folderPathName, searchOptions);
        }
    }


    /**
     * Refreshes the metadata for a file with a tabular schema (a CSV file or an Apache Parquet file) based on the
     * provided parameters. This method extracts the data structure and data fields from the columns that
     * the file's survey attached to the file asset.
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
    private void refreshTabularFile(String                   dataFieldsFolderGUID,
                                    String                   dataStructuresFolderGUID,
                                    OpenMetadataRootElement  dataStoreElement,
                                    String                   dataSharingHubGUID,
                                    String                   dataSharingHubQualifiedName,
                                    Map<String, Set<String>> dataStructures) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        String methodName = "refreshTabularFile(" + dataStoreElement.getElementHeader().getGUID() + ")";

        if (dataStoreElement.getProperties() instanceof DataFileProperties dataFileProperties)
        {
            logRecord(methodName,
                      LiskovAuditCode.REFRESHING_TABULAR_FILE.getMessageDefinition(connectorName,
                                                                                   dataStoreElement.getElementHeader().getType().getTypeName(),
                                                                                   dataFileProperties.getQualifiedName(),
                                                                               dataStoreElement.getElementHeader().getGUID(),
                                                                               dataSharingHubQualifiedName,
                                                                               dataSharingHubGUID));

            if ((dataStoreElement.getSchemaType() instanceof RelatedMetadataHierarchySummary schemaType) && (schemaType.getNestedElements() != null))
            {
                refreshDataStructure(dataFieldsFolderGUID,
                                     dataStructuresFolderGUID,
                                     dataStoreElement.getSchemaType().getRelatedElement().getElementHeader().getGUID(),
                                     null,
                                     dataFileProperties.getFileName(),
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
            /*
             * The first level of the schema is the tables.  Each table becomes a data structure, and its
             * columns (the next level down) become the data fields.  The database and schema names scope the
             * data structure's qualified name, since table names repeat across schemas and databases.
             */
            String schemaScopeName = databaseSchemaProperties.getDisplayName();

            if (dataStoreElement.getDataSetContent() != null)
            {
                for (RelatedMetadataElementSummary database : dataStoreElement.getDataSetContent())
                {
                    if ((database != null) &&
                            (propertyHelper.isTypeOf(database.getRelatedElement().getElementHeader(), OpenMetadataType.DATABASE.typeName)) &&
                            (database.getRelatedElement().getProperties() instanceof AssetProperties databaseProperties))
                    {
                        schemaScopeName = databaseProperties.getDisplayName() + "::" + schemaScopeName;
                        break;
                    }
                }
            }

            for (RelatedMetadataElementSummary table : schemaType.getNestedElements())
            {
                if ((table instanceof RelatedMetadataHierarchySummary tableHierarchy) &&
                        (tableHierarchy.getNestedElements() != null) &&
                        (table.getRelatedElement().getProperties() instanceof SchemaAttributeProperties tableProperties))
                {
                    refreshDataStructure(dataFieldsFolderGUID,
                                         dataStructuresFolderGUID,
                                         table.getRelatedElement().getElementHeader().getGUID(),
                                         schemaScopeName,
                                         tableProperties.getDisplayName(),
                                         tableHierarchy.getNestedElements(),
                                         dataSharingHubGUID,
                                         dataSharingHubQualifiedName,
                                         dataStructures);
                }
            }
        }
    }


    /**
     * Refreshes the data structure and data fields that describe one schema element - a CSV or Parquet file's schema type or
     * a database table.  A data field is maintained for each schema attribute, and a data structure for the
     * set of data fields.
     *
     * @param dataFieldsFolderGUID     Globally unique identifier (GUID) for the folder containing data fields.
     * @param dataStructuresFolderGUID Globally unique identifier (GUID) for the folder containing data structures.
     * @param schemaGUID               unique identifier of the schema element (schema type or table) being refreshed.
     * @param schemaScopeName          name of the scope that the schema element belongs to ("database::schema" for a
     *                                 table; null for a file).  It is part of the data structure's qualified name so
     *                                 same-named tables in different schemas and databases get different data structures.
     * @param schemaName               display name of the schema element being refreshed.
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
                                      String                              schemaScopeName,
                                      String                              schemaName,
                                      List<RelatedMetadataElementSummary> schemaAttributes,
                                      String                              dataSharingHubGUID,
                                      String                              dataSharingHubQualifiedName,
                                      Map<String, Set<String>>            dataStructures) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "refreshDataStructure";

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
            String qualifiedName     = OpenMetadataType.DATA_STRUCTURE.typeName + "::" + dataSharingHubGUID + "::";
            String description       = "Data structure for " + schemaName;

            if (schemaScopeName != null)
            {
                qualifiedName = qualifiedName + schemaScopeName + "::";
                description   = description + " in schema " + schemaScopeName;
            }

            DataStructureProperties dataStructureProperties = new DataStructureProperties();

            dataStructureProperties.setQualifiedName(qualifiedName + dataStructureName);
            dataStructureProperties.setDisplayName(dataStructureName);
            dataStructureProperties.setDescription(description + " in the data sharing hub " + dataSharingHubQualifiedName);

            dataStructureGUID = getDataStructure(dataSharingHubGUID,
                                                 dataSharingHubQualifiedName,
                                                 dataStructuresFolderGUID,
                                                 dataStructureProperties);

            dataStructures.put(dataStructureGUID, dataFieldGUIDs);

            DataStructureClient dataStructureClient = integrationContext.getDataStructureClient();

            for (String dataFieldGUID : dataFieldGUIDs)
            {
                dataStructureClient.linkMemberDataField(dataStructureGUID, dataFieldGUID, dataStructureClient.getMakeAnchorOptions(false), null);
            }
        }

        /*
         * Every schema that matches the data structure is linked to it - not just the one that caused it to be created.
         */
        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();
        ImplementedByProperties    implementedByProperties    = new ImplementedByProperties();

        implementedByProperties.setDesignStep("data-abstraction");

        governanceDefinitionClient.linkDesignToImplementation(dataStructureGUID, schemaGUID, governanceDefinitionClient.getMakeAnchorOptions(false), implementedByProperties);

        /*
         * A schema is implemented by exactly one of this hub's data structures.  Links left from earlier refreshes -
         * for example, before the data structures were shared, or when the schema's columns have since changed -
         * are removed.  Data structures from other data sharing hubs are left alone.
         */
        String                     hubDataStructurePrefix     = OpenMetadataType.DATA_STRUCTURE.typeName + "::" + dataSharingHubGUID + "::";
        OpenMetadataStore          openMetadataStore          = integrationContext.getOpenMetadataStore();
        RelatedMetadataElementList relatedMetadataElementList = openMetadataStore.getRelatedMetadataElements(schemaGUID,
                                                                                                             2,
                                                                                                             OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                                                                             0,
                                                                                                             openMetadataStore.getMaxPagingSize());

        if ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
        {
            for (RelatedMetadataElement design : relatedMetadataElementList.getElementList())
            {
                if ((design != null) &&
                        (! dataStructureGUID.equals(design.getElement().getElementGUID())) &&
                        (propertyHelper.isTypeOf(design.getElement(), OpenMetadataType.DATA_STRUCTURE.typeName)))
                {
                    String designQualifiedName = propertyHelper.getStringProperty(connectorName,
                                                                                  OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                  design.getElement().getElementProperties(),
                                                                                  methodName);

                    if ((designQualifiedName != null) && (designQualifiedName.startsWith(hubDataStructurePrefix)))
                    {
                        governanceDefinitionClient.detachDesignFromImplementation(design.getElement().getElementGUID(),
                                                                                  schemaGUID,
                                                                                  governanceDefinitionClient.getDeleteOptions(false));
                    }
                }
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

            logRecord(methodName,
                      LiskovAuditCode.NEW_DATA_FIELD.getMessageDefinition(connectorName,
                                                                          dataFieldProperties.getDisplayName(),
                                                                          dataFieldGUID,
                                                                          dataSharingHubQualifiedName,
                                                                          dataSharingHubGUID));
        }

        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();
        ImplementedByProperties    implementedByProperties    = new ImplementedByProperties();

        implementedByProperties.setDesignStep("data-abstraction");

        governanceDefinitionClient.linkDesignToImplementation(dataFieldGUID, schemaGUID, governanceDefinitionClient.getMakeAnchorOptions(false), implementedByProperties);

        return dataFieldGUID;
    }


    /**
     * Retrieves or creates a new DataStructure based on the provided properties within a specified DataSharingHub and its collection.
     *
     * @param dataSharingHubGUID Unique identifier of the DataSharingHub where the DataStructure resides or will be created.
     * @param dataSharingHubQualifiedName Qualified name of the DataSharingHub where the DataStructure resides or will be created.
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

            logRecord(methodName,
                      LiskovAuditCode.NEW_DATA_STRUCTURE.getMessageDefinition(connectorName,
                                                                              dataStructuresProperties.getDisplayName(),
                                                                              dataStructureGUID,
                                                                              dataSharingHubQualifiedName,
                                                                              dataSharingHubGUID));
        }

        return dataStructureGUID;
    }


    /* ==============================================================================
     * Methods that ensure the members of the data sharing hub are catalogued and surveyed.
     * Egeria's content packs define governance action types for cataloguing and surveying each
     * type of technology.  They are linked to the technology type (deployedImplementationType) that
     * they support with a ResourceList relationship, qualified by the resource use.
     */


    /**
     * Ensure that the contents of the supplied element are being catalogued, and request a new survey when one
     * is due so that the description of its contents stays complete and up-to-date.  Cataloguing is only requested
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
                logRecord(methodName,
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

                List<MetadataElementSummary> surveyActionTypes = new ArrayList<>();
                boolean                      surveyRunning     = false;

                for (RelatedMetadataElementSummary resource : technologyType.getResourceList())
                {
                    if ((resource != null) &&
                            (propertyHelper.isTypeOf(resource.getRelatedElement().getElementHeader(), OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName)) &&
                            (resource.getRelationshipProperties() instanceof ResourceListProperties resourceListProperties))
                    {
                        boolean running = runningActionTypeGUIDs.contains(resource.getRelatedElement().getElementHeader().getGUID());

                        if (ResourceUse.CATALOG_RESOURCE.getResourceUse().equals(resourceListProperties.getResourceUse()))
                        {
                            if (! running)
                            {
                                enableCataloguing(assetElement, assetProperties, resource.getRelatedElement());
                            }
                        }
                        else if (ResourceUse.SURVEY_RESOURCE.getResourceUse().equals(resourceListProperties.getResourceUse()))
                        {
                            surveyActionTypes.add(resource.getRelatedElement());
                            surveyRunning = surveyRunning || running;
                        }
                    }
                }

                /*
                 * Only one survey of an element runs at a time, and only the one that describes it most fully.
                 */
                if ((! surveyActionTypes.isEmpty()) && (! surveyRunning))
                {
                    MetadataElementSummary chosenSurvey = chooseSurvey(assetElement.getElementHeader().getGUID(), surveyActionTypes);

                    if (chosenSurvey != null)
                    {
                        requestSurvey(assetElement, assetProperties, chosenSurvey);
                    }
                }
            }
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
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

        /*
         * The governance action types for the technology are linked to it through its resource list - nothing
         * else attached to it is used.
         */
        queryOptions.setGraphQueryDepth(1);
        queryOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName));

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

        /*
         * Its predefined action targets and its executor are all that is used from its graph.
         */
        GetOptions getOptions = classificationExplorerClient.getGetOptions();

        getOptions.setGraphQueryDepth(1);
        getOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                       OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName));

        OpenMetadataRootElement governanceActionType = classificationExplorerClient.getRootElementByGUID(governanceActionTypeGUID, getOptions);

        actionTypeCache.put(governanceActionTypeGUID, governanceActionType);

        return governanceActionType;
    }


    /**
     * Request that the contents of the supplied element are catalogued, if this is not already happening.  The
     * cataloguing governance action type links the element to an integration connector that maintains the
     * description of its contents.  Therefore, cataloguing is already enabled if the element is a catalog target
     * of that integration connector - or if the database that holds it is, since cataloguing a database
     * catalogues each of its schemas.  A schema found that way has no connection of its own, so making it a
     * catalog target as well left the connector a target it could not connect to, on every refresh.
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
         * The governance action types that enable cataloguing of an existing element have the integration
         * connector that will do the cataloguing set up as a predefined action target.  If the element is already
         * one of its catalog targets then cataloguing is already enabled.
         *
         * A technology type's catalog resources also include governance action types that create a new element
         * from a template (such as create-file-folder).  They have no integration connector target.  The element
         * already exists here, so they are skipped - running them would request a pointless create on every refresh.
         */
        boolean enablesCatalogTarget = false;

        if (governanceActionType.getPredefinedTargetForAction() != null)
        {
            for (RelatedMetadataElementSummary predefinedTarget : governanceActionType.getPredefinedTargetForAction())
            {
                if ((predefinedTarget != null) &&
                        (predefinedTarget.getRelationshipProperties() instanceof TargetForGovernanceActionProperties targetForGovernanceActionProperties) &&
                        (ActionTarget.INTEGRATION_CONNECTOR.getName().equals(targetForGovernanceActionProperties.getActionTargetName())))
                {
                    Set<String> catalogTargetGUIDs = getCatalogTargetGUIDs(predefinedTarget.getRelatedElement().getElementHeader().getGUID());

                    for (String cataloguedGUID : getCataloguingScopeGUIDs(assetElement, propertyHelper))
                    {
                        if (catalogTargetGUIDs.contains(cataloguedGUID))
                        {
                            return;
                        }
                    }

                    enablesCatalogTarget = true;
                }
            }
        }

        if (! enablesCatalogTarget)
        {
            return;
        }

        String engineActionGUID = initiateGovernanceActionType(governanceActionTypeProperties.getQualifiedName(), assetGUID);

        logRecord(methodName,
                  LiskovAuditCode.ENABLING_CATALOGUING.getMessageDefinition(connectorName,
                                                                            engineActionGUID,
                                                                            assetElement.getElementHeader().getType().getTypeName(),
                                                                            assetProperties.getDisplayName(),
                                                                            assetGUID,
                                                                            governanceActionTypeProperties.getQualifiedName()));
    }


    /**
     * Return the elements whose cataloguing catalogues the supplied element: the element itself, and any database
     * that holds it.
     *
     * @param assetElement element to catalog
     * @param propertyHelper helper that walks the element types' supertypes
     * @return unique identifiers, the element's first
     */
    static List<String> getCataloguingScopeGUIDs(OpenMetadataRootElement assetElement,
                                                 PropertyHelper          propertyHelper)
    {
        List<String> scopeGUIDs = new ArrayList<>();

        scopeGUIDs.add(assetElement.getElementHeader().getGUID());

        if (assetElement.getDataSetContent() != null)
        {
            for (RelatedMetadataElementSummary dataStore : assetElement.getDataSetContent())
            {
                if ((dataStore != null) && (dataStore.getRelatedElement() != null)
                            && (dataStore.getRelatedElement().getElementHeader() != null)
                            && (propertyHelper.isTypeOf(dataStore.getRelatedElement().getElementHeader(), OpenMetadataType.DATABASE.typeName)))
                {
                    scopeGUIDs.add(dataStore.getRelatedElement().getElementHeader().getGUID());
                }
            }
        }

        return scopeGUIDs;
    }


    /**
     * Choose which of the surveys listed for an element's technology type to run - see {@link SurveySelector}.
     * Each survey's latest completed report for the element is found from the element's survey reports, and the
     * different types of annotation it reported are counted.  Surveys that are excluded by configuration, or whose
     * governance action type cannot be read, are not considered.
     *
     * @param assetGUID unique identifier of the element to survey
     * @param surveyActionTypes governance action types of the surveys listed for the element's technology type
     * @return survey to run, or null if none can be run
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private MetadataElementSummary chooseSurvey(String                       assetGUID,
                                                List<MetadataElementSummary> surveyActionTypes) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        List<SurveySelector.Candidate<MetadataElementSummary>> candidates = new ArrayList<>();

        for (MetadataElementSummary surveyActionType : surveyActionTypes)
        {
            OpenMetadataRootElement governanceActionType = getGovernanceActionType(surveyActionType.getElementHeader().getGUID());

            if ((governanceActionType == null) ||
                    (! (governanceActionType.getProperties() instanceof GovernanceActionTypeProperties governanceActionTypeProperties)) ||
                    (isExcludedSurvey(governanceActionTypeProperties.getQualifiedName())))
            {
                continue;
            }

            String requestType = getRequestType(governanceActionType, governanceActionTypeProperties);

            if (requestType == null)
            {
                /*
                 * This survey's reports cannot be told apart from the others', so nothing is known of its results.
                 * It is only chosen if no other survey has produced any annotations.
                 */
                candidates.add(new SurveySelector.Candidate<>(surveyActionType, true, 0));
                continue;
            }

            List<OpenMetadataRootElement> surveyReports          = new ArrayList<>();
            List<OpenMetadataRootElement> completedSurveyReports = new ArrayList<>();

            this.getRecentSurveyReports(assetGUID, requestType, surveyReports, completedSurveyReports);

            int annotationTypeCount = completedSurveyReports.isEmpty() ? 0 : getReportedAnnotationTypeCount(completedSurveyReports.get(0).getElementHeader().getGUID());

            candidates.add(new SurveySelector.Candidate<>(surveyActionType, ! surveyReports.isEmpty(), annotationTypeCount));
        }

        return SurveySelector.chooseSurvey(candidates);
    }


    /**
     * Request a new survey of the supplied element if one is due - see {@link SurveySchedule}.  A
     * survey is never requested while one requested on a previous refresh is still running.
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

        SurveySchedule.Decision decision = this.isSurveyDue(assetGUID, getRequestType(governanceActionType, governanceActionTypeProperties));

        if (! decision.due())
        {
            return;
        }

        String engineActionGUID = initiateGovernanceActionType(governanceActionTypeProperties.getQualifiedName(), assetGUID);

        logRecord(methodName,
                  LiskovAuditCode.STARTING_SURVEY.getMessageDefinition(connectorName,
                                                                       engineActionGUID,
                                                                       assetElement.getElementHeader().getType().getTypeName(),
                                                                       assetProperties.getDisplayName(),
                                                                       assetGUID,
                                                                       governanceActionTypeProperties.getQualifiedName(),
                                                                       decision.reason()));
    }


    /**
     * Return the request type that the governance action type passes to its survey engine.  The survey action
     * engine records it as the purpose of each survey report it creates, which is how the reports from this
     * survey are told apart from those of any other survey of the same element.  It is a property of the
     * GovernanceActionExecutor relationship.  Should that not be available, the request type is taken from the
     * end of the governance action type's qualified name, which the content packs build from the engine name and
     * request type (for example, FileSurvey::survey-folder).
     *
     * @param governanceActionType governance action type
     * @param governanceActionTypeProperties its properties
     * @return request type, or null if it cannot be determined
     */
    private String getRequestType(OpenMetadataRootElement        governanceActionType,
                                  GovernanceActionTypeProperties governanceActionTypeProperties)
    {
        if ((governanceActionType.getGovernanceActionExecutor() != null) &&
                (governanceActionType.getGovernanceActionExecutor().getRelationshipProperties() instanceof GovernanceActionExecutorProperties governanceActionExecutorProperties) &&
                (governanceActionExecutorProperties.getRequestType() != null))
        {
            return governanceActionExecutorProperties.getRequestType();
        }

        String qualifiedName = governanceActionTypeProperties.getQualifiedName();

        if (qualifiedName != null)
        {
            int requestTypeStart = qualifiedName.lastIndexOf("::");

            if (requestTypeStart >= 0)
            {
                return qualifiedName.substring(requestTypeStart + 2);
            }
        }

        return null;
    }


    /**
     * Determine whether a survey of an element is due - see {@link SurveySchedule} for the rules.  The schedule
     * is worked out from the element's survey reports, so nothing about it is stored.
     *
     * @param assetGUID unique identifier of the element to survey
     * @param requestType request type of the survey - recorded as the purpose of its survey reports
     * @return whether a survey should be requested, and why
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private SurveySchedule.Decision isSurveyDue(String assetGUID,
                                                String requestType) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        if (requestType == null)
        {
            /*
             * The survey's reports cannot be identified, so there is no history to go on.
             */
            return new SurveySchedule.Decision(true, "its survey reports cannot be identified because the survey's request type is not known");
        }

        List<OpenMetadataRootElement> surveyReports          = new ArrayList<>();
        List<OpenMetadataRootElement> completedSurveyReports = new ArrayList<>();

        this.getRecentSurveyReports(assetGUID, requestType, surveyReports, completedSurveyReports);

        Date latestSurveyTime = surveyReports.isEmpty() ? null : this.getSurveyTime(surveyReports.get(0));

        return surveySchedule.decide(latestSurveyTime,
                                          completedSurveyReports.size(),
                                          surveyIndex -> this.surveyFoundChange(completedSurveyReports.get(surveyIndex),
                                                                                completedSurveyReports.get(surveyIndex + 1)),
                                          System.currentTimeMillis());
    }


    /**
     * Return one of the survey interval configuration properties.
     *
     * @param propertyName name of the configuration property
     * @param defaultDays value to use if the property is not set
     * @return number of days
     */
    private int getSurveyIntervalDays(String propertyName,
                                      int    defaultDays)
    {
        Map<String, Object> configurationProperties = this.getConfigurationProperties();

        if ((configurationProperties != null) && (configurationProperties.get(propertyName) != null))
        {
            return super.getIntConfigurationProperty(propertyName, configurationProperties);
        }

        return defaultDays;
    }


    /**
     * Retrieve the most recent survey reports for an element that were produced by the requested survey, newest
     * first.  The element's reports are read a page at a time, most recently created first, and stop as soon as
     * the survey schedule has all the completed reports it can need.  An element that has been
     * surveyed for a long time may have a great many reports, and none beyond these are read.
     *
     * @param assetGUID unique identifier of the surveyed element
     * @param requestType request type of the survey - recorded as the purpose of its survey reports
     * @param surveyReports all the matching reports found, newest first
     * @param completedSurveyReports the matching reports whose survey completed, newest first
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private void getRecentSurveyReports(String                        assetGUID,
                                        String                        requestType,
                                        List<OpenMetadataRootElement> surveyReports,
                                        List<OpenMetadataRootElement> completedSurveyReports) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        AnnotationClient annotationClient = integrationContext.getAnnotationClient();
        int              startFrom        = 0;

        QueryOptions queryOptions = annotationClient.getQueryOptions(startFrom, surveyReportPageSize);
        queryOptions.setGraphQueryDepth(0);

        List<OpenMetadataRootElement> retrievedReports = annotationClient.getSurveyReportsForElement(assetGUID, queryOptions);

        while ((retrievedReports != null) && (! retrievedReports.isEmpty()))
        {
            for (OpenMetadataRootElement retrievedReport : retrievedReports)
            {
                if ((retrievedReport != null) &&
                        (retrievedReport.getProperties() instanceof SurveyReportProperties surveyReportProperties) &&
                        (requestType.equals(surveyReportProperties.getPurpose())))
                {
                    surveyReports.add(retrievedReport);

                    if (surveyReportProperties.getCompletionTime() != null)
                    {
                        completedSurveyReports.add(retrievedReport);

                        if (completedSurveyReports.size() >= surveySchedule.getCompletedSurveysNeeded())
                        {
                            return;
                        }
                    }
                }
            }

            startFrom = startFrom + surveyReportPageSize;

            queryOptions = annotationClient.getQueryOptions(startFrom, surveyReportPageSize);
            queryOptions.setGraphQueryDepth(0);

            retrievedReports = annotationClient.getSurveyReportsForElement(assetGUID, queryOptions);
        }
    }


    /**
     * Return the time that a survey started.
     *
     * @param surveyReport survey report
     * @return start time - the report's creation time if the survey did not record one
     */
    private Date getSurveyTime(OpenMetadataRootElement surveyReport)
    {
        if (surveyReport.getProperties() instanceof SurveyReportProperties surveyReportProperties)
        {
            if (surveyReportProperties.getStartTime() != null)
            {
                return surveyReportProperties.getStartTime();
            }

            if (surveyReportProperties.getCreatedTime() != null)
            {
                return surveyReportProperties.getCreatedTime();
            }
        }

        return surveyReport.getElementHeader().getVersions().getCreateTime();
    }


    /**
     * Determine whether a survey found a change since the survey before it.  An annotation that a later survey
     * finds again is reused rather than recreated, so two surveys that found the same things report exactly the
     * same annotations.  A change is a new annotation, or an annotation the earlier survey reported that the later
     * one did not.
     * <br><br>
     * The numbers of annotations are compared first, because the repository counts them without returning them.
     * Different numbers are a change.  Equal numbers are not proof of no change - an annotation can drop out of
     * one survey and come back in the next, reused from an earlier one - so only then are the annotations'
     * unique identifiers retrieved and compared.
     *
     * @param newerSurveyReport report of the later survey
     * @param olderSurveyReport report of the survey before it
     * @return true if the later survey found a change
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private boolean surveyFoundChange(OpenMetadataRootElement newerSurveyReport,
                                      OpenMetadataRootElement olderSurveyReport) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        String comparisonKey = newerSurveyReport.getElementHeader().getGUID() + "/" + olderSurveyReport.getElementHeader().getGUID();

        Boolean foundChange = surveyReportComparisons.get(comparisonKey);

        if (foundChange == null)
        {
            String newerSurveyReportGUID = newerSurveyReport.getElementHeader().getGUID();
            String olderSurveyReportGUID = olderSurveyReport.getElementHeader().getGUID();

            AnnotationClient annotationClient = integrationContext.getAnnotationClient();

            if (annotationClient.countReportedAnnotations(newerSurveyReportGUID, annotationClient.getQueryOptions()) !=
                    annotationClient.countReportedAnnotations(olderSurveyReportGUID, annotationClient.getQueryOptions()))
            {
                foundChange = true;
            }
            else
            {
                foundChange = ! getReportedAnnotationGUIDs(newerSurveyReportGUID).equals(getReportedAnnotationGUIDs(olderSurveyReportGUID));
            }

            surveyReportComparisons.put(comparisonKey, foundChange);
        }

        return foundChange;
    }


    /**
     * Return the unique identifiers of the annotations reported by a survey report.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @return set of annotation GUIDs - may be empty but not null
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private Set<String> getReportedAnnotationGUIDs(String surveyReportGUID) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        Set<String> annotationGUIDs = new HashSet<>();

        for (OpenMetadataRootElement annotation : getReportedAnnotations(surveyReportGUID))
        {
            annotationGUIDs.add(annotation.getElementHeader().getGUID());
        }

        return annotationGUIDs;
    }


    /**
     * Return the number of different types of annotation reported by a completed survey report.  The result is
     * cached because a completed report does not change.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @return number of annotation types
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private int getReportedAnnotationTypeCount(String surveyReportGUID) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        Integer annotationTypeCount = surveyReportAnnotationTypeCounts.get(surveyReportGUID);

        if (annotationTypeCount == null)
        {
            Set<String> annotationTypeNames = new HashSet<>();

            for (OpenMetadataRootElement annotation : getReportedAnnotations(surveyReportGUID))
            {
                if (annotation.getElementHeader().getType() != null)
                {
                    annotationTypeNames.add(annotation.getElementHeader().getType().getTypeName());
                }
            }

            annotationTypeCount = annotationTypeNames.size();

            surveyReportAnnotationTypeCounts.put(surveyReportGUID, annotationTypeCount);
        }

        return annotationTypeCount;
    }


    /**
     * Return the annotations reported by a survey report.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @return annotations - may be empty but not null
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    private List<OpenMetadataRootElement> getReportedAnnotations(String surveyReportGUID) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        List<OpenMetadataRootElement> reportedAnnotations = new ArrayList<>();

        AnnotationClient annotationClient = integrationContext.getAnnotationClient();
        int              startFrom        = 0;

        QueryOptions queryOptions = annotationClient.getQueryOptions(startFrom, annotationClient.getMaxPagingSize());
        queryOptions.setGraphQueryDepth(0);

        List<OpenMetadataRootElement> annotations = annotationClient.getNewAnnotations(surveyReportGUID, queryOptions);

        while ((annotations != null) && (! annotations.isEmpty()))
        {
            for (OpenMetadataRootElement annotation : annotations)
            {
                if ((annotation != null) && (annotation.getElementHeader() != null))
                {
                    reportedAnnotations.add(annotation);
                }
            }

            startFrom = startFrom + annotationClient.getMaxPagingSize();

            queryOptions = annotationClient.getQueryOptions(startFrom, annotationClient.getMaxPagingSize());
            queryOptions.setGraphQueryDepth(0);

            annotations = annotationClient.getNewAnnotations(surveyReportGUID, queryOptions);
        }

        return reportedAnnotations;
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
                                                                                      Collections.singletonList(this.getCatalogTargetElementHeader().getGUID()),
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

        QueryOptions queryOptions = assetClient.getQueryOptions(startFrom, assetClient.getMaxPagingSize());

        queryOptions.setGraphQueryDepth(0); // Only the catalog targets' GUIDs are used.

        List<OpenMetadataRootElement> catalogTargets = assetClient.getCatalogTargets(integrationConnectorGUID, queryOptions);

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
            queryOptions.setStartFrom(startFrom);
            catalogTargets = assetClient.getCatalogTargets(integrationConnectorGUID, queryOptions);
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

        QueryOptions queryOptions = assetClient.getQueryOptions(startFrom, assetClient.getMaxPagingSize());

        queryOptions.setGraphQueryDepth(0); // Only the engine actions' properties are used.

        List<OpenMetadataRootElement> engineActions = assetClient.getActionsForActionTarget(assetGUID,
                                                                                            liveActivityStatuses,
                                                                                            queryOptions);

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
            queryOptions.setStartFrom(startFrom);
            engineActions = assetClient.getActionsForActionTarget(assetGUID,
                                                                  liveActivityStatuses,
                                                                  queryOptions);
        }

        return runningActionTypeGUIDs;
    }
}
