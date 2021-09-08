/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * DisplayDataContainerHandler manages the schemas for display data containers.  It is build on the
 * SchemaAttributeHandler.  Its role is to maintain a simple interface.  It automatically creates and links in the schema type
 * information in the structure and keeps it linked together.
 */
public class DisplayDataContainerHandler<SCHEMA_ATTRIBUTE, SCHEMA_TYPE> extends SchemaElementHandler<SCHEMA_ATTRIBUTE>

{
    private String                                                 serviceName;
    private String                                                 serverName;
    private OMRSRepositoryHelper                                   repositoryHelper;
    private InvalidParameterHandler                                invalidParameterHandler;
    private SchemaAttributeHandler<SCHEMA_ATTRIBUTE, SCHEMA_TYPE>  dataContainerHandler;

    protected RepositoryErrorHandler errorHandler;


    /**
     * Construct the relational data handler with information needed to work with assets and schemas.
     *
     * @param dataContainerConverter specific converter for the SCHEMA_ATTRIBUTE bean class
     * @param dataContainerClass name of bean class that is represented by the generic class SCHEMA_ATTRIBUTE
     * @param schemaTypeConverter specific converter for the SCHEMA_TYPE bean class
     * @param schemaTypeClass name of bean class that is represented by the generic class SCHEMA_TYPE
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public DisplayDataContainerHandler(OpenMetadataAPIGenericConverter<SCHEMA_ATTRIBUTE> dataContainerConverter,
                                       Class<SCHEMA_ATTRIBUTE>                           dataContainerClass,
                                       OpenMetadataAPIGenericConverter<SCHEMA_TYPE>      schemaTypeConverter,
                                       Class<SCHEMA_TYPE>                                schemaTypeClass,
                                       String                                            serviceName,
                                       String                                            serverName,
                                       InvalidParameterHandler                           invalidParameterHandler,
                                       RepositoryHandler                                 repositoryHandler,
                                       OMRSRepositoryHelper                              repositoryHelper,
                                       String                                            localServerUserId,
                                       OpenMetadataServerSecurityVerifier                securityVerifier,
                                       List<String>                                      supportedZones,
                                       List<String>                                      defaultZones,
                                       List<String>                                      publishZones,
                                       AuditLog auditLog)
    {
        super(dataContainerConverter,
              dataContainerClass,
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

        this.serviceName                     = serviceName;
        this.serverName                      = serverName;
        this.invalidParameterHandler         = invalidParameterHandler;
        this.repositoryHelper                = repositoryHelper;



        this.dataContainerHandler = new SchemaAttributeHandler<>(dataContainerConverter,
                                                                 dataContainerClass,
                                                                 schemaTypeConverter,
                                                                 schemaTypeClass,
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


        this.errorHandler = new RepositoryErrorHandler(repositoryHelper, serviceName, serverName, auditLog);
    }



    /**
     * Create a new metadata element to represent a data container.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the DBMS
     * @param externalSourceName unique name of software server capability representing the DBMS
     * @param parentGUID unique identifier of the data container's parent where the data container is located
     * @param qualifiedName unique name for the data container
     * @param displayName the stored display name property for the data container
     * @param description the stored description property associated with the data container
     * @param isDeprecated is this table deprecated?
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of DisplayDataContainer - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param vendorProperties additional properties relating to the source of the data container technology
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the data container
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataContainer(String               userId,
                                      String               externalSourceGUID,
                                      String               externalSourceName,
                                      String               parentGUID,
                                      String               qualifiedName,
                                      String               displayName,
                                      String               description,
                                      boolean              isDeprecated,
                                      int                  elementPosition,
                                      int                  minCardinality,
                                      int                  maxCardinality,
                                      Map<String, String>  additionalProperties,
                                      String               typeName,
                                      Map<String, Object>  extendedProperties,
                                      Map<String, String>  vendorProperties,
                                      String               methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String parentElementGUIDParameterName = "parentGUID";
        final String schemaTypeGUIDParameterName    = "schemaTypeGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        String databaseSchemaTypeGUID = dataContainerHandler.getAssetSchemaTypeGUID(userId,
                                                                                    externalSourceGUID,
                                                                                    externalSourceName,
                                                                                    parentGUID,
                                                                                    parentElementGUIDParameterName,
                                                                                    OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                                                                                    OpenMetadataAPIMapper.RELATIONAL_DB_SCHEMA_TYPE_TYPE_GUID,
                                                                                    OpenMetadataAPIMapper.RELATIONAL_DB_SCHEMA_TYPE_TYPE_NAME,
                                                                                    methodName);

        if (databaseSchemaTypeGUID != null)
        {
            /*
             * A data container is represented as a schemaAttribute of type DisplayDataContainer (or a subtype).
             * Check that the type name requested is valid.
             */
            String attributeTypeName = OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_NAME;
            String attributeTypeId   = OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_GUID;

            if (typeName != null)
            {
                attributeTypeName = typeName;
                attributeTypeId   = invalidParameterHandler.validateTypeName(typeName,
                                                                             OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_NAME,
                                                                             serviceName,
                                                                             methodName,
                                                                             repositoryHelper);
            }

            /*
             * Load up the builder objects for processing by the dataContainerHandler.  The builders manage the properties
             * of the metadata elements that make up the data container, and the schemaTypeHandler manages the elements themselves.
             */
            SchemaAttributeBuilder schemaAttributeBuilder = new SchemaAttributeBuilder(qualifiedName,
                                                                                       displayName,
                                                                                       description,
                                                                                       elementPosition,
                                                                                       minCardinality,
                                                                                       maxCardinality,
                                                                                       isDeprecated,
                                                                                       null,
                                                                                       true,
                                                                                       false,
                                                                                       0,
                                                                                       0,
                                                                                       0,
                                                                                       0,
                                                                                       false,
                                                                                       null,
                                                                                       null,
                                                                                       additionalProperties,
                                                                                       attributeTypeId,
                                                                                       attributeTypeName,
                                                                                       extendedProperties,
                                                                                       repositoryHelper,
                                                                                       serviceName,
                                                                                       serverName);

            schemaAttributeBuilder.setAnchors(userId, parentGUID, methodName);

            SchemaTypeBuilder schemaTypeBuilder = new SchemaTypeBuilder(qualifiedName + ":containerType",
                                                                        OpenMetadataAPIMapper.DISPLAY_DATA_SCHEMA_TYPE_TYPE_GUID,
                                                                        OpenMetadataAPIMapper.DISPLAY_DATA_SCHEMA_TYPE_TYPE_NAME,
                                                                        repositoryHelper,
                                                                        serviceName,
                                                                        serverName);

            schemaAttributeBuilder.setSchemaType(userId, schemaTypeBuilder, methodName);

            String dataContainerGUID = dataContainerHandler.createNestedSchemaAttribute(userId,
                                                                                        externalSourceGUID,
                                                                                        externalSourceName,
                                                                                        databaseSchemaTypeGUID,
                                                                                        schemaTypeGUIDParameterName,
                                                                                        OpenMetadataAPIMapper.RELATIONAL_DB_SCHEMA_TYPE_TYPE_NAME,
                                                                                        OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                                        OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                                        qualifiedName,
                                                                                        qualifiedNameParameterName,
                                                                                        schemaAttributeBuilder,
                                                                                        methodName);

            if (dataContainerGUID != null)
            {
                dataContainerHandler.setVendorProperties(userId, dataContainerGUID, vendorProperties, methodName);
            }

            return dataContainerGUID;
        }

        /*
         * Not reachable because any failures result in exceptions.
         */
        return null;
    }


    /**
     * Create a new metadata element to represent a data container using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the DBMS - if null a local element is created
     * @param externalSourceName unique name of software server capability representing the DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param parentGUID unique identifier of the data container's parent where the data container is located.
     * @param qualifiedName unique name for the data container's parent
     * @param displayName the stored display name property for the data container
     * @param description the stored description property associated with the data container
     * @param methodName calling method
     *
     * @return unique identifier of the new data container's parent
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataContainerFromTemplate(String               userId,
                                                  String               externalSourceGUID,
                                                  String               externalSourceName,
                                                  String               templateGUID,
                                                  String               parentGUID,
                                                  String               qualifiedName,
                                                  String               displayName,
                                                  String               description,
                                                  String               methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String guidParameterName = "parentGUID";
        final String parentElementGUIDParameterName = "parentGUID";
        final String templateParameterName = "templateGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateGUID(parentGUID, guidParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * If the deployed data container's parent (which is an asset) is new, it will not have a schema type attached.
         * However, if there are other tables already attached, the schema type will be there too.
         */
        String databaseSchemaTypeGUID = dataContainerHandler.getAssetSchemaTypeGUID(userId,
                                                                                    externalSourceGUID,
                                                                                    externalSourceName,
                                                                                    parentGUID,
                                                                                    parentElementGUIDParameterName,
                                                                                    OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                                                                                    OpenMetadataAPIMapper.RELATIONAL_DB_SCHEMA_TYPE_TYPE_GUID,
                                                                                    OpenMetadataAPIMapper.RELATIONAL_DB_SCHEMA_TYPE_TYPE_NAME,
                                                                                    methodName);

        if (databaseSchemaTypeGUID != null)
        {
            SchemaAttributeBuilder builder = new SchemaAttributeBuilder(qualifiedName,
                                                                        displayName,
                                                                        description,
                                                                        repositoryHelper,
                                                                        serviceName,
                                                                        serverName);

            builder.setAnchors(userId, parentGUID, methodName);

            String dataContainerGUID = dataContainerHandler.createBeanFromTemplate(userId,
                                                                                   externalSourceGUID,
                                                                                   externalSourceName,
                                                                                   templateGUID,
                                                                                   templateParameterName,
                                                                                   OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_GUID,
                                                                                   OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_NAME,
                                                                                   qualifiedName,
                                                                                   qualifiedNameParameterName,
                                                                                   builder,
                                                                                   methodName);

            if (dataContainerGUID != null)
            {
                final String databaseSchemaTypeGUIDParameterName = "databaseSchemaTypeGUID";
                final String dataContainerGUIDParameterName = "dataContainerGUID";

                dataContainerHandler.linkElementToElement(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          databaseSchemaTypeGUID,
                                                          databaseSchemaTypeGUIDParameterName,
                                                          OpenMetadataAPIMapper.RELATIONAL_DB_SCHEMA_TYPE_TYPE_NAME,
                                                          dataContainerGUID,
                                                          dataContainerGUIDParameterName,
                                                          OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_NAME,
                                                          OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                          OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                          null,
                                                          methodName);
                return dataContainerGUID;
            }
        }

        return null;
    }


    /**
     * Update the metadata element representing a data container.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the DBMS
     * @param externalSourceName unique name of software server capability representing the DBMS
     * @param dataContainerGUID unique identifier of the data container to update
     * @param qualifiedName unique name for the data container's parent
     * @param displayName the stored display name property for the data container
     * @param description the stored description property associated with the data container
     * @param isDeprecated is this table deprecated?
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of DisplayDataContainer - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param vendorProperties additional properties relating to the source of the data container technology
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDataContainer(String               userId,
                                    String               externalSourceGUID,
                                    String               externalSourceName,
                                    String               dataContainerGUID,
                                    String               qualifiedName,
                                    String               displayName,
                                    String               description,
                                    boolean              isDeprecated,
                                    Map<String, String>  additionalProperties,
                                    String               typeName,
                                    Map<String, Object>  extendedProperties,
                                    Map<String, String>  vendorProperties,
                                    boolean              isMergeUpdate,
                                    String               methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String elementGUIDParameterName   = "dataContainerGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataContainerGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * A data container is represented as a schemaAttribute of type DisplayDataContainer (or a subtype).
         * Check that the type name requested is valid.
         */
        String attributeTypeName = OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_NAME;
        String attributeTypeId   = OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_GUID;

        if (typeName != null)
        {
            attributeTypeName = typeName;
            attributeTypeId   = invalidParameterHandler.validateTypeName(typeName,
                                                                         OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_NAME,
                                                                         serviceName,
                                                                         methodName,
                                                                         repositoryHelper);
        }

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                                     qualifiedName,
                                                                                     methodName);

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.IS_DEPRECATED_PROPERTY_NAME,
                                                                   isDeprecated,
                                                                   methodName);

        if (additionalProperties != null)
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                         additionalProperties,
                                                                         methodName);
        }

        if (extendedProperties != null)
        {
            try
            {
                properties = repositoryHelper.addPropertyMapToInstance(serviceName,
                                                                       null,
                                                                       extendedProperties,
                                                                       methodName);
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException error)
            {
                final String  propertyName = "extendedProperties";

                errorHandler.handleUnsupportedProperty(error, methodName, propertyName);
            }
        }

        dataContainerHandler.updateBeanInRepository(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    dataContainerGUID,
                                                    elementGUIDParameterName,
                                                    attributeTypeId,
                                                    attributeTypeName,
                                                    properties,
                                                    isMergeUpdate,
                                                    methodName);

        dataContainerHandler.setVendorProperties(userId, dataContainerGUID, vendorProperties, methodName);
    }


    /**
     * Remove the metadata element representing a data container.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the DBMS
     * @param externalSourceName unique name of software server capability representing the DBMS
     * @param dataContainerGUID unique identifier of the metadata element to remove
     * @param dataContainerGUIDParameterName name of parameter supplying dataContainerGUID
     * @param qualifiedName unique name of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDataContainer(String userId,
                                    String externalSourceGUID,
                                    String externalSourceName,
                                    String dataContainerGUID,
                                    String dataContainerGUIDParameterName,
                                    String qualifiedName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        dataContainerHandler.deleteBeanInRepository(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    dataContainerGUID,
                                                    dataContainerGUIDParameterName,
                                                    OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_GUID,
                                                    OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_NAME,
                                                    OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                    qualifiedName,
                                                    methodName);
    }


    /**
     * Retrieve the list of data container metadata elements that contain the search string.
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
    public List<SCHEMA_ATTRIBUTE> findDataContainers(String userId,
                                                     String searchString,
                                                     int    startFrom,
                                                     int    pageSize,
                                                     String methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        return dataContainerHandler.findSchemaAttributes(userId,
                                                         searchString,
                                                         searchStringParameterName,
                                                         OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_GUID,
                                                         OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_NAME,
                                                         null,
                                                         null,
                                                         startFrom,
                                                         pageSize,
                                                         methodName);
    }


    /**
     * Retrieve the list of data containers associated with a data container's parent.
     *
     * @param userId calling user
     * @param parentGUID unique identifier of the data container's parent of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SCHEMA_ATTRIBUTE> getContainersForParent(String userId,
                                                         String parentGUID,
                                                         int    startFrom,
                                                         int    pageSize,
                                                         String methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String parentElementGUIDParameterName = "parentGUID";

        /*
         * If the deployed data container's parent (which is an asset) is new, it will not have a schema type attached.
         * However, if there are other tables already attached, the schema type will be there too.
         */
        String databaseSchemaTypeGUID = dataContainerHandler.getAssetSchemaTypeGUID(userId,
                                                                                    null,
                                                                                    null,
                                                                                    parentGUID,
                                                                                    parentElementGUIDParameterName,
                                                                                    OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                                                                                    OpenMetadataAPIMapper.RELATIONAL_DB_SCHEMA_TYPE_TYPE_GUID,
                                                                                    OpenMetadataAPIMapper.RELATIONAL_DB_SCHEMA_TYPE_TYPE_NAME,
                                                                                    methodName);

        if (databaseSchemaTypeGUID != null)
        {
            final String databaseSchemaTypeGUIDParameterName = "databaseSchemaTypeGUID";

            return dataContainerHandler.getSchemaAttributesForComplexSchemaType(userId,
                                                                                databaseSchemaTypeGUID,
                                                                                databaseSchemaTypeGUIDParameterName,
                                                                                null,
                                                                                OpenMetadataAPIMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                                                startFrom,
                                                                                pageSize,
                                                                                methodName);
        }

        return null;
    }


    /**
     * Retrieve the list of data container metadata elements with a matching qualified or display name.
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
    public List<SCHEMA_ATTRIBUTE> getDataContainersByName(String userId,
                                                          String name,
                                                          int    startFrom,
                                                          int    pageSize,
                                                          String methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return dataContainerHandler.getSchemaAttributesByName(userId,
                                                              OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_GUID,
                                                              OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_NAME,
                                                              name,
                                                              null,
                                                              null,
                                                              startFrom,
                                                              pageSize,
                                                              methodName);
    }


    /**
     * Retrieve the data container metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SCHEMA_ATTRIBUTE getDataContainerByGUID(String userId,
                                                   String guid,
                                                   String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String guidParameterName = "guid";

        return dataContainerHandler.getSchemaAttribute(userId,
                                                       guid,
                                                       guidParameterName,
                                                       OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_NAME,
                                                       null,
                                                       null,
                                                       methodName);
    }
}
