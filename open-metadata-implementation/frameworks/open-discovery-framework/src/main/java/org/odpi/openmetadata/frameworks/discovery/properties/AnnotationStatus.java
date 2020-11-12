/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
    NEW_ANNOTATION      (0,  0,  "New",      "Annotation has been created but not reviewed"),
    REVIEWED_ANNOTATION (1,  1,  "Reviewed", "Annotation has been reviewed by no decision has been made"),
    APPROVED_ANNOTATION (2,  2,  "Approved", "Annotation has been approved"),
    ACTIONED_ANNOTATION (3,  3,  "Actioned", "Annotation has been approved and insight has been added to Asset's metadata"),
    INVALID_ANNOTATION  (4,  4,  "Invalid",  "Annotation has been reviewed and declared invalid"),
    IGNORE_ANNOTATION   (5,  5,  "Ignore",   "Annotation is invalid and should be ignored"),
    OTHER_STATUS        (98, 99, "Other",    "Annotation's status stored in additional properties"),
    UNKNOWN_STATUS      (99, 0,  "Unknown",  "Annotation has not had a status assigned");

    private static final long     serialVersionUID = 1L;

    public static final String ENUM_TYPE_GUID  = "71187df6-ef66-4f88-bc03-cd3c7f925165";
    public static final String ENUM_TYPE_NAME  = "AnnotationStatus";

    private int    statusCode;
    private int    openTypeOrdinal;
    private String statusName;
    private String statusDescription;


    /**
     * Typical Constructor
     *
     * @param statusCode ordinal
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param statusName short name
     * @param statusDescription longer explanation
     */
    AnnotationStatus(int    statusCode,
                     int    openTypeOrdinal,
                     String statusName,
                     String statusDescription)
    {
        this.statusCode = statusCode;
        this.openTypeOrdinal = openTypeOrdinal;
        this.statusName = statusName;
        this.statusDescription = statusDescription;
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
     * Return the code for this enum that comes from the Open Metadata Type that this enum represents.
     *
     * @return int code number
     */
    public int getOpenTypeOrdinal()
    {
        return openTypeOrdinal;
    }


    /**
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public String getOpenTypeName() { return ENUM_TYPE_NAME; }


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
                ", openTypeOrdinal=" + openTypeOrdinal +
                ", statusName='" + statusName + '\'' +
                ", statusDescription='" + statusDescription + '\'' +
                '}';
    }
}