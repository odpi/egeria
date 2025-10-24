/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.solution;

import org.odpi.openmetadata.frameworks.openmetadata.definitions.EgeriaSolutionComponent;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionBlueprintDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionComponentDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A description of the predefined solution blueprints.  Solution blueprints identify the key solution oriented
 * investments.  They may be used to document both the as-is and to-be solutions.
 */
public enum PostgresSolutionBlueprint implements SolutionBlueprintDefinition
{
    /**
     * Description of the processing used to create the content packs shipped with Egeria.
     */
    POSTGRES_SOLUTION_BLUEPRINT("1ad296ff-6be7-47de-b8a8-e2cee73cbb58",
                               "OPEN-METADATA-CONTENT-PACK",
                               "PostgreSQL Solution Blueprint",
                               "PostgreSQL is an advanced open source relational database. Egeria's content pave for PostgreSQL provides templates for PostgreSQL services, databases and tables along with function to survey the contents of PostgreSQL databases and catalog their schemas.",
                               new SolutionComponentDefinition[]{
                                       EgeriaSolutionComponent.POSTGRES_SERVER,
                                       EgeriaSolutionComponent.METADATA_ACCESS_STORE,
                                       EgeriaSolutionComponent.LOAD_ARCHIVE,
                                       EgeriaSolutionComponent.OPEN_METADATA_REPOSITORY}),


    ;

    private final String                         guid;
    private final String                        identifier;
    private final String                        displayName;
    private final String                        description;
    private final SolutionComponentDefinition[] solutionComponents;

    /**
     * Construct an enum instance.
     *
     * @param guid unique identifier
     * @param identifier the assigned identifier
     * @param displayName display name of solution blue print
     * @param description description of solution blueprint
     * @param solutionComponents content of the blueprint
     */
    PostgresSolutionBlueprint(String                            guid,
                              String                            identifier,
                              String                            displayName,
                              String                            description,
                              SolutionComponentDefinition[]     solutionComponents)
    {
        this.guid                    = guid;
        this.identifier              = identifier;
        this.displayName             = displayName;
        this.description             = description;
        this.solutionComponents      = solutionComponents;
    }


    /**
     * Return the unique identifier of this element.  It is only needed if the elements are to be loaded
     * into an open metadata archive.
     *
     * @return string
     */
    @Override
    public String getGUID() { return guid; }


    /**
     * Return the version identifier of the solution blueprint.
     *
     * @return string
     */
    @Override
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Return the display name of the solution blueprint.
     *
     * @return string
     */
    @Override
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the solution blueprint
     *
     * @return string
     */
    @Override
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
        return "SolutionBlueprint{" + displayName + '}';
    }


    /**
     * Return the list of components that are members of the solution blueprint.
     *
     * @return list of component definitions
     */
    @Override
    public List<SolutionComponentDefinition> getSolutionComponents()
    {
        if (solutionComponents != null)
        {
            return new ArrayList<>(Arrays.asList(solutionComponents));
        }

        return null;
    }
}
