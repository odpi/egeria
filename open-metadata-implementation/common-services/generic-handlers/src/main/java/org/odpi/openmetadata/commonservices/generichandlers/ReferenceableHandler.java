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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * ReferenceableHandler manages methods on generic referenceables.
 */
public class ReferenceableHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
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
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public ReferenceableHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Return the unique identifier of the bean with the requested qualified name.
     * The match is exact.  It uses the supportedZones supplied with the service.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param methodName calling method
     *
     * @return matching B bean
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String getBeanGUIDByQualifiedName(String       userId,
                                             String       typeGUID,
                                             String       typeName,
                                             String       name,
                                             String       nameParameterName,
                                             String       methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        return this.getBeanGUIDByQualifiedName(userId,
                                               typeGUID,
                                               typeName,
                                               name,
                                               nameParameterName,
                                               supportedZones,
                                               null,
                                               methodName);
    }


    /**
     * Return the bean with the requested qualified name.
     * The match is exact.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return matching B bean
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String getBeanGUIDByQualifiedName(String       userId,
                                             String       typeGUID,
                                             String       typeName,
                                             String       name,
                                             String       nameParameterName,
                                             List<String> serviceSupportedZones,
                                             Date         effectiveTime,
                                             String       methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        return this.getBeanGUIDByUniqueName(userId,
                                            name,
                                            nameParameterName,
                                            OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                            resultTypeGUID,
                                            resultTypeName,
                                            serviceSupportedZones,
                                            effectiveTime,
                                            methodName);
    }


    /**
     * Return the bean with the requested qualified name.
     * The match is exact. It uses the supportedZones supplied with the service.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param methodName calling method
     *
     * @return matching B bean
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getBeanByQualifiedName(String       userId,
                                    String       typeGUID,
                                    String       typeName,
                                    String       name,
                                    String       nameParameterName,
                                    String       methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return this.getBeanByQualifiedName(userId,
                                           typeGUID,
                                           typeName,
                                           name,
                                           nameParameterName,
                                           supportedZones,
                                           null,
                                           methodName);
    }


    /**
     * Return the bean with the requested qualified name.
     * The match is exact.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return matching B bean
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getBeanByQualifiedName(String       userId,
                                    String       typeGUID,
                                    String       typeName,
                                    String       name,
                                    String       nameParameterName,
                                    List<String> serviceSupportedZones,
                                    Date         effectiveTime,
                                    String       methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        return this.getBeanByUniqueName(userId,
                                        name,
                                        nameParameterName,
                                        OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                        resultTypeGUID,
                                        resultTypeName,
                                        serviceSupportedZones,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return a list of unique identifiers for referenceables with the requested qualified name.
     * The match is exact. It uses the supportedZones supplied with the service.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getBeanGUIDsByQualifiedName(String       userId,
                                                    String       typeGUID,
                                                    String       typeName,
                                                    String       name,
                                                    String       nameParameterName,
                                                    int          startFrom,
                                                    int          pageSize,
                                                    String       methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        return this.getBeanGUIDsByQualifiedName(userId,
                                                typeGUID,
                                                typeName,
                                                name,
                                                nameParameterName,
                                                supportedZones,
                                                startFrom,
                                                pageSize,
                                                null,
                                                methodName);
    }


    /**
     * Return a list of unique identifiers for referenceables with the requested qualified name.
     * The match is exact.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getBeanGUIDsByQualifiedName(String       userId,
                                                    String       typeGUID,
                                                    String       typeName,
                                                    String       name,
                                                    String       nameParameterName,
                                                    List<String> serviceSupportedZones,
                                                    int          startFrom,
                                                    int          pageSize,
                                                    Date         effectiveTime,
                                                    String       methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);

        return this.getEntityGUIDsByValue(userId,
                                          name,
                                          nameParameterName,
                                          resultTypeGUID,
                                          resultTypeName,
                                          specificMatchPropertyNames,
                                          true,
                                          null,
                                          null,
                                          false,
                                          serviceSupportedZones,
                                          null,
                                          startFrom,
                                          pageSize,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Return a list of referenceables with the requested qualified name.
     * The match is exact.  It uses the supportedZones supplied with the service.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getBeansByQualifiedName(String   userId,
                                           String   typeGUID,
                                           String   typeName,
                                           String   name,
                                           String   nameParameterName,
                                           int      startFrom,
                                           int      pageSize,
                                           String   methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        return getBeansByQualifiedName(userId,
                                       typeGUID,
                                       typeName,
                                       name,
                                       nameParameterName,
                                       supportedZones,
                                       startFrom,
                                       pageSize,
                                       null,
                                       methodName);
    }


    /**
     * Return a list of referenceables with the requested qualified name.
     * The match is exact.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getBeansByQualifiedName(String       userId,
                                           String       typeGUID,
                                           String       typeName,
                                           String       name,
                                           String       nameParameterName,
                                           List<String> serviceSupportedZones,
                                           int          startFrom,
                                           int          pageSize,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    resultTypeGUID,
                                    resultTypeName,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    false,
                                    serviceSupportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return the entity for a qualified name - if multiple entities have this name, an exception
     * is thrown.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail getEntityByUniqueQualifiedName(String       userId,
                                                       String       typeGUID,
                                                       String       typeName,
                                                       String       name,
                                                       String       nameParameterName,
                                                       String       methodName) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        return this.getEntityByUniqueQualifiedName(userId, typeGUID, typeName, name, nameParameterName, null, methodName);
    }


    /**
     * Return the entity for a qualified name that is effective at a specific time -
     * if multiple entities have this name, an exception is thrown.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail getEntityByUniqueQualifiedName(String       userId,
                                                       String       typeGUID,
                                                       String       typeName,
                                                       String       name,
                                                       String       nameParameterName,
                                                       Date         effectiveTime,
                                                       String       methodName) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);

        List<EntityDetail> matchingEntities = this.getEntitiesByValue(userId,
                                                                      name,
                                                                      nameParameterName,
                                                                      resultTypeGUID,
                                                                      resultTypeName,
                                                                      specificMatchPropertyNames,
                                                                      true,
                                                                      null,
                                                                      null,
                                                                      false,
                                                                      supportedZones,
                                                                      null,
                                                                      0,
                                                                      0,
                                                                      effectiveTime,
                                                                      methodName);

        if (matchingEntities == null)
        {
            return null;
        }
        else if (matchingEntities.size() == 1)
        {
            return matchingEntities.get(0);
        }

        errorHandler.handleAmbiguousEntityName(name,
                                               nameParameterName,
                                               resultTypeName,
                                               matchingEntities,
                                               methodName);
        /* not reachable */
        return null;
    }



    /**
     * Return a list of unique identifiers for referenceables with the requested qualified name.
     * The match is via a Regular Expression (RegEx).  It uses the supportedZones supplied with the service.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for - this is a regular expression (RegEx)
     * @param nameParameterName property that provided the name
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> findBeanGUIDsByQualifiedName(String       userId,
                                                     String       typeGUID,
                                                     String       typeName,
                                                     String       name,
                                                     String       nameParameterName,
                                                     int          startFrom,
                                                     int          pageSize,
                                                     String       methodName) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        return this.findBeanGUIDsByQualifiedName(userId,
                                                 typeGUID,
                                                 typeName,
                                                 name,
                                                 nameParameterName,
                                                 supportedZones,
                                                 startFrom,
                                                 pageSize,
                                                 null,
                                                 methodName);
    }


    /**
     * Return a list of unique identifiers for referenceables with the requested qualified name.
     * The match is via a Regular Expression (RegEx).
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for - this is a regular expression (RegEx)
     * @param nameParameterName property that provided the name
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> findBeanGUIDsByQualifiedName(String       userId,
                                                     String       typeGUID,
                                                     String       typeName,
                                                     String       name,
                                                     String       nameParameterName,
                                                     List<String> serviceSupportedZones,
                                                     int          startFrom,
                                                     int          pageSize,
                                                     Date         effectiveTime,
                                                     String       methodName) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);

        return this.getEntityGUIDsByValue(userId,
                                        name,
                                        nameParameterName,
                                        resultTypeGUID,
                                        resultTypeName,
                                        specificMatchPropertyNames,
                                        false,
                                        null,
                                        null,
                                        false,
                                        serviceSupportedZones,
                                        null,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return a list of referenceables with the requested qualified name.
     * The match is via a Regular Expression (RegEx).  It uses the supportedZones supplied with the service.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for - this is a regular expression (RegEx)
     * @param nameParameterName property that provided the name
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> findBeansByQualifiedName(String   userId,
                                            String   typeGUID,
                                            String   typeName,
                                            String   name,
                                            String   nameParameterName,
                                            int      startFrom,
                                            int      pageSize,
                                            String   methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return findBeansByQualifiedName(userId,
                                        typeGUID,
                                        typeName,
                                        name,
                                        nameParameterName,
                                        supportedZones,
                                        startFrom,
                                        pageSize,
                                        null,
                                        methodName);
    }


    /**
     * Return a list of referenceables with the requested qualified name.
     * The match is via a Regular Expression (RegEx).
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for - this is a regular expression (RegEx)
     * @param nameParameterName property that provided the name
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> findBeansByQualifiedName(String       userId,
                                            String       typeGUID,
                                            String       typeName,
                                            String       name,
                                            String       nameParameterName,
                                            List<String> serviceSupportedZones,
                                            int          startFrom,
                                            int          pageSize,
                                            Date         effectiveTime,
                                            String       methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    resultTypeGUID,
                                    resultTypeName,
                                    specificMatchPropertyNames,
                                    false,
                                    null,
                                    null,
                                    false,
                                    serviceSupportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Add or replace the ownership for a referenceable.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param owner name of the owner
     * @param ownerTypeName type of element that owner comes from
     * @param ownerPropertyName name of property used to identify owner
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addOwner(String userId,
                         String beanGUID,
                         String beanGUIDParameterName,
                         String beanGUIDTypeName,
                         String owner,
                         String ownerTypeName,
                         String ownerPropertyName,
                         String methodName) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        ReferenceableBuilder builder = new ReferenceableBuilder(OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID,
                                                                OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataAPIMapper.OWNERSHIP_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.OWNERSHIP_CLASSIFICATION_TYPE_NAME,
                                           builder.getOwnershipProperties(owner, ownerTypeName, ownerPropertyName, methodName),
                                           false,
                                           methodName);
    }


    /**
     * Remove the ownership classification from a referenceable.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeOwner(String userId,
                             String beanGUID,
                             String beanGUIDParameterName,
                             String beanGUIDTypeName,
                             String methodName) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataAPIMapper.OWNERSHIP_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.OWNERSHIP_CLASSIFICATION_TYPE_NAME,
                                                methodName);
    }



    /**
     * Add or replace the security tags for a referenceable.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param securityLabels list of security labels defining the security characteristics of the element
     * @param securityProperties Descriptive labels describing origin of the asset
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addSecurityTags(String                userId,
                                 String                beanGUID,
                                 String                beanGUIDParameterName,
                                 String                beanGUIDTypeName,
                                 List<String>          securityLabels,
                                 Map<String, Object>   securityProperties,
                                 String                methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        ReferenceableBuilder builder = new ReferenceableBuilder(OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID,
                                                                OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataAPIMapper.SECURITY_TAG_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.SECURITY_TAG_CLASSIFICATION_TYPE_NAME,
                                           builder.getSecurityTagProperties(securityLabels, securityProperties, methodName),
                                           false,
                                           methodName);
    }


    /**
     * Remove the security tags classification from a referenceable.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeSecurityTags(String userId,
                                    String beanGUID,
                                    String beanGUIDParameterName,
                                    String beanGUIDTypeName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataAPIMapper.SECURITY_TAG_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.SECURITY_TAG_CLASSIFICATION_TYPE_NAME,
                                                methodName);
    }


    /**
     * Classify a referenceable as "BusinessSignificant" (this may effect the way that lineage is displayed).
     *
     * @param userId calling user
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param description description of why this is significant
     * @param scope scope of its business significance
     * @param businessCapabilityGUID unique identifier of the business capability that rates this as significant
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setBusinessSignificant(String userId,
                                       String beanGUID,
                                       String beanGUIDParameterName,
                                       String beanGUIDTypeName,
                                       String description,
                                       String scope,
                                       String businessCapabilityGUID,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        ReferenceableBuilder builder = new ReferenceableBuilder(OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID,
                                                                OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataAPIMapper.BUSINESS_SIGNIFICANCE_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.BUSINESS_SIGNIFICANCE_CLASSIFICATION_TYPE_NAME,
                                           builder.getBusinessSignificanceProperties(description, scope, businessCapabilityGUID, methodName),
                                           false,
                                           methodName);
    }


    /**
     * Remove the "BusinessSignificant" designation from the element.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param methodName calling method
    *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearBusinessSignificant(String userId,
                                         String beanGUID,
                                         String beanGUIDParameterName,
                                         String beanGUIDTypeName,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataAPIMapper.BUSINESS_SIGNIFICANCE_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.BUSINESS_SIGNIFICANCE_CLASSIFICATION_TYPE_NAME,
                                                methodName);
    }


    /**
     * Classify an asset as suitable to be used as a template for cataloguing assets of a similar types.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param name name of the template
     * @param description description of when, where and how to use the template
     * @param additionalProperties any additional properties
     * @param methodName calling method
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addTemplateClassification(String              userId,
                                          String              beanGUID,
                                          String              beanGUIDParameterName,
                                          String              beanGUIDTypeName,
                                          String              name,
                                          String              description,
                                          Map<String, String> additionalProperties,
                                          String              methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        ReferenceableBuilder builder = new ReferenceableBuilder(OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID,
                                                                OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataAPIMapper.TEMPLATE_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.TEMPLATE_CLASSIFICATION_TYPE_NAME,
                                           builder.getTemplateProperties(name, description, additionalProperties, methodName),
                                           false,
                                           methodName);
    }


    /**
     * Remove the classification that indicates that this asset can be used as a template.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param methodName calling method
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeTemplateClassification(String userId,
                                             String beanGUID,
                                             String beanGUIDParameterName,
                                             String beanGUIDTypeName,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataAPIMapper.TEMPLATE_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.TEMPLATE_CLASSIFICATION_TYPE_NAME,
                                                methodName);
    }


    /**
     * Returns the list of elements that are linked to provide more information.  It uses the supportedZones supplied with the service.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
     * @param startFrom int      starting position for fist returned element.
     * @param pageSize  int      maximum number of elements to return on the call.
     * @param methodName String calling method
     *
     * @return a list of assets or
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<B> getMoreInformation(String userId,
                                      String startingGUID,
                                      String startingGUIDParameterName,
                                      String suppliedStartingTypeName,
                                      String suppliedResultingTypeName,
                                      int    startFrom,
                                      int    pageSize,
                                      String methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        return this.getMoreInformation(userId,
                                       startingGUID,
                                       startingGUIDParameterName,
                                       suppliedStartingTypeName,
                                       suppliedResultingTypeName,
                                       supportedZones,
                                       startFrom,
                                       pageSize,
                                       null,
                                       methodName);
    }


    /**
     * Returns the list of elements that are linked to provide more information.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
     * @param serviceSupportedZones supported zones for calling service
     * @param startFrom int      starting position for fist returned element.
     * @param pageSize  int      maximum number of elements to return on the call.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName String calling method
     *
     * @return a list of assets or
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<B> getMoreInformation(String       userId,
                                      String       startingGUID,
                                      String       startingGUIDParameterName,
                                      String       suppliedStartingTypeName,
                                      String       suppliedResultingTypeName,
                                      List<String> serviceSupportedZones,
                                      int          startFrom,
                                      int          pageSize,
                                      Date         effectiveTime,
                                      String       methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME;
        String resultingTypeName = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }
        if (suppliedResultingTypeName != null)
        {
            resultingTypeName = suppliedResultingTypeName;
        }

        return this.getAttachedElements(userId,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_MORE_INFO_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_MORE_INFO_TYPE_NAME,
                                        resultingTypeName,
                                        serviceSupportedZones,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Create a simple relationship between a glossary term and a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source - null for local
     * @param beanGUID unique identifier of the asset that is being described
     * @param beanGUIDParameter parameter supply the beanGUID
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param glossaryTermGUIDParameter parameter supplying the list of GlossaryTermGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException the guid properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void  saveSemanticAssignment(String userId,
                                        String externalSourceGUID,
                                        String externalSourceName,
                                        String beanGUID,
                                        String beanGUIDParameter,
                                        String glossaryTermGUID,
                                        String glossaryTermGUIDParameter,
                                        String methodName)  throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  beanGUID,
                                  beanGUIDParameter,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                  glossaryTermGUID,
                                  glossaryTermGUIDParameter,
                                  OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                  supportedZones,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_MEANING_TYPE_GUID,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_MEANING_TYPE_NAME,
                                  null,
                                  methodName);
    }



    /**
     * Create a simple relationship between a glossary term and a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source - null for local
     * @param beanGUID unique identifier of the element that is being described
     * @param beanGUIDParameter parameter supply the beanGUID
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param glossaryTermGUIDParameter parameter supplying the list of GlossaryTermGUID
     * @param description description of the assignment
     * @param expression expression used to determine the assignment
     * @param statusOrdinal the status of the assignment
     * @param confidence how confident is the assignment - no confidence = 0 to complete confidence = 100
     * @param createdBy who/what created the assignment
     * @param steward which steward is responsible for assignment
     * @param source where was the source of the assignment
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the guid properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void  saveSemanticAssignment(String userId,
                                        String externalSourceGUID,
                                        String externalSourceName,
                                        String beanGUID,
                                        String beanGUIDParameter,
                                        String glossaryTermGUID,
                                        String glossaryTermGUIDParameter,
                                        String description,
                                        String expression,
                                        int    statusOrdinal,
                                        int    confidence,
                                        String createdBy,
                                        String steward,
                                        String source,
                                        Date   effectiveFrom,
                                        Date   effectiveTo,
                                        String methodName)  throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                                     description,
                                                                                     methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.EXPRESSION_PROPERTY_NAME,
                                                                  expression,
                                                                  methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.STATUS_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.TERM_ASSIGNMENT_STATUS_ENUM_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.TERM_ASSIGNMENT_STATUS_ENUM_TYPE_NAME,
                                                                    statusOrdinal,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.TERM_ASSIGNMENT_STATUS_ENUM_TYPE_NAME);
        }

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.CONFIDENCE_PROPERTY_NAME,
                                                               confidence,
                                                               methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CREATED_BY_PROPERTY_NAME,
                                                                  createdBy,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME,
                                                                  steward,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME,
                                                                  source,
                                                                  methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  beanGUID,
                                  beanGUIDParameter,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                  glossaryTermGUID,
                                  glossaryTermGUIDParameter,
                                  OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                  supportedZones,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_MEANING_TYPE_GUID,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_MEANING_TYPE_NAME,
                                  setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                  methodName);
    }


    /**
     * Remove the relationship between a glossary term and a referenceable (typically
     * a field in the schema).
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param beanGUID unique identifier of the element that is being described
     * @param beanGUIDParameter parameter supply the beanGUID
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param glossaryTermGUIDParameter parameter supplying the list of GlossaryTermGUID
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeSemanticAssignment(String userId,
                                          String externalSourceGUID,
                                          String externalSourceName,
                                          String beanGUID,
                                          String beanGUIDParameter,
                                          String glossaryTermGUID,
                                          String glossaryTermGUIDParameter,
                                          Date   effectiveTime,
                                          String methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      beanGUID,
                                      beanGUIDParameter,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                      glossaryTermGUID,
                                      glossaryTermGUIDParameter,
                                      OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_GUID,
                                      OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                      supportedZones,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_MEANING_TYPE_GUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_MEANING_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Create a relationship between a referenceable and a resource it uses.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source - null for local
     * @param beanGUID unique identifier of the starting element
     * @param beanGUIDParameter parameter supplying the beanGUID
     * @param memberGUID unique identifier of the element to link
     * @param memberGUIDParameter parameter supplying the memberGUID
     * @param resourceUse description of the way that the resource list is used
     * @param watchResource should changes in the members result in notifications
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the guid properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void  saveResourceListMember(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  beanGUID,
                                        String  beanGUIDParameter,
                                        String  memberGUID,
                                        String  memberGUIDParameter,
                                        String  resourceUse,
                                        boolean watchResource,
                                        Date    effectiveFrom,
                                        Date    effectiveTo,
                                        String  methodName)  throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.RESOURCE_USE_PROPERTY_NAME,
                                                                                     resourceUse,
                                                                                     methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.WATCH_RESOURCE_PROPERTY_NAME,
                                                                  watchResource,
                                                                  methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  beanGUID,
                                  beanGUIDParameter,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                  memberGUID,
                                  memberGUIDParameter,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                  supportedZones,
                                  OpenMetadataAPIMapper.RESOURCE_LIST_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataAPIMapper.RESOURCE_LIST_RELATIONSHIP_TYPE_NAME,
                                  setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                  methodName);
    }


    /**
     * Remove the relationship between a referenceable and a resource it uses.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param beanGUID unique identifier of the referenceable
     * @param beanGUIDParameter parameter supplying beanGUID
     * @param memberGUID unique identifier of the glossary term
     * @param memberGUIDParameter parameter supplying memberGUID
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeResourceListMember(String userId,
                                          String externalSourceGUID,
                                          String externalSourceName,
                                          String beanGUID,
                                          String beanGUIDParameter,
                                          String memberGUID,
                                          String memberGUIDParameter,
                                          Date   effectiveTime,
                                          String methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        if (beanGUID != null)
        {
            this.unlinkElementFromElement(userId,
                                          false,
                                          externalSourceGUID,
                                          externalSourceName,
                                          beanGUID,
                                          beanGUIDParameter,
                                          OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                          memberGUID,
                                          memberGUIDParameter,
                                          OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID,
                                          OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                          supportedZones,
                                          OpenMetadataAPIMapper.RESOURCE_LIST_RELATIONSHIP_TYPE_GUID,
                                          OpenMetadataAPIMapper.RESOURCE_LIST_RELATIONSHIP_TYPE_NAME,
                                          effectiveTime,
                                          methodName);
        }
    }




    /**
     * Return the list of elements associated with a collection.
     *
     * @param userId calling user
     * @param collectionGUID unique identifier of the collection to query
     * @param collectionGUIDParameterName name of the parameter supplying collectionGUID
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of metadata elements describing the definitions associated with the requested definition
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getCollectionMembers(String userId,
                                          String collectionGUID,
                                          String collectionGUIDParameterName,
                                          int    startFrom,
                                          int    pageSize,
                                          Date   effectiveTime,
                                          String methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        collectionGUID,
                                        collectionGUIDParameterName,
                                        OpenMetadataAPIMapper.COLLECTION_TYPE_NAME,
                                        OpenMetadataAPIMapper.COLLECTION_MEMBERSHIP_TYPE_GUID,
                                        OpenMetadataAPIMapper.COLLECTION_MEMBERSHIP_TYPE_NAME,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Create a simple relationship between two elements in an Asset description (typically the asset itself or
     * attributes in their schema).
     *
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element1GUIDParameter name of parameter supplying element1GUID
     * @param element2GUID unique identifier of second element
     * @param element2GUIDParameter name of parameter supplying element2GUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  linkElementsAsDuplicates(String userId,
                                          String element1GUID,
                                          String element1GUIDParameter,
                                          String element2GUID,
                                          String element2GUIDParameter,
                                          String methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(element1GUID, element1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(element2GUID, element2GUIDParameter, methodName);

        /*
         * First check the GUIDs are valid.
         */
        EntityDetail entity1 = this.getEntityFromRepository(userId,
                                                            element1GUID,
                                                            element1GUIDParameter,
                                                            OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                            null,
                                                            null,
                                                            true,
                                                            supportedZones,
                                                            null,
                                                            methodName);

        EntityDetail entity2 = this.getEntityFromRepository(userId,
                                                            element2GUID,
                                                            element2GUIDParameter,
                                                            OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                            null,
                                                            null,
                                                            true,
                                                            supportedZones,
                                                            null,
                                                            methodName);

        /*
         * Next set up the classifications if needed.
         */
        boolean classificationNeeded = true;
        try
        {
            if (repositoryHelper.getClassificationFromEntity(serviceName,
                                                             entity1,
                                                             OpenMetadataAPIMapper.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME,
                                                             methodName) == null)
            {
                classificationNeeded = false;
            }
        }
        catch (ClassificationErrorException classificationError)
        {
            // nothing to do
        }

        if (classificationNeeded)
        {
            this.setClassificationInRepository(userId,
                                               element1GUID,
                                               element1GUIDParameter,
                                               OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                               OpenMetadataAPIMapper.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_GUID,
                                               OpenMetadataAPIMapper.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME,
                                               null,
                                               methodName);
        }

        classificationNeeded = true;
        try
        {
            if (repositoryHelper.getClassificationFromEntity(serviceName,
                                                             entity2,
                                                             OpenMetadataAPIMapper.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME,
                                                             methodName) == null)
            {
                classificationNeeded = false;
            }
        }
        catch (ClassificationErrorException classificationError)
        {
            // nothing to do
        }

        if (classificationNeeded)
        {
            this.setClassificationInRepository(userId,
                                               element2GUID,
                                               element2GUIDParameter,
                                               OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                               OpenMetadataAPIMapper.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_GUID,
                                               OpenMetadataAPIMapper.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME,
                                               null,
                                               methodName);
        }

        /*
         * Finally link the entities together.
         */
        InstanceProperties properties  = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                      null,
                                                                                      OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME,
                                                                                      userId,
                                                                                      methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME,
                                                                  serviceName,
                                                                  methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.DUPLICATE_TYPE_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.DUPLICATE_TYPE_ENUM_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.DUPLICATE_TYPE_ENUM_TYPE_NAME,
                                                                    OpenMetadataAPIMapper.PEER_ORDINAL,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.DUPLICATE_TYPE_ENUM_TYPE_NAME);
        }

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.STATUS_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_STATUS_ENUM_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_STATUS_ENUM_TYPE_NAME,
                                                                    OpenMetadataAPIMapper.VALIDATED_GC_STATUS_ORDINAL,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_STATUS_ENUM_TYPE_NAME);
        }

        this.linkElementToElement(userId,
                                  null,
                                  null,
                                  element1GUID,
                                  element1GUIDParameter,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                  element2GUID,
                                  element2GUIDParameter,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                  supportedZones,
                                  OpenMetadataAPIMapper.KNOWN_DUPLICATE_LINK_TYPE_GUID,
                                  OpenMetadataAPIMapper.KNOWN_DUPLICATE_LINK_TYPE_NAME,
                                  properties,
                                  methodName);
    }


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element1GUIDParameter name of parameter supplying element1GUID
     * @param element2GUID unique identifier of second element
     * @param element2GUIDParameter name of parameter supplying element2GUID
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  unlinkElementsAsDuplicates(String userId,
                                            String element1GUID,
                                            String element1GUIDParameter,
                                            String element2GUID,
                                            String element2GUIDParameter,
                                            Date   effectiveTime,
                                            String methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(element1GUID, element1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(element2GUID, element2GUIDParameter, methodName);

        /*
         * The repository helper will validate the types of GUIDs etc
         */
        repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                            null,
                                                            null,
                                                            OpenMetadataAPIMapper.KNOWN_DUPLICATE_LINK_TYPE_GUID,
                                                            OpenMetadataAPIMapper.KNOWN_DUPLICATE_LINK_TYPE_NAME,
                                                            element1GUID,
                                                            OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                            element2GUID,
                                                            effectiveTime,
                                                            methodName);

        /*
         * Determine whether the entities are still linked as duplicates.  If they are not, then remove their known duplicate classification.
         */
        if (this.getAttachmentLinks(userId,
                                    element1GUID,
                                    element1GUIDParameter,
                                    OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                    OpenMetadataAPIMapper.KNOWN_DUPLICATE_LINK_TYPE_GUID,
                                    OpenMetadataAPIMapper.KNOWN_DUPLICATE_LINK_TYPE_NAME,
                                    null,
                                    null,
                                    0,
                                    0,
                                    invalidParameterHandler.getMaxPagingSize(),
                                    effectiveTime,
                                    methodName) == null)
        {
            this.removeClassificationFromRepository(userId,
                                                    null,
                                                    null,
                                                    element1GUID,
                                                    element1GUIDParameter,
                                                    OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                    OpenMetadataAPIMapper.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_GUID,
                                                    OpenMetadataAPIMapper.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME,
                                                    methodName);
        }


        if (this.getAttachmentLinks(userId,
                                    element2GUID,
                                    element2GUIDParameter,
                                    OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                    OpenMetadataAPIMapper.KNOWN_DUPLICATE_LINK_TYPE_GUID,
                                    OpenMetadataAPIMapper.KNOWN_DUPLICATE_LINK_TYPE_NAME,
                                    null,
                                    null,
                                    0,
                                    0,
                                    invalidParameterHandler.getMaxPagingSize(),
                                    effectiveTime,
                                    methodName) == null)
        {
            this.removeClassificationFromRepository(userId,
                                                    null,
                                                    null,
                                                    element2GUID,
                                                    element2GUIDParameter,
                                                    OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                    OpenMetadataAPIMapper.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_GUID,
                                                    OpenMetadataAPIMapper.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME,
                                                    methodName);
        }
    }


    /**
     * Create the property facet for the vendor properties.
     *
     * @param userId calling user
     * @param referenceableGUID unique identifier of the software server capability
     * @param vendorProperties properties for the vendor
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void setVendorProperties(String               userId,
                                    String               referenceableGUID,
                                    Map<String, String>  vendorProperties,
                                    String               methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String referenceableGUIDParameter = "referenceableGUID";
        final String propertyFacetGUIDParameter = "propertyFacet[x]";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);

        List<EntityDetail> propertyFacets = this.getAttachedEntities(userId,
                                                                     referenceableGUID,
                                                                     referenceableGUIDParameter,
                                                                     OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.REFERENCEABLE_TO_PROPERTY_FACET_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.REFERENCEABLE_TO_PROPERTY_FACET_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.PROPERTY_FACET_TYPE_NAME,
                                                                     null,
                                                                     null,
                                                                     false,
                                                                     supportedZones,
                                                                     0,
                                                                     invalidParameterHandler.getMaxPagingSize(),
                                                                     null,
                                                                     methodName);

        if (vendorProperties != null)
        {
            PropertyFacetBuilder builder = new PropertyFacetBuilder(referenceableGUID + "_" + OpenMetadataAPIMapper.VENDOR_PROPERTIES_DESCRIPTION_VALUE,
                                                                    null,
                                                                    OpenMetadataAPIMapper.VENDOR_PROPERTIES_DESCRIPTION_VALUE,
                                                                    vendorProperties,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

            if (propertyFacets != null)
            {
                for (EntityDetail propertyFacet : propertyFacets)
                {
                    if (propertyFacet != null)
                    {
                        String description = repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                                propertyFacet.getProperties(),
                                                                                methodName);

                        if (OpenMetadataAPIMapper.VENDOR_PROPERTIES_DESCRIPTION_VALUE.equals(description))
                        {
                            this.updateBeanInRepository(userId,
                                                        null,
                                                        null,
                                                        propertyFacet.getGUID(),
                                                        propertyFacetGUIDParameter,
                                                        OpenMetadataAPIMapper.PROPERTY_FACET_TYPE_GUID,
                                                        OpenMetadataAPIMapper.PROPERTY_FACET_TYPE_NAME,
                                                        supportedZones,
                                                        builder.getInstanceProperties(methodName),
                                                        true,
                                                        methodName);
                        }
                    }
                }
            }
            else
            {
                /*
                 * No property facet exists - so create one.
                 */
                builder.setAnchors(userId, referenceableGUID, methodName);

                String propertyFacetGUID = createBeanInRepository(userId,
                                                                  null,
                                                                  null,
                                                                  OpenMetadataAPIMapper.PROPERTY_FACET_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.PROPERTY_FACET_TYPE_NAME,
                                                                  null,
                                                                  null,
                                                                  builder,
                                                                  methodName);

                InstanceProperties relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                         null,
                                                                                                         OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME,
                                                                                                         serviceName,
                                                                                                         methodName);
                linkElementToElement(userId,
                                     null,
                                     null,
                                     referenceableGUID,
                                     referenceableGUIDParameter,
                                     OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                     propertyFacetGUID,
                                     propertyFacetGUIDParameter,
                                     OpenMetadataAPIMapper.PROPERTY_FACET_TYPE_NAME,
                                     supportedZones,
                                     OpenMetadataAPIMapper.REFERENCEABLE_TO_PROPERTY_FACET_TYPE_GUID,
                                     OpenMetadataAPIMapper.REFERENCEABLE_TO_PROPERTY_FACET_TYPE_NAME,
                                     relationshipProperties,
                                     methodName);
            }
        }
        else
        {
            if (propertyFacets != null)
            {
                for (EntityDetail propertyFacet : propertyFacets)
                {
                    if (propertyFacet != null)
                    {
                        String description = repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                                propertyFacet.getProperties(),
                                                                                methodName);

                        if (OpenMetadataAPIMapper.VENDOR_PROPERTIES_DESCRIPTION_VALUE.equals(description))
                        {
                            this.deleteBeanInRepository(userId,
                                                        null,
                                                        null,
                                                        propertyFacet.getGUID(),
                                                        propertyFacetGUIDParameter,
                                                        OpenMetadataAPIMapper.PROPERTY_FACET_TYPE_GUID,
                                                        OpenMetadataAPIMapper.PROPERTY_FACET_TYPE_NAME,
                                                        null,
                                                        null,
                                                        methodName);
                        }
                    }
                }
            }
        }
    }


    /**
     * Retrieve the property facet for the vendor properties. It uses the supportedZones supplied with the service.
     *
     * @param userId calling user
     * @param referenceableGUID unique identifier of the metadata element
     * @param referenceableGUIDParameter parameter name for referenceableGUID
     * @param methodName calling method
     *
     * @return map of properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public Map<String, String> getVendorProperties(String userId,
                                                   String referenceableGUID,
                                                   String referenceableGUIDParameter,
                                                   String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        List<EntityDetail> propertyFacets = this.getAttachedEntities(userId,
                                                                     referenceableGUID,
                                                                     referenceableGUIDParameter,
                                                                     OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.REFERENCEABLE_TO_PROPERTY_FACET_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.REFERENCEABLE_TO_PROPERTY_FACET_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.PROPERTY_FACET_TYPE_NAME,
                                                                     null,
                                                                     null,
                                                                     false,
                                                                     supportedZones,
                                                                     0,
                                                                     invalidParameterHandler.getMaxPagingSize(),
                                                                     null,
                                                                     methodName);

        if (propertyFacets != null)
        {
            for (EntityDetail propertyFacet : propertyFacets)
            {
                if (propertyFacet != null)
                {
                    String description = repositoryHelper.getStringProperty(serviceName,
                                                                            OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                            propertyFacet.getProperties(),
                                                                            methodName);

                    if (OpenMetadataAPIMapper.VENDOR_PROPERTIES_DESCRIPTION_VALUE.equals(description))
                    {
                        return repositoryHelper.getStringMapFromProperty(serviceName,
                                                                         OpenMetadataAPIMapper.PROPERTIES_PROPERTY_NAME,
                                                                         propertyFacet.getProperties(),
                                                                         methodName);
                    }
                }
            }
        }

        return null;
    }
}
