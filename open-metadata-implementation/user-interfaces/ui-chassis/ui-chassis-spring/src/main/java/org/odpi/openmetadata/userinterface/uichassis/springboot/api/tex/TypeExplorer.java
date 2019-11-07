/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.tex;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;



@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeExplorer {


    private Map<String, EntityExplorer> entities;
    private Map<String, RelationshipExplorer>     relationships;
    private Map<String, ClassificationExplorer>   classifications;
    private Map<String, EnumDef>                  enums;


    public TypeExplorer() {
        entities        = new HashMap<>();
        relationships   = new HashMap<>();
        classifications = new HashMap<>();
        enums           = new HashMap<>();
    }

    /*
     * Getters for Jackson
     */
    public Map<String, EntityExplorer> getEntities() {
        return entities;
    }

    public Map<String, RelationshipExplorer> getRelationships() {
        return relationships;
    }

    public Map<String, ClassificationExplorer> getClassifications() {
        return classifications;
    }

    public Map<String, EnumDef> getEnums() {
        return enums;
    }


    public void addEntityExplorer(String entityTypeName, EntityExplorer entityExplorer) {
        entities.put(entityTypeName, entityExplorer);
    }

    public void addRelationshipExplorer(String relationshipTypeName, RelationshipExplorer relationshipExplorer) {
        relationships.put(relationshipTypeName, relationshipExplorer);
    }

    public void addClassificationExplorer(String classificationTypeName, ClassificationExplorer classificationExplorer) {
        classifications.put(classificationTypeName, classificationExplorer);
    }

    public void addEnumExplorer(String enumTypeName, EnumDef enumDef) {
        enums.put(enumTypeName, enumDef);
    }

    public void resolve() {

        /*
         * After all types have been loaded, call the resolver methods to expand the TEX for each type category
         * Order is important, entities are expanded first.
         */

        resolveEntities();
        resolveRelationships();
        resolveClassifications();

    }

    private void resolveEntities() {

        // For each entityExplorer add it to its superType's subTypes
        for (String entityTypeName : entities.keySet()) {
            EntityExplorer entityExplorer = entities.get(entityTypeName);
            TypeDefLink superType = entityExplorer.getEntityDef().getSuperType();
            // Not every entity type has a superType
            if (superType != null) {
                String superTypeName = superType.getName();
                // Find supertype in explorer
                EntityExplorer superTypeExplorer = entities.get(superTypeName);
                superTypeExplorer.addSubTypName(entityTypeName);
            }
        }

        // For each entityExplorer resolve its full list of attributes
        for (String entityTypeName : entities.keySet()) {
            EntityExplorer entityExplorer = entities.get(entityTypeName);
            TypeDefLink superType = entityExplorer.getEntityDef().getSuperType();
            while (superType != null) {
                // add supertype's attributes to subtype
                String superTypeName = superType.getName();
                // find supertype in explorer
                EntityExplorer superTypeExplorer = entities.get(superTypeName);
                EntityDef superDef = superTypeExplorer.getEntityDef();
                List<TypeDefAttribute> superAttributes = superDef.getPropertiesDefinition();
                entityExplorer.addInheritedAttributes(superAttributes);
                superType = superDef.getSuperType();
            }
        }
    }

    private void resolveRelationships()
    {
        // For each relationshipExplorer gets its end types and add the relationship type to each of the entity types
        for (String relationshipTypeName : relationships.keySet()) {

            RelationshipExplorer relationshipExplorer = relationships.get(relationshipTypeName);
            RelationshipEndDef entityOneDef = relationshipExplorer.getRelationshipDef().getEndDef1();
            RelationshipEndDef entityTwoDef = relationshipExplorer.getRelationshipDef().getEndDef2();

            if (entityOneDef != null) {
                String entityOneTypeName = entityOneDef.getEntityType().getName();
                EntityExplorer entityOneExplorer = entities.get(entityOneTypeName);
                entityOneExplorer.addRelationship(relationshipTypeName);
                List<String> subTypeNames = entityOneExplorer.getSubTypeNames();
                for (String subTypeName : subTypeNames) {
                    addRelationshipToSubType(relationshipTypeName,subTypeName);
                }
            }

            if (entityTwoDef != null) {
                String entityTwoTypeName = entityTwoDef.getEntityType().getName();
                EntityExplorer entityTwoExplorer = entities.get(entityTwoTypeName);
                entityTwoExplorer.addRelationship(relationshipTypeName);
                List<String> subTypeNames = entityTwoExplorer.getSubTypeNames();
                for (String subTypeName : subTypeNames) {
                    addRelationshipToSubType(relationshipTypeName,subTypeName);
                }
            }


        }
    }

    private void addRelationshipToSubType(String relationshipTypeName, String entityTypeName) {

        EntityExplorer entityExplorer = entities.get(entityTypeName);
        entityExplorer.addInheritedRelationship(relationshipTypeName);

        // And recurse downwards...
        List<String> subTypeNames = entityExplorer.getSubTypeNames();
        if (!subTypeNames.isEmpty()) {
            for (String subTypeName : subTypeNames) {
                addRelationshipToSubType(relationshipTypeName,subTypeName);
            }
        }
    }


    private void resolveClassifications()
    {
        // For each classificationExplorer add its name to the known classifications for its valid entity types
        for (String classificationTypeName : classifications.keySet()) {
            ClassificationExplorer classificationExplorer = classifications.get(classificationTypeName);
            List<TypeDefLink> validEntityTypes = classificationExplorer.getClassificationDef().getValidEntityDefs();
            if (validEntityTypes != null) {
                for (TypeDefLink entityType : validEntityTypes) {
                    String entityTypeName = entityType.getName();
                    EntityExplorer entityExplorer = entities.get(entityTypeName);
                    entityExplorer.addClassification(classificationTypeName);
                    List<String> subTypeNames = entityExplorer.getSubTypeNames();
                    for (String subTypeName : subTypeNames) {
                        addClassificationToSubType(classificationTypeName,subTypeName);
                    }
                }
            }
        }

        // For each classificationExplorer add it to its superType's subTypes
        for (String classificationTypeName : classifications.keySet()) {
            ClassificationExplorer classificationExplorer = classifications.get(classificationTypeName);
            TypeDefLink superType = classificationExplorer.getClassificationDef().getSuperType();
            if (superType != null) {
                String superTypeName = superType.getName();
                // find supertype in explorer
                ClassificationExplorer superTypeExplorer = classifications.get(superTypeName);
                superTypeExplorer.addSubTypName(classificationTypeName);
            }
        }

        // For each classificationExplorer resolve its full list of attributes
        for (String classificationTypeName : classifications.keySet()) {
            ClassificationExplorer classificationExplorer = classifications.get(classificationTypeName);
            TypeDefLink superType = classificationExplorer.getClassificationDef().getSuperType();
            while (superType != null) {
                // add supertype's attributes to subtype
                String superTypeName = superType.getName();
                // find supertype in explorer
                ClassificationExplorer superTypeExplorer = classifications.get(superTypeName);
                ClassificationDef superDef = superTypeExplorer.getClassificationDef();
                List<TypeDefAttribute> superAttributes = superDef.getPropertiesDefinition();
                classificationExplorer.addInheritedAttributes(superAttributes);
                superType = superDef.getSuperType();
            }
        }
    }

    private void addClassificationToSubType(String classificationTypeName, String entityTypeName) {

        EntityExplorer entityExplorer = entities.get(entityTypeName);
        entityExplorer.addInheritedClassification(classificationTypeName);

        // And recurse downwards...
        List<String> subTypeNames = entityExplorer.getSubTypeNames();
        if (!subTypeNames.isEmpty()) {
            for (String subTypeName : subTypeNames) {
                addClassificationToSubType(classificationTypeName,subTypeName);
            }
        }
    }

    @Override
    public String toString()
    {
        return "TypeExplorer{" +
                "entities=" + entities +
                ", relationships=" + relationships +
                ", classifications=" + classifications +
                '}';
    }



}
