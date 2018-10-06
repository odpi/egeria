/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.archivemanager;

import org.odpi.openmetadata.repositoryservices.archivemanager.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.*;


/**
 * OMRSArchiveAccessor provides utility methods to help access the content of an open metadata archive.
 */
public class OMRSArchiveAccessor
{
    private final Map<String, EntityDef>         entityDefs         = new HashMap<>();
    private final Map<String, ClassificationDef> classificationDefs = new HashMap<>();
    private final Map<String, RelationshipDef>   relationshipDefs   = new HashMap<>();
    private final Map<String, EnumDef>           enumDefs           = new HashMap<>();

    private OpenMetadataArchive          openMetadataArchive;
    private OpenMetadataArchiveTypeStore typeStore;

    private static OMRSArchiveAccessor instance = null;

    /**
     * Return a special singleton for the open metadata types archive (note not thread safe).
     *
     * @return OMRSArchiveAccessor instance loaded with the open metadata types
     */
    public static OMRSArchiveAccessor getInstance()
    {
        if (instance == null)
        {
            instance = new OMRSArchiveAccessor(new OpenMetadataTypesArchive().getOpenMetadataArchive());
        }
        return instance;
    }



    /**
     * Parse the supplied archive.
     *
     * @param openMetadataArchive archive object
     */
    public OMRSArchiveAccessor(OpenMetadataArchive openMetadataArchive)
    {
        this.openMetadataArchive = openMetadataArchive;
        this.typeStore = this.openMetadataArchive.getArchiveTypeStore();

        /*
         * break down these types so we can consume them.
         */
        for (TypeDef typeDef : this.typeStore.getNewTypeDefs())
        {
            switch (typeDef.getCategory())
            {
                case ENTITY_DEF:
                    this.entityDefs.put(typeDef.getName(), ((EntityDef) typeDef));
                    break;
                case CLASSIFICATION_DEF:
                    this.classificationDefs.put(typeDef.getName(), ((ClassificationDef) typeDef));
                    break;
                case RELATIONSHIP_DEF:
                    this.relationshipDefs.put(typeDef.getName(), (RelationshipDef) typeDef);
                    break;
            }
        }

        for (AttributeTypeDef attributeTypeDef : this.typeStore.getAttributeTypeDefs())
        {
            switch (attributeTypeDef.getCategory())
            {
                case ENUM_DEF:
                    enumDefs.put(attributeTypeDef.getName(), ((EnumDef) attributeTypeDef));
                    break;

                case PRIMITIVE:
                    // todo
                    break;
                case COLLECTION:
                    // todo
                    break;
            }

        }
    }


    /**
     * Return the entity type definition for the supplied name.
     *
     * @param typeName name of type
     * @return EntityDef object from the archive
     */
    public EntityDef getEntityDefByName(String typeName)
    {
        return entityDefs.get(typeName);
    }


    /**
     * Return the relationship type definition for the supplied name.
     *
     * @param typeName name of type
     * @return RelationshipDef object from the archive
     */
    public RelationshipDef getRelationshipDefByName(String typeName)
    {
        return relationshipDefs.get(typeName);
    }


    /**
     * Return the classification type definition for the supplied name.
     *
     * @param typeName name of type
     * @return ClassificationDef object from the archive
     */
    public ClassificationDef getClassificationDefByName(String typeName)
    {
        return classificationDefs.get(typeName);
    }


    /**
     * Return the enumeration type definition for the supplied name.
     *
     * @param typeName name of type
     * @return EnumDef object from the archive
     */
    public EnumDef getEnumDefByName(String typeName)
    {
        return enumDefs.get(typeName);
    }
}

