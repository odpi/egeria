/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.model;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.ArrayList;
import java.util.*;
import java.util.HashMap;

/**
 * This is a filter of the entity and relationship types that could be useful for the omas. This list of types is a subset of the archive types.
 */
public class OmTypeFilter {


    private List<TypeDef> allTypeDefs = new ArrayList();
    private List<AttributeTypeDef> allAttributeTypeDefs = new ArrayList();

    private Map<String, EnumDef> allEnumTypedefs = new HashMap<String, EnumDef>();
    private Map<String, EntityDef> allEntityTypedefs = new HashMap<String, EntityDef>();
    private Map<String, RelationshipDef> allRelationshipTypedefs = new HashMap<String, RelationshipDef>();
    private Map<String, ClassificationDef> allClassificationTypedefs = new HashMap<String, ClassificationDef>();
    private Set<String> group3EntityDefSet = new HashSet<String>();

    private Map<String, EntityDef> entityDefsMap = new HashMap<String, EntityDef>();
    private Map<String, ClassificationDef> classificationDefsMap = new HashMap<String, ClassificationDef>();
    private Map<String, EnumDef> enumDefsMap = new HashMap<String, EnumDef>();
    private Map<String, RelationshipDef> relationshipTypeDefsMap = new HashMap<String, RelationshipDef>();

    private static String[] ENTITY_TYPES = {"APIOperation", "Community", "ExternalGlossaryLink", "GovernanceMetric", "ImplementationSnippet",
            "MetadataCollection", "ProjectCharter", "SoftwareServer","APISchemaType", "ComplexSchemaType", "ExternalId", "GovernancePolicy",
            "InformalTag", "MetadataRepositoryCohort", "PropertyFacet", "SoftwareServerCapability","ActorProfile", "Connection", "ExternalReference",
            "GovernanceProcess", "LicenseType", "Network", "Rating", "SubscriberList","Application", "ConnectorType", "FileFolder", "GovernanceResponsibility",
            "Like", "NetworkGateway", "Referenceable", "Team","Asset", "ContactDetails", "Glossary", "GovernanceRule", "Location", "NoteEntry", "Regulation",
            "ToDo","BusinessCapability", "DataFile", "GlossaryCategory", "GovernanceZone", "MapSchemaType", "NoteLog", "RelatedMedia", "Topic",
            "CertificationType", "DataSet", "GlossaryTerm", "GraphEdge", "MediaCollection", "OperatingPlatform", "SchemaAttribute", "UserIdentity",
            "CohortMember", "DeployedAPI", "GovernanceControl", "GraphVertex", "MediaFile", "Person", "SchemaLinkElement", "VirtualConnection","Collection",
            "DerivedSchemaAttribute", "GovernanceDefinition", "Host", "MediaUsage", "Process", "SchemaType", "VirtualContainer","Comment", "Endpoint",
            "GovernanceDriver", "HostCluster", "Meeting", "Project", "SoftwareComponent","DeployedSoftwareComponent"};
    private static String[] RELATIONSHIP_TYPES = {
            "APIEndpoint", "BusinessCapabilityResponsibility", "ExternallySourcedGlossary", "LibraryCategoryReference", "LibraryTermReference","ProfileIdentity", "ServerDeployment",
            "APIHeader", "CategoryAnchor", "FolderHierarchy", "License", "", "ProjectCharterLink", "ServerEndpoint", "APIOperations", "CategoryHierarchyLink",
            "GovernanceControlLink", "LinkedFile", "ProjectDependency", "ServerSupportedCapability", "APIRequest", "Certification", "GovernanceDefinitionMetric",
            "LinkedMedia", "ProjectHierarchy", "SoftwareComponentDeployment", "APIResponse", "CohortMemberMetadataCollection", "GovernanceImplementation",
            "LinkedType", "ProjectMeeting", "StaffAssignment", "AcceptedAnswer", "CollectionMember", "GovernancePolicyLink", "MapFromElementType",
            "ProjectResources", "Synonym", "ActorCollection", "CommunityMembership", "GovernanceProcessImplementation", "MapToElementType",
            "ProjectScope", "TermAnchor", "AdjacentLocation", "CommunityResources", "GovernanceResponse", "MediaReference", "ProjectTeam",
            "TermCategorization", "Antonym", "", "ConnectionConnectorType", "GovernanceResults", "MediaUsageGuidance", "ReferenceableFacet",
            "TermHASARelationship", "AssetLocation", "ConnectionEndpoint", "GovernanceRuleImplementation", "MeetingOnReferenceable",
            "RegulationCertificationType", "TermISATypeOFRelationship", "AssetSchemaType", "ConnectionToAsset", "GraphEdgeLink",
            "MetadataCohortPeer", "RelatedTerm", "TermTYPEDBYRelationship", "AssetServerUse", "ContactThrough", "GroupedMedia",
            "NestedFile", "ReplacementTerm", "ToDoOnReferenceable", "AttachedComment", "Contributor", "HostClusterMember",
            "NestedLocation", "ResponsibilityStaffContact", "ToDoSource", "AttachedLike", "DataContentForDataSet",
            "HostLocation", "NetworkGatewayLink", "RuntimeForProcess", "TopicSubscribers", "AttachedNoteLog",
            "DeployedVirtualContainer", "HostNetwork", "OrganizationalCapability", "SchemaAttributeType", "Translation", "AttachedNoteLogEntry",
            "EmbeddedConnection", "HostOperatingPlatform", "Peer", "SchemaLinkToType", "UsedInContext", "AttachedRating", "ExternalIdLink",
            "ISARelationship", "PreferredTerm", "SchemaQueryImplementation", "ValidValue", "AttachedTag", "ExternalIdScope", "IsTaxonomy",
            "ProcessInput", "SchemaTypeImplementation", "ZoneGovernance", "AttributeForSchema", "ExternalReferenceLink", "Leadership",
            "ProcessOutput", "SemanticAssignment", "ZoneMembership"
    };

    public static final Set<String> RELATIONSHIP_TYPES_SET = new HashSet<>(Arrays.asList(RELATIONSHIP_TYPES));
    public static final Set<String> ENTITY_TYPES_SET = new HashSet<>(Arrays.asList(ENTITY_TYPES));
    /**
     * Constructor
     * spin through all the types and store the types that are relevant to this omas.
     * <p>
     * The types relevant to this omas are define in the arrays above ENTITY_TYPES and RELATIONSHIP_TYPES. I suspect the scope of this is
     * too big - for example we have Host, because it has a relationship to Location and we have Location because we are generating an asset. In each of thee cases
     * we would not be able to generateClientSideRelationshipImpl the entity reference files if the types were not included in the generation
     *
     * @param newTypeDefs all the Type defs
     * @param attributeTypeDefs all the attribute defs
     */
    public OmTypeFilter(List<TypeDef> newTypeDefs, List<AttributeTypeDef> attributeTypeDefs) {
        allTypeDefs = newTypeDefs;
        allAttributeTypeDefs = attributeTypeDefs;


        // break down these types so we can consume them.
        for (TypeDef omTypeDef : this.allTypeDefs) {
            String typeName = omTypeDef.getName();
            switch (omTypeDef.getCategory()) {
                case ENTITY_DEF:
                    this.allEntityTypedefs.put(typeName, ((EntityDef) omTypeDef));
                    if (ENTITY_TYPES_SET.contains(typeName)) {
                        this.entityDefsMap.put(typeName, ((EntityDef) omTypeDef));
                    }
                    break;
                case CLASSIFICATION_DEF:
                    this.allClassificationTypedefs.put(omTypeDef.getName(), ((ClassificationDef) omTypeDef));
                    break;
                case RELATIONSHIP_DEF:
                    this.allRelationshipTypedefs.put(omTypeDef.getName(), (RelationshipDef) omTypeDef);
                    if (RELATIONSHIP_TYPES_SET.contains(typeName)) {
                        this.relationshipTypeDefsMap.put(typeName, ((RelationshipDef) omTypeDef));
                    }
                    break;
            }
        }
        for (AttributeTypeDef omAttrTypeDef : this.allAttributeTypeDefs) {
            switch (omAttrTypeDef.getCategory()) {
                case ENUM_DEF:
                    allEnumTypedefs.put(omAttrTypeDef.getName(), ((EnumDef) omAttrTypeDef));
                    break;
// We are not interested in primitives or collections  do we need to capture the primitives or the collections (maps and arrays) here ?
                case PRIMITIVE:
//                   do nothing
                    break;
                case COLLECTION:
//                   do nothing
                    break;
            }

        }
        this.enumDefsMap = this.allEnumTypedefs;
        this.classificationDefsMap = this.allClassificationTypedefs;

    }

    // helper methods
    private EntityDef getEntityDef(String typeName) {
        return this.allEntityTypedefs.get(typeName);
    }
    private ClassificationDef getClassificationDef(String typeName) {
        return this.allClassificationTypedefs.get(typeName);
    }
    private EnumDef getEnumDef(String typeName) {
        return this.allEnumTypedefs.get(typeName);
    }
    private RelationshipDef getRelationshipDef(String typeName) {
        return this.allRelationshipTypedefs.get(typeName);
    }


    public Map<String,EntityDef> getEntityDefsMap() {
        return entityDefsMap;
    }

    public Map<String,ClassificationDef> getClassificationDefsMap() {
        return classificationDefsMap;
    }

    public Map<String, EnumDef> getEnumDefsMap() {
        return enumDefsMap;
    }

    public Map<String, RelationshipDef> getRelationshipTypeDefsMap() {
        return relationshipTypeDefsMap;
    }

    public String getTypeDescription (String typeName) {
        String description = null;
        if (this.entityDefsMap.get(typeName) !=null) {
            description = this.entityDefsMap.get(typeName).getDescription();
        } else if (this.classificationDefsMap.get(typeName) !=null) {
            description = this.classificationDefsMap.get(typeName).getDescription();
        } else  if (this.relationshipTypeDefsMap.get(typeName) !=null) {
            description = this.relationshipTypeDefsMap.get(typeName).getDescription();
        } else if (this.enumDefsMap.get(typeName) !=null) {
            description = this.enumDefsMap.get(typeName).getDescription();
        }
        if (description==null) {
            description="";
        }
        return description;
    }
}
