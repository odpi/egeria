/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.client;


import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.RootClassificationNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * The Governance Engine Open Metadata Access Service (OMAS) provides an interface to support a policy engine such as Ranger
 */
public class GovernanceEngineImpl implements GovernanceEngineClient {
    private String omasServerURL;  /* Initialized in constructor */


    // Use Dependency Injection to aid in testing

    RestTemplate restTemplate;// = new RestTemplate();

    /**
     * Create a new GovernanceEngine client.
     *
     * @param newServerURL - the network address of the server running the OMAS REST servers
     */
    public GovernanceEngineImpl(String newServerURL) {
        omasServerURL = newServerURL;
        restTemplate = new RestTemplate();
    }

    /**
     * @param userId                 - String - userId of user making request.
     * @param rootClassificationType - String - name of base classification type
     * @param rootType - String - root asset type
     * @return governedAssetList     - map of classification
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<GovernedAssetComponent> getGovernedAssetComponentList(String userId, String rootClassificationType,
    String rootType) throws InvalidParameterException,
            UserNotAuthorizedException, MetadataServerException, RootClassificationNotFoundException {

        final String methodName = "getGovernedAssetComponentList";
        final String urlTemplate = "/{0}/govAssets"; //TODO: Need to figure out path for getting assets

        validateOMASServerURL(methodName);
        validateUserId(userId,methodName); // cannot be null
        // No validation for other parms -- optional. Managed server-side

        GovernedAssetComponentListAPIResponse restResult = callGovernanceEngineGovernedAssetComponentListRESTCall(methodName,
                omasServerURL + urlTemplate,
                userId,
                rootType,
                rootClassificationType);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRootClassificationNotFoundException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGovernedAssetList();
    }

    /**
     * @param userId                 - String - userId of user making request.
     * @param rootClassificationType - String - name of base classification type
     * @return tagDefinitionList       - Tag definitions
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<GovernanceClassificationDefinition> getGovernanceClassificationDefinitionList(String userId, String rootClassificationType) throws InvalidParameterException,
            UserNotAuthorizedException, MetadataServerException, RootClassificationNotFoundException {
        final String methodName = "getGovernanceClassificationDefinitionList";
        final String urlTemplate = "/{0}/govclassdefs?rootClassification={1}"; //TODO: Need to allow for root classification to be speced and do validation

        validateOMASServerURL(methodName);

        GovernanceClassificationDefinitionListAPIResponse restResult = callGovernanceEngineClassificationDefinitionListRESTCall(methodName,
                omasServerURL + urlTemplate,
                userId,
                rootClassificationType);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRootClassificationNotFoundException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return (restResult.getTagList());
    }

    /**
     * @param userId                 - String - userId of user making request.
     * @param assetGuid - String - asset guid
     * @return governedAssetList     - map of classification
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GovernedAssetComponent getGovernedAssetComponent(String userId, String assetGuid) throws InvalidParameterException,
            UserNotAuthorizedException, MetadataServerException, RootClassificationNotFoundException {
        final String methodName = "getGovernedAssetComponentList";
        final String urlTemplate = "/{0}/govAssets/{1}"; //TODO: Need to figure out path for getting assets

        validateOMASServerURL(methodName);

        GovernedAssetComponentAPIResponse restResult = callGovernanceEngineGovernedAssetComponentRESTCall(methodName,
                omasServerURL + urlTemplate,
                userId,
                assetGuid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRootClassificationNotFoundException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getAsset();
    }

    /**
     * @param userId                 - String - userId of user making request.
     * @param tagGuid - String - guid of classification
     * @return tagDefinitionList       - Tag definitions
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GovernanceClassificationDefinition getGovernanceClassificationDefinition(String userId, String tagGuid) throws InvalidParameterException,
            UserNotAuthorizedException, MetadataServerException, RootClassificationNotFoundException {
        final String methodName = "getGovernanceClassificationDefinitionList";
        final String urlTemplate = "/{0}/govclassdefs?rootClassification={1}?rootType={2}"; //TODO: Need to allow for root classification to be speced and do validation

        validateOMASServerURL(methodName);

        GovernanceClassificationDefinitionAPIResponse restResult = callGovernanceEngineClassificationDefinitionRESTCall(methodName,
                omasServerURL + urlTemplate,
                userId,
                tagGuid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRootClassificationNotFoundException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return (restResult.getGovernanceClassificationDefinition());
    }


    private void validateOMASServerURL(String methodName) throws InvalidParameterException {
        if (StringUtils.isEmpty(omasServerURL))  {
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
     * Throw an exception if the supplied userId is null
     *
     * @param userId     - user name to validate
     * @param methodName - name of the method making the call.
     * @throws InvalidParameterException - the userId is null
     */
    private void validateUserId(String userId,
                                String methodName) throws InvalidParameterException {
        if (StringUtils.isEmpty(userId)) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.NULL_USER_ID;
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
     * Issue a GET REST call that returns a GovernedAssetList object.
     *
     * @param methodName  - name of the method being called
     * @param urlTemplate - template of the URL for the REST API call with place-holders for the parameters
     * @param params      - a list of parameters that are slotted into the url template
     * @return GovernanceClassificationDefinitionAPIResponse
     * @throws MetadataServerException - something went wrong with the REST call stack.
     */
    private GovernanceClassificationDefinitionListAPIResponse callGovernanceEngineClassificationDefinitionListRESTCall(String methodName,
                                                                                   String urlTemplate,
                                                                                   Object... params) throws MetadataServerException {
        GovernanceClassificationDefinitionListAPIResponse restResult = new GovernanceClassificationDefinitionListAPIResponse();

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
     * Issue a GET REST call that returns a GovernedAssetList object.
     *
     * @param methodName  - name of the method being called
     * @param urlTemplate - template of the URL for the REST API call with place-holders for the parameters
     * @param params      - a list of parameters that are slotted into the url template
     * @return GovernanceClassificationDefinitionAPIResponse
     * @throws MetadataServerException - something went wrong with the REST call stack.
     */
    private GovernanceClassificationDefinitionAPIResponse callGovernanceEngineClassificationDefinitionRESTCall(String methodName,
                                                                         String urlTemplate,
                                                                         Object... params) throws MetadataServerException {
        GovernanceClassificationDefinitionAPIResponse restResult = new GovernanceClassificationDefinitionAPIResponse();

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
     * Issue a GET REST call that returns a GovernedAssetList object.
     *
     * @param methodName  - name of the method being called
     * @param urlTemplate - template of the URL for the REST API call with place-holders for the parameters
     * @param params      - a list of parameters that are slotted into the url template
     * @return GovernanceClassificationDefinitionAPIResponse
     * @throws MetadataServerException - something went wrong with the REST call stack.
     */
    private GovernedAssetComponentListAPIResponse callGovernanceEngineGovernedAssetComponentListRESTCall(String methodName,
                                                                                                                       String urlTemplate,
                                                                                                                       Object... params) throws MetadataServerException {
        GovernedAssetComponentListAPIResponse restResult = new GovernedAssetComponentListAPIResponse();

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
     * Issue a GET REST call that returns a GovernedAssetList object.
     *
     * @param methodName  - name of the method being called
     * @param urlTemplate - template of the URL for the REST API call with place-holders for the parameters
     * @param params      - a list of parameters that are slotted into the url template
     * @return GovernanceClassificationDefinitionAPIResponse
     * @throws MetadataServerException - something went wrong with the REST call stack.
     */
    private GovernedAssetComponentAPIResponse callGovernanceEngineGovernedAssetComponentRESTCall(String methodName,
                                                                                       String urlTemplate,
                                                                                       Object... params) throws MetadataServerException {
        GovernedAssetComponentAPIResponse restResult = new GovernedAssetComponentAPIResponse();

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
     * @param restResult - response from the rest call.  This generated in the remote server.
     * @throws InvalidParameterException - encoded exception from the server
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
     * Throw an MetadataServerException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResult - response from the rest call.  This generated in the remote server.
     * @throws MetadataServerException - encoded exception from the server
     */
    private void detectAndThrowPropertyServerException(String methodName,
                                                       GovernanceEngineOMASAPIResponse restResult) throws MetadataServerException {
        final String exceptionClassName = MetadataServerException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName()))) {
            throw new MetadataServerException(restResult.getRelatedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    restResult.getExceptionErrorMessage(),
                    restResult.getExceptionSystemAction(),
                    restResult.getExceptionUserAction());
        }
    }


    /**
     * Throw an UnrecognizedConnectionNameException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResult - response from the rest call.  This generated in the remote server.
     * @throws RootClassificationNotFoundException - encoded exception from the server
     */
    private void detectAndThrowRootClassificationNotFoundException(String methodName,
                                                                   GovernanceEngineOMASAPIResponse restResult) throws RootClassificationNotFoundException {
        final String exceptionClassName = RootClassificationNotFoundException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName()))) {
            throw new RootClassificationNotFoundException(restResult.getRelatedHTTPCode(),
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
     * @param restResult - response from UserNotAuthorizedException - encoded exception from the server
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
