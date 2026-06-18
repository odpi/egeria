/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssociatedSkillSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PeerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.LinkedMediaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionRequesterProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportOriginatorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportSubjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingSpecificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DetailedProcessingActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.PermittedProcessingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptDesignProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns.NestedDesignPatternProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns.RelatedDesignPatternProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns.SpecializedDesignPatternProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessCapabilityDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSupportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.LineageRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.AdjacentLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.KnownLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.NestedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.propertyfacets.ReferenceableFacetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.MoreInformationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ResourcePermissionsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecretsCollectionSecurityListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.UserAccountProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.NetworkGatewayLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionDesignProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationExtensionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationMatchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AssociatedAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ReportedAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.CatalogTemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

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
                @JsonSubTypes.Type(value = ActionsProperties.class, name = "ActionsProperties"),
                @JsonSubTypes.Type(value = AdjacentLocationProperties.class, name = "AdjacentLocationProperties"),
                @JsonSubTypes.Type(value = AnnotationExtensionProperties.class, name = "AnnotationExtensionProperties"),
                @JsonSubTypes.Type(value = AnnotationMatchProperties.class, name = "AnnotationMatchProperties"),
                @JsonSubTypes.Type(value = ApprovedPurposeProperties.class, name = "ApprovedPurposeProperties"),
                @JsonSubTypes.Type(value = AssociatedAnnotationProperties.class, name = "AssociatedAnnotationProperties"),
                @JsonSubTypes.Type(value = AssociatedSkillSetProperties.class, name = "AssociatedSkillSetProperties"),
                @JsonSubTypes.Type(value = BusinessCapabilityDependencyProperties.class, name = "BusinessCapabilityDependencyProperties"),
                @JsonSubTypes.Type(value = CatalogTemplateProperties.class, name = "CatalogTemplateProperties"),
                @JsonSubTypes.Type(value = ConceptDesignProperties.class, name = "ConceptDesignProperties"),
                @JsonSubTypes.Type(value = ContextEventEvidenceProperties.class, name = "ContextEventEvidenceProperties"),
                @JsonSubTypes.Type(value = ContextEventForTimelineEffectsProperties.class, name = "ContextEventForTimelineEffectsProperties"),
                @JsonSubTypes.Type(value = ContextEventImpactProperties.class, name = "ContextEventImpactProperties"),
                @JsonSubTypes.Type(value = DataValueAssignmentProperties.class, name = "DataValueAssignmentProperties"),
                @JsonSubTypes.Type(value = DataClassCompositionProperties.class, name = "DataClassCompositionProperties"),
                @JsonSubTypes.Type(value = DataValueDefinitionProperties.class, name = "DataValueDefinitionProperties"),
                @JsonSubTypes.Type(value = DataValueHierarchyProperties.class, name = "DataValueHierarchyProperties"),
                @JsonSubTypes.Type(value = DataDescriptionProperties.class, name = "DataDescriptionProperties"),
                @JsonSubTypes.Type(value = DataProcessingSpecificationProperties.class, name = "DataProcessingSpecificationProperties"),
                @JsonSubTypes.Type(value = DataProcessingTargetProperties.class, name = "DataProcessingTargetProperties"),
                @JsonSubTypes.Type(value = DataStructureDefinitionProperties.class, name = "DataStructureDefinitionProperties"),
                @JsonSubTypes.Type(value = DependentContextEventProperties.class, name = "DependentContextEventProperties"),
                @JsonSubTypes.Type(value = DetailedProcessingActionProperties.class, name = "DetailedProcessingActionProperties"),
                @JsonSubTypes.Type(value = DigitalSupportProperties.class, name = "DigitalSupportProperties"),
                @JsonSubTypes.Type(value = ExceptionProperties.class, name = "ExceptionProperties"),
                @JsonSubTypes.Type(value = ExternalReferenceLinkProperties.class, name = "ExternalReferenceLinkProperties"),
                @JsonSubTypes.Type(value = GovernedByProperties.class, name = "GovernedByProperties"),
                @JsonSubTypes.Type(value = InformationSupplyChainLinkProperties.class, name = "InformationSupplyChainLinkProperties"),
                @JsonSubTypes.Type(value = KnownLocationProperties.class, name = "KnownLocationProperties"),
                @JsonSubTypes.Type(value = LineageRelationshipProperties.class, name = "LineageRelationshipProperties"),
                @JsonSubTypes.Type(value = LinkedMediaProperties.class, name = "LinkedMediaProperties"),
                @JsonSubTypes.Type(value = MonitoredResourceProperties.class, name = "MonitoredResourceProperties"),
                @JsonSubTypes.Type(value = MoreInformationProperties.class, name = "MoreInformationProperties"),
                @JsonSubTypes.Type(value = NestedDesignPatternProperties.class, name = "NestedDesignPatternProperties"),
                @JsonSubTypes.Type(value = NestedLocationProperties.class, name = "NestedLocationProperties"),
                @JsonSubTypes.Type(value = NetworkGatewayLinkProperties.class, name = "NetworkGatewayLinkProperties"),
                @JsonSubTypes.Type(value = NotificationSubscriberProperties.class, name = "NotificationSubscriberProperties"),
                @JsonSubTypes.Type(value = PeerDefinitionProperties.class, name = "PeerDefinitionProperties"),
                @JsonSubTypes.Type(value = PeerProperties.class, name = "PeerProperties"),
                @JsonSubTypes.Type(value = PermittedProcessingProperties.class, name = "PermittedProcessingProperties"),
                @JsonSubTypes.Type(value = ProjectDependencyProperties.class, name = "ProjectDependencyProperties"),
                @JsonSubTypes.Type(value = ProjectHierarchyProperties.class, name = "ProjectHierarchyProperties"),
                @JsonSubTypes.Type(value = ReferenceableFacetProperties.class, name = "ReferenceableFacetProperties"),
                @JsonSubTypes.Type(value = RegulationCertificationTypeProperties.class, name = "RegulationCertificationTypeProperties"),
                @JsonSubTypes.Type(value = RegulatorProperties.class, name = "RegulatorProperties"),
                @JsonSubTypes.Type(value = ReportedAnnotationProperties.class, name = "ReportedAnnotationProperties"),
                @JsonSubTypes.Type(value = ReportDependencyProperties.class, name = "ReportDependencyProperties"),
                @JsonSubTypes.Type(value = ReportOriginatorProperties.class, name = "ReportOriginatorProperties"),
                @JsonSubTypes.Type(value = ReportSubjectProperties.class, name = "ReportSubjectProperties"),
                @JsonSubTypes.Type(value = ResourcePermissionsProperties.class, name = "ResourcePermissionsProperties"),
                @JsonSubTypes.Type(value = RelatedContextEventProperties.class, name = "RelatedContextEventProperties"),
                @JsonSubTypes.Type(value = RelatedDesignPatternProperties.class, name = "RelatedDesignPatternProperties"),
                @JsonSubTypes.Type(value = ReportDependencyProperties.class, name = "ReportDependencyProperties"),
                @JsonSubTypes.Type(value = SchemaAttributeDefinitionProperties.class, name = "SchemaAttributeDefinitionProperties"),
                @JsonSubTypes.Type(value = SchemaTypeDefinitionProperties.class, name = "SchemaTypeDefinitionProperties"),
                @JsonSubTypes.Type(value = SecretsCollectionSecurityListProperties.class, name = "SecretsCollectionSecurityListProperties"),
                @JsonSubTypes.Type(value = SemanticDefinitionProperties.class, name = "SemanticDefinitionProperties"),
                @JsonSubTypes.Type(value = SolutionLinkingWireProperties.class, name = "SolutionLinkingWireProperties"),
                @JsonSubTypes.Type(value = SolutionDesignProperties.class, name = "SolutionDesignProperties"),
                @JsonSubTypes.Type(value = SpecializedDesignPatternProperties.class, name = "SpecializedDesignPatternProperties"),
                @JsonSubTypes.Type(value = SupplementaryPropertiesProperties.class, name = "SupplementaryPropertiesProperties"),
                @JsonSubTypes.Type(value = UserAccountProperties.class, name = "UserAccountProperties"),
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
        super.typeName = OpenMetadataType.LABELED_RELATIONSHIP.typeName;
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