/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction.controls;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AnnotationTypeType describes an annotation type of survey action service it is part of the metadata to help
 * tools understand the operations of a service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AnnotationTypeType
{
    /**
     * Value to use for the name of the annotation type.
     */
    private String name = null;


    /**
     * The analysis step that produces this annotation.
     */
    public String analysisStepName = null;


    /**
     * The open metadata type used for this annotation type.
     */
    public String openMetadataTypeName = null;


    /**
     * Short description of the annotation type.
     */
    private String summary = null;


    /**
     * Description of the annotation type processing.
     */
    private String explanation = null;


    /**
     * Description of expression used in the annotation type processing.
     */
    private String expression = null;


    /**
     * A map of additional property name to property value for this governance service.
     */
    private Map<String, String> otherPropertyValues = null;


    /**
     * Default constructor
     */
    public AnnotationTypeType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AnnotationTypeType(AnnotationTypeType template)
    {
        if (template != null)
        {
            this.name                 = template.getName();
            this.analysisStepName     = template.getAnalysisStepName();
            this.openMetadataTypeName = template.getOpenMetadataTypeName();
            this.summary              = template.getSummary();
            this.explanation          = template.getExplanation();
            this.expression           = template.getExpression();
            this.otherPropertyValues  = template.getOtherPropertyValues();
        }
    }


    /**
     * Return the name of the annotation type.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the annotation type.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the analysis step that produces this type of annotation.
     *
     * @return completion status
     */
    public String getAnalysisStepName()
    {
        return analysisStepName;
    }


    /**
     * Set up the analysis step that produces this type of annotation.
     *
     * @param analysisStepName string
     */
    public void setAnalysisStepName(String analysisStepName)
    {
        this.analysisStepName = analysisStepName;
    }


    /**
     * Return the name of the open metadata type used for this type of annotation.
     *
     * @return type name
     */
    public String getOpenMetadataTypeName()
    {
        return openMetadataTypeName;
    }


    /**
     * Set up the name of the open metadata type used for this type of annotation.
     *
     * @param openMetadataTypeName typeName
     */
    public void setOpenMetadataTypeName(String openMetadataTypeName)
    {
        this.openMetadataTypeName = openMetadataTypeName;
    }


    /**
     * Return the short description of the annotation type.
     *
     * @return string
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Set up the short description of the annotation type.
     *
     * @param summary string
     */
    public void setSummary(String summary)
    {
        this.summary = summary;
    }


    /**
     * Return the description of the annotation type processing.
     *
     * @return string
     */
    public String getExplanation()
    {
        return explanation;
    }


    /**
     * Set up the description of the annotation type processing.
     *
     * @param explanation string
     */
    public void setExplanation(String explanation)
    {
        this.explanation = explanation;
    }


    /**
     * Return the expression used in the annotation type processing.
     *
     * @return string
     */
    public String getExpression()
    {
        return expression;
    }


    /**
     * Set up the expression used in the annotation type processing.
     *
     * @param expression string
     */
    public void setExpression(String expression)
    {
        this.expression = expression;
    }


    /**
     * Return a map of property name to property value to provide additional information for this governance service.
     *
     * @return map of string to string
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }


    /**
     * Set up a map of property name to property value to provide additional information for this governance service..
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
        return "AnnotationTypeType{" +
                "name='" + name + '\'' +
                ", analysisStepName='" + analysisStepName + '\'' +
                ", openMetadataTypeName='" + openMetadataTypeName + '\'' +
                ", summary='" + summary + '\'' +
                ", explanation='" + explanation + '\'' +
                ", expression='" + expression + '\'' +
                ", otherPropertyValues=" + otherPropertyValues +
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
        AnnotationTypeType that = (AnnotationTypeType) objectToCompare;
        return Objects.equals(name, that.name) &&
                Objects.equals(analysisStepName, that.analysisStepName) &&
                Objects.equals(openMetadataTypeName, that.openMetadataTypeName) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(explanation, that.explanation) &&
                Objects.equals(expression, that.expression) &&
                Objects.equals(otherPropertyValues, that.otherPropertyValues);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(explanation, expression, name, analysisStepName, openMetadataTypeName, otherPropertyValues);
    }
}
