/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey;

import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;

/**
 * An annotation produced by a database survey, together with the names of the database resource that it
 * describes.  The names are those used by the database itself.  A database annotation has only the database
 * name set; a schema annotation adds the schema name; a table annotation adds the table name; and a column
 * annotation adds the column name.  {@link SurveyDatabaseElementResolver} uses the names to find the catalogued
 * element that the annotation is linked to.
 *
 * @param annotationProperties properties of the annotation
 * @param databaseName name of the database
 * @param schemaName name of the schema, or null for a database annotation
 * @param tableName name of the table or view, or null for a database or schema annotation
 * @param columnName name of the column, or null unless this is a column annotation
 */
public record DatabaseElementAnnotation(AnnotationProperties annotationProperties,
                                        String               databaseName,
                                        String               schemaName,
                                        String               tableName,
                                        String               columnName)
{
}
