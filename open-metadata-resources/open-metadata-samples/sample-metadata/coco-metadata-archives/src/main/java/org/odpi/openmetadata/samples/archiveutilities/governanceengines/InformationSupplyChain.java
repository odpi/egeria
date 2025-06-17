/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.governanceengines;

import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.samples.archiveutilities.businesssystems.ScopeDefinition;
import org.odpi.openmetadata.samples.governanceactions.clinicaltrials.CocoClinicalTrialPlaceholderProperty;

import java.util.Arrays;
import java.util.List;

/**
 * A description of the predefined information supply chains.  There are two formats - one for templates and
 * the other for normal information supply chains.
 */
public enum InformationSupplyChain
{
    /**
     * Standard template
     */
    INFORMATION_SUPPLY_CHAIN_TEMPLATE("ba3ab0dd-3ec5-4ec5-9db9-f3dc56e3a732",
                                      PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                                      PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                      ScopeDefinition.TEMPLATE_PLACEHOLDER,
                                      null,
                                      "Standard information supply chain template",
                                      "Create a new type of information supply chain"),

    /**
     * Delivering data relating to the teddy bear drop foot clinical trial from the hospitals to the Coco Researchers.
     */
    CLINICAL_TRIALS_TREATMENT_VALIDATION_TEMPLATE("1f71e403-1187-4f03-a1dd-ae7dc105f06f",
                                                  "Clinical Trial Treatment Validation::" + CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getPlaceholder(),
                                                  "Delivering data relating to the " + CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getPlaceholder() + " clinical trial from the hospitals to the Coco Researchers so that they can then determine the efficacy of the treatment to report to the regulators.",
                                                  ScopeDefinition.WITHIN_PROJECT,
                                                  new String[]
                                                 {
                                                         "Deliver patient measurement data from hospitals to data scientists in research.",
                                                         "Ensure incoming data is only from certified hospitals.",
                                                         "Ensure incoming data is validated and catalogued.",
                                                         "Ensure data and process owners are informed of key milestones and issues requiring attention.",
                                                         "Ensure the process of data capture and treatment validation is transparent and auditable.",
                                                         "Ensure the treatment validation report is complete and regulatory compliant."
                                                 },
                                                  "Clinical Trial Validation Information Supply Chain Template",
                                                  "Template for a new clinical trial validation information supply chain"),


    /**
     * Delivering the data necessary to add a person as a subject in a clinical trial
     */
    CLINICAL_TRIAL_SUBJECT_ONBOARDING("39a035f0-3b2b-45fe-adb8-ee8a19581f6a",
                                      "Clinical Trial Subject Onboarding::" + CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getPlaceholder(),
                                      "Delivering the data necessary to add a person as a subject in the teddy bear drop foot clinical trial.",
                                      ScopeDefinition.WITHIN_PROJECT,
                                      new String[]
                                              {
                                                      "Ensure patient subject is aware of the process and potential risks in participation.",
                                                      "Ensure patient subject has given permission for Coco Pharmaceuticals to acquire, store and process their personal data needed for the clinical trial.",
                                                      "Ensure incoming data is validated and catalogued.",
                                                      "Ensure data and process owners are informed of key milestones and issues requiring attention.",
                                                      "Ensure the process of data capture and validation is transparent and auditable."
                                              },
                                      "Clinical Trial Subject Onboarding Information Supply Chain Template",
                                      "Template for onboarding patient information into a new clinical trial."),

    /**
     * Adding information about a new employee to all appropriate systems and directories.
     */
    NEW_EMPLOYEE_ONBOARDING("022009d9-53cb-4469-afeb-16ee81d8f9bc",
                            "New Employee Onboarding",
                            "Adding information about a new employee to all appropriate systems and directories.",
                            ScopeDefinition.ALL_COCO,
                            new String[]
                                    {
                                            "Ensure a new employee is productive and engaged in a timely manner."
                                    }),


    NEW_DRUG_PRODUCT_INFO_DISTRIBUTION("b0491fd4-6324-4ed8-9a1c-7cbd9892e21b",
                                       "New Drug Product Information Distribution",
                                       "Managing the distribution of information about a new product.",
                                       ScopeDefinition.ALL_COCO,
                                       new String[]
                                               {
                                                       "Ensuring information about a new product is distributed to all of the appropriate system so that it is visible for ordering, manufacturing and invoicing."
                                               }),
    PERSONALIZED_TREATMENT_ORDER("adbae740-57a3-41b8-a722-266b895794e6",
                                 "Personalized Treatment Ordering",
                                 "Delivering information about a new personalized medicine order so that it fulfilled and invoiced.",
                                 ScopeDefinition.ALL_COCO,
                                 new String[]
                                         {
                                                 "Ensuring orders are fulfilled effectively."
                                         }),


    SUSTAINABILITY_REPORTING("dd15b286-a38d-4f03-8625-aaded8596048",
                             "Sustainability Reporting",
                             "Delivering information to the sustainability reporting tools.",
                             ScopeDefinition.ALL_COCO,
                             new String[]
                                     {
                                             "Ensuring sustainability reporting is accurate."
                                     }),


    PHYSICAL_INVENTORY_TRACKING("7480a3b1-8d6c-4062-ae59-f3b81e146ed0",
                                "Physical Inventory Tracking",
                                "Managing information take tracks physical goods from suppliers, to depot and manufacturing.",
                                ScopeDefinition.ALL_COCO,
                                new String[]
                                        {
                                                "Ensuring effective management of physical inventory.",
                                                "Ensuring hazardous materials are properly identified, reported and managed."
                                        }),


    EMPLOYEE_EXPENSE_PAYMENT("79d1d83f-6a37-4c32-bf3f-eb8b4358027c",
                             "Employee Expense Payment",
                             "Managing the collection of expense data, its approval and the subsequent payment authorization flows.",
                             ScopeDefinition.ALL_COCO,
                             new String[]
                                     {
                                             "Ensure employees are reimbursed for their expenses in a timely manner."
                                     }),


    ;


    private final String          guid;
    private final String          displayName;
    private final String          description;
    private final ScopeDefinition scope;
    private final String[]        purposes;


    private boolean               isTemplate = false;
    private String                templateName = null;
    private String                templateDescription = null;


    /**
     * Construct an enum instance (non-template).
     *
     * @param guid unique identifier
     * @param displayName display name of information supply chain
     * @param description description of information supply chain
     * @param scope scope of information supply chain
     * @param purposes purposes of information supply chain
     */
    InformationSupplyChain(String          guid,
                           String          displayName,
                           String          description,
                           ScopeDefinition scope,
                           String[]        purposes)
    {
        this.guid        = guid;
        this.displayName = displayName;
        this.description = description;
        this.scope       = scope;
        this.purposes    = purposes;
    }


    /**
     * Construct an enum instance (template).
     *
     * @param guid unique identifier
     * @param displayName display name of information supply chain
     * @param description description of information supply chain
     * @param scope scope of information supply chain
     * @param purposes purposes of information supply chain
     * @param templateName is this a template? What is it called?
     * @param templateDescription describe how this template is used
     */
    InformationSupplyChain(String          guid,
                           String          displayName,
                           String          description,
                           ScopeDefinition scope,
                           String[]        purposes,
                           String          templateName,
                           String          templateDescription)
    {
        this.guid                = guid;
        this.displayName         = displayName;
        this.description         = description;
        this.scope               = scope;
        this.purposes            = purposes;
        this.isTemplate          = true;
        this.templateName        = templateName;
        this.templateDescription = templateDescription;
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
     * Return the display name of the information supply chain.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the solution information supply chain.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the scope of the information supply chain.
     *
     * @return string
     */
    public ScopeDefinition getScope()
    {
        return scope;
    }


    /**
     * Return the purposes of the information supply chain.
     *
     * @return list of strings
     */
    public List<String> getPurposes()
    {
        if (purposes == null)
        {
            return null;

        }

        return Arrays.asList(purposes);
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
     * Return the template name.
     *
     * @return string
     */
    public String getTemplateName()
    {
        return templateName;
    }


    /**
     * Return the template description.
     *
     * @return string
     */
    public String getTemplateDescription()
    {
        return templateDescription;
    }



    /**
     * Return the unique name of the information supply chain.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "InformationSupplyChain::" + displayName;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "InformationSupplyChain{" + displayName + '}';
    }
}
