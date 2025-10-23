/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.specificationproperties;

import java.util.Objects;

/**
 * TemplateType defines a template supported by this integration connector.
 */
public class TemplateType extends SpecificationProperty
{
    /**
     * Symbolic name of the template.
     */
    private String templateName = null;

    /**
     * Open metadata type name of the template.
     */
    private String typeName = null;

    /**
     * Is this catalog template required for the connector to work successfully.
     */
    private boolean required = true;

    /**
     * Option guid for a template to use if no template is specified.
     */
    private String defaultTemplateGUID = null;


    /**
     * Constructor
     */
    public TemplateType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TemplateType(TemplateType template)
    {
        super (template);

        if (template != null)
        {
            this.templateName = template.getTemplateName();
            this.typeName = template.getTypeName();
            this.required = template.getRequired();
            this.defaultTemplateGUID = template.getDefaultTemplateGUID();
        }
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
     * Return the unique identifier of the default template to use with this connector - this is optional.
     *
     * @return string
     */
    public String getDefaultTemplateGUID()
    {
        return defaultTemplateGUID;
    }


    /**
     * Set up  the unique identifier of the default template to use with this connector - this is optional.
     *
     * @param defaultTemplateGUID string
     */
    public void setDefaultTemplateGUID(String defaultTemplateGUID)
    {
        this.defaultTemplateGUID = defaultTemplateGUID;
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
                ", typeName='" + typeName + '\'' +
                ", required=" + required +
                ", defaultTemplateGUID='" + defaultTemplateGUID + '\'' +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        TemplateType that = (TemplateType) objectToCompare;
        return required == that.required &&
                Objects.equals(templateName, that.templateName) &&
                Objects.equals(typeName, that.typeName) &&
                Objects.equals(defaultTemplateGUID, that.defaultTemplateGUID);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), templateName, typeName, required, defaultTemplateGUID);
    }
}
