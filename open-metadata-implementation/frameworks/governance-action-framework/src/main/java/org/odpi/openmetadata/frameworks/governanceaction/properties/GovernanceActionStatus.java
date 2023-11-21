/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionStatus defines the current status for a governance action.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@Deprecated
public enum GovernanceActionStatus
{
    /**
     * The governance action has been created and is pending.
     */
    REQUESTED       (0,  0,  "Requested",  "The governance action has been created and is pending.", EngineActionStatus.REQUESTED),

    /**
     * The governance action is approved to run.
     */
    APPROVED        (1,  1,  "Approved",   "The governance action is approved to run.", EngineActionStatus.APPROVED),

    /**
     * The governance action is waiting for its start time or the right conditions to run.
     */
    WAITING         (2,  2,  "Waiting",    "The governance action is waiting for its start time or the right conditions to run.", EngineActionStatus.WAITING),

    /**
     * The governance action service for the governance action is being initialized in the governance engine.
     */
    ACTIVATING      (3,  3,  "Activating", "The governance action service for the governance action is being initialized in the governance engine.", EngineActionStatus.ACTIVATING),

    /**
     * The governance engine is running the associated governance action service for the governance action.
     */
    IN_PROGRESS     (4,  4,  "In Progress","The governance engine is running the associated governance action service for the governance action.", EngineActionStatus.IN_PROGRESS),

    /**
     * The governance action service for the governance action has successfully completed processing.
     */
    ACTIONED        (5,  10, "Actioned",   "The governance action service for the governance action has successfully completed processing.", EngineActionStatus.ACTIONED),

    /**
     * The governance action has not been run because it is not appropriate (for example, a false positive).
     */
    INVALID         (6,  11, "Invalid",    "The governance action has not been run because it is not appropriate (for example, a false positive).", EngineActionStatus.INVALID),

    /**
     * The governance action has not been run because a different governance action was chosen.
     */
    IGNORED         (7,  12, "Ignored",    "The governance action has not been run because a different governance action was chosen.", EngineActionStatus.IGNORED),

    /**
     * The governance action service for the governance action failed to execute.
     */
    FAILED          (8,  13, "Failed",     "The governance action service for the governance action failed to execute.", EngineActionStatus.FAILED),

    /**
     * Undefined or unknown governance action status.
     */
    OTHER           (99, 99, "Other",      "Undefined or unknown governance action status.", EngineActionStatus.OTHER);

    private static final String ENUM_TYPE_GUID  = "a6e698b0-a4f7-4a39-8c80-db0bb0f972e";
    private static final String ENUM_TYPE_NAME  = "EngineActionStatus";

    private final String statusName;
    private final String statusDescription;
    private final int    statusCode;

    private final int openTypeOrdinal;

    private final EngineActionStatus engineActionStatus;

    /**
     * Typical Constructor
     *
     * @param statusCode ordinal
     * @param openTypeOrdinal ordinal used in the open metadata types
     * @param statusName short name
     * @param statusDescription longer explanation
     * @param engineActionStatus equivalent engine action status
     */
    GovernanceActionStatus(int                statusCode,
                           int                openTypeOrdinal,
                           String             statusName,
                           String             statusDescription,
                           EngineActionStatus engineActionStatus)
    {
        this.statusCode        = statusCode;
        this.openTypeOrdinal   = openTypeOrdinal;
        this.statusName        = statusName;
        this.statusDescription = statusDescription;
        this.engineActionStatus = engineActionStatus;
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
     * Return the equivalent action status.
     *
     * @return enum
     */
    public EngineActionStatus getEngineActionStatus()
    {
        return engineActionStatus;
    }


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