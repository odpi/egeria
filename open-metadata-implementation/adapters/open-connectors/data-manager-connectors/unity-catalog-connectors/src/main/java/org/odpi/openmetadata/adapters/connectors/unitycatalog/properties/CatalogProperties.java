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
 * Description of the properties to pass when creating a catalog - maps to CreateCatalog.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CatalogProperties extends BasicElementProperties
{
    private String  isolation_mode                  = null;
    private boolean accessible_in_current_workspace = false;

    /**
     * Return the type of isolation - eg OPEN.
     *
     * @return string
     */
    public String getIsolation_mode()
    {
        return isolation_mode;
    }


    /**
     * Set up the type of isolation - eg OPEN.
     *
     * @param isolation_mode string
     */
    public void setIsolation_mode(String isolation_mode)
    {
        this.isolation_mode = isolation_mode;
    }


    /**
     * Return whether accessible to this user.
     *
     * @return boolean flag
     */
    public boolean isAccessible_in_current_workspace()
    {
        return accessible_in_current_workspace;
    }


    /**
     * Set up whether accessible to this user.
     *
     * @param accessible_in_current_workspace boolean flag
     */
    public void setAccessible_in_current_workspace(boolean accessible_in_current_workspace)
    {
        this.accessible_in_current_workspace = accessible_in_current_workspace;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CatalogProperties{" +
                "isolation_mode='" + isolation_mode + '\'' +
                ", accessible_in_current_workspace=" + accessible_in_current_workspace +
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
        CatalogProperties that = (CatalogProperties) objectToCompare;
        return accessible_in_current_workspace == that.accessible_in_current_workspace &&
                Objects.equals(isolation_mode, that.isolation_mode);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), isolation_mode, accessible_in_current_workspace);
    }
}
