/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata;

import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;

/**
 * A description of the predefined solution blueprints.
 */
public enum SolutionBlueprint
{
    SOLUTION_BLUEPRINT_TEMPLATE("ff8e9750-182c-4f0d-b432-c7a48b8d660b",
                                PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                                PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                true),
    CLINICAL_TRIAL_MANAGEMENT("c4f8d707-7c85-4125-b5fd-c3257a2ef2ef",
                              "Clinical Trial Management Solution Blueprint",
                              "A description of how a clinical trial is managed in Coco Pharmaceuticals",
                              "V1.2",
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
    SolutionBlueprint(String  guid,
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
        return "SolutionBlueprint:" + displayName + ":" + versionIdentifier;
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
