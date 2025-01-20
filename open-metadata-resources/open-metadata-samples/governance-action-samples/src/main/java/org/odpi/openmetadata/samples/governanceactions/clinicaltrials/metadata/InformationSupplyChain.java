/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata;

import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;

import java.util.Arrays;
import java.util.List;

/**
 * A description of the predefined information supply chains.
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
                                      true),

    /**
     * Delivering data relating to a clinical trial from the hospitals to the Coco Researchers.
     */
    CLINICAL_TRIALS_TREATMENT_VALIDATION("1f71e403-1187-4f03-a1dd-ae7dc105f06f",
                                         "Clinical Trial Treatment Validation",
                                         "Delivering data relating to a clinical trial from the hospitals to the Coco Researchers so that they can then determine the efficacy of the treatment to report to the regulators.",
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
                                         false),
    ;


    private final String          guid;
    private final String          displayName;
    private final String          description;
    private final ScopeDefinition scope;
    private final String[]        purposes;
    private final boolean         isTemplate;


    /**
     * Construct an enum instance.
     *
     * @param guid unique identifier
     * @param displayName display name of information supply chain
     * @param description description of information supply chain
     * @param scope scope of information supply chain
     * @param purposes purposes of information supply chain
     * @param isTemplate is this a template?
     */
    InformationSupplyChain(String          guid,
                           String          displayName,
                           String          description,
                           ScopeDefinition scope,
                           String[]        purposes,
                           boolean         isTemplate)
    {
        this.guid        = guid;
        this.displayName = displayName;
        this.description = description;
        this.scope       = scope;
        this.purposes    = purposes;
        this.isTemplate  = isTemplate;
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
     * Return the unique name of the solution blueprint.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "InformationSupplyChain:" + displayName + ":" + scope;
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
