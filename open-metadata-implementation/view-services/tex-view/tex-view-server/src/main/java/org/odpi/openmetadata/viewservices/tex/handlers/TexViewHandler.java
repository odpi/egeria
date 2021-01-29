/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.tex.handlers;


import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
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
import org.odpi.openmetadata.viewservices.tex.api.ffdc.TexExceptionHandler;
import org.odpi.openmetadata.viewservices.tex.api.ffdc.TexViewErrorCode;
import org.odpi.openmetadata.viewservices.tex.api.ffdc.TexViewServiceException;
import org.odpi.openmetadata.viewservices.tex.api.properties.ClassificationExplorer;
import org.odpi.openmetadata.viewservices.tex.api.properties.EntityExplorer;
import org.odpi.openmetadata.viewservices.tex.api.properties.RelationshipExplorer;
import org.odpi.openmetadata.viewservices.tex.api.properties.ResourceEndpoint;
import org.odpi.openmetadata.viewservices.tex.api.properties.TypeExplorer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
    private Map<String, ResourceEndpoint>  configuredPlatforms = null;          // map is keyed using platformRootURL
    private Map<String, ResourceEndpoint>  configuredServerInstances   = null;  // map is keyed using serverName+platformRootURL so each instance is unique



    /**
     * Default constructor for TexViewHandler
     */
    public TexViewHandler() {

    }


    /**
     * TexViewHandler constructor with configured resourceEndpoints
     * @param resourceEndpoints - list of resource endpoint configuration objects for this view service
     */
    public TexViewHandler(List<ResourceEndpointConfig>  resourceEndpoints) {

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
     * Exceptions
     * @throws TexViewServiceException  an error was detected and details are reported in the exception
     */
    private String resolvePlatformRootURL(String platformName, String methodName) throws TexViewServiceException

    {
        String platformRootURL = null;

        if (platformName != null) {
            ResourceEndpoint resource = configuredPlatforms.get(platformName);
            if (resource != null) {
                platformRootURL = resource.getResourceRootURL();
            }
        }
        if (platformName == null || platformRootURL == null) {
            throw new TexViewServiceException(TexViewErrorCode.VIEW_SERVICE_NULL_PLATFORM_NAME.getMessageDefinition(),
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
     * @param deprecationOption only include deprecated types if this option is true
     * @param methodName The name of the method being invoked
     * @return response containing the TypeExplorer object.
     *
     * Exceptions
     * @throws TexViewServiceException  an error was detected and details are reported in the exception
     */
    public TypeExplorer getTypeExplorer(String    userId,
                                        String    repositoryServerName,
                                        String    platformName,
                                        boolean   enterpriseOption,
                                        boolean   deprecationOption,
                                        String    methodName)
    throws
        TexViewServiceException

    {

        try {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

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
            // The platformRootURL and repositoryName are passed in only for error logging
            tex.resolve(deprecationOption, platformRootURL, repositoryServerName);
            return tex;

        }
        catch (UserNotAuthorizedException e)
        {
            throw TexExceptionHandler.mapOMRSUserNotAuthorizedException(this.getClass().getName(), methodName, e);
        }
        catch (RepositoryErrorException e)
        {
            throw TexExceptionHandler.mapOMRSRepositoryErrorException(this.getClass().getName(), methodName, e);
        }
        catch (InvalidParameterException e)
        {
            throw TexExceptionHandler.mapOMRSInvalidParameterException(this.getClass().getName(), methodName, e);
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




}
