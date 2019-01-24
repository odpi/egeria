/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Paging;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
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

    private static final Logger log = LoggerFactory.getLogger(IGCRestClient.class);

    private String authorization;
    private String baseURL;
    private Boolean workflowEnabled = false;
    private List<String> cookies = null;

    private IGCVersionEnum igcVersion;
    private HashMap<String, Class> registeredPojosByType;

    private int defaultPageSize = 100;

    private ObjectMapper mapper;

    public static final String EP_TYPES = "/ibm/iis/igc-rest/v1/types";
    public static final String EP_ASSET = "/ibm/iis/igc-rest/v1/assets";
    public static final String EP_SEARCH = "/ibm/iis/igc-rest/v1/search";
    public static final String EP_LOGOUT  = "/ibm/iis/igc-rest/v1/logout";
    public static final String EP_BUNDLES = "/ibm/iis/igc-rest/v1/bundles";
    public static final String EP_BUNDLE_ASSETS = EP_BUNDLES + "/assets";

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
        this.registeredPojosByType = new HashMap<>();

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

        // Start with lowest version supported
        this.igcVersion = IGCVersionEnum.values()[0];
        ArrayNode igcTypes = getTypes();
        for (JsonNode node : igcTypes) {
            // Check for a type that does not exist in the lowest version supported against higher versions, and if found
            // set our version to that higher version
            String assetType = node.path("_id").asText();
            for (IGCVersionEnum aVersion : IGCVersionEnum.values()) {
                if (aVersion.isHigherThan(this.igcVersion)
                        && assetType.equals(aVersion.getTypeNameFirstAvailableInThisVersion())) {
                    this.igcVersion = aVersion;
                }
            }
        }
        log.info("Detected IGC version: {}", this.igcVersion.getVersionString());

    }

    /**
     * Setup the HTTP headers of a request based on either session reuse (forceLogin = false) or forcing a new
     * session (forceLogin = true).
     *
     * @param forceLogin indicates whether to create a new session by forcing login (true), or reuse existing session (false)
     * @return
     */
    private HttpHeaders getHttpHeaders(boolean forceLogin) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        // If we have cookies already, and haven't been asked to force the login,
        // re-use these (to maintain the same session)
        if (cookies != null && !forceLogin) {
            headers.addAll(HttpHeaders.COOKIE, cookies);
        } else { // otherwise re-authenticate by Basic authentication
            String auth = "Basic " + this.authorization;
            headers.add(HttpHeaders.AUTHORIZATION, auth);
        }

        return headers;

    }

    /**
     * Attempts to open a new session while sending the provided request. If the alreadyTriedNewSession is true,
     * and we are unable to open a new session with this attempt, will give up. If the alreadyTriedNewSession is false,
     * will attempt to re-send this request to open a new session precisely once before giving up.
     *
     * @param endpoint the endpoint to which to send the request
     * @param method the HTTP method to use in sending the request
     * @param contentType the type of content to expect in the payload (if any)
     * @param payload the payload (if any) for the request
     * @param alreadyTriedNewSession indicates whether a new session was already attempted (true) or not (false)
     * @return ResponseEntity<String>
     */
    private ResponseEntity<String> openNewSessionWithRequest(String endpoint,
                                                             HttpMethod method,
                                                             MediaType contentType,
                                                             String payload,
                                                             boolean alreadyTriedNewSession) {
        if (alreadyTriedNewSession) {
            log.error("Opening a new session already attempted without success -- giving up on {} to {} with {}", method, endpoint, payload);
            return null;
        } else {
            log.info("Session appears to have timed out -- starting a new session and re-trying the request.");
            // By removing cookies, we'll force a login
            this.cookies = null;
            return makeRequest(endpoint, method, contentType, payload, true);
        }
    }

    /**
     * Attempts to open a new session while uploading the provided file. If the alreadyTriedNewSession is true,
     * and we are unable to open a new session with this attempt, will give up. If the alreadyTriedNewSession is false,
     * will attempt to re-upload the file to open a new session precisely once before giving up.
     *
     * @param endpoint the endpoint to which to upload the file
     * @param method the HTTP method to use in sending the request
     * @param filePath the location of the file on the local filesystem
     * @param alreadyTriedNewSession indicates whether a new session was already attempted (true) or not (false)
     * @return ResponseEntity<String>
     */
    private ResponseEntity<String> openNewSessionWithUpload(String endpoint,
                                                            HttpMethod method,
                                                            String filePath,
                                                            boolean alreadyTriedNewSession) {
        if (alreadyTriedNewSession) {
            log.error("Opening a new session already attempted without success -- giving up on {} to {} with {}", method, endpoint, filePath);
            return null;
        } else {
            log.info("Session appears to have timed out -- starting a new session and re-trying the upload.");
            // By removing cookies, we'll force a login
            this.cookies = null;
            return uploadFile(endpoint, method, filePath, true);
        }
    }

    /**
     * Adds the cookies from a response into subsequent headers, so that we re-use the session indicated by those
     * cookies.
     *
     * @param response the response from which to obtain the cookies
     */
    private void setCookiesFromResponse(ResponseEntity<String> response) {

        // If we had a successful response, setup the cookies
        if (response.getStatusCode() == HttpStatus.OK) {
            HttpHeaders headers = response.getHeaders();
            if (headers.get(HttpHeaders.SET_COOKIE) != null) {
                this.cookies = headers.get(HttpHeaders.SET_COOKIE);
            }
        } else {
            log.error("Unable to make request or unexpected status: {}", response.getStatusCode());
        }

    }

    /**
     * Attempt to convert a JSON structure into a Java object, based on the registered POJOs.
     *
     * @param jsonNode the JSON structure to convert
     * @return Reference - an IGC object
     */
    protected Reference readJSONIntoPOJO(JsonNode jsonNode) {
        return readJSONIntoPOJO(jsonNode.toString());
    }

    /**
     * Attempt to convert the JSON string into a Java object, based on the registered POJOs.
     *
     * @param json the JSON string to convert
     * @return Reference - an IGC object
     */
    public Reference readJSONIntoPOJO(String json) {
        Reference reference = null;
        try {
            reference = this.mapper.readValue(json, Reference.class);
        } catch (IOException e) {
            log.error("Unable to translate JSON into POJO: {}", json, e);
        }
        return reference;
    }

    /**
     * Attempt to convert the JSON string into a ReferenceList.
     *
     * @param json the JSON string to convert
     * @return ReferenceList
     */
    public ReferenceList readJSONIntoReferenceList(String json) {
        ReferenceList referenceList = null;
        try {
            referenceList = this.mapper.readValue(json, ReferenceList.class);
        } catch (IOException e) {
            log.error("Unable to translate JSON into ReferenceList: {}", json, e);
        }
        return referenceList;
    }

    /**
     * Attempt to convert the provided IGC object into JSON, based on the registered POJOs.
     *
     * @param asset the IGC asset to convert
     * @return String of JSON representing the asset
     */
    public String getValueAsJSON(Reference asset) {
        String payload = null;
        try {
            payload = this.mapper.writeValueAsString(asset);
        } catch (JsonProcessingException e) {
            log.error("Unable to translate asset into JSON: {}", asset, e);
        }
        return payload;
    }

    /**
     * Retrieve the version of the IGC environment (static member VERSION_115 or VERSION_117).
     *
     * @return IGCVersionEnum
     */
    public IGCVersionEnum getIgcVersion() { return igcVersion; }

    /**
     * Retrieve the base URL of this IGC REST API connection.
     *
     * @return String
     */
    public String getBaseURL() { return baseURL; }

    /**
     * Retrieve the default page size for this IGC REST API connection.
     *
     * @return int
     */
    public int getDefaultPageSize() { return defaultPageSize; }

    /**
     * Set the default page size for this IGC REST API connection.
     *
     * @param pageSize the new default page size to use
     */
    public void setDefaultPageSize(int pageSize) { this.defaultPageSize = pageSize; }

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
     * Internal utility for making potentially repeat requests (if session expires and needs to be re-opened),
     * to upload a file to a given endpoint.
     *
     * @param endpoint the URL against which to POST the upload
     * @param filePath the location of the file on the local filesystem
     * @param forceLogin a boolean indicating whether login should be forced (true) or session reused (false)
     * @return ResponseEntity<String>
     */
    private ResponseEntity<String> uploadFile(String endpoint, HttpMethod method, String filePath, boolean forceLogin) {

        HttpHeaders headers = getHttpHeaders(forceLogin);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ResponseEntity<String> response;
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(filePath));

        HttpEntity<MultiValueMap<String, Object>> toSend = new HttpEntity<>(body, headers);

        try {
            response = new RestTemplate().exchange(
                    endpoint,
                    method,
                    toSend,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            // If the response was forbidden (fails with exception), the session may have expired -- create a new one
            response = openNewSessionWithUpload(
                    endpoint,
                    method,
                    filePath,
                    forceLogin
            );
        }

        return response;

    }

    /**
     * General utility for uploading binary files.
     *
     * @param endpoint the URL against which to upload the file
     * @param method HttpMethod (POST, PUT, etc)
     * @param filePath the location of the file on the local filesystem
     * @return boolean - indicates success (true) or failure (false)
     */
    public boolean uploadFile(String endpoint, HttpMethod method, String filePath) {
        ResponseEntity<String> response = uploadFile(endpoint, method, filePath, false);
        return (response.getStatusCode() == HttpStatus.OK);
    }

    /**
     * Internal utility for making potentially repeat requests (if session expires and needs to be re-opened).
     *
     * @param endpoint the URL against which to make the request
     * @param method HttpMethod (GET, POST, etc)
     * @param contentType the type of content to expect in the payload (if any)
     * @param payload if POSTing some content, the JSON structure providing what should be POSTed
     * @param forceLogin a boolean indicating whether login should be forced (true) or session reused (false)
     * @return ResponseEntity<String>
     */
    private ResponseEntity<String> makeRequest(String endpoint,
                                               HttpMethod method,
                                               MediaType contentType,
                                               String payload,
                                               boolean forceLogin) {
        HttpHeaders headers = getHttpHeaders(forceLogin);
        HttpEntity<String> toSend;
        if (payload != null) {
            headers.setContentType(contentType);
            toSend = new HttpEntity<>(payload, headers);
        } else {
            toSend = new HttpEntity<>(headers);
        }
        ResponseEntity<String> response;
        try {
            log.debug("{}ing to {} with: {}", method, endpoint, payload);
            response = new RestTemplate().exchange(
                    endpoint,
                    method,
                    toSend,
                    String.class);
            setCookiesFromResponse(response);
        } catch (HttpClientErrorException e) {
            // If the response was forbidden (fails with exception), the session may have expired -- create a new one
            response = openNewSessionWithRequest(
                    endpoint,
                    method,
                    contentType,
                    payload,
                    forceLogin
            );
        }
        return response;
    }

    /**
     * General utility for making requests.
     *
     * @param endpoint the URL against which to make the request
     * @param method HttpMethod (GET, POST, etc)
     * @param contentType the type of content to expect in the payload (if any)
     * @param payload if POSTing some content, the JSON structure providing what should be POSTed
     * @return JsonNode - JSON structure of the response
     */
    public JsonNode makeRequest(String endpoint, HttpMethod method, MediaType contentType, String payload) {
        ResponseEntity<String> response = makeRequest(
                endpoint,
                method,
                contentType,
                payload,
                false
        );
        JsonNode jsonNode = null;
        if (response == null) {
            log.error("Unable to make request -- are you certain the authentication details for IGC are correct?");
        } else if (response.hasBody()) {
            try {
                jsonNode = mapper.readTree(response.getBody());
            } catch (IOException e) {
                log.error("Unable to read JSON response body when {} to {} with {}", method, endpoint, payload, e);
            }
        }
        return jsonNode;
    }

    /**
     * Retrieves the list of metadata types supported by IGC.
     *
     * @return ArrayNode the list of types supported by IGC, as a JSON structure
     */
    public ArrayNode getTypes() {
        return (ArrayNode) makeRequest(baseURL + EP_TYPES, HttpMethod.GET, null,null);
    }

    /**
     * Retrieve all information about an asset from IGC.
     *
     * @param rid the Repository ID of the asset
     * @return JsonNode - the JSON response of the retrieval
     */
    public JsonNode getJsonAssetById(String rid) {
        return makeRequest(baseURL + EP_ASSET + "/" + rid, HttpMethod.GET, null,null);
    }

    /**
     * Retrieve all information about an asset from IGC.
     * This can be an expensive operation that may retrieve far more information than you actually need.
     *
     * @see #getAssetRefById(String)
     *
     * @param rid the Repository ID of the asset
     * @return Reference - the IGC object representing the asset
     */
    public Reference getAssetById(String rid) {
        return readJSONIntoPOJO(getJsonAssetById(rid));
    }

    /**
     * Retrieve only the minimal unique properties of an asset from IGC.
     * This will generally be the most performant way to see that an asset exists and get its identifying characteristics.
     *
     * @param rid the Repository ID of the asset
     * @return Reference - the minimalistic IGC object representing the asset
     */
    public Reference getAssetRefById(String rid) {

        // We can search for any object by ID by using "main_object" as the type
        // (no properties needed)
        IGCSearchCondition condition = new IGCSearchCondition(
                "_id",
                "=",
                rid
        );
        IGCSearchConditionSet conditionSet = new IGCSearchConditionSet(condition);
        IGCSearch igcSearch = new IGCSearch("main_object", conditionSet);
        // Add non-main_object types that might also be looked-up by RID
        igcSearch.addType("classification");
        igcSearch.addType("label");
        igcSearch.addType("user");
        igcSearch.addType("group");
        ReferenceList results = search(igcSearch);
        Reference reference = null;
        if (results.getPaging().getNumTotal() > 0) {
            if (results.getPaging().getNumTotal() > 1) {
                log.warn("Found multiple assets for RID {}, taking only the first.", rid);
            }
            reference = results.getItems().get(0);
        }

        return reference;

    }

    /**
     * Retrieve all assets that match the provided search criteria from IGC.
     *
     * @param query the JSON query by which to search
     * @return JsonNode - the first JSON page of results from the search
     */
    public JsonNode searchJson(JsonNode query) {
        return makeRequest(baseURL + EP_SEARCH, HttpMethod.POST, MediaType.APPLICATION_JSON, query.toString());
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
            log.error("Unable to translate JSON results: {}", results, e);
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
        return makeRequest(baseURL + EP_ASSET, HttpMethod.PUT, MediaType.APPLICATION_JSON, value.toString());
    }

    /**
     * Upload the specified bundle, creating it if it does not already exist or updating it if it does.
     *
     * @param name the bundleId of the bundle
     * @param filePath the location of the zip file containing the bundle
     * @return boolean - indication of success (true) or failure (false)
     */
    public boolean upsertOpenIgcBundle(String name, String filePath) {
        boolean success;
        List<String> existingBundles = getOpenIgcBundles();
        if (existingBundles.contains(name)) {
            success = uploadFile(baseURL + EP_BUNDLES, HttpMethod.PUT, filePath);
        } else {
            success = uploadFile(baseURL + EP_BUNDLES, HttpMethod.POST, filePath);
        }
        return success;
    }

    /**
     * Generates an OpenIGC bundle zip file from the provided directory path, and returns the temporary file it creates.
     *
     * @param directory the directory under which the OpenIGC bundle is defined (ie. including an
     *                  'asset_type_descriptor.xml', an 'i18n' subdirectory and an 'icons' subdirectory)
     * @return File - the temporary zip file containing the bundle
     */
    public File createOpenIgcBundleFile(File directory) {

        File bundle = null;
        try {
            bundle = File.createTempFile("openigc", "zip");
        } catch (IOException e) {
            log.error("Unable to create temporary file needed for OpenIGC bundle from directory: {}", directory, e);
        }
        if (bundle != null) {
            try (
                    FileOutputStream bundleOut = new FileOutputStream(bundle);
                    ZipOutputStream zipOutput = new ZipOutputStream(bundleOut)
            ) {
                if (!directory.isDirectory()) {
                    log.error("Provided bundle location is not a directory: {}", directory);
                } else {
                    recursivelyZipFiles(directory, "", zipOutput);
                }

            } catch (IOException e) {
                log.error("Unable to create temporary file needed for OpenIGC bundle from directory: {}", directory, e);
            }
        }
        return bundle;

    }

    /**
     * Recursively traverses the provided file to build up a zip file output in the provided ZipOutputStream.
     *
     * @param file the file from which to recursively process
     * @param name the name of the file from which to recursively process
     * @param zipOutput the zip output stream into which to write the entries
     */
    private void recursivelyZipFiles(File file, String name, ZipOutputStream zipOutput) {

        if (file.isDirectory()) {

            // Make sure the directory name ends with a separator
            String directoryName = name;
            if (!directoryName.equals("")) {
                directoryName = directoryName.endsWith(File.separator) ? directoryName : directoryName + File.separator;
            }

            // Create an entry in the zip file for the directory, then recurse on the files within it
            try {
                if (!directoryName.equals("")) {
                    zipOutput.putNextEntry(new ZipEntry(directoryName));
                }
                File[] files = file.listFiles();
                for (File subFile : files) {
                    recursivelyZipFiles(subFile, directoryName + subFile.getName(), zipOutput);
                }
            } catch (IOException e) {
                log.error("Unable to create directory entry in zip file for {}.", directoryName, e);
            }

        } else {

            try (FileInputStream fileInput = new FileInputStream(file)) {
                // Add an entry for the file into the zip file, and write its bytes into the zipfile output
                zipOutput.putNextEntry(new ZipEntry(name));
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fileInput.read(buffer)) >= 0) {
                    zipOutput.write(buffer, 0, length);
                }
            } catch (FileNotFoundException e) {
                log.error("Unable to find file: {}", file, e);
            } catch (IOException e) {
                log.error("Unable to read/write file: {}", file, e);
            }

        }

    }

    /**
     * Retrieve the set of OpenIGC bundles already defined in the environment.
     *
     * @return List<String>
     */
    public List<String> getOpenIgcBundles() {
        JsonNode bundles = makeRequest(baseURL + EP_BUNDLES, HttpMethod.GET, null,null);
        ArrayList<String> alBundles = new ArrayList<>();
        try {
            String[] aBundles = mapper.readValue(bundles.toString(), String[].class);
            for (String bundleName : aBundles) {
                alBundles.add(bundleName);
            }
        } catch (IOException e) {
            log.error("Unable to parse bundle response: {}", bundles.toString(), e);
        }
        return alBundles;
    }

    /**
     * Upload the provided OpenIGC asset XML: creating the asset(s) if they do not exist, updating if they do.
     *
     * @param assetXML the XML string defining the OpenIGC asset
     * @return JsonNode - the JSON structure indicating the updated assets' RID(s)
     */
    public JsonNode upsertOpenIgcAsset(String assetXML) {
        return makeRequest(baseURL + EP_BUNDLE_ASSETS, HttpMethod.POST, MediaType.APPLICATION_XML, assetXML);
    }

    /**
     * Delete using the provided OpenIGC asset XML: deleting the asset(s) specified within it.
     *
     * @param assetXML the XML string defining the OpenIGC asset deletion
     * @return boolean - true on success, false on failure
     */
    public boolean deleteOpenIgcAsset(String assetXML) {
        return (makeRequest(baseURL + EP_BUNDLE_ASSETS, HttpMethod.DELETE, MediaType.APPLICATION_XML, assetXML) == null);
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
                    nextPage = makeRequest(sNextURL, HttpMethod.GET, null, null);
                    // If the page is part of an ASSET retrieval, we need to strip off the attribute
                    // name of the relationship for proper multi-page composition
                    if (sNextURL.contains(EP_ASSET)) {
                        String remainder = sNextURL.substring((baseURL + EP_ASSET).length() + 2);
                        String attributeName = remainder.substring(remainder.indexOf('/') + 1, remainder.indexOf('?'));
                        nextPage = nextPage.path(attributeName);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Unable to parse next page from JSON: {}", paging, e);
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
            log.error("Unable to parse next page from JSON: {}", paging, e);
        }
        return rlNextPage;
    }

    /**
     * Retrieve all pages of results from a set of Paging details and items<br>
     * ... or if there is no next page, return the items provided.
     *
     * @param items the List of items for which to retrieve all pages
     * @param paging the Paging object for which to retrieve all pages
     * @return List - an List containing all items from all pages of results
     */
    public List<Reference> getAllPages(List<Reference> items, Paging paging) {
        List<Reference> allPages = items;
        ReferenceList results = getNextPage(paging);
        List<Reference> resultsItems = results.getItems();
        if (!resultsItems.isEmpty()) {
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
        makeRequest(baseURL + EP_LOGOUT, HttpMethod.GET, null,null);
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
     * To allow this dynamic registration to work, also ensure you have a 'public static String getIgcTypeId()'
     * in your class set to the type that the IGC REST API uses to refer to the asset (eg. for Term.class
     * it would be "term"). See the generated POJOs for examples.
     *
     * @param clazz the Java Class (POJO) object to register
     * @see #getPOJOForType(String)
     */
    public void registerPOJO(Class clazz) {
        try {
            // Every POJO should have a public static getIgcTypeId method giving its IGC asset type
            Method getTypeId = clazz.getMethod("getIgcTypeId");
            String typeId = (String) getTypeId.invoke(null);
            this.mapper.registerSubtypes(new NamedType(clazz, typeId));
            this.registeredPojosByType.put(typeId, clazz);
            log.debug("Registered IGC type {} to be handled by handle class: {}", typeId, clazz.getCanonicalName());
        } catch (NoSuchMethodException e) {
            log.error("Unable to find 'getIgcTypeId' method on class: {}", clazz.getCanonicalName(), e);
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error("Unable to access or invoke 'getIgcTypeId' method on class: {}", clazz.getCanonicalName(), e);
        }
    }

    /**
     * Returns the POJO that is registered to serde the provided IGC asset type.
     * <br><br>
     * Note that the POJO must first be registered via registerPOJO!
     *
     * @param typeName name of the IGC asset
     * @return Class
     * @see #registerPOJO(Class)
     */
    public Class getPOJOForType(String typeName) {
        return this.registeredPojosByType.get(typeName);
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
