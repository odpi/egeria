/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.fvt.opentypes.model;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.ArrayList;
import java.util.*;
import java.util.HashMap;

/**
 * This stores the types in maps - so they can be accessed easily. This class can be used as a filter to restrict the types
 * that are used for generation.
 */
public class OmTypeFilter {

    private List<TypeDef> allTypeDefs;
    private List<AttributeTypeDef> allAttributeTypeDefs;

    private Map<String, EnumDef> allEnumTypedefs = new HashMap<>();
    private Map<String, EntityDef> allEntityTypedefs = new HashMap<>();
    private Map<String, RelationshipDef> allRelationshipTypedefs = new HashMap<>();
    private Map<String, ClassificationDef> allClassificationTypedefs = new HashMap<>();
    
    /**
     * Constructor
     * spin through all the types and store the types in maps
     * <p>
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
                    break;
                case CLASSIFICATION_DEF:
                    this.allClassificationTypedefs.put(omTypeDef.getName(), ((ClassificationDef) omTypeDef));
                    break;
                case RELATIONSHIP_DEF:
                    this.allRelationshipTypedefs.put(omTypeDef.getName(), (RelationshipDef) omTypeDef);
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

    }

    // helper methods
    public EntityDef getEntityDef(String typeName) {
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
        return allEntityTypedefs;
    }

    public Map<String,ClassificationDef> getClassificationDefsMap() {
        return allClassificationTypedefs;
    }

    public Map<String, EnumDef> getEnumDefsMap() {
        return allEnumTypedefs;
    }

    public Map<String, RelationshipDef> getRelationshipTypeDefsMap() {
        return allRelationshipTypedefs;
    }

    public String getTypeDescription (String typeName) {
        String description = null;
        if (this.allEntityTypedefs.get(typeName) !=null) {
            description = this.allEntityTypedefs.get(typeName).getDescription();
        } else if (this.allClassificationTypedefs.get(typeName) !=null) {
            description = this.allClassificationTypedefs.get(typeName).getDescription();
        } else  if (this.allRelationshipTypedefs.get(typeName) !=null) {
            description = this.allRelationshipTypedefs.get(typeName).getDescription();
        } else if (this.allEnumTypedefs.get(typeName) !=null) {
            description = this.allEnumTypedefs.get(typeName).getDescription();
        }
        if (description==null) {
            description="";
        }
        return description;
    }
}
