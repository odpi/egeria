/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

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
    NEW_ANNOTATION      (0,  "New",      "Annotation has been created but not reviewed"),
    REVIEWED_ANNOTATION (1,  "Reviewed", "Annotation has been reviewed by no decision has been made"),
    APPROVED_ANNOTATION (2,  "Approved", "Annotation has been approved"),
    ACTIONED_ANNOTATION (3,  "Actioned", "Annotation has been approved and insight has been added to Asset's metadata"),
    INVALID_ANNOTATION  (4,  "Invalid",  "Annotation has been reviewed and declared invalid"),
    IGNORE_ANNOTATION   (5,  "Ignore",   "Annotation is invalid and should be ignored"),
    OTHER_STATUS        (98, "Other",    "Annotation's status stored in additional properties"),
    UNKNOWN_STATUS      (99, "Unknown",  "Annotation has not had a status assigned");

    private static final long     serialVersionUID = 1L;

    private int            statusCode;
    private String         statusName;
    private String         statusDescription;


    /**
     * Typical Constructor
     */
    AnnotationStatus(int     statusCode, String   statusName,  String   statusDescription)
    {
        /*
         * Save the values supplied
         */
        this.statusCode = statusCode;
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
     * REturn the default description for the status for this enum instance.
     *
     * @return String default status description
     */
    public String getDescription()
    {
        return statusDescription;
    }


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
                '}';
    }
}