/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.APIEndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FolderHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.LinkedFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.NestedFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.CapabilityAssetUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.DeployedOnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionRequesterProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.RegisteredIntegrationConnectorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.SupportedGovernanceServiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ImpactedResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportOriginatorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportSubjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CrowdSourcingContributionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.DependentContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.RelatedContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementActorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementItemProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.ContractLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSubscriberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.CitedDocumentLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.MediaReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.TargetForGovernanceActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.AssociatedSecurityGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.*;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipBeanProperties provides the base class for relationship beans.
 * This provides extended properties to allow the existing types to be extended.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AcceptedAnswerProperties.class, name = "AcceptedAnswerProperties"),
                @JsonSubTypes.Type(value = ActionRequesterProperties.class, name = "ActionRequesterProperties"),
                @JsonSubTypes.Type(value = ActionsProperties.class, name = "ActionsProperties"),
                @JsonSubTypes.Type(value = ActionTargetProperties.class, name = "ActionTargetProperties"),
                @JsonSubTypes.Type(value = AgreementActorProperties.class, name = "AgreementActorProperties"),
                @JsonSubTypes.Type(value = AgreementItemProperties.class, name = "AgreementItemProperties"),
                @JsonSubTypes.Type(value = AnnotationExtensionProperties.class, name = "AnnotationExtensionProperties"),
                @JsonSubTypes.Type(value = APIEndpointProperties.class, name = "APIEndpointProperties"),
                @JsonSubTypes.Type(value = AssetConnectionProperties.class, name = "AssetConnectionProperties"),
                @JsonSubTypes.Type(value = AssetSchemaTypeProperties.class, name = "AssetSchemaTypeProperties"),
                @JsonSubTypes.Type(value = AssignmentScopeProperties.class, name = "AssignmentScopeProperties"),
                @JsonSubTypes.Type(value = AssociatedAnnotationProperties.class, name = "AssociatedAnnotationProperties"),
                @JsonSubTypes.Type(value = AssociatedSecurityGroupProperties.class, name = "AssociatedSecurityGroupProperties"),
                @JsonSubTypes.Type(value = AttachedCommentProperties.class, name = "AttachedCommentProperties"),
                @JsonSubTypes.Type(value = AttachedLikeProperties.class, name = "AttachedLikeProperties"),
                @JsonSubTypes.Type(value = AttachedRatingProperties.class, name = "AttachedRatingProperties"),
                @JsonSubTypes.Type(value = AttachedTagProperties.class, name = "AttachedTagProperties"),
                @JsonSubTypes.Type(value = CapabilityAssetUseProperties.class, name = "CapabilityAssetUseProperties"),
                @JsonSubTypes.Type(value = CatalogTargetProperties.class, name = "CatalogTargetProperties"),
                @JsonSubTypes.Type(value = CertificationProperties.class, name = "CertificationProperties"),
                @JsonSubTypes.Type(value = CitedDocumentLinkProperties.class, name = "CitedDocumentLinkProperties"),
                @JsonSubTypes.Type(value = CollectionMembershipProperties.class, name = "CollectionMembershipProperties"),
                @JsonSubTypes.Type(value = ConnectionConnectorTypeProperties.class, name = "ConnectionConnectorTypeProperties"),
                @JsonSubTypes.Type(value = ConnectToEndpointProperties.class, name = "ConnectToEndpointProperties"),
                @JsonSubTypes.Type(value = ConsistentValidValuesProperties.class, name = "ConsistentValidValuesProperties"),
                @JsonSubTypes.Type(value = ContactThroughProperties.class, name = "ContactThroughProperties"),
                @JsonSubTypes.Type(value = ContractLinkProperties.class, name = "ContractLinkProperties"),
                @JsonSubTypes.Type(value = ContributionProperties.class, name = "ContributionProperties"),
                @JsonSubTypes.Type(value = CrowdSourcingContributionProperties.class, name = "CrowdSourcingContributionProperties"),
                @JsonSubTypes.Type(value = DataClassAssignmentProperties.class, name = "DataClassAssignmentProperties"),
                @JsonSubTypes.Type(value = DataClassCompositionProperties.class, name = "DataClassCompositionProperties"),
                @JsonSubTypes.Type(value = DataClassDefinitionProperties.class, name = "DataClassDefinitionProperties"),
                @JsonSubTypes.Type(value = DataClassHierarchyProperties.class, name = "DataClassHierarchyProperties"),
                @JsonSubTypes.Type(value = DataClassMatchProperties.class, name = "DataClassMatchProperties"),
                @JsonSubTypes.Type(value = DataSetContentProperties.class, name = "DataSetContentProperties"),
                @JsonSubTypes.Type(value = DataStructureDefinitionProperties.class, name = "DataStructureDefinitionProperties"),
                @JsonSubTypes.Type(value = DependentContextEventProperties.class, name = "DependentContextEventProperties"),
                @JsonSubTypes.Type(value = DeployedOnProperties.class, name = "DeployedOnProperties"),
                @JsonSubTypes.Type(value = DerivedSchemaTypeQueryTargetProperties.class, name = "DerivedSchemaTypeQueryTargetProperties"),
                @JsonSubTypes.Type(value = DigitalSubscriberProperties.class, name = "DigitalSubscriberProperties"),
                @JsonSubTypes.Type(value = DiscoveredSchemaTypeProperties.class, name = "DiscoveredSchemaTypeProperties"),
                @JsonSubTypes.Type(value = DuplicateProperties.class, name = "DuplicateProperties"),
                @JsonSubTypes.Type(value = EmbeddedConnectionProperties.class, name = "EmbeddedConnectionProperties"),
                @JsonSubTypes.Type(value = ExternalIdLinkProperties.class, name = "ExternalIdLinkProperties"),
                @JsonSubTypes.Type(value = ExternalIdScopeProperties.class, name = "ExternalIdScopeProperties"),
                @JsonSubTypes.Type(value = FeedbackProperties.class, name = "FeedbackProperties"),
                @JsonSubTypes.Type(value = FolderHierarchyProperties.class, name = "FolderHierarchyProperties"),
                @JsonSubTypes.Type(value = ForeignKeyProperties.class, name = "ForeignKeyProperties"),
                @JsonSubTypes.Type(value = GlossaryTermRelationshipProperties.class, name = "GlossaryTermRelationshipProperties"),
                @JsonSubTypes.Type(value = GovernanceResultsProperties.class, name = "GovernanceResultsProperties"),
                @JsonSubTypes.Type(value = GraphEdgeLinkProperties.class, name = "GraphEdgeLinkProperties"),
                @JsonSubTypes.Type(value = ImpactedResourceProperties.class, name = "ImpactedResourceProperties"),
                @JsonSubTypes.Type(value = ITInfrastructureProfileProperties.class, name = "ITInfrastructureProfileProperties"),
                @JsonSubTypes.Type(value = ITProfileRoleAppointmentProperties.class, name = "ITProfileRoleAppointmentProperties"),
                @JsonSubTypes.Type(value = LabeledRelationshipProperties.class, name = "LabeledRelationshipProperties"),
                @JsonSubTypes.Type(value = LicenseProperties.class, name = "LicenseProperties"),
                @JsonSubTypes.Type(value = LinkedExternalSchemaTypeProperties.class, name = "LinkedExternalSchemaTypeProperties"),
                @JsonSubTypes.Type(value = LinkedFileProperties.class, name = "LinkedFileProperties"),
                @JsonSubTypes.Type(value = MapFromElementTypeProperties.class, name = "MapFromElementTypeProperties"),
                @JsonSubTypes.Type(value = MapToElementTypeProperties.class, name = "MapToElementTypeProperties"),
                @JsonSubTypes.Type(value = MediaReferenceProperties.class, name = "MediaReferenceProperties"),
                @JsonSubTypes.Type(value = NestedFileProperties.class, name = "NestedFileProperties"),
                @JsonSubTypes.Type(value = PartOfRelationshipProperties.class, name = "PartOfRelationshipProperties"),
                @JsonSubTypes.Type(value = PeerProperties.class, name = "PeerProperties"),
                @JsonSubTypes.Type(value = PortSchemaProperties.class, name = "PortSchemaProperties"),
                @JsonSubTypes.Type(value = PersonRoleAppointmentProperties.class, name = "PersonRoleAppointmentProperties"),
                @JsonSubTypes.Type(value = ProcessHierarchyProperties.class, name = "ProcessHierarchyProperties"),
                @JsonSubTypes.Type(value = ProfileIdentityProperties.class, name = "ProfileIdentityProperties"),
                @JsonSubTypes.Type(value = ReferenceValueAssignmentProperties.class, name = "ReferenceValueAssignmentProperties"),
                @JsonSubTypes.Type(value = RegisteredIntegrationConnectorProperties.class, name = "RegisteredIntegrationConnectorProperties"),
                @JsonSubTypes.Type(value = RegulationCertificationTypeProperties.class, name = "RegulationCertificationTypeProperties"),
                @JsonSubTypes.Type(value = RelatedContextEventProperties.class, name = "RelatedContextEventProperties"),
                @JsonSubTypes.Type(value = ReportedAnnotationProperties.class, name = "ReportedAnnotationProperties"),
                @JsonSubTypes.Type(value = ReportOriginatorProperties.class, name = "ReportOriginatorProperties"),
                @JsonSubTypes.Type(value = ReportSubjectProperties.class, name = "ReportSubjectProperties"),
                @JsonSubTypes.Type(value = RequestForActionTargetProperties.class, name = "RequestForActionTargetProperties"),
                @JsonSubTypes.Type(value = ResourceListProperties.class, name = "ResourceListProperties"),
                @JsonSubTypes.Type(value = ResourceProfileDataProperties.class, name = "ResourceProfileDataProperties"),
                @JsonSubTypes.Type(value = RoledRelationshipProperties.class, name = "RoledRelationshipProperties"),
                @JsonSubTypes.Type(value = SchemaAttributeRelationshipProperties.class, name = "SchemaAttributeRelationshipProperties"),
                @JsonSubTypes.Type(value = SchemaTypeOptionProperties.class, name = "SchemaTypeOptionProperties"),
                @JsonSubTypes.Type(value = ScopedByProperties.class, name = "ScopedByProperties"),
                @JsonSubTypes.Type(value = SearchKeywordLinkProperties.class, name = "SearchKeywordLinkProperties"),
                @JsonSubTypes.Type(value = SemanticAssignmentProperties.class, name = "SemanticAssignmentProperties"),
                @JsonSubTypes.Type(value = SemanticDefinitionProperties.class, name = "SemanticDefinitionProperties"),
                @JsonSubTypes.Type(value = ServerEndpointProperties.class, name = "ServerEndpointProperties"),
                @JsonSubTypes.Type(value = SolutionPortSchemaProperties.class, name = "SolutionPortSchemaProperties"),
                @JsonSubTypes.Type(value = SpecificationPropertyAssignmentProperties.class, name = "SpecificationPropertyAssignmentProperties"),
                @JsonSubTypes.Type(value = SubjectAreaHierarchyProperties.class, name = "SubjectAreaHierarchyProperties"),
                @JsonSubTypes.Type(value = SupplementaryPropertiesProperties.class, name = "SupplementaryPropertiesProperties"),
                @JsonSubTypes.Type(value = SupportedGovernanceServiceProperties.class, name = "SupportedGovernanceServiceProperties"),
                @JsonSubTypes.Type(value = SupportingDefinitionProperties.class, name = "SupportingDefinitionProperties"),
                @JsonSubTypes.Type(value = TargetForGovernanceActionProperties.class, name = "TargetForGovernanceActionProperties"),
                @JsonSubTypes.Type(value = TeamStructureProperties.class, name = "TeamStructureProperties"),
                @JsonSubTypes.Type(value = TeamRoleAppointmentProperties.class, name = "TeamRoleAppointmentProperties"),
                @JsonSubTypes.Type(value = ValidValuesAssignmentProperties.class, name = "ValidValuesAssignmentProperties"),
                @JsonSubTypes.Type(value = ValidValueAssociationProperties.class, name = "ValidValueAssociationProperties"),
                @JsonSubTypes.Type(value = ValidValueMemberProperties.class, name = "ValidValueMemberProperties"),
                @JsonSubTypes.Type(value = ValidValuesImplementationProperties.class, name = "ValidValuesImplementationProperties"),
                @JsonSubTypes.Type(value = ValidValuesMappingProperties.class, name = "ValidValuesMappingProperties"),
                @JsonSubTypes.Type(value = ZoneHierarchyProperties.class, name = "ZoneHierarchyProperties"),

        })
public class RelationshipBeanProperties extends RelationshipProperties
{
    private String               typeName             = null;
    private Map<String, Object>  extendedProperties   = null;

    /**
     * Default constructor
     */
    public RelationshipBeanProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public RelationshipBeanProperties(RelationshipBeanProperties template)
    {
        super(template);

        if (template != null)
        {
            typeName             = template.getTypeName();
            extendedProperties   = template.getExtendedProperties();
        }
    }


    /**
     * Return the name of the open metadata type for this metadata element.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the name of the open metadata type for this element.
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
        return "RelationshipBeanProperties{" +
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
        RelationshipBeanProperties that = (RelationshipBeanProperties) objectToCompare;
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