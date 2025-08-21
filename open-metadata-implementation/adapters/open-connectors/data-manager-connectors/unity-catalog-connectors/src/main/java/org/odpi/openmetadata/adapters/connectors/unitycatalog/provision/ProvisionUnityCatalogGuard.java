/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.provision;

import org.odpi.openmetadata.frameworks.opengovernance.controls.GuardType;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CompletionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * The Guard enum describes some common guards that can be used when implementing governance services.
 * Using the common guard names where possible saves time when coding the provider class and improves the
 * ability of the service to be incorporated into governance action processes.
 */
public enum ProvisionUnityCatalogGuard
{
    SET_UP_COMPLETE("set-up-complete", CompletionStatus.ACTIONED, "The element has been defined in Egeria.  The resource will be provisioned to Unity Catalog as soon as the integration connector receives a notification of the new asset - or it performs a refresh.."),
    NO_TECHNOLOGY_TYPE("no-technology-type", CompletionStatus.INVALID, "The technology type defining which unity catalog resource has not been provided."),
    INVALID_TECHNOLOGY_TYPE("invalid-technology-type", CompletionStatus.INVALID, "The technology type defining which unity catalog resource is not set to a supported value."),
    MISSING_PLACEHOLDER_VALUES("missing-placeholder-values", CompletionStatus.INVALID, "There are missing placeholder property values in the request parameters."),
    SERVICE_FAILED("service-failed", CompletionStatus.FAILED, "An unexpected error occurred while the governance service was running.  Messages are logged to the audit log explaining the source of the error."),
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
    ProvisionUnityCatalogGuard(String name, CompletionStatus completionStatus, String description)
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

        for (ProvisionUnityCatalogGuard guard : ProvisionUnityCatalogGuard.values())
        {
            GuardType guardType = new GuardType();

            guardType.setGuard(guard.getName());
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

        guardType.setGuard(name);
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
