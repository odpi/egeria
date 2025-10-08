/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * The OpenMetadataRoleDefinition is used to feed the definition of the actor roles that are used in many solutions.
 */
public enum EgeriaRoleDefinition implements ActorRoleDefinition
{
    /**
     * Open Source Community developing the Egeria Software and related resources.
     */
    EGERIA_COMMUNITY("c8df1738-674f-489b-afe2-aea1086881f7",
                             OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName,
                             "EGERIA-OPEN-SOURCE-COMMUNITY",
                             "Egeria Open Source Community",
                             "Open Source Community developing the Egeria Software and related resources."),


    /**
     * A person or system maintaining and using open metadata.
     */
    OPEN_METADATA_USER("78079a77-5bbb-4e48-9a7c-ef7a4f0ecfe4",
                       OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName,
                       "OPEN-METADATA-USER",
                       "Open Metadata User",
                       "A person or system maintaining and using open metadata."),

    /**
     * A person/system capable of writing python code.
     */
    PYTHON_PROGRAMMER("caa7f7ff-c041-4572-b46a-bcd453e656ce",
                      OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName,
                      "PYTHON-PROGRAMMER",
                      "Python Programmer",
                      "A person/system capable of writing python code."),

    ;

    private final String guid;
    private final String typeName;
    private final String identifier;
    private final String displayName;
    private final String description;


    /**
     * ProductRoleDefinition constructor creates an instance of the enum
     *
     * @param guid         unique identifier for the role
     * @param identifier   unique Id for the role
     * @param displayName   text for the role
     * @param description   description of the assets in the role
     */
    EgeriaRoleDefinition(String guid,
                         String typeName,
                         String identifier,
                         String displayName,
                         String description)
    {
        this.guid        = guid;
        this.typeName    = typeName;
        this.identifier  = identifier;
        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Return the name of the PersonRole type to use.
     *
     * @return type name
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the type name
     *
     * @return string
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Returns scope of the role.
     *
     * @return description
     */
    @Override
    public String getScope()
    {
        return null;
    }


    /**
     * Returns the unique name for the role.
     *
     * @return identifier
     */
    @Override
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Returns a descriptive name of the role.
     *
     * @return display name
     */
    @Override
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Returns a detailed description of the role.
     *
     * @return description
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
        return "OpenMetadataRoleDefinition{" + "identifier='" + identifier + '}';
    }
}
