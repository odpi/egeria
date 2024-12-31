/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.notificationmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.notificationmanager.server.NotificationManagerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The NotificationManagerResource provides part of the server-side implementation of the Notification Manager OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/notification-manager")

@Tag(name="API: Notification Manager OMVS", description="The Notification Manager OMVS provides APIs for supporting a personalized notification service.  This includes the definition of the trigger for the notification, the style of notification and the recipient.",
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

}
