/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.fvt.opentypes.common.Line;

import org.odpi.openmetadata.fvt.opentypes.relationships.UsedInContext.UsedInContext;
import org.odpi.openmetadata.fvt.opentypes.relationships.UsedInContext.UsedInContextMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedComment.AttachedComment;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedComment.AttachedCommentMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ContactThrough.ContactThrough;
import org.odpi.openmetadata.fvt.opentypes.relationships.ContactThrough.ContactThroughMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.License.License;
import org.odpi.openmetadata.fvt.opentypes.relationships.License.LicenseMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ZoneGovernance.ZoneGovernance;
import org.odpi.openmetadata.fvt.opentypes.relationships.ZoneGovernance.ZoneGovernanceMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProcessPort.ProcessPort;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProcessPort.ProcessPortMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataFieldAnalysis.DataFieldAnalysis;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataFieldAnalysis.DataFieldAnalysisMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveryServiceImplementation.DiscoveryServiceImplementation;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveryServiceImplementation.DiscoveryServiceImplementationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.SoftwareServerPlatformDeployment.SoftwareServerPlatformDeployment;
import org.odpi.openmetadata.fvt.opentypes.relationships.SoftwareServerPlatformDeployment.SoftwareServerPlatformDeploymentMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.FolderHierarchy.FolderHierarchy;
import org.odpi.openmetadata.fvt.opentypes.relationships.FolderHierarchy.FolderHierarchyMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.HostClusterMember.HostClusterMember;
import org.odpi.openmetadata.fvt.opentypes.relationships.HostClusterMember.HostClusterMemberMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AnnotationReviewLink.AnnotationReviewLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.AnnotationReviewLink.AnnotationReviewLinkMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.LibraryCategoryReference.LibraryCategoryReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.LibraryCategoryReference.LibraryCategoryReferenceMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectCharterLink.ProjectCharterLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectCharterLink.ProjectCharterLinkMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProfileIdentity.ProfileIdentity;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProfileIdentity.ProfileIdentityMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ToDoSource.ToDoSource;
import org.odpi.openmetadata.fvt.opentypes.relationships.ToDoSource.ToDoSourceMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassComposition.DataClassComposition;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassComposition.DataClassCompositionMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.Translation.Translation;
import org.odpi.openmetadata.fvt.opentypes.relationships.Translation.TranslationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaLinkToType.SchemaLinkToType;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaLinkToType.SchemaLinkToTypeMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredNestedDataField.DiscoveredNestedDataField;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredNestedDataField.DiscoveredNestedDataFieldMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.TeamMembership.TeamMembership;
import org.odpi.openmetadata.fvt.opentypes.relationships.TeamMembership.TeamMembershipMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.Actions.Actions;
import org.odpi.openmetadata.fvt.opentypes.relationships.Actions.ActionsMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.PersonalContribution.PersonalContribution;
import org.odpi.openmetadata.fvt.opentypes.relationships.PersonalContribution.PersonalContributionMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataContentForDataSet.DataContentForDataSet;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataContentForDataSet.DataContentForDataSetMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.TeamLeadership.TeamLeadership;
import org.odpi.openmetadata.fvt.opentypes.relationships.TeamLeadership.TeamLeadershipMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.BusinessCapabilityControls.BusinessCapabilityControls;
import org.odpi.openmetadata.fvt.opentypes.relationships.BusinessCapabilityControls.BusinessCapabilityControlsMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ServerEndpoint.ServerEndpoint;
import org.odpi.openmetadata.fvt.opentypes.relationships.ServerEndpoint.ServerEndpointMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveryInvocationReport.DiscoveryInvocationReport;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveryInvocationReport.DiscoveryInvocationReportMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdLink.ExternalIdLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdLink.ExternalIdLinkMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceList;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceListMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.CategoryHierarchyLink.CategoryHierarchyLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.CategoryHierarchyLink.CategoryHierarchyLinkMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.PreferredTerm.PreferredTerm;
import org.odpi.openmetadata.fvt.opentypes.relationships.PreferredTerm.PreferredTermMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectHierarchy.ProjectHierarchy;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectHierarchy.ProjectHierarchyMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DeployedVirtualContainer.DeployedVirtualContainer;
import org.odpi.openmetadata.fvt.opentypes.relationships.DeployedVirtualContainer.DeployedVirtualContainerMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AnnotationExtension.AnnotationExtension;
import org.odpi.openmetadata.fvt.opentypes.relationships.AnnotationExtension.AnnotationExtensionMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassHierarchy.DataClassHierarchy;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassHierarchy.DataClassHierarchyMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceResults.GovernanceResults;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceResults.GovernanceResultsMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.APIResponse.APIResponse;
import org.odpi.openmetadata.fvt.opentypes.relationships.APIResponse.APIResponseMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaAttributeDefinition.SchemaAttributeDefinition;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaAttributeDefinition.SchemaAttributeDefinitionMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.HostNetwork.HostNetwork;
import org.odpi.openmetadata.fvt.opentypes.relationships.HostNetwork.HostNetworkMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttributeForSchema.AttributeForSchema;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttributeForSchema.AttributeForSchemaMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AssetLocation.AssetLocation;
import org.odpi.openmetadata.fvt.opentypes.relationships.AssetLocation.AssetLocationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ParentPort.ParentPort;
import org.odpi.openmetadata.fvt.opentypes.relationships.ParentPort.ParentPortMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.SoftwareServerSupportedCapability.SoftwareServerSupportedCapability;
import org.odpi.openmetadata.fvt.opentypes.relationships.SoftwareServerSupportedCapability.SoftwareServerSupportedCapabilityMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredAnnotation.DiscoveredAnnotation;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredAnnotation.DiscoveredAnnotationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaAttributeType.SchemaAttributeType;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaAttributeType.SchemaAttributeTypeMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceImplementation.GovernanceImplementation;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceImplementation.GovernanceImplementationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.LinkedMedia.LinkedMedia;
import org.odpi.openmetadata.fvt.opentypes.relationships.LinkedMedia.LinkedMediaMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.MapToElementType.MapToElementType;
import org.odpi.openmetadata.fvt.opentypes.relationships.MapToElementType.MapToElementTypeMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternallySourcedGlossary.ExternallySourcedGlossary;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternallySourcedGlossary.ExternallySourcedGlossaryMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalReferenceLink.ExternalReferenceLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalReferenceLink.ExternalReferenceLinkMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.MediaReference.MediaReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.MediaReference.MediaReferenceMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.CommunityMembership.CommunityMembership;
import org.odpi.openmetadata.fvt.opentypes.relationships.CommunityMembership.CommunityMembershipMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdScope.ExternalIdScope;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdScope.ExternalIdScopeMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ValidValue.ValidValue;
import org.odpi.openmetadata.fvt.opentypes.relationships.ValidValue.ValidValueMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AcceptedAnswer.AcceptedAnswer;
import org.odpi.openmetadata.fvt.opentypes.relationships.AcceptedAnswer.AcceptedAnswerMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceDefinitionMetric.GovernanceDefinitionMetric;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceDefinitionMetric.GovernanceDefinitionMetricMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.TeamStructure.TeamStructure;
import org.odpi.openmetadata.fvt.opentypes.relationships.TeamStructure.TeamStructureMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaQueryImplementation.SchemaQueryImplementation;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaQueryImplementation.SchemaQueryImplementationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AssetSchemaType.AssetSchemaType;
import org.odpi.openmetadata.fvt.opentypes.relationships.AssetSchemaType.AssetSchemaTypeMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectDependency.ProjectDependency;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectDependency.ProjectDependencyMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.TopicSubscribers.TopicSubscribers;
import org.odpi.openmetadata.fvt.opentypes.relationships.TopicSubscribers.TopicSubscribersMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReplacementTerm.ReplacementTerm;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReplacementTerm.ReplacementTermMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.OrganizationalCapability.OrganizationalCapability;
import org.odpi.openmetadata.fvt.opentypes.relationships.OrganizationalCapability.OrganizationalCapabilityMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.MetadataCohortPeer.MetadataCohortPeer;
import org.odpi.openmetadata.fvt.opentypes.relationships.MetadataCohortPeer.MetadataCohortPeerMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.CollectionMembership.CollectionMembership;
import org.odpi.openmetadata.fvt.opentypes.relationships.CollectionMembership.CollectionMembershipMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedNoteLogEntry.AttachedNoteLogEntry;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedNoteLogEntry.AttachedNoteLogEntryMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AssetServerUse.AssetServerUse;
import org.odpi.openmetadata.fvt.opentypes.relationships.AssetServerUse.AssetServerUseMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.RuntimeForProcess.RuntimeForProcess;
import org.odpi.openmetadata.fvt.opentypes.relationships.RuntimeForProcess.RuntimeForProcessMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.SupportedDiscoveryService.SupportedDiscoveryService;
import org.odpi.openmetadata.fvt.opentypes.relationships.SupportedDiscoveryService.SupportedDiscoveryServiceMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.NestedLocation.NestedLocation;
import org.odpi.openmetadata.fvt.opentypes.relationships.NestedLocation.NestedLocationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.LinkedFile.LinkedFile;
import org.odpi.openmetadata.fvt.opentypes.relationships.LinkedFile.LinkedFileMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermCategorization.TermCategorization;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermCategorization.TermCategorizationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.PortWire.PortWire;
import org.odpi.openmetadata.fvt.opentypes.relationships.PortWire.PortWireMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.APIEndpoint.APIEndpoint;
import org.odpi.openmetadata.fvt.opentypes.relationships.APIEndpoint.APIEndpointMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectManagement.ProjectManagement;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectManagement.ProjectManagementMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassDefinition.DataClassDefinition;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassDefinition.DataClassDefinitionMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ConnectionEndpoint.ConnectionEndpoint;
import org.odpi.openmetadata.fvt.opentypes.relationships.ConnectionEndpoint.ConnectionEndpointMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.CategoryAnchor.CategoryAnchor;
import org.odpi.openmetadata.fvt.opentypes.relationships.CategoryAnchor.CategoryAnchorMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectTeam.ProjectTeam;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectTeam.ProjectTeamMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.APIOperations.APIOperations;
import org.odpi.openmetadata.fvt.opentypes.relationships.APIOperations.APIOperationsMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotation;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernancePolicyLink.GovernancePolicyLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernancePolicyLink.GovernancePolicyLinkMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResponsibilityStaffContact.ResponsibilityStaffContact;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResponsibilityStaffContact.ResponsibilityStaffContactMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredDataField.DiscoveredDataField;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredDataField.DiscoveredDataFieldMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.PersonRoleAppointment.PersonRoleAppointment;
import org.odpi.openmetadata.fvt.opentypes.relationships.PersonRoleAppointment.PersonRoleAppointmentMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AssetDiscoveryService.AssetDiscoveryService;
import org.odpi.openmetadata.fvt.opentypes.relationships.AssetDiscoveryService.AssetDiscoveryServiceMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.Peer.Peer;
import org.odpi.openmetadata.fvt.opentypes.relationships.Peer.PeerMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ActionAssignment.ActionAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.ActionAssignment.ActionAssignmentMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationshipMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.MapFromElementType.MapFromElementType;
import org.odpi.openmetadata.fvt.opentypes.relationships.MapFromElementType.MapFromElementTypeMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaTypeDefinition.SchemaTypeDefinition;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaTypeDefinition.SchemaTypeDefinitionMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AssetWire.AssetWire;
import org.odpi.openmetadata.fvt.opentypes.relationships.AssetWire.AssetWireMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.Certification.Certification;
import org.odpi.openmetadata.fvt.opentypes.relationships.Certification.CertificationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernancePost.GovernancePost;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernancePost.GovernancePostMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.NetworkGatewayLink.NetworkGatewayLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.NetworkGatewayLink.NetworkGatewayLinkMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceResponsibilityAssignment.GovernanceResponsibilityAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceResponsibilityAssignment.GovernanceResponsibilityAssignmentMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ConnectionConnectorType.ConnectionConnectorType;
import org.odpi.openmetadata.fvt.opentypes.relationships.ConnectionConnectorType.ConnectionConnectorTypeMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.Synonym.Synonym;
import org.odpi.openmetadata.fvt.opentypes.relationships.Synonym.SynonymMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.Antonym.Antonym;
import org.odpi.openmetadata.fvt.opentypes.relationships.Antonym.AntonymMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.GraphEdgeLink.GraphEdgeLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.GraphEdgeLink.GraphEdgeLinkMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectScope.ProjectScope;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectScope.ProjectScopeMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ISARelationship.ISARelationship;
import org.odpi.openmetadata.fvt.opentypes.relationships.ISARelationship.ISARelationshipMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermISATypeOFRelationship.TermISATypeOFRelationshipMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.Meetings.Meetings;
import org.odpi.openmetadata.fvt.opentypes.relationships.Meetings.MeetingsMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.EmbeddedConnection.EmbeddedConnection;
import org.odpi.openmetadata.fvt.opentypes.relationships.EmbeddedConnection.EmbeddedConnectionMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceControlLink.GovernanceControlLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceControlLink.GovernanceControlLinkMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedRating.AttachedRating;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedRating.AttachedRatingMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassAssignment.DataClassAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassAssignment.DataClassAssignmentMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedNoteLog.AttachedNoteLog;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedNoteLog.AttachedNoteLogMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ConnectionToAsset.ConnectionToAsset;
import org.odpi.openmetadata.fvt.opentypes.relationships.ConnectionToAsset.ConnectionToAssetMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.RegulationCertificationType.RegulationCertificationType;
import org.odpi.openmetadata.fvt.opentypes.relationships.RegulationCertificationType.RegulationCertificationTypeMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.LinkedType.LinkedType;
import org.odpi.openmetadata.fvt.opentypes.relationships.LinkedType.LinkedTypeMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.CrowdSourcingContribution.CrowdSourcingContribution;
import org.odpi.openmetadata.fvt.opentypes.relationships.CrowdSourcingContribution.CrowdSourcingContributionMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProcessHierarchy.ProcessHierarchy;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProcessHierarchy.ProcessHierarchyMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceRuleImplementation.GovernanceRuleImplementation;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceRuleImplementation.GovernanceRuleImplementationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AdjacentLocation.AdjacentLocation;
import org.odpi.openmetadata.fvt.opentypes.relationships.AdjacentLocation.AdjacentLocationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.SemanticAssignment.SemanticAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.SemanticAssignment.SemanticAssignmentMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.HostLocation.HostLocation;
import org.odpi.openmetadata.fvt.opentypes.relationships.HostLocation.HostLocationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelatedTerm.RelatedTerm;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelatedTerm.RelatedTermMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaTypeImplementation.SchemaTypeImplementation;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaTypeImplementation.SchemaTypeImplementationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.APIHeader.APIHeader;
import org.odpi.openmetadata.fvt.opentypes.relationships.APIHeader.APIHeaderMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.LibraryTermReference.LibraryTermReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.LibraryTermReference.LibraryTermReferenceMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedTag.AttachedTag;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedTag.AttachedTagMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermHASARelationship.TermHASARelationship;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermHASARelationship.TermHASARelationshipMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceProcessImplementation.GovernanceProcessImplementation;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceProcessImplementation.GovernanceProcessImplementationMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.GroupedMedia.GroupedMedia;
import org.odpi.openmetadata.fvt.opentypes.relationships.GroupedMedia.GroupedMediaMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceResponse.GovernanceResponse;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceResponse.GovernanceResponseMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.NestedFile.NestedFile;
import org.odpi.openmetadata.fvt.opentypes.relationships.NestedFile.NestedFileMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.PortInterface.PortInterface;
import org.odpi.openmetadata.fvt.opentypes.relationships.PortInterface.PortInterfaceMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.HostOperatingPlatform.HostOperatingPlatform;
import org.odpi.openmetadata.fvt.opentypes.relationships.HostOperatingPlatform.HostOperatingPlatformMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermAnchor.TermAnchor;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermAnchor.TermAnchorMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ForeignKey.ForeignKey;
import org.odpi.openmetadata.fvt.opentypes.relationships.ForeignKey.ForeignKeyMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataProfileLogFile.DataProfileLogFile;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataProfileLogFile.DataProfileLogFileMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveryEngineReport.DiscoveryEngineReport;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveryEngineReport.DiscoveryEngineReportMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.APIRequest.APIRequest;
import org.odpi.openmetadata.fvt.opentypes.relationships.APIRequest.APIRequestMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.SoftwareServerDeployment.SoftwareServerDeployment;
import org.odpi.openmetadata.fvt.opentypes.relationships.SoftwareServerDeployment.SoftwareServerDeploymentMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceRoleAssignment.GovernanceRoleAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceRoleAssignment.GovernanceRoleAssignmentMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.CohortMemberMetadataCollection.CohortMemberMetadataCollection;
import org.odpi.openmetadata.fvt.opentypes.relationships.CohortMemberMetadataCollection.CohortMemberMetadataCollectionMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReferenceableFacet.ReferenceableFacet;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReferenceableFacet.ReferenceableFacetMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AssetDiscoveryReport.AssetDiscoveryReport;
import org.odpi.openmetadata.fvt.opentypes.relationships.AssetDiscoveryReport.AssetDiscoveryReportMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedLike.AttachedLike;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedLike.AttachedLikeMapper;
import org.odpi.openmetadata.fvt.opentypes.relationships.NoteLogAuthorship.NoteLogAuthorship;
import org.odpi.openmetadata.fvt.opentypes.relationships.NoteLogAuthorship.NoteLogAuthorshipMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * OMRS Relationship to Lines converter
 */
public class OMRSRelationshipToLines {
    /**
     * convert omrs Relationships into Lines
     * @param omrsRelationships relationships to convert.
     * @return a set of Lines which can be used by the Subject Area OMAS API
     */
    public static Set<Line> convert(List<Relationship> omrsRelationships) {
        Set<Line> lines = new HashSet<>();
        for (Relationship omrsRelationship:omrsRelationships){
            String name = omrsRelationship.getType().getTypeDefName();
            if (name.equals("UsedInContext")) {
                UsedInContext usedInContext= UsedInContextMapper.mapOmrsRelationshipToUsedInContext(omrsRelationship);
                lines.add(usedInContext);
            }
            if (name.equals("AttachedComment")) {
                AttachedComment attachedComment= AttachedCommentMapper.mapOmrsRelationshipToAttachedComment(omrsRelationship);
                lines.add(attachedComment);
            }
            if (name.equals("ContactThrough")) {
                ContactThrough contactThrough= ContactThroughMapper.mapOmrsRelationshipToContactThrough(omrsRelationship);
                lines.add(contactThrough);
            }
            if (name.equals("License")) {
                License license= LicenseMapper.mapOmrsRelationshipToLicense(omrsRelationship);
                lines.add(license);
            }
            if (name.equals("ZoneGovernance")) {
                ZoneGovernance zoneGovernance= ZoneGovernanceMapper.mapOmrsRelationshipToZoneGovernance(omrsRelationship);
                lines.add(zoneGovernance);
            }
            if (name.equals("ProcessPort")) {
                ProcessPort processPort= ProcessPortMapper.mapOmrsRelationshipToProcessPort(omrsRelationship);
                lines.add(processPort);
            }
            if (name.equals("DataFieldAnalysis")) {
                DataFieldAnalysis dataFieldAnalysis= DataFieldAnalysisMapper.mapOmrsRelationshipToDataFieldAnalysis(omrsRelationship);
                lines.add(dataFieldAnalysis);
            }
            if (name.equals("DiscoveryServiceImplementation")) {
                DiscoveryServiceImplementation discoveryServiceImplementation= DiscoveryServiceImplementationMapper.mapOmrsRelationshipToDiscoveryServiceImplementation(omrsRelationship);
                lines.add(discoveryServiceImplementation);
            }
            if (name.equals("SoftwareServerPlatformDeployment")) {
                SoftwareServerPlatformDeployment softwareServerPlatformDeployment= SoftwareServerPlatformDeploymentMapper.mapOmrsRelationshipToSoftwareServerPlatformDeployment(omrsRelationship);
                lines.add(softwareServerPlatformDeployment);
            }
            if (name.equals("FolderHierarchy")) {
                FolderHierarchy folderHierarchy= FolderHierarchyMapper.mapOmrsRelationshipToFolderHierarchy(omrsRelationship);
                lines.add(folderHierarchy);
            }
            if (name.equals("HostClusterMember")) {
                HostClusterMember hostClusterMember= HostClusterMemberMapper.mapOmrsRelationshipToHostClusterMember(omrsRelationship);
                lines.add(hostClusterMember);
            }
            if (name.equals("AnnotationReviewLink")) {
                AnnotationReviewLink annotationReviewLink= AnnotationReviewLinkMapper.mapOmrsRelationshipToAnnotationReviewLink(omrsRelationship);
                lines.add(annotationReviewLink);
            }
            if (name.equals("LibraryCategoryReference")) {
                LibraryCategoryReference libraryCategoryReference= LibraryCategoryReferenceMapper.mapOmrsRelationshipToLibraryCategoryReference(omrsRelationship);
                lines.add(libraryCategoryReference);
            }
            if (name.equals("ProjectCharterLink")) {
                ProjectCharterLink projectCharterLink= ProjectCharterLinkMapper.mapOmrsRelationshipToProjectCharterLink(omrsRelationship);
                lines.add(projectCharterLink);
            }
            if (name.equals("ProfileIdentity")) {
                ProfileIdentity profileIdentity= ProfileIdentityMapper.mapOmrsRelationshipToProfileIdentity(omrsRelationship);
                lines.add(profileIdentity);
            }
            if (name.equals("ToDoSource")) {
                ToDoSource toDoSource= ToDoSourceMapper.mapOmrsRelationshipToToDoSource(omrsRelationship);
                lines.add(toDoSource);
            }
            if (name.equals("DataClassComposition")) {
                DataClassComposition dataClassComposition= DataClassCompositionMapper.mapOmrsRelationshipToDataClassComposition(omrsRelationship);
                lines.add(dataClassComposition);
            }
            if (name.equals("Translation")) {
                Translation translation= TranslationMapper.mapOmrsRelationshipToTranslation(omrsRelationship);
                lines.add(translation);
            }
            if (name.equals("SchemaLinkToType")) {
                SchemaLinkToType schemaLinkToType= SchemaLinkToTypeMapper.mapOmrsRelationshipToSchemaLinkToType(omrsRelationship);
                lines.add(schemaLinkToType);
            }
            if (name.equals("DiscoveredNestedDataField")) {
                DiscoveredNestedDataField discoveredNestedDataField= DiscoveredNestedDataFieldMapper.mapOmrsRelationshipToDiscoveredNestedDataField(omrsRelationship);
                lines.add(discoveredNestedDataField);
            }
            if (name.equals("TeamMembership")) {
                TeamMembership teamMembership= TeamMembershipMapper.mapOmrsRelationshipToTeamMembership(omrsRelationship);
                lines.add(teamMembership);
            }
            if (name.equals("Actions")) {
                Actions actions= ActionsMapper.mapOmrsRelationshipToActions(omrsRelationship);
                lines.add(actions);
            }
            if (name.equals("PersonalContribution")) {
                PersonalContribution personalContribution= PersonalContributionMapper.mapOmrsRelationshipToPersonalContribution(omrsRelationship);
                lines.add(personalContribution);
            }
            if (name.equals("DataContentForDataSet")) {
                DataContentForDataSet dataContentForDataSet= DataContentForDataSetMapper.mapOmrsRelationshipToDataContentForDataSet(omrsRelationship);
                lines.add(dataContentForDataSet);
            }
            if (name.equals("TeamLeadership")) {
                TeamLeadership teamLeadership= TeamLeadershipMapper.mapOmrsRelationshipToTeamLeadership(omrsRelationship);
                lines.add(teamLeadership);
            }
            if (name.equals("BusinessCapabilityControls")) {
                BusinessCapabilityControls businessCapabilityControls= BusinessCapabilityControlsMapper.mapOmrsRelationshipToBusinessCapabilityControls(omrsRelationship);
                lines.add(businessCapabilityControls);
            }
            if (name.equals("ServerEndpoint")) {
                ServerEndpoint serverEndpoint= ServerEndpointMapper.mapOmrsRelationshipToServerEndpoint(omrsRelationship);
                lines.add(serverEndpoint);
            }
            if (name.equals("DiscoveryInvocationReport")) {
                DiscoveryInvocationReport discoveryInvocationReport= DiscoveryInvocationReportMapper.mapOmrsRelationshipToDiscoveryInvocationReport(omrsRelationship);
                lines.add(discoveryInvocationReport);
            }
            if (name.equals("ExternalIdLink")) {
                ExternalIdLink externalIdLink= ExternalIdLinkMapper.mapOmrsRelationshipToExternalIdLink(omrsRelationship);
                lines.add(externalIdLink);
            }
            if (name.equals("ResourceList")) {
                ResourceList resourceList= ResourceListMapper.mapOmrsRelationshipToResourceList(omrsRelationship);
                lines.add(resourceList);
            }
            if (name.equals("CategoryHierarchyLink")) {
                CategoryHierarchyLink categoryHierarchyLink= CategoryHierarchyLinkMapper.mapOmrsRelationshipToCategoryHierarchyLink(omrsRelationship);
                lines.add(categoryHierarchyLink);
            }
            if (name.equals("PreferredTerm")) {
                PreferredTerm preferredTerm= PreferredTermMapper.mapOmrsRelationshipToPreferredTerm(omrsRelationship);
                lines.add(preferredTerm);
            }
            if (name.equals("ProjectHierarchy")) {
                ProjectHierarchy projectHierarchy= ProjectHierarchyMapper.mapOmrsRelationshipToProjectHierarchy(omrsRelationship);
                lines.add(projectHierarchy);
            }
            if (name.equals("DeployedVirtualContainer")) {
                DeployedVirtualContainer deployedVirtualContainer= DeployedVirtualContainerMapper.mapOmrsRelationshipToDeployedVirtualContainer(omrsRelationship);
                lines.add(deployedVirtualContainer);
            }
            if (name.equals("AnnotationExtension")) {
                AnnotationExtension annotationExtension= AnnotationExtensionMapper.mapOmrsRelationshipToAnnotationExtension(omrsRelationship);
                lines.add(annotationExtension);
            }
            if (name.equals("DataClassHierarchy")) {
                DataClassHierarchy dataClassHierarchy= DataClassHierarchyMapper.mapOmrsRelationshipToDataClassHierarchy(omrsRelationship);
                lines.add(dataClassHierarchy);
            }
            if (name.equals("GovernanceResults")) {
                GovernanceResults governanceResults= GovernanceResultsMapper.mapOmrsRelationshipToGovernanceResults(omrsRelationship);
                lines.add(governanceResults);
            }
            if (name.equals("APIResponse")) {
                APIResponse aPIResponse= APIResponseMapper.mapOmrsRelationshipToAPIResponse(omrsRelationship);
                lines.add(aPIResponse);
            }
            if (name.equals("SchemaAttributeDefinition")) {
                SchemaAttributeDefinition schemaAttributeDefinition= SchemaAttributeDefinitionMapper.mapOmrsRelationshipToSchemaAttributeDefinition(omrsRelationship);
                lines.add(schemaAttributeDefinition);
            }
            if (name.equals("HostNetwork")) {
                HostNetwork hostNetwork= HostNetworkMapper.mapOmrsRelationshipToHostNetwork(omrsRelationship);
                lines.add(hostNetwork);
            }
            if (name.equals("AttributeForSchema")) {
                AttributeForSchema attributeForSchema= AttributeForSchemaMapper.mapOmrsRelationshipToAttributeForSchema(omrsRelationship);
                lines.add(attributeForSchema);
            }
            if (name.equals("AssetLocation")) {
                AssetLocation assetLocation= AssetLocationMapper.mapOmrsRelationshipToAssetLocation(omrsRelationship);
                lines.add(assetLocation);
            }
            if (name.equals("ParentPort")) {
                ParentPort parentPort= ParentPortMapper.mapOmrsRelationshipToParentPort(omrsRelationship);
                lines.add(parentPort);
            }
            if (name.equals("SoftwareServerSupportedCapability")) {
                SoftwareServerSupportedCapability softwareServerSupportedCapability= SoftwareServerSupportedCapabilityMapper.mapOmrsRelationshipToSoftwareServerSupportedCapability(omrsRelationship);
                lines.add(softwareServerSupportedCapability);
            }
            if (name.equals("DiscoveredAnnotation")) {
                DiscoveredAnnotation discoveredAnnotation= DiscoveredAnnotationMapper.mapOmrsRelationshipToDiscoveredAnnotation(omrsRelationship);
                lines.add(discoveredAnnotation);
            }
            if (name.equals("SchemaAttributeType")) {
                SchemaAttributeType schemaAttributeType= SchemaAttributeTypeMapper.mapOmrsRelationshipToSchemaAttributeType(omrsRelationship);
                lines.add(schemaAttributeType);
            }
            if (name.equals("GovernanceImplementation")) {
                GovernanceImplementation governanceImplementation= GovernanceImplementationMapper.mapOmrsRelationshipToGovernanceImplementation(omrsRelationship);
                lines.add(governanceImplementation);
            }
            if (name.equals("LinkedMedia")) {
                LinkedMedia linkedMedia= LinkedMediaMapper.mapOmrsRelationshipToLinkedMedia(omrsRelationship);
                lines.add(linkedMedia);
            }
            if (name.equals("MapToElementType")) {
                MapToElementType mapToElementType= MapToElementTypeMapper.mapOmrsRelationshipToMapToElementType(omrsRelationship);
                lines.add(mapToElementType);
            }
            if (name.equals("ExternallySourcedGlossary")) {
                ExternallySourcedGlossary externallySourcedGlossary= ExternallySourcedGlossaryMapper.mapOmrsRelationshipToExternallySourcedGlossary(omrsRelationship);
                lines.add(externallySourcedGlossary);
            }
            if (name.equals("ExternalReferenceLink")) {
                ExternalReferenceLink externalReferenceLink= ExternalReferenceLinkMapper.mapOmrsRelationshipToExternalReferenceLink(omrsRelationship);
                lines.add(externalReferenceLink);
            }
            if (name.equals("MediaReference")) {
                MediaReference mediaReference= MediaReferenceMapper.mapOmrsRelationshipToMediaReference(omrsRelationship);
                lines.add(mediaReference);
            }
            if (name.equals("CommunityMembership")) {
                CommunityMembership communityMembership= CommunityMembershipMapper.mapOmrsRelationshipToCommunityMembership(omrsRelationship);
                lines.add(communityMembership);
            }
            if (name.equals("ExternalIdScope")) {
                ExternalIdScope externalIdScope= ExternalIdScopeMapper.mapOmrsRelationshipToExternalIdScope(omrsRelationship);
                lines.add(externalIdScope);
            }
            if (name.equals("ValidValue")) {
                ValidValue validValue= ValidValueMapper.mapOmrsRelationshipToValidValue(omrsRelationship);
                lines.add(validValue);
            }
            if (name.equals("AcceptedAnswer")) {
                AcceptedAnswer acceptedAnswer= AcceptedAnswerMapper.mapOmrsRelationshipToAcceptedAnswer(omrsRelationship);
                lines.add(acceptedAnswer);
            }
            if (name.equals("GovernanceDefinitionMetric")) {
                GovernanceDefinitionMetric governanceDefinitionMetric= GovernanceDefinitionMetricMapper.mapOmrsRelationshipToGovernanceDefinitionMetric(omrsRelationship);
                lines.add(governanceDefinitionMetric);
            }
            if (name.equals("TeamStructure")) {
                TeamStructure teamStructure= TeamStructureMapper.mapOmrsRelationshipToTeamStructure(omrsRelationship);
                lines.add(teamStructure);
            }
            if (name.equals("SchemaQueryImplementation")) {
                SchemaQueryImplementation schemaQueryImplementation= SchemaQueryImplementationMapper.mapOmrsRelationshipToSchemaQueryImplementation(omrsRelationship);
                lines.add(schemaQueryImplementation);
            }
            if (name.equals("AssetSchemaType")) {
                AssetSchemaType assetSchemaType= AssetSchemaTypeMapper.mapOmrsRelationshipToAssetSchemaType(omrsRelationship);
                lines.add(assetSchemaType);
            }
            if (name.equals("ProjectDependency")) {
                ProjectDependency projectDependency= ProjectDependencyMapper.mapOmrsRelationshipToProjectDependency(omrsRelationship);
                lines.add(projectDependency);
            }
            if (name.equals("TopicSubscribers")) {
                TopicSubscribers topicSubscribers= TopicSubscribersMapper.mapOmrsRelationshipToTopicSubscribers(omrsRelationship);
                lines.add(topicSubscribers);
            }
            if (name.equals("ReplacementTerm")) {
                ReplacementTerm replacementTerm= ReplacementTermMapper.mapOmrsRelationshipToReplacementTerm(omrsRelationship);
                lines.add(replacementTerm);
            }
            if (name.equals("OrganizationalCapability")) {
                OrganizationalCapability organizationalCapability= OrganizationalCapabilityMapper.mapOmrsRelationshipToOrganizationalCapability(omrsRelationship);
                lines.add(organizationalCapability);
            }
            if (name.equals("MetadataCohortPeer")) {
                MetadataCohortPeer metadataCohortPeer= MetadataCohortPeerMapper.mapOmrsRelationshipToMetadataCohortPeer(omrsRelationship);
                lines.add(metadataCohortPeer);
            }
            if (name.equals("CollectionMembership")) {
                CollectionMembership collectionMembership= CollectionMembershipMapper.mapOmrsRelationshipToCollectionMembership(omrsRelationship);
                lines.add(collectionMembership);
            }
            if (name.equals("AttachedNoteLogEntry")) {
                AttachedNoteLogEntry attachedNoteLogEntry= AttachedNoteLogEntryMapper.mapOmrsRelationshipToAttachedNoteLogEntry(omrsRelationship);
                lines.add(attachedNoteLogEntry);
            }
            if (name.equals("AssetServerUse")) {
                AssetServerUse assetServerUse= AssetServerUseMapper.mapOmrsRelationshipToAssetServerUse(omrsRelationship);
                lines.add(assetServerUse);
            }
            if (name.equals("RuntimeForProcess")) {
                RuntimeForProcess runtimeForProcess= RuntimeForProcessMapper.mapOmrsRelationshipToRuntimeForProcess(omrsRelationship);
                lines.add(runtimeForProcess);
            }
            if (name.equals("SupportedDiscoveryService")) {
                SupportedDiscoveryService supportedDiscoveryService= SupportedDiscoveryServiceMapper.mapOmrsRelationshipToSupportedDiscoveryService(omrsRelationship);
                lines.add(supportedDiscoveryService);
            }
            if (name.equals("NestedLocation")) {
                NestedLocation nestedLocation= NestedLocationMapper.mapOmrsRelationshipToNestedLocation(omrsRelationship);
                lines.add(nestedLocation);
            }
            if (name.equals("LinkedFile")) {
                LinkedFile linkedFile= LinkedFileMapper.mapOmrsRelationshipToLinkedFile(omrsRelationship);
                lines.add(linkedFile);
            }
            if (name.equals("TermCategorization")) {
                TermCategorization termCategorization= TermCategorizationMapper.mapOmrsRelationshipToTermCategorization(omrsRelationship);
                lines.add(termCategorization);
            }
            if (name.equals("PortWire")) {
                PortWire portWire= PortWireMapper.mapOmrsRelationshipToPortWire(omrsRelationship);
                lines.add(portWire);
            }
            if (name.equals("APIEndpoint")) {
                APIEndpoint aPIEndpoint= APIEndpointMapper.mapOmrsRelationshipToAPIEndpoint(omrsRelationship);
                lines.add(aPIEndpoint);
            }
            if (name.equals("ProjectManagement")) {
                ProjectManagement projectManagement= ProjectManagementMapper.mapOmrsRelationshipToProjectManagement(omrsRelationship);
                lines.add(projectManagement);
            }
            if (name.equals("DataClassDefinition")) {
                DataClassDefinition dataClassDefinition= DataClassDefinitionMapper.mapOmrsRelationshipToDataClassDefinition(omrsRelationship);
                lines.add(dataClassDefinition);
            }
            if (name.equals("ConnectionEndpoint")) {
                ConnectionEndpoint connectionEndpoint= ConnectionEndpointMapper.mapOmrsRelationshipToConnectionEndpoint(omrsRelationship);
                lines.add(connectionEndpoint);
            }
            if (name.equals("CategoryAnchor")) {
                CategoryAnchor categoryAnchor= CategoryAnchorMapper.mapOmrsRelationshipToCategoryAnchor(omrsRelationship);
                lines.add(categoryAnchor);
            }
            if (name.equals("ProjectTeam")) {
                ProjectTeam projectTeam= ProjectTeamMapper.mapOmrsRelationshipToProjectTeam(omrsRelationship);
                lines.add(projectTeam);
            }
            if (name.equals("APIOperations")) {
                APIOperations aPIOperations= APIOperationsMapper.mapOmrsRelationshipToAPIOperations(omrsRelationship);
                lines.add(aPIOperations);
            }
            if (name.equals("RelationshipAnnotation")) {
                RelationshipAnnotation relationshipAnnotation= RelationshipAnnotationMapper.mapOmrsRelationshipToRelationshipAnnotation(omrsRelationship);
                lines.add(relationshipAnnotation);
            }
            if (name.equals("GovernancePolicyLink")) {
                GovernancePolicyLink governancePolicyLink= GovernancePolicyLinkMapper.mapOmrsRelationshipToGovernancePolicyLink(omrsRelationship);
                lines.add(governancePolicyLink);
            }
            if (name.equals("ResponsibilityStaffContact")) {
                ResponsibilityStaffContact responsibilityStaffContact= ResponsibilityStaffContactMapper.mapOmrsRelationshipToResponsibilityStaffContact(omrsRelationship);
                lines.add(responsibilityStaffContact);
            }
            if (name.equals("DiscoveredDataField")) {
                DiscoveredDataField discoveredDataField= DiscoveredDataFieldMapper.mapOmrsRelationshipToDiscoveredDataField(omrsRelationship);
                lines.add(discoveredDataField);
            }
            if (name.equals("PersonRoleAppointment")) {
                PersonRoleAppointment personRoleAppointment= PersonRoleAppointmentMapper.mapOmrsRelationshipToPersonRoleAppointment(omrsRelationship);
                lines.add(personRoleAppointment);
            }
            if (name.equals("AssetDiscoveryService")) {
                AssetDiscoveryService assetDiscoveryService= AssetDiscoveryServiceMapper.mapOmrsRelationshipToAssetDiscoveryService(omrsRelationship);
                lines.add(assetDiscoveryService);
            }
            if (name.equals("Peer")) {
                Peer peer= PeerMapper.mapOmrsRelationshipToPeer(omrsRelationship);
                lines.add(peer);
            }
            if (name.equals("ActionAssignment")) {
                ActionAssignment actionAssignment= ActionAssignmentMapper.mapOmrsRelationshipToActionAssignment(omrsRelationship);
                lines.add(actionAssignment);
            }
            if (name.equals("TermTYPEDBYRelationship")) {
                TermTYPEDBYRelationship termTYPEDBYRelationship= TermTYPEDBYRelationshipMapper.mapOmrsRelationshipToTermTYPEDBYRelationship(omrsRelationship);
                lines.add(termTYPEDBYRelationship);
            }
            if (name.equals("MapFromElementType")) {
                MapFromElementType mapFromElementType= MapFromElementTypeMapper.mapOmrsRelationshipToMapFromElementType(omrsRelationship);
                lines.add(mapFromElementType);
            }
            if (name.equals("SchemaTypeDefinition")) {
                SchemaTypeDefinition schemaTypeDefinition= SchemaTypeDefinitionMapper.mapOmrsRelationshipToSchemaTypeDefinition(omrsRelationship);
                lines.add(schemaTypeDefinition);
            }
            if (name.equals("AssetWire")) {
                AssetWire assetWire= AssetWireMapper.mapOmrsRelationshipToAssetWire(omrsRelationship);
                lines.add(assetWire);
            }
            if (name.equals("Certification")) {
                Certification certification= CertificationMapper.mapOmrsRelationshipToCertification(omrsRelationship);
                lines.add(certification);
            }
            if (name.equals("GovernancePost")) {
                GovernancePost governancePost= GovernancePostMapper.mapOmrsRelationshipToGovernancePost(omrsRelationship);
                lines.add(governancePost);
            }
            if (name.equals("NetworkGatewayLink")) {
                NetworkGatewayLink networkGatewayLink= NetworkGatewayLinkMapper.mapOmrsRelationshipToNetworkGatewayLink(omrsRelationship);
                lines.add(networkGatewayLink);
            }
            if (name.equals("GovernanceResponsibilityAssignment")) {
                GovernanceResponsibilityAssignment governanceResponsibilityAssignment= GovernanceResponsibilityAssignmentMapper.mapOmrsRelationshipToGovernanceResponsibilityAssignment(omrsRelationship);
                lines.add(governanceResponsibilityAssignment);
            }
            if (name.equals("ConnectionConnectorType")) {
                ConnectionConnectorType connectionConnectorType= ConnectionConnectorTypeMapper.mapOmrsRelationshipToConnectionConnectorType(omrsRelationship);
                lines.add(connectionConnectorType);
            }
            if (name.equals("Synonym")) {
                Synonym synonym= SynonymMapper.mapOmrsRelationshipToSynonym(omrsRelationship);
                lines.add(synonym);
            }
            if (name.equals("Antonym")) {
                Antonym antonym= AntonymMapper.mapOmrsRelationshipToAntonym(omrsRelationship);
                lines.add(antonym);
            }
            if (name.equals("GraphEdgeLink")) {
                GraphEdgeLink graphEdgeLink= GraphEdgeLinkMapper.mapOmrsRelationshipToGraphEdgeLink(omrsRelationship);
                lines.add(graphEdgeLink);
            }
            if (name.equals("ProjectScope")) {
                ProjectScope projectScope= ProjectScopeMapper.mapOmrsRelationshipToProjectScope(omrsRelationship);
                lines.add(projectScope);
            }
            if (name.equals("ISARelationship")) {
                ISARelationship iSARelationship= ISARelationshipMapper.mapOmrsRelationshipToISARelationship(omrsRelationship);
                lines.add(iSARelationship);
            }
            if (name.equals("TermISATypeOFRelationship")) {
                TermISATypeOFRelationship termISATypeOFRelationship= TermISATypeOFRelationshipMapper.mapOmrsRelationshipToTermISATypeOFRelationship(omrsRelationship);
                lines.add(termISATypeOFRelationship);
            }
            if (name.equals("Meetings")) {
                Meetings meetings= MeetingsMapper.mapOmrsRelationshipToMeetings(omrsRelationship);
                lines.add(meetings);
            }
            if (name.equals("EmbeddedConnection")) {
                EmbeddedConnection embeddedConnection= EmbeddedConnectionMapper.mapOmrsRelationshipToEmbeddedConnection(omrsRelationship);
                lines.add(embeddedConnection);
            }
            if (name.equals("GovernanceControlLink")) {
                GovernanceControlLink governanceControlLink= GovernanceControlLinkMapper.mapOmrsRelationshipToGovernanceControlLink(omrsRelationship);
                lines.add(governanceControlLink);
            }
            if (name.equals("AttachedRating")) {
                AttachedRating attachedRating= AttachedRatingMapper.mapOmrsRelationshipToAttachedRating(omrsRelationship);
                lines.add(attachedRating);
            }
            if (name.equals("DataClassAssignment")) {
                DataClassAssignment dataClassAssignment= DataClassAssignmentMapper.mapOmrsRelationshipToDataClassAssignment(omrsRelationship);
                lines.add(dataClassAssignment);
            }
            if (name.equals("AttachedNoteLog")) {
                AttachedNoteLog attachedNoteLog= AttachedNoteLogMapper.mapOmrsRelationshipToAttachedNoteLog(omrsRelationship);
                lines.add(attachedNoteLog);
            }
            if (name.equals("ConnectionToAsset")) {
                ConnectionToAsset connectionToAsset= ConnectionToAssetMapper.mapOmrsRelationshipToConnectionToAsset(omrsRelationship);
                lines.add(connectionToAsset);
            }
            if (name.equals("RegulationCertificationType")) {
                RegulationCertificationType regulationCertificationType= RegulationCertificationTypeMapper.mapOmrsRelationshipToRegulationCertificationType(omrsRelationship);
                lines.add(regulationCertificationType);
            }
            if (name.equals("LinkedType")) {
                LinkedType linkedType= LinkedTypeMapper.mapOmrsRelationshipToLinkedType(omrsRelationship);
                lines.add(linkedType);
            }
            if (name.equals("CrowdSourcingContribution")) {
                CrowdSourcingContribution crowdSourcingContribution= CrowdSourcingContributionMapper.mapOmrsRelationshipToCrowdSourcingContribution(omrsRelationship);
                lines.add(crowdSourcingContribution);
            }
            if (name.equals("ProcessHierarchy")) {
                ProcessHierarchy processHierarchy= ProcessHierarchyMapper.mapOmrsRelationshipToProcessHierarchy(omrsRelationship);
                lines.add(processHierarchy);
            }
            if (name.equals("GovernanceRuleImplementation")) {
                GovernanceRuleImplementation governanceRuleImplementation= GovernanceRuleImplementationMapper.mapOmrsRelationshipToGovernanceRuleImplementation(omrsRelationship);
                lines.add(governanceRuleImplementation);
            }
            if (name.equals("AdjacentLocation")) {
                AdjacentLocation adjacentLocation= AdjacentLocationMapper.mapOmrsRelationshipToAdjacentLocation(omrsRelationship);
                lines.add(adjacentLocation);
            }
            if (name.equals("SemanticAssignment")) {
                SemanticAssignment semanticAssignment= SemanticAssignmentMapper.mapOmrsRelationshipToSemanticAssignment(omrsRelationship);
                lines.add(semanticAssignment);
            }
            if (name.equals("HostLocation")) {
                HostLocation hostLocation= HostLocationMapper.mapOmrsRelationshipToHostLocation(omrsRelationship);
                lines.add(hostLocation);
            }
            if (name.equals("RelatedTerm")) {
                RelatedTerm relatedTerm= RelatedTermMapper.mapOmrsRelationshipToRelatedTerm(omrsRelationship);
                lines.add(relatedTerm);
            }
            if (name.equals("SchemaTypeImplementation")) {
                SchemaTypeImplementation schemaTypeImplementation= SchemaTypeImplementationMapper.mapOmrsRelationshipToSchemaTypeImplementation(omrsRelationship);
                lines.add(schemaTypeImplementation);
            }
            if (name.equals("APIHeader")) {
                APIHeader aPIHeader= APIHeaderMapper.mapOmrsRelationshipToAPIHeader(omrsRelationship);
                lines.add(aPIHeader);
            }
            if (name.equals("LibraryTermReference")) {
                LibraryTermReference libraryTermReference= LibraryTermReferenceMapper.mapOmrsRelationshipToLibraryTermReference(omrsRelationship);
                lines.add(libraryTermReference);
            }
            if (name.equals("AttachedTag")) {
                AttachedTag attachedTag= AttachedTagMapper.mapOmrsRelationshipToAttachedTag(omrsRelationship);
                lines.add(attachedTag);
            }
            if (name.equals("TermHASARelationship")) {
                TermHASARelationship termHASARelationship= TermHASARelationshipMapper.mapOmrsRelationshipToTermHASARelationship(omrsRelationship);
                lines.add(termHASARelationship);
            }
            if (name.equals("GovernanceProcessImplementation")) {
                GovernanceProcessImplementation governanceProcessImplementation= GovernanceProcessImplementationMapper.mapOmrsRelationshipToGovernanceProcessImplementation(omrsRelationship);
                lines.add(governanceProcessImplementation);
            }
            if (name.equals("GroupedMedia")) {
                GroupedMedia groupedMedia= GroupedMediaMapper.mapOmrsRelationshipToGroupedMedia(omrsRelationship);
                lines.add(groupedMedia);
            }
            if (name.equals("GovernanceResponse")) {
                GovernanceResponse governanceResponse= GovernanceResponseMapper.mapOmrsRelationshipToGovernanceResponse(omrsRelationship);
                lines.add(governanceResponse);
            }
            if (name.equals("NestedFile")) {
                NestedFile nestedFile= NestedFileMapper.mapOmrsRelationshipToNestedFile(omrsRelationship);
                lines.add(nestedFile);
            }
            if (name.equals("PortInterface")) {
                PortInterface portInterface= PortInterfaceMapper.mapOmrsRelationshipToPortInterface(omrsRelationship);
                lines.add(portInterface);
            }
            if (name.equals("HostOperatingPlatform")) {
                HostOperatingPlatform hostOperatingPlatform= HostOperatingPlatformMapper.mapOmrsRelationshipToHostOperatingPlatform(omrsRelationship);
                lines.add(hostOperatingPlatform);
            }
            if (name.equals("TermAnchor")) {
                TermAnchor termAnchor= TermAnchorMapper.mapOmrsRelationshipToTermAnchor(omrsRelationship);
                lines.add(termAnchor);
            }
            if (name.equals("ForeignKey")) {
                ForeignKey foreignKey= ForeignKeyMapper.mapOmrsRelationshipToForeignKey(omrsRelationship);
                lines.add(foreignKey);
            }
            if (name.equals("DataProfileLogFile")) {
                DataProfileLogFile dataProfileLogFile= DataProfileLogFileMapper.mapOmrsRelationshipToDataProfileLogFile(omrsRelationship);
                lines.add(dataProfileLogFile);
            }
            if (name.equals("DiscoveryEngineReport")) {
                DiscoveryEngineReport discoveryEngineReport= DiscoveryEngineReportMapper.mapOmrsRelationshipToDiscoveryEngineReport(omrsRelationship);
                lines.add(discoveryEngineReport);
            }
            if (name.equals("APIRequest")) {
                APIRequest aPIRequest= APIRequestMapper.mapOmrsRelationshipToAPIRequest(omrsRelationship);
                lines.add(aPIRequest);
            }
            if (name.equals("SoftwareServerDeployment")) {
                SoftwareServerDeployment softwareServerDeployment= SoftwareServerDeploymentMapper.mapOmrsRelationshipToSoftwareServerDeployment(omrsRelationship);
                lines.add(softwareServerDeployment);
            }
            if (name.equals("GovernanceRoleAssignment")) {
                GovernanceRoleAssignment governanceRoleAssignment= GovernanceRoleAssignmentMapper.mapOmrsRelationshipToGovernanceRoleAssignment(omrsRelationship);
                lines.add(governanceRoleAssignment);
            }
            if (name.equals("CohortMemberMetadataCollection")) {
                CohortMemberMetadataCollection cohortMemberMetadataCollection= CohortMemberMetadataCollectionMapper.mapOmrsRelationshipToCohortMemberMetadataCollection(omrsRelationship);
                lines.add(cohortMemberMetadataCollection);
            }
            if (name.equals("ReferenceableFacet")) {
                ReferenceableFacet referenceableFacet= ReferenceableFacetMapper.mapOmrsRelationshipToReferenceableFacet(omrsRelationship);
                lines.add(referenceableFacet);
            }
            if (name.equals("AssetDiscoveryReport")) {
                AssetDiscoveryReport assetDiscoveryReport= AssetDiscoveryReportMapper.mapOmrsRelationshipToAssetDiscoveryReport(omrsRelationship);
                lines.add(assetDiscoveryReport);
            }
            if (name.equals("AttachedLike")) {
                AttachedLike attachedLike= AttachedLikeMapper.mapOmrsRelationshipToAttachedLike(omrsRelationship);
                lines.add(attachedLike);
            }
            if (name.equals("NoteLogAuthorship")) {
                NoteLogAuthorship noteLogAuthorship= NoteLogAuthorshipMapper.mapOmrsRelationshipToNoteLogAuthorship(omrsRelationship);
                lines.add(noteLogAuthorship);
            }
        }
        return lines;
    }
}
