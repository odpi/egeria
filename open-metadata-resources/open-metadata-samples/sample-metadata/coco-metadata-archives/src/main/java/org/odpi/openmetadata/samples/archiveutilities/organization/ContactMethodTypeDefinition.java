/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.organization;


/**
 * The ContactMethodTypeDefinition is used to feed the definition of the contactMethodType valid value set for Coco Pharmaceuticals' employees.
 */
public enum ContactMethodTypeDefinition
{
    /**
     * Email
     */
    EMAIL("E", "Email"),

    /**
     * Phone
     */
    PHONE("P", "Phone"),

    /**
     * Chat Services
     */
    CHAT("C", "Chat Services"),

    /**
     * External Profile
     */
    PROFILE("P", "External Profile"),

    /**
     * Account
     */
    ACCOUNT("A", "Account"),

    /**
     * Other
     */
    OTHER("O", "Other"),
    ;

    private final String code;
    private final String displayName;


    /**
     * The constructor creates an instance of the enum
     *
     * @param code   unique id for the enum
     * @param displayName   name for the enum
     */
    ContactMethodTypeDefinition(String code, String displayName)
    {
        this.code = code;
        this.displayName = displayName;
    }



    /**
     * This is the preferred value that applications should use for this valid value.
     *
     * @return string value
     */
    public String getCode()
    {
        return code;
    }


    /**
     * Return the printable name.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }



    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ContactMethodType{" + displayName + '}';
    }
}
