/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceengine.server.spring;

import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernanceClassificationDefAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernanceClassificationDefListAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernedAssetAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernedAssetListAPIResponse;
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
     * @param classification - this may be the qualifiedName or displayName of the connection.
     * @return GovernanceClassificationDefinitionList or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/classificationdefs")
    public GovernanceClassificationDefListAPIResponse getGovernanceClassificationDefinitions(@PathVariable String userId,
                                                                                             @RequestParam(value =
                                                                                                     "classification"
                                                                                                     , required = false) List<String> classification
    ) {
        return restAPI.getGovernanceClassificationDefinitions(userId, classification);
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
     * @param classificationGuid - guid of the definition to retrieve
     * @return GovernanceClassificationDef or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/classificationdefs/{classificationGuid}")
    GovernanceClassificationDefAPIResponse getGovernanceClassificationDefinition(@PathVariable String userId,
                                                                                 @PathVariable String classificationGuid) {
        return restAPI.getClassificationDefinition(userId, classificationGuid);
    }

    /**
     * Returns the list of governed asset
     * <p>
     * These include the tag associations but not the definitions of those tags
     *
     * @param userId             - String - userId of user making request.
     * @param classification - this may be the qualifiedName or displayName of the connection.
     * @param type
     * @return GovernedAssetComponentList or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets")
    GovernedAssetListAPIResponse getGovernedAssetComponents(@PathVariable String userId,
                                                            @RequestParam(value = "classification", required = false) List<String> classification,
                                                            @RequestParam(value = "type", required = false) List<String> type) {
        return restAPI.getGovernedAssetComponents(userId, classification, type);
    }

    /**
     * Returns a single governed asset
     * <p>
     * These include the tag associations but not the definitions of those tags
     *
     * @param userId             - String - userId of user making request.
     * @param assetGuid - Guid of the asset component to retrieve
     * @return GovernedAsset or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetComponentGuid}")
    public GovernedAssetAPIResponse getGovernedAssetComponent(@PathVariable String userId,
                                                              @PathVariable String assetGuid) {
        return restAPI.getGovernedAssetComponent(userId, assetGuid);
    }


}
