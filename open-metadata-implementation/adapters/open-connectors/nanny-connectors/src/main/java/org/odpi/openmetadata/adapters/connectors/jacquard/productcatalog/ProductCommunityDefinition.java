/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;


/**
 * The CommunityDefinition is used to feed the definition of the communities associated with the open metadata
 * product catalog.
 */
public enum ProductCommunityDefinition
{
    /**
     * ReferenceDataSIG - Community of people focused on reference data products derived from the open metadata ecosystem.
     */
    REFERENCE_DATA_SIG("ReferenceDataSIG",
                       "Open Metadata Reference Data special interest group",
                       "Community of people focused on reference data products derived from the open metadata ecosystem."),

    /**
     * MasterDataSIG - Community of people focused on master data products derived from the open metadata ecosystem.
     */
    MASTER_DATA_SIG("MasterDataSIG",
                    "Open Metadata Master Data special interest group",
                    "Community of people focused on master data products derived from the open metadata ecosystem."),


    /**
     * ObservabilitySIG - Community of people focused on the observability products derived from the open metadata ecosystem.
     */
    OBSERVABILITY_SIG("ObservabilitySIG",
                      "Open Metadata Observability special interest group",
                      "Community of people focused on the observability products derived from the open metadata ecosystem."),

    ;

    private final String identifier;
    private final String displayName;
    private final String description;


    /**
     * The constructor creates an instance of the enum
     *
     * @param identifier   unique id for the enum
     * @param displayName   name for the enum
     * @param description   description of the use of this value
     */
    ProductCommunityDefinition(String identifier,
                               String displayName,
                               String description)
    {
        this.identifier  = identifier;
        this.displayName = displayName;
        this.description = description;

    }

    /**
     * Return the qualified name to use for the community.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "Community::OpenMetadataProductCatalog" + identifier;
    }

    /**
     * Return the identifier of the community.
     *
     * @return string
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Return the display name of the community.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the community.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "CommunityDefinition{" + displayName + '}';
    }
}
