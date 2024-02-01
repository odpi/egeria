/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataWikiPages;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * An AnnotationStatus defines the current status for an annotation plus some default descriptive text.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AnnotationStatus implements Serializable
{
    /**
     * Annotation has been created but not reviewed.
     */
    NEW_ANNOTATION      (0,   "New",      "Annotation has been created but not reviewed", "9f7e13d1-ec51-4c95-9931-383b3ab9295b"),

    /**
     * Annotation has been reviewed by no decision has been made.
     */
    REVIEWED_ANNOTATION (1,   "Reviewed", "Annotation has been reviewed by no decision has been made", "64730138-399a-4f30-a0b8-1f6cc487cb53"),

    /**
     * Annotation has been approved.
     */
    APPROVED_ANNOTATION (2,   "Approved", "Annotation has been approved", "91b5c627-2c3a-4598-af72-e1b52f1f03c5"),

    /**
     * Annotation has been approved and insight has been added to Asset's metadata.
     */
    ACTIONED_ANNOTATION (3,    "Actioned", "Annotation has been approved and insight has been added to Asset's metadata", "b848b720-4171-4d28-9e4c-1d34f51aaf5f"),

    /**
     * Annotation has been reviewed and declared invalid.
     */
    INVALID_ANNOTATION  (4,    "Invalid",  "Annotation has been reviewed and declared invalid", "c65a21dc-d1ae-4a8f-ba33-58ec401c1b42"),

    /**
     * Annotation is invalid and should be ignored.
     */
    IGNORE_ANNOTATION   (5,   "Ignore",   "Annotation is invalid and should be ignored", "519cf063-9386-42e5-88dd-c5baab234df6"),

    /**
     * Annotation's status stored in additional properties
     */
    OTHER_STATUS        (98,  "Other",    "Annotation's status stored in additional properties", "fd4b6779-9582-4cc2-885e-72e44445ff04"),

    /**
     * Annotation has not had a status assigned
     */
    UNKNOWN_STATUS      (99, "Unknown",  "Annotation has not had a status assigned", "a9fc9231-f04a-40c4-99b1-4a1058063f5e");

    private static final long     serialVersionUID = 1L;

    private static final String ENUM_TYPE_GUID  = "71187df6-ef66-4f88-bc03-cd3c7f925165";
    private static final String ENUM_TYPE_NAME  = "AnnotationStatus";
    private static final String ENUM_TYPE_DESCRIPTION  = "Defines the status of an annotation.";
    private static final String ENUM_TYPE_DESCRIPTION_GUID  = "a2671bf6-5eef-4432-a189-1e99d84507e1";
    private static final String ENUM_TYPE_WIKI_URL  = OpenMetadataWikiPages.MODEL_0612_ANNOTATION_REVIEWS;

    private final int    statusCode;
    private final String statusName;
    private final String statusDescription;
    private final String statusDescriptionGUID;


    /**
     * Typical Constructor
     *
     * @param statusCode ordinal
     * @param statusName short name
     * @param statusDescription longer explanation
     * @param statusDescriptionGUID unique identifier of valid value description
     */
    AnnotationStatus(int    statusCode,
                     String statusName,
                     String statusDescription,
                     String statusDescriptionGUID)
    {
        this.statusCode = statusCode;
        this.statusName = statusName;
        this.statusDescription = statusDescription;
        this.statusDescriptionGUID = statusDescriptionGUID;
    }


    /**
     * Return the status code for this enum instance
     *
     * @return int status code
     */
    public int getOrdinal()
    {
        return statusCode;
    }


    /**
     * Return the default name for the status for this enum instance.
     *
     * @return String default status name
     */
    public String getName()
    {
        return statusName;
    }


    /**
     * Return the default description for the status for this enum instance.
     *
     * @return String default status description
     */
    public String getDescription()
    {
        return statusDescription;
    }


    /**
     * Return the unique identifier fort the valid value for the status for this enum instance.
     *
     * @return String default status description
     */
    public String getDescriptionGUID()
    {
        return statusDescriptionGUID;
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
     * @return string text
     */
    public static String getOpenTypeDescription() { return ENUM_TYPE_DESCRIPTION; }


    /**
     * Return the unique identifier of the valid value for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeDescriptionGUID() { return ENUM_TYPE_DESCRIPTION_GUID; }


    /**
     * Return the description for the open metadata enum type that this enum class represents.
     *
     * @return string url
     */
    public static String getOpenTypeWikiURL() { return ENUM_TYPE_WIKI_URL; }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AnnotationStatus{" +
                "statusCode=" + statusCode +
                ", statusName='" + statusName + '\'' +
                ", statusDescription='" + statusDescription + '\'' +
                ", statusDescriptionGUID='" + statusDescriptionGUID + '\'' +
                "}";
    }
}