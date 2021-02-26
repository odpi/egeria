/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * SchemaTypeHandler manages SchemaType objects.  It runs server-side in
 * the OMAG Server Platform and retrieves SchemaElement entities through the OMRSRepositoryConnector.
 */
public class SchemaTypeHandler<B> extends SchemaElementHandler<B>
{
    private OpenMetadataAPIGenericConverter<B> schemaTypeConverter;

    /**
     * Construct the asset handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from.
     * @param defaultZones list of zones that the access service should set in all new B instances.
     * @param publishZones list of zones that the access service sets up in published B instances.
     * @param auditLog destination for audit log events.
     */
    public SchemaTypeHandler(OpenMetadataAPIGenericConverter<B>       converter,
                             Class<B>                                 beanClass,
                             String                                   serviceName,
                             String                                   serverName,
                             InvalidParameterHandler                  invalidParameterHandler,
                             RepositoryHandler                        repositoryHandler,
                             OMRSRepositoryHelper                     repositoryHelper,
                             String                                   localServerUserId,
                             OpenMetadataServerSecurityVerifier       securityVerifier,
                             List<String>                             supportedZones,
                             List<String>                             defaultZones,
                             List<String>                             publishZones,
                             AuditLog                                 auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);

        /*
         * Schema types need their own specialized converter because the schema type may be represented by multiple entities.
         */
        this.schemaTypeConverter = converter;
    }


    /**
     * Store a new schema type (and optional attributes) in the repository and return its unique identifier (GUID).
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
    public String  addSchemaType(String            userId,
                                 String            externalSourceGUID,
                                 String            externalSourceName,
                                 SchemaTypeBuilder schemaTypeBuilder,
                                 String            methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";

        String schemaTypeGUID = this.createBeanInRepository(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            schemaTypeBuilder.getTypeGUID(),
                                                            schemaTypeBuilder.getTypeName(),
                                                            schemaTypeBuilder.getQualifiedName(),
                                                            OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                            schemaTypeBuilder,
                                                            methodName);


        addEmbeddedTypes(userId,
                         externalSourceGUID,
                         externalSourceName,
                         schemaTypeGUID,
                         schemaTypeGUIDParameterName,
                         schemaTypeBuilder.getTypeName(),
                         schemaTypeBuilder,
                         methodName);

        return schemaTypeGUID;
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new schema type.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the schema type - used in other configuration
     * @param displayName short display name for the schema type
     * @param description description of the schema type
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(String userId,
                                               String externalSourceGUID,
                                               String externalSourceName,
                                               String templateGUID,
                                               String qualifiedName,
                                               String displayName,
                                               String description,
                                               String methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        SchemaTypeBuilder builder = new SchemaTypeBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        return this.createBeanFromTemplate(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_GUID,
                                           OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           builder,
                                           methodName);
    }


    /**
     * Update a stored schemaType.
     *
     * @param userId                 userId
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param existingSchemaTypeGUID unique identifier of the existing schemaType entity
     * @param existingSchemaTypeGUIDParameterName name of parameter for existingSchemaTypeGUID
     * @param builder             new schemaType values
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void   updateSchemaType(String            userId,
                                   String            externalSourceGUID,
                                   String            externalSourceName,
                                   String            existingSchemaTypeGUID,
                                   String            existingSchemaTypeGUIDParameterName,
                                   SchemaTypeBuilder builder) throws InvalidParameterException, 
                                                                     PropertyServerException, 
                                                                     UserNotAuthorizedException
    {
        final String methodName = "updateSchemaType";

        this.updateSchemaType(userId,
                              externalSourceGUID,
                              externalSourceName,
                              existingSchemaTypeGUID,
                              existingSchemaTypeGUIDParameterName,
                              builder,
                              false,
                              methodName);
    }


    /**
     * Update a stored schemaType.  Note - this only updates the main schema - it does not travel
     * through the nested schemas.
     *
     * @param userId                 userId
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param existingSchemaTypeGUID unique identifier of the existing schemaType entity
     * @param existingSchemaTypeGUIDParameterName name of parameter for existingSchemaTypeGUID
     * @param builder             new schemaType values
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     *
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void   updateSchemaType(String            userId,
                                   String            externalSourceGUID,
                                   String            externalSourceName,
                                   String            existingSchemaTypeGUID,
                                   String            existingSchemaTypeGUIDParameterName,
                                   SchemaTypeBuilder builder,
                                   boolean           isMergeUpdate,
                                   String            methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    existingSchemaTypeGUID,
                                    existingSchemaTypeGUIDParameterName,
                                    this.getSchemaTypeTypeGUID(builder),
                                    this.getSchemaTypeTypeName(builder),
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    methodName);
    }



    /**
     * Return the type guid contained in this builder bean.
     *
     * @param builder bean to interrogate
     * @return guid of the type
     */
    private String getSchemaTypeTypeGUID(SchemaTypeBuilder   builder)
    {
        if (builder.getTypeGUID() != null)
        {
            return builder.getTypeGUID();
        }
        else
        {
            return OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_GUID;
        }
    }


    /**
     * Return the type name contained in this builder bean.
     *
     * @param builder bean to interrogate
     * @return name of the type
     */
    private String getSchemaTypeTypeName(SchemaTypeBuilder   builder)
    {
        if (builder.getTypeName() != null)
        {
            return builder.getTypeName();
        }
        else
        {
            return OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;
        }
    }


    /**
     * Remove the requested schemaType if it is no longer connected to any other entity.
     *
     * @param userId       calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller - null for local cohort
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeGUID object to delete
     * @throws InvalidParameterException  the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void removeSchemaType(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String schemaTypeGUID) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String methodName        = "removeSchemaType";
        final String guidParameterName = "schemaTypeGUID";

        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    schemaTypeGUID,
                                    guidParameterName,
                                    OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_GUID,
                                    OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                    null,
                                    null,
                                    methodName);
    }


    /**
     * Is there an attached schema for the Asset?
     *
     * @param userId     calling user
     * @param assetGUID identifier for the entity that the object is attached to
     * @param assetGUIDParameterName name of parameter for assetGUID
     * @param methodName calling method
     *
     * @return schemaType object or null
     *
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public B getSchemaTypeForAsset(String   userId,
                                   String   assetGUID,
                                   String   assetGUIDParameterName,
                                   String   methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return this.getSchemaTypeForParent(userId,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                           OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                           methodName);
    }


    /**
     * Is there an attached schema for the Port?
     *
     * @param userId     calling user
     * @param portGUID identifier for the entity that the object is attached to
     * @param portGUIDParameterName name of parameter for portGUID
     * @param methodName calling method
     *
     * @return schemaType object or null
     *
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public B getSchemaTypeForPort(String   userId,
                                  String   portGUID,
                                  String   portGUIDParameterName,
                                  String   methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return this.getSchemaTypeForParent(userId,
                                           portGUID,
                                           portGUIDParameterName,
                                           OpenMetadataAPIMapper.PORT_TYPE_NAME,
                                           OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_GUID,
                                           OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                                           methodName);
    }


    /**
     * Is there an attached schema for the parent entity?  This is either an asset or a port entity.  This method
     * should not be used to get the schema type for a schema attribute.
     *
     * @param userId     calling user
     * @param parentGUID identifier for the entity that the object is attached to
     * @param parentGUIDParameterName parameter supplying parentGUID
     * @param parentTypeName type name of anchor
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
    public B getSchemaTypeForParent(String   userId,
                                    String   parentGUID,
                                    String   parentGUIDParameterName,
                                    String   parentTypeName,
                                    String   relationshipTypeGUID,
                                    String   relationshipTypeName,
                                    String   methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        EntityDetail  schemaTypeEntity = this.getAttachedEntity(userId,
                                                                parentGUID,
                                                                parentGUIDParameterName,
                                                                parentTypeName,
                                                                relationshipTypeGUID,
                                                                relationshipTypeName,
                                                                OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
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
    public B getSchemaType(String   userId,
                           String   schemaTypeGUID,
                           String   guidParameterName,
                           String   methodName) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        EntityDetail  schemaTypeEntity = this.getEntityFromRepository(userId,
                                                                      schemaTypeGUID,
                                                                      guidParameterName,
                                                                      OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                      methodName);

        return getSchemaTypeFromEntity(userId, schemaTypeEntity, methodName);
    }



    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findSchemaTypes(String userId,
                                   String searchString,
                                   int    startFrom,
                                   int    pageSize,
                                   String methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String searchStringParameterName = "searchString";

        List<EntityDetail> entities = this.findEntities(userId,
                                                        searchString,
                                                        searchStringParameterName,
                                                        OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_GUID,
                                                        OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                        null,
                                                        null,
                                                        startFrom,
                                                        pageSize,
                                                        methodName);

        return getSchemaTypesFromEntities(userId, entities, methodName);
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getSchemaTypeByName(String userId,
                                         String name,
                                         int    startFrom,
                                         int    pageSize,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String nameParameterName = "name";

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        List<EntityDetail> entities = this.getEntitiesByValue(userId,
                                                              name,
                                                              nameParameterName,
                                                              OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_GUID,
                                                              OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                              specificMatchPropertyNames,
                                                              true,
                                                              null,
                                                              null,
                                                              startFrom,
                                                              pageSize,
                                                              methodName);

        return getSchemaTypesFromEntities(userId, entities, methodName);
    }


    /**
     * Transform a ;ist of schema type entities into a list of schema type beans.  To completely fill out each schema type
     * it may be necessary to retrieve additional entities.  For example, a map schema type includes links
     * to the two types that are being mapped together.
     *
     * @param userId calling user
     * @param schemaTypeEntities list of entities retrieved from the repositories
     * @param methodName calling method
     *
     * @return list of beans
     *
     * @throws InvalidParameterException  problem with an entity
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private List<B> getSchemaTypesFromEntities(String             userId,
                                               List<EntityDetail> schemaTypeEntities,
                                               String             methodName) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        List<B> results = new ArrayList<>();

        if (schemaTypeEntities != null)
        {
            for (EntityDetail entity : schemaTypeEntities)
            {
                if (entity != null)
                {
                    results.add(this.getSchemaTypeFromEntity(userId, entity, methodName));
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }

        return results;
    }


    /**
     * Transform a schema type entity into a schema type bean.  To completely fill out the schema type
     * it may be necessary to retrieve additional entities.  For example, a map schema type includes links
     * to the two types that are being mapped together.
     *
     * @param userId calling user
     * @param schemaTypeEntity entity retrieved from the repository
     * @param methodName calling method
     *
     * @return schema type bean
     *
     * @throws InvalidParameterException  problem with the entity
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private B getSchemaTypeFromEntity(String       userId,
                                      EntityDetail schemaTypeEntity,
                                      String       methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        if ((schemaTypeEntity != null) && (schemaTypeEntity.getType() != null))
        {
            return getSchemaTypeFromInstance(userId,
                                             schemaTypeEntity,
                                             schemaTypeEntity.getType().getTypeDefName(),
                                             schemaTypeEntity.getProperties(),
                                             schemaTypeEntity.getClassifications(),
                                             methodName);
        }

        return null;
    }


    /**
     * Transform the schema type information stored either as a schema type entity or in the TypeClassifiedAttribute classification into a schema type
     * bean.  To completely fill out the schema type it may be necessary to retrieve additional entities.  For example, a map schema type includes
     * links to the two types that are being mapped together.
     *
     * @param userId calling user
     * @param schemaRootHeader header of the schema element that holds the root information
     * @param schemaRootTypeName name of type of the schema element that holds the root information
     * @param instanceProperties properties describing the schema type
     * @param entityClassifications classifications from the root entity
     * @param methodName calling method
     *
     * @return schema type bean
     * @throws InvalidParameterException  problem with the entity
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public  B getSchemaTypeFromInstance(String               userId,
                                        InstanceHeader       schemaRootHeader,
                                        String               schemaRootTypeName,
                                        InstanceProperties   instanceProperties,
                                        List<Classification> entityClassifications,
                                        String               methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String schemaGUIDParameterName = "schemaRootGUID";

        int     attributeCount         = 0;
        String  validValuesSetGUID     = null;
        String  externalSchemaTypeGUID = null;
        B       externalSchemaType     = null;
        B       mapToSchemaType        = null;
        B       mapFromSchemaType      = null;
        List<B> schemaTypeOptions      = null;

        /*
         * Look for an external schema type - the real content will be hanging off of this element.
         */
        if (repositoryHelper.isTypeOf(serviceName, schemaRootTypeName, OpenMetadataAPIMapper.EXTERNAL_SCHEMA_TYPE_TYPE_NAME))
        {
            EntityDetail externalSchemaTypeEntity = this.getAttachedEntity(userId,
                                                                           schemaRootHeader.getGUID(),
                                                                           schemaGUIDParameterName,
                                                                           OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                           OpenMetadataAPIMapper.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_GUID,
                                                                           OpenMetadataAPIMapper.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_NAME,
                                                                           OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                           methodName);

            if (externalSchemaTypeEntity != null)
            {
                externalSchemaType = this.getSchemaTypeFromEntity(userId, externalSchemaTypeEntity, methodName);
            }
        }

        /*
         * Collect up the interesting information about the schema type that is outside of the entity.
         */
        if (repositoryHelper.isTypeOf(serviceName, schemaRootTypeName, OpenMetadataAPIMapper.COMPLEX_SCHEMA_TYPE_TYPE_NAME))
        {
            attributeCount = this.countSchemaAttributes(userId,
                                                        schemaRootHeader.getGUID(),
                                                        schemaGUIDParameterName,
                                                        methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, schemaRootTypeName, OpenMetadataAPIMapper.ENUM_SCHEMA_TYPE_TYPE_NAME))
        {
            EntityDetail validValuesSetEntity = this.getAttachedEntity(userId,
                                                                       schemaRootHeader.getGUID(),
                                                                       schemaGUIDParameterName,
                                                                       OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_NAME,
                                                                       OpenMetadataAPIMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                                                       OpenMetadataAPIMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                                       OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_NAME,
                                                                       methodName);

            if (validValuesSetEntity != null)
            {
                validValuesSetGUID = validValuesSetEntity.getGUID();
            }
        }
        else if (repositoryHelper.isTypeOf(serviceName, schemaRootTypeName, OpenMetadataAPIMapper.MAP_SCHEMA_TYPE_TYPE_NAME))
        {
            EntityDetail mapFromSchemaTypeEntity = this.getAttachedEntity(userId,
                                                                          schemaRootHeader.getGUID(),
                                                                          schemaGUIDParameterName,
                                                                          OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                          OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_GUID,
                                                                          OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_NAME,
                                                                          OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                          methodName);

            if (mapFromSchemaTypeEntity != null)
            {
                mapFromSchemaType = this.getSchemaTypeFromEntity(userId, mapFromSchemaTypeEntity, methodName);
            }

            EntityDetail mapToSchemaTypeEntity = this.getAttachedEntity(userId,
                                                                        schemaRootHeader.getGUID(),
                                                                        schemaGUIDParameterName,
                                                                        OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                        OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_GUID,
                                                                        OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_NAME,
                                                                        OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                        methodName);

            if (mapToSchemaTypeEntity != null)
            {
                mapToSchemaType = this.getSchemaTypeFromEntity(userId, mapToSchemaTypeEntity, methodName);
            }
        }
        else if (repositoryHelper.isTypeOf(serviceName, schemaRootTypeName, OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME))
        {
            List<EntityDetail> schemaTypeOptionsEntities = getAttachedEntities(userId,
                                                                               schemaRootHeader.getGUID(),
                                                                               schemaGUIDParameterName,
                                                                               OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                               OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                                                               OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME,
                                                                               OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                               0,
                                                                               invalidParameterHandler.getMaxPagingSize(),
                                                                               methodName);

            if ((schemaTypeOptionsEntities != null) && (! schemaTypeOptionsEntities.isEmpty()))
            {
                schemaTypeOptions = new ArrayList<>();

                for (EntityDetail schemaTypeOptionEntity : schemaTypeOptionsEntities)
                {
                    if (schemaTypeOptionEntity != null)
                    {
                        schemaTypeOptions.add(this.getSchemaTypeFromEntity(userId, schemaTypeOptionEntity, methodName));
                    }
                }
            }

        }

        return schemaTypeConverter.getNewSchemaTypeBean(beanClass,
                                                        schemaRootHeader,
                                                        schemaRootTypeName,
                                                        instanceProperties,
                                                        entityClassifications,
                                                        attributeCount,
                                                        validValuesSetGUID,
                                                        externalSchemaTypeGUID,
                                                        externalSchemaType,
                                                        mapFromSchemaType,
                                                        mapToSchemaType,
                                                        schemaTypeOptions,
                                                        methodName);
    }
}
