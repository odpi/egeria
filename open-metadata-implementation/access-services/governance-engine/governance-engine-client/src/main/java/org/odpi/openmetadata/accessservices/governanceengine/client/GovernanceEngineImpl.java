/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * The Governance Engine Open Metadata Access Service (OMAS) provides an interface to support a policy engine
 * such as Apache Ranger. See interface definition for more explanation
 */
public class GovernanceEngineImpl implements GovernanceEngineClient {

    private String serverName;  /* Initialized in constructor */
    private String omasServerURL;  /* Initialized in constructor */
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineImpl.class);


    /**
     * Create a new GovernanceEngine client.
     *
     * @param serverName - the network address of the process running the OMAS REST servers
     * @param newServerURL - the network address of the process running the OMAS REST servers
     */
    public GovernanceEngineImpl(String serverName,
                                String newServerURL) {
        this.serverName = serverName;
        this.omasServerURL = newServerURL;
        restTemplate = new RestTemplate() ;
    }

    /**
     * @param userId                 - String - userId of user making request.
     * @param classification - String - name of base classification type
     * @param type               - String - root asset type
     * @return governedAssetList                    - map of classification
     * @throws InvalidParameterException           - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException          - the requesting user is not authorized to issue this request.
     * @throws ClassificationNotFoundException - the classification to scope search is not found
     * @throws TypeNotFoundException      - the classification to scope search is not found
     * @throws MetadataServerException             - A failure occurred communicating with the metadata repository
     */
    public List<GovernedAsset> getGovernedAssetList(String userId, String classification,
                                                    String type) throws InvalidParameterException,
            UserNotAuthorizedException, MetadataServerException, ClassificationNotFoundException,
            TypeNotFoundException {

        final String methodName = "getGovernedAssetList";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/assets?classification={2}&type={3}";

        log.debug("Calling method: " + methodName);


        validateOMASServerURL(methodName);
        validateUserId(userId, methodName); // cannot be null
        // No validation for other parms -- optional. Managed server-side

        GovernedAssetListAPIResponse restResult = callGovernedAssetListREST(methodName,
                omasServerURL + urlTemplate,
                serverName,
                userId,
                type,
                classification);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowClassificationNotFoundException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowTypeNotFoundException(methodName, restResult);

        return restResult.getGovernedAssetList();
    }

    /**
     * @param userId                 - String - userId of user making request.
     * @param classification - String - name of base classification type
     * @return AssetTagDefinitions                  - Tag definitions
     * @throws InvalidParameterException           - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException          - the requesting user is not authorized to issue this request.
     * @throws MetadataServerException             - A failure occurred communicating with the metadata repository
     * @throws ClassificationNotFoundException - the classification to scope search is not found
     */
    public List<GovernanceClassificationDef> getGovernanceClassificationDefList(String userId, String classification) throws InvalidParameterException,
            UserNotAuthorizedException, MetadataServerException, ClassificationNotFoundException {
        final String methodName = "getGovernanceClassificationDefList";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/classificationdefs?classification={2}";

        log.debug("Calling method: " + methodName);


        validateOMASServerURL(methodName);
        validateUserId(userId, methodName); // cannot be null

        GovernanceClassificationDefListAPIResponse restResult = callClassificationDefListREST(methodName,
                omasServerURL + urlTemplate,
                serverName,
                userId,
                classification);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowClassificationNotFoundException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);

        return (restResult.getTagList());
    }

    /**
     * @param userId             - String - userId of user making request.
     * @param assetGuid - String - guid of asset component
     * @return AssetTagMap                  - map of classification
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     * @throws MetadataServerException    - A failure occurred communicating with the metadata repository
     * @throws GuidNotFoundException      - the guid is not found
     */
    public GovernedAsset getGovernedAsset(String userId, String assetGuid) throws InvalidParameterException,
            UserNotAuthorizedException, MetadataServerException, GuidNotFoundException {
        final String methodName = "getGovernedAssetList";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/assets/{2}";

        log.debug("Calling method: " + methodName);


        validateOMASServerURL(methodName);
        validateUserId(userId, methodName); // cannot be null

        validateGuid(assetGuid, methodName); // cannot be null

        GovernedAssetAPIResponse restResult = callGovernedAssetREST(methodName,
                omasServerURL + urlTemplate,
                serverName,
                userId,
                assetGuid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowGuidNotFoundException(methodName, restResult);

        return restResult.getAsset();
    }

    /**
     * @param userId  - String - userId of user making request.
     * @param classificationGuid - String - classification guid
     * @return AssetTagDefinitions          - Tag definitions
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     * @throws MetadataServerException    - A failure occurred communicating with the metadata repository
     * @throws GuidNotFoundException      - the guid is not found
     */
    public GovernanceClassificationDef getGovernanceClassificationDef(String userId, String classificationGuid) throws InvalidParameterException,
            UserNotAuthorizedException, MetadataServerException, GuidNotFoundException {
        final String methodName = "getGovernanceClassificationDef";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/assets?classification={2}"; //TODO: Need to allow for root
        // classification to be speced and do validation

        log.debug("Calling method: " + methodName);

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName); // cannot be null

        validateGuid(classificationGuid, methodName); // cannot be null


        GovernanceClassificationDefAPIResponse restResult = callClassificationDefREST(methodName,
                omasServerURL + urlTemplate,
                serverName,
                userId,
                classificationGuid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowGuidNotFoundException(methodName, restResult);

        return (restResult.getGovernanceClassificationDef());
    }

    /**
     * Throw an exception if the OMASServerURL (from constructor ) is empty or null
     *
     * @param methodName - name of the method making the call.
     * @throws InvalidParameterException - the OMASServerURL is null
     */
    private void validateOMASServerURL(String methodName) throws InvalidParameterException {
        if (StringUtils.isEmpty(omasServerURL)) {
            /*
             * It is not possible to retrieve anything without knowledge of where the OMAS Server is located.
             */
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.SERVER_URL_NOT_SPECIFIED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    /**
     * Throw an exception if the supplied userId is empty or null
     *
     * @param userId     - user name to validate
     * @param methodName - name of the method making the call.
     * @throws InvalidParameterException - the userId is null
     */
    private void validateUserId(String userId,
                                String methodName) throws InvalidParameterException {
        if (StringUtils.isEmpty(userId)) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.EMPTY_USER_ID;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    /**
     * Throw an exception if the supplied guid is empty or null
     *
     * @param guid       - user name to validate
     * @param methodName - name of the method making the call.
     * @throws InvalidParameterException - the userId is null
     */
    private void validateGuid(String guid,
                              String methodName) throws InvalidParameterException {
        if (StringUtils.isEmpty(guid)) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.NULL_GUID;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    /**
     * Issue a GET REST call that returns a GovernanceClassificationDefListAPIResponse object.
     *
     * @param methodName  - name of the method being called
     * @param urlTemplate - template of the URL for the REST API call with place-holders for the parameters
     * @param params      - a list of parameters that are slotted into the url template
     * @return GovernanceClassificationDefListAPIResponse    - List of classification definitions
     * @throws MetadataServerException - something went wrong with the REST call stack.
     */
    private GovernanceClassificationDefListAPIResponse callClassificationDefListREST(String methodName,
                                                                                     String urlTemplate,
                                                                                     Object... params) throws MetadataServerException {
        GovernanceClassificationDefListAPIResponse restResult = new GovernanceClassificationDefListAPIResponse();

        /*
         * Issue the request
         */
        try {

            // - (Class member to support mocking) RestTemplate restTemplate = new RestTemplate();

            restResult = restTemplate.getForObject(urlTemplate, restResult.getClass(), params);
        } catch (Throwable error) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    omasServerURL,
                    error.getMessage());

            throw new MetadataServerException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    error);
        }

        return restResult;
    }

    /**
     * Issue a GET REST call that returns a GovernanceClassificationDefAPIResponse object.
     *
     * @param methodName  - name of the method being called
     * @param urlTemplate - template of the URL for the REST API call with place-holders for the parameters
     * @param params      - a list of parameters that are slotted into the url template
     * @return GovernanceClassificationDefAPIResponse        - classification definition
     * @throws MetadataServerException - something went wrong with the REST call stack.
     */
    private GovernanceClassificationDefAPIResponse callClassificationDefREST(String methodName,
                                                                             String urlTemplate,
                                                                             Object... params) throws MetadataServerException {
        GovernanceClassificationDefAPIResponse restResult = new GovernanceClassificationDefAPIResponse();

        /*
         * Issue the request
         */
        try {
            // - (Class member to support mocking) RestTemplate restTemplate = new RestTemplate();

            restResult = restTemplate.getForObject(urlTemplate, restResult.getClass(), params);
        } catch (Throwable error) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    omasServerURL,
                    error.getMessage());

            throw new MetadataServerException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    error);
        }

        return restResult;
    }

    /**
     * Issue a GET REST call that returns a GovernedAssetListAPIResponse object.
     *
     * @param methodName  - name of the method being called
     * @param urlTemplate - template of the URL for the REST API call with place-holders for the parameters
     * @param params      - a list of parameters that are slotted into the url template
     * @return GovernedAssetListAPIResponse    - list of governed asset components
     * @throws MetadataServerException - something went wrong with the REST call stack.
     */
    private GovernedAssetListAPIResponse callGovernedAssetListREST(String methodName,
                                                                   String urlTemplate,
                                                                   Object... params) throws MetadataServerException {
        GovernedAssetListAPIResponse restResult = new GovernedAssetListAPIResponse();

        /*
         * Issue the request
         */
        try {

            // - (Class member to support mocking) RestTemplate restTemplate = new RestTemplate();

            restResult = restTemplate.getForObject(urlTemplate, restResult.getClass(), params);
        } catch (Throwable error) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    omasServerURL,
                    error.getMessage());

            throw new MetadataServerException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    error);
        }

        return restResult;
    }

    /**
     * Issue a GET REST call that returns a GovernedAssetAPIResponse object.
     *
     * @param methodName  - name of the method being called
     * @param urlTemplate - template of the URL for the REST API call with place-holders for the parameters
     * @param params      - a list of parameters that are slotted into the url template
     * @return GovernedAssetAPIResponse    - the governed asset component
     * @throws MetadataServerException - something went wrong with the REST call stack.
     */
    private GovernedAssetAPIResponse callGovernedAssetREST(String methodName,
                                                           String urlTemplate,
                                                           Object... params) throws MetadataServerException {
        GovernedAssetAPIResponse restResult = new GovernedAssetAPIResponse();

        /*
         * Issue the request
         */
        try {

            // move to class member to support mocking - https://stackoverflow.com/questions/42406625/how-to-mock-resttemplate-in-java-spring
            // RestTemplate restTemplate = new RestTemplate();

            restResult = restTemplate.getForObject(urlTemplate, restResult.getClass(), params);
        } catch (Throwable error) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    omasServerURL,
                    error.getMessage());

            throw new MetadataServerException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    error);
        }

        return restResult;
    }

    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResult - response from the rest call.  This generated in the remote handlers.
     * @throws InvalidParameterException - encoded exception from the handlers
     */
    private void detectAndThrowInvalidParameterException(String methodName,
                                                         GovernanceEngineOMASAPIResponse restResult) throws InvalidParameterException {
        final String exceptionClassName = InvalidParameterException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName()))) {
            throw new InvalidParameterException(restResult.getRelatedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    restResult.getExceptionErrorMessage(),
                    restResult.getExceptionSystemAction(),
                    restResult.getExceptionUserAction());
        }
    }

    /**
     * Throw an GuidNotFoundException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResult - response from the rest call.  This generated in the remote handlers.
     * @throws GuidNotFoundException - encoded exception from the handlers
     */
    private void detectAndThrowGuidNotFoundException(String methodName,
                                                     GovernanceEngineOMASAPIResponse restResult) throws GuidNotFoundException {
        final String exceptionClassName = GuidNotFoundException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName()))) {
            throw new GuidNotFoundException(restResult.getRelatedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    restResult.getExceptionErrorMessage(),
                    restResult.getExceptionSystemAction(),
                    restResult.getExceptionUserAction());
        }
    }


    /**
     * Throw a ClassificationNotFoundException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResult - response from the rest call.  This generated in the remote handlers.
     * @throws ClassificationNotFoundException - encoded exception from the handlers
     */
    private void detectAndThrowClassificationNotFoundException(String methodName,
                                                               GovernanceEngineOMASAPIResponse restResult) throws ClassificationNotFoundException {
        final String exceptionClassName = ClassificationNotFoundException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName()))) {
            throw new ClassificationNotFoundException(restResult.getRelatedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    restResult.getExceptionErrorMessage(),
                    restResult.getExceptionSystemAction(),
                    restResult.getExceptionUserAction());
        }
    }

    /**
     * Throw a TypeNotFoundException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResult - response from the rest call.  This generated in the remote handlers.
     * @throws TypeNotFoundException - encoded exception from the handlers
     */
    private void detectAndThrowTypeNotFoundException(String methodName,
                                                     GovernanceEngineOMASAPIResponse restResult) throws TypeNotFoundException {
        final String exceptionClassName = TypeNotFoundException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName()))) {
            throw new TypeNotFoundException(restResult.getRelatedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    restResult.getExceptionErrorMessage(),
                    restResult.getExceptionSystemAction(),
                    restResult.getExceptionUserAction());
        }
    }

    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResult - response from UserNotAuthorizedException - encoded exception from the handlers
     * @throws UserNotAuthorizedException - encoded exception from the handlers
     */
    private void detectAndThrowUserNotAuthorizedException(String methodName,
                                                          GovernanceEngineOMASAPIResponse restResult) throws UserNotAuthorizedException {
        final String exceptionClassName = UserNotAuthorizedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName()))) {
            throw new UserNotAuthorizedException(restResult.getRelatedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    restResult.getExceptionErrorMessage(),
                    restResult.getExceptionSystemAction(),
                    restResult.getExceptionUserAction());
        }
    }

}
