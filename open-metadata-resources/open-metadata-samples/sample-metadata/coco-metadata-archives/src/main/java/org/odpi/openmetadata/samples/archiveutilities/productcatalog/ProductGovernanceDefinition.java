/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.productcatalog;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.archiveutilities.organization.ScopeDefinition;
import org.odpi.openmetadata.samples.archiveutilities.sustainability.SustainabilityDomainDefinition;

import java.util.Arrays;
import java.util.List;


/**
 * The GovernanceDefinition is used to feed the definition of the Governance Definitions for
 * Coco Pharmaceuticals sustainability scenarios.
 */
public enum ProductGovernanceDefinition
{
    PRODUCT_OPPORTUNITY("b5ee2a5b-7b46-43d8-9e4b-001824d74f29",
                         OpenMetadataType.BUSINESS_IMPERATIVE.typeName,
                         "The value of data sharing",
                         "Coco Pharmaceuticals must be data-driven",
                         "Our new business initiatives, such as personalized medicine, and our acquisition strategy makes it imperative that valuable data is trusted, shared and used across all divisions of the company.",
                         "High",
                         new String[]{"Data must be treated like a valuable asset."},
                         null,
                         null,
                         ProductDomainDefinition.DIGITAL_PRODUCT_MANAGEMENT.getDomainIdentifier(),
                         ScopeDefinition.ALL_COCO,
                         null),


    ;


    private final String          guid;
    private final String          type;
    private final String          title;
    private final String          summary;
    private final String          description;
    private final String          importance;
    private final String[]        implications;
    private final String[]        outcomes;
    private final String[]        results;
    private final int             domain;
    private final ScopeDefinition scope;
    private final String          externalURL;


    /**
     * The constructor creates an instance of the enum
     *
     * @param guid            unique id for the enum
     * @param type           governance definition type
     * @param title          title the enum
     * @param summary        short description for the enum
     * @param description description of this definition
     * @param importance importance of this definition
     * @param implications implications of implementing this definition
     * @param outcomes expected outcomes
     * @param results results observed from following this definition
     * @param domain governance domain identifier
     * @param scope usage scope
     * @param externalURL link to external information
     */
    ProductGovernanceDefinition(String          guid,
                                String          type,
                                String          title,
                                String          summary,
                                String          description,
                                String          importance,
                                String[]        implications,
                                String[]        outcomes,
                                String[]        results,
                                int             domain,
                                ScopeDefinition scope,
                                String          externalURL)
    {
        this.guid         = guid;
        this.type         = type;
        this.title        = title;
        this.summary      = summary;
        this.description  = description;
        this.importance   = importance;
        this.implications = implications;
        this.outcomes     = outcomes;
        this.results      = results;
        this.domain       = domain;
        this.scope        = scope;
        this.externalURL  = externalURL;
    }

    public String getQualifiedName()
    {
        return type + "::" + title;
    }


    public String getGUID()
    {
        return guid;
    }

    public String getType()
    {
        return type;
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

    public String getImportance() { return importance; }

    public List<String> getImplications()
    {
        if (implications != null)
        {
            return Arrays.asList(implications);
        }

        return null;
    }

    public List<String> getOutcomes()
    {
        if (outcomes != null)
        {
            return Arrays.asList(outcomes);
        }

        return null;
    }

    public List<String> getResults()
    {
        if (results != null)
        {
            return Arrays.asList(results);
        }

        return null;
    }

    public ScopeDefinition getScope() {return scope; }

    /**
     * Return the identifier of the domain that this subject area belongs - 0 means all domains
     *
     * @return domain identifier
     */
    public int getDomain()
    {
        return domain;
    }


    public String getExternalURL()
    {
        return externalURL;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "GovernanceDefinition{" + summary + '}';
    }
}
