/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.customization;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferCustomizations {

    public static final String INCLUDE_SCHEMA_NAMES = "includeSchemaNames";
    public static final String EXCLUDE_SCHEMA_NAMES = "excludeSchemaNames";
    public static final String INCLUDE_TABLE_NAMES = "includeTableNames";
    public static final String EXCLUDE_TABLE_NAMES = "excludeTableNames";
    public static final String INCLUDE_VIEW_NAMES = "includeViewNames";
    public static final String EXCLUDE_VIEW_NAMES = "excludeViewNames";
    public static final String INCLUDE_COLUMN_NAMES = "includeColumnNames";
    public static final String EXCLUDE_COLUMN_NAMES = "excludeColumnNames";

    public static final List<String> INCLUSION_AND_EXCLUSION_NAMES = Arrays.asList(INCLUDE_SCHEMA_NAMES,
            INCLUDE_TABLE_NAMES, INCLUDE_VIEW_NAMES, INCLUDE_COLUMN_NAMES, EXCLUDE_SCHEMA_NAMES, EXCLUDE_TABLE_NAMES,
            EXCLUDE_VIEW_NAMES, EXCLUDE_COLUMN_NAMES);
    private static final String DELIMITER = ", ";

    private final Map<String,List<String>> customizations = new HashMap<>();

    public TransferCustomizations(Map<String, Object> configurationProperties) {
        for(String customizationKey : TransferCustomizations.INCLUSION_AND_EXCLUSION_NAMES) {
            addCustomization(customizationKey, configurationProperties.get(customizationKey));
        }
    }

    /**
     * Determines if schema should be transferred
     *
     * @param schemaName the schema name
     * @return the boolean
     */
    public boolean shouldTransferSchema(String schemaName) {
        return shouldTransfer(schemaName, getCustomization(INCLUDE_SCHEMA_NAMES), getCustomization(EXCLUDE_SCHEMA_NAMES));
    }

    /**
     * Determines if table should be transferred
     *
     * @param tableName the table name
     * @return the boolean
     */
    public boolean shouldTransferTable(String tableName) {
        return shouldTransfer(tableName, getCustomization(INCLUDE_TABLE_NAMES), getCustomization(EXCLUDE_TABLE_NAMES));
    }

    /**
     * Determines if view should be transferred
     *
     * @param viewName the view name
     * @return the boolean
     */
    public boolean shouldTransferView(String viewName) {
        return shouldTransfer(viewName, getCustomization(INCLUDE_VIEW_NAMES), getCustomization(EXCLUDE_VIEW_NAMES));
    }

    /**
     * Determines if column should be transferred
     *
     * @param columnName the column name
     * @return the boolean
     */
    public boolean shouldTransferColumn(String columnName) {
        return shouldTransfer(columnName, getCustomization(INCLUDE_COLUMN_NAMES), getCustomization(EXCLUDE_COLUMN_NAMES));
    }

    /**
     * Gets excluded schemas, only if took into consideration (inclusion is not present).
     *
     * @return the excluded schemas
     */
    public String getExcludedSchemas() {
        if(!CollectionUtils.isEmpty(customizations.get(INCLUDE_SCHEMA_NAMES))) {
            return "";
        }
        return String.join(DELIMITER, customizations.get(EXCLUDE_SCHEMA_NAMES));
    }

    /**
     * Gets excluded tables, only if took into consideration (inclusion is not present).
     *
     * @return the excluded tables
     */
    public String getExcludedTables() {
        if(!CollectionUtils.isEmpty(customizations.get(INCLUDE_TABLE_NAMES))) {
            return "";
        }
        return String.join(DELIMITER, customizations.get(EXCLUDE_TABLE_NAMES));
    }

    /**
     * Gets excluded view, only if took into consideration (inclusion is not present).
     *
     * @return the excluded views
     */
    public String getExcludedViews() {
        if(!CollectionUtils.isEmpty(customizations.get(INCLUDE_VIEW_NAMES))) {
            return "";
        }
        return String.join(DELIMITER, customizations.get(EXCLUDE_VIEW_NAMES));
    }

    /**
     * Gets excluded columns, only if took into consideration (inclusion is not present).
     *
     * @return the excluded columns
     */
    public String getExcludedColumns() {
        if(!CollectionUtils.isEmpty(customizations.get(INCLUDE_COLUMN_NAMES))) {
            return "";
        }
        return String.join(DELIMITER, customizations.get(EXCLUDE_COLUMN_NAMES));
    }

    /**
     * Determines if object should be transferred. If it's present in the inclusions, the exclusions are ignored.
     *
     * @param objectName the object to be transferred
     * @param inclusions the list of objects to be included
     * @param exclusions the list of objects to be excluded
     * @return the boolean
     */
    private boolean shouldTransfer(String objectName, List<String> inclusions, List<String> exclusions) {
        if(CollectionUtils.isNotEmpty(inclusions)) {
            return inclusions.contains(objectName);
        }

        if(CollectionUtils.isNotEmpty(exclusions)) {
            return !exclusions.contains(objectName);
        }

        return true;
    }

    private List<String> getCustomization(String key) {
        return customizations.get(key);
    }

    private void addCustomization(String key, Object customization) {
        if(INCLUSION_AND_EXCLUSION_NAMES.contains(key)) {
            List<String> processedCustomization = processCustomization(customization);
            customizations.put(key, processedCustomization);
        }
    }

    private List<String> processCustomization(Object customization) {
        List<String> processedCustomization = new ArrayList<>();
        if (customization instanceof String) {
            processedCustomization.add((String)customization);
        } else if (customization instanceof List<?>) {
            List<?> intermediary = (List<?>) customization;
            for(Object intermediaryObject : intermediary) {
                if(intermediaryObject instanceof String) {
                    processedCustomization.add((String)intermediaryObject);
                }
            }
        }
        return processedCustomization;
    }

}
