/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DivergentAttachmentAnnotation is the annotation used to record details attachments to an asset whose values are diverging from the values of a
 * similar attachment in an asset that has been linked as a duplicate.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DivergentAttachmentValueAnnotation.class, name = "DivergentAttachmentValueAnnotation"),
                @JsonSubTypes.Type(value = DivergentAttachmentClassificationAnnotation.class, name = "DivergentAttachmentClassificationAnnotation"),
        })
public class DivergentAttachmentAnnotation extends DivergentDuplicateAnnotation
{
    private static final long serialVersionUID = 1L;

    private String attachmentGUID = null;
    private String duplicateAttachmentGUID = null;


    /**
     * Default constructor
     */
    public DivergentAttachmentAnnotation()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DivergentAttachmentAnnotation(DivergentAttachmentAnnotation template)
    {
        super(template);

        if (template != null)
        {
            attachmentGUID = template.getAttachmentGUID();
            duplicateAttachmentGUID = template.getDuplicateAttachmentGUID();
        }
    }


    /**
     * Return the unique identifier for the attachment to the Asset that is diverging.
     *
     * @return string guid
     */
    public String getAttachmentGUID()
    {
        return attachmentGUID;
    }


    /**
     * Set up the unique identifier for the attachment to the Asset that is diverging.
     *
     * @param attachmentGUID string guid
     */
    public void setAttachmentGUID(String attachmentGUID)
    {
        this.attachmentGUID = attachmentGUID;
    }


    /**
     * Return the unique identifier for the attachment of the other Asset that is diverging.
     *
     * @return string guid
     */
    public String getDuplicateAttachmentGUID()
    {
        return duplicateAttachmentGUID;
    }


    /**
     * Set up the unique identifier for the attachment of the other Asset that is diverging.
     *
     * @param duplicateAttachmentGUID string guid
     */
    public void setDuplicateAttachmentGUID(String duplicateAttachmentGUID)
    {
        this.duplicateAttachmentGUID = duplicateAttachmentGUID;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DivergentAttachmentAnnotation{" +
                "attachmentGUID='" + attachmentGUID + '\'' +
                ", duplicateAttachmentGUID='" + duplicateAttachmentGUID + '\'' +
                ", duplicateAnchorGUIDs='" + getDuplicateAnchorGUID() + '\'' +
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
        DivergentAttachmentAnnotation that = (DivergentAttachmentAnnotation) objectToCompare;
        return Objects.equals(attachmentGUID, that.attachmentGUID) &&
                Objects.equals(duplicateAttachmentGUID, that.duplicateAttachmentGUID);
    }



    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), attachmentGUID, duplicateAttachmentGUID);
    }
}
