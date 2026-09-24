/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworkservices.omf.ffdc.OMFServicesErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * OMRSTypeDefConverter turns the open metadata framework's description of a type definition into the repository
 * services' TypeDef, TypeDefPatch and EnumDef - the reverse of the conversion used to retrieve types.
 * <br><br>
 * The references in a type definition - its supertype, the types of its attributes, the entity types at the ends
 * of a relationship and so on - are resolved by name against the types already known, so the caller does not need
 * to supply their unique identifiers, and an unknown type is reported here with the name of the parameter that
 * refers to it.  The repository checks the result as a whole against the rest of the type system when it is added.
 */
public class OMRSTypeDefConverter
{
    private static final long   firstVersion     = 1L;
    private static final String versionNameSuffix = ".0";

    private final OMRSRepositoryHelper repositoryHelper;
    private final String               serviceName;


    /**
     * Constructor
     *
     * @param repositoryHelper helper holding the known types
     * @param serviceName name of the calling service
     */
    public OMRSTypeDefConverter(OMRSRepositoryHelper repositoryHelper,
                                String               serviceName)
    {
        this.repositoryHelper = repositoryHelper;
        this.serviceName      = serviceName;
    }


    /**
     * Return the repository services' version of a new type definition.  A unique identifier is generated if none
     * is supplied, the version defaults to 1, and the valid and initial statuses default to those of the supertype
     * (or to ACTIVE and DELETED for a type with no supertype) so that instances of the type can be created.
     *
     * @param openMetadataTypeDef type definition from the caller
     * @param methodName calling method
     * @return new TypeDef
     * @throws InvalidParameterException the type definition refers to a type that is not known or uses a value
     *                                   the repository cannot represent
     */
    public TypeDef getNewTypeDef(OpenMetadataTypeDef openMetadataTypeDef,
                                 String              methodName) throws InvalidParameterException
    {
        final String parameterName = "newTypeDef";

        TypeDef typeDef;

        if (openMetadataTypeDef instanceof OpenMetadataEntityDef)
        {
            typeDef = new EntityDef();
        }
        else if (openMetadataTypeDef instanceof OpenMetadataRelationshipDef openMetadataRelationshipDef)
        {
            RelationshipDef relationshipDef = new RelationshipDef();

            relationshipDef.setPropagationRule(this.getEnum(ClassificationPropagationRule.class,
                                                            openMetadataRelationshipDef.getPropagationRule(),
                                                            ClassificationPropagationRule.NONE,
                                                            parameterName + ".propagationRule",
                                                            methodName));
            relationshipDef.setEndDef1(this.getRelationshipEndDef(openMetadataRelationshipDef.getEndDef1(), parameterName + ".endDef1", methodName));
            relationshipDef.setEndDef2(this.getRelationshipEndDef(openMetadataRelationshipDef.getEndDef2(), parameterName + ".endDef2", methodName));
            relationshipDef.setMultiLink(openMetadataRelationshipDef.getRelationshipCategory() == OpenMetadataRelationshipCategory.MULTI_LINK);

            typeDef = relationshipDef;
        }
        else if (openMetadataTypeDef instanceof OpenMetadataClassificationDef openMetadataClassificationDef)
        {
            ClassificationDef classificationDef = new ClassificationDef();

            classificationDef.setValidEntityDefs(this.getTypeDefLinks(openMetadataClassificationDef.getValidEntityDefs(),
                                                                      parameterName + ".validEntityDefs",
                                                                      methodName));
            classificationDef.setPropagatable(openMetadataClassificationDef.isPropagatable());

            typeDef = classificationDef;
        }
        else
        {
            throw new InvalidParameterException(OMFServicesErrorCode.UNSUPPORTED_TYPE_DEF_CLASS.getMessageDefinition(methodName,
                                                                                                                     openMetadataTypeDef.getClass().getName()),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        if (openMetadataTypeDef.getGUID() == null)
        {
            typeDef.setGUID(UUID.randomUUID().toString());
        }
        else
        {
            typeDef.setGUID(openMetadataTypeDef.getGUID());
        }

        long version = openMetadataTypeDef.getVersion();

        if (version <= 0)
        {
            version = firstVersion;
        }

        typeDef.setName(openMetadataTypeDef.getName());
        typeDef.setStatus(this.getEnum(TypeDefStatus.class,
                                       openMetadataTypeDef.getStatus(),
                                       TypeDefStatus.ACTIVE_TYPEDEF,
                                       parameterName + ".status",
                                       methodName));
        typeDef.setReplacedByTypeGUID(openMetadataTypeDef.getReplacedByTypeGUID());
        typeDef.setReplacedByTypeName(openMetadataTypeDef.getReplacedByTypeName());
        typeDef.setVersion(version);
        typeDef.setVersionName(this.getVersionName(openMetadataTypeDef.getVersionName(), version));
        typeDef.setSuperType(this.getTypeDefLink(openMetadataTypeDef.getSuperType(), parameterName + ".superType", methodName));
        typeDef.setDescription(openMetadataTypeDef.getDescription());
        typeDef.setDescriptionGUID(openMetadataTypeDef.getDescriptionGUID());
        typeDef.setDescriptionWiki(openMetadataTypeDef.getDescriptionWiki());
        typeDef.setCreatedBy(openMetadataTypeDef.getCreatedBy());
        typeDef.setCreateTime(openMetadataTypeDef.getCreateTime());
        typeDef.setOptions(openMetadataTypeDef.getOptions());
        typeDef.setExternalStandardMappings(this.getExternalStandardMappings(openMetadataTypeDef.getExternalStandardTypeMappings()));
        typeDef.setPropertiesDefinition(this.getTypeDefAttributes(openMetadataTypeDef.getAttributeDefinitions(),
                                                                  parameterName + ".attributeDefinitions",
                                                                  methodName));

        List<InstanceStatus> validInstanceStatusList = this.getInstanceStatuses(openMetadataTypeDef.getValidElementStatusList(),
                                                                                parameterName + ".validElementStatusList",
                                                                                methodName);
        InstanceStatus       initialStatus           = this.getEnum(InstanceStatus.class,
                                                                    openMetadataTypeDef.getInitialStatus(),
                                                                    null,
                                                                    parameterName + ".initialStatus",
                                                                    methodName);

        /*
         * The statuses default to those of the supertype, which is how the open metadata archives build types.
         */
        TypeDef superTypeDef = null;

        if (typeDef.getSuperType() != null)
        {
            superTypeDef = repositoryHelper.getTypeDefByName(serviceName, typeDef.getSuperType().getName());
        }

        if (validInstanceStatusList == null)
        {
            if ((superTypeDef != null) && (superTypeDef.getValidInstanceStatusList() != null))
            {
                validInstanceStatusList = superTypeDef.getValidInstanceStatusList();
            }
            else
            {
                validInstanceStatusList = new ArrayList<>();

                validInstanceStatusList.add(InstanceStatus.ACTIVE);
                validInstanceStatusList.add(InstanceStatus.DELETED);
            }
        }

        if (initialStatus == null)
        {
            if ((superTypeDef != null) && (superTypeDef.getInitialStatus() != null))
            {
                initialStatus = superTypeDef.getInitialStatus();
            }
            else
            {
                initialStatus = InstanceStatus.ACTIVE;
            }
        }

        typeDef.setValidInstanceStatusList(validInstanceStatusList);
        typeDef.setInitialStatus(initialStatus);

        return typeDef;
    }


    /**
     * Return the repository services' version of a patch to a type definition.  The version the patch creates
     * defaults to the one after the version it applies to.
     *
     * @param openMetadataTypeDefPatch patch from the caller
     * @param methodName calling method
     * @return TypeDefPatch
     * @throws InvalidParameterException the patch refers to a type that is not known or uses a value the
     *                                   repository cannot represent
     */
    public TypeDefPatch getTypeDefPatch(OpenMetadataTypeDefPatch openMetadataTypeDefPatch,
                                        String                   methodName) throws InvalidParameterException
    {
        final String parameterName = "typeDefPatch";

        TypeDefPatch typeDefPatch = new TypeDefPatch();

        long updateToVersion = openMetadataTypeDefPatch.getUpdateToVersion();

        if (updateToVersion <= 0)
        {
            updateToVersion = openMetadataTypeDefPatch.getApplyToVersion() + 1;
        }

        typeDefPatch.setTypeDefGUID(openMetadataTypeDefPatch.getTypeDefGUID());
        typeDefPatch.setTypeDefName(openMetadataTypeDefPatch.getTypeDefName());
        typeDefPatch.setApplyToVersion(openMetadataTypeDefPatch.getApplyToVersion());
        typeDefPatch.setUpdateToVersion(updateToVersion);
        typeDefPatch.setNewVersionName(this.getVersionName(openMetadataTypeDefPatch.getNewVersionName(), updateToVersion));
        typeDefPatch.setTypeDefStatus(this.getEnum(TypeDefStatus.class,
                                                   openMetadataTypeDefPatch.getTypeDefStatus(),
                                                   null,
                                                   parameterName + ".typeDefStatus",
                                                   methodName));
        typeDefPatch.setDescription(openMetadataTypeDefPatch.getDescription());
        typeDefPatch.setDescriptionGUID(openMetadataTypeDefPatch.getDescriptionGUID());
        typeDefPatch.setSuperType(this.getTypeDefLink(openMetadataTypeDefPatch.getSuperType(), parameterName + ".superType", methodName));
        typeDefPatch.setPropertyDefinitions(this.getTypeDefAttributes(openMetadataTypeDefPatch.getAttributeDefinitions(),
                                                                      parameterName + ".attributeDefinitions",
                                                                      methodName));
        typeDefPatch.setTypeDefOptions(openMetadataTypeDefPatch.getOptions());
        typeDefPatch.setExternalStandardMappings(this.getExternalStandardMappings(openMetadataTypeDefPatch.getExternalStandardTypeMappings()));
        typeDefPatch.setValidInstanceStatusList(this.getInstanceStatuses(openMetadataTypeDefPatch.getValidElementStatusList(),
                                                                         parameterName + ".validElementStatusList",
                                                                         methodName));
        typeDefPatch.setInitialStatus(this.getEnum(InstanceStatus.class,
                                                   openMetadataTypeDefPatch.getInitialStatus(),
                                                   null,
                                                   parameterName + ".initialStatus",
                                                   methodName));
        typeDefPatch.setValidEntityDefs(this.getTypeDefLinks(openMetadataTypeDefPatch.getValidEntityDefs(),
                                                             parameterName + ".validEntityDefs",
                                                             methodName));
        typeDefPatch.setEndDef1(this.getRelationshipEndDef(openMetadataTypeDefPatch.getEndDef1(), parameterName + ".endDef1", methodName));
        typeDefPatch.setEndDef2(this.getRelationshipEndDef(openMetadataTypeDefPatch.getEndDef2(), parameterName + ".endDef2", methodName));

        if (openMetadataTypeDefPatch.getRelationshipCategory() != null)
        {
            typeDefPatch.setUpdateMultiLink(true);
            typeDefPatch.setMultiLink(openMetadataTypeDefPatch.getRelationshipCategory() == OpenMetadataRelationshipCategory.MULTI_LINK);
        }

        return typeDefPatch;
    }


    /**
     * Return the repository services' version of a new enum definition.  A unique identifier is generated if none
     * is supplied and the version defaults to 1.
     *
     * @param openMetadataEnumDef enum definition from the caller
     * @return new EnumDef
     */
    public EnumDef getNewEnumDef(OpenMetadataEnumDef openMetadataEnumDef)
    {
        EnumDef enumDef = new EnumDef();

        if (openMetadataEnumDef.getGUID() == null)
        {
            enumDef.setGUID(UUID.randomUUID().toString());
        }
        else
        {
            enumDef.setGUID(openMetadataEnumDef.getGUID());
        }

        long version = openMetadataEnumDef.getVersion();

        if (version <= 0)
        {
            version = firstVersion;
        }

        enumDef.setName(openMetadataEnumDef.getName());
        enumDef.setVersion(version);
        enumDef.setVersionName(this.getVersionName(openMetadataEnumDef.getVersionName(), version));
        enumDef.setDescription(openMetadataEnumDef.getDescription());
        enumDef.setDescriptionGUID(openMetadataEnumDef.getDescriptionGUID());

        if (openMetadataEnumDef.getElementDefs() != null)
        {
            List<EnumElementDef> elementDefs = new ArrayList<>();

            for (OpenMetadataEnumElementDef openMetadataEnumElementDef : openMetadataEnumDef.getElementDefs())
            {
                elementDefs.add(this.getEnumElementDef(openMetadataEnumElementDef));
            }

            enumDef.setElementDefs(elementDefs);
        }

        if (openMetadataEnumDef.getDefaultValue() != null)
        {
            enumDef.setDefaultValue(this.getEnumElementDef(openMetadataEnumDef.getDefaultValue()));
        }

        return enumDef;
    }


    /**
     * Return the repository services' version of a valid value of an enum.
     *
     * @param openMetadataEnumElementDef valid value from the caller - may be null
     * @return EnumElementDef or null
     */
    private EnumElementDef getEnumElementDef(OpenMetadataEnumElementDef openMetadataEnumElementDef)
    {
        if (openMetadataEnumElementDef == null)
        {
            return null;
        }

        EnumElementDef enumElementDef = new EnumElementDef();

        enumElementDef.setOrdinal(openMetadataEnumElementDef.getOrdinal());
        enumElementDef.setValue(openMetadataEnumElementDef.getValue());
        enumElementDef.setDescription(openMetadataEnumElementDef.getDescription());
        enumElementDef.setDescriptionGUID(openMetadataEnumElementDef.getDescriptionGUID());

        return enumElementDef;
    }


    /**
     * Return the version name supplied, or one made from the version number.
     *
     * @param versionName supplied version name - may be null
     * @param version version number
     * @return version name
     */
    private String getVersionName(String versionName,
                                  long   version)
    {
        if (versionName == null)
        {
            return version + versionNameSuffix;
        }

        return versionName;
    }


    /**
     * Return a link to a known type.  The type is found by name, or by unique identifier if no name is supplied.
     * The link is built from the known type so that its identifiers are consistent.
     *
     * @param openMetadataTypeDefLink link from the caller - may be null
     * @param parameterName name of the parameter holding the link
     * @param methodName calling method
     * @return link or null
     * @throws InvalidParameterException the type is not known
     */
    private TypeDefLink getTypeDefLink(OpenMetadataTypeDefLink openMetadataTypeDefLink,
                                       String                  parameterName,
                                       String                  methodName) throws InvalidParameterException
    {
        if (openMetadataTypeDefLink == null)
        {
            return null;
        }

        TypeDef knownTypeDef = null;

        if (openMetadataTypeDefLink.getName() != null)
        {
            knownTypeDef = repositoryHelper.getTypeDefByName(serviceName, openMetadataTypeDefLink.getName());
        }
        else if (openMetadataTypeDefLink.getGUID() != null)
        {
            for (TypeDef typeDef : repositoryHelper.getKnownTypeDefs())
            {
                if ((typeDef != null) && (openMetadataTypeDefLink.getGUID().equals(typeDef.getGUID())))
                {
                    knownTypeDef = typeDef;
                    break;
                }
            }
        }

        if (knownTypeDef == null)
        {
            String typeIdentifier = openMetadataTypeDefLink.getName();

            if (typeIdentifier == null)
            {
                typeIdentifier = openMetadataTypeDefLink.getGUID();
            }

            throw new InvalidParameterException(OMFServicesErrorCode.UNKNOWN_TYPE_REFERENCE.getMessageDefinition("linked",
                                                                                                                 typeIdentifier,
                                                                                                                 parameterName,
                                                                                                                 methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        /*
         * The guid supplied by the caller is kept so that the repository can report a mismatch.
         */
        TypeDefLink typeDefLink = new TypeDefLink(knownTypeDef);

        if (openMetadataTypeDefLink.getGUID() != null)
        {
            typeDefLink.setGUID(openMetadataTypeDefLink.getGUID());
        }

        return typeDefLink;
    }


    /**
     * Return a list of links to known types.
     *
     * @param openMetadataTypeDefLinks links from the caller - may be null
     * @param parameterName name of the parameter holding the links
     * @param methodName calling method
     * @return list of links or null
     * @throws InvalidParameterException one of the types is not known
     */
    private List<TypeDefLink> getTypeDefLinks(List<OpenMetadataTypeDefLink> openMetadataTypeDefLinks,
                                              String                        parameterName,
                                              String                        methodName) throws InvalidParameterException
    {
        if (openMetadataTypeDefLinks == null)
        {
            return null;
        }

        List<TypeDefLink> typeDefLinks = new ArrayList<>();

        for (OpenMetadataTypeDefLink openMetadataTypeDefLink : openMetadataTypeDefLinks)
        {
            if (openMetadataTypeDefLink != null)
            {
                typeDefLinks.add(this.getTypeDefLink(openMetadataTypeDefLink, parameterName, methodName));
            }
        }

        if (typeDefLinks.isEmpty())
        {
            return null;
        }

        return typeDefLinks;
    }


    /**
     * Return the repository services' version of the end of a relationship type.
     *
     * @param openMetadataRelationshipEndDef end from the caller - may be null
     * @param parameterName name of the parameter holding the end
     * @param methodName calling method
     * @return end or null
     * @throws InvalidParameterException the entity type at the end is not known
     */
    private RelationshipEndDef getRelationshipEndDef(OpenMetadataRelationshipEndDef openMetadataRelationshipEndDef,
                                                     String                         parameterName,
                                                     String                         methodName) throws InvalidParameterException
    {
        if (openMetadataRelationshipEndDef == null)
        {
            return null;
        }

        RelationshipEndDef relationshipEndDef = new RelationshipEndDef();

        relationshipEndDef.setEntityType(this.getTypeDefLink(openMetadataRelationshipEndDef.getEntityType(), parameterName + ".entityType", methodName));
        relationshipEndDef.setAttributeName(openMetadataRelationshipEndDef.getAttributeName());
        relationshipEndDef.setAttributeDescription(openMetadataRelationshipEndDef.getAttributeDescription());
        relationshipEndDef.setAttributeDescriptionGUID(openMetadataRelationshipEndDef.getAttributeDescriptionGUID());
        relationshipEndDef.setAttributeCardinality(this.getEnum(RelationshipEndCardinality.class,
                                                                openMetadataRelationshipEndDef.getAttributeCardinality(),
                                                                RelationshipEndCardinality.ANY_NUMBER,
                                                                parameterName + ".attributeCardinality",
                                                                methodName));

        return relationshipEndDef;
    }


    /**
     * Return the repository services' version of a list of attributes.
     *
     * @param openMetadataTypeDefAttributes attributes from the caller - may be null
     * @param parameterName name of the parameter holding the attributes
     * @param methodName calling method
     * @return list of attributes or null
     * @throws InvalidParameterException an attribute's type is not known
     */
    private List<TypeDefAttribute> getTypeDefAttributes(List<OpenMetadataTypeDefAttribute> openMetadataTypeDefAttributes,
                                                        String                             parameterName,
                                                        String                             methodName) throws InvalidParameterException
    {
        if (openMetadataTypeDefAttributes == null)
        {
            return null;
        }

        List<TypeDefAttribute> typeDefAttributes = new ArrayList<>();

        for (OpenMetadataTypeDefAttribute openMetadataTypeDefAttribute : openMetadataTypeDefAttributes)
        {
            if (openMetadataTypeDefAttribute != null)
            {
                String attributeParameterName = parameterName + "." + openMetadataTypeDefAttribute.getAttributeName();

                TypeDefAttribute typeDefAttribute = new TypeDefAttribute();

                typeDefAttribute.setAttributeName(openMetadataTypeDefAttribute.getAttributeName());
                typeDefAttribute.setAttributeType(this.getAttributeType(openMetadataTypeDefAttribute.getAttributeType(),
                                                                        attributeParameterName + ".attributeType",
                                                                        methodName));
                typeDefAttribute.setAttributeStatus(this.getEnum(TypeDefAttributeStatus.class,
                                                                 openMetadataTypeDefAttribute.getAttributeStatus(),
                                                                 TypeDefAttributeStatus.ACTIVE_ATTRIBUTE,
                                                                 attributeParameterName + ".attributeStatus",
                                                                 methodName));
                typeDefAttribute.setReplacedByAttribute(openMetadataTypeDefAttribute.getReplacedByAttribute());
                typeDefAttribute.setAttributeDescription(openMetadataTypeDefAttribute.getAttributeDescription());
                typeDefAttribute.setAttributeDescriptionGUID(openMetadataTypeDefAttribute.getAttributeDescriptionGUID());
                typeDefAttribute.setAttributeCardinality(this.getEnum(AttributeCardinality.class,
                                                                      openMetadataTypeDefAttribute.getAttributeCardinality(),
                                                                      AttributeCardinality.AT_MOST_ONE,
                                                                      attributeParameterName + ".attributeCardinality",
                                                                      methodName));
                typeDefAttribute.setValuesMinCount(openMetadataTypeDefAttribute.getValuesMinCount());
                typeDefAttribute.setValuesMaxCount(openMetadataTypeDefAttribute.getValuesMaxCount());
                typeDefAttribute.setUnique(openMetadataTypeDefAttribute.isUnique());
                typeDefAttribute.setIndexable(openMetadataTypeDefAttribute.isIndexable());
                typeDefAttribute.setDefaultValue(openMetadataTypeDefAttribute.getDefaultValue());
                typeDefAttribute.setExternalStandardMappings(this.getExternalStandardMappings(openMetadataTypeDefAttribute.getExternalStandardMappings()));

                typeDefAttributes.add(typeDefAttribute);
            }
        }

        if (typeDefAttributes.isEmpty())
        {
            return null;
        }

        return typeDefAttributes;
    }


    /**
     * Return the known attribute type for an attribute.  Attribute types are identified by name - the rest of the
     * caller's description of the attribute type is ignored in favour of the known definition.
     *
     * @param openMetadataAttributeTypeDef attribute type from the caller - may be null
     * @param parameterName name of the parameter holding the attribute type
     * @param methodName calling method
     * @return known attribute type or null
     * @throws InvalidParameterException the attribute type is not known
     */
    private AttributeTypeDef getAttributeType(OpenMetadataAttributeTypeDef openMetadataAttributeTypeDef,
                                              String                       parameterName,
                                              String                       methodName) throws InvalidParameterException
    {
        if (openMetadataAttributeTypeDef == null)
        {
            return null;
        }

        AttributeTypeDef knownAttributeTypeDef = null;

        if (openMetadataAttributeTypeDef.getName() != null)
        {
            knownAttributeTypeDef = repositoryHelper.getAttributeTypeDefByName(serviceName, openMetadataAttributeTypeDef.getName());
        }

        if (knownAttributeTypeDef == null)
        {
            throw new InvalidParameterException(OMFServicesErrorCode.UNKNOWN_TYPE_REFERENCE.getMessageDefinition("attribute",
                                                                                                                 openMetadataAttributeTypeDef.getName(),
                                                                                                                 parameterName,
                                                                                                                 methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        return knownAttributeTypeDef.cloneFromSubclass();
    }


    /**
     * Return the repository services' version of a list of external standard mappings.
     *
     * @param externalStandardTypeMappings mappings from the caller - may be null
     * @return list of mappings or null
     */
    private List<ExternalStandardMapping> getExternalStandardMappings(List<ExternalStandardTypeMapping> externalStandardTypeMappings)
    {
        if (externalStandardTypeMappings == null)
        {
            return null;
        }

        List<ExternalStandardMapping> externalStandardMappings = new ArrayList<>();

        for (ExternalStandardTypeMapping externalStandardTypeMapping : externalStandardTypeMappings)
        {
            if (externalStandardTypeMapping != null)
            {
                ExternalStandardMapping externalStandardMapping = new ExternalStandardMapping();

                externalStandardMapping.setStandardName(externalStandardTypeMapping.getStandardName());
                externalStandardMapping.setStandardOrganization(externalStandardTypeMapping.getStandardOrganization());
                externalStandardMapping.setStandardTypeName(externalStandardTypeMapping.getStandardTypeName());

                externalStandardMappings.add(externalStandardMapping);
            }
        }

        if (externalStandardMappings.isEmpty())
        {
            return null;
        }

        return externalStandardMappings;
    }


    /**
     * Return the repository services' version of a list of element statuses.
     *
     * @param elementStatuses statuses from the caller - may be null
     * @param parameterName name of the parameter holding the statuses
     * @param methodName calling method
     * @return list of statuses or null
     * @throws InvalidParameterException a status has no equivalent
     */
    private List<InstanceStatus> getInstanceStatuses(List<ElementStatus> elementStatuses,
                                                     String              parameterName,
                                                     String              methodName) throws InvalidParameterException
    {
        if (elementStatuses == null)
        {
            return null;
        }

        List<InstanceStatus> instanceStatuses = new ArrayList<>();

        for (ElementStatus elementStatus : elementStatuses)
        {
            if (elementStatus != null)
            {
                instanceStatuses.add(this.getEnum(InstanceStatus.class, elementStatus, null, parameterName, methodName));
            }
        }

        if (instanceStatuses.isEmpty())
        {
            return null;
        }

        return instanceStatuses;
    }


    /**
     * Return the repository services' equivalent of one of the open metadata framework's enums.  Each pair of
     * enums used in type definitions has the same value names, so the value is matched by name.  The framework
     * beans initialize their cardinalities to UNKNOWN, so where there is a default, UNKNOWN is taken to mean the
     * caller did not say.
     *
     * @param enumClass repository services enum
     * @param value value from the caller - may be null
     * @param defaultValue value to return if the caller supplied none
     * @param parameterName name of the parameter holding the value
     * @param methodName calling method
     * @return equivalent value
     * @param <E> repository services enum
     * @throws InvalidParameterException the value has no equivalent
     */
    private <E extends Enum<E>> E getEnum(Class<E> enumClass,
                                          Enum<?>  value,
                                          E        defaultValue,
                                          String   parameterName,
                                          String   methodName) throws InvalidParameterException
    {
        final String unknownValueName = "UNKNOWN";

        if ((value == null) || ((defaultValue != null) && (unknownValueName.equals(value.name()))))
        {
            return defaultValue;
        }

        try
        {
            return Enum.valueOf(enumClass, value.name());
        }
        catch (IllegalArgumentException noEquivalent)
        {
            throw new InvalidParameterException(OMFServicesErrorCode.UNMAPPABLE_TYPE_VALUE.getMessageDefinition(value.name(),
                                                                                                                parameterName,
                                                                                                                methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                noEquivalent,
                                                parameterName);
        }
    }
}
