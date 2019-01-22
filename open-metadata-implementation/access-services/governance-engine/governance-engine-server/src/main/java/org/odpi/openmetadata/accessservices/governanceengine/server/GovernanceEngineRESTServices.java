/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server;


import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.GuidNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassificationDefAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassificationDefListAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAssetAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAssetListAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.server.handlers.GovernanceClassificationDefHandler;
import org.odpi.openmetadata.accessservices.governanceengine.server.handlers.GovernedAssetHandler;
import org.odpi.openmetadata.accessservices.governanceengine.server.util.ExceptionHandler;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The GovernanceEngineRESTServices provides the handlers-side implementation of the GovernanceEngine Open Metadata
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

public class GovernanceEngineRESTServices {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineRESTServices.class);
    private static GovernanceEngineInstanceHandler instanceHandler = new GovernanceEngineInstanceHandler();
    private ExceptionHandler exceptionHandler = new ExceptionHandler();

    public GovernanceClassificationDefListAPIResponse getGovernanceClassificationDefs(String serverName, String userId, List<String> classification) {
        GovernanceClassificationDefListAPIResponse response = new GovernanceClassificationDefListAPIResponse();
        try {
            GovernanceClassificationDefHandler governanceClassificationDefHandler = new GovernanceClassificationDefHandler(
                    instanceHandler.getAccessServiceName(serverName),
                    instanceHandler.getRepositoryConnector(serverName));

            response.setClassificationDefsList(governanceClassificationDefHandler.getGovernanceClassificationDefs(userId, classification));
        } catch (InvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (MetadataServerException error) {
            exceptionHandler.captureMetadataServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (PropertyServerException error) {
            exceptionHandler.capturePropertyServerException(response, error);
        }

        return response;
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
     * @param serverName         - name of the server that the request is for
     * @param userId             - String - userId of user making request.
     * @param classificationGuid - guid of the definition to retrieve
     * @return GovernanceClassificationDef or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */

    public GovernanceClassificationDefAPIResponse getClassificationDefs(String serverName, String userId, String classificationGuid) {
        GovernanceClassificationDefAPIResponse response = new GovernanceClassificationDefAPIResponse();

        try {
            GovernanceClassificationDefHandler tagHandler = new GovernanceClassificationDefHandler(
                    instanceHandler.getAccessServiceName(serverName),
                    instanceHandler.getRepositoryConnector(serverName));

            response.setGovernanceClassificationDef(tagHandler.getGovernanceClassificationDef(userId, classificationGuid));
        } catch (InvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (MetadataServerException error) {
            exceptionHandler.captureMetadataServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (GuidNotFoundException error) {
            exceptionHandler.captureGuidNotFoundException(response, error);
        } catch (PropertyServerException error) {
            exceptionHandler.capturePropertyServerException(response, error);
        }

        return response;
    }


    /**
     * Returns the list of governed asset
     * <p>
     * These include the tag associations but not the definitions of those tags
     *
     * @param serverName     - name of the server that the request is for
     * @param userId         - String - userId of user making request.
     * @param classification - this may be the qualifiedName or displayName of the connection.
     * @param type           types to start query from
     * @return GovernedAssetComponentList or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GovernedAssetListAPIResponse getGovernedAssets(String serverName,
                                                          String userId,
                                                          List<String> classification,
                                                          List<String> type) {
        log.debug("Calling method: getGovernedAssets");
        GovernedAssetListAPIResponse response = new GovernedAssetListAPIResponse();

        try {
            GovernedAssetHandler governedAssetHandler = new GovernedAssetHandler(instanceHandler.getRepositoryConnector(serverName));

            response.setGovernedAssetList(governedAssetHandler.getGovernedAssets(userId, classification, type));
        } catch (InvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (MetadataServerException error) {
            exceptionHandler.captureMetadataServerException(response, error);
        } catch (PropertyServerException error) {
            exceptionHandler.capturePropertyServerException(response, error);
        } catch (PagingErrorException | RepositoryErrorException | FunctionNotSupportedException | org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException | EntityProxyOnlyException | PropertyErrorException | org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException | EntityNotKnownException | TypeErrorException | TypeDefNotKnownException e) {
            log.error(e.getErrorMessage());
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

    public GovernedAssetAPIResponse getGovernedAsset(String serverName, String userId, String assetGuid) {
        GovernedAssetAPIResponse response = new GovernedAssetAPIResponse();

        try {
            GovernedAssetHandler governedAssetHandler = new GovernedAssetHandler(instanceHandler.getRepositoryConnector(serverName));

            response.setAsset(governedAssetHandler.getGovernedAsset(userId, assetGuid));
        } catch (InvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (MetadataServerException error) {
            exceptionHandler.captureMetadataServerException(response, error);
        } catch (PropertyServerException error) {
            exceptionHandler.capturePropertyServerException(response, error);
        }

        return response;
    }
}