<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository find type definitions by category test case

This test validates that all of the supported type definitions (TypeDefs) can be retrieved by category.

## Operation

Extract all known TypeDefs.  Separate them into three lists: 
Entity TypeDefs (EntityDefs), 
Relationship TypeDefs (RelationshipDefs)
and Classification TypeDefs (Classification).
Issue a query for each of these categories of TypeDef and validate that what is returned matches
the original complied lists.

## Assertions

* **repository-find-typedefs-by-category-01** - All type definitions returned by category.

   The correct list of TypeDefs were returned.

## Discovered Properties

* **Number of supported EntityDefs** - count of returned entity types
* **Supported EntityDefs** - list of returned entity types
* **Number of supported RelationshipDefs** - count of returned relationship types
* **Supported RelationshipDefs** - list of returned relationship types
* **Number of supported ClassificationDefs** - count of returned classification types
* **Supported ClassificationDefs** - list of returned classification types

## Example Output

```json
{
      "class" : "OpenMetadataTestCaseResult",
      "testCaseId" : "repository-find-typedefs-by-category",
      "testCaseName" : "Repository find type definitions by category test case",
      "testCaseDescriptionURL" : "https://egeria-project.org/guides/cts/repository-workbench/repository-find-typedefs-by-category-test-case.md",
      "assertionMessage" : "Type definitions can be extracted by category",
      "successfulAssertions" : [ "All type definitions returned by category." ],
      "unsuccessfulAssertions" : [ ],
      "discoveredProperties" : {
        "Supported ClassificationDefs" : [ "DataValue", "Confidentiality", "NamingConventionRule", "ControlPoint", "MetadataServer", "AbstractConcept", "CloudService", "SpineAttribute", "ListenerInterface", "MeteringLog", "ContextDefinition", "PrimaryKey", "DataVirtualizationEngine", "NotificationManager", "PublisherInterface", "MobileAsset", "Webserver", "DataMovementEngine", "PrimeWord", "ClassWord", "ActivityDescription", "SecureLocation", "RelationalView", "SpineObject", "CanonicalVocabulary", "Set", "AuditLog", "Campaign", "CloudPlatform", "ApplicationServer", "FixedLocation", "FileSystem", "AuditLogFile", "CloudProvider", "GovernanceDaemon", "DataStoreEncoding", "DatabaseServer", "VerificationPoint", "GovernanceProject", "Task", "ContentManager", "Retention", "GlossaryProject", "RepositoryProxy", "Criticality", "EnforcementPoint", "AnalyticsEngine", "SubjectArea", "ExceptionBacklog", "GovernanceMeasurementsResultsDataSet", "ReportingEngine", "RequestResponseInterface", "ExceptionLogFile", "Folder", "CyberLocation", "StewardshipServer", "Taxonomy", "ObjectIdentifier", "Confidence", "WorkflowEngine" ],
        "Supported EntityDefs" : [ "PropertyFacet", "SetSchemaType", "UserIdentity", "NoteEntry", "Referenceable", "ExternalId", "Document", "Network", "KeystoreFile", "SoftwareServerCapability", "Database", "StructSchemaType", "GovernanceProcedure", "MetadataRepositoryCohort", "ImplementationSnippet", "APISchemaType", "ProjectCharter", "InformationView", "DocumentSchemaAttribute", "SimpleDocumentType", "ComplexSchemaType", "LicenseType", "TabularSchemaType", "Connection", "GovernanceControl", "GovernanceZone", "GlossaryCategory", "DataClass", "APIOperation", "Asset", "HostCluster", "Regulation", "Like", "DocumentStore", "InformalTag", "Team", "GraphEdge", "Glossary", "MetadataCollection", "EventSet", "GovernanceProcess", "GovernanceDriver", "MetadataRepository", "ReferenceCodeTable", "DeployedSoftwareComponent", "EngineProfile", "GovernancePrinciple", "Organization", "BoundedSchemaType", "BusinessCapability", "GovernanceDefinition", "Process", "MediaFile", "SchemaElement", "OperatingPlatform", "NamingStandardRule", "GovernanceStrategy", "CertificationType", "ExternalGlossaryLink", "RelatedMedia", "LogFile", "Topic", "SoftwareServer", "GovernanceOfficer", "TabularColumn", "GovernancePolicy", "Infrastructure", "CohortRegistryStore", "SchemaAttribute", "DerivedRelationalColumn", "NetworkGateway", "RelationalColumnType", "MapSchemaType", "ITInfrastructure", "Engine", "PrivateTag", "DerivedSchemaAttribute", "GraphStore", "CohortMember", "RelationalColumn", "SetDocumentType", "GovernanceObligation", "TabularColumnType", "ArrayDocumentType", "ObjectAttribute", "StructDocumentType", "DataSet", "Project", "ArraySchemaType", "MapDocumentType", "Meeting", "RelationalTableType", "ActorProfile", "ToDo", "OrganizationalControl", "GovernanceMetric", "ConnectorType", "ObjectSchemaType", "Community", "Location", "VirtualContainer", "DeployedReport", "GovernanceRule", "FileFolder", "NamingStandardRuleSet", "Comment", "DataStore", "ReferenceCodeMappingTable", "SubscriberList", "DocumentSchemaType", "GlossaryTerm", "Person", "PrimitiveSchemaType", "ContactDetails", "Form", "Application", "GraphVertex", "Rating", "RelationalDBSchemaType", "Collection", "SchemaType", "TechnicalControl", "Endpoint", "GovernanceApproach", "VirtualConnection", "DeployedDatabaseSchema", "ControlledGlossaryTerm", "GraphSchemaType", "DataFile", "Host", "RelationalTable", "EnterpriseAccessLayer", "DeployedAPI", "NoteLog", "MediaCollection", "GovernanceResponsibility", "ExternalReference", "KeyStoreCollection", "EventType", "SchemaLinkElement" ],
        "Supported RelationshipDefs" : [ "TermISATypeOFRelationship", "StaffAssignment", "CategoryAnchor", "AssetLocation", "ProfileIdentity", "GovernanceControlLink", "ProjectCharterLink", "BusinessCapabilityControls", "GovernanceProcessImplementation", "ProcessOutput", "Antonym", "NestedLocation", "ReplacementTerm", "GovernanceResults", "AdjacentLocation", "AttachedNoteLog", "GovernanceResponse", "SoftwareComponentDeployment", "UsedInContext", "HostNetwork", "MediaReference", "DataClassHierarchy", "Synonym", "HostClusterMember", "GovernancePost", "LinkedFile", "License", "Peer", "NestedFile", "DataClassComposition", "APIRequest", "Translation", "APIOperations", "ProjectTeam", "FolderHierarchy", "PreferredTerm", "AttachedLike", "RegulationCertificationType", "CohortMemberMetadataCollection", "TopicSubscribers", "NetworkGatewayLink", "MapFromElementType", "GovernanceRuleImplementation", "ProjectMeeting", "CommunityResources", "ProcessInput", "DeployedVirtualContainer", "ServerSupportedCapability", "ZoneGovernance", "ValidValue", "CommunityMembership", "ConnectionToAsset", "RuntimeForProcess", "ServerEndpoint", "AssetServerUse", "TermHASARelationship", "GovernanceDefinitionMetric", "LinkedType", "ReferenceableFacet", "Leadership", "LinkedMedia", "ActorCollection", "TermCategorization", "ToDoOnReferenceable", "LibraryTermReference", "ProjectScope", "ContactThrough", "Certification", "SchemaQueryImplementation", "GroupedMedia", "HostOperatingPlatform", "SchemaAttributeType", "MeetingOnReferenceable", "ServerDeployment", "APIEndpoint", "HostLocation", "CategoryHierarchyLink", "SchemaTypeImplementation", "MapToElementType", "ExternalIdScope", "CollectionMembership", "ConnectionEndpoint", "AttachedNoteLogEntry", "AssetSchemaType", "LibraryCategoryReference", "DataClassAssignment", "AttributeForSchema", "ToDoSource", "APIHeader", "AttachedComment", "AttachedRating", "GovernancePolicyLink", "ConnectionConnectorType", "TermAnchor", "ProjectDependency", "RelatedTerm", "ExternallySourcedGlossary", "DataSetContent", "GovernanceImplementation", "OrganizationalCapability", "APIResponse", "ExternalIdLink", "AcceptedAnswer", "GraphEdgeLink", "Contributor", "AttachedTag", "ExternalReferenceLink", "ProjectHierarchy", "ForeignKey", "ProjectResources", "TermTYPEDBYRelationship", "EmbeddedConnection", "SemanticAssignment", "MetadataCohortPeer", "SchemaLinkToType", "ResponsibilityStaffContact", "ISARelationship" ],
        "Number of supported RelationshipDefs" : 117,
        "Number of supported EntityDefs" : 140,
        "Number of supported ClassificationDefs" : 60
      }
}
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.