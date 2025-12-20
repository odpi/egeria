/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.contentpacks.core;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;

/**
 * Describes the engine actions that auto-start governance services in the core content packs.
 */
public enum EngineActionDefinition
{
    KARMA_POINTS("2d23a09b-0cbf-44d3-b2ad-d818d070561f",
                 "Egeria:" + OpenMetadataType.ENGINE_ACTION.typeName + ":AwardKarmaPoints",
                 "Award Karma Points Action",
                 RequestTypeDefinition.AWARD_KARMA_POINTS,
                 null,
                 "egeria",
                 ContentPackDefinition.ORGANIZATION_INSIGHT_CONTENT_PACK),


    ;

    private final String                guid;
    private final String                qualifiedName;
    private final String                displayName;
    private final String                requesterUserId;
    private final ContentPackDefinition contentPackDefinition;
    private final RequestTypeDefinition requestTypeDefinition;
    private final Map<String, String>   requestParameters;


    EngineActionDefinition(String                guid,
                           String                qualifiedName,
                           String                displayName,
                           RequestTypeDefinition requestTypeDefinition,
                           Map<String, String>   requestParameters,
                           String                requesterUserId,
                           ContentPackDefinition contentPackDefinition)
    {
        this.guid                  = guid;
        this.qualifiedName         = qualifiedName;
        this.displayName           = displayName;
        this.requestTypeDefinition = requestTypeDefinition;
        this.requestParameters     = requestParameters;
        this.requesterUserId       = requesterUserId;
        this.contentPackDefinition = contentPackDefinition;
    }


    /**
     * Return the unique identifier for the integration group.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the unique name of the integration group.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return domain identifier.
     *
     * @return int
     */
    public int getDomainIdentifier() { return 0; }


    /**
     * Return the status of the activity.  Approved means it is able to run now.
     *
     * @return status enum
     */
    public ActivityStatus getActivityStatus()
    {
        return ActivityStatus.APPROVED;
    }


    /**
     * Return the request type to call.
     *
     * @return string
     */
    public String getRequestType()
    {
        return requestTypeDefinition.getGovernanceRequestType();
    }


    /**
     * Return the request parameter to pass to the service when it runs.
     *
     * @return map
     */
    public Map<String, String> getRequestParameters()
    {
        return requestParameters;
    }


    public String getGovernanceEngineGUID()
    {
        return requestTypeDefinition.getGovernanceEngine().getGUID();
    }


    public String getGovernanceEngineName()
    {
        return requestTypeDefinition.getGovernanceEngine().getName();
    }


    /**
     * Return the name of the integration group.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the integration group.
     *
     * @return string
     */
    public String getDescription()
    {
        return requestTypeDefinition.getGovernanceService().getDescription();
    }


    /**
     * Get identifier of content pack where this template should be located.
     *
     * @return content pack definition
     */
    public ContentPackDefinition getContentPackDefinition()
    {
        return contentPackDefinition;
    }


    /**
     * Get the anchorScopeGUID.
     *
     * @return string
     */
    public String getRequesterUserId()
    {
        return requesterUserId;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DigitalProductCatalogDefinition{name='" + name() + "'}";
    }
}
