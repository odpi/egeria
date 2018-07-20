/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.archivemanager;


import org.odpi.openmetadata.repositoryservices.archivemanager.opentypes.OpenMetadataTypesArchive;
        import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
        import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

        import java.util.*;

        import java.util.ArrayList;
        import java.util.Date;

/**
 * OMRSArchiveAccessor provides utility methods to help accessing open metadata archive content.
 */
public class OMRSArchiveAccessor
{
    private final Map<String,EntityDef> entityDefs= new HashMap<>();
    private final Map<String,ClassificationDef> classificationDefs= new HashMap<>();
    private final Map<String,RelationshipDef> relationshipDefs= new HashMap<>();
    private final Map<String,EnumDef> enumDefs= new HashMap<>();

    OpenMetadataTypesArchive openMetadataTypesArchive =null;
    OpenMetadataArchiveTypeStore typeStore = null;
    private static OMRSArchiveAccessor instance = null;

    public static OMRSArchiveAccessor getInstance() {
        if (instance == null) {
            instance = new  OMRSArchiveAccessor(new OpenMetadataTypesArchive());
        }
        return instance;
    }

    private OMRSArchiveAccessor(OpenMetadataTypesArchive openMetadataTypesArchive) {
        this.openMetadataTypesArchive = openMetadataTypesArchive;
        this.typeStore = this.openMetadataTypesArchive.getOpenMetadataArchive().getArchiveTypeStore();

        // break down these types so we can consume them.
        for (TypeDef omTypeDef : this.typeStore.getNewTypeDefs()) {
            switch (omTypeDef.getCategory()) {
                case ENTITY_DEF:
                    this.entityDefs.put(omTypeDef.getName(), ((EntityDef) omTypeDef));
                    break;
                case CLASSIFICATION_DEF:
                    this.classificationDefs.put(omTypeDef.getName(), ((ClassificationDef) omTypeDef));
                    break;
                case RELATIONSHIP_DEF:
                    this.relationshipDefs.put(omTypeDef.getName(), (RelationshipDef) omTypeDef);
                    break;
            }
        }
        for (AttributeTypeDef omAttrTypeDef : this.typeStore.getAttributeTypeDefs()) {
            switch (omAttrTypeDef.getCategory()) {
                case ENUM_DEF:
                    enumDefs.put(omAttrTypeDef.getName(), ((EnumDef) omAttrTypeDef));
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
    public EntityDef getEntityDefByName(String typeName) {
        return entityDefs.get(typeName);
    }
}

