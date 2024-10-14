/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Description of credentials for accessing a table.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TemporaryTableCredential
{
    private String table_id  = null;
    private String operation = null;


    /**
     * Constructor
     */
    public TemporaryTableCredential()
    {
    }

    /**
     * Return the unique identifier of the table.
     *
     * @return string
     */
    public String getTable_id()
    {
        return table_id;
    }


    /**
     * Set up the unique identifier of the table.
     *
     * @param table_id string
     */
    public void setTable_id(String table_id)
    {
        this.table_id = table_id;
    }


    /**
     * Return the operation permitted by the credential.
     *
     * @return text
     */
    public String getOperation()
    {
        return operation;
    }


    /**
     * Set up the operation permitted by the credential.
     *
     * @param operation text
     */
    public void setOperation(String operation)
    {
        this.operation = operation;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TemporaryTableCredential{" +
                "table_id='" + table_id + '\'' +
                ", operation=" + operation +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        TemporaryTableCredential that = (TemporaryTableCredential) objectToCompare;
        return Objects.equals(table_id, that.table_id) &&
                Objects.equals(operation, that.operation);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(table_id, operation);
    }
}
