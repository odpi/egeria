/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.*;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.SchemaAttributeConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.SchemaTypeConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * SchemaTypeHandler manages SchemaType objects.  It runs server-side in
 * the OMAG Server Platform and retrieves SchemaType entities through the OMRSRepositoryConnector.
 */
public class SchemaTypeHandler
{
    private String                  serviceName;
    private String                  serverName;
    private OMRSRepositoryHelper    repositoryHelper;
    private RepositoryHandler       repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;
    private LastAttachmentHandler   lastAttachmentHandler;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param lastAttachmentHandler handler for recording last attachment
     */
    public SchemaTypeHandler(String                  serviceName,
                             String                  serverName,
                             InvalidParameterHandler invalidParameterHandler,
                             RepositoryHandler       repositoryHandler,
                             OMRSRepositoryHelper    repositoryHelper,
                             LastAttachmentHandler   lastAttachmentHandler)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
        this.lastAttachmentHandler = lastAttachmentHandler;
    }


    private void setElementType(String        schemaTypeGUID,
                                String        schemaTypeName,
                                SchemaElement bean)
    {
        ElementType elementType = new ElementType();


        elementType.setElementOrigin(ElementOrigin.LOCAL_COHORT);
        elementType.setElementTypeId(schemaTypeGUID);
        elementType.setElementTypeName(schemaTypeName);

        bean.setType(elementType);
    }


    /**
     * Return the requested schemaType with the specific type information filled in.
     *
     * @param schemaTypeGUID unique identifier of the required type
     * @param schemaTypeName unique name of the required type
     * @return new object
     */
    public ComplexSchemaType getEmptyComplexSchemaType(String     schemaTypeGUID,
                                                       String     schemaTypeName)
    {
        ComplexSchemaType  schemaType = new ComplexSchemaType();

        setElementType(schemaTypeGUID, schemaTypeName, schemaType);

        return schemaType;
    }


    /**
     * Return the requested schemaType with the specific type information filled in.
     *
     * @param schemaTypeGUID unique identifier of the required type
     * @param schemaTypeName unique name of the required type
     * @return new object
     */
    public PrimitiveSchemaType getEmptyPrimitiveSchemaType(String     schemaTypeGUID,
                                                           String     schemaTypeName)
    {
        PrimitiveSchemaType  schemaType = new PrimitiveSchemaType();

        setElementType(schemaTypeGUID, schemaTypeName, schemaType);

        return schemaType;
    }


    /**
     * Return a schema attribute object with the type set up.
     *
     * @return new object
     */
    public SchemaAttribute  getEmptySchemaAttribute()
    {
        SchemaAttribute   schemaAttribute = new SchemaAttribute();

        setElementType(SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
                       SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                       schemaAttribute);

        return schemaAttribute;
    }

    /**
     * Return a schema attribute object with the type set up.
     *
     * @return new object
     */
    public SchemaAttribute  getEmptyTabularColumn()
    {
        SchemaAttribute   schemaAttribute = new SchemaAttribute();

        setElementType(SchemaElementMapper.TABULAR_COLUMN_TYPE_GUID,
                       SchemaElementMapper.TABULAR_COLUMN_TYPE_NAME,
                       schemaAttribute);

        return schemaAttribute;
    }



    /**
     * Is there an attached schema for the Asset?
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     *
     * @return schemaType object or null
     *
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    SchemaType getSchemaTypeForAsset(String   userId,
                                     String   anchorGUID,
                                     String   methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        return this.getSchemaTypeForAnchor(userId,
                                           anchorGUID,
                                           AssetMapper.ASSET_TYPE_NAME,
                                           AssetMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                           AssetMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                           methodName);
    }



    /**
     * Is there an attached schema for the SchemaAttribute? it may be in the classification of the schema attribute
     * or linked via a relationship.
     *
     * @param userId     calling user
     * @param schemaAttributeEntity the schema attribute entity that the potential schema type is attached to
     * @param methodName calling method
     *
     * @return schemaType object or null
     *
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private SchemaType getSchemaTypeForAttribute(String       userId,
                                                 EntityDetail schemaAttributeEntity,
                                                 String       methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        if (schemaAttributeEntity != null)
        {
            List<Classification> classifications = schemaAttributeEntity.getClassifications();

            if (classifications != null)
            {
                for (Classification classification : classifications)
                {
                    if (classification != null)
                    {
                        if (SchemaElementMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            return this.getSchemaTypeFromClassification(userId,
                                                                        schemaAttributeEntity.getGUID(),
                                                                        classification,
                                                                        methodName);
                        }
                    }
                }
            }

            return this.getSchemaTypeForAnchor(userId,
                                                   schemaAttributeEntity.getGUID(),
                                                   SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                   SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                   SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME,
                                                   methodName);
        }

        return null;
    }



    /**
     * Is there an attached schema for the anchor entity?
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param anchorTypeName type name of anchor
     * @param relationshipTypeGUID unique identifier of the relationship type to search along
     * @param relationshipTypeName unique name of the relationship type to search along
     * @param methodName calling method
     *
     * @return schemaType object or null
     *
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private SchemaType getSchemaTypeForAnchor(String   userId,
                                              String   anchorGUID,
                                              String   anchorTypeName,
                                              String   relationshipTypeGUID,
                                              String   relationshipTypeName,
                                              String   methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        EntityDetail  schemaTypeEntity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                                        anchorGUID,
                                                                                        anchorTypeName,
                                                                                        relationshipTypeGUID,
                                                                                        relationshipTypeName,
                                                                                        methodName);

        return getSchemaTypeFromEntity(userId, schemaTypeEntity, methodName);
    }


    /**
     * Retrieve a specific schema type based on its unique identifier (GUID).  This is use to do updates
     * and to retrieve a linked schema.
     *
     * @param userId calling user
     * @param schemaTypeGUID guid of schema type to retrieve.
     * @param guidParameterName parameter describing where the guid came from
     * @param methodName calling method
     *
     * @return schema type or null depending on whether the object is found
     * @throws InvalidParameterException  the guid is null
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private SchemaType getSchemaType(String   userId,
                                     String   schemaTypeGUID,
                                     String   guidParameterName,
                                     String   methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        EntityDetail  schemaTypeEntity = repositoryHandler.getEntityByGUID(userId,
                                                                           schemaTypeGUID,
                                                                           guidParameterName,
                                                                           SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                           methodName);

        return getSchemaTypeFromEntity(userId, schemaTypeEntity, methodName);
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
                                                                        serviceName);

                return this.getEmbeddedTypes(userId, schemaTypeEntity.getGUID(), converter.getBean(), methodName);
            }
        }

        return null;
    }


    /**
     * Transform a schema type entity into a schema type bean.  To completely fill out the schema type
     * it may be necessary to
     *
     * @param userId calling user
     * @param embeddedSchemaType entity retrieved from the repository
     * @param methodName calling method
     *
     * @return schema type bean
     * @throws InvalidParameterException problem with the entity
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private SchemaType getSchemaTypeFromClassification(String         userId,
                                                       String         schemaAttributeGUID,
                                                       Classification embeddedSchemaType,
                                                       String         methodName) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String guidParameterName = "schemaAttributeGUID";

        if ((embeddedSchemaType != null) && (embeddedSchemaType.getProperties() != null))
        {
            InstanceProperties properties = embeddedSchemaType.getProperties();
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
                                                                    schemaAttributeGUID,
                                                                    guidParameterName,
                                                                    methodName);
                    }
                }

                SchemaTypeConverter converter = new SchemaTypeConverter(typeName,
                                                                        properties,
                                                                        attributeCount,
                                                                        repositoryHelper,
                                                                        serviceName);

                return this.getEmbeddedTypes(userId, schemaAttributeGUID, converter.getBean(), methodName);
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
        else if (parentSchemaType instanceof BoundedSchemaType)
        {
            EntityDetail entity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                                 schemaElementGUID,
                                                                                 SchemaElementMapper.BOUNDED_SCHEMA_TYPE_TYPE_NAME,
                                                                                 SchemaElementMapper.BOUNDED_ELEMENT_RELATIONSHIP_TYPE_GUID,
                                                                                 SchemaElementMapper.BOUNDED_ELEMENT_RELATIONSHIP_TYPE_NAME,
                                                                                 methodName);

            if (entity != null)
            {
                ((BoundedSchemaType) parentSchemaType).setElementType(this.getSchemaTypeFromEntity(userId, entity, methodName));
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
    private int countSchemaAttributes(String   userId,
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
     * Returns a list of schema attributes for a schema type.
     *
     * @param userId         String   userId of user making request.
     * @param schemaTypeGUID String   unique id for containing schema type.
     * @param elementStart   int      starting position for first returned element.
     * @param maxElements    int      maximum number of elements to return on the call.
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SchemaAttribute> getSchemaAttributes(String  userId,
                                                     String  schemaTypeGUID,
                                                     int     elementStart,
                                                     int     maxElements,
                                                     String  methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String guidParameterName      = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, guidParameterName, methodName);

        List<EntityDetail>  entities = repositoryHandler.getEntitiesForRelationshipType(userId,
                                                                                        schemaTypeGUID,
                                                                                        SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                                        SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                                        SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                                        elementStart,
                                                                                        maxElements,
                                                                                        methodName);

        List<SchemaAttribute>  results = new ArrayList<>();

        if (entities != null)
        {
            for (EntityDetail schemaAttributeEntity : entities)
            {
                if (schemaAttributeEntity != null)
                {
                    EntityDetail  attributeTypeEntity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                                                       schemaAttributeEntity.getGUID(),
                                                                                                       SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                                                       SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                                                                       SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME,
                                                                                                       methodName);
                    SchemaType   attributeType = null;
                    if (attributeTypeEntity != null)
                    {
                        attributeType = this.getSchemaTypeForAttribute(userId, attributeTypeEntity, methodName);
                    }

                    SchemaAttributeConverter converter = new SchemaAttributeConverter(schemaAttributeEntity,
                                                                                      attributeType,
                                                                                      null,  // TODO
                                                                                      null,  // TODO
                                                                                      repositoryHelper,
                                                                                      serviceName);
                    results.add(converter.getBean());
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Work through the schema attributes adding or updating the instances
     *
     * @param userId calling userId
     * @param schemaTypeGUID anchor object
     * @param schemaAttributes list of nested schema attribute objects or null
     * @param methodName calling method
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void saveSchemaAttributes(String                userId,
                                     String                schemaTypeGUID,
                                     List<SchemaAttribute> schemaAttributes,
                                     String                methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        if (schemaAttributes != null)
        {
            for (SchemaAttribute  schemaAttribute : schemaAttributes)
            {
                if (schemaAttribute != null)
                {
                    saveSchemaTypeAttribute(userId, schemaTypeGUID, schemaAttribute, methodName);
                }
            }
        }
    }


    /**
     * Work through the schema attributes adding or updating the instances
     *
     * @param userId calling userId
     * @param parentGUID anchor object
     * @param schemaAttribute schema attribute object with embedded type
     * @param methodName calling method
     * @return guid of new schema
     *
     * @throws InvalidParameterException the guid or bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String saveSchemaAttribute(String          userId,
                                      String          parentGUID,
                                      SchemaAttribute schemaAttribute,
                                      String          methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String guidParameterName = "parentGUID";

        invalidParameterHandler.validateGUID(parentGUID, guidParameterName, methodName);

        EntityDetail parentEntity = repositoryHandler.getEntityByGUID(userId,
                                                                      parentGUID,
                                                                      guidParameterName,
                                                                      SchemaElementMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                      methodName);

        if (parentEntity != null)
        {
            String       typeName = null;
            InstanceType type = parentEntity.getType();

            if (type != null)
            {
                typeName = type.getTypeDefName();
            }

            if (repositoryHelper.isTypeOf(serviceName, typeName, SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME))
            {
                return saveSchemaTypeAttribute(userId, parentGUID, schemaAttribute, methodName);
            }
            else
            {
                return saveNestedSchemaAttribute(userId, parentGUID, schemaAttribute, methodName);
            }
        }
        else
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        parentGUID,
                                                        SchemaElementMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
            return null;
        }
    }



    /**
     * Work through the schema attributes adding or updating the instances
     *
     * @param userId calling userId
     * @param schemaTypeGUID anchor object
     * @param schemaAttribute schema attribute object with embedded type
     * @param methodName calling method
     * @return guid of new schema
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String saveSchemaTypeAttribute(String          userId,
                                           String          schemaTypeGUID,
                                           SchemaAttribute schemaAttribute,
                                           String          methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        String schemaAttributeGUID = this.findSchemaAttribute(userId, schemaAttribute, methodName);

        if (schemaAttributeGUID == null)
        {
            schemaAttributeGUID = addSchemaAttribute(userId, schemaAttribute);

            repositoryHandler.createRelationship(userId,
                                                 SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                 schemaTypeGUID,
                                                 schemaAttributeGUID,
                                                 null,
                                                 methodName);
        }
        else
        {
            updateSchemaAttribute(userId, schemaAttributeGUID, schemaAttribute);
        }

        return schemaAttributeGUID;
    }


    /**
     * Work through the schema attributes adding or updating the instances
     *
     * @param userId calling userId
     * @param parentSchemaAttributeGUID anchor object
     * @param nestedSchemaAttribute schema attribute object with embedded type
     * @param methodName calling method
     * @return guid of new schema
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String saveNestedSchemaAttribute(String          userId,
                                             String          parentSchemaAttributeGUID,
                                             SchemaAttribute nestedSchemaAttribute,
                                             String          methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        String schemaAttributeGUID = this.findSchemaAttribute(userId, nestedSchemaAttribute, methodName);

        if (schemaAttributeGUID == null)
        {
            schemaAttributeGUID = addSchemaAttribute(userId, nestedSchemaAttribute);

            repositoryHandler.createRelationship(userId,
                                                 SchemaElementMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                 parentSchemaAttributeGUID,
                                                 schemaAttributeGUID,
                                                 null,
                                                 methodName);
        }
        else
        {
            updateSchemaAttribute(userId, schemaAttributeGUID, nestedSchemaAttribute);
        }

        return schemaAttributeGUID;
    }



    /**
     * Work through the schema attributes from an external source adding or updating the instances
     *
     * @param userId calling userId
     * @param schemaTypeGUID anchor object
     * @param schemaAttributes list of nested schema attribute objects or null
     * @param externalSourceGUID unique identifier of the external source
     * @param externalSourceName unique name of the external source
     * @param methodName calling method
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void  saveExternalSchemaAttributes(String                userId,
                                              String                schemaTypeGUID,
                                              List<SchemaAttribute> schemaAttributes,
                                              String                externalSourceGUID,
                                              String                externalSourceName,
                                              String                methodName) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        if (schemaAttributes != null)
        {
            for (SchemaAttribute  schemaAttribute : schemaAttributes)
            {
                if (schemaAttribute != null)
                {
                    String schemaAttributeGUID = this.findSchemaAttribute(userId, schemaAttribute, methodName);

                    if (schemaAttributeGUID == null)
                    {
                        schemaAttributeGUID = addExternalSchemaAttribute(userId, schemaAttribute,externalSourceGUID,externalSourceName);

                        repositoryHandler.createExternalRelationship(userId,
                                                                     SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                     externalSourceGUID,
                                                                     externalSourceName,
                                                                     schemaTypeGUID,
                                                                     schemaAttributeGUID,
                                                                     null,
                                                                     methodName);
                    }
                    else
                    {
                        updateSchemaAttribute(userId, schemaAttributeGUID, schemaAttribute);
                    }

                }
            }
        }
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
     * Find out if the SchemaType object is already stored in the repository.  If the schemaAttribute's
     * guid is set, it uses it to retrieve the entity.  If the GUID is not set, it tries the
     * fully qualified name.  If neither are set it throws an exception.
     *
     * @param userId calling user
     * @param schemaAttribute object to find
     * @param methodName calling method
     *
     * @return unique identifier of the schemaAttribute or null
     *
     * @throws InvalidParameterException the schemaAttribute bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String findSchemaAttribute(String               userId,
                                       SchemaAttribute      schemaAttribute,
                                       String               methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        if (schemaAttribute != null)
        {
            return this.findSchemaAttribute(userId,
                                       schemaAttribute.getGUID(),
                                       schemaAttribute.getQualifiedName(),
                                       schemaAttribute.getAttributeName(),
                                       methodName);
        }

        return null;
    }


    /**
     * Add the schema attribute to the repository.  Notice there is no attempt to process its type.
     *
     * @param userId   calling userId
     * @param schemaAttribute object to add
     * @return unique identifier of the schemaAttribute in the repository.
     * @throws InvalidParameterException  the schemaAttribute bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private String addSchemaAttribute(String               userId,
                                      SchemaAttribute      schemaAttribute) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "addSchemaAttribute";

        SchemaAttributeBuilder schemaAttributeBuilder = this.getSchemaAttributeBuilder(schemaAttribute);

        String schemaAttributeGUID = repositoryHandler.createEntity(userId,
                                                                    this.getSchemaAttributeTypeGUID(schemaAttribute),
                                                                    this.getSchemaAttributeTypeName(schemaAttribute),
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
                                                 schemaAttributeGUID,
                                                 SchemaElementMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_GUID,
                                                 SchemaElementMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                                                 schemaTypeBuilder.getInstanceProperties(methodName),
                                                 methodName);
            }
            else
            {
                String schemaTypeGUID = addSchemaType(userId, schemaType);
                if (schemaTypeGUID != null)
                {
                    repositoryHandler.createRelationship(userId,
                                                         SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                         schemaAttributeGUID,
                                                         schemaTypeGUID,
                                                         null,
                                                         methodName);
                }
            }
        }
        else if (schemaAttribute.getExternalAttributeType() != null)
        {
            final String guidParameterName = "schemaAttribute.getExternalAttributeType().getLinkedSchemaTypeGUID()";
            SchemaLink schemaLink = schemaAttribute.getExternalAttributeType();

            SchemaType linkedType = this.getSchemaType(userId,
                                                       schemaLink.getLinkedSchemaTypeGUID(),
                                                       guidParameterName,
                                                       methodName);

            if (linkedType != null)
            {
                SchemaLinkBuilder builder = new SchemaLinkBuilder(schemaLink.getQualifiedName(),
                                                                  schemaLink.getDisplayName(),
                                                                  repositoryHelper,
                                                                  serviceName,
                                                                  serverName);

                String schemaLinkGUID = repositoryHandler.createEntity(userId,
                                                                       SchemaElementMapper.SCHEMA_LINK_TYPE_GUID,
                                                                       SchemaElementMapper.SCHEMA_LINK_TYPE_NAME,
                                                                       builder.getInstanceProperties(methodName),
                                                                       methodName);

                if (schemaLinkGUID != null)
                {
                    repositoryHandler.createRelationship(userId,
                                                         SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                         schemaLinkGUID,
                                                         schemaType.getGUID(),
                                                         null,
                                                         methodName);
                }
            }
        }

        return schemaAttributeGUID;
    }


    /**
     * Add the schema type from an external source to the repository.
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
    public String addExternalSchemaAttribute(String               userId,
                                             SchemaAttribute      schemaAttribute,
                                             String               externalSourceGUID,
                                             String               externalSourceName) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "addExternalSchemaAttribute";

        SchemaAttributeBuilder schemaAttributeBuilder = this.getSchemaAttributeBuilder(schemaAttribute);

        String schemaAttributeGUID = repositoryHandler.createExternalEntity(userId,
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
                                                 schemaAttributeGUID,
                                                 SchemaElementMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_GUID,
                                                 SchemaElementMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                                                 schemaTypeBuilder.getInstanceProperties(methodName),
                                                 methodName);
            }
            else
            {
                String schemaTypeGUID = addSchemaType(userId, schemaType);
                if (schemaTypeGUID != null)
                {
                    repositoryHandler.createExternalRelationship(userId,
                                                                 SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 schemaAttributeGUID,
                                                                 schemaTypeGUID,
                                                                 null,
                                                                 methodName);
                }
            }
        }
        else if (schemaAttribute.getExternalAttributeType() != null)
        {
            final String guidParameterName = "schemaAttribute.getExternalAttributeType().getLinkedSchemaTypeGUID()";
            SchemaLink schemaLink = schemaAttribute.getExternalAttributeType();

            SchemaType linkedType = this.getSchemaType(userId,
                                                       schemaLink.getLinkedSchemaTypeGUID(),
                                                       guidParameterName,
                                                       methodName);

            if (linkedType != null)
            {
                SchemaLinkBuilder builder = new SchemaLinkBuilder(schemaLink.getQualifiedName(),
                                                                  schemaLink.getDisplayName(),
                                                                  repositoryHelper,
                                                                  serviceName,
                                                                  serverName);

                String schemaLinkGUID = repositoryHandler.createExternalEntity(userId,
                                                                               SchemaElementMapper.SCHEMA_LINK_TYPE_GUID,
                                                                               SchemaElementMapper.SCHEMA_LINK_TYPE_NAME,
                                                                               externalSourceGUID,
                                                                               externalSourceName,
                                                                               builder.getInstanceProperties(methodName),
                                                                               methodName);

                if (schemaLinkGUID != null)
                {
                    repositoryHandler.createExternalRelationship(userId,
                                                                 SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 schemaLinkGUID,
                                                                 schemaType.getGUID(),
                                                                 null,
                                                                 methodName);
                }
            }
        }

        return schemaAttributeGUID;
    }


    /**
     * Update a stored schemaAttribute.
     *
     * @param userId                      userId
     * @param existingSchemaAttributeGUID unique identifier of the existing schemaAttribute entity
     * @param schemaAttribute             new schemaAttribute values
     * @return unique identifier of the schemaAttribute in the repository.
     * @throws InvalidParameterException  the schemaAttribute bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
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
            builder = new SchemaAttributeBuilder(schemaAttribute.getQualifiedName(),
                                                 schemaAttribute.getAttributeName(),
                                                 schemaAttribute.getDescription(),
                                                 schemaAttribute.getElementPosition(),
                                                 schemaAttribute.getMinCardinality(),
                                                 schemaAttribute.getMaxCardinality(),
                                                 schemaAttribute.isDeprecated(),
                                                 schemaAttribute.getDefaultValueOverride(),
                                                 schemaAttribute.isAllowsDuplicateValues(),
                                                 schemaAttribute.isOrderedValues(),
                                                 schemaAttribute.getSortOrder(),
                                                 schemaAttribute.getAnchorGUID(),
                                                 schemaAttribute.getMinimumLength(),
                                                 schemaAttribute.getLength(),
                                                 schemaAttribute.getSignificantDigits(),
                                                 schemaAttribute.isNullable(),
                                                 schemaAttribute.getNativeJavaClass(),
                                                 schemaAttribute.getAliases(),
                                                 schemaAttribute.getAdditionalProperties(),
                                                 schemaAttribute.getExtendedProperties(),
                                                 repositoryHelper,
                                                 serviceName,
                                                 serverName);

            /*
             * Extract additional properties if this is a derived schema attribute.
             */
            if (schemaAttribute instanceof DerivedSchemaAttribute)
            {
                DerivedSchemaAttribute derivedSchemaAttribute = (DerivedSchemaAttribute) schemaAttribute;

                builder.setFormula(derivedSchemaAttribute.getFormula());
            }
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
     * Find out if the SchemaType object is already stored in the repository.  If the schemaType's
     * guid is set, it uses it to retrieve the entity.  If the GUID is not set, it tries the
     * fully qualified name.  If neither are set it throws an exception.
     *
     * @param userId calling user
     * @param schemaType object to find
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType or null
     *
     * @throws InvalidParameterException the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String findSchemaType(String               userId,
                                  SchemaType           schemaType,
                                  String               methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        if (schemaType != null)
        {
            return this.findSchemaType(userId,
                                       schemaType.getGUID(),
                                       schemaType.getQualifiedName(),
                                       schemaType.getDisplayName(),
                                       methodName);
        }

        return null;
    }


    /**
     * Determine if the SchemaType object is stored in the repository and create it if it is not.
     * If the schemaType is located, there is no check that the schemaType values are equal to those in
     * the supplied object.
     *
     * @param userId calling userId
     * @param schemaType object to add
     * @param schemaAttributes list of nested schema attribute objects or null. These attributes can not have
     *                         attributes nested themselves because the GUID is not returned to the caller.
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  saveSchemaType(String                userId,
                                  SchemaType            schemaType,
                                  List<SchemaAttribute> schemaAttributes,
                                  String                methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        String schemaTypeGUID = this.saveSchemaType(userId, schemaType, methodName);

        if (schemaAttributes != null)
        {
            this.saveSchemaAttributes(userId, schemaTypeGUID, schemaAttributes, methodName);
        }

        return schemaTypeGUID;
    }


    /**
     * Determine if the SchemaType object is stored in the repository and create it if it is not.
     * If the schemaType is located, there is no check that the schemaType values are equal to those in
     * the supplied object.
     *
     * @param userId calling userId
     * @param schemaType object to add
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  saveSchemaType(String                userId,
                                  SchemaType            schemaType,
                                  String                methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        String schemaTypeGUID = this.findSchemaType(userId, schemaType, methodName);

        if (schemaTypeGUID == null)
        {
            schemaTypeGUID = addSchemaType(userId, schemaType);
        }
        else
        {
            updateSchemaType(userId, schemaTypeGUID, schemaType);
        }

        return schemaTypeGUID;
    }


    /**
     * Determine if the SchemaType object is stored in the repository and create it if it is not.
     * If the schemaType is located, there is no check that the schemaType values are equal to those in
     * the supplied object.
     *
     * @param userId calling userId
     * @param schemaType object to add
     * @param externalSourceGUID unique identifier(guid) for the external source
     * @param externalSourceName unique name for the external source
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  saveExternalSchemaType(String                userId,
                                          SchemaType            schemaType,
                                          String                externalSourceGUID,
                                          String                externalSourceName,
                                          String                methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        String schemaTypeGUID = this.findSchemaType(userId, schemaType, methodName);

        if (schemaTypeGUID == null)
        {
            schemaTypeGUID = addExternalSchemaType(userId, schemaType, externalSourceGUID, externalSourceName);
        }
        else
        {
            updateSchemaType(userId, schemaTypeGUID, schemaType);
        }

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
                                 SchemaType            schemaType) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "addSchemaType";

        SchemaTypeBuilder schemaTypeBuilder = this.getSchemaTypeBuilder(schemaType);

        String schemaTypeGUID = repositoryHandler.createEntity(userId,
                                                               this.getSchemaTypeTypeGUID(schemaType),
                                                               this.getSchemaTypeTypeName(schemaType),
                                                               schemaTypeBuilder.getInstanceProperties(methodName),
                                                               methodName);

        return addEmbeddedTypes(userId, schemaTypeGUID, schemaType);
    }


    /**
     * Add additional schema types that are part of a schema type.
     *
     * @param userId calling user
     * @param schemaTypeGUID schema type entity to link to
     * @param schemaType description of complete schema type.
     * @return schemaTypeGUID
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @SuppressWarnings(value = "deprecation")
    private String addEmbeddedTypes(String     userId,
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
                String mapFromGUID = addSchemaType(userId, mapFrom);

                if (mapFromGUID != null)
                {
                    repositoryHandler.createRelationship(userId,
                                                         SchemaElementMapper.MAP_FROM_RELATIONSHIP_TYPE_NAME,
                                                         schemaTypeGUID,
                                                         mapFromGUID,
                                                         null,
                                                         methodName);
                }
            }

            if (mapTo != null)
            {
                String mapToGUID = addSchemaType(userId, mapTo);

                if (mapToGUID != null)
                {
                    repositoryHandler.createRelationship(userId,
                                                         SchemaElementMapper.MAP_TO_RELATIONSHIP_TYPE_NAME,
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
                        String optionGUID = addSchemaType(userId, option);

                        if (optionGUID != null)
                        {
                            repositoryHandler.createRelationship(userId,
                                                                 SchemaElementMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                                                 schemaTypeGUID,
                                                                 optionGUID,
                                                                 null,
                                                                 methodName);
                        }
                    }
                }
            }
        }
        else if (schemaType instanceof BoundedSchemaType)
        {
            SchemaType elementType = ((BoundedSchemaType) schemaType).getElementType();

            if (elementType != null)
            {
                String elementTypeGUID = addSchemaType(userId, elementType);

                if (elementTypeGUID != null)
                {
                    repositoryHandler.createRelationship(userId,
                                                         SchemaElementMapper.BOUNDED_ELEMENT_RELATIONSHIP_TYPE_GUID,
                                                         schemaTypeGUID,
                                                         elementTypeGUID,
                                                         null,
                                                         methodName);
                }
            }
        }

        return schemaTypeGUID;
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
        final String methodName = "addExternalSchemaType";

        SchemaTypeBuilder schemaTypeBuilder = this.getSchemaTypeBuilder(schemaType);

        String schemaTypeGUID = repositoryHandler.createExternalEntity(userId,
                                                                       this.getSchemaTypeTypeGUID(schemaType),
                                                                       this.getSchemaTypeTypeName(schemaType),
                                                                       externalSourceGUID,
                                                                       externalSourceName,
                                                                       schemaTypeBuilder.getInstanceProperties(methodName),
                                                                       methodName);

        return addExternalEmbeddedTypes(userId, schemaTypeGUID, schemaType, externalSourceGUID, externalSourceName);
    }


    /**
     * Add additional schema types that are part of an external schema type.
     *
     * @param userId calling user
     * @param schemaTypeGUID schema type entity to link to
     * @param schemaType description of complete schema type.
     * @param externalSourceGUID unique identifier(guid) for the external source
     * @param externalSourceName unique name for the external source
     * @return schemaTypeGUID
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @SuppressWarnings(value = "deprecation")
    private String addExternalEmbeddedTypes(String                userId,
                                            String                schemaTypeGUID,
                                            SchemaType            schemaType,
                                            String                externalSourceGUID,
                                            String                externalSourceName) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "addExternalEmbeddedTypes";

        if (schemaType instanceof MapSchemaType)
        {
            SchemaType mapFrom = ((MapSchemaType) schemaType).getMapFromElement();
            SchemaType mapTo   = ((MapSchemaType) schemaType).getMapToElement();

            if (mapFrom != null)
            {
                String mapFromGUID = addExternalSchemaType(userId, mapFrom, externalSourceGUID, externalSourceName);

                if (mapFromGUID != null)
                {
                    repositoryHandler.createExternalRelationship(userId,
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
                String mapToGUID = addExternalSchemaType(userId, mapTo, externalSourceGUID, externalSourceName);

                if (mapToGUID != null)
                {
                    repositoryHandler.createExternalRelationship(userId,
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
                        String optionGUID = addExternalSchemaType(userId, option, externalSourceGUID, externalSourceName);

                        if (optionGUID != null)
                        {
                            repositoryHandler.createExternalRelationship(userId,
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
        else if (schemaType instanceof BoundedSchemaType)
        {
            SchemaType elementType = ((BoundedSchemaType) schemaType).getElementType();

            if (elementType != null)
            {
                String elementTypeGUID = addExternalSchemaType(userId, elementType, externalSourceGUID, externalSourceName);

                if (elementTypeGUID != null)
                {
                    repositoryHandler.createExternalRelationship(userId,
                                                                 SchemaElementMapper.BOUNDED_ELEMENT_RELATIONSHIP_TYPE_GUID,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 schemaTypeGUID,
                                                                 elementTypeGUID,
                                                                 null,
                                                                 methodName);
                }
            }
        }

        return schemaTypeGUID;
    }



    /**
     * Update a stored schemaType.
     *
     * @param userId               userId
     * @param existingSchemaTypeGUID unique identifier of the existing schemaType entity
     * @param schemaType             new schemaType values
     * @return unique identifier of the schemaType in the repository.
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
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
            builder = new SchemaTypeBuilder(SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                            schemaType.getQualifiedName(),
                                            schemaType.getDisplayName(),
                                            schemaType.getDescription(),
                                            schemaType.getVersionNumber(),
                                            schemaType.isDeprecated(),
                                            schemaType.getAuthor(),
                                            schemaType.getUsage(),
                                            schemaType.getEncodingStandard(),
                                            schemaType.getNamespace(),
                                            schemaType.getAdditionalProperties(),
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
            else if (schemaType instanceof BoundedSchemaType)
            {
                builder.setMaximumElements(((BoundedSchemaType) schemaType).getMaximumElements());
            }

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
                else if (schemaType instanceof BoundedSchemaType)
                {
                    typeName = SchemaElementMapper.BOUNDED_SCHEMA_TYPE_TYPE_NAME;
                }
                else
                {
                    typeName = SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME;
                }
            }

            builder.setTypeName(typeName);
        }

        return builder;
    }



    /**
     * Remove the requested schemaType if it is no longer connected to any other entity.
     *
     * @param userId       calling user
     * @param schemaTypeGUID object to delete
     * @throws InvalidParameterException  the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void removeSchemaType(String userId,
                                 String schemaTypeGUID) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String methodName        = "removeSchemaType";
        final String guidParameterName = "schemaTypeGUID";

        repositoryHandler.removeEntityOnLastUse(userId,
                                                schemaTypeGUID,
                                                guidParameterName,
                                                SchemaElementMapper.SCHEMA_TYPE_TYPE_GUID,
                                                SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                                                methodName);
    }



    /**
     * Remove the requested schemaAttribute if it is no longer connected to any other entity.
     *
     * @param userId       calling user
     * @param schemaAttributeGUID object to delete
     * @throws InvalidParameterException  the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void removeSchemaAttribute(String userId,
                                      String schemaAttributeGUID) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName        = "removeSchemaAttribute";
        final String guidParameterName = "schemaAttributeGUID";

        repositoryHandler.removeEntityOnLastUse(userId,
                                                schemaAttributeGUID,
                                                guidParameterName,
                                                SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                                SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                methodName);
    }

}
