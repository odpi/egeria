<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# The open metadata type system

Knowledge about data is spread amongst many people and systems.
One of the roles of a metadata repository is to provide a place
where this knowledge can be collected and correlated in as automated fashion as possible.
To enable many different tools and processes to populate the metadata repository we need
agreement on what data should be stored and in what format (structures). 

Figure 1 shows the different areas of metadata that we need
to support for a wide range of metadata management and governance tasks.
This metadata may be spread across different metadata repositories that each specialize in
particular use cases or communities of users.

![Figure 1: Common metadata areas](Figure-1-Open-Metadata-Areas.png#pagewidth)
> Figure 1: Common metadata areas

* **[Area 0](Area-0-models.md)** describes base types and infrastructure.  This includes the root type for all open metadata entities called **OpenMetadataRoot** and types for Asset, DataSet, Infrastructure, Process, Referenceable, SoftwareServer and Host.
* **[Area 1](Area-1-models.md)** collects information from people using the data assets.  It includes their use of the assets and their feedback.  It also manages crowd-sourced enhancements to the metadata from other areas before it is approved and incorporated into the governance program.
* **[Area 2](Area-2-models.md)** describes the data assets.  These are the data sources, APIs, analytics models, transformation functions and rule implementations that store and manage data.  The definitions in Area 2 include connectivity information that is used by the open connector framework (and other tools) to get access to the data assets.
* **[Area 3](Area-3-models.md)** describes the glossary.  This is the definitions of terms and concepts and how they relate to one another.  Linking the concepts/terms defined in the glossary to the data assets in Area 2 defines the meaning of the data that is managed by the data assets.  This is a key relationship that helps people locate and understand the data assets they are working with.
* **[Area 4](Area-4-models.md)** defines how the data assets should be governed.  This is where the classifications, policies and rules are defined.
* **[Area 5](Area-5-models.md)** is where standards are established.  This includes data models, schema fragments and reference data that are used to assist developers and architects in using best practice data structures and valid values as they develop new capabilities around the data assets.
* **[Area 6](Area-6-models.md)** provides the additional information that automated metadata discovery engines have discovered about the data assets.  This includes profile information, quality scores and suggested classifications.
* **[Area 7](Area-7-models.md)** provides the structures for recording lineage.

Figure 2 provides more detail of the metadata structures in each area and how they link together.

Bottom left is Area 0 - the foundation of the open metadata types along with the IT infrastructure
that digital systems run on such as platforms, servers and network connections.
Sitting on the foundation are the assets.  The base definition for Asset is in Area 0 but
Area 2 (middle bottom) builds
out some of the common types of assets that an organization uses.
These assets are hosted and linked to the infrastructure described in Area 0.  For example,
a data set could be linked to the file system description to show where it is stored.

Area 5 (right middle) focuses on defining the structure of data and the standard sets of values (called reference data).
The structure of data is described in schemas and these are linked to the assets that use them.

Many assets have technical names.  Area 3 (top middle) captures business and real world terminologies and
organizes them into glossaries.  The individual terms described can be linked to the technical
names and labels given to the assets and the data fields described in their schemas.

Area 6 (bottom right) captures additional metadata captured through automated analysis of data.
These analysis results are linked to the assets that hold the data so that data professionals can
evaluate the suitability of the data for different purposes.
Area 7 (left middle) captures the lineage of assets from a business and technical perspective.
Above that in Area 4 are the definitions that control the governance of all of the assets.
Finally Area 1 (top right) captures information about users
(people, automated process) their organization, such as teams and projects, and feedback.

![Figure 2: Metadata detail within the metadata areas](Figure-2-Open-Metadata-Areas-Detail.png#pagewidth)
> Figure 2: Metadata detail within the metadata areas

Within each area, the definitions are broken down into numbered packages to help identify
groups of related elements.
The numbering system relates to the area that the elements belong to.
For example, area 1 has models 0100-0199, area 2 has models 0200-299, etc.
Each area's sub-models are dispersed along its range, ensuring there is space to
insert additional models in the future.

Figure 3 shows a couple of fragments from the models.  Each of the UML classes represents
an open metadata type. The stereotype on the UML class in the double angle brackets
of **entity**, **relationship** and **classification** defines the category of type:
Entity, Relationship or Classification respectively. The line between entities with the big
arrow head means "inheritance".  A type points to its supertype.  

The example on the left of figure 3 comes from model [0010](0010-Base-Model.md).
it shows that **Asset** inherits from **Referenceable** which inherits from **OpenMetadataRoot**.
This means that **Asset** is a subtype of **Referenceable** which is a subtype of **OpenMetadataRoot**.
Alternatively, **OpenMetadataRoot** is the supertype of **Referenceable** which is a supertype of
**Asset**.  This inheritance identifies which attributes (instance properties) are valid for an instance of a
particular type since
it is the aggregation of the attributes defined explicitly for the type and all of its supertypes.
For example, **Asset** has two attributes defined: **name** and **description**.
It also supports **qualifiedName** and **additionalProperties** because they are
inherited from **Referenceable**.  **OpenMetadataRoot** does not have any attributes defined
so **Asset** gets nothing from it.

The fragment on the right-hand side of figure 3 comes from mode [0011](0011-Managing-Referenceables.md).
It shows the classification called **Template** that can be connected to a **Referenceable**.
Since **Referenceable** is already defined in model 0010, it is shown without the white box
where the attributes are show (called the "attribute container" in UML parlance).

**SourcedFrom** is a relationship that connects two instances of **Referenceable** and any of its
sub types.  This means **SourcedFrom** could connect two instances of type **Asset** together.
The types of the instances connected do not need to be the same - **SourcedFrom** could
connect a **Referenceable** instance with an **Asset** instance.


![Figure 3: Guide to reading the open metadata type models](model-guidance.png#pagewidth)
> Figure 3: Guide to reading the open metadata type models

The UML model diagrams show the currently active types.  Some types and attributes have been deprecated and
these have been removed from the model diagrams.  However there is a description of the deprecated types and
which of the active types to use instead.  Although the deprecated types can be used (for backwards compatibility)
it is always preferable to use the latest types since they are typically more efficient
and more consistent than their predecessors.

## Links to the detailed description of the types

* **[Area 0](Area-0-models.md)** - base types and infrastructure.  This includes types for Asset, DataSet, Infrastructure, Process, Referenceable, Server and Host.
* **[Area 1](Area-1-models.md)** - people, projects, communities, collaboration and feedback.
* **[Area 2](Area-2-models.md)** - specific types of assets such as data sources, APIs, analytics models, transformation functions and rule implementations that store and manage data.
* **[Area 3](Area-3-models.md)** - glossaries and semantic knowledge.
* **[Area 4](Area-4-models.md)** - governance definitions.
* **[Area 5](Area-5-models.md)** - models, schemas and reference data.
* **[Area 6](Area-6-models.md)** - metadata captured during automated analysis of assets.
* **[Area 7](Area-7-models.md)** - the structures for recording lineage.

## Index of open metadata types

### Attribute Type Definitions

These are the types defined for attributes (properties) in the open metadata types

* object 
* boolean
* byte
* char
* short
* int
* long
* float
* double
* biginteger
* bigdecimal
* string
* date
* map<string,string>
* map<string,boolean>
* map<string,int>
* map<string,long>
* map<string,object>
* array<string>
* array<int>

### Type Definitions

Below are the open metadata types in alphabetical order.
The link takes you to the UML model and description page for the type.


[AbstractConcept](0340-Dictionary.md)
[AcceptedAnswer](0150-Feedback.md)
[ActionAssignment](0137-Actions.md)
[Actions](0137-Actions.md)
[ActionTarget](0137-Actions.md)
[ActivityDescription](0340-Dictionary.md)
[ActivityType](0340-Dictionary.md)
[ActorProfile](0110-Actors.md)
[AdjacentLocation](0025-Locations.md)
[AnalyticsModel](0285-Analytics-Development-Assets.md)
[AnalyticsModelExperiment](0285-Analytics-Development-Assets.md)
[AnalyticsModelExperimentParticipant](0285-Analytics-Development-Assets.md)
[AnalyticsModelRole](0285-Analytics-Development-Assets.md)
[AnalyticsEngine](0055-Data-Processing-Engines.md)
[AnalyticsProject](0285-Analytics-Development-Assets.md)
[Anchors](0010-Base-Model.md)
[Annotation](0610-Annotations.md)
[AnnotationStatus](0612-Annotation-Reviews.md)
[AnnotationExtension](0610-Annotations.md)
[AnnotationReview](0612-Annotation-Reviews.md)
[AnnotationReviewLink](0612-Annotation-Reviews.md)
[Antonym](0350-Related-Terms.md)
[APIEndpoint](0212-Deployed-APIs.md)
[APIHeader](0536-API-Schemas.md)
[APIManager](0050-Applications-and-Processes.md)
[APIOperation](0536-API-Schemas.md)
[APIOperations](0536-API-Schemas.md)
[APIParameterList](0536-API-Schemas.md)
[APIParameter](0536-API-Schemas.md)
[APIRequest](0536-API-Schemas.md)
[APIResponse](0536-API-Schemas.md)
[APISchemaType](0536-API-Schemas.md)
[Application](0050-Applications-and-Processes.md)
[ApplicationServer](0050-Applications-and-Processes.md)
[ApprovedPurpose](0485-Data-Processing-Purposes.md)
[ArrayDocumentType]()
[ArraySchemaType](0507-External-Schema-Type.md)
[Asset](0010-Base-Model.md)
[AssetDiscoveryReport](0605-Open-Discovery-Analysis-Reports.md)
[AssetLocation](0025-Locations.md)
[AssetManager](0056-Asset-Managers.md)
[AssetOrigin](0440-Organizational-Controls.md)
[AssetOwner](0445-Governance-Roles.md)
[AssetOwnership](0445-Governance-Roles.md)
[AssetOwnerType](0445-Governance-Roles.md)
[AssetSchemaType](0503-Asset-Schema.md)
[AssetZoneMembership](0424-Governance-Zones.md)
[AttachedComment](0150-Feedback.md)
[AttachedLike](0150-Feedback.md)
[AttachedNoteLog](0160-Notes.md)
[AttachedNoteLogEntry](0160-Notes.md)
[AttachedRating](0150-Feedback.md)
[AttachedTag](0150-Feedback.md)
[AttributeForSchema](0505-Schema-Attributes.md)
[AuditLog](0455-Exception-Management.md)
[AuditLogFile](0455-Exception-Management.md)
[AvroFile](0220-Files-and-Folders.md)

[BoundedSchemaType](0507-External-Schema-Type.md)
[BoundedSchemaElementType](0507-External-Schema-Type.md)
[BuildTarget](0280-Software-Development-Assets.md)
[BusinessCapability](0440-Organizational-Controls.md)
[BusinessCapabilityControls](0440-Organizational-Controls.md)
[BusinessCapabilityType](0440-Organizational-Controls.md)
[BusinessImperative](0405-Governance-Drivers.md)
[BusinessSignificant](0760-Business-Lineage.md)


[CalculatedValue](0512-Derived-Schema-Elements.md)
[Campaign](0130-Projects.md)
[CanonicalVocabulary](0310-Glossary.md)
[CategoryAnchor](0320-Category-Hierarchy.md)
[CategoryHierarchyLink](0320-Category-Hierarchy.md)
[Certification](0482-Certifications.md)
[CertificationType](0482-Certifications.md)
[ClassificationAnnotation](0635-Classification-Discovery.md)
[ClassWord](0438-Naming-Standards.md)
[CloudPlatform](0090-Cloud-Platforms-and-Services.md)
[CloudProvider](0090-Cloud-Platforms-and-Services.md)
[CloudService](0090-Cloud-Platforms-and-Services.md)
[CloudTenant](0090-Cloud-Platforms-and-Services.md)
[CohortMember](0225-Metadata-Repositories.md)
[CohortMemberMetadataCollection](0225-Metadata-Repositories.md)
[CohortRegistryStore](0225-Metadata-Repositories.md)
[Collection](0120-Collections.md)
[CollectionMembership](0120-Collections.md)
[Comment](0150-Feedback.md)
[CommentType](0150-Feedback.md)
[Community](0140-Communities.md)
[CommunityMember](0140-Communities.md)
[CommunityMembership](0140-Communities.md)
[CommunityMembershipType](0140-Communities.md)
[ComplexSchemaType](0505-Schema-Attributes.png)
[ComponentOwner](0445-Governance-Roles.md)
[ConceptBead](0571-Concept-Models.md)
[ConceptBeadAttribute](0571-Concept-Models.md)
[ConceptBeadAttributeCoverage](0571-Concept-Models.md)
[ConceptBeadAttributeLink](0571-Concept-Models.md)
[ConceptBeadLink](0571-Concept-Models.md)
[ConceptBeadRelationshipEnd](0571-Concept-Models.md)
[ConceptModelAttributeCoverageCategory](0571-Concept-Models.md)
[ConceptModelDecoration](0571-Concept-Models.md)
[ConceptModelElement](0571-Concept-Models.md)
[Confidence](0422-Governance-Action-Classifications.md)
[ConfidenceLevel](0422-Governance-Action-Classifications.md)
[Confidentiality](0422-Governance-Action-Classifications.md)
[ConfidentialityLevel](0422-Governance-Action-Classifications.md)
[Connection](0201-Connectors-and-Connections.md)
[ConnectionConnectorType](0201-Connectors-and-Connections.md)
[ConnectionEndpoint](0201-Connectors-and-Connections.md)
[ConnectionToAsset](0205-Connection-Linkage.md)
[ConnectorCategory](0201-Connectors-and-Connections.md)
[ConnectorDirectory](0201-Connectors-and-Connections.md)
[ConnectorImplementationChoice](0201-Connectors-and-Connections.md)
[ConnectorType](0201-Connectors-and-Connections.md)
[ContactDetails](0110-Actors.md)
[ContactMethodType](0110-Actors.md)
[ContactThrough](0110-Actors.md)
[ContentCollectionManager](0221-Document-Stores.md)
[ContentManager](0221-Document-Stores.md)
[ContextDefinition](0360-Contexts.md)
[ContributionRecord](0112-People.md)
[ControlFlow](0750-Data-Passing.md)
[ControlledGlossaryTerm](0385-Controlled-Glossary-Development.md)
[ControlPoint](0460-Governance-Execution-Points.md)
[ControlPointDefinition](0460-Governance-Execution-Points.md)
[Criticality](0422-Governance-Action-Classifications.md)
[CriticalityLevel](0422-Governance-Action-Classifications.md)
[CrowdSourcingContribution](0155-Crowd-Sourcing.md)
[CrowdSourcingContributor](0155-Crowd-Sourcing.md)
[CrowdSourcingRole](0155-Crowd-Sourcing.md)
[CSVFile](0220-Files-and-Folders.md)
[CyberLocation](0025-Locations.md)

[Database](0224-Databases.md)
[DatabaseManager](0224-Databases.md)
[DatabaseServer](0224-Databases.md)
[DataClass](0540-Data-Classes.md)
[DataClassAnnotation](0625-Data-Class-Discovery.md)
[DataClassAssignment](0540-Data-Classes.md)
[DataClassAssignmentStatus](0540-Data-Classes.md)
[DataClassComposition](0540-Data-Classes.md)
[DataClassDefinition](0615-Schema-Extraction.md)
[DataClassHierarchy](0540-Data-Classes.md)
[DataContentForDataSet](0210-Data-Stores.md)
[DataField](0615-Schema-Extraction.md)
[DataFieldAnalysis](0617-Data-Field-Analysis.md)
[DataFieldAnnotation](0617-Data-Field-Analysis.md)
[DataFlow](0750-Data-Passing.md)
[DataFolder](0220-Files-and-Folders.md)
[DataFile](0220-Files-and-Folders.md)
[DataItemOwner](0445-Governance-Roles.md)
[DataItemSortOrder](0505-Schema-Attributes.md)
[DataMeasurementLevel](0525-Process-Variables.md)
[DataMovementEngine](0055-Data-Processing-Engines.md)
[DataProcessingAction](0485-Data-Processing-Purposes.md)
[DataProcessingDescription](0485-Data-Processing-Purposes.md)
[DataProcessingPurpose](0485-Data-Processing-Purposes.md)
[DataProcessingSpecification](0485-Data-Processing-Purposes.md)
[DataProcessingTarget](0485-Data-Processing-Purposes.md)
[DataProfileAnnotation](0620-Data-Profiling.md)
[DataProfileLogAnnotation](0620-Data-Profiling.md)
[DataProfileLogFile](0620-Data-Profiling.md)
[DataSet](0010-Base-Model.md)
[DataSourceMeasurementAnnotation](0660-Data-Source-Measurements.md)
[DataSourcePhysicalStatusAnnotation](0660-Data-Source-Measurements.md)
[DataStore](0210-Data-Stores.md)
[DataStoreEncoding](0210-Data-Stores.md)
[DataValue](0340-Dictionary.md)
[DataVirtualizationEngine](0055-Data-Processing-Engines.md)
[DependentSoftwareComponent](0282-Released-Software-Components.md)
[DeployedAPI](0212-Deployed-APIs.md)
[DeployedAnalyticalComponent](0265-Analytics-Assets.md)
[DeployedConnector](0215-Software-Components.md)
[DeployedDatabaseSchema](0224-Databases.md)
[DeployedReport](0239-Reports.md)
[DeployedSoftwareComponent](0215-Software-Components.md)
[DeployedVirtualContainer](0035-Complex-Hosts.md)
[DerivedRelationalColumn](0534-Relational-Schemas.md)
[DerivedSchemaAttribute](0512-Derived-Schema-Elements.md)
[DerivedSchemaTypeQueryTarget](0512-Derived-Schema-Elements.md)
[DesignModel](0566-Design-Model-Organization.md)
[DesignModelElement](0565-Design-Model-Elements.md)
[DesignModelElementOwnership](0566-Design-Model-Organization.md)
[DesignModelGroup](0566-Design-Model-Organization.md)
[DesignModelGroupHierarchy](0566-Design-Model-Organization.md)
[DesignModelGroupMembership](0566-Design-Model-Organization.md)
[DesignModelGroupOwnership](0566-Design-Model-Organization.md)
[DesignModelElementsInScope](0568-Design-Model-Scoping.md)
[DesignModelImplementation](0568-Design-Model-Scoping.md)
[DesignModelScope](0568-Design-Model-Scoping.md)
[DesignPattern](0595-Design-Patterns.md)
[DetailedProcessingActions](0485-Data-Processing-Purposes.md)
[DigitalService](0710-Digital-Service.md)
[DigitalServiceDependency](0710-Digital-Service.md)
[DigitalServiceDesign](0740-Solution-Blueprints.md)
[DigitalServiceImplementation](0717-Digital-Service-Implementation.md)
[DigitalServiceManagement](0715-Digital-Service-Ownership.md)
[DigitalServiceManager](0715-Digital-Service-Ownership.md)
[DigitalServiceOperator](0715-Digital-Service-Ownership.md)
[DigitalSupport](0715-Digital-Service-Ownership.md)
[DiscoveredAnnotation](0610-Annotations.md)
[DiscoveredDataField](0615-Schema-Extraction.md)
[DiscoveredNestedDataField](0615-Schema-Extraction.md)
[DiscoveryEngineReport](0605-Open-Discovery-Analysis-Reports.md)
[DiscoveryInvocationReport](0605-Open-Discovery-Analysis-Reports.md)
[DiscoveryRequestStatus](0605-Open-Discovery-Analysis-Reports.md)
[DiscoveryServiceRequestStatus](0605-Open-Discovery-Analysis-Reports.md)
[DisplayDataContainer](0537-Display-Schemas.md)
[DisplayDataField](0537-Display-Schemas.md)
[DisplayDataSchemaType](0537-Display-Schemas.md)
[DivergentAttachmentAnnotation](0655-Asset-Deduplication.md)
[DivergentAttachmentClassificationAnnotation](0655-Asset-Deduplication.md)
[DivergentAttachmentRelationshipAnnotation](0655-Asset-Deduplication.md)
[DivergentAttachmentValueAnnotation](0655-Asset-Deduplication.md)
[DivergentClassificationAnnotation](0655-Asset-Deduplication.md)
[DivergentDuplicateAnnotation](0655-Asset-Deduplication.md)
[DivergentRelationshipAnnotation](0655-Asset-Deduplication.md)
[DivergentValueAnnotation](0655-Asset-Deduplication.md)
[Document](0221-Document-Stores.md)
[DocumentSchemaType](0531-Document-Schemas.md)
[DocumentSchemaAttribute](0531-Document-Schemas.md)
[DocumentStore](0221-Document-Stores.md)
[DuplicateType](0465-Duplicate-Processing.md)

[ElementSupplement](0395-Supplementary-Properties.md)
[EmbeddedConnection](0205-Connection-Linkage.md)
[EmbeddedProcess](0215-Software-Components.md)
[Endianness](0030-Hosts-and-Platforms.md)
[Endpoint](0040-Software-Servers.md)
[EnforcementPoint](0460-Governance-Execution-Points.md)
[EnforcementPointDefinition](0460-Governance-Execution-Points.md)
[Engine](0055-Data-Processing-Engines.md)
[EngineHostingService](0057-Integration-Capabilities.md)
[EnumSchemaType](0501-Schema-Elements.md)
[EnterpriseAccessLayer](0225-Metadata-Repositories.md)
[EventBroker](0050-Applications-and-Processes.md)
[EventSchemaAttribute](0535-Event-Schemas.md)
[EventSet](0535-Event-Schemas.md)
[EventType](0535-Event-Schemas.md)
[EventTypeList](0535-Event-Schemas.md)
[ExceptionBacklog](0455-Exception-Management.md)
[ExceptionLogFile](0455-Exception-Management.md)
[ExecutionPointDefinition](0460-Governance-Execution-Points.md)
[ExecutionPointUse](0460-Governance-Execution-Points.md)
[ExternalGlossaryLink](0310-Glossary.md)
[ExternalId](0017-External-Identifiers.md)
[ExternalIdLink](0017-External-Identifiers.md)
[ExternalIdScope](0017-External-Identifiers.md)
[ExternallySourcedGlossary](0310-Glossary.md)
[ExternalReference](0015-Linked-Media-Types.md)
[ExternalReferenceLink](0015-Linked-Media-Types.md)
[ExternalSchemaType](0507-External-Schema-Type.md)

[FileFolder](0220-Files-and-Folders.md)
[FileManager](0220-Files-and-Folders.md)
[FileSystem](0220-Files-and-Folders.md)
[FixedLocation](0025-Locations.md)
[Folder](0120-Collections.md)
[FolderHierarchy](0220-Files-and-Folders.md)
[ForeignKey](0534-Relational-Schemas.md)
[Form](0239-Reports.md)

[Glossary](0310-Glossary.md)
[GlossaryCategory](0320-Category-Hierarchy.md)
[GlossaryProject](0390-Glossary-Projects.md)
[GlossaryTerm](0350-Related-Terms.md)
[GovernanceAction](0463-Governance-Actions.md)
[GovernanceActionExecutor](0463-Governance-Actions.md)
[GovernanceActionEngine](0461-Governance-Engines.md)
[GovernanceActionFlow](0462-Governance-Action-Types.md)
[GovernanceActionProcess](0462-Governance-Action-Types.md)
[GovernanceActionRequestSource](0463-Governance-Actions.md)
[GovernanceActionService](0461-Governance-Engines.md)
[GovernanceActionStatus](0463-Governance-Actions.md)
[GovernanceActionType](0462-Governance-Action-Types.md)
[GovernanceActionTypeExecutor](0462-Governance-Action-Types.md)
[GovernanceActionTypeUse](0463-Governance-Actions.md)
[GovernanceApproach](0415-Governance-Responses.md)
[GovernanceClassificationLevel](0421-Governance-Classification-Levels.md)
[GovernanceClassificationSet](0421-Governance-Classification-Levels.md)
[GovernanceClassificationStatus](0422-Governance-Action-Classifications.md)
[GovernanceConfidentialityLevel](0421-Governance-Classification-Levels.md)
[GovernanceControl](0420-Governance-Controls.md)
[GovernanceControlLink](0420-Governance-Controls.md)
[GovernanceConfidentialityLevel]()
[GovernanceDomain](0401-Governance-Definitions.md)
[GovernanceDomainDescription](0401-Governance-Definitions.md)
[GovernanceDomainSet](0401-Governance-Definitions.md)
[GovernanceDaemon](0455-Exception-Management.md)
[GovernanceDefinition](0401-Governance-Definitions.md)
[GovernanceDefinitionMetric](0450-Governance-Rollout.md)
[GovernanceDefinitionScope](0401-Governance-Definitions.md)
[GovernanceDriver](0405-Governance-Drivers.md)
[GovernanceDriverLink](0405-Governance-Drivers.md)
[GovernanceEngine](0461-Governance-Engines.md)
[GovernanceImplementation](0420-Governance-Controls.md)
[GovernanceMeasurements](0450-Governance-Rollout.md)
[GovernanceMeasurementsDataSet](0450-Governance-Rollout.md)
[GovernanceMetric](0450-Governance-Rollout.md)
[GovernanceObligation](0415-Governance-Responses.md)
[GovernanceOfficer](0445-Governance-Roles.md)
[GovernancePolicy](0415-Governance-Responses.md)
[GovernancePolicyLink](0415-Governance-Responses.md)
[GovernancePrinciple](0415-Governance-Responses.md)
[GovernanceProcedure](0440-Organizational-Controls.md)
[GovernanceProcess](0430-Technical-Controls.md)
[GovernanceProcessImplementation](0430-Technical-Controls.md)
[GovernanceProject](0417-Governance-Projects.md)
[GovernanceResponse](0420-Governance-Controls.md)
[GovernanceResponsibility](0440-Organizational-Controls.md)
[GovernanceResponsibilityAssignment](0445-Governance-Roles.md)
[GovernanceResults](0450-Governance-Rollout.md)
[GovernanceRole](0445-Governance-Roles.md)
[GovernanceRoleAssignment](0445-Governance-Roles.md)
[GovernanceRule](0430-Technical-Controls.md)
[GovernanceRuleImplementation](0430-Technical-Controls.md)
[GovernanceService](0461-Governance-Engines.md)
[GovernanceStrategy](0405-Governance-Drivers.md)
[GovernanceZone](0424-Governance-Zones.md)
[GovernedBy](0401-Governance-Definitions.md)
[GraphEdge](0533-Graph-Schemas.md)
[GraphEdgeLink](0533-Graph-Schemas.md)
[GraphSchemaType](0533-Graph-Schemas.md)
[GraphStore](0222-Graph-Stores.md)
[GraphVertex](0533-Graph-Schemas.md)
[GroupedMedia](0221-Document-Stores.md)

[Host](0030-Hosts-and-Platforms.md)
[HostCluster](0035-Complex-Hosts.md)
[HostClusterMember](0035-Complex-Hosts.md)
[HostNetwork](0070-Networks-and-Gateways.md)
[HostOperatingPlatform](0030-Hosts-and-Platforms.md)


[Impact](0422-Governance-Action-Classifications.md)
[ImpactedResource](0470-Incident-Reporting.md)
[ImpactSeverity](0422-Governance-Action-Classifications.md)
[ImplementationLocation](0280-Software-Development-Assets.md)
[ImplementationSnippet](0504-Implementation-Snippets.md)
[IncidentClassifier](0470-Incident-Reporting.md)
[IncidentClassifierSet](0470-Incident-Reporting.md)
[IncidentDependency](0470-Incident-Reporting.md)
[IncidentOriginator](0470-Incident-Reporting.md)
[IncidentReport](0470-Incident-Reporting.md)
[IncidentReportStatus](0470-Incident-Reporting.md)
[InformalTag](0150-Feedback.md)
[InformationSupplyChain](0720-Information-Supply-Chains.md)
[InformationSupplyChainComposition](0720-Information-Supply-Chains.md)
[InformationSupplyChainImplementation](0720-Information-Supply-Chains.md)
[InformationSupplyChainSegment](0720-Information-Supply-Chains.md)
[InformationView](0235-Information-View.md)
[Infrastructure](0010-Base-Model.md)
[InstanceMetadata](0550-Instance-Metadata.md)
[ISARelationship](0350-Related-Terms.md)
[IsATypeOfRelationship](0380-Spine-Objects.md)
[ITInfrastructure](0030-Hosts-and-Platforms.md)
[ITProfile](0117-IT-Profiles.md)


[JSONFile](0220-Files-and-Folders.md)

[KeyPattern](0017-External-Identifiers.md)
[KeystoreFile](0227-Keystores.md)
[KeyStoreCollection](0227-Keystores.md)
[KnownDuplicate](0465-Duplicate-Processing.md)
[KnownDuplicateLink](0465-Duplicate-Processing.md)

[LastAttachment](0011-Managing-Referenceables.md)
[LastAttachmentLink](0011-Managing-Referenceables.md)
[LatestChange](0011-Managing-Referenceables.md)
[LatestChangeAction](0011-Managing-Referenceables.md)
[LatestChangeTarget](0011-Managing-Referenceables.md)
[LibraryCategoryReference](0320-Category-Hierarchy.md)
[LibraryTermReference](0330-Terms.md)
[License](0481-Licenses.md)
[LicenseType](0481-Licenses.md)
[Like](0150-Feedback.md)
[LineageMapping](0770-Lineage-Mapping.md)
[LinkedExternalSchemaType](0507-External-Schema-Type.md)
[LinkedFile](0220-Files-and-Folders.md)
[LinkedMedia](0221-Document-Stores.md)
[LinkedType](0512-Derived-Schema-Elements.md)
[ListenerInterface](0212-Deployed-APIs.md)
[LiteralSchemaType](0501-Schema-Elements.md)
[Location](0025-Locations.md)
[LogFile](0223-Events-and-Logs.md)


[MapDocumentType](0531-Document-Schemas.md)
[MapFromElementType](0511-Map-Schema-Elements.md)
[MapSchemaType](0511-Map-Schema-Elements.md)
[MapToElementType](0511-Map-Schema-Elements.md)
[MasterDataManager](0056-Asset-Managers.md)
[MediaCollection](0221-Document-Stores.md)
[MediaFile](0221-Document-Stores.md)
[MediaReference](0015-Linked-Media-Types.md)
[MediaType](0015-Linked-Media-Types.md)
[MediaUsage](0015-Linked-Media-Types.md)
[Meeting](0135-Meetings.md)
[Meetings](0135-Meetings.md)
[Memento](0010-Base-Model.md)
[MetadataAccessService](0057-Integration-Capabilities.md)
[MetadataCohortPeer](0225-Metadata-Repositories.md)
[MetadataCollection](0225-Metadata-Repositories.md)
[MetadataIntegrationService](0057-Integration-Capabilities.md)
[MetadataRepository](0225-Metadata-Repositories.md)
[MetadataRepositoryCohort](0225-Metadata-Repositories.md)
[MetadataServer](0225-Metadata-Repositories.md)
[MetamodelInstance](0570-Metamodels.md)
[MeteringLog](0455-Exception-Management.md)
[MobileAsset](0025-Locations.md)
[Modifier](0438-Naming-Standards.md)
[MoreInformation](0019-More-Information.md)

[NamingConventionRule](0438-Naming-Standards.md)
[NamingStandardRule](0438-Naming-Standards.md)
[NamingStandardRuleSet](0438-Naming-Standards.md)
[NestedFile](0220-Files-and-Folders.md)
[NestedLocation](0025-Locations.md)
[NestedSchemaAttribute](0505-Schema-Attributes.md)
[Network](0070-Networks-and-Gateways.md)
[NetworkGateway](0070-Networks-and-Gateways.md)
[NetworkGatewayLink](0070-Networks-and-Gateways.md)
[NextGovernanceAction](0463-Governance-Actions.md)
[NextGovernanceActionType](0462-Governance-Action-Types.md)
[NoteEntry](0160-Notes.md)
[NoteLog](0160-Notes.md)
[NoteLogAuthor](0160-Notes.md)
[NoteLogAuthorship](0160-Notes.md)
[NotificationManager](0223-Events-and-Logs.md)

[ObjectAttribute](0532-Object-Schemas.md)
[ObjectIdentifier](0380-Spine-Objects.md)
[ObjectSchemaType](0532-Object-Schemas.md)
[OpenDiscoveryAnalysisReport](0605-Open-Discovery-Analysis-Reports.md)
[OpenDiscoveryEngine](0601-Open-Discovery-Engine.md)
[OpenDiscoveryService](0601-Open-Discovery-Engine.md)
[OpenDiscoveryPipeline](0601-Open-Discovery-Engine.md)
[OpenMetadataRoot](0010-Base-Model.md)
[OperationalStatus](0037-Software-Server-Platforms.md)
[OperatingPlatform](0030-Hosts-and-Platforms.md)
[OrderBy](0120-Collections.md)
[Organization](0440-Organizational-Controls.md)
[OrganizationalCapability](0420-Governance-Controls.md)
[OrganizationalControl](0420-Governance-Controls.md)
[Ownership](0445-Governance-Roles.md)
[OwnerType](0445-Governance-Roles.md)

[Peer](0112-People.md)
[PermittedProcessing](0485-Data-Processing-Purposes.md)
[PermittedSynchronization](0017-External-Identifiers.md)
[Person](0112-People.md)
[PersonalContribution](0112-People.md)
[PersonRole](0112-People.md)
[PersonRoleAppointment](0112-People.md)
[PolicyAdministrationPoint](0435-Policy-Management-Capabilities.md)
[PolicyDecisionPoint](0435-Policy-Management-Capabilities.md)
[PolicyEnforcementPoint](0435-Policy-Management-Capabilities.md)
[PolicyInformationPoint](0435-Policy-Management-Capabilities.md)
[PolicyRetrievalPoint](0435-Policy-Management-Capabilities.md)
[Port](0217-Ports.md)
[PortAlias](0217-Ports.md)
[PortDelegation](0217-Ports.md)
[PortImplementation](0217-Ports.md)
[PortSchema](0520-Process-Schemas.md)
[PortType](0217-Ports.md)
[PreferredTerm](0350-Related-Terms.md)
[PrimaryCategory](0335-Primary-Category.md)
[PrimaryKey](0534-Relational-Schemas.md)
[PrimeWord](0438-Naming-Standards.md)
[PrimitiveSchemaType](0501-Schema-Elements.md)
[Process](0010-Base-Model.md)
[ProcessCall](0750-Data-Passing.md)
[ProcessContainmentType](0215-Software-Components.md)
[ProcessInput](0750-Data-Passing.md)
[ProcessHierarchy](0215-Software-Components.md)
[ProcessOutput](0750-Data-Passing.md)
[ProcessPort](0217-Ports.md)
[ProcessVariable](0525-Process-Variables.md)
[ProcessVariableMapping](0525-Process-Variables.md)
[ProfileIdentity](0110-Actors.md)
[Project](0130-Projects.md)
[ProjectCharter](0442-Project-Charter.md)
[ProjectCharterLink](0442-Project-Charter.md)
[ProjectDependency](0130-Projects.md)
[ProjectHierarchy](0130-Projects.md)
[ProjectManager](0130-Projects.md)
[ProjectManagement](0130-Projects.md)
[ProjectScope](0130-Projects.md)
[ProjectTeam](0130-Projects.md)
[PropertyFacet](0020-Property-Facets.md)
[PublisherInterface](0212-Deployed-APIs.md)

[QualityAnnotation](0640-Quality-Scores.md)
[QueryDataContainer](0537-Display-Schemas.md)
[QueryDataField](0537-Display-Schemas.md)
[QuerySchemaType](0537-Display-Schemas.md)

[Rating](0150-Feedback.md)
[Regulation](0405-Governance-Drivers.md)
[RegulationArticle](0405-Governance-Drivers.md)
[RegulationCertificationType](0482-Certifications.md)
[Referenceable](0010-Base-Model.md)
[ReferenceableFacet](0020-Property-Facets.md)
[ReferenceCodeTable](0230-Code-Tables.md)
[ReferenceCodeMappingTable](0230-Code-Tables.md)
[ReferenceData](0545-Reference-Data.md)
[ReferenceValueAssignment](0545-Reference-Data.md)
[RelatedDesignPattern](0595-Design-Patterns.md)
[RelatedMedia](0015-Linked-Media-Types.md)
[RelatedKeyword](0012-Search-Keywords.md)
[RelatedTerm](0350-Related-Terms.md)
[RelationalColumn](0534-Relational-Schemas.md)
[RelationalColumnType](0534-Relational-Schemas.md)
[RelationalDBSchemaType](0534-Relational-Schemas.md)
[RelationalTableType](0534-Relational-Schemas.md)
[RelationalTable](0534-Relational-Schemas.md)
[RelationalView](0534-Relational-Schemas.md)
[RelationshipAdviceAnnotation](0650-Relationship-Discovery.md)
[RelationshipAnnotation](0650-Relationship-Discovery.md)
[ReplacementTerm](0350-Related-Terms.md)
[ReportingEngine](0055-Data-Processing-Engines.md)
[RepositoryProxy](0225-Metadata-Repositories.md)
[RequestForAction](0690-Request-for-Action.md)
[RequestResponseInterface](0212-Deployed-APIs.md)
[RequirementsLibrary](0280-Software-Development-Assets.md)
[ResponsibilityStaffContact](0445-Governance-Roles.md)
[ResourceList](0120-Collections.md)
[Retention](0422-Governance-Action-Classifications.md)
[RetentionBasis](0422-Governance-Action-Classifications.md)
[ReusableTechnique](0280-Software-Development-Assets.md)
[ReusableTechniqueUse](0280-Software-Development-Assets.md)
[RunnableSoftwareComponent](0282-Released-Software-Components.md)
[RuntimeForProcess](0050-Applications-and-Processes.md)

[SchemaAnalysisAnnotation](0615-Schema-Extraction.md)
[SchemaAttribute](0505-Schema-Attributes.png)
[SchemaAttributeDefinition](0615-Schema-Extraction.md)
[SchemaAttributeType](0505-Schema-Attributes.md)
[SchemaElement](0501-Schema-Elements.md)
[SchemaLinkElement](0512-Derived-Schema-Elements.md)
[SchemaLinkToType](0512-Derived-Schema-Elements.md)
[SchemaQueryImplementation](0512-Derived-Schema-Elements.md)
[SchemaType](0501-Schema-Elements.md)
[SchemaTypeChoice](0501-Schema-Elements.md)
[SchemaTypeDefinition](0615-Schema-Extraction.md)
[SchemaTypeOption](0501-Schema-Elements.md)
[SchemaTypeSnippet](0504-Implementation-Snippets.md)
[SchemaTypeImplementation](0504-Implementation-Snippets.md)
[SearchKeyword](0012-Search-Keywords.md)
[SearchKeywordLink](0012-Search-Keywords.md)
[SecureLocation](0025-Locations.md)
[SecurityTags](0423-Security-Tags.md)
[SemanticAnnotation](0630-Semantic-Discovery.md)
[SemanticAssignment](0370-Semantic-Assignment.md)
[ServerAssetUse](0045-Servers-and-Assets.md)
[ServerAssetUseType](0045-Servers-and-Assets.md)
[ServerEndpoint](0040-Software-Servers.md)
[Set](0120-Collections.md)
[SetDocumentType](0531-Document-Schemas.md)
[SetSchemaType](0507-External-Schema-Type.md)
[SimpleDocumentType](0531-Document-Schemas.md)
[SimpleSchemaType](0501-Schema-Elements.md)
[SoftwareComponent](0281-Software-Modules.md)
[SoftwareManifest](0282-Released-Software-Components.md)
[SoftwareModule](0281-Software-Modules.md)
[SoftwareModuleContent](0281-Software-Modules.md)
[SoftwareLibrary](0280-Software-Development-Assets.md)
[SoftwareServer](0040-Software-Servers.md)
[SoftwareServerCapability](0042-Software-Server-Capabilities.md)
[SoftwareServerDeployment](0040-Software-Servers.md)
[SoftwareServerPlatform](0037-Software-Server-Platforms.md)
[SoftwareServerPlatformDeployment](0037-Software-Server-Platforms.md)
[SoftwareServerSupportedCapability](0042-Software-Server-Capabilities.md)
[SoftwareService](0057-Integration-Capabilities.md)
[SolutionBlueprint](0740-Solution-Blueprints.md)
[SolutionBlueprintComposition](0740-Solution-Blueprints.md)
[SolutionComponent](0730-Solution-Components.md)
[SolutionComponentImplementation](0730-Solution-Components.md)
[SolutionComponentPort](0735-Solution-Ports-and-Wires.md)
[SolutionComposition](0730-Solution-Components.md)
[SolutionLinkingWire](0735-Solution-Ports-and-Wires.md)
[SolutionPort](0735-Solution-Ports-and-Wires.md)
[SolutionPortDelegation](0735-Solution-Ports-and-Wires.md)
[SolutionPortDirection](0735-Solution-Ports-and-Wires.md)
[SourceCodeFile](0280-Software-Development-Assets.md)
[SourceComponent](0282-Released-Software-Components.md)
[SourceControlLibrary](0280-Software-Development-Assets.md)
[SourcedFrom](0011-Managing-Referenceables.md)
[SpineObject](0380-Spine-Objects.md)
[SpineAttribute](0380-Spine-Objects.md)
[StarRating](0150-Feedback.md)
[StewardshipServer](0455-Exception-Management.md)
[StructDocumentType](0531-Document-Schemas.md)
[StructSchemaType](0505-Schema-Attributes.png)
[SubjectArea](0425-Subject-Areas.md)
[SubjectAreaDefinition](0425-Subject-Areas.md)
[SubjectAreaGovernance](0425-Subject-Areas.md)
[SubjectAreaHierarchy](0425-Subject-Areas.md)
[SubjectAreaOwner](0445-Governance-Roles.md)
[SubscriberList](0223-Events-and-Logs.md)
[SupplementaryProperties](0395-Supplementary-Properties.md)
[SupportedComponentVariable](0525-Process-Variables.md)
[SupportedDiscoveryService](0601-Open-Discovery-Engine.md)
[SupportedGovernanceService](0461-Governance-Engines.md)
[SupportedProcessVariable](0525-Process-Variables.md)
[SupportedVariableType](0525-Process-Variables.md)
[SuspectDuplicateAnnotation](0655-Asset-Deduplication.md)
[Synonym](0350-Related-Terms.md)


[TabularColumnType](0530-Tabular-Schemas.md)
[TabularColumn](0530-Tabular-Schemas.md)
[TabularFileColumn](0530-Tabular-Schemas.md)
[TabularSchemaType](0530-Tabular-Schemas.md)
[TargetForAction](0463-Governance-Actions.md)
[Task](0130-Projects.md)
[Taxonomy](0310-Glossary.md)
[Team](0115-Teams.md)
[TeamLeader](0115-Teams.md)
[TeamLeadership](0115-Teams.md)
[TeamMember](0115-Teams.md)
[TeamMembership](0115-Teams.md)
[TeamStructure](0115-Teams.md)
[TechnicalControl](0420-Governance-Controls.md)
[Template](0011-Managing-Referenceables.md)
[TermAnchor](0330-Terms.md)
[TermAssignmentStatus](0370-Semantic-Assignment.md)
[TermCategorization](0330-Terms.md)
[TermHASARelationship](0380-Spine-Objects.md)
[TermISATypeOFRelationship](0380-Spine-Objects.md)
[TermTYPEDBYRelationship](0380-Spine-Objects.md)
[TermRelationshipStatus](0330-Terms.md)
[Threat](0405-Governance-Drivers.md)
[ToDo](0137-Actions.md)
[ToDoSource](0137-Actions.md)
[ToDoStatus](0137-Actions.md)
[Topic](0223-Events-and-Logs.md)
[TopicSubscribers](0223-Events-and-Logs.md)
[TransientEmbeddedProcess](0215-Software-Components.md)
[Translation](0350-Related-Terms.md)
[TypeEmbeddedAttribute](0505-Schema-Attributes.md)

[UsedInContext](0360-Contexts.md)
[UserAccessDirectory](0056-Asset-Managers.md)
[UserIdentity](0110-Actors.md)
[UserProfileManager](0056-Asset-Managers.md)
[UserViewService](0057-Integration-Capabilities.md)

[ValidValue](0350-Related-Terms.md)
[ValidValueAssignment](0350-Related-Terms.md)
[ValidValueDefinition](0545-Reference-Data.md)
[ValidValueMember](0545-Reference-Data.md)
[ValidValuesImplementation](0545-Reference-Data.md)
[ValidValuesMapping](0545-Reference-Data.md)
[ValidValuesSet](0545-Reference-Data.md)
[ValueCategory](0525-Process-Variables.md)
[VerificationPoint](0460-Governance-Execution-Points.md)
[VerificationPointDefinition](0460-Governance-Execution-Points.md)
[VirtualContainer](0035-Complex-Hosts.md)
[VirtualConnection](0205-Connection-Linkage.md)

[Webserver](0050-Applications-and-Processes.md)
[WorkflowEngine](0055-Data-Processing-Engines.md)

[ZoneGovernance](0424-Governance-Zones.md)
[ZoneHierarchy](0424-Governance-Zones.md)


----

* Return to [Egeria Overview](../../../index.md).
* Got to the [Open Metadata Type definitions](../../../open-metadata-resources/open-metadata-archives/open-metadata-types).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.