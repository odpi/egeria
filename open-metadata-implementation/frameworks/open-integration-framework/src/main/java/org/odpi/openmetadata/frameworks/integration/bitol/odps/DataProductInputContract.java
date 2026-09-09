/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odps;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class identifies a data contract that an output port of an Open Data Product Standard (ODPS) data product
 * depends on.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataProductInputContract
{
    private String id = null;
    private String version = null;


    /**
     * Default constructor
     */
    public DataProductInputContract()
    {
    }


    /**
     * Return the identifier of the data contract.
     *
     * @return string contract identifier
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the identifier of the data contract.
     *
     * @param id string contract identifier
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the version of the data contract.
     *
     * @return string version
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the version of the data contract.
     *
     * @param version string version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataProductInputContract{" +
                       "id='" + id + '\'' +
                       ", version='" + version + '\'' +
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
        DataProductInputContract that = (DataProductInputContract) objectToCompare;
        return Objects.equals(id, that.id) &&
                       Objects.equals(version, that.version);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, version);
    }
}
