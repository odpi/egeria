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
    private List<RelatedMetadataElementSummary> searchKeywords   = null; // SearchKeywordLink (0012)
    private List<RelatedMetadataElementSummary> keywordElements   = null; // SearchKeywordLink (0012)
    private List<RelatedMetadataElementSummary> externalReferences  = null; // ExternalReferenceLink (0014)
    private List<RelatedMetadataElementSummary> referencingElements = null; // ExternalReferenceLink (0014)
    private List<RelatedMetadataElementSummary> alsoKnownAs         = null; // ExternalIdLink (0017)
    private List<RelatedMetadataElementSummary> equivalentElements         = null; // ExternalIdLink (0017)
    private List<RelatedMetadataElementSummary> recognizedExternalIdentifiers         = null; // ExternalIdScope (0017)
    private List<RelatedMetadataElementSummary> identifierScopedTo         = null; // ExternalIdScope (0017)
    private List<RelatedMetadataElementSummary> resourceList              = null; // ResourceList (0019)
    private List<RelatedMetadataElementSummary> resourceListUsers              = null; // ResourceList (0019)
    private List<RelatedMetadataElementSummary> memberOfCollections       = null; // CollectionMembership (0021)
    private List<RelatedMetadataElementSummary> collectionMembers       = null; // CollectionMembership (0021)

    private List<RelatedMetadataElementSummary> propertyFacets            = null; // ReferenceableFacet (0020)
    private List<RelatedMetadataElementSummary> facetedElements           = null; // ReferenceableFacet (0020)

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


    /*
     * Area 3
     */

    private List<RelatedMetadataElementSummary> associatedGlossaries = null; // CategoryHierarchy (0320)
    private List<RelatedMetadataElementSummary> categories           = null; // CategoryHierarchy (0320)
    private RelatedMetadataElementSummary       parentGlossary       = null; // ParentGlossary (0330)
    private List<RelatedMetadataElementSummary> terms                = null; // ParentGlossary (0330)
    private List<RelatedMetadataElementSummary> relatedTerms           = null; // various (0350)
    private List<RelatedMetadataElementSummary> usedInContexts           = null; // UsedInContext (0360)
    private List<RelatedMetadataElementSummary> contextRelevantTerms           = null; // UsedInContext (0360)
    private List<RelatedMetadataElementSummary> meaningForDataElements = null;
    private List<RelatedMetadataElementSummary> meanings               = null;
    private List<RelatedMetadataElementSummary> semanticDefinitions    = null;
    private List<RelatedMetadataElementSummary> semanticallyAssociatedDefinitions = null;

    private List<RelatedMetadataElementSummary> supplementaryProperties   = null;
    private RelatedMetadataElementSummary       supplementsElement   = null;

    /*
     * Area 4
     */
    private List<RelatedMetadataElementSummary> governedBy                = null; // GovernedBy (0401)
    private List<RelatedMetadataElementSummary> governedElements           = null; // GovernedBy (0401)

    private List<RelatedMetadataElementSummary> peerGovernanceDefinitions      = null;
    private List<RelatedMetadataElementSummary> supportedGovernanceDefinitions = null;
    private List<RelatedMetadataElementSummary> supportingGovernanceDefinitions = null;

    private List<RelatedMetadataElementSummary> metrics          = null;
    private List<RelatedMetadataElementSummary> measuredDefinitions          = null;

    private List<RelatedMetadataElementSummary> licenses                  = null; // License (0481)
    private List<RelatedMetadataElementSummary> licensedElements           = null; // License (0481)
    private List<RelatedMetadataElementSummary> certifications            = null; // Certification (0482)
    private List<RelatedMetadataElementSummary> certifiedElements          = null; // Certification (0482)

    private List<RelatedMetadataElementSummary> agreementItems        = null; // AgreementItem (0484)
    private List<RelatedMetadataElementSummary> agreementContents        = null; // AgreementItem (0484)
    private List<RelatedMetadataElementSummary> agreementActors       = null; // AgreementActor (0484)
    private List<RelatedMetadataElementSummary> involvedInAgreements       = null; // AgreementActor (0484)
    private List<RelatedMetadataElementSummary> contracts             = null; // ContractLink (0484)
    private List<RelatedMetadataElementSummary> agreementsForContract             = null; // ContractLink (0484)

    /*
     * Area 5
     */
    private List<RelatedMetadataElementSummary> parentSchemaElements       = null; // SchemaTypeOption (0501), AttributeForSchema, NestedSchemaAttribute (0505)
    private List<RelatedMetadataElementSummary>       schemaOptions = null; // SchemaTypeOption (0501)
    private List<RelatedMetadataElementSummary> schemaAttributes = null;  // AttributeForSchema, NestedSchemaAttribute (0505)
    private RelatedMetadataElementSummary             externalSchemaType = null; // LinkedExternalSchemaType (0507)
    private RelatedMetadataElementSummary             mapFromElement = null; // MapFromElementType (0511)
    private RelatedMetadataElementSummary             mapToElement   = null; // MapToElementType (0511)
    private List<RelatedMetadataElementSummary> queries     = null;  // DerivedSchemaTypeQueryTarget (0512)

    private RelatedMetadataElementSummary       linkedToPrimaryKey     = null; // ForeignKey (0534)
    private List<RelatedMetadataElementSummary> foreignKeys            = null; // ForeignKey (0534)
    private List<RelatedMetadataElementSummary> vertices            = null; // GraphEdgeLink (0533)
    private List<RelatedMetadataElementSummary> edges            = null; // GraphEdgeLink (0533)

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


    private List<RelatedMetadataElementSummary> lineageLinkage            = null;

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
            elementHeader             = template.getElementHeader();

            searchKeywords   = template.getSearchKeywords();
            keywordElements   = template.getKeywordElements();

            externalReferences  = template.getExternalReferences();
            referencingElements = template.getReferencingElements();

            alsoKnownAs         = template.getAlsoKnownAs();
            equivalentElements        = template.getEquivalentElements();
            identifierScopedTo  = template.getIdentifierScopedTo();
            recognizedExternalIdentifiers = template.getRecognizedExternalIdentifiers();

            resourceList              = template.getResourceList();
            resourceListUsers              = template.getResourceListUsers();

            propertyFacets            = template.getPropertyFacets();
            facetedElements           = template.getFacetedElements();

            memberOfCollections       = template.getMemberOfCollections();
            collectionMembers       = template.getCollectionMembers();

            /*
             * Area 1
             */

            contactDetails      = template.getContactDetails();
            contacts      = template.getContacts();

            relevantToScope           = template.getRelevantToScope();
            scopedElements            = template.getScopedElements();
            assignmentScope           = template.getAssignmentScope();
            assignedActors            = template.getAssignedActors();
            commissionedElements      = template.getCommissionedElements();
            commissionedBy            = template.getCommissionedBy();

            likes            = template.getLikes();
            likedElement            = template.getLikedElement();
            reviews  = template.getReviews();
            reviewedElement = template.getReviewedElement();
            informalTags    = template.getInformalTags();
            taggedElements    = template.getTaggedElements();
            comments = template.getComments();
            commentedOnElement = template.getCommentedOnElement();

            /*
             * Area 3
             */
            categories             = template.getCategories();
            associatedGlossaries   = template.getAssociatedGlossaries();
            parentGlossary         = template.getParentGlossary();
            terms                  = template.getTerms();
            relatedTerms           = template.getRelatedTerms();
            usedInContexts         = template.getUsedInContexts();
            contextRelevantTerms              = template.getContextRelevantTerms();
            semanticDefinitions = template.getSemanticDefinitions();
            semanticallyAssociatedDefinitions = template.getSemanticallyAssociatedDefinitions();
            meaningForDataElements  = template.getMeaningForDataElements();
            meanings                = template.getMeanings();
            supplementaryProperties = template.getSupplementaryProperties();
            supplementsElement = template.getSupplementsElement();

            /*
             * Area 4
             */

            governedBy                = template.getGovernedBy();
            governedElements          = template.getGovernedElements();


            peerGovernanceDefinitions      = template.getPeerGovernanceDefinitions();
            supportedGovernanceDefinitions = template.getSupportedGovernanceDefinitions();
            supportingGovernanceDefinitions = template.getSupportingGovernanceDefinitions();

            metrics          = template.getMetrics();
            measuredDefinitions          = template.getMeasuredDefinitions();

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
            schemaAttributes     = template.getSchemaAttributes();
            parentSchemaElements       = template.getParentSchemaElements();

            mapFromElement     = template.getMapFromElement();
            mapToElement       = template.getMapToElement();
            externalSchemaType = template.getExternalSchemaType();
            schemaOptions      = template.getSchemaOptions();
            queries            = template.getQueries();

            linkedToPrimaryKey = template.getLinkedToPrimaryKey();
            foreignKeys = template.getForeignKeys();

            vertices = template.getVertices();
            edges = template.getEdges();

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

            digitalSubscribers = template.getDigitalSubscribers();
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

    public List<RelatedMetadataElementSummary> getCategories()
    {
        return categories;
    }

    public void setCategories(List<RelatedMetadataElementSummary> categories)
    {
        this.categories = categories;
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



    /**
     * Return the governance metrics that measure this governance definition.
     *
     * @return list of governance definition stubs
     */
    public List<RelatedMetadataElementSummary> getMetrics()
    {
        return metrics;
    }


    /**
     * Set up the governance metrics that measure this governance definition.
     *
     * @param metrics list of governance definition stubs
     */
    public void setMetrics(List<RelatedMetadataElementSummary> metrics)
    {
        this.metrics = metrics;
    }


    public List<RelatedMetadataElementSummary> getMeasuredDefinitions()
    {
        return measuredDefinitions;
    }

    public void setMeasuredDefinitions(List<RelatedMetadataElementSummary> measuredDefinitions)
    {
        this.measuredDefinitions = measuredDefinitions;
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
