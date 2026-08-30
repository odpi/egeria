/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.OpenMetadataRootElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworkservices.gaf.rest.InitiateGovernanceActionProcessRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.InitiateGovernanceActionTypeRequestBody;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.TechnologyTypeReport;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.TechnologyTypeSummary;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeReportResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeSummaryListResponse;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * A thin client for the Automated Curation Open Metadata View Service (OMVS) - the API a curator uses to find
 * out what Egeria can automate for a technology and then ask for it to happen.
 * <br>
 * The Automated Curation OMVS has no generated Java client, so this suite drives its REST API directly.  It
 * does so with the service's own request and response beans rather than hand-built JSON, so that a change to
 * either is a compile failure here rather than a test that silently stops checking anything.
 * <br>
 * Two things about this API shape the client.  It is a <b>view service</b>, so it takes the caller's userId
 * from the security context rather than from the request - {@link FilesFvtSecurityConfig} is what makes
 * that userId this suite's own.  And, like the rest of Egeria's REST surface, it reports failures <b>inside a
 * 200 response</b>: an FFDC response carries {@code relatedHTTPCode} and the exception details as fields.  A
 * client that only checked the HTTP status would treat "the governance action type does not exist" as a
 * success and return a null GUID, so {@link #checkResponse} inspects every response before its payload is
 * used.
 */
class AutomatedCurationClient
{
    private final RestTemplate restTemplate = new RestTemplate();
    private final String       urlRoot;


    /**
     * Create a client for the Automated Curation service running on this suite's view server.
     */
    AutomatedCurationClient()
    {
        this.urlRoot = OMAGPlatformExtension.getPlatformURLRoot()
                               + "/servers/" + OMAGPlatformExtension.VIEW_SERVER_NAME
                               + "/api/open-metadata/" + ViewServiceDescription.AUTOMATED_CURATION.getViewServiceURLMarker();
    }


    /**
     * Return the technology types whose names contain the supplied string.
     *
     * @param searchString string to look for
     * @return matching technology types, empty rather than null when there are none
     * @throws Exception the service reported a problem
     */
    List<TechnologyTypeSummary> findTechnologyTypes(String searchString) throws Exception
    {
        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setStartFrom(0);
        requestBody.setPageSize(FilesFvtTestSupport.MAX_PAGE_SIZE);

        TechnologyTypeSummaryListResponse response = restTemplate.postForObject(urlRoot + "/technology-types/by-search-string",
                                                                                requestBody,
                                                                                TechnologyTypeSummaryListResponse.class);

        checkResponse("findTechnologyTypes(" + searchString + ")", response);

        return (response.getElements() == null) ? List.of() : response.getElements();
    }


    /**
     * Return the full report for one technology type: its catalog templates, the governance action processes
     * that can be run against it, and the connectors and services that support it.
     * <br>
     * This is the call that makes the content pack usable by someone who did not write it - everything the
     * suite goes on to run is discoverable from here.
     *
     * @param technologyTypeName exact name of the technology type, no wildcards
     * @return the report
     * @throws Exception the service reported a problem, or there is no such technology type
     */
    TechnologyTypeReport getTechnologyTypeDetail(String technologyTypeName) throws Exception
    {
        FilterRequestBody requestBody = new FilterRequestBody();

        requestBody.setFilter(technologyTypeName);

        TechnologyTypeReportResponse response = restTemplate.postForObject(urlRoot + "/technology-types/by-name",
                                                                           requestBody,
                                                                           TechnologyTypeReportResponse.class);

        checkResponse("getTechnologyTypeDetail(" + technologyTypeName + ")", response);

        if (response.getElement() == null)
        {
            throw new AssertionError("The Automated Curation service returned no report for technology type '" + technologyTypeName
                                             + "' - it is not defined in the content packs loaded into "
                                             + OMAGPlatformExtension.METADATA_STORE_NAME + ".");
        }

        return response.getElement();
    }


    /**
     * Return the elements that have been catalogued as instances of one technology type.  This is how a
     * curator sees what has actually been catalogued, as opposed to what could be.
     *
     * @param technologyTypeName exact name of the technology type, no wildcards
     * @return matching elements, empty rather than null when there are none
     * @throws Exception the service reported a problem
     */
    List<OpenMetadataRootElement> getTechnologyTypeElements(String technologyTypeName) throws Exception
    {
        FilterRequestBody requestBody = new FilterRequestBody();

        requestBody.setFilter(technologyTypeName);
        requestBody.setStartFrom(0);
        requestBody.setPageSize(FilesFvtTestSupport.MAX_PAGE_SIZE);

        OpenMetadataRootElementsResponse response = restTemplate.postForObject(urlRoot + "/technology-types/elements",
                                                                               requestBody,
                                                                               OpenMetadataRootElementsResponse.class);

        checkResponse("getTechnologyTypeElements(" + technologyTypeName + ")", response);

        return (response.getElements() == null) ? List.of() : response.getElements();
    }


    /**
     * Create a new element from one of the catalog templates, supplying a value for each of its placeholders.
     *
     * @param templateGUID template to use
     * @param placeholderPropertyValues value for each placeholder the template declares
     * @return unique identifier of the new element
     * @throws Exception the service reported a problem
     */
    String createElementFromTemplate(String              templateGUID,
                                     Map<String, String> placeholderPropertyValues) throws Exception
    {
        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(templateGUID);
        requestBody.setPlaceholderPropertyValues(placeholderPropertyValues);

        GUIDResponse response = restTemplate.postForObject(urlRoot + "/catalog-templates/new-element",
                                                           requestBody,
                                                           GUIDResponse.class);

        checkResponse("createElementFromTemplate(" + templateGUID + ")", response);

        return response.getGUID();
    }


    /**
     * Ask for a single governance action type to be run, and return the engine action that represents the
     * request.
     * <br>
     * The call returns as soon as the request has been recorded.  Running it is somebody else's job: the
     * metadata access store publishes the new engine action on the Open Governance out topic, and the engine
     * host that hosts the named engine picks it up, claims it and runs the governance service behind the
     * request type.  {@link EngineActionWaiter} is what turns the returned GUID into an outcome.
     *
     * @param governanceActionTypeQualifiedName qualified name of the governance action type, for example
     *                                          "PostgreSQLSurvey::survey-postgres-server"
     * @param requestParameters values passed to the governance service, may be null
     * @param actionTargets elements the action is to act on, may be null
     * @return unique identifier of the new engine action
     * @throws Exception the service reported a problem
     */
    String initiateGovernanceActionType(String                governanceActionTypeQualifiedName,
                                        Map<String, String>   requestParameters,
                                        List<NewActionTarget> actionTargets) throws Exception
    {
        InitiateGovernanceActionTypeRequestBody requestBody = new InitiateGovernanceActionTypeRequestBody();

        requestBody.setGovernanceActionTypeQualifiedName(governanceActionTypeQualifiedName);
        requestBody.setRequestParameters(requestParameters);
        requestBody.setActionTargets(actionTargets);

        GUIDResponse response = restTemplate.postForObject(urlRoot + "/governance-action-types/initiate",
                                                           requestBody,
                                                           GUIDResponse.class);

        checkResponse("initiateGovernanceActionType(" + governanceActionTypeQualifiedName + ")", response);

        return response.getGUID();
    }


    /**
     * Ask for a governance action process to be run, and return the process instance that represents this run.
     * <br>
     * The GUID that comes back is <b>not</b> an engine action.  Initiating a process creates a
     * {@code GovernanceActionProcessInstance} - the element standing for this particular run - and links the
     * engine action for step one to it by an {@code ActionRequester} relationship.  From there each step's
     * completion guards decide which step runs next.  See {@link EngineActionWaiter#waitForProcess} for how the
     * suite gets from this GUID to the outcome of the whole chain.
     *
     * @param processQualifiedName qualified name of the governance action process, for example
     *                             "PostgreSQLServer:CreateAndSurveyGovernanceActionProcess"
     * @param requestParameters values passed to every step of the process, may be null
     * @param actionTargets elements the process is to act on, may be null
     * @return unique identifier of the governance action process instance for this run
     * @throws Exception the service reported a problem
     */
    String initiateGovernanceActionProcess(String                processQualifiedName,
                                           Map<String, String>   requestParameters,
                                           List<NewActionTarget> actionTargets) throws Exception
    {
        InitiateGovernanceActionProcessRequestBody requestBody = new InitiateGovernanceActionProcessRequestBody();

        requestBody.setProcessQualifiedName(processQualifiedName);
        requestBody.setRequestParameters(requestParameters);
        requestBody.setActionTargets(actionTargets);

        GUIDResponse response = restTemplate.postForObject(urlRoot + "/governance-action-processes/initiate",
                                                           requestBody,
                                                           GUIDResponse.class);

        checkResponse("initiateGovernanceActionProcess(" + processQualifiedName + ")", response);

        return response.getGUID();
    }


    /**
     * Fail the calling test if the service reported a problem.
     * <br>
     * Egeria's REST surface answers 200 and puts the failure in the body, so this is not defensive
     * programming: without it, every call in this class would quietly return null on failure and the test
     * would fail later, somewhere else, with a message about a missing element rather than about the request
     * that was actually refused.
     *
     * @param callDescription what was being asked for
     * @param response response to check
     */
    private void checkResponse(String           callDescription,
                               FFDCResponseBase response)
    {
        if (response == null)
        {
            throw new AssertionError("The Automated Curation service returned no response to " + callDescription
                                             + " - is " + OMAGPlatformExtension.VIEW_SERVER_NAME + " running?");
        }

        if (response.getRelatedHTTPCode() != 200)
        {
            throw new AssertionError("The Automated Curation service refused " + callDescription + ": "
                                             + response.getRelatedHTTPCode() + " "
                                             + response.getExceptionErrorMessage()
                                             + " [" + response.getExceptionClassName() + "]"
                                             + " - " + response.getExceptionUserAction());
        }
    }
}
