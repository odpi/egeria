/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.productmanager.solutionblueprint;

/**
 * A description of the predefined solution blueprints.  Solution blueprints identify the key solution oriented
 * investments.  They may be used to document both the as-is and to-be solutions.
 */
public enum ProductSolutionBlueprint
{
    /**
     * Description of the processing used to create the content packs shipped with Egeria.
     */
    OPEN_METADATA_CONTENT_PACK("Open Metadata Archive Solution Blueprint",
                          "Description of the processing used to create the content packs shipped with Egeria.  These content packs contain open metadata that is generally useful to teams starting to use Egeria.  The core content pack and the open metadata types are loaded automatically when the metadata access store starts up.  The other content packs are loaded on demand.",
                          "5.4-SNAPSHOT",
                          false),

    /**
     * Description of the processing used to create the content packs shipped with Egeria.
     */
    AUTO_PRODUCT_MANAGER("Open Metadata Digital Product Solution Blueprint",
                               "Description of the processing used to manage the digital products derived from open metadata.",
                               "5.4-SNAPSHOT",
                               false),


    /**
     * Description of the simple open metadata deployment environment used for evaluation and small team deployments.
     */
    EGERIA_WORKSPACES("Egeria Workspaces Solution Blueprint",
                         "Description of the simple open metadata deployment environment used for evaluation and small team deployments.  It includes a configured Egeria runtime plus Apache Kafka, a PostgreSQL server, and an OpenLineage proxy (for receiving open lineage events).  There are optional packages for Marquez, Apache Airflow, Unity Catalog and Apache Superset",
                         "5.4-SNAPSHOT",
                         false),


    /**
     * Description of the services configured by Egeria's build process to create a simple open metadata deployment environment used for evaluation.
     */
    DEFAULT_RUNTIME("Egeria Default Runtime Solution Blueprint",
                      "Description of the services configured by Egeria's build process to create a simple open metadata deployment environment used for evaluation.",
                      "5.4-SNAPSHOT",
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
        return "SolutionBlueprint::" + displayName + "::" + versionIdentifier;
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
