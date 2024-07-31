/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceprogram.server.GovernanceDefinitionRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * GovernanceDefinitionsResource sets up the governance definitions that are part of an organization governance.
 * Each governance definition describes a decision.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-program/users/{userId}")

@Tag(name="Metadata Access Server: Governance Program OMAS",
     description="The Governance Program OMAS provides APIs and events for tools and applications focused on defining a data strategy, planning support for a regulation and/or developing a governance program for the data landscape.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/governance-program/overview/"))

public class GovernanceDefinitionsResource
{
    private final GovernanceDefinitionRESTServices restAPI = new GovernanceDefinitionRESTServices();

    /**
     * Default constructor
     */
    public GovernanceDefinitionsResource()
    {
    }


    /* ========================================
     * Governance Definitions
     */

    /**
     * Create a new governance definition.  The type of the definition is located in the properties.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody properties of the definition and initial status
     *
     * @return unique identifier of the definition or
     *  InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     *  PropertyServerException problem accessing the metadata service
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-definitions")

    public GUIDResponse createGovernanceDefinition(@PathVariable String                          serverName,
                                                   @PathVariable String                          userId,
                                                   @RequestBody  GovernanceDefinitionRequestBody requestBody)
    {
        return restAPI.createGovernanceDefinition(serverName, userId, requestBody);
    }


    /**
     * Update an existing governance definition.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionGUID unique identifier of the definition to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody properties to update
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-definitions/{definitionGUID}/update")

    public VoidResponse  updateGovernanceDefinition(@PathVariable String                          serverName,
                                                    @PathVariable String                          userId,
                                                    @PathVariable String                          definitionGUID,
                                                    @RequestParam boolean                         isMergeUpdate,
                                                    @RequestBody  GovernanceDefinitionRequestBody requestBody)
    {
        return restAPI.updateGovernanceDefinition(serverName, userId, definitionGUID, isMergeUpdate, requestBody);
    }


    /**
     * Update the status of a governance definition
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionGUID unique identifier
     * @param requestBody new status
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path="/governance-definitions/{definitionGUID}/update-status")

    public VoidResponse setGovernanceDefinitionStatus(@PathVariable String                      serverName,
                                                      @PathVariable String                      userId,
                                                      @PathVariable String                      definitionGUID,
                                                      @RequestBody  GovernanceStatusRequestBody requestBody)
    {
        return restAPI.setGovernanceDefinitionStatus(serverName, userId, definitionGUID, requestBody);
    }


    /**
     * Delete a specific governance definition.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionGUID unique identifier of the definition to remove
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path="/governance-definitions/{definitionGUID}/delete")

    public VoidResponse  deleteGovernanceDefinition(@PathVariable String                    serverName,
                                                    @PathVariable String                    userId,
                                                    @PathVariable String                    definitionGUID,
                                                    @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.deleteGovernanceDefinition(serverName, userId, definitionGUID, requestBody);
    }


    /**
     * Link two related governance definitions together.  The governance definitions are of the same type as follows:
     * <ul>
     *     <li>A relationship of type GovernanceDriverLink is between two GovernanceDriver definitions</li>
     *     <li>A relationship of type GovernancePolicyLink is between two GovernancePolicy definitions</li>
     *     <li>A relationship of type GovernanceControl is between two GovernanceControl definitions</li>
     * </ul>
     * If the link already exists the description is updated.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionOneGUID unique identifier of the first definition
     * @param definitionTwoGUID unique identifier of the second definition
     * @param requestBody description of their relationship
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path="/governance-definitions/{definitionOneGUID}/peers/{definitionTwoGUID}/link")

    public VoidResponse linkPeerDefinitions(@PathVariable String                  serverName,
                                            @PathVariable String                  userId,
                                            @PathVariable String                  definitionOneGUID,
                                            @PathVariable String                  definitionTwoGUID,
                                            @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.linkPeerDefinitions(serverName, userId, definitionOneGUID, definitionTwoGUID, requestBody);
    }


    /**
     * Remove the link between two definitions.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionOneGUID unique identifier of the first definition
     * @param definitionTwoGUID unique identifier of the second definition
     * @param requestBody the name of the relationship to delete
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path="/governance-definitions/{definitionOneGUID}/peers/{definitionTwoGUID}/unlink")

    public VoidResponse unlinkPeerDefinitions(@PathVariable String                  serverName,
                                              @PathVariable String                  userId,
                                              @PathVariable String                  definitionOneGUID,
                                              @PathVariable String                  definitionTwoGUID,
                                              @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.unlinkPeerDefinitions(serverName, userId, definitionOneGUID, definitionTwoGUID, requestBody);
    }


    /**
     * Create a link to show that a governance definition supports the requirements of another governance definition.
     * This supporting relationship is between definitions of different types as follows:
     * <ul>
     *     <li>A relationship of type GovernanceResponse is between a GovernanceDriver and a GovernancePolicy</li>
     *     <li>A relationship of type GovernanceImplementation is between a GovernancePolicy and a GovernanceControl</li>
     * </ul>
     * If the link already exists the rationale is updated.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionGUID unique identifier of the governance definition
     * @param supportingDefinitionGUID unique identifier of the supporting governance definition
     * @param requestBody description of how the supporting definition provides support
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path="/governance-definitions/{definitionGUID}/supporting-definitions/{supportingDefinitionGUID}/link")

    public VoidResponse setupSupportingDefinition(@PathVariable String                  serverName,
                                                  @PathVariable String                  userId,
                                                  @PathVariable String                  definitionGUID,
                                                  @PathVariable String                  supportingDefinitionGUID,
                                                  @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupSupportingDefinition(serverName, userId, definitionGUID, supportingDefinitionGUID, requestBody);
    }


    /**
     * Remove the supporting link between two governance definitions.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionGUID unique identifier of the governance definition
     * @param supportingDefinitionGUID unique identifier of the supporting governance definition
     * @param requestBody the name of the relationship to delete
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path="/governance-definitions/{definitionGUID}/supporting-definitions/{supportingDefinitionGUID}/unlink")

    public VoidResponse clearSupportingDefinition(@PathVariable String                  serverName,
                                                  @PathVariable String                  userId,
                                                  @PathVariable String                  definitionGUID,
                                                  @PathVariable String                  supportingDefinitionGUID,
                                                  @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.clearSupportingDefinition(serverName, userId, definitionGUID, supportingDefinitionGUID, requestBody);
    }
}
