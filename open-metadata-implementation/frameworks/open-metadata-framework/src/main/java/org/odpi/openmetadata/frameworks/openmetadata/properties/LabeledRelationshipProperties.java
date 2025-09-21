/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataDescriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessCapabilityDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSupportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.MonitoredResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationSubscriberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.AdjacentLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.KnownLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.NestedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.MoreInformationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.CatalogTemplateProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LabeledRelationshipProperties describe the common properties for a relationship that has a label and a description.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AdjacentLocationProperties.class, name = "AdjacentLocationProperties"),
                @JsonSubTypes.Type(value = BusinessCapabilityDependencyProperties.class, name = "BusinessCapabilityDependencyProperties"),
                @JsonSubTypes.Type(value = CatalogTemplateProperties.class, name = "CatalogTemplateProperties"),
                @JsonSubTypes.Type(value = DataDescriptionProperties.class, name = "DataDescriptionProperties"),
                @JsonSubTypes.Type(value = DigitalSupportProperties.class, name = "DigitalSupportProperties"),
                @JsonSubTypes.Type(value = ExternalReferenceLinkProperties.class, name = "ExternalReferenceLinkProperties"),
                @JsonSubTypes.Type(value = GovernedByProperties.class, name = "GovernedByProperties"),
                @JsonSubTypes.Type(value = InformationSupplyChainLinkProperties.class, name = "InformationSupplyChainLinkProperties"),
                @JsonSubTypes.Type(value = KnownLocationProperties.class, name = "KnownLocationProperties"),
                @JsonSubTypes.Type(value = LineageRelationshipProperties.class, name = "LineageRelationshipProperties"),
                @JsonSubTypes.Type(value = MonitoredResourceProperties.class, name = "MonitoredResourceProperties"),
                @JsonSubTypes.Type(value = MoreInformationProperties.class, name = "MoreInformationProperties"),
                @JsonSubTypes.Type(value = NestedLocationProperties.class, name = "NestedLocationProperties"),
                @JsonSubTypes.Type(value = NotificationSubscriberProperties.class, name = "NotificationSubscriberProperties"),
                @JsonSubTypes.Type(value = PeerDefinitionProperties.class, name = "PeerDefinitionProperties"),
                @JsonSubTypes.Type(value = ProjectDependencyProperties.class, name = "ProjectDependencyProperties"),
                @JsonSubTypes.Type(value = ProjectHierarchyProperties.class, name = "ProjectHierarchyProperties"),
                @JsonSubTypes.Type(value = ReportDependencyProperties.class, name = "ReportDependencyProperties"),
                @JsonSubTypes.Type(value = SolutionLinkingWireProperties.class, name = "SolutionLinkingWireProperties"),
        })
public class LabeledRelationshipProperties extends RelationshipBeanProperties
{
    private String label            = null;
    private String description      = null;


    /**
     * Default constructor
     */
    public LabeledRelationshipProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public LabeledRelationshipProperties(LabeledRelationshipProperties template)
    {
        super(template);

        if (template != null)
        {
            label       = template.getLabel();
            description = template.getDescription();
        }
    }


    /**
     * Return the label used when displaying this relationship.
     *
     * @return string
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Set up the label used when displaying this relationship.
     *
     * @param label string
     */
    public void setLabel(String label)
    {
        this.label = label;
    }


    /**
     * Return the description of the relationship.
     *
     * @return string text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the relationship.
     *
     * @param description string text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LabeledRelationshipProperties{" +
                "label='" + label + '\'' +
                ", description='" + description + '\'' +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare)) return false;
        LabeledRelationshipProperties that = (LabeledRelationshipProperties) objectToCompare;
        return Objects.equals(getLabel(), that.getLabel()) &&
                       Objects.equals(getDescription(), that.getDescription());
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), label, description);
    }
}