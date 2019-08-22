/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.util;

import java.util.*;

import static org.odpi.openmetadata.governanceservers.openlineage.util.Constants.*;
import static org.odpi.openmetadata.governanceservers.openlineage.util.Constants.NESTED_FILE;

public enum EdgeLabels {
    SCHEMA_ATTRIBUTE_TYPE,
    ATTRIBUTE_FOR_SCHEMA,
    GLOSSARY_TERM,
    SEMANTIC_ASSIGNMENT,
    DEPLOYED_DB_SCHEMA_TYPE,
    DATABASE;
}
