/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.datamanager.server.DataManagerRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.springframework.web.bind.annotation.*;

/**
 * Server-side REST API support for data manager independent REST endpoints
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-manager/users/{userId}")

@Tag(name="Metadata Access Server: Data Manager OMAS",
     description="The Data Manager OMAS provides APIs for tools and applications wishing to manage metadata relating to data managers " +
                         "such as database servers, event brokers, content managers and file systems.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/data-manager/overview/"))

public class DataManagerOMASResource
{
    private final DataManagerRESTServices restAPI = new DataManagerRESTServices();


    /**
     * Instantiates a new Data Manager OMAS resource.
     */
    public DataManagerOMASResource()
    {
    }


    /**
     * Return the description of this service.
     *
     * @param serverName name of the server to route the request to
     * @param userId identifier of calling user
     *
     * @return service description or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/description")

    public RegisteredOMAGServiceResponse getServiceDescription(@PathVariable String serverName,
                                                               @PathVariable String userId)
    {
        return restAPI.getServiceDescription(serverName, userId);
    }


    /**
     * Return the connection object for the Data Manager OMAS's out topic.
     *
     * @param serverName name of the server to route the request to
     * @param userId identifier of calling user
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/topics/out-topic-connection/{callerId}")

    public OCFConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }



    /**
     * Files live on a file system.  This method creates a top level software server capability for a filesystem.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody properties of the file system
     *
     * @return unique identifier for the file system or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/metadata-sources/filesystems")

    public GUIDResponse   createFileSystemInCatalog(@PathVariable String                serverName,
                                                    @PathVariable String                userId,
                                                    @RequestBody  FileSystemRequestBody requestBody)
    {
        return restAPI.createFileSystemInCatalog(serverName, userId, requestBody);
    }

    /**
     * Files can be owned by a file manager.  This method creates a top level software server capability for a file manager.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody properties of the file manager
     *
     * @return unique identifier for the file system or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/metadata-sources/file-managers")

    public GUIDResponse   createFileManagerInCatalog(@PathVariable String                 serverName,
                                                     @PathVariable String                 userId,
                                                     @RequestBody  FileManagerRequestBody requestBody)
    {
        return restAPI.createFileManagerInCatalog(serverName, userId, requestBody);
    }


    /**
     * Create information about the database manager (DBMS) that manages database schemas.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody description of the software server capability (specify qualified name at a minimum)
     *
     * @return unique identifier of the database manager's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-sources/database-managers")

    public GUIDResponse createDatabaseManager(@PathVariable String                     serverName,
                                              @PathVariable String                     userId,
                                              @RequestBody  DatabaseManagerRequestBody requestBody)
    {
        return restAPI.createDatabaseManagerInCatalog(serverName, userId, requestBody);
    }


    /**
     * Create information about the API manager that manages APIs.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody description of the software server capability (specify qualified name at a minimum)
     *
     * @return unique identifier of the database manager's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-sources/api-managers")

    public GUIDResponse createAPIManager(@PathVariable String                serverName,
                                         @PathVariable String                userId,
                                         @RequestBody  APIManagerRequestBody requestBody)
    {
        return restAPI.createAPIManagerInCatalog(serverName, userId, requestBody);
    }


    /**
     * Create information about the event broker that manages topics.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody description of the software server capability (specify qualified name at a minimum)
     *
     * @return unique identifier of the database manager's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-sources/event-brokers")

    public GUIDResponse createEventBroker(@PathVariable String                 serverName,
                                          @PathVariable String                 userId,
                                          @RequestBody  EventBrokerRequestBody requestBody)
    {
        return restAPI.createEventBrokerInCatalog(serverName, userId, requestBody);
    }


    /**
     * Create information about the applications tha manage business processes and interactions with users.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody description of the software server capability (specify qualified name at a minimum)
     *
     * @return unique identifier of the database manager's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-sources/applications")

    public GUIDResponse createApplication(@PathVariable String                 serverName,
                                          @PathVariable String                 userId,
                                          @RequestBody  ApplicationRequestBody requestBody)
    {
        return restAPI.createApplicationInCatalog(serverName, userId, requestBody);
    }


    /**
     * Create information about a data processing engine such as a reporting engine.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody description of the software server capability (specify qualified name at a minimum)
     *
     * @return unique identifier of the database manager's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-sources/data-processing-engines")

    public GUIDResponse createDataProcessingEngine(@PathVariable String                          serverName,
                                                   @PathVariable String                          userId,
                                                   @RequestBody  DataProcessingEngineRequestBody requestBody)
    {
        return restAPI.createDataProcessingEngineInCatalog(serverName, userId, requestBody);
    }


    /**
     * Retrieve the unique identifier of the software server capability representing a metadata source.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody unique name of the integration daemon
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "metadata-sources/by-name")

    public GUIDResponse  getMetadataSourceGUID(@PathVariable String          serverName,
                                               @PathVariable String          userId,
                                               @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getMetadataSourceGUID(serverName, userId, requestBody);
    }
}
