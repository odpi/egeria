/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.samples.subjectareas;


/**
 * The SubjectAreaSampleDefinitions is used to feed the definition of the subject areas for Coco Pharmaceuticals.
 */
public enum SubjectAreaSampleDefinitions
{
    ORGANIZATION(   "Organization",
                    null,
                    "Organization",
                    "Information relating to an organization.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to different types of organizational structures and catalogues.",
                    0),

    HOSPITAL(       "Organization:Hospital",
                    "Organization",
                    "Hospital",
                    "Information relating to a hospital's organization.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to different types of hospital organizational structures and catalogues.",
                    0),

    SUPPLIER(       "Organization:Supplier",
                    "Organization",
                    "Supplier",
                    "Information relating to a supplier's organization.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to different types of supplier organizational structures and catalogues.",
                    0),

    PERSON(         "Person",
                    null,
                    "Person",
                    "Information relating to an individual.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to information that describes an individual.",
                    0),

    PATIENT(        "Person:Patient",
                    "Person",
                    "Patient",
                    "Information relating to an individual patient.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to information that describes an individual patient.",
                    0),

    CLINICIAN(      "Person:Clinician",
                    "Person",
                    "Clinician",
                    "Information relating to an individual who works with patients.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to information that describes an individual workign with patients.",
                    0),

    EMPLOYEE(       "Person:Employee",
                    "Person",
                    "Employee",
                    "Information relating to an individual who is employed by an organization.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to information that describes an employee.",
                    0),

    COLLABORATOR(   "Person:Collaborator",
                    "Person",
                    "Collaborator",
                    "Information relating to an individual who is a business partner.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to information that describes an individuals who are business partners.",
                    0),

    CLINICAL(       "Clinical",
                    null,
                    "Clinical",
                    "Information relating to the work understanding medical conditions and their resolution.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    0),

    SYMPTOM(        "Clinical:Symptom",
                    "Clinical",
                    "Symptom",
                    "Information relating to the symptoms of a medical condition.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    0),

    MEASUREMENT(    "Clinical:Measurement",
                    "Clinical",
                    "Measurement",
                    "Information relating to the measurements taken to understanding medical conditions and their effectiveness.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    0),

    PRESCRIPTION(   "Clinical:Prescription",
                    "Clinical",
                    "Prescription",
                    "Information relating to the treatment defined for a specific patient.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    0),

    OUTCOME(        "Clinical:Outcome",
                    "Clinical",
                    "Outcome",
                    "Information relating to the work understanding the result of a course of treatment.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    0),

    TREATMENT(      "Treatment",
                    null,
                    "Treatment",
                    "Information relating to the Coco Pharmaceutical products and practices around patient care.",
                    "Within research, sales, manufacturing and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' treatments.",
                    0),

    PRODUCT(        "Treatment:Product",
                    "Treatment",
                    "Product",
                    "Information relating to the Coco Pharmaceutical products.",
                    "Within research, sales, manufacturing and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' treatments.",
                    0),

    ORDER(          "Treatment:Order",
                    "Treatment",
                    "Order",
                    "Information relating to orders for Coco Pharmaceutical products.",
                    "Within research, sales, manufacturing and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' treatments.",
                    0),

    RECIPE(         "Treatment:Recipe",
                    "Treatment",
                    "Recipe",
                    "Information relating to the ingredients and manufacturing know-how for Coco Pharmaceutical products.",
                    "Within research, sales, manufacturing and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' treatments.",
                    0),

    SERVICE_QUALITY("ServiceQuality",
                    null,
                    "Service Quality",
                    "Information relating to the Coco Pharmaceuticals' business operations.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    0),

    CONTRACT       ("ServiceQuality:Contract",
                    "ServiceQuality",
                    "Contract",
                    "Information relating to the Coco Pharmaceuticals' contracts.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    0),

    STOCK          ("ServiceQuality:Stock",
                    "ServiceQuality",
                    "Stock",
                    "Information relating to the Coco Pharmaceuticals' stock management and control.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    0),

    DISTRIBUTION   ("ServiceQuality:Distribution",
                    "ServiceQuality",
                    "Distribution",
                    "Information relating to the Coco Pharmaceuticals' distribution of treatments.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    0),

    INVOICE        ("ServiceQuality:Invoice",
                    "ServiceQuality",
                    "Invoice",
                    "Information relating to the Coco Pharmaceuticals' billing and payments.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    0),

    ;


    private final String subjectAreaName;
    private final String parentName;
    private final String displayName;
    private final String description;
    private final String scope;
    private final String usage;
    private final int    domain;


    SubjectAreaSampleDefinitions(String name,
                                 String parentName,
                                 String displayName,
                                 String description,
                                 String scope,
                                 String usage,
                                 int    domain)
    {
        this.subjectAreaName = name;
        this.parentName = parentName;
        this.displayName = displayName;
        this.description = description;
        this.scope = scope;
        this.usage = usage;
        this.domain = domain;
    }


    /**
     * Returns the unique name for the zone.
     *
     * @return qualified name
     */
    public String getSubjectAreaName()
    {
        return subjectAreaName;
    }


    /**
     * Return the name of the parent subject area - null for top level.
     *
     * @return parent subject area name.
     */
    public String getParentName()
    {
        return parentName;
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
     * Get the typical usage of the subject area.
     *
     * @return text
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
        return "SubjectAreaSampleDefinitions{" +
                       "subjectAreaName='" + subjectAreaName + '\'' +
                       ", parentName='" + parentName + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", scope='" + scope + '\'' +
                       ", usage='" + usage + '\'' +
                       ", domain=" + domain +
                       '}';
    }
}
