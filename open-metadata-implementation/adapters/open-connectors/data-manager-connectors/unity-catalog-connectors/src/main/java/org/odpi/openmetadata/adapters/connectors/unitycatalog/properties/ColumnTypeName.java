/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;


import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Name of column type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ColumnTypeName
{
  BOOLEAN("BOOLEAN"),

  BYTE("BYTE"),

  SHORT("SHORT"),

  INT("INT"),

  LONG("LONG"),

  FLOAT("FLOAT"),

  DOUBLE("DOUBLE"),

  DATE("DATE"),

  TIMESTAMP("TIMESTAMP"),

  TIMESTAMP_NTZ("TIMESTAMP_NTZ"),

  STRING("STRING"),

  BINARY("BINARY"),

  DECIMAL("DECIMAL"),

  INTERVAL("INTERVAL"),

  ARRAY("ARRAY"),

  STRUCT("STRUCT"),

  MAP("MAP"),

  CHAR("CHAR"),

  NULL("NULL"),

  USER_DEFINED_TYPE("USER_DEFINED_TYPE"),

  TABLE_TYPE("TABLE_TYPE");

  private final String value;


  /**
   * Constructor
   *
   * @param value string value
   */
  ColumnTypeName(String value)
  {
    this.value = value;
  }


  /**
   * Return the column type value
   *
   * @return string value
   */
  public String getValue()
  {
    return value;
  }


  /**
   * Standard toString method.
   *
   * @return print out of variables in a JSON-style
   */
  @Override
  public String toString()
  {
    return String.valueOf(value);
  }
}

