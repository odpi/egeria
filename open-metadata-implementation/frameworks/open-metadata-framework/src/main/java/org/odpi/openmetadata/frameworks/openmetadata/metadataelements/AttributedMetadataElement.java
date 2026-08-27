/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Description of an open metadata element (entity instance) retrieved from the open metadata repositories
 * that is expected to have external references and other elements attached.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AttributedMetadataElement implements MetadataElement
{
    private ElementHeader elementHeader = null;

    /*
     * Area 0
     */
    private List<RelatedMetadataElementSummary> sampleData                    = null; // SampleData (0010)
    private List<RelatedMetadataElementSummary> sourcesOfSampleData           = null; // SampleData (0010)
    private List<RelatedMetadataElementSummary> templateCreatedElements       = null; // SourcedFrom (0011)
    private RelatedMetadataElementSummary       sourcedFromTemplate           = null; // SourcedFrom (0011)
    private List<RelatedMetadataElementSummary> templatesForCataloguing       = null; // CatalogTemplate (0011)
    private List<RelatedMetadataElementSummary> templateUses                  = null; // CatalogTemplate (0011)
    private RelatedMetadataElementSummary       actionSource                  = null; // ActionRequester (0013)
    private List<RelatedMetadataElementSummary> requestedActions              = null; // ActionRequester (0013)
    private List<RelatedMetadataElementSummary> actionCause                   = null; // Actions (0013)
    private List<RelatedMetadataElementSummary> relatedActions                = null; // Actions (0013)
    private List<RelatedMetadataElementSummary> actionTargets                 = null; // ActionTarget (0013)
    private List<RelatedMetadataElementSummary> actionsForTarget              = null; // ActionTarget (0013)
    private List<RelatedMetadataElementSummary> searchKeywords                = null; // SearchKeywordLink (0012)
    private List<RelatedMetadataElementSummary> keywordElements               = null; // SearchKeywordLink (0012)
    private List<RelatedMetadataElementSummary> externalReferences            = null; // ExternalReferenceLink (0014)
    private List<RelatedMetadataElementSummary> referencingElements           = null; // ExternalReferenceLink (0014)
    private List<RelatedMetadataElementSummary> alsoKnownAs                   = null; // ExternalIdLink (0017)
    private List<RelatedMetadataElementSummary> equivalentElements            = null; // ExternalIdLink (0017)
    private List<RelatedMetadataElementSummary> resourceList                  = null; // ResourceList (0019)
    private List<RelatedMetadataElementSummary> resourceListUsers             = null; // ResourceList (0019)
    private List<RelatedMetadataElementSummary> providesMoreInformation       = null; // MoreInformation (0019)
    private List<RelatedMetadataElementSummary> describes                     = null; // MoreInformation (0019)
    private List<RelatedMetadataElementSummary> propertyFacets                = null; // ReferenceableFacet (0020)
    private List<RelatedMetadataElementSummary> facetedElements               = null; // ReferenceableFacet (0020)
    private List<RelatedMetadataElementSummary> memberOfCollections           = null; // CollectionMembership (0021)
    private List<RelatedMetadataElementSummary> collectionMembers             = null; // CollectionMembership (0021)

    private List<RelatedMetadataElementSummary> knownLocations    = null; // KnownLocation (0025)
    private List<RelatedMetadataElementSummary> localResources    = null; // KnownLocation (0025)
    private List<RelatedMetadataElementSummary> peerLocations     = null; // AdjacentLocation (0025)
    private List<RelatedMetadataElementSummary> groupingLocations = null; // NestedLocation (0025)
    private List<RelatedMetadataElementSummary> nestedLocations   = null; // NestedLocation (0025)


    private List<RelatedMetadataElementSummary> serverEndpoints                  = null; // ServerEndpoint (0026)
    private RelatedMetadataElementSummary       serverForEndpoint                = null; // ServerEndpoint (0026)
    private List<RelatedMetadataElementSummary> hostedITAssets                   = null; // DeployedOn (0035)
    private List<RelatedMetadataElementSummary> deployedTo                       = null; // DeployedOn (0035)
    private List<RelatedMetadataElementSummary> storageVolumes                   = null; // AttachedStorage (0036)
    private List<RelatedMetadataElementSummary> providesStorageFor               = null; // AttachedStorage (0036)
    private List<RelatedMetadataElementSummary> managesStorageFor                = null; // StoredOn (0036)
    private List<RelatedMetadataElementSummary> storedOn                         = null; // StoredOn (0036)
    private List<RelatedMetadataElementSummary> installedOn                      = null; // OperatingPlatformUse (0030)
    private List<RelatedMetadataElementSummary> operatingPlatforms               = null; // OperatingPlatformUse (0030)
    private List<RelatedMetadataElementSummary> includesSoftwarePackages         = null; // OperatingPlatformManifest (0030)
    private List<RelatedMetadataElementSummary> packagedInOperatingPlatforms     = null; // OperatingPlatformManifest (0030)
    private List<RelatedMetadataElementSummary> dependsOnSoftwarePackages        = null; // SoftwarePackageDependency (0030)
    private List<RelatedMetadataElementSummary> runningWithAsset                 = null; // SoftwarePackageDependency (0030)
    private List<RelatedMetadataElementSummary> managedHosts                     = null; // HostClusterMember (0035)
    private List<RelatedMetadataElementSummary> hostCluster                      = null; // HostClusterMember (0035)
    private List<RelatedMetadataElementSummary> resultsStoredIn                  = null; // SmartQuery (0725)
    private List<RelatedMetadataElementSummary> populatedUsingQuery              = null; // SmartQuery (0725)
    private List<RelatedMetadataElementSummary> consumedByCapabilities           = null; // CapabilityAssetUse (0045)
    private List<RelatedMetadataElementSummary> capabilityConsumedAssets         = null; // CapabilityAssetUse (0045)
    private List<RelatedMetadataElementSummary> capabilities                     = null; // SupportedSoftwareCapability (0042)
    private List<RelatedMetadataElementSummary> hostedByDeployedITInfrastructure = null; // SupportedSoftwareCapability (0042)
    private List<RelatedMetadataElementSummary> cohortMembership                 = null; // MetadataCohortPeer (0057)
    private List<RelatedMetadataElementSummary> registeredWithCohorts            = null; // MetadataCohortPeer (0057)
    private List<RelatedMetadataElementSummary> visibleEndpoints                 = null; // VisibleEndpoint (0070)
    private List<RelatedMetadataElementSummary> visibleInNetworks                = null; // VisibleEndpoint (0070)

    /*
     * Area 1
     */

    private RelatedMetadataElementSummary       userProfile    = null; // ProfileIdentity (0110)
    private List<RelatedMetadataElementSummary> userIdentities = null; // ProfileIdentity (0110)

    private List<RelatedMetadataElementSummary> contactDetails = null; // ContactThrough (0111)
    private List<RelatedMetadataElementSummary> contacts       = null; // ContactThrough (0111)

    private List<RelatedMetadataElementSummary> myFollowers = null; // Peer (0112)
    private List<RelatedMetadataElementSummary> myPeers = null; // Peer (0112)

    private RelatedMetadataElementSummary       superTeam = null; // TeamStructure (0115)
    private List<RelatedMetadataElementSummary> subTeams  = null; // TeamStructure (0115)

    private List<RelatedMetadataElementSummary> profilesForAsset   = null; // ITInfrastructureProfile (0117)
    private List<RelatedMetadataElementSummary> assetsUsingProfile = null; // ITInfrastructureProfile (0117)

    private List<RelatedMetadataElementSummary> performsRoles  = null; // PersonRoleAppointment, TeamRoleAppointment, ITProfileRoleAppointment (0118)
    private List<RelatedMetadataElementSummary> rolePerformers = null; // PersonRoleAppointment, TeamRoleAppointment, ITProfileRoleAppointment (0118)

    private List<RelatedMetadataElementSummary> relevantToScopes = null; // ScopedBy (0120)
    private List<RelatedMetadataElementSummary> scopedElements   = null; // ScopedBy (0120)
    private List<RelatedMetadataElementSummary> assignmentScope           = null; // AssignmentScope (0120)
    private List<RelatedMetadataElementSummary> assignedActors            = null; // AssignmentScope (0120)

    private RelatedMetadataElementSummary contributionRecord = null; // ContributionRecord (0125)
    private RelatedMetadataElementSummary contributorProfile = null; // ContributionRecord (0125)

    private List<RelatedMetadataElementSummary> dependentProjects = null; // ProjectDependency (0130)
    private List<RelatedMetadataElementSummary> dependsOnProjects = null; // ProjectDependency (0130)
    private List<RelatedMetadataElementSummary> managingProjects  = null; // ProjectHierarchy (0130)
    private List<RelatedMetadataElementSummary> managedProjects   = null; // ProjectHierarchy (0130)

    private List<RelatedMetadataElementSummary> likes              = null; // AttachedLike (0150)
    private RelatedMetadataElementSummary       likedElement       = null; // AttachedLike (0150)
    private List<RelatedMetadataElementSummary> informalTags       = null; // AttachedTag (0150)
    private List<RelatedMetadataElementSummary> taggedElements     = null; // AttachedTag (0150)
    private List<RelatedMetadataElementSummary> reviews            = null; // AttachedRating (0150)
    private RelatedMetadataElementSummary       reviewedElement    = null; // AttachedRating (0150)
    private List<RelatedMetadataElementSummary> comments           = null; // AttachedComment (0150)
    private RelatedMetadataElementSummary       commentedOnElement = null; // AttachedComment (0150)
    private List<RelatedMetadataElementSummary> answeredQuestions  = null; // AcceptedAnswer (0150)
    private List<RelatedMetadataElementSummary> acceptedAnswers    = null; // AcceptedAnswer (0150)

    private List<RelatedMetadataElementSummary> crowdSourcingContributors = null; // CrowdSourcingContribution (0155)
    private List<RelatedMetadataElementSummary> crowdSourcedContributions = null; // CrowdSourcingContribution (0155)


    private List<RelatedMetadataElementSummary> noteLogSubjects   = null; // AttachedNoteLog (0160)
    private List<RelatedMetadataElementSummary> noteLogs          = null; // AttachedNoteLog (0160)
    private List<RelatedMetadataElementSummary> presentInNoteLogs = null; // AttachedNoteLogEntry (0160)
    private List<RelatedMetadataElementSummary> noteLogEntries    = null; // AttachedNoteLogEntry (0160)


    /*
     * Area 2
     */

    private List<RelatedMetadataElementSummary> connections         = null; // ResourceConnection (0205), ConnectorConnectionType, ConnectToEndpoint (0201)
    private RelatedMetadataElementSummary       connectorType       = null; // ConnectorConnectionType (0201)
    private RelatedMetadataElementSummary       endpoint            = null; // ConnectToEndpoint (0201)
    private List<RelatedMetadataElementSummary> connectedResources  = null; // ResourceConnection (0205)
    private List<RelatedMetadataElementSummary> embeddedConnections = null; // EmbeddedConnection (0205)
    private List<RelatedMetadataElementSummary> parentConnections   = null; // EmbeddedConnection (0205)


    private List<RelatedMetadataElementSummary> supportedDataSets             = null; // DataSetContent (0210)
    private List<RelatedMetadataElementSummary> dataSetContent                = null; // DataSetContent (0210)
    private List<RelatedMetadataElementSummary> apiEndpoints                  = null; // APIEndpoint (0212)
    private List<RelatedMetadataElementSummary> supportedAPIs                 = null; // APIEndpoint (0212)
    private List<RelatedMetadataElementSummary> parentProcesses               = null; // ProcessHierarchy (0215)
    private List<RelatedMetadataElementSummary> childProcesses                = null; // ProcessHierarchy (0215)
    private List<RelatedMetadataElementSummary> ports                         = null; // ProcessPort (0217)
    private List<RelatedMetadataElementSummary> portOwningProcesses           = null; // ProcessPort (0217)
    private List<RelatedMetadataElementSummary> portDelegatingFrom            = null; // PortDelegation (0217)
    private RelatedMetadataElementSummary       portDelegatingTo              = null; // PortDelegation (0217)
    private RelatedMetadataElementSummary       homeFolder                    = null; // NestedFile (0220)
    private List<RelatedMetadataElementSummary> nestedFiles                   = null; // NestedFile (0220)
    private List<RelatedMetadataElementSummary> linkedFiles                   = null; // LinkedFile (0220)
    private List<RelatedMetadataElementSummary> linkedFolders                 = null; // LinkedFile (0220)
    private RelatedMetadataElementSummary       parentFolder                  = null; // FolderHierarchy (0220)
    private List<RelatedMetadataElementSummary> nestedFolders                 = null; // FolderHierarchy (0220)
    private List<RelatedMetadataElementSummary> linkedMediaFiles              = null; // LinkedMedia (0221)
    private List<RelatedMetadataElementSummary> associatedLogs                = null; // AssociatedLog (0223)
    private List<RelatedMetadataElementSummary> associatedLogSubjects         = null; // AssociatedLog (0223)
    private RelatedMetadataElementSummary       archiveContents               = null; // ArchiveContents (0226)
    private List<RelatedMetadataElementSummary> packagedInArchiveFiles        = null; // ArchiveContents (0226)

    private RelatedMetadataElementSummary       reportOriginator               = null; // ReportOrigin (0239)
    private List<RelatedMetadataElementSummary> generatedReports               = null; // ReportOrigin (0239)
    private List<RelatedMetadataElementSummary> reportSubjects                 = null; // ReportSubject (0239)
    private List<RelatedMetadataElementSummary> reports                        = null; // ReportSubject (0239)
    private List<RelatedMetadataElementSummary> priorReports                   = null; // ReportDependency (0239)
    private List<RelatedMetadataElementSummary> followOnReports                = null; // ReportDependency (0239)


    /*
     * Area 3
     */

    private List<RelatedMetadataElementSummary> relatedTerms                      = null; // various (0350)
    private List<RelatedMetadataElementSummary> usedInContexts                    = null; // UsedInContext (0360)
    private List<RelatedMetadataElementSummary> contextRelevantTerms              = null; // UsedInContext (0360)
    private List<RelatedMetadataElementSummary> meaningForDataElements            = null; // SemanticAssignment
    private List<RelatedMetadataElementSummary> meanings                          = null; // SemanticAssignment
    private List<RelatedMetadataElementSummary> semanticDefinitions               = null; // SemanticDefinition
    private List<RelatedMetadataElementSummary> semanticallyAssociatedDefinitions = null; // SemanticDefinition

    private List<RelatedMetadataElementSummary> supplementaryProperties = null;
    private RelatedMetadataElementSummary       supplementsElement      = null;

    /*
     * Area 4
     */
    private List<RelatedMetadataElementSummary> governedBy       = null; // GovernedBy (0401)
    private List<RelatedMetadataElementSummary> governedElements = null; // GovernedBy (0401)

    private List<RelatedMetadataElementSummary> peerGovernanceDefinitions       = null;
    private List<RelatedMetadataElementSummary> supportedGovernanceDefinitions  = null;
    private List<RelatedMetadataElementSummary> supportingGovernanceDefinitions = null;

    private List<RelatedMetadataElementSummary> configuredInSecurityCollections = null; // UserAccount (0423)
    private List<RelatedMetadataElementSummary> userAccounts                    = null; // UserAccount (0423)

    private List<RelatedMetadataElementSummary> definedInSecretsCollection = null; // ResourcePermissions (0423)
    private List<RelatedMetadataElementSummary> securityAccessControls     = null; // ResourcePermissions (0423)

    private List<RelatedMetadataElementSummary> listedInSecretsCollection = null; // SecretsCollectionSecurityList (0423)
    private List<RelatedMetadataElementSummary> securityLists             = null; // SecretsCollectionSecurityList (0423)

    private List<RelatedMetadataElementSummary> usedInAccessControls    = null;     // AssociatedSecurityGroup (0423)
    private List<RelatedMetadataElementSummary> associatedSecurityLists = null; // AssociatedSecurityGroup (0423)

    private RelatedMetadataElementSummary       inheritsFromZone = null; // ZoneHierarchy (0424)
    private List<RelatedMetadataElementSummary> controlsZones = null; // ZoneHierarchy (0424)

    private RelatedMetadataElementSummary       broaderSubjectArea = null; // SubjectAreaHierarchy (0425)
    private List<RelatedMetadataElementSummary> nestedSubjectAreas = null; // SubjectAreaHierarchy (0425)

    private List<RelatedMetadataElementSummary> metrics      = null; // GovernanceResults (0450)
    private List<RelatedMetadataElementSummary> measurements = null; // GovernanceResults (0450)

    private List<RelatedMetadataElementSummary> monitoredThrough             = null; // MonitoredResource (0451)
    private List<RelatedMetadataElementSummary> monitoredResources           = null; // MonitoredResource (0451)
    private List<RelatedMetadataElementSummary> interestingNotificationTypes = null; // NotificationSubscriber (0451)
    private List<RelatedMetadataElementSummary> subscribers                  = null; // NotificationSubscriber (0451)

    private List<RelatedMetadataElementSummary> excludedFromRequirements    = null; // Exception (0455)
    private List<RelatedMetadataElementSummary> exceptions                  = null; // Exception (0455)

    private List<RelatedMetadataElementSummary> calledFromGovernanceEngines = null; // SupportedGovernanceService (0461)
    private List<RelatedMetadataElementSummary> supportedGovernanceServices = null; // SupportedGovernanceService (0461)

    private List<RelatedMetadataElementSummary> associatedGovernanceActions = null; // TargetForGovernanceAction (0462)
    private List<RelatedMetadataElementSummary> predefinedTargetForAction   = null; // TargetForGovernanceAction (0462)
    private List<RelatedMetadataElementSummary> triggeredFrom               = null; // GovernanceActionProcessFlow (0462)
    private RelatedMetadataElementSummary       firstStep                   = null; // GovernanceActionProcessFlow (0462)
    private List<RelatedMetadataElementSummary> dependedOnProcessSteps      = null; // NextGovernanceActionProcessStep (0462)
    private List<RelatedMetadataElementSummary> followOnProcessSteps        = null; // NextGovernanceActionProcessStep (0462)
    private List<RelatedMetadataElementSummary> supportsGovernanceActions   = null; // GovernanceActionExecutor (0462)
    private RelatedMetadataElementSummary       governanceActionExecutor    = null; // GovernanceActionExecutor (0462)

    private List<RelatedMetadataElementSummary> includedInIntegrationGroups    = null; // RegisteredIntegrationConnector (0464)
    private List<RelatedMetadataElementSummary> supportedIntegrationConnectors = null; // RegisteredIntegrationConnector (0464)
    private List<RelatedMetadataElementSummary> refreshedByConnectors          = null; // CatalogTarget (0464)
    private List<RelatedMetadataElementSummary> catalogTargets                 = null; // CatalogTarget (0464)

    private List<RelatedMetadataElementSummary> peerDuplicateOrigin         = null; // PeerDuplicateLink (0465)
    private List<RelatedMetadataElementSummary> peerDuplicatePartner        = null; // PeerDuplicateLink (0465)
    private List<RelatedMetadataElementSummary> consolidatedDuplicateOrigin = null; // ConsolidatedDuplicateLink (0466)
    private List<RelatedMetadataElementSummary> consolidatedDuplicateResult = null; // ConsolidatedDuplicateLink (0465)

    private List<RelatedMetadataElementSummary> impactedResources = null; // ImpactedResource (0470)
    private List<RelatedMetadataElementSummary> incidentReports   = null; // ImpactedResource (0470)

    private List<RelatedMetadataElementSummary> licenses          = null; // License (0481)
    private List<RelatedMetadataElementSummary> licensedElements  = null; // License (0481)
    private List<RelatedMetadataElementSummary> certifications    = null; // Certification (0482)
    private List<RelatedMetadataElementSummary> certifiedElements = null; // Certification (0482)



    /*
     * Area 5
     */
    private RelatedMetadataElementSummary schemaType         = null; // Schema (0503)
    private RelatedMetadataElementSummary describesStructure = null; // Schema (0503)

    private List<RelatedMetadataElementSummary> parentSchemaElements = null; // SchemaTypeOption (0501), AttributeForSchema, NestedSchemaAttribute (0505), APIOperations (0536), APIHeader (0536), APIRequest (0536), APIResponse (0536)
    private List<RelatedMetadataElementSummary> schemaOptions        = null; // SchemaTypeOption (0501)
    private List<RelatedMetadataElementSummary> schemaAttributes     = null;  // AttributeForSchema, NestedSchemaAttribute (0505)
    private RelatedMetadataElementSummary       externalSchemaType   = null; // LinkedExternalSchemaType (0507)
    private RelatedMetadataElementSummary       mapFromElement       = null; // MapFromElementType (0511)
    private RelatedMetadataElementSummary       mapToElement         = null; // MapToElementType (0511)
    private List<RelatedMetadataElementSummary> queries              = null;  // DerivedSchemaTypeQueryTarget (0512)
    private List<RelatedMetadataElementSummary> containsOperations   = null; // APIOperations (0536)
    private RelatedMetadataElementSummary       apiHeader            = null; // APIOperations (0536)
    private RelatedMetadataElementSummary       apiRequest           = null; // APIOperations (0536)
    private RelatedMetadataElementSummary       apiResponse          = null; // APIOperations (0536)

    private RelatedMetadataElementSummary       databaseSchemaType     = null; // RelationalDBSchema (0534)
    private List<RelatedMetadataElementSummary> containsDBSchemas      = null; // RelationalDBSchema (0534)
    private RelatedMetadataElementSummary       linkedToPrimaryKey     = null; // ForeignKey (0534)
    private List<RelatedMetadataElementSummary> foreignKeys            = null; // ForeignKey (0534)
    private List<RelatedMetadataElementSummary> vertices               = null; // GraphEdgeLink (0533)
    private List<RelatedMetadataElementSummary> edges                  = null; // GraphEdgeLink (0533)

    private List<RelatedMetadataElementSummary> describedByDataValueSpecifications = null; // DataValueDefinition (0540)
    private List<RelatedMetadataElementSummary> dataValueSpecifications            = null; // DataValueDefinition (0540)
    private List<RelatedMetadataElementSummary> assignedToDataValueSpecifications  = null; // DataValueAssignment (0540)
    private List<RelatedMetadataElementSummary> assignedDataValueSpecifications    = null; // DataValueAssignment (0540)
    private RelatedMetadataElementSummary       superDataValueSpecification        = null; // DataValueHierarchy (0540)
    private List<RelatedMetadataElementSummary> subDataValueSpecifications         = null; // DataValueHierarchy (0540)

    private List<RelatedMetadataElementSummary> madeOfDataClasses = null; // DataClassComposition (0541)
    private List<RelatedMetadataElementSummary> partOfDataClasses = null; // DataClassComposition (0541)

    private List<RelatedMetadataElementSummary> validValues               = null; // ValidValueAssignment (0545)
    private List<RelatedMetadataElementSummary> validValueConsumers       = null; // ValidValueAssignment (0545)
    private List<RelatedMetadataElementSummary> referenceValues           = null; // ReferenceValueAssignment (0545)
    private List<RelatedMetadataElementSummary> assignedItems             = null; // ReferenceValueAssignment (0545)
    private List<RelatedMetadataElementSummary> matchingValues            = null; // ValidValueMapping (0545)
    private List<RelatedMetadataElementSummary> consistentValues          = null; // ConsistentValidValues (0545)
    private List<RelatedMetadataElementSummary> associatedValues          = null; // ValidValueAssociation (0545)
    private List<RelatedMetadataElementSummary> validValueMembers         = null; // ValidValueMember (0545)
    private List<RelatedMetadataElementSummary> memberOfValidValueSets    = null; // ValidValueMember (0545)
    private List<RelatedMetadataElementSummary> validValueImplementations = null; // ValidValuesImplementation (0545)
    private List<RelatedMetadataElementSummary> canonicalValidValues      = null; // ValidValuesImplementation (0545)
    private List<RelatedMetadataElementSummary> specificationProperties    = null; // SpecificationPropertyAssignment (0545)
    private List<RelatedMetadataElementSummary> specificationPropertyUsers = null; // SpecificationPropertyAssignment (0545)

    private RelatedMetadataElementSummary       dataStructureDefinition   = null; // DataStructureDefinition (0580)
    private List<RelatedMetadataElementSummary> usedInCertifications      = null; // DataStructureDefinition (0580)
    private List<RelatedMetadataElementSummary> dataDescription           = null; // DataDescription (0580)
    private List<RelatedMetadataElementSummary> describesDataFor          = null; // DataDescription (0580)
    private List<RelatedMetadataElementSummary> containsDataFields        = null; // MemberDataField (0580)
    private List<RelatedMetadataElementSummary> partOfDataStructures      = null; // MemberDataField (0580)
    private List<RelatedMetadataElementSummary> parentDataFields          = null; // NestedDataField (0581)
    private List<RelatedMetadataElementSummary> nestedDataFields          = null; // NestedDataField (0581)
    private List<RelatedMetadataElementSummary> linkedToDataFields        = null; // LinkedDataField (0581)
    private List<RelatedMetadataElementSummary> linkedFromDataFields      = null; // LinkedDataField (0581)
    private RelatedMetadataElementSummary       derivedFromDataStructure  = null; // SchemaTypeDefinition (0581)
    private RelatedMetadataElementSummary       equivalentSchemaType      = null; // SchemaTypeDefinition (0581)
    private RelatedMetadataElementSummary       derivedFromDataField      = null; // SchemaAttributeDefinition (0581)
    private RelatedMetadataElementSummary       equivalentSchemaAttribute = null; // SchemaAttributeDefinition (0581)

    private List<RelatedMetadataElementSummary> relatedDesignPatterns      = null; // RelatedDesignPattern (0595)
    private List<RelatedMetadataElementSummary> consumingDesignPatterns    = null; // NestedDesignPattern (0595)
    private List<RelatedMetadataElementSummary> consumedDesignPatterns     = null; // NestedDesignPattern (0595)
    private List<RelatedMetadataElementSummary> generalizedDesignPattern   = null; // SpecializedDesignPattern (0595)
    private List<RelatedMetadataElementSummary> specializedDesignPattern   = null; // SpecializedDesignPattern (0595)

    /*
     * Area 6
     */
    private RelatedMetadataElementSummary       fromSurveyReport              = null; // ReportedAnnotation (0610)
    private List<RelatedMetadataElementSummary> reportedAnnotations           = null; // ReportedAnnotation (0610)
    private List<RelatedMetadataElementSummary> previousAnnotations           = null; // AnnotationExtension (0610)
    private List<RelatedMetadataElementSummary> annotationExtensions          = null; // AnnotationExtension (0610)
    private List<RelatedMetadataElementSummary> annotationSubjects            = null; // AssociatedAnnotation (0610)
    private List<RelatedMetadataElementSummary> associatedAnnotations         = null; // AssociatedAnnotation (0610)
    private List<RelatedMetadataElementSummary> annotationMatches             = null; // DiscoveredSchemaType (0615)
    private List<RelatedMetadataElementSummary> matchedByAnnotations          = null; // DiscoveredSchemaType (0615)
    private List<RelatedMetadataElementSummary> resourceProfileAnnotations    = null; // ResourceProfileData (0620)
    private List<RelatedMetadataElementSummary> resourceProfileData           = null; // ResourceProfileData (0620)
    private List<RelatedMetadataElementSummary> identifiedByRequestForActions = null; // RequestForActionTarget (0660)
    private List<RelatedMetadataElementSummary> requestForActionTargets       = null; // RequestForActionTarget (0660)


    /*
     * Area 7
     */
    private List<RelatedMetadataElementSummary> usedByDigitalProducts = null; // DigitalProductDependency (0710)
    private List<RelatedMetadataElementSummary> usesDigitalProducts   = null; // DigitalProductDependency (0710)

    private List<RelatedMetadataElementSummary> agreementItems        = null; // AgreementItem (0711)
    private List<RelatedMetadataElementSummary> agreementContents     = null; // AgreementItem (0711)
    private List<RelatedMetadataElementSummary> agreementActors       = null; // AgreementActor (0711)
    private List<RelatedMetadataElementSummary> involvedInAgreements  = null; // AgreementActor (0711)
    private List<RelatedMetadataElementSummary> contracts             = null; // ContractLink (0711)
    private List<RelatedMetadataElementSummary> agreementsForContract = null; // ContractLink (0711)

    private List<RelatedMetadataElementSummary> digitalSubscribers   = null; // DigitalSubscriber (0712)
    private List<RelatedMetadataElementSummary> digitalSubscriptions = null; // DigitalSubscriber (0712)

    private List<RelatedMetadataElementSummary> consumingBusinessCapabilities = null; /* DigitalSupport (0715) */
    private List<RelatedMetadataElementSummary> usesDigitalServices           = null; /* DigitalSupport (0715) */
    private List<RelatedMetadataElementSummary> supportsBusinessCapabilities  = null; /* BusinessCapabilityDependency (0715) */
    private List<RelatedMetadataElementSummary> dependsOnBusinessCapabilities = null; /* BusinessCapabilityDependency (0715) */

    private List<RelatedMetadataElementSummary> supplyFrom              = null; /* InformationSupplyChainLink (0720) */
    private List<RelatedMetadataElementSummary> supplyTo                = null; /* InformationSupplyChainLink (0720) */

    private List<RelatedMetadataElementSummary> usedInSolutionComponents          = null; /* SolutionComposition (0730) */
    private List<RelatedMetadataElementSummary> nestedSolutionComponents          = null; /* SolutionComposition (0730) */
    private List<RelatedMetadataElementSummary> interactingWithActors             = null; /* SolutionComponentActor (0730) */
    private List<RelatedMetadataElementSummary> interactingWithSolutionComponents = null; /* SolutionComponentActor (0730) */


    private RelatedMetadataElementSummary       solutionComponent         = null; // SolutionComponentPort (0735)
    private List<RelatedMetadataElementSummary> solutionPorts             = null; // SolutionComponentPort (0735)
    private List<RelatedMetadataElementSummary> wiredTo                   = null; // SolutionLinkingWire (0735)
    private RelatedMetadataElementSummary       alignsToPort              = null; // SolutionPortDelegation (0735)
    private List<RelatedMetadataElementSummary> delegationPorts           = null; // SolutionPortDelegation (0735)

    private List<RelatedMetadataElementSummary> derivedFrom               = null; // ImplementedBy (0737)
    private List<RelatedMetadataElementSummary> implementedBy             = null; // ImplementedBy (0737)
    private List<RelatedMetadataElementSummary> usedInImplementationOf    = null; // ImplementationResource (0737)
    private List<RelatedMetadataElementSummary> implementationResources   = null; // ImplementationResource (0737)
    private List<RelatedMetadataElementSummary> describesDesignOf          = null; // SolutionDesign (0740)
    private List<RelatedMetadataElementSummary> solutionDesigns            = null; // SolutionDesign (0740)

    private List<RelatedMetadataElementSummary> lineageLinkage            = null; // Many (0750, 0755, 0770, ...)


    /*
     * Others
     */
    private List<RelatedMetadataElementSummary> otherRelatedElements      = null;
    private RelatedBy                           relatedBy                 = null;

    /**
     * Default constructor used by subclasses
     */
    public AttributedMetadataElement()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template template to copy
     */
    public AttributedMetadataElement(AttributedMetadataElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();

            sampleData          = template.getSampleData();
            sourcesOfSampleData = template.getSourcesOfSampleData();

            templateCreatedElements = template.getTemplateCreatedElements();
            sourcedFromTemplate     = template.getSourcedFromTemplate();
            templatesForCataloguing = template.getTemplatesForCataloguing();
            templateUses            = template.getTemplateUses();

            searchKeywords   = template.getSearchKeywords();
            keywordElements  = template.getKeywordElements();

            actionSource     = template.getActionSource();
            requestedActions = template.getRequestedActions();
            actionCause      = template.getActionCause();
            relatedActions   = template.getRelatedActions();
            actionTargets    = template.getActionTargets();
            actionsForTarget = template.getActionsForTarget();

            externalReferences  = template.getExternalReferences();
            referencingElements = template.getReferencingElements();

            alsoKnownAs                   = template.getAlsoKnownAs();
            equivalentElements            = template.getEquivalentElements();

            resourceList            = template.getResourceList();
            resourceListUsers       = template.getResourceListUsers();
            describes               = template.getDescribes();
            providesMoreInformation = template.getProvidesMoreInformation();

            propertyFacets  = template.getPropertyFacets();
            facetedElements = template.getFacetedElements();

            memberOfCollections = template.getMemberOfCollections();
            collectionMembers   = template.getCollectionMembers();

            knownLocations    = template.getKnownLocations();
            localResources    = template.getLocalResources();
            peerLocations     = template.getPeerLocations();
            groupingLocations = template.getGroupingLocations();
            nestedLocations   = template.getNestedLocations();

            serverEndpoints   = template.getServerEndpoints();
            serverForEndpoint = template.getServerForEndpoint();

            hostedITAssets = template.getHostedITAssets();
            deployedTo     = template.getDeployedTo();

            storageVolumes     = template.getStorageVolumes();
            providesStorageFor = template.getProvidesStorageFor();
            managesStorageFor  = template.getManagesStorageFor();
            storedOn           = template.getStoredOn();
            installedOn                  = template.getInstalledOn();
            operatingPlatforms           = template.getOperatingPlatforms();
            includesSoftwarePackages     = template.getIncludesSoftwarePackages();
            packagedInOperatingPlatforms = template.getPackagedInOperatingPlatforms();
            dependsOnSoftwarePackages    = template.getDependsOnSoftwarePackages();
            runningWithAsset             = template.getRunningWithAsset();
            managedHosts                 = template.getManagedHosts();
            hostCluster                  = template.getHostCluster();
            resultsStoredIn              = template.getResultsStoredIn();
            populatedUsingQuery          = template.getPopulatedUsingQuery();

            consumedByCapabilities   = template.getConsumedByCapabilities();
            capabilityConsumedAssets = template.getCapabilityConsumedAssets();

            capabilities                     = template.getCapabilities();
            hostedByDeployedITInfrastructure = template.getHostedByDeployedITInfrastructure();

            cohortMembership      = template.getCohortMembership();
            registeredWithCohorts = template.getRegisteredWithCohorts();

            visibleEndpoints              = template.getVisibleEndpoints();
            visibleInNetworks             = template.getVisibleInNetworks();


            /*
             * Area 1
             */

            userProfile    = template.getUserProfile();
            userIdentities = template.getUserIdentities();

            contactDetails = template.getContactDetails();
            contacts       = template.getContacts();

            myFollowers = template.getMyFollowers();
            myPeers = template.getMyPeers();

            superTeam = template.getSuperTeam();
            subTeams  = template.getSubTeams();

            profilesForAsset   = template.getProfilesForAsset();
            assetsUsingProfile = template.getAssetsUsingProfile();

            performsRoles  = template.getPerformsRoles();
            rolePerformers = template.getRolePerformers();

            relevantToScopes = template.getRelevantToScopes();
            scopedElements   = template.getScopedElements();
            assignmentScope = template.getAssignmentScope();
            assignedActors  = template.getAssignedActors();

            contributionRecord = template.getContributionRecord();
            contributorProfile = template.getContributorProfile();

            dependsOnProjects = template.getDependsOnProjects();
            dependentProjects = template.getDependentProjects();
            managedProjects   = template.getManagedProjects();
            managingProjects  = template.getManagingProjects();

            likes              = template.getLikes();
            likedElement       = template.getLikedElement();
            reviews            = template.getReviews();
            reviewedElement    = template.getReviewedElement();
            informalTags       = template.getInformalTags();
            taggedElements     = template.getTaggedElements();
            comments           = template.getComments();
            commentedOnElement = template.getCommentedOnElement();
            answeredQuestions  = template.getAnsweredQuestions();
            acceptedAnswers    = template.getAcceptedAnswers();

            crowdSourcingContributors = template.getCrowdSourcingContributors();
            crowdSourcedContributions = template.getCrowdSourcedContributions();

            noteLogSubjects   = template.getNoteLogSubjects();
            noteLogs          = template.getNoteLogs();
            presentInNoteLogs = template.getPresentInNoteLogs();
            noteLogEntries    = template.getNoteLogEntries();

            /*
             * Area 2
             */
            connections         = template.getConnections();
            connectorType       = template.getConnectorType();
            endpoint            = template.getEndpoint();
            connectedResources  = template.getConnectedResources();
            parentConnections   = template.getParentConnections();
            embeddedConnections = template.getEmbeddedConnections();

            supportedDataSets       = template.getSupportedDataSets();
            dataSetContent          = template.getDataSetContent();
            apiEndpoints            = template.getAPIEndpoints();
            supportedAPIs           = template.getSupportedAPIs();
            parentProcesses         = template.getParentProcesses();
            childProcesses          = template.getChildProcesses();
            ports                   = template.getPorts();
            portOwningProcesses     = template.getPortOwningProcesses();
            portDelegatingFrom      = template.getPortDelegatingFrom();
            portDelegatingTo        = template.getPortDelegatingTo();
            homeFolder              = template.getHomeFolder();
            nestedFiles             = template.getNestedFiles();
            linkedFiles             = template.getLinkedFiles();
            linkedFolders           = template.getLinkedFolders();
            parentFolder            = template.getParentFolder();
            nestedFolders           = template.getNestedFolders();
            linkedMediaFiles        = template.getLinkedMediaFiles();
            associatedLogs          = template.getAssociatedLogs();
            associatedLogSubjects   = template.getAssociatedLogSubjects();
            archiveContents         = template.getArchiveContents();
            packagedInArchiveFiles  = template.getPackagedInArchiveFiles();

            reportOriginator = template.getReportOriginator();
            generatedReports = template.getGeneratedReports();
            reportSubjects   = template.getReportSubjects();
            reports          = template.getReports();
            priorReports     = template.getPriorReports();
            followOnReports  = template.getFollowOnReports();

            /*
             * Area 3
             */
            relatedTerms                      = template.getRelatedTerms();
            usedInContexts                    = template.getUsedInContexts();
            contextRelevantTerms              = template.getContextRelevantTerms();
            semanticDefinitions               = template.getSemanticDefinitions();
            semanticallyAssociatedDefinitions = template.getSemanticallyAssociatedDefinitions();
            meaningForDataElements            = template.getMeaningForDataElements();
            meanings                          = template.getMeanings();
            supplementaryProperties           = template.getSupplementaryProperties();
            supplementsElement                = template.getSupplementsElement();

            /*
             * Area 4
             */

            governedBy                = template.getGovernedBy();
            governedElements          = template.getGovernedElements();

            peerGovernanceDefinitions      = template.getPeerGovernanceDefinitions();
            supportedGovernanceDefinitions = template.getSupportedGovernanceDefinitions();
            supportingGovernanceDefinitions = template.getSupportingGovernanceDefinitions();

            configuredInSecurityCollections = template.getConfiguredInSecurityCollections();
            userAccounts                    = template.getUserAccounts();

            definedInSecretsCollection = template.getDefinedInSecretsCollection();
            securityAccessControls     = template.getSecurityAccessControls();

            listedInSecretsCollection = template.getListedInSecretsCollection();
            securityLists             = template.getSecurityLists();

            usedInAccessControls    = template.getUsedInAccessControls();
            associatedSecurityLists = template.getAssociatedSecurityLists();

            inheritsFromZone = template.getInheritsFromZone();
            controlsZones = template.getControlsZones();

            broaderSubjectArea = template.getBroaderSubjectArea();
            nestedSubjectAreas = template.getNestedSubjectAreas();

            metrics      = template.getMetrics();
            measurements = template.getMeasurements();

            monitoredThrough             = template.getMonitoredThrough();
            monitoredResources           = template.getMonitoredResources();
            interestingNotificationTypes = template.getInterestingNotificationTypes();
            subscribers                  = template.getSubscribers();

            excludedFromRequirements    = template.getExcludedFromRequirements();
            exceptions                  = template.getExceptions();

            calledFromGovernanceEngines = template.getCalledFromGovernanceEngines();
            supportedGovernanceServices = template.getSupportedGovernanceServices();

            associatedGovernanceActions = template.getAssociatedGovernanceActions();
            predefinedTargetForAction   = template.getPredefinedTargetForAction();
            triggeredFrom               = template.getTriggeredFrom();
            firstStep                   = template.getFirstStep();
            dependedOnProcessSteps      = template.getDependedOnProcessSteps();
            followOnProcessSteps        = template.getFollowOnProcessSteps();
            supportsGovernanceActions   = template.getSupportsGovernanceActions();
            governanceActionExecutor    = template.getGovernanceActionExecutor();

            includedInIntegrationGroups    = template.getIncludedInIntegrationGroups();
            supportedIntegrationConnectors = template.getSupportedIntegrationConnectors();
            refreshedByConnectors          = template.getRefreshedByConnectors();
            catalogTargets                 = template.getCatalogTargets();

            peerDuplicateOrigin = template.getPeerDuplicateOrigin();
            peerDuplicatePartner = template.getPeerDuplicatePartner();
            consolidatedDuplicateOrigin = template.getConsolidatedDuplicateOrigin();
            consolidatedDuplicateResult = template.getConsolidatedDuplicateResult();

            impactedResources = template.getImpactedResources();
            incidentReports = template.getIncidentReports();

            licenses                  = template.getLicenses();
            licensedElements          = template.getLicensedElements();
            certifications            = template.getCertifications();
            certifiedElements         = template.getCertifiedElements();

            agreementActors       = template.getAgreementActors();
            involvedInAgreements  = template.getInvolvedInAgreements();
            agreementItems        = template.getAgreementItems();
            agreementContents     = template.getAgreementContents();
            contracts             = template.getContracts();
            agreementsForContract = template.getAgreementsForContract();


            /*
             * Area 5
             */
            schemaType           = template.getSchemaType();
            describesStructure   = template.getDescribesStructure();
            schemaAttributes     = template.getSchemaAttributes();
            parentSchemaElements = template.getParentSchemaElements();

            mapFromElement     = template.getMapFromElement();
            mapToElement       = template.getMapToElement();
            externalSchemaType = template.getExternalSchemaType();
            schemaOptions      = template.getSchemaOptions();
            queries            = template.getQueries();

            containsOperations = template.getContainsOperations();
            apiHeader          = template.getAPIHeader();
            apiRequest         = template.getAPIRequest();
            apiResponse        = template.getAPIResponse();

            databaseSchemaType = template.getDatabaseSchemaType();
            containsDBSchemas  = template.getContainsDBSchemas();
            linkedToPrimaryKey = template.getLinkedToPrimaryKey();
            foreignKeys        = template.getForeignKeys();

            vertices = template.getVertices();
            edges    = template.getEdges();

            describedByDataValueSpecifications = template.getDescribedByDataValueSpecifications();
            dataValueSpecifications            = template.getDataValueSpecifications();
            assignedToDataValueSpecifications  = template.getAssignedToDataValueSpecifications();
            assignedDataValueSpecifications    = template.getAssignedDataValueSpecifications();
            superDataValueSpecification        = template.getSuperDataValueSpecification();
            subDataValueSpecifications         = template.getSubDataValueSpecifications();

            madeOfDataClasses     = template.getMadeOfDataClasses();
            partOfDataClasses     = template.getPartOfDataClasses();

            validValues               = template.getValidValues();
            validValueConsumers       = template.getValidValueConsumers();
            referenceValues           = template.getReferenceValues();
            assignedItems             = template.getAssignedItems();
            matchingValues            = template.getMatchingValues();
            consistentValues          = template.getConsistentValues();
            associatedValues          = template.getAssociatedValues();
            validValueMembers         = template.getValidValueMembers();
            memberOfValidValueSets    = template.getMemberOfValidValueSets();
            validValueImplementations = template.getValidValueImplementations();
            canonicalValidValues      = template.getCanonicalValidValues();
            specificationProperties    = template.getSpecificationProperties();
            specificationPropertyUsers = template.getSpecificationPropertyUsers();

            dataStructureDefinition    = template.getDataStructureDefinition();
            usedInCertifications       = template.getUsedInCertifications();
            containsDataFields         = template.getContainsDataFields();
            partOfDataStructures       = template.getPartOfDataStructures();
            dataDescription            = template.getDataDescription();
            describesDataFor           = template.getDescribesDataFor();

            parentDataFields          = template.getParentDataFields();
            nestedDataFields          = template.getNestedDataFields();
            linkedToDataFields        = template.getLinkedToDataFields();
            linkedFromDataFields      = template.getLinkedFromDataFields();
            derivedFromDataStructure  = template.getDerivedFromDataStructure();
            equivalentSchemaType      = template.getEquivalentSchemaType();
            derivedFromDataField      = template.getDerivedFromDataField();
            equivalentSchemaAttribute = template.getEquivalentSchemaAttribute();

            relatedDesignPatterns    = template.getRelatedDesignPatterns();
            consumingDesignPatterns  = template.getConsumingDesignPatterns();
            consumedDesignPatterns   = template.getConsumedDesignPatterns();
            generalizedDesignPattern = template.getGeneralizedDesignPattern();
            specializedDesignPattern = template.getSpecializedDesignPattern();

            /*
             * Area 6
             */
            fromSurveyReport              = template.getFromSurveyReport(); // ReportedAnnotation (0610)
            reportedAnnotations           = template.getReportedAnnotations(); // ReportedAnnotation (0610)
            previousAnnotations           = template.getPreviousAnnotations(); // AnnotationExtension (0610)
            annotationExtensions          = template.getAnnotationExtensions(); // AnnotationExtension (0610)
            annotationSubjects            = template.getAnnotationSubjects(); // AssociatedAnnotation (0610)
            associatedAnnotations  = template.getAssociatedAnnotations(); // AssociatedAnnotation (0610)
            annotationMatches          = template.getAnnotationMatches(); // DiscoveredSchemaType (0615)
            matchedByAnnotations       = template.getMatchedByAnnotations(); // DiscoveredSchemaType (0615)
            resourceProfileAnnotations = template.getResourceProfileAnnotations(); // ResourceProfileData (0620)
            resourceProfileData           = template.getResourceProfileData(); // ResourceProfileData (0620)
            identifiedByRequestForActions = template.getIdentifiedByRequestForActions(); // RequestForActionTarget (0660)
            requestForActionTargets       = template.getRequestForActionTargets(); // RequestForActionTarget (0660)

            /*
             * Area 7
             */
            usedByDigitalProducts = template.getUsedByDigitalProducts();
            usesDigitalProducts   = template.getUsesDigitalProducts();

            digitalSubscribers   = template.getDigitalSubscribers();
            digitalSubscriptions = template.getDigitalSubscriptions();

            consumingBusinessCapabilities = template.getConsumingBusinessCapabilities();
            usesDigitalServices           = template.getUsesDigitalServices();
            supportsBusinessCapabilities  = template.getSupportsBusinessCapabilities();
            dependsOnBusinessCapabilities = template.getDependsOnBusinessCapabilities();

            supplyFrom              = template.getSupplyFrom();
            supplyTo                = template.getSupplyTo();

            usedInSolutionComponents          = template.getUsedInSolutionComponents();
            nestedSolutionComponents          = template.getNestedSolutionComponents();
            interactingWithActors             = template.getInteractingWithActors();
            interactingWithSolutionComponents = template.getInteractingWithSolutionComponents();

            solutionComponent         = template.getSolutionComponent();
            solutionPorts             = template.getSolutionPorts();
            wiredTo                   = template.getWiredTo();
            alignsToPort              = template.getAlignsToPort();
            delegationPorts           = template.getDelegationPorts();

            derivedFrom               = template.getDerivedFrom();
            implementedBy             = template.getImplementedBy();
            usedInImplementationOf    = template.getUsedInImplementationOf();
            implementationResources   = template.getImplementationResources();

            describesDesignOf          = template.getDescribesDesignOf();
            solutionDesigns            = template.getSolutionDesigns();

            lineageLinkage            = template.getLineageLinkage();

            /*
             * Others
             */
            otherRelatedElements      = template.getOtherRelatedElements();
            relatedBy                 = template.getRelatedBy();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }

    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return details of the asset(s) that provides the sample data.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getSampleData()
    {
        return sampleData;
    }


    /**
     * Set up details of the asset(s) that provides the sample data.
     *
     * @param sampleData list
     */
    public void setSampleData(List<RelatedMetadataElementSummary> sampleData)
    {
        this.sampleData = sampleData;
    }


    /**
     * Return the assets that this sample was created from.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getSourcesOfSampleData()
    {
        return sourcesOfSampleData;
    }


    /**
     * Set up the assets that this sample was created from.
     *
     * @param sourcesOfSampleData list
     */
    public void setSourcesOfSampleData(List<RelatedMetadataElementSummary> sourcesOfSampleData)
    {
        this.sourcesOfSampleData = sourcesOfSampleData;
    }


    /**
     * Return the elements created from this template.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getTemplateCreatedElements()
    {
        return templateCreatedElements;
    }


    /**
     * Set up the elements created from this template.
     *
     * @param templateCreatedElements list
     */
    public void setTemplateCreatedElements(List<RelatedMetadataElementSummary> templateCreatedElements)
    {
        this.templateCreatedElements = templateCreatedElements;
    }


    /**
     * Return the template used to create this element.
     *
     * @return template element
     */
    public RelatedMetadataElementSummary getSourcedFromTemplate()
    {
        return sourcedFromTemplate;
    }


    /**
     * Set up the template used to create this element.
     *
     * @param sourcedFromTemplate template element
     */
    public void setSourcedFromTemplate(RelatedMetadataElementSummary sourcedFromTemplate)
    {
        this.sourcedFromTemplate = sourcedFromTemplate;
    }


    /**
     * Return the templates that are relevant to this element.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getTemplatesForCataloguing()
    {
        return templatesForCataloguing;
    }


    /**
     * Set up the templates that are relevant to this element.
     *
     * @param templatesForCataloguing list
     */
    public void setTemplatesForCataloguing(List<RelatedMetadataElementSummary> templatesForCataloguing)
    {
        this.templatesForCataloguing = templatesForCataloguing;
    }


    /**
     * Return the elements that the linked template is useful for.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getTemplateUses()
    {
        return templateUses;
    }


    /**
     * Set up the elements that the linked template is useful for.
     *
     * @param templateUses list
     */
    public void setTemplateUses(List<RelatedMetadataElementSummary> templateUses)
    {
        this.templateUses = templateUses;
    }


    /**
     * Set up the list of external references for this element
     *
     * @param externalReferences list
     */
    public void setExternalReferences(List<RelatedMetadataElementSummary> externalReferences)
    {
        this.externalReferences = externalReferences;
    }


    /**
     * Return the list of external references for this element.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getExternalReferences()
    {
        return externalReferences;
    }


    /**
     * Return the elements referencing this external element.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getReferencingElements()
    {
        return referencingElements;
    }


    /**
     * Set up the elements referencing this external element.
     *
     * @param referencingElements list
     */
    public void setReferencingElements(List<RelatedMetadataElementSummary> referencingElements)
    {
        this.referencingElements = referencingElements;
    }


    /**
     * Return attached external identifiers.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getAlsoKnownAs()
    {
        return alsoKnownAs;
    }


    /**
     * Set up attached external identifiers.
     *
     * @param alsoKnownAs list
     */
    public void setAlsoKnownAs(List<RelatedMetadataElementSummary> alsoKnownAs)
    {
        this.alsoKnownAs = alsoKnownAs;
    }


    /**
     * Return the elements that represent the external element for this external identifier.
     *
     * @return list (since the mapping may not be 1-1)
     */
    public List<RelatedMetadataElementSummary> getEquivalentElements()
    {
        return equivalentElements;
    }


    /**
     * Set up the elements that represent the external element for this external identifier.
     *
     * @param equivalentElements list
     */
    public void setEquivalentElements(List<RelatedMetadataElementSummary> equivalentElements)
    {
        this.equivalentElements = equivalentElements;
    }


    /**
     * Return the list of collections that is definition is a member of.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getMemberOfCollections()
    {
        return memberOfCollections;
    }


    /**
     * Set up the list of collections that is definition is a member of.
     *
     * @param memberOfCollections list
     */
    public void setMemberOfCollections(List<RelatedMetadataElementSummary> memberOfCollections)
    {
        this.memberOfCollections = memberOfCollections;
    }


    /**
     * Return members of this collection.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getCollectionMembers()
    {
        return collectionMembers;
    }

    /**
     * Set up members of this collection.
     *
     * @param collectionMembers list of related elements
     */
    public void setCollectionMembers(List<RelatedMetadataElementSummary> collectionMembers)
    {
        this.collectionMembers = collectionMembers;
    }


    /**
     * Return places where this element is known to be sited.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getKnownLocations()
    {
        return knownLocations;
    }

    /**
     * Set up places where this element is known to be sited.
     *
     * @param knownLocations list of related elements
     */
    public void setKnownLocations(List<RelatedMetadataElementSummary> knownLocations)
    {
        this.knownLocations = knownLocations;
    }

    public List<RelatedMetadataElementSummary> getLocalResources()
    {
        return localResources;
    }

    public void setLocalResources(List<RelatedMetadataElementSummary> localResources)
    {
        this.localResources = localResources;
    }

    /**
     * Return location that is adjacent to this location.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getPeerLocations()
    {
        return peerLocations;
    }

    /**
     * Set up location that is adjacent to this location.
     *
     * @param peerLocations list of related elements
     */
    public void setPeerLocations(List<RelatedMetadataElementSummary> peerLocations)
    {
        this.peerLocations = peerLocations;
    }

    /**
     * Return location that is covering the broader area.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getGroupingLocations()
    {
        return groupingLocations;
    }

    /**
     * Set up location that is covering the broader area.
     *
     * @param groupingLocations list of related elements
     */
    public void setGroupingLocations(List<RelatedMetadataElementSummary> groupingLocations)
    {
        this.groupingLocations = groupingLocations;
    }

    /**
     * Return location that is nested in this location.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getNestedLocations()
    {
        return nestedLocations;
    }

    /**
     * Set up location that is nested in this location.
     *
     * @param nestedLocations list of related elements
     */
    public void setNestedLocations(List<RelatedMetadataElementSummary> nestedLocations)
    {
        this.nestedLocations = nestedLocations;
    }

    public List<RelatedMetadataElementSummary> getServerEndpoints()
    {
        return serverEndpoints;
    }

    public void setServerEndpoints(List<RelatedMetadataElementSummary> serverEndpoints)
    {
        this.serverEndpoints = serverEndpoints;
    }

    public RelatedMetadataElementSummary getServerForEndpoint()
    {
        return serverForEndpoint;
    }

    public void setServerForEndpoint(RelatedMetadataElementSummary serverForEndpoint)
    {
        this.serverForEndpoint = serverForEndpoint;
    }

    /**
     * Return end 1 of DeployedOn relationship.  These are, for example, platforms and servers on a host.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getHostedITAssets()
    {
        return hostedITAssets;
    }


    /**
     * Set up end 1 of DeployedOn relationship.  These are, for example, platforms and servers on a host.
     *
     * @param hostedITAssets list of related elements
     */
    public void setHostedITAssets(List<RelatedMetadataElementSummary> hostedITAssets)
    {
        this.hostedITAssets = hostedITAssets;
    }


    /**
     * Return end 2 of the DeployedOn relationship.  This would be, say the platforms hosting a server.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getDeployedTo()
    {
        return deployedTo;
    }


    /**
     * Set up end 2 of the DeployedOn relationship.  This would be, say the platforms hosting a server.
     *
     * @param deployedTo list of related elements
     */
    public void setDeployedTo(List<RelatedMetadataElementSummary> deployedTo)
    {
        this.deployedTo = deployedTo;
    }


    /**
     * Return the storage volumes attached to a host.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getStorageVolumes()
    {
        return storageVolumes;
    }


    /**
     * Set up the hosts that are using this storage volume
     *
     * @param storageVolumes list of related elements
     */
    public void setStorageVolumes(List<RelatedMetadataElementSummary> storageVolumes)
    {
        this.storageVolumes = storageVolumes;
    }


    /**
     * Return the infrastructure using this storage volume.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getProvidesStorageFor()
    {
        return providesStorageFor;
    }


    /**
     * Set up the infrastructure using this storage volume.
     *
     * @param providesStorageFor list of related elements
     */
    public void setProvidesStorageFor(List<RelatedMetadataElementSummary> providesStorageFor)
    {
        this.providesStorageFor = providesStorageFor;
    }


    /**
     * Return the data stores that manage storage on this storage volume.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getManagesStorageFor()
    {
        return managesStorageFor;
    }


    /**
     * Set up the data stores that manage storage on this storage volume.
     *
     * @param managesStorageFor list of related elements
     */
    public void setManagesStorageFor(List<RelatedMetadataElementSummary> managesStorageFor)
    {
        this.managesStorageFor = managesStorageFor;
    }


    /**
     * Return the storage volume that this data store is stored on.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getStoredOn()
    {
        return storedOn;
    }


    /**
     * Set up the storage volume that this data store is stored on.
     *
     * @param storedOn list of related elements
     */
    public void setStoredOn(List<RelatedMetadataElementSummary> storedOn)
    {
        this.storedOn = storedOn;
    }


    /**
     * Return the IT infrastructure that this operating platform is installed on.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getInstalledOn()
    {
        return installedOn;
    }


    /**
     * Set up the IT infrastructure that this operating platform is installed on.
     *
     * @param installedOn list of related elements
     */
    public void setInstalledOn(List<RelatedMetadataElementSummary> installedOn)
    {
        this.installedOn = installedOn;
    }


    /**
     * Return the operating platforms installed on this IT infrastructure.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getOperatingPlatforms()
    {
        return operatingPlatforms;
    }


    /**
     * Set up the operating platforms installed on this IT infrastructure.
     *
     * @param operatingPlatforms list of related elements
     */
    public void setOperatingPlatforms(List<RelatedMetadataElementSummary> operatingPlatforms)
    {
        this.operatingPlatforms = operatingPlatforms;
    }


    /**
     * Return the collection of software packages included in this operating platform.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getIncludesSoftwarePackages()
    {
        return includesSoftwarePackages;
    }


    /**
     * Set up the collection of software packages included in this operating platform.
     *
     * @param includesSoftwarePackages list of related elements
     */
    public void setIncludesSoftwarePackages(List<RelatedMetadataElementSummary> includesSoftwarePackages)
    {
        this.includesSoftwarePackages = includesSoftwarePackages;
    }


    /**
     * Return the operating platforms that package this collection of software packages.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getPackagedInOperatingPlatforms()
    {
        return packagedInOperatingPlatforms;
    }


    /**
     * Set up the operating platforms that package this collection of software packages.
     *
     * @param packagedInOperatingPlatforms list of related elements
     */
    public void setPackagedInOperatingPlatforms(List<RelatedMetadataElementSummary> packagedInOperatingPlatforms)
    {
        this.packagedInOperatingPlatforms = packagedInOperatingPlatforms;
    }


    /**
     * Return the collection of software packages that this asset depends on.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getDependsOnSoftwarePackages()
    {
        return dependsOnSoftwarePackages;
    }


    /**
     * Set up the collection of software packages that this asset depends on.
     *
     * @param dependsOnSoftwarePackages list of related elements
     */
    public void setDependsOnSoftwarePackages(List<RelatedMetadataElementSummary> dependsOnSoftwarePackages)
    {
        this.dependsOnSoftwarePackages = dependsOnSoftwarePackages;
    }


    /**
     * Return the assets that use this collection of software packages.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getRunningWithAsset()
    {
        return runningWithAsset;
    }


    /**
     * Set up the assets that use this collection of software packages.
     *
     * @param runningWithAsset list of related elements
     */
    public void setRunningWithAsset(List<RelatedMetadataElementSummary> runningWithAsset)
    {
        this.runningWithAsset = runningWithAsset;
    }


    /**
     * Return the hosts that are members of this host cluster.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getManagedHosts()
    {
        return managedHosts;
    }


    /**
     * Set up the hosts that are members of this host cluster.
     *
     * @param managedHosts list of related elements
     */
    public void setManagedHosts(List<RelatedMetadataElementSummary> managedHosts)
    {
        this.managedHosts = managedHosts;
    }


    /**
     * Return the cluster managing this host.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getHostCluster()
    {
        return hostCluster;
    }


    /**
     * Set up the cluster managing this host.
     *
     * @param hostCluster list of related elements
     */
    public void setHostCluster(List<RelatedMetadataElementSummary> hostCluster)
    {
        this.hostCluster = hostCluster;
    }


    /**
     * Return the results sets that are populated by this saved query.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getResultsStoredIn()
    {
        return resultsStoredIn;
    }


    /**
     * Set up the results sets that are populated by this saved query.
     *
     * @param resultsStoredIn list of related elements
     */
    public void setResultsStoredIn(List<RelatedMetadataElementSummary> resultsStoredIn)
    {
        this.resultsStoredIn = resultsStoredIn;
    }


    /**
     * Return the saved query that determines the members of this results set.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getPopulatedUsingQuery()
    {
        return populatedUsingQuery;
    }


    /**
     * Set up the saved query that determines the members of this results set.
     *
     * @param populatedUsingQuery list of related elements
     */
    public void setPopulatedUsingQuery(List<RelatedMetadataElementSummary> populatedUsingQuery)
    {
        this.populatedUsingQuery = populatedUsingQuery;
    }


    /**
     * Return the capabilities that are using this asset.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getConsumedByCapabilities()
    {
        return consumedByCapabilities;
    }


    /**
     * Set up the capabilities that are using this asset.
     *
     * @param consumedByCapabilities list of related elements
     */
    public void setConsumedByCapabilities(List<RelatedMetadataElementSummary> consumedByCapabilities)
    {
        this.consumedByCapabilities = consumedByCapabilities;
    }


    /**
     * Return the assets that are consumed by this software capability.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getCapabilityConsumedAssets()
    {
        return capabilityConsumedAssets;
    }


    /**
     * Set up the assets that are consumed by this software capability.
     *
     * @param capabilityConsumedAssets list of related elements
     */
    public void setCapabilityConsumedAssets(List<RelatedMetadataElementSummary> capabilityConsumedAssets)
    {
        this.capabilityConsumedAssets = capabilityConsumedAssets;
    }


    /**
     * Return the software capabilities supported by this IT asset.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getCapabilities()
    {
        return capabilities;
    }


    /**
     * Set up this software capabilities supported by this IT asset.
     *
     * @param capabilities list of related elements
     */
    public void setCapabilities(List<RelatedMetadataElementSummary> capabilities)
    {
        this.capabilities = capabilities;
    }



    /**
     * Return iT infrastructure hosting this capability.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getHostedByDeployedITInfrastructure()
    {
        return hostedByDeployedITInfrastructure;
    }

    /**
     * Set up iT infrastructure hosting this capability.
     *
     * @param hostedByDeployedITInfrastructure list of related elements
     */
    public void setHostedByDeployedITInfrastructure(List<RelatedMetadataElementSummary> hostedByDeployedITInfrastructure)
    {
        this.hostedByDeployedITInfrastructure = hostedByDeployedITInfrastructure;
    }

    /**
     * Return members of this cohort.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getCohortMembership()
    {
        return cohortMembership;
    }

    /**
     * Set up members of this cohort.
     *
     * @param cohortMembership list of related elements
     */
    public void setCohortMembership(List<RelatedMetadataElementSummary> cohortMembership)
    {
        this.cohortMembership = cohortMembership;
    }

    /**
     * Return identifies which cohorts this cohort member is registered with.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getRegisteredWithCohorts()
    {
        return registeredWithCohorts;
    }

    /**
     * Set up identifies which cohorts this cohort member is registered with.
     *
     * @param registeredWithCohorts list of related elements
     */
    public void setRegisteredWithCohorts(List<RelatedMetadataElementSummary> registeredWithCohorts)
    {
        this.registeredWithCohorts = registeredWithCohorts;
    }

    /**
     * Return endpoint callable through network.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getVisibleEndpoints()
    {
        return visibleEndpoints;
    }

    /**
     * Set up endpoint callable through network.
     *
     * @param visibleEndpoints list of related elements
     */
    public void setVisibleEndpoints(List<RelatedMetadataElementSummary> visibleEndpoints)
    {
        this.visibleEndpoints = visibleEndpoints;
    }

    public List<RelatedMetadataElementSummary> getVisibleInNetworks()
    {
        return visibleInNetworks;
    }

    public void setVisibleInNetworks(List<RelatedMetadataElementSummary> visibleInNetworks)
    {
        this.visibleInNetworks = visibleInNetworks;
    }

    public RelatedMetadataElementSummary getUserProfile()
    {
        return userProfile;
    }

    public void setUserProfile(RelatedMetadataElementSummary userProfile)
    {
        this.userProfile = userProfile;
    }


    /**
     * Return the list of user identities for this element.
     *
     * @return list of userIds
     */
    public List<RelatedMetadataElementSummary> getUserIdentities()
    {
        return userIdentities;
    }


    /**
     * Set up the list of user identities for this element.
     *
     * @param userIdentities list of userIds
     */
    public void setUserIdentities(List<RelatedMetadataElementSummary> userIdentities)
    {
        this.userIdentities = userIdentities;
    }


    /**
     * Return the contact methods for this element.
     *
     * @return list of contact methods
     */
    public List<RelatedMetadataElementSummary> getContactDetails()
    {
        return contactDetails;
    }


    /**
     * Set up the contact methods for this element.
     *
     * @param contactDetails list of contact methods
     */
    public void setContactDetails(List<RelatedMetadataElementSummary> contactDetails)
    {
        this.contactDetails = contactDetails;
    }


    /**
     * Return contact information.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getContacts()
    {
        return contacts;
    }

    /**
     * Set up contact information.
     *
     * @param contacts list of related elements
     */
    public void setContacts(List<RelatedMetadataElementSummary> contacts)
    {
        this.contacts = contacts;
    }


    /**
     * Return list of people who have created a peer network connection to me.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getMyFollowers()
    {
        return myFollowers;
    }

    /**
     * Set up list of people who have created a peer network connection to me.
     *
     * @param myFollowers list of related elements
     */
    public void setMyFollowers(List<RelatedMetadataElementSummary> myFollowers)
    {
        this.myFollowers = myFollowers;
    }

    /**
     * Return list of this person's peer network.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getMyPeers()
    {
        return myPeers;
    }

    /**
     * Set up list of this person's peer network.
     *
     * @param myPeers list of related elements
     */
    public void setMyPeers(List<RelatedMetadataElementSummary> myPeers)
    {
        this.myPeers = myPeers;
    }

    /**
     * Return the aggregating team.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getSuperTeam()
    {
        return superTeam;
    }

    /**
     * Set up the aggregating team.
     *
     * @param superTeam related element
     */
    public void setSuperTeam(RelatedMetadataElementSummary superTeam)
    {
        this.superTeam = superTeam;
    }

    public List<RelatedMetadataElementSummary> getSubTeams()
    {
        return subTeams;
    }

    public void setSubTeams(List<RelatedMetadataElementSummary> subTeams)
    {
        this.subTeams = subTeams;
    }

    public List<RelatedMetadataElementSummary> getProfilesForAsset()
    {
        return profilesForAsset;
    }

    public void setProfilesForAsset(List<RelatedMetadataElementSummary> profilesForAsset)
    {
        this.profilesForAsset = profilesForAsset;
    }

    public List<RelatedMetadataElementSummary> getAssetsUsingProfile()
    {
        return assetsUsingProfile;
    }

    public void setAssetsUsingProfile(List<RelatedMetadataElementSummary> assetsUsingProfile)
    {
        this.assetsUsingProfile = assetsUsingProfile;
    }

    /**
     * Return roles performed by this person.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getPerformsRoles()
    {
        return performsRoles;
    }

    /**
     * Set up roles performed by this person.
     *
     * @param performsRoles list of related elements
     */
    public void setPerformsRoles(List<RelatedMetadataElementSummary> performsRoles)
    {
        this.performsRoles = performsRoles;
    }

    /**
     * Return the people performing this role.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getRolePerformers()
    {
        return rolePerformers;
    }

    /**
     * Set up the people performing this role.
     *
     * @param rolePerformers list of related elements
     */
    public void setRolePerformers(List<RelatedMetadataElementSummary> rolePerformers)
    {
        this.rolePerformers = rolePerformers;
    }

    /**
     * Return the glossary terms linked by semantic assignment.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getMeanings()
    {
        return meanings;
    }


    /**
     * Set up the glossary terms linked by semantic assignment.
     *
     * @param meanings list
     */
    public void setMeanings(List<RelatedMetadataElementSummary> meanings)
    {
        this.meanings = meanings;
    }


    /**
     * Return the list of likes for this element.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getLikes()
    {
        return likes;
    }


    /**
     * Set up the list of likes for this element.
     *
     * @param likes list
     */
    public void setLikes(List<RelatedMetadataElementSummary> likes)
    {
        this.likes = likes;
    }


    public RelatedMetadataElementSummary getLikedElement()
    {
        return likedElement;
    }

    public void setLikedElement(RelatedMetadataElementSummary likedElement)
    {
        this.likedElement = likedElement;
    }

    /**
     * Return the attached informal tags.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getInformalTags()
    {
        return informalTags;
    }


    /**
     * Set up the attached informal tags.
     *
     * @param informalTags list
     */
    public void setInformalTags(List<RelatedMetadataElementSummary> informalTags)
    {
        this.informalTags = informalTags;
    }

    public List<RelatedMetadataElementSummary> getTaggedElements()
    {
        return taggedElements;
    }

    public void setTaggedElements(List<RelatedMetadataElementSummary> taggedElements)
    {
        this.taggedElements = taggedElements;
    }

    /**
     * Return the attached search keywords.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getSearchKeywords()
    {
        return searchKeywords;
    }


    /**
     * Set up the attached search keywords.
     *
     * @param searchKeywords list
     */
    public void setSearchKeywords(List<RelatedMetadataElementSummary> searchKeywords)
    {
        this.searchKeywords = searchKeywords;
    }


    public List<RelatedMetadataElementSummary> getKeywordElements()
    {
        return keywordElements;
    }

    public void setKeywordElements(List<RelatedMetadataElementSummary> keywordElements)
    {
        this.keywordElements = keywordElements;
    }

    public RelatedMetadataElementSummary getActionSource()
    {
        return actionSource;
    }

    public void setActionSource(RelatedMetadataElementSummary actionSource)
    {
        this.actionSource = actionSource;
    }

    public List<RelatedMetadataElementSummary> getRequestedActions()
    {
        return requestedActions;
    }

    public void setRequestedActions(List<RelatedMetadataElementSummary> requestedActions)
    {
        this.requestedActions = requestedActions;
    }

    /**
     * Return the reason that the action is required.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getActionCause()
    {
        return actionCause;
    }

    /**
     * Set up the reason that the action is required.
     *
     * @param actionCause list of related elements
     */
    public void setActionCause(List<RelatedMetadataElementSummary> actionCause)
    {
        this.actionCause = actionCause;
    }

    /**
     * Return actions related to this element.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getRelatedActions()
    {
        return relatedActions;
    }

    /**
     * Set up actions related to this element.
     *
     * @param relatedActions list of related elements
     */
    public void setRelatedActions(List<RelatedMetadataElementSummary> relatedActions)
    {
        this.relatedActions = relatedActions;
    }

    public List<RelatedMetadataElementSummary> getActionTargets()
    {
        return actionTargets;
    }

    public void setActionTargets(List<RelatedMetadataElementSummary> actionTargets)
    {
        this.actionTargets = actionTargets;
    }

    public List<RelatedMetadataElementSummary> getActionsForTarget()
    {
        return actionsForTarget;
    }

    public void setActionsForTarget(List<RelatedMetadataElementSummary> actionsForTarget)
    {
        this.actionsForTarget = actionsForTarget;
    }

    /**
     * Return the attached comments.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getComments()
    {
        return comments;
    }

    /**
     * Set up the attached comments.
     *
     * @param comments list
     */
    public void setComments(List<RelatedMetadataElementSummary> comments)
    {
        this.comments = comments;
    }


    public RelatedMetadataElementSummary getCommentedOnElement()
    {
        return commentedOnElement;
    }

    public void setCommentedOnElement(RelatedMetadataElementSummary commentedOnElement)
    {
        this.commentedOnElement = commentedOnElement;
    }

    /**
     * Return the attached reviews (ratings).
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getReviews()
    {
        return reviews;
    }


    /**
     * Set up the attached reviews (ratings).
     *
     * @param reviews list
     */
    public void setReviews(List<RelatedMetadataElementSummary> reviews)
    {
        this.reviews = reviews;
    }


    public RelatedMetadataElementSummary getReviewedElement()
    {
        return reviewedElement;
    }

    public void setReviewedElement(RelatedMetadataElementSummary reviewedElement)
    {
        this.reviewedElement = reviewedElement;
    }

    /**
     * Return questions that now has an accepted answer.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getAnsweredQuestions()
    {
        return answeredQuestions;
    }

    /**
     * Set up questions that now has an accepted answer.
     *
     * @param answeredQuestions list of related elements
     */
    public void setAnsweredQuestions(List<RelatedMetadataElementSummary> answeredQuestions)
    {
        this.answeredQuestions = answeredQuestions;
    }

    /**
     * Return accumulated answers.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getAcceptedAnswers()
    {
        return acceptedAnswers;
    }

    /**
     * Set up accumulated answers.
     *
     * @param acceptedAnswers list of related elements
     */
    public void setAcceptedAnswers(List<RelatedMetadataElementSummary> acceptedAnswers)
    {
        this.acceptedAnswers = acceptedAnswers;
    }

    public List<RelatedMetadataElementSummary> getCrowdSourcingContributors()
    {
        return crowdSourcingContributors;
    }

    public void setCrowdSourcingContributors(List<RelatedMetadataElementSummary> crowdSourcingContributors)
    {
        this.crowdSourcingContributors = crowdSourcingContributors;
    }

    public List<RelatedMetadataElementSummary> getCrowdSourcedContributions()
    {
        return crowdSourcedContributions;
    }

    public void setCrowdSourcedContributions(List<RelatedMetadataElementSummary> crowdSourcedContributions)
    {
        this.crowdSourcedContributions = crowdSourcedContributions;
    }

    /**
     * Return elements that this note log carries information for.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getNoteLogSubjects()
    {
        return noteLogSubjects;
    }

    /**
     * Set up elements that this note log carries information for.
     *
     * @param noteLogSubjects list of related elements
     */
    public void setNoteLogSubjects(List<RelatedMetadataElementSummary> noteLogSubjects)
    {
        this.noteLogSubjects = noteLogSubjects;
    }

    /**
     * Return log of related notes.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getNoteLogs()
    {
        return noteLogs;
    }

    /**
     * Set up log of related notes.
     *
     * @param noteLogs list of related elements
     */
    public void setNoteLogs(List<RelatedMetadataElementSummary> noteLogs)
    {
        this.noteLogs = noteLogs;
    }

    /**
     * Return logs that this entry is linked to.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getPresentInNoteLogs()
    {
        return presentInNoteLogs;
    }

    /**
     * Set up logs that this entry is linked to.
     *
     * @param presentInNoteLogs list of related elements
     */
    public void setPresentInNoteLogs(List<RelatedMetadataElementSummary> presentInNoteLogs)
    {
        this.presentInNoteLogs = presentInNoteLogs;
    }

    /**
     * Return accumulated notifications.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getNoteLogEntries()
    {
        return noteLogEntries;
    }

    /**
     * Set up accumulated notifications.
     *
     * @param noteLogEntries list of related elements
     */
    public void setNoteLogEntries(List<RelatedMetadataElementSummary> noteLogEntries)
    {
        this.noteLogEntries = noteLogEntries;
    }

    /**
     * Return elements linked via the resource list relationship.
     *
     * @return list of related element summaries
     */
    public List<RelatedMetadataElementSummary> getResourceList()
    {
        return resourceList;
    }


    /**
     * Set up elements linked via the resource list relationship.
     *
     * @param resourceList list of related element summaries
     */
    public void setResourceList(List<RelatedMetadataElementSummary> resourceList)
    {
        this.resourceList = resourceList;
    }


    public List<RelatedMetadataElementSummary> getResourceListUsers()
    {
        return resourceListUsers;
    }

    public void setResourceListUsers(List<RelatedMetadataElementSummary> resourceListUsers)
    {
        this.resourceListUsers = resourceListUsers;
    }

    /**
     * Return connections using this connector type.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getConnections()
    {
        return connections;
    }

    /**
     * Set up connections using this connector type.
     *
     * @param connections list of related elements
     */
    public void setConnections(List<RelatedMetadataElementSummary> connections)
    {
        this.connections = connections;
    }

    /**
     * Set up the connector type properties for this Connection.
     *
     * @param connectorType ConnectorType properties object
     */
    public void setConnectorType(RelatedMetadataElementSummary connectorType)
    {
        this.connectorType = connectorType;
    }


    /**
     * Returns a copy of the properties for this connection's connector type.
     * A null means there is no connection type.
     *
     * @return connector type for the connection
     */
    public RelatedMetadataElementSummary getConnectorType()
    {
        return connectorType;
    }


    /**
     * Set up the endpoint properties for this Connection.
     *
     * @param endpoint Endpoint properties object
     */
    public void setEndpoint(RelatedMetadataElementSummary endpoint)
    {
        this.endpoint = endpoint;
    }


    /**
     * Returns a copy of the properties for this connection's endpoint.
     * Null means no endpoint information available.
     *
     * @return endpoint for the connection
     */
    public RelatedMetadataElementSummary getEndpoint()
    {
        return endpoint;
    }


    /**
     * Return the elements whose digital resources are reached through this connection.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getConnectedResources()
    {
        return connectedResources;
    }


    /**
     * Set up the elements whose digital resources are reached through this connection.
     *
     * @param connectedResources list
     */
    public void setConnectedResources(List<RelatedMetadataElementSummary> connectedResources)
    {
        this.connectedResources = connectedResources;
    }


    /**
     * Return the list of embedded connections for this virtual connection.
     *
     * @return list of EmbeddedConnection objects
     */
    public List<RelatedMetadataElementSummary> getEmbeddedConnections()
    {
        return embeddedConnections;
    }


    /**
     * Set up the list of embedded connections for this virtual connection.
     *
     * @param embeddedConnections list of EmbeddedConnection objects
     */
    public void setEmbeddedConnections(List<RelatedMetadataElementSummary> embeddedConnections)
    {
        this.embeddedConnections = embeddedConnections;
    }


    /**
     * Return the list of connections that this connection is embedded in.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getParentConnections()
    {
        return parentConnections;
    }


    /**
     * Set up the list of connections that this connection is embedded in.
     *
     * @param parentConnections list
     */
    public void setParentConnections(List<RelatedMetadataElementSummary> parentConnections)
    {
        this.parentConnections = parentConnections;
    }

    /**
     * Return data sets that use this asset.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSupportedDataSets()
    {
        return supportedDataSets;
    }

    /**
     * Set up data sets that use this asset.
     *
     * @param supportedDataSets list of related elements
     */
    public void setSupportedDataSets(List<RelatedMetadataElementSummary> supportedDataSets)
    {
        this.supportedDataSets = supportedDataSets;
    }

    public List<RelatedMetadataElementSummary> getDataSetContent()
    {
        return dataSetContent;
    }

    public void setDataSetContent(List<RelatedMetadataElementSummary> dataSetContent)
    {
        this.dataSetContent = dataSetContent;
    }

    public List<RelatedMetadataElementSummary> getAPIEndpoints()
    {
        return apiEndpoints;
    }

    public void setAPIEndpoints(List<RelatedMetadataElementSummary> apiEndpoints)
    {
        this.apiEndpoints = apiEndpoints;
    }

    /**
     * Return aPIs that can be called from this endpoint.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSupportedAPIs()
    {
        return supportedAPIs;
    }

    /**
     * Set up aPIs that can be called from this endpoint.
     *
     * @param supportedAPIs list of related elements
     */
    public void setSupportedAPIs(List<RelatedMetadataElementSummary> supportedAPIs)
    {
        this.supportedAPIs = supportedAPIs;
    }

    public List<RelatedMetadataElementSummary> getParentProcesses()
    {
        return parentProcesses;
    }

    public void setParentProcesses(List<RelatedMetadataElementSummary> parentProcesses)
    {
        this.parentProcesses = parentProcesses;
    }

    public List<RelatedMetadataElementSummary> getChildProcesses()
    {
        return childProcesses;
    }

    public void setChildProcesses(List<RelatedMetadataElementSummary> childProcesses)
    {
        this.childProcesses = childProcesses;
    }

    /**
     * Return port to the process.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getPorts()
    {
        return ports;
    }

    /**
     * Set up port to the process.
     *
     * @param ports list of related elements
     */
    public void setPorts(List<RelatedMetadataElementSummary> ports)
    {
        this.ports = ports;
    }

    public List<RelatedMetadataElementSummary> getPortOwningProcesses()
    {
        return portOwningProcesses;
    }

    public void setPortOwningProcesses(List<RelatedMetadataElementSummary> portOwningProcesses)
    {
        this.portOwningProcesses = portOwningProcesses;
    }

    public List<RelatedMetadataElementSummary> getPortDelegatingFrom()
    {
        return portDelegatingFrom;
    }

    public void setPortDelegatingFrom(List<RelatedMetadataElementSummary> portDelegatingFrom)
    {
        this.portDelegatingFrom = portDelegatingFrom;
    }

    public RelatedMetadataElementSummary getPortDelegatingTo()
    {
        return portDelegatingTo;
    }

    public void setPortDelegatingTo(RelatedMetadataElementSummary portDelegatingTo)
    {
        this.portDelegatingTo = portDelegatingTo;
    }

    /**
     * Return identifies the containing folder of this datafile.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getHomeFolder()
    {
        return homeFolder;
    }

    /**
     * Set up identifies the containing folder of this datafile.
     *
     * @param homeFolder related element
     */
    public void setHomeFolder(RelatedMetadataElementSummary homeFolder)
    {
        this.homeFolder = homeFolder;
    }

    /**
     * Return files stored in this folder.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getNestedFiles()
    {
        return nestedFiles;
    }

    /**
     * Set up files stored in this folder.
     *
     * @param nestedFiles list of related elements
     */
    public void setNestedFiles(List<RelatedMetadataElementSummary> nestedFiles)
    {
        this.nestedFiles = nestedFiles;
    }

    /**
     * Return files linked to the folder.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getLinkedFiles()
    {
        return linkedFiles;
    }

    /**
     * Set up files linked to the folder.
     *
     * @param linkedFiles list of related elements
     */
    public void setLinkedFiles(List<RelatedMetadataElementSummary> linkedFiles)
    {
        this.linkedFiles = linkedFiles;
    }

    /**
     * Return folders that this file is linked to.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getLinkedFolders()
    {
        return linkedFolders;
    }

    /**
     * Set up folders that this file is linked to.
     *
     * @param linkedFolders list of related elements
     */
    public void setLinkedFolders(List<RelatedMetadataElementSummary> linkedFolders)
    {
        this.linkedFolders = linkedFolders;
    }

    /**
     * Return parent folder.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getParentFolder()
    {
        return parentFolder;
    }

    /**
     * Set up parent folder.
     *
     * @param parentFolder related element
     */
    public void setParentFolder(RelatedMetadataElementSummary parentFolder)
    {
        this.parentFolder = parentFolder;
    }

    public List<RelatedMetadataElementSummary> getNestedFolders()
    {
        return nestedFolders;
    }

    public void setNestedFolders(List<RelatedMetadataElementSummary> nestedFolders)
    {
        this.nestedFolders = nestedFolders;
    }

    /**
     * Return link to related media files.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getLinkedMediaFiles()
    {
        return linkedMediaFiles;
    }

    /**
     * Set up link to related media files.
     *
     * @param linkedMediaFiles list of related elements
     */
    public void setLinkedMediaFiles(List<RelatedMetadataElementSummary> linkedMediaFiles)
    {
        this.linkedMediaFiles = linkedMediaFiles;
    }

    /**
     * Return destinations for log records.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getAssociatedLogs()
    {
        return associatedLogs;
    }

    /**
     * Set up destinations for log records.
     *
     * @param associatedLogs list of related elements
     */
    public void setAssociatedLogs(List<RelatedMetadataElementSummary> associatedLogs)
    {
        this.associatedLogs = associatedLogs;
    }

    public List<RelatedMetadataElementSummary> getAssociatedLogSubjects()
    {
        return associatedLogSubjects;
    }

    public void setAssociatedLogSubjects(List<RelatedMetadataElementSummary> associatedLogSubjects)
    {
        this.associatedLogSubjects = associatedLogSubjects;
    }


    public RelatedMetadataElementSummary getArchiveContents()
    {
        return archiveContents;
    }

    public void setArchiveContents(RelatedMetadataElementSummary archiveContents)
    {
        this.archiveContents = archiveContents;
    }

    /**
     * Return associated archive file.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getPackagedInArchiveFiles()
    {
        return packagedInArchiveFiles;
    }

    /**
     * Set up associated archive file.
     *
     * @param packagedInArchiveFiles list of related elements
     */
    public void setPackagedInArchiveFiles(List<RelatedMetadataElementSummary> packagedInArchiveFiles)
    {
        this.packagedInArchiveFiles = packagedInArchiveFiles;
    }

    /**
     * Return the creator of the report.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getReportOriginator()
    {
        return reportOriginator;
    }

    /**
     * Set up the creator of the report.
     *
     * @param reportOriginator related element
     */
    public void setReportOriginator(RelatedMetadataElementSummary reportOriginator)
    {
        this.reportOriginator = reportOriginator;
    }

    /**
     * Return reports generated by this element.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getGeneratedReports()
    {
        return generatedReports;
    }

    /**
     * Set up reports generated by this element.
     *
     * @param generatedReports list of related elements
     */
    public void setGeneratedReports(List<RelatedMetadataElementSummary> generatedReports)
    {
        this.generatedReports = generatedReports;
    }

    /**
     * Return the subjects that the report describes.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getReportSubjects()
    {
        return reportSubjects;
    }

    /**
     * Set up the subjects that the report describes.
     *
     * @param reportSubjects list of related elements
     */
    public void setReportSubjects(List<RelatedMetadataElementSummary> reportSubjects)
    {
        this.reportSubjects = reportSubjects;
    }

    /**
     * Return reports about this element.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getReports()
    {
        return reports;
    }

    /**
     * Set up reports about this element.
     *
     * @param reports list of related elements
     */
    public void setReports(List<RelatedMetadataElementSummary> reports)
    {
        this.reports = reports;
    }

    /**
     * Return the reports that came before.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getPriorReports()
    {
        return priorReports;
    }

    /**
     * Set up the reports that came before.
     *
     * @param priorReports list of related elements
     */
    public void setPriorReports(List<RelatedMetadataElementSummary> priorReports)
    {
        this.priorReports = priorReports;
    }

    /**
     * Return reports that provide later information.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getFollowOnReports()
    {
        return followOnReports;
    }

    /**
     * Set up reports that provide later information.
     *
     * @param followOnReports list of related elements
     */
    public void setFollowOnReports(List<RelatedMetadataElementSummary> followOnReports)
    {
        this.followOnReports = followOnReports;
    }



    /**
     * Return the related terms.
     *
     * @return list of terms
     */
    public List<RelatedMetadataElementSummary> getRelatedTerms()
    {
        return relatedTerms;
    }


    /**
     * Set up the related terms.
     *
     * @param relatedToTerms list of terms
     */
    public void setRelatedTerms(List<RelatedMetadataElementSummary> relatedToTerms)
    {
        this.relatedTerms = relatedToTerms;
    }

    /**
     * Return elements describing the contexts where this term is used.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getUsedInContexts()
    {
        return usedInContexts;
    }

    /**
     * Set up elements describing the contexts where this term is used.
     *
     * @param usedInContexts list of related elements
     */
    public void setUsedInContexts(List<RelatedMetadataElementSummary> usedInContexts)
    {
        this.usedInContexts = usedInContexts;
    }

    /**
     * Return glossary terms used in this specific context.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getContextRelevantTerms()
    {
        return contextRelevantTerms;
    }

    /**
     * Set up glossary terms used in this specific context.
     *
     * @param contextRelevantTerms list of related elements
     */
    public void setContextRelevantTerms(List<RelatedMetadataElementSummary> contextRelevantTerms)
    {
        this.contextRelevantTerms = contextRelevantTerms;
    }

    /**
     * Return the data definitions that are linked to this glossary term via the semantic definition relationship.
     *
     * @return list of data definitions
     */
    public List<RelatedMetadataElementSummary> getSemanticallyAssociatedDefinitions()
    {
        return semanticallyAssociatedDefinitions;
    }


    /**
     * Set up the data definitions that are linked to this glossary term via the semantic definition relationship.
     *
     * @param semanticallyAssociatedDefinitions list of data definitions
     */
    public void setSemanticallyAssociatedDefinitions(List<RelatedMetadataElementSummary> semanticallyAssociatedDefinitions)
    {
        this.semanticallyAssociatedDefinitions = semanticallyAssociatedDefinitions;
    }

    public List<RelatedMetadataElementSummary> getSemanticDefinitions()
    {
        return semanticDefinitions;
    }

    public void setSemanticDefinitions(List<RelatedMetadataElementSummary> semanticDefinitions)
    {
        this.semanticDefinitions = semanticDefinitions;
    }

    public List<RelatedMetadataElementSummary> getMeaningForDataElements()
    {
        return meaningForDataElements;
    }

    public void setMeaningForDataElements(List<RelatedMetadataElementSummary> meaningForDataElements)
    {
        this.meaningForDataElements = meaningForDataElements;
    }


    /**
     * Return any attached supplementary properties.
     *
     * @return list of glossary terms providing additional descriptions of an asset.
     */
    public List<RelatedMetadataElementSummary> getSupplementaryProperties()
    {
        return supplementaryProperties;
    }


    /**
     * Set up any attached supplementary properties.
     *
     * @param supplementaryProperties  list of glossary terms providing additional descriptions of an asset.
     */
    public void setSupplementaryProperties(List<RelatedMetadataElementSummary> supplementaryProperties)
    {
        this.supplementaryProperties = supplementaryProperties;
    }

    /**
     * Return describes this technical element.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getSupplementsElement()
    {
        return supplementsElement;
    }

    /**
     * Set up describes this technical element.
     *
     * @param supplementsElement related element
     */
    public void setSupplementsElement(RelatedMetadataElementSummary supplementsElement)
    {
        this.supplementsElement = supplementsElement;
    }

    /**
     * Return any attached property facets such as vendor specific properties.
     *
     * @return list of property facets
     */
    public List<RelatedMetadataElementSummary> getPropertyFacets()
    {
        return propertyFacets;
    }


    /**
     * Set up the property facets associated with this element.
     *
     * @param propertyFacets list of property facets
     */
    public void setPropertyFacets(List<RelatedMetadataElementSummary> propertyFacets)
    {
        this.propertyFacets = propertyFacets;
    }


    /**
     * Return identifies which element this property facet belongs to.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getFacetedElements()
    {
        return facetedElements;
    }

    /**
     * Set up identifies which element this property facet belongs to.
     *
     * @param facetedElements list of related elements
     */
    public void setFacetedElements(List<RelatedMetadataElementSummary> facetedElements)
    {
        this.facetedElements = facetedElements;
    }

    /**
     * Return the lineage relationships associated with this element.
     *
     * @return list of elements linked by lineage
     */
    public List<RelatedMetadataElementSummary> getLineageLinkage()
    {
        return lineageLinkage;
    }


    /**
     * Set up the lineage relationships associated with this element.
     *
     * @param lineageRelationships list of elements linked by lineage
     */
    public void setLineageLinkage(List<RelatedMetadataElementSummary> lineageRelationships)
    {
        this.lineageLinkage = lineageRelationships;
    }


    /**
     * Return the governance definition that defines how this element is governed.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getGovernedBy()
    {
        return governedBy;
    }

    /**
     * Set up the governance definition that defines how this element is governed.
     *
     * @param governedBy list of related elements
     */
    public void setGovernedBy(List<RelatedMetadataElementSummary> governedBy)
    {
        this.governedBy = governedBy;
    }



    /**
     * Return an element that is governed according to the governance definition.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getGovernedElements()
    {
        return governedElements;
    }

    /**
     * Set up an element that is governed according to the governance definition.
     *
     * @param governedElements list of related elements
     */
    public void setGovernedElements(List<RelatedMetadataElementSummary> governedElements)
    {
        this.governedElements = governedElements;
    }

    public List<RelatedMetadataElementSummary> getPeerGovernanceDefinitions()
    {
        return peerGovernanceDefinitions;
    }

    public void setPeerGovernanceDefinitions(List<RelatedMetadataElementSummary> peerGovernanceDefinitions)
    {
        this.peerGovernanceDefinitions = peerGovernanceDefinitions;
    }

    public List<RelatedMetadataElementSummary> getSupportedGovernanceDefinitions()
    {
        return supportedGovernanceDefinitions;
    }

    public void setSupportedGovernanceDefinitions(List<RelatedMetadataElementSummary> supportedGovernanceDefinitions)
    {
        this.supportedGovernanceDefinitions = supportedGovernanceDefinitions;
    }

    public List<RelatedMetadataElementSummary> getSupportingGovernanceDefinitions()
    {
        return supportingGovernanceDefinitions;
    }

    public void setSupportingGovernanceDefinitions(List<RelatedMetadataElementSummary> supportingGovernanceDefinitions)
    {
        this.supportingGovernanceDefinitions = supportingGovernanceDefinitions;
    }

    public List<RelatedMetadataElementSummary> getConfiguredInSecurityCollections()
    {
        return configuredInSecurityCollections;
    }

    public void setConfiguredInSecurityCollections(List<RelatedMetadataElementSummary> configuredInSecurityCollections)
    {
        this.configuredInSecurityCollections = configuredInSecurityCollections;
    }

    /**
     * Return the users defined in the secrets collection.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getUserAccounts()
    {
        return userAccounts;
    }

    /**
     * Set up the users defined in the secrets collection.
     *
     * @param userAccounts list of related elements
     */
    public void setUserAccounts(List<RelatedMetadataElementSummary> userAccounts)
    {
        this.userAccounts = userAccounts;
    }

    /**
     * Return a secrets collection where the security access control is defined.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getDefinedInSecretsCollection()
    {
        return definedInSecretsCollection;
    }

    /**
     * Set up a secrets collection where the security access control is defined.
     *
     * @param definedInSecretsCollection list of related elements
     */
    public void setDefinedInSecretsCollection(List<RelatedMetadataElementSummary> definedInSecretsCollection)
    {
        this.definedInSecretsCollection = definedInSecretsCollection;
    }

    /**
     * Return the security access controls defined in the secrets collection.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSecurityAccessControls()
    {
        return securityAccessControls;
    }

    /**
     * Set up the security access controls defined in the secrets collection.
     *
     * @param securityAccessControls list of related elements
     */
    public void setSecurityAccessControls(List<RelatedMetadataElementSummary> securityAccessControls)
    {
        this.securityAccessControls = securityAccessControls;
    }

    /**
     * Return a secrets collection where the security list is defined.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getListedInSecretsCollection()
    {
        return listedInSecretsCollection;
    }

    /**
     * Set up a secrets collection where the security list is defined.
     *
     * @param listedInSecretsCollection list of related elements
     */
    public void setListedInSecretsCollection(List<RelatedMetadataElementSummary> listedInSecretsCollection)
    {
        this.listedInSecretsCollection = listedInSecretsCollection;
    }

    /**
     * Return the security lists defined in the secrets collection.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSecurityLists()
    {
        return securityLists;
    }

    /**
     * Set up the security lists defined in the secrets collection.
     *
     * @param securityLists list of related elements
     */
    public void setSecurityLists(List<RelatedMetadataElementSummary> securityLists)
    {
        this.securityLists = securityLists;
    }

    /**
     * Return an access control definition that uses the security group.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getUsedInAccessControls()
    {
        return usedInAccessControls;
    }

    /**
     * Set up an access control definition that uses the security group.
     *
     * @param usedInAccessControls list of related elements
     */
    public void setUsedInAccessControls(List<RelatedMetadataElementSummary> usedInAccessControls)
    {
        this.usedInAccessControls = usedInAccessControls;
    }

    /**
     * Return the security groups to use to validate access for the operation.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getAssociatedSecurityLists()
    {
        return associatedSecurityLists;
    }

    /**
     * Set up the security groups to use to validate access for the operation.
     *
     * @param associatedSecurityLists list of related elements
     */
    public void setAssociatedSecurityLists(List<RelatedMetadataElementSummary> associatedSecurityLists)
    {
        this.associatedSecurityLists = associatedSecurityLists;
    }

    /**
     * Return the zone that provides additional governance requirements.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getInheritsFromZone()
    {
        return inheritsFromZone;
    }

    /**
     * Set up the zone that provides additional governance requirements.
     *
     * @param inheritsFromZone related element
     */
    public void setInheritsFromZone(RelatedMetadataElementSummary inheritsFromZone)
    {
        this.inheritsFromZone = inheritsFromZone;
    }

    public List<RelatedMetadataElementSummary> getControlsZones()
    {
        return controlsZones;
    }

    public void setControlsZones(List<RelatedMetadataElementSummary> controlsZones)
    {
        this.controlsZones = controlsZones;
    }


    /**
     * Return the subject area that describes a broader topic.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getBroaderSubjectArea()
    {
        return broaderSubjectArea;
    }

    /**
     * Set up the subject area that describes a broader topic.
     *
     * @param broaderSubjectArea related element
     */
    public void setBroaderSubjectArea(RelatedMetadataElementSummary broaderSubjectArea)
    {
        this.broaderSubjectArea = broaderSubjectArea;
    }

    public List<RelatedMetadataElementSummary> getNestedSubjectAreas()
    {
        return nestedSubjectAreas;
    }

    public void setNestedSubjectAreas(List<RelatedMetadataElementSummary> nestedSubjectAreas)
    {
        this.nestedSubjectAreas = nestedSubjectAreas;
    }

    /**
     * Return the governance metrics that are captured in this data set.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getMetrics()
    {
        return metrics;
    }

    /**
     * Set up the governance metrics that are captured in this data set.
     *
     * @param metrics list of related elements
     */
    public void setMetrics(List<RelatedMetadataElementSummary> metrics)
    {
        this.metrics = metrics;
    }

    /**
     * Return the data set that captures the measurements for this governance metric.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getMeasurements()
    {
        return measurements;
    }

    /**
     * Set up the data set that captures the measurements for this governance metric.
     *
     * @param measurements list of related elements
     */
    public void setMeasurements(List<RelatedMetadataElementSummary> measurements)
    {
        this.measurements = measurements;
    }

    /**
     * Return the notifications that monitor changes around this resource.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getMonitoredThrough()
    {
        return monitoredThrough;
    }

    /**
     * Set up the notifications that monitor changes around this resource.
     *
     * @param monitoredThrough list of related elements
     */
    public void setMonitoredThrough(List<RelatedMetadataElementSummary> monitoredThrough)
    {
        this.monitoredThrough = monitoredThrough;
    }

    /**
     * Return resources that should be monitored to support this notification type.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getMonitoredResources()
    {
        return monitoredResources;
    }

    /**
     * Set up resources that should be monitored to support this notification type.
     *
     * @param monitoredResources list of related elements
     */
    public void setMonitoredResources(List<RelatedMetadataElementSummary> monitoredResources)
    {
        this.monitoredResources = monitoredResources;
    }

    /**
     * Return notification types of interest to the subscriber.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getInterestingNotificationTypes()
    {
        return interestingNotificationTypes;
    }

    /**
     * Set up notification types of interest to the subscriber.
     *
     * @param interestingNotificationTypes list of related elements
     */
    public void setInterestingNotificationTypes(List<RelatedMetadataElementSummary> interestingNotificationTypes)
    {
        this.interestingNotificationTypes = interestingNotificationTypes;
    }

    /**
     * Return subscribers interested in this notification type.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSubscribers()
    {
        return subscribers;
    }

    /**
     * Set up subscribers interested in this notification type.
     *
     * @param subscribers list of related elements
     */
    public void setSubscribers(List<RelatedMetadataElementSummary> subscribers)
    {
        this.subscribers = subscribers;
    }


    /**
     * Return the elements that are non-compliant with the associated policy.  The exception type defines the nature of the non-compliance.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getExcludedFromRequirements()
    {
        return excludedFromRequirements;
    }

    /**
     * Set up the elements that are non-compliant with the associated policy.  The exception type defines the nature of the non-compliance.
     *
     * @param excludedFromRequirements list of related elements
     */
    public void setExcludedFromRequirements(List<RelatedMetadataElementSummary> excludedFromRequirements)
    {
        this.excludedFromRequirements = excludedFromRequirements;
    }

    /**
     * Return types of exception assigned to this element.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getExceptions()
    {
        return exceptions;
    }

    /**
     * Set up types of exception assigned to this element.
     *
     * @param exceptions list of related elements
     */
    public void setExceptions(List<RelatedMetadataElementSummary> exceptions)
    {
        this.exceptions = exceptions;
    }

    /**
     * Return governance Engine making use of the governance service.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getCalledFromGovernanceEngines()
    {
        return calledFromGovernanceEngines;
    }

    /**
     * Set up governance Engine making use of the governance service.
     *
     * @param calledFromGovernanceEngines list of related elements
     */
    public void setCalledFromGovernanceEngines(List<RelatedMetadataElementSummary> calledFromGovernanceEngines)
    {
        this.calledFromGovernanceEngines = calledFromGovernanceEngines;
    }

    /**
     * Return governance service that is part of the governance engine.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSupportedGovernanceServices()
    {
        return supportedGovernanceServices;
    }

    /**
     * Set up governance service that is part of the governance engine.
     *
     * @param supportedGovernanceServices list of related elements
     */
    public void setSupportedGovernanceServices(List<RelatedMetadataElementSummary> supportedGovernanceServices)
    {
        this.supportedGovernanceServices = supportedGovernanceServices;
    }


    public List<RelatedMetadataElementSummary> getAssociatedGovernanceActions()
    {
        return associatedGovernanceActions;
    }

    public void setAssociatedGovernanceActions(List<RelatedMetadataElementSummary> associatedGovernanceActions)
    {
        this.associatedGovernanceActions = associatedGovernanceActions;
    }

    /**
     * Return provides a fixed target for action that will be used when this governance action executes.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getPredefinedTargetForAction()
    {
        return predefinedTargetForAction;
    }

    /**
     * Set up provides a fixed target for action that will be used when this governance action executes.
     *
     * @param predefinedTargetForAction list of related elements
     */
    public void setPredefinedTargetForAction(List<RelatedMetadataElementSummary> predefinedTargetForAction)
    {
        this.predefinedTargetForAction = predefinedTargetForAction;
    }

    /**
     * Return governance process that describes the set of process steps.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getTriggeredFrom()
    {
        return triggeredFrom;
    }

    /**
     * Set up governance process that describes the set of process steps.
     *
     * @param triggeredFrom list of related elements
     */
    public void setTriggeredFrom(List<RelatedMetadataElementSummary> triggeredFrom)
    {
        this.triggeredFrom = triggeredFrom;
    }

    /**
     * Return first step to execute in a governance action process.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getFirstStep()
    {
        return firstStep;
    }

    /**
     * Set up first step to execute in a governance action process.
     *
     * @param firstStep related element
     */
    public void setFirstStep(RelatedMetadataElementSummary firstStep)
    {
        this.firstStep = firstStep;
    }

    /**
     * Return governance Action Process Step caller.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getDependedOnProcessSteps()
    {
        return dependedOnProcessSteps;
    }

    /**
     * Set up governance Action Process Step caller.
     *
     * @param dependedOnProcessSteps list of related elements
     */
    public void setDependedOnProcessSteps(List<RelatedMetadataElementSummary> dependedOnProcessSteps)
    {
        this.dependedOnProcessSteps = dependedOnProcessSteps;
    }

    /**
     * Return governance Action Process Step called.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getFollowOnProcessSteps()
    {
        return followOnProcessSteps;
    }

    /**
     * Set up governance Action Process Step called.
     *
     * @param followOnProcessSteps list of related elements
     */
    public void setFollowOnProcessSteps(List<RelatedMetadataElementSummary> followOnProcessSteps)
    {
        this.followOnProcessSteps = followOnProcessSteps;
    }

    /**
     * Return governance action that drives calls to a governance engine.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSupportsGovernanceActions()
    {
        return supportsGovernanceActions;
    }

    /**
     * Set up governance action that drives calls to a governance engine.
     *
     * @param supportsGovernanceActions list of related elements
     */
    public void setSupportsGovernanceActions(List<RelatedMetadataElementSummary> supportsGovernanceActions)
    {
        this.supportsGovernanceActions = supportsGovernanceActions;
    }

    /**
     * Return governance engine that will run the requested action.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getGovernanceActionExecutor()
    {
        return governanceActionExecutor;
    }

    /**
     * Set up governance engine that will run the requested action.
     *
     * @param governanceActionExecutor related element
     */
    public void setGovernanceActionExecutor(RelatedMetadataElementSummary governanceActionExecutor)
    {
        this.governanceActionExecutor = governanceActionExecutor;
    }

    /**
     * Return an integration group that this integration connector is a member of.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getIncludedInIntegrationGroups()
    {
        return includedInIntegrationGroups;
    }

    /**
     * Set up an integration group that this integration connector is a member of.
     *
     * @param includedInIntegrationGroups list of related elements
     */
    public void setIncludedInIntegrationGroups(List<RelatedMetadataElementSummary> includedInIntegrationGroups)
    {
        this.includedInIntegrationGroups = includedInIntegrationGroups;
    }

    public List<RelatedMetadataElementSummary> getSupportedIntegrationConnectors()
    {
        return supportedIntegrationConnectors;
    }

    public void setSupportedIntegrationConnectors(List<RelatedMetadataElementSummary> supportedIntegrationConnectors)
    {
        this.supportedIntegrationConnectors = supportedIntegrationConnectors;
    }

    public List<RelatedMetadataElementSummary> getRefreshedByConnectors()
    {
        return refreshedByConnectors;
    }

    public void setRefreshedByConnectors(List<RelatedMetadataElementSummary> refreshedByConnectors)
    {
        this.refreshedByConnectors = refreshedByConnectors;
    }

    /**
     * Return an open metadata element that the integration connector is working on.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getCatalogTargets()
    {
        return catalogTargets;
    }

    /**
     * Set up an open metadata element that the integration connector is working on.
     *
     * @param catalogTargets list of related elements
     */
    public void setCatalogTargets(List<RelatedMetadataElementSummary> catalogTargets)
    {
        this.catalogTargets = catalogTargets;
    }

    /**
     * Return oldest element.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getPeerDuplicateOrigin()
    {
        return peerDuplicateOrigin;
    }

    /**
     * Set up oldest element.
     *
     * @param peerDuplicateOrigin list of related elements
     */
    public void setPeerDuplicateOrigin(List<RelatedMetadataElementSummary> peerDuplicateOrigin)
    {
        this.peerDuplicateOrigin = peerDuplicateOrigin;
    }

    /**
     * Return newest element.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getPeerDuplicatePartner()
    {
        return peerDuplicatePartner;
    }

    /**
     * Set up newest element.
     *
     * @param peerDuplicatePartner list of related elements
     */
    public void setPeerDuplicatePartner(List<RelatedMetadataElementSummary> peerDuplicatePartner)
    {
        this.peerDuplicatePartner = peerDuplicatePartner;
    }

    /**
     * Return detected duplicate element - the source of the properties.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getConsolidatedDuplicateOrigin()
    {
        return consolidatedDuplicateOrigin;
    }

    /**
     * Set up detected duplicate element - the source of the properties.
     *
     * @param consolidatedDuplicateOrigin list of related elements
     */
    public void setConsolidatedDuplicateOrigin(List<RelatedMetadataElementSummary> consolidatedDuplicateOrigin)
    {
        this.consolidatedDuplicateOrigin = consolidatedDuplicateOrigin;
    }

    /**
     * Return element resulting from combining the duplicate entities.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getConsolidatedDuplicateResult()
    {
        return consolidatedDuplicateResult;
    }

    /**
     * Set up element resulting from combining the duplicate entities.
     *
     * @param consolidatedDuplicateResult list of related elements
     */
    public void setConsolidatedDuplicateResult(List<RelatedMetadataElementSummary> consolidatedDuplicateResult)
    {
        this.consolidatedDuplicateResult = consolidatedDuplicateResult;
    }

    /**
     * Return resources impacted by the incident.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getImpactedResources()
    {
        return impactedResources;
    }

    /**
     * Set up resources impacted by the incident.
     *
     * @param impactedResources list of related elements
     */
    public void setImpactedResources(List<RelatedMetadataElementSummary> impactedResources)
    {
        this.impactedResources = impactedResources;
    }

    /**
     * Return descriptions of incidents affecting this resource and the action taken.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getIncidentReports()
    {
        return incidentReports;
    }

    /**
     * Set up descriptions of incidents affecting this resource and the action taken.
     *
     * @param incidentReports list of related elements
     */
    public void setIncidentReports(List<RelatedMetadataElementSummary> incidentReports)
    {
        this.incidentReports = incidentReports;
    }

    /**
     * Return the types of licenses that apply.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getLicenses()
    {
        return licenses;
    }

    /**
     * Set up the types of licenses that apply.
     *
     * @param licenses list of related elements
     */
    public void setLicenses(List<RelatedMetadataElementSummary> licenses)
    {
        this.licenses = licenses;
    }

    public List<RelatedMetadataElementSummary> getLicensedElements()
    {
        return licensedElements;
    }

    public void setLicensedElements(List<RelatedMetadataElementSummary> licensedElements)
    {
        this.licensedElements = licensedElements;
    }

    /**
     * Return the types of certifications that apply.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getCertifications()
    {
        return certifications;
    }

    /**
     * Set up the types of certifications that apply.
     *
     * @param certifications list of related elements
     */
    public void setCertifications(List<RelatedMetadataElementSummary> certifications)
    {
        this.certifications = certifications;
    }

    public List<RelatedMetadataElementSummary> getCertifiedElements()
    {
        return certifiedElements;
    }

    public void setCertifiedElements(List<RelatedMetadataElementSummary> certifiedElements)
    {
        this.certifiedElements = certifiedElements;
    }


    /**
     * Return the ScopedBy relationships connected at end 2.
     *
     * @return list of relationships
     */
    public List<RelatedMetadataElementSummary> getRelevantToScopes()
    {
        return relevantToScopes;
    }

    /**
     * Set up the ScopedBy relationships connected at end 2.
     *
     * @param relevantToScopes list of relationships
     */
    public void setRelevantToScopes(List<RelatedMetadataElementSummary> relevantToScopes)
    {
        this.relevantToScopes = relevantToScopes;
    }


    /**
     * Return elements that affected by the scope.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getScopedElements()
    {
        return scopedElements;
    }

    /**
     * Set up elements that affected by the scope.
     *
     * @param scopedElements list of related elements
     */
    public void setScopedElements(List<RelatedMetadataElementSummary> scopedElements)
    {
        this.scopedElements = scopedElements;
    }

    public List<RelatedMetadataElementSummary> getAssignmentScope()
    {
        return assignmentScope;
    }

    public void setAssignmentScope(List<RelatedMetadataElementSummary> assignmentScope)
    {
        this.assignmentScope = assignmentScope;
    }

    /**
     * Return person, team, project or other type of actor that has been assigned.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getAssignedActors()
    {
        return assignedActors;
    }

    /**
     * Set up person, team, project or other type of actor that has been assigned.
     *
     * @param assignedActors list of related elements
     */
    public void setAssignedActors(List<RelatedMetadataElementSummary> assignedActors)
    {
        this.assignedActors = assignedActors;
    }

    public RelatedMetadataElementSummary getContributionRecord()
    {
        return contributionRecord;
    }

    public void setContributionRecord(RelatedMetadataElementSummary contributionRecord)
    {
        this.contributionRecord = contributionRecord;
    }

    /**
     * Return the actor profile associated via userId to the contribution.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getContributorProfile()
    {
        return contributorProfile;
    }

    /**
     * Set up the actor profile associated via userId to the contribution.
     *
     * @param contributorProfile related element
     */
    public void setContributorProfile(RelatedMetadataElementSummary contributorProfile)
    {
        this.contributorProfile = contributorProfile;
    }

    /**
     * Return the list of projects that need this project to complete.
     *
     * @return list of project summaries
     */
    public List<RelatedMetadataElementSummary> getDependentProjects()
    {
        return dependentProjects;
    }


    /**
     * Set up the list of projects that need this project to complete.
     *
     * @param dependentProjects list of project summaries
     */
    public void setDependentProjects(List<RelatedMetadataElementSummary> dependentProjects)
    {
        this.dependentProjects = dependentProjects;
    }


    /**
     * Return the list of projects that this project needs to complete.
     *
     * @return list of project summaries
     */
    public List<RelatedMetadataElementSummary> getDependsOnProjects()
    {
        return dependsOnProjects;
    }


    /**
     * Set up the list of projects that this project needs to complete.
     *
     * @param dependsOnProjects list of project summaries
     */
    public void setDependsOnProjects(List<RelatedMetadataElementSummary> dependsOnProjects)
    {
        this.dependsOnProjects = dependsOnProjects;
    }


    /**
     * Return the governance definitions that support this governance definition.
     *
     * @return list of governance definition stubs
     */
    public List<RelatedMetadataElementSummary> getManagedProjects()
    {
        return managedProjects;
    }


    /**
     * Set up the governance definitions that support this governance definition.
     *
     * @param managedProjects list of governance definition stubs
     */
    public void setManagedProjects(List<RelatedMetadataElementSummary> managedProjects)
    {
        this.managedProjects = managedProjects;
    }


    /**
     * Return parent projects.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getManagingProjects()
    {
        return managingProjects;
    }


    /**
     * Set up parent projects.
     *
     * @param managingProjects list
     */
    public void setManagingProjects(List<RelatedMetadataElementSummary> managingProjects)
    {
        this.managingProjects = managingProjects;
    }


    /**
     * Return abstract representation.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getDerivedFrom()
    {
        return derivedFrom;
    }

    /**
     * Set up abstract representation.
     *
     * @param derivedFrom list of related elements
     */
    public void setDerivedFrom(List<RelatedMetadataElementSummary> derivedFrom)
    {
        this.derivedFrom = derivedFrom;
    }

    /**
     * Return resulting refined element.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getImplementedBy()
    {
        return implementedBy;
    }

    /**
     * Set up resulting refined element.
     *
     * @param implementedBy list of related elements
     */
    public void setImplementedBy(List<RelatedMetadataElementSummary> implementedBy)
    {
        this.implementedBy = implementedBy;
    }

    /**
     * Return place where the linked resources could be used as part of an implementation.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getUsedInImplementationOf()
    {
        return usedInImplementationOf;
    }

    /**
     * Set up place where the linked resources could be used as part of an implementation.
     *
     * @param usedInImplementationOf list of related elements
     */
    public void setUsedInImplementationOf(List<RelatedMetadataElementSummary> usedInImplementationOf)
    {
        this.usedInImplementationOf = usedInImplementationOf;
    }

    /**
     * Return useful components in creating an implementation.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getImplementationResources()
    {
        return implementationResources;
    }

    /**
     * Set up useful components in creating an implementation.
     *
     * @param implementationResources list of related elements
     */
    public void setImplementationResources(List<RelatedMetadataElementSummary> implementationResources)
    {
        this.implementationResources = implementationResources;
    }


    /**
     * Return the attached schema for this asset.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getSchemaType()
    {
        return schemaType;
    }


    /**
     * Set up the attached schema for this asset.
     *
     * @param schemaType related element
     */
    public void setSchemaType(RelatedMetadataElementSummary schemaType)
    {
        this.schemaType = schemaType;
    }


    /**
     * Return the asset that this schema describes.
     *
     * @return SchemaElement
     */
    public RelatedMetadataElementSummary getDescribesStructure()
    {
        return describesStructure;
    }


    /**
     * Set up the asset that this schema describes.
     *
     * @param describesStructure SchemaElement
     */
    public void setDescribesStructure(RelatedMetadataElementSummary describesStructure)
    {
        this.describesStructure = describesStructure;
    }


    /**
     * Return the schema attributes in this schema type.
     *
     * @return String data type name
     */
    public List<RelatedMetadataElementSummary> getSchemaAttributes() { return schemaAttributes; }


    /**
     * Set up the schema attributes in this schema type
     *
     * @param schemaAttributes list
     */
    public void setSchemaAttributes(List<RelatedMetadataElementSummary> schemaAttributes)
    {
        this.schemaAttributes = schemaAttributes;
    }


    /**
     * Return the schema elements that are using this schema type.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getParentSchemaElements()
    {
        return parentSchemaElements;
    }


    /**
     * Set up the schema elements that are using this schema type.
     *
     * @param parentSchemaElements list
     */
    public void setParentSchemaElements(List<RelatedMetadataElementSummary> parentSchemaElements)
    {
        this.parentSchemaElements = parentSchemaElements;
    }


    /**
     * Return the type of schema element that represents the key or property name for the map.
     * This is also called the domain of the map.
     *
     * @return SchemaElement
     */
    public RelatedMetadataElementSummary getMapFromElement()
    {
        return mapFromElement;
    }


    /**
     * Set up the type of schema element that represents the key or property name for the map.
     * This is also called the domain of the map.
     *
     * @param mapFromElement SchemaElement
     */
    public void setMapFromElement(RelatedMetadataElementSummary mapFromElement)
    {
        this.mapFromElement = mapFromElement;
    }


    /**
     * Return the type of schema element that represents the property value for the map.
     * This is also called the range of the map.
     *
     * @return SchemaElement
     */
    public RelatedMetadataElementSummary getMapToElement()
    {
        return mapToElement;
    }


    /**
     * Set up the type of schema element that represents the property value for the map.
     * This is also called the range of the map.
     *
     * @param mapToElement SchemaType
     */
    public void setMapToElement(RelatedMetadataElementSummary mapToElement)
    {
        this.mapToElement = mapToElement;
    }


    /**
     * Return the schema type that is reusable amongst assets.
     *
     * @return bean describing external schema
     */
    public RelatedMetadataElementSummary getExternalSchemaType()
    {
        return externalSchemaType;
    }


    /**
     * Set up the schema type that is reusable amongst assets.
     *
     * @param externalSchemaType bean describing external schema
     */
    public void setExternalSchemaType(RelatedMetadataElementSummary externalSchemaType)
    {
        this.externalSchemaType = externalSchemaType;
    }


    /**
     * Return the list of alternative schema types that this attribute or asset may use.
     *
     * @return list of schema types
     */
    public List<RelatedMetadataElementSummary> getSchemaOptions()
    {
        return schemaOptions;
    }


    /**
     * Set up the list of alternative schema types that this attribute or asset may use.
     *
     * @param schemaOptions list of schema types
     */
    public void setSchemaOptions(List<RelatedMetadataElementSummary> schemaOptions)
    {
        this.schemaOptions = schemaOptions;
    }


    /**
     * Return the list of individual query targets for a derived column.
     *
     * @return list of queries and their target element
     */
    public List<RelatedMetadataElementSummary> getQueries()
    {
        return queries;
    }


    /**
     * Set up the list of individual query targets for a derived column.
     *
     * @param queries list of queries and their target element
     */
    public void setQueries(List<RelatedMetadataElementSummary> queries)
    {
        this.queries = queries;
    }


    /**
     * Return end 2 of APIResponse relationship.
     *
     * @return relationship to schema
     */
    public RelatedMetadataElementSummary getAPIResponse()
    {
        return apiResponse;
    }


    /**
     * Set up end 2 of APIResponse relationship.
     *
     * @param apiResponse relationship to schema
     */
    public void setAPIResponse(RelatedMetadataElementSummary apiResponse)
    {
        this.apiResponse = apiResponse;
    }


    /**
     * Return end 2 of APIRequest relationship.
     *
     * @return relationship to schema
     */
    public RelatedMetadataElementSummary getAPIRequest()
    {
        return apiRequest;
    }


    /**
     * Set up end 2 of APIRequest relationship.
     *
     * @param apiRequest relationship to schema
     */
    public void setAPIRequest(RelatedMetadataElementSummary apiRequest)
    {
        this.apiRequest = apiRequest;
    }


    /**
     * Return end 2 of APIHeader relationship.
     *
     * @return relationship to schema
     */
    public RelatedMetadataElementSummary getAPIHeader()
    {
        return apiHeader;
    }


    /**
     * Set up end 2 of APIHeader relationship.
     *
     * @param apiHeader relationship to schema
     */
    public void setAPIHeader(RelatedMetadataElementSummary apiHeader)
    {
        this.apiHeader = apiHeader;
    }


    /**
     * Return list of operations contained in this API.
     *
     * @return list of operations
     */
    public List<RelatedMetadataElementSummary> getContainsOperations()
    {
        return containsOperations;
    }


    /**
     * Set up list of operations contained in this API.
     *
     * @param containsOperations list of operations
     */
    public void setContainsOperations(List<RelatedMetadataElementSummary> containsOperations)
    {
        this.containsOperations = containsOperations;
    }

    /**
     * Return link to the database where this schema is located.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getDatabaseSchemaType()
    {
        return databaseSchemaType;
    }

    /**
     * Set up link to the database where this schema is located.
     *
     * @param databaseSchemaType related element
     */
    public void setDatabaseSchemaType(RelatedMetadataElementSummary databaseSchemaType)
    {
        this.databaseSchemaType = databaseSchemaType;
    }

    /**
     * Return the schemas found in the database.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getContainsDBSchemas()
    {
        return containsDBSchemas;
    }

    /**
     * Set up the schemas found in the database.
     *
     * @param containsDBSchemas list of related elements
     */
    public void setContainsDBSchemas(List<RelatedMetadataElementSummary> containsDBSchemas)
    {
        this.containsDBSchemas = containsDBSchemas;
    }

    public RelatedMetadataElementSummary getLinkedToPrimaryKey()
    {
        return linkedToPrimaryKey;
    }

    public void setLinkedToPrimaryKey(RelatedMetadataElementSummary linkedToPrimaryKey)
    {
        this.linkedToPrimaryKey = linkedToPrimaryKey;
    }

    public List<RelatedMetadataElementSummary> getForeignKeys()
    {
        return foreignKeys;
    }

    public void setForeignKeys(List<RelatedMetadataElementSummary> foreignKeys)
    {
        this.foreignKeys = foreignKeys;
    }

    /**
     * Return vertices for this edge.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getVertices()
    {
        return vertices;
    }

    /**
     * Set up vertices for this edge.
     *
     * @param vertices list of related elements
     */
    public void setVertices(List<RelatedMetadataElementSummary> vertices)
    {
        this.vertices = vertices;
    }

    /**
     * Return edges for this vertex.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getEdges()
    {
        return edges;
    }

    /**
     * Set up edges for this vertex.
     *
     * @param edges list of related elements
     */
    public void setEdges(List<RelatedMetadataElementSummary> edges)
    {
        this.edges = edges;
    }

    public List<RelatedMetadataElementSummary> getDescribedByDataValueSpecifications()
    {
        return describedByDataValueSpecifications;
    }

    public void setDescribedByDataValueSpecifications(List<RelatedMetadataElementSummary> describedByDataValueSpecifications)
    {
        this.describedByDataValueSpecifications = describedByDataValueSpecifications;
    }

    /**
     * Return data value specifications that augment this element.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getDataValueSpecifications()
    {
        return dataValueSpecifications;
    }

    /**
     * Set up data value specifications that augment this element.
     *
     * @param dataValueSpecifications list of related elements
     */
    public void setDataValueSpecifications(List<RelatedMetadataElementSummary> dataValueSpecifications)
    {
        this.dataValueSpecifications = dataValueSpecifications;
    }

    /**
     * Return elements identified as managing data values that match the specification of a data class.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getAssignedToDataValueSpecifications()
    {
        return assignedToDataValueSpecifications;
    }

    /**
     * Set up elements identified as managing data values that match the specification of a data class.
     *
     * @param assignedToDataValueSpecifications list of related elements
     */
    public void setAssignedToDataValueSpecifications(List<RelatedMetadataElementSummary> assignedToDataValueSpecifications)
    {
        this.assignedToDataValueSpecifications = assignedToDataValueSpecifications;
    }


    /**
     * Return the assigned data classes that describes the content in this data field.
     *
     * @return related elements
     */
    public List<RelatedMetadataElementSummary> getAssignedDataValueSpecifications()
    {
        return assignedDataValueSpecifications;
    }


    /**
     * Set up the assigned data classes that describes the content in this data field.
     *
     * @param assignedDataValueSpecifications related elements
     */
    public void setAssignedDataValueSpecifications(List<RelatedMetadataElementSummary> assignedDataValueSpecifications)
    {
        this.assignedDataValueSpecifications = assignedDataValueSpecifications;
    }

    /**
     * Return data value specification that is the more abstract.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getSuperDataValueSpecification()
    {
        return superDataValueSpecification;
    }

    /**
     * Set up data value specification that is the more abstract.
     *
     * @param superDataValueSpecification related element
     */
    public void setSuperDataValueSpecification(RelatedMetadataElementSummary superDataValueSpecification)
    {
        this.superDataValueSpecification = superDataValueSpecification;
    }

    /**
     * Return data value specification that are more specialized.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSubDataValueSpecifications()
    {
        return subDataValueSpecifications;
    }

    /**
     * Set up data value specification that are more specialized.
     *
     * @param subDataValueSpecifications list of related elements
     */
    public void setSubDataValueSpecifications(List<RelatedMetadataElementSummary> subDataValueSpecifications)
    {
        this.subDataValueSpecifications = subDataValueSpecifications;
    }

    /**
     * Return data classes that provide part of another data class's definitions.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getMadeOfDataClasses()
    {
        return madeOfDataClasses;
    }

    /**
     * Set up data classes that provide part of another data class's definitions.
     *
     * @param madeOfDataClasses list of related elements
     */
    public void setMadeOfDataClasses(List<RelatedMetadataElementSummary> madeOfDataClasses)
    {
        this.madeOfDataClasses = madeOfDataClasses;
    }

    /**
     * Return data classes that includes other data classes in its definition.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getPartOfDataClasses()
    {
        return partOfDataClasses;
    }

    /**
     * Set up data classes that includes other data classes in its definition.
     *
     * @param partOfDataClasses list of related elements
     */
    public void setPartOfDataClasses(List<RelatedMetadataElementSummary> partOfDataClasses)
    {
        this.partOfDataClasses = partOfDataClasses;
    }


    /**
     * Return any valid values associated with this element.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getValidValues()
    {
        return validValues;
    }


    /**
     * Set up any valid values associated with this element.
     *
     * @param validValues list
     */
    public void setValidValues(List<RelatedMetadataElementSummary> validValues)
    {
        this.validValues = validValues;
    }

    public List<RelatedMetadataElementSummary> getValidValueConsumers()
    {
        return validValueConsumers;
    }

    public void setValidValueConsumers(List<RelatedMetadataElementSummary> validValueConsumers)
    {
        this.validValueConsumers = validValueConsumers;
    }

    public List<RelatedMetadataElementSummary> getReferenceValues()
    {
        return referenceValues;
    }

    public void setReferenceValues(List<RelatedMetadataElementSummary> referenceValues)
    {
        this.referenceValues = referenceValues;
    }

    public List<RelatedMetadataElementSummary> getAssignedItems()
    {
        return assignedItems;
    }

    public void setAssignedItems(List<RelatedMetadataElementSummary> assignedItems)
    {
        this.assignedItems = assignedItems;
    }

    public List<RelatedMetadataElementSummary> getMatchingValues()
    {
        return matchingValues;
    }

    public void setMatchingValues(List<RelatedMetadataElementSummary> matchingValues)
    {
        this.matchingValues = matchingValues;
    }

    public List<RelatedMetadataElementSummary> getConsistentValues()
    {
        return consistentValues;
    }

    public void setConsistentValues(List<RelatedMetadataElementSummary> consistentValues)
    {
        this.consistentValues = consistentValues;
    }

    public List<RelatedMetadataElementSummary> getAssociatedValues()
    {
        return associatedValues;
    }

    public void setAssociatedValues(List<RelatedMetadataElementSummary> associatedValues)
    {
        this.associatedValues = associatedValues;
    }

    public List<RelatedMetadataElementSummary> getValidValueMembers()
    {
        return validValueMembers;
    }

    public void setValidValueMembers(List<RelatedMetadataElementSummary> validValueMembers)
    {
        this.validValueMembers = validValueMembers;
    }

    public List<RelatedMetadataElementSummary> getMemberOfValidValueSets()
    {
        return memberOfValidValueSets;
    }

    public void setMemberOfValidValueSets(List<RelatedMetadataElementSummary> memberOfValidValueSets)
    {
        this.memberOfValidValueSets = memberOfValidValueSets;
    }

    public List<RelatedMetadataElementSummary> getValidValueImplementations()
    {
        return validValueImplementations;
    }

    public void setValidValueImplementations(List<RelatedMetadataElementSummary> validValueImplementations)
    {
        this.validValueImplementations = validValueImplementations;
    }


    /**
     * Return the valid values set that this element implements.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getCanonicalValidValues()
    {
        return canonicalValidValues;
    }


    /**
     * Set up the valid values set that this element implements.
     *
     * @param canonicalValidValues list of related elements
     */
    public void setCanonicalValidValues(List<RelatedMetadataElementSummary> canonicalValidValues)
    {
        this.canonicalValidValues = canonicalValidValues;
    }


    public List<RelatedMetadataElementSummary> getSpecificationProperties()
    {
        return specificationProperties;
    }


    public void setSpecificationProperties(List<RelatedMetadataElementSummary> specificationProperties)
    {
        this.specificationProperties = specificationProperties;
    }


    public List<RelatedMetadataElementSummary> getSpecificationPropertyUsers()
    {
        return specificationPropertyUsers;
    }


    public void setSpecificationPropertyUsers(List<RelatedMetadataElementSummary> specificationPropertyUsers)
    {
        this.specificationPropertyUsers = specificationPropertyUsers;
    }


    /**
     * Return data structure that provides the specification used to certify data for the certification type.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getDataStructureDefinition()
    {
        return dataStructureDefinition;
    }

    /**
     * Set up data structure that provides the specification used to certify data for the certification type.
     *
     * @param dataStructureDefinition related element
     */
    public void setDataStructureDefinition(RelatedMetadataElementSummary dataStructureDefinition)
    {
        this.dataStructureDefinition = dataStructureDefinition;
    }

    public List<RelatedMetadataElementSummary> getUsedInCertifications()
    {
        return usedInCertifications;
    }

    public void setUsedInCertifications(List<RelatedMetadataElementSummary> usedInCertifications)
    {
        this.usedInCertifications = usedInCertifications;
    }

    /**
     * Return contained data fields.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getContainsDataFields()
    {
        return containsDataFields;
    }

    /**
     * Set up contained data fields.
     *
     * @param containsDataFields list of related elements
     */
    public void setContainsDataFields(List<RelatedMetadataElementSummary> containsDataFields)
    {
        this.containsDataFields = containsDataFields;
    }

    /**
     * Return provides more information about this referenceable.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getProvidesMoreInformation()
    {
        return providesMoreInformation;
    }

    /**
     * Set up provides more information about this referenceable.
     *
     * @param providesMoreInformation list of related elements
     */
    public void setProvidesMoreInformation(List<RelatedMetadataElementSummary> providesMoreInformation)
    {
        this.providesMoreInformation = providesMoreInformation;
    }

    /**
     * Return describes this core element.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getDescribes()
    {
        return describes;
    }

    /**
     * Set up describes this core element.
     *
     * @param describes list of related elements
     */
    public void setDescribes(List<RelatedMetadataElementSummary> describes)
    {
        this.describes = describes;
    }

    /**
     * Return description of the data.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getDataDescription()
    {
        return dataDescription;
    }

    /**
     * Set up description of the data.
     *
     * @param dataDescription list of related elements
     */
    public void setDataDescription(List<RelatedMetadataElementSummary> dataDescription)
    {
        this.dataDescription = dataDescription;
    }

    /**
     * Return element associated with the data being described.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getDescribesDataFor()
    {
        return describesDataFor;
    }

    /**
     * Set up element associated with the data being described.
     *
     * @param describesDataFor list of related elements
     */
    public void setDescribesDataFor(List<RelatedMetadataElementSummary> describesDataFor)
    {
        this.describesDataFor = describesDataFor;
    }

    public List<RelatedMetadataElementSummary> getPartOfDataStructures()
    {
        return partOfDataStructures;
    }

    public void setPartOfDataStructures(List<RelatedMetadataElementSummary> partOfDataStructures)
    {
        this.partOfDataStructures = partOfDataStructures;
    }


    /**
     * Return parent data field(s).
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getParentDataFields()
    {
        return parentDataFields;
    }

    /**
     * Set up parent data field(s).
     *
     * @param parentDataFields list of related elements
     */
    public void setParentDataFields(List<RelatedMetadataElementSummary> parentDataFields)
    {
        this.parentDataFields = parentDataFields;
    }

    /**
     * Return nested data fields.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getNestedDataFields()
    {
        return nestedDataFields;
    }

    /**
     * Set up nested data fields.
     *
     * @param nestedDataFields list of related elements
     */
    public void setNestedDataFields(List<RelatedMetadataElementSummary> nestedDataFields)
    {
        this.nestedDataFields = nestedDataFields;
    }

    public List<RelatedMetadataElementSummary> getLinkedToDataFields()
    {
        return linkedToDataFields;
    }

    public void setLinkedToDataFields(List<RelatedMetadataElementSummary> linkedToDataFields)
    {
        this.linkedToDataFields = linkedToDataFields;
    }

    public List<RelatedMetadataElementSummary> getLinkedFromDataFields()
    {
        return linkedFromDataFields;
    }

    public void setLinkedFromDataFields(List<RelatedMetadataElementSummary> linkedFromDataFields)
    {
        this.linkedFromDataFields = linkedFromDataFields;
    }

    /**
     * Return the description of the structure.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getDerivedFromDataStructure()
    {
        return derivedFromDataStructure;
    }

    /**
     * Set up the description of the structure.
     *
     * @param derivedFromDataStructure related element
     */
    public void setDerivedFromDataStructure(RelatedMetadataElementSummary derivedFromDataStructure)
    {
        this.derivedFromDataStructure = derivedFromDataStructure;
    }

    /**
     * Return equivalent schema type.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getEquivalentSchemaType()
    {
        return equivalentSchemaType;
    }

    /**
     * Set up equivalent schema type.
     *
     * @param equivalentSchemaType related element
     */
    public void setEquivalentSchemaType(RelatedMetadataElementSummary equivalentSchemaType)
    {
        this.equivalentSchemaType = equivalentSchemaType;
    }

    /**
     * Return the data fields using this schema.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getDerivedFromDataField()
    {
        return derivedFromDataField;
    }

    /**
     * Set up the data fields using this schema.
     *
     * @param derivedFromDataField related element
     */
    public void setDerivedFromDataField(RelatedMetadataElementSummary derivedFromDataField)
    {
        this.derivedFromDataField = derivedFromDataField;
    }

    /**
     * Return official schema attribute definition.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getEquivalentSchemaAttribute()
    {
        return equivalentSchemaAttribute;
    }

    /**
     * Set up official schema attribute definition.
     *
     * @param equivalentSchemaAttribute related element
     */
    public void setEquivalentSchemaAttribute(RelatedMetadataElementSummary equivalentSchemaAttribute)
    {
        this.equivalentSchemaAttribute = equivalentSchemaAttribute;
    }


    /**
     * Return another design pattern that operates in similar contexts.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getRelatedDesignPatterns()
    {
        return relatedDesignPatterns;
    }

    /**
     * Set up another design pattern that operates in similar contexts.
     *
     * @param relatedDesignPatterns list of related elements
     */
    public void setRelatedDesignPatterns(List<RelatedMetadataElementSummary> relatedDesignPatterns)
    {
        this.relatedDesignPatterns = relatedDesignPatterns;
    }

    /**
     * Return parent design pattern.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getConsumingDesignPatterns()
    {
        return consumingDesignPatterns;
    }

    /**
     * Set up parent design pattern.
     *
     * @param consumingDesignPatterns list of related elements
     */
    public void setConsumingDesignPatterns(List<RelatedMetadataElementSummary> consumingDesignPatterns)
    {
        this.consumingDesignPatterns = consumingDesignPatterns;
    }

    /**
     * Return child design pattern.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getConsumedDesignPatterns()
    {
        return consumedDesignPatterns;
    }

    /**
     * Set up child design pattern.
     *
     * @param consumedDesignPatterns list of related elements
     */
    public void setConsumedDesignPatterns(List<RelatedMetadataElementSummary> consumedDesignPatterns)
    {
        this.consumedDesignPatterns = consumedDesignPatterns;
    }

    public List<RelatedMetadataElementSummary> getGeneralizedDesignPattern()
    {
        return generalizedDesignPattern;
    }

    public void setGeneralizedDesignPattern(List<RelatedMetadataElementSummary> generalizedDesignPattern)
    {
        this.generalizedDesignPattern = generalizedDesignPattern;
    }

    public List<RelatedMetadataElementSummary> getSpecializedDesignPattern()
    {
        return specializedDesignPattern;
    }

    public void setSpecializedDesignPattern(List<RelatedMetadataElementSummary> specializedDesignPattern)
    {
        this.specializedDesignPattern = specializedDesignPattern;
    }

    /**
     * Return the report that the annotations belong to.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getFromSurveyReport()
    {
        return fromSurveyReport;
    }

    /**
     * Set up the report that the annotations belong to.
     *
     * @param fromSurveyReport related element
     */
    public void setFromSurveyReport(RelatedMetadataElementSummary fromSurveyReport)
    {
        this.fromSurveyReport = fromSurveyReport;
    }

    /**
     * Return the annotations providing the contents for the report.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getReportedAnnotations()
    {
        return reportedAnnotations;
    }

    /**
     * Set up the annotations providing the contents for the report.
     *
     * @param reportedAnnotations list of related elements
     */
    public void setReportedAnnotations(List<RelatedMetadataElementSummary> reportedAnnotations)
    {
        this.reportedAnnotations = reportedAnnotations;
    }

    /**
     * Return the annotations being extended.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getPreviousAnnotations()
    {
        return previousAnnotations;
    }

    /**
     * Set up the annotations being extended.
     *
     * @param previousAnnotations list of related elements
     */
    public void setPreviousAnnotations(List<RelatedMetadataElementSummary> previousAnnotations)
    {
        this.previousAnnotations = previousAnnotations;
    }

    /**
     * Return the annotations providing additional information.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getAnnotationExtensions()
    {
        return annotationExtensions;
    }

    /**
     * Set up the annotations providing additional information.
     *
     * @param annotationExtensions list of related elements
     */
    public void setAnnotationExtensions(List<RelatedMetadataElementSummary> annotationExtensions)
    {
        this.annotationExtensions = annotationExtensions;
    }

    public List<RelatedMetadataElementSummary> getAnnotationSubjects()
    {
        return annotationSubjects;
    }

    public void setAnnotationSubjects(List<RelatedMetadataElementSummary> annotationSubjects)
    {
        this.annotationSubjects = annotationSubjects;
    }

    /**
     * Return the annotations describing the element or its real-world counterpart.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getAssociatedAnnotations()
    {
        return associatedAnnotations;
    }

    /**
     * Set up the annotations describing the element or its real-world counterpart.
     *
     * @param associatedAnnotations list of related elements
     */
    public void setAssociatedAnnotations(List<RelatedMetadataElementSummary> associatedAnnotations)
    {
        this.associatedAnnotations = associatedAnnotations;
    }

    /**
     * Return elements that are matching the analysed data.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getAnnotationMatches()
    {
        return annotationMatches;
    }

    /**
     * Set up elements that are matching the analysed data.
     *
     * @param annotationMatches list of related elements
     */
    public void setAnnotationMatches(List<RelatedMetadataElementSummary> annotationMatches)
    {
        this.annotationMatches = annotationMatches;
    }

    /**
     * Return the annotations that have identified the element as a match.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getMatchedByAnnotations()
    {
        return matchedByAnnotations;
    }

    /**
     * Set up the annotations that have identified the element as a match.
     *
     * @param matchedByAnnotations list of related elements
     */
    public void setMatchedByAnnotations(List<RelatedMetadataElementSummary> matchedByAnnotations)
    {
        this.matchedByAnnotations = matchedByAnnotations;
    }

    /**
     * Return the annotations that refer to this log file.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getResourceProfileAnnotations()
    {
        return resourceProfileAnnotations;
    }

    /**
     * Set up the annotations that refer to this log file.
     *
     * @param resourceProfileAnnotations list of related elements
     */
    public void setResourceProfileAnnotations(List<RelatedMetadataElementSummary> resourceProfileAnnotations)
    {
        this.resourceProfileAnnotations = resourceProfileAnnotations;
    }

    public List<RelatedMetadataElementSummary> getResourceProfileData()
    {
        return resourceProfileData;
    }

    public void setResourceProfileData(List<RelatedMetadataElementSummary> resourceProfileData)
    {
        this.resourceProfileData = resourceProfileData;
    }

    public List<RelatedMetadataElementSummary> getIdentifiedByRequestForActions()
    {
        return identifiedByRequestForActions;
    }

    public void setIdentifiedByRequestForActions(List<RelatedMetadataElementSummary> identifiedByRequestForActions)
    {
        this.identifiedByRequestForActions = identifiedByRequestForActions;
    }

    /**
     * Return elements that originated the data.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getRequestForActionTargets()
    {
        return requestForActionTargets;
    }

    /**
     * Set up elements that originated the data.
     *
     * @param requestForActionTargets list of related elements
     */
    public void setRequestForActionTargets(List<RelatedMetadataElementSummary> requestForActionTargets)
    {
        this.requestForActionTargets = requestForActionTargets;
    }

    /**
     * Return the digital services dependent on the others.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getUsedByDigitalProducts()
    {
        return usedByDigitalProducts;
    }

    /**
     * Set up the digital services dependent on the others.
     *
     * @param usedByDigitalProducts list of related elements
     */
    public void setUsedByDigitalProducts(List<RelatedMetadataElementSummary> usedByDigitalProducts)
    {
        this.usedByDigitalProducts = usedByDigitalProducts;
    }

    /**
     * Return the digital products that the others depends on.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getUsesDigitalProducts()
    {
        return usesDigitalProducts;
    }

    /**
     * Set up the digital products that the others depends on.
     *
     * @param usesDigitalProducts list of related elements
     */
    public void setUsesDigitalProducts(List<RelatedMetadataElementSummary> usesDigitalProducts)
    {
        this.usesDigitalProducts = usesDigitalProducts;
    }

    /**
     * Return specific items in the agreement.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getAgreementItems()
    {
        return agreementItems;
    }

    /**
     * Set up specific items in the agreement.
     *
     * @param agreementItems list of related elements
     */
    public void setAgreementItems(List<RelatedMetadataElementSummary> agreementItems)
    {
        this.agreementItems = agreementItems;
    }

    /**
     * Return the agreement that the item relates to.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getAgreementContents()
    {
        return agreementContents;
    }

    /**
     * Set up the agreement that the item relates to.
     *
     * @param agreementContents list of related elements
     */
    public void setAgreementContents(List<RelatedMetadataElementSummary> agreementContents)
    {
        this.agreementContents = agreementContents;
    }

    /**
     * Return the actors that are named in the agreement.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getAgreementActors()
    {
        return agreementActors;
    }

    /**
     * Set up the actors that are named in the agreement.
     *
     * @param agreementActors list of related elements
     */
    public void setAgreementActors(List<RelatedMetadataElementSummary> agreementActors)
    {
        this.agreementActors = agreementActors;
    }

    public List<RelatedMetadataElementSummary> getInvolvedInAgreements()
    {
        return involvedInAgreements;
    }

    public void setInvolvedInAgreements(List<RelatedMetadataElementSummary> involvedInAgreements)
    {
        this.involvedInAgreements = involvedInAgreements;
    }



    /**
     * Return details of the contract documents.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getContracts()
    {
        return contracts;
    }

    /**
     * Set up details of the contract documents.
     *
     * @param contracts list of related elements
     */
    public void setContracts(List<RelatedMetadataElementSummary> contracts)
    {
        this.contracts = contracts;
    }

    public List<RelatedMetadataElementSummary> getAgreementsForContract()
    {
        return agreementsForContract;
    }

    public void setAgreementsForContract(List<RelatedMetadataElementSummary> agreementsForContract)
    {
        this.agreementsForContract = agreementsForContract;
    }


    /**
     * Return the digital subscribers registered under a subscription.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getDigitalSubscribers()
    {
        return digitalSubscribers;
    }

    /**
     * Set up the digital subscribers registered under a subscription.
     *
     * @param digitalSubscribers list of related elements
     */
    public void setDigitalSubscribers(List<RelatedMetadataElementSummary> digitalSubscribers)
    {
        this.digitalSubscribers = digitalSubscribers;
    }

    /**
     * Return the digital subscriptions in use by the subscriber.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getDigitalSubscriptions()
    {
        return digitalSubscriptions;
    }

    /**
     * Set up the digital subscriptions in use by the subscriber.
     *
     * @param digitalSubscriptions list of related elements
     */
    public void setDigitalSubscriptions(List<RelatedMetadataElementSummary> digitalSubscriptions)
    {
        this.digitalSubscriptions = digitalSubscriptions;
    }


    /**
     * Return tThe business capabilities that depend on the digital services.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getConsumingBusinessCapabilities()
    {
        return consumingBusinessCapabilities;
    }

    /**
     * Set up tThe business capabilities that depend on the digital services.
     *
     * @param consumingBusinessCapabilities list of related elements
     */
    public void setConsumingBusinessCapabilities(List<RelatedMetadataElementSummary> consumingBusinessCapabilities)
    {
        this.consumingBusinessCapabilities = consumingBusinessCapabilities;
    }

    /**
     * Return the digital services used to deliver the business capability,.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getUsesDigitalServices()
    {
        return usesDigitalServices;
    }

    /**
     * Set up the digital services used to deliver the business capability,.
     *
     * @param usesDigitalServices list of related elements
     */
    public void setUsesDigitalServices(List<RelatedMetadataElementSummary> usesDigitalServices)
    {
        this.usesDigitalServices = usesDigitalServices;
    }

    /**
     * Return the business capabilities that this business capability supports.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSupportsBusinessCapabilities()
    {
        return supportsBusinessCapabilities;
    }

    /**
     * Set up the business capabilities that this business capability supports.
     *
     * @param supportsBusinessCapabilities list of related elements
     */
    public void setSupportsBusinessCapabilities(List<RelatedMetadataElementSummary> supportsBusinessCapabilities)
    {
        this.supportsBusinessCapabilities = supportsBusinessCapabilities;
    }

    public List<RelatedMetadataElementSummary> getDependsOnBusinessCapabilities()
    {
        return dependsOnBusinessCapabilities;
    }

    public void setDependsOnBusinessCapabilities(List<RelatedMetadataElementSummary> dependsOnBusinessCapabilities)
    {
        this.dependsOnBusinessCapabilities = dependsOnBusinessCapabilities;
    }

    /**
     * Return logical source of the information supply chain.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSupplyFrom()
    {
        return supplyFrom;
    }

    /**
     * Set up logical source of the information supply chain.
     *
     * @param supplyFrom list of related elements
     */
    public void setSupplyFrom(List<RelatedMetadataElementSummary> supplyFrom)
    {
        this.supplyFrom = supplyFrom;
    }

    /**
     * Return logical destination of an information supply chain.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSupplyTo()
    {
        return supplyTo;
    }

    /**
     * Set up logical destination of an information supply chain.
     *
     * @param supplyTo list of related elements
     */
    public void setSupplyTo(List<RelatedMetadataElementSummary> supplyTo)
    {
        this.supplyTo = supplyTo;
    }


    /**
     * Return the solution components that embed this component.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getUsedInSolutionComponents()
    {
        return usedInSolutionComponents;
    }

    /**
     * Set up the solution components that embed this component.
     *
     * @param usedInSolutionComponents list of related elements
     */
    public void setUsedInSolutionComponents(List<RelatedMetadataElementSummary> usedInSolutionComponents)
    {
        this.usedInSolutionComponents = usedInSolutionComponents;
    }

    /**
     * Return the sub-parts of this solution component.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getNestedSolutionComponents()
    {
        return nestedSolutionComponents;
    }

    /**
     * Set up the sub-parts of this solution component.
     *
     * @param nestedSolutionComponents list of related elements
     */
    public void setNestedSolutionComponents(List<RelatedMetadataElementSummary> nestedSolutionComponents)
    {
        this.nestedSolutionComponents = nestedSolutionComponents;
    }

    /**
     * Return the actors that use this component.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getInteractingWithActors()
    {
        return interactingWithActors;
    }

    /**
     * Set up the actors that use this component.
     *
     * @param interactingWithActors list of related elements
     */
    public void setInteractingWithActors(List<RelatedMetadataElementSummary> interactingWithActors)
    {
        this.interactingWithActors = interactingWithActors;
    }


    /**
     * Return details of the relationships to solution components.
     *
     * @return list of element stubs
     */
    public List<RelatedMetadataElementSummary> getInteractingWithSolutionComponents()
    {
        return interactingWithSolutionComponents;
    }


    /**
     * Set up details of the relationships to solution components.
     *
     * @param interactingWithSolutionComponents relationship details
     */
    public void setInteractingWithSolutionComponents(List<RelatedMetadataElementSummary> interactingWithSolutionComponents)
    {
        this.interactingWithSolutionComponents = interactingWithSolutionComponents;
    }


    /**
     * Return owning solution component that this port belongs to.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getSolutionComponent()
    {
        return solutionComponent;
    }

    /**
     * Set up owning solution component that this port belongs to.
     *
     * @param solutionComponent related element
     */
    public void setSolutionComponent(RelatedMetadataElementSummary solutionComponent)
    {
        this.solutionComponent = solutionComponent;
    }

    /**
     * Return list ports for this solution component.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSolutionPorts()
    {
        return solutionPorts;
    }

    /**
     * Set up list ports for this solution component.
     *
     * @param solutionPorts list of related elements
     */
    public void setSolutionPorts(List<RelatedMetadataElementSummary> solutionPorts)
    {
        this.solutionPorts = solutionPorts;
    }

    /**
     * Return component that the wire connects to.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getWiredTo()
    {
        return wiredTo;
    }

    /**
     * Set up component that the wire connects to.
     *
     * @param wiredTo list of related elements
     */
    public void setWiredTo(List<RelatedMetadataElementSummary> wiredTo)
    {
        this.wiredTo = wiredTo;
    }

    /**
     * Return encapsulating solution component's port.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getAlignsToPort()
    {
        return alignsToPort;
    }

    /**
     * Set up encapsulating solution component's port.
     *
     * @param alignsToPort related element
     */
    public void setAlignsToPort(RelatedMetadataElementSummary alignsToPort)
    {
        this.alignsToPort = alignsToPort;
    }

    /**
     * Return ports from nested components that align with the port from the.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getDelegationPorts()
    {
        return delegationPorts;
    }

    /**
     * Set up ports from nested components that align with the port from the.
     *
     * @param delegationPorts list of related elements
     */
    public void setDelegationPorts(List<RelatedMetadataElementSummary> delegationPorts)
    {
        this.delegationPorts = delegationPorts;
    }

    /**
     * Return digital service described by the blueprint.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getDescribesDesignOf()
    {
        return describesDesignOf;
    }

    /**
     * Set up digital service described by the blueprint.
     *
     * @param describesDesignOf list of related elements
     */
    public void setDescribesDesignOf(List<RelatedMetadataElementSummary> describesDesignOf)
    {
        this.describesDesignOf = describesDesignOf;
    }

    /**
     * Return the difference versions of the digital service's designs.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSolutionDesigns()
    {
        return solutionDesigns;
    }

    /**
     * Set up the difference versions of the digital service's designs.
     *
     * @param solutionDesigns list of related elements
     */
    public void setSolutionDesigns(List<RelatedMetadataElementSummary> solutionDesigns)
    {
        this.solutionDesigns = solutionDesigns;
    }


    /**
     * Return details of other related elements retrieved from the repository.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getOtherRelatedElements()
    {
        return otherRelatedElements;
    }


    /**
     * Set up details of other related elements retrieved from the repository.
     *
     * @param otherRelatedElements list
     */
    public void setOtherRelatedElements(List<RelatedMetadataElementSummary> otherRelatedElements)
    {
        this.otherRelatedElements = otherRelatedElements;
    }


    /**
     * Return details of the relationship used to retrieve this element.
     * Will be null if the element was retrieved directly rather than via a relationship.
     *
     * @return list of element stubs
     */
    public RelatedBy getRelatedBy()
    {
        return relatedBy;
    }


    /**
     * Set up details of the relationship used to retrieve this element.
     * Will be null if the element was retrieved directly rather than via a relationship.
     *
     * @param relatedBy relationship details
     */
    public void setRelatedBy(RelatedBy relatedBy)
    {
        this.relatedBy = relatedBy;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AttributedMetadataElement{" +
                "elementHeader=" + elementHeader +
                ", sampleData=" + sampleData +
                ", sourcesOfSampleData=" + sourcesOfSampleData +
                ", templateCreatedElements=" + templateCreatedElements +
                ", sourcedFromTemplate=" + sourcedFromTemplate +
                ", templatesForCataloguing=" + templatesForCataloguing +
                ", templateUses=" + templateUses +
                ", actionSource=" + actionSource +
                ", requestedActions=" + requestedActions +
                ", actionCause=" + actionCause +
                ", relatedActions=" + relatedActions +
                ", actionTargets=" + actionTargets +
                ", actionsForTarget=" + actionsForTarget +
                ", searchKeywords=" + searchKeywords +
                ", keywordElements=" + keywordElements +
                ", externalReferences=" + externalReferences +
                ", referencingElements=" + referencingElements +
                ", alsoKnownAs=" + alsoKnownAs +
                ", equivalentElements=" + equivalentElements +
                ", resourceList=" + resourceList +
                ", resourceListUsers=" + resourceListUsers +
                ", providesMoreInformation=" + providesMoreInformation +
                ", describes=" + describes +
                ", propertyFacets=" + propertyFacets +
                ", facetedElements=" + facetedElements +
                ", memberOfCollections=" + memberOfCollections +
                ", collectionMembers=" + collectionMembers +
                ", knownLocations=" + knownLocations +
                ", localResources=" + localResources +
                ", peerLocations=" + peerLocations +
                ", groupingLocations=" + groupingLocations +
                ", nestedLocations=" + nestedLocations +
                ", serverEndpoints=" + serverEndpoints +
                ", serverForEndpoint=" + serverForEndpoint +
                ", hostedITAssets=" + hostedITAssets +
                ", deployedTo=" + deployedTo +
                ", storageVolumes=" + storageVolumes +
                ", providesStorageFor=" + providesStorageFor +
                ", managesStorageFor=" + managesStorageFor +
                ", storedOn=" + storedOn +
                ", installedOn=" + installedOn +
                ", operatingPlatforms=" + operatingPlatforms +
                ", includesSoftwarePackages=" + includesSoftwarePackages +
                ", packagedInOperatingPlatforms=" + packagedInOperatingPlatforms +
                ", dependsOnSoftwarePackages=" + dependsOnSoftwarePackages +
                ", runningWithAsset=" + runningWithAsset +
                ", managedHosts=" + managedHosts +
                ", hostCluster=" + hostCluster +
                ", resultsStoredIn=" + resultsStoredIn +
                ", populatedUsingQuery=" + populatedUsingQuery +
                ", consumedByCapabilities=" + consumedByCapabilities +
                ", capabilityConsumedAssets=" + capabilityConsumedAssets +
                ", capabilities=" + capabilities +
                ", hostedByDeployedITInfrastructure=" + hostedByDeployedITInfrastructure +
                ", cohortMembership=" + cohortMembership +
                ", registeredWithCohorts=" + registeredWithCohorts +
                ", visibleEndpoints=" + visibleEndpoints +
                ", visibleInNetworks=" + visibleInNetworks +
                ", userProfile=" + userProfile +
                ", userIdentities=" + userIdentities +
                ", contactDetails=" + contactDetails +
                ", contacts=" + contacts +
                ", myFollowers=" + myFollowers +
                ", myPeers=" + myPeers +
                ", superTeam=" + superTeam +
                ", subTeams=" + subTeams +
                ", profilesForAsset=" + profilesForAsset +
                ", assetsUsingProfile=" + assetsUsingProfile +
                ", performsRoles=" + performsRoles +
                ", rolePerformers=" + rolePerformers +
                ", relevantToScopes=" + relevantToScopes +
                ", scopedElements=" + scopedElements +
                ", assignmentScope=" + assignmentScope +
                ", assignedActors=" + assignedActors +
                ", contributionRecord=" + contributionRecord +
                ", contributorProfile=" + contributorProfile +
                ", dependentProjects=" + dependentProjects +
                ", dependsOnProjects=" + dependsOnProjects +
                ", managingProjects=" + managingProjects +
                ", managedProjects=" + managedProjects +
                ", likes=" + likes +
                ", likedElement=" + likedElement +
                ", informalTags=" + informalTags +
                ", taggedElements=" + taggedElements +
                ", reviews=" + reviews +
                ", reviewedElement=" + reviewedElement +
                ", comments=" + comments +
                ", commentedOnElement=" + commentedOnElement +
                ", answeredQuestions=" + answeredQuestions +
                ", acceptedAnswers=" + acceptedAnswers +
                ", crowdSourcingContributors=" + crowdSourcingContributors +
                ", crowdSourcedContributions=" + crowdSourcedContributions +
                ", noteLogSubjects=" + noteLogSubjects +
                ", noteLogs=" + noteLogs +
                ", presentInNoteLogs=" + presentInNoteLogs +
                ", noteLogEntries=" + noteLogEntries +
                ", connections=" + connections +
                ", connectorType=" + connectorType +
                ", endpoint=" + endpoint +
                ", connectedResources=" + connectedResources +
                ", embeddedConnections=" + embeddedConnections +
                ", parentConnections=" + parentConnections +
                ", supportedDataSets=" + supportedDataSets +
                ", dataSetContent=" + dataSetContent +
                ", apiEndpoints=" + apiEndpoints +
                ", supportedAPIs=" + supportedAPIs +
                ", parentProcesses=" + parentProcesses +
                ", childProcesses=" + childProcesses +
                ", ports=" + ports +
                ", portOwningProcesses=" + portOwningProcesses +
                ", portDelegatingFrom=" + portDelegatingFrom +
                ", portDelegatingTo=" + portDelegatingTo +
                ", homeFolder=" + homeFolder +
                ", nestedFiles=" + nestedFiles +
                ", linkedFiles=" + linkedFiles +
                ", linkedFolders=" + linkedFolders +
                ", parentFolder=" + parentFolder +
                ", nestedFolders=" + nestedFolders +
                ", linkedMediaFiles=" + linkedMediaFiles +
                ", associatedLogs=" + associatedLogs +
                ", associatedLogSubjects=" + associatedLogSubjects +
                ", archiveContents=" + archiveContents +
                ", packagedInArchiveFiles=" + packagedInArchiveFiles +
                ", reportOriginator=" + reportOriginator +
                ", generatedReports=" + generatedReports +
                ", reportSubjects=" + reportSubjects +
                ", reports=" + reports +
                ", priorReports=" + priorReports +
                ", followOnReports=" + followOnReports +
                ", relatedTerms=" + relatedTerms +
                ", usedInContexts=" + usedInContexts +
                ", contextRelevantTerms=" + contextRelevantTerms +
                ", meaningForDataElements=" + meaningForDataElements +
                ", meanings=" + meanings +
                ", semanticDefinitions=" + semanticDefinitions +
                ", semanticallyAssociatedDefinitions=" + semanticallyAssociatedDefinitions +
                ", supplementaryProperties=" + supplementaryProperties +
                ", supplementsElement=" + supplementsElement +
                ", governedBy=" + governedBy +
                ", governedElements=" + governedElements +
                ", peerGovernanceDefinitions=" + peerGovernanceDefinitions +
                ", supportedGovernanceDefinitions=" + supportedGovernanceDefinitions +
                ", supportingGovernanceDefinitions=" + supportingGovernanceDefinitions +
                ", configuredInSecurityCollections=" + configuredInSecurityCollections +
                ", userAccounts=" + userAccounts +
                ", definedInSecretsCollection=" + definedInSecretsCollection +
                ", securityAccessControls=" + securityAccessControls +
                ", listedInSecretsCollection=" + listedInSecretsCollection +
                ", securityLists=" + securityLists +
                ", usedInAccessControls=" + usedInAccessControls +
                ", associatedSecurityLists=" + associatedSecurityLists +
                ", inheritsFromZone=" + inheritsFromZone +
                ", controlsZones=" + controlsZones +
                ", broaderSubjectArea=" + broaderSubjectArea +
                ", nestedSubjectAreas=" + nestedSubjectAreas +
                ", metrics=" + metrics +
                ", measurements=" + measurements +
                ", monitoredThrough=" + monitoredThrough +
                ", monitoredResources=" + monitoredResources +
                ", interestingNotificationTypes=" + interestingNotificationTypes +
                ", subscribers=" + subscribers +
                ", excludedFromRequirements=" + excludedFromRequirements +
                ", exceptions=" + exceptions +
                ", calledFromGovernanceEngines=" + calledFromGovernanceEngines +
                ", supportedGovernanceServices=" + supportedGovernanceServices +
                ", associatedGovernanceActions=" + associatedGovernanceActions +
                ", predefinedTargetForAction=" + predefinedTargetForAction +
                ", triggeredFrom=" + triggeredFrom +
                ", firstStep=" + firstStep +
                ", dependedOnProcessSteps=" + dependedOnProcessSteps +
                ", followOnProcessSteps=" + followOnProcessSteps +
                ", supportsGovernanceActions=" + supportsGovernanceActions +
                ", governanceActionExecutor=" + governanceActionExecutor +
                ", includedInIntegrationGroups=" + includedInIntegrationGroups +
                ", supportedIntegrationConnectors=" + supportedIntegrationConnectors +
                ", refreshedByConnectors=" + refreshedByConnectors +
                ", catalogTargets=" + catalogTargets +
                ", peerDuplicateOrigin=" + peerDuplicateOrigin +
                ", peerDuplicatePartner=" + peerDuplicatePartner +
                ", consolidatedDuplicateOrigin=" + consolidatedDuplicateOrigin +
                ", consolidatedDuplicateResult=" + consolidatedDuplicateResult +
                ", impactedResources=" + impactedResources +
                ", incidentReports=" + incidentReports +
                ", licenses=" + licenses +
                ", licensedElements=" + licensedElements +
                ", certifications=" + certifications +
                ", certifiedElements=" + certifiedElements +
                ", schemaType=" + schemaType +
                ", describesStructure=" + describesStructure +
                ", parentSchemaElements=" + parentSchemaElements +
                ", schemaOptions=" + schemaOptions +
                ", schemaAttributes=" + schemaAttributes +
                ", externalSchemaType=" + externalSchemaType +
                ", mapFromElement=" + mapFromElement +
                ", mapToElement=" + mapToElement +
                ", queries=" + queries +
                ", containsOperations=" + containsOperations +
                ", apiHeader=" + apiHeader +
                ", apiRequest=" + apiRequest +
                ", apiResponse=" + apiResponse +
                ", databaseSchemaType=" + databaseSchemaType +
                ", containsDBSchemas=" + containsDBSchemas +
                ", linkedToPrimaryKey=" + linkedToPrimaryKey +
                ", foreignKeys=" + foreignKeys +
                ", vertices=" + vertices +
                ", edges=" + edges +
                ", describedByDataValueSpecifications=" + describedByDataValueSpecifications +
                ", dataValueSpecifications=" + dataValueSpecifications +
                ", assignedToDataValueSpecifications=" + assignedToDataValueSpecifications +
                ", assignedDataValueSpecifications=" + assignedDataValueSpecifications +
                ", superDataValueSpecification=" + superDataValueSpecification +
                ", subDataValueSpecifications=" + subDataValueSpecifications +
                ", madeOfDataClasses=" + madeOfDataClasses +
                ", partOfDataClasses=" + partOfDataClasses +
                ", validValues=" + validValues +
                ", validValueConsumers=" + validValueConsumers +
                ", referenceValues=" + referenceValues +
                ", assignedItems=" + assignedItems +
                ", matchingValues=" + matchingValues +
                ", consistentValues=" + consistentValues +
                ", associatedValues=" + associatedValues +
                ", validValueMembers=" + validValueMembers +
                ", memberOfValidValueSets=" + memberOfValidValueSets +
                ", validValueImplementations=" + validValueImplementations +
                ", canonicalValidValues=" + canonicalValidValues +
                ", specificationProperties=" + specificationProperties +
                ", specificationPropertyUsers=" + specificationPropertyUsers +
                ", dataStructureDefinition=" + dataStructureDefinition +
                ", usedInCertifications=" + usedInCertifications +
                ", dataDescription=" + dataDescription +
                ", describesDataFor=" + describesDataFor +
                ", containsDataFields=" + containsDataFields +
                ", partOfDataStructures=" + partOfDataStructures +
                ", parentDataFields=" + parentDataFields +
                ", nestedDataFields=" + nestedDataFields +
                ", linkedToDataFields=" + linkedToDataFields +
                ", linkedFromDataFields=" + linkedFromDataFields +
                ", derivedFromDataStructure=" + derivedFromDataStructure +
                ", equivalentSchemaType=" + equivalentSchemaType +
                ", derivedFromDataField=" + derivedFromDataField +
                ", equivalentSchemaAttribute=" + equivalentSchemaAttribute +
                ", relatedDesignPatterns=" + relatedDesignPatterns +
                ", consumingDesignPatterns=" + consumingDesignPatterns +
                ", consumedDesignPatterns=" + consumedDesignPatterns +
                ", generalizedDesignPattern=" + generalizedDesignPattern +
                ", specializedDesignPattern=" + specializedDesignPattern +
                ", fromSurveyReport=" + fromSurveyReport +
                ", reportedAnnotations=" + reportedAnnotations +
                ", previousAnnotations=" + previousAnnotations +
                ", annotationExtensions=" + annotationExtensions +
                ", annotationSubjects=" + annotationSubjects +
                ", associatedAnnotations=" + associatedAnnotations +
                ", annotationMatches=" + annotationMatches +
                ", matchedByAnnotations=" + matchedByAnnotations +
                ", resourceProfileAnnotations=" + resourceProfileAnnotations +
                ", resourceProfileData=" + resourceProfileData +
                ", identifiedByRequestForActions=" + identifiedByRequestForActions +
                ", requestForActionTargets=" + requestForActionTargets +
                ", usedByDigitalProducts=" + usedByDigitalProducts +
                ", usesDigitalProducts=" + usesDigitalProducts +
                ", agreementItems=" + agreementItems +
                ", agreementContents=" + agreementContents +
                ", agreementActors=" + agreementActors +
                ", involvedInAgreements=" + involvedInAgreements +
                ", contracts=" + contracts +
                ", agreementsForContract=" + agreementsForContract +
                ", digitalSubscribers=" + digitalSubscribers +
                ", digitalSubscriptions=" + digitalSubscriptions +
                ", consumingBusinessCapabilities=" + consumingBusinessCapabilities +
                ", usesDigitalServices=" + usesDigitalServices +
                ", supportsBusinessCapabilities=" + supportsBusinessCapabilities +
                ", dependsOnBusinessCapabilities=" + dependsOnBusinessCapabilities +
                ", supplyFrom=" + supplyFrom +
                ", supplyTo=" + supplyTo +
                ", usedInSolutionComponents=" + usedInSolutionComponents +
                ", nestedSolutionComponents=" + nestedSolutionComponents +
                ", interactingWithActors=" + interactingWithActors +
                ", interactingWithSolutionComponents=" + interactingWithSolutionComponents +
                ", solutionComponent=" + solutionComponent +
                ", solutionPorts=" + solutionPorts +
                ", wiredTo=" + wiredTo +
                ", alignsToPort=" + alignsToPort +
                ", delegationPorts=" + delegationPorts +
                ", derivedFrom=" + derivedFrom +
                ", implementedBy=" + implementedBy +
                ", usedInImplementationOf=" + usedInImplementationOf +
                ", implementationResources=" + implementationResources +
                ", describesDesignOf=" + describesDesignOf +
                ", solutionDesigns=" + solutionDesigns +
                ", lineageLinkage=" + lineageLinkage +
                ", otherRelatedElements=" + otherRelatedElements +
                ", relatedBy=" + relatedBy +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        AttributedMetadataElement that = (AttributedMetadataElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) && Objects.equals(sampleData, that.sampleData) && Objects.equals(sourcesOfSampleData, that.sourcesOfSampleData) && Objects.equals(templateCreatedElements, that.templateCreatedElements) && Objects.equals(sourcedFromTemplate, that.sourcedFromTemplate) && Objects.equals(templatesForCataloguing, that.templatesForCataloguing) && Objects.equals(templateUses, that.templateUses) && Objects.equals(actionSource, that.actionSource) && Objects.equals(requestedActions, that.requestedActions) && Objects.equals(actionCause, that.actionCause) && Objects.equals(relatedActions, that.relatedActions) && Objects.equals(actionTargets, that.actionTargets) && Objects.equals(actionsForTarget, that.actionsForTarget) && Objects.equals(searchKeywords, that.searchKeywords) && Objects.equals(keywordElements, that.keywordElements) && Objects.equals(externalReferences, that.externalReferences) && Objects.equals(referencingElements, that.referencingElements) && Objects.equals(alsoKnownAs, that.alsoKnownAs) && Objects.equals(equivalentElements, that.equivalentElements) && Objects.equals(resourceList, that.resourceList) && Objects.equals(resourceListUsers, that.resourceListUsers) && Objects.equals(providesMoreInformation, that.providesMoreInformation) && Objects.equals(describes, that.describes) && Objects.equals(propertyFacets, that.propertyFacets) && Objects.equals(facetedElements, that.facetedElements) && Objects.equals(memberOfCollections, that.memberOfCollections) && Objects.equals(collectionMembers, that.collectionMembers) && Objects.equals(knownLocations, that.knownLocations) && Objects.equals(localResources, that.localResources) && Objects.equals(peerLocations, that.peerLocations) && Objects.equals(groupingLocations, that.groupingLocations) && Objects.equals(nestedLocations, that.nestedLocations) && Objects.equals(serverEndpoints, that.serverEndpoints) && Objects.equals(serverForEndpoint, that.serverForEndpoint) && Objects.equals(hostedITAssets, that.hostedITAssets) && Objects.equals(deployedTo, that.deployedTo) && Objects.equals(storageVolumes, that.storageVolumes) && Objects.equals(providesStorageFor, that.providesStorageFor) && Objects.equals(managesStorageFor, that.managesStorageFor) && Objects.equals(storedOn, that.storedOn) && Objects.equals(installedOn, that.installedOn) && Objects.equals(operatingPlatforms, that.operatingPlatforms) && Objects.equals(includesSoftwarePackages, that.includesSoftwarePackages) && Objects.equals(packagedInOperatingPlatforms, that.packagedInOperatingPlatforms) && Objects.equals(dependsOnSoftwarePackages, that.dependsOnSoftwarePackages) && Objects.equals(runningWithAsset, that.runningWithAsset) && Objects.equals(managedHosts, that.managedHosts) && Objects.equals(hostCluster, that.hostCluster) && Objects.equals(resultsStoredIn, that.resultsStoredIn) && Objects.equals(populatedUsingQuery, that.populatedUsingQuery) && Objects.equals(consumedByCapabilities, that.consumedByCapabilities) && Objects.equals(capabilityConsumedAssets, that.capabilityConsumedAssets) && Objects.equals(capabilities, that.capabilities) && Objects.equals(hostedByDeployedITInfrastructure, that.hostedByDeployedITInfrastructure) && Objects.equals(cohortMembership, that.cohortMembership) && Objects.equals(registeredWithCohorts, that.registeredWithCohorts) && Objects.equals(visibleEndpoints, that.visibleEndpoints) && Objects.equals(visibleInNetworks, that.visibleInNetworks) && Objects.equals(userProfile, that.userProfile) && Objects.equals(userIdentities, that.userIdentities) && Objects.equals(contactDetails, that.contactDetails) && Objects.equals(contacts, that.contacts) && Objects.equals(myFollowers, that.myFollowers) && Objects.equals(myPeers, that.myPeers) && Objects.equals(superTeam, that.superTeam) && Objects.equals(subTeams, that.subTeams) && Objects.equals(profilesForAsset, that.profilesForAsset) && Objects.equals(assetsUsingProfile, that.assetsUsingProfile) && Objects.equals(performsRoles, that.performsRoles) && Objects.equals(rolePerformers, that.rolePerformers) && Objects.equals(relevantToScopes, that.relevantToScopes) && Objects.equals(scopedElements, that.scopedElements) && Objects.equals(assignmentScope, that.assignmentScope) && Objects.equals(assignedActors, that.assignedActors) && Objects.equals(contributionRecord, that.contributionRecord) && Objects.equals(contributorProfile, that.contributorProfile) && Objects.equals(dependentProjects, that.dependentProjects) && Objects.equals(dependsOnProjects, that.dependsOnProjects) && Objects.equals(managingProjects, that.managingProjects) && Objects.equals(managedProjects, that.managedProjects) && Objects.equals(likes, that.likes) && Objects.equals(likedElement, that.likedElement) && Objects.equals(informalTags, that.informalTags) && Objects.equals(taggedElements, that.taggedElements) && Objects.equals(reviews, that.reviews) && Objects.equals(reviewedElement, that.reviewedElement) && Objects.equals(comments, that.comments) && Objects.equals(commentedOnElement, that.commentedOnElement) && Objects.equals(answeredQuestions, that.answeredQuestions) && Objects.equals(acceptedAnswers, that.acceptedAnswers) && Objects.equals(crowdSourcingContributors, that.crowdSourcingContributors) && Objects.equals(crowdSourcedContributions, that.crowdSourcedContributions) && Objects.equals(noteLogSubjects, that.noteLogSubjects) && Objects.equals(noteLogs, that.noteLogs) && Objects.equals(presentInNoteLogs, that.presentInNoteLogs) && Objects.equals(noteLogEntries, that.noteLogEntries) && Objects.equals(connections, that.connections) && Objects.equals(connectorType, that.connectorType) && Objects.equals(endpoint, that.endpoint) && Objects.equals(connectedResources, that.connectedResources) && Objects.equals(embeddedConnections, that.embeddedConnections) && Objects.equals(parentConnections, that.parentConnections) && Objects.equals(supportedDataSets, that.supportedDataSets) && Objects.equals(dataSetContent, that.dataSetContent) && Objects.equals(apiEndpoints, that.apiEndpoints) && Objects.equals(supportedAPIs, that.supportedAPIs) && Objects.equals(parentProcesses, that.parentProcesses) && Objects.equals(childProcesses, that.childProcesses) && Objects.equals(ports, that.ports) && Objects.equals(portOwningProcesses, that.portOwningProcesses) && Objects.equals(portDelegatingFrom, that.portDelegatingFrom) && Objects.equals(portDelegatingTo, that.portDelegatingTo) && Objects.equals(homeFolder, that.homeFolder) && Objects.equals(nestedFiles, that.nestedFiles) && Objects.equals(linkedFiles, that.linkedFiles) && Objects.equals(linkedFolders, that.linkedFolders) && Objects.equals(parentFolder, that.parentFolder) && Objects.equals(nestedFolders, that.nestedFolders) && Objects.equals(linkedMediaFiles, that.linkedMediaFiles) && Objects.equals(associatedLogs, that.associatedLogs) && Objects.equals(associatedLogSubjects, that.associatedLogSubjects) && Objects.equals(archiveContents, that.archiveContents) && Objects.equals(packagedInArchiveFiles, that.packagedInArchiveFiles) && Objects.equals(reportOriginator, that.reportOriginator) && Objects.equals(generatedReports, that.generatedReports) && Objects.equals(reportSubjects, that.reportSubjects) && Objects.equals(reports, that.reports) && Objects.equals(priorReports, that.priorReports) && Objects.equals(followOnReports, that.followOnReports) && Objects.equals(relatedTerms, that.relatedTerms) && Objects.equals(usedInContexts, that.usedInContexts) && Objects.equals(contextRelevantTerms, that.contextRelevantTerms) && Objects.equals(meaningForDataElements, that.meaningForDataElements) && Objects.equals(meanings, that.meanings) && Objects.equals(semanticDefinitions, that.semanticDefinitions) && Objects.equals(semanticallyAssociatedDefinitions, that.semanticallyAssociatedDefinitions) && Objects.equals(supplementaryProperties, that.supplementaryProperties) && Objects.equals(supplementsElement, that.supplementsElement) && Objects.equals(governedBy, that.governedBy) && Objects.equals(governedElements, that.governedElements) && Objects.equals(peerGovernanceDefinitions, that.peerGovernanceDefinitions) && Objects.equals(supportedGovernanceDefinitions, that.supportedGovernanceDefinitions) && Objects.equals(supportingGovernanceDefinitions, that.supportingGovernanceDefinitions) && Objects.equals(configuredInSecurityCollections, that.configuredInSecurityCollections) && Objects.equals(userAccounts, that.userAccounts) && Objects.equals(definedInSecretsCollection, that.definedInSecretsCollection) && Objects.equals(securityAccessControls, that.securityAccessControls) && Objects.equals(listedInSecretsCollection, that.listedInSecretsCollection) && Objects.equals(securityLists, that.securityLists) && Objects.equals(usedInAccessControls, that.usedInAccessControls) && Objects.equals(associatedSecurityLists, that.associatedSecurityLists) && Objects.equals(inheritsFromZone, that.inheritsFromZone) && Objects.equals(controlsZones, that.controlsZones) && Objects.equals(broaderSubjectArea, that.broaderSubjectArea) && Objects.equals(nestedSubjectAreas, that.nestedSubjectAreas) && Objects.equals(metrics, that.metrics) && Objects.equals(measurements, that.measurements) && Objects.equals(monitoredThrough, that.monitoredThrough) && Objects.equals(monitoredResources, that.monitoredResources) && Objects.equals(interestingNotificationTypes, that.interestingNotificationTypes) && Objects.equals(subscribers, that.subscribers) && Objects.equals(excludedFromRequirements, that.excludedFromRequirements) && Objects.equals(exceptions, that.exceptions) && Objects.equals(calledFromGovernanceEngines, that.calledFromGovernanceEngines) && Objects.equals(supportedGovernanceServices, that.supportedGovernanceServices) && Objects.equals(associatedGovernanceActions, that.associatedGovernanceActions) && Objects.equals(predefinedTargetForAction, that.predefinedTargetForAction) && Objects.equals(triggeredFrom, that.triggeredFrom) && Objects.equals(firstStep, that.firstStep) && Objects.equals(dependedOnProcessSteps, that.dependedOnProcessSteps) && Objects.equals(followOnProcessSteps, that.followOnProcessSteps) && Objects.equals(supportsGovernanceActions, that.supportsGovernanceActions) && Objects.equals(governanceActionExecutor, that.governanceActionExecutor) && Objects.equals(includedInIntegrationGroups, that.includedInIntegrationGroups) && Objects.equals(supportedIntegrationConnectors, that.supportedIntegrationConnectors) && Objects.equals(refreshedByConnectors, that.refreshedByConnectors) && Objects.equals(catalogTargets, that.catalogTargets) && Objects.equals(peerDuplicateOrigin, that.peerDuplicateOrigin) && Objects.equals(peerDuplicatePartner, that.peerDuplicatePartner) && Objects.equals(consolidatedDuplicateOrigin, that.consolidatedDuplicateOrigin) && Objects.equals(consolidatedDuplicateResult, that.consolidatedDuplicateResult) && Objects.equals(impactedResources, that.impactedResources) && Objects.equals(incidentReports, that.incidentReports) && Objects.equals(licenses, that.licenses) && Objects.equals(licensedElements, that.licensedElements) && Objects.equals(certifications, that.certifications) && Objects.equals(certifiedElements, that.certifiedElements) && Objects.equals(schemaType, that.schemaType) && Objects.equals(describesStructure, that.describesStructure) && Objects.equals(parentSchemaElements, that.parentSchemaElements) && Objects.equals(schemaOptions, that.schemaOptions) && Objects.equals(schemaAttributes, that.schemaAttributes) && Objects.equals(externalSchemaType, that.externalSchemaType) && Objects.equals(mapFromElement, that.mapFromElement) && Objects.equals(mapToElement, that.mapToElement) && Objects.equals(queries, that.queries) && Objects.equals(containsOperations, that.containsOperations) && Objects.equals(apiHeader, that.apiHeader) && Objects.equals(apiRequest, that.apiRequest) && Objects.equals(apiResponse, that.apiResponse) && Objects.equals(databaseSchemaType, that.databaseSchemaType) && Objects.equals(containsDBSchemas, that.containsDBSchemas) && Objects.equals(linkedToPrimaryKey, that.linkedToPrimaryKey) && Objects.equals(foreignKeys, that.foreignKeys) && Objects.equals(vertices, that.vertices) && Objects.equals(edges, that.edges) && Objects.equals(describedByDataValueSpecifications, that.describedByDataValueSpecifications) && Objects.equals(dataValueSpecifications, that.dataValueSpecifications) && Objects.equals(assignedToDataValueSpecifications, that.assignedToDataValueSpecifications) && Objects.equals(assignedDataValueSpecifications, that.assignedDataValueSpecifications) && Objects.equals(superDataValueSpecification, that.superDataValueSpecification) && Objects.equals(subDataValueSpecifications, that.subDataValueSpecifications) && Objects.equals(madeOfDataClasses, that.madeOfDataClasses) && Objects.equals(partOfDataClasses, that.partOfDataClasses) && Objects.equals(validValues, that.validValues) && Objects.equals(validValueConsumers, that.validValueConsumers) && Objects.equals(referenceValues, that.referenceValues) && Objects.equals(assignedItems, that.assignedItems) && Objects.equals(matchingValues, that.matchingValues) && Objects.equals(consistentValues, that.consistentValues) && Objects.equals(associatedValues, that.associatedValues) && Objects.equals(validValueMembers, that.validValueMembers) && Objects.equals(memberOfValidValueSets, that.memberOfValidValueSets) && Objects.equals(validValueImplementations, that.validValueImplementations) && Objects.equals(canonicalValidValues, that.canonicalValidValues) && Objects.equals(specificationProperties, that.specificationProperties) && Objects.equals(specificationPropertyUsers, that.specificationPropertyUsers) && Objects.equals(dataStructureDefinition, that.dataStructureDefinition) && Objects.equals(usedInCertifications, that.usedInCertifications) && Objects.equals(dataDescription, that.dataDescription) && Objects.equals(describesDataFor, that.describesDataFor) && Objects.equals(containsDataFields, that.containsDataFields) && Objects.equals(partOfDataStructures, that.partOfDataStructures) && Objects.equals(parentDataFields, that.parentDataFields) && Objects.equals(nestedDataFields, that.nestedDataFields) && Objects.equals(linkedToDataFields, that.linkedToDataFields) && Objects.equals(linkedFromDataFields, that.linkedFromDataFields) && Objects.equals(derivedFromDataStructure, that.derivedFromDataStructure) && Objects.equals(equivalentSchemaType, that.equivalentSchemaType) && Objects.equals(derivedFromDataField, that.derivedFromDataField) && Objects.equals(equivalentSchemaAttribute, that.equivalentSchemaAttribute) && Objects.equals(relatedDesignPatterns, that.relatedDesignPatterns) && Objects.equals(consumingDesignPatterns, that.consumingDesignPatterns) && Objects.equals(consumedDesignPatterns, that.consumedDesignPatterns) && Objects.equals(generalizedDesignPattern, that.generalizedDesignPattern) && Objects.equals(specializedDesignPattern, that.specializedDesignPattern) && Objects.equals(fromSurveyReport, that.fromSurveyReport) && Objects.equals(reportedAnnotations, that.reportedAnnotations) && Objects.equals(previousAnnotations, that.previousAnnotations) && Objects.equals(annotationExtensions, that.annotationExtensions) && Objects.equals(annotationSubjects, that.annotationSubjects) && Objects.equals(associatedAnnotations, that.associatedAnnotations) && Objects.equals(annotationMatches, that.annotationMatches) && Objects.equals(matchedByAnnotations, that.matchedByAnnotations) && Objects.equals(resourceProfileAnnotations, that.resourceProfileAnnotations) && Objects.equals(resourceProfileData, that.resourceProfileData) && Objects.equals(identifiedByRequestForActions, that.identifiedByRequestForActions) && Objects.equals(requestForActionTargets, that.requestForActionTargets) && Objects.equals(usedByDigitalProducts, that.usedByDigitalProducts) && Objects.equals(usesDigitalProducts, that.usesDigitalProducts) && Objects.equals(agreementItems, that.agreementItems) && Objects.equals(agreementContents, that.agreementContents) && Objects.equals(agreementActors, that.agreementActors) && Objects.equals(involvedInAgreements, that.involvedInAgreements) && Objects.equals(contracts, that.contracts) && Objects.equals(agreementsForContract, that.agreementsForContract) && Objects.equals(digitalSubscribers, that.digitalSubscribers) && Objects.equals(digitalSubscriptions, that.digitalSubscriptions) && Objects.equals(consumingBusinessCapabilities, that.consumingBusinessCapabilities) && Objects.equals(usesDigitalServices, that.usesDigitalServices) && Objects.equals(supportsBusinessCapabilities, that.supportsBusinessCapabilities) && Objects.equals(dependsOnBusinessCapabilities, that.dependsOnBusinessCapabilities) && Objects.equals(supplyFrom, that.supplyFrom) && Objects.equals(supplyTo, that.supplyTo) && Objects.equals(usedInSolutionComponents, that.usedInSolutionComponents) && Objects.equals(nestedSolutionComponents, that.nestedSolutionComponents) && Objects.equals(interactingWithActors, that.interactingWithActors) && Objects.equals(interactingWithSolutionComponents, that.interactingWithSolutionComponents) && Objects.equals(solutionComponent, that.solutionComponent) && Objects.equals(solutionPorts, that.solutionPorts) && Objects.equals(wiredTo, that.wiredTo) && Objects.equals(alignsToPort, that.alignsToPort) && Objects.equals(delegationPorts, that.delegationPorts) && Objects.equals(derivedFrom, that.derivedFrom) && Objects.equals(implementedBy, that.implementedBy) && Objects.equals(usedInImplementationOf, that.usedInImplementationOf) && Objects.equals(implementationResources, that.implementationResources) && Objects.equals(describesDesignOf, that.describesDesignOf) && Objects.equals(solutionDesigns, that.solutionDesigns) && Objects.equals(lineageLinkage, that.lineageLinkage) && Objects.equals(otherRelatedElements, that.otherRelatedElements) && Objects.equals(relatedBy, that.relatedBy);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, sampleData, sourcesOfSampleData, templateCreatedElements, sourcedFromTemplate, templatesForCataloguing, templateUses, actionSource, requestedActions, actionCause, relatedActions, actionTargets, actionsForTarget, searchKeywords, keywordElements, externalReferences, referencingElements, alsoKnownAs, equivalentElements, resourceList, resourceListUsers, providesMoreInformation, describes, propertyFacets, facetedElements, memberOfCollections, collectionMembers, knownLocations, localResources, peerLocations, groupingLocations, nestedLocations, serverEndpoints, serverForEndpoint, hostedITAssets, deployedTo, storageVolumes, providesStorageFor, managesStorageFor, storedOn, installedOn, operatingPlatforms, includesSoftwarePackages, packagedInOperatingPlatforms, dependsOnSoftwarePackages, runningWithAsset, managedHosts, hostCluster, resultsStoredIn, populatedUsingQuery, consumedByCapabilities, capabilityConsumedAssets, capabilities, hostedByDeployedITInfrastructure, cohortMembership, registeredWithCohorts, visibleEndpoints, visibleInNetworks, userProfile, userIdentities, contactDetails, contacts, myFollowers, myPeers, superTeam, subTeams, profilesForAsset, assetsUsingProfile, performsRoles, rolePerformers, relevantToScopes, scopedElements, assignmentScope, assignedActors, contributionRecord, contributorProfile, dependentProjects, dependsOnProjects, managingProjects, managedProjects, likes, likedElement, informalTags, taggedElements, reviews, reviewedElement, comments, commentedOnElement, answeredQuestions, acceptedAnswers, crowdSourcingContributors, crowdSourcedContributions, noteLogSubjects, noteLogs, presentInNoteLogs, noteLogEntries, connections, connectorType, endpoint, connectedResources, embeddedConnections, parentConnections, supportedDataSets, dataSetContent, apiEndpoints, supportedAPIs, parentProcesses, childProcesses, ports, portOwningProcesses, portDelegatingFrom, portDelegatingTo, homeFolder, nestedFiles, linkedFiles, linkedFolders, parentFolder, nestedFolders, linkedMediaFiles, associatedLogs, associatedLogSubjects, archiveContents, packagedInArchiveFiles, reportOriginator, generatedReports, reportSubjects, reports, priorReports, followOnReports, relatedTerms, usedInContexts, contextRelevantTerms, meaningForDataElements, meanings, semanticDefinitions, semanticallyAssociatedDefinitions, supplementaryProperties, supplementsElement, governedBy, governedElements, peerGovernanceDefinitions, supportedGovernanceDefinitions, supportingGovernanceDefinitions, configuredInSecurityCollections, userAccounts, definedInSecretsCollection, securityAccessControls, listedInSecretsCollection, securityLists, usedInAccessControls, associatedSecurityLists, inheritsFromZone, controlsZones, broaderSubjectArea, nestedSubjectAreas, metrics, measurements, monitoredThrough, monitoredResources, interestingNotificationTypes, subscribers, excludedFromRequirements, exceptions, calledFromGovernanceEngines, supportedGovernanceServices, associatedGovernanceActions, predefinedTargetForAction, triggeredFrom, firstStep, dependedOnProcessSteps, followOnProcessSteps, supportsGovernanceActions, governanceActionExecutor, includedInIntegrationGroups, supportedIntegrationConnectors, refreshedByConnectors, catalogTargets, peerDuplicateOrigin, peerDuplicatePartner, consolidatedDuplicateOrigin, consolidatedDuplicateResult, impactedResources, incidentReports, licenses, licensedElements, certifications, certifiedElements, schemaType, describesStructure, parentSchemaElements, schemaOptions, schemaAttributes, externalSchemaType, mapFromElement, mapToElement, queries, containsOperations, apiHeader, apiRequest, apiResponse, databaseSchemaType, containsDBSchemas, linkedToPrimaryKey, foreignKeys, vertices, edges, describedByDataValueSpecifications, dataValueSpecifications, assignedToDataValueSpecifications, assignedDataValueSpecifications, superDataValueSpecification, subDataValueSpecifications, madeOfDataClasses, partOfDataClasses, validValues, validValueConsumers, referenceValues, assignedItems, matchingValues, consistentValues, associatedValues, validValueMembers, memberOfValidValueSets, validValueImplementations, canonicalValidValues, specificationProperties, specificationPropertyUsers, dataStructureDefinition, usedInCertifications, dataDescription, describesDataFor, containsDataFields, partOfDataStructures, parentDataFields, nestedDataFields, linkedToDataFields, linkedFromDataFields, derivedFromDataStructure, equivalentSchemaType, derivedFromDataField, equivalentSchemaAttribute, relatedDesignPatterns, consumingDesignPatterns, consumedDesignPatterns, generalizedDesignPattern, specializedDesignPattern, fromSurveyReport, reportedAnnotations, previousAnnotations, annotationExtensions, annotationSubjects, associatedAnnotations, annotationMatches, matchedByAnnotations, resourceProfileAnnotations, resourceProfileData, identifiedByRequestForActions, requestForActionTargets, usedByDigitalProducts, usesDigitalProducts, agreementItems, agreementContents, agreementActors, involvedInAgreements, contracts, agreementsForContract, digitalSubscribers, digitalSubscriptions, consumingBusinessCapabilities, usesDigitalServices, supportsBusinessCapabilities, dependsOnBusinessCapabilities, supplyFrom, supplyTo, usedInSolutionComponents, nestedSolutionComponents, interactingWithActors, interactingWithSolutionComponents, solutionComponent, solutionPorts, wiredTo, alignsToPort, delegationPorts, derivedFrom, implementedBy, usedInImplementationOf, implementationResources, describesDesignOf, solutionDesigns, lineageLinkage, otherRelatedElements, relatedBy);
    }
}
