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
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * The GlossaryTermAssignmentStatus defines the status of the relationship between a glossary term and an element that represents data.
 * It indicates how much trust a steward or process should give to the relationship based on its source.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GlossaryTermAssignmentStatus
{
    /**
     * Discovered - The term assignment was discovered by an automated process.
     */
    DISCOVERED ("6bfd3f33-e550-4575-a29f-7199f5865615",0,  "Discovered","The term assignment was discovered by an automated process.", false),

    /**
     * Proposed - The term assignment was proposed by a subject-matter expert.
     */
    PROPOSED  ("2c939678-7381-44ba-beec-f30791e0b560",1,  "Proposed",  "The term assignment was proposed by a subject-matter expert.", false),

    /**
     * Imported - The term assignment was imported from another metadata system.
     */
    IMPORTED  ("ac932f9e-a573-4c70-a49f-56830a5a535a",2,  "Imported",  "The term assignment was imported from another metadata system.", false),

    /**
     * Validated - The term assignment has been validated and approved by a subject-matter expert.
     */
    VALIDATED  ("18b3d426-e406-4098-ba6a-bb1e42318b21",3,  "Validated",  "The term assignment has been validated and approved by a subject-matter expert.", false),

    /**
     * Deprecated - The term assignment is out of date and should not be used.
     */
    DEPRECATED("6234a509-6d96-4450-adaf-d97918ec8646",4, "Deprecated","The term assignment should no longer be used.", false),

    /**
     * Obsolete - The term assignment must no longer be used.
     */
    OBSOLETE("289ffbed-3fb7-4e6a-a4e1-8f11e83bd1ac",5, "Obsolete","The term assignment must no longer be used.", false),

    /**
     * Other - The term assignment is in a locally defined state.
     */
    OTHER     ("74cfc2eb-4669-4780-a9b7-ca49d3661630",99, "Other",     "The term assignment is in a locally defined state.", false),
    ;

    private static final String ENUM_TYPE_GUID  = "c8fe36ac-369f-4799-af75-46b9c1343ab3";
    private static final String ENUM_TYPE_NAME  = "TermAssignmentStatus";

    private static final String ENUM_DESCRIPTION = "Defines the provenance and confidence of a term assignment.";
    private static final String ENUM_DESCRIPTION_GUID = "ae0313dd-921d-4a1f-a71c-b234e1b2e543";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0370_SEMANTIC_ASSIGNMENT;

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
    GlossaryTermAssignmentStatus(String  descriptionGUID,
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
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return  guid
     */
    public  String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
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
                                                OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.name,
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
        return constructValidValueCategory(ENUM_TYPE_NAME,
                                           OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.name,
                                           null);
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "GlossaryTermAssignmentStatus{name='" + name + '}';
    }}
