/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

/**
 * TypeDefAttributeStatus is an enum that describes the status of a specific typeDef attribute.
 */
public enum TypeDefAttributeStatus
{
    ACTIVE_ATTRIBUTE              (1, "ActiveAttribute",
                                   "Attribute available and in use.  This is the default value equivalent to null"),
    RENAMED_ATTRIBUTE             (2, "RenamedAttribute",
                                   "This attribute should not be used because it has been renamed."),
    DEPRECATED_ATTRIBUTE          (3,  "DeprecatedAttribute",
                                   "This attribute should not be used because it has been deprecated");


    private int     ordinal;
    private String  name;
    private String  description;


    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param ordinal numeric code for the patch action
     * @param name descriptive name for the patch action
     * @param description description of the patch action
     */
    TypeDefAttributeStatus(int ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the code value for the status.
     *
     * @return int code
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the descriptive name for the status.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the status.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "TypeDefAttributeStatus{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
