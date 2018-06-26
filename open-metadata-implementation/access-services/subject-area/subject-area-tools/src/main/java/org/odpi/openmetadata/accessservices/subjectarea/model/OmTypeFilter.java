/**
 * Licensed to the Apache Software Foundation (ASF) under oneØ
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.model;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.ArrayList;
import java.util.*;
import java.util.HashMap;

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
            "APIEndpoint", "BusinessCapabilityResponsibility", "ExternallySourcedGlossary", "LibraryCategoryReference", "ProfileIdentity", "ServerDeployment",
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
            "EmbeddedConnection", "HostOperatingPlatform", "Peer", "", "SchemaLinkToType", "UsedInContext", "AttachedRating", "ExternalIdLink",
            "ISARelationship", "PreferredTerm", "SchemaQueryImplementation", "ValidValue", "AttachedTag", "ExternalIdScope", "IsTaxonomy",
            "ProcessInput", "SchemaTypeImplementation", "ZoneGovernance", "AttributeForSchema", "ExternalReferenceLink", "Leadership",
            "ProcessOutput", "SemanticAssignment", "ZoneMembership"
    };

    public static final Set<String> RELATIONSHIP_TYPES_SET = new HashSet<>(Arrays.asList(RELATIONSHIP_TYPES));
    public static final Set<String> ENTITY_TYPES_SET = new HashSet<>(Arrays.asList(ENTITY_TYPES));
    /**
     *
     * spin through all the types and populate the types that are relaevant to this omas.
     * <p>
     * The types relevant to this omas are
     * <p>
     * group 1: Glossary, GlossaryTerm, GlossaryCategory
     * group 2: Any relationships that group 1 entities have
     * group 3:  Any types that the group 2 relationships connect to that are not in group 1.
     * group 4: Of the group 3 entity types we have, all the contained relationships and entities
     *
     * TODO  we may decide we want to restrict the enums and classifications for group 5 and 6
     * TODO group 5: any enumtypes that are referred to in any of the above types.
     * TODO group 6: all classifications relevant to the above types
     *
     * @param newTypeDefs
     * @param attributeTypeDefs
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

//        group1();
//        group2();
//        group3();
//        group4();
//        group5();
//        group6();


// Uncomment the below to remove filtering.
//        this.entityDefsMap = this.allEntityTypedefs;
//        this.relationshipTypeDefsMap = this.allRelationshipTypedefs;

    }

    /**
     * Add the basic glossary entity defs
     */
    private void group1() {
        String typeName = "Glossary";
        this.entityDefsMap.put(typeName,this.getEntityDef(typeName));
        typeName = "GlossaryTerm";
        this.entityDefsMap.put(typeName,this.getEntityDef(typeName));
        typeName = "GlossaryCategory";
        this.entityDefsMap.put(typeName,this.getEntityDef(typeName));
    }


    /**
     * Add any relationships that group 1 entityDefsMap have
     */
    private void group2() {
        Set<String> interestingEntities = new HashSet();
        //add the supertyper
        interestingEntities.add("Referenceable");
        for (String entityDefName:this.entityDefsMap.keySet()) {
            interestingEntities.add(entityDefName);
        }
        for (String relationshipDefName:this.allRelationshipTypedefs.keySet()) {
            RelationshipDef relationshipDef = this.allRelationshipTypedefs.get(relationshipDefName);
            // check to see if this is a relevant relationshipdef
            for (String interestingEntity:interestingEntities) {
                String endTypeName = relationshipDef.getEndDef1().getEntityType().getName();
                // process end 1
                if (endTypeName.equals(interestingEntity)) {
                    if (!this.relationshipTypeDefsMap.containsKey(relationshipDefName)) {
                        this.relationshipTypeDefsMap.put(relationshipDefName,relationshipDef);
                    }
                }
                // process end 2
                endTypeName = relationshipDef.getEndDef2().getEntityType().getName();
                if (endTypeName.equals(interestingEntity)) {
                    if (!this.relationshipTypeDefsMap.containsKey(relationshipDefName)) {
                        this.relationshipTypeDefsMap.put(relationshipDefName,relationshipDef);

                    }
                }
            }
        }

    }

    /**
     * Any entity types that the group 2 relationships connect to that are not in group 1.
     */
    private void group3() {

        Set<String> group1Entities = this.allEntityTypedefs.keySet();
        for (String relationshipDefName:this.allRelationshipTypedefs.keySet()) {
            RelationshipDef relationshipDef = this.allRelationshipTypedefs.get(relationshipDefName);
            // check to see if this is a relevant relationshipdef

            String endTypeName = relationshipDef.getEndDef1().getEntityType().getName();
            // process end 1
            if (group1Entities.contains(endTypeName)) {
                if (!this.relationshipTypeDefsMap.containsKey(relationshipDefName)) {
                    this.relationshipTypeDefsMap.put(relationshipDefName, relationshipDef);
                }
                this.group3EntityDefSet.add(relationshipDef.getEndDef2().getEntityType().getName());
            }
            // process end 2
            endTypeName = relationshipDef.getEndDef2().getEntityType().getName();
            if (group1Entities.contains(endTypeName)) {
                if (!this.relationshipTypeDefsMap.containsKey(relationshipDefName)) {
                    this.relationshipTypeDefsMap.put(relationshipDefName,relationshipDef);
                }
                this.group3EntityDefSet.add(relationshipDef.getEndDef1().getEntityType().getName());
            }
        }
        for (String group3EntityName:this.group3EntityDefSet) {
            if (this.entityDefsMap.get(group3EntityName)==null) {
                EntityDef group3Entity = this.allEntityTypedefs.get(group3EntityName);
                this.entityDefsMap.put(group3EntityName, group3Entity);
            }
        }
    }

    /**
     *  Of the group 3 entity types we have, all the contained relationships and entities
     */
    private void group4() {
//        for (String group3EntityDefName:group3EntityDefSet ) {
//            // check all the relationships
//            for (String relationshipDefName:this.allRelationshipTypedefs.keySet()) {
//                RelationshipDef relationshipDef = this.allRelationshipTypedefs.get(relationshipDefName);
//
//
//
//                String end1TypeName = relationshipDef.getEndDef1().getEntityType().getName();
//                String end2TypeName = relationshipDef.getEndDef2().getEntityType().getName();
//
//                // is this relationship with a containment type as one of our group3 entityDefsMap
//
//                // TODO scope the types we are interested in now we cannot use containment.

//               if (relationshipDef.getRelationshipContainerEnd()!=null) {
//                    String containerEndName =  relationshipDef.getRelationshipContainerEnd().getName();
//                    String containedEndName = null;
//                    if (containerEndName.equals(end1TypeName)) {
//                        containedEndName = end2TypeName;
//                    } else {
//                        containedEndName = end1TypeName;
//                    }
//
//                    if (this.entityDefsMap.get(containedEndName)==null) {
//                        EntityDef containedEntityDef = this.allEntityTypedefs.get(containedEndName);
//                        this.entityDefsMap.put(containedEndName,containedEntityDef);
//                    }
//                    // add this relationship
//                   if (!this.relationshipTypeDefsMap.containsKey(relationshipDefName)) {
//                        this.relationshipTypeDefsMap.put(relationshipDefName,relationshipDef);
//                   }
//               }
//            }
//        }
    }

    /**
     * any enumtypes that are referred to in any of the above types.
     */
    private void group5() {
        //TODO restrict?
        this.enumDefsMap = this.allEnumTypedefs;
    }

    /**
     * any classifications that are referred to in any of the above types.
     */
    private void group6() {
        //TODO restrict?
        this.classificationDefsMap =this.allClassificationTypedefs;
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
