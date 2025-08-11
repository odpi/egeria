/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.productcatalog;

import org.odpi.openmetadata.samples.archiveutilities.organization.ScopeDefinition;


/**
 * The ProductLicenseTypeDefinition is used to feed the definition of the LicenseTypes for
 * Coco Pharmaceuticals' product catalog scenarios.
 */
public enum ProductLicenseTypeDefinition
{
    /**
     * This license permits the subscriber to use the data for internal systems and activities that are conducting legitimate business of Coco Pharmaceuticals.
     */
    INTERNAL_USE_ONLY("Internal Use Only License",
                 "This license permits the subscriber to use the data for internal systems and activities that are conducting legitimate business of Coco Pharmaceuticals.",
                 "This license provides an unrestricted use of data for purposes that support the Coco Pharmaceuticals business.  The data may not be distributed outside of Coco Pharmaceuticals.",
                 "There are no additional requirements beyond ensuring that the latest version of this data is used wherever possible.",
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
    ProductLicenseTypeDefinition(String          title,
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
        return "ProductLicense::" + title;
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
        return "ProductLicenseTypeDefinition{" + summary + '}';
    }
}
