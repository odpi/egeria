/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * ProjectHealth lists suggested health values for a project.
 */
public enum ProjectHealth
{
    /**
     * Project activity has not yet started.
     */
    NOT_STARTED("Not Started",
                "Project activity has not yet started.",
                "orange"),

    /**
     * The project activity is proceeding, consuming time and resources as anticipated, whilst making the planned amount of progress towards the desired deliverables.
     */
    ON_TRACK("On Track",
            "The project activity is proceeding, consuming time and resources as anticipated, whilst making the planned amount of progress towards the desired deliverables.",
           "green"),

    /**
     * Work on the project has finished, and all deliverables have either been completed, abandoned or moved to a different project.
     */
    AT_RISK("At Risk",
            "Some aspects of the project are beginning to deviate from either the planned schedule, or are not consuming time/resources as anticipated.  A small course correction is needed to bring it back on track.",
            "yellow"),

    /**
     * Work on the project will not continue despite incomplete deliverables.  This may be due to a change in direction or a road block that is preventing further progress.
     */
    OFF_TRACK("Off Track",
              "Work on the project is not making the expected amount of progress for the time and resources spent on it.  A major change needs to be made if the project is to be brought back on track.",
              "red"),

    /**
     * The project is no longer being worked on either because it completed successfully, or was cancelled, or put on hold.
     */
    NO_ACTIVITY("No Activity",
                "The project is no longer being worked on either because it completed successfully, or was cancelled, or put on hold.",
                "blue"),

    ;

    /**
     * Property value.
     */
    private final String name;


    /**
     * Property value description.
     */
    private final String description;

    /**
     * Property value colour.
     */
    private final String colour;


    /**
     * Constructor for individual enum value.
     *
     * @param name the property value to use in project status
     * @param description description of the project status property value
     *
     */
    ProjectHealth(String name,
                  String description,
                  String colour)
    {
        this.name        = name;
        this.description = description;
        this.colour      = colour;
    }


    /**
     * Return the name of the value.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description for this value.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the colour for this value.
     *
     * @return string
     */
    public String getColour()
    {
        return colour;
    }


    /**
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(null,
                                                OpenMetadataProperty.PROJECT_HEALTH.name,
                                                null,
                                                name);
    }


    /**
     * Return the namespace for this value.
     *
     * @return string
     */
    public String getNamespace()
    {
        return constructValidValueNamespace(null,
                                            OpenMetadataProperty.PROJECT_HEALTH.name,
                                            null);
    }




    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProjectHealth{" + name + '}';
    }
}
