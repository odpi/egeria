/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.Label;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.Paging;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.search.IGCSearch;
import org.springframework.util.Base64Utils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Library of methods to connect to and interact with an IBM Information Governance Catalog environment
 * using appropriate session management.
 * <br><br>
 * Methods are provided to interact with REST API endpoints and process results as JsonNode objects
 * (ie. allowing direct traversal of the JSON objects) and through the use of registered POJOs to
 * automatically (de-)serialise between the JSON form and a native Java object.
 * <br><br>
 * The native Java objects for out-of-the-box Information Governance Catalog asset types have been
 * generated under org.odpi.openmetadata.adapters.repositoryservices.igc.model.* -- including different
 * versions depending on the environment to which you are connecting.
 * <br><br>
 * For additional examples of using the REST API (eg. potential criteria and operators for searching, etc), see:
 * <ul>
 *     <li><a href="http://www-01.ibm.com/support/docview.wss?uid=swg27047054">IGC REST API: Tips, tricks, and time-savers</a></li>
 *     <li><a href="http://www-01.ibm.com/support/docview.wss?uid=swg27047059">IGC REST API: Sample REST API calls and use case descriptions</a></li>
 * </ul>
 *
 * @see #registerPOJO(Class)
 */
public class IGCRestClient {

    private String authorization;
    private String baseURL;
    private Boolean workflowEnabled = false;
    private List<String> cookies = null;

    private ObjectMapper mapper;

    public static final String TYPES = "/ibm/iis/igc-rest/v1/types";
    public static final String ASSET = "/ibm/iis/igc-rest/v1/assets";
    public static final String SEARCH = "/ibm/iis/igc-rest/v1/search";
    public static final String LOGOUT  = "/ibm/iis/igc-rest/v1/logout";

    // TODO: pickup the URL and authorization information from a properties file, by default
    public IGCRestClient() {
        this(null, null);
    }

    /**
     * Default constructor used by the IGCRestClient.
     * <br><br>
     * Creates a new session on the server and retains the cookies to re-use the same session for the life
     * of the client (or until the session times out); whichever occurs first.
     *
     * @param baseURL the base URL of the domain tier of Information Server
     * @param authorization the Basic-encoded authorization string to use to login to Information Server
     */
    public IGCRestClient(String baseURL, String authorization) {

        this.baseURL = baseURL;
        this.authorization = authorization;
        this.mapper = new ObjectMapper();
        this.mapper.enableDefaultTyping();

        // Run a simple initial query to obtain a session and setup the cookies
        if (this.baseURL != null && this.authorization != null) {
            IGCSearch igcSearch = new IGCSearch("category");
            igcSearch.addType("term");
            igcSearch.addType("information_governance_policy");
            igcSearch.addType("information_governance_rule");
            igcSearch.setPageSize(1);
            igcSearch.setDevGlossary(true);
            JsonNode response = searchJson(igcSearch);
            this.workflowEnabled = response.path("paging").path("numTotal").asInt(0) > 0;
        }

        // Register the non-generated types
        this.registerPOJO(Paging.class);
        this.registerPOJO(Label.class);

    }

    private HttpHeaders getHttpHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        // If we have cookies already, re-use these (to maintain the same session)
        if (cookies != null) {
            headers.addAll(HttpHeaders.COOKIE, cookies);
        } else { // otherwise re-authenticate by Basic authentication
            String auth = "Basic " + this.authorization;
            headers.add(HttpHeaders.AUTHORIZATION, auth);
        }

        return headers;

    }

    // TODO: would be good to find a way to identify when session times out and automatically re-authenticate
    private void setCookiesFromResponse(ResponseEntity<String> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            HttpHeaders headers = response.getHeaders();
            if (headers.get(HttpHeaders.SET_COOKIE) != null) {
                this.cookies = headers.get(HttpHeaders.SET_COOKIE);
            }
        }
    }

    /**
     * Attempt to convert a JSON structure into a Java object, based on the registered POJOs.
     *
     * @param jsonNode the JSON structure to convert
     * @return Reference - an IGC object
     */
    protected Reference readJSONIntoPOJO(JsonNode jsonNode) {
        Reference reference = null;
        try {
            reference = this.mapper.readValue(jsonNode.toString(), Reference.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reference;
    }

    /**
     * Retrieve the base URL of this IGC REST API connection.
     *
     * @return String
     */
    public String getBaseURL() { return baseURL; }

    /**
     * Utility function to easily encode a username and password to send through as authorization info.
     *
     * @param username username to encode
     * @param password password to encode
     * @return String of appropriately-encoded credentials for authorization
     */
    public static String encodeBasicAuth(String username, String password) {
        return Base64Utils.encodeToString((username + ":" + password).getBytes(UTF_8));
    }

    /**
     * General utility for making requests.
     *
     * @param endpoint the URL against which to make the request
     * @param method HttpMethod (GET, POST, etc)
     * @param payload if POSTing some content, the JSON structure providing what should be POSTed
     * @return JsonNode - JSON structure of the response
     */
    public JsonNode makeRequest(String endpoint, HttpMethod method, JsonNode payload) {
        HttpEntity<String> toSend = new HttpEntity<>(getHttpHeaders());
        if (payload != null) {
            toSend = new HttpEntity<>(payload.toString(), getHttpHeaders());
        }
        ResponseEntity<String> response = new RestTemplate().exchange(
                endpoint,
                method,
                toSend,
                String.class);
        setCookiesFromResponse(response);
        JsonNode jsonNode = null;
        if (response.hasBody()) {
            try {
                jsonNode = mapper.readTree(response.getBody());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonNode;
    }

    /**
     * Retrieves the list of metadata types supported by IGC.
     *
     * @return JsonNode the list of types supported by IGC, as a JSON structure
     */
    public JsonNode getTypes() {
        return makeRequest(baseURL + TYPES, HttpMethod.GET, null);
    }

    /**
     * Retrieve all information about an asset from IGC.
     *
     * @param rid the Repository ID of the asset
     * @return JsonNode - the JSON response of the retrieval
     */
    public JsonNode getJsonAssetById(String rid) {
        return makeRequest(baseURL + ASSET + "/" + rid, HttpMethod.GET, null);
    }

    /**
     * Retrieve all information about an asset from IGC.
     *
     * @param rid the Repository ID of the asset
     * @return Reference - the IGC object representing the asset
     */
    public Reference getAssetById(String rid) {
        return readJSONIntoPOJO(getJsonAssetById(rid));
    }

    /**
     * Retrieve all assets that match the provided search criteria from IGC.
     *
     * @param query the JSON query by which to search
     * @return JsonNode - the first JSON page of results from the search
     */
    public JsonNode searchJson(JsonNode query) {
        return makeRequest(baseURL + SEARCH, HttpMethod.POST, query);
    }

    /**
     * Retrieve all assets that match the provided search criteria from IGC.
     *
     * @param igcSearch the IGCSearch object defining criteria by which to search
     * @return JsonNode - the first JSON page of results from the search
     */
    public JsonNode searchJson(IGCSearch igcSearch) { return searchJson(igcSearch.getQuery()); }

    /**
     * Retrieve all assets that match the provided search criteria from IGC.
     *
     * @param igcSearch search conditions and criteria to use
     * @return ReferenceList - the first page of results from the search
     */
    public ReferenceList search(IGCSearch igcSearch) {
        ReferenceList referenceList = null;
        JsonNode results = searchJson(igcSearch);
        try {
            referenceList = this.mapper.readValue(results.toString(), ReferenceList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return referenceList;
    }

    /**
     * Update the asset specified by the provided RID with the value(s) provided.
     *
     * @param rid the Repository ID of the asset to update
     * @param value the JSON structure defining what value(s) of the asset to update (and mode)
     * @return JsonNode - the JSON structure indicating the updated asset's RID and updates made
     */
    public JsonNode updateJson(String rid, JsonNode value) {
        return makeRequest(baseURL + ASSET, HttpMethod.PUT, value);
    }

    /**
     * Retrieve the next page of results from a set of paging details<br>
     * ... or if there is no next page, return an empty JSON Items set.
     *
     * @param paging the "paging" portion of the JSON response from which to retrieve the next page
     * @return JsonNode - the JSON response of the next page of results
     */
    public JsonNode getNextPage(JsonNode paging) {
        JsonNode nextPage = null;
        try {
            nextPage = mapper.readTree("{\"items\": []}");
            JsonNode nextURL = paging.path("next");
            if (!nextURL.isMissingNode()) {
                String sNextURL = nextURL.asText();
                if (sNextURL != "null") {
                    if (this.workflowEnabled && !sNextURL.contains("workflowMode=draft")) {
                        sNextURL += "&workflowMode=draft";
                    }
                    nextPage = makeRequest(sNextURL, HttpMethod.GET, null);
                    // If the page is part of an ASSET retrieval, we need to strip off the attribute
                    // name of the relationship for proper multi-page composition
                    if (sNextURL.contains(ASSET)) {
                        String remainder = sNextURL.substring((baseURL + ASSET).length() + 2);
                        String attributeName = remainder.substring(remainder.indexOf("/") + 1, remainder.indexOf("?"));
                        nextPage = nextPage.path(attributeName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nextPage;
    }

    /**
     * Retrieve all pages of results from a set of paging details and items<br>
     * ... or if there is no next page, return the items provided.
     *
     * @param items the "items" array of the JSON response for which to retrieve all pages
     * @param paging the "paging" portion of the JSON response for which to retrieve all pages
     * @return JsonNode - the JSON containing all pages of results as an "items" array
     */
    public ArrayNode getAllPages(ArrayNode items, JsonNode paging) {
        ArrayNode allPages = items;
        JsonNode results = getNextPage(paging);
        ArrayNode resultsItems = (ArrayNode) results.path("items");
        if (resultsItems.size() > 0) {
            allPages = getAllPages(items.addAll(resultsItems), results.path("paging"));
        }
        return allPages;
    }

    /**
     * Retrieve the next page of results from a set of Paging details<br>
     * ... or if there is no next page, return an empty JSON Items set.
     *
     * @param paging the "Paging" object from which to retrieve the next page
     * @return ReferenceList - the ReferenceList containing the next page of results
     */
    public ReferenceList getNextPage(Paging paging) {
        JsonNode nextPage = getNextPage(mapper.convertValue(paging, JsonNode.class));
        ReferenceList rlNextPage = null;
        try {
            rlNextPage = this.mapper.readValue(nextPage.toString(), ReferenceList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rlNextPage;
    }

    /**
     * Retrieve all pages of results from a set of Paging details and items<br>
     * ... or if there is no next page, return the items provided.
     *
     * @param items the ArrayList of items for which to retrieve all pages
     * @param paging the Paging object for which to retrieve all pages
     * @return ArrayList - an ArrayList containing all items from all pages of results
     */
    public ArrayList<Reference> getAllPages(ArrayList<Reference> items, Paging paging) {
        ArrayList<Reference> allPages = items;
        ReferenceList results = getNextPage(paging);
        ArrayList<Reference> resultsItems = results.getItems();
        if (resultsItems.size() > 0) {
            // NOTE: this ordering of addAll is important, to avoid side-effecting the original set of items
            resultsItems.addAll(allPages);
            allPages = getAllPages(resultsItems, results.getPaging());
        }
        return allPages;
    }

    /**
     * Disconnect from IGC REST API and invalidate the session.
     */
    public void disconnect() {
        makeRequest(baseURL + LOGOUT, HttpMethod.GET, null);
    }

    /**
     * Disables SSL verification, to allow self-signed certificates.
     */
    public static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register a POJO as an object to handle serde of JSON objects.<br>
     * Note that this MUST be done BEFORE any object mappingRemoved (translation) is done!
     * <br><br>
     * In general, you'll want your POJO to extend at least the model.Reference
     * object in this package; more likely the model.MainObject (for your own OpenIGC object),
     * or if you are adding custom attributes to one of the native asset types, consider
     * directly extending that asset from model.generated.*
     * <br><br>
     * To allow this dynamic registration to work, also ensure you have a 'static final String IGC_TYPE_ID'
     * in your class set to the type that the IGC REST API uses to refer to the asset (eg. for Term.class
     * it would be "term"). See the generated POJOs for examples.
     *
     * @param clazz the Java Class (POJO) object to register
     */
    public void registerPOJO(Class clazz) {
        try {
            // Every POJO should have a static final IGC_TYPE_ID giving its IGC asset type
            String typeId = (String) clazz.getDeclaredField("IGC_TYPE_ID").get(null);
            this.mapper.registerSubtypes(new NamedType(clazz, typeId));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns true iff the workflow is enabled in the environment against which the REST connection is defined.
     *
     * @return Boolean
     */
    public Boolean isWorkflowEnabled() {
        return this.workflowEnabled;
    }

}
