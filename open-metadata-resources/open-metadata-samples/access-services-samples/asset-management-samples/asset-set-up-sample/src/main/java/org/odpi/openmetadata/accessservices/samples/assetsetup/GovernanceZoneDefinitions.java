/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.samples.assetsetup;


/**
 * The GovernanceZoneDefinitions is used to feed the definition of the governance zones for
 * Coco Pharmaceuticals.
 */
public enum GovernanceZoneDefinitions
{
    PERSONAL(   "personal-files",
                "Personal Files Zone",
                "Assets that are for an individual's use.  Initially the creator of the asset is the owner. " +
                      "This person can reassign the asset to additional zones to increase its visibility or " +
                      "reassign the ownership.",
                "Assets that should only be visible and editable to the owner."),

    QUARANTINE( "quarantine",
                "Quarantine Zone",
                "Assets from third parties that are being evaluated by the onboarding team. " +
                        "The assets will move into the other zones once the asset has been catalogued and classified.",
                "Data sets just received and have not yet been properly catalogued."),

    DATA_LAKE( "data-lake",
                "Data Lake Zone",
                "Assets for sharing that are read only.",
                "These are production assets that can be used for business decisions."),

    TRASH_CAN(  "trash-can",
                "Trash Can Zone",
                "Asset that are in a holding zone ready to be deleted.",
                "Assets that are no longer required.")
;


    private String   zoneName;
    private String   displayName;
    private String   description;
    private String   criteria;


    /**
     * GovernanceZoneDefinitions constructor creates an instance of the enum
     *
     * @param zoneName   unique Id for the zone
     * @param displayName   text for the zone
     * @param description   description of the assets in the zone
     * @param criteria   criteria for inclusion
     */
    GovernanceZoneDefinitions(String zoneName, String displayName, String description, String criteria)
    {
        this.zoneName = zoneName;
        this.displayName = displayName;
        this.description = description;
        this.criteria = criteria;
    }


    /**
     * Returns the unique name for the zone.
     *
     * @return qualified name
     */
    public String getZoneName()
    {
        return zoneName;
    }


    /**
     * Returns a descriptive name of the zone.
     *
     * @return display name
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Returns a detailed description of the assets within the zone.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Returns a description of the criteria for including assets in the zone.
     *
     * @return criteria
     */
    public String getCriteria()
    {
        return criteria;
    }
}
