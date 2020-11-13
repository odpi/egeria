/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;

import java.util.ArrayList;
import java.util.List;


public class EntityExplorer {

    private EntityDef                entityDef;
    private List<TypeDefAttribute>   inheritedAttributes;
    private List<String>             subTypeNames;
    private List<String>             classificationNames;
    private List<String>             relationshipNames;
    private List<String>             inheritedRelationshipNames;
    private List<String>             inheritedClassificationNames;


    public EntityExplorer(EntityDef def) {
        entityDef                      = def;
        inheritedAttributes            = new ArrayList<>();
        subTypeNames                   = new ArrayList<>();
        classificationNames            = new ArrayList<>();
        relationshipNames              = new ArrayList<>();
        inheritedRelationshipNames     = new ArrayList<>();
        inheritedClassificationNames   = new ArrayList<>();

    }

    public EntityDef getEntityDef() { return entityDef; }

    public List<TypeDefAttribute> getInheritedAttributes() { return inheritedAttributes; }

    public List<String> getSubTypeNames() { return subTypeNames; }

    public List<String> getClassificationNames() { return classificationNames; }

    public List<String> getRelationshipNames() { return relationshipNames; }

    public List<String> getInheritedRelationshipNames() { return inheritedRelationshipNames; }

    public List<String> getInheritedClassificationNames() { return inheritedClassificationNames; }

    public void addSubTypName(String subTypeName) {
        subTypeNames.add(subTypeName);
    }

    public void addInheritedAttributes(List<TypeDefAttribute> inheritedAttrs) {
        if (inheritedAttrs != null) {
            inheritedAttributes.addAll(inheritedAttrs);
        }
    }

    public void addRelationship(String relationshipTypeName) {
        if (relationshipTypeName != null) {
            relationshipNames.add(relationshipTypeName);
        }
    }

    public void addInheritedRelationship(String relationshipTypeName) {
        if (relationshipTypeName != null) {
            inheritedRelationshipNames.add(relationshipTypeName);
        }
    }

    public void addClassification(String classificationName) {
        if (classificationName != null) {
            classificationNames.add(classificationName);
        }
    }

    public void addInheritedClassification(String classificationName) {
        if (classificationName != null) {
            inheritedClassificationNames.add(classificationName);
        }
    }

}
