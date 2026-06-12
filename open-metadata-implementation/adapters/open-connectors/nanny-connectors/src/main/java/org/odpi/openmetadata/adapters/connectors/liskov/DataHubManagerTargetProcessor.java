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
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataDescriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataDictionaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DataHubProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;



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
            OpenMetadataRootElement catalogTargetElement = this.getCatalogTargetElement();

            if ((catalogTargetElement != null) &&
                    (propertyHelper.isTypeOf(catalogTargetElement.getElementHeader(), OpenMetadataType.DATA_HUB.typeName)) &&
                    (catalogTargetElement.getProperties() instanceof DataHubProperties dataHubProperties))
            {
                String dataHubGUID        = catalogTargetElement.getElementHeader().getGUID();
                String dataDictionaryGUID = null;

                if (catalogTargetElement.getDataDescription() != null)
                {
                    for (RelatedMetadataElementSummary dataDescription : catalogTargetElement.getDataDescription())
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

                OpenMetadataRootElement DataDictionary = collectionClient.getCollectionByGUID(dataDictionaryGUID, collectionClient.getGetOptions());

                if (DataDictionary != null)
                {
                    if (catalogTargetElement.getCollectionMembers() != null)
                    {
                        for (RelatedMetadataElementSummary member : catalogTargetElement.getCollectionMembers())
                        {
                            if ((member != null) && (member.getRelatedElement().getProperties() instanceof DataStoreProperties dataStoreProperties))
                            {
                                auditLog.logMessage(methodName, LiskovAuditCode.REFRESHING_DATA_HUB_STORE.getMessageDefinition(connectorName,
                                                                                                                               member.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                                                                                               dataStoreProperties.getDisplayName(),
                                                                                                                               member.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                               dataHubProperties.getDisplayName(),
                                                                                                                               dataHubGUID));

                                refreshDataStore(DataDictionary, member.getRelatedElement().getElementHeader().getGUID(), dataHubGUID);
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


    private void refreshDataStore(OpenMetadataRootElement dataDictionaryElement,
                                  String                  dataStoreGUID,
                                  String                  dataHubGUID)
    {
        String methodName = "refreshDataStore(" + dataStoreGUID + ")";

        try
        {
            AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.DATA_STORE.typeName);

            OpenMetadataRootElement dataStoreElement = assetClient.getAssetByGUID(dataStoreGUID, assetClient.getGetOptions());


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
}
