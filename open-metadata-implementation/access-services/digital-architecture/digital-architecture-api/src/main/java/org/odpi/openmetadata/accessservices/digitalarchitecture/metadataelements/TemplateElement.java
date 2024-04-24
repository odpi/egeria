/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TemplateElement provides the base class for a template element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TemplateElement
{
    private Map<String, List<Map<String, String>>> specification  = null;
    private OpenMetadataElement                    relatedElement = null;

    /**
     * Default constructor
     */
    public TemplateElement()
    {
    }


    /**
     * Return the specification reference data for the attached element.
     *
     * @return specification (attributeName, list[propertyName, propertyValue])
     */
    public Map<String, List<Map<String, String>>> getSpecification()
    {
        return specification;
    }


    /**
     * Set up the specification reference data for the attached element.
     *
     * @param specification specification
     */
    public void setSpecification(Map<String, List<Map<String, String>>> specification)
    {
        this.specification = specification;
    }


    /**
     * Return the element associated with the technology type.
     *
     * @return element object
     */
    public OpenMetadataElement getRelatedElement()
    {
        return relatedElement;
    }


    /**
     * Set up the element associated with the technology type.
     *
     * @param relatedElement element object
     */
    public void setRelatedElement(OpenMetadataElement relatedElement)
    {
        this.relatedElement = relatedElement;
    }


    /**
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "TemplateElement{" +
                "specification=" + specification +
                ", relatedElement=" + relatedElement +
                '}';
    }
}
