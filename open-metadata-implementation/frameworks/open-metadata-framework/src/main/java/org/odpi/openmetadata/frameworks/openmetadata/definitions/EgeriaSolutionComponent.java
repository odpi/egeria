/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;

/**
 * A description of the predefined solution components.
 */
public enum EgeriaSolutionComponent implements SolutionComponentDefinition
{
    OPEN_METADATA_REPOSITORY("1dcdc147-e023-4480-ae5c-93c009927b1a",
                             "OPEN-METADATA-REPOSITORY",
                             SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                             DeployedImplementationType.OPEN_METADATA_REPOSITORY.getAssociatedTypeName(),
                             DeployedImplementationType.OPEN_METADATA_REPOSITORY.getDeployedImplementationType(),
                             DeployedImplementationType.OPEN_METADATA_REPOSITORY.getDescription(),
                             null,
                             null),

    METADATA_ACCESS_STORE("74385479-73a1-4e61-9c2f-a98be526acc7",
                          "METADATA-ACCESS-STORE",
                          SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                          EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER.getAssociatedTypeName(),
                          EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER.getDeployedImplementationType(),
                          EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER.getDescription(),
                          new EgeriaSolutionComponent[]{OPEN_METADATA_REPOSITORY},
                          null),


    INTEGRATION_DAEMON("31d227b4-08b2-4544-bd31-f7f1d1f65ec9",
                       "INTEGRATION-DAEMON",
                       SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                       EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getAssociatedTypeName(),
                       EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getDeployedImplementationType(),
                       EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getDescription(),
                       null,
                       null),


    ENGINE_HOST("0ec8aaf3-42cf-4a81-83fa-2db6b20c7507",
                "ENGINE-HOST",
                SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                EgeriaDeployedImplementationType.ENGINE_HOST.getAssociatedTypeName(),
                EgeriaDeployedImplementationType.ENGINE_HOST.getDeployedImplementationType(),
                EgeriaDeployedImplementationType.ENGINE_HOST.getDescription(),
                null,
                null),


    VIEW_SERVER("5037b995-3e3e-4ad1-94f7-1d7960f28b44",
                "VIEW-SERVER",
                SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                EgeriaDeployedImplementationType.VIEW_SERVER.getAssociatedTypeName(),
                EgeriaDeployedImplementationType.VIEW_SERVER.getDeployedImplementationType(),
                EgeriaDeployedImplementationType.VIEW_SERVER.getDescription(),
                null,
                null),

    SERVER_PLATFORM("1442a1bc-f791-4fda-a3be-56fb68934a4c",
                    "SERVER-PLATFORM",
                    SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                    EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getAssociatedTypeName(),
                    EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType(),
                    EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDescription(),
                    new EgeriaSolutionComponent[]{METADATA_ACCESS_STORE, VIEW_SERVER, INTEGRATION_DAEMON, ENGINE_HOST},
                    null),

    LOAD_ARCHIVE("cc3e0a81-ad10-4c72-b6a5-01301c5dd587",
                 "LOAD_ARCHIVE",
                 SolutionComponentType.CONSOLE_COMMAND.getSolutionComponentType(),
                 null,
                 "Load Archive",
                 "A command to load an open metadata archive.",
                 null,
                 null),

    PYEGERIA("c3fd85ae-4226-4d20-b57f-af3b0e748e5f",
             "PYEGERIA",
             SolutionComponentType.SOFTWARE_LIBRARY.getSolutionComponentType(),
             OpenMetadataType.SOFTWARE_LIBRARY_CLASSIFICATION.typeName,
             "pyegeria",
             "Python language library supporting calls to Egeria's REST APIs",
            null,
             null),

    MY_EGERIA("57f6a0c7-fbbe-42b2-a30f-3298a8816096",
             "MY-EGERIA",
             SolutionComponentType.USER_INTERFACE.getSolutionComponentType(),
             null,
             "my_egeria",
             "A user interface for working with Egeria and open metadata.",
             new EgeriaSolutionComponent[]{PYEGERIA},
             null),

    DR_EGERIA("1ddd9283-3dc9-4220-ac42-be0894b5a930",
              "DR-EGERIA",
              SolutionComponentType.SOFTWARE_LIBRARY.getSolutionComponentType(),
              null,
              "Dr.Egeria",
              "A markdown processor that can maintain and retrieve open metadata by processing and creating markdown documents.",
              new EgeriaSolutionComponent[]{PYEGERIA},
              null),

    HEY_EGERIA("b2278203-63a6-4a4f-b142-6f75cd592415",
              "HEY-EGERIA",
              SolutionComponentType.CONSOLE_COMMAND.getSolutionComponentType(),
              null,
              "hey_egeria",
              "A user interface for working with Egeria and open metadata.",
              new EgeriaSolutionComponent[]{
                      PYEGERIA,
                      LOAD_ARCHIVE},
              null),


    OPEN_METADATA_ARCHIVE("8624e13c-6b08-417e-8aee-5d78a5278af2",
                          "OPEN-METADATA-ARCHIVE",
                          SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                          OpenMetadataType.ARCHIVE_FILE.typeName,
                          "Open Metadata Archive",
                          "An archive file containing pre-defined metadata types and instances.",
                          null,
                          null),

    EGERIA_BUILD("f763d7df-1d49-4ceb-a079-c7f40e15982b",
                 "EGERIA-BUILD",
                 SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                 OpenMetadataType.BUILD_INSTRUCTION_FILE.typeName,
                 "Egeria Gradle Build",
                 "Comprehensive build process that complies the software, runs tests and creates the assembly for Egeria's runtime.",
                 null,
                 null),

    JUPYTER_NOTEBOOK("3af1e6e6-7a65-4d67-8cfd-d202cc5660d7",
                "JUPYTER-NOTEBOOK",
                SolutionComponentType.USER_INTERFACE.getSolutionComponentType(),
                OpenMetadataType.SOFTWARE_SERVER.typeName,
                "Jupyter Notebook",
                "Comprehensive build process that complies the software, runs tests and creates the assembly for Egeria's runtime.",
                null,
                null),

    JUPYTER_LAB("e3e3a894-43a8-4745-a220-c3c047c01688",
                 "JUPYTER-LAB",
                 SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                 OpenMetadataType.SOFTWARE_SERVER.typeName,
                 "JupyterLab",
                 "Comprehensive build process that complies the software, runs tests and creates the assembly for Egeria's runtime.",
                new EgeriaSolutionComponent[]{PYEGERIA, JUPYTER_NOTEBOOK},
                 null),

    UNITY_CATALOG("8b472a17-9c2b-4a31-81b2-7c81fbdb148a",
                "UNITY-CATALOG",
                SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                OpenMetadataType.SOFTWARE_SERVER.typeName,
                "Unity Catalog",
                "Credential serving catalog for distributed data.",
                null,
                null),

    OL_PROXY("19af8706-17b9-4367-865f-2181dcb51646",
             "OPEN-LINEAGE-PROXY",
             SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
             OpenMetadataType.SOFTWARE_SERVER.typeName,
             "Open Lineage Proxy",
             "Consolidates open lineage events from data engines and pipes them to Apache Kafka.",
             null,
             null),

    OL_KAFKA_TOPIC("d4b3a089-b909-4f49-9d95-bbbba7008f17",
             "OPEN-LINEAGE-TOPIC",
             SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
             OpenMetadataType.TOPIC.typeName,
             "Open Lineage Topic",
             "Stores and distributes Open Lineage Events.",
             null,
             null),

    APACHE_AIRFLOW("6db0416a-1e7f-4e7c-aae6-8925c2148820",
                   "APACHE-AIRFLOW",
                   SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
                   OpenMetadataType.SOFTWARE_SERVER.typeName,
                   "Apache Airflow",
                   "Runs data movement and transformation pipelines.",
                   null,
                   null)
    ;


    private final String                    guid;
    private final String                    identifier;
    private final String                    componentType;
    private final String                    implementationType;
    private final String                    displayName;
    private final String                    description;
    private final EgeriaSolutionComponent[] subComponents;
    private final String                    implementationResource;


    /**
     * Construct an enum instance.
     *
     * @param guid unique identifier
     * @param componentType   type of solution component - ege automated process
     * @param implementationType   type of software component - for example, is it a process, of file or database.
     * @param displayName display name of solution component
     * @param description description of solution component
     * @param subComponents optional subcomponents of the solution
     * @param implementationResource components useful when creating implementations
     */
    EgeriaSolutionComponent(String                    guid,
                            String                    identifier,
                            String                    componentType,
                            String                    implementationType,
                            String                    displayName,
                            String                    description,
                            EgeriaSolutionComponent[] subComponents,
                            String                    implementationResource)
    {
        this.guid                   = guid;
        this.identifier             = identifier;
        this.componentType          = componentType;
        this.implementationType     = implementationType;
        this.displayName            = displayName;
        this.description            = description;
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
