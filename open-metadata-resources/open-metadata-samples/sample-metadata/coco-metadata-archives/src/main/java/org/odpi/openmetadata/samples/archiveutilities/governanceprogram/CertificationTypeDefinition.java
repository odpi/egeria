/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.ScopeDefinition;


/**
 * The CertificationTypeDefinition is used to feed the definition of the Certification Types for
 * Coco Pharmaceuticals scenarios.
 */
public enum CertificationTypeDefinition
{
    /**
     * Approved clinical trial
     */
    APPROVED_CLINICAL_TRIAL("28ee93d2-f5a5-4352-bcad-0674aae2b2a6",
                            "CT_APPROVED",
                            "Approved clinical trial",
                            "The clinical trial is approved by the board of directors and the regulators",
                            "This certification gives permission for resources to be deployed as part of the linked clinical trial project.",
                            "The clinical trial must be conducted as agreed with the approved level and types of participants and the identified data and analysis.",
                            ScopeDefinition.WITHIN_ORGANIZATION,
                            false,
                            null,
                            null),

    /**
     * Participating Hospital for the ~{clinicalTrialId}~ approved Clinical Trial
     */
    CLINICAL_TRIAL_APPROVED_HOSPITAL("5acc69bf-dfbb-4e4a-b47b-19d610f2cb06",
                                     "CT_APPROVED_HOSPITAL::~{clinicalTrialId}~",
                                     "Participating Hospital for the ~{clinicalTrialId}~ approved Clinical Trial",
                                     "The hospital is participating in the associated Clinical Trial.",
                                     "This certification is started when the hospital completes the necessary preparations for the clinical trail.",
                                     "This certification requires the hospital to provided signed data sharing agreements for each of their patient subjects and agrees to supply data that conforms to the schema and quality levels laid out for both the patient details and weekly patient measurements data specifications.",
                                     ScopeDefinition.WITHIN_PROJECT,
                                     true,
                                     "Clinical Trial Approved Hospital Template",
                                     "This template is used to create a clinical trial specific certification type to identify hospitals that are certified to participate in a specific clinical trial."),


    /**
     * The data in this file matches the specification for the ~{clinicalTrialId}~ Clinical Trial.
     */
    WEEKLY_MEASUREMENTS_APPROVED_DATA("8a921039-ad5f-454d-ae17-e5a5b69f9333",
                                      "CT_APPROVED_WEEKLY_MEASUREMENTS::~{clinicalTrialId}~",
                                      "Valid weekly measurements data for the ~{clinicalTrialId}~ Clinical Trial",
                                      "The data in this file matches the specification for the associated Clinical Trial.",
                                      "This certification is awarded when the data matches both the schema and the data specification.",
                                      "This certification is added to the weekly patient measurements data sets if they are correctly formatted and the data passes the valid value tests.",
                                      ScopeDefinition.WITHIN_PROJECT,
                                      true,
                                      "Clinical Trial Valid Weekly Measurements Template.",
                                      "This template is used to create a clinical trial specific certification type to identify weekly measurements files that are certified to match the data specification for a specific clinical trial."),


    ;


    private final String          guid;
    private final String          identifier;
    private final String          displayName;
    private final String          summary;
    private final String          description;
    private final String          details;
    private final ScopeDefinition scope;
    private final boolean         isTemplate;
    private final String          templateName;
    private final String          templateDescription;

    /**
     * The constructor creates an instance of the enum
     *
     * @param guid            unique id for the enum
     * @param identifier          identifier of the enum
     * @param displayName          title of the enum
     * @param summary        short description for the enum
     * @param description   description of the use of this value
     * @param details qualifying details
     * @param scope usage scope
     */
    CertificationTypeDefinition(String          guid,
                                String          identifier,
                                String          displayName,
                                String          summary,
                                String          description,
                                String          details,
                                ScopeDefinition scope,
                                boolean         isTemplate,
                                String          templateName,
                                String          templateDescription)
    {
        this.guid                = guid;
        this.identifier          = identifier;
        this.displayName         = displayName;
        this.summary             = summary;
        this.description         = description;
        this.details             = details;
        this.scope               = scope;
        this.isTemplate          = isTemplate;
        this.templateName        = templateName;
        this.templateDescription = templateDescription;
    }

    /**
     * Return the unique name of the certification type.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "CertificationType:: " + identifier + ":: " + displayName;
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
     * Return the identifier for the certification type.
     *
     * @return string
     */
    public String getIdentifier()
    {
        return identifier;
    }

    /**
     * Return the title of the certification type.
     *
     * @return string
     */
    public String getDisplayName() { return displayName; }

    /**
     * Return the summary of the certification type.
     *
     * @return string
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Return the description of the certification type.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Return the details of how the certification type works.
     *
     * @return string
     */
    public String getDetails() { return details; }


    /**
     * Return the scope of usage for this information supply chain.
     *
     * @return string
     */
    public ScopeDefinition getScope() {return scope; }

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
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "CertificationTypeDefinition{" + summary + '}';
    }
}
