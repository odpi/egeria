/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.refdata;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;

import static org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * ProjectStatus lists suggested status values for a project.
 */
public enum ProjectPhase
{
    /**
     * The project's scope, mission and stakeholders are being defined.
     */
    DEFINE("Define",
        "The project's scope, mission and stakeholders are being defined."),

    /**
     * The project activity and resources are being defined in a work plan.
     */
    PLAN("Plan",
         "The project activity and resources are being defined in a work plan."),

    /**
     * The project's scope, mission and stakeholders are being defined.
     */
    RUN("Run",
        "The project's scope, mission and stakeholders are being defined."),

    /**
     * Work on the project will not continue despite incomplete deliverables.  This may be due to a change in direction or a road block that is preventing further progress.
     */
    HANDOVER("Handover",
            "Project deliverables are being finalized and handed off to an operational team."),

    /**
     * The project has completed its deliverables.
     */
    DONE("Done",
            "The project has completed its deliverables."),

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
    ProjectPhase(String name,
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
                                                OpenMetadataProperty.PROJECT_PHASE.name,
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
                                           OpenMetadataProperty.PROJECT_PHASE.name,
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
        return "ProjectPhase{" + name + '}';
    }
}
