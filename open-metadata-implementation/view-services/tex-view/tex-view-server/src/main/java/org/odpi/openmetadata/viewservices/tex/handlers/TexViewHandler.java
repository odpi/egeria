/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.tex.handlers;


import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.odpi.openmetadata.commonservices.ffdc.OMAGCommonErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
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
import org.odpi.openmetadata.viewservices.tex.api.properties.ClassificationExplorer;
import org.odpi.openmetadata.viewservices.tex.api.properties.EntityExplorer;
import org.odpi.openmetadata.viewservices.tex.api.properties.RelationshipExplorer;
import org.odpi.openmetadata.viewservices.tex.api.properties.ResourceEndpoint;
import org.odpi.openmetadata.viewservices.tex.api.properties.TypeExplorer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The TexViewHandler is initialised with the server the call should be sent to.
 * The handler exposes methods for functionality for the type explorer view
 */
public class TexViewHandler
{
    private static final Logger log = LoggerFactory.getLogger(TexViewHandler.class);

    /*
     * Specify a constant for the (max) length to which labels will be truncated.
     */
    private static final int TRUNCATED_STRING_LENGTH = 24;


    /*
    * viewServiceOptions should have been validated in the Admin layer.
    * The viewServiceOptions contains a list of resource endpoints that the
    * view service can connect to. It is formatted like this:
    * "resourceEndpoints" : [
                {
                    resourceCategory   : "Platform",
                    resourceName       : "Platform2",
                    resourceRootURL    : "https://localhost:9443"
                },
                {
                    resourceCategory   : "Platform",
                    resourceName       : "Platform1",
                    resourceRootURL    : "https://localhost:8082"
                },
                {
                    resourceCategory   : "Server",
                    resourceName       : "Metadata_Server1",
                    resourceRootURL    : "https://localhost:8082"
                },
                {
                    resourceCategory   : "Server",
                    resourceName       : "Metadata_Server2",
                    resourceRootURL    : "https://localhost:9443"
                }
            ]
    */
    private Map<String, ResourceEndpoint>  configuredPlatforms = null;  // map is keyed using platformRootURL
    private Map<String, ResourceEndpoint>  configuredServerInstances   = null;  // map is keyed using serverName+platformRootURL so each instance is unique



    public TexViewHandler(List<ResourceEndpointConfig>  resourceEndpoints) {


        /*
         * Populate map of resources with their endpoints....
         */

        // TODO - It would be desirable to add validation rules to ensure uniqueness etc.

        if (resourceEndpoints != null && !resourceEndpoints.isEmpty()) {
            configuredPlatforms         = new HashMap<>();
            configuredServerInstances   = new HashMap<>();

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
     * This method will return the resource endpoints that have been configured for the view service
     *
     */
    public Map<String, List<ResourceEndpoint>> getResourceEndpoints(String userId, String methodName)

    {

        List<ResourceEndpoint> platformList = new ArrayList<>();
        List<ResourceEndpoint> serverList   = new ArrayList<>();

        platformList.addAll(configuredPlatforms.values());
        serverList.addAll(configuredServerInstances.values());

        Map<String, List<ResourceEndpoint>> returnMap = new HashMap<>();
        returnMap.put("platformList",platformList);
        returnMap.put("serverList",serverList);
        return returnMap;
    }



    /**
     * resolvePlatformRootURL
     *
     * This method will look up the configured root URL for the named platform.
     *
     * @param platformName
     * @return resolved platform URL Root
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException
     */
    private String resolvePlatformRootURL(String platformName, String methodName) throws InvalidParameterException

    {
        String platformRootURL = null;

        if (platformName != null) {
            ResourceEndpoint resource = configuredPlatforms.get(platformName);
            if (resource != null) {
                platformRootURL = resource.getResourceRootURL();
            }
        }
        if (platformName == null || platformRootURL == null) {
            throw new InvalidParameterException(OMAGCommonErrorCode.VIEW_SERVICE_NULL_PLATFORM_NAME.getMessageDefinition(),
                                                this.getClass().getName(),
                                                methodName,
                                                "platformName");
        }

        return platformRootURL;
    }





    /**
     * Constructor for the TexViewHandler
     */
    public TexViewHandler() {

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
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     *
     * Client library Exceptions
     * @throws RepositoryErrorException Repository could not satisfy the request
     */
    public TypeExplorer getTypeExplorer(String    userId,
                                        String    repositoryServerName,
                                        String    platformName,
                                        boolean   enterpriseOption,
                                        String    methodName)
    throws
        RepositoryErrorException,
        InvalidParameterException,
        UserNotAuthorizedException

    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            /*
             *  Switch between local and enterprise services clients depending
             *  on enterprise option...
             */
            MetadataCollectionServicesClient repositoryServicesClient;

            if (!enterpriseOption) {
                repositoryServicesClient = this.getLocalRepositoryServicesClient(repositoryServerName, platformRootURL);
            } else {
                repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(repositoryServerName, platformRootURL);
            }

            TypeExplorer tex = new TypeExplorer();

            TypeDefGallery typeDefGallery = repositoryServicesClient.getAllTypes(userId);

            List<TypeDef> typeDefs = typeDefGallery.getTypeDefs();
            for (TypeDef typeDef : typeDefs) {
                TypeDefCategory tdCat = typeDef.getCategory();
                switch (tdCat) {
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
            for (AttributeTypeDef attributeTypeDef : attributeTypeDefs) {
                AttributeTypeDefCategory tdCat = attributeTypeDef.getCategory();
                switch (tdCat) {
                    case ENUM_DEF:
                        tex.addEnumExplorer(attributeTypeDef.getName(), (EnumDef) attributeTypeDef);
                        break;
                    default:
                        // Ignore this AttributeTypeDef and continue with next
                        break;
                }
            }

            // All typeDefs processed, resolve linkages and return the TEX object
            tex.resolve();
            return tex;

        }
        catch (UserNotAuthorizedException |
                RepositoryErrorException |
                InvalidParameterException e) {
            throw e;
        }

    }




    /**
     * getLocalRepositoryServicesClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * MetadataCollection interface. This client is used when the enterprise option is not set, and will
     * connect to the local repository.
     *
     * @param serverName
     * @param serverRootURL
     * @throws InvalidParameterException
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
     * @param serverName
     * @param serverRootURL
     * @throws InvalidParameterException
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




}
