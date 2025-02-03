/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.businesssystems;

import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;

/**
 * A description of the predefined solution blueprints.  Solution blueprints identify the key solution oriented
 * investments.  They may be used to document both the as-is and to-be solutions.
 */
public enum SolutionBlueprint
{
    /**
     * Basic template
     */
    SOLUTION_BLUEPRINT_TEMPLATE("ff8e9750-182c-4f0d-b432-c7a48b8d660b",
                                PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                                PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                true),

    /**
     * A description of how a clinical trial is managed in Coco Pharmaceuticals.
     */
    CLINICAL_TRIAL_MANAGEMENT("c4f8d707-7c85-4125-b5fd-c3257a2ef2ef",
                              "Clinical Trial Management Solution Blueprint",
                              "A description of how a clinical trial is managed in Coco Pharmaceuticals.",
                              "V1.2",
                              false),

    /**
     * A description of how data is gathered to support sustainability reporting in Coco Pharmaceuticals.
     */
    SUSTAINABILITY_REPORTING("aed5c289-6e81-4cf8-8852-752005eee0c4",
                              "Sustainability Reporting Solution Blueprint",
                              "A description of how data is gathered to support sustainability reporting in Coco Pharmaceuticals.",
                              "V1.0",
                              false),

    /**
     * A description of how an order for a personalized medicine prescription is fulfilled by Coco Pharmaceuticals.
     */
    PERSONALIZED_MEDICINE_ORDER_FULFILLMENT("9b1d5648-58b9-4fc8-959c-ed0316068d75",
                             "Personalized Medicine Order Fulfillment Solution Blueprint",
                             "A description of how an order for a personalized medicine prescription is fulfilled by Coco Pharmaceuticals.",
                             "V1.0",
                             false),

    /**
     * A description of how information about Coco Pharmaceuticals employees is managed.
     */
    EMPLOYEE_MANAGEMENT("4558ef22-9cde-4dcb-aebe-45d5acfa818a",
                        "Employee Management Solution Blueprint",
                        "A description of how information about Coco Pharmaceuticals employees is managed.",
                        "V5.2",
                        false),

    /**
     * A description of how the new industry 4.0 manufacturing control system operates.
     */
    AUTOMATED_MANUFACTURING_CONTROL("8a222c5d-b206-454f-b861-2b803cfe3cbd",
                                    "Automated Manufacturing Control",
                                    "A description of how the new industry 4.0 manufacturing control system operates.",
                                    "0.5.9.1",
                                    false),

    /**
     * A description of how physical inventory is managed between procurement, the distribution centres, manufacturing and finance.
     */
    INVENTORY_MANAGEMENT("d0af5eeb-b341-4046-a336-938b88761719",
                         "Inventory Management",
                         "A description of how physical inventory is managed between procurement, the distribution centres, manufacturing and finance.",
                         "1.0",
                         false),

    HAZARDOUS_MATERIAL_MANAGEMENT("f1a008b9-bace-4d37-8dd8-d24fb45477e2",
                                  "Hazardous Material Management Solution Blueprint",
                                  "A description of how hazardous material is handled, tracked and any incidents reported/managed.",
                                  "1.0",
                                  false),

    ;


    private final String  guid;
    private final String  displayName;
    private final String  description;
    private final String  versionIdentifier;
    private final boolean isTemplate;


    /**
     * Construct an enum instance.
     *
     * @param guid unique identifier
     * @param displayName display name of solution blue print
     * @param description description of solution blueprint
     * @param versionIdentifier version identifier of the solution blueprint
     * @param isTemplate is this a template?
     */
    SolutionBlueprint(String  guid,
                      String  displayName,
                      String  description,
                      String  versionIdentifier,
                      boolean isTemplate)
    {
        this.guid              = guid;
        this.displayName       = displayName;
        this.description       = description;
        this.versionIdentifier = versionIdentifier;
        this.isTemplate        = isTemplate;
    }




    /**
     * Return the GUID for the element.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the display name of the solution blueprint.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the solution blueprint
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the version identifier of the solution blueprint.
     *
     * @return string
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Return whether this is a template or not.
     *
     * @return boolean
     */
    public boolean isTemplate()
    {
        return isTemplate;
    }


    /**
     * Return the unique name of the solution blueprint.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "SolutionBlueprint:" + displayName + ":" + versionIdentifier;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SolutionBlueprint{" + displayName + '}';
    }
}
