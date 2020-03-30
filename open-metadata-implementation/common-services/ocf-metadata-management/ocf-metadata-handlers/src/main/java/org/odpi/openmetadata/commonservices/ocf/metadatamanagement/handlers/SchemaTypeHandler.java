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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
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
    public SchemaType getSchemaTypeForAsset(String   userId,
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
     * Is there an attached schema for the SchemaAttribute?
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
    public SchemaType getSchemaTypeForAttribute(String   userId,
                                                String   anchorGUID,
                                                String   methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        return this.getSchemaTypeForAnchor(userId,
                                           anchorGUID,
                                           SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                           SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                           SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME,
                                           methodName);
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
        if (schemaTypeEntity != null)
        {
            /*
             * Complex schema types have attributes attached to them
             */
            int attributeCount = 0;

            if (schemaTypeEntity.getType() != null)
            {
                if (repositoryHelper.isTypeOf(serviceName,
                                              schemaTypeEntity.getType().getTypeDefName(),
                                              SchemaElementMapper.COMPLEX_SCHEMA_TYPE_TYPE_NAME))
                {
                    attributeCount = this.countSchemaAttributes(userId,
                                                                schemaTypeEntity.getGUID(),
                                                                methodName);
                }
            }

            SchemaTypeConverter converter = new SchemaTypeConverter(schemaTypeEntity,
                                                                    attributeCount,
                                                                    repositoryHelper,
                                                                    serviceName);

            return converter.getBean();
        }

        return null;
    }


    /**
     * Turn the list of column headers into a SchemaType object.  The assumption is that all of the columns contain
     * strings.  Later analysis may update this.
     *
     * @param anchorQualifiedName unique name for the object that this schema is connected to
     * @param anchorDisplayName human-readable name for the object that this schema is connected to
     * @param author userId of author
     * @param encodingStandard internal encoding
     * @param columnHeaders  list of column headers.
     *
     * @return schema type object
     */
    public SchemaType getTabularSchemaType(String            anchorQualifiedName,
                                           String            anchorDisplayName,
                                           String            author,
                                           String            encodingStandard,
                                           List<String>      columnHeaders)
    {
        ComplexSchemaType    tableSchemaType = this.getEmptyComplexSchemaType(SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID,
                                                                              SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME);

        if (columnHeaders != null)
        {
            tableSchemaType.setAttributeCount(columnHeaders.size());
        }

        tableSchemaType.setQualifiedName(anchorQualifiedName + ":TabularSchema");
        tableSchemaType.setDisplayName(anchorDisplayName + " Tabular Schema");
        tableSchemaType.setAuthor(author);
        tableSchemaType.setVersionNumber("1.0");
        tableSchemaType.setEncodingStandard(encodingStandard);

        return tableSchemaType;
    }


    /**
     * Turn the list of column headers into a SchemaType object.  The assumption is that all of the columns contain
     * strings.  Later analysis may update this.
     *
     * @param parentSchemaQualifiedName name of the linked schema's qualified name
     * @param columnHeaders   list of column names.
     *
     * @return list of schema attribute objects
     */
    public  List<SchemaAttribute> getTabularSchemaColumns(String            parentSchemaQualifiedName,
                                                          List<String>      columnHeaders)
    {
        List<SchemaAttribute>    tableColumns = new ArrayList<>();

        if (columnHeaders != null)
        {
            int positionCount = 0;
            for (String  columnName : columnHeaders)
            {
                if (columnName != null)
                {
                    SchemaAttribute schemaAttribute = this.getEmptySchemaAttribute();

                    schemaAttribute.setQualifiedName(parentSchemaQualifiedName + ":Column:" + columnName);
                    schemaAttribute.setAttributeName(columnName);
                    schemaAttribute.setCardinality("1");
                    schemaAttribute.setElementPosition(positionCount);
                    tableColumns.add(schemaAttribute);
                    positionCount++;
                }
            }
        }

        if (tableColumns.isEmpty())
        {
            return null;
        }
        else
        {
            return tableColumns;
        }
    }


    /**
     * Count the number of connection attached to an anchor asset.
     *
     * @param userId     calling user
     * @param schemaTypeGUID identifier for the parent schema type
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countSchemaAttributes(String   userId,
                                     String   schemaTypeGUID,
                                     String   methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String guidParameterName      = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, guidParameterName, methodName);

        return repositoryHandler.countAttachedRelationshipsByType(userId,
                                                                  schemaTypeGUID,
                                                                  SchemaElementMapper.COMPLEX_SCHEMA_TYPE_TYPE_NAME,
                                                                  SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                  SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                  methodName);
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
                        attributeType = this.getSchemaTypeForAttribute(userId, attributeTypeEntity.getGUID(), methodName);
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
    public void  saveSchemaAttributes(String                userId,
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
                }
            }
        }
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
     * Add the schema type to the repository.
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

        SchemaAttributeBuilder builder = this.getSchemaAttributeBuilder(schemaAttribute);

        return repositoryHandler.createEntity(userId,
                                              this.getSchemaAttributeTypeGUID(schemaAttribute),
                                              this.getSchemaAttributeTypeName(schemaAttribute),
                                              builder.getInstanceProperties(methodName),
                                              methodName);
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

        SchemaAttributeBuilder builder = this.getSchemaAttributeBuilder(schemaAttribute);

        return repositoryHandler.createExternalEntity(userId,
                                                      this.getSchemaAttributeTypeGUID(schemaAttribute),
                                                      this.getSchemaAttributeTypeName(schemaAttribute),
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      builder.getInstanceProperties(methodName),
                                                      methodName);
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
    private SchemaAttributeBuilder  getSchemaAttributeBuilder(SchemaAttribute  schemaAttribute)
    {
        if (schemaAttribute != null)
        {
            // TODO will need different builder for DerivedSchemaAttribute
            return new SchemaAttributeBuilder(schemaAttribute.getQualifiedName(),
                                              schemaAttribute.getAttributeName(),
                                              schemaAttribute.getElementPosition(),
                                              schemaAttribute.getCardinality(),
                                              schemaAttribute.getDefaultValueOverride(),
                                              schemaAttribute.getAnchorGUID(),
                                              schemaAttribute.getAdditionalProperties(),
                                              schemaAttribute.getExtendedProperties(),
                                              repositoryHelper,
                                              serviceName,
                                              serverName);
        }

        return null;
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
     * fully qualified name.  If neither are set it throws an exception.
     *
     * @param userId calling user
     * @param schemaTypeGUID unique Id
     * @param qualifiedName unique name
     * @param displayName human readable name
     * @param methodName calling method
     *
     * @return unique identifier of the connection or null
     *
     * @throws InvalidParameterException the connection bean properties are invalid
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
     * @param schemaAttributes list of nested schema attribute objects or null
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
        String schemaTypeGUID = this.findSchemaType(userId, schemaType, methodName);

        if (schemaTypeGUID == null)
        {
            schemaTypeGUID = addSchemaType(userId, schemaType);
        }
        else
        {
            updateSchemaType(userId, schemaTypeGUID, schemaType);
        }

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
     * @param schemaAttributes list of nested schema attribute objects or null
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
                                          List<SchemaAttribute> schemaAttributes,
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

        if (schemaAttributes != null)
        {
            this.saveExternalSchemaAttributes(userId, schemaTypeGUID, schemaAttributes, externalSourceGUID,
                    externalSourceName, methodName);
        }

        return schemaTypeGUID;
    }


    /**
     * Add the schema type to the repository.
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

        return repositoryHandler.createEntity(userId,
                                              this.getSchemaTypeTypeGUID(schemaType),
                                              this.getSchemaTypeTypeName(schemaType),
                                              schemaTypeBuilder.getInstanceProperties(methodName),
                                              methodName);
    }


    /**
     * Add the schema type from an external source to the repository.
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

        return repositoryHandler.createExternalEntity(userId,
                                                      this.getSchemaTypeTypeGUID(schemaType),
                                                      this.getSchemaTypeTypeName(schemaType),
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      schemaTypeBuilder.getInstanceProperties(methodName),
                                                      methodName);
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
     * Return the schemaType builder for bounded schema types such as sets and arrays.
     *
     * @param schemaType object with properties
     * @return builder object.
     */
    private SchemaTypeBuilder  getBoundedSchemaTypeBuilder(BoundedSchemaType  schemaType)
    {
        return  new BoundedSchemaTypeBuilder(schemaType.getQualifiedName(),
                                             schemaType.getDisplayName(),
                                             schemaType.getVersionNumber(),
                                             schemaType.getAuthor(),
                                             schemaType.getUsage(),
                                             schemaType.getEncodingStandard(),
                                             schemaType.getMaximumElements(),
                                             schemaType.getAdditionalProperties(),
                                             schemaType.getExtendedProperties(),
                                             repositoryHelper,
                                             serviceName,
                                             serverName);
    }


    /**
     * Return the schemaType builder for complex schema types such as tables.
     *
     * @param schemaType object with properties
     * @return builder object.
     */
    private SchemaTypeBuilder  getComplexSchemaTypeBuilder(ComplexSchemaType  schemaType)
    {
        return  new ComplexSchemaTypeBuilder(schemaType.getQualifiedName(),
                                             schemaType.getDisplayName(),
                                             schemaType.getVersionNumber(),
                                             schemaType.getAuthor(),
                                             schemaType.getUsage(),
                                             schemaType.getEncodingStandard(),
                                             schemaType.getAdditionalProperties(),
                                             schemaType.getExtendedProperties(),
                                             repositoryHelper,
                                             serviceName,
                                             serverName);
    }


    /**
     * Return the schemaType builder for primitive schema types such as strings and numbers.
     *
     * @param schemaType object with properties
     * @return builder object.
     */
    private SchemaTypeBuilder  getPrimitiveSchemaTypeBuilder(PrimitiveSchemaType  schemaType)
    {
        return  new PrimitiveSchemaTypeBuilder(schemaType.getQualifiedName(),
                                               schemaType.getDisplayName(),
                                               schemaType.getVersionNumber(),
                                               schemaType.getAuthor(),
                                               schemaType.getUsage(),
                                               schemaType.getEncodingStandard(),
                                               schemaType.getDataType(),
                                               schemaType.getDefaultValue(),
                                               schemaType.getAdditionalProperties(),
                                               schemaType.getExtendedProperties(),
                                               repositoryHelper,
                                               serviceName,
                                               serverName);
    }



    /**
     * Return the appropriate schemaType builder for the supplied schema type.
     *
     * @param schemaType object with properties
     * @return builder object.
     */
    private SchemaTypeBuilder  getSchemaTypeBuilder(SchemaType  schemaType)
    {
        if (schemaType != null)
        {
            if (schemaType instanceof PrimitiveSchemaType)
            {
                return getPrimitiveSchemaTypeBuilder((PrimitiveSchemaType) schemaType);
            }
            else if (schemaType instanceof BoundedSchemaType)
            {
                return getBoundedSchemaTypeBuilder((BoundedSchemaType) schemaType);
            }
            else if (schemaType instanceof ComplexSchemaType)
            {
                return getComplexSchemaTypeBuilder((ComplexSchemaType) schemaType);
            }
            else
            {
                return new SchemaTypeBuilder(schemaType.getQualifiedName(),
                                             schemaType.getDisplayName(),
                                             schemaType.getVersionNumber(),
                                             schemaType.getAuthor(),
                                             schemaType.getUsage(),
                                             schemaType.getEncodingStandard(),
                                             schemaType.getAdditionalProperties(),
                                             schemaType.getExtendedProperties(),
                                             repositoryHelper,
                                             serviceName,
                                             serverName);
            }
        }

        return null;
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
