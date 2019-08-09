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

    public static final String NODE_LABEL_PROCESS = "Process";
    public static final String NODE_LABEL_HOST = "Host";
    public static final String NODE_LABEL_TABLE = "Table";
    public static final String NODE_LABEL_COLUMN = "Column";
    public static final String NODE_LABEL_GLOSSARYTERM = "Glossary Term";

    public static final String EDGE_LABEL_COLUMN_TO_TABLE = "Column included in table";
    public static final String EDGE_LABEL_TABLE_TO_HOST = "Table included in host";

    //Edge can go in either direction
    public static final String EDGE_LABEL_COLUMN_AND_PROCESS = "Process associated with column";
    public static final String EDGE_LABEL_TABLE_AND_PROCESS = "Process associated with table";
    public static final String EDGE_LABEL_HOST_AND_PROCESS = "Process associated with host";

    public static final String EDGE_LABEL_ENTITY_TO_GLOSSARYTERM = "Entity is associated with business term";

    public static final String EDGE_LABEL_GLOSSARYTERM_TO_GLOSSARYTERM = "Synonym";


    /*
     *  Elements
     */

    public static final String PROPERTY_KEY_PREFIX_ElEMENT = "ve";

    public static final String PROPERTY_KEY_ENTITY_GUID = PROPERTY_KEY_PREFIX_ElEMENT + PROPERTY_NAME_GUID;
    public static final String PROPERTY_KEY_NAME_QUALIFIED_NAME = PROPERTY_KEY_PREFIX_ElEMENT + PROPERTY_NAME_QUALIFIED_NAME;
    public static final String PROPERTY_KEY_ENTITY_NAME = PROPERTY_KEY_PREFIX_ElEMENT + PROPERTY_NAME_NAME;

}
