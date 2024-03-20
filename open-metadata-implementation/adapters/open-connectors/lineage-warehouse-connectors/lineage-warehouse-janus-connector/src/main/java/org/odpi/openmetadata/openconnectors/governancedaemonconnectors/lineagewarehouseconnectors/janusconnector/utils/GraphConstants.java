/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class GraphConstants {

    private GraphConstants() {
    }

    private static final String JAVA_STRING = "java.lang.String";

    public static final String PROPERTY_KEY_PREFIX_ELEMENT = "vertex--";
    public static final String PROPERTY_KEY_PREFIX_RELATIONSHIP = "edge--";
    public static final String PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY = "InstanceProp";
    public static final String PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY;

    public static final String PROPERTY_NAME_PORT_TYPE = PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY + "portType";
    public static final String PROPERTY_NAME_DISPLAY_NAME = "displayName";
    public static final String PROPERTY_NAME_INSTANCEPROP_DISPLAY_NAME = PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY + PROPERTY_NAME_DISPLAY_NAME;
    public static final String PROPERTY_NAME_NODE_ID = "nodeID";
    public static final String PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE = "condensedSource";
    public static final String PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION = "condensedDestination";
    public static final String PROPERTY_NAME_GUID = "guid";
    public static final String PROPERTY_NAME_QUALIFIED_NAME = "qualifiedName";
    public static final String PROPERTY_NAME_VERSION = "version";
    public static final String PROPERTY_NAME_CREATED_BY = "createdBy";
    public static final String PROPERTY_NAME_CREATE_TIME = "createTime";
    public static final String PROPERTY_NAME_UPDATED_BY = "updatedBy";
    public static final String PROPERTY_NAME_UPDATE_TIME = "updateTime";
    public static final String PROPERTY_NAME_LABEL = "label";
    public static final String PROPERTY_NAME_PROXY = "proxy";
    public static final String PROPERTY_NAME_GLOSSARY_TERM = "glossaryTerm";
    public static final String PROPERTY_NAME_HOST_DISPLAY_NAME = "displayName";
    public static final String PROPERTY_NAME_DATABASE_DISPLAY_NAME = "databaseDisplayName";
    public static final String PROPERTY_NAME_SCHEMA_DISPLAY_NAME = "databaseSchemaDisplayName";
    public static final String PROPERTY_NAME_TABLE_DISPLAY_NAME = "tableDisplayName";
    public static final String PROPERTY_NAME_SCHEMA_TYPE_DISPLAY_NAME = "schemaTypeDisplayName";
    public static final String PROPERTY_NAME_CONNECTION = "connectionDisplayName";
    public static final String PROPERTY_NAME_FORMULA = "formula";
    public static final String PROPERTY_NAMEPROCESS_DESCRIPTION_URI = "descriptionURI";
    public static final String PROPERTY_NAME_PROCESS_TYPE = "processType";
    public static final String PROPERTY_NAME_PARENT_PROCESS_GUID = "parent.process.guid";
    public static final String PROPERTY_NAME_GLOSSARY = "glossary";
    public static final String PROPERTY_NAME_METADATA_ID = "metadataCollectionId";
    public static final String PROPERTY_NAME_ASSET_LINEAGE_LAST_UPDATE_TIMESTAMP = "assetLineageLastUpdateTimestamp";
    private static final String PROPERTY_NAME_PATH = "path";
    public static final String PROPERTY_NAME_PROCESS_LINEAGE_COMPLETED_FLAG = "processLineageCompletedFlag";

    public static final String VARIABLE_NAME_ASSET_LINEAGE_LAST_UPDATE_TIME = "assetLineageLastUpdateTimestamp";

    public static final String CONDENSED_NODE_DISPLAY_NAME = "...";
    public static final String SOURCE_CONDENSATION = "source";
    public static final String DESTINATION_CONDENSATION = "destination";

    public static final String NODE_LABEL_CONDENSED = "condensedNode";
    public static final String NODE_LABEL_PROCESS = "process";
    public static final String NODE_LABEL_SUB_PROCESS = "subProcess";
    public static final List<String> PROCESS_NODES = Arrays.asList(NODE_LABEL_PROCESS, NODE_LABEL_SUB_PROCESS);

    // GLOSSARY TERM RELATED EDGE LABELS
    public static final String EDGE_LABEL_SEMANTIC_ASSIGNMENT = "SemanticAssignment";
    public static final String EDGE_LABEL_RELATED_TERM = "RelatedTerm";
    public static final String EDGE_LABEL_SYNONYM = "Synonym";
    public static final String EDGE_LABEL_ANTONYM = "Antonym";
    public static final String EDGE_LABEL_REPLACEMENT_TERM = "ReplacementTerm";
    public static final String EDGE_LABEL_TERM_ANCHOR = "TermAnchor";
    public static final String EDGE_LABEL_TRANSLATION = "Translation";
    public static final String EDGE_LABEL_IS_A_RELATIONSHIP = "ISARelationship";
    public static final String EDGE_LABEL_CONDENSED = "condensed";
    public static final String EDGE_LABEL_INCLUDED_IN = "includedIn";
    public static final String EDGE_LABEL_TERM_CATEGORIZATION = "TermCategorization";


    public static final String EDGE_LABEL_COLUMN_DATA_FLOW = "ColumnDataFlow";
    public static final String EDGE_LABEL_TABLE_DATA_FLOW = "TableDataFlow";

    // CLASSIFICATION RELATED EDGE LABELS
    public static final String EDGE_LABEL_CLASSIFICATION = "Classification";

    // SUB-PROCESS REAL PROCESS GUID
    public static final String PROPERTY_KEY_PROCESS_GUID = "processGuid";
    public static final String PROPERTY_KEY_COLUMN_IN_GUID = "columnInGuid";
    public static final String PROPERTY_KEY_COLUMN_OUT_GUID = "columnOutGuid";

    //EMBEDDED PROPERTIES

    public static final String PROPERTY_KEY_ADDITIONAL_PROPERTIES = "additionalProperties";
    public static final String PROPERTY_KEY_EXTENDED_PROPERTIES = "extendedProperties";


    public static final String PROPERTY_KEY_ENTITY_NODE_ID = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_NODE_ID;
    public static final String PROPERTY_KEY_ENTITY_GUID = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_GUID;
    public static final String PROPERTY_KEY_NAME_QUALIFIED_NAME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_QUALIFIED_NAME;
    public static final String PROPERTY_KEY_LABEL = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_LABEL;
    public static final String PROPERTY_KEY_PROXY = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_PROXY;
    public static final String PROPERTY_KEY_GLOSSARY_TERM = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_GLOSSARY_TERM;
    public static final String PROPERTY_KEY_DISPLAY_NAME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_DISPLAY_NAME;
    public static final String PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_INSTANCEPROP_DISPLAY_NAME;
    public static final String PROPERTY_KEY_METADATA_ID = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_METADATA_ID;
    public static final String PROPERTY_KEY_ASSET_LINEAGE_LAST_UPDATE_TIMESTAMP = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_ASSET_LINEAGE_LAST_UPDATE_TIMESTAMP;

    public static final String PROPERTY_KEY_ENTITY_VERSION = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_VERSION;
    public static final String PROPERTY_KEY_ENTITY_CREATED_BY = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_CREATED_BY;
    public static final String PROPERTY_KEY_ENTITY_CREATE_TIME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_CREATE_TIME;
    public static final String PROPERTY_KEY_ENTITY_UPDATED_BY = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_UPDATED_BY;
    public static final String PROPERTY_KEY_ENTITY_UPDATE_TIME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_UPDATE_TIME;
    public static final String PROPERTY_KEY_SCHEMA_DISPLAY_NAME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_SCHEMA_DISPLAY_NAME;
    public static final String PROPERTY_KEY_TABLE_DISPLAY_NAME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_TABLE_DISPLAY_NAME;
    public static final String PROPERTY_KEY_SCHEMA_TYPE_DISPLAY_NAME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_SCHEMA_TYPE_DISPLAY_NAME;
    public static final String PROPERTY_KEY_DATABASE_DISPLAY_NAME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_DATABASE_DISPLAY_NAME;
    public static final String PROPERTY_KEY_CONNECTION_NAME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_CONNECTION;
    public static final String PROPERTY_KEY_PATH = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_PATH;
    public static final String PROPERTY_KEY_PROCESS_LINEAGE_COMPLETED_FLAG = PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY + PROPERTY_NAME_PROCESS_LINEAGE_COMPLETED_FLAG;
    public static final String PROPERTY_KEY_INSTANCE_PROP_ADDITIONAL_PROPERTIES =
            PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY + PROPERTY_KEY_ADDITIONAL_PROPERTIES;

    public static final String PROPERTY_KEY_RELATIONSHIP_GUID = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_GUID;
    public static final String PROPERTY_KEY_RELATIONSHIP_VERSION = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_VERSION;
    public static final String PROPERTY_KEY_RELATIONSHIP_CREATED_BY = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_CREATED_BY;
    public static final String PROPERTY_KEY_RELATIONSHIP_CREATE_TIME = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_CREATE_TIME;
    public static final String PROPERTY_KEY_RELATIONSHIP_UPDATED_BY = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_UPDATED_BY;
    public static final String PROPERTY_KEY_RELATIONSHIP_UPDATE_TIME = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_UPDATE_TIME;
    public static final String PROPERTY_KEY_RELATIONSHIP_LABEL = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_LABEL;
    public static final String PROPERTY_KEY_RELATIONSHIP_DISPLAY_NAME = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_KEY_DISPLAY_NAME;
    public static final String PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME = PROPERTY_KEY_PREFIX_ELEMENT +
            PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY + PROPERTY_NAME_QUALIFIED_NAME;
    private static final HashSet<String> returnedPropertiesWhiteList = new HashSet<>();

    static {
        returnedPropertiesWhiteList.add(PROPERTY_KEY_ENTITY_GUID);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_DISPLAY_NAME);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_NAME_QUALIFIED_NAME);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_GLOSSARY_TERM);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_ENTITY_CREATED_BY);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_SCHEMA_DISPLAY_NAME);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_TABLE_DISPLAY_NAME);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_ENTITY_CREATE_TIME);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_ENTITY_UPDATED_BY);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_ENTITY_UPDATE_TIME);
    }

    public static final ImmutableSet<String> immutableReturnedPropertiesWhiteList = ImmutableSet.copyOf(returnedPropertiesWhiteList);

    private static final Map<String, String> corePropertyTypes = new HashMap<>();


    static {
        corePropertyTypes.put(PROPERTY_NAME_GUID, JAVA_STRING);
        corePropertyTypes.put(PROPERTY_NAME_VERSION, "java.lang.Long");
        corePropertyTypes.put(PROPERTY_NAME_CREATED_BY, JAVA_STRING);
        corePropertyTypes.put(PROPERTY_NAME_CREATE_TIME, "java.lang.Date");
        corePropertyTypes.put(PROPERTY_NAME_UPDATED_BY, JAVA_STRING);
        corePropertyTypes.put(PROPERTY_NAME_UPDATE_TIME, "java.lang.Date");
        corePropertyTypes.put(PROPERTY_NAME_LABEL, JAVA_STRING);
        corePropertyTypes.put(PROPERTY_NAME_PROXY, "java.lang.Boolean");
        corePropertyTypes.put(PROPERTY_NAME_NODE_ID, JAVA_STRING);
        corePropertyTypes.put(PROPERTY_NAME_METADATA_ID, JAVA_STRING);
        corePropertyTypes.put(Constants.ASSET_LINEAGE_VARIABLES, JAVA_STRING);
    }

    public static final ImmutableMap<String, String> immutableCorePropertyTypes = ImmutableMap.copyOf(corePropertyTypes);

}