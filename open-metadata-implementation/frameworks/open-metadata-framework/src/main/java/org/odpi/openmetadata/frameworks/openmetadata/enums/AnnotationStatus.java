/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * An AnnotationStatus defines the current status for an annotation plus some default descriptive text.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AnnotationStatus implements OpenMetadataEnum
{
    /**
     * AnnotationProperties has been created but not reviewed.
     */
    NEW_ANNOTATION      (0,   "4a4cd94d-e407-4218-9d5b-fbc1a68a2d37", "New",      "AnnotationProperties has been created but not reviewed", false),

    /**
     * AnnotationProperties has been reviewed by no decision has been made.
     */
    REVIEWED_ANNOTATION (1,   "273cb963-2145-470a-9040-7634c8f30f0b",   "Reviewed", "AnnotationProperties has been reviewed by no decision has been made", false),

    /**
     * AnnotationProperties has been approved.
     */
    APPROVED_ANNOTATION (2,   "c5447bdb-d1dc-4d6a-811b-b98845ca19da",   "Approved", "AnnotationProperties has been approved", false),

    /**
     * AnnotationProperties has been approved and insight has been added to Asset's metadata.
     */
    ACTIONED_ANNOTATION (3,   "f561ac14-d6ea-44d7-a432-8f0dd2e40123",    "Actioned", "AnnotationProperties has been approved and insight has been added to Asset's metadata", false),

    /**
     * AnnotationProperties has been reviewed and declared invalid.
     */
    INVALID_ANNOTATION  (4,   "6b9bc674-24ce-466f-8647-a2a303813341",    "Invalid",  "AnnotationProperties has been reviewed and declared invalid", false),

    /**
     * AnnotationProperties is invalid and should be ignored.
     */
    IGNORE_ANNOTATION   (5,   "90949088-ca0f-4bd9-9936-ee6b343f5e94",   "Ignore",   "AnnotationProperties is invalid and should be ignored", false),

    /**
     * AnnotationProperties's status stored in additional properties
     */
    OTHER_STATUS        (98,   "e16ec89e-f2af-4b63-bbac-a286ffdba771",  "Other",    "AnnotationProperties's status stored in additional properties", false),

    /**
     * AnnotationProperties has not had a status assigned
     */
    UNKNOWN_STATUS      (99,   "691a566f-27a7-4106-817a-9f04fad870ab", "Unknown",  "AnnotationProperties has not had a status assigned", false);


    private static final String ENUM_TYPE_GUID  = "71187df6-ef66-4f88-bc03-cd3c7f925165";
    private static final String ENUM_TYPE_NAME  = "AnnotationStatus";
    private static final String ENUM_DESCRIPTION  = "Defines the status of an annotation.";
    private static final String ENUM_DESCRIPTION_GUID  = "a2671bf6-5eef-4432-a189-1e99d84507e1";
    private static final String ENUM_DESCRIPTION_WIKI  = OpenMetadataWikiPages.MODEL_0612_ANNOTATION_REVIEWS;

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
    AnnotationStatus(int     ordinal,
                     String  descriptionGUID,
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AnnotationStatus : " + name;
    }
}