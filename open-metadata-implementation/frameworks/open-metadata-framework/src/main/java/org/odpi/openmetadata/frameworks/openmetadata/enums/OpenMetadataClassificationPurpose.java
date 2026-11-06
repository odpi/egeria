/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Describes why a classification type exists.  The purpose is informational for the repositories.
 * It helps tools decide which classifications to show to which audience, and which classifications
 * are relevant when deciding how an element should be governed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OpenMetadataClassificationPurpose implements OpenMetadataEnum
{
    /**
     * Used by Egeria to manage the element, for example its anchor, zone membership, template status, or whether it is a memento or a known duplicate.
     */
    MANAGEMENT(0, "26ecfb80-9c51-475a-a1ed-6c151a68f5d3", "Management",
               "Used by Egeria to manage the element, for example its anchor, zone membership, template status, or whether it is a memento or a known duplicate.",
               false),

    /**
     * Statistics and profiles gathered about the element and its use, typically maintained by Egeria's own services such as the nanny connectors.
     */
    ANALYTICAL(1, "576dee44-fc2b-4c29-93cd-c1b29d73038d", "Analytical",
               "Statistics and profiles gathered about the element and its use, typically maintained by Egeria's own services such as the nanny connectors.",
               false),

    /**
     * Describes the governance characteristics of the element, such as its owner, confidentiality, criticality, or retention, which determine the governance actions that are appropriate.
     */
    GOVERNANCE(2, "480820c1-5a38-48ae-a2df-13ca0af827d7", "Governance",
               "Describes the governance characteristics of the element, such as its owner, confidentiality, criticality, or retention, which determine the governance actions that are appropriate.",
               false),

    /**
     * Identifies the element as a place where governance is implemented, such as a control, verification, or enforcement point, or a policy management point.
     */
    GOVERNANCE_IMPLEMENTATION(3, "5a2d128d-6704-42a1-8e24-c1110ac4bae2", "GovernanceImplementation",
                              "Identifies the element as a place where governance is implemented, such as a control, verification, or enforcement point, or a policy management point.",
                              false),

    /**
     * Refines the kind of element, such as the kind of project, collection, location, log, or glossary term.
     */
    CATEGORY(4, "78178dcb-04fe-43da-82ec-de4d8a060349", "Category",
             "Refines the kind of element, such as the kind of project, collection, location, log, or glossary term.",
             true),

    /**
     * Adds detail about the shape or encoding of the data that the element describes, such as a primary key or a calculated value.
     */
    DESCRIPTIVE(5, "acfc0b09-f3d1-4354-84a6-03c916a41c3c", "Descriptive",
                "Adds detail about the shape or encoding of the data that the element describes, such as a primary key or a calculated value.",
                false),
    ;


    private static final String ENUM_TYPE_GUID  = "67302767-27f1-4bf1-9f4c-783d62e933a3";
    private static final String ENUM_TYPE_NAME  = "OpenMetadataClassificationPurpose";

    private static final String ENUM_DESCRIPTION = "Describes why a classification type exists.";
    private static final String ENUM_DESCRIPTION_GUID = "e196246f-882a-4390-b5f3-98d47bd67bec";
    private static final String ENUM_DESCRIPTION_WIKI = "https://egeria-project.org/concepts/classification-purpose/";

    private final int     ordinal;
    private final String  name;
    private final String  description;
    private final String  descriptionGUID;
    private final boolean isDefault;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param isDefault is this the default value for the enum?
     */
    OpenMetadataClassificationPurpose(int     ordinal,
                                      String  descriptionGUID,
                                      String  name,
                                      String  description,
                                      boolean isDefault)
    {
        this.ordinal         = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
        this.isDefault       = isDefault;
    }


    /**
     * Return the purpose with the supplied enum name (for example, as stored in a ClassificationDef).
     *
     * @param purposeName name of the enum value
     * @return matching enum value or null if the name is null or not recognized
     */
    public static OpenMetadataClassificationPurpose fromName(String purposeName)
    {
        if (purposeName != null)
        {
            for (OpenMetadataClassificationPurpose purpose : OpenMetadataClassificationPurpose.values())
            {
                if (purpose.name().equals(purposeName))
                {
                    return purpose;
                }
            }
        }

        return null;
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
    public String getDisplayName() { return name; }


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
     * Return the link to the documentation for the open metadata enum type that this enum class represents.
     *
     * @return string url
     */
    public static String getOpenTypeDescriptionWiki()
    {
        return ENUM_DESCRIPTION_WIKI;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenMetadataClassificationPurpose{" +
            "ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", descriptionGUID='" + descriptionGUID + '\'' +
            ", isDefault=" + isDefault +
            '}';
    }
}
