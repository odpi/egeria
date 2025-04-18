/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;

import org.odpi.openmetadata.samples.archiveutilities.organization.ScopeDefinition;


/**
 * The LicenseTypeDefinition is used to feed the definition of the LicenseTypes for
 * Coco Pharmaceuticals scenarios.
 */
public enum LicenseTypeDefinition
{
    /**
     * The license granted by the participant in a clinical trial on the use of their data.
     */
    CLINICAL_TRIAL_LICENSE("Standard Clinical Trial License",
                 "The license granted by the participant in a clinical trial on the use of their data.",
                 "This license is part of the agreement made between participant of a clinical trial and Coco Pharmaceuticals.  It describes the types of processing and governance that are permitted/required concerning the use of the data collected during a clinical trial.",
                 "This license requires that all data transmitted over a public network is either de-identified or encrypted.  The data can be used for assessing the efficacy of the treatment under test as well as ongoing research into similar conditions/treatments.  It may not be used for any other purposes that jeopardizes the anonymity of the participants since the identity of participants in a clinical trial is confidential.  It may not be transmitted to any third parties without the explicit permission of the affected participant. The participants understand that for legal compliance reasons, Coco Pharmaceuticals will need to retain the data for 20 years.",
                 ScopeDefinition.ALL_COCO),
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
    LicenseTypeDefinition(String          title,
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
        return "License::" + title;
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
