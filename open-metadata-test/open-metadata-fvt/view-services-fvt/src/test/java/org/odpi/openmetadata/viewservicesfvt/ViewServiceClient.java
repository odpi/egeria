/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import com.fasterxml.jackson.databind.JsonNode;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.OpenMetadataRootElementResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.OpenMetadataRootElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * ViewServiceClient drives one view service's REST API on this suite's view server.
 * <br>
 * Most view services have no generated Java client, and the point of this suite is the REST API that user
 * interfaces and pyegeria call, so the tests use the services' own request and response beans over plain HTTP.
 * A change to a bean is then a compile failure here rather than a test that quietly stops checking anything.
 * <br>
 * Egeria reports failures <b>inside a 200 response</b> - an FFDC response carries {@code relatedHTTPCode} and the
 * exception details as fields - so every call is checked with {@link #checkResponse} before its payload is
 * used, and fails with the service's own explanation.  {@link #postForFailure} is for the tests that expect a
 * request to be refused.
 * <br>
 * Paths are relative to the service's root, {@code /servers/{viewServer}/api/open-metadata/{urlMarker}}, and
 * start with "/" (or are empty for the root itself).
 */
public class ViewServiceClient
{
    private final RestTemplate restTemplate = new RestTemplate();
    private final String       serviceName;
    private final String       urlRoot;


    /**
     * Create a client for one view service.
     *
     * @param viewService the service
     */
    public ViewServiceClient(ViewServiceDescription viewService)
    {
        this(viewService.getViewServiceName(), viewService.getViewServiceURLMarker());
    }


    /**
     * Create a client for a URL marker.  The generic view services serve several markers each.
     *
     * @param serviceName name used in failure messages
     * @param urlMarker the service's URL marker
     */
    public ViewServiceClient(String serviceName,
                             String urlMarker)
    {
        this.serviceName = serviceName;
        this.urlRoot     = OMAGPlatformExtension.getPlatformURLRoot()
                                   + "/servers/" + OMAGPlatformExtension.VIEW_SERVER_NAME
                                   + "/api/open-metadata/" + urlMarker;
    }


    /**
     * POST a request and check that it succeeded.
     *
     * @param path path below the service root
     * @param requestBody request body (may be null)
     * @param responseType expected response bean
     * @return the checked response
     * @param <T> response type
     */
    public <T extends FFDCResponseBase> T post(String   path,
                                               Object   requestBody,
                                               Class<T> responseType)
    {
        T response = restTemplate.postForObject(urlRoot + path, requestBody, responseType);

        checkResponse("POST " + path, response);

        return response;
    }


    /**
     * GET a request and check that it succeeded.
     *
     * @param path path below the service root
     * @param responseType expected response bean
     * @return the checked response
     * @param <T> response type
     */
    public <T extends FFDCResponseBase> T get(String   path,
                                              Class<T> responseType)
    {
        T response = restTemplate.getForObject(urlRoot + path, responseType);

        checkResponse("GET " + path, response);

        return response;
    }


    /**
     * POST a request without checking the response, for a caller that tests the outcome itself.
     *
     * @param path path below the service root
     * @param requestBody request body (may be null)
     * @param responseType expected response bean
     * @return the response, unchecked
     * @param <T> response type
     */
    public <T extends FFDCResponseBase> T postUnchecked(String   path,
                                                        Object   requestBody,
                                                        Class<T> responseType)
    {
        return restTemplate.postForObject(urlRoot + path, requestBody, responseType);
    }


    /**
     * POST a request that is expected to be refused, and return the response so the caller can check why.  The
     * response is read as plain JSON because a refused request's response class depends on the request.
     *
     * @param path path below the service root
     * @param requestBody request body (may be null)
     * @return the response, which reported a failure
     */
    public JsonNode postForFailure(String path,
                                   Object requestBody)
    {
        JsonNode response = restTemplate.postForObject(urlRoot + path, requestBody, JsonNode.class);

        assertNotNull(response, serviceName + " returned no response to POST " + path);
        assertNotEquals(200, response.path("relatedHTTPCode").asInt(), serviceName + " accepted POST " + path + ", which should have been refused");

        return response;
    }


    /**
     * POST a request that creates something and return its unique identifier.
     *
     * @param path path below the service root
     * @param requestBody request body
     * @return the new GUID
     */
    public String create(String path,
                         Object requestBody)
    {
        String guid = post(path, requestBody, GUIDResponse.class).getGUID();

        assertNotNull(guid, serviceName + " POST " + path + " succeeded but returned no GUID");

        return guid;
    }


    /**
     * POST a request that returns nothing.
     *
     * @param path path below the service root
     * @param requestBody request body (may be null)
     */
    public void call(String path,
                     Object requestBody)
    {
        post(path, requestBody, VoidResponse.class);
    }


    /**
     * POST a request that returns one element.
     *
     * @param path path below the service root
     * @param requestBody request body (may be null)
     * @return the element, which may be null
     */
    public OpenMetadataRootElement element(String path,
                                           Object requestBody)
    {
        return post(path, requestBody, OpenMetadataRootElementResponse.class).getElement();
    }


    /**
     * POST a request that returns a list of elements.
     *
     * @param path path below the service root
     * @param requestBody request body (may be null)
     * @return the elements, empty rather than null when there are none
     */
    public List<OpenMetadataRootElement> elements(String path,
                                                  Object requestBody)
    {
        List<OpenMetadataRootElement> elements = post(path, requestBody, OpenMetadataRootElementsResponse.class).getElements();

        return (elements == null) ? List.of() : elements;
    }


    /**
     * Fail with the service's own explanation if a call did not succeed.
     *
     * @param callDescription what was called
     * @param response response to check
     */
    private void checkResponse(String           callDescription,
                               FFDCResponseBase response)
    {
        assertNotNull(response, serviceName + " returned no response to " + callDescription);
        assertEquals(200,
                     response.getRelatedHTTPCode(),
                     serviceName + " refused " + callDescription + ": " + response.getExceptionErrorMessage()
                             + " [" + response.getExceptionClassName() + "] - " + response.getExceptionUserAction());
    }
}
