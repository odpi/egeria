/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.governanceengines;

import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.ActorRoleDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.CollectionDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionBlueprintDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionComponentDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A description of the predefined solution blueprints.  Solution blueprints identify the key solution oriented
 * investments.  They may be used to document both the as-is and to-be solutions.
 */
public enum SolutionBlueprint implements SolutionBlueprintDefinition
{
    /**
     * Basic template
     */
    SOLUTION_BLUEPRINT_TEMPLATE("ff8e9750-182c-4f0d-b432-c7a48b8d660b",
                                PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                                PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                PlaceholderProperty.IDENTIFIER.getPlaceholder(),
                                PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder()),


    /**
     * A description of how a clinical trial is managed in Coco Pharmaceuticals.
     */
    CLINICAL_TRIAL_MANAGEMENT("c4f8d707-7c85-4125-b5fd-c3257a2ef2ef",
                              "Clinical Trial Management Solution Blueprint",
                              "A description of how a clinical trial is managed in Coco Pharmaceuticals.",
                              "Clinical Trial Management",
                              new SolutionComponentDefinition[]{
                                      SolutionComponent.HOSPITAL,
                                      SolutionComponent.HOSPITAL_LANDING_AREA_FOLDER,
                                      SolutionComponent.LANDING_FOLDER_CATALOGUER,
                                      SolutionComponent.WEEKLY_MEASUREMENTS_ONBOARDING_PIPELINE,
                                      SolutionComponent.WEEKLY_MEASUREMENTS_DATA_LAKE_FOLDER,
                                      SolutionComponent.POPULATE_SANDBOX,
                                      SolutionComponent.TREATMENT_VALIDATION_SANDBOX,
                                      SolutionComponent.ANALYSE_PATIENT_DATA,
                                      SolutionComponent.TREATMENT_EFFICACY_EVIDENCE,
                                      SolutionComponent.ASSEMBLE_REPORT,
                                      SolutionComponent.REPORT_VALIDATION_AND_DELIVERY,
                                      SolutionComponent.NOMINATE_HOSPITAL,
                                      SolutionComponent.CERTIFY_HOSPITAL,
                                      SolutionComponent.ONBOARD_HOSPITAL,
                                      SolutionComponent.SET_UP_DATA_LAKE,
                                      SolutionComponent.SET_UP_CLINICAL_TRIAL
                              },
                              new ActorRoleDefinition[]{
                                      SolutionRoleDefinition.CLINICAL_TRIAL_MANAGER,
                                      SolutionRoleDefinition.CLINICAL_TRIAL_DATA_ENGINEER,
                                      SolutionRoleDefinition.CLINICAL_TRIAL_RESEARCHER,
                                      SolutionRoleDefinition.CLINICAL_TRIAL_SPONSOR,
                                      SolutionRoleDefinition.CLINICAL_TRIAL_DATA_SCIENTIST,
                                      SolutionRoleDefinition.CLINICAL_TRIALS_EXECUTIVE,
                                      SolutionRoleDefinition.PARTICIPATING_HOSPITAL,
                                      SolutionRoleDefinition.PARTICIPATING_HOSPITAL_COORDINATOR
                              }),


    /**
     * A description of how data is gathered to support sustainability reporting in Coco Pharmaceuticals.
     */
    SUSTAINABILITY_REPORTING("aed5c289-6e81-4cf8-8852-752005eee0c4",
                              "Sustainability Reporting Solution Blueprint",
                              "A description of how data is gathered to support sustainability reporting in Coco Pharmaceuticals.",
                              "Sustainability Reporting",
                              new SolutionComponentDefinition[]{
                                      SolutionComponent.SUSTAINABILITY_ODS,
                                      SolutionComponent.SUSTAINABILITY_CALCULATORS,
                                      SolutionComponent.SUSTAINABILITY_DASHBOARDS,
                                      SolutionComponent.EMPLOYEE_EXPENSE_TOOL,
                                      SolutionComponent.HAZMAT_INVENTORY,
                                      SolutionComponent.ACCOUNTING_LEDGER,
                                      SolutionComponent.GOODS_INVENTORY,
                              },
                             new ActorRoleDefinition[]{
                                     SolutionRoleDefinition.SUSTAINABILITY_LEADER,
                                     SolutionRoleDefinition.SUSTAINABILITY_EXECUTIVE,
                                     SolutionRoleDefinition.SUSTAINABILITY_CHAMPION
                             }),

    /**
     * A description of how an order for a personalized medicine prescription is fulfilled by Coco Pharmaceuticals.
     */
    PERSONALIZED_MEDICINE_ORDER_FULFILLMENT("9b1d5648-58b9-4fc8-959c-ed0316068d75",
                                            "Personalized Medicine Order Fulfillment Solution Blueprint",
                                            "A description of how an order for a personalized medicine prescription is fulfilled by Coco Pharmaceuticals.",
                                            "Personalized Medicine Order Fulfillment",
                                            new SolutionComponentDefinition[]{
                                                    SolutionComponent.ACCOUNTING_LEDGER
                                            },
                                            null),

    /**
     * A description of how information about Coco Pharmaceuticals employees is managed.
     */
    EMPLOYEE_MANAGEMENT("4558ef22-9cde-4dcb-aebe-45d5acfa818a",
                        "Employee Management Solution Blueprint",
                        "A description of how information about Coco Pharmaceuticals employees is managed.",
                        "Employee Management",
                        new SolutionComponentDefinition[]{
                                SolutionComponent.EMPLOYEE_EXPENSE_TOOL
                        },
                        new ActorRoleDefinition[]{
                                SolutionRoleDefinition.EMPLOYEE
                        }),

    /**
     * A description of how the new industry 4.0 manufacturing control system operates.
     */
    AUTOMATED_MANUFACTURING_CONTROL("8a222c5d-b206-454f-b861-2b803cfe3cbd",
                                    "Automated Manufacturing Control Solution Blueprint",
                                    "A description of how the new industry 4.0 manufacturing control system operates.",
                                    "Automated Manufacturing Control",
                                    null,
                                    null),

    /**
     * A description of how physical inventory is managed between procurement, the distribution centres, manufacturing and finance.
     */
    INVENTORY_MANAGEMENT("d0af5eeb-b341-4046-a336-938b88761719",
                         "Inventory Management Solution Blueprint",
                         "A description of how physical inventory is managed between procurement, the distribution centres, manufacturing and finance.",
                         "Inventory Management",
                         new SolutionComponentDefinition[]{
                                 SolutionComponent.GOODS_INVENTORY
                         },
                         null),

    HAZARDOUS_MATERIAL_MANAGEMENT("f1a008b9-bace-4d37-8dd8-d24fb45477e2",
                                  "Hazardous Material Management Solution Blueprint",
                                  "A description of how hazardous material is handled, tracked and any incidents reported/managed.",
                                  "Hazardous Material Management",
                                  new SolutionComponentDefinition[]{
                                          SolutionComponent.HAZMAT_INVENTORY
                                  },
                                  null),

    ;


    private final String                        guid;
    private final String                        displayName;
    private final String                        description;
    private final String                        identifier;
    private String                              versionIdentifier = EGERIA_VERSION_IDENTIFIER;
    private final SolutionComponentDefinition[] solutionComponents;
    private final ActorRoleDefinition[]         solutionRoles;

    private final boolean isTemplate;


    /**
     * Construct an enum instance.
     *
     * @param guid              unique identifier
     * @param displayName       display name of solution blueprint
     * @param description       description of solution blueprint
     * @param identifier        Identifier of the solution blueprint
     * @param versionIdentifier version identifier of the solution blueprint
     */
    SolutionBlueprint(String guid,
                      String displayName,
                      String description,
                      String identifier,
                      String versionIdentifier)
    {
        this.guid               = guid;
        this.displayName        = displayName;
        this.description        = description;
        this.identifier         = identifier;
        this.versionIdentifier  = versionIdentifier;
        this.solutionComponents = null;
        this.solutionRoles      = null;
        this.isTemplate         = true;
    }


    /**
     * Construct an enum instance.
     *
     * @param guid unique identifier
     * @param displayName display name of solution blueprint
     * @param description description of solution blueprint
     */
    SolutionBlueprint(String                        guid,
                      String                        displayName,
                      String                        description,
                      String                        identifier,
                      SolutionComponentDefinition[] solutionComponents,
                      ActorRoleDefinition[]         solutionRoles)
    {
        this.guid               = guid;
        this.displayName        = displayName;
        this.description        = description;
        this.identifier         = identifier;
        this.solutionComponents = solutionComponents;
        this.solutionRoles      = solutionRoles;
        this.isTemplate         = false;
    }



    /**
     * Return the GUID for the element.
     *
     * @return string
     */
    @Override
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the display name of the solution blueprint.
     *
     * @return string
     */
    @Override
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the solution blueprint
     *
     * @return string
     */
    @Override
    public String getDescription()
    {
        return description;
    }

    /**
     * Return the optional collection that this element is a part of.
     *
     * @return collection definition
     */
    @Override
    public CollectionDefinition getParentCollection()
    {
        return SolutionBlueprintDefinition.super.getParentCollection();
    }


    /**
     * Return the version identifier of the solution blueprint.
     *
     * @return string
     */
    @Override
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Return the identifier of the solution blueprint.
     *
     * @return string
     */
    @Override
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Return whether this is a template or not.
     *
     * @return boolean
     */
    @Override
    public boolean isTemplate()
    {
        return isTemplate;
    }


    /**
     * Return the authors of the design model.  It defaults to the Egeria community but can be overridden.
     *
     * @return string
     */
    @Override
    public List<String> authors()
    {
        return SolutionBlueprintDefinition.super.authors();
    }


    /**
     * Return the list of components that are members of the solution blueprint.
     *
     * @return list of component definitions
     */
    @Override
    public List<SolutionComponentDefinition> getSolutionComponents()
    {
        if (solutionComponents != null)
        {
            return new ArrayList<>(Arrays.asList(solutionComponents));
        }

        return null;
    }


    /**
     * Return the list of roles that are members of the solution blueprint.
     *
     * @return list of role definitions
     */
    @Override
    public List<ActorRoleDefinition> getSolutionRoles()
    {
        if (solutionRoles != null)
        {
            return new ArrayList<>(Arrays.asList(solutionRoles));
        }

        return null;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SolutionBlueprint{" +
                "guid='" + guid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", identifier='" + identifier + '\'' +
                ", versionIdentifier='" + versionIdentifier + '\'' +
                ", solutionComponents=" + Arrays.toString(solutionComponents) +
                ", solutionRoles=" + Arrays.toString(solutionRoles) +
                ", isTemplate=" + isTemplate +
                "} " + super.toString();
    }
}
