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
    DROP_FOOT_APPROVED_HOSPITAL("5acc69bf-dfbb-4e4a-b47b-19d610f2cb06",
                                "Participating Hospital for Teddy Bear Drop Foot Clinical Trial",
                 "The hospital is participating in the Teddy Bear Drop Foot Clinical Trial.",
                 "This certification is started when the hospital completes the necessary preparations for the clinical trail.",
                 "This certification requires the hospital to provided signed data sharing agreements for each of their patient subjects and agrees to supply data that conforms to the schema and quality levels laid out for both the patient details and weekly patient measurements data sets.",
                 ScopeDefinition.WITHIN_PROJECT),


    /**
     * The data in this file matches the specification for the Teddy Bear Drop Foot Clinical Trial.
     */
    DROP_FOOT_APPROVED_DATA("8a921039-ad5f-454d-ae17-e5a5b69f9333",
                            "Valid data for Teddy Bear Drop Foot Clinical Trial",
                            "The data in this file matches the specification for the Teddy Bear Drop Foot Clinical Trial.",
                            "This certification is awarded when the data matches both the schema and the va;lid values specification.",
                            "This certification is added to the weekly patient measurements data sets if they are correctly formatted and the data passes the valid value tests.",
                            ScopeDefinition.WITHIN_PROJECT),


    ;


    private final String          guid;
    private final String          title;
    private final String          summary;
    private final String          description;
    private final String          details;
    private final ScopeDefinition scope;


    /**
     * The constructor creates an instance of the enum
     *
     * @param guid            unique id for the enum
     * @param title          title the enum
     * @param summary        short description for the enum
     * @param description   description of the use of this value
     * @param details qualifying details
     * @param scope usage scope
     */
    CertificationTypeDefinition(String          guid,
                                String          title,
                                String          summary,
                                String          description,
                                String          details,
                                ScopeDefinition scope)
    {
        this.guid          = guid;
        this.title         = title;
        this.summary       = summary;
        this.description   = description;
        this.details       = details;
        this.scope         = scope;
    }

    public String getQualifiedName()
    {
        return "CertificationType: " + title;
    }


    public String getGUID()
    {
        return guid;
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
        return "CertificationTypeDefinition{" + summary + '}';
    }
}
