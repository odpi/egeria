/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager.productcatalog;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * The GovernanceDomainDefinition is used to feed the definition of the governance domains for
 * Coco Pharmaceuticals.
 */
public enum ProductDomainDefinition
{
    /**
     * The rules and processes associated with managing digital products.
     */
    DIGITAL_PRODUCT_MANAGEMENT(10,
                     "Digital Product Management",
                     "The rules and processes associated with managing digital products.",
                     "Digital Product Managers Community"),

    ;


    private final int              domainIdentifier;
    private final String           displayName;
    private final String           description;
    private final String           communityName;


    /**
     * GovernanceDomainDefinition constructor creates an instance of the enum
     *
     * @param domainIdentifier   unique Id for the zone
     * @param displayName   text for the zone
     * @param description   description of the assets in the zone
     * @param communityName name of community driving the
     */
    ProductDomainDefinition(int              domainIdentifier,
                            String           displayName,
                            String           description,
                            String           communityName)
    {
        this.domainIdentifier = domainIdentifier;
        this.displayName = displayName;
        this.description = description;
        this.communityName = communityName;
    }


    /**
     * Returns the unique name for the zone entity.
     *
     * @return qualified name
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(null,
                                                OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                null,
                                                Integer.toString(domainIdentifier));
    }


    /**
     * Return the category for this resourceUse value.
     *
     * @return string
     */
    public String getCategory()
    {
        return constructValidValueNamespace(null,
                                            OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                            null);
    }


    /**
     * Returns the unique name for the zone.
     *
     * @return identifier for domain
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
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
     * Return the name of the community that will coordinate the governance domain.
     *
     * @return string name
     */
    public String getCommunityName()
    {
        return communityName;
    }

    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "GovernanceDomain{" + displayName + '}';
    }
}
