/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.notificationmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.viewservices.notificationmanager.server.NotificationManagerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The NotificationManagerResource provides part of the server-side implementation of the Notification Manager OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/notification-manager")

@Tag(name="API: Notification Manager", description="Supports a personalized notification service.  This includes the definition of the trigger for the notification, the style of notification and the recipient.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/notification-manager/overview/"))

public class NotificationManagerResource
{
    private final NotificationManagerRESTServices restAPI = new NotificationManagerRESTServices();

    /**
     * Default constructor
     */
    public NotificationManagerResource()
    {
    }


    /**
     * Attach a monitored resource to a notification type.
     *
     * @param serverName         name of called server
     * @param notificationTypeGUID            unique identifier of the notification type
     * @param elementGUID             unique identifier of the element to monitor
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/notification-types/{notificationTypeGUID}/monitored-resources/{elementGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkMonitoredResource",
            description="Attach a monitored resource to a notification type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/notification-type"))

    public VoidResponse linkMonitoredResource(@PathVariable
                                              String                  serverName,
                                              @PathVariable
                                              String notificationTypeGUID,
                                              @PathVariable
                                              String elementGUID,
                                              @RequestBody (required = false)
                                              NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkMonitoredResource(serverName, notificationTypeGUID, elementGUID, requestBody);
    }


    /**
     * Detach a monitored resource from a notification type.
     *
     * @param serverName         name of called server
     * @param notificationTypeGUID            unique identifier of the notification type
     * @param elementGUID             unique identifier of the element to monitor
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/notification-types/{notificationTypeGUID}/monitored-resources/{elementGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachMonitoredResource",
            description="Detach a monitored resource from a notification type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/notification-type"))

    public VoidResponse detachMonitoredResource(@PathVariable
                                                String                    serverName,
                                                @PathVariable
                                                String notificationTypeGUID,
                                                @PathVariable
                                                String elementGUID,
                                                @RequestBody (required = false)
                                                DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachMonitoredResource(serverName, notificationTypeGUID, elementGUID, requestBody);
    }


    /**
     * Attach subscriber to a notification type.
     *
     * @param serverName         name of called server
     * @param notificationTypeGUID            unique identifier of the notification type
     * @param elementGUID             unique identifier of the subscriber element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/notification-types/{notificationTypeGUID}/notification-subscribers/{elementGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkNotificationSubscriber",
            description="Attach subscriber to a notification type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/notification-type"))

    public VoidResponse linkNotificationSubscriber(@PathVariable
                                                   String                  serverName,
                                                   @PathVariable
                                                   String notificationTypeGUID,
                                                   @PathVariable
                                                   String elementGUID,
                                                   @RequestBody (required = false)
                                                   NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkNotificationSubscriber(serverName, notificationTypeGUID, elementGUID, requestBody);
    }


    /**
     * Detach a subscriber from a notification type.
     *
     * @param serverName         name of called server
     * @param notificationTypeGUID            unique identifier of the notification type
     * @param elementGUID             unique identifier of the subscriber element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/notification-types/{notificationTypeGUID}/notification-subscribers/{elementGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachNotificationSubscriber",
            description="Detach a subscriber from a notification type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/notification-type"))

    public VoidResponse detachNotificationSubscriber(@PathVariable
                                                     String                    serverName,
                                                     @PathVariable
                                                     String notificationTypeGUID,
                                                     @PathVariable
                                                     String elementGUID,
                                                     @RequestBody (required = false)
                                                     DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachNotificationSubscriber(serverName, notificationTypeGUID, elementGUID, requestBody);
    }
}
