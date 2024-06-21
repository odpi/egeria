/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Describes elements that sit in the catalog namespace.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CatalogNamespaceProperties extends BasicElementProperties
{
    private String catalog_name = null;

    public CatalogNamespaceProperties()
    {
    }


    /**
     * Return the name of the catalog that this element belongs to.
     *
     * @return name
     */
    public String getCatalog_name()
    {
        return catalog_name;
    }


    /**
     * Set up the name of the catalog that this element belongs to.
     *
     * @param catalog_name name
     */
    public void setCatalog_name(String catalog_name)
    {
        this.catalog_name = catalog_name;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CatalogNamespaceProperties{" +
                "catalog_name='" + catalog_name + '\'' +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare)) return false;
        CatalogNamespaceProperties that = (CatalogNamespaceProperties) objectToCompare;
        return Objects.equals(catalog_name, that.catalog_name);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), catalog_name);
    }
}
