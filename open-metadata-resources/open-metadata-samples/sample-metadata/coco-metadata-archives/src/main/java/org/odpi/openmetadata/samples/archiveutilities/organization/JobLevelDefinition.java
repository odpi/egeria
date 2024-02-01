/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.organization;


/**
 * The JobLevelDefinition is used to feed the definition of the JobLevel valid value set for Coco Pharmaceuticals' employees.
 */
public enum JobLevelDefinition
{
    /**
     * This person is a founder of the organization.
     */
    LEVEL_9 ("9", "Founder", "This person is a founder of the organization."),

    /**
     * This person is a board member responsible for a significant part of the business - typically involving multiple business areas and services.
     */
    LEVEL_8 ("8", "Executive", "This person is a board member responsible for a significant part of the business - typically involving multiple business areas and services."),

    /**
     * This person manages a business area.
     */
    LEVEL_7 ("7", "Business Area Manager", "This person manages a business area."),

    /**
     * This person manages multiple departments.  They are often responsible for one or more business services within a business area.
     */
    LEVEL_6 ("6", "Senior Manager", "This person manages multiple departments.  They are often responsible for one or more business services within a business area."),

    /**
     * This person provides significant expertise that is necessary to run the business.  They may supervise a small number of people but they are not typically personnel managers.
     */
    LEVEL_5 ("5", "Expert", "This person provides significant expertise that is necessary to run the business.  They may supervise a small number of people but they are not typically personnel managers."),

    /**
     * This person is a manager of supervisors and individual contributors.
     */
    LEVEL_4 ("4", "Personnel Manager", "This person is a manager of supervisors and individual contributors."),

    /**
     * This person is responsible for supervising a small number of people.
     */
    LEVEL_3 ("3", "Supervisor", "This person is responsible for supervising a small number of people."),

    /**
     * This person is responsible for their own work.
     */
    LEVEL_2 ("2", "Individual contributor", "This person is responsible for their own work."),
   ;

    public static final String             validValueSetName = "JobLevel";
    public static final String             validValueSetPropertyName = "jobLevel";
    public static final String             validValueSetDescription = "Describes the seniority of an employee in the Coco Pharmaceuticals' organization.";
    public static final String             validValueSetUsage = "Stored in the `jobLevel` additional property of the Person entity.";
    public static final String             validValueSetScope = "Used when the person described has an employment contract with Coco Pharmaceuticals.";
    public static final JobLevelDefinition defaultValue       = JobLevelDefinition.LEVEL_2;

    private final String preferredValue;
    private final String displayName;
    private final String description;


    /**
     * The constructor creates an instance of the enum
     *
     * @param preferredValue   unique id for the enum
     * @param displayName   name for the enum
     * @param description further information about the enum
     */
    JobLevelDefinition(String preferredValue, String displayName, String description)
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
     * Return the description of the value's meaning.
     *
     * @return string text
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
        return "JobLevelDefinition{" + displayName + '}';
    }
}
