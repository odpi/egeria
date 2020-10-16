/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DivergentDuplicateAnnotation is the annotation used to record details of an asset has been identified as a duplicate because it originally
 * had very similar values.  This annotation records the differences.  As these asset diverge, it may cause updates to be made to the other copies,
 * or the assets may eventually be separated because they actually represent different assets now.  There are many subclasses of this
 * annotation that allow additional information to be logged on exactly what is changing.  An asset may have multiple divergent duplicate
 * annotations - even for the same duplicate.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DivergentAttachmentAnnotation.class, name = "DivergentAttachmentAnnotation"),
                @JsonSubTypes.Type(value = DivergentValueAnnotation.class, name = "DivergentValueAnnotation"),
                @JsonSubTypes.Type(value = DivergentClassificationAnnotation.class, name = "DivergentClassificationAnnotation"),
                @JsonSubTypes.Type(value = DataProfileLogAnnotation.class, name = "DataProfileLogAnnotation"),
                @JsonSubTypes.Type(value = QualityAnnotation.class, name = "QualityAnnotation"),
                @JsonSubTypes.Type(value = RelationshipAdviceAnnotation.class, name = "RelationshipAdviceAnnotation"),
                @JsonSubTypes.Type(value = SemanticAnnotation.class, name = "SemanticAnnotation"),
        })
public class DivergentDuplicateAnnotation extends Annotation
{
    private static final long serialVersionUID = 1L;

    private String  duplicateAnchorGUID = null;


    /**
     * Default constructor
     */
    public DivergentDuplicateAnnotation()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DivergentDuplicateAnnotation(DivergentDuplicateAnnotation template)
    {
        super(template);

        if (template != null)
        {
            duplicateAnchorGUID = template.getDuplicateAnchorGUID();
        }
    }


    /**
     * Return the unique identifier for the Asset that is a confirmed duplicate that is diverging.
     *
     * @return string guid
     */
    public String getDuplicateAnchorGUID()
    {
        return duplicateAnchorGUID;
    }


    /**
     * Set up he unique identifier for the Asset that is a confirmed duplicate that is diverging.
     *
     * @param duplicateAnchorGUID string guid
     */
    public void setDuplicateAnchorGUID(String duplicateAnchorGUID)
    {
        this.duplicateAnchorGUID = duplicateAnchorGUID;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DivergentDuplicateAnnotation{" +
                "duplicateAnchorGUID='" + duplicateAnchorGUID + '\'' +
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
        DivergentDuplicateAnnotation that = (DivergentDuplicateAnnotation) objectToCompare;
        return Objects.equals(duplicateAnchorGUID, that.duplicateAnchorGUID);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), duplicateAnchorGUID);
    }
}
