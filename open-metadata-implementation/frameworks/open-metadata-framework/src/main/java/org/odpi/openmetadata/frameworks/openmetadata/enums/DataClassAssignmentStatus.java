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
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * The DataClassAssignmentStatus defines the status of the relationship between a data class and an element that represents data.
 * It indicates how much trust a steward or process should give to the relationship based on its source.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DataClassAssignmentStatus implements OpenMetadataEnum
{
    /**
     * Discovered - The term assignment was discovered by an automated process.
     */
    DISCOVERED ("4074e762-ef65-4705-aa37-940b42bde97c",0,  "Discovered","The term assignment was discovered by an automated process.", false),

    /**
     * Proposed - The term assignment was proposed by a subject-matter expert.
     */
    PROPOSED  ("cf3477d9-8373-4894-b7e1-63722faca3cb",1,  "Proposed",  "The term assignment was proposed by a subject-matter expert.", false),

    /**
     * Imported - The term assignment was imported from another metadata system.
     */
    IMPORTED  ("2fae7977-e463-4f03-a9fe-a3765faf8f5e",2,  "Imported",  "The term assignment was imported from another metadata system.", false),

    /**
     * Validated - The term assignment has been validated and approved by a subject-matter expert.
     */
    VALIDATED  ("b9dc8d2c-342e-4596-aa66-34c133731017",3,  "Validated",  "The term assignment has been validated and approved by a subject-matter expert.", false),

    /**
     * Deprecated - The term assignment is out of date and should not be used.
     */
    DEPRECATED("519ca599-6675-4675-b9ce-e71262714216",4, "Deprecated","The term assignment should no longer be used.", false),

    /**
     * Obsolete - The term assignment must no longer be used.
     */
    OBSOLETE("cc18444b-c202-4c13-80fd-4caea1d2549e",5, "Obsolete","The term assignment must no longer be used.", false),

    /**
     * Other - The term assignment is in a locally defined state.
     */
    OTHER     ("6e5493c7-cd87-4de6-9570-6c9471f56f1f",99, "Other",     "The term assignment is in a locally defined state.", false),
    ;

    private static final String ENUM_TYPE_GUID  = "2611892f-0527-478f-8843-a3aa2b9abb47";
    private static final String ENUM_TYPE_NAME  = "DataClassAssignmentStatus";

    private static final String ENUM_DESCRIPTION = "Defines the provenance and confidence of a data class assignment.";
    private static final String ENUM_DESCRIPTION_GUID = "36d8a171-1b8d-4f7e-88f1-f89b9566f33a";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0540_DATA_CLASSES;

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
    DataClassAssignmentStatus(String  descriptionGUID,
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
                                                OpenMetadataProperty.DATA_CLASS_ASSIGNMENT_STATUS.name,
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
        return constructValidValueNamespace(ENUM_TYPE_NAME,
                                            OpenMetadataProperty.DATA_CLASS_ASSIGNMENT_STATUS.name,
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
        return "DataClassAssignmentStatus{name='" + name + '}';
    }
}
