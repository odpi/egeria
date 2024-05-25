/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.DataFlowProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * DataFlowElement contains the properties and header for a data flow relationship retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataFlowElement
{
    private ElementHeader      dataFlowHeader     = null;
    private DataFlowProperties dataFlowProperties = null;
    private ElementHeader      dataSupplier       = null;
    private ElementHeader      dataConsumer       = null;

    /**
     * Default constructor
     */
    public DataFlowElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataFlowElement(DataFlowElement template)
    {
        if (template != null)
        {
            dataFlowHeader = template.getDataFlowHeader();
            dataFlowProperties = template.getDataFlowProperties();
            dataSupplier = template.getDataSupplier();
            dataConsumer = template.getDataConsumer();
        }
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    public ElementHeader getDataFlowHeader()
    {
        return dataFlowHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param dataFlowHeader element header object
     */
    public void setDataFlowHeader(ElementHeader dataFlowHeader)
    {
        this.dataFlowHeader = dataFlowHeader;
    }


    /**
     * Return details of the relationship
     *
     * @return relationship properties
     */
    public DataFlowProperties getDataFlowProperties()
    {
        return dataFlowProperties;
    }


    /**
     * Set up relationship properties
     *
     * @param dataFlowProperties relationship properties
     */
    public void setDataFlowProperties(DataFlowProperties dataFlowProperties)
    {
        this.dataFlowProperties = dataFlowProperties;
    }


    /**
     * Return the element header associated with end 1 of the relationship.
     *
     * @return element header object
     */
    public ElementHeader getDataSupplier()
    {
        return dataSupplier;
    }


    /**
     * Set up the element header associated with end 1 of the relationship.
     *
     * @param dataSupplier element header object
     */
    public void setDataSupplier(ElementHeader dataSupplier)
    {
        this.dataSupplier = dataSupplier;
    }



    /**
     * Return the element header associated with end 2 of the relationship.
     *
     * @return element header object
     */
    public ElementHeader getDataConsumer()
    {
        return dataConsumer;
    }


    /**
     * Set up the element header associated with end 2 of the relationship.
     *
     * @param dataConsumer element header object
     */
    public void setDataConsumer(ElementHeader dataConsumer)
    {
        this.dataConsumer = dataConsumer;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataFlowElement{" +
                       "dataFlowHeader=" + dataFlowHeader +
                       ", dataFlowProperties=" + dataFlowProperties +
                       ", dataSupplier=" + dataSupplier +
                       ", dataConsumer=" + dataConsumer +
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
        DataFlowElement that = (DataFlowElement) objectToCompare;
        return Objects.equals(getDataFlowHeader(), that.getDataFlowHeader()) &&
                       Objects.equals(getDataFlowProperties(), that.getDataFlowProperties()) &&
                       Objects.equals(getDataSupplier(), that.getDataSupplier()) &&
                       Objects.equals(getDataConsumer(), that.getDataConsumer());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), dataFlowHeader, dataFlowProperties, dataSupplier, dataConsumer);
    }
}
