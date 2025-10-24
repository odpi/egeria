/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * GovernanceClassificationStatus identifies the status of one of the governance action classification.
 * It provides some provenance on the process that assigned the value.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GovernanceClassificationStatus implements OpenMetadataEnum
{
    /**
     * The classification assignment was discovered by an automated process.
     */
    DISCOVERED ("fa86c9f2-00d4-4acd-a232-1d58ee19703c", 0,"Discovered",
                               "The classification assignment was discovered by an automated process.", false),

    /**
     * The classification assignment was proposed by a subject-matter expert.
     */
    PROPOSED   ("ba53bff2-1c9c-4b49-8ab2-c2403354726b", 1,"Proposed",
                               "The classification assignment was proposed by a subject matter expert.", false),

    /**
     * The classification assignment was imported from another metadata system.
     */
    IMPORTED   ("e64e8337-b5cb-40e9-b1af-1aed1ee54ea1", 2,"Imported",
                               "The classification assignment was imported from another metadata system.", false),

    /**
     * The classification assignment has been validated and approved by a subject-matter-expert.
     */
    VALIDATED  ("f0e4b19e-8f08-4bfd-91aa-8070bce01b69", 3,"Validated",
                               "The classification assignment has been validated and approved by a subject matter expert.", false),

    /**
     * The classification assignment should no longer be used.
     */
    DEPRECATED ("42db10ad-3e9e-4e7c-875a-3b095f9f0434", 4,"Deprecated",
                               "The classification assignment should no longer be used.", false),

    /**
     * The classification assignment must no longer be used.
     */
    OBSOLETE   ("850930ea-63f5-4dbe-ba1d-e970c6e3e26c", 5,"Obsolete",
                               "The classification assignment must no longer be used.", false),

    /**
     * Another classification assignment status.
     */
    OTHER     ("d6bfaecb-de02-4f86-a318-a189e9a5aa5a", 99, "Other",
                               "Another classification assignment status.", false);

    private static final String ENUM_TYPE_GUID  = "cc540586-ac7c-41ba-8cc1-4da694a6a8e4";
    private static final String ENUM_TYPE_NAME  = "GovernanceClassificationStatus";

    private static final String ENUM_DESCRIPTION = "Defines the status values of a governance action classification.";
    private static final String ENUM_DESCRIPTION_GUID = "34fd0e5d-4ecd-4939-8245-054872c188d9";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0421_GOVERNANCE_CLASSIFICATION;

    private final String descriptionGUID;

    private final int            ordinal;
    private final String         name;
    private final String         description;
    private final boolean        isDefault;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param isDefault is this the default value for the enum?
     */
    GovernanceClassificationStatus(String  descriptionGUID,
                                   int     ordinal,
                                   String  name,
                                   String  description,
                                   boolean isDefault)
    {
        this.ordinal = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
        this.isDefault = isDefault;
    }



    /**
     * Return the numeric representation of the enumeration.
     *
     * @return int ordinal
     */
    @Override
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    @Override
    public String getName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    @Override
    public String getDescription() { return description; }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return  guid
     */
    @Override
    public  String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
    @Override
    public boolean isDefault()
    {
        return isDefault;
    }

    /**
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public static String getOpenTypeName() { return ENUM_TYPE_NAME; }


    /**
     * Return the description for the open metadata enum type that this enum class represents.
     *
     * @return string description
     */
    public static String getOpenTypeDescription()
    {
        return ENUM_DESCRIPTION;
    }


    /**
     * Return the unique identifier for the valid value element for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeDescriptionGUID()
    {
        return ENUM_DESCRIPTION_GUID;
    }


    /**
     * Return the unique identifier for the valid value element for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeDescriptionWiki()
    {
        return ENUM_DESCRIPTION_WIKI;
    }


    /**
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(ENUM_TYPE_NAME,
                                                OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                null,
                                                name);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GovernanceClassificationStatus{name='" + name + '}';
    }
}
