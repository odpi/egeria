/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.rex.handlers;


import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceAuditHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.clients.EnterpriseRepositoryServicesClient;
import org.odpi.openmetadata.repositoryservices.clients.LocalRepositoryServicesClient;
import org.odpi.openmetadata.repositoryservices.clients.MetadataCollectionServicesClient;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.viewservices.rex.api.ffdc.RexExceptionHandler;
import org.odpi.openmetadata.viewservices.rex.api.ffdc.RexViewErrorCode;
import org.odpi.openmetadata.viewservices.rex.api.ffdc.RexViewServiceException;
import org.odpi.openmetadata.viewservices.rex.api.properties.ClassificationExplorer;
import org.odpi.openmetadata.viewservices.rex.api.properties.EntityExplorer;
import org.odpi.openmetadata.viewservices.rex.api.properties.RelationshipExplorer;
import org.odpi.openmetadata.viewservices.rex.api.properties.ResourceEndpoint;
import org.odpi.openmetadata.viewservices.rex.api.properties.RexEntityDigest;
import org.odpi.openmetadata.viewservices.rex.api.properties.RexExpandedEntityDetail;
import org.odpi.openmetadata.viewservices.rex.api.properties.RexExpandedRelationship;
import org.odpi.openmetadata.viewservices.rex.api.properties.RexPreTraversal;
import org.odpi.openmetadata.viewservices.rex.api.properties.RexRelationshipAndEntitiesDigest;
import org.odpi.openmetadata.viewservices.rex.api.properties.RexRelationshipDigest;
import org.odpi.openmetadata.viewservices.rex.api.properties.RexTraversal;
import org.odpi.openmetadata.viewservices.rex.api.properties.RexTypeStats;
import org.odpi.openmetadata.viewservices.rex.api.properties.TypeExplorer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The RexViewHandler is initialised with the server the call should be sent to.
 * The handler exposes methods for functionality for the repository explorer view
 */
public class RexViewHandler
{
    private static final Logger log = LoggerFactory.getLogger(RexViewHandler.class);

    /*
     * Specify a constant for the (max) length to which labels will be truncated.
     */
    private static final int TRUNCATED_STRING_LENGTH = 24;


    /*
     * viewServiceOptions should have been validated in the Admin layer.
     * The viewServiceOptions contains a list of resource endpoints that the
     * view service can connect to. It is formatted like this:
     *
     *  "resourceEndpoints" : [
     *            {
     *               "class"              : "ResourceEndpointConfig",
     *               "resourceCategory"   : "Platform",
     *               "platformName"       : "Platform1",
     *               "platformRootURL"    : "https://localhost:8082",
     *               "description"        : "Egeria deployment on local development server"
     *           },
     *           {
     *               "class"              : "ResourceEndpointConfig",
     *               "resourceCategory"   : "Platform",
     *               "platformName"       : "Platform2",
     *               "platformRootURL"    : "https://localhost:9443",
     *               "description"        : "Egeria deployment on local test server"
     *           },
     *           {
     *               "class"              : "ResourceEndpointConfig",
     *               "resourceCategory"   : "Server",
     *               "serverName"         : "Metadata_Server",
     *               "serverInstanceName" : "Metadata Server 1",
     *               "platformName"       : "Platform1",
     *               "description"        : "Metadata server for development testing"
     *           },
     *           {
     *               "class"              : "ResourceEndpointConfig",
     *               "resourceCategory"   : "Server",
     *               "serverName"         : "Metadata_Server2",
     *               "serverInstanceName" : "Metadata Server 2",
     *               "platformName"       : "Platform2",
     *               "description"        : "Metadata server used as home for test artefacts"
     *           }
     *       ]
     */

    private Map<String, ResourceEndpoint>  configuredPlatforms = null;          // map is keyed using platformRootURL
    private Map<String, ResourceEndpoint>  configuredServerInstances   = null;  // map is keyed using serverName+platformRootURL so each instance is unique




    /**
     * Default constructor for RexViewHandler
     */
    public RexViewHandler() {

    }


    /**
     * Constructor for RexViewHandler with configured resourceEndpoints
     * @param resourceEndpoints - list of resource endpoint configuration objects for this view service
     */
    public RexViewHandler(List<ResourceEndpointConfig>  resourceEndpoints) {

        /*
         * Populate map of resources with their endpoints....
         */

        // TODO - It would be desirable to add validation rules to ensure uniqueness etc.

        configuredPlatforms         = new HashMap<>();
        configuredServerInstances   = new HashMap<>();

        if (resourceEndpoints != null && !resourceEndpoints.isEmpty()) {

            resourceEndpoints.forEach(res -> {

                String resCategory   = res.getResourceCategory();
                ResourceEndpoint rep = new ResourceEndpoint(res);

                String resName = null;

                switch (resCategory) {
                    case "Platform":
                        resName = res.getPlatformName();
                        configuredPlatforms.put(resName, rep);
                        break;

                    case "Server":
                        resName = res.getServerInstanceName();
                        configuredServerInstances.put(resName, rep);
                        break;

                    default:
                        // Unsupported category is ignored
                        break;

                }
            });
        }

    }



    /**
     * getResourceEndpoints - returns a list of the configured resource endpoints. Does not include discovered resource endpoints.
     *
     * @param userId  userId under which the request is performed
     * @param methodName The name of the method being invoked
     * @return The resource endpoints that have been configured for the view service
     *
     */
    public Map<String, List<ResourceEndpoint>> getResourceEndpoints(String userId, String methodName)

    {
        Map<String, List<ResourceEndpoint>> returnMap = new HashMap<>();

        List<ResourceEndpoint> platformList = null;
        List<ResourceEndpoint> serverList   = null;

        if (!configuredPlatforms.isEmpty())
        {
            platformList = new ArrayList<>();
            platformList.addAll(configuredPlatforms.values());
        }

        if (!configuredServerInstances.isEmpty())
        {
            serverList = new ArrayList<>();
            serverList.addAll(configuredServerInstances.values());
        }

        returnMap.put("platformList",platformList);
        returnMap.put("serverList",serverList);

        return returnMap;
    }



    /**
     * resolvePlatformRootURL
     *
     * This method will look up the configured root URL for the named platform.
     *
     * @param platformName - the name if the platform to be resolved (to a URL)
     * @return resolved platform URL Root
     *
     * Exceptions
     * @throws RexViewServiceException  an error was detected and details are reported in the exception
     */
    private String resolvePlatformRootURL(String platformName, String methodName) throws RexViewServiceException

    {
        String platformRootURL = null;

        if (platformName != null) {
            ResourceEndpoint resource = configuredPlatforms.get(platformName);
            if (resource != null) {
                platformRootURL = resource.getResourceRootURL();
            }
        }
        if (platformName == null || platformRootURL == null) {
            throw new RexViewServiceException(RexViewErrorCode.VIEW_SERVICE_NULL_PLATFORM_NAME.getMessageDefinition(),
                                                this.getClass().getName(),
                                                methodName);
        }

        return platformRootURL;
    }
    

    /**
     * Retrieve type information from the repository server
     * @param userId  userId under which the request is performed
     * @param repositoryServerName The name of the repository server to interrogate
     * @param platformName The name of the platform running the repository server to interrogate
     * @param enterpriseOption Whether the query is at cohort level or server specific
     * @param methodName The name of the method being invoked
     * @return response containing the TypeExplorer object.
     *
     * Exceptions
     * @throws RexViewServiceException  an error was detected and details are reported in the exception
     */
    public TypeExplorer getTypeExplorer(String    userId,
                                        String    repositoryServerName,
                                        String    platformName,
                                        boolean   enterpriseOption,
                                        String    methodName)
    throws
        RexViewServiceException

    {


        try
        {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Switch between local and enterprise services clients depending
             *  on enterprise option...
             */
            MetadataCollectionServicesClient repositoryServicesClient;

            if (!enterpriseOption)
            {
                repositoryServicesClient = this.getLocalRepositoryServicesClient(repositoryServerName, platformRootURL);
            }
            else
            {
                repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(repositoryServerName, platformRootURL);
            }

            TypeExplorer tex = new TypeExplorer();

            TypeDefGallery typeDefGallery = repositoryServicesClient.getAllTypes(userId);

            List<TypeDef> typeDefs = typeDefGallery.getTypeDefs();
            for (TypeDef typeDef : typeDefs)
            {
                TypeDefCategory tdCat = typeDef.getCategory();
                switch (tdCat)
                {
                    case ENTITY_DEF:
                        EntityExplorer eex = new EntityExplorer((EntityDef) typeDef);
                        tex.addEntityExplorer(typeDef.getName(), eex);
                        break;
                    case RELATIONSHIP_DEF:
                        RelationshipExplorer rex = new RelationshipExplorer((RelationshipDef) typeDef);
                        tex.addRelationshipExplorer(typeDef.getName(), rex);
                        break;
                    case CLASSIFICATION_DEF:
                        ClassificationExplorer cex = new ClassificationExplorer((ClassificationDef) typeDef);
                        tex.addClassificationExplorer(typeDef.getName(), cex);
                        break;
                    default:
                        // Ignore this typeDef and continue with next
                        break;
                }
            }

            // Include EnumDefs in the TEX
            List<AttributeTypeDef> attributeTypeDefs = typeDefGallery.getAttributeTypeDefs();
            for (AttributeTypeDef attributeTypeDef : attributeTypeDefs)
            {
                AttributeTypeDefCategory tdCat = attributeTypeDef.getCategory();
                switch (tdCat)
                {
                    case ENUM_DEF:
                        tex.addEnumExplorer(attributeTypeDef.getName(), (EnumDef) attributeTypeDef);
                        break;
                    default:
                        // Ignore this AttributeTypeDef and continue with next
                        break;
                }
            }

            // All typeDefs processed, resolve linkages and return the TEX object
            // The platformRootURL and repositoryName are passed in only for error logging
            tex.resolve(platformRootURL, repositoryServerName);
            return tex;

        }


        catch (UserNotAuthorizedException e)
        {
            throw RexExceptionHandler.mapOMRSUserNotAuthorizedException(this.getClass().getName(), methodName, e);
        }
        catch (RepositoryErrorException e)
        {
            throw RexExceptionHandler.mapOMRSRepositoryErrorException(this.getClass().getName(), methodName, e);
        }
        catch (InvalidParameterException e)
        {
            throw RexExceptionHandler.mapOMRSInvalidParameterException(this.getClass().getName(), methodName, e);
        }


    }

    /**
     * Retrieve entity (by GUID) from the repository server
     * @param userId  userId under which the request is performed
     * @param repositoryServerName The name of the repository server to interrogate
     * @param platformName The name of the platform running the repository server to interrogate
     * @param enterpriseOption Whether the query is at cohort level or server specific
     * @param entityGUID the GUID of the entity to retrieve
     * @param methodName The name of the method being invoked
     * @return response containing the RexExpandedEntityDetail object.
     *
     * Exceptions
     * @throws RexViewServiceException  an error was detected and details are reported in the exception
     */
    public RexExpandedEntityDetail getEntity(String    userId,
                                             String    repositoryServerName,
                                             String    platformName,
                                             boolean   enterpriseOption,
                                             String    entityGUID,
                                             String    methodName)
    throws
    RexViewServiceException

    {


        try
        {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Switch between local and enterprise services clients depending
             *  on enterprise option...
             */
            MetadataCollectionServicesClient repositoryServicesClient;

            if (!enterpriseOption)
            {
                repositoryServicesClient = this.getLocalRepositoryServicesClient(repositoryServerName, platformRootURL);
            }
            else
            {
                repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(repositoryServerName, platformRootURL);
            }

            /*
             * Find the metadataCollectionId of the repository - this is used later to determine whether
             * each returned entity is homed in the metadataCollection owned by this repository, or not.
             */
            String metadataCollectionId = repositoryServicesClient.getMetadataCollectionId(userId);


            EntityDetail entityDetail = repositoryServicesClient.getEntityDetail(userId, entityGUID);

            TypeExplorer typeExplorer = getTypeExplorer(userId,
                                                        repositoryServerName,
                                                        platformName,
                                                        enterpriseOption,
                                                        methodName);

            String label = this.chooseLabelForEntity(entityDetail, typeExplorer);

            String provenance = determineInstanceProvenance(entityDetail, metadataCollectionId, enterpriseOption);

            RexExpandedEntityDetail rexExpEntityDetail = new RexExpandedEntityDetail(entityDetail,
                                                                                     label,
                                                                                     repositoryServerName,
                                                                                     platformName,
                                                                                     provenance);

            return rexExpEntityDetail;

        }
        catch (UserNotAuthorizedException e)
        {
            throw RexExceptionHandler.mapOMRSUserNotAuthorizedException(this.getClass().getName(),
                                                                        methodName,
                                                                        e);
        }
        catch (RepositoryErrorException e)
        {
            throw RexExceptionHandler.mapOMRSRepositoryErrorException(this.getClass().getName(),
                                                                      methodName,
                                                                      e);
        }
        catch (InvalidParameterException e)
        {
            throw RexExceptionHandler.mapOMRSInvalidParameterException(this.getClass().getName(),
                                                                       methodName,
                                                                       e);
        }
        catch (EntityNotKnownException e)
        {
            throw RexExceptionHandler.mapOMRSEntityNotKnownException(this.getClass().getName(),
                                                                     methodName,
                                                                     repositoryServerName,
                                                                     enterpriseOption,
                                                                     e);
        }
        catch (EntityProxyOnlyException e)
        {
            throw RexExceptionHandler.mapOMRSEntityProxyOnlyException(this.getClass().getName(), methodName, e);
        }

    }


    /**
     * Retrieve relationship (by GUID) from the repository server
     * @param userId  userId under which the request is performed
     * @param repositoryServerName The name of the repository server to interrogate
     * @param platformName The name of the platform running the repository server to interrogate
     * @param enterpriseOption Whether the query is at cohort level or server specific
     * @param relationshipGUID the GUID of the relationship to retrieve
     * @param methodName The name of the method being invoked
     * @return response containing the RexExpandedEntityDetail object.
     *
     * Exceptions
     * @throws RexViewServiceException  an error was detected and details are reported in the exception
     */
    public RexExpandedRelationship getRelationship(String    userId,
                                                   String    repositoryServerName,
                                                   String    platformName,
                                                   boolean   enterpriseOption,
                                                   String    relationshipGUID,
                                                   String    methodName)
    throws
    RexViewServiceException

    {


        try
        {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);


            /*
             *  Switch between local and enterprise services clients depending
             *  on enterprise option...
             */
            MetadataCollectionServicesClient repositoryServicesClient;

            if (!enterpriseOption)
            {
                repositoryServicesClient = this.getLocalRepositoryServicesClient(repositoryServerName, platformRootURL);
            }
            else
            {
                repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(repositoryServerName, platformRootURL);
            }

            /*
             * Find the metadataCollectionId of the repository - this is used later to determine whether
             * each returned entity is homed in the metadataCollection owned by this repository, or not.
             */
            String metadataCollectionId = repositoryServicesClient.getMetadataCollectionId(userId);


            Relationship relationship = repositoryServicesClient.getRelationship(userId, relationshipGUID);

            // Create digests for both ends

            TypeExplorer typeExplorer = getTypeExplorer(userId,
                                                        repositoryServerName,
                                                        platformName,
                                                        enterpriseOption,
                                                        methodName);

            EntityProxy entity1 = relationship.getEntityOneProxy();
            EntityProxy entity2 = relationship.getEntityTwoProxy();
            String label1 = this.chooseLabelForEntityProxy(entity1, typeExplorer);
            String label2 = this.chooseLabelForEntityProxy(entity2, typeExplorer);

            RexEntityDigest digest1 = new RexEntityDigest(entity1.getGUID(),
                                                          label1,
                                                          0,
                                                          entity1.getMetadataCollectionName(),
                                                          entity1.getMetadataCollectionId(),
                                                          "proxy");
            RexEntityDigest digest2 = new RexEntityDigest(entity2.getGUID(),
                                                          label2,
                                                          0,
                                                          entity2.getMetadataCollectionName(),
                                                          entity2.getMetadataCollectionId(),
                                                          "proxy");

            String label = this.chooseLabelForRelationship(relationship);

            String provenance = determineInstanceProvenance(relationship, metadataCollectionId, enterpriseOption);


            RexExpandedRelationship rexExpRelationship = new RexExpandedRelationship(relationship,
                                                                                     label,
                                                                                     digest1,
                                                                                     digest2,
                                                                                     repositoryServerName,
                                                                                     platformName,
                                                                                     provenance);

            return rexExpRelationship;

        }
        catch (UserNotAuthorizedException e)
        {
            throw RexExceptionHandler.mapOMRSUserNotAuthorizedException(this.getClass().getName(),
                                                                        methodName,
                                                                        e);
        }
        catch (RepositoryErrorException e)
        {
            throw RexExceptionHandler.mapOMRSRepositoryErrorException(this.getClass().getName(),
                                                                      methodName,
                                                                      e);
        }
        catch (InvalidParameterException e)
        {
            throw RexExceptionHandler.mapOMRSInvalidParameterException(this.getClass().getName(),
                                                                       methodName,
                                                                       e);
        }
        catch (RelationshipNotKnownException e)
        {
            throw RexExceptionHandler.mapOMRSRelationshipNotKnownException(this.getClass().getName(),
                                                                           methodName,
                                                                           repositoryServerName,
                                                                           enterpriseOption,
                                                                           e);
        }


    }

    /**
     * Retrieve entities (by text search) from the repository server
     * @param userId  userId under which the request is performed
     * @param repositoryServerName The name of the repository server to interrogate
     * @param platformName The name of the platform running the repository server to interrogate
     * @param enterpriseOption Whether the query is at cohort level or server specific
     * @param searchText the search expression that entities must match
     * @param entityTypeName the name of a type used to filter the entity search
     * @param classificationNames classification names to limit find by
     * @param methodName The name of the method being invoked
     * @return a map of entity digests for the entities that matched the search
     *
     * Exceptions
     * @throws RexViewServiceException  an error was detected and details are reported in the exception
     */
    public Map<String, RexEntityDigest> findEntities(String       userId,
                                                     String       repositoryServerName,
                                                     String       platformName,
                                                     boolean      enterpriseOption,
                                                     String       searchText,
                                                     String       entityTypeName,
                                                     List<String> classificationNames,
                                                     String       methodName)
    throws
    RexViewServiceException

    {


        try
        {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);


            /*
             *  Switch between local and enterprise services clients depending
             *  on enterprise option...
             */
            MetadataCollectionServicesClient repositoryServicesClient;

            if (!enterpriseOption)
            {
                repositoryServicesClient = this.getLocalRepositoryServicesClient(repositoryServerName, platformRootURL);
            }
            else
            {
                repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(repositoryServerName, platformRootURL);
            }

            /*
             * Find the metadataCollectionId of the repository - this is used later to determine whether
             * each returned entity is homed in the metadataCollection owned by this repository, or not.
             */
            String metadataCollectionId = repositoryServicesClient.getMetadataCollectionId(userId);


            TypeExplorer typeExplorer = getTypeExplorer(userId,
                                                        repositoryServerName,
                                                        platformName,
                                                        enterpriseOption,
                                                        methodName);


            String entityTypeGUID = typeExplorer.getEntityTypeGUID(entityTypeName);

            List<EntityDetail> entities = repositoryServicesClient.findEntitiesByPropertyValue(
                    userId,
                    entityTypeGUID,
                    searchText,
                    0,
                    null,
                    classificationNames,
                    null,
                    null,
                    null,
                    0);


            if (entities != null)
            {

                // Process the list of EntityDetail objects and produce a map of EntityDigest objects

                Map<String, RexEntityDigest> digestMap = new HashMap<>();

                for (int e = 0; e < entities.size(); e++)
                {
                    EntityDetail entityDetail = entities.get(e);
                    String label = this.chooseLabelForEntity(entityDetail, typeExplorer);

                    String provenance = determineInstanceProvenance(entityDetail, metadataCollectionId, enterpriseOption);

                    RexEntityDigest entityDigest = new RexEntityDigest(entityDetail.getGUID(),
                                                                       label,
                                                                       0,
                                                                       entityDetail.getMetadataCollectionName(),
                                                                       entityDetail.getMetadataCollectionId(),
                                                                       provenance);

                    digestMap.put(entityDetail.getGUID(), entityDigest);

                }

                return digestMap;
            }
            else
            {
                return null;
            }

        }

        catch (UserNotAuthorizedException e)
        {
            throw RexExceptionHandler.mapOMRSUserNotAuthorizedException(this.getClass().getName(),
                                                                        methodName,
                                                                        e);
        }
        catch (RepositoryErrorException e)
        {
            throw RexExceptionHandler.mapOMRSRepositoryErrorException(this.getClass().getName(),
                                                                      methodName,
                                                                      e);
        }
        catch (InvalidParameterException e)
        {
            throw RexExceptionHandler.mapOMRSInvalidParameterException(this.getClass().getName(),
                                                                       methodName,
                                                                       e);
        }
        catch (TypeErrorException e)
        {
            throw RexExceptionHandler.mapOMRSTypeErrorException(this.getClass().getName(),
                                                                methodName,
                                                                e);
        }
        catch (PropertyErrorException e)
        {
            throw RexExceptionHandler.mapOMRSPropertyErrorException(this.getClass().getName(),
                                                                    methodName,
                                                                    e);
        }
        catch (PagingErrorException e)
        {
            throw RexExceptionHandler.mapOMRSPagingErrorException(this.getClass().getName(),
                                                                  methodName,
                                                                  e);
        }
        catch (FunctionNotSupportedException e)
        {
            throw RexExceptionHandler.mapOMRSFunctionNotSupportedException(this.getClass().getName(),
                                                                           methodName,
                                                                           e);
        }

    }


    /**
     * Retrieve relationships (by text search) from the repository server
     * @param userId  userId under which the request is performed
     * @param repositoryServerName The name of the repository server to interrogate
     * @param platformName The name of the platform running the repository server to interrogate
     * @param enterpriseOption Whether the query is at cohort level or server specific
     * @param searchText the search expression that relationships must match
     * @param relationshipTypeName the name of a type used to filter the relationship search
     * @param methodName The name of the method being invoked
     * @return a map of relationship digests for the relationships that matched the search
     *
     * Exceptions
     * @throws RexViewServiceException  an error was detected and details are reported in the exception
     */
    public Map<String, RexRelationshipAndEntitiesDigest> findRelationships(String    userId,
                                                                           String    repositoryServerName,
                                                                           String    platformName,
                                                                           boolean   enterpriseOption,
                                                                           String    searchText,
                                                                           String    relationshipTypeName,
                                                                           String    methodName)
    throws
    RexViewServiceException

    {


        try
        {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Switch between local and enterprise services clients depending
             *  on enterprise option...
             */
            MetadataCollectionServicesClient repositoryServicesClient;

            if (!enterpriseOption)
            {
                repositoryServicesClient = this.getLocalRepositoryServicesClient(repositoryServerName, platformRootURL);
            }
            else
            {
                repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(repositoryServerName, platformRootURL);
            }

            /*
             * Find the metadataCollectionId of the repository - this is used later to determine whether
             * each returned entity is homed in the metadataCollection owned by this repository, or not.
             */
            String metadataCollectionId = repositoryServicesClient.getMetadataCollectionId(userId);


            TypeExplorer typeExplorer = getTypeExplorer(userId,
                                                        repositoryServerName,
                                                        platformName,
                                                        enterpriseOption,
                                                        methodName);


            String relationshipTypeGUID = typeExplorer.getRelationshipTypeGUID(relationshipTypeName);

            List<Relationship> relationships = repositoryServicesClient.findRelationshipsByPropertyValue(
                    userId,
                    relationshipTypeGUID,
                    searchText,
                    0,
                    null,
                    null,
                    null,
                    null,
                    0);


            if (relationships != null)
            {

                // Process the list of Relationship objects and produce a list of super digests...

                Map<String, RexRelationshipAndEntitiesDigest> superDigests = new HashMap<>();


                for (int r = 0; r < relationships.size(); r++)
                {

                    Relationship relationship = relationships.get(r);
                    String label = this.chooseLabelForRelationship(relationship);

                    String provenance = determineInstanceProvenance(relationship, metadataCollectionId, enterpriseOption);

                    // Create the super digest

                    RexRelationshipAndEntitiesDigest superDigest = new RexRelationshipAndEntitiesDigest();

                    // Create the relationship digest

                    RexRelationshipDigest relationshipDigest = new RexRelationshipDigest(relationship.getGUID(),
                                                                                         label,
                                                                                         relationship.getEntityOneProxy().getGUID(),
                                                                                         relationship.getEntityTwoProxy().getGUID(),
                                                                                         0,
                                                                                         0,
                                                                                         relationship.getMetadataCollectionName(),
                                                                                         relationship.getMetadataCollectionId(),
                                                                                         provenance);


                    superDigest.setRelationshipDigest(relationshipDigest);

                    // Create digests for both ends

                    EntityProxy entity1 = relationship.getEntityOneProxy();
                    EntityProxy entity2 = relationship.getEntityTwoProxy();
                    String label1 = this.chooseLabelForEntityProxy(entity1, typeExplorer);
                    String label2 = this.chooseLabelForEntityProxy(entity2, typeExplorer);

                    RexEntityDigest digest1 = new RexEntityDigest(entity1.getGUID(),
                                                                  label1,
                                                                  0,
                                                                  entity1.getMetadataCollectionName(),
                                                                  entity1.getMetadataCollectionId(),
                                                                  "proxy");
                    RexEntityDigest digest2 = new RexEntityDigest(entity2.getGUID(),
                                                                  label2,
                                                                  0,
                                                                  entity2.getMetadataCollectionName(),
                                                                  entity2.getMetadataCollectionId(),
                                                                  "proxy");


                    superDigest.setEnd1Digest(digest1);
                    superDigest.setEnd2Digest(digest2);

                    superDigests.put(relationship.getGUID(), superDigest);


                }

                return superDigests;
            }
            else
            {
                return null;
            }

        }
        catch (UserNotAuthorizedException e)
        {
            throw RexExceptionHandler.mapOMRSUserNotAuthorizedException(this.getClass().getName(),
                                                                        methodName,
                                                                        e);
        }
        catch (RepositoryErrorException e)
        {
            throw RexExceptionHandler.mapOMRSRepositoryErrorException(this.getClass().getName(),
                                                                      methodName,
                                                                      e);
        }
        catch (InvalidParameterException e)
        {
            throw RexExceptionHandler.mapOMRSInvalidParameterException(this.getClass().getName(),
                                                                       methodName,
                                                                       e);
        }
        catch (TypeErrorException e)
        {
            throw RexExceptionHandler.mapOMRSTypeErrorException(this.getClass().getName(),
                                                                methodName,
                                                                e);
        }
        catch (PropertyErrorException e)
        {
            throw RexExceptionHandler.mapOMRSPropertyErrorException(this.getClass().getName(),
                                                                    methodName,
                                                                    e);
        }
        catch (PagingErrorException e)
        {
            throw RexExceptionHandler.mapOMRSPagingErrorException(this.getClass().getName(),
                                                                  methodName,
                                                                  e);
        }
        catch (FunctionNotSupportedException e)
        {
            throw RexExceptionHandler.mapOMRSFunctionNotSupportedException(this.getClass().getName(),
                                                                           methodName,
                                                                           e);
        }

    }




    /**
     * Retrieve the neighborhood surrounding an entity for pre-traversal.
     * @param userId  userId under which the request is performed
     * @param repositoryServerName The name of the repository server to interrogate
     * @param platformName The name of the platform running the repository server to interrogate
     * @param enterpriseOption Whether the query is at cohort level or server specific
     * @param entityGUID the identity of the entity from which to traverse
     * @param depth the depth to which the method should traverse
     * @param methodName The name of the method being invoked
     * @return a RexTraversal object containing the neighborhood information
     *
     * Exceptions
     * @throws RexViewServiceException  an error was detected and details are reported in the exception
     */
    public RexPreTraversal preTraversal(String          userId,
                                        String          repositoryServerName,
                                        String          platformName,
                                        boolean         enterpriseOption,
                                        String          entityGUID,
                                        int             depth,
                                        String          methodName)
    throws
    RexViewServiceException

    {


        try
        {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Switch between local and enterprise services clients depending
             *  on enterprise option...
             */
            MetadataCollectionServicesClient repositoryServicesClient;

            if (!enterpriseOption)
            {
                repositoryServicesClient = this.getLocalRepositoryServicesClient(repositoryServerName, platformRootURL);
            }
            else
            {
                repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(repositoryServerName, platformRootURL);
            }

            /*
             * Because we will want to extract labels based on type we'll need to know the types supported by the repository...
             */

            TypeExplorer typeExplorer = getTypeExplorer(userId,
                                                        repositoryServerName,
                                                        platformName,
                                                        enterpriseOption,
                                                        methodName);

            InstanceGraph instGraph = null;

            if (depth > 0)
            {

                instGraph = repositoryServicesClient.getEntityNeighborhood(userId,
                                                                           entityGUID,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           depth);
            }


            else
            {

                /*
                 * Since depth is 0 - use getEntityDetail instead of neighborhood
                 */

                EntityDetail entityDetail = repositoryServicesClient.getEntityDetail(
                        userId,
                        entityGUID);

                // Construct an InstanceGraph containing just the entityDetail
                instGraph = new InstanceGraph();

                List<EntityDetail> entityDetailList = new ArrayList<>();
                entityDetailList.add(entityDetail);
                instGraph.setEntities(entityDetailList);

            }


            /*
             * Should have an InstanceGraph with one or more entities and maybe relationships
             * Parse the RexTraversal into a RexPreTraversal for the PreTraversalResponse
             */

            RexPreTraversal rexPreTraversal = new RexPreTraversal();


            rexPreTraversal.setEntityGUID(entityGUID);
            rexPreTraversal.setDepth(depth);

            // Process entities
            List<EntityDetail> entities = instGraph.getEntities();
            Map<String, RexTypeStats> entityCountsByType = new HashMap<>();
            Map<String, RexTypeStats> classificationCountsByType = new HashMap<>();
            if (entities != null)
            {
                for (EntityDetail ent : entities)
                {
                    /*
                     * Process entity type information
                     *
                     * Skip the entity that the traversal started from.
                     * Counting the starting entity will distort the counts
                     */
                    if (!ent.getGUID().equals(entityGUID))
                    {

                        InstanceType instanceType = ent.getType();
                        String typeGUID = instanceType.getTypeDefGUID();
                        String typeName = instanceType.getTypeDefName();
                        if (entityCountsByType.get(typeName) == null)
                        {
                            // First sight of an instance of this type
                            RexTypeStats stats = new RexTypeStats(typeGUID, 1);
                            entityCountsByType.put(typeName, stats);
                        }
                        else
                        {
                            // Add to the count of instances of this type
                            Integer existingCount = entityCountsByType.get(typeName).getCount();
                            entityCountsByType.get(typeName).setCount(existingCount + 1);
                        }
                        // Process entity classification information
                        List<Classification> classifications = ent.getClassifications();
                        if (classifications != null)
                        {
                            for (Classification classification : classifications)
                            {
                                String classificationName = classification.getName();
                                if (classificationCountsByType.get(classificationName) == null)
                                {
                                    // First sight of an instance of this type
                                    RexTypeStats stats = new RexTypeStats(null, 1);
                                    classificationCountsByType.put(classificationName, stats);
                                }
                                else
                                {
                                    // Add to the count of instances of this type
                                    Integer existingCount = classificationCountsByType.get(classificationName).getCount();
                                    classificationCountsByType.get(classificationName).setCount(existingCount + 1);
                                }
                            }
                        }
                    }
                }
            }
            // Process relationships
            List<Relationship> relationships = instGraph.getRelationships();
            Map<String, RexTypeStats> relationshipCountsByType = new HashMap<>();
            if (relationships != null)
            {
                for (Relationship rel : relationships)
                {
                    InstanceType instanceType = rel.getType();
                    String typeGUID = instanceType.getTypeDefGUID();
                    String typeName = instanceType.getTypeDefName();
                    if (relationshipCountsByType.get(typeName) == null)
                    {
                        // First sight of an instance of this type
                        RexTypeStats stats = new RexTypeStats(typeGUID, 1);
                        relationshipCountsByType.put(typeName, stats);
                    }
                    else
                    {
                        // Add to the count of instances of this type
                        Integer existingCount = relationshipCountsByType.get(typeName).getCount();
                        relationshipCountsByType.get(typeName).setCount(existingCount + 1);
                    }
                }
            }
            // Update the rexPreTraversal
            rexPreTraversal.setEntityInstanceCounts(entityCountsByType);
            rexPreTraversal.setRelationshipInstanceCounts(relationshipCountsByType);
            rexPreTraversal.setClassificationInstanceCounts(classificationCountsByType);

            return rexPreTraversal;

        }
        catch (UserNotAuthorizedException e)
        {
            throw RexExceptionHandler.mapOMRSUserNotAuthorizedException(this.getClass().getName(),
                                                                        methodName,
                                                                        e);
        }
        catch (RepositoryErrorException e)
        {
            throw RexExceptionHandler.mapOMRSRepositoryErrorException(this.getClass().getName(),
                                                                      methodName,
                                                                      e);
        }
        catch (InvalidParameterException e)
        {
            throw RexExceptionHandler.mapOMRSInvalidParameterException(this.getClass().getName(),
                                                                       methodName,
                                                                       e);
        }
        catch (TypeErrorException e)
        {
            throw RexExceptionHandler.mapOMRSTypeErrorException(this.getClass().getName(),
                                                                methodName,
                                                                e);
        }
        catch (PropertyErrorException e)
        {
            throw RexExceptionHandler.mapOMRSPropertyErrorException(this.getClass().getName(),
                                                                    methodName,
                                                                    e);
        }
        catch (FunctionNotSupportedException e)
        {
            throw RexExceptionHandler.mapOMRSFunctionNotSupportedException(this.getClass().getName(),
                                                                           methodName,
                                                                           e);
        }
        catch (EntityNotKnownException e)
        {
            throw RexExceptionHandler.mapOMRSEntityNotKnownException(this.getClass().getName(),
                                                                     methodName,
                                                                     repositoryServerName,
                                                                     enterpriseOption,
                                                                     e);
        }
        catch (EntityProxyOnlyException e)
        {
            throw RexExceptionHandler.mapOMRSEntityProxyOnlyException(this.getClass().getName(), methodName, e);
        }




    }



    /**
     * Retrieve the neighborhood surrounding an entity.
     * @param userId  userId under which the request is performed
     * @param repositoryServerName The name of the repository server to interrogate
     * @param platformName The name of the platform running the repository server to interrogate
     * @param enterpriseOption Whether the query is at cohort level or server specific
     * @param entityGUID the identity of the entity from which to traverse
     * @param depth the depth to which the method should traverse
     * @param entityTypeGUIDs the GUIDs of entity types to filter the neighborhood
     * @param relationshipTypeGUIDs the GUIDs of relationship types to filter the neighborhood
     * @param classificationNames the names of classification types to filter the neighborhood
     * @param methodName The name of the method being invoked
     * @return a RexTraversal object containing the neighborhood information
     *
     * Exceptions
     * @throws RexViewServiceException  an error was detected and details are reported in the exception
     */
    public RexTraversal traversal(String          userId,
                                  String          repositoryServerName,
                                  String          platformName,
                                  boolean         enterpriseOption,
                                  String          entityGUID,
                                  int             depth,
                                  List<String>    entityTypeGUIDs,
                                  List<String>    relationshipTypeGUIDs,
                                  List<String>    classificationNames,
                                  String          methodName)
    throws
    RexViewServiceException

    {


        try
        {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Switch between local and enterprise services clients depending
             *  on enterprise option...
             */
            MetadataCollectionServicesClient repositoryServicesClient;

            if (!enterpriseOption)
            {
                repositoryServicesClient = this.getLocalRepositoryServicesClient(repositoryServerName, platformRootURL);
            }
            else
            {
                repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(repositoryServerName, platformRootURL);
            }


            /*
             * Find the metadataCollectionId of the repository - this is used later to determine whether
             * each returned entity is homed in the metadataCollection owned by this repository, or not.
             */
            String metadataCollectionId = repositoryServicesClient.getMetadataCollectionId(userId);



            /*
             * Because we will want to extract labels based on type we'll need to know the types supported by the repository...
             */

            TypeExplorer typeExplorer = getTypeExplorer(userId,
                                                        repositoryServerName,
                                                        platformName,
                                                        enterpriseOption,
                                                        methodName);

            InstanceGraph instGraph = null;

            if (depth > 0)
            {

                instGraph = repositoryServicesClient.getEntityNeighborhood(userId,
                                                                           entityGUID,
                                                                           entityTypeGUIDs,
                                                                           relationshipTypeGUIDs,
                                                                           null,
                                                                           classificationNames,
                                                                           null,
                                                                           depth);
            }


            else
            {

                /*
                 * Since depth is 0 - use getEntityDetail instead of neighborhood
                 */

                EntityDetail entityDetail = repositoryServicesClient.getEntityDetail(
                        userId,
                        entityGUID);

                // Construct an InstanceGraph containing just the entityDetail
                instGraph = new InstanceGraph();

                List<EntityDetail> entityDetailList = new ArrayList<>();
                entityDetailList.add(entityDetail);
                instGraph.setEntities(entityDetailList);

            }


            // Should have an InstanceGraph with one or more entities and maybe relationships

            if (instGraph != null)
            {

                RexTraversal rt = new RexTraversal();

                /* Format the results
                 *
                 * The format of the digests in the RexTraversal is as follows:
                 *   a map of entityGUID       --> { entityGUID, label, gen }
                 *   a map of relationshipGUID --> { relationshipGUID, end1GUID, end2GUID, idx, label, gen }
                 */

                /*
                 * An InstanceGraph contains relationships and entities that are homed by the repository that
                 * created the InstanceGraph. The relationships may refer to entities that are not homed by that
                 * repository, so those entities will not be included in the 'entities' portion of the InstanceGraph.
                 * Since our RexTraversal needs to be 'complete' - i.e. we have an EntityDigest for each end of
                 * every relationship, we must generate digests not just from the entities lit, but also spot any
                 * relationship ends that are NOT in the list and generate a digest for each of them as well.
                 * Start by processing the homed entities.
                 * Then process the relationships and check fr each end of each relationship whether we need to
                 * augment the RexTraversal entityDigestMap.
                 */
                List<EntityDetail> entities = instGraph.getEntities();
                Map<String, RexEntityDigest> entityDigestMap = null;
                if (entities != null && !entities.isEmpty())
                {
                    entityDigestMap = new HashMap<>();
                    for (EntityDetail entityDetail : entities)
                    {
                        /*
                         * We need entityGUID, label (computed) and if !preTraversal also include gen
                         */
                        String entGUID = entityDetail.getGUID();

                        // Pass the typeExplorer to the labeller so that it can traverse...
                        String entLabel = this.chooseLabelForEntity(entityDetail, typeExplorer);

                        String provenance = determineInstanceProvenance(entityDetail, metadataCollectionId, enterpriseOption);

                        RexEntityDigest red = new RexEntityDigest(entGUID,
                                                                  entLabel,
                                                                  0,
                                                                  entityDetail.getMetadataCollectionName(),
                                                                  entityDetail.getMetadataCollectionId(),
                                                                  provenance);
                        entityDigestMap.put(entGUID, red);
                    }

                }

                List<Relationship> relationships = instGraph.getRelationships();
                Map<String, RexRelationshipDigest> relationshipDigestMap = null;
                if (relationships != null && !relationships.isEmpty())
                {
                    relationshipDigestMap = new HashMap<>();
                    for (Relationship relationship : relationships)
                    {
                        /*
                         * We need: entityGUID, label (computed) and if !preTraversal also include gen
                         *   relationshipGUID, label (computed), end1GUID, end2GUID, idx (computed), gen
                         */
                        String relGUID = relationship.getGUID();
                        String relLabel = this.chooseLabelForRelationship(relationship);
                        String end1GUID = relationship.getEntityOneProxy().getGUID();
                        String end2GUID = relationship.getEntityTwoProxy().getGUID();

                        /* check for proxies... */
                        if (entityDigestMap.get(end1GUID) == null)
                        {
                            /* add a digest for this proxy... */
                            EntityProxy end1Proxy = relationship.getEntityOneProxy();
                            String end1Label = this.chooseLabelForEntityProxy(end1Proxy, typeExplorer);
                            RexEntityDigest red = new RexEntityDigest(end1GUID,
                                                                      end1Label,
                                                                      0,
                                                                      end1Proxy.getMetadataCollectionName(),
                                                                      end1Proxy.getMetadataCollectionId(),
                                                                      "proxy"
                            );
                            entityDigestMap.put(end1GUID, red);
                        }
                        if (entityDigestMap.get(end2GUID) == null)
                        {
                            /* add a digest for this proxy... */
                            EntityProxy end2Proxy = relationship.getEntityTwoProxy();
                            String end2Label = this.chooseLabelForEntityProxy(end2Proxy, typeExplorer);
                            RexEntityDigest red = new RexEntityDigest(end2GUID,
                                                                      end2Label,
                                                                      0,
                                                                      end2Proxy.getMetadataCollectionName(),
                                                                      end2Proxy.getMetadataCollectionId(),
                                                                      "proxy");
                            entityDigestMap.put(end2GUID, red);
                        }


                        int idx = 0;

                        String provenance = determineInstanceProvenance(relationship, metadataCollectionId, enterpriseOption);

                        RexRelationshipDigest rrd = new RexRelationshipDigest(relGUID,
                                                                              relLabel,
                                                                              end1GUID,
                                                                              end2GUID,
                                                                              idx,
                                                                              0,
                                                                              relationship.getMetadataCollectionName(),
                                                                              relationship.getMetadataCollectionId(),
                                                                              provenance);
                        relationshipDigestMap.put(relGUID, rrd);
                    }
                }

                rt.setEntityGUID(entityGUID);
                rt.setDepth(depth);
                rt.setGen(0);
                // Instead of using type guids in the traversal (which is to be sent to the browser) use type names instead.
                List<String> entityTypeNames = new ArrayList<>();
                if (entityTypeGUIDs != null && !entityTypeGUIDs.isEmpty())
                    for (String entityTypeGUID : entityTypeGUIDs)
                    {
                        // Convert from typeGIUD to typeName
                        String entityTypeName = typeExplorer.getEntityTypeName(entityTypeGUID);
                        entityTypeNames.add(entityTypeName);
                    }
                rt.setEntityTypeNames(entityTypeNames);
                rt.setRelationshipTypeGUIDs(relationshipTypeGUIDs);
                rt.setClassificationNames(classificationNames);
                rt.setEntities(entityDigestMap);
                rt.setRelationships(relationshipDigestMap);
                rt.setServerName(repositoryServerName);
                rt.setPlatformName(platformName);

                return rt;
            }

            else
            {

                String excMsg = "Could not retrieve subgraph for entity with guid" + entityGUID;
                return null;
            }
        }
        catch (UserNotAuthorizedException e)
        {
            throw RexExceptionHandler.mapOMRSUserNotAuthorizedException(this.getClass().getName(),
                                                                        methodName,
                                                                        e);
        }
        catch (RepositoryErrorException e)
        {
            throw RexExceptionHandler.mapOMRSRepositoryErrorException(this.getClass().getName(),
                                                                      methodName,
                                                                      e);
        }
        catch (InvalidParameterException e)
        {
            throw RexExceptionHandler.mapOMRSInvalidParameterException(this.getClass().getName(),
                                                                       methodName,
                                                                       e);
        }
        catch (TypeErrorException e)
        {
            throw RexExceptionHandler.mapOMRSTypeErrorException(this.getClass().getName(),
                                                                methodName,
                                                                e);
        }
        catch (PropertyErrorException e)
        {
            throw RexExceptionHandler.mapOMRSPropertyErrorException(this.getClass().getName(),
                                                                    methodName,
                                                                    e);
        }
        catch (FunctionNotSupportedException e)
        {
            throw RexExceptionHandler.mapOMRSFunctionNotSupportedException(this.getClass().getName(),
                                                                           methodName,
                                                                           e);
        }
        catch (EntityNotKnownException e)
        {
            throw RexExceptionHandler.mapOMRSEntityNotKnownException(this.getClass().getName(),
                                                                     methodName,
                                                                     repositoryServerName,
                                                                     enterpriseOption,
                                                                     e);
        }
        catch (EntityProxyOnlyException e)
        {
            throw RexExceptionHandler.mapOMRSEntityProxyOnlyException(this.getClass().getName(), methodName, e);
        }

    }


    /**
     * getLocalRepositoryServicesClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * MetadataCollection interface. This client is used when the enterprise option is not set, and will
     * connect to the local repository.
     *
     * @param serverName - name of the server to connect to
     * @param serverRootURL - the root URL to connect to the server
     * @throws InvalidParameterException - an invalid parameter was detected and reported
     */
    private LocalRepositoryServicesClient getLocalRepositoryServicesClient(String serverName,
                                                                           String serverRootURL)
    throws
    InvalidParameterException

    {
        /*
         * The serverName is used as the repositoryName
         * The serverRootURL is used as part of the restRootURL, along with the serverName
         */

        /*
         * The only exception thrown by the CTOR is InvalidParameterException, and this is not caught
         * here because we want to surface it to the REST API that called this method so that the
         * exception can be wrapped and a suitable indication sent in the REST Response.
         */
        String restRootURL = serverRootURL + "/servers/" + serverName;
        LocalRepositoryServicesClient client = new LocalRepositoryServicesClient(serverName, restRootURL);

        return client;
    }

    /**
     * getEnterpriseRepositoryServicesClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * MetadataCollection interface. This client is used when the enterprise option is set, and will
     * perform federation.
     *
     * @param serverName - name of the server to connect to
     * @param serverRootURL - the root URL to connect to the server
     * @throws InvalidParameterException - an invalid parameter was detected and reported
     */
    private EnterpriseRepositoryServicesClient getEnterpriseRepositoryServicesClient(String serverName,
                                                                                     String serverRootURL)
    throws
    InvalidParameterException
    {

        /*
         * The serverName is used as the repositoryName
         * The serverRootURL is used as part of the restRootURL, along with the serverName
         */

        /*
         * The only exception thrown by the CTOR is InvalidParameterException, and this is not caught
         * here because we want to surface it to the REST API that called this method so that the
         * exception can be wrapped and a suitable indication sent in the REST Response.
         */
        String restRootURL = serverRootURL + "/servers/" + serverName;
        EnterpriseRepositoryServicesClient client = new EnterpriseRepositoryServicesClient(serverName, restRootURL);

        return client;
    }

    private String chooseLabelForEntity(EntityDetail entityDetail, TypeExplorer typeExplorer)
    {

        // By default, use the GUID of the instance. This is not a desirable
        // label but if there is really nothing else to use, then the GUID is
        // better than nothing at all. Maybe.
        String label = entityDetail.getGUID();


        // Find the effective typeName - this is the highest supertype of the instance type
        String instanceTypeName = null;

        InstanceType instanceType = entityDetail.getType();
        if (instanceType != null) {
            instanceTypeName = instanceType.getTypeDefName();
        }
        if (instanceTypeName == null || instanceTypeName.equals("")) {
            // Drop out - there is no proper type information for the instance - just adopt the default set above.
            return label;
        }

        // We know that instanceTypeName is set to something we can use...

        // Traverse the TypeExplorer looking for the highest supertype..
        Map<String, EntityExplorer> entityTypes = typeExplorer.getEntities();

        String effTypeName = "DEFAULT";
        if (entityTypes != null)
        {

            // Get the immediate entity instance type...
            EntityExplorer eex = entityTypes.get(instanceTypeName);
            TypeDefLink superType = eex.getEntityDef().getSuperType();
            while (superType != null)
            {
                String superTypeName = superType.getName();
                eex = entityTypes.get(superTypeName);
                superType = eex.getEntityDef().getSuperType();
            }
            // eex is now the effective type entry
            TypeDef effectiveTypeDef = eex.getEntityDef();

            effTypeName = effectiveTypeDef.getName();

        }

        switch (effTypeName) {

            case "InformalTag":
                if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get("tagName") != null &&
                        entityDetail.getProperties().getInstanceProperties().get("tagName").valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get("tagName").valueAsString();
                }
                break;

            case "Like":
            case "Rating":
                if (entityDetail.getCreatedBy() != null) {
                    label = entityDetail.getCreatedBy();
                }
                break;

            case "DataField":
                if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get("dataFieldName") != null &&
                        entityDetail.getProperties().getInstanceProperties().get("dataFieldName").valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get("dataFieldName").valueAsString();
                }
                break;

            case "Annotation":
            case "AnnotationReview":
                if (instanceTypeName != null) {
                    label = instanceTypeName;  // use the local type name for anything under these types
                }
                break;

            case "LastAttachment":
                if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get("attachmentType") != null &&
                        entityDetail.getProperties().getInstanceProperties().get("attachmentType").valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get("attachmentType").valueAsString();
                }
                break;

            default:
                // Anything that is left should be a Referenceable.
                // If it has a displayName use that.
                // Else if it has a name use that.
                // Otherwise if it has a qualifiedName use up to the last TRUNCATED_STRING_LENGTH chars of that.
                // If there is not qualifiedName drop through and use GUID (default)
                if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get("displayName") != null &&
                        entityDetail.getProperties().getInstanceProperties().get("displayName").valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get("displayName").valueAsString();
                }
                else if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get("name") != null &&
                        entityDetail.getProperties().getInstanceProperties().get("name").valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get("name").valueAsString();
                }
                else if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get("qualifiedName") != null) {

                    String fullQN = entityDetail.getProperties().getInstanceProperties().get("qualifiedName").valueAsString();

                    int lengthQN = fullQN.length();
                    if (lengthQN > TRUNCATED_STRING_LENGTH) {
                        String tailQN = "..." + fullQN.substring(lengthQN-TRUNCATED_STRING_LENGTH, lengthQN);
                        label = tailQN;
                    } else {
                        label = fullQN;
                    }
                }
        }

        return label;

    }



    private String chooseLabelForEntityProxy(EntityProxy entityProxy, TypeExplorer typeExplorer)
    {

        // Refer to the comment in chooseLabelForEntity for labelling strategy - similar applies
        // here but implementation caters for the entity only being a proxy.
        // By default, use the GUID of the instance. This is not a desirable
        // label but if there is really nothing else to use, then the GUID is
        // better than nothing at all. Maybe.
        String label = entityProxy.getGUID();


        // Find the effective typeName - this is the highest supertype of the instance type
        String instanceTypeName = null;

        InstanceType instanceType = entityProxy.getType();
        if (instanceType != null) {
            instanceTypeName = instanceType.getTypeDefName();
        }
        if (instanceTypeName == null || instanceTypeName.equals("")) {
            // Drop out - there is no proper type information for the instance - just adopt the default set above.
            return label;
        }

        // We know that instanceTypeName is set to something we can use...

        // Traverse the TypeExplorer looking for the highest supertype..
        Map<String, EntityExplorer> entityTypes = typeExplorer.getEntities();

        String effTypeName = "DEFAULT";
        if (entityTypes != null)
        {

            // Get the immediate entity instance type...
            EntityExplorer eex = entityTypes.get(instanceTypeName);
            TypeDefLink superType = eex.getEntityDef().getSuperType();
            while (superType != null)
            {
                String superTypeName = superType.getName();
                eex = entityTypes.get(superTypeName);
                superType = eex.getEntityDef().getSuperType();
            }
            // eex is now the effective type entry
            TypeDef effectiveTypeDef = eex.getEntityDef();

            effTypeName = effectiveTypeDef.getName();
        }

        switch (effTypeName) {

            case "InformalTag":
                if (entityProxy.getUniqueProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("tagName") != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("tagName").valueAsString() != null) {

                    label = entityProxy.getUniqueProperties().getInstanceProperties().get("tagName").valueAsString();

                }
                break;

            case "Like":
            case "Rating":
                if (entityProxy.getCreatedBy() != null) {
                    label = entityProxy.getCreatedBy();

                }
                break;

            case "DataField":
                if (entityProxy.getUniqueProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("dataFieldName") != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("dataFieldName").valueAsString() != null) {

                    label = entityProxy.getUniqueProperties().getInstanceProperties().get("dataFieldName").valueAsString();

                }
                break;

            case "Annotation":
            case "AnnotationReview":
                if (instanceTypeName != null) {
                    label = instanceTypeName;  // use the local type name for anything under these types

                }
                break;

            default:
                // Anything that is left should be a Referenceable.
                // If it has a displayName use that.
                // Otherwise if it has a qualifiedName use up to the last TRUNCATED_STRING_LENGTH chars of that.
                // If there is not qualifiedName drop through and use GUID (default)
                if (entityProxy.getUniqueProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("displayName") != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("displayName").valueAsString() != null) {

                    label = entityProxy.getUniqueProperties().getInstanceProperties().get("displayName").valueAsString();

                }
                else if (entityProxy.getUniqueProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("name") != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("name").valueAsString() != null) {

                    label = entityProxy.getUniqueProperties().getInstanceProperties().get("name").valueAsString();

                }
                else if (entityProxy.getUniqueProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("qualifiedName") != null) {

                    String fullQN = entityProxy.getUniqueProperties().getInstanceProperties().get("qualifiedName").valueAsString();

                    int lengthQN = fullQN.length();
                    if (lengthQN > TRUNCATED_STRING_LENGTH) {
                        String tailQN = "..." + fullQN.substring(lengthQN-TRUNCATED_STRING_LENGTH, lengthQN);
                        label = tailQN;
                    } else {
                        label = fullQN;
                    }
                }

        }

        return label;

    }



    private String chooseLabelForRelationship(Relationship relationship)
    {

        // By default, use the GUID of the instance. This is not a desirable
        // label but if there is really nothing else to use, then the GUID is
        // better than nothing at all. Maybe.
        String label = relationship.getGUID();

        String instanceTypeName = null;

        InstanceType instanceType = relationship.getType();
        if (instanceType != null) {
            instanceTypeName = instanceType.getTypeDefName();
        }
        if (instanceTypeName == null || instanceTypeName.equals("")) {
            // Drop out - there is no proper type information for the instance - just adopt the default set above.
            return label;
        }

        // We know that instanceTypeName is set to something we can use...

        // For now simply label relationships by type name.
        label = instanceTypeName;

        return label;

    }


    /*
     * Record the provenance of the instance, in so far as this is discernible.
     * Depending on whether enterprise option is enabled or not, retrieve and compare the
     * metadataCollectionId from the entity with the one retrieved by the caller for the
     * repository's metadataCollection.
     */
    private String determineInstanceProvenance(InstanceAuditHeader   instance,
                                               String                metadataCollectionId,
                                               boolean               enterpriseOption)
    {

        String provenance;
        if (enterpriseOption)
        {
            /*
             * If enterprise option is set there is no way to differentiate home and reference copies.
             */
            provenance = "ent";
        }
        else if (instance.getMetadataCollectionId().equals(metadataCollectionId))
        {
            /*
             * This entity looks very at home here...
             */
            provenance = "home";
        }
        else
        {
            /*
             * This instance looks like it is not at home...
             */
            provenance = "refCopy";
        }

        return provenance;

    }


}
