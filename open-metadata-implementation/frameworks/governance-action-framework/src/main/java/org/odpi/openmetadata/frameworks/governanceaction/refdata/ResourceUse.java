/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.refdata;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;

import static org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * ResourceUse describes the difference values used in the resourceUse property found in the ResourceList
 * relationship.
 */
public enum ResourceUse
{
    /**
     * Create a survey report that details the content of the real-world resource that is attached to an asset.
     */
    SURVEY_RESOURCE("Survey Resource",
           "Create a survey report that details the content of the real-world resource that is attached to an asset."),

    /**
     * Create a survey report that details the content of the real-world resource that is attached to an asset.
     */
    CATALOG_RESOURCE("Catalog Resource",
            "Extract metadata from the real-world resource and add it to the open metadata repositories.  Ongoing monitoring means that any changes to the resource are reflected in the open metadata repositories."),

    /**
     * Make a change to the real-world resource such as add or copy data.
     */
    PROVISION_RESOURCE("Provision Resource",
                       "Make a change to the real-world resource such as add or copy data."),

    /**
     * Improve the information about an element in the open metadata repositories.
     */
    IMPROVE_METADATA("Improve Metadata Element",
            "Improve the information about an element in the open metadata repositories."),

    /**
     * Send notification to a steward.
     */
    INFORM_STEWARD("Inform Steward",
                     "Send notification to a steward."),


    /**
     * Choose the appropriate path to take.
     */
    CHOOSE_PATH("Choose Path",
                   "Choose the appropriate path to take."),

    /**
     * Monitor for changes to a metadata element and its related elements and take action if required.
     */
    WATCH_DOG("Watch Metadata Element",
                     "Monitor for changes to a metadata element and its related elements and take action if required."),

    /**
     * A team assigned to support the work associated with the element.
     */
    SUPPORTING_PEOPLE("Supporting Team",
                      "A team assigned to support the work associated with the element."),

    /**
     * A person assigned to support the work associated with the element.
     */
    SUPPORTING_PERSON("Supporting Person",
                      "A person assigned to support the work associated with the element."),

    /**
     * A type of service that can be hosted on a type of server.
     */
    HOSTED_SERVICE("Hosted Service", "A type of service that can be hosted on this type of server."),

    /**
     * A type of connector that can be hosted by this type of service/engine..
     */
    HOSTED_CONNECTOR("Hosted Connector", "A type of connector that can be hosted by this type of service/engine."),


    /**
     * A type of governance engine that can be hosted by this type of service.
     */
    HOSTED_GOVERNANCE_ENGINE("Hosted Governance Engine", "A type of governance engine that can be hosted by this type of service."),

    /**
     * A type of service that may be called by this service.
     */
    CALLED_SERVICE("Called Service", "A type of service that may be called by this service."),

    ;

    /**
     * Property value.
     */
    private final String resourceUse;


    /**
     * Property value description.
     */
    private final String description;


    /**
     * Constructor for individual enum value.
     *
     * @param resourceUse the property value to use in resourceUse
     * @param description description of the resource use property value
     */
    ResourceUse(String resourceUse,
                String description)
    {
        this.resourceUse = resourceUse;
        this.description = description;
    }


    /**
     * Return the value of resourceUse.
     *
     * @return string
     */
    public String getResourceUse()
    {
        return resourceUse;
    }


    /**
     * Return the description for this value.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }



    /**
     * Return the qualified name for this resourceUse value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(null,
                                                OpenMetadataProperty.RESOURCE_USE.name,
                                                null,
                                                resourceUse);
    }


    /**
     * Return the category for this resourceUse value.
     *
     * @return string
     */
    public String getCategory()
    {
        return constructValidValueCategory(null,
                                           OpenMetadataProperty.RESOURCE_USE.name,
                                           null);
    }




    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ResourceUse{" + resourceUse + '}';
    }
}
