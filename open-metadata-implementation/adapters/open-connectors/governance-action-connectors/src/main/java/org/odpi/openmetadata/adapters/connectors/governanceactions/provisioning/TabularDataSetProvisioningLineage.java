/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning;

import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TabularDataSetProvisioningLineage records the lineage of a copy made by a tabular data set provisioning service:
 * the data flows from the source data set, through the process that did the copying, into the destination data
 * set, and - where both data sets have their columns catalogued - the data mappings from each source column to the
 * destination column it was copied into.
 * <br><br>
 * The process that the lineage passes through is chosen as follows:
 * <ul>
 *     <li>If the caller named a top-level process, it is used - and created if it does not exist, optionally
 *     from a template.  Unless lineage is restricted to the top-level process, each run adds a child process
 *     beneath it, and the lineage passes through the child.</li>
 *     <li>Otherwise, if the engine action running the service was started by a governance action process - which
 *     is the case for the provisioning pipeline a digital subscription sets up - the lineage passes through the
 *     process instance that Egeria created for this run of it.  The instance is a transient embedded process
 *     governed by the governance action process, so it plays the part of the child process and no other is
 *     created.  If lineage is restricted to the top-level process, it passes through the governance action
 *     process itself.</li>
 *     <li>Otherwise the default top-level process for the service is used, as in the first case.</li>
 * </ul>
 * Deliveries are repeated - a subscription refreshes its destination whenever the product's data changes - so a
 * relationship that is already in place is not created again.
 */
public class TabularDataSetProvisioningLineage
{
    private final GovernanceActionContext governanceContext;
    private final String                  defaultTopLevelProcessName;
    private final PropertyHelper          propertyHelper = new PropertyHelper();

    private String  topLevelProcessName                  = null;
    private String  topLevelProcessTemplateQualifiedName = null;
    private String  informationSupplyChainQualifiedName  = null;
    private boolean childProcessLineage                  = true;
    private boolean columnLevelLineage                   = true;


    /**
     * The outcome of recording the lineage of one delivery.
     *
     * @param processGUID unique identifier of the process the lineage passes through
     * @param columnMappingCount number of column-level data mappings in place for the delivered tables
     */
    public record LineageSummary(String processGUID,
                                 int    columnMappingCount)
    {
    }


    /**
     * Constructor.
     *
     * @param governanceContext context of the calling governance action service
     * @param defaultTopLevelProcessName qualified name of the top-level process to use when neither the caller nor
     *                                   the engine action supply one
     */
    public TabularDataSetProvisioningLineage(GovernanceActionContext governanceContext,
                                             String                  defaultTopLevelProcessName)
    {
        this.governanceContext          = governanceContext;
        this.defaultTopLevelProcessName = defaultTopLevelProcessName;
    }


    /**
     * Set the qualified name of the top-level process that the caller asked for.
     *
     * @param topLevelProcessName qualified name
     */
    public void setTopLevelProcessName(String topLevelProcessName)
    {
        this.topLevelProcessName = topLevelProcessName;
    }


    /**
     * Set the qualified name of the template to create the top-level process from, if it does not exist.
     *
     * @param topLevelProcessTemplateQualifiedName qualified name of a process template
     */
    public void setTopLevelProcessTemplateQualifiedName(String topLevelProcessTemplateQualifiedName)
    {
        this.topLevelProcessTemplateQualifiedName = topLevelProcessTemplateQualifiedName;
    }


    /**
     * Set the qualified name of the information supply chain that the lineage belongs to.  If none is set, the
     * one recorded on the engine action is used.
     *
     * @param informationSupplyChainQualifiedName qualified name
     */
    public void setInformationSupplyChainQualifiedName(String informationSupplyChainQualifiedName)
    {
        this.informationSupplyChainQualifiedName = informationSupplyChainQualifiedName;
    }


    /**
     * Set whether each run adds a child process beneath the top-level process.
     *
     * @param childProcessLineage flag
     */
    public void setChildProcessLineage(boolean childProcessLineage)
    {
        this.childProcessLineage = childProcessLineage;
    }


    /**
     * Set whether the columns of the source and destination are mapped to one another.
     *
     * @param columnLevelLineage flag
     */
    public void setColumnLevelLineage(boolean columnLevelLineage)
    {
        this.columnLevelLineage = columnLevelLineage;
    }


    /**
     * Record the lineage for a delivery from the source data set to the destination data set.
     *
     * @param sourceAssetGUID unique identifier of the source data set
     * @param destinationAssetGUID unique identifier of the destination data set
     * @param runIdentifier identifier of this run, used to name its child process
     * @param deliveredTables the tables delivered, in canonical form: source table name mapped to the name of the
     *                        destination table it was copied into
     * @return summary of the lineage recorded
     * @throws InvalidParameterException one of the parameters passed to open metadata is invalid
     * @throws UserNotAuthorizedException the service is not authorized to create lineage
     * @throws PropertyServerException a problem with the metadata store
     */
    public LineageSummary createLineage(String              sourceAssetGUID,
                                        String              destinationAssetGUID,
                                        String              runIdentifier,
                                        Map<String, String> deliveredTables) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        OpenMetadataStore metadataStore = governanceContext.getOpenMetadataStore();

        metadataStore.setForLineage(true);

        try
        {
            OpenMetadataElement engineAction = this.getEngineAction(metadataStore);
            String              iscQualifiedName = this.getInformationSupplyChain(engineAction);
            String              processGUID = this.getProcessGUID(metadataStore, engineAction, runIdentifier);

            this.createLineageRelationshipIfMissing(metadataStore,
                                                    OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                    sourceAssetGUID,
                                                    processGUID,
                                                    iscQualifiedName,
                                                    null);
            this.createLineageRelationshipIfMissing(metadataStore,
                                                    OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                    processGUID,
                                                    destinationAssetGUID,
                                                    iscQualifiedName,
                                                    null);

            int columnMappingCount = 0;

            if ((columnLevelLineage) && (deliveredTables != null) && (! deliveredTables.isEmpty()))
            {
                columnMappingCount = this.createColumnLineage(metadataStore,
                                                              sourceAssetGUID,
                                                              destinationAssetGUID,
                                                              deliveredTables,
                                                              iscQualifiedName);
            }

            return new LineageSummary(processGUID, columnMappingCount);
        }
        finally
        {
            metadataStore.setForLineage(false);
        }
    }


    /**
     * Retrieve the engine action that is running the calling service.
     *
     * @param metadataStore client
     * @return engine action, or null if it cannot be found
     * @throws InvalidParameterException invalid parameter
     * @throws UserNotAuthorizedException not authorized
     * @throws PropertyServerException problem with the metadata store
     */
    private OpenMetadataElement getEngineAction(OpenMetadataStore metadataStore) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        if (governanceContext.getEngineActionGUID() != null)
        {
            return metadataStore.getMetadataElementByGUID(governanceContext.getEngineActionGUID());
        }

        return null;
    }


    /**
     * Return the information supply chain to record on the lineage relationships: the one the caller supplied,
     * or else the one the engine action was started for.
     *
     * @param engineAction engine action running the service (may be null)
     * @return qualified name or null
     */
    private String getInformationSupplyChain(OpenMetadataElement engineAction)
    {
        final String methodName = "getInformationSupplyChain";

        if ((informationSupplyChainQualifiedName == null) && (engineAction != null))
        {
            return propertyHelper.getStringProperty(defaultTopLevelProcessName,
                                                    OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                    engineAction.getElementProperties(),
                                                    methodName);
        }

        return informationSupplyChainQualifiedName;
    }


    /**
     * Return the process that the lineage passes through - see the class description for how it is chosen.
     *
     * @param metadataStore client
     * @param engineAction engine action running the service (may be null)
     * @param runIdentifier identifier of this run
     * @return unique identifier of the process
     * @throws InvalidParameterException invalid parameter
     * @throws UserNotAuthorizedException not authorized
     * @throws PropertyServerException problem with the metadata store
     */
    private String getProcessGUID(OpenMetadataStore   metadataStore,
                                  OpenMetadataElement engineAction,
                                  String              runIdentifier) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "getProcessGUID";

        if ((topLevelProcessName == null) && (engineAction != null))
        {
            String initiatingProcessName = propertyHelper.getStringProperty(defaultTopLevelProcessName,
                                                                            OpenMetadataProperty.PROCESS_NAME.name,
                                                                            engineAction.getElementProperties(),
                                                                            methodName);

            if (initiatingProcessName != null)
            {
                OpenMetadataElement initiatingProcess = metadataStore.getMetadataElementByUniqueName(initiatingProcessName, null);

                if (initiatingProcess != null)
                {
                    if ((! childProcessLineage)
                            && (propertyHelper.isTypeOf(initiatingProcess, OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName)))
                    {
                        String governingProcessGUID = this.getGoverningProcessGUID(metadataStore, initiatingProcess.getElementGUID());

                        if (governingProcessGUID != null)
                        {
                            return governingProcessGUID;
                        }
                    }

                    return initiatingProcess.getElementGUID();
                }
            }
        }

        String processName = topLevelProcessName;

        if (processName == null)
        {
            processName = defaultTopLevelProcessName;
        }

        String topLevelProcessGUID = metadataStore.getMetadataElementGUIDByUniqueName(processName, null);

        if (topLevelProcessGUID == null)
        {
            String templateGUID = null;

            if (topLevelProcessTemplateQualifiedName != null)
            {
                templateGUID = metadataStore.getMetadataElementGUIDByUniqueName(topLevelProcessTemplateQualifiedName, null);
            }

            if (templateGUID == null)
            {
                topLevelProcessGUID = governanceContext.createProcess(OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                                                                      processName,
                                                                      processName,
                                                                      null);
            }
            else
            {
                topLevelProcessGUID = governanceContext.createProcessFromTemplate(templateGUID,
                                                                                  processName,
                                                                                  processName,
                                                                                  null);
            }
        }

        if (childProcessLineage)
        {
            return governanceContext.createChildProcess(OpenMetadataType.TRANSIENT_EMBEDDED_PROCESS.typeName,
                                                        processName + "::" + runIdentifier,
                                                        processName,
                                                        null,
                                                        topLevelProcessGUID);
        }

        return topLevelProcessGUID;
    }


    /**
     * Return the governance action process that a process instance is a run of.  Egeria links each instance to
     * its process with a GovernedBy relationship when it starts the run.
     *
     * @param metadataStore client
     * @param processInstanceGUID unique identifier of the process instance
     * @return unique identifier of the governance action process, or null if it is not linked
     * @throws InvalidParameterException invalid parameter
     * @throws UserNotAuthorizedException not authorized
     * @throws PropertyServerException problem with the metadata store
     */
    private String getGoverningProcessGUID(OpenMetadataStore metadataStore,
                                           String            processInstanceGUID) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        RelatedMetadataElementList governedBy = metadataStore.getRelatedMetadataElements(processInstanceGUID,
                                                                                         0,
                                                                                         OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                                                         0,
                                                                                         governanceContext.getMaxPageSize());

        if ((governedBy != null) && (governedBy.getElementList() != null))
        {
            for (RelatedMetadataElement relatedMetadataElement : governedBy.getElementList())
            {
                if ((relatedMetadataElement != null)
                        && (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName)))
                {
                    return relatedMetadataElement.getElement().getElementGUID();
                }
            }
        }

        return null;
    }


    /**
     * Map each column of each delivered source table to the destination column of the same name.  The columns
     * come from the schemas catalogued for the two data sets; if either has no schema there is nothing to map.
     *
     * @param metadataStore client
     * @param sourceAssetGUID source data set
     * @param destinationAssetGUID destination data set
     * @param deliveredTables source table name mapped to destination table name
     * @param iscQualifiedName information supply chain
     * @return number of column mappings in place for the delivered tables
     * @throws InvalidParameterException invalid parameter
     * @throws UserNotAuthorizedException not authorized
     * @throws PropertyServerException problem with the metadata store
     */
    private int createColumnLineage(OpenMetadataStore   metadataStore,
                                    String              sourceAssetGUID,
                                    String              destinationAssetGUID,
                                    Map<String, String> deliveredTables,
                                    String              iscQualifiedName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        Map<String, Map<String, String>> sourceColumns      = this.getColumns(metadataStore, sourceAssetGUID);
        Map<String, Map<String, String>> destinationColumns = this.getColumns(metadataStore, destinationAssetGUID);

        if ((sourceColumns.isEmpty()) || (destinationColumns.isEmpty()))
        {
            return 0;
        }

        int columnMappingCount = 0;

        for (String sourceTableName : deliveredTables.keySet())
        {
            Map<String, String> sourceTableColumns      = this.getTableColumns(sourceColumns, sourceTableName);
            Map<String, String> destinationTableColumns = this.getTableColumns(destinationColumns, deliveredTables.get(sourceTableName));

            if ((sourceTableColumns != null) && (destinationTableColumns != null))
            {
                for (String columnName : sourceTableColumns.keySet())
                {
                    String destinationColumnGUID = destinationTableColumns.get(columnName);

                    if (destinationColumnGUID != null)
                    {
                        this.createLineageRelationshipIfMissing(metadataStore,
                                                                OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                                                                sourceTableColumns.get(columnName),
                                                                destinationColumnGUID,
                                                                iscQualifiedName,
                                                                "copy");
                        columnMappingCount++;
                    }
                }
            }
        }

        return columnMappingCount;
    }


    /**
     * Return the columns of one table.  A data set describing a single table has its columns directly beneath
     * its root schema type, and these are held under the empty table name - they are the columns of whichever
     * table the data set holds.
     *
     * @param columns columns of the data set, by normalized table name
     * @param tableName canonical name of the table
     * @return columns of the table by normalized name, or null
     */
    private Map<String, String> getTableColumns(Map<String, Map<String, String>> columns,
                                                String                           tableName)
    {
        Map<String, String> tableColumns = columns.get(this.normalize(tableName));

        if (tableColumns == null)
        {
            tableColumns = columns.get("");
        }

        return tableColumns;
    }


    /**
     * Return the columns catalogued for a data set, grouped by table.  The schema attributes directly beneath
     * the data set's root schema type are either its columns (a data set of one table) or its tables, whose
     * columns are nested beneath them (a collection of tables).
     *
     * @param metadataStore client
     * @param assetGUID data set
     * @return map of normalized table name to (map of normalized column name to column GUID); empty if the data
     * set has no schema
     * @throws InvalidParameterException invalid parameter
     * @throws UserNotAuthorizedException not authorized
     * @throws PropertyServerException problem with the metadata store
     */
    private Map<String, Map<String, String>> getColumns(OpenMetadataStore metadataStore,
                                                        String            assetGUID) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        Map<String, Map<String, String>> columns = new HashMap<>();

        RelatedMetadataElement schemaType = metadataStore.getRelatedMetadataElement(assetGUID,
                                                                                    1,
                                                                                    OpenMetadataType.SCHEMA_RELATIONSHIP.typeName);

        if (schemaType != null)
        {
            for (OpenMetadataElement topLevelAttribute : this.getRelatedElements(metadataStore,
                                                                                  schemaType.getElement().getElementGUID(),
                                                                                  OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName))
            {
                List<OpenMetadataElement> nestedAttributes = this.getRelatedElements(metadataStore,
                                                                                      topLevelAttribute.getElementGUID(),
                                                                                      OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName);

                if (nestedAttributes.isEmpty())
                {
                    columns.computeIfAbsent("", key -> new HashMap<>()).put(this.getName(topLevelAttribute),
                                                                            topLevelAttribute.getElementGUID());
                }
                else
                {
                    Map<String, String> tableColumns = columns.computeIfAbsent(this.getName(topLevelAttribute), key -> new HashMap<>());

                    for (OpenMetadataElement column : nestedAttributes)
                    {
                        tableColumns.put(this.getName(column), column.getElementGUID());
                    }
                }
            }
        }

        return columns;
    }


    /**
     * Return every element related to the starting element through relationships of the requested type, where the
     * starting element is at end 1.
     *
     * @param metadataStore client
     * @param elementGUID starting element
     * @param relationshipTypeName relationship type
     * @return list of elements (may be empty)
     * @throws InvalidParameterException invalid parameter
     * @throws UserNotAuthorizedException not authorized
     * @throws PropertyServerException problem with the metadata store
     */
    private List<OpenMetadataElement> getRelatedElements(OpenMetadataStore metadataStore,
                                                         String            elementGUID,
                                                         String            relationshipTypeName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        List<OpenMetadataElement> elements = new ArrayList<>();

        for (RelatedMetadataElement relatedMetadataElement : this.getRelationships(metadataStore, elementGUID, relationshipTypeName))
        {
            elements.add(relatedMetadataElement.getElement());
        }

        return elements;
    }


    /**
     * Return every relationship of the requested type where the starting element is at end 1, a page at a time.
     *
     * @param metadataStore client
     * @param elementGUID starting element
     * @param relationshipTypeName relationship type
     * @return list of relationships with the element at end 2 (may be empty)
     * @throws InvalidParameterException invalid parameter
     * @throws UserNotAuthorizedException not authorized
     * @throws PropertyServerException problem with the metadata store
     */
    private List<RelatedMetadataElement> getRelationships(OpenMetadataStore metadataStore,
                                                          String            elementGUID,
                                                          String            relationshipTypeName) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        List<RelatedMetadataElement> relationships = new ArrayList<>();

        int pageSize  = governanceContext.getMaxPageSize();
        int startFrom = 0;

        RelatedMetadataElementList page = metadataStore.getRelatedMetadataElements(elementGUID, 1, relationshipTypeName, startFrom, pageSize);

        while ((page != null) && (page.getElementList() != null) && (! page.getElementList().isEmpty()))
        {
            for (RelatedMetadataElement relatedMetadataElement : page.getElementList())
            {
                if ((relatedMetadataElement != null) && (relatedMetadataElement.getElement() != null))
                {
                    relationships.add(relatedMetadataElement);
                }
            }

            if ((pageSize <= 0) || (page.getElementList().size() < pageSize))
            {
                break;
            }

            startFrom = startFrom + pageSize;
            page      = metadataStore.getRelatedMetadataElements(elementGUID, 1, relationshipTypeName, startFrom, pageSize);
        }

        return relationships;
    }


    /**
     * Create a lineage relationship unless one of the same type, between the same two elements and for the same
     * information supply chain, is already in place.  Lineage relationships may be repeated between two elements
     * - one per information supply chain - so the chain is part of the match.
     *
     * @param metadataStore client
     * @param relationshipTypeName DataFlow or DataMapping
     * @param sourceGUID element the data comes from
     * @param destinationGUID element the data goes to
     * @param iscQualifiedName information supply chain (may be null)
     * @param label label for the relationship (may be null)
     * @throws InvalidParameterException invalid parameter
     * @throws UserNotAuthorizedException not authorized
     * @throws PropertyServerException problem with the metadata store
     */
    private void createLineageRelationshipIfMissing(OpenMetadataStore metadataStore,
                                                    String            relationshipTypeName,
                                                    String            sourceGUID,
                                                    String            destinationGUID,
                                                    String            iscQualifiedName,
                                                    String            label) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "createLineageRelationshipIfMissing";

        for (RelatedMetadataElement existing : this.getRelationships(metadataStore, sourceGUID, relationshipTypeName))
        {
            if (destinationGUID.equals(existing.getElement().getElementGUID()))
            {
                String existingISC = propertyHelper.getStringProperty(defaultTopLevelProcessName,
                                                                      OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                      existing.getRelationshipProperties(),
                                                                      methodName);

                if ((iscQualifiedName == null) ? (existingISC == null) : iscQualifiedName.equals(existingISC))
                {
                    return;
                }
            }
        }

        governanceContext.createLineageRelationship(relationshipTypeName,
                                                    sourceGUID,
                                                    iscQualifiedName,
                                                    label,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    destinationGUID);
    }


    /**
     * Return the normalized name of a schema attribute - its display name, or its qualified name if it has none.
     *
     * @param element schema attribute
     * @return normalized name
     */
    private String getName(OpenMetadataElement element)
    {
        final String methodName = "getName";

        String name = propertyHelper.getStringProperty(defaultTopLevelProcessName,
                                                       OpenMetadataProperty.DISPLAY_NAME.name,
                                                       element.getElementProperties(),
                                                       methodName);

        if (name == null)
        {
            name = propertyHelper.getStringProperty(defaultTopLevelProcessName,
                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                    element.getElementProperties(),
                                                    methodName);
        }

        return this.normalize(name);
    }


    /**
     * Reduce a table or column name to lower-case letters and digits so that the same name matches whatever
     * convention each side uses: "Product Code", "product_code" and "ProductCode" are all "productcode".
     *
     * @param name name (may be null)
     * @return normalized name - empty for a null name
     */
    private String normalize(String name)
    {
        if (name == null)
        {
            return "";
        }

        return name.toLowerCase().replaceAll("[^a-z0-9]", "");
    }
}
