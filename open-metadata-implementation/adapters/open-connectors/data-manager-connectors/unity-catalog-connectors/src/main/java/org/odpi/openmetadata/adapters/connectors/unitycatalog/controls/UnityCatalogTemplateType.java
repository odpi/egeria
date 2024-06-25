/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.unitycatalog.controls;

import org.odpi.openmetadata.frameworks.connectors.controls.TemplateType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TemplateTypeEnum characterises the type of templates supported by an integration connector.  It should be copied
 * by developers of an integration connector to help populate the supportedTemplates property of its provider.
 */
public enum UnityCatalogTemplateType
{
    EXAMPLE("exampleTemplateName", "Asset", "Supply the qualified name of the template that the ", null);


    /**
     * Catalog target name.
     */
    private final String name;


    /**
     * The open metadata type name of the element that should be the parent element of the template.
     */
    private final String typeName;


    /**
     * The deployed implementation type allows the connector to be more specific about the resources it works with.
     */
    private final String description;

    /**
     * A map of property name to property value for values that should match in the catalog template for it to be compatible with this integration
     * connector.
     */
    private final Map<String, String> otherPropertyValues;


    /**
     * Constructor for Enum
     *
     * @param name catalog template name
     * @param typeName open metadata type name for the linked element
     * @param description deployed implementation type for the linked element
     * @param otherPropertyValues other values
     */
    UnityCatalogTemplateType(String name, String typeName, String description, Map<String, String> otherPropertyValues)
    {
        this.name                = name;
        this.typeName            = typeName;
        this.description         = description;
        this.otherPropertyValues = otherPropertyValues;
    }


    /**
     * Return the catalog template name.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the type name (or super type name) of a permitted catalog template.
     *
     * @return name of an open metadata type
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return a more specific definition of a permitted catalog template.
     *
     * @return deployed implementation type name
     */
    public String getDescription()
    {
        return description;
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

        for (UnityCatalogTemplateType templateTypeEnum : UnityCatalogTemplateType.values())
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
        TemplateType catalogTargetType = new TemplateType();

        catalogTargetType.setTemplateName(name);
        catalogTargetType.setTypeName(typeName);
        catalogTargetType.setTemplateDescription(description);
        catalogTargetType.setOtherPropertyValues(otherPropertyValues);

        return catalogTargetType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TemplateTypeEnum{templateName='" + name + "'}";
    }
}
