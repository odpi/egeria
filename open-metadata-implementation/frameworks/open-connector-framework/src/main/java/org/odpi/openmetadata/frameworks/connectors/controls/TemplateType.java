/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.controls;

import java.util.Map;

/**
 * TemplateType defines a template supported by this integration connector.
 */
public class TemplateType
{
    /**
     * Symbolic name of the template.
     */
    private String templateName = null;

    /**
     * Description of the value to provide for this template.
     */
    private String templateDescription = null;

    /**
     * Open metadata type name for the parent entity of the type name.
     */
    private String typeName = null;

    /**
     * Is this catalog template required for the connector to work successfully.
     */
    private boolean required = true;

    /**
     * A map of property name to property value for values that should match in the catalog template for it to be compatible with this integration
     * connector.
     */
    private Map<String, String> otherPropertyValues = null;

    
    /**
     * Constructor
     */
    public TemplateType()
    {
    }


    /**
     * Return the name of the template.
     *
     * @return name
     */
    public String getTemplateName()
    {
        return templateName;
    }


    /**
     * Set up the name of the template.
     *
     * @param templateName name
     */
    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }


    /**
     * Return the description of the template, such as its content.
     *
     * @return description
     */
    public String getTemplateDescription()
    {
        return templateDescription;
    }


    /**
     * Set up the description of the template, such as its content.
     *
     * @param templateDescription description
     */
    public void setTemplateDescription(String templateDescription)
    {
        this.templateDescription = templateDescription;
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
     * Set up open metadata type name.
     * 
     * @param typeName open metadata type name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
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
     * Set up whether this catalog template is required for this service to work successful.
     *
     * @param required boolean flag
     */
    public void setRequired(boolean required)
    {
        this.required = required;
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
     * Set up a map of property name to property value that the catalog template should have to be valid for this integration connector.
     *
     * @param otherPropertyValues map of string to string
     */
    public void setOtherPropertyValues(Map<String, String> otherPropertyValues)
    {
        this.otherPropertyValues = otherPropertyValues;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TemplateType{" +
                "templateName='" + templateName + '\'' +
                ", templateDescription='" + templateDescription + '\'' +
                ", typeName='" + typeName + '\'' +
                ", required=" + required +
                ", otherPropertyValues=" + otherPropertyValues +
                '}';
    }
}
