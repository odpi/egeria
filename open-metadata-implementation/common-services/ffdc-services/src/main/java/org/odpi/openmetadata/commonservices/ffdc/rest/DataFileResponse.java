/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataFileElement;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DataFileResponse is the response structure used on the OMAS REST API calls that return the properties
 * for a file.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataFileResponse extends FFDCResponseBase
{
    private DataFileElement dataFile = null;


    /**
     * Default constructor
     */
    public DataFileResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataFileResponse(DataFileResponse template)
    {
        super(template);

        if (template != null)
        {
            this.dataFile = template.getDataFile();
        }
    }


    /**
     * Return the file result.
     *
     * @return bean
     */
    public DataFileElement getDataFile()
    {
        return dataFile;
    }


    /**
     * Set up the file result.
     *
     * @param dataFile  bean
     */
    public void setDataFile(DataFileElement dataFile)
    {
        this.dataFile = dataFile;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataFileResponse{" +
                "dataFile=" + dataFile +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof DataFileResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DataFileResponse that = (DataFileResponse) objectToCompare;
        return Objects.equals(dataFile, that.dataFile);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(dataFile);
    }
}
