/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;

import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataTypesArchiveAccessor provides utility methods to help access the content of an open metadata archive.
 */
public class OpenMetadataTypesArchiveAccessor
{
    private final Map<String, TypeDef>           typeDefsByName         = new HashMap<>();
    private final Map<String, TypeDef>           typeDefsByGUID         = new HashMap<>();
    private final Map<String, EntityDef>         entityDefsByName       = new HashMap<>();
    private final Map<String, EntityDef>         entityDefsByGUID       = new HashMap<>();
    private final Map<String, ClassificationDef> classificationDefs     = new HashMap<>();
    private final Map<String, RelationshipDef>   relationshipDefsByName = new HashMap<>();
    private final Map<String, RelationshipDef>   relationshipDefsByGUID = new HashMap<>();
    private final Map<String, EnumDef>           enumDefs               = new HashMap<>();

    private static OpenMetadataTypesArchiveAccessor instance = null;


    /**
     * Return a special singleton for the open metadata types archive (note not thread safe).
     *
     * @return OpenMetadataTypesArchiveAccessor instance loaded with the open metadata types
     */
    public synchronized static OpenMetadataTypesArchiveAccessor getInstance()
    {
        if (instance == null)
        {
            instance = new OpenMetadataTypesArchiveAccessor(new OpenMetadataTypesArchive().getOpenMetadataArchive());
        }

        return instance;
    }


    /**
     * Parse the supplied archive.
     *
     * @param openMetadataArchive archive object
     */
    public OpenMetadataTypesArchiveAccessor(OpenMetadataArchive openMetadataArchive)
    {
        OpenMetadataArchiveTypeStore typeStore = openMetadataArchive.getArchiveTypeStore();

        /*
         * break down these types so we can consume them.
         */
        for (TypeDef typeDef : typeStore.getNewTypeDefs())
        {
            this.typeDefsByName.put(typeDef.getName(), typeDef);
            this.typeDefsByGUID.put(typeDef.getGUID(), typeDef);

            switch (typeDef.getCategory())
            {
                case ENTITY_DEF:
                    this.entityDefsByName.put(typeDef.getName(), ((EntityDef) typeDef));
                    this.entityDefsByGUID.put(typeDef.getGUID(), ((EntityDef) typeDef));
                    break;

                case CLASSIFICATION_DEF:
                    this.classificationDefs.put(typeDef.getName(), ((ClassificationDef) typeDef));
                    break;

                case RELATIONSHIP_DEF:
                    this.relationshipDefsByName.put(typeDef.getName(), (RelationshipDef) typeDef);
                    this.relationshipDefsByGUID.put(typeDef.getGUID(), (RelationshipDef) typeDef);
                    break;
            }
        }

        for (AttributeTypeDef attributeTypeDef : typeStore.getAttributeTypeDefs())
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
     * Return the type definition for the supplied name.
     *
     * @param typeName name of type
     * @return TypeDef object from the archive
     */
    public TypeDef getTypeDefByName(String typeName)
    {
        return typeDefsByName.get(typeName);
    }


    /**
     * Return the type definition for the supplied type guid.
     *
     * @param typeGUID guid of the type
     * @return TypeDef object from the archive
     */
    public TypeDef getTypeDefByGUID(String typeGUID)
    {
        return typeDefsByGUID.get(typeGUID);
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
        return entityDefsByGUID.get(typeGuid);
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
        return relationshipDefsByGUID.get(typeGuid);
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
    public InstanceType createTemplateFromTypeDef(TypeDef typeDef)
    {
        InstanceType template = new InstanceType();

        template.setHeaderVersion(InstanceType.CURRENT_INSTANCE_TYPE_HEADER_VERSION);
        template.setTypeDefName(typeDef.getName());
        template.setTypeDefCategory(typeDef.getCategory());
        template.setTypeDefDescription(typeDef.getDescription());
        template.setTypeDefDescriptionGUID(typeDef.getDescriptionGUID());
        template.setTypeDefGUID(typeDef.getGUID());

        List<TypeDefLink> supertypes = new ArrayList<>();
        supertypes.add(typeDef.getSuperType());
        template.setTypeDefSuperTypes(supertypes);
        template.setTypeDefVersion(typeDef.getVersion());
        template.setValidStatusList(typeDef.getValidInstanceStatusList());
        // Not setting template.setValidInstanceProperties(); as this information is not in the typeDef
        return template;
    }
}

