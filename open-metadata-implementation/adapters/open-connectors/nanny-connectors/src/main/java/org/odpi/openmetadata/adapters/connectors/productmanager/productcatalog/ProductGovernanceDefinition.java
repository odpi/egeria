/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager.productcatalog;

import org.odpi.openmetadata.frameworks.openmetadata.enums.GovernanceDomain;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ScopeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;


/**
 * The GovernanceDefinition is used to feed the definition of the Governance Definitions for the
 * open metadata product catalog.
 */
public enum ProductGovernanceDefinition
{
    DATA_DRIVEN("BUSINESS-IMPERATIVE-05-1",
                OpenMetadataType.BUSINESS_IMPERATIVE.typeName,
                "Data-driven organization",
                "We must be data-driven",
                "Today's business is fast moving and at the same time, we need to demonstrate that the business is operating effectively, legally and ethically.  This means we need to generate, share and preserve key data to support decision making and subsequent business activity.",
                null,
                "Mandatory",
                new String[]{"Key data must be treated like a valuable asset.",
                        "Data management is a requirement for all teams.",
                        "Data about suppliers, employees, customers, our products, and related reference data must be managed so it can be used consistently across all parts of the business.",
                        "Business transaction data is needed to fulfil customer requests.  It may need to be shared between the teams involved in completing the transaction. It must be preserved for audit purposes."},
                new String[]{"Data is available to support decisions and business activity.", "Each part of the business is working from the same data."},
                new String[]{"The business is auditable.", "The business is effective.", "The business is able to evolve."},
                null,
                null,
                null,
                ProductDomainDefinition.DIGITAL_PRODUCT_MANAGEMENT.getDomainIdentifier(),
                ScopeDefinition.WITHIN_ORGANIZATION,
                null),

    ENABLE_DATA_SHARING("POLICY-443-1",
                        OpenMetadataType.GOVERNANCE_POLICY.typeName,
                        "The value of data sharing",
                        "We must be data-driven",
                        "Our new business initiatives and our acquisition strategy makes it imperative that valuable data is trusted, shared and used across all divisions of the organization.  Data is typically generated as a result of activity in a particular division of the organization.",
                        null,
                        "High",
                        new String[]{"Data must be treated like a valuable asset."},
                        null,
                        null,
                        null,
                        null,
                        null,
                        ProductDomainDefinition.DIGITAL_PRODUCT_MANAGEMENT.getDomainIdentifier(),
                        ScopeDefinition.WITHIN_ORGANIZATION,
                        null),

    DIGITAL_PRODUCT_CATALOG("POLICY-731-1",
                            OpenMetadataType.GOVERNANCE_APPROACH.typeName,
                            "Digital Products",
                            "The use of digital products organized in catalogs will improve our ability to share data across the organization.",
                            "Our new business initiatives and our acquisition strategy makes it imperative that valuable data is trusted, shared and used across all divisions of the organization.  Digital products provide a well-defined approach to packaging data in a way that maximizes its potential uses.  Organizing digital products into one or more digital product catalogs helps data consumers find the data that need.  Digital subscriptions create a formal contract between the team providing the data and the teams consuming it.  This ensures data is delivered with the appropriate reliability and quality, and the consumers properly manage the data they have received.",
                            "This approach should be used for all business critical data sharing across the organization.",
                            "High",
                            new String[]{"Teams needs to be educated on how to build and use digital products.",
                                         "New tools and infrastructure is required to support the digital product definitions, subscription and the data delivery mechanisms."},
                            new String[]{"Data sharing increases", "Data used across the business is more consistent.", "Duplicated effort extracting and normalizing external data is reduced."},
                            new String[]{"Valuable data is identified and maintained",
                                         "Data sharing becomes explicit, allowing the organization to identify both legitimate use and teams that are not using the approved sources.",
                                         "Teams with data that should be shared are recognized for this contribution to the business."},
                            null,
                            null,
                            null,
                            ProductDomainDefinition.DIGITAL_PRODUCT_MANAGEMENT.getDomainIdentifier(),
                            ScopeDefinition.WITHIN_ORGANIZATION,
                            null),

    /**
     * This license permits the use of data for internal systems and activities that are conducting legitimate business of the organization.
     */
    INTERNAL_USE_ONLY("LICENSE-TYPE-IOU",
                      OpenMetadataType.LICENSE_TYPE.typeName,
                      "Data for Internal Use",
                      "This data is for business use within the organization.",
                      "This license provides an unrestricted use of data for purposes that support our business.",
                      null,
                      "Mandatory",
                      new String[]{"This license identifies data that may cause commercial harm to this organization if it is disclosed externally."},
                      new String[]{"By identifying internal use only data, the requirements for how this type of data is handled are made clear."},
                      new String[]{"Accidental disclosure of key internal data is avoided."},
                      new String[]{"The data may be used for internal systems and activities that are conducting legitimate business of this organization."},
                      new String[]{"The data must not be distributed to persons or systems outside of the organization."},
                      new String[]{"The data should be secured in a way that restricts access to employees and internal systems.",
                              "Ensure that the latest version of this data is used wherever possible."},
                      GovernanceDomain.UNCLASSIFIED.getOrdinal(),
                      ScopeDefinition.WITHIN_ORGANIZATION,
                      null),

    /**
     * This license permits the use of data for internal systems and activities that are conducting legitimate business of the organization.
     */
    PERSONAL_DATA("LICENSE-TYPE-PERSONAL-DATA",
                      OpenMetadataType.LICENSE_TYPE.typeName,
                      "Personal Information",
                      "This data is for business use within the organization.  It must only be disclosed to people and systems that have a legitimate need and it can only be processed within the approved purposes.",
                      "This license is for data that identifies individuals.  It may be public information, however many privacy legislation restrict the processing that can be performed on personal information.  Therefore this data must be identified and any processing must be within the permitted purposes.  Note that individuals may have rights to personally defined the permitted processing of their data.",
                      null,
                      "Mandatory",
                      new String[]{"This license identifies data that has legal and ethical requirements in how it is stored, processed and how long it is kept."},
                      new String[]{"By identifying personal data, the requirements for how this type of data is handled are made clear."},
                      new String[]{"Accidental disclosure or inappropriate processing is avoided."},
                      new String[]{"The data may be used for internal systems and activities that are conducting legitimate business of this organization providing that the processing is withing the approved purposes."},
                      new String[]{"The data must not be distributed to persons or systems outside of the organization."},
                      new String[]{"The data should be secured in a way that restricts access to employees and internal systems with a need to know.",
                                   "Ensure that the latest version of this data is used wherever possible.",
                                   "This type of data should not be retained if there are no permitted processing purposes."},
                      GovernanceDomain.UNCLASSIFIED.getOrdinal(),
                      ScopeDefinition.WITHIN_ORGANIZATION,
                      null),

    /**
     * This license provides an unrestricted use of data for purposes that support our business.
     */
    CC_BY_40("LICENSE-TYPE-CC-BY-40",
                      OpenMetadataType.LICENSE_TYPE.typeName,
                      "Creative Commons Attribution 4.0 International License (CC BY 4.0)",
                      "This allows users to distribute, remix, adapt, and build upon the material in any medium or format, as long as they provide appropriate credit to the original creator.",
                      "This license provides an unrestricted use of data for purposes that support our business, as long as the originator is properly credited.",
                      "Used when distributing material from outside this organization that has the CC BY 4.0 license.",
                      "Mandatory",
                      null,
                      null,
                      null,
                      new String[]{"The material may be distributed, modified, remixed and adapted.", "The material may be used for commercial purposes."},
                      null,
                      new String[]{"Attribution: Credit must be given to the creator", "Modification: If you modify the material, you must indicate that changes were made"},
                      GovernanceDomain.UNCLASSIFIED.getOrdinal(),
                      ScopeDefinition.WITHIN_ORGANIZATION,
                      "https://creativecommons.org/licenses/by/4.0/"),
    ;


    private final String          identifier;
    private final String          type;
    private final String          displayName;
    private final String          summary;
    private final String          description;
    private final String          usage;
    private final String          importance;
    private final String[]        implications;
    private final String[]        outcomes;
    private final String[]        results;
    private final String[]        entitlements;
    private final String[]        restrictions;
    private final String[]        obligations;
    private final int             domain;
    private final ScopeDefinition scope;
    private final String          externalURL;


    /**
     * The constructor creates an instance of the enum
     *
     * @param identifier            unique id for the enum
     * @param type           governance definition type
     * @param displayName          title the enum
     * @param summary        short description for the enum
     * @param description description of this definition
     * @param usage how should this be used
     * @param importance importance of this definition
     * @param implications implications of implementing this definition
     * @param outcomes expected outcomes
     * @param results results observed from following this definition
     * @param entitlements associated entitlements
     * @param obligations associated obligations
     * @param restrictions associated restrictions
     * @param domain governance domain identifier
     * @param scope usage scope
     * @param externalURL link to external information
     */
    ProductGovernanceDefinition(String          identifier,
                                String          type,
                                String          displayName,
                                String          summary,
                                String          description,
                                String          usage,
                                String          importance,
                                String[]        implications,
                                String[]        outcomes,
                                String[]        results,
                                String[]        entitlements,
                                String[]        restrictions,
                                String[]        obligations,
                                int             domain,
                                ScopeDefinition scope,
                                String          externalURL)
    {
        this.identifier   = identifier;
        this.type         = type;
        this.displayName  = displayName;
        this.summary      = summary;
        this.description  = description;
        this.usage        = usage;
        this.importance   = importance;
        this.implications = implications;
        this.outcomes     = outcomes;
        this.results      = results;
        this.entitlements = entitlements;
        this.restrictions = restrictions;
        this.obligations  = obligations;
        this.domain       = domain;
        this.scope        = scope;
        this.externalURL  = externalURL;
    }


    /**
     * Return the unique identifier for this element.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return type + "::" + identifier + "::" + displayName;
    }


    /**
     * Return the admin identifier for this element.
     *
     * @return string
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Return the open metadata type.
     *
     * @return string
     */
    public String getType()
    {
        return type;
    }


    /**
     * Return the display name.
     *
     * @return string
     */
    public String getDisplayName() { return displayName; }


    /**
     * Return the summary of this element.
     *
     * @return string
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Return the description of the element.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return how this definition should be used.
     *
     * @return string
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Return the importance of the requirements laid out in this definition.
     *
     * @return string
     */
    public String getImportance() { return importance; }


    /**
     * Return the implications of adopting this definition.
     *
     * @return list
     */
    public List<String> getImplications()
    {
        if (implications != null)
        {
            return Arrays.asList(implications);
        }

        return null;
    }



    /**
     * Return the outcomes expected by adopting this definition.
     *
     * @return list
     */
    public List<String> getOutcomes()
    {
        if (outcomes != null)
        {
            return Arrays.asList(outcomes);
        }

        return null;
    }


    /**
     * Return the expected results of adopting this definition.
     *
     * @return list
     */
    public List<String> getResults()
    {
        if (results != null)
        {
            return Arrays.asList(results);
        }

        return null;
    }


    /**
     * Return the associated entitlements.
     *
     * @return list
     */
    public List<String> getEntitlements()
    {
        if (entitlements != null)
        {
            return Arrays.asList(entitlements);
        }

        return null;
    }


    /**
     * Return the associated restriction.
     *
     * @return list
     */
    public List<String> getRestrictions()
    {
        if (restrictions != null)
        {
            return Arrays.asList(restrictions);
        }

        return null;
    }


    /**
     * Return the associated obligations.
     *
     * @return list
     */
    public List<String> getObligations()
    {
        if (obligations != null)
        {
            return Arrays.asList(obligations);
        }

        return null;
    }


    /**
     * Return the scope of this definition when it is used.
     *
     * @return scope enum
     */
    public ScopeDefinition getScope() { return scope; }


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
     * Return the link to an external web page.
     *
     * @return url string
     */
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
        return "ProductGovernanceDefinition{" + displayName + '}';
    }
}
