/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.productcatalog;


import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.CocoSubjectAreaDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ScopeDefinition;

/**
 * The CocoSubjectAreaDefinition is used to feed the definition of the subject areas for Coco Pharmaceuticals.
 */
public enum ProductSubjectAreaDefinition
{

    DIGITAL_PRODUCT_MANAGEMENT ("Governance:DigitalProductManagement",
                                CocoSubjectAreaDefinition.GOVERNANCE,
                                "Digital Product Management",
                                "Information relating to the management of digital products.",
                                ScopeDefinition.WITHIN_ORGANIZATION.getPreferredValue(),
                                "Development and management of digital products.",
                                ProductDomainDefinition.DIGITAL_PRODUCT_MANAGEMENT.getDomainIdentifier()),
    ;


    private final String                    subjectAreaName;
    private final CocoSubjectAreaDefinition parent;
    private final String                    displayName;
    private final String                    description;
    private final String                    scope;
    private final String                    usage;
    private final int                       domain;


    ProductSubjectAreaDefinition(String                    name,
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
