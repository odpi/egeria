/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.archivemanager;

import org.odpi.openmetadata.repositoryservices.archivemanager.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.*;


/**
 * OMRSArchiveAccessor provides utility methods to help access the content of an open metadata archive.
 */
public class OMRSArchiveAccessor
{
    private final Map<String, EntityDef> entityDefsByName = new HashMap<>();
    private final Map<String, EntityDef> entityDefsByGuid = new HashMap<>();
    private final Map<String, ClassificationDef> classificationDefs = new HashMap<>();
    private final Map<String, RelationshipDef> relationshipDefsByName = new HashMap<>();
    private final Map<String, RelationshipDef> relationshipDefsByGuid = new HashMap<>();
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
                    this.entityDefsByName.put(typeDef.getName(), ((EntityDef) typeDef));
                    this.entityDefsByGuid.put(typeDef.getGUID(), ((EntityDef) typeDef));
                    break;
                case CLASSIFICATION_DEF:
                    this.classificationDefs.put(typeDef.getName(), ((ClassificationDef) typeDef));
                    break;
                case RELATIONSHIP_DEF:
                    this.relationshipDefsByName.put(typeDef.getName(), (RelationshipDef) typeDef);
                    this.relationshipDefsByGuid.put(typeDef.getGUID(), (RelationshipDef) typeDef);
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
        return entityDefsByName.get(typeName);
    }
    /**
     * Return the entity type definition for the supplied type guid.
     *
     * @param typeGuid guid of the type
     * @return EntityDef object from the archive
     */
    public EntityDef getEntityDefByGuid(String typeGuid)
    {
        return entityDefsByGuid.get(typeGuid);
    }


    /**
     * Return the relationship type definition for the supplied name.
     *
     * @param typeName name of type
     * @return RelationshipDef object from the archive
     */
    public RelationshipDef getRelationshipDefByName(String typeName)
    {
        return relationshipDefsByName.get(typeName);
    }
    /**
     * Return the relationship type definition for the supplied type guid.
     *
     * @param typeGuid guid of type
     * @return RelationshipDef object from the archive
     */
    public RelationshipDef getRelationshipDefByGuid(String typeGuid)
    {
        return relationshipDefsByGuid.get(typeGuid);
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


    /**
     * Return the instance type definition for the supplied TypeDef
     *
     * @param typeDef typeDef source values for the InstanceType
     * @return the an InstanceType (template)
     */
    public InstanceType createTemplateFromTypeDef(TypeDef typeDef) {
        InstanceType template = new InstanceType();
        template.setTypeDefName(typeDef.getName());
        template.setTypeDefCategory(typeDef.getCategory());
        template.setTypeDefDescription(typeDef.getDescription());
        template.setTypeDefDescriptionGUID(typeDef.getDescriptionGUID());
        template.setTypeDefGUID(typeDef.getGUID());

        List supertypes = new ArrayList();
        supertypes.add(typeDef.getSuperType());
        template.setTypeDefSuperTypes(supertypes);
        template.setTypeDefVersion(typeDef.getVersion());
        template.setValidStatusList(typeDef.getValidInstanceStatusList());
        // Not setting template.setValidInstanceProperties(); as this information is not in the typeDef
        return template;
    }
}

