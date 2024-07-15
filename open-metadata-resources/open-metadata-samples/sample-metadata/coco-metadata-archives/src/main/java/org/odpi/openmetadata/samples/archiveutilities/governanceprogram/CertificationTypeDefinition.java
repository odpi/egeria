/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;

import org.odpi.openmetadata.samples.archiveutilities.organization.ScopeDefinition;


/**
 * The CertificationTypeDefinition is used to feed the definition of the Certification Types for
 * Coco Pharmaceuticals scenarios.
 */
public enum CertificationTypeDefinition
{
    /**
     * The certification that a particular hospital is approved to participate in the Teddy Bear Drop Foot Clinical Trial.
     */
    DROP_FOOT_APPROVED_HOSPITAL("Approved Hospital for Teddy Bear Drop Foot Clinical Trial",
                 "The hospital is approved to participate in the Teddy Bear Drop Foot Clinical Trial.",
                 "This certification is awarded when the hospital completes the onboarding process for the clinical trail.",
                 "This certification is awarded when the hospital has provided signed data sharing agreements for each of their patient subjects and has agreed to conform to the schema and quality levels laid out for both the patient details and weekly patient measurements data sets.",
                 ScopeDefinition.WITHIN_PROJECT),


    ;


    private final String          title;
    private final String          summary;
    private final String          description;
    private final String          details;
    private final ScopeDefinition scope;


    /**
     * The constructor creates an instance of the enum
     *
     * @param title          unique id for the enum
     * @param summary        short description for the enum
     * @param description   description of the use of this value
     */
    CertificationTypeDefinition(String          title,
                                String          summary,
                                String          description,
                                String          details,
                                ScopeDefinition scope)
    {
        this.title         = title;
        this.summary       = summary;
        this.description   = description;
        this.details       = details;
        this.scope         = scope;
    }

    public String getQualifiedName()
    {
        return "License: " + title;
    }


    public String getTitle() { return title;
    }
    public String getSummary()
    {
        return summary;
    }


    public String getDescription()
    {
        return description;
    }

    public String getDetails() { return details; }

    public ScopeDefinition getScope() {return scope; }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "LicenseTypeDefinition{" + summary + '}';
    }
}
