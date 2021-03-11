/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.server.services;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.GovernedAssetListResponse;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.GovernedAssetResponse;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.SoftwareServerCapabilityRequestBody;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.SoftwareServerCapabilityResponse;
import org.odpi.openmetadata.accessservices.securityofficer.server.handler.GovernedAssetHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.List;

/**
 * The GovernedAssetRESTServices provides the handlers-side implementation of the GovernanceEngine Open Metadata
 * Access Service (OMAS).
 * <p>
 * This package deals with supporting the REST API from within the OMAG Server
 * <p>
 * Governance engines such as Apache Ranger are interested in the classification of resources that they provide
 * access to. They are unlikely to be interested in all the nitty gritty properties of that resource, only knowing that
 * it is PI or SPI and perhaps masking or preventing access.
 * <p>
 * They tend to use this information in two ways
 * - At policy authoring time - where knowing what tags are available is interesting to help in that definition process
 * - At data access time - where we need to know if resource X is protected in some way due to it's metadata ie classification
 * <p>
 * Initially this OMAS client will provide information on those
 * - classifiers - we'll call them 'tags' here
 * - Managed assets - those resources - and in effect any tags associated with them. Details on the assets themselves are
 * better supported through the AssetConsumer OMAS API, and additionally the governance engine is not interested currently in HOW
 * the assets get classified - ie through the association of a classification directly to an asset, or via business terms,
 * so effectively flatten this
 * <p>
 * The result is a fairly simple object being made available to the engine, which will evolve as work is done on enhancing
 * the interaction (for example capturing information back from the engine), and as we interconnect with new governance engines
 **/

public class GovernedAssetRESTServices
{

    private static SecurityOfficerInstanceHandler instanceHandler = new SecurityOfficerInstanceHandler();
    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Returns the list of governed asset
     * <p>
     * These include the tag associations but not the definitions of those tags
     *
     * @param serverName  - name of the server that the request is for
     * @param userId      - String - userId of user making request.
     * @param entityTypes types to start query offset
     * @return GovernedAssetComponentList
     */
    public GovernedAssetListResponse getGovernedAssets(String serverName, String userId, List<String> entityTypes, Integer offset, Integer pageSize) {
        String methodName = "getGovernedAssets";

        GovernedAssetListResponse response = new GovernedAssetListResponse();
        AuditLog     auditLog = null;
        try {
            GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGovernedAssetList(governedAssetHandler.getGovernedAssets(userId, entityTypes, offset, pageSize));
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(response, e, methodName, auditLog);
        }

        return response;
    }

    /**
     * Returns a single governed asset
     * <p>
     * These include the tag associations but not the definitions of those tags
     *
     * @param serverName - name of the server that the request is for
     * @param userId     - String - userId of user making request.
     * @param assetGuid  - Guid of the asset component to retrieve
     * @return GovernedAsset or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GovernedAssetResponse getGovernedAsset(String serverName, String userId, String assetGuid) {
        String methodName = "getGovernedAsset";
        GovernedAssetResponse response = new GovernedAssetResponse();
        AuditLog     auditLog = null;

        try {
            GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setAsset(governedAssetHandler.getGovernedAsset(userId, assetGuid));
        } catch (InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(response, e, methodName, auditLog);
        }

        return response;
    }

    public StringResponse createSoftwareServer(String serverName, String userId, SoftwareServerCapabilityRequestBody requestBody) {
        String methodName = "createSoftwareServer";

        StringResponse response = new StringResponse();
        AuditLog     auditLog = null;
        try {
            GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setResultString(governedAssetHandler.createSoftwareServerCapability(userId, requestBody.getSoftwareServerCapability()));
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(response, e, methodName, auditLog);
        }

        return response;
    }

    public SoftwareServerCapabilityResponse getSoftwareServerByGUID(String serverName, String userId, String guid) {
        String                           methodName = "getSoftwareServerByGUID";
        SoftwareServerCapabilityResponse response   = new SoftwareServerCapabilityResponse();
        AuditLog                         auditLog   = null;

        try {
            GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setServerCapability(governedAssetHandler.getSoftwareServerCapabilityByGUID(userId, guid));
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(response, e, methodName, auditLog);
        }

        return response;
    }
}