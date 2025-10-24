/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.verification;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.GuardType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CompletionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * The Guard enum describes some common guards that can be used when implementing governance services.
 * Using the common guard names where possible saves time when coding the provider class and improves the
 * ability of the service to be incorporated into governance action processes.
 */
public enum VerifyAssetGuard
{
    NO_ZONES                     ( "no-zones", CompletionStatus.ACTIONED, "There are no governance zones assigned to this Asset"),
    NO_OWNER                     ( "no-owner", CompletionStatus.ACTIONED, "There is no owner assigned to this Asset"),
    NO_ORIGIN                    ( "no-origin", CompletionStatus.FAILED, "There are no origin properties assigned to this Asset"),
    ZONES_ASSIGNED               ( "zones-assigned", CompletionStatus.ACTIONED, "Governance zones are assigned to this asset."),
    OWNER_ASSIGNED               ( "owner-assigned", CompletionStatus.ACTIONED, "An owner is assigned to this asset."),
    ORIGIN_ASSIGNED              ( "origin-assigned", CompletionStatus.ACTIONED, "Origin properties are assigned to this asset."),
    VERIFICATION_FAILED          ( "verification-failed", CompletionStatus.FAILED, "An unexpected error occurred during the verification process."),
    NO_TARGETS_DETECTED          ( "no-targets-detected", CompletionStatus.INVALID, "There is no supplied action target and so the governance service does not know which asset to work on."),
    TARGET_NOT_ASSET             ( "target-not-asset", CompletionStatus.INVALID, "The action target is not an asset."),
    MULTIPLE_TARGETS_DETECTED    ( "multiple-targets-detected", CompletionStatus.INVALID, "Multiple action targets supplied.  This governance service does not support multiple action targets because the result of the origin search could be different for each action target making it difficult to automate the response."),

    ;


    public final String           name;
    public final CompletionStatus completionStatus;
    public final String           description;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the guard
     * @param completionStatus associated completion status
     * @param description description of the guard
     */
    VerifyAssetGuard(String name, CompletionStatus completionStatus, String description)
    {
        this.name             = name;
        this.completionStatus = completionStatus;
        this.description      = description;
    }


    /**
     * Return the name of the guard.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the typical completion status used with this guard.
     *
     * @return completion status
     */
    public CompletionStatus getCompletionStatus()
    {
        return completionStatus;
    }


    /**
     * Return the description of the guard.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return details of all defined guards.
     *
     * @return guard types
     */
    public static List<GuardType> getGuardTypes()
    {
        List<GuardType> guardTypes = new ArrayList<>();

        for (VerifyAssetGuard guard : VerifyAssetGuard.values())
        {
            GuardType guardType = new GuardType();

            guardType.setName(guard.getName());
            guardType.setDescription(guard.getDescription());
            guardType.setCompletionStatus(guard.getCompletionStatus());

            guardTypes.add(guardType);
        }

        return guardTypes;
    }


    /**
     * Return details of a specific guard.
     *
     * @return guard type
     */
    public GuardType getGuardType()
    {
        GuardType guardType = new GuardType();

        guardType.setName(name);
        guardType.setDescription(description);
        guardType.setCompletionStatus(completionStatus);

        return guardType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "Guard{ name='" + name + "}";
    }
}
