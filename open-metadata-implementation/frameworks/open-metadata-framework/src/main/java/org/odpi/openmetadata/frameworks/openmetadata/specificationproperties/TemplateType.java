/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.specificationproperties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SupportedTemplate defines a template supported by the linked element (typically a connector or governance action).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = SupportedTemplate.class, name = "SupportedTemplate"),
        })
public class TemplateType extends SpecificationProperty
{
    /**
     * Open metadata type name of the template.
     */
    private String openMetadataTypeName = null;

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
            this.openMetadataTypeName = template.getOpenMetadataTypeName();
            this.required             = template.getRequired();
            this.defaultTemplateGUID = template.getDefaultTemplateGUID();
        }
    }


    /**
     * Return the open metadata type name.
     * 
     * @return open metadata type name
     */
    public String getOpenMetadataTypeName()
    {
        return openMetadataTypeName;
    }


    /**
     * Set up open metadata type name.
     * 
     * @param openMetadataTypeName open metadata type name
     */
    public void setOpenMetadataTypeName(String openMetadataTypeName)
    {
        this.openMetadataTypeName = openMetadataTypeName;
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
                "openMetadataTypeName='" + openMetadataTypeName + '\'' +
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
                Objects.equals(openMetadataTypeName, that.openMetadataTypeName) &&
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
        return Objects.hash(super.hashCode(), openMetadataTypeName, required, defaultTemplateGUID);
    }
}
