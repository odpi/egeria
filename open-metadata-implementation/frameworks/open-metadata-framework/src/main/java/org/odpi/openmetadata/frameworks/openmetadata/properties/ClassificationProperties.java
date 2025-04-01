/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.CyberLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.DigitalLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.FixedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.SecureLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.PrimaryKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabasePrimaryKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipProperties provides the base class for relationships items.  This provides extended properties with the ability to
 * set effectivity dates.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ActivityDescriptionProperties.class, name = "ActivityDescriptionProperties"),
                @JsonSubTypes.Type(value = AssetOriginProperties.class, name = "AssetOriginProperties"),
                @JsonSubTypes.Type(value = CanonicalVocabularyProperties.class, name = "CanonicalVocabularyProperties"),
                @JsonSubTypes.Type(value = CyberLocationProperties.class, name = "CyberLocationProperties"),
                @JsonSubTypes.Type(value = DatabasePrimaryKeyProperties.class, name = "DatabasePrimaryKeyProperties"),
                @JsonSubTypes.Type(value = DigitalLocationProperties.class, name = "DigitalLocationProperties"),
                @JsonSubTypes.Type(value = DigitalProductProperties.class, name = "DigitalProductProperties"),
                @JsonSubTypes.Type(value = EditingGlossaryProperties.class, name = "EditingGlossaryProperties"),
                @JsonSubTypes.Type(value = FixedLocationProperties.class, name = "FixedLocationProperties"),
                @JsonSubTypes.Type(value = GlossaryTermContextDefinition.class, name = "GlossaryTermContextDefinition"),
                @JsonSubTypes.Type(value = GovernanceClassificationBase.class, name = "GovernanceClassificationBase"),
                @JsonSubTypes.Type(value = GovernanceMeasurementsDataSetProperties.class, name = "GovernanceMeasurementsDataSetProperties"),
                @JsonSubTypes.Type(value = GovernanceMeasurementsProperties.class, name = "GovernanceMeasurementsProperties"),
                @JsonSubTypes.Type(value = GovernanceExpectationsProperties.class, name = "GovernanceExpectationsProperties"),
                @JsonSubTypes.Type(value = OwnerProperties.class, name = "OwnerProperties"),
                @JsonSubTypes.Type(value = PrimaryKeyProperties.class, name = "PrimaryKeyProperties"),
                @JsonSubTypes.Type(value = SecureLocationProperties.class, name = "SecureLocationProperties"),
                @JsonSubTypes.Type(value = SecurityTagsProperties.class, name = "SecurityTagsProperties"),
                @JsonSubTypes.Type(value = StagingGlossaryProperties.class, name = "StagingGlossaryProperties"),
                @JsonSubTypes.Type(value = SubjectAreaClassificationProperties.class, name = "SubjectAreaClassificationProperties"),
                @JsonSubTypes.Type(value = TaxonomyProperties.class, name = "TaxonomyProperties"),
        })
public class ClassificationProperties
{
    private Date effectiveFrom = null;
    private Date effectiveTo   = null;

    private Map<String, Object> extendedProperties = null;


    /**
     * Default constructor
     */
    public ClassificationProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieve values from the supplied template
     *
     * @param template element to copy
     */
    public ClassificationProperties(ClassificationProperties template)
    {
        if (template != null)
        {
            effectiveFrom = template.getEffectiveFrom();
            effectiveTo = template.getEffectiveTo();
            extendedProperties = template.getExtendedProperties();
        }
    }


    /**
     * Return the date/time that this element is effective from (null means effective from the epoch).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this element is effective from (null means effective from the epoch).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }


    /**
     * Return the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @return property map
     */
    public Map<String, Object> getExtendedProperties()
    {
        return extendedProperties;
    }


    /**
     * Set up the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @param extendedProperties property map
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelationshipProperties{" +
                       "effectiveFrom=" + effectiveFrom +
                       ", effectiveTo=" + effectiveTo +
                       ", extendedProperties=" + extendedProperties +
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
        ClassificationProperties that = (ClassificationProperties) objectToCompare;
        return Objects.equals(effectiveFrom, that.effectiveFrom) &&
                       Objects.equals(effectiveTo, that.effectiveTo);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(effectiveFrom, effectiveTo);
    }
}