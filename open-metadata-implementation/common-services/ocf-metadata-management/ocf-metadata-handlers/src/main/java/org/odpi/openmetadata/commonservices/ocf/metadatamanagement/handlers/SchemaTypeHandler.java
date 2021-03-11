/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.*;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.SchemaTypeConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * SchemaTypeHandler manages SchemaType objects.  It runs server-side in
 * the OMAG Server Platform and retrieves SchemaType entities through the OMRSRepositoryConnector.
 */
public class SchemaTypeHandler extends RootHandler
{

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     */
    public SchemaTypeHandler(String                  serviceName,
                             String                  serverName,
                             InvalidParameterHandler invalidParameterHandler,
                             RepositoryHandler       repositoryHandler,
                             OMRSRepositoryHelper    repositoryHelper)
    {
        super(serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper);
    }


    /**
     * Walk the graph to locate the asset for a schema type.  Schemas are attached to each other through various levels of nesting, ports (for
     * process assets) and asset through the schema type.  It is also possible that the schema is not attached to anything.  This is common if the
     * schema is a template.
     *
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of schema type (it is assumed that the anchorGUID property of this type is null);
     * @param methodName calling method
     *
     * @return unique identifier of attached asset or null if there is no attached asset
     *
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private String getAnchorGUIDForType(String userId,
                                        String schemaTypeGUID,
                                        String methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        /*
         * The most obvious test is that this schema type is attached directly to the asset.
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  schemaTypeGUID,
                                                                                  SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                                  AssetMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                                                  AssetMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                                                  methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return proxy.getGUID();
        }

        /*
         * Next test to see if the type is connected to an attribute.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                                     SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForAttribute(userId, proxy.getGUID(), methodName);
        }

        /*
         * Next test to see if the type is connected to a SchemaTypeChoice.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     SchemaElementMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                                                     SchemaElementMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForType(userId, proxy.getGUID(), methodName);
        }

        /*
         * Next test to see if the type is connected to a MapSchemaType.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     SchemaElementMapper.MAP_FROM_RELATIONSHIP_TYPE_GUID,
                                                                     SchemaElementMapper.MAP_FROM_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForType(userId, proxy.getGUID(), methodName);
        }
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     SchemaElementMapper.MAP_TO_RELATIONSHIP_TYPE_GUID,
                                                                     SchemaElementMapper.MAP_TO_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForType(userId, proxy.getGUID(), methodName);
        }

        /*
         * Finally test to see if the type is connected to an API operation or API schema type.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     SchemaElementMapper.API_OPERATIONS_RELATIONSHIP_TYPE_GUID,
                                                                     SchemaElementMapper.API_OPERATIONS_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForType(userId, proxy.getGUID(), methodName);
        }
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     SchemaElementMapper.API_HEADER_RELATIONSHIP_TYPE_GUID,
                                                                     SchemaElementMapper.API_HEADER_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForType(userId, proxy.getGUID(), methodName);
        }
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     SchemaElementMapper.API_REQUEST_RELATIONSHIP_TYPE_GUID,
                                                                     SchemaElementMapper.API_REQUEST_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForType(userId, proxy.getGUID(), methodName);
        }
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     SchemaElementMapper.API_RESPONSE_RELATIONSHIP_TYPE_GUID,
                                                                     SchemaElementMapper.API_RESPONSE_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForType(userId, proxy.getGUID(), methodName);
        }

        return null;
    }



    /**
     * Walk the graph to locate the asset for a schema attribute.  Schemas are attached to each other through various levels of nesting, ports (for
     * process assets) and asset through the schema type.  It is also possible that the schema is not attached to anything.  This is common if the
     * schema is a template.
     *
     * @param userId calling user
     * @param attributeGUID unique identifier of attribute (it is assumed that the anchorGUID property of this attribute is null);
     * @param methodName calling method
     *
     * @return unique identifier of attached asset or null if there is no attached asset
     *
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private String getAnchorGUIDForAttribute(String userId,
                                             String attributeGUID,
                                             String methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        /*
         * Is the schema attribute connected to a type.
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  attributeGUID,
                                                                                  SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                                  SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                                  SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                                  methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForType(userId, proxy.getGUID(), methodName);
        }

        /*
         * Is the attribute nested in another attribute?
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     attributeGUID,
                                                                     SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                     SchemaElementMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                     SchemaElementMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForAttribute(userId, proxy.getGUID(), methodName);
        }

        return null;
    }



    /**
     * Return the requested schemaType with the specific type information filled in.
     * There is no validation of the type name and the bean is assumed to be for the local cohort.
     *
     * @param schemaTypeGUID unique identifier of the required type
     * @param schemaTypeName unique name of the required type
     * @return new object
     */
    public ComplexSchemaType getEmptyComplexSchemaType(String     schemaTypeGUID,
                                                       String     schemaTypeName)
    {
        ComplexSchemaType  schemaType = new ComplexSchemaType();

        ElementType elementType = new ElementType();

        elementType.setElementOrigin(ElementOrigin.LOCAL_COHORT);
        elementType.setElementTypeId(schemaTypeGUID);
        elementType.setElementTypeName(schemaTypeName);

        schemaType.setType(elementType);

        return schemaType;
    }


    /**
     * Return a schema attribute object with the type set up.  This is for columns that are created
     * in the local cohort only.
     *
     * @return new object
     */
    public SchemaAttribute  getEmptyTabularColumn()
    {
        SchemaAttribute   schemaAttribute = new SchemaAttribute();

        ElementType elementType = new ElementType();

        elementType.setElementOrigin(ElementOrigin.LOCAL_COHORT);
        elementType.setElementTypeId(SchemaElementMapper.TABULAR_COLUMN_TYPE_GUID);
        elementType.setElementTypeName(SchemaElementMapper.TABULAR_COLUMN_TYPE_NAME);

        schemaAttribute.setType(elementType);

        return schemaAttribute;
    }


    /**
     * Transform a schema type entity into a schema type bean.  To completely fill out the schema type
     * it may be necessary to
     *
     * @param userId calling user
     * @param schemaTypeEntity entity retrieved from the repository
     * @param methodName calling method
     *
     * @return schema type bean
     * @throws InvalidParameterException problem with the entity
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private SchemaType getSchemaTypeFromEntity(String       userId,
                                               EntityDetail schemaTypeEntity,
                                               String       methodName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String guidParameterName = "schemaTypeEntity.getGUID()";

        if ((schemaTypeEntity != null) && (schemaTypeEntity.getType() != null))
        {
            InstanceProperties properties = schemaTypeEntity.getProperties();
            String typeName = repositoryHelper.removeStringProperty(serviceName,
                                                                    SchemaElementMapper.TYPE_NAME_PROPERTY_NAME,
                                                                    properties,
                                                                    methodName);

            if (properties != null)
            {
                /*
                 * Complex schema types have attributes attached to them
                 */
                int attributeCount = 0;

                if (typeName != null)
                {
                    if (repositoryHelper.isTypeOf(serviceName,
                                                  typeName,
                                                  SchemaElementMapper.COMPLEX_SCHEMA_TYPE_TYPE_NAME))
                    {
                        attributeCount = this.countSchemaAttributes(userId,
                                                                    schemaTypeEntity.getGUID(),
                                                                    guidParameterName,
                                                                    methodName);
                    }
                }

                SchemaTypeConverter converter = new SchemaTypeConverter(schemaTypeEntity,
                                                                        attributeCount,
                                                                        repositoryHelper,
                                                                        serviceName,
                                                                        serverName);

                return this.getEmbeddedTypes(userId, schemaTypeEntity.getGUID(), converter.getBean(), methodName);
            }
        }

        return null;
    }


    /**
     * This method manufactures a complete schema type bean of the requested type from the supplied properties and
     * potentially more retrieves from the repository if necessary.
     *
     * @param userId calling user
     * @param schemaElementGUID GUID of the object that is the root of the schema type - needed if have to retrieve
     *                          additional information.
     * @param parentSchemaType properties for the schema type
     * @param methodName calling method
     * @return schema type bean or null
     *
     * @throws InvalidParameterException problem with the properties
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server or values retrieved are weird.
     */
    @SuppressWarnings(value = "deprecation")
    private SchemaType getEmbeddedTypes(String     userId,
                                        String     schemaElementGUID,
                                        SchemaType parentSchemaType,
                                        String     methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        if (parentSchemaType instanceof MapSchemaType)
        {
            EntityDetail entity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                                 schemaElementGUID,
                                                                                 SchemaElementMapper.MAP_SCHEMA_TYPE_TYPE_NAME,
                                                                                 SchemaElementMapper.MAP_FROM_RELATIONSHIP_TYPE_GUID,
                                                                                 SchemaElementMapper.MAP_FROM_RELATIONSHIP_TYPE_NAME,
                                                                                 methodName);
            if (entity != null)
            {
                ((MapSchemaType) parentSchemaType).setMapFromElement(this.getSchemaTypeFromEntity(userId, entity, methodName));
            }

            entity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                    schemaElementGUID,
                                                                    SchemaElementMapper.MAP_SCHEMA_TYPE_TYPE_NAME,
                                                                    SchemaElementMapper.MAP_TO_RELATIONSHIP_TYPE_GUID,
                                                                    SchemaElementMapper.MAP_TO_RELATIONSHIP_TYPE_NAME,
                                                                    methodName);

            if (entity != null)
            {
                ((MapSchemaType) parentSchemaType).setMapToElement(this.getSchemaTypeFromEntity(userId, entity, methodName));
            }
        }
        else if (parentSchemaType instanceof SchemaTypeChoice)
        {
            List<EntityDetail>  entities = repositoryHandler.getEntitiesForRelationshipType(userId,
                                                                                            schemaElementGUID,
                                                                                            SchemaElementMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME,
                                                                                            SchemaElementMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                                                                            SchemaElementMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME,
                                                                                            0,
                                                                                            invalidParameterHandler.getMaxPagingSize(),
                                                                                            methodName);

            if (entities != null)
            {
                List<SchemaType> schemaTypes = new ArrayList<>();

                for (EntityDetail entity : entities)
                {
                    if (entity != null)
                    {
                        schemaTypes.add(getSchemaTypeFromEntity(userId, entity, methodName));
                    }
                }

                if (! schemaTypes.isEmpty())
                {
                    ((SchemaTypeChoice) parentSchemaType).setSchemaOptions(schemaTypes);
                }
            }
        }

        return parentSchemaType;
    }


    /**
     * Count the number of connection attached to an anchor schema type.
     *
     * @param userId     calling user
     * @param schemaElementGUID identifier for the parent complex schema type - this could be a schemaAttribute or a
     *                          schema type
     * @param guidParameterName name of guid parameter
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    int countSchemaAttributes(String   userId,
                              String   schemaElementGUID,
                              String   guidParameterName,
                              String   methodName) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, guidParameterName, methodName);

        int count = repositoryHandler.countAttachedRelationshipsByType(userId,
                                                                       schemaElementGUID,
                                                                       SchemaElementMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                       SchemaElementMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                       SchemaElementMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                       methodName);


        if (count == 0)
        {
            count = repositoryHandler.countAttachedRelationshipsByType(userId,
                                                                       schemaElementGUID,
                                                                       SchemaElementMapper.COMPLEX_SCHEMA_TYPE_TYPE_NAME,
                                                                       SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                       SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                       methodName);
        }

        return count;
    }


    /**
     * Find out if the schema type object is already stored in the repository.  If the schema type's
     * guid is set, it uses it to retrieve the entity.  If the GUID is not set, it tries the
     * fully qualified name.  If neither are set it throws an exception.
     *
     * @param userId calling user
     * @param schemaAttributeGUID unique Id
     * @param qualifiedName unique name
     * @param attributeName human readable name
     * @param methodName calling method
     *
     * @return unique identifier of the connection or null
     *
     * @throws InvalidParameterException the connection bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String findSchemaAttribute(String               userId,
                                       String               schemaAttributeGUID,
                                       String               qualifiedName,
                                       String               attributeName,
                                       String               methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String guidParameterName        = "schemaAttributeGUID";
        final String qualifiedNameParameter   = "qualifiedName";

        if (schemaAttributeGUID != null)
        {
            if (repositoryHandler.isEntityKnown(userId,
                                                schemaAttributeGUID,
                                                SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                methodName,
                                                guidParameterName) != null)
            {
                return schemaAttributeGUID;
            }
        }

        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        SchemaAttributeBuilder builder = new SchemaAttributeBuilder(qualifiedName,
                                                                    attributeName,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        EntityDetail existingSchemaType = repositoryHandler.getUniqueEntityByName(userId,
                                                                                  qualifiedName,
                                                                                  qualifiedNameParameter,
                                                                                  builder.getQualifiedNameInstanceProperties(methodName),
                                                                                  SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                                                                  SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                                  methodName);
        if (existingSchemaType != null)
        {
            return existingSchemaType.getGUID();
        }

        return null;
    }


    /**
     * Add the schema attribute to the repository.
     *
     * @param userId   calling userId
     * @param externalSourceGUID unique identifier(guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param schemaAttribute object to add
     * @return unique identifier of the schemaAttribute in the repository.
     * @throws InvalidParameterException  the schemaAttribute bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private String addSchemaAttribute(String           userId,
                                      String           externalSourceGUID,
                                      String           externalSourceName,
                                      SchemaAttribute  schemaAttribute) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "addSchemaAttribute";

        SchemaAttributeBuilder schemaAttributeBuilder = this.getSchemaAttributeBuilder(schemaAttribute);

        String schemaAttributeGUID = repositoryHandler.createEntity(userId,
                                                                    this.getSchemaAttributeTypeGUID(schemaAttribute),
                                                                    this.getSchemaAttributeTypeName(schemaAttribute),
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    schemaAttributeBuilder.getInstanceProperties(methodName),
                                                                    methodName);

        SchemaType schemaType = schemaAttribute.getAttributeType();

        if (schemaType != null)
        {
            if ((schemaType.getExtendedProperties() == null) &&
                    ((schemaType instanceof ComplexSchemaType) ||
                     (schemaType instanceof LiteralSchemaType) ||
                     (schemaType instanceof SimpleSchemaType)))
            {
                /*
                 * The schema type can be represented as a classification on the schema attribute.
                 */
                SchemaTypeBuilder schemaTypeBuilder = this.getSchemaTypeBuilder(schemaType);

                repositoryHandler.classifyEntity(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 schemaAttributeGUID,
                                                 SchemaElementMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_GUID,
                                                 SchemaElementMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                                                 null,
                                                 null,
                                                 schemaTypeBuilder.getInstanceProperties(methodName),
                                                 methodName);
            }
            else
            {
                String schemaTypeGUID = addSchemaType(userId, externalSourceGUID, externalSourceName, schemaType);
                if (schemaTypeGUID != null)
                {
                    repositoryHandler.createRelationship(userId,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                         schemaAttributeGUID,
                                                         schemaTypeGUID,
                                                         null,
                                                         methodName);
                }
            }
        }

        return schemaAttributeGUID;
    }


    /**
     * Add the schema type from an external source to the repository. This call is deprecated because it is no longer
     * necessary.  Change caller to use addSchemaAttribute and delete this method when no longer used.
     *
     * @param userId   calling userId
     * @param schemaAttribute object to add
     * @param externalSourceGUID unique identifier(guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @return unique identifier of the schemaAttribute in the repository.
     * @throws InvalidParameterException  the schemaAttribute bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Deprecated
    public String addExternalSchemaAttribute(String               userId,
                                             SchemaAttribute      schemaAttribute,
                                             String               externalSourceGUID,
                                             String               externalSourceName) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        return addSchemaAttribute(userId, externalSourceGUID, externalSourceName, schemaAttribute);
    }


    /**
     * Update a stored schema attribute.  This method is deprecated because the provenance information is not available.
     *
     * @param userId                      userId
     * @param existingSchemaAttributeGUID unique identifier of the existing schemaAttribute entity
     * @param schemaAttribute             new schemaAttribute values
     * @return unique identifier of the schemaAttribute in the repository.
     * @throws InvalidParameterException  the schemaAttribute bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Deprecated
    public String updateSchemaAttribute(String           userId,
                                        String           existingSchemaAttributeGUID,
                                        SchemaAttribute  schemaAttribute) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "updateSchemaAttribute";

        SchemaAttributeBuilder builder = this.getSchemaAttributeBuilder(schemaAttribute);

        repositoryHandler.updateEntity(userId,
                                       existingSchemaAttributeGUID,
                                       this.getSchemaAttributeTypeGUID(schemaAttribute),
                                       this.getSchemaAttributeTypeName(schemaAttribute),
                                       builder.getInstanceProperties(methodName),
                                       methodName);

        return existingSchemaAttributeGUID;
    }


    /**
     * Return the appropriate schemaAttribute builder for the supplied schema attribute bean.
     *
     * @param schemaAttribute object with properties
     * @return builder object.
     */
    public SchemaAttributeBuilder  getSchemaAttributeBuilder(SchemaAttribute schemaAttribute)
    {
        SchemaAttributeBuilder builder = null;

        if (schemaAttribute != null)
        {
            String typeName = SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME;
            String typeId   = SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_GUID;

            ElementType type = schemaAttribute. getType();
            if (type != null)
            {
                typeName = type.getElementTypeName();
                typeId   = type.getElementTypeId();
            }

            EnumPropertyValue  sortOrder = null;

            if (schemaAttribute.getSortOrder() != null)
            {
                sortOrder = new EnumPropertyValue();

                sortOrder.setOrdinal(schemaAttribute.getSortOrder().getOrdinal());
                sortOrder.setSymbolicName(schemaAttribute.getSortOrder().getName());
                sortOrder.setDescription(schemaAttribute.getSortOrder().getDescription());

            }

            builder = new SchemaAttributeBuilder(schemaAttribute.getQualifiedName(),
                                                 schemaAttribute.getDisplayName(),
                                                 schemaAttribute.getDescription(),
                                                 schemaAttribute.getElementPosition(),
                                                 schemaAttribute.getMinCardinality(),
                                                 schemaAttribute.getMaxCardinality(),
                                                 schemaAttribute.getIsDeprecated(),
                                                 schemaAttribute.getDefaultValueOverride(),
                                                 schemaAttribute.getAllowsDuplicateValues(),
                                                 schemaAttribute.getOrderedValues(),
                                                 sortOrder,
                                                 schemaAttribute.getMinimumLength(),
                                                 schemaAttribute.getLength(),
                                                 schemaAttribute.getPrecision(),
                                                 schemaAttribute.getIsNullable(),
                                                 schemaAttribute.getNativeJavaClass(),
                                                 schemaAttribute.getAliases(),
                                                 schemaAttribute.getAdditionalProperties(),
                                                 schemaAttribute.getAnchorGUID(),
                                                 typeName,
                                                 typeId,
                                                 schemaAttribute.getExtendedProperties(),
                                                 repositoryHelper,
                                                 serviceName,
                                                 serverName);
        }

        return builder;
    }


    /**
     * Return the type guid contained in this schemaAttribute bean.
     *
     * @param schemaAttribute bean to interrogate
     * @return guid of the type
     */
    private String getSchemaAttributeTypeGUID(SchemaAttribute   schemaAttribute)
    {
        ElementType type = schemaAttribute.getType();
        if (type != null)
        {
            return type.getElementTypeId();
        }
        else
        {
            return SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_GUID;
        }
    }


    /**
     * Return the type name contained in this schemaAttribute bean.
     *
     * @param schemaAttribute bean to interrogate
     * @return name of the type
     */
    private String getSchemaAttributeTypeName(SchemaAttribute   schemaAttribute)
    {
        ElementType type = schemaAttribute.getType();
        if (type != null)
        {
            return type.getElementTypeName();
        }
        else
        {
            return SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME;
        }
    }



    /**
     * Find out if the schema type object is already stored in the repository.  If the schema type's
     * guid is set, it uses it to retrieve the entity.  If the GUID is not set, it tries the
     * fully qualified name.  If neither are set it throws an exception.  If the schema type is not
     * found it returns null.
     *
     * @param userId calling user
     * @param schemaTypeGUID unique Id
     * @param qualifiedName unique name
     * @param displayName human readable name
     * @param methodName calling method
     *
     * @return unique identifier of the connection or null
     *
     * @throws InvalidParameterException the schema type bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String findSchemaType(String               userId,
                                  String               schemaTypeGUID,
                                  String               qualifiedName,
                                  String               displayName,
                                  String               methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String guidParameterName        = "schemaTypeGUID";
        final String qualifiedNameParameter   = "qualifiedName";

        if (schemaTypeGUID != null)
        {
            if (repositoryHandler.isEntityKnown(userId,
                                                schemaTypeGUID,
                                                SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                methodName,
                                                guidParameterName) != null)
            {
                return schemaTypeGUID;
            }
        }

        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        SchemaTypeBuilder builder = new SchemaTypeBuilder(qualifiedName,
                                                          displayName,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        EntityDetail existingSchemaType = repositoryHandler.getUniqueEntityByName(userId,
                                                                                  qualifiedName,
                                                                                  qualifiedNameParameter,
                                                                                  builder.getQualifiedNameInstanceProperties(methodName),
                                                                                  SchemaElementMapper.SCHEMA_TYPE_TYPE_GUID,
                                                                                  SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                                  methodName);
        if (existingSchemaType != null)
        {
            return existingSchemaType.getGUID();
        }

        return null;
    }


    /**
     * Store a new schema type (and optional attributes in the repository and return its unique identifier (GUID).
     *
     * @param userId calling userId
     * @param externalSourceGUID unique identifier of software server capability representing the caller - null for local cohort
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeBuilder properties for new schemaType
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String  addSchemaType(String userId,
                                  String externalSourceGUID,
                                  String externalSourceName,
                                  SchemaTypeBuilder schemaTypeBuilder,
                                  String methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        String schemaTypeGUID = repositoryHandler.createEntity(userId,
                                                               schemaTypeBuilder.getTypeId(),
                                                               schemaTypeBuilder.getTypeName(),
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               schemaTypeBuilder.getInstanceProperties(methodName),
                                                               schemaTypeBuilder.getEntityClassifications(userId, methodName),
                                                               methodName);

        addEmbeddedTypes(userId, externalSourceGUID, externalSourceName, schemaTypeGUID, schemaTypeBuilder, methodName);


        return schemaTypeGUID;
    }


    /**
     * Add the schema type to the repository and any schema types embedded within it.
     *
     * @param userId   calling userId
     * @param schemaType object to add
     * @return unique identifier of the schemaType in the repository.
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private String addSchemaType(String                userId,
                                 String                externalSourceGUID,
                                 String                externalSourceName,
                                 SchemaType            schemaType) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "addSchemaType";

        SchemaTypeBuilder schemaTypeBuilder = this.getSchemaTypeBuilder(schemaType);

        String schemaTypeGUID = repositoryHandler.createEntity(userId,
                                                               this.getSchemaTypeTypeGUID(schemaType),
                                                               this.getSchemaTypeTypeName(schemaType),
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               schemaTypeBuilder.getInstanceProperties(methodName),
                                                               methodName);

        return addEmbeddedTypes(userId, externalSourceGUID, externalSourceName, schemaTypeGUID, schemaType);
    }


    /**
     * Add additional schema types that are part of a schema type.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeGUID schema type entity to link to
     * @param schemaType description of complete schema type.
     * @return schemaTypeGUID
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @SuppressWarnings(value = "deprecation")
    private String addEmbeddedTypes(String     userId,
                                    String     externalSourceGUID,
                                    String     externalSourceName,
                                    String     schemaTypeGUID,
                                    SchemaType schemaType) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName = "addEmbeddedTypes";

        if (schemaType instanceof MapSchemaType)
        {
            SchemaType mapFrom = ((MapSchemaType) schemaType).getMapFromElement();
            SchemaType mapTo   = ((MapSchemaType) schemaType).getMapToElement();

            if (mapFrom != null)
            {
                String mapFromGUID = addSchemaType(userId, externalSourceGUID, externalSourceName, mapFrom);

                if (mapFromGUID != null)
                {
                    repositoryHandler.createRelationship(userId,
                                                         SchemaElementMapper.MAP_FROM_RELATIONSHIP_TYPE_NAME,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         schemaTypeGUID,
                                                         mapFromGUID,
                                                         null,
                                                         methodName);
                }
            }

            if (mapTo != null)
            {
                String mapToGUID = addSchemaType(userId, externalSourceGUID, externalSourceName, mapTo);

                if (mapToGUID != null)
                {
                    repositoryHandler.createRelationship(userId,
                                                         SchemaElementMapper.MAP_TO_RELATIONSHIP_TYPE_NAME,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         schemaTypeGUID,
                                                         mapToGUID,
                                                         null,
                                                         methodName);
                }
            }
        }
        else if (schemaType instanceof SchemaTypeChoice)
        {
            List<SchemaType>  schemaOptions = ((SchemaTypeChoice) schemaType).getSchemaOptions();

            if (schemaOptions != null)
            {
                for (SchemaType option : schemaOptions)
                {
                    if (option != null)
                    {
                        String optionGUID = addSchemaType(userId, externalSourceGUID, externalSourceName, option);

                        if (optionGUID != null)
                        {
                            repositoryHandler.createRelationship(userId,
                                                                 SchemaElementMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 schemaTypeGUID,
                                                                 optionGUID,
                                                                 null,
                                                                 methodName);
                        }
                    }
                }
            }
        }

        return schemaTypeGUID;
    }


    /**
     * Add additional schema types that are part of a schema type.  Specifically for maps and schema type choices.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeGUID schema type entity to link to
     * @param schemaTypeBuilder properties of complete schema type.
     * @param methodName calling method
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private void   addEmbeddedTypes(String            userId,
                                    String            externalSourceGUID,
                                    String            externalSourceName,
                                    String            schemaTypeGUID,
                                    SchemaTypeBuilder schemaTypeBuilder,
                                    String            methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        if (SchemaElementMapper.MAP_SCHEMA_TYPE_TYPE_NAME.equals(schemaTypeBuilder.getTypeName()))
        {
            SchemaTypeBuilder mapFrom = schemaTypeBuilder.getMapFrom();
            SchemaTypeBuilder mapTo   = schemaTypeBuilder.getMapTo();

            if (mapFrom != null)
            {
                String mapFromGUID = addSchemaType(userId, externalSourceGUID, externalSourceName, mapFrom, methodName);

                if (mapFromGUID != null)
                {
                    repositoryHandler.createRelationship(userId,
                                                         SchemaElementMapper.MAP_FROM_RELATIONSHIP_TYPE_NAME,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         schemaTypeGUID,
                                                         mapFromGUID,
                                                         null,
                                                         methodName);
                }
            }

            if (mapTo != null)
            {
                String mapToGUID = addSchemaType(userId, externalSourceGUID, externalSourceName, mapTo, methodName);

                if (mapToGUID != null)
                {
                    repositoryHandler.createRelationship(userId,
                                                         SchemaElementMapper.MAP_TO_RELATIONSHIP_TYPE_NAME,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         schemaTypeGUID,
                                                         mapToGUID,
                                                         null,
                                                         methodName);
                }
            }
        }
        else if (SchemaElementMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME.equals(schemaTypeBuilder.getTypeName()))
        {
            List<SchemaTypeBuilder>  schemaOptions = schemaTypeBuilder.getSchemaOptions();

            if (schemaOptions != null)
            {
                for (SchemaTypeBuilder option : schemaOptions)
                {
                    if (option != null)
                    {
                        String optionGUID = addSchemaType(userId, externalSourceGUID, externalSourceName, option, methodName);

                        if (optionGUID != null)
                        {
                            repositoryHandler.createRelationship(userId,
                                                                 SchemaElementMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 schemaTypeGUID,
                                                                 optionGUID,
                                                                 null,
                                                                 methodName);
                        }
                    }
                }
            }
        }
    }



    /**
     * Add the schema type from an external source to the repository along with any schema types embedded within it.
     *
     * @param userId   calling userId
     * @param schemaType object to add
     * @param externalSourceGUID unique identifier(guid) for the external source
     * @param externalSourceName unique name for the external source
     * @return unique identifier of the schemaType in the repository.
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String addExternalSchemaType(String                userId,
                                        SchemaType            schemaType,
                                        String                externalSourceGUID,
                                        String                externalSourceName) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        return addSchemaType(userId, externalSourceGUID, externalSourceName, schemaType);
    }


    /**
     * Update a stored schemaType.  This method is deprecated because it does not pass provenance information -
     * assuming local cohort provenance.
     *
     * @param userId                 userId
     * @param existingSchemaTypeGUID unique identifier of the existing schemaType entity
     * @param schemaType             new schemaType values
     * @return GUID of existing schema type
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Deprecated
    public String updateSchemaType(String      userId,
                                   String      existingSchemaTypeGUID,
                                   SchemaType  schemaType) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName = "updateSchemaType";

        SchemaTypeBuilder schemaTypeBuilder = this.getSchemaTypeBuilder(schemaType);

        repositoryHandler.updateEntity(userId,
                                       existingSchemaTypeGUID,
                                       this.getSchemaTypeTypeGUID(schemaType),
                                       this.getSchemaTypeTypeName(schemaType),
                                       schemaTypeBuilder.getInstanceProperties(methodName),
                                       methodName);

        return existingSchemaTypeGUID;
    }


    /**
     * Return the type guid contained in this schemaType bean.
     *
     * @param schemaType bean to interrogate
     * @return guid of the type
     */
    private String getSchemaTypeTypeGUID(SchemaType   schemaType)
    {
        ElementType type = schemaType.getType();
        if (type != null)
        {
            return type.getElementTypeId();
        }
        else
        {
            return SchemaElementMapper.SCHEMA_TYPE_TYPE_GUID;
        }
    }


    /**
     * Return the type name contained in this schemaType bean.
     *
     * @param schemaType bean to interrogate
     * @return name of the type
     */
    private String getSchemaTypeTypeName(SchemaType   schemaType)
    {
        ElementType type = schemaType.getType();
        if (type != null)
        {
            return type.getElementTypeName();
        }
        else
        {
            return SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME;
        }
    }


    /**
     * Return the appropriate schemaType builder for the supplied schema type.
     *
     * @param schemaType object with properties
     * @return builder object.
     */
    @SuppressWarnings(value = "deprecation")
    private SchemaTypeBuilder  getSchemaTypeBuilder(SchemaType  schemaType)
    {
        SchemaTypeBuilder builder = null;

        if (schemaType != null)
        {
            /*
             * The type name is extracted from the header in preference (in case it is a subtype that we do
             * not explicitly support).  If the caller has not set up the type name then it is extracted
             * from the class of the schema type.
             */
            String typeName = null;

            if (schemaType.getType() != null)
            {
                typeName = schemaType.getType().getElementTypeName();
            }

            if (typeName == null)
            {
                if (schemaType instanceof PrimitiveSchemaType)
                {
                    typeName = SchemaElementMapper.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME;
                }
                else if (schemaType instanceof EnumSchemaType)
                {
                    typeName = SchemaElementMapper.ENUM_SCHEMA_TYPE_TYPE_NAME;
                }
                else if (schemaType instanceof SimpleSchemaType)
                {
                    typeName = SchemaElementMapper.SIMPLE_SCHEMA_TYPE_TYPE_NAME;
                }
                else if (schemaType instanceof LiteralSchemaType)
                {
                    typeName = SchemaElementMapper.LITERAL_SCHEMA_TYPE_TYPE_NAME;
                }
                else if (schemaType instanceof ComplexSchemaType)
                {
                    typeName = SchemaElementMapper.COMPLEX_SCHEMA_TYPE_TYPE_NAME;
                }
                else if (schemaType instanceof MapSchemaType)
                {
                    typeName = SchemaElementMapper.MAP_SCHEMA_TYPE_TYPE_NAME;
                }
                else
                {
                    typeName = SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME;
                }
            }

            builder = new SchemaTypeBuilder(schemaType.getQualifiedName(),
                                            schemaType.getDisplayName(),
                                            schemaType.getDescription(),
                                            schemaType.getVersionNumber(),
                                            schemaType.getIsDeprecated(),
                                            schemaType.getAuthor(),
                                            schemaType.getUsage(),
                                            schemaType.getEncodingStandard(),
                                            schemaType.getNamespace(),
                                            schemaType.getAdditionalProperties(),
                                            schemaType.getAnchorGUID(),
                                            typeName,
                                            null,
                                            schemaType.getExtendedProperties(),
                                            repositoryHelper,
                                            serviceName,
                                            serverName);

            /*
             * Set up the properties that are specific to particular subtypes of schema type.
             */
            if (schemaType instanceof SimpleSchemaType)
            {
                builder.setDataType(((SimpleSchemaType) schemaType).getDataType());
                builder.setDefaultValue(((SimpleSchemaType) schemaType).getDefaultValue());
            }
            else if (schemaType instanceof LiteralSchemaType)
            {
                builder.setDataType(((LiteralSchemaType) schemaType).getDataType());
                builder.setFixedValue(((LiteralSchemaType) schemaType).getFixedValue());
            }
        }

        return builder;
    }
}
