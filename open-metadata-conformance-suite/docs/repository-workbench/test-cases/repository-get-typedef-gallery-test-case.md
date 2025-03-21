<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Repository get type definition gallery test case

Validate that it is possible to retrieve type definitions from the repository.

## Operation

This test uses the getAllTypes
(`/open-metadata/repository-services/users/{userId}/types/all`)
operation to retrieve all of the supported types.
The type definition gallery (TypeDefGallery)
returned contains definitions of both the attribute types (AttributeTypeDefs)
and entity, relationship and classification type (TypeDefs) supported by the
repository.
The contents of the type definition gallery
is used to verify the results of other
calls to the repository services.

## Assertions

* **repository-get-typedef-gallery-01** TypeDefGallery retrieved.
  
  The `getAllTypes` call returned a type definition gallery.
  
## Discovered Properties

* **Number of supported AttributeTypeDefs** count of the attribute type definitions returned.
* **Supported AttributeTypeDefs** list of the attribute type definitions returned.
* **Number of supported TypeDefs** count of the type definitions returned.
* **Supported TypeDefs** list of the type definitions returned.

## Example output

```json
{
      "class" : "OpenMetadataTestCaseResult",
      "testCaseId" : "repository-get-typedef-gallery",
      "testCaseName" : "Repository get type definition gallery test case",
      "testCaseDescriptionURL" : "https://egeria-project.org/guides/cts/origin-workbench/repository-get-typedef-gallery-test-case.md",
      "assertionMessage" : "Repository type definition gallery retrieved",
      "successfulAssertions" : [ "TypeDefGallery retrieved." ],
      "unsuccessfulAssertions" : [ ],
      "discoveredProperties" : {
        "Supported AttributeTypeDefs" : [ "CrowdSourcingRole", "string", "char", "CriticalityLevel", "TermRelationshipStatus", "Endianness", "array<int>", "ContactMethodType", "GovernanceClassificationStatus", "BusinessCapabilityType", "StarRating", "OperationalStatus", "OrderBy", "boolean", "TermAssignmentStatus", "RetentionBasis", "ServerAssetUse", "ConfidentialityLevel", "MediaUsage", "biginteger", "ActivityType", "byte", "CommunityMembershipType", "long", "ConfidenceLevel", "KeyPattern", "CommentType", "double", "date", "MediaType", "GovernanceDomain", "object", "map<string,string>", "array<string>", "bigdecimal", "short", "float", "ToDoStatus", "int" ],
        "Supported TypeDefs" : [ "PropertyFacet", "DataValue", "Confidentiality", "TermISATypeOFRelationship", "StaffAssignment", "SetSchemaType", "CategoryAnchor", "AssetLocation", "UserIdentity", "NoteEntry", "ProfileIdentity", "NamingConventionRule", "Referenceable", "ControlPoint", "GovernanceControlLink", "ProjectCharterLink", "BusinessCapabilityControls", "GovernanceProcessImplementation", "ExternalId", "Document", "Network", "KeystoreFile", "ProcessOutput", "MetadataServer", "SoftwareServerCapability", "Database", "AbstractConcept", "Antonym", "StructSchemaType", "CloudService", "SpineAttribute", "ListenerInterface", "GovernanceProcedure", "MeteringLog", "NestedLocation", "ReplacementTerm", "GovernanceResults", "AdjacentLocation", "ContextDefinition", "PrimaryKey", "AttachedNoteLog", "MetadataRepositoryCohort", "ImplementationSnippet", "GovernanceResponse", "DataVirtualizationEngine", "APISchemaType", "SoftwareComponentDeployment", "UsedInContext", "ProjectCharter", "NotificationManager", "HostNetwork", "InformationView", "DocumentSchemaAttribute", "SimpleDocumentType", "PublisherInterface", "MediaReference", "ComplexSchemaType", "Synonym", "LicenseType", "TabularSchemaType", "Connection", "GovernanceControl", "HostClusterMember", "GovernancePost", "GovernanceZone", "GlossaryCategory", "MobileAsset", "LinkedFile", "License", "Peer", "APIOperation", "Asset", "NestedFile", "APIRequest", "HostCluster", "Translation", "Webserver", "APIOperations", "ProjectTeam", "Regulation", "FolderHierarchy", "Like", "DocumentStore", "InformalTag", "DataMovementEngine", "Team", "GraphEdge", "Glossary", "MetadataCollection", "EventSet", "GovernanceProcess", "GovernanceDriver", "PreferredTerm", "AttachedLike", "RegulationCertificationType", "MetadataRepository", "ReferenceCodeTable", "DeployedSoftwareComponent", "EngineProfile", "GovernancePrinciple", "CohortMemberMetadataCollection", "Organization", "BoundedSchemaType", "BusinessCapability", "PrimeWord", "ClassWord", "GovernanceDefinition", "TopicSubscribers", "ActivityDescription", "NetworkGatewayLink", "MapFromElementType", "Process", "GovernanceRuleImplementation", "ProjectMeeting", "CommunityResources", "MediaFile", "SchemaElement", "SecureLocation", "ProcessInput", "DeployedVirtualContainer", "RelationalView", "OperatingPlatform", "SpineObject", "ServerSupportedCapability", "CanonicalVocabulary", "ZoneGovernance", "NamingStandardRule", "ValidValue", "DataStrategy", "CommunityMembership", "CertificationType", "ExternalGlossaryLink", "ConnectionToAsset", "RelatedMedia", "RuntimeForProcess", "LogFile", "Topic", "Set", "AuditLog", "SoftwareServer", "GovernanceOfficer", "ServerEndpoint", "TabularColumn", "AssetServerUse", "GovernancePolicy", "Infrastructure", "TermHASARelationship", "Campaign", "CohortRegistryStore", "SchemaAttribute", "DerivedRelationalColumn", "NetworkGateway", "CloudPlatform", "ApplicationServer", "RelationalColumnType", "GovernanceDefinitionMetric", "LinkedType", "MapSchemaType", "ReferenceableFacet", "Leadership", "LinkedMedia", "ITInfrastructure", "FixedLocation", "ActorCollection", "Engine", "FileSystem", "TermCategorization", "AuditLogFile", "ToDoOnReferenceable", "PrivateTag", "DerivedSchemaAttribute", "LibraryTermReference", "GraphStore", "CloudProvider", "ProjectScope", "CohortMember", "RelationalColumn", "ContactThrough", "SetDocumentType", "Certification", "GovernanceObligation", "SchemaQueryImplementation", "GovernanceDaemon", "Report", "TabularColumnType", "ArrayDocumentType", "GroupedMedia", "ObjectAttribute", "HostOperatingPlatform", "SchemaAttributeType", "StructDocumentType", "DataStoreEncoding", "DatabaseServer", "VerificationPoint", "MeetingOnReferenceable", "DataSet", "ZoneMembership", "Project", "ArraySchemaType", "MapDocumentType", "ServerDeployment", "APIEndpoint", "GovernanceProject", "HostLocation", "Task", "CategoryHierarchyLink", "SchemaTypeImplementation", "Meeting", "MapToElementType", "ContentManager", "ExternalIdScope", "CollectionMember", "ConnectionEndpoint", "AttachedNoteLogEntry", "RelationalTableType", "AssetSchemaType", "ActorProfile", "LibraryCategoryReference", "ToDo", "OrganizationalControl", "GovernanceMetric", "ConnectorType", "AttributeForSchema", "ToDoSource", "APIHeader", "AttachedComment", "AttachedRating", "Retention", "ObjectSchemaType", "GlossaryProject", "Community", "GovernancePolicyLink", "ConnectionConnectorType", "Location", "VirtualContainer", "TermAnchor", "RepositoryProxy", "GovernanceRule", "FileFolder", "NamingStandardRuleSet", "Criticality", "ProjectDependency", "RelatedTerm", "Comment", "DataStore", "EnforcementPoint", "ExternallySourcedGlossary", "ReferenceCodeMappingTable", "SubscriberList", "DataSetContent", "GovernanceImplementation", "DocumentSchemaType", "GlossaryTerm", "Person", "PrimitiveSchemaType", "AnalyticsEngine", "SubjectArea", "ContactDetails", "ExceptionBacklog", "OrganizationalCapability", "Form", "Application", "GraphVertex", "Rating", "GovernanceMeasurementsResultsDataSet", "RelationalDBSchemaType", "Collection", "SchemaType", "APIResponse", "ExternalIdLink", "TechnicalControl", "AcceptedAnswer", "GraphEdgeLink", "Contributor", "ReportingEngine", "AttachedTag", "Endpoint", "ExternalReferenceLink", "RequestResponseInterface", "GovernanceApproach", "VirtualConnection", "DeployedDatabaseSchema", "ControlledGlossaryTerm", "ProjectHierarchy", "GraphSchemaType", "ExceptionLogFile", "ForeignKey", "DataFile", "Folder", "ProjectResources", "TermTYPEDBYRelationship", "Host", "EmbeddedConnection", "SemanticAssignment", "MetadataCohortPeer", "RelationalTable", "CyberLocation", "StewardshipServer", "EnterpriseAccessLayer", "SchemaLinkToType", "ResponsibilityStaffContact", "Taxonomy", "ObjectIdentifier", "DeployedAPI", "NoteLog", "MediaCollection", "GovernanceResponsibility", "Confidence", "ExternalReference", "KeyStoreCollection", "EventType", "WorkflowEngine", "ISARelationship", "SchemaLinkElement" ],
        "Number of supported AttributeTypeDefs" : 39,
        "Number of supported TypeDefs" : 314
      }
}
```



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.