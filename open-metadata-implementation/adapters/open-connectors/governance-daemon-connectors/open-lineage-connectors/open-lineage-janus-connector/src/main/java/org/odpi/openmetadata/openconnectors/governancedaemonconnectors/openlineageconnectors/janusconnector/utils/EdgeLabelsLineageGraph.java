/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils;

public enum EdgeLabelsLineageGraph {
    SEMANTIC_ASSIGNMENT,
    PROCESS_PORT,
    PORT_DELEGATION,
    PORT_SCHEMA,
    ATTRIBUTE_FOR_SCHEMA,
    SCHEMA_TYPE,
    SCHEMA_ATTRIBUTE_TYPE,
    DATA_FLOW,
    NESTED_FILE,
    FOLDER_HIERARCHY,
    ASSET_TO_CONNECTION,
    ASSET_SCHEMA_TYPE,
    DATA_CONTENT_FOR_DATASET,
    INCLUDED_IN,
    DATA_FLOW_WITH_PROCESS
}