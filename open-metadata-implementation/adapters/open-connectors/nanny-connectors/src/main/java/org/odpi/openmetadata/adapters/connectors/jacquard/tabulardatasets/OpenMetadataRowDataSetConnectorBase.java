/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataAuditCode;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationshipSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SequencingOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenMetadataRowDataSetConnectorBase is the base class for tabular data sets whose rows are not one element
 * each.  A row may be built from an element together with values the connector works out for itself - the
 * survey report an annotation was published in, the team a department reports to, a measurement from an
 * annotation's map of measurements - or from a relationship and the elements at its ends.
 * <br><br>
 * The subclass gathers its rows in {@link #collectRows()} by calling {@link #addRow}.  The whole data set is
 * rebuilt on each refresh, which keeps the rows consistent with each other: these data sets join several
 * queries together, so a row-by-row refresh of the kind the single-type data sets use would leave rows built
 * from different moments in the repository's history.
 */
public abstract class OpenMetadataRowDataSetConnectorBase extends OpenMetadataDataSetConnectorBase
{
    private List<List<String>> records = new ArrayList<>();
    private List<List<String>> newRecords = null;


    /**
     * Constructor used to set up the name of this connector (supplied by the subclasses).
     *
     * @param connectorName name of the connector
     * @param productDefinition definition of the product that this connector serves
     */
    public OpenMetadataRowDataSetConnectorBase(String            connectorName,
                                               ProductDefinition productDefinition)
    {
        super(connectorName, productDefinition);
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException  the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws UserNotAuthorizedException,
                               ConnectorCheckedException
    {
        super.start();

        refreshCache();
    }


    /**
     * Rebuild the rows of the data set.
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    @Override
    public synchronized void refreshCache() throws ConnectorCheckedException
    {
        final String methodName = "refreshCache";

        newRecords = new ArrayList<>();

        try
        {
            collectRows();

            records = newRecords;
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            super.logRecord(methodName, TabularDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName,
                                                                                                       error.getMessage()));

            throw new ConnectorCheckedException(TabularDataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
        finally
        {
            newRecords = null;
        }
    }


    /**
     * Gather the rows of the data set by calling addRow for each one.
     *
     * @throws Exception any problem retrieving the metadata - it is logged and returned as a ConnectorCheckedException
     */
    protected abstract void collectRows() throws Exception;


    /**
     * Add a row to the data set being built.  Each column is filled from, in order: the supplied values (keyed
     * by the column's display name, so a null value there is an explicit null), the element's header, the
     * element's properties.  A column that none of these can fill is null.
     *
     * @param elementHeader header of the element that the row describes (may be null)
     * @param properties properties of the element that the row describes (may be null)
     * @param suppliedValues values worked out by the connector, keyed by ProductDataFieldDefinition display name
     * @throws ConnectorCheckedException a column that is part of the unique identifier has no value
     */
    protected void addRow(ElementHeader              elementHeader,
                          OpenMetadataRootProperties properties,
                          Map<String, String>        suppliedValues) throws ConnectorCheckedException
    {
        final String methodName = "addRow";

        List<String> recordValues = new ArrayList<>();

        for (TabularColumnDescription column : this.getColumnDescriptions())
        {
            String columnName = column.columnName();
            int    before     = recordValues.size();

            if ((suppliedValues != null) && (suppliedValues.containsKey(columnName)))
            {
                recordValues.add(suppliedValues.get(columnName));
            }
            else if ((elementHeader == null) || (! getElementHeaderRecordValue(elementHeader, columnName, recordValues)))
            {
                if ((properties == null) || (! getElementRecordValue(properties, columnName, recordValues)))
                {
                    recordValues.add(null);
                }
            }

            /*
             * Each column must contribute exactly one value or every later column is shifted.
             */
            while (recordValues.size() > before + 1)
            {
                recordValues.remove(recordValues.size() - 1);
            }

            if ((! column.isNullable()) && (recordValues.get(before) == null))
            {
                throw new ConnectorCheckedException(TabularDataErrorCode.UNMAPPED_COLUMN.getMessageDefinition(connectorName, columnName),
                                                    this.getClass().getName(),
                                                    methodName);
            }
        }

        newRecords.add(recordValues);
    }


    /**
     * Add a row for an element.
     *
     * @param element element that the row describes
     * @param suppliedValues values worked out by the connector, keyed by ProductDataFieldDefinition display name
     * @throws ConnectorCheckedException a column that is part of the unique identifier has no value
     */
    protected void addRow(OpenMetadataRootElement element,
                          Map<String, String>     suppliedValues) throws ConnectorCheckedException
    {
        addRow(element.getElementHeader(), element.getProperties(), suppliedValues);
    }


    /**
     * Start a map of supplied values.
     *
     * @return empty map
     */
    protected Map<String, String> newValues()
    {
        return new HashMap<>();
    }


    /**
     * Add a value to the supplied values.
     *
     * @param values map of values
     * @param field field that the value belongs to
     * @param value value (may be null, which forces the column to be null)
     */
    protected void putValue(Map<String, String>        values,
                            ProductDataFieldDefinition field,
                            Object                     value)
    {
        if (value == null)
        {
            values.put(field.getDisplayName(), null);
        }
        else if (value instanceof Date date)
        {
            values.put(field.getDisplayName(), date.toInstant().toString());
        }
        else
        {
            values.put(field.getDisplayName(), value.toString());
        }
    }


    /**
     * Retrieve every element of a type (and its subtypes).
     *
     * @param typeName name of the type
     * @param graphQueryDepth how many levels of related elements to return with each element
     * @return list of elements, oldest first
     * @throws Exception problem retrieving the elements
     */
    protected List<OpenMetadataRootElement> getAllElements(String typeName,
                                                           int    graphQueryDepth) throws Exception
    {
        return getAllElements(typeName, graphQueryDepth, null);
    }


    /**
     * Retrieve every element of a type (and its subtypes), with only the named relationships.
     *
     * @param typeName name of the type
     * @param graphQueryDepth how many levels of related elements to return with each element
     * @param includeOnlyRelationships relationship types to return with each element (null for all)
     * @return list of elements, oldest first
     * @throws Exception problem retrieving the elements
     */
    protected List<OpenMetadataRootElement> getAllElements(String       typeName,
                                                           int          graphQueryDepth,
                                                           List<String> includeOnlyRelationships) throws Exception
    {
        List<OpenMetadataRootElement> results = new ArrayList<>();

        int pageSize = getPageSize();

        ClassificationExplorerClient client = connectorContext.getClassificationExplorerClient(typeName);

        SearchOptions searchOptions = client.getSearchOptions(0, pageSize);

        searchOptions.setGraphQueryDepth(graphQueryDepth);
        searchOptions.setIncludeOnlyRelationships(includeOnlyRelationships);
        searchOptions.setSequencingOrder(SequencingOrder.CREATION_DATE_OLDEST);

        List<OpenMetadataRootElement> page = client.findRootElements(null, searchOptions);

        while (page != null)
        {
            for (OpenMetadataRootElement element : page)
            {
                if (element != null)
                {
                    results.add(element);
                }
            }

            if (page.size() < pageSize)
            {
                break;
            }

            searchOptions.setStartFrom(searchOptions.getStartFrom() + pageSize);
            page = client.findRootElements(null, searchOptions);
        }

        return results;
    }


    /**
     * Retrieve every relationship of a type (and its subtypes).
     *
     * @param relationshipTypeName name of the relationship type
     * @return list of relationships, oldest first
     * @throws Exception problem retrieving the relationships
     */
    protected List<MetadataRelationshipSummary> getAllRelationships(String relationshipTypeName) throws Exception
    {
        List<MetadataRelationshipSummary> results = new ArrayList<>();

        int pageSize = getPageSize();

        ClassificationExplorerClient client = connectorContext.getClassificationExplorerClient();

        SearchOptions searchOptions = client.getSearchOptions(0, pageSize);

        searchOptions.setSequencingOrder(SequencingOrder.CREATION_DATE_OLDEST);

        List<MetadataRelationshipSummary> page = client.getRelationshipsByType(relationshipTypeName, searchOptions);

        while (page != null)
        {
            for (MetadataRelationshipSummary relationship : page)
            {
                if (relationship != null)
                {
                    results.add(relationship);
                }
            }

            if (page.size() < pageSize)
            {
                break;
            }

            searchOptions.setStartFrom(searchOptions.getStartFrom() + pageSize);
            page = client.getRelationshipsByType(relationshipTypeName, searchOptions);
        }

        return results;
    }


    /**
     * Is the element of the named type, or one of its subtypes?
     *
     * @param elementHeader header of the element
     * @param typeName type to test for
     * @return boolean
     */
    protected boolean isTypeOf(ElementHeader elementHeader,
                               String        typeName)
    {
        if ((elementHeader == null) || (elementHeader.getType() == null))
        {
            return false;
        }

        if (typeName.equals(elementHeader.getType().getTypeName()))
        {
            return true;
        }

        return (elementHeader.getType().getSuperTypeNames() != null) && (elementHeader.getType().getSuperTypeNames().contains(typeName));
    }


    /**
     * Return the page size to use for retrieving metadata.
     *
     * @return page size
     */
    private int getPageSize()
    {
        int pageSize = connectorContext.getMaxPageSize();

        if (pageSize == 0)
        {
            return 100;
        }

        return pageSize;
    }


    /**
     * Return the record count in the data source.
     *
     * @return count
     */
    @Override
    public long getRecordCount()
    {
        return records.size();
    }


    /**
     * Return the requested data record.  The first record is record 0.
     *
     * @param rowNumber long
     * @return list of values (as strings) where each string is the value from a column.  The order is the same as the columns
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    @Override
    public List<String> readRecord(long rowNumber) throws ConnectorCheckedException
    {
        final String methodName = "readRecord";

        List<List<String>> currentRecords = records;

        if ((rowNumber >= 0) && (rowNumber < currentRecords.size()))
        {
            return new ArrayList<>(currentRecords.get((int) rowNumber));
        }

        throw new ConnectorCheckedException(TabularDataErrorCode.NULL_RECORD.getMessageDefinition(connectorName,
                                                                                                  Long.toString(rowNumber),
                                                                                                  Integer.toString(currentRecords.size())),
                                            this.getClass().getName(),
                                            methodName);
    }
}
