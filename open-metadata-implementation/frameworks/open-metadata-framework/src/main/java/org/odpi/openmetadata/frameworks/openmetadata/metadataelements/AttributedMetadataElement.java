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
    private ElementHeader                       elementHeader             = null;

    /*
     * Area 0
     */
    private List<RelatedMetadataElementSummary> sampleData                    = null; // SampleData (0010)
    private List<RelatedMetadataElementSummary> sourcesOfSampleData           = null; // SampleData (0010)
    private List<RelatedMetadataElementSummary> templateCreatedElements       = null; // SourcedFrom (0011)
    private RelatedMetadataElementSummary       sourcedFromTemplate           = null; // SourcedFrom (0011)
    private List<RelatedMetadataElementSummary> templatesForCataloguing       = null; // CatalogTemplate (0011)
    private List<RelatedMetadataElementSummary> supportedImplementationTypes  = null; // CatalogTemplate (0011)
    private RelatedMetadataElementSummary       actionSource                  = null; // ActionRequester (0013)
    private List<RelatedMetadataElementSummary> requestedActions              = null; // ActionRequester (0013)
    private List<RelatedMetadataElementSummary> actionSponsors                = null; // ActionSponsor (0013)
    private List<RelatedMetadataElementSummary> trackedActions                = null; // ActionSponsor (0013)
    private List<RelatedMetadataElementSummary> actionCause                   = null; // Actions (0013)
    private List<RelatedMetadataElementSummary> relatedActions                = null; // Actions (0013)
    private List<RelatedMetadataElementSummary> actionTargets                 = null; // ActionTarget (0013)
    private List<RelatedMetadataElementSummary> actionsForTarget              = null; // ActionTarget (0013)
    private List<RelatedMetadataElementSummary> searchKeywords                 = null; // SearchKeywordLink (0012)
    private List<RelatedMetadataElementSummary> keywordElements               = null; // SearchKeywordLink (0012)
    private List<RelatedMetadataElementSummary> externalReferences            = null; // ExternalReferenceLink (0014)
    private List<RelatedMetadataElementSummary> referencingElements           = null; // ExternalReferenceLink (0014)
    private List<RelatedMetadataElementSummary> alsoKnownAs                   = null; // ExternalIdLink (0017)
    private List<RelatedMetadataElementSummary> equivalentElements            = null; // ExternalIdLink (0017)
    private List<RelatedMetadataElementSummary> recognizedExternalIdentifiers = null; // ExternalIdScope (0017)
    private List<RelatedMetadataElementSummary> identifierScopedTo            = null; // ExternalIdScope (0017)
    private List<RelatedMetadataElementSummary> resourceList                  = null; // ResourceList (0019)
    private List<RelatedMetadataElementSummary> resourceListUsers             = null; // ResourceList (0019)
    private List<RelatedMetadataElementSummary> propertyFacets                = null; // ReferenceableFacet (0020)
    private List<RelatedMetadataElementSummary> facetedElements               = null; // ReferenceableFacet (0020)
    private List<RelatedMetadataElementSummary> memberOfCollections           = null; // CollectionMembership (0021)
    private List<RelatedMetadataElementSummary> collectionMembers             = null; // CollectionMembership (0021)


    private List<RelatedMetadataElementSummary> serverEndpoints   = null; // ServerEndpoint (0026)
    private RelatedMetadataElementSummary       serverForEndpoint = null; // ServerEndpoint (0026)
    private List<RelatedMetadataElementSummary> hostedITAssets    = null; // DeployedOn (0035)
    private List<RelatedMetadataElementSummary> deployedTo                    = null; // DeployedOn (0035)
    private List<RelatedMetadataElementSummary> storageVolumes                = null; // AttachedStorage (0036)
    private List<RelatedMetadataElementSummary> hostsUsingStorageVolume       = null; // AttachedStorage (0036)
    private List<RelatedMetadataElementSummary> consumedByCapabilities        = null; // CapabilityAssetUse (0045)
    private List<RelatedMetadataElementSummary> capabilityConsumedAssets      = null; // CapabilityAssetUse (0045)
    private List<RelatedMetadataElementSummary> supportedSoftwareCapabilities = null; // SupportedSoftwareCapability (0042)
    private List<RelatedMetadataElementSummary> capabilityHostedBy            = null; // SupportedSoftwareCapability (0042)
    private List<RelatedMetadataElementSummary> visibleEndpoints              = null; // VisibleEndpoint (0070)
    private List<RelatedMetadataElementSummary> visibleInNetworks             = null; // VisibleEndpoint (0070)

    /*
     * Area 1
     */

    private List<RelatedMetadataElementSummary> contactDetails = null; // ContactThrough (0111)
    private List<RelatedMetadataElementSummary> contacts       = null; // ContactThrough (0111)

    private List<RelatedMetadataElementSummary> relevantToScope           = null; // ScopedBy (0120)
    private List<RelatedMetadataElementSummary> scopedElements            = null; // ScopedBy (0120)
    private List<RelatedMetadataElementSummary> assignmentScope           = null; // AssignmentScope (0120)
    private List<RelatedMetadataElementSummary> assignedActors            = null; // AssignmentScope (0120)
    private List<RelatedMetadataElementSummary> commissionedElements      = null; // Stakeholder (0120)
    private List<RelatedMetadataElementSummary> commissionedBy            = null; // Stakeholder (0120)

    private List<RelatedMetadataElementSummary> likes              = null;
    private RelatedMetadataElementSummary       likedElement       = null;
    private List<RelatedMetadataElementSummary> informalTags       = null;
    private List<RelatedMetadataElementSummary> taggedElements     = null;
    private List<RelatedMetadataElementSummary> reviews            = null;
    private RelatedMetadataElementSummary       reviewedElement    = null;
    private List<RelatedMetadataElementSummary> comments           = null;
    private RelatedMetadataElementSummary       commentedOnElement = null;

    /*
     * Area 2
     */

    private List<RelatedMetadataElementSummary> connections         = null; // AssetConnection (0205), ConnectorConnectionType, ConnectToEndpoint (0201)
    private RelatedMetadataElementSummary       connectorType       = null; // ConnectorConnectionType (0201)
    private RelatedMetadataElementSummary       endpoint            = null; // ConnectToEndpoint (0201)
    private List<RelatedMetadataElementSummary> connectedAssets     = null; // AssetConnection (0205)
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
    private List<RelatedMetadataElementSummary> portDelegatingTo              = null; // PortDelegation (0217)
    private RelatedMetadataElementSummary       homeFolder                    = null; // NestedFile (0220)
    private List<RelatedMetadataElementSummary> nestedFiles                   = null; // NestedFile (0220)
    private List<RelatedMetadataElementSummary> linkedFiles                   = null; // LinkedFile (0220)
    private List<RelatedMetadataElementSummary> linkedFolders                 = null; // LinkedFile (0220)
    private RelatedMetadataElementSummary       parentFolder                  = null; // FolderHierarchy (0220)
    private List<RelatedMetadataElementSummary> nestedFolders                 = null; // FolderHierarchy (0220)
    private List<RelatedMetadataElementSummary> linkedMediaFiles              = null; // LinkedMedia (0221)
    private List<RelatedMetadataElementSummary> topicSubscribers              = null; // TopicSubscribers (0223)
    private List<RelatedMetadataElementSummary> topicsForSubscribers          = null; // TopicSubscribers (0223)
    private List<RelatedMetadataElementSummary> associatedLogs                = null; // AssociatedLog (0223)
    private List<RelatedMetadataElementSummary> associatedLogSubjects         = null; // AssociatedLog (0223)
    private RelatedMetadataElementSummary       cohortMember                  = null; // CohortMemberMetadataCollection (0225)
    private RelatedMetadataElementSummary       localMetadataCollection       = null; // CohortMemberMetadataCollection (0225)
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

    private List<RelatedMetadataElementSummary> associatedGlossaries              = null; // CategoryHierarchy (0320)
    private List<RelatedMetadataElementSummary> glossaryCategories                = null; // CategoryHierarchy (0320)
    private RelatedMetadataElementSummary       parentGlossary                    = null; // ParentGlossary (0330)
    private List<RelatedMetadataElementSummary> terms                             = null; // ParentGlossary (0330)
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

    private List<RelatedMetadataElementSummary> licenses          = null; // License (0481)
    private List<RelatedMetadataElementSummary> licensedElements  = null; // License (0481)
    private List<RelatedMetadataElementSummary> certifications    = null; // Certification (0482)
    private List<RelatedMetadataElementSummary> certifiedElements = null; // Certification (0482)

    private List<RelatedMetadataElementSummary> agreementItems        = null; // AgreementItem (0484)
    private List<RelatedMetadataElementSummary> agreementContents     = null; // AgreementItem (0484)
    private List<RelatedMetadataElementSummary> agreementActors       = null; // AgreementActor (0484)
    private List<RelatedMetadataElementSummary> involvedInAgreements  = null; // AgreementActor (0484)
    private List<RelatedMetadataElementSummary> contracts             = null; // ContractLink (0484)
    private List<RelatedMetadataElementSummary> agreementsForContract = null; // ContractLink (0484)

    /*
     * Area 5
     */
    private List<RelatedMetadataElementSummary> parentSchemaElements = null; // SchemaTypeOption (0501), AttributeForSchema, NestedSchemaAttribute (0505)
    private List<RelatedMetadataElementSummary> schemaOptions        = null; // SchemaTypeOption (0501)
    private List<RelatedMetadataElementSummary> schemaAttributes     = null;  // AttributeForSchema, NestedSchemaAttribute (0505)
    private RelatedMetadataElementSummary       externalSchemaType   = null; // LinkedExternalSchemaType (0507)
    private RelatedMetadataElementSummary       mapFromElement       = null; // MapFromElementType (0511)
    private RelatedMetadataElementSummary       mapToElement         = null; // MapToElementType (0511)
    private List<RelatedMetadataElementSummary> queries              = null;  // DerivedSchemaTypeQueryTarget (0512)

    private RelatedMetadataElementSummary       linkedToPrimaryKey     = null; // ForeignKey (0534)
    private List<RelatedMetadataElementSummary> foreignKeys            = null; // ForeignKey (0534)
    private List<RelatedMetadataElementSummary> vertices               = null; // GraphEdgeLink (0533)
    private List<RelatedMetadataElementSummary> edges                  = null; // GraphEdgeLink (0533)

    private RelatedMetadataElementSummary       rootSchemaType                = null; // AssetSchemaType (0503)
    private RelatedMetadataElementSummary       describesStructureForAsset    = null; // AssetSchemaType (0503)

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

    /*
     * Area 7
     */
    private List<RelatedMetadataElementSummary> usedByDigitalProducts = null;
    private List<RelatedMetadataElementSummary> usesDigitalProducts   = null;


    private List<RelatedMetadataElementSummary> digitalSubscribers = null;
    private List<RelatedMetadataElementSummary> digitalSubscriptions = null;


    private List<RelatedMetadataElementSummary> lineageLinkage            = null; // Many

    private List<RelatedMetadataElementSummary> derivedFrom               = null; // ImplementedBy (0737)
    private List<RelatedMetadataElementSummary> implementedBy             = null; // ImplementedBy (0737)
    private List<RelatedMetadataElementSummary> usedInImplementationOf    = null; // Implementation Resource (0737)
    private List<RelatedMetadataElementSummary> implementationResources   = null; // Implementation Resource (0737)

    /*
     * Others
     */
    private List<RelatedMetadataElementSummary> otherRelatedElements      = null;
    private RelatedBy                           relatedBy                 = null;
    private String                              mermaidGraph              = null;

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

            templateCreatedElements       = template.getTemplateCreatedElements();
            sourcedFromTemplate           = template.getSourcedFromTemplate();
            templatesForCataloguing       = template.getTemplatesForCataloguing();
            supportedImplementationTypes  = template.getSupportedImplementationTypes();

            searchKeywords   = template.getSearchKeywords();
            keywordElements  = template.getKeywordElements();

            actionSource     = template.getActionSource();
            requestedActions = template.getRequestedActions();
            actionSponsors   = template.getActionSponsors();
            trackedActions   = template.getTrackedActions();
            actionCause      = template.getActionCause();
            relatedActions   = template.getRelatedActions();
            actionTargets    = template.getActionTargets();
            actionsForTarget = template.getActionsForTarget();

            externalReferences  = template.getExternalReferences();
            referencingElements = template.getReferencingElements();

            alsoKnownAs                   = template.getAlsoKnownAs();
            equivalentElements            = template.getEquivalentElements();
            identifierScopedTo            = template.getIdentifierScopedTo();
            recognizedExternalIdentifiers = template.getRecognizedExternalIdentifiers();

            resourceList      = template.getResourceList();
            resourceListUsers = template.getResourceListUsers();

            propertyFacets  = template.getPropertyFacets();
            facetedElements = template.getFacetedElements();

            memberOfCollections = template.getMemberOfCollections();
            collectionMembers   = template.getCollectionMembers();

            serverEndpoints   = template.getServerEndpoints();
            serverForEndpoint = template.getServerForEndpoint();

            hostedITAssets = template.getHostedITAssets();
            deployedTo     = template.getDeployedTo();

            storageVolumes                = template.getStorageVolumes();
            hostsUsingStorageVolume       = template.getHostsUsingStorageVolume();

            consumedByCapabilities   = template.getConsumedByCapabilities();
            capabilityConsumedAssets = template.getCapabilityConsumedAssets();

            supportedSoftwareCapabilities = template.getSupportedSoftwareCapabilities();
            capabilityHostedBy            = template.getCapabilityHostedBy();

            visibleEndpoints              = template.getVisibleEndpoints();
            visibleInNetworks             = template.getVisibleInNetworks();


            /*
             * Area 1
             */

            contactDetails = template.getContactDetails();
            contacts       = template.getContacts();

            relevantToScope           = template.getRelevantToScope();
            scopedElements            = template.getScopedElements();
            assignmentScope           = template.getAssignmentScope();
            assignedActors            = template.getAssignedActors();
            commissionedElements      = template.getCommissionedElements();
            commissionedBy            = template.getCommissionedBy();

            likes              = template.getLikes();
            likedElement       = template.getLikedElement();
            reviews            = template.getReviews();
            reviewedElement    = template.getReviewedElement();
            informalTags       = template.getInformalTags();
            taggedElements     = template.getTaggedElements();
            comments           = template.getComments();
            commentedOnElement = template.getCommentedOnElement();

            /*
             * Area 2
             */
            connections         = template.getConnections();
            connectorType       = template.getConnectorType();
            endpoint            = template.getEndpoint();
            connectedAssets     = template.getConnectedAssets();
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
            topicSubscribers        = template.getTopicSubscribers();
            topicsForSubscribers    = template.getTopicsForSubscribers();
            associatedLogs          = template.getAssociatedLogs();
            associatedLogSubjects   = template.getAssociatedLogSubjects();
            cohortMember            = template.getCohortMember();
            localMetadataCollection = template.getLocalMetadataCollection();
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
            glossaryCategories                = template.getGlossaryCategories();
            associatedGlossaries              = template.getAssociatedGlossaries();
            parentGlossary                    = template.getParentGlossary();
            terms                             = template.getTerms();
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
            rootSchemaType             = template.getRootSchemaType();
            describesStructureForAsset = template.getDescribesStructureForAsset();
            schemaAttributes           = template.getSchemaAttributes();
            parentSchemaElements       = template.getParentSchemaElements();

            mapFromElement     = template.getMapFromElement();
            mapToElement       = template.getMapToElement();
            externalSchemaType = template.getExternalSchemaType();
            schemaOptions      = template.getSchemaOptions();
            queries            = template.getQueries();

            linkedToPrimaryKey = template.getLinkedToPrimaryKey();
            foreignKeys = template.getForeignKeys();

            vertices = template.getVertices();
            edges    = template.getEdges();

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

            /*
             * Area 6
             */

            /*
             * Area 7
             */
            usedByDigitalProducts = template.getUsedByDigitalProducts();
            usesDigitalProducts   = template.getUsesDigitalProducts();

            digitalSubscribers   = template.getDigitalSubscribers();
            digitalSubscriptions = template.getDigitalSubscriptions();

            lineageLinkage            = template.getLineageLinkage();
            derivedFrom               = template.getDerivedFrom();
            implementedBy             = template.getImplementedBy();
            usedInImplementationOf    = template.getUsedInImplementationOf();
            implementationResources   = template.getImplementationResources();

            /*
             * Others
             */

            otherRelatedElements      = template.getOtherRelatedElements();
            relatedBy                 = template.getRelatedBy();
            mermaidGraph              = template.getMermaidGraph();
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

    public List<RelatedMetadataElementSummary> getSourcesOfSampleData()
    {
        return sourcesOfSampleData;
    }

    public void setSourcesOfSampleData(List<RelatedMetadataElementSummary> sourcesOfSampleData)
    {
        this.sourcesOfSampleData = sourcesOfSampleData;
    }


    public List<RelatedMetadataElementSummary> getTemplateCreatedElements()
    {
        return templateCreatedElements;
    }

    public void setTemplateCreatedElements(List<RelatedMetadataElementSummary> templateCreatedElements)
    {
        this.templateCreatedElements = templateCreatedElements;
    }

    public RelatedMetadataElementSummary getSourcedFromTemplate()
    {
        return sourcedFromTemplate;
    }

    public void setSourcedFromTemplate(RelatedMetadataElementSummary sourcedFromTemplate)
    {
        this.sourcedFromTemplate = sourcedFromTemplate;
    }

    public List<RelatedMetadataElementSummary> getTemplatesForCataloguing()
    {
        return templatesForCataloguing;
    }

    public void setTemplatesForCataloguing(List<RelatedMetadataElementSummary> templatesForCataloguing)
    {
        this.templatesForCataloguing = templatesForCataloguing;
    }

    public List<RelatedMetadataElementSummary> getSupportedImplementationTypes()
    {
        return supportedImplementationTypes;
    }

    public void setSupportedImplementationTypes(List<RelatedMetadataElementSummary> supportedImplementationTypes)
    {
        this.supportedImplementationTypes = supportedImplementationTypes;
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


    public List<RelatedMetadataElementSummary> getReferencingElements()
    {
        return referencingElements;
    }

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


    public List<RelatedMetadataElementSummary> getEquivalentElements()
    {
        return equivalentElements;
    }

    public void setEquivalentElements(List<RelatedMetadataElementSummary> equivalentElements)
    {
        this.equivalentElements = equivalentElements;
    }


    public List<RelatedMetadataElementSummary> getRecognizedExternalIdentifiers()
    {
        return recognizedExternalIdentifiers;
    }

    public void setRecognizedExternalIdentifiers(List<RelatedMetadataElementSummary> recognizedExternalIdentifiers)
    {
        this.recognizedExternalIdentifiers = recognizedExternalIdentifiers;
    }

    public List<RelatedMetadataElementSummary> getIdentifierScopedTo()
    {
        return identifierScopedTo;
    }

    public void setIdentifierScopedTo(List<RelatedMetadataElementSummary> identifierScopedTo)
    {
        this.identifierScopedTo = identifierScopedTo;
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


    public List<RelatedMetadataElementSummary> getCollectionMembers()
    {
        return collectionMembers;
    }

    public void setCollectionMembers(List<RelatedMetadataElementSummary> collectionMembers)
    {
        this.collectionMembers = collectionMembers;
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
     * Return end 1 of DeployedOn relationship.  These are, for example, platforms and servers on a host..
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
     * Return the hosts that are using this storage volume.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getHostsUsingStorageVolume()
    {
        return hostsUsingStorageVolume;
    }


    /**
     * Set up the hosts that are using this storage volume.
     *
     * @param hostsUsingStorageVolume list of related elements
     */
    public void setHostsUsingStorageVolume(List<RelatedMetadataElementSummary> hostsUsingStorageVolume)
    {
        this.hostsUsingStorageVolume = hostsUsingStorageVolume;
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
    public List<RelatedMetadataElementSummary> getSupportedSoftwareCapabilities()
    {
        return supportedSoftwareCapabilities;
    }


    /**
     * Set up this software capabilities supported by this IT asset.
     *
     * @param supportedSoftwareCapabilities list of related elements
     */
    public void setSupportedSoftwareCapabilities(List<RelatedMetadataElementSummary> supportedSoftwareCapabilities)
    {
        this.supportedSoftwareCapabilities = supportedSoftwareCapabilities;
    }

    public List<RelatedMetadataElementSummary> getCapabilityHostedBy()
    {
        return capabilityHostedBy;
    }

    public void setCapabilityHostedBy(List<RelatedMetadataElementSummary> capabilityHostedBy)
    {
        this.capabilityHostedBy = capabilityHostedBy;
    }

    public List<RelatedMetadataElementSummary> getVisibleEndpoints()
    {
        return visibleEndpoints;
    }

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


    public List<RelatedMetadataElementSummary> getContacts()
    {
        return contacts;
    }

    public void setContacts(List<RelatedMetadataElementSummary> contacts)
    {
        this.contacts = contacts;
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

    public List<RelatedMetadataElementSummary> getActionSponsors()
    {
        return actionSponsors;
    }

    public void setActionSponsors(List<RelatedMetadataElementSummary> actionSponsors)
    {
        this.actionSponsors = actionSponsors;
    }

    public List<RelatedMetadataElementSummary> getTrackedActions()
    {
        return trackedActions;
    }

    public void setTrackedActions(List<RelatedMetadataElementSummary> trackedActions)
    {
        this.trackedActions = trackedActions;
    }

    public List<RelatedMetadataElementSummary> getActionCause()
    {
        return actionCause;
    }

    public void setActionCause(List<RelatedMetadataElementSummary> actionCause)
    {
        this.actionCause = actionCause;
    }

    public List<RelatedMetadataElementSummary> getRelatedActions()
    {
        return relatedActions;
    }

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

    public List<RelatedMetadataElementSummary> getConnections()
    {
        return connections;
    }

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
     * Return the assets reached through this connection.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getConnectedAssets()
    {
        return connectedAssets;
    }


    /**
     * Set up the assets reached through this connection.
     *
     * @param connectedAssets list
     */
    public void setConnectedAssets(List<RelatedMetadataElementSummary> connectedAssets)
    {
        this.connectedAssets = connectedAssets;
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

    public List<RelatedMetadataElementSummary> getSupportedDataSets()
    {
        return supportedDataSets;
    }

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

    public List<RelatedMetadataElementSummary> getSupportedAPIs()
    {
        return supportedAPIs;
    }

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

    public List<RelatedMetadataElementSummary> getPorts()
    {
        return ports;
    }

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

    public List<RelatedMetadataElementSummary> getPortDelegatingTo()
    {
        return portDelegatingTo;
    }

    public void setPortDelegatingTo(List<RelatedMetadataElementSummary> portDelegatingTo)
    {
        this.portDelegatingTo = portDelegatingTo;
    }

    public RelatedMetadataElementSummary getHomeFolder()
    {
        return homeFolder;
    }

    public void setHomeFolder(RelatedMetadataElementSummary homeFolder)
    {
        this.homeFolder = homeFolder;
    }

    public List<RelatedMetadataElementSummary> getNestedFiles()
    {
        return nestedFiles;
    }

    public void setNestedFiles(List<RelatedMetadataElementSummary> nestedFiles)
    {
        this.nestedFiles = nestedFiles;
    }

    public List<RelatedMetadataElementSummary> getLinkedFiles()
    {
        return linkedFiles;
    }

    public void setLinkedFiles(List<RelatedMetadataElementSummary> linkedFiles)
    {
        this.linkedFiles = linkedFiles;
    }

    public List<RelatedMetadataElementSummary> getLinkedFolders()
    {
        return linkedFolders;
    }

    public void setLinkedFolders(List<RelatedMetadataElementSummary> linkedFolders)
    {
        this.linkedFolders = linkedFolders;
    }

    public RelatedMetadataElementSummary getParentFolder()
    {
        return parentFolder;
    }

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

    public List<RelatedMetadataElementSummary> getLinkedMediaFiles()
    {
        return linkedMediaFiles;
    }

    public void setLinkedMediaFiles(List<RelatedMetadataElementSummary> linkedMediaFiles)
    {
        this.linkedMediaFiles = linkedMediaFiles;
    }

    public List<RelatedMetadataElementSummary> getTopicSubscribers()
    {
        return topicSubscribers;
    }

    public void setTopicSubscribers(List<RelatedMetadataElementSummary> topicSubscribers)
    {
        this.topicSubscribers = topicSubscribers;
    }

    public List<RelatedMetadataElementSummary> getTopicsForSubscribers()
    {
        return topicsForSubscribers;
    }

    public void setTopicsForSubscribers(List<RelatedMetadataElementSummary> topicsForSubscribers)
    {
        this.topicsForSubscribers = topicsForSubscribers;
    }

    public List<RelatedMetadataElementSummary> getAssociatedLogs()
    {
        return associatedLogs;
    }

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

    public RelatedMetadataElementSummary getCohortMember()
    {
        return cohortMember;
    }

    public void setCohortMember(RelatedMetadataElementSummary cohortMember)
    {
        this.cohortMember = cohortMember;
    }

    public RelatedMetadataElementSummary getLocalMetadataCollection()
    {
        return localMetadataCollection;
    }

    public void setLocalMetadataCollection(RelatedMetadataElementSummary localMetadataCollection)
    {
        this.localMetadataCollection = localMetadataCollection;
    }

    public RelatedMetadataElementSummary getArchiveContents()
    {
        return archiveContents;
    }

    public void setArchiveContents(RelatedMetadataElementSummary archiveContents)
    {
        this.archiveContents = archiveContents;
    }

    public List<RelatedMetadataElementSummary> getPackagedInArchiveFiles()
    {
        return packagedInArchiveFiles;
    }

    public void setPackagedInArchiveFiles(List<RelatedMetadataElementSummary> packagedInArchiveFiles)
    {
        this.packagedInArchiveFiles = packagedInArchiveFiles;
    }

    public RelatedMetadataElementSummary getReportOriginator()
    {
        return reportOriginator;
    }

    public void setReportOriginator(RelatedMetadataElementSummary reportOriginator)
    {
        this.reportOriginator = reportOriginator;
    }

    public List<RelatedMetadataElementSummary> getGeneratedReports()
    {
        return generatedReports;
    }

    public void setGeneratedReports(List<RelatedMetadataElementSummary> generatedReports)
    {
        this.generatedReports = generatedReports;
    }

    public List<RelatedMetadataElementSummary> getReportSubjects()
    {
        return reportSubjects;
    }

    public void setReportSubjects(List<RelatedMetadataElementSummary> reportSubjects)
    {
        this.reportSubjects = reportSubjects;
    }

    public List<RelatedMetadataElementSummary> getReports()
    {
        return reports;
    }

    public void setReports(List<RelatedMetadataElementSummary> reports)
    {
        this.reports = reports;
    }

    public List<RelatedMetadataElementSummary> getPriorReports()
    {
        return priorReports;
    }

    public void setPriorReports(List<RelatedMetadataElementSummary> priorReports)
    {
        this.priorReports = priorReports;
    }

    public List<RelatedMetadataElementSummary> getFollowOnReports()
    {
        return followOnReports;
    }

    public void setFollowOnReports(List<RelatedMetadataElementSummary> followOnReports)
    {
        this.followOnReports = followOnReports;
    }

    /**
     * Return the parent glossary for this element.
     *
     * @return glossary details
     */
    public RelatedMetadataElementSummary getParentGlossary()
    {
        return parentGlossary;
    }


    public List<RelatedMetadataElementSummary> getAssociatedGlossaries()
    {
        return associatedGlossaries;
    }

    public void setAssociatedGlossaries(List<RelatedMetadataElementSummary> associatedGlossaries)
    {
        this.associatedGlossaries = associatedGlossaries;
    }

    public List<RelatedMetadataElementSummary> getGlossaryCategories()
    {
        return glossaryCategories;
    }

    public void setGlossaryCategories(List<RelatedMetadataElementSummary> glossaryCategories)
    {
        this.glossaryCategories = glossaryCategories;
    }

    /**
     * Set up parent glossary for this element.
     *
     * @param parentGlossary glossary details
     */
    public void setParentGlossary(RelatedMetadataElementSummary parentGlossary)
    {
        this.parentGlossary = parentGlossary;
    }

    public List<RelatedMetadataElementSummary> getTerms()
    {
        return terms;
    }

    public void setTerms(List<RelatedMetadataElementSummary> terms)
    {
        this.terms = terms;
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

    public List<RelatedMetadataElementSummary> getUsedInContexts()
    {
        return usedInContexts;
    }

    public void setUsedInContexts(List<RelatedMetadataElementSummary> usedInContexts)
    {
        this.usedInContexts = usedInContexts;
    }

    public List<RelatedMetadataElementSummary> getContextRelevantTerms()
    {
        return contextRelevantTerms;
    }

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

    public RelatedMetadataElementSummary getSupplementsElement()
    {
        return supplementsElement;
    }

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


    public List<RelatedMetadataElementSummary> getFacetedElements()
    {
        return facetedElements;
    }

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


    public List<RelatedMetadataElementSummary> getGovernedBy()
    {
        return governedBy;
    }

    public void setGovernedBy(List<RelatedMetadataElementSummary> governedBy)
    {
        this.governedBy = governedBy;
    }



    public List<RelatedMetadataElementSummary> getGovernedElements()
    {
        return governedElements;
    }

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


    public List<RelatedMetadataElementSummary> getLicenses()
    {
        return licenses;
    }

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

    public List<RelatedMetadataElementSummary> getCertifications()
    {
        return certifications;
    }

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

    public List<RelatedMetadataElementSummary> getRelevantToScope()
    {
        return relevantToScope;
    }

    public void setRelevantToScope(List<RelatedMetadataElementSummary> relevantToScope)
    {
        this.relevantToScope = relevantToScope;
    }

    public List<RelatedMetadataElementSummary> getScopedElements()
    {
        return scopedElements;
    }

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

    public List<RelatedMetadataElementSummary> getAssignedActors()
    {
        return assignedActors;
    }

    public void setAssignedActors(List<RelatedMetadataElementSummary> assignedActors)
    {
        this.assignedActors = assignedActors;
    }

    public List<RelatedMetadataElementSummary> getCommissionedElements()
    {
        return commissionedElements;
    }

    public void setCommissionedElements(List<RelatedMetadataElementSummary> commissionedElements)
    {
        this.commissionedElements = commissionedElements;
    }

    public List<RelatedMetadataElementSummary> getCommissionedBy()
    {
        return commissionedBy;
    }

    public void setCommissionedBy(List<RelatedMetadataElementSummary> commissionedBy)
    {
        this.commissionedBy = commissionedBy;
    }

    public List<RelatedMetadataElementSummary> getDerivedFrom()
    {
        return derivedFrom;
    }

    public void setDerivedFrom(List<RelatedMetadataElementSummary> derivedFrom)
    {
        this.derivedFrom = derivedFrom;
    }

    public List<RelatedMetadataElementSummary> getImplementedBy()
    {
        return implementedBy;
    }

    public void setImplementedBy(List<RelatedMetadataElementSummary> implementedBy)
    {
        this.implementedBy = implementedBy;
    }

    public List<RelatedMetadataElementSummary> getUsedInImplementationOf()
    {
        return usedInImplementationOf;
    }

    public void setUsedInImplementationOf(List<RelatedMetadataElementSummary> usedInImplementationOf)
    {
        this.usedInImplementationOf = usedInImplementationOf;
    }

    public List<RelatedMetadataElementSummary> getImplementationResources()
    {
        return implementationResources;
    }

    public void setImplementationResources(List<RelatedMetadataElementSummary> implementationResources)
    {
        this.implementationResources = implementationResources;
    }



    /**
     * Return the attached schema for this asset.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getRootSchemaType()
    {
        return rootSchemaType;
    }


    /**
     * Set up the attached schema for this asset.
     *
     * @param rootSchemaType related element
     */
    public void setRootSchemaType(RelatedMetadataElementSummary rootSchemaType)
    {
        this.rootSchemaType = rootSchemaType;
    }



    /**
     * Return the asset that this schema describes.
     *
     * @return SchemaElement
     */
    public RelatedMetadataElementSummary getDescribesStructureForAsset()
    {
        return describesStructureForAsset;
    }


    /**
     * Set up the asset that this schema describes.
     *
     * @param describesStructureForAsset SchemaElement
     */
    public void setDescribesStructureForAsset(RelatedMetadataElementSummary describesStructureForAsset)
    {
        this.describesStructureForAsset = describesStructureForAsset;
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

    public List<RelatedMetadataElementSummary> getVertices()
    {
        return vertices;
    }

    public void setVertices(List<RelatedMetadataElementSummary> vertices)
    {
        this.vertices = vertices;
    }

    public List<RelatedMetadataElementSummary> getEdges()
    {
        return edges;
    }

    public void setEdges(List<RelatedMetadataElementSummary> edges)
    {
        this.edges = edges;
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


    public List<RelatedMetadataElementSummary> getCanonicalValidValues()
    {
        return canonicalValidValues;
    }


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


    public List<RelatedMetadataElementSummary> getUsedByDigitalProducts()
    {
        return usedByDigitalProducts;
    }

    public void setUsedByDigitalProducts(List<RelatedMetadataElementSummary> usedByDigitalProducts)
    {
        this.usedByDigitalProducts = usedByDigitalProducts;
    }

    public List<RelatedMetadataElementSummary> getUsesDigitalProducts()
    {
        return usesDigitalProducts;
    }

    public void setUsesDigitalProducts(List<RelatedMetadataElementSummary> usesDigitalProducts)
    {
        this.usesDigitalProducts = usesDigitalProducts;
    }

    public List<RelatedMetadataElementSummary> getAgreementItems()
    {
        return agreementItems;
    }

    public void setAgreementItems(List<RelatedMetadataElementSummary> agreementItems)
    {
        this.agreementItems = agreementItems;
    }

    public List<RelatedMetadataElementSummary> getAgreementContents()
    {
        return agreementContents;
    }

    public void setAgreementContents(List<RelatedMetadataElementSummary> agreementContents)
    {
        this.agreementContents = agreementContents;
    }

    public List<RelatedMetadataElementSummary> getAgreementActors()
    {
        return agreementActors;
    }

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

    public List<RelatedMetadataElementSummary> getDigitalSubscribers()
    {
        return digitalSubscribers;
    }

    public void setDigitalSubscribers(List<RelatedMetadataElementSummary> digitalSubscribers)
    {
        this.digitalSubscribers = digitalSubscribers;
    }

    public List<RelatedMetadataElementSummary> getDigitalSubscriptions()
    {
        return digitalSubscriptions;
    }

    public void setDigitalSubscriptions(List<RelatedMetadataElementSummary> digitalSubscriptions)
    {
        this.digitalSubscriptions = digitalSubscriptions;
    }

    public List<RelatedMetadataElementSummary> getContracts()
    {
        return contracts;
    }

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
     * Return the mermaid representation of this data structure.
     *
     * @return string markdown
     */
    public String getMermaidGraph()
    {
        return mermaidGraph;
    }


    /**
     * Set up the mermaid representation of this data structure.
     *
     * @param mermaidGraph markdown string
     */
    public void setMermaidGraph(String mermaidGraph)
    {
        this.mermaidGraph = mermaidGraph;
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
                ", externalReferences=" + externalReferences +
                ", alsoKnownAs=" + alsoKnownAs +
                ", validValues=" + validValues +
                ", contactMethods=" + contactDetails +
                ", memberOfCollections=" + memberOfCollections +
                ", semanticAssignments=" + meanings +
                ", attachedLikes=" + likes +
                ", attachedTags=" + informalTags +
                ", attachedKeywords=" + searchKeywords +
                ", attachedComments=" + comments +
                ", attachedReviews=" + reviews +
                ", resourceList=" + resourceList +
                ", supplementaryProperties=" + supplementaryProperties +
                ", propertyFacets=" + propertyFacets +
                ", lineageLinkage=" + lineageLinkage +
                ", governedBy=" + governedBy +
                ", licenses=" + licenses +
                ", certifications=" + certifications +
                ", relevantToScope=" + relevantToScope +
                ", scopedElements=" + scopedElements +
                ", assignmentScope=" + assignmentScope +
                ", assignedActors=" + assignedActors +
                ", commissionedElements=" + commissionedElements +
                ", commissionedBy=" + commissionedBy +
                ", derivedFrom=" + derivedFrom +
                ", implementedBy=" + implementedBy +
                ", usedInImplementationOf=" + usedInImplementationOf +
                ", implementationResources=" + implementationResources +
                ", otherRelatedElements=" + otherRelatedElements +
                ", relatedBy=" + relatedBy +
                ", mermaidGraph='" + mermaidGraph + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        AttributedMetadataElement that = (AttributedMetadataElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) && Objects.equals(externalReferences, that.externalReferences) && Objects.equals(alsoKnownAs, that.alsoKnownAs) && Objects.equals(validValues, that.validValues) && Objects.equals(contactDetails, that.contactDetails) && Objects.equals(memberOfCollections, that.memberOfCollections) && Objects.equals(meanings, that.meanings) && Objects.equals(likes, that.likes) && Objects.equals(informalTags, that.informalTags) && Objects.equals(searchKeywords, that.searchKeywords) && Objects.equals(comments, that.comments) && Objects.equals(reviews, that.reviews) && Objects.equals(resourceList, that.resourceList) && Objects.equals(supplementaryProperties, that.supplementaryProperties) && Objects.equals(propertyFacets, that.propertyFacets) && Objects.equals(lineageLinkage, that.lineageLinkage) && Objects.equals(governedBy, that.governedBy) && Objects.equals(licenses, that.licenses) && Objects.equals(certifications, that.certifications) && Objects.equals(relevantToScope, that.relevantToScope) && Objects.equals(scopedElements, that.scopedElements) && Objects.equals(assignmentScope, that.assignmentScope) && Objects.equals(assignedActors, that.assignedActors) && Objects.equals(commissionedElements, that.commissionedElements) && Objects.equals(commissionedBy, that.commissionedBy) && Objects.equals(derivedFrom, that.derivedFrom) && Objects.equals(implementedBy, that.implementedBy) && Objects.equals(usedInImplementationOf, that.usedInImplementationOf) && Objects.equals(implementationResources, that.implementationResources) && Objects.equals(otherRelatedElements, that.otherRelatedElements) && Objects.equals(relatedBy, that.relatedBy) && Objects.equals(mermaidGraph, that.mermaidGraph);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, externalReferences, alsoKnownAs, validValues, contactDetails, memberOfCollections, meanings, likes, informalTags, searchKeywords, comments, reviews, resourceList, supplementaryProperties, propertyFacets, lineageLinkage, governedBy, licenses, certifications, relevantToScope, scopedElements, assignmentScope, assignedActors, commissionedElements, commissionedBy, derivedFrom, implementedBy, usedInImplementationOf, implementationResources, otherRelatedElements, relatedBy, mermaidGraph);
    }
}
