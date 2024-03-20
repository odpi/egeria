/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EngineActionStatus defines the current status for a engine action.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum EngineActionStatus
{
    /**
     * The engine action has been created and is pending.
     */
    REQUESTED       (0,  0,  "Requested",  "The engine action has been created and is pending."),

    /**
     * The engine action is approved to run. This means that the mandatory guards have been satisfied.
     */
    APPROVED        (1,  1,  "Approved",   "The engine action is approved to run.  This means that the mandatory guards have been satisfied."),

    /**
     * The engine action is waiting for its start time or the engine host to claim it.
     */
    WAITING         (2,  2,  "Waiting",    "The engine action is waiting for its start time or the right conditions to run."),

    /**
     * The governance service for the engine action is being initialized in the governance engine.
     */
    ACTIVATING      (3,  3,  "Activating", "The governance service for the engine action is being initialized in the governance engine."),

    /**
     * The governance engine is running the associated governance service for the engine action.
     */
    IN_PROGRESS     (4,  4,  "In Progress","The governance engine is running the associated governance service for the engine action."),

    /**
     * The governance service for the engine action has successfully completed processing.
     */
    ACTIONED        (5,  10, "Actioned",   "The governance service for the engine action has successfully completed processing."),

    /**
     * The engine action has not been run because it is not appropriate (for example, a false positive).
     */
    INVALID         (6,  11, "Invalid",    "The engine action has not been run because it is not appropriate (for example, a false positive)."),

    /**
     * The engine action has not been run because a different engine action was chosen.
     */
    IGNORED         (7,  12, "Ignored",    "The engine action has not been run because a different engine action was chosen."),

    /**
     * The governance service for the engine action failed to execute.
     */
    FAILED          (8,  13, "Failed",     "The governance service for the engine action failed to execute."),

    /**
     * The engine action was cancelled by an external caller.
     */
    CANCELLED       (9,  14, "Cancelled",     "The engine action was cancelled by an external caller."),

    /**
     * Undefined or unknown engine action status.
     */
    OTHER           (99, 99, "Other",      "Undefined or unknown engine action status.");

    private static final String ENUM_TYPE_GUID  = "a6e698b0-a4f7-4a39-8c80-db0bb0f972e";
    private static final String ENUM_TYPE_NAME  = "EngineActionStatus";

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
    EngineActionStatus(int    statusCode,
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
        return "EngineActionStatus{" + statusName + "}";
    }
}