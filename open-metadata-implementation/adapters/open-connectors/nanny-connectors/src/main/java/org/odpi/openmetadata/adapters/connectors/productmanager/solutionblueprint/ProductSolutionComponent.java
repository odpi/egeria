/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.productmanager.solutionblueprint;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;

import java.util.Arrays;
import java.util.List;

/**
 * A description of the predefined solution components.
 */
public enum ProductSolutionComponent
{
    OPEN_METADATA_HARVESTER("3bd61f93-2aca-443d-84cf-330cd50a0c1d",
                            SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                            DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                            "Open Metadata Harvester",
                            "This is the integration connector that is started when the Open Metadata Digital Product Archive is loaded.  It is responsible for detecting opportunities for open metadata digital products, creating a catalog entry for them and then maintaining last update information in the product information.  This is used to drive the distribution of open metadata updates to subscribers.",
                            "5.4-SNAPSHOT",
                            new ProductSolutionBlueprint[]{ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER},
                            null,
                            null),


    OPEN_METADATA_PRODUCT("1759c9fe-71c3-424a-80ca-e202b7dc2577",
                            SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                            DeployedImplementationType.DATA_SET.getDeployedImplementationType(),
                            "Open Metadata Product",
                            "This is the component that represents the data for an open metadata digital products.",
                            "5.4-SNAPSHOT",
                            new ProductSolutionBlueprint[]{ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER},
                            null,
                            null),


    OPEN_METADATA_WATCHDOG("b31abcf4-89bc-477c-870f-c93631253d80",
                          SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                          DeployedImplementationType.WATCHDOG_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType(),
                          "Open Metadata Watchdog",
                          "This is the service that monitors for changes in registered elements and notifies subscribers when changes occur.",
                          "5.4-SNAPSHOT",
                          new ProductSolutionBlueprint[]{ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER},
                          null,
                          null),


    PROVISIONING_PIPELINE("ee19d8f9-caf3-4e74-9082-d63abadd8723",
                           SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
                           DeployedImplementationType.GOVERNANCE_SERVICE.getDeployedImplementationType(),
                           "Digital Product Provisioning Pipeline",
                           "This is the service that copies the product data to each of the subscribers' delivery location.",
                           "5.4-SNAPSHOT",
                           new ProductSolutionBlueprint[]{ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER},
                           null,
                           null),


    PRODUCT_DELIVERY_LOCATION("3c0bf60b-bcff-4cb0-9ca8-65ccd5ecdbbf",
                              SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                              DeployedImplementationType.GOVERNANCE_SERVICE.getDeployedImplementationType(),
                              "Digital Product Delivery Location",
                              "This is the service that copies the product data to each of the subscribers' delivery location.",
                              "5.4-SNAPSHOT",
                              new ProductSolutionBlueprint[]{ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER},
                              null,
                              null),


    PRODUCT_COMMUNITY_COMPONENT("aad04a4c-59f9-4375-83b6-93bd1dac0512",
                              SolutionComponentType.MANUAL_PROCESS.getSolutionComponentType(),
                              null,
                              "Digital Product Community",
                              "The community of interest for those people who are using the open metadata digital products.",
                              "5.4-SNAPSHOT",
                              new ProductSolutionBlueprint[]{ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER},
                              null,
                              null),

    PRODUCT_CATALOG("601b23fa-7da9-4c77-bf1a-ee11c7571718",
                    SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                    null,
                    "Product Catalog",
                    "Supports the process of locating the appropriate digital product by providing organized, searchable descriptions of the digital products to the product consumers.",
                    "5.4-SNAPSHOT",
                    new ProductSolutionBlueprint[]{ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER},
                    null,
                    null),


    NEW_PRODUCT_SUBSCRIPTION("237f8c1c-cb01-46a5-8c0a-62754d2acb66",
                             SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                             null,
                             "Create New Product Subscription",
                             "The process of setting up the subscription for a new consumer.",
                             "5.4-SNAPSHOT",
                             new ProductSolutionBlueprint[]{ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER},
                             null,
                             null),

    CANCEL_PRODUCT_SUBSCRIPTION("e2005ecc-f5a6-4643-b979-a08a13f0984b",
                             SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                             null,
                             "Cancel Product Subscription",
                             "The process of cancelling an existing consumer's subscription to a digital product.",
                             "5.4-SNAPSHOT",
                             new ProductSolutionBlueprint[]{ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER},
                             null,
                             null),


    METADATA_ACCESS_STORE("e6d41398-0b50-454c-943f-6c27ed643fca",
                                SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                null,
                                "Metadata Access Store",
                                "The server that provides access to the open metadata ecosystem..",
                                "5.4-SNAPSHOT",
                                new ProductSolutionBlueprint[]{ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER},
                                null,
                                null),



    ;


    private final String                     guid;
    private final String                     componentType;
    private final String                     implementationType;
    private final String                     displayName;
    private final String                     description;
    private final String                     versionIdentifier;
    private final ProductSolutionBlueprint[] consumingBlueprints;
    private final ProductSolutionComponent[] subComponents;
    private final String                     implementationResource;


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
