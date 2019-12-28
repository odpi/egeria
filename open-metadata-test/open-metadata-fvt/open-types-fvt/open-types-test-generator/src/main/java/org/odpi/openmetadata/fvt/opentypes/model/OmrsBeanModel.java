/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.fvt.opentypes.model;

import org.odpi.openmetadata.fvt.opentypes.utils.GeneratorUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.io.IOException;
import java.util.*;

/**
 * The Omrs bean model is a representation of entities attributes and references in a form that can be easily used by the generator
 */
public class OmrsBeanModel {
    private OmTypeFilter typeFilter=null;
    // folder with other model files
    String[] jsonFolders;

    private static final ObjectMapper mapper = new ObjectMapper();

    //references
    private List<OmrsBeanReference> omrsBeanReferences = new ArrayList<>();

    private Map<String, List<OmrsBeanAttribute>> omrsBeanReferencesAsAttributesByEntity = new HashMap<String, List<OmrsBeanAttribute>>();
    private Set<String> typesWithRelationships = new HashSet<>();
    // map entity name to its attributes
    private Map<String, List<OmrsBeanAttribute>> omrsBeanEntityAttributesMap = new HashMap<String, List<OmrsBeanAttribute>>();
    // map entity name to its description
    private Map<String, String> omrsBeanTypeDefDescriptionMap = new HashMap<String, String>();
    // map enum to enumvalues
    private Map<String, List<OmrsBeanEnumValue>> omrsBeanEnumToValuesMap = new HashMap<String, List<OmrsBeanEnumValue>>();

    // map classification name to its attributes
    private Map<String, List<OmrsBeanAttribute>> omrsBeanClassificationAttributesMap = new HashMap<String, List<OmrsBeanAttribute>>();
    // map classification name to its description
    private Map<String, String> omrsBeanClassificationDescriptionMap = new HashMap<String, String>();

    // map relationship name to its attributes
    private Map<String, List<OmrsBeanAttribute>> omrsBeanRelationshipAttributesMap = new HashMap<String, List<OmrsBeanAttribute>>();
    // map relationship name to its omrsBean object
    private Map<String, OmrsBeanRelationship> nametoRelationshipMap = new HashMap<String, OmrsBeanRelationship>();
    // archive
    OpenMetadataArchive omArchive = null;
    // store all types
    List<TypeDef> allOmTypedefs = null;
    List<AttributeTypeDef> allOmAttributeTypeDefs = null;
    // types subject area omrsBean is interested in.
    List<TypeDef> omTypedefs = null;
    List<AttributeTypeDef> omAttributeTypeDefs = null;

    Map<String,EntityDef> omEntityTypedefs = new HashMap<String,EntityDef>();
    Map<String,RelationshipDef> omRelationshipTypedefs = new HashMap<String,RelationshipDef>();
    Map<String,ClassificationDef> omClassificationTypedefs = new HashMap<String,ClassificationDef>();
    Map<String,EnumDef> omEnumTypedefs =new HashMap<String,EnumDef>();

    public OmrsBeanModel() throws IOException {
        OpenMetadataTypesArchive typesArchive = new OpenMetadataTypesArchive();
        this.omArchive = typesArchive.getOpenMetadataArchive();
        this.typeFilter = new OmTypeFilter(omArchive.getArchiveTypeStore().getNewTypeDefs(),
                omArchive.getArchiveTypeStore().getAttributeTypeDefs());

        this.omEntityTypedefs = typeFilter.getEntityDefsMap();
        this.omClassificationTypedefs = typeFilter.getClassificationDefsMap();
        this.omEnumTypedefs = typeFilter.getEnumDefsMap();
        this.omRelationshipTypedefs = typeFilter.getRelationshipTypeDefsMap();
        //TODO account for patches when we have some

        createOmrsBeanEnums();
        createOmrsBeanReferences();
        createOmrsBeanEntityAttributes();
        createOmrsBeanClassifications();
        createOmrsBeanRelationships();
    }

    public String getTypeDefDescription(String typeName) {
        return this.typeFilter.getTypeDescription(typeName);
    }

    private void createOmrsBeanEnums() {

        for (String enumName : this.omEnumTypedefs.keySet()) {
            EnumDef enumDef = this.omEnumTypedefs.get(enumName);

            List<EnumElementDef> enumElements = enumDef.getElementDefs();

            List<OmrsBeanEnumValue> enumValues = new ArrayList<OmrsBeanEnumValue>();
            for (EnumElementDef enumElement : enumElements) {
                OmrsBeanEnumValue omrsBeanEnumValue = new OmrsBeanEnumValue();
                omrsBeanEnumValue.name = this.normalizeTypeName(enumElement.getValue());
                omrsBeanEnumValue.description = enumElement.getDescription();
                omrsBeanEnumValue.ordinal = enumElement.getOrdinal();
                enumValues.add(omrsBeanEnumValue);
            }
            omrsBeanEnumToValuesMap.put(enumName, enumValues);
        }
    }

    private void createOmrsBeanEntityAttributes() {

        for (String entityName : this.omEntityTypedefs.keySet()) {
            EntityDef entityDef = this.omEntityTypedefs.get(entityName);
            List<TypeDefAttribute> attributeDefs = entityDef.getPropertiesDefinition();
            if (attributeDefs==null) {
                attributeDefs=new ArrayList<TypeDefAttribute>();
            }
            Set<String> attrNamesSet = new HashSet<>();
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
                TypeDefLink superType = entityDef.getSuperType();
                entityDef = this.typeFilter.getEntityDef(superType.getName());
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
            List<OmrsBeanAttribute> omrsBeanattributeList = createOmrsBeanAttributeList(attributeDefs);
            omrsBeanEntityAttributesMap.put(entityName, omrsBeanattributeList);
        }
    }

    private void createOmrsBeanClassifications() {

        for (String classificationDefName : this.omClassificationTypedefs.keySet()) {
            classificationDefName = this.normalizeTypeName(classificationDefName);
            ClassificationDef classificationDef =this.omClassificationTypedefs.get(classificationDefName);
            List<TypeDefAttribute> attributeDefs = classificationDef.getPropertiesDefinition();
            List<OmrsBeanAttribute> omrsBeanattributeList = createOmrsBeanAttributeList(attributeDefs);
            omrsBeanClassificationAttributesMap.put(classificationDefName, omrsBeanattributeList);
            omrsBeanClassificationDescriptionMap.put(classificationDefName, classificationDef.getDescription());
        }
    }

    private void createOmrsBeanRelationships() {
        for (String relationshipDefName : this.omRelationshipTypedefs.keySet()) {
            RelationshipDef relationshipDef = this.omRelationshipTypedefs.get(relationshipDefName);
            List<TypeDefAttribute> attributeDefs = relationshipDef.getPropertiesDefinition();
            List<OmrsBeanAttribute> omrsBeanattributeList = createOmrsBeanAttributeList(attributeDefs);
            omrsBeanRelationshipAttributesMap.put(relationshipDefName, omrsBeanattributeList);
            OmrsBeanRelationship omrsBeanRelationship = new OmrsBeanRelationship();
            omrsBeanRelationship.description = relationshipDef.getDescription();
            omrsBeanRelationship.entityProxy1Name = relationshipDef.getEndDef1().getAttributeName();
            omrsBeanRelationship.entityProxy1Type = relationshipDef.getEndDef1().getEntityType().getName();
            omrsBeanRelationship.entityProxy2Name = relationshipDef.getEndDef2().getAttributeName();
            omrsBeanRelationship.entityProxy2Type = relationshipDef.getEndDef2().getEntityType().getName();
            omrsBeanRelationship.typeDefGuid = relationshipDef.getGUID();
            omrsBeanRelationship.label = relationshipDefName;
            nametoRelationshipMap.put(relationshipDefName, omrsBeanRelationship);
        }
    }

    public Map<String, List<OmrsBeanEnumValue>> getEnumsMap() {
        return this.omrsBeanEnumToValuesMap;
    }

    private List<OmrsBeanAttribute> createOmrsBeanAttributeList(List<TypeDefAttribute> attributeDefs) {
        List<OmrsBeanAttribute> omrsBeanattributeList = new ArrayList<>();
        if (attributeDefs !=null ) {
            for (TypeDefAttribute attributeDef : attributeDefs) {
                // deal with primitives , collections and enum attributes

                String normalizedTypeName = normalizeTypeName(attributeDef.getAttributeType().getName());
                OmrsBeanAttribute omrsBeanAttribute = new OmrsBeanAttribute();
                // assume the archive does not have any unknown cardinalities
                if (attributeDef.getAttributeCardinality() == AttributeCardinality.AT_MOST_ONE || attributeDef.getAttributeCardinality() == AttributeCardinality.ONE_ONLY) {
                    omrsBeanAttribute.isList = false;
                } else {
                    omrsBeanAttribute.isList = true;
                }
                // count arrays as lists. Do not need to support AttributeCardinality more than 1 and an array with current model
                if (attributeDef.getAttributeType().getCategory().equals(AttributeTypeDefCategory.COLLECTION) &&
                        attributeDef.getAttributeType().getName().startsWith("array<")) {
                    omrsBeanAttribute.isList = true;
                }


                omrsBeanAttribute.name = attributeDef.getAttributeName();
                omrsBeanAttribute.description =attributeDef.getAttributeDescription();
                omrsBeanAttribute.type = normalizedTypeName;
                omrsBeanAttribute.isMap = false;
                omrsBeanAttribute.isEnum = false;
                omrsBeanattributeList.add(omrsBeanAttribute);

                // need to store whether this is an enumeration or not.
                if (attributeDef.getAttributeType().getCategory() == AttributeTypeDefCategory.ENUM_DEF) {
                    // need to store whether this is an enumeration
                    omrsBeanAttribute.isEnum = true;
                }


                if (attributeDef.getAttributeType().getCategory() == AttributeTypeDefCategory.COLLECTION && attributeDef.getAttributeType().getName().startsWith("map<")) {
                    omrsBeanAttribute.isMap = true;
                }
            }
        }
        return omrsBeanattributeList;
    }

    private void createOmrsBeanReferences() {
        int i=0;
        for (String relationshipDefName : this.omRelationshipTypedefs.keySet()) {
            i++;
            RelationshipDef relationshipDef =  this.omRelationshipTypedefs.get(relationshipDefName);
            String description = relationshipDef.getDescription();
            String relationshipGuid = relationshipDef.getGUID();
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
            List<OmrsBeanAttribute> omrsBeanattributeList = createOmrsBeanAttributeList(attributeDefs);

            if (end1Name.equals(end2Name) && end1Type.equals(end2Type)) {
                OmrsBeanReference omrsBeanReference = new OmrsBeanReference();
                omrsBeanReference.attrList = omrsBeanattributeList;
                omrsBeanReference.uReferenceName = GeneratorUtilities.uppercase1stLetter(end1Name);
                omrsBeanReference.referenceName = GeneratorUtilities.lowercase1stLetter(end1Name);

                omrsBeanReference.relatedEndType = end1Type;
                omrsBeanReference.relationshipType = relationshipDefName;
                omrsBeanReference.relationshipGuid = relationshipGuid;
                omrsBeanReference.myType = end1Type;
                omrsBeanReference.description = description;
                omrsBeanReference.isSet = end1IsSet;
                omrsBeanReferences.add(omrsBeanReference);

                typesWithRelationships.add(omrsBeanReference.myType);

                List<OmrsBeanAttribute> entityOmrsBeanReferencesAsAttributes = omrsBeanReferencesAsAttributesByEntity.get(omrsBeanReference.myType);
                if (entityOmrsBeanReferencesAsAttributes == null) {
                    entityOmrsBeanReferencesAsAttributes = new ArrayList<>();
                }
                OmrsBeanAttribute omrsBeanAttribute = new OmrsBeanAttribute();
                omrsBeanAttribute.name = omrsBeanReference.referenceName;
                omrsBeanAttribute.type = omrsBeanReference.relatedEndType;
                omrsBeanAttribute.isSet = end1IsSet;
                omrsBeanAttribute.isReference = true;
                omrsBeanAttribute.referencePackage = GeneratorUtilities.getReferencePackage(omrsBeanReference.myType, omrsBeanReference.relatedEndType);
                omrsBeanAttribute.referenceRelationshipName =  omrsBeanReference.relationshipType;
                entityOmrsBeanReferencesAsAttributes.add(omrsBeanAttribute);
                omrsBeanReferencesAsAttributesByEntity.put(omrsBeanReference.myType, entityOmrsBeanReferencesAsAttributes);
            } else {
                // generateClientSideRelationshipImpl 2 References
                OmrsBeanReference omrsBeanReference1 = new OmrsBeanReference();
                omrsBeanReference1.attrList = omrsBeanattributeList;
                omrsBeanReference1.uReferenceName = GeneratorUtilities.uppercase1stLetter(end2Name);
                omrsBeanReference1.referenceName = GeneratorUtilities.lowercase1stLetter(end2Name);
                omrsBeanReference1.relatedEndType = end2Type;
                omrsBeanReference1.relationshipType = relationshipDefName;
                omrsBeanReference1.relationshipGuid = relationshipGuid;
                omrsBeanReference1.myType = end1Type;
                omrsBeanReference1.isSet = end2IsSet;
                // and the other way round
                OmrsBeanReference omrsBeanReference2 = new OmrsBeanReference();
                omrsBeanReference2.attrList = omrsBeanattributeList;
                omrsBeanReference2.uReferenceName = GeneratorUtilities.uppercase1stLetter(end1Name);
                omrsBeanReference2.referenceName = GeneratorUtilities.lowercase1stLetter(end1Name);
                omrsBeanReference2.relationshipType = relationshipDefName;
                omrsBeanReference2.relationshipGuid = relationshipGuid;
                omrsBeanReference2.relatedEndType = end1Type;
                omrsBeanReference2.myType = end2Type;
                omrsBeanReference2.isSet = end1IsSet;
                omrsBeanReferences.add(omrsBeanReference1);
                omrsBeanReferences.add(omrsBeanReference2);
                typesWithRelationships.add(omrsBeanReference1.myType);
                typesWithRelationships.add(omrsBeanReference2.myType);

                List<OmrsBeanAttribute> entityOmrsBeanReferences1 = omrsBeanReferencesAsAttributesByEntity.get(omrsBeanReference1.myType);
                if (entityOmrsBeanReferences1 == null) {
                    entityOmrsBeanReferences1 = new ArrayList<>();
                }
                OmrsBeanAttribute omrsBeanAttribute1 = new OmrsBeanAttribute();
                omrsBeanAttribute1.name = omrsBeanReference1.referenceName;
                omrsBeanAttribute1.type = omrsBeanReference1.relatedEndType;
                omrsBeanAttribute1.isSet = omrsBeanReference1.isSet;
                omrsBeanAttribute1.isReference = true;
                omrsBeanAttribute1.referencePackage = GeneratorUtilities.getReferencePackage(omrsBeanReference1.myType, omrsBeanReference1.relatedEndType);
                omrsBeanAttribute1.referenceRelationshipName =  omrsBeanReference1.relationshipType;
                entityOmrsBeanReferences1.add(omrsBeanAttribute1);
                omrsBeanReferencesAsAttributesByEntity.put(omrsBeanReference1.myType, entityOmrsBeanReferences1);
                List<OmrsBeanAttribute> entityOmrsBeanReferences2 = omrsBeanReferencesAsAttributesByEntity.get(omrsBeanReference2.myType);
                if (entityOmrsBeanReferences2 == null) {
                    entityOmrsBeanReferences2 = new ArrayList<>();
                }
                OmrsBeanAttribute omrsBeanAttribute2 = new OmrsBeanAttribute();
                omrsBeanAttribute2.name = omrsBeanReference2.referenceName;
                omrsBeanAttribute2.type = omrsBeanReference2.relatedEndType;
                omrsBeanAttribute2.isSet= omrsBeanReference2.isSet;
                omrsBeanAttribute2.isReference = true;
                omrsBeanAttribute2.referencePackage = GeneratorUtilities.getReferencePackage(omrsBeanReference2.myType, omrsBeanReference2.relatedEndType);
                omrsBeanAttribute2.referenceRelationshipName =  omrsBeanReference1.relationshipType;
                entityOmrsBeanReferences2.add(omrsBeanAttribute2);
                omrsBeanReferencesAsAttributesByEntity.put(omrsBeanReference2.myType, entityOmrsBeanReferences2);
            }
        }
        // now go through all the entityDefs and spin up their supertypes. Add the parent supertypes references to it.

        for (String entityDefName : this.omEntityTypedefs.keySet()) {
            EntityDef entityDef = this.omEntityTypedefs.get(entityDefName);
            List<OmrsBeanAttribute> deduplicatedOmrsBeanAttributes = this.omrsBeanReferencesAsAttributesByEntity.get(entityDefName);
            // get all the attribute names
            Set<String> attributeNames = new HashSet<>();
            if (deduplicatedOmrsBeanAttributes !=null) {
                for (OmrsBeanAttribute omrsBeanAttribute : deduplicatedOmrsBeanAttributes) {
                    attributeNames.add(omrsBeanAttribute.name);
                }
                if (attributeNames.size() != deduplicatedOmrsBeanAttributes.size()) {
                    // we we end up here then deduplicatedOmrsBeanAttributes is not dedupliucated
                    throw new RuntimeException("Unexpected mismatch of attributes during reference processing");
                }
            }

            // we could have null in deduplicatedAttributes if we do not have any attributes.
            while (entityDef.getSuperType()!= null) {
                TypeDefLink  superType = entityDef.getSuperType();
                entityDef = this.typeFilter.getEntityDef(superType.getName());
                List<OmrsBeanAttribute> parentOmrsBeanAttributes = this.omrsBeanReferencesAsAttributesByEntity.get(entityDef.getName());
                if (parentOmrsBeanAttributes !=null && parentOmrsBeanAttributes.size() >0) {
                    // parent has attributes
                    Set<OmrsBeanAttribute>  newAttributes = new HashSet<>();
                    // may not have found any attributes yet.
                    if (deduplicatedOmrsBeanAttributes == null ) {
                        deduplicatedOmrsBeanAttributes = new ArrayList<>();
                    }
                    // we have deduplicated attributes
                    for (OmrsBeanAttribute omrsBeanAttribute : parentOmrsBeanAttributes) {
                        // only add the parent attributes if we do not already have it.
                        if (!attributeNames.contains(omrsBeanAttribute.name)) {
                            attributeNames.add(omrsBeanAttribute.name);
                            newAttributes.add(omrsBeanAttribute);
                        }
                    }
                    // store the new attributes we find in a separate variable to avoid a Concurrent access exception
                    if (!newAttributes.isEmpty() ) {
                        for (OmrsBeanAttribute newOmrsBeanAttribute :newAttributes) {
                            deduplicatedOmrsBeanAttributes.add(newOmrsBeanAttribute);
                        }
                    }
                    if (attributeNames.size() != deduplicatedOmrsBeanAttributes.size()) {
                        // we we end up here then deduplicatedOmrsBeanAttributes is not dedupliucated
                        throw new RuntimeException("Unexpected mismatch2 of attributes during reference processing");
                    }
                }
            }
            this.omrsBeanReferencesAsAttributesByEntity.put(entityDefName, deduplicatedOmrsBeanAttributes);
        }
    }

    public List<OmrsBeanReference> getOmrsBeanReferences() {
        return this.omrsBeanReferences;
    }

    public OmrsBeanRelationship getOmrsBeanRelationshipByName(String name) {
        return this.nametoRelationshipMap.get(name);
    }

    public Map<String, OmrsBeanRelationship> getOmrsBeanRelationshipMap() {
        return this.nametoRelationshipMap;
    }

    public Map<String, List<OmrsBeanAttribute>> getOmrsBeanRelationshipAttributeMap() {
        return this.omrsBeanRelationshipAttributesMap;
    }

    public Map<String, List<OmrsBeanAttribute>> getOmrsBeanEntityAttributeMap() {
        return this.omrsBeanEntityAttributesMap;
    }

    public Map<String, List<OmrsBeanAttribute>> getOmrsBeanClassificationAttributeMap() {
        return this.omrsBeanClassificationAttributesMap;
    }

    public Map<String, String> getOmrsBeanClassificationDescriptionMap() {
        return this.omrsBeanClassificationDescriptionMap;
    }

    public Map<String, List<OmrsBeanAttribute>> getOmrsBeanReferencesAsAttributesByEntity() {
        return this.omrsBeanReferencesAsAttributesByEntity;
    }

    /**
     * Open metadata types maps in the same way as java - but with lower case.
     * At this time there are only maps with string string. This method coverts the typeName to be
     * valid java.
     * It also converts arrays into lists.
     *
     * @param typeName
     * @return String
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
