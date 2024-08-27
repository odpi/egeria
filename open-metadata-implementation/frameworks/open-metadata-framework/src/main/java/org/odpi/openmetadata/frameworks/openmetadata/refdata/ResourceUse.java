/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.List;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

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
           "Create a survey report that details the content of the real-world resource that is attached to an asset.",
                    null),

    /**
     * Create a survey report that details whether the content of the real-world resource that is attached to an asset is withing the bounds required by its data specification.
     */
    VALIDATE_RESOURCE("Validate Resource",
                    "Creates a certification relationship between a certification type and a resource (asset, person, project, ...) that has passed the appropriate quality checks.",
                    null),

    /**
     * Create a survey report that details the content of the real-world resource that is attached to an asset.
     */
    CERTIFY_RESOURCE("Certify Resource",
                    "Creates a certification relationship between a certification type and a resource (asset, person, project, ...) that has passed the appropriate quality checks.",
                    null),

    /**
     * Extract metadata from the real-world resource and add it to the open metadata repositories.  Ongoing monitoring means that any changes to the resource are reflected in the open metadata repositories.
     */
    CATALOG_RESOURCE("Catalog Resource",
            "Extract metadata from the real-world resource and add it to the open metadata repositories.  Ongoing monitoring means that any changes to the resource are reflected in the open metadata repositories.",
                     null),

    /**
     * Make a change to the real-world resource such as add or copy data.
     */
    PROVISION_RESOURCE("Provision Resource",
                       "Make a change to the real-world resource such as add or copy data.",
                       null),

    /**
     * Improve the information about an element in the open metadata repositories.
     */
    IMPROVE_METADATA("Improve Metadata Element",
            "Improve the information about an element in the open metadata repositories.",
                     null),

    /**
     * Send notification to a steward.
     */
    INFORM_STEWARD("Inform Steward",
                     "Send notification to a steward.",
                   null),

    /**
     * Choose the appropriate path to take.
     */
    CHOOSE_PATH("Choose Path",
                   "Choose the appropriate path to take.",
                null),

    /**
     * Monitor for changes to a metadata element and its related elements and take action if required.
     */
    WATCH_DOG("Watch Metadata Element",
                     "Monitor for changes to a metadata element and its related elements and take action if required.",
              null),

    /**
     * A team assigned to support the work associated with the element.
     */
    SUPPORTING_PEOPLE("Supporting Team",
                      "A team assigned to support the work associated with the element.",
                      null),

    /**
     * A person assigned to support the work associated with the element.
     */
    SUPPORTING_PERSON("Supporting Person",
                      "A person assigned to support the work associated with the element.",
                      null),

    /**
     * A template that can be used in conjunction with a governance action or connector.
     */
    SUPPORTING_TEMPLATE("Supporting Template",
                        "A template that can be used in conjunction with a governance action or connector.",
                        null),

    /**
     * A process that automates some of the work associated with the element.
     */
    SUPPORTING_PROCESS("Supporting Process",
                       "A process that automates some of the work associated with the element.",
                       null),

    /**
     * A type of service that can be hosted on a type of server.
     */
    HOSTED_SERVICE("Hosted Service",
                   "A type of service that can be hosted on this type of server.",
                   null),

    /**
     * A type of connector that can be hosted by this type of service/engine.
     */
    HOSTED_CONNECTOR("Hosted Connector",
                     "A type of connector that can be hosted by this type of service/engine.",
                     null),

    /**
     * A type of governance engine that can be hosted by this type of service.
     */
    HOSTED_GOVERNANCE_ENGINE("Hosted Governance Engine",
                             "A type of governance engine that can be hosted by this type of service.",
                             null),

    /**
     * A type of service that may be called by this service.
     */
    CALLED_SERVICE("Called Service",
                   "A type of service that may be called by this service.",
                   null),

    /**
     * A connector type that can be used in a connection used to create a connector instance.
     */
    CONFIGURE_CONNECTOR("Configure Connector",
                        "A connector type that can be used in a connection used to create a connector instance.",
                        null),

    /**
     * A collection of guided activities either suggested, or taken, to complete a project.
     */
    ACTIVITY_FOLDER("Activity Folder",
                    "A collection of guided activities either suggested, or taken, to complete a project.",
                    new ResourceUseProperties[]{ResourceUseProperties.PARENT_RELATIONSHIP_TYPE_NAME}),

    /**
     * A template to create a member of the linked collection.
     */
    MEMBER_TEMPLATE("Member Template",
                    "A template to create a member of the linked collection.",
                    new ResourceUseProperties[]{ResourceUseProperties.PARENT_RELATIONSHIP_TYPE_NAME}),

    /**
     * A template to create an element from the values in the linked element.  The placeholder properties match the attribute names of the linked element.
     */
    DERIVED_ELEMENT_TEMPLATE("Derived Element Template",
                             "A template to create an element from the values in the linked element.  The placeholder properties match the attribute names of the linked element.",
                             new ResourceUseProperties[]{ResourceUseProperties.PARENT_RELATIONSHIP_TYPE_NAME}),



    ;

    /**
     * Property value.
     */
    private final String resourceUse;


    /**
     * Property value description.
     */
    private final String description;


    private final ResourceUseProperties[] resourceUseProperties;


    /**
     * Constructor for individual enum value.
     *
     * @param resourceUse the property value to use in resourceUse
     * @param description description of the resource use property value
     * @param resourceUseProperties resource use properties used with this resource use value
     */
    ResourceUse(String                  resourceUse,
                String                  description,
                ResourceUseProperties[] resourceUseProperties)
    {
        this.resourceUse = resourceUse;
        this.description = description;
        this.resourceUseProperties = resourceUseProperties;
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
     * Return the resource use properties associated with this resourceUse value
     *
     * @return null or list
     */
    public List<ResourceUseProperties> getResourceUseProperties()
    {
        if (resourceUseProperties != null)
        {
            return List.of(resourceUseProperties);
        }

        return null;
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
