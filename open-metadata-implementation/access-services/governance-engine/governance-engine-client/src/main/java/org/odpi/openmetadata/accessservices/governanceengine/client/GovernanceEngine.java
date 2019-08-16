/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.client;

import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAssetAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAssetListAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.SoftwareServerCapabilityResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * The Governance Engine Open Metadata Access Service (OMAS) provides an interface to support a policy engine
 * such as Apache Ranger. See interface definition for more explanation
 */
public class GovernanceEngine implements GovernanceEngineInterface {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngine.class);
    private String serverName;
    private String omasServerURL;
    private RestTemplate restTemplate;


    /**
     * Create a new GovernanceEngine client.
     *
     * @param serverName   - the network address of the process running the OMAS REST servers
     * @param newServerURL - the network address of the process running the OMAS REST servers
     */
    public GovernanceEngine(String serverName, String newServerURL) {
        this.serverName = serverName;
        this.omasServerURL = newServerURL;
        restTemplate = new RestTemplate();
    }

    /**
     * @param userId         - String - userId of user making request.
     * @param classification - String - name of base classification type
     * @param type           - String - root asset type
     * @return governedAssetList                    - map of classification
     * @throws InvalidParameterException - one of the parameters is null or invalid.
     */
    public List<GovernedAsset> getGovernedAssetList(String userId, String classification, String type) throws InvalidParameterException {
        final String methodName = "getGovernedAssetList";
        log.debug("Calling method: {}", methodName);


        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);

        final String url = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/assets?type={2}";
        GovernedAssetListAPIResponse response = getRestCall(url, GovernedAssetListAPIResponse.class, serverName, userId, type, classification);

        if (response != null) {
            return response.getGovernedAssetList();
        }

        return Collections.emptyList();
    }

    /**
     * @param userId    - String - userId of user making request.
     * @param assetGuid - String - guid of asset component
     * @return AssetTagMap                  - map of classification
     * @throws InvalidParameterException - one of the parameters is null or invalid
     */
    public GovernedAsset getGovernedAsset(String userId, String assetGuid) throws InvalidParameterException {
        final String methodName = "getGovernedAssetList";


        log.debug("Calling method: {}", methodName);

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGuid(assetGuid, methodName);


        final String url = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/assets/{2}";
        GovernedAssetAPIResponse response = getRestCall(url, GovernedAssetAPIResponse.class, serverName, userId, assetGuid);
        if (response != null) {
            return response.getAsset();
        }

        return null;
    }

    /**
     * Create a Software Server Capability entity
     *
     * @param userId                   - String - userId of user making request.
     * @param softwareServerCapability - SoftwareServerCapabilityRequestBody
     * @return the Software Server entity created
     */
    @Override
    public SoftwareServerCapability createSoftwareServerCapability(String userId, SoftwareServerCapability softwareServerCapability) throws InvalidParameterException {
        final String methodName = "createSoftwareServerCapability";
        log.debug("Calling method: {}", methodName);

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);

        final String url = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/software-server-capabilities";
        SoftwareServerCapabilityResponse response = postRestCall(url, softwareServerCapability, SoftwareServerCapabilityResponse.class, serverName, userId);

        return response.getServerCapability();
    }

    /**
     * Fetch the Software Server Capability entity by global identifier
     *
     * @param userId the name of the calling user
     * @param guid   guid of the software server
     * @return unique identifier of the created process
     */
    @Override
    public SoftwareServerCapability getSoftwareServerCapabilityByGUID(String userId, String guid) throws InvalidParameterException {
        final String methodName = "getSoftwareServerCapabilityByGUID";
        log.debug("Calling method: {}", methodName);
        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);


        final String url = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/software-server-capabilities/{2}";
        SoftwareServerCapabilityResponse response = getRestCall(url, SoftwareServerCapabilityResponse.class, serverName, userId, guid);
        return response.getServerCapability();
    }

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
    private void validateUserId(String userId, String methodName) throws InvalidParameterException {
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
    private void validateGuid(String guid, String methodName) throws InvalidParameterException {
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

    private <T> T postRestCall(String url, Object requestBody, Class<T> clazz, Object... params) {
        return restTemplate.postForObject(omasServerURL + url, requestBody, clazz, params);
    }

    private <T> T getRestCall(String url, Class<T> clazz, Object... params) {
        return restTemplate.getForObject(omasServerURL + url, clazz, params);
    }

}
