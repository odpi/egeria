/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.samples.subjectareas;


/**
 * The SubjectAreaSampleDefinitions is used to feed the definition of the subject areas for Coco Pharmaceuticals.
 */
public enum SubjectAreaSampleDefinitions
{
    ORGANIZATION(   "SubjectAreaDefinition:Organization",
                    null,
                    "Organization",
                    "Information relating to an organization.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to different types of organizational structures and catalogues.",
                    0),

    HOSPITAL(       "SubjectAreaDefinition:Organization:Hospital",
                    "SubjectAreaDefinition:Organization",
                    "Hospital",
                    "Information relating to a hospital's organization.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to different types of hospital organizational structures and catalogues.",
                    0),

    SUPPLIER(       "SubjectAreaDefinition:Organization:Supplier",
                    "SubjectAreaDefinition:Organization",
                    "Supplier",
                    "Information relating to a supplier's organization.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to different types of supplier organizational structures and catalogues.",
                    0),

    PERSON(         "SubjectAreaDefinition:Person",
                    null,
                    "Person",
                    "Information relating to an individual.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to information that describes an individual.",
                    0),

    PATIENT(         "SubjectAreaDefinition:Person:Patient",
                    "SubjectAreaDefinition:Person",
                    "Patient",
                    "Information relating to an individual patient.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to information that describes an individual patient.",
                    0),

    CLINICIAN(         "SubjectAreaDefinition:Person:Clinician",
                    "SubjectAreaDefinition:Person",
                    "Clinician",
                    "Information relating to an individual who works with patients.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to information that describes an individual workign with patients.",
                    0),

    EMPLOYEE(       "SubjectAreaDefinition:Person:Employee",
                    "SubjectAreaDefinition:Person",
                    "Employee",
                    "Information relating to an individual who is employed by an organization.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to information that describes an employee.",
                    0),

    COLLABORATOR(   "SubjectAreaDefinition:Person:Collaborator",
                    "SubjectAreaDefinition:Person",
                    "Collaborator",
                    "Information relating to an individual woth is a business partner.",
                    "Across Coco Pharmaceuticals.",
                    "Controlling data management relating to information that describes an individuals who are business partners.",
                    0),

    CLINICAL(       "SubjectAreaDefinition:Clinical",
                    null,
                    "Clinical",
                    "Information relating to the work understanding medical conditions and their resolution.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    0),

    SYMPTOM(        "SubjectAreaDefinition:Clinical:Symptom",
                    "Symptom:Clinical",
                    "Clinical",
                    "Information relating to the symptoms of a medical condition.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    0),

    MEASUREMENT(    "SubjectAreaDefinition:Clinical:Measurement",
                    "SubjectAreaDefinition:Clinical",
                    "Measurement",
                    "Information relating to the measurements taken to understanding medical conditions and their effectiveness.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    0),

    PERSCRIPTION(   "SubjectAreaDefinition:Clinical:Perscription",
                    "SubjectAreaDefinition:Clinical",
                    "Perscription",
                    "Information relating to the treatment defined for a specific patient.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    0),

    OUTCOME(        "SubjectAreaDefinition:Clinical:Outcome",
                    "SubjectAreaDefinition:Clinical",
                    "Outcome",
                    "Information relating to the work understanding the result of a course of treatment.",
                    "Within research, sales and the data lake.",
                    "Controlling data management relating to research and patient care.",
                    0),

    TREATMENT(      "SubjectAreaDefinition:Treatment",
                    null,
                    "Treatment",
                    "Information relating to the Coco Pharmaceutical products and practices around patient care.",
                    "Within research, sales, manufacturing and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' treatments.",
                    0),

    PRODUCT(        "SubjectAreaDefinition:Treatment:Product",
                    "SubjectAreaDefinition:Treatment",
                    "Product",
                    "Information relating to the Coco Pharmaceutical products.",
                    "Within research, sales, manufacturing and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' treatments.",
                    0),

    ORDER(          "SubjectAreaDefinition:Treatment:Order",
                    "SubjectAreaDefinition:Treatment",
                    "Order",
                    "Information relating to orders for Coco Pharmaceutical products.",
                    "Within research, sales, manufacturing and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' treatments.",
                    0),

    RECIPE(         "SubjectAreaDefinition:Treatment:Recipe",
                    "SubjectAreaDefinition:Treatment",
                    "Recipe",
                    "Information relating to the ingrediatns and manufacturing knowhow for Coco Pharmaceutical products.",
                    "Within research, sales, manufacturing and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' treatments.",
                    0),

    SERVICE_QUALITY("SubjectAreaDefinition:ServiceQuality",
                    null,
                    "Service Quality",
                    "Information relating to the Coco Pharmaceuticals' business operations.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    0),

    CONTRACT       ("SubjectAreaDefinition:ServiceQuality:Contract",
                    "SubjectAreaDefinition:ServiceQuality",
                    "Contract",
                    "Information relating to the Coco Pharmaceuticals' contracts.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    0),

    STOCK          ("SubjectAreaDefinition:ServiceQuality:Stock",
                    "SubjectAreaDefinition:ServiceQuality",
                    "Stock",
                    "Information relating to the Coco Pharmaceuticals' stock management and control.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    0),

    DISTRIBUTION   ("SubjectAreaDefinition:ServiceQuality:Distribution",
                    "SubjectAreaDefinition:ServiceQuality",
                    "Distribution",
                    "Information relating to the Coco Pharmaceuticals' distribution of treatments.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    0),

    INVOICE        ("SubjectAreaDefinition:ServiceQuality:Invoice",
                    "SubjectAreaDefinition:ServiceQuality",
                    "Invoice",
                    "Information relating to the Coco Pharmaceuticals' billing and payments.",
                    "Within research, sales, manufacturing, finance and the data lake.",
                    "Controlling data management relating to Coco Pharmaceuticals' business operations.",
                    0),

    ;


    private String subjectAreaName;
    private String parentName;
    private String displayName;
    private String description;
    private String scope;
    private String usage;
    private int    domain;


    SubjectAreaSampleDefinitions(String name,
                                 String parentName,
                                 String displayName,
                                 String description,
                                 String scope,
                                 String usage,
                                 int    domain)
    {
        this.subjectAreaName = name;
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
     * @return subject area name.
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
     * Get the typical usage of the subject area;
     * @return
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
    }}
