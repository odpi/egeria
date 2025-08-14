/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors;

/**
 * Create a tabular column definition.  This can be extracted from the data, or used to set up a new data set.
 *
 * @param columnName name of the column
 * @param columnDataType type of the column
 * @param description description of the data in hte column
 */
public record TabularColumnDescription(String columnName,
                                       String columnDataType,
                                       String description)
{
}
