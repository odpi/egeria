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

import org.odpi.openmetadata.accessservices.subjectarea.utils.GeneratorUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.archivemanager.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.io.IOException;
import java.util.*;

/**
 * The Omas model is a representation of entities attributes and references in a form that can be easily used by the omasgenerator
 */
public class SubjectAreaOmasModel {
    private OmTypeFilter typeFilter=null;
    // folder with other model files
    String[] jsonFolders;

    private static final ObjectMapper mapper = new ObjectMapper();

    //references
    private List<OmasReference> omasReferences = new ArrayList();
//    private Map<String, List<OmasReference>> omasReferencesByModel = new HashMap<String, List<OmasReference>>();

    private Map<String, List<OmasAttribute>> omasReferencesAsAttributesByEntity = new HashMap<String, List<OmasAttribute>>();
    private Set<String> typesWithRelationships = new HashSet();
    // map entity name to its attributes
    private Map<String, List<OmasAttribute>> omasEntityAttributesMap = new HashMap<String, List<OmasAttribute>>();
    // map entity name to its description
    private Map<String, String> omasTypeDefDescriptionMap = new HashMap<String, String>();
    // map enum to enumvalues
    private Map<String, List<OmasEnumValue>> omasEnumToValuesMap = new HashMap<String, List<OmasEnumValue>>();

    // map classification name to its attributes
    private Map<String, List<OmasAttribute>> omasClassificationAttributesMap = new HashMap<String, List<OmasAttribute>>();
    // map classification name to its description
    private Map<String, String> omasClassificationDescriptionMap = new HashMap<String, String>();

    // map relationship name to its attributes
    private Map<String, List<OmasAttribute>> omasRelationshipAttributesMap = new HashMap<String, List<OmasAttribute>>();
    // map relationship name to its omas object
    private Map<String, OmasRelationship> nametoRelationshipMap = new HashMap<String, OmasRelationship>();
    // archive
    OpenMetadataArchive omArchive = null;
    // store all types
    List<TypeDef> allOmTypedefs = null;
    List<AttributeTypeDef> allOmAttributeTypeDefs = null;
    // types subject area omas is interested in.
    List<TypeDef> omTypedefs = null;
    List<AttributeTypeDef> omAttributeTypeDefs = null;

    Map<String,EntityDef> omEntityTypedefs = new HashMap<String,EntityDef>();
    Map<String,RelationshipDef> omRelationshipTypedefs = new HashMap<String,RelationshipDef>();
    Map<String,ClassificationDef> omClassificationTypedefs = new HashMap<String,ClassificationDef>();
    Map<String,EnumDef> omEnumTypedefs =new HashMap<String,EnumDef>();

    public SubjectAreaOmasModel() throws IOException {
        OpenMetadataTypesArchive typesArchive = new OpenMetadataTypesArchive();
        this.omArchive = typesArchive.getOpenMetadataArchive();
        this.typeFilter = new OmTypeFilter(omArchive.getArchiveTypeStore().getNewTypeDefs(),
        omArchive.getArchiveTypeStore().getAttributeTypeDefs());

        this.omEntityTypedefs = typeFilter.getEntityDefsMap();
        this.omClassificationTypedefs = typeFilter.getClassificationDefsMap();
        this.omEnumTypedefs = typeFilter.getEnumDefsMap();
        this.omRelationshipTypedefs = typeFilter.getRelationshipTypeDefsMap();



        //TODO account for patches when we have some

        createOmasEnums();
        createOmasReferences();
        createOmasEntityAttributes();
        createOmasClassifications();
        createOmasRelationships();
    }

    public String getTypeDefDescription(String typeName) {
        return this.typeFilter.getTypeDescription(typeName);
    }

    private void createOmasEnums() {

        for (String enumName : this.omEnumTypedefs.keySet()) {
            EnumDef enumDef = this.omEnumTypedefs.get(enumName);

            List<EnumElementDef> enumElements = enumDef.getElementDefs();

            List<OmasEnumValue> enumValues = new ArrayList<OmasEnumValue>();
            for (EnumElementDef enumElement : enumElements) {
                OmasEnumValue omasEnumValue = new OmasEnumValue();
                omasEnumValue.name = this.normalizeTypeName(enumElement.getValue());
                omasEnumValue.description = enumElement.getDescription();
                enumValues.add(omasEnumValue);
            }
            omasEnumToValuesMap.put(enumName, enumValues);
        }
    }

    private void createOmasEntityAttributes() {

        for (String entityName : this.omEntityTypedefs.keySet()) {
            EntityDef entityDef = this.omEntityTypedefs.get(entityName);
            List<TypeDefAttribute> attributeDefs = entityDef.getPropertiesDefinition();
            if (attributeDefs==null) {
                attributeDefs=new ArrayList<TypeDefAttribute>();
            }
            Set<String> attrNamesSet = new HashSet();
            for (TypeDefAttribute attr:attributeDefs) {
                attrNamesSet.add((attr.getAttributeName()));
            }

            if (entityName.equals("GlossaryTerm") || entityName.equals("GlossaryCategory")) {
                // add a glossaryName typeDefAttribute
                TypeDefAttribute     typeDefAttribute = new TypeDefAttribute();
                typeDefAttribute.setAttributeName("glossaryName");
                typeDefAttribute.setAttributeDescription("The name of the glossary associated with this term. This must be present.");
                PrimitiveDef primitiveDef = new PrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitiveDef.setGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
                primitiveDef.setName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                primitiveDef.setVersion(0);
                primitiveDef.setVersionName("0");
                typeDefAttribute.setAttributeType(primitiveDef);
                typeDefAttribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
                typeDefAttribute.setValuesMinCount(0);
                typeDefAttribute.setValuesMaxCount(1);
                typeDefAttribute.setIndexable(true);
                typeDefAttribute.setUnique(false);
                typeDefAttribute.setDefaultValue(null);
                typeDefAttribute.setExternalStandardMappings(null);

                attrNamesSet.add(("glossaryName"));
                attributeDefs.add(typeDefAttribute);
            }

            // add in any new parent entity attributes
            while (entityDef.getSuperType()!=null) {
                entityDef = (EntityDef)entityDef.getSuperType();
                if (entityDef.getPropertiesDefinition()!= null){
                    for (TypeDefAttribute attr : entityDef.getPropertiesDefinition()) {
                        final String attributeName = attr.getAttributeName();
                        if (!attrNamesSet.contains(attributeName)) {
                            attributeDefs.add(attr);
                            attrNamesSet.add(attributeName);
                        }
                    }
                }
            }
            List<OmasAttribute> omasattributeList = createOmasAttributeList(attributeDefs);
            omasEntityAttributesMap.put(entityName, omasattributeList);
        }
    }

    private void createOmasClassifications() {

        for (String classificationDefName : this.omClassificationTypedefs.keySet()) {
            classificationDefName = this.normalizeTypeName(classificationDefName);
            ClassificationDef classificationDef =this.omClassificationTypedefs.get(classificationDefName);
            List<TypeDefAttribute> attributeDefs = classificationDef.getPropertiesDefinition();
            List<OmasAttribute> omasattributeList = createOmasAttributeList(attributeDefs);
            omasClassificationAttributesMap.put(classificationDefName, omasattributeList);
            omasClassificationDescriptionMap.put(classificationDefName, classificationDef.getDescription());
        }
    }

    private void createOmasRelationships() {
        for (String relationshipDefName : this.omRelationshipTypedefs.keySet()) {
            RelationshipDef relationshipDef = this.omRelationshipTypedefs.get(relationshipDefName);
            List<TypeDefAttribute> attributeDefs = relationshipDef.getPropertiesDefinition();
            List<OmasAttribute> omasattributeList = createOmasAttributeList(attributeDefs);
            omasRelationshipAttributesMap.put(relationshipDefName, omasattributeList);
            OmasRelationship omasRelationship = new OmasRelationship();
            omasRelationship.description = relationshipDef.getDescription();

            omasRelationship.entityProxy1Name = relationshipDef.getEndDef1().getAttributeName();
            omasRelationship.entityProxy1Type = relationshipDef.getEndDef1().getEntityType().getName();
            omasRelationship.entityProxy2Name = relationshipDef.getEndDef2().getAttributeName();
            omasRelationship.entityProxy2Type = relationshipDef.getEndDef2().getEntityType().getName();
            omasRelationship.label = relationshipDefName;
            nametoRelationshipMap.put(relationshipDefName, omasRelationship);
        }
    }

    public Map<String, List<OmasEnumValue>> getEnumsMap() {
        return this.omasEnumToValuesMap;
    }

    private List<OmasAttribute> createOmasAttributeList(List<TypeDefAttribute> attributeDefs) {
        List<OmasAttribute> omasattributeList = new ArrayList();
        if (attributeDefs !=null ) {
            for (TypeDefAttribute attributeDef : attributeDefs) {
                // deal with primitives , collections and enum attributes

                String normalizedTypeName = normalizeTypeName(attributeDef.getAttributeType().getName());
                OmasAttribute omasAttribute = new OmasAttribute();
                // assume the archive does not have any unknown cardinalities
                if (attributeDef.getAttributeCardinality() == AttributeCardinality.AT_MOST_ONE || attributeDef.getAttributeCardinality() == AttributeCardinality.ONE_ONLY) {
                    omasAttribute.isList = false;
                } else {
                    omasAttribute.isList = true;
                }
                // count arrays as lists. Do not need to support AttributeCardinality more than 1 and an array with current model
                if (attributeDef.getAttributeType().getCategory().equals(AttributeTypeDefCategory.COLLECTION) &&
                        attributeDef.getAttributeType().getName().startsWith("array<")) {
                    omasAttribute.isList = true;
                }


                omasAttribute.name = attributeDef.getAttributeName();
                omasAttribute.description =attributeDef.getAttributeDescription();
                omasAttribute.type = normalizedTypeName;
                omasAttribute.isMap = false;
                omasAttribute.isEnum = false;
                omasattributeList.add(omasAttribute);

                // need to store whether this is an enumeration or not.
                if (attributeDef.getAttributeType().getCategory() == AttributeTypeDefCategory.ENUM_DEF) {
                    // need to store whether this is an enumeration
                    omasAttribute.isEnum = true;
                }


                if (attributeDef.getAttributeType().getCategory() == AttributeTypeDefCategory.COLLECTION && attributeDef.getAttributeType().getName().startsWith("map<")) {
                    omasAttribute.isMap = true;
                }
            }
        }
        return omasattributeList;
    }

    private void createOmasReferences() {
        int i=0;
        for (String relationshipDefName : this.omRelationshipTypedefs.keySet()) {
            i++;
            RelationshipDef relationshipDef =  this.omRelationshipTypedefs.get(relationshipDefName);
            String description = relationshipDef.getDescription();
            String relationshipGuid = relationshipDef.getGUID();

            System.err.println("DEBUG0 :"+i+": relationship name =" +relationshipDefName);

            RelationshipEndDef end1 = relationshipDef.getEndDef1();
            RelationshipEndDef end2 = relationshipDef.getEndDef2();

            String end1Name = end1.getAttributeName();
            String end1Type = this.normalizeTypeName(end1.getEntityType().getName());

            String end2Name = end2.getAttributeName();
            String end2Type = this.normalizeTypeName(end2.getEntityType().getName());

            boolean end1IsSet = false;
            boolean end2IsSet = false;

           if  (end1.getAttributeCardinality() == RelationshipEndCardinality.ANY_NUMBER) {
               end1IsSet = true;

            }

            if  (end2.getAttributeCardinality() == RelationshipEndCardinality.ANY_NUMBER) {
                end2IsSet = true;

            }

            /*
             from all of the above information we need to create 1 or 2 references.
             We create one reference if both ends have the same name and type
             Otherwise we create 2 references.
             We create packages
             TermToTerm etc
              */
            List<TypeDefAttribute> attributeDefs = relationshipDef.getPropertiesDefinition();
            List<OmasAttribute> omasattributeList = createOmasAttributeList(attributeDefs);

            if (end1Name.equals(end2Name) && end1Type.equals(end2Type)) {
                OmasReference omasReference = new OmasReference();
                omasReference.attrList = omasattributeList;
                omasReference.uReferenceName = GeneratorUtilities.uppercase1stLetter(end1Name);
                omasReference.referenceName = GeneratorUtilities.lowercase1stLetter(end1Name);

                if (end1Name.toUpperCase().startsWith("SYNONYM")) {
                    int o=0;
                }
                omasReference.relatedEndType = end1Type;
                omasReference.relationshipType = relationshipDefName;
                omasReference.relationshipGuid = relationshipGuid;
                omasReference.myType = end1Type;
                omasReference.description = description;
                omasReference.isSet = end1IsSet;
                omasReferences.add(omasReference);

                typesWithRelationships.add(omasReference.myType);

                List<OmasAttribute> entityOmasReferencesAsAttributes = omasReferencesAsAttributesByEntity.get(omasReference.myType);
                if (entityOmasReferencesAsAttributes == null) {
                    entityOmasReferencesAsAttributes = new ArrayList();
                }
                OmasAttribute omasAttribute = new OmasAttribute();
                omasAttribute.name = omasReference.referenceName;
                omasAttribute.type = omasReference.relatedEndType;
                if ( omasAttribute.name.toUpperCase().startsWith("COMMENT")) {
                    int o=0;
                }
                omasAttribute.isSet = end1IsSet;
                omasAttribute.isReference = true;
                omasAttribute.referencePackage = GeneratorUtilities.getReferencePackage(omasReference.myType, omasReference.relatedEndType);
                omasAttribute.referenceRelationshipName =  omasReference.relationshipType;
                entityOmasReferencesAsAttributes.add(omasAttribute);
                omasReferencesAsAttributesByEntity.put(omasReference.myType, entityOmasReferencesAsAttributes);
                System.err.println("DEBUG1 ::  omasReferencesAsAttributesByEntity entity " + omasReference.myType + " adding attribute name," + omasAttribute.name);
            } else {
                // generate 2 References
                OmasReference omasReference1 = new OmasReference();
                omasReference1.attrList = omasattributeList;
                omasReference1.uReferenceName = GeneratorUtilities.uppercase1stLetter(end2Name);
                omasReference1.referenceName = GeneratorUtilities.lowercase1stLetter(end2Name);
                omasReference1.relatedEndType = end2Type;
                omasReference1.relationshipType = relationshipDefName;
                omasReference1.relationshipGuid = relationshipGuid;
                omasReference1.myType = end1Type;
                if (  omasReference1.referenceName.toUpperCase().startsWith("COMMENT")) {
                    int o=0;
                }
                omasReference1.isSet = end2IsSet;
                // and the other way round
                OmasReference omasReference2 = new OmasReference();
                omasReference2.attrList = omasattributeList;
                omasReference2.uReferenceName = GeneratorUtilities.uppercase1stLetter(end1Name);
                omasReference2.referenceName = GeneratorUtilities.lowercase1stLetter(end1Name);
                omasReference2.relationshipType = relationshipDefName;
                omasReference2.relationshipGuid = relationshipGuid;
                omasReference2.relatedEndType = end1Type;
                omasReference2.myType = end2Type;
                if ( omasReference2.referenceName.toUpperCase().startsWith("COMMENT")) {
                    int o=0;
                }
                omasReference2.isSet = end1IsSet;
                omasReferences.add(omasReference1);
                omasReferences.add(omasReference2);
                typesWithRelationships.add(omasReference1.myType);
                typesWithRelationships.add(omasReference2.myType);

                List<OmasAttribute> entityOmasReferences1 = omasReferencesAsAttributesByEntity.get(omasReference1.myType);
                if (entityOmasReferences1 == null) {
                    entityOmasReferences1 = new ArrayList();
                }
                OmasAttribute omasAttribute1 = new OmasAttribute();
                omasAttribute1.name = omasReference1.referenceName;
                omasAttribute1.type = omasReference1.relatedEndType;
                if (   omasAttribute1.name.toUpperCase().startsWith("COMMENT")) {
                    int o=0;
                }
                omasAttribute1.isSet = omasReference1.isSet;
                omasAttribute1.isReference = true;
                omasAttribute1.referencePackage = GeneratorUtilities.getReferencePackage(omasReference1.myType, omasReference1.relatedEndType);
                omasAttribute1.referenceRelationshipName =  omasReference1.relationshipType;
                entityOmasReferences1.add(omasAttribute1);
                omasReferencesAsAttributesByEntity.put(omasReference1.myType, entityOmasReferences1);
                System.err.println("DEBUG2 :"+i+": relationship name =" +relationshipDefName+ "  omasReferencesAsAttributesByEntity entity " + omasReference1.myType + " adding attribute name," + omasAttribute1.name);

                List<OmasAttribute> entityOmasReferences2 = omasReferencesAsAttributesByEntity.get(omasReference2.myType);
                if (entityOmasReferences2 == null) {
                    entityOmasReferences2 = new ArrayList();
                }
                OmasAttribute omasAttribute2 = new OmasAttribute();
                omasAttribute2.name = omasReference2.referenceName;
                omasAttribute2.type = omasReference2.relatedEndType;
                if (  omasAttribute2.name.toUpperCase().startsWith("COMMENT")) {
                    int o=0;
                }
                omasAttribute2.isSet= omasReference2.isSet;
                omasAttribute2.isReference = true;
                omasAttribute2.referencePackage = GeneratorUtilities.getReferencePackage(omasReference2.myType, omasReference2.relatedEndType);
                omasAttribute2.referenceRelationshipName =  omasReference1.relationshipType;
                entityOmasReferences2.add(omasAttribute2);
                omasReferencesAsAttributesByEntity.put(omasReference2.myType, entityOmasReferences2);
                System.err.println("DEBUG3 :"+i+": relationship name =" +relationshipDefName+ " omasReferencesAsAttributesByEntity entity " + omasReference2.myType + " adding attribute name," + omasAttribute2.name);
            }
        }
        // now go through all the entityDefs and spin up their supertypes. Add the parent supertypes references to it.

        for (String entityDefName : this.omEntityTypedefs.keySet()) {
            EntityDef entityDef = this.omEntityTypedefs.get(entityDefName);
            List<OmasAttribute> deduplicatedOMASAttributes = this.omasReferencesAsAttributesByEntity.get(entityDefName);
            if (org.odpi.openmetadata.accessservices.subjectarea.utils.GeneratorUtilities.lowercase1stLetter(entityDefName).equals("complexSchemaType")) {
                int ioo=0;
            }
            // get all the attribute names
            Set<String> attributeNames = new HashSet();
            if (deduplicatedOMASAttributes!=null) {
                for (OmasAttribute omasAttribute : deduplicatedOMASAttributes) {
                    attributeNames.add(omasAttribute.name);
                }
                if (attributeNames.size() != deduplicatedOMASAttributes.size()) {
                    // we we end up here then deduplicatedOMASAttributes is not dedupliucated
                    throw new RuntimeException("Unexpected mismatch of attributes during reference processing");
                }
            }

            // we could have null in deduplicatedAttributes if we do not have any attributes.
            while (entityDef.getSuperType()!= null) {
                entityDef = (EntityDef)entityDef.getSuperType();
                List<OmasAttribute> parentOmasAttributes  = this.omasReferencesAsAttributesByEntity.get(entityDef.getName());
                if (parentOmasAttributes!=null && parentOmasAttributes.size() >0) {
                    // parent has attributes
                    Set<OmasAttribute>  newAttributes = new HashSet<>();
                    // may not have found any attributes yet.
                    if (deduplicatedOMASAttributes == null ) {
                        deduplicatedOMASAttributes = new ArrayList();
                    }
                    // we have deduplicated attributes
                    for (OmasAttribute omasAttribute:parentOmasAttributes) {
                         // only add the parent attributes if we do not already have it.
                         if (!attributeNames.contains(omasAttribute.name)) {
                             attributeNames.add(omasAttribute.name);
                             newAttributes.add(omasAttribute);
                         }
                    }
                    // store the new attributes we find in a separate variable to avoid a Concurrent access exception
                    if (!newAttributes.isEmpty() ) {
                        for (OmasAttribute newOmasAttribute :newAttributes) {
                            deduplicatedOMASAttributes.add(newOmasAttribute);
                        }
                    }
                    if (attributeNames.size() != deduplicatedOMASAttributes.size()) {
                        // we we end up here then deduplicatedOMASAttributes is not dedupliucated
                        throw new RuntimeException("Unexpected mismatch2 of attributes during reference processing");
                    }
                }
            }
            this.omasReferencesAsAttributesByEntity.put(entityDefName,deduplicatedOMASAttributes);
        }
    }

    public List<OmasReference> getOmasReferences() {
        return this.omasReferences;
    }

    public OmasRelationship getOmasRelationshipByName(String name) {
        return this.nametoRelationshipMap.get(name);
    }

    public Map<String, OmasRelationship> getOMASRelationshipMap() {
        return this.nametoRelationshipMap;
    }

    public Map<String, List<OmasAttribute>> getOmasRelationshipAttributeMap() {
        return this.omasRelationshipAttributesMap;
    }

    public Map<String, List<OmasAttribute>> getOmasEntityAttributeMap() {
        return this.omasEntityAttributesMap;
    }

    public Map<String, List<OmasAttribute>> getOmasClassificationAttributesMap() {
        return this.omasClassificationAttributesMap;
    }

    public Map<String, String> getOmasClassificationDescriptionMap() {
        return this.omasClassificationDescriptionMap;
    }

    public Map<String, List<OmasAttribute>> getOmasReferencesAsAttributesByEntity() {
        return this.omasReferencesAsAttributesByEntity;
    }

    /**
     * Open metadata types maps in the same way as java - but with lower case.
     * At this time there are only maps with string string. This method coverts the typeName to be
     * valid java.
     * It also converts arrays into lists.
     *
     * @param typeName
     * @return
     */
    private String normalizeTypeName(String typeName) {
        String normalizedTypeName = typeName;
        // assume that all the maps are string string
        if (typeName.startsWith("map<string")) {
            normalizedTypeName = "Map<String,String>";
        }
        if (typeName.startsWith("array<")) {
            normalizedTypeName =   normalizedTypeName.replaceAll(" ", "");
            int endIndex = normalizedTypeName.length() -1;
            String element = normalizedTypeName.substring(6,endIndex);
            element =  GeneratorUtilities.uppercase1stLetter(element);
            if (element.equals("Int")) {
                element = "Integer";
            }
            normalizedTypeName = "List<" + element + ">";
        }
        if (typeName.equals("int")) {
            normalizedTypeName = "Integer";
        }
        return normalizedTypeName;
    }
}
