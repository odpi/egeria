/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.timekeeper.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.timekeeper.server.TimeKeeperRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The TimeKeeperResource provides part of the server-side implementation of the Time Keeper OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")

@Tag(name="API: Time Keeper", description="Manages and queries context events.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/time-keeper/overview/"))

public class TimeKeeperResource
{
    private final TimeKeeperRESTServices restAPI = new TimeKeeperRESTServices();

    /**
     * Default constructor
     */
    public TimeKeeperResource()
    {
    }


    /**
     * Create a context event.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the context event.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/context-events")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createContextEvent",
            description="Create a context event.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public GUIDResponse createContextEvent(@PathVariable String                               serverName,
                                           @PathVariable String             urlMarker,
                                           @RequestBody (required = false)
                                           NewElementRequestBody requestBody)
    {
        return restAPI.createContextEvent(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent a context event using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/context-events/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createContextEventFromTemplate",
            description="Create a new metadata element to represent a context event using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public GUIDResponse createContextEventFromTemplate(@PathVariable
                                                       String              serverName,
                                                       @PathVariable String             urlMarker,
                                                       @RequestBody (required = false)
                                                       TemplateRequestBody requestBody)
    {
        return restAPI.createContextEventFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of a context event.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param contextEventGUID unique identifier of the contextEvent (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/context-events/{contextEventGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateContextEvent",
            description="Update the properties of a context event.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public BooleanResponse updateContextEvent(@PathVariable
                                              String                                  serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String                                  contextEventGUID,
                                              @RequestBody (required = false)
                                              UpdateElementRequestBody requestBody)
    {
        return restAPI.updateContextEvent(serverName, urlMarker, contextEventGUID, requestBody);
    }


    /**
     * Connect two context events to show that one is dependent on another.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param parentContextEventGUID    unique identifier of the parent context event
     * @param childContextEventGUID     unique identifier of the chile context event
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/context-events/{parentContextEventGUID}/dependent-context-events/{childContextEventGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkDependentContextEvents",
            description="Connect two context events to show that one is dependent on another.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public VoidResponse linkDependentContextEvents(@PathVariable
                                                   String                     serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String parentContextEventGUID,
                                                   @PathVariable
                                                   String childContextEventGUID,
                                                   @RequestBody (required = false)
                                                   NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDependentContextEvents(serverName, urlMarker, parentContextEventGUID, childContextEventGUID, requestBody);
    }


    /**
     * Detach two dependent context events from one another.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param parentContextEventGUID    unique identifier of the parent context event
     * @param childContextEventGUID     unique identifier of the child context event
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/context-events/{parentContextEventGUID}/dependent-context-events/{childContextEventGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachDependentContextEvents",
            description="Detach two dependent context events from one another.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public VoidResponse detachDependentContextEvents(@PathVariable String                    serverName,
                                                     @PathVariable String             urlMarker,
                                                     @PathVariable String parentContextEventGUID,
                                                     @PathVariable String childContextEventGUID,
                                                     @RequestBody (required = false)
                                                     DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDependentContextEvents(serverName, urlMarker, parentContextEventGUID, childContextEventGUID, requestBody);
    }


    /**
     * Connect two context events to show that one is related to the other.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventOneGUID    unique identifier of the  context event at end 1
     * @param contextEventTwoGUID     unique identifier of the  context event at end 2
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/context-events/{contextEventOneGUID}/related-context-events/{contextEventTwoGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkRelatedContextEvents",
            description="Connect two context events to show that one is related to the other.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public VoidResponse linkRelatedContextEvents(@PathVariable String                     serverName,
                                                 @PathVariable String             urlMarker,
                                                 @PathVariable String contextEventOneGUID,
                                                 @PathVariable String contextEventTwoGUID,
                                                 @RequestBody (required = false)
                                                 NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkRelatedContextEvents(serverName, urlMarker, contextEventOneGUID, contextEventTwoGUID, requestBody);
    }


    /**
     * Detach two context events that are related to one another.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventOneGUID    unique identifier of the  context event at end 1
     * @param contextEventTwoGUID     unique identifier of the  context event at end 2
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/context-events/{contextEventOneGUID}/related-context-events/{contextEventTwoGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachRelatedContextEvents",
            description="Detach two context events that are related to one another.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public VoidResponse detachRelatedContextEvents(@PathVariable String                    serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable String contextEventOneGUID,
                                                   @PathVariable String contextEventTwoGUID,
                                                   @RequestBody (required = false)
                                                   DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachRelatedContextEvents(serverName, urlMarker, contextEventOneGUID, contextEventTwoGUID, requestBody);
    }


    /**
     * Connect a context event to an element that provides evidence that this context event is real.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventGUID    unique identifier of the context event
     * @param evidenceGUID     unique identifier of the element representing the evidence
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/context-events/{contextEventGUID}/evidence/{evidenceGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkContextEventEvidence",
            description="Connect a context event to an element that provides evidence that this context event is real.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public VoidResponse linkContextEventEvidence(@PathVariable
                                                 String                     serverName,
                                                 @PathVariable String             urlMarker,
                                                 @PathVariable
                                                 String contextEventGUID,
                                                 @PathVariable
                                                 String evidenceGUID,
                                                 @RequestBody (required = false)
                                                 NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkContextEventEvidence(serverName, urlMarker, contextEventGUID, evidenceGUID, requestBody);
    }


    /**
     * Detach a context event from an element that provides evidence that this context event is real.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventGUID    unique identifier of the context event
     * @param evidenceGUID     unique identifier of the element representing the evidence
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/context-events/{contextEventGUID}/evidence/{evidenceGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachContextEventEvidence",
            description="Detach a context event from an element that provides evidence that this context event is real.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public VoidResponse detachContextEventEvidence(@PathVariable
                                                   String                    serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String contextEventGUID,
                                                   @PathVariable
                                                   String evidenceGUID,
                                                   @RequestBody (required = false)
                                                   DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachContextEventEvidence(serverName, urlMarker, contextEventGUID, evidenceGUID, requestBody);
    }




    /**
     * Connect a context event to an element that is impacted by this event.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventGUID    unique identifier of the context event
     * @param impactedElementGUID     unique identifier of the element that is impacted by the event
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/context-events/{contextEventGUID}/impacts/{impactedElementGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkContextEventImpact",
            description="Connect a context event to an element that is impacted by this event.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public VoidResponse linkContextEventImpact(@PathVariable
                                                 String                     serverName,
                                                 @PathVariable String             urlMarker,
                                                 @PathVariable
                                                 String contextEventGUID,
                                                 @PathVariable
                                                 String impactedElementGUID,
                                                 @RequestBody (required = false)
                                                 NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkContextEventImpact(serverName, urlMarker, contextEventGUID, impactedElementGUID, requestBody);
    }


    /**
     * Detach a context event from an element that is impacted by the event.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventGUID    unique identifier of the context event
     * @param impactedElementGUID     unique identifier of the element that is impacted by the event
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/context-events/{contextEventGUID}/impacts/{impactedElementGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachContextEventImpact",
            description="Detach a context event from an element that is impacted by the event.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public VoidResponse detachContextEventImpact(@PathVariable
                                                   String                    serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String contextEventGUID,
                                                   @PathVariable
                                                   String impactedElementGUID,
                                                   @RequestBody (required = false)
                                                   DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachContextEventImpact(serverName, urlMarker, contextEventGUID, impactedElementGUID, requestBody);
    }


    /**
     * Connect a context event to an element whose data is impacted by this event.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param timelineAffectedElementGUID     unique identifier of the element whose data is impacted by the context event
     * @param contextEventGUID    unique identifier of the context event
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{timelineAffectedElementGUID}/context-events-describing-timeline-effects/{contextEventGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkContextEventTimelineEffect",
            description="Connect a context event to an element whose data is impacted by this event.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public VoidResponse linkContextEventTimelineEffect(@PathVariable
                                                       String                     serverName,
                                                       @PathVariable String             urlMarker,
                                                       @PathVariable
                                                       String timelineAffectedElementGUID,
                                                       @PathVariable
                                                       String contextEventGUID,
                                                       @RequestBody (required = false)
                                                       NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkContextEventTimelineEffect(serverName, urlMarker, timelineAffectedElementGUID, contextEventGUID, requestBody);
    }


    /**
     * Detach a context event from an element whose data is impacted by the event.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param timelineAffectedElementGUID     unique identifier of the element whose data is impacted by the context event
     * @param contextEventGUID    unique identifier of the context event
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{timelineAffectedElementGUID}/context-events-describing-timeline-effect/{contextEventGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachContextEventTimelineEffect",
            description="Detach a context event from an element whose data is impacted by the event.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public VoidResponse detachContextEventTimelineEffect(@PathVariable
                                                         String                    serverName,
                                                         @PathVariable String             urlMarker,
                                                         @PathVariable
                                                         String timelineAffectedElementGUID,
                                                         @PathVariable
                                                         String contextEventGUID,
                                                         @RequestBody (required = false)
                                                         DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachContextEventTimelineEffect(serverName, urlMarker, timelineAffectedElementGUID, contextEventGUID, requestBody);
    }


    /**
     * Delete a context event.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/context-events/{contextEventGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteContextEvent",
            description="Delete a context event.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public VoidResponse deleteContextEvent(@PathVariable
                                           String                    serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String                    contextEventGUID,
                                           @RequestBody (required = false)
                                           DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteContextEvent(serverName, urlMarker, contextEventGUID, requestBody);
    }


    /**
     * Returns the list of contextEvents with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/context-events/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getContextEventsByName",
            description="Returns the list of contextEvents with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public OpenMetadataRootElementsResponse getContextEventsByName(@PathVariable
                                                                   String            serverName,
                                                                   @PathVariable String             urlMarker,
                                                                   @RequestBody (required = false)
                                                                   FilterRequestBody requestBody)
    {
        return restAPI.getContextEventsByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of contextEvent metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/context-events/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findContextEvents",
            description="Retrieve the list of contextEvent metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public OpenMetadataRootElementsResponse findContextEvents(@PathVariable
                                                              String                  serverName,
                                                              @PathVariable String             urlMarker,
                                                              @RequestBody (required = false)
                                                              SearchStringRequestBody requestBody)
    {
        return restAPI.findContextEvents(serverName, urlMarker, requestBody);
    }


    /**
     * Return the properties of a specific context event.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param contextEventGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/context-events/{contextEventGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getContextEventByGUID",
            description="Return the properties of a specific context event.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/context-event"))

    public OpenMetadataRootElementResponse getContextEventByGUID(@PathVariable
                                                                 String             serverName,
                                                                 @PathVariable String             urlMarker,
                                                                 @PathVariable
                                                                 String             contextEventGUID,
                                                                 @RequestBody (required = false)
                                                                 GetRequestBody requestBody)
    {
        return restAPI.getContextEventByGUID(serverName, urlMarker, contextEventGUID, requestBody);
    }
}
