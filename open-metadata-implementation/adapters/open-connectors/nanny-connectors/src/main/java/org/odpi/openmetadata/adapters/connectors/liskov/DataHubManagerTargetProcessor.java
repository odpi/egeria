/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.liskov;


import org.odpi.openmetadata.adapters.connectors.liskov.ffdc.LiskovAuditCode;
import org.odpi.openmetadata.adapters.connectors.liskov.ffdc.LiskovErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataDescriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataDictionaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DataHubProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.Map;


/**
 * Calculates the last time an update was made to the tabular data set that is the target and if it has changes since
 * the last refresh (or this is the first refresh), the DataScope classification is updated with the latest update time.
 * This will be detected as a change to the catalog target by any monitoring process.
 */
public class DataHubManagerTargetProcessor extends CatalogTargetProcessorBase
{
    /**
     * Constructor
     *
     * @param catalogTarget catalog target information
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public DataHubManagerTargetProcessor(CatalogTarget            catalogTarget,
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
     * Check whether the data set has changed since the last refresh.  If it has then update the asset's
     * DataScope classification.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is unable to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected so stop refresh processing
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        super.refresh();

        try
        {
            /*
             * These are lookup tables for data fields and data structures found in the data hub.
             * They are used to match schema attributes to the data fields.
             */
            Map<String, String>     dataFields     = new HashMap<>();
            Map<String, String>     dataStructures = new HashMap<>();


            OpenMetadataRootElement dataHubElement = this.getCatalogTargetElement();

            /*
             * Notice that members that are not data hubs are skipped.
             */
            if ((dataHubElement != null) &&
                    (propertyHelper.isTypeOf(dataHubElement.getElementHeader(), OpenMetadataType.DATA_HUB.typeName)) &&
                    (dataHubElement.getProperties() instanceof DataHubProperties dataHubProperties))
            {
                String dataHubGUID        = dataHubElement.getElementHeader().getGUID();
                String dataDictionaryGUID = null;

                if (dataHubElement.getDataDescription() != null)
                {
                    for (RelatedMetadataElementSummary dataDescription : dataHubElement.getDataDescription())
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

                    dataDictionaryProperties.setQualifiedName(dataHubProperties.getQualifiedName() + "_DataDictionary");
                    dataDictionaryProperties.setDisplayName("Data dictionary for " + dataHubProperties.getDisplayName());

                    NewElementOptions newElementOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());

                    newElementOptions.setIsOwnAnchor(false);
                    newElementOptions.setAnchorGUID(dataHubGUID);
                    newElementOptions.setParentGUID(dataHubGUID);
                    newElementOptions.setParentAtEnd1(true);
                    newElementOptions.setParentRelationshipTypeName(OpenMetadataType.DATA_DESCRIPTION_RELATIONSHIP.typeName);

                    DataDescriptionProperties dataDescriptionProperties = new DataDescriptionProperties();

                    dataDescriptionProperties.setLabel("Data dictionary");
                    dataDescriptionProperties.setDescription("Details of the data fields found in the data hub.");

                    dataDictionaryGUID = collectionClient.createCollection(newElementOptions,
                                                                           null,
                                                                           dataDictionaryProperties,
                                                                           dataDescriptionProperties);

                    auditLog.logMessage(methodName, LiskovAuditCode.NEW_DATA_DICTIONARY.getMessageDefinition(connectorName,
                                                                                                             dataHubProperties.getDisplayName(),
                                                                                                             dataHubGUID));
                }

                OpenMetadataRootElement dataDictionary = collectionClient.getCollectionByGUID(dataDictionaryGUID, collectionClient.getGetOptions());

                if (dataDictionary != null)
                {
                    auditLog.logMessage(methodName, LiskovAuditCode.RETRIEVING_DATA_FIELDS.getMessageDefinition(connectorName,
                                                                                                                dataHubProperties.getDisplayName(),
                                                                                                                dataHubGUID));

                    /*
                     * Extract the existing data fields.
                     */
                    String dataFieldsFolderGUID = null;
                    String dataStructureFolderGUID = null;

                    if (dataDictionary.getCollectionMembers() != null)
                    {
                        for (RelatedMetadataElementSummary member : dataHubElement.getCollectionMembers())
                        {
                            if (member != null)
                            {
                                if (member.getRelatedElement().getProperties() instanceof CollectionFolderProperties collectionFolderProperties)
                                {
                                    if (collectionFolderProperties.getQualifiedName().equals(dataHubProperties.getQualifiedName() + "_DataFields"))
                                    {
                                        dataFieldsFolderGUID = member.getRelatedElement().getElementHeader().getGUID();
                                    }
                                    else if (collectionFolderProperties.getQualifiedName().equals(dataHubProperties.getQualifiedName() + "_DataStructures"))
                                    {
                                        dataStructureFolderGUID = member.getRelatedElement().getElementHeader().getGUID();
                                    }
                                }

                                retrieveDataDictionaryElements(member, dataFields, dataStructures);
                            }
                        }
                    }

                    boolean refreshDataDictionary = false;
                    if (dataFieldsFolderGUID == null)
                    {
                        CollectionFolderProperties collectionFolderProperties = new CollectionFolderProperties();

                        collectionFolderProperties.setQualifiedName(dataHubProperties.getQualifiedName() + "_DataFields");
                        collectionFolderProperties.setDisplayName("Data Fields");
                        collectionFolderProperties.setDescription("Data fields found in the data hub.");

                        NewElementOptions newElementOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());

                        newElementOptions.setIsOwnAnchor(false);
                        newElementOptions.setAnchorGUID(dataHubGUID);
                        newElementOptions.setParentGUID(dataDictionaryGUID);
                        newElementOptions.setParentAtEnd1(true);
                        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

                        dataFieldsFolderGUID = collectionClient.createCollection(newElementOptions,
                                                                               null,
                                                                               collectionFolderProperties,
                                                                               null);
                        refreshDataDictionary = true;
                    }


                    if (dataStructureFolderGUID == null)
                    {
                        CollectionFolderProperties collectionFolderProperties = new CollectionFolderProperties();

                        collectionFolderProperties.setQualifiedName(dataHubProperties.getQualifiedName() + "_DataStructures");
                        collectionFolderProperties.setDisplayName("Data Structures");
                        collectionFolderProperties.setDescription("Data structures found in the data hub.");

                        NewElementOptions newElementOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());

                        newElementOptions.setIsOwnAnchor(false);
                        newElementOptions.setAnchorGUID(dataHubGUID);
                        newElementOptions.setParentGUID(dataDictionaryGUID);
                        newElementOptions.setParentAtEnd1(true);
                        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

                        dataStructureFolderGUID = collectionClient.createCollection(newElementOptions,
                                                                                   null,
                                                                                    collectionFolderProperties,
                                                                                   null);
                        refreshDataDictionary = true;
                    }

                    if (refreshDataDictionary)
                    {
                        dataDictionary = collectionClient.getCollectionByGUID(dataDictionaryGUID, collectionClient.getGetOptions());
                    }

                    /*
                     * Process the members of the data hub
                     */
                    if (dataHubElement.getCollectionMembers() != null)
                    {
                        for (RelatedMetadataElementSummary member : dataHubElement.getCollectionMembers())
                        {
                            if ((member != null) && (member.getRelatedElement().getProperties() instanceof DataStoreProperties dataStoreProperties))
                            {
                                auditLog.logMessage(methodName, LiskovAuditCode.REFRESHING_DATA_HUB_STORE.getMessageDefinition(connectorName,
                                                                                                                               member.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                                                                                               dataStoreProperties.getDisplayName(),
                                                                                                                               member.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                               dataHubProperties.getDisplayName(),
                                                                                                                               dataHubGUID));

                                refreshDataStore(dataDictionary, dataFieldsFolderGUID, dataStructureFolderGUID, member.getRelatedElement().getElementHeader().getGUID(), dataHubGUID, dataFields, dataStructures);
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
     * Loads the existing content of the data dictionary into the supplied maps to use when refreshing each data store.
     *
     * @param member         retrieved member of the data dictionary
     * @param dataFields     map of data fields
     * @param dataStructures map of data structures
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user not authorized to issue this request
     */
    private void retrieveDataDictionaryElements(RelatedMetadataElementSummary member,
                                                Map<String, String> dataFields,
                                                Map<String, String> dataStructures) throws InvalidParameterException,
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
                dataStructures.put(dataDictionaryElementProperties.getQualifiedName(), member.getRelatedElement().getElementHeader().getGUID());
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
     * @param dataDictionaryElement       Root element of the data dictionary providing contextual information for the data store.
     * @param dataFieldsFolderGUID        Globally unique identifier (GUID) for the folder containing data fields.
     * @param dataStructuresFolderGUID    Globally unique identifier (GUID) for the folder containing data structures.
     * @param dataStoreGUID               Globally unique identifier (GUID) of the data store to be refreshed.
     * @param dataHubGUID                 Globally unique identifier (GUID) of the data hub associated with the data store.
     * @param dataFields                  Map of identifiers for data fields used during the refresh process.
     * @param dataStructures              Map of identifiers for data structures used during the refresh process.
     */
    private void refreshDataStore(OpenMetadataRootElement dataDictionaryElement,
                                  String                  dataFieldsFolderGUID,
                                  String                  dataStructuresFolderGUID,
                                  String                  dataStoreGUID,
                                  String                  dataHubGUID,
                                  Map<String, String>     dataFields,
                                  Map<String, String>     dataStructures)
    {
        String methodName = "refreshDataStore(" + dataStoreGUID + ")";

        try
        {
            AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.DATA_STORE.typeName);

            OpenMetadataRootElement dataStoreElement = assetClient.getAssetByGUID(dataStoreGUID, assetClient.getGetOptions());

            if ((dataStoreElement != null) && (dataStoreElement.getSchemaType() != null))
            {
                if (propertyHelper.isTypeOf(dataStoreElement.getElementHeader(), OpenMetadataType.FILE_FOLDER.typeName))
                {
                    refreshFileFolder(dataDictionaryElement, dataFieldsFolderGUID, dataStructuresFolderGUID, dataStoreElement, dataHubGUID, dataFields, dataStructures);
                }
                else if (propertyHelper.isTypeOf(dataStoreElement.getElementHeader(), OpenMetadataType.CSV_FILE.typeName))
                {
                    refreshCSVFile(dataDictionaryElement, dataFieldsFolderGUID, dataStructuresFolderGUID, dataStoreElement, dataHubGUID, dataFields, dataStructures);
                }
                else if (propertyHelper.isTypeOf(dataStoreElement.getElementHeader(), OpenMetadataType.DATABASE.typeName))
                {
                    refreshRelationalDatabase(dataDictionaryElement, dataFieldsFolderGUID, dataStructuresFolderGUID, dataStoreElement, dataHubGUID, dataFields, dataStructures);
                }
                else if (propertyHelper.isTypeOf(dataStoreElement.getElementHeader(), OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName))
                {
                    refreshRelationalDatabaseSchema(dataDictionaryElement, dataFieldsFolderGUID, dataStructuresFolderGUID, dataStoreElement, dataHubGUID, dataFields, dataStructures);
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
     * @param dataDictionaryElement       Root element of the data dictionary providing contextual information for the data store.
     * @param dataFieldsFolderGUID        Globally unique identifier (GUID) for the folder containing data fields.
     * @param dataStructuresFolderGUID    Globally unique identifier (GUID) for the folder containing data structures.
     * @param dataStoreElement            The data store to be refreshed.
     * @param dataHubGUID                 Globally unique identifier (GUID) of the data hub associated with the data store.
     * @param dataFields                  Map of identifiers for data fields used during the refresh process.
     * @param dataStructures              Map of identifiers for data structures used during the refresh process.
     */
    private void refreshFileFolder(OpenMetadataRootElement dataDictionaryElement,
                                   String                  dataFieldsFolderGUID,
                                   String                  dataStructuresFolderGUID,
                                   OpenMetadataRootElement dataStoreElement,
                                   String                  dataHubGUID,
                                   Map<String, String>     dataFields,
                                   Map<String, String>     dataStructures)
    {
        String methodName = "refreshFileFolder(" + dataStoreElement.getElementHeader().getGUID() + ")";
    }


    /**
     * Refreshes the metadata for a CSV File based on the provided parameters. This method extracts the data structure and
     * data fields in a CSV File asset.
     *
     * @param dataDictionaryElement       Root element of the data dictionary providing contextual information for the data store.
     * @param dataFieldsFolderGUID        Globally unique identifier (GUID) for the folder containing data fields.
     * @param dataStructuresFolderGUID    Globally unique identifier (GUID) for the folder containing data structures.
     * @param dataStoreElement            The data store to be refreshed.
     * @param dataHubGUID                 Globally unique identifier (GUID) of the data hub associated with the data store.
     * @param dataFields                  Map of identifiers for data fields used during the refresh process.
     * @param dataStructures              Map of identifiers for data structures used during the refresh process.
     */
    private void refreshCSVFile(OpenMetadataRootElement dataDictionaryElement,
                                String                  dataFieldsFolderGUID,
                                String                  dataStructuresFolderGUID,
                                OpenMetadataRootElement dataStoreElement,
                                String                  dataHubGUID,
                                Map<String, String>     dataFields,
                                Map<String, String>     dataStructures)
    {
        String methodName = "refreshCSVFile(" + dataStoreElement.getElementHeader().getGUID() + ")";
    }


    /**
     * Refreshes the metadata for a Relational Database based on the provided parameters. This method extracts the data structure and
     * data fields for each database schema attached to the data store.
     *
     * @param dataDictionaryElement    Root element of the data dictionary providing contextual information for the data store.
     * @param dataFieldsFolderGUID     Globally unique identifier (GUID) for the folder containing data fields.
     * @param dataStructuresFolderGUID Globally unique identifier (GUID) for the folder containing data structures.
     * @param dataStoreElement         The data store to be refreshed.
     * @param dataHubGUID              Globally unique identifier (GUID) of the data hub associated with the data store.
     * @param dataFields               Map of identifiers for data fields used during the refresh process.
     * @param dataStructures           Map of identifiers for data structures used during the refresh process.
     */
    private void refreshRelationalDatabase(OpenMetadataRootElement dataDictionaryElement,
                                           String                  dataFieldsFolderGUID,
                                           String                  dataStructuresFolderGUID,
                                           OpenMetadataRootElement dataStoreElement,
                                           String                  dataHubGUID,
                                           Map<String, String>     dataFields,
                                           Map<String, String>     dataStructures)
    {
        String methodName = "refreshRelationalDatabase(" + dataStoreElement.getElementHeader().getGUID() + ")";
    }



    /**
     * Refreshes the metadata for a Relational Database Schema based on the provided parameters. This method extracts the data structure and
     * data fields for each database table attached to the data store.
     *
     * @param dataDictionaryElement    Root element of the data dictionary providing contextual information for the data store.
     * @param dataFieldsFolderGUID     Globally unique identifier (GUID) for the folder containing data fields.
     * @param dataStructuresFolderGUID Globally unique identifier (GUID) for the folder containing data structures.
     * @param dataStoreElement         The data store to be refreshed.
     * @param dataHubGUID              Globally unique identifier (GUID) of the data hub associated with the data store.
     * @param dataFields               Map of identifiers for data fields used during the refresh process.
     * @param dataStructures           Map of identifiers for data structures used during the refresh process.
     */
    private void refreshRelationalDatabaseSchema(OpenMetadataRootElement dataDictionaryElement,
                                                 String                  dataFieldsFolderGUID,
                                                 String                  dataStructuresFolderGUID,
                                                 OpenMetadataRootElement dataStoreElement,
                                                 String                  dataHubGUID,
                                                 Map<String, String>     dataFields,
                                                 Map<String, String>     dataStructures)
    {
        String methodName = "refreshRelationalDatabaseSchema(" + dataStoreElement.getElementHeader().getGUID() + ")";


    }



    private String getDataField(String              dataHubGUID,
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
            return dataFieldGUID;
        }


        NewElementOptions newElementOptions = new NewElementOptions(dataFieldClient.getMetadataSourceOptions());
        //return dataFieldClient.createDataField(newElementOptions)

        return null;
    }
}
