/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworkservices.gaf.rest.CompletionStatusRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionElementsResponse;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * A thin client for the Governance Engine Open Metadata View Service (OMVS) - the API a governance engine that
 * runs outside Egeria uses to find the engine actions approved to run on it, claim them and record how they
 * ended.
 * <br>
 * Like {@link AutomatedCurationClient}, it drives the REST API directly with the service's own request and
 * response beans, and it checks every response body, because failures come back inside a 200 response.  The
 * caller's userId comes from the security context, so every call is made as
 * {@value OMAGPlatformExtension#USER_ID} - see {@link FilesFvtSecurityConfig}.
 */
class GovernanceEngineClient
{
    private final RestTemplate restTemplate = new RestTemplate();
    private final String       urlRoot;


    /**
     * Create a client for the Governance Engine service running on this suite's view server.
     */
    GovernanceEngineClient()
    {
        this.urlRoot = OMAGPlatformExtension.getPlatformURLRoot()
                               + "/servers/" + OMAGPlatformExtension.VIEW_SERVER_NAME
                               + "/api/open-metadata/" + ViewServiceDescription.GOVERNANCE_ENGINE.getViewServiceURLMarker();
    }


    /**
     * Return the engine actions that are approved to run on a governance engine and that nobody has claimed.
     *
     * @param governanceEngineGUID governance engine
     * @return engine actions, empty rather than null when there are none
     * @throws Exception the service reported a problem
     */
    List<EngineActionElement> getApprovedEngineActions(String governanceEngineGUID) throws Exception
    {
        EngineActionElementsResponse response = restTemplate.getForObject(urlRoot + "/governance-engines/" + governanceEngineGUID
                                                                                  + "/engine-actions/approved?startFrom=0&pageSize="
                                                                                  + FilesFvtTestSupport.MAX_PAGE_SIZE,
                                                                          EngineActionElementsResponse.class);

        checkResponse("getApprovedEngineActions(" + governanceEngineGUID + ")", response);

        return (response.getElements() == null) ? List.of() : response.getElements();
    }


    /**
     * Return the engine actions on a governance engine that this caller has claimed and not yet finished.
     *
     * @param governanceEngineGUID governance engine
     * @return engine actions, empty rather than null when there are none
     * @throws Exception the service reported a problem
     */
    List<EngineActionElement> getActiveClaimedEngineActions(String governanceEngineGUID) throws Exception
    {
        EngineActionElementsResponse response = restTemplate.getForObject(urlRoot + "/governance-engines/" + governanceEngineGUID
                                                                                  + "/engine-actions/active-claimed?startFrom=0&pageSize="
                                                                                  + FilesFvtTestSupport.MAX_PAGE_SIZE,
                                                                          EngineActionElementsResponse.class);

        checkResponse("getActiveClaimedEngineActions(" + governanceEngineGUID + ")", response);

        return (response.getElements() == null) ? List.of() : response.getElements();
    }


    /**
     * Claim an engine action for this caller.
     *
     * @param engineActionGUID engine action to claim
     * @throws Exception the service reported a problem - including that someone else has claimed it
     */
    void claimEngineAction(String engineActionGUID) throws Exception
    {
        VoidResponse response = restTemplate.postForObject(urlRoot + "/engine-actions/" + engineActionGUID + "/claim",
                                                           null,
                                                           VoidResponse.class);

        checkResponse("claimEngineAction(" + engineActionGUID + ")", response);
    }


    /**
     * Declare that the work for a claimed engine action is finished.  If the engine action is a step in a
     * governance action process, the output guards decide which steps run next.
     *
     * @param engineActionGUID engine action that is finished
     * @param status how it ended
     * @param outputGuards guards that select the next steps
     * @param requestParameters request parameters to pass to the next steps, may be null
     * @param newActionTargets action targets to pass to the next steps, may be null
     * @param completionMessage message describing the outcome
     * @throws Exception the service reported a problem
     */
    void recordCompletionStatus(String                engineActionGUID,
                                CompletionStatus      status,
                                List<String>          outputGuards,
                                Map<String, String>   requestParameters,
                                List<NewActionTarget> newActionTargets,
                                String                completionMessage) throws Exception
    {
        CompletionStatusRequestBody requestBody = new CompletionStatusRequestBody();

        requestBody.setStatus(status);
        requestBody.setOutputGuards(outputGuards);
        requestBody.setRequestParameters(requestParameters);
        requestBody.setNewActionTargets(newActionTargets);
        requestBody.setCompletionMessage(completionMessage);

        VoidResponse response = restTemplate.postForObject(urlRoot + "/engine-actions/" + engineActionGUID + "/completion-status",
                                                           requestBody,
                                                           VoidResponse.class);

        checkResponse("recordCompletionStatus(" + engineActionGUID + ")", response);
    }


    /**
     * Fail the calling test if the service reported a problem.  See {@link AutomatedCurationClient} for why
     * the HTTP status alone is not enough.
     *
     * @param callDescription what was being asked for
     * @param response response to check
     */
    private void checkResponse(String           callDescription,
                               FFDCResponseBase response)
    {
        if (response == null)
        {
            throw new AssertionError("The Governance Engine service returned no response to " + callDescription
                                             + " - is " + OMAGPlatformExtension.VIEW_SERVER_NAME + " running it?");
        }

        if (response.getRelatedHTTPCode() != 200)
        {
            throw new AssertionError("The Governance Engine service refused " + callDescription + ": "
                                             + response.getRelatedHTTPCode() + " "
                                             + response.getExceptionErrorMessage()
                                             + " [" + response.getExceptionClassName() + "]"
                                             + " - " + response.getExceptionUserAction());
        }
    }
}
