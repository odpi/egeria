/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.stewardshipengineservices.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;


/**
 * StewardshipEngineResource provides the server-side catcher for REST calls using Spring that target a specific
 * stewardship engine hosted in a stewardship server.
 * The stewardship server routes these requests to the named stewardship engine.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/stewardship-server/users/{userId}/stewardship-engines/{stewardshipEngineName}")

@Tag(name="Stewardship Engine Services", description="The stewardship engine services provide the core subsystem for a stewardship server. A stewardship server is an OMAG Server that hosts automated metadata stewardship.", externalDocs=@ExternalDocumentation(description="Stewardship Engine Services",url="https://egeria.odpi.org/open-metadata-implementation/governance-servers/stewardship-engine-services/"))

public class StewardshipEngineResource
{
}
