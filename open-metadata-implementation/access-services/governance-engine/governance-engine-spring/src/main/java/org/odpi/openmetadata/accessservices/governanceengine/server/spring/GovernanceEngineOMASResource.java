/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceengine.server.spring;

import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernanceClassificationDefinitionAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernanceClassificationDefinitionListAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernedAssetComponentAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernedAssetComponentListAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.server.GovernanceEngineRESTServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/open-metadata/access-services/governance-engine/users/{userId}")
public class GovernanceEngineOMASResource {
    private GovernanceEngineRESTServices restAPI = new GovernanceEngineRESTServices();

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineOMASResource.class);

    /**
     * Constructor
     */
    public GovernanceEngineOMASResource() {

        final String methodName = "initialize";

        log.debug(">>" + methodName);

        log.debug("<<" + methodName);

    }

    /**
     * Returns the list of governance tags for the enforcement engine
     * <p>
     * These are the definitions - so tell us the name, guid, attributes
     * associated with the tag. The security engine will want to know about
     * these tags to assist in policy authoring/validation, as well as know
     * when they change, since any existing assets classified with the tags
     * are affected
     *
     * @param userId             - String - userId of user making request.
     * @param rootClassification - this may be the qualifiedName or displayName of the connection.
     * @return GovernanceClassificationDefinitionList or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/govclassdefs")
    public GovernanceClassificationDefinitionListAPIResponse getGovernanceClassificationDefinitions(@PathVariable String userId,
                                                                                                    @RequestParam(value = "rootClassification", required = false) List<String> rootClassification
    ) {
        return restAPI.getGovernanceClassificationDefinitions(userId, rootClassification);
    }

    /**
     * Returns a single governance tag for the enforcement engine
     * <p>
     * These are the definitions - so tell us the name, guid, attributes
     * associated with the tag. The security engine will want to know about
     * these tags to assist in policy authoring/validation, as well as know
     * when they change, since any existing assets classified with the tags
     * are affected
     *
     * @param userId  - String - userId of user making request.
     * @param tagguid - guid of the definition to retrieve
     * @return GovernanceClassificationDefinition or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/govclassdefs/{tagguid}")
    GovernanceClassificationDefinitionAPIResponse getGovernanceClassificationDefinition(@PathVariable String userId, @PathVariable String tagguid) {
        return restAPI.getClassificationDefinition(userId, tagguid);
    }

    /**
     * Returns the list of governed asset
     * <p>
     * These include the tag associations but not the definitions of those tags
     *
     * @param userId             - String - userId of user making request.
     * @param rootClassification - this may be the qualifiedName or displayName of the connection.
     * @param rootType
     * @return GovernedAssetComponentList or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/govAssets")
    GovernedAssetComponentListAPIResponse getGovernedAssetComponents(@PathVariable String userId,
                                                                     @RequestParam(value = "rootClassification", required = false) List<String> rootClassification,
                                                                     @RequestParam(value = "rootType", required = false) List<String> rootType) {
        return restAPI.getGovernedAssetComponents(userId, rootClassification, rootType);
    }

    /**
     * Returns a single governed asset
     * <p>
     * These include the tag associations but not the definitions of those tags
     *
     * @param userId             - String - userId of user making request.
     * @param assetComponentGuid - Guid of the asset component to retrieve
     * @return GovernedAssetComponent or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/govAssets/{assetComponentGuid}")
    public GovernedAssetComponentAPIResponse getGovernedAssetComponent(@PathVariable String userId,
                                                                       @PathVariable String assetComponentGuid) {
        return restAPI.getGovernedAssetComponent(userId, assetComponentGuid);
    }


}
