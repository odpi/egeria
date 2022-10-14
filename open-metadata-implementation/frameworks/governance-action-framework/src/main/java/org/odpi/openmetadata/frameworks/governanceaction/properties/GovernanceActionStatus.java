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
 * GovernanceActionStatus defines the current status for a governance action.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GovernanceActionStatus implements Serializable
{
    REQUESTED       (0,  0,  "Requested",  "The governance action has been created and is pending"),
    APPROVED        (1,  1,  "Approved",   "The governance action is approved to run"),
    WAITING         (2,  2,  "Waiting",    "The governance action is waiting for its start time or the right conditions to run"),
    ACTIVATING      (3,  3,  "Activating", "The governance action service for the governance action is being initialized in the governance engine"),
    IN_PROGRESS     (4,  4,  "In Progress","The governance engine is running the associated governance action service for the governance action"),
    ACTIONED        (5,  10, "Actioned",   "The governance action service for the governance action has successfully completed processing"),
    INVALID         (6,  11, "Invalid",    "The governance action has not been run because it is not appropriate (for example, a false positive)"),
    IGNORED         (7,  12, "Ignored",    "The governance action has not been run because a different governance action was chosen"),
    FAILED          (8,  13, "Failed",     "The governance action service for the governance action failed to execute"),
    OTHER           (99, 99, "Other",      "Undefined or unknown governance action status");

    private static final long     serialVersionUID = 1L;

    private static final String ENUM_TYPE_GUID  = "a6e698b0-a4f7-4a39-8c80-db0bb0f972e";
    private static final String ENUM_TYPE_NAME  = "GovernanceActionStatus";

    private final String statusName;
    private final String statusDescription;
    private final int    statusCode;

    private final int openTypeOrdinal;

    /**
     * Typical Constructor
     *
     * @param statusCode ordinal
     * @param statusName short name
     * @param statusDescription longer explanation
     */
    GovernanceActionStatus(int    statusCode,
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
        return "GovernanceActionStatus{" + statusName + "}";
    }
}