/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.solutionblueprint;

/**
 * A description of the predefined solution blueprints.  Solution blueprints identify the key solution-oriented
 * investments.  They may be used to document both the as-is and to-be solutions.
 */
public enum ProductSolutionBlueprint
{
    /**
     * Description of the processing used to create and manage the digital products derived from open metadata.
     */
    AUTO_PRODUCT_MANAGER("Open Metadata Digital Product Catalog Solution Blueprint",
                               "Description of the processing used to create and manage the digital products derived from open metadata.",
                               "6.0-SNAPSHOT",
                               false),

    /**
     * escription of the processing used to create and manage the digital products derived from open metadata in the Open Metadata Digital Product Catalog.
     */
    JACQUARD("Jacquard Digital Product Loom Solution Blueprint",
                         "Description of the processing used to create and manage the digital products derived from open metadata in the Open Metadata Digital Product Catalog.",
                         "6.0-SNAPSHOT",
                         false),


    /**
     * Description of the processing used to create and manage subscriptions to Open Metadata Digital Products.
     */
    SUBSCRIPTION_MANAGEMENT("Open Metadata Digital Product Subscriptions Solution Blueprint",
             "Description of the processing used to create and manage subscriptions to Open Metadata Digital Products.",
             "6.0-SNAPSHOT",
             false),

    /**
     * Description of the organization of the open metadata digital product communities.
     */
    PRODUCT_COMMUNITY("Open Metadata Digital Product Community Solution Blueprint",
                            "Description of the organization of the open metadata digital product communities.",
                            "6.0-SNAPSHOT",
                            false),

    ;


    private final String  displayName;
    private final String  description;
    private final String  versionIdentifier;
    private final boolean isTemplate;


    /**
     * Construct an enum instance.
     *
     * @param displayName display name of solution blue print
     * @param description description of solution blueprint
     * @param versionIdentifier version identifier of the solution blueprint
     * @param isTemplate is this a template?
     */
    ProductSolutionBlueprint(String  displayName,
                             String  description,
                             String  versionIdentifier,
                             boolean isTemplate)
    {
        this.displayName       = displayName;
        this.description       = description;
        this.versionIdentifier = versionIdentifier;
        this.isTemplate        = isTemplate;
    }



    /**
     * Return the display name of the solution blueprint.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the solution blueprint
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the version identifier of the solution blueprint.
     *
     * @return string
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Return whether this is a template or not.
     *
     * @return boolean
     */
    public boolean isTemplate()
    {
        return isTemplate;
    }


    /**
     * Return the unique name of the solution blueprint.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "SolutionBlueprint::OpenMetadataDigitalProduct::" + displayName + "::" + versionIdentifier;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SolutionBlueprint{" + displayName + '}';
    }
}
