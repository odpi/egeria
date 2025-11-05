/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.automatedcuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.SpecificationProperty;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RefDataElementBase provides the base class for an element that is defined using metadata.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RefDataElementBase
{
    protected String                                   displayName               = null;
    protected String                                   description               = null;
    protected Map<String, String>                      additionalProperties      = null;
    private   Map<String, List<SpecificationProperty>> specification             = null;
    private   String                                   specificationMermaidGraph = null;
    private   MetadataElementSummary                   relatedElement            = null;

    /**
     * Default constructor
     */
    public RefDataElementBase()
    {
    }


    /**
     * Default constructor
     */
    public RefDataElementBase(RefDataElementBase template)
    {
        if (template != null)
        {
            this.displayName               = template.getDisplayName();
            this.description               = template.getDescription();
            this.additionalProperties      = template.getAdditionalProperties();
            this.specification             = template.getSpecification();
            this.specificationMermaidGraph = template.getSpecificationMermaidGraph();
            this.relatedElement            = template.getRelatedElement();
        }
    }


    /**
     * Returns the display name property for the element.
     * If no name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the name property for the element.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the description property for the element.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description property associated with the element.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }



    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return the specification reference data for the attached element.
     *
     * @return specification (attributeName, list[propertyName, propertyValue])
     */
    public Map<String, List<SpecificationProperty>> getSpecification()
    {
        return specification;
    }


    /**
     * Set up the specification reference data for the attached element.
     *
     * @param specification specification
     */
    public void setSpecification(Map<String, List<SpecificationProperty>> specification)
    {
        this.specification = specification;
    }


    /**
     * Return the mermaid graph of a linked specification if there is one.
     *
     * @return mermaid markdown
     */
    public String getSpecificationMermaidGraph()
    {
        return specificationMermaidGraph;
    }


    /**
     * Set up the mermaid graph of a linked specification if there is one.
     *
     * @param specificationMermaidGraph mermaid markdown
     */
    public void setSpecificationMermaidGraph(String specificationMermaidGraph)
    {
        this.specificationMermaidGraph = specificationMermaidGraph;
    }


    /**
     * Return the element header associated with the technology type.
     *
     * @return element stub object
     */
    public MetadataElementSummary getRelatedElement()
    {
        return relatedElement;
    }


    /**
     * Set up the element header associated with the technology type.
     *
     * @param relatedElement element stub object
     */
    public void setRelatedElement(MetadataElementSummary relatedElement)
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
        return "RefDataElementBase{" +
                "specification=" + specification +
                ", specificationMermaidGraph='" + specificationMermaidGraph + '\'' +
                ", relatedElement=" + relatedElement +
                '}';
    }
}
