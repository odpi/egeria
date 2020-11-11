/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaAnalysisAnnotation is used to describe the results of reviewing the structure of the content of an asset.
 * This structure is expressed as what is called a schema.  Of then the schema describes a set of nested data fields
 * that each have a name and a type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaAnalysisAnnotation extends Annotation
{
    private static final long    serialVersionUID = 1L;

    private String schemaName           = null;
    private String schemaTypeName       = null;
    private int    discoveredDataFields = 0;


    /**
     * Default constructor
     */
    public SchemaAnalysisAnnotation()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SchemaAnalysisAnnotation(SchemaAnalysisAnnotation  template)
    {
        super(template);

        if (template != null)
        {
            this.schemaName = template.getSchemaName();
            this.schemaTypeName = template.getSchemaTypeName();
        }
    }


    /**
     * Return the name of the schema - this will be used in the creation of the schema object and reflects the content
     * associated with the asset.  The schema that is created/validated is unique to the asset.
     *
     * @return name of schema
     */
    public String getSchemaName()
    {
        return schemaName;
    }


    /**
     * Set up the name of the schema - this will be used in the creation of the schema object and reflects the content
     * associated with the asset.  The schema that is created/validated is unique to the asset.
     *
     * @param schemaName name of schema
     */
    public void setSchemaName(String schemaName)
    {
        this.schemaName = schemaName;
    }


    /**
     * Return the name of the schema type for this asset.
     *
     * @return name
     */
    public String getSchemaTypeName()
    {
        return schemaTypeName;
    }


    /**
     * Set up the name of the schema type for this asset.
     *
     * @param schemaTypeName name
     */
    public void setSchemaTypeName(String schemaTypeName)
    {
        this.schemaTypeName = schemaTypeName;
    }


    /**
     * Return the number of discovered data fields in this schema.
     *
     * @return integer
     */
    public int getDiscoveredDataFields()
    {
        return discoveredDataFields;
    }


    /**
     * Set up the number of discovered data fields in this schema.
     *
     * @param discoveredDataFields integer
     */
    public void setDiscoveredDataFields(int discoveredDataFields)
    {
        this.discoveredDataFields = discoveredDataFields;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaAnalysisAnnotation{" +
                "schemaName='" + schemaName + '\'' +
                ", schemaTypeName='" + schemaTypeName + '\'' +
                ", discoveredDataFields=" + discoveredDataFields +
                ", annotationType='" + getAnnotationType() + '\'' +
                ", summary='" + getSummary() + '\'' +
                ", confidenceLevel=" + getConfidenceLevel() +
                ", expression='" + getExpression() + '\'' +
                ", explanation='" + getExplanation() + '\'' +
                ", analysisStep='" + getAnalysisStep() + '\'' +
                ", jsonProperties='" + getJsonProperties() + '\'' +
                ", annotationStatus=" + getAnnotationStatus() +
                ", numAttachedAnnotations=" + getNumAttachedAnnotations() +
                ", reviewDate=" + getReviewDate() +
                ", steward='" + getSteward() + '\'' +
                ", reviewComment='" + getReviewComment() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", headerVersion=" + getHeaderVersion() +
                ", elementHeader=" + getElementHeader() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {

        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SchemaAnalysisAnnotation that = (SchemaAnalysisAnnotation) objectToCompare;
        return discoveredDataFields == that.discoveredDataFields &&
                Objects.equals(schemaName, that.schemaName) &&
                Objects.equals(schemaTypeName, that.schemaTypeName);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSchemaName(), getSchemaTypeName(), getDiscoveredDataFields());
    }
}
