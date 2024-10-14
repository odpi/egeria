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
 * Common properties of an element (Catalog, Schema, Volume, Table, Function) stored in Unity catalog.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BasicElementProperties extends BasicProperties
{
    private String  metastore_id   = null;
    private String  securable_type = null;
    private String  securable_kind = null;
    private String  owner          = null;
    private boolean browse_only    = false;


    /**
     * Constructor
     */
    public BasicElementProperties()
    {
    }

    /**
     * Return the guid is the associated hive metastore.
     *
     * @return guid
     */
    public String getMetastore_id()
    {
        return metastore_id;
    }


    /**
     * Set up the guid is the associated hive metastore.
     *
     * @param metastore_id string guid
     */
    public void setMetastore_id(String metastore_id)
    {
        this.metastore_id = metastore_id;
    }


    /**
     * Return the type of securable element - eg SCHEMA.
     *
     * @return string name
     */
    public String getSecurable_type()
    {
        return securable_type;
    }


    /**
     * Set up the type of securable element - eg SCHEMA.
     *
     * @param securable_type string name
     */
    public void setSecurable_type(String securable_type)
    {
        this.securable_type = securable_type;
    }


    /**
     * Return the kind of securable element eg SCHEMA_STANDARD.
     *
     * @return string name
     */
    public String getSecurable_kind()
    {
        return securable_kind;
    }


    /**
     * Set up the kind of securable element eg SCHEMA_STANDARD.
     *
     * @param securable_kind string name
     */
    public void setSecurable_kind(String securable_kind)
    {
        this.securable_kind = securable_kind;
    }


    /**
     * Return the owner of the element.
     *
     * @return string name
     */
    public String getOwner()
    {
        return owner;
    }


    /**
     * Set up the owner of the element.
     *
     * @param owner string name
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
    }


    /**
     * Return whether this element is read only or not.
     *
     * @return boolean flag
     */
    public boolean isBrowse_only()
    {
        return browse_only;
    }


    /**
     * Set up whether this element is read only or not.
     *
     * @param browse_only boolean flag
     */
    public void setBrowse_only(boolean browse_only)
    {
        this.browse_only = browse_only;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BasicElementProperties{" +
                "metastore_id='" + metastore_id + '\'' +
                ", securable_type='" + securable_type + '\'' +
                ", securable_kind='" + securable_kind + '\'' +
                ", owner='" + owner + '\'' +
                ", browse_only=" + browse_only +
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
        BasicElementProperties that = (BasicElementProperties) objectToCompare;
        return browse_only == that.browse_only &&
                Objects.equals(metastore_id, that.metastore_id) &&
                Objects.equals(securable_type, that.securable_type) &&
                Objects.equals(securable_kind, that.securable_kind) &&
                Objects.equals(owner, that.owner);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(metastore_id, securable_type, securable_kind, owner, browse_only);
    }
}
