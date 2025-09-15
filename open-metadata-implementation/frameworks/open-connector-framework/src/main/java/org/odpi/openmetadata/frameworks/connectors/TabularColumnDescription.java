/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

/**
 * Create a tabular column definition.  This can be extracted from the data, or used to set up a new data set.
 *
 * @param columnName name of the column
 * @param columnDataType type of the column
 * @param description description of the data in the column
 * @param isNullable is a null value allowed in this column?
 * @param isIdentifier is this field all or part of the table's unique identifier?
 */
public record TabularColumnDescription(String   columnName,
                                       DataType columnDataType,
                                       String   description,
                                       boolean  isNullable,
                                       boolean  isIdentifier)
{
}
