/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CompletionStatus defines the completion status of a governance action service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CompletionStatus implements Serializable
{
    ACTIONED        (5,  10, "Actioned",   "The governance action service for the governance action has successfully completed processing"),
    INVALID         (6,  11, "Invalid",    "The governance action service has not performed the requested action because it is not appropriate (for example, a false positive)"),
    FAILED          (8,  13, "Failed",     "The governance action service failed to execute the requested action"),
    OTHER           (99, 99, "Other",      "Undefined or unknown completion status");

    private static final long     serialVersionUID = 1L;

    private static final String ENUM_TYPE_GUID  = "a6e698b0-a4f7-4a39-8c80-db0bb0f972e";
    private static final String ENUM_TYPE_NAME  = "GovernanceActionStatus";

    private String statusName;
    private String statusDescription;
    private int    statusCode;

    private int openTypeOrdinal;

    /**
     * Typical Constructor
     *
     * @param statusCode ordinal
     * @param statusName short name
     * @param statusDescription longer explanation
     */
    CompletionStatus(int    statusCode,
                     int    openTypeOrdinal,
                     String statusName,
                     String statusDescription)
    {
        this.statusCode        = statusCode;
        this.openTypeOrdinal   = openTypeOrdinal;
        this.statusName        = statusName;
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
        return "CompletionStatus{" +
                "statusName='" + statusName + '\'' +
                ", statusDescription='" + statusDescription + '\'' +
                ", statusCode=" + statusCode +
                ", openTypeOrdinal=" + openTypeOrdinal +
                '}';
    }
}