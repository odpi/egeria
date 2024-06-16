/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

/**
 * OMAGServiceMode sets up whether an open metadata and governance service (OMAS) is enabled or not.
 */
public enum OMAGServiceMode
{
    /**
     * The open metadata and governance service is available and running.
     */
    ENABLED          (1, "Enabled",         "The open metadata and governance service is available and running."),

    /**
     * The open metadata and governance service is disabled.
     */
    DISABLED         (0, "Disabled",        "The open metadata and governance service is disabled.");

    private final int    typeCode;
    private final String typeName;
    private final String typeDescription;


    /**
     * Default Constructor
     *
     * @param typeCode ordinal for this enum
     * @param typeName symbolic name for this enum
     * @param typeDescription short description for this enum
     */
    OMAGServiceMode(int     typeCode, String   typeName, String   typeDescription)
    {
        /*
         * Save the values supplied
         */
        this.typeCode = typeCode;
        this.typeName = typeName;
        this.typeDescription = typeDescription;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int type code
     */
    public int getTypeCode()
    {
        return typeCode;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return the default description for the type for this enum instance.
     *
     * @return String default description
     */
    public String getTypeDescription()
    {
        return typeDescription;
    }
}
