/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.openlineage.util;

public class GraphConstants {

    /*
     *  Short names for core properties
     */

    public static final String PROPERTY_NAME_GUID = "guid";
    public static final String PROPERTY_NAME_QUALIFIED_NAME = "qualifiedName";
    public static final String PROPERTY_NAME_NAME = "name";
  
    public static final String NODE_LABEL_PROCESS = "process";
    public static final String NODE_LABEL_SUB_PROCESS = "subprocess";
    public static final String NODE_LABEL_TABLE = "table";
    public static final String NODE_LABEL_COLUMN = "column";
    public static final String NODE_LABEL_GLOSSARYTERM = "glossaryTerm";
    public static final String NODE_LABEL_CONDENSED = "condensedNode";

    public static final String EDGE_LABEL_INCLUDED_IN = "includedIn";

    //Edge can go in either direction
    public static final String EDGE_LABEL_COLUMN_AND_PROCESS = "processColumn";
    public static final String EDGE_LABEL_TABLE_AND_PROCESS = "processTable";

    public static final String EDGE_LABEL_SEMANTIC = "semantic-assignment";
    public static final String EDGE_LABEL_GLOSSARYTERM_TO_GLOSSARYTERM = "synonym";

    public static final String EDGE_LABEL_CONDENSED = "condensed";

    public static final String PROPERTY_KEY_PREFIX_ElEMENT = "ve";

    //Column
    public static final String PROPERTY_KEY_DISPLAY_NAME = "displayName";
    public static final String PROPERTY_KEY_GLOSSARY_TERM = "glossaryTerm";
    public static final String PROPERTY_KEY_HOST_DISPLAY_NAME = "displayname";
    public static final String PROPERTY_KEY_DATABASE_DISPLAY_NAME = "databaseDisplayname";
    public static final String PROPERTY_KEY_SCHEMA_DISPLAY_NAME = "schemaDisplayname";
    public static final String PROPERTY_KEY_TABLE_DISPLAY_NAME = "tableDisplayname";

    //Process
    public static final String PROPERTY_KEY_CREATE_TIME= "createTime";
    public static final String PROPERTY_KEY_UPDATE_TIME = "updateTime";
    public static final String PROPERTY_KEY_FORMULA= "formula";
    public static final String PROPERTY_KEY_PROCESS_DESCRIPTION_URI= "descriptionURI";
    public static final String PROPERTY_KEY_VERSION= "version";
    public static final String PROPERTY_KEY_PROCESS_TYPE = "processType";
    public static final String PROPERTY_KEY_PARENT_PROCESS_GUID = "parent.process.guid";

    public static final String PROPERTY_KEY_GLOSSARY = "glossary";


    public static final String PROPERTY_KEY_ENTITY_GUID = PROPERTY_KEY_PREFIX_ElEMENT + PROPERTY_NAME_GUID;
    public static final String PROPERTY_KEY_NAME_QUALIFIED_NAME = PROPERTY_KEY_PREFIX_ElEMENT + PROPERTY_NAME_QUALIFIED_NAME;
    public static final String PROPERTY_KEY_ENTITY_NAME = PROPERTY_KEY_PREFIX_ElEMENT + PROPERTY_NAME_NAME;

}
