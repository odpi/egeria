/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;

/**
 * DataFileElement contains the properties and header for a data file retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataFileElement implements MetadataElement
{
    private ElementHeader      elementHeader = null;
    private DataFileProperties dataFileProperties = null;


    /**
     * Default constructor
     */
    public DataFileElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataFileElement(DataFileElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            dataFileProperties = template.getDataFileProperties();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties of the file.
     *
     * @return file properties
     */
    public DataFileProperties getDataFileProperties()
    {
        return dataFileProperties;
    }


    /**
     * Set up the data file properties.
     *
     * @param dataFileProperties file properties
     */
    public void setDataFileProperties(DataFileProperties dataFileProperties)
    {
        this.dataFileProperties = dataFileProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataFileElement{" +
                "elementHeader=" + elementHeader +
                ", dataFileProperties=" + dataFileProperties +
                '}';
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        DataFileElement that = (DataFileElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(dataFileProperties, that.dataFileProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, dataFileProperties);
    }
}
