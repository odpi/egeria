/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.datamanager.properties.SchemaTypeProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TabularColumnTypeElement contains the properties and header for a database column entity
 * retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TabularColumnTypeElement  implements MetadataElement, Serializable
{
    private static final long serialVersionUID = 1L;

    private ElementHeader                elementHeader = null;
    private SchemaTypeProperties         tabularColumnTypeProperties = null;

    /**
     * Default constructor
     */
    public TabularColumnTypeElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TabularColumnTypeElement(TabularColumnTypeElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
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
     * Return the properties for the tabular column's type.
     *
     * @return properties bean
     */
    public SchemaTypeProperties getTabularColumnTypeProperties()
    {
        return tabularColumnTypeProperties;
    }


    /**
     * Set up the properties for the tabular column's type.
     *
     * @param tabularColumnTypeProperties properties bean
     */
    public void setTabularColumnTypeProperties(SchemaTypeProperties tabularColumnTypeProperties)
    {
        this.tabularColumnTypeProperties = tabularColumnTypeProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TabularColumnTypeElement{" +
                "elementHeader=" + elementHeader +
                ", tabularColumnTypeProperties=" + tabularColumnTypeProperties +
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
        TabularColumnTypeElement that = (TabularColumnTypeElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(tabularColumnTypeProperties, that.tabularColumnTypeProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, tabularColumnTypeProperties);
    }
}
