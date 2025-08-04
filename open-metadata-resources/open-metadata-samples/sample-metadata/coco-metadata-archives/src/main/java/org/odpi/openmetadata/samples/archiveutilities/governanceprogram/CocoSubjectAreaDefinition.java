/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;


import org.odpi.openmetadata.samples.archiveutilities.organization.ScopeDefinition;

/**
 * The CocoSubjectAreaDefinition is used to feed the definition of the subject areas for Coco Pharmaceuticals.
 */
public enum CocoSubjectAreaDefinition
{
    /**
     * Organization - Information relating to an organization.
     */
    ORGANIZATION(   "Organization",
                    null,
                    "Organization",
                    "Information relating to an organization.",
                    ScopeDefinition.ALL_COCO.getPreferredValue(),
                    "Controlling data management relating to different types of organizational structures and catalogues.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Organization:Hospital - Information relating to a hospital's organization.
     */
    HOSPITAL(       "Organization:Hospital",
                    CocoSubjectAreaDefinition.ORGANIZATION,
                    "Hospital",
                    "Information relating to a hospital's organization.",
                    ScopeDefinition.ALL_COCO.getPreferredValue(),
                    "Controlling data management relating to different types of hospital organizational structures and catalogues.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Organization:Supplier - Information relating to a supplier's organization.
     */
    SUPPLIER(       "Organization:Supplier",
                    CocoSubjectAreaDefinition.ORGANIZATION,
                    "Supplier",
                    "Information relating to a supplier's organization.",
                    ScopeDefinition.ALL_COCO.getPreferredValue(),
                    "Controlling data management relating to different types of supplier organizational structures and catalogues.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Person - Information relating to an individual.
     */
    PERSON(         "Person",
                    null,
                    "Person",
                    "Information relating to an individual.",
                    ScopeDefinition.ALL_COCO.getPreferredValue(),
                    "Controlling data management relating to information that describes an individual.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Person:Patient - Information relating to an individual patient.
     */
    PATIENT(         "Person:Patient",
                     CocoSubjectAreaDefinition.PERSON,
                    "Patient",
                    "Information relating to an individual patient.",
                     ScopeDefinition.ALL_COCO.getPreferredValue(),
                    "Controlling data management relating to information that describes an individual patient.",
                     GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Person:Clinician - Information relating to an individual who works with patients.
     */
    CLINICIAN(         "Person:Clinician",
                    CocoSubjectAreaDefinition.PERSON,
                    "Clinician",
                    "Information relating to an individual who works with patients.",
                       ScopeDefinition.ALL_COCO.getPreferredValue(),
                    "Controlling data management relating to information that describes an individual working with patients.",
                       GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Person:Employee - Information relating to an individual who is employed by an organization.
     */
    EMPLOYEE(       "Person:Employee",
                    CocoSubjectAreaDefinition.PERSON,
                    "Employee",
                    "Information relating to an individual who is employed by an organization.",
                    ScopeDefinition.ALL_COCO.getPreferredValue(),
                    "Controlling data management relating to information that describes an employee.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Person:Collaborator - Information relating to an individual who works for a business partner.
     */
    COLLABORATOR(   "Person:Collaborator",
                    CocoSubjectAreaDefinition.PERSON,
                    "Collaborator",
                    "Information relating to an individual who works for a business partner.",
                    ScopeDefinition.ALL_COCO.getPreferredValue(),
                    "Controlling data management relating to information that describes an individuals who are business partners.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Clinical - Information relating to the work understanding medical conditions and their resolution.
     */
    CLINICAL(       "Clinical",
                    null,
                    "Clinical",
                    "Information relating to the work understanding medical conditions and their resolution.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Clinical:Symptom - Information relating to the symptoms of a medical condition.
     */
    SYMPTOM(        "Clinical:Symptom",
                    CocoSubjectAreaDefinition.CLINICAL,
                    "Clinical",
                    "Information relating to the symptoms of a medical condition.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Clinical:Measurement - Information relating to the measurements taken to understanding medical conditions and their effectiveness.
     */
    MEASUREMENT(    "Clinical:Measurement",
                    CocoSubjectAreaDefinition.CLINICAL,
                    "Measurement",
                    "Information relating to the measurements taken to understanding medical conditions and their effectiveness.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Clinical:Prescription - Information relating to the treatment defined for a specific patient.
     */
    PRESCRIPTION(   "Clinical:Prescription",
                    CocoSubjectAreaDefinition.CLINICAL,
                    "Prescription",
                    "Information relating to the treatment defined for a specific patient.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Clinical:Outcome - Information relating to the work understanding the result of a course of treatment.
     */
    OUTCOME(        "Clinical:Outcome",
                    CocoSubjectAreaDefinition.CLINICAL,
                    "Outcome",
                    "Information relating to the work understanding the result of a course of treatment.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Treatment - Information relating to the Coco Pharmaceutical products and practices around patient care.
     */
    TREATMENT(      "Treatment",
                    null,
                    "Treatment",
                    "Information relating to the Coco Pharmaceutical products and practices around patient care.",
                    "Within research, sales, manufacturing and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' treatments.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Treatment:Product - Information relating to the Coco Pharmaceutical products to be used in particular treatments.
     */
    PRODUCT(        "Treatment:Product",
                    CocoSubjectAreaDefinition.TREATMENT,
                    "Product",
                    "Information relating to the Coco Pharmaceutical products to be used in particular treatments.",
                    "Within research, sales, manufacturing and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' treatments.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Treatment:Order - Information relating to orders for Coco Pharmaceutical products.
     */
    ORDER(          "Treatment:Order",
                    CocoSubjectAreaDefinition.TREATMENT,
                    "Order",
                    "Information relating to orders for Coco Pharmaceutical products.",
                    "Within research, sales, manufacturing and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' treatments.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Treatment:Recipe - Information relating to the ingredients and manufacturing know-how for Coco Pharmaceutical products.
     */
    RECIPE(         "Treatment:Recipe",
                    CocoSubjectAreaDefinition.TREATMENT,
                    "Recipe",
                    "Information relating to the ingredients and manufacturing know-how for Coco Pharmaceutical products.",
                    "Within research, sales, manufacturing and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' treatments.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * ServiceQuality - Information relating to the Coco Pharmaceuticals' business operations.
     */
    SERVICE_QUALITY("ServiceQuality",
                    null,
                    "Service Quality",
                    "Information relating to the Coco Pharmaceuticals' business operations.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * ServiceQuality:Contract - Information relating to the Coco Pharmaceuticals' contracts.
     */
    CONTRACT       ("ServiceQuality:Contract",
                    CocoSubjectAreaDefinition.SERVICE_QUALITY,
                    "Contract",
                    "Information relating to the Coco Pharmaceuticals' contracts.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * ServiceQuality:Stock - Information relating to the Coco Pharmaceuticals' stock management and control.
     */
    STOCK          ("ServiceQuality:Stock",
                    CocoSubjectAreaDefinition.SERVICE_QUALITY,
                    "Stock",
                    "Information relating to the Coco Pharmaceuticals' stock management and control.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * ServiceQuality:Distribution - Information relating to the Coco Pharmaceuticals' distribution of treatments.
     */
    DISTRIBUTION   ("ServiceQuality:Distribution",
                    CocoSubjectAreaDefinition.SERVICE_QUALITY,
                    "Distribution",
                    "Information relating to the Coco Pharmaceuticals' distribution of treatments.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * ServiceQuality:Invoice - Information relating to the Coco Pharmaceuticals' billing and payments.
     */
    INVOICE        ("ServiceQuality:Invoice",
                    CocoSubjectAreaDefinition.SERVICE_QUALITY,
                    "Invoice",
                    "Information relating to the Coco Pharmaceuticals' billing and payments.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Governance - Information relating to the Coco Pharmaceuticals' governance initiatives.
     */
    GOVERNANCE("Governance",
                    null,
                    "Governance",
                    "Information relating to the Coco Pharmaceuticals' governance initiatives.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Education on the terminology associated with a governance domain",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier()),


    /**
     * Product Development - information relating to the Coco Pharmaceuticals' product development initiatives.
     */
    PRODUCT_DEVELOPMENT("ProductDevelopment",
               null,
               "Product Development",
               "Information relating to the Coco Pharmaceuticals' treatment development initiatives.",
               "Within research and the board.",
               "Coordinating and regulating data during the development of new treatments",
               GovernanceDomainDefinition.ALL.getDomainIdentifier()),

    /**
     * Clinical Trial - Coordinating and regulating data during a clinical trial.
     */
    CLINICAL_TRIAL("ProductDevelopment:ClinicalTrial",
                    CocoSubjectAreaDefinition.PRODUCT_DEVELOPMENT,
                    "Clinical Trial",
                        "Information relating to the clinical trials run to support Coco Pharmaceuticals' product development initiatives.",
                        "Within research and the board.",
                        "Coordinating and regulating data during a clinical trial.",
                        GovernanceDomainDefinition.ALL.getDomainIdentifier()),
    ;


    private final String                    subjectAreaName;
    private final CocoSubjectAreaDefinition parent;
    private final String                    displayName;
    private final String                    description;
    private final String                    scope;
    private final String                    usage;
    private final int                       domain;


    CocoSubjectAreaDefinition(String                    name,
                              CocoSubjectAreaDefinition parent,
                              String                    displayName,
                              String                    description,
                              String                    scope,
                              String                    usage,
                              int                       domain)
    {
        this.subjectAreaName = name;
        this.parent = parent;
        this.displayName = displayName;
        this.description = description;
        this.scope = scope;
        this.usage = usage;
        this.domain = domain;
    }



    /**
     * Returns the unique name for the subject area entity.
     *
     * @return qualified name
     */
    public String getQualifiedName()
    {
        return "SubjectArea::" + subjectAreaName;
    }


    /**
     * Returns the unique name for the zone.
     *
     * @return unique name
     */
    public String getSubjectAreaName()
    {
        return subjectAreaName;
    }


    /**
     * Return the name of the parent subject area - null for top level.
     *
     * @return subject area name.
     */
    public CocoSubjectAreaDefinition getParent()
    {
        return parent;
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
     * Returns a description of the organizational scope for the use of this subject area.
     *
     * @return scope
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Get the typical usage of the subject area
     *
     * @return usage
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Return the identifier of the domain that this subject area belongs - 0 means all domains
     *
     * @return domain identifier
     */
    public int getDomain()
    {
        return domain;
    }


    /**
     * String containing the definition values
     *
     * @return printable string
     */
    @Override
    public String toString()
    {
        return "CocoSubjectAreaDefinition{" +
                       "subjectAreaName='" + subjectAreaName + '\'' + '}';
    }
}
