/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * ProjectStatus lists suggested status values for a project.
 */
public enum ProjectStatus
{
    /**
     * This project has been proposed but there is no agreement to allocate resources or proceed.
     */
    PROPOSED ("Proposed",
              "This project has been proposed but there is no agreement to allocate resources or proceed."),

    /**
     * This project has be approved to go ahead.  Resources a approved for it in principle, but specific resources may not have been allocated yet.
     */
    APPROVED ( "Approved",
               "This project has be approved to go ahead.  Resources a approved for it in principle, but specific resources may not have been allocated yet."),

    /**
     * This project has an outline project plan and timeline.  Resources have been allocated.
     */
    PLANNED ( "Planned",
              "This project has an outline project plan and timeline.  Resources have been allocated."),

    /**
     * This project is in final preparations to start.
     */
    ACTIVATING ( "Activating",
                 "This project is in final preparations to start."),


    /**
     * The project is currently being worked on by the project team.
     */
    ACTIVE("Active",
            "The project is currently being worked on by the project team."),

    /**
     * This project is not able to make progress because of an external factor.
     */
    STALLED ("Stalled",
             "This project is not able to make progress because of an external factor."),

    /**
     * Work on the project has finished, and all deliverables have either been completed, abandoned or moved to a different project.
     */
    COMPLETED("Completed",
              "Work on the project has finished, and all deliverables have either been completed, abandoned or moved to a different project."),

    /**
     * Work on the project will not continue despite incomplete deliverables.  This may be due to a change in direction or a road block that is preventing further progress.
     */
    CANCELLED("Cancelled",
            "Work on the project will not continue despite incomplete deliverables.  This may be due to a change in direction or a road block that is preventing further progress."),

    /**
     * The project has not finished, and work on the project has been temporarily suspended.
     */
    ON_HOLD("On Hold",
            "The project has not finished, and work on the project has been temporarily suspended."),

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
     * Constructor for individual enum value.
     *
     * @param name the property value to use in project status
     * @param description description of the project status property value
     */
    ProjectStatus(String name,
                  String description)
    {
        this.name        = name;
        this.description = description;
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
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(null,
                                                OpenMetadataProperty.PROJECT_STATUS.name,
                                                null,
                                                name);
    }


    /**
     * Return the category for this value.
     *
     * @return string
     */
    public String getCategory()
    {
        return constructValidValueCategory(null,
                                           OpenMetadataProperty.PROJECT_STATUS.name,
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
        return "ProjectStatus{" + name + '}';
    }
}
