/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.archiveutilities;


import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities.OMRSRepositoryPropertiesUtilities;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;

import java.util.*;

/**
 * OMRSArchiveHelper provides utility methods to help in the construction of open metadata archives.
 */
public class OMRSArchiveHelper extends OMRSRepositoryPropertiesUtilities
{
    private final OpenMetadataArchiveBuilder archiveBuilder;
    private final String                     archiveGUID;
    private       String                     archiveName;
    private final String                     originatorName;
    private final Date                       creationDate;
    private final long                       versionNumber;
    private final String                     versionName;
    private       InstanceProvenanceType     instanceProvenanceType = InstanceProvenanceType.CONTENT_PACK;
    private       String                     license = null;


    /**
     * Constructor receives information about the archive being built.
     * This constructor is suitable if only creating typeDefs.
     *
     * @param archiveBuilder archive builder with the archive contents in it.
     * @param archiveGUID unique identifier for the archive.
     * @param originatorName name of the person / process creating the archive.
     * @param creationDate date that the archive was first built.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     */
    public OMRSArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                             String                     archiveGUID,
                             String                     originatorName,
                             Date                       creationDate,
                             long                       versionNumber,
                             String                     versionName)
    {
        this.archiveBuilder = archiveBuilder;
        this.archiveGUID = archiveGUID;
        this.originatorName = originatorName;
        this.creationDate = creationDate;
        this.versionNumber = versionNumber;
        this.versionName = versionName;
    }


    /**
     * Constructor receives information about the archive being built.
     * This constructor is suitable for creating TypeDefs and instances.
     *
     * @param archiveBuilder archive builder with the archive contents in it.
     * @param archiveGUID unique identifier for the archive.
     * @param archiveName unique name for the archive.
     * @param originatorName name of the person / process creating the archive.
     * @param creationDate date that the archive was first built.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     * @param instanceProvenanceType type of archive.
     * @param license license for the archive contents.
     */
    public OMRSArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                             String                     archiveGUID,
                             String                     archiveName,
                             String                     originatorName,
                             Date                       creationDate,
                             long                       versionNumber,
                             String                     versionName,
                             InstanceProvenanceType     instanceProvenanceType,
                             String                     license)
    {
        this.archiveBuilder = archiveBuilder;
        this.archiveGUID = archiveGUID;
        this.archiveName = archiveName;
        this.originatorName = originatorName;
        this.creationDate = creationDate;
        this.versionNumber = versionNumber;
        this.versionName = versionName;
        this.instanceProvenanceType = instanceProvenanceType;
        this.license = license;
    }


    /**
     * Set up an individual primitive definition
     *
     * @param primitiveDefCategory category of the primitive def defines the unique
     *                             information about this primitive type.
     * @return initialized PrimitiveDef object ready for the archive
     */
    public PrimitiveDef getPrimitiveDef(PrimitiveDefCategory primitiveDefCategory)
    {
        PrimitiveDef  primitiveDef = new PrimitiveDef(primitiveDefCategory);

        primitiveDef.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        primitiveDef.setGUID(primitiveDefCategory.getGUID());
        primitiveDef.setName(primitiveDefCategory.getName());
        primitiveDef.setVersion(versionNumber);
        primitiveDef.setVersionName(versionName);

        return primitiveDef;
    }


    /**
     * Create a CollectionDef for an Array.  A new CollectionDef is required for each combination of primitive types
     * used to initialize the collection.  Each CollectionDef has its own unique identifier (guid) and
     * its name is a combination of the collection type and the primitives use to initialize it.
     *
     * @param guid unique identifier for the CollectionDef
     * @param description short default description of the enum type
     * @param descriptionGUID guid of the valid value describing this collection type
     * @param arrayType type of the array.
     * @return Filled out CollectionDef
     */
    public CollectionDef getArrayCollectionDef(String                guid,
                                               String                description,
                                               String                descriptionGUID,
                                               PrimitiveDefCategory  arrayType)
    {
        CollectionDef   collectionDef = new CollectionDef(CollectionDefCategory.OM_COLLECTION_ARRAY);

        collectionDef.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        collectionDef.setGUID(guid);
        collectionDef.setName("array<" + arrayType.getName() + ">");
        collectionDef.setDescription(description);
        collectionDef.setDescriptionGUID(descriptionGUID);
        collectionDef.setVersion(versionNumber);
        collectionDef.setVersionName(versionName);

        List<PrimitiveDefCategory> argumentList = new ArrayList<>();
        argumentList.add(arrayType);
        collectionDef.setArgumentTypes(argumentList);

        return collectionDef;
    }


    /**
     * Create a CollectionDef for a map.  A new CollectionDef is required for each combination of primitive types
     * used to initialize the collection.  Each CollectionDef has its own unique identifier (guid) and
     * its name is a combination of the collection type and the primitives use to initialize it.
     *
     * @param guid unique identifier for the CollectionDef
     * @param description short default description of the enum type
     * @param descriptionGUID guid of the valid value describing this collection type
     * @param propertyKeyType type of the key for the map.
     * @param propertyValueType  type of map value.
     * @return Filled out CollectionDef
     */
    public CollectionDef getMapCollectionDef(String                guid,
                                             String                description,
                                             String                descriptionGUID,
                                             PrimitiveDefCategory  propertyKeyType,
                                             PrimitiveDefCategory  propertyValueType)
    {
        CollectionDef   collectionDef = new CollectionDef(CollectionDefCategory.OM_COLLECTION_MAP);

        collectionDef.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        collectionDef.setGUID(guid);
        collectionDef.setName("map<" + propertyKeyType.getName() + "," + propertyValueType.getName() + ">");
        collectionDef.setDescription(description);
        collectionDef.setDescriptionGUID(descriptionGUID);
        collectionDef.setVersion(versionNumber);
        collectionDef.setVersionName(versionName);

        ArrayList<PrimitiveDefCategory> argumentList = new ArrayList<>();
        argumentList.add(propertyKeyType);
        argumentList.add(propertyValueType);
        collectionDef.setArgumentTypes(argumentList);

        return collectionDef;
    }


    /**
     * Create an EnumDef that has no valid values defined.  These are added by the caller.
     *
     * @param guid unique identifier for the CollectionDef
     * @param name unique name for the CollectionDef
     * @param description short default description of the enum type
     * @param descriptionGUID guid of the valid value describing this enum type
     * @return basic EnumDef without valid values
     */
    public EnumDef getEmptyEnumDef(String                guid,
                                   String                name,
                                   String                description,
                                   String                descriptionGUID)
    {
        return this.getEmptyEnumDef(guid, name, description, descriptionGUID, null);
    }


    /**
     * Create an EnumDef that has no valid values defined.  These are added by the caller.
     *
     * @param guid unique identifier for the CollectionDef
     * @param name unique name for the CollectionDef
     * @param description short default description of the enum type
     * @param descriptionGUID guid of the valid value describing this enum type
     * @param descriptionWiki link to the url for documentation of this type
     * @return basic EnumDef without valid values
     */
    public EnumDef getEmptyEnumDef(String                guid,
                                   String                name,
                                   String                description,
                                   String                descriptionGUID,
                                   String                descriptionWiki)
    {
        EnumDef  enumDef = new EnumDef();

        enumDef.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        enumDef.setGUID(guid);
        enumDef.setName(name);
        enumDef.setDescription(description);
        enumDef.setDescriptionGUID(descriptionGUID);
        enumDef.setDescriptionWiki(descriptionWiki);
        enumDef.setDefaultValue(null);
        enumDef.setVersion(versionNumber);
        enumDef.setVersionName(versionName);

        return enumDef;
    }


    /**
     * Create an EnumElementDef that carries one of the valid values for an Enum.
     *
     * @param ordinal code number
     * @param value name
     * @param description short description
     * @param descriptionGUID guid of the valid value describing this enum element
     * @return Fully filled out EnumElementDef
     */
    public EnumElementDef  getEnumElementDef(int     ordinal,
                                             String  value,
                                             String  description,
                                             String  descriptionGUID)
    {
        EnumElementDef   enumElementDef = new EnumElementDef();

        enumElementDef.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        enumElementDef.setOrdinal(ordinal);
        enumElementDef.setValue(value);
        enumElementDef.setDescription(description);
        enumElementDef.setDescriptionGUID(descriptionGUID);

        return enumElementDef;
    }


    /**
     * Sets up a default EntityDef.  Calling methods can override the default values.  This EntityDef
     * has no attribute defined.
     *
     * @param guid unique identifier for the entity
     * @param name name of the entity
     * @param superType Super type for this entity (null for top-level)
     * @param description short description of the entity
     * @param descriptionGUID guid of the valid value describing this entity type
     * @return Initialized EntityDef
     */
    public EntityDef  getDefaultEntityDef(String                  guid,
                                          String                  name,
                                          TypeDefLink             superType,
                                          String                  description,
                                          String                  descriptionGUID)
    {
        return this.getDefaultEntityDef(guid, name, superType, description, descriptionGUID, null);
    }


    /**
     * Sets up a default EntityDef.  Calling methods can override the default values.  This EntityDef
     * has no attribute defined.
     *
     * @param type type description enum
     * @param superType Super type for this entity (null for top-level)
     * @return Initialized EntityDef
     */
    public EntityDef  getDefaultEntityDef(OpenMetadataType type,
                                          TypeDefLink      superType)
    {
        return this.getDefaultEntityDef(type.typeGUID, type.typeName, superType, type.description, type.descriptionGUID, type.wikiURL);
    }


    /**
     * Sets up a default EntityDef.  Calling methods can override the default values.  This EntityDef
     * has no attribute defined.
     *
     * @param guid unique identifier for the entity
     * @param name name of the entity
     * @param superType Super type for this entity (null for top-level)
     * @param description short description of the entity
     * @param descriptionGUID guid of the valid value definition describing this entity type
     * @param descriptionWiki url to wiki page describing this type
     * @return Initialized EntityDef
     */
    public EntityDef  getDefaultEntityDef(String                  guid,
                                          String                  name,
                                          TypeDefLink             superType,
                                          String                  description,
                                          String                  descriptionGUID,
                                          String                  descriptionWiki)
    {
        EntityDef entityDef = new EntityDef();

        entityDef.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);

        /*
         * Set up the parameters supplied by the caller.
         */
        entityDef.setGUID(guid);
        entityDef.setName(name);
        entityDef.setSuperType(superType);
        entityDef.setDescription(description);
        entityDef.setDescriptionGUID(descriptionGUID);
        entityDef.setDescriptionWiki(descriptionWiki);

        /*
         * Set up the defaults
         */
        entityDef.setOrigin(archiveGUID);
        entityDef.setCreatedBy(originatorName);
        entityDef.setCreateTime(creationDate);
        entityDef.setVersion(versionNumber);
        entityDef.setVersionName(versionName);

        /*
         * Set default valid instance statuses
         */
        TypeDef superTypeDef = null;
        if (superType != null)
        {
            superTypeDef = this.archiveBuilder.getTypeDefByName(superType.getName());
        }

        if (superTypeDef != null)
        {
            entityDef.setValidInstanceStatusList(superTypeDef.getValidInstanceStatusList());
            entityDef.setInitialStatus(superTypeDef.getInitialStatus());
        }
        else
        {
            /*
             * These are the standard valid instance statuses used by entities
             */
            ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();
            validInstanceStatusList.add(InstanceStatus.ACTIVE);
            validInstanceStatusList.add(InstanceStatus.DELETED);

            entityDef.setValidInstanceStatusList(validInstanceStatusList);
            entityDef.setInitialStatus(InstanceStatus.ACTIVE);
        }

        entityDef.setStatus(TypeDefStatus.ACTIVE_TYPEDEF);

        return entityDef;
    }


    /**
     * Return an attribute with the name and description that is of type identified by the supplied definition.
     * It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param propertyDefinition details of the attribute
     * @return  TypeDefAttribute
     */
    public TypeDefAttribute  getTypeDefAttribute(OpenMetadataProperty propertyDefinition)
    {
        final String methodName = "getTypeDefAttribute";

        if (DataType.STRING.getName().equals(propertyDefinition.type))
        {
            return getTypeDefAttribute(propertyDefinition.name,
                                       propertyDefinition.description,
                                       propertyDefinition.descriptionGUID,
                                       PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        }
        else if (DataType.INT.getName().equals(propertyDefinition.type))
        {
            return getTypeDefAttribute(propertyDefinition.name,
                                       propertyDefinition.description,
                                       propertyDefinition.descriptionGUID,
                                       PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
        }
        else if (DataType.LONG.getName().equals(propertyDefinition.type))
        {
            return getTypeDefAttribute(propertyDefinition.name,
                                       propertyDefinition.description,
                                       propertyDefinition.descriptionGUID,
                                       PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG);
        }
        else if (DataType.SHORT.getName().equals(propertyDefinition.type))
        {
            return getTypeDefAttribute(propertyDefinition.name,
                                       propertyDefinition.description,
                                       propertyDefinition.descriptionGUID,
                                       PrimitiveDefCategory.OM_PRIMITIVE_TYPE_SHORT);
        }
        else if (DataType.DATE.getName().equals(propertyDefinition.type))
        {
            return getTypeDefAttribute(propertyDefinition.name,
                                       propertyDefinition.description,
                                       propertyDefinition.descriptionGUID,
                                       PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
        }
        else if (DataType.BOOLEAN.getName().equals(propertyDefinition.type))
        {
            return getTypeDefAttribute(propertyDefinition.name,
                                       propertyDefinition.description,
                                       propertyDefinition.descriptionGUID,
                                       PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
        }
        else if (DataType.CHAR.getName().equals(propertyDefinition.type))
        {
            return getTypeDefAttribute(propertyDefinition.name,
                                       propertyDefinition.description,
                                       propertyDefinition.descriptionGUID,
                                       PrimitiveDefCategory.OM_PRIMITIVE_TYPE_CHAR);
        }
        else if (DataType.BYTE.getName().equals(propertyDefinition.type))
        {
            return getTypeDefAttribute(propertyDefinition.name,
                                       propertyDefinition.description,
                                       propertyDefinition.descriptionGUID,
                                       PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BYTE);
        }
        else if (DataType.FLOAT.getName().equals(propertyDefinition.type))
        {
            return getTypeDefAttribute(propertyDefinition.name,
                                       propertyDefinition.description,
                                       propertyDefinition.descriptionGUID,
                                       PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT);
        }
        else if (DataType.DOUBLE.getName().equals(propertyDefinition.type))
        {
            return getTypeDefAttribute(propertyDefinition.name,
                                       propertyDefinition.description,
                                       propertyDefinition.descriptionGUID,
                                       PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DOUBLE);
        }
        else if (DataType.BIGINTEGER.getName().equals(propertyDefinition.type))
        {
            return getTypeDefAttribute(propertyDefinition.name,
                                       propertyDefinition.description,
                                       propertyDefinition.descriptionGUID,
                                       PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGINTEGER);
        }
        else if (DataType.BIGDECIMAL.getName().equals(propertyDefinition.type))
        {
            return getTypeDefAttribute(propertyDefinition.name,
                                       propertyDefinition.description,
                                       propertyDefinition.descriptionGUID,
                                       PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL);
        }
        else if (DataType.OBJECT.getName().equals(propertyDefinition.type))
        {
            return getTypeDefAttribute(propertyDefinition.name,
                                       propertyDefinition.description,
                                       propertyDefinition.descriptionGUID,
                                       PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN);
        }
        else if (DataType.ARRAY_STRING.getName().equals(propertyDefinition.type))
        {
            return getArrayStringTypeDefAttribute(propertyDefinition.name,
                                                  propertyDefinition.description,
                                                  propertyDefinition.descriptionGUID);
        }
        else if (DataType.ARRAY_INT.getName().equals(propertyDefinition.type))
        {
            return getArrayIntTypeDefAttribute(propertyDefinition.name,
                                               propertyDefinition.description,
                                               propertyDefinition.descriptionGUID);
        }
        else if (DataType.MAP_STRING_BOOLEAN.getName().equals(propertyDefinition.type))
        {
            return getMapStringBooleanTypeDefAttribute(propertyDefinition.name,
                                                       propertyDefinition.description,
                                                       propertyDefinition.descriptionGUID);
        }
        else if (DataType.MAP_STRING_INT.getName().equals(propertyDefinition.type))
        {
            return getMapStringIntTypeDefAttribute(propertyDefinition.name,
                                                   propertyDefinition.description,
                                                   propertyDefinition.descriptionGUID);
        }
        else if (DataType.MAP_STRING_DOUBLE.getName().equals(propertyDefinition.type))
        {
            return getMapStringDoubleTypeDefAttribute(propertyDefinition.name,
                                                      propertyDefinition.description,
                                                      propertyDefinition.descriptionGUID);
        }
        else if (DataType.MAP_STRING_DATE.getName().equals(propertyDefinition.type))
        {
            return getMapStringDateTypeDefAttribute(propertyDefinition.name,
                                                    propertyDefinition.description,
                                                    propertyDefinition.descriptionGUID);
        }
        else if (DataType.MAP_STRING_STRING.getName().equals(propertyDefinition.type))
        {
            return getMapStringStringTypeDefAttribute(propertyDefinition.name,
                                                      propertyDefinition.description,
                                                      propertyDefinition.descriptionGUID);
        }
        else if (DataType.MAP_STRING_LONG.getName().equals(propertyDefinition.type))
        {
            return getMapStringLongTypeDefAttribute(propertyDefinition.name,
                                                    propertyDefinition.description,
                                                    propertyDefinition.descriptionGUID);
        }
        else if (DataType.MAP_STRING_OBJECT.getName().equals(propertyDefinition.type))
        {
            return getMapStringStringTypeDefAttribute(propertyDefinition.name,
                                                      propertyDefinition.description,
                                                      propertyDefinition.descriptionGUID);
        }
        else
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.BAD_DATA_TYPE.getMessageDefinition(propertyDefinition.type,
                                                                                               methodName),
                                              this.getClass().getName(),
                                              methodName);
        }

    }


    /**
     * Return an attribute with the supplied name and description that is of type String.  It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getTypeDefAttribute(String               attributeName,
                                                 String               attributeDescription,
                                                 String               attributeDescriptionGUID,
                                                 PrimitiveDefCategory primitiveDefCategory)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getPrimitiveDef(primitiveDefCategory.getName()));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is of type String.  It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getStringTypeDefAttribute(String      attributeName,
                                                       String      attributeDescription,
                                                       String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName()));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is of type int.  It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getIntTypeDefAttribute(String      attributeName,
                                                    String      attributeDescription,
                                                    String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT.getName()));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is of type boolean.  It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getBooleanTypeDefAttribute(String      attributeName,
                                                        String      attributeDescription,
                                                        String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN.getName()));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }
    
    
    /**
     * Return an attribute with the supplied name and description that is of type date.  It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getDateTypeDefAttribute(String      attributeName,
                                                     String      attributeDescription,
                                                     String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE.getName()));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is of type long.  It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getLongTypeDefAttribute(String      attributeName,
                                                     String      attributeDescription,
                                                     String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG.getName()));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is of type long.  It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getFloatTypeDefAttribute(String      attributeName,
                                                      String      attributeDescription,
                                                      String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT.getName()));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is an array of strings.
     * It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getArrayStringTypeDefAttribute(String      attributeName,
                                                            String      attributeDescription,
                                                            String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getCollectionDef("array<string>"));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is an array of strings.
     * It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getArrayIntTypeDefAttribute(String      attributeName,
                                                         String      attributeDescription,
                                                         String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getCollectionDef("array<int>"));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is of a map from string to string.
     * It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getMapStringStringTypeDefAttribute(String      attributeName,
                                                                String      attributeDescription,
                                                                String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getCollectionDef("map<string,string>"));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is of a map from string to string.
     * It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getMapStringObjectTypeDefAttribute(String      attributeName,
                                                                String      attributeDescription,
                                                                String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getCollectionDef("map<string,object>"));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is of a map from string to boolean.
     * It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getMapStringBooleanTypeDefAttribute(String      attributeName,
                                                                 String      attributeDescription,
                                                                 String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getCollectionDef("map<string,boolean>"));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is of a map from string to long.
     * It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getMapStringLongTypeDefAttribute(String      attributeName,
                                                              String      attributeDescription,
                                                              String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getCollectionDef("map<string,long>"));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is of a map from string to double.
     * It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getMapStringDoubleTypeDefAttribute(String      attributeName,
                                                                String      attributeDescription,
                                                                String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getCollectionDef("map<string,double>"));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is of a map from string to date.
     * It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getMapStringDateTypeDefAttribute(String      attributeName,
                                                              String      attributeDescription,
                                                              String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getCollectionDef("map<string,date>"));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name and description that is of a map from string to integer.
     * It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getMapStringIntTypeDefAttribute(String      attributeName,
                                                             String      attributeDescription,
                                                             String      attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getCollectionDef("map<string,int>"));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name, type and description.  It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param propertyDescription details of the attribute
     * @return  TypeDefAttribute of type enum
     */
    public TypeDefAttribute  getEnumTypeDefAttribute(OpenMetadataProperty propertyDescription)
    {
        return this.getEnumTypeDefAttribute(propertyDescription.type,
                                            propertyDescription.name,
                                            propertyDescription.description,
                                            propertyDescription.descriptionGUID);
    }


    /**
     * Return an attribute with the supplied name, type and description.  It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param enumTypeName name of the enum type for this attribute
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type enum
     */
    public TypeDefAttribute  getEnumTypeDefAttribute(String     enumTypeName,
                                                     String     attributeName,
                                                     String     attributeDescription,
                                                     String     attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getEnumDef(enumTypeName));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Return an attribute with the supplied name, type and description.  It is set up to be optional,
     * indexable (useful for searches) but the value does not need to be unique.
     * These are the typical values used for most open metadata attribute.
     * They can be changed by the caller once the TypeDefAttribute is returned.
     *
     * @param enumArrayTypeName name of the enum type for this attribute
     * @param attributeName name of the attribute
     * @param attributeDescription short description for the attribute
     * @param attributeDescriptionGUID guid of the glossary term that describes this attribute.
     * @return  TypeDefAttribute of type string
     */
    public TypeDefAttribute  getArrayEnumTypeDefAttribute(String     enumArrayTypeName,
                                                          String     attributeName,
                                                          String     attributeDescription,
                                                          String     attributeDescriptionGUID)
    {
        TypeDefAttribute     attribute = new TypeDefAttribute();

        attribute.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        attribute.setAttributeName(attributeName);
        attribute.setAttributeDescription(attributeDescription);
        attribute.setAttributeDescriptionGUID(attributeDescriptionGUID);
        attribute.setAttributeType(this.archiveBuilder.getCollectionDef(enumArrayTypeName));
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setValuesMinCount(0);
        attribute.setValuesMaxCount(1);
        attribute.setIndexable(true);
        attribute.setUnique(false);
        attribute.setDefaultValue(null);
        attribute.setExternalStandardMappings(null);

        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);

        return attribute;
    }


    /**
     * Returns a basic RelationshipDef without any attributes or ends set up.
     * The caller is responsible for adding the attributes and ends definition.
     *
     * @param guid unique identifier for the relationship
     * @param name name of the relationship
     * @param superType Super type for this relationship (null for top-level)
     * @param description short default description of the relationship
     * @param descriptionGUID guid of the valid value that describes this relationship
     * @param propagationRule should classifications propagate over this relationship?
     * @return RelationshipDef with no ends defined.
     */
    public RelationshipDef getBasicRelationshipDef(String                        guid,
                                                   String                        name,
                                                   TypeDefLink                   superType,
                                                   String                        description,
                                                   String                        descriptionGUID,
                                                   ClassificationPropagationRule propagationRule)
    {
        RelationshipDef relationshipDef = new RelationshipDef();

        relationshipDef.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);

        /*
         * Set up the parameters supplied by the caller.
         */
        relationshipDef.setGUID(guid);
        relationshipDef.setName(name);
        relationshipDef.setSuperType(superType);
        relationshipDef.setDescription(description);
        relationshipDef.setDescriptionGUID(descriptionGUID);

        /*
         * Set up the defaults
         */
        relationshipDef.setOrigin(archiveGUID);
        relationshipDef.setCreatedBy(originatorName);
        relationshipDef.setCreateTime(creationDate);
        relationshipDef.setVersion(versionNumber);
        relationshipDef.setVersionName(versionName);

        /*
         * Set default valid instance statuses
         */
        TypeDef superTypeDef = null;
        if (superType != null)
        {
            superTypeDef = this.archiveBuilder.getTypeDefByName(superType.getName());
        }

        if (superTypeDef != null)
        {
            relationshipDef.setValidInstanceStatusList(superTypeDef.getValidInstanceStatusList());
            relationshipDef.setInitialStatus(superTypeDef.getInitialStatus());
        }
        else
        {
            /*
             * These are the standard valid instance statuses used by entities
             */
            ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();
            validInstanceStatusList.add(InstanceStatus.ACTIVE);
            validInstanceStatusList.add(InstanceStatus.DELETED);

            relationshipDef.setValidInstanceStatusList(validInstanceStatusList);
            relationshipDef.setInitialStatus(InstanceStatus.ACTIVE);
        }

        /*
         * Use the supplied propagation rule.
         */
        relationshipDef.setPropagationRule(propagationRule);

        relationshipDef.setStatus(TypeDefStatus.ACTIVE_TYPEDEF);

        return relationshipDef;
    }


    /**
     * Returns a basic RelationshipDef without any attributes or ends set up.
     * The caller is responsible for adding the attributes and ends definition.
     *
     * @param type type description for the relationship
     * @param superType Super type for this relationship (null for top-level)
     * @param propagationRule should classifications propagate over this relationship?
     * @return RelationshipDef with no ends defined.
     */
    public RelationshipDef getBasicRelationshipDef(OpenMetadataType              type,
                                                   TypeDefLink                   superType,
                                                   ClassificationPropagationRule propagationRule)
    {
        return this.getBasicRelationshipDef(type.typeGUID,
                                            type.typeName,
                                            superType,
                                            type.description,
                                            type.descriptionGUID,
                                            type.wikiURL,
                                            propagationRule);
    }


    /**
     * Returns a basic RelationshipDef without any attributes or ends set up.
     * The caller is responsible for adding the attributes and ends definition.
     *
     * @param guid unique identifier for the relationship
     * @param name name of the relationship
     * @param superType Super type for this relationship (null for top-level)
     * @param description short default description of the relationship
     * @param descriptionGUID guid of the valid value that describes this relationship
     * @param propagationRule should classifications propagate over this relationship?
     * @param descriptionWiki url to docs
     * @return RelationshipDef with no ends defined.
     */
    public RelationshipDef getBasicRelationshipDef(String                        guid,
                                                   String                        name,
                                                   TypeDefLink                   superType,
                                                   String                        description,
                                                   String                        descriptionGUID,
                                                   String                        descriptionWiki,
                                                   ClassificationPropagationRule propagationRule)
    {
        RelationshipDef relationshipDef = new RelationshipDef();

        relationshipDef.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);

        /*
         * Set up the parameters supplied by the caller.
         */
        relationshipDef.setGUID(guid);
        relationshipDef.setName(name);
        relationshipDef.setSuperType(superType);
        relationshipDef.setDescription(description);
        relationshipDef.setDescriptionGUID(descriptionGUID);
        relationshipDef.setDescriptionWiki(descriptionWiki);

        /*
         * Set up the defaults
         */
        relationshipDef.setOrigin(archiveGUID);
        relationshipDef.setCreatedBy(originatorName);
        relationshipDef.setCreateTime(creationDate);
        relationshipDef.setVersion(versionNumber);
        relationshipDef.setVersionName(versionName);

        /*
         * Set default valid instance statuses
         */
        TypeDef superTypeDef = null;
        if (superType != null)
        {
            superTypeDef = this.archiveBuilder.getTypeDefByName(superType.getName());
        }

        if (superTypeDef != null)
        {
            relationshipDef.setValidInstanceStatusList(superTypeDef.getValidInstanceStatusList());
            relationshipDef.setInitialStatus(superTypeDef.getInitialStatus());
        }
        else
        {
            /*
             * These are the standard valid instance statuses used by entities
             */
            ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();
            validInstanceStatusList.add(InstanceStatus.ACTIVE);
            validInstanceStatusList.add(InstanceStatus.DELETED);

            relationshipDef.setValidInstanceStatusList(validInstanceStatusList);
            relationshipDef.setInitialStatus(InstanceStatus.ACTIVE);
        }

        /*
         * Use the supplied propagation rule.
         */
        relationshipDef.setPropagationRule(propagationRule);

        relationshipDef.setStatus(TypeDefStatus.ACTIVE_TYPEDEF);

        return relationshipDef;
    }


    /**
     * Returns a RelationshipEndDef object that sets up details of an entity at one end of a relationship.
     *
     * @param entityType details of the type of entity connected to this end.
     * @param attributeName name of the attribute that the entity at the other end uses to refer to this entity.
     * @param attributeDescription description of this attribute
     * @param attributeDescriptionGUID unique identifier of the glossary term describing this attribute.
     * @param attributeCardinality cardinality of this end of the relationship.
     * @return the definition of one end of a Relationship.
     */
    public RelationshipEndDef  getRelationshipEndDef(TypeDefLink                entityType,
                                                     String                     attributeName,
                                                     String                     attributeDescription,
                                                     String                     attributeDescriptionGUID,
                                                     RelationshipEndCardinality attributeCardinality)
    {
        RelationshipEndDef  relationshipEndDef = new RelationshipEndDef();

        relationshipEndDef.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
        relationshipEndDef.setEntityType(entityType);
        relationshipEndDef.setAttributeName(attributeName);
        relationshipEndDef.setAttributeDescription(attributeDescription);
        relationshipEndDef.setAttributeDescriptionGUID(attributeDescriptionGUID);
        relationshipEndDef.setAttributeCardinality(attributeCardinality);

        return relationshipEndDef;
    }


    /**
     * Returns a basic ClassificationDef without any attributes.   The caller is responsible for adding the
     * attribute definitions.
     *
     * @param guid unique identifier for the classification
     * @param name name of the classification
     * @param superType Super type for this classification (null for top-level)
     * @param description short description of the classification
     * @param descriptionGUID unique identifier of the valid value that describes this classification.
     * @param validEntityDef which entities can this classification be linked to.
     * @param propagatable can the classification propagate over relationships?
     * @return ClassificationDef with no attributes defined.
     */
    public ClassificationDef getClassificationDef(String                        guid,
                                                  String                        name,
                                                  TypeDefLink                   superType,
                                                  String                        description,
                                                  String                        descriptionGUID,
                                                  TypeDefLink                   validEntityDef,
                                                  boolean                       propagatable)
    {
        /*
         * Set up the supplied validEntityType as an array and call the method to create the ClassificationDef.
         */
        ArrayList<TypeDefLink>   validEntityDefs = new ArrayList<>();
        validEntityDefs.add(validEntityDef);

        return this.getClassificationDef(guid, name, superType, description, descriptionGUID, validEntityDefs, propagatable);
    }


    /**
     * Returns a basic ClassificationDef without any attributes.   The caller is responsible for adding the
     * attribute definitions.
     *
     * @param type type description of the classification
     * @param superType Super type for this classification (null for top-level)
     * @param validEntityDef which entities can this classification be linked to.
     * @param propagatable can the classification propagate over relationships?
     * @return ClassificationDef with no attributes defined.
     */
    public ClassificationDef getClassificationDef(OpenMetadataType              type,
                                                  TypeDefLink                   superType,
                                                  TypeDefLink                   validEntityDef,
                                                  boolean                       propagatable)
    {
        /*
         * Set up the supplied validEntityType as an array and call the method to create the ClassificationDef.
         */
        ArrayList<TypeDefLink>   validEntityDefs = new ArrayList<>();
        validEntityDefs.add(validEntityDef);

        return this.getClassificationDef(type.typeGUID,
                                         type.typeName,
                                         superType,
                                         type.description,
                                         type.descriptionGUID,
                                         type.wikiURL,
                                         validEntityDefs,
                                         propagatable);
    }


    /**
     * Returns a basic ClassificationDef without any attributes.   The caller is responsible for adding the
     * attribute definitions.
     *
     * @param guid unique identifier for the classification
     * @param name name of the classification
     * @param superType Super type for this classification (null for top-level)
     * @param description short description of the classification
     * @param descriptionGUID unique identifier of the valid value that describes this classification
     * @param descriptionWiki url to wiki
     * @param validEntityDef which entities can this classification be linked to.
     * @param propagatable can the classification propagate over relationships?
     * @return ClassificationDef with no attributes defined.
     */
    public ClassificationDef getClassificationDef(String                        guid,
                                                  String                        name,
                                                  TypeDefLink                   superType,
                                                  String                        description,
                                                  String                        descriptionGUID,
                                                  String                        descriptionWiki,
                                                  TypeDefLink                   validEntityDef,
                                                  boolean                       propagatable)
    {
        /*
         * Set up the supplied validEntityType as an array and call the method to create the ClassificationDef.
         */
        ArrayList<TypeDefLink>   validEntityDefs = new ArrayList<>();
        validEntityDefs.add(validEntityDef);

        return this.getClassificationDef(guid, name, superType, description, descriptionGUID, descriptionWiki, validEntityDefs, propagatable);
    }


    /**
     * Returns a basic ClassificationDef without any attributes.   The caller is responsible for adding the
     * attribute definitions.
     *
     * @param guid unique identifier for the classification
     * @param name name of the classification
     * @param superType Super type for this classification (null for top-level)
     * @param description short description of the classification
     * @param descriptionGUID unique identifier of the valid value that describes this classification.
     * @param validEntityDefs which entities can this classification be linked to.
     * @param propagatable can the classification propagate over relationships?
     * @return ClassificationDef with no attributes defined.
     */
    public ClassificationDef getClassificationDef(String                        guid,
                                                  String                        name,
                                                  TypeDefLink                   superType,
                                                  String                        description,
                                                  String                        descriptionGUID,
                                                  List<TypeDefLink>             validEntityDefs,
                                                  boolean                       propagatable)
    {
        return getClassificationDef(guid, name, superType, description, descriptionGUID, null, validEntityDefs, propagatable);
    }


    /**
     * Returns a basic ClassificationDef without any attributes.   The caller is responsible for adding the
     * attribute definitions.
     *
     * @param guid unique identifier for the classification
     * @param name name of the classification
     * @param superType Super type for this classification (null for top-level)
     * @param description short description of the classification
     * @param descriptionGUID unique identifier of the valid value that describes this classification
     * @param descriptionWiki url to wiki
     * @param validEntityDefs which entities can this classification be linked to.
     * @param propagatable can the classification propagate over relationships?
     * @return ClassificationDef with no attributes defined.
     */
    public ClassificationDef getClassificationDef(String                        guid,
                                                  String                        name,
                                                  TypeDefLink                   superType,
                                                  String                        description,
                                                  String                        descriptionGUID,
                                                  String                        descriptionWiki,
                                                  List<TypeDefLink>             validEntityDefs,
                                                  boolean                       propagatable)
    {
        ClassificationDef classificationDef = new ClassificationDef();

        classificationDef.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);

        /*
         * Set up the parameters supplied by the caller.
         */
        classificationDef.setGUID(guid);
        classificationDef.setName(name);
        classificationDef.setSuperType(superType);
        classificationDef.setDescription(description);
        classificationDef.setDescriptionGUID(descriptionGUID);
        classificationDef.setDescriptionWiki(descriptionWiki);

        /*
         * Set up the defaults
         */
        classificationDef.setOrigin(archiveGUID);
        classificationDef.setCreatedBy(originatorName);
        classificationDef.setCreateTime(creationDate);
        classificationDef.setVersion(versionNumber);
        classificationDef.setVersionName(versionName);

        /*
         * Set default valid instance statuses
         */
        TypeDef superTypeDef = null;
        if (superType != null)
        {
            superTypeDef = this.archiveBuilder.getTypeDefByName(superType.getName());
        }

        if (superTypeDef != null)
        {
            classificationDef.setValidInstanceStatusList(superTypeDef.getValidInstanceStatusList());
            classificationDef.setInitialStatus(superTypeDef.getInitialStatus());
        }
        else
        {
            /*
             * These are the standard valid instance statuses used by entities
             */
            ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();
            validInstanceStatusList.add(InstanceStatus.ACTIVE);
            validInstanceStatusList.add(InstanceStatus.DELETED);

            classificationDef.setValidInstanceStatusList(validInstanceStatusList);
            classificationDef.setInitialStatus(InstanceStatus.ACTIVE);
        }


        /*
         * Set up the supplied validEntityTypes and propagatable flag.
         */
        classificationDef.setValidEntityDefs(validEntityDefs);
        classificationDef.setPropagatable(propagatable);

        classificationDef.setStatus(TypeDefStatus.ACTIVE_TYPEDEF);

        return classificationDef;
    }


    /*
     * ----------------------------------------------
     * Working with instances
     * ----------------------------------------------
     */

    /**
     * Return the list of valid properties for the instance
     *
     * @param definedAttributes list of attributes defined for the typedef
     * @param currentList current list of properties extracted from the subtypes
     * @return accumulated list of properties.
     */
    private List<String>  getPropertiesList(List<TypeDefAttribute>  definedAttributes,
                                            List<String>            currentList)
    {
        List<String>   newList = currentList;

        if (newList == null)
        {
            newList = new ArrayList<>();
        }

        if (definedAttributes != null)
        {
            for (TypeDefAttribute  attribute : definedAttributes)
            {
                if (attribute != null)
                {
                    newList.add(attribute.getAttributeName());
                }
            }
        }

        if (newList.isEmpty())
        {
            return null;
        }

        return newList;
    }


    /**
     * Return an instance properties that only contains the properties that uniquely identify the entity.
     * This is used when creating entity proxies.
     *
     * @param typeName name of instance's type
     * @param allProperties all the instance's properties
     * @return just the unique properties
     */
    private InstanceProperties getUniqueProperties(String              typeName,
                                                   InstanceProperties  allProperties)
    {
        InstanceProperties uniqueProperties = null;

        if (allProperties != null)
        {
            uniqueProperties = new InstanceProperties();

            uniqueProperties.setEffectiveFromTime(allProperties.getEffectiveFromTime());
            uniqueProperties.setEffectiveToTime(allProperties.getEffectiveToTime());

            /*
             * Walk the type hierarchy to pick up the list of unique properties. eg qualifiedName in Referenceable.
             */
            TypeDef typeDef = archiveBuilder.getTypeDefByName(typeName);

            if (typeDef != null)
            {
                /*
                 * Determine the list of names of unique properties
                 */
                List<String> uniquePropertyNames = this.getUniquePropertiesList(typeDef.getPropertiesDefinition(), null);
                TypeDef superType = null;

                if (typeDef.getSuperType() != null)
                {
                    superType = archiveBuilder.getTypeDefByName(typeDef.getSuperType().getName());
                }

                while (superType != null)
                {
                    uniquePropertyNames = this.getUniquePropertiesList(superType.getPropertiesDefinition(),
                                                                       uniquePropertyNames);

                    if (superType.getSuperType() != null)
                    {
                        superType = archiveBuilder.getTypeDefByName(superType.getSuperType().getName());
                    }
                    else
                    {
                        superType = null;
                    }
                }

                if (uniquePropertyNames != null)
                {
                    /*
                     * Create a new InstanceProperties object containing only the unique properties.
                     */
                    Map<String, InstancePropertyValue> allInstancePropertiesMap = allProperties.getInstanceProperties();
                    Map<String, InstancePropertyValue> uniqueInstancePropertiesMap = new HashMap<>();

                    if (allInstancePropertiesMap != null)
                    {
                        for (String propertyName : allInstancePropertiesMap.keySet())
                        {
                            if (propertyName != null)
                            {
                                if (uniquePropertyNames.contains(propertyName))
                                {
                                    uniqueInstancePropertiesMap.put(propertyName, allInstancePropertiesMap.get(propertyName));
                                }
                            }
                        }
                    }

                    if (! uniqueInstancePropertiesMap.isEmpty())
                    {
                        uniqueProperties.setInstanceProperties(uniqueInstancePropertiesMap);
                    }
                }
            }
        }

        return uniqueProperties;
    }


    /**
     * Returns the instance type object with the fields that can be directly extracted from the
     * supplied type definition.  This leaves the super types and valid properties list that needs
     * to be able to loop through the super types.
     *
     * @param typeDef supplied typeDef.
     * @return new instance type object
     */
    private InstanceType getInstanceTypeHeader(TypeDef  typeDef)
    {
        InstanceType instanceType = null;

        if (typeDef != null)
        {
            instanceType = new InstanceType();

            instanceType.setHeaderVersion(InstanceType.CURRENT_INSTANCE_TYPE_HEADER_VERSION);
            instanceType.setTypeDefCategory(typeDef.getCategory());
            instanceType.setTypeDefGUID(typeDef.getGUID());
            instanceType.setTypeDefName(typeDef.getName());
            instanceType.setTypeDefVersion(typeDef.getVersion());
        }

        return instanceType;
    }


    /**
     * Return the filled out instance type for the new entity.
     *
     * @param typeDefName  name of requested type.
     * @return new instance type.
     */
    private InstanceType getInstanceType(String typeDefName)
    {
        TypeDef typeDef = archiveBuilder.getTypeDefByName(typeDefName);

        return this.getInstanceTypeHeader(typeDef);
    }


    /**
     * Set up the common fields for an entity or a relationship.
     *
     * @param instanceAuditHeader instance object to fill
     * @param type type of the object
     * @param status instance status
     */
    private void setInstanceAuditHeader(InstanceAuditHeader  instanceAuditHeader,
                                        InstanceType         type,
                                        InstanceStatus       status)
    {
        instanceAuditHeader.setHeaderVersion(InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION);
        instanceAuditHeader.setCreatedBy(originatorName);
        if (creationDate == null)
        {
            instanceAuditHeader.setCreateTime(new Date());
        }
        else
        {
            instanceAuditHeader.setCreateTime(creationDate);
        }
        instanceAuditHeader.setInstanceLicense(license);
        instanceAuditHeader.setInstanceProvenanceType(instanceProvenanceType);
        instanceAuditHeader.setMaintainedBy(null);
        instanceAuditHeader.setMetadataCollectionId(archiveGUID);
        instanceAuditHeader.setMetadataCollectionName(archiveName);
        instanceAuditHeader.setReplicatedBy(null);
        instanceAuditHeader.setStatus(InstanceStatus.ACTIVE);
        instanceAuditHeader.setStatusOnDelete(null);
        instanceAuditHeader.setType(type);
        instanceAuditHeader.setVersion(versionNumber);
        if (versionNumber == 1L)
        {
            instanceAuditHeader.setUpdatedBy(null);
            instanceAuditHeader.setUpdateTime(null);
        }
        else
        {
            instanceAuditHeader.setUpdatedBy(originatorName);
            instanceAuditHeader.setUpdateTime(new Date());
        }

        instanceAuditHeader.setStatus(status);
    }


    /**
     * Set up the common fields for an entity or a relationship.
     *
     * @param instanceHeader instance object to fill
     * @param typeName type name of the object
     * @param guid unique identifier
     * @param status instance status
     */
    private void setInstanceHeader(InstanceHeader  instanceHeader,
                                   String          typeName,
                                   String          guid,
                                   InstanceStatus  status)
    {
        InstanceType  type = this.getInstanceType(typeName);

        setInstanceAuditHeader(instanceHeader, type, status);

        instanceHeader.setGUID(guid);
        instanceHeader.setInstanceURL(null);
    }


    /**
     * Return a specific entity detail instance.
     *
     * @param typeName type name of the entity
     * @param guid unique identifier of the entity
     * @param properties properties (attributes) for the entity
     * @param status instance status
     * @param classifications list of classifications
     * @return assembled entity
     */
    public EntityDetail getEntityDetail(String                typeName,
                                        String                guid,
                                        InstanceProperties    properties,
                                        InstanceStatus        status,
                                        List<Classification>  classifications)
    {
        EntityDetail  entityDetail = new EntityDetail();

        this.setInstanceHeader(entityDetail, typeName, guid, status);
        entityDetail.setProperties(properties);
        entityDetail.setClassifications(classifications);

        return entityDetail;
    }


    /**
     * Return a specific relationship instance.
     *
     * @param typeName type name of the relationship
     * @param guid unique identifier of the relationship
     * @param properties properties (attributes) for the relationship
     * @param status instance status
     * @param end1 relationship end 1
     * @param end2 relationship end 2
     * @return relationship instance
     */
    public Relationship getRelationship(String                typeName,
                                        String                guid,
                                        InstanceProperties    properties,
                                        InstanceStatus        status,
                                        EntityProxy           end1,
                                        EntityProxy           end2)
    {
        Relationship  relationship = new Relationship();

        this.setInstanceHeader(relationship, typeName, guid, status);
        relationship.setProperties(properties);
        relationship.setEntityOneProxy(end1);
        relationship.setEntityTwoProxy(end2);

        return relationship;
    }


    /**
     * Return a specific classification instance.
     *
     * @param typeName type name of the classification
     * @param properties properties (attributes) for the classification
     * @param status instance status
     * @return classification instance
     */
    public Classification getClassification(String                typeName,
                                            InstanceProperties    properties,
                                            InstanceStatus        status)
    {
        Classification  classification = new Classification();
        InstanceType    type = this.getInstanceType(typeName);

        classification.setName(typeName);
        this.setInstanceAuditHeader(classification, type, status);
        classification.setProperties(properties);

        return classification;
    }


    /**
     * Return a specific entity proxy instance.
     *
     * @param typeName type name of the entity
     * @param guid unique identifier of the entity
     * @param properties unique properties (attributes) for the entity
     * @param status instance status
     * @param classifications list of classifications
     * @return classification instance
     */
    public EntityProxy getEntityProxy(String                typeName,
                                      String                guid,
                                      InstanceProperties    properties,
                                      InstanceStatus        status,
                                      List<Classification>  classifications)
    {
        EntityProxy  entityProxy = new EntityProxy();

        this.setInstanceHeader(entityProxy, typeName, guid, status);
        entityProxy.setUniqueProperties(properties);
        entityProxy.setClassifications(classifications);

        return entityProxy;
    }


    /**
     * Build an entity proxy from an entity.
     *
     * @param entity entity to use as a template
     * @return new entity proxy.
     */
    public EntityProxy getEntityProxy(EntityDetail entity)
    {
        EntityProxy  entityProxy = new EntityProxy(entity);
        String       typeName = entity.getType().getTypeDefName();

        entityProxy.setUniqueProperties(this.getUniqueProperties(typeName, entity.getProperties()));
        entityProxy.setClassifications(entity.getClassifications());

        return entityProxy;
    }


    /**
     * Build a classification entity extension that is used to pass a classification in an archive.
     *
     * @param entity entity proxy to shoe where the classification should be attached
     * @param classification classification to attach
     * @return new object
     */
    public ClassificationEntityExtension getClassificationEntityExtension(EntityProxy    entity,
                                                                          Classification classification)
    {
        ClassificationEntityExtension classificationEntityExtension = new ClassificationEntityExtension();

        classificationEntityExtension.setHeaderVersion(ClassificationEntityExtension.CURRENT_CLASSIFICATION_EXT_HEADER_VERSION);
        classificationEntityExtension.setEntityToClassify(entity);
        classificationEntityExtension.setClassification(classification);

        return classificationEntityExtension;
    }


    /**
     * Extract the enum value definition based on the ordinal.
     *
     * @param enumTypeName name of the enum type
     * @param ordinal ordinal number for the enum value
     * @return enum element def or its default value
     */
    public EnumElementDef getEnumElement(String enumTypeName,
                                         int    ordinal)
    {
        EnumDef enumDef = archiveBuilder.getEnumDef(enumTypeName);

        for (EnumElementDef enumElementDef : enumDef.getElementDefs())
        {
            if (enumElementDef.getOrdinal() == ordinal)
            {
                return enumElementDef;
            }
        }

        return enumDef.getDefaultValue();
    }
}
