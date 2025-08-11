/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.productcatalog;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;

import java.util.Arrays;
import java.util.List;

/**
 * A description of the predefined solution components.
 */
public enum ProductSolutionComponent
{

    COCO_ARCHIVE("f78e70d6-053b-42aa-ba42-f6f2900baef1",
             SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
             "JSON File",
             "CocoComboArchive",
             "This is the open metadata archive that is manufactured during the Egeria build..",
             "V1.0",
             new ProductSolutionBlueprint[]{ProductSolutionBlueprint.COCO_COMBO_PRODUCTS},
             null,
             null),



    ;


    private final String                          guid;
    private final String                          componentType;
    private final String                          implementationType;
    private final String                          displayName;
    private final String                          description;
    private final String                          versionIdentifier;
    private final ProductSolutionBlueprint[]        consumingBlueprints;
    private final ProductSolutionComponent[] subComponents;
    private final String                   implementationResource;


    /**
     * Construct an enum instance.
     *
     * @param guid unique identifier
     * @param componentType   type of solution component - ege automated process
     * @param implementationType   type of software component - for example, is it a process, of file or database.
     * @param displayName display name of solution component
     * @param description description of solution component
     * @param versionIdentifier version identifier of the solution component
     * @param consumingBlueprints the blueprint that this belongs to
     * @param subComponents optional subcomponents of the solution
     * @param implementationResource components useful when creating implementations
     */
    ProductSolutionComponent(String                     guid,
                             String                     componentType,
                             String                     implementationType,
                             String                     displayName,
                             String                     description,
                             String                     versionIdentifier,
                             ProductSolutionBlueprint[] consumingBlueprints,
                             ProductSolutionComponent[] subComponents,
                             String                     implementationResource)
    {
        this.guid                   = guid;
        this.componentType          = componentType;
        this.implementationType     = implementationType;
        this.displayName            = displayName;
        this.description            = description;
        this.versionIdentifier      = versionIdentifier;
        this.consumingBlueprints    = consumingBlueprints;
        this.subComponents          = subComponents;
        this.implementationResource = implementationResource;
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
     * Return the type of solution component - for example, is it a process, of file or database.
     *
     * @return string
     */
    public String getComponentType()
    {
        return componentType;
    }


    /**
     * Return which type of software component is likely to be deployed to implement this solution component.
     *
     * @return string
     */
    public String getImplementationType()
    {
        return implementationType;
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


    public List<ProductSolutionBlueprint> getConsumingBlueprints()
    {
        if (consumingBlueprints == null)
        {
            return null;
        }

        return Arrays.asList(consumingBlueprints);
    }


    /**
     * Return the optional list of subcomponents.
     *
     * @return null or list
     */
    public List<ProductSolutionComponent> getSubComponents()
    {
        if (subComponents == null)
        {
            return null;
        }

        return Arrays.asList(subComponents);
    }


    /**
     * Return the GUID of the implementation element (or null)
     *
     * @return guid
     */
    public String getImplementationResource()
    {
        return implementationResource;
    }

    /**
     * Return the unique name of the solution blueprint.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "ProductSolutionComponent::" + displayName + "::" + versionIdentifier;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProductSolutionComponent{" + displayName + '}';
    }
}
