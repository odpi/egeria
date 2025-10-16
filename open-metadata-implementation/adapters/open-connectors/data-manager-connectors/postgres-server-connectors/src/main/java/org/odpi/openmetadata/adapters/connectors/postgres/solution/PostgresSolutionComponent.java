/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.solution;

import org.odpi.openmetadata.frameworks.openmetadata.definitions.EgeriaSolutionComponent;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionComponentDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;

import java.util.Arrays;
import java.util.List;

/**
 * A description of the predefined solution components.
 */
public enum PostgresSolutionComponent implements SolutionComponentDefinition
{
    SURVEY_POSTGRES_SERVER("f945efc5-2ada-4cc9-b3a7-36cedbd87f72",
                           "SURVEY-POSTGRESQL-SERVER",
                           SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                           null,
                           "Survey PostgreSQL Server",
                           "Extract high-level details (such a size) about the different databases managed by the PostgreSQL server.  This survey does not scan the data in the databases.  Its aim is to provide an overview of the databases resident in the database server.  With this information it is possible to select which databases are worth performing a more detailed survey on.",
                           new SolutionComponentDefinition[]{EgeriaSolutionComponent.ENGINE_HOST},
                           null,
                           null),

    SURVEY_POSTGRES_DATABASE("df94a083-5ebc-4081-af5e-d0b6fd16b899",
                           "SURVEY-POSTGRESQL-DATABASE",
                           SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                           null,
                           "Survey PostgreSQL Database",
                           "Extract details about the schemas, tables and columns defined in a PostgreSQL database.  This survey does not scan the data in the database.  Its aim is to provide an overview of the type of data stored in the database.  With this information is it possible to decide whether to explore the data itself.",
                           new SolutionComponentDefinition[]{EgeriaSolutionComponent.ENGINE_HOST},
                           null,
                           null),


    CATALOG_POSTGRES_SERVER("101b4f3b-f91d-4054-9620-d1f5fd3d4050",
                           "CATALOG-POSTGRESQL-SERVER",
                           SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                           null,
                           "Catalog PostgreSQL Server",
                           "Create database assets in the metadata server for each of the databases found in the PostgreSQL server.",
                           new SolutionComponentDefinition[]{EgeriaSolutionComponent.INTEGRATION_DAEMON},
                           null,
                           null),

    CATALOG_POSTGRES_DATABASE("06ed1756-7265-4bc7-8f94-c87e849f32d4",
                             "CATALOG-POSTGRESQL-DATABASE",
                             SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                             null,
                             "Catalog PostgreSQL Database",
                             "Extract details about the schemas, tables and columns defined in a PostgreSQL database.  This survey does not scan the data in the database.  Its aim is to provide an overview of the type of data stored in the database.  With this information is it possible to decide whether to explore the data itself.",
                             new SolutionComponentDefinition[]{EgeriaSolutionComponent.INTEGRATION_DAEMON},
                             null,
                             null),

    POSTGRES_TABULAR_DATA_SET("d0b49ac1-77f0-43c6-8e62-d38fb0139798",
                              "POSTGRESQL-TABULAR-DATA-SET-CONNECTOR",
                              SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                              null,
                              "PostgreSQL Tabular Data Set Connector",
                              "Manages the definition and maintenance of data in a PostgreSQL table.",
                              null,
                              null,
                              null),


    ;


    private final String                        guid;
    private final String                        identifier;
    private final String                        componentType;
    private final String                        implementationType;
    private final String                        displayName;
    private final String                        description;
    private final SolutionComponentDefinition[] parentComponents;
    private final SolutionComponentDefinition[] subComponents;
    private final String                        implementationResource;


    /**
     * Construct an enum instance.
     *
     * @param guid unique identifier
     * @param componentType   type of solution component - ege automated process
     * @param implementationType   type of software component - for example, is it a process, of file or database.
     * @param displayName display name of solution component
     * @param description description of solution component
     * @param parentComponents optional parent components of the solution
     * @param subComponents optional subcomponents of the solution
     * @param implementationResource components useful when creating implementations
     */
    PostgresSolutionComponent(String                        guid,
                              String                        identifier,
                              String                        componentType,
                              String                        implementationType,
                              String                        displayName,
                              String                        description,
                              SolutionComponentDefinition[] parentComponents,
                              SolutionComponentDefinition[] subComponents,
                              String                        implementationResource)
    {
        this.guid                   = guid;
        this.identifier             = identifier;
        this.componentType          = componentType;
        this.implementationType     = implementationType;
        this.displayName            = displayName;
        this.description            = description;
        this.parentComponents       = parentComponents;
        this.subComponents          = subComponents;
        this.implementationResource = implementationResource;
    }


    /**
     * Return the GUID for the element.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the type of solution component - for example, is it a process, of file or database.
     *
     * @return string
     */
    public String getComponentType()
    {
        return componentType;
    }


    /**
     * Return which type of software component is likely to be deployed to implement this solution component.
     *
     * @return string
     */
    public String getImplementationType()
    {
        return implementationType;
    }


    /**
     * Return the display name of the solution component.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the solution component.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


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
     * Return the optional list of subcomponents.
     *
     * @return null or list
     */
    @Override
    public List<SolutionComponentDefinition> getSubComponents()
    {
        if (subComponents == null)
        {
            return null;
        }

        return Arrays.asList(subComponents);
    }

    /**
     * Return the optional list of parent components to link it to.
     *
     * @return null or list
     */
    @Override
    public  List<SolutionComponentDefinition> getParentComponents()
    {
        if (parentComponents == null)
        {
            return null;
        }

        return Arrays.asList(parentComponents);
    }

    /**
     * Return the GUID of the implementation element (or null)
     *
     * @return guid
     */
    @Override
    public String getImplementationResource()
    {
        return implementationResource;
    }



    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "EgeriaSolutionComponent{" + displayName + '}';
    }
}
