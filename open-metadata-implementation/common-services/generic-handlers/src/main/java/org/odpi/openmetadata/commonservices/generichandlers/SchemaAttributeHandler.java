/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;

import java.util.ArrayList;
import java.util.List;

/**
 * SchemaAttributeHandler manages Schema Attribute objects.  It runs server-side in
 * the OMAG Server Platform and retrieves SchemaElement entities through the OMRSRepositoryConnector.
 */
public class SchemaAttributeHandler<SCHEMA_ATTRIBUTE, SCHEMA_TYPE> extends SchemaElementHandler<SCHEMA_ATTRIBUTE>
{
    private OpenMetadataAPIGenericConverter<SCHEMA_ATTRIBUTE> schemaAttributeConverter;
    private Class<SCHEMA_TYPE>                                schemaTypeBeanClass;
    private SchemaTypeHandler<SCHEMA_TYPE>                    schemaTypeHandler;


    /**
     * Construct the handler with information needed to work with B objects.
     *
     * @param schemaAttributeConverter specific converter for the SCHEMA_ATTRIBUTE bean class
     * @param schemaAttributeBeanClass name of bean class that is represented by the generic class SCHEMA_ATTRIBUTE
     * @param schemaTypeConverter specific converter for the SCHEMA_TYPE bean class
     * @param schemaTypeBeanClass name of bean class that is represented by the generic class SCHEMA_TYPE
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
    public SchemaAttributeHandler(OpenMetadataAPIGenericConverter<SCHEMA_ATTRIBUTE> schemaAttributeConverter,
                                  Class<SCHEMA_ATTRIBUTE>                                 schemaAttributeBeanClass,
                                  OpenMetadataAPIGenericConverter<SCHEMA_TYPE> schemaTypeConverter,
                                  Class<SCHEMA_TYPE>                                      schemaTypeBeanClass,
                                  String                                                  serviceName,
                                  String                                                  serverName,
                                  InvalidParameterHandler invalidParameterHandler,
                                  RepositoryHandler repositoryHandler,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String                                                  localServerUserId,
                                  OpenMetadataServerSecurityVerifier securityVerifier,
                                  List<String>                                            supportedZones,
                                  List<String>                                            defaultZones,
                                  List<String>                                            publishZones,
                                  AuditLog auditLog)
    {
        super(schemaAttributeConverter,
              schemaAttributeBeanClass,
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
         * Schema Attributes need their own specialized converter because the schema type may be represented by multiple entities.
         */
        this.schemaAttributeConverter = schemaAttributeConverter;
        this.schemaTypeBeanClass      = schemaTypeBeanClass;
        this.schemaTypeHandler        = new SchemaTypeHandler<>(schemaTypeConverter,
                                                                schemaTypeBeanClass,
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
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new schema attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param parentElementGUID  element to connect this schema attribute to
     * @param parentElementGUIDParameterName parameter supplying parentElementGUID
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the schema attribute - used in other configuration
     * @param displayName short display name for the schema attribute
     * @param description description of the schema attribute
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttributeFromTemplate(String userId,
                                                    String externalSourceGUID,
                                                    String externalSourceName,
                                                    String parentElementGUID,
                                                    String parentElementGUIDParameterName,
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
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        SchemaAttributeBuilder builder = new SchemaAttributeBuilder(qualifiedName,
                                                                    displayName,
                                                                    description,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        String schemaAttributeGUID = this.createBeanFromTemplate(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 templateGUID,
                                                                 templateGUIDParameterName,
                                                                 OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                                                 OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                 qualifiedName,
                                                                 OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                 builder,
                                                                 methodName);

        if (schemaAttributeGUID != null)
        {
            final String schemaAttributeGUIDParameterName = "schemaAttributeGUID";

            EntityDetail parentEntity = this.getEntityFromRepository(userId,
                                                                     parentElementGUID,
                                                                     parentElementGUIDParameterName,
                                                                     OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                     methodName);

            String parentElementTypeName = OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;
            String parentElementRelationshipTypeGUID = OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID;
            String parentElementRelationshipTypeName = OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;

            if ((parentEntity != null) && (parentEntity.getType() != null))
            {
                if (repositoryHelper.isTypeOf(serviceName, parentEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME))
                {
                    parentElementTypeName = OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME;
                    parentElementRelationshipTypeGUID = OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID;
                    parentElementRelationshipTypeName = OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;
                }
            }

            this.linkElementToElement(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      parentElementGUID,
                                      parentElementGUIDParameterName,
                                      parentElementTypeName,
                                      schemaAttributeGUID,
                                      schemaAttributeGUIDParameterName,
                                      builder.getTypeName(),
                                      parentElementRelationshipTypeGUID,
                                      parentElementRelationshipTypeName,
                                      null,
                                      methodName);
        }

        return schemaAttributeGUID;
    }


    /**
     * Create a new schema attribute with its type attached.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param parentElementGUID unique identifier of the metadata element to connect the new schema attribute to
     * @param parentElementGUIDParameterName parameter name supplying parentElementGUID
     * @param qualifiedName unique identifier for this schema type
     * @param qualifiedNameParameterName name of parameter supplying the qualified name
     * @param schemaAttributeBuilder builder containing the properties of the schema type
     * @param methodName calling method
     *
     * @return unique identifier of the new schema attribute already linked to its parent
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createNestedSchemaAttribute(String                 userId,
                                              String                 externalSourceGUID,
                                              String                 externalSourceName,
                                              String                 parentElementGUID,
                                              String                 parentElementGUIDParameterName,
                                              String                 qualifiedName,
                                              String                 qualifiedNameParameterName,
                                              SchemaAttributeBuilder schemaAttributeBuilder,
                                              String                 methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        EntityDetail parentEntity = this.getEntityFromRepository(userId,
                                                                 parentElementGUID,
                                                                 parentElementGUIDParameterName,
                                                                 OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                 methodName);

        String parentAttributeTypeName = OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;
        String parentAttributeRelationshipTypeGUID = OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID;
        String parentAttributeRelationshipTypeName = OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;

        if ((parentEntity != null) && (parentEntity.getType() != null))
        {
            if (repositoryHelper.isTypeOf(serviceName, parentEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME))
            {
                parentAttributeTypeName = OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME;
                parentAttributeRelationshipTypeGUID = OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID;
                parentAttributeRelationshipTypeName = OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;
            }
        }

        return this.createNestedSchemaAttribute(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                parentElementGUID,
                                                parentElementGUIDParameterName,
                                                parentAttributeTypeName,
                                                parentAttributeRelationshipTypeGUID,
                                                parentAttributeRelationshipTypeName,
                                                qualifiedName,
                                                qualifiedNameParameterName,
                                                schemaAttributeBuilder,
                                                methodName);
    }


    /**
     * Create a new schema attribute with its type attached.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param parentElementGUID unique identifier of the metadata element to connect the new schema attribute to
     * @param parentElementGUIDParameterName parameter name supplying parentElementGUID
     * @param parentElementTypeName type of the parent element - may be a schema attribute or a schema type
     * @param parentAttributeRelationshipTypeGUID unique identifier of the relationship from the new schema type to the parent
     * @param parentAttributeRelationshipTypeName unique name of the relationship from the new schema type to the parent
     * @param qualifiedName unique identifier for this schema type
     * @param qualifiedNameParameterName name of parameter supplying the qualified name
     * @param schemaAttributeBuilder builder containing the properties of the schema type
     * @param methodName calling method
     *
     * @return unique identifier of the new schema attribute already linked to its parent
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createNestedSchemaAttribute(String                 userId,
                                              String                 externalSourceGUID,
                                              String                 externalSourceName,
                                              String                 parentElementGUID,
                                              String                 parentElementGUIDParameterName,
                                              String                 parentElementTypeName,
                                              String                 parentAttributeRelationshipTypeGUID,
                                              String                 parentAttributeRelationshipTypeName,
                                              String                 qualifiedName,
                                              String                 qualifiedNameParameterName,
                                              SchemaAttributeBuilder schemaAttributeBuilder,
                                              String                 methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * Now create the table itself along with its schema type.  It also links the resulting table to the database schema type.
         * The returned value is the guid of the table.
         */
        String schemaAttributeGUID = this.createBeanInRepository(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 schemaAttributeBuilder.getTypeGUID(),
                                                                 schemaAttributeBuilder.getTypeName(),
                                                                 qualifiedName,
                                                                 OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                 schemaAttributeBuilder,
                                                                 methodName);

        if (schemaAttributeGUID != null)
        {
            final String schemaAttributeGUIDParameterName = "schemaAttributeGUID";

            this.addEmbeddedTypes(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  schemaAttributeGUID,
                                  schemaAttributeGUIDParameterName,
                                  schemaAttributeBuilder.getTypeName(),
                                  schemaAttributeBuilder.getSchemaTypeBuilder(),
                                  methodName);

            this.linkElementToElement(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      parentElementGUID,
                                      parentElementGUIDParameterName,
                                      parentElementTypeName,
                                      schemaAttributeGUID,
                                      schemaAttributeGUIDParameterName,
                                      schemaAttributeBuilder.getTypeName(),
                                      parentAttributeRelationshipTypeGUID,
                                      parentAttributeRelationshipTypeName,
                                      null,
                                      methodName);


            return schemaAttributeGUID;
        }

        /*
         * Not reachable because any failures result in exceptions.
         */
        return null;
    }


    /**
     * Returns a list of schema attributes that are linked to a ComplexSchemaType via the AttributeForSchema relationship.
     * It validates the unique identifier of the complex schema type, and the visibility/security of any attached asset using the supported zones
     * for this component.  Then begins to extract the schema attributes.  Exceptions occur if a schema attribute does not have a type.
     *
     * @param userId         String   userId of user making request.
     * @param schemaTypeGUID String   unique id for containing schema type.
     * @param guidParameterName String name of the parameter supplying the guid.
     * @param requiredClassificationName  String the name of the classification that must be on the schema attribute or linked schema type entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the schema attribute or linked schema type entity.
     * @param startFrom   int      starting position for first returned element.
     * @param pageSize    int      maximum number of elements to return on the call.
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getSchemaAttributesForComplexSchemaType(String userId,
                                                                          String schemaTypeGUID,
                                                                          String guidParameterName,
                                                                          String requiredClassificationName,
                                                                          String omittedClassificationName,
                                                                          int    startFrom,
                                                                          int    pageSize,
                                                                          String methodName) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        return this.getSchemaAttributesForComplexSchemaType(userId,
                                                            schemaTypeGUID,
                                                            guidParameterName,
                                                            requiredClassificationName,
                                                            omittedClassificationName,
                                                            supportedZones,
                                                            startFrom,
                                                            pageSize,
                                                            methodName);
    }


    /**
     * Returns a list of schema attributes that are linked to a ComplexSchemaType via the AttributeForSchema relationship.
     * It validates the unique identifier of the complex schema type, and the visibility/security of any attached asset using the
     * supplied supported zones (needed for the calls from the OCF Metadata REST Services).
     * Then it begins to extract the schema attributes. Exceptions occur if a schema attribute does not have a type.
     *
     * @param userId         String   userId of user making request.
     * @param schemaTypeGUID String   unique id for containing schema type.
     * @param schemaTypeGUIDParameterName String name of the parameter supplying the guid.
     * @param requiredClassificationName  String the name of the classification that must be on the schema attribute or linked schema type entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the schema attribute or linked schema type entity.
     * @param serviceSupportedZones list of zone names for calling service
     * @param startFrom   int      starting position for first returned element.
     * @param pageSize    int      maximum number of elements to return on the call.
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getSchemaAttributesForComplexSchemaType(String       userId,
                                                                          String       schemaTypeGUID,
                                                                          String       schemaTypeGUIDParameterName,
                                                                          String       requiredClassificationName,
                                                                          String       omittedClassificationName,
                                                                          List<String> serviceSupportedZones,
                                                                          int          startFrom,
                                                                          int          pageSize,
                                                                          String       methodName) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        final String schemaAttributeGUIDParameterName = "schemaAttributeEntity.getGUID()";

        List<EntityDetail>  entities = this.getAttachedEntities(userId,
                                                                schemaTypeGUID,
                                                                schemaTypeGUIDParameterName,
                                                                OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                requiredClassificationName,
                                                                omittedClassificationName,
                                                                false,
                                                                serviceSupportedZones,
                                                                startFrom,
                                                                pageSize,
                                                                methodName);

        List<SCHEMA_ATTRIBUTE>  results = new ArrayList<>();

        if (entities != null)
        {
            for (EntityDetail schemaAttributeEntity : entities)
            {
                if (schemaAttributeEntity != null)
                {
                    /*
                     * This method verifies the visibility of the entity and the security permission.
                     */
                    results.add(this.getSchemaAttributeFromEntity(userId,
                                                                  schemaAttributeEntity.getGUID(),
                                                                  schemaAttributeGUIDParameterName,
                                                                  schemaAttributeEntity,
                                                                  methodName));
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
     * Returns a list of schema attributes that are linked to a schema attribute via the NestedSchemaAttribute relationship.
     * It validates the unique identifier of the parent schema attribute, and the visibility/security of any attached asset using the
     * supplied supported zones (needed for the calls from the OCF Metadata REST Services).
     * Then it begins to extract the schema attributes. Exceptions occur if a schema attribute does not have a type.
     *
     * @param userId         String   userId of user making request.
     * @param schemaAttributeGUID String   unique id for containing schema attribute.
     * @param schemaAttributeGUIDParameterName String name of the parameter supplying the guid.
     * @param startFrom   int      starting position for first returned element.
     * @param pageSize    int      maximum number of elements to return on the call.
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getNestedSchemaAttributes(String       userId,
                                                            String       schemaAttributeGUID,
                                                            String       schemaAttributeGUIDParameterName,
                                                            int          startFrom,
                                                            int          pageSize,
                                                            String       methodName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        return getNestedSchemaAttributes(userId, schemaAttributeGUID, schemaAttributeGUIDParameterName, supportedZones, startFrom, pageSize, methodName);
    }


    /**
     * Returns a list of schema attributes that are linked to a schema attribute via the NestedSchemaAttribute relationship.
     * It validates the unique identifier of the parent schema attribute, and the visibility/security of any attached asset using the
     * supplied supported zones (needed for the calls from the OCF Metadata REST Services).
     * Then it begins to extract the schema attributes. Exceptions occur if a schema attribute does not have a type.
     *
     * @param userId         String   userId of user making request.
     * @param schemaAttributeGUID String   unique id for containing schema attribute.
     * @param schemaAttributeGUIDParameterName String name of the parameter supplying the guid.
     * @param serviceSupportedZones list of zone names for calling service
     * @param startFrom   int      starting position for first returned element.
     * @param pageSize    int      maximum number of elements to return on the call.
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getNestedSchemaAttributes(String       userId,
                                                            String       schemaAttributeGUID,
                                                            String       schemaAttributeGUIDParameterName,
                                                            List<String> serviceSupportedZones,
                                                            int          startFrom,
                                                            int          pageSize,
                                                            String       methodName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String nestedSchemaAttributeGUIDParameterName = "schemaAttributeEntity.getGUID()";

        List<EntityDetail>  entities = this.getAttachedEntities(userId,
                                                                schemaAttributeGUID,
                                                                schemaAttributeGUIDParameterName,
                                                                OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                null,
                                                                null,
                                                                false,
                                                                serviceSupportedZones,
                                                                startFrom,
                                                                pageSize,
                                                                methodName);

        List<SCHEMA_ATTRIBUTE>  results = new ArrayList<>();

        if (entities != null)
        {
            for (EntityDetail schemaAttributeEntity : entities)
            {
                if (schemaAttributeEntity != null)
                {
                    /*
                     * This method verifies the visibility of the entity and the security permission.
                     */
                    results.add(this.getSchemaAttributeFromEntity(userId,
                                                                  schemaAttributeEntity.getGUID(),
                                                                  nestedSchemaAttributeGUIDParameterName,
                                                                  schemaAttributeEntity,
                                                                  methodName));
                }
            }
        }
        else
        {
            /*
             * Using the old pattern where a schema type is between the nested schema attributes.
             */
            EntityDetail entity = this.getAttachedEntity(userId,
                                                         schemaAttributeGUID,
                                                         schemaAttributeGUIDParameterName,
                                                         OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                         OpenMetadataAPIMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                         OpenMetadataAPIMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME,
                                                         OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                         methodName);

            if (entity != null)
            {
                String schemaTypeGUIDParameterName = "schemaTypeGUID";

                return getSchemaAttributesForComplexSchemaType(userId,
                                                               entity.getGUID(),
                                                               schemaTypeGUIDParameterName,
                                                               null,
                                                               null,
                                                               supportedZones,
                                                               startFrom,
                                                               pageSize,
                                                               methodName);
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
     * Retrieve the special links (like foreign keys) between attributes.
     *
     * @param userId calling user
     * @param schemaAttributeEntity details from the repository
     * @param methodName calling method
     * @return list of schema attribute relationships populated with information from the repository
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private List<Relationship> getSchemaAttributeRelationships(String        userId,
                                                               EntityDetail schemaAttributeEntity,
                                                               String        methodName) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String schemaAttributeGUIDParameterName = "schemaAttributeEntity";

        List<Relationship> results = new ArrayList<>();

        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   schemaAttributeEntity.getGUID(),
                                                                   schemaAttributeGUIDParameterName,
                                                                   OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                   null,
                                                                   null,
                                                                   OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                   0,
                                                                   invalidParameterHandler.getMaxPagingSize(),
                                                                   methodName);

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if ((relationship != null) && (relationship.getType() != null))
                {
                    String typeName = relationship.getType().getTypeDefName();

                    if (OpenMetadataAPIMapper.FOREIGN_KEY_RELATIONSHIP_TYPE_NAME.equals(typeName))
                    {
                        results.add(relationship);
                    }
                    else if (OpenMetadataAPIMapper.GRAPH_EDGE_LINK_RELATIONSHIP_TYPE_NAME.equals(typeName))
                    {
                        results.add(relationship);
                    }
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
     * From the schema attribute entity, gather the related schema type, schema link and/or schema attribute relationships
     * and create a schema attribute bean.  The caller is expected to have validated that is is ok to return this schema attribute.
     *
     * @param userId calling userId
     * @param schemaAttributeGUID unique identifier of schema attribute
     * @param schemaAttributeGUIDParameterName parameter passing the schemaAttributeGUID
     * @param schemaAttributeEntity entity retrieved for the schema attribute
     * @param methodName calling method
     * @return a new schema attribute object, or null if the schema attribute is either not visible to the user, or its classifications
     *         are not what are requested.
     *
     * @throws InvalidParameterException the guid or bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private SCHEMA_ATTRIBUTE getSchemaAttributeFromEntity(String       userId,
                                                          String       schemaAttributeGUID,
                                                          String       schemaAttributeGUIDParameterName,
                                                          EntityDetail schemaAttributeEntity,
                                                          String       methodName) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        if ((schemaAttributeEntity != null) && (schemaAttributeEntity.getType() != null))
        {
            /*
             * Extra entities are required depending on the type of the schema.
             */
            SCHEMA_TYPE  schemaType  = null;

            /*
             * The table may have its type stored as a classification, or as a linked schema type.  The column is linked to
             * the attribute in the first case, and the schema type in the second case.
             */
            try
            {
                Classification typeClassification = repositoryHelper.getClassificationFromEntity(serviceName,
                                                                                                 schemaAttributeEntity,
                                                                                                 OpenMetadataAPIMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                                                                                                 methodName);

                if ((typeClassification != null) && (typeClassification.getProperties() != null))
                {
                    String schemaTypeName = repositoryHelper.getStringProperty(serviceName,
                                                                               OpenMetadataAPIMapper.TYPE_NAME_PROPERTY_NAME,
                                                                               typeClassification.getProperties(),
                                                                               methodName);

                    schemaType = schemaTypeHandler.getSchemaTypeFromInstance(userId,
                                                                             schemaAttributeEntity,
                                                                             schemaTypeName,
                                                                             typeClassification.getProperties(),
                                                                             schemaAttributeEntity.getClassifications(),
                                                                             methodName);
                }
            }
            catch (ClassificationErrorException classificationNotKnown)
            {
                /*
                 * Type classification not supported.
                 */
            }


            if (schemaType == null)
            {
                /*
                 * Look for an explicit private schema type
                 */
                schemaType = schemaTypeHandler.getSchemaTypeForParent(userId,
                                                                      schemaAttributeGUID,
                                                                      schemaAttributeGUIDParameterName,
                                                                      OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                      OpenMetadataAPIMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                                      OpenMetadataAPIMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME,
                                                                      methodName);
            }


            List<Relationship> attributeRelationships = this.getSchemaAttributeRelationships(userId, schemaAttributeEntity, methodName);

            return schemaAttributeConverter.getNewSchemaAttributeBean(beanClass,
                                                                      schemaAttributeEntity,
                                                                      schemaTypeBeanClass,
                                                                      schemaType,
                                                                      attributeRelationships,
                                                                      methodName);
        }

        return null;
    }


    /**
     * Work through the schema attributes adding or updating the instances
     *
     * @param userId calling userId
     * @param schemaAttributeEntities list of retrieved entities
     * @param methodName calling method
     * @return list of new schema attributes
     *
     * @throws InvalidParameterException the guid or bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private List<SCHEMA_ATTRIBUTE> getSchemaAttributesFromEntities(String             userId,
                                                                   List<EntityDetail> schemaAttributeEntities,
                                                                   String             methodName) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String parameterName = "schemaAttributeEntities";

        if (schemaAttributeEntities != null)
        {
            List<SCHEMA_ATTRIBUTE> schemaAttributes = new ArrayList<>();

            for (EntityDetail entity : schemaAttributeEntities)
            {
                if (entity != null)
                {
                    schemaAttributes.add(this.getSchemaAttributeFromEntity(userId,
                                                                           entity.getGUID(),
                                                                           parameterName,
                                                                           entity,
                                                                           methodName));
                }
            }

            if (!schemaAttributes.isEmpty())
            {
                return schemaAttributes;
            }
        }

        return null;
    }


    /**
     * Retrieve a specific schema attribute by GUID.   It is only returned if the guid is known, the entity is of the correct type,
     * the classifications are as expected and any asset it is connected to is both visible via the zones setting and the
     * security verifier allows the update.
     *
     * @param userId calling userId
     * @param schemaAttributeGUID unique identifier of schema attribute
     * @param schemaAttributeGUIDParameterName parameter passing the schemaAttributeGUID
     * @param expectedTypeName type or subtype of schema attribute
     * @param requiredClassificationName a classification that must be either on the schema attribute or its type.
     * @param omittedClassificationName a classification that must NOT be on either the schema attribute or its type.
     * @param methodName calling method
     * @return guid of new schema
     *
     * @throws InvalidParameterException the guid or bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public SCHEMA_ATTRIBUTE getSchemaAttribute(String userId,
                                               String schemaAttributeGUID,
                                               String schemaAttributeGUIDParameterName,
                                               String expectedTypeName,
                                               String requiredClassificationName,
                                               String omittedClassificationName,
                                               String methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String typeParameterName = "expectedTypeName";

        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);
        invalidParameterHandler.validateName(expectedTypeName, typeParameterName, methodName);

        /*
         * This method verifies the GUID and the type
         */
        EntityDetail schemaAttributeEntity = this.getEntityFromRepository(userId,
                                                                          schemaAttributeGUID,
                                                                          schemaAttributeGUIDParameterName,
                                                                          expectedTypeName,
                                                                          requiredClassificationName,
                                                                          omittedClassificationName,
                                                                          methodName);

        /*
         * This method issues additional retrieves to the metadata repositories to build the schema attribute bean, its type and any attribute
         * relationships.
         */
        return this.getSchemaAttributeFromEntity(userId,
                                                 schemaAttributeGUID,
                                                 schemaAttributeGUIDParameterName,
                                                 schemaAttributeEntity,
                                                 methodName);
    }


    /**
     * Returns a list of specifically typed schema attributes with matching names - either display names or qualified names
     *
     * @param userId         String   userId of user making request.
     * @param typeGUID       String   unique identifier of type of schema attribute required.
     * @param typeName       String  unique name of type of schema attribute required.
     * @param name           String   name (qualified or display name) of schema attribute.
     * @param requiredClassificationName  String name of classification that must be present
     * @param omittedClassificationName  String name of classification that must not be present
     * @param elementStart   int      starting position for first returned element.
     * @param maxElements    int      maximum number of elements to return on the call.
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getSchemaAttributesByName(String       userId,
                                                            String       typeGUID,
                                                            String       typeName,
                                                            String       name,
                                                            String       requiredClassificationName,
                                                            String       omittedClassificationName,
                                                            int          elementStart,
                                                            int          maxElements,
                                                            String       methodName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        return getSchemaAttributesByName(userId,
                                         typeGUID,
                                         typeName,
                                         name,
                                         requiredClassificationName,
                                         omittedClassificationName,
                                         supportedZones,
                                         elementStart,
                                         maxElements,
                                         methodName);
    }


    /**
     * Returns a list of specifically typed schema attributes with matching names - either display names or qualified names
     *
     * @param userId         String   userId of user making request
     * @param typeGUID       String   unique identifier of type of schema attribute required
     * @param typeName       String  unique name of type of schema attribute required
     * @param name           String   name (qualified or display name) of schema attribute
     * @param requiredClassificationName  String name of classification that must be present
     * @param omittedClassificationName  String name of classification that must not be present
     * @param serviceSupportedZones zones that assets must be;ong in to be visible
     * @param startFrom   int      starting position for first returned element
     * @param pageSize    int      maximum number of elements to return on the call
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getSchemaAttributesByName(String       userId,
                                                            String       typeGUID,
                                                            String       typeName,
                                                            String       name,
                                                            String       requiredClassificationName,
                                                            String       omittedClassificationName,
                                                            List<String> serviceSupportedZones,
                                                            int          startFrom,
                                                            int          pageSize,
                                                            String       methodName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String nameParameterName = "name";

        List<String> specificMatchPropertyNames = new ArrayList<>();

        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        List<EntityDetail>  schemaAttributeEntities = this.getEntitiesByValue(userId,
                                                                              name,
                                                                              nameParameterName,
                                                                              typeGUID,
                                                                              typeName,
                                                                              specificMatchPropertyNames,
                                                                              true,
                                                                              requiredClassificationName,
                                                                              omittedClassificationName,
                                                                              false,
                                                                              serviceSupportedZones,
                                                                              startFrom,
                                                                              pageSize,
                                                                              methodName);

        return this.getSchemaAttributesFromEntities(userId, schemaAttributeEntities, methodName);
    }


    /**
     * Retrieve the list of database table metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of endpoint.  This may include wild card characters.
     * @param searchStringParameterName name of parameter providing search string
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param requiredClassificationName  String the name of the classification that must be on the entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the entity.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return list of matching schema attribute elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SCHEMA_ATTRIBUTE>   findSchemaAttributes(String       userId,
                                                         String       searchString,
                                                         String       searchStringParameterName,
                                                         String       resultTypeGUID,
                                                         String       resultTypeName,
                                                         String       requiredClassificationName,
                                                         String       omittedClassificationName,
                                                         int          startFrom,
                                                         int          pageSize,
                                                         String       methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        List<EntityDetail> schemaAttributeEntities = this.findEntities(userId,
                                                                       searchString,
                                                                       searchStringParameterName,
                                                                       resultTypeGUID,
                                                                       resultTypeName,
                                                                       requiredClassificationName,
                                                                       omittedClassificationName,
                                                                       startFrom,
                                                                       pageSize,
                                                                       methodName);

        return this.getSchemaAttributesFromEntities(userId, schemaAttributeEntities, methodName);
    }


    /**
     * Update a schema attribute
     *
     * @param userId                      calling user
     * @param externalSourceGUID          unique identifier of software server capability representing the caller
     * @param externalSourceName          unique name of software server capability representing the caller
     * @param schemaAttributeGUID         unique identifier of schema attribute
     * @param instanceProperties          the schema attribute's properties
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaAttribute(String                userId,
                                      String                externalSourceGUID,
                                      String                externalSourceName,
                                      String                schemaAttributeGUID,
                                      InstanceProperties instanceProperties) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "updateSchemaAttribute";
        final String parameterName = "schemaAttributeGUID";

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    schemaAttributeGUID,
                                    parameterName,
                                    OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                    OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                    instanceProperties,
                                    true,
                                    methodName);
    }


    /**
     * Update a schema attribute
     *
     * @param userId                      calling user
     * @param externalSourceGUID          unique identifier of software server capability representing the caller
     * @param externalSourceName          unique name of software server capability representing the caller
     * @param schemaAttributeGUID         unique identifier of schema attribute
     * @param schemaAttributeGUIDParameterName  parameter supplying schemaAttributeGUID
     * @param instanceProperties          the schema attribute's properties
     * @param isMergeUpdate               should the properties be merged with existing properties of replace them?
     * @param methodName                  calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaAttribute(String                userId,
                                      String                externalSourceGUID,
                                      String                externalSourceName,
                                      String                schemaAttributeGUID,
                                      String                schemaAttributeGUIDParameterName,
                                      InstanceProperties instanceProperties,
                                      boolean               isMergeUpdate,
                                      String                methodName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    schemaAttributeGUID,
                                    schemaAttributeGUIDParameterName,
                                    OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                    OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                    instanceProperties,
                                    isMergeUpdate,
                                    methodName);
    }
}
