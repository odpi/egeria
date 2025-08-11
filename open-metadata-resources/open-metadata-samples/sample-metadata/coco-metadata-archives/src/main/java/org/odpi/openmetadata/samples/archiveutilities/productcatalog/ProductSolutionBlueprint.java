/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.productcatalog;

/**
 * A description of the predefined solution blueprints.  Solution blueprints identify the key solution oriented
 * investments.  They may be used to document both the as-is and to-be solutions.
 */
public enum ProductSolutionBlueprint
{
    /**
     * Harvesting valid value sets as digital products.
     */
    COCO_COMBO_PRODUCTS("ce881b7b-048a-4cb0-b3b6-b5c42d32ea36",
                        "CocoCombo Products Solution Blueprint",
                        "Harvesting valid value sets from CocoComboArchive as digital products.",
                        "V1.0",
                        false),


    ;


    private final String  guid;
    private final String  displayName;
    private final String  description;
    private final String  versionIdentifier;
    private final boolean isTemplate;


    /**
     * Construct an enum instance.
     *
     * @param guid unique identifier
     * @param displayName display name of solution blue print
     * @param description description of solution blueprint
     * @param versionIdentifier version identifier of the solution blueprint
     * @param isTemplate is this a template?
     */
    ProductSolutionBlueprint(String  guid,
                             String  displayName,
                             String  description,
                             String  versionIdentifier,
                             boolean isTemplate)
    {
        this.guid              = guid;
        this.displayName       = displayName;
        this.description       = description;
        this.versionIdentifier = versionIdentifier;
        this.isTemplate        = isTemplate;
    }




    /**
     * Return the GUID for the element.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
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
