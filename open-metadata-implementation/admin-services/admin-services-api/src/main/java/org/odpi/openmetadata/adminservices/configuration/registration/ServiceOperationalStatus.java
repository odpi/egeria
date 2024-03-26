/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;


import java.io.Serializable;

/**
 * ServiceOperationalStatus sets up whether an open metadata service is enabled or not.
 */
public enum ServiceOperationalStatus implements Serializable
{
    /**
     * Code for this service is not available/applicable.
     */
    NOT_IMPLEMENTED  (0, "Not Implemented", "Code for this service is not available/applicable."),

    /**
     * The service is available and running.
     */
    ENABLED          (1, "Enabled",         "The service is available and running."),

    /**
     * The service has been disabled.
     */
    DISABLED         (2, "Disabled",        "The service has been disabled.");

    private static final long serialVersionUID = 1L;

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
    ServiceOperationalStatus(int     typeCode, String   typeName, String   typeDescription)
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
