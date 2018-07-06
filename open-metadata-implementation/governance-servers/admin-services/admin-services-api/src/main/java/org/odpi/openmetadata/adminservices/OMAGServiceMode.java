/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adminservices;


import java.io.Serializable;

/**
 * OMAGServiceMode sets up whether an open metadata and governance service (OMAS) is enabled or not.
 */
public enum OMAGServiceMode implements Serializable
{
    ENABLED          (1, "Enabled",         "The open metadata and governance service is available and running."),
    DISABLED         (0, "Disabled",        "The open metadata and governance service is disabled.");

    private static final long serialVersionUID = 1L;

    private int            typeCode;
    private String         typeName;
    private String         typeDescription;


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
