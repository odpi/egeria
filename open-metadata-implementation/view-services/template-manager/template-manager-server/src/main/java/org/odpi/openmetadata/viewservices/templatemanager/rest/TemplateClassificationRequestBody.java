/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.templatemanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.TemplateClassificationProperties;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TemplateClassificationRequestBody carries the parameters for working with template classifications.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TemplateClassificationRequestBody
{
    private TemplateClassificationProperties       templateClassificationProperties = null;
    private Map<String, List<Map<String, String>>> specification                    = null;

    /**
     * Default constructor
     */
    public TemplateClassificationRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TemplateClassificationRequestBody(TemplateClassificationRequestBody template)
    {
        if (template != null)
        {
            templateClassificationProperties = template.getTemplateClassificationProperties();
            specification = template.getSpecification();
        }
    }


    /**
     * Return the properties that describe the template.
     *
     * @return properties
     */
    public TemplateClassificationProperties getTemplateClassificationProperties()
    {
        return templateClassificationProperties;
    }


    /**
     *  Set up the properties that describe the template.
     *
     * @param templateClassificationProperties properties
     */
    public void setTemplateClassificationProperties(TemplateClassificationProperties templateClassificationProperties)
    {
        this.templateClassificationProperties = templateClassificationProperties;
    }


    /**
     * Return the specification reference data for the template.
     *
     * @return specification (attributeName, list[propertyName, propertyValue])
     */
    public Map<String, List<Map<String, String>>> getSpecification()
    {
        return specification;
    }


    /**
     * Set up the specification reference data for the template.
     *
     * @param specification specification
     */
    public void setSpecification(Map<String, List<Map<String, String>>> specification)
    {
        this.specification = specification;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TemplateClassificationRequestBody{" +
                "templateClassificationProperties=" + templateClassificationProperties +
                ", specification=" + specification +
                '}';
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
        TemplateClassificationRequestBody that = (TemplateClassificationRequestBody) objectToCompare;
        return Objects.equals(templateClassificationProperties, that.templateClassificationProperties) &&
                Objects.equals(specification, that.specification);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(templateClassificationProperties, specification);
    }
}
