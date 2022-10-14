/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ElementClassificationHeader bean describes the header for a classification associated with an element.
 * The ElementClassification is a generic classification.  Then classes such as SecurityTags are specifically featured
 * classifications.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ElementClassification.class, name = "ElementClassification"),
                @JsonSubTypes.Type(value = SecurityTags.class, name = "SecurityTags"),
                @JsonSubTypes.Type(value = GovernanceClassificationBase.class, name = "GovernanceClassificationBase")
        })
public class ElementClassificationHeader extends ElementControlHeader
{
    private static final long    serialVersionUID = 1L;

    private ClassificationOrigin classificationOrigin          = ClassificationOrigin.ASSIGNED;
    private String               classificationOriginGUID      = null;


    /**
     * Default constructor
     */
    public ElementClassificationHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template template to copy
     */
    public ElementClassificationHeader(ElementClassificationHeader template)
    {
        super(template);

        if (template != null)
        {
            classificationOrigin          = template.getClassificationOrigin();
            classificationOriginGUID      = template.getClassificationOriginGUID();
        }
    }


    /**
     * Return whether the classification was added explicitly to this asset or propagated from another entity.
     *
     * @return classification origin enum
     */
    public ClassificationOrigin getClassificationOrigin()
    {
        return classificationOrigin;
    }


    /**
     * Set up the setting for classification origin
     *
     * @param classificationOrigin enum
     */
    public void setClassificationOrigin(ClassificationOrigin classificationOrigin)
    {
        this.classificationOrigin = classificationOrigin;
    }


    /**
     * Return the entity guid that the classification was propagated from (or null if not propagated).
     *
     * @return string guid
     */
    public String getClassificationOriginGUID()
    {
        return classificationOriginGUID;
    }


    /**
     * Set up the origin of the classification.
     *
     * @param classificationOriginGUID string guid
     */
    public void setClassificationOriginGUID(String classificationOriginGUID)
    {
        this.classificationOriginGUID = classificationOriginGUID;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ElementClassificationHeader{" +
                       "classificationOrigin=" + classificationOrigin +
                       ", classificationOriginGUID='" + classificationOriginGUID + '\'' +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", headerVersion=" + getHeaderVersion() +
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
        ElementClassificationHeader that = (ElementClassificationHeader) objectToCompare;
        return classificationOrigin == that.classificationOrigin &&
                Objects.equals(classificationOriginGUID, that.classificationOriginGUID);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), classificationOrigin, classificationOriginGUID);
    }
}
