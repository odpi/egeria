/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;


import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Gets or Sets TableOperation
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum TableOperation
{
  
  UNKNOWN_TABLE_OPERATION("UNKNOWN_TABLE_OPERATION"),
  
  READ("READ"),
  
  READ_WRITE("READ_WRITE");

  private final String value;

  /**
   * Constructor
   *
   * @param value string value
   */
  TableOperation(String value) {
    this.value = value;
  }


  /**
   * Retrieve string value
   *
   * @return string
   */
  public String getValue() {
    return value;
  }


  /**
   * Standard toString method.
   *
   * @return print out of variables in a JSON-style
   */
  @Override
  public String toString() {
    return String.valueOf(value);
  }
}

