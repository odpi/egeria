/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.TemplateType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * EventStreamTemplateType describes the catalog templates supplied with Egeria for topics and subscriptions of
 * event streaming services.  Each template creates just the asset, identified by its resource name within a
 * namespace (for example the name and namespace that OpenLineage gives a dataset or job).
 */
public enum EventStreamTemplateType implements TemplateDefinition
{
    /**
     * Create an asset with the Google Cloud Pub/Sub Topic technology type.
     */
    GOOGLE_PUBSUB_TOPIC_TEMPLATE(EventStreamDeployedImplementationType.GOOGLE_PUBSUB_TOPIC.getDeployedImplementationType(),
                                 "Create a " + EventStreamDeployedImplementationType.GOOGLE_PUBSUB_TOPIC.getAssociatedTypeName() + " asset with technology type " + EventStreamDeployedImplementationType.GOOGLE_PUBSUB_TOPIC.getDeployedImplementationType() + ".",
                                 EventStreamDeployedImplementationType.GOOGLE_PUBSUB_TOPIC.getAssociatedTypeName(),
                                 false,
                                 "fd966fc9-009f-4ad1-92ee-2ae9befdbc20",
                                 EventStreamDeployedImplementationType.GOOGLE_PUBSUB_TOPIC,
                                 PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                                 null),

    /**
     * Create an asset with the Google Cloud Pub/Sub Subscription technology type.
     */
    GOOGLE_PUBSUB_SUBSCRIPTION_TEMPLATE(EventStreamDeployedImplementationType.GOOGLE_PUBSUB_SUBSCRIPTION.getDeployedImplementationType(),
                                        "Create a " + EventStreamDeployedImplementationType.GOOGLE_PUBSUB_SUBSCRIPTION.getAssociatedTypeName() + " asset with technology type " + EventStreamDeployedImplementationType.GOOGLE_PUBSUB_SUBSCRIPTION.getDeployedImplementationType() + ".",
                                        EventStreamDeployedImplementationType.GOOGLE_PUBSUB_SUBSCRIPTION.getAssociatedTypeName(),
                                        false,
                                        "93af5b29-4b6e-41de-a123-4ca883442fe0",
                                        EventStreamDeployedImplementationType.GOOGLE_PUBSUB_SUBSCRIPTION,
                                        PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                                        null)

    ;


    /**
     * Symbolic name of the template.
     */
    private final String templateName;

    /**
     * Description of the value to provide for this template.
     */
    private final String templateDescription;

    /**
     * Open metadata type name of the template.
     */
    private final String typeName;

    /**
     * Is this catalog template required for the connector to work successfully.
     */
    private final boolean required;


    /**
     * Option guid for a template to use if no template is specified.
     */
    private final String defaultTemplateGUID;

    /**
     * A map of property name to property value for values that should match in the catalog template for it to be compatible with this integration
     * connector.
     */
    private final Map<String, String> otherPropertyValues;

    private final DeployedImplementationTypeDefinition deployedImplementationType;

    private final List<PlaceholderPropertyType>        placeholderPropertyTypes;


    /**
     * Constructor for Enum
     *
     * @param templateName catalog template name
     * @param typeName open metadata type name for the linked element
     * @param templateDescription deployed implementation type for the linked element
     * @param required is this template required bu the connector
     * @param defaultTemplateGUID is there a default template
     * @param deployedImplementationType       deployed implementation type for the technology
     * @param placeholderPropertyTypes         placeholder variables used in the supplied parameters
     * @param otherPropertyValues other values
     */
    EventStreamTemplateType(String                               templateName,
                      String                               templateDescription,
                      String                               typeName,
                      boolean                              required,
                      String                               defaultTemplateGUID,
                      DeployedImplementationTypeDefinition deployedImplementationType,
                      List<PlaceholderPropertyType>        placeholderPropertyTypes,
                      Map<String, String>                  otherPropertyValues)
    {
        this.templateName               = templateName;
        this.templateDescription        = templateDescription;
        this.typeName                   = typeName;
        this.required                   = required;
        this.defaultTemplateGUID        = defaultTemplateGUID;
        this.deployedImplementationType = deployedImplementationType;
        this.placeholderPropertyTypes   = placeholderPropertyTypes;
        this.otherPropertyValues        = otherPropertyValues;
    }


    /**
     * Return the unique identifier of the template.
     *
     * @return name
     */
    @Override
    public String getTemplateGUID()
    {
        return defaultTemplateGUID;
    }

    /**
     * Return the name of the template.
     *
     * @return name
     */
    @Override
    public String getTemplateName()
    {
        return templateName;
    }


    /**
     * Return the description of the template, such as its content.
     *
     * @return description
     */
    @Override
    public String getTemplateDescription()
    {
        return templateDescription;
    }


    /**
     * Return the version identifier for the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateVersionIdentifier()
    {
        return "6.3-SNAPSHOT";
    }


    /**
     * Return the supported deployed implementation for this template.
     *
     * @return enum
     */
    @Override
    public DeployedImplementationTypeDefinition getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the value to use in the element that describes its version.
     *
     * @return version identifier placeholder
     */
    @Override
    public String getElementVersionIdentifier()
    {
        return PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder();
    }



    /**
     * Return the list of placeholders supported by this template.
     *
     * @return list of placeholder types
     */
    @Override
    public List<PlaceholderPropertyType> getPlaceholders()
    {
        return placeholderPropertyTypes;
    }


    /**
     * Return the list of attributes that should be supplied by the caller using this template.
     *
     * @return list of replacement attributes
     */
    @Override
    public List<ReplacementAttributeType> getReplacementAttributes()
    {
        return null;
    }


    /**
     * Return the open metadata type name.
     *
     * @return open metadata type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return whether this catalog template is required for this service to work successful.
     *
     * @return boolean flag
     */
    public boolean getRequired()
    {
        return required;
    }


    /**
     * Return a map of property name to property value that the catalog template should have to be valid for this integration connector.
     *
     * @return map of string to string
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }


    /**
     * Return all the template types defined by this enum.
     *
     * @return list of catalog template type
     */
    public static List<TemplateType> getTemplateTypes()
    {
        List<TemplateType> templateTypes = new ArrayList<>();

        for (EventStreamTemplateType templateTypeEnum : EventStreamTemplateType.values())
        {
            templateTypes.add(templateTypeEnum.getTemplateType());
        }

        return templateTypes;
    }


    /**
     * Return the catalog template type for a specific catalog template enum.
     *
     * @return catalog template type
     */
    public TemplateType getTemplateType()
    {
        TemplateType templateType = new TemplateType();

        templateType.setName(templateName);
        templateType.setOpenMetadataTypeName(typeName);
        templateType.setDescription(templateDescription);
        templateType.setRequired(required);
        templateType.setDefaultTemplateGUID(defaultTemplateGUID);
        templateType.setOtherPropertyValues(otherPropertyValues);

        return templateType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "EventStreamTemplateType{templateName='" + templateName + "'}";
    }
}
