/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.organization;


/**
 * The ContactTypeDefinition is used to feed the definition of the contactType valid value set for Coco Pharmaceuticals' employees.
 */
public enum ContactTypeDefinition
{
    /**
     * Email address assigned by Coco Pharmaceuticals.
     */
    COMPANY_EMAIL           ("Work Email",              "Work Email Address", "Email address assigned by Coco Pharmaceuticals."),

    /**
     * Email address supplied by the employee.
     */
    PERSONAL_EMAIL          ("Personal Email",          "Personal Email Address", "Email address supplied by the employee."),

    /**
     * Email address of a relative or friend to call in an emergency.
     */
    EMERGENCY_CONTACT_EMAIL ("Emergency Contact Email", "Emergency Contact (Email)", "Email address of a relative or friend to call in an emergency."),

    /**
     * Phone number fixed to a desk or work location.
     */
    OFFICE_PHONE            ("Office Phone",            "Office Phone Number", "Phone number fixed to a desk or work location."),

    /**
     * Number of mobile/cell phone.
     */
    MOBILE_PHONE            ("Mobile Phone",            "Mobile Phone Number", "Number of mobile/cell phone."),

    /**
     * Phone number of a relative or friend to call in an emergency.
     */
    EMERGENCY_CONTACT_PHONE ("Emergency Contact Phone", "Emergency Contact (Phone Number)", "Phone number of a relative or friend to call in an emergency."),

    /**
     * URL link to person's LinkedIn account.
     */
    LINKED_IN               ("LinkedIn",                "LinkedIn Profile", "URL link to person's LinkedIn account."),

    /**
     * URL of a website related to the individual.
     */
    WEBSITE                 ("Website",                "Personal Web Site", "URL of a website related to the individual."),
    ;

    public static final String validValueSetName        = "ContactType";
    public static final String validValueSetDescription = "Identifies the purpose of an individual's contact details.";
    public static final String validValueSetUsage       = "Used in the contactType attribute of ContactDetails entities.";
    public static final String validValueSetScope       = "For profiles of Coco Pharmaceuticals' employees and partners.";


    private final String preferredValue;
    private final String displayName;
    private final String description;


    /**
     * The constructor creates an instance of the enum
     *
     * @param preferredValue   unique id for the enum
     * @param displayName   name for the enum
     */
    ContactTypeDefinition(String preferredValue,
                          String displayName,
                          String description)
    {
        this.preferredValue = preferredValue;
        this.displayName = displayName;
        this.description = description;
    }



    /**
     * This is the preferred value that applications should use for this valid value.
     *
     * @return string value
     */
    public String getPreferredValue()
    {
        return preferredValue;
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
     * Return the description of the contact type.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ContactTypeDefinition{" + displayName + '}';
    }
}
