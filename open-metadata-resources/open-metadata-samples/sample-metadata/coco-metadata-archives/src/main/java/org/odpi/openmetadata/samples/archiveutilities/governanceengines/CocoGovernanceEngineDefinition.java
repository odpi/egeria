/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.governanceengines;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * Describes the standard governance engines shipped with Egeria
 */
public enum CocoGovernanceEngineDefinition
{
    /**
     * Manages the set up and operation of clinical trials at Coco Pharmaceuticals.
     */
    CLINICAL_TRIALS_ENGINE("cc812fd8-b50d-489c-8240-3a4ed42ab18d",
                           "ClinicalTrials@CocoPharmaceuticals",
                           "Clinical Trials Engine",
                           "Manages the set up and operation of clinical trials at Coco Pharmaceuticals.",
                           OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName),

    /**
     * Manages the data validation and operation of clinical trials at Coco Pharmaceuticals.
     */
    ASSET_QUALITY_ENGINE("8460ffc5-d242-4b6e-b62c-386ebb84b869",
                         "AssetQuality@CocoPharmaceuticals",
                         "Asset Quality Survey Action Engine",
                         "Assess the quality of a digital resource identified by the asset in the request.",
                         OpenMetadataType.SURVEY_ACTION_ENGINE.typeName),

    ;



    private final String guid;
    private final String name;
    private final String displayName;
    private final String description;
    private final String type;

    CocoGovernanceEngineDefinition(String guid, String name, String displayName, String description, String type)
    {
        this.guid        = guid;
        this.name        = name;
        this.displayName = displayName;
        this.description = description;
        this.type        = type;
    }


    /**
     * Return the unique identifier of the governance engine.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the unique name of the governance engine.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the display name of the governance engine.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Return the description of the governance engine.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the open metadata type name for this engine.
     *
     * @return string
     */
    public String getType()
    {
        return type;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "GovernanceEngineDefinition{" + "name='" + name + '\'' + "}";
    }
}
