/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.datamanager.properties.SchemaTypeProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.TabularColumnProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * TabularColumnElement contains the properties and header for a table column entity
 * retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TabularColumnElement implements MetadataElement, Serializable
{
    private static final long serialVersionUID = 1L;

    private ElementHeader           elementHeader           = null;
    private TabularColumnProperties tabularColumnProperties = null;

    /**
     * Default constructor
     */
    public TabularColumnElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TabularColumnElement(TabularColumnElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            tabularColumnProperties = template.getTabularColumnProperties();
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
     * Return the tabular column.
     *
     * @return properties bean
     */
    public TabularColumnProperties getTabularColumnProperties()
    {
        return tabularColumnProperties;
    }


    /**
     * Set up the tabular column.
     *
     * @param tabularColumnProperties properties bean
     */
    public void setTabularColumnProperties(TabularColumnProperties tabularColumnProperties)
    {
        this.tabularColumnProperties = tabularColumnProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TabularColumnElement{" +
                "elementHeader=" + elementHeader +
                ", tabularColumnProperties=" + tabularColumnProperties +
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
        TabularColumnElement that = (TabularColumnElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(tabularColumnProperties, that.tabularColumnProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, tabularColumnProperties);
    }
}
