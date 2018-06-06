/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adminservices;

import java.io.Serializable;

/**
 * OMAGServerCategory sets up whether the server container is the Atlas Server or the OMAG Server.
 */
public enum OMAGServerCategory implements Serializable
{
    ATLAS_SERVER        (1, "Atlas Server",       "The server supports the Atlas graph repository plus value-add services."),
    OMAG_SERVER         (0, "OMAG Server",        "The server is the open metadata and governance (OMAG) server.");

    private static final long serialVersionUID = 1L;

    private int            typeCode;
    private String         typeName;
    private String         typeDescription;


    /**
     * Default Constructor
     *
     * @param typeCode - ordinal for this enum
     * @param typeName - symbolic name for this enum
     * @param typeDescription - short description for this enum
     */
    OMAGServerCategory(int     typeCode, String   typeName, String   typeDescription)
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
     * @return int - type code
     */
    public int getTypeCode()
    {
        return typeCode;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String - default name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return the default description for the type for this enum instance.
     *
     * @return String - default description
     */
    public String getTypeDescription()
    {
        return typeDescription;
    }
}
