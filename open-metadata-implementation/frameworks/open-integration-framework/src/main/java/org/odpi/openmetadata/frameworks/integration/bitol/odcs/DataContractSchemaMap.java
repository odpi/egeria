/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class describes the key and value of a schema property whose logical type is map (ODCS v3.2.0, RFC 0030).  Both the key and the value follow the standard property shape.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContractSchemaMap
{
    private DataContractSchemaProperty key = null;
    private DataContractSchemaProperty value = null;


    /**
     * Default constructor
     */
    public DataContractSchemaMap()
    {
    }


    /**
     * Return the definition of the map key.
     *
     * @return DataContractSchemaProperty
     */
    public DataContractSchemaProperty getKey()
    {
        return key;
    }


    /**
     * Set up the definition of the map key.
     *
     * @param key DataContractSchemaProperty
     */
    public void setKey(DataContractSchemaProperty key)
    {
        this.key = key;
    }


    /**
     * Return the definition of the map value.
     *
     * @return DataContractSchemaProperty
     */
    public DataContractSchemaProperty getValue()
    {
        return value;
    }


    /**
     * Set up the definition of the map value.
     *
     * @param value DataContractSchemaProperty
     */
    public void setValue(DataContractSchemaProperty value)
    {
        this.value = value;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContractSchemaMap{" +
                       "key=" + key +
                       ", value=" + value +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        DataContractSchemaMap that = (DataContractSchemaMap) objectToCompare;
        return Objects.equals(key, that.key) &&
                       Objects.equals(value, that.value);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(key, value);
    }
}
