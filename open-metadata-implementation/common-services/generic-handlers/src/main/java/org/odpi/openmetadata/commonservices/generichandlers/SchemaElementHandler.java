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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

/**
 * SchemaElementHandler manages common methods fof SchemaType and Schema Attribute objects.  It runs server-side in
 * the OMAG Server Platform and retrieves SchemaElement entities and relationships through the OMRSRepositoryConnector.
 */
class SchemaElementHandler<B> extends ReferenceableHandler<B>
{
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
    SchemaElementHandler(OpenMetadataAPIGenericConverter<B> converter,
                         Class<B>                           beanClass,
                         String                             serviceName,
                         String                             serverName,
                         InvalidParameterHandler            invalidParameterHandler,
                         RepositoryHandler                  repositoryHandler,
                         OMRSRepositoryHelper               repositoryHelper,
                         String                             localServerUserId,
                         OpenMetadataServerSecurityVerifier securityVerifier,
                         List<String>                       supportedZones,
                         List<String>                       defaultZones,
                         List<String>                       publishZones,
                         AuditLog                           auditLog)
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
    }


    /**
     * Create/retrieve the unique identifier of the SchemaType that sits between the asset and a top-level
     * schema attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller - if null a local element is created
     * @param externalSourceName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the database schema where the database table is located
     * @param assetGUIDParameterName name of the parameter of assetGUID
     * @param assetTypeName the name of the asset's type
     * @param schemaTypeTypeGUID unique identifier of the type
     * @param schemaTypeTypeName unique name of the type
     * @param methodName calling method
     * @return properties of the anchor schema type for the database schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String getAssetSchemaTypeGUID(String userId,
                                  String externalSourceGUID,
                                  String externalSourceName,
                                  String assetGUID,
                                  String assetGUIDParameterName,
                                  String assetTypeName,
                                  String schemaTypeTypeGUID,
                                  String schemaTypeTypeName,
                                  String methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);

        /*
         * This method verifies the visibility of this asset to the calling user.  It also returns the qualified name of the
         * asset which is used to create the qualified name of the schema type if it does not exist.
         */
        String assetQualifiedName = this.getBeanStringPropertyFromRepository(userId,
                                                                             assetGUID,
                                                                             assetGUIDParameterName,
                                                                             assetTypeName,
                                                                             OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                             methodName);

        if (assetQualifiedName == null)
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        assetGUID,
                                                        assetTypeName,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
            /* unreachable */
            return null;
        }

        /*
         * Now retrieve the schema type attached to the asset
         */
        EntityDetail schemaTypeEntity = this.getAttachedEntity(userId,
                                                               assetGUID,
                                                               assetGUIDParameterName,
                                                               assetTypeName,
                                                               OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                               OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                               schemaTypeTypeName,
                                                               methodName);

        if (schemaTypeEntity == null)
        {
            /*
             * The schema type does not exist so need to create one and link it ito the asset
             */
            String schemaTypeQualifiedName = "SchemaOf:" + assetQualifiedName;

            SchemaTypeBuilder builder = new SchemaTypeBuilder(schemaTypeQualifiedName,
                                                              schemaTypeTypeGUID,
                                                              schemaTypeTypeName,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

            builder.setAnchors(userId, assetGUID, methodName);

            String schemaTypeGUID = this.createBeanInRepository(userId,
                                                                externalSourceGUID,
                                                                externalSourceName,
                                                                schemaTypeTypeGUID,
                                                                schemaTypeTypeName,
                                                                schemaTypeQualifiedName,
                                                                OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                builder,
                                                                methodName);

            final String schemaTypeGUIDParameterName = "schemaTypeGUID";

            if (schemaTypeGUID != null)
            {
                this.linkElementToElement(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          assetGUID,
                                          assetGUIDParameterName,
                                          assetTypeName,
                                          schemaTypeGUID,
                                          schemaTypeGUIDParameterName,
                                          schemaTypeTypeName,
                                          OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                          OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                          null,
                                          methodName);
            }

            return schemaTypeGUID;
        }
        else
        {
            return schemaTypeEntity.getGUID();
        }
    }


    /**
     * Add additional schema types that are part of a schema type.  Specifically for maps and schema type choices.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeGUID schema type entity to link to
     * @param schemaTypeGUIDParameterName parameter supplying schemaTypeGUID
     * @param schemaTypeTypeName name of schema type's type
     * @param schemaTypeBuilder properties of complete schema type
     * @param methodName calling method
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void addEmbeddedTypes(String            userId,
                          String            externalSourceGUID,
                          String            externalSourceName,
                          String            schemaTypeGUID,
                          String            schemaTypeGUIDParameterName,
                          String            schemaTypeTypeName,
                          SchemaTypeBuilder schemaTypeBuilder,
                          String            methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String mapFromParameterName = "mapFromSchemaType";
        final String mapToParameterName = "mapToSchemaType";
        final String externalParameterName = "externalSchemaType";
        final String optionParameterName = "optionSchemaType";

        if (OpenMetadataAPIMapper.EXTERNAL_SCHEMA_TYPE_TYPE_NAME.equals(schemaTypeBuilder.getTypeName()))
        {
            String externalSchemaGUID = schemaTypeBuilder.getExternalSchemaTypeGUID();

            if (externalSchemaGUID != null)
            {
                this.linkElementToElement(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          schemaTypeGUID,
                                          schemaTypeGUIDParameterName,
                                          schemaTypeTypeName,
                                          externalSchemaGUID,
                                          externalParameterName,
                                          OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                          OpenMetadataAPIMapper.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_GUID,
                                          OpenMetadataAPIMapper.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_NAME,
                                          null,
                                          methodName);
            }
        }
        else if (OpenMetadataAPIMapper.MAP_SCHEMA_TYPE_TYPE_NAME.equals(schemaTypeBuilder.getTypeName()))
        {
            SchemaTypeBuilder mapFromBuilder = schemaTypeBuilder.getMapFrom();
            SchemaTypeBuilder mapToBuilder   = schemaTypeBuilder.getMapTo();

            if (mapFromBuilder != null)
            {
                String mapFromGUID = addSchemaType(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   mapFromBuilder.qualifiedName,
                                                   mapFromBuilder,
                                                   methodName);

                if (mapFromGUID != null)
                {
                    this.linkElementToElement(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              schemaTypeGUID,
                                              schemaTypeGUIDParameterName,
                                              schemaTypeTypeName,
                                              mapFromGUID,
                                              mapFromParameterName,
                                              OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                              OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_GUID,
                                              OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_NAME,
                                              null,
                                              methodName);
                }
            }

            if (mapToBuilder != null)
            {
                String mapToGUID = addSchemaType(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 mapToBuilder.qualifiedName,
                                                 mapToBuilder,
                                                 methodName);

                if (mapToGUID != null)
                {
                    this.linkElementToElement(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              schemaTypeGUID,
                                              schemaTypeGUIDParameterName,
                                              schemaTypeTypeName,
                                              mapToGUID,
                                              mapToParameterName,
                                              OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                              OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_GUID,
                                              OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_NAME,
                                              null,
                                              methodName);
                }
            }
        }
        else if (OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME.equals(schemaTypeBuilder.getTypeName()))
        {
            List<SchemaTypeBuilder>  schemaOptionBuilders = schemaTypeBuilder.getSchemaOptions();

            if (schemaOptionBuilders != null)
            {
                for (SchemaTypeBuilder schemaOptionBuilder : schemaOptionBuilders)
                {
                    if (schemaOptionBuilder != null)
                    {
                        String optionGUID = addSchemaType(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          schemaOptionBuilder.qualifiedName,
                                                          schemaOptionBuilder,
                                                          methodName);

                        if (optionGUID != null)
                        {
                            this.linkElementToElement(userId,
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      schemaTypeGUID,
                                                      schemaTypeGUIDParameterName,
                                                      schemaTypeTypeName,
                                                      optionGUID,
                                                      optionParameterName,
                                                      OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                      OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_GUID,
                                                      OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME,
                                                      null,
                                                      methodName);
                        }
                    }
                }
            }
        }
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
    private String  addSchemaType(String            userId,
                                  String            externalSourceGUID,
                                  String            externalSourceName,
                                  String            qualifiedName,
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
                                                            qualifiedName,
                                                            OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                            schemaTypeBuilder,
                                                            methodName);

        addEmbeddedTypes(userId,
                         externalSourceGUID,
                         externalSourceName,
                         schemaTypeGUID,
                         schemaTypeGUIDParameterName,
                         OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                         schemaTypeBuilder,
                         methodName);

        return schemaTypeGUID;
    }


    /**
     * Count the number of attributes attached to an parent schema attribute or schema type.
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
                                                                       OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                       OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                       OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                       methodName);


        if (count == 0)
        {
            count = repositoryHandler.countAttachedRelationshipsByType(userId,
                                                                       schemaElementGUID,
                                                                       OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                       OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                       OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                       methodName);
        }

        return count;
    }
}
