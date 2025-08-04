/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetEncodingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.ListenerInterfaceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.PublisherInterfaceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.RequestResponseInterfaceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.CyberLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.FixedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.SecureLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.CalculatedValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.PrimaryKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.TypeEmbeddedAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ReferenceDataProperties;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ClassificationProperties provides the base class for classification beans.
 * This provides extended properties to allow the existing classification types to be extended.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AbstractConceptProperties.class, name = "AbstractConceptProperties"),
                @JsonSubTypes.Type(value = ActivityDescriptionProperties.class, name = "ActivityDescriptionProperties"),
                @JsonSubTypes.Type(value = AnchorsProperties.class, name = "AnchorsProperties"),
                @JsonSubTypes.Type(value = CalculatedValueProperties.class, name = "CalculatedValueProperties"),
                @JsonSubTypes.Type(value = CanonicalVocabularyProperties.class, name = "CanonicalVocabularyProperties"),
                @JsonSubTypes.Type(value = CyberLocationProperties.class, name = "CyberLocationProperties"),
                @JsonSubTypes.Type(value = DataAssetEncodingProperties.class, name = "DataAssetEncodingProperties"),
                @JsonSubTypes.Type(value = DataScopeProperties.class, name = "DataScopeProperties"),
                @JsonSubTypes.Type(value = DataValueProperties.class, name = "DataValueProperties"),
                @JsonSubTypes.Type(value = DigitalResourceOriginProperties.class, name = "DigitalResourceOriginProperties"),
                @JsonSubTypes.Type(value = EditingGlossaryProperties.class, name = "EditingGlossaryProperties"),
                @JsonSubTypes.Type(value = FixedLocationProperties.class, name = "FixedLocationProperties"),
                @JsonSubTypes.Type(value = ContextDefinitionProperties.class, name = "ContextDefinitionProperties"),
                @JsonSubTypes.Type(value = GovernanceClassificationBase.class, name = "GovernanceClassificationBase"),
                @JsonSubTypes.Type(value = GovernanceMeasurementsResultsDataSetProperties.class, name = "GovernanceMeasurementsResultsDataSetProperties"),
                @JsonSubTypes.Type(value = GovernanceMeasurementsProperties.class, name = "GovernanceMeasurementsProperties"),
                @JsonSubTypes.Type(value = GovernanceExpectationsProperties.class, name = "GovernanceExpectationsProperties"),
                @JsonSubTypes.Type(value = ListenerInterfaceProperties.class, name = "ListenerInterfaceProperties"),
                @JsonSubTypes.Type(value = OwnershipProperties.class, name = "OwnershipProperties"),
                @JsonSubTypes.Type(value = PrimaryKeyProperties.class, name = "PrimaryKeyProperties"),
                @JsonSubTypes.Type(value = PublisherInterfaceProperties.class, name = "PublisherInterfaceProperties"),
                @JsonSubTypes.Type(value = ReferenceDataProperties.class, name = "ReferenceDataProperties"),
                @JsonSubTypes.Type(value = RequestResponseInterfaceProperties.class, name = "RequestResponseInterfaceProperties"),
                @JsonSubTypes.Type(value = SecureLocationProperties.class, name = "SecureLocationProperties"),
                @JsonSubTypes.Type(value = SecurityGroupMembershipProperties.class, name = "SecurityGroupMembershipProperties"),
                @JsonSubTypes.Type(value = SecurityTagsProperties.class, name = "SecurityTagsProperties"),
                @JsonSubTypes.Type(value = StagingGlossaryProperties.class, name = "StagingGlossaryProperties"),
                @JsonSubTypes.Type(value = SubjectAreaProperties.class, name = "SubjectAreaProperties"),
                @JsonSubTypes.Type(value = TaxonomyProperties.class, name = "TaxonomyProperties"),
                @JsonSubTypes.Type(value = TemplateProperties.class, name = "TemplateProperties"),
                @JsonSubTypes.Type(value = TypeEmbeddedAttributeProperties.class, name = "TypeEmbeddedAttributeProperties"),
                @JsonSubTypes.Type(value = ZoneMembershipProperties.class, name = "ZoneMembershipProperties"),
        })
public class ClassificationBeanProperties extends ClassificationProperties
{
    private String               typeName             = null;
    private Map<String, Object>  extendedProperties   = null;


    /**
     * Default constructor
     */
    public ClassificationBeanProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieve values from the supplied template
     *
     * @param template element to copy
     */
    public ClassificationBeanProperties(ClassificationBeanProperties template)
    {
        super(template);

        if (template != null)
        {
            typeName           = template.getTypeName();
            extendedProperties = template.getExtendedProperties();
        }
    }


    /**
     * Return the name of the open metadata type for this classification.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the name of the open metadata type for this classification.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
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
        return "ClassificationBeanProperties{" +
                "typeName='" + typeName + '\'' +
                ", extendedProperties=" + extendedProperties +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ClassificationBeanProperties that = (ClassificationBeanProperties) objectToCompare;
        return Objects.equals(typeName, that.typeName) && Objects.equals(extendedProperties, that.extendedProperties);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), typeName, extendedProperties);
    }
}