/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;


/**
 * The ProjectStatusDefinition is used to feed the definition of the ProjectStatus valid value set for Coco Pharmaceuticals' projects.
 */
public enum ProjectStatusDefinition
{
    /**
     * This project has been proposed but there is no agreement to allocate resources or proceed.
     */
    PROPOSED ("0", "Proposed", "This project has been proposed but there is no agreement to allocate resources or proceed."),

    /**
     * This project has be approved to go ahead.  Resources a approved for it in principle, but specific resources may not have been allocated yet.
     */
    APPROVED ("1", "Approved", "This project has be approved to go ahead.  Resources a approved for it in principle, but specific resources may not have been allocated yet."),

    /**
     * This project has an outline project plan and timeline.  Resources have been allocated.
     */
    PLANNED ("2", "Planned", "This project has an outline project plan and timeline.  Resources have been allocated."),

    /**
     * This project is in final preparations to start.
     */
    ACTIVATING ("3", "Activating", "This project is in final preparations to start."),

    /**
     * This project is running.
     */
    ACTIVE ("4", "Active", "This project is running."),

    /**
     * This project is not able to make progress because of an external factor.
     */
    STALLED ("5", "Stalled", "This project is not able to make progress because of an external factor."),

    /**
     * This project has completed.
     */
    COMPLETED ("6", "Completed", "This project has completed."),

    /**
     * This project has been abandoned and no further work on it is expected.
     */
    ABANDONED ("7", "Abandoned", "This project has been abandoned and no further work on it is expected."),
   ;

    public static final String             validValueSetName = "ProjectStatus";
    public static final String             validValueSetPropertyName = "projectStatus";
    public static final String             validValueSetDescription = "Describes the stages that a project may pass through.";
    public static final String             validValueSetUsage = "Stored in the `projectStatus` property of the Project entity.";
    public static final String                  validValueSetScope = "Used when operating a project.";
    public static final ProjectStatusDefinition defaultValue       = ProjectStatusDefinition.PROPOSED;

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
    ProjectStatusDefinition(String preferredValue, String displayName, String description)
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
        return "ProjectStatusDefinition{" + displayName + '}';
    }
}
