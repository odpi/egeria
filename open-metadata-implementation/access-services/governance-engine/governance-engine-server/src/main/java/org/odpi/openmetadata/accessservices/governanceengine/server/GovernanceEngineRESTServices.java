/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server;


import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.*;
import org.odpi.openmetadata.accessservices.governanceengine.server.admin.GovernanceEngineAdmin;
import org.odpi.openmetadata.accessservices.governanceengine.server.handlers.GovernanceClassificationDefHandler;
import org.odpi.openmetadata.accessservices.governanceengine.server.handlers.GovernedAssetHandler;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
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
 *  - At policy authoring time - where knowing what tags are available is interesting to help in that definition process
 *  - At data access time - where we need to know if resource X is protected in some way due to it's metadata ie classification
 * <p>
 * Initially this OMAS client will provide information on those
 *  - classifiers - we'll call them 'tags' here
 *  - Managed assets - those resources - and in effect any tags associated with them. Details on the assets themselves are
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


    /**
     * Default constructor
     */
    public GovernanceEngineRESTServices() {
    }


    /**
     * Tag related queries
     * .../{userId}/tags - all relevant tags
     * .../{userid}/tags/{guid} - just that specific tag
     * Search Params
     * ?rootClassification= - only use classifications of this type
     * ?rootType= - only retrieve assets of this type
     */
    /**
     * Returns the list of governance tags for the enforcement engine
     * <p>
     * These are the definitions - so tell us the name, guid, attributes
     * associated with the tag. The security engine will want to know about
     * these tags to assist in policy authoring/validation, as well as know
     * when they change, since any existing assets classified with the tags
     * are affected
     *
     * @param serverName  - name of the server that the request is for
     * @param userId             - String - userId of user making request.
     * @param classification - this may be the qualifiedName or displayName of the connection.
     * @return GovernanceClassificationDefinitionList or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GovernanceClassificationDefListAPIResponse getGovernanceClassificationDefs(String serverName, String userId, List<String> classification
    ) {
        final String methodName = "getGovernanceClassificationDefs";

        if (log.isDebugEnabled()) {
            log.debug("Calling method: " + methodName);
        }

        // create API response
        GovernanceClassificationDefListAPIResponse response = new GovernanceClassificationDefListAPIResponse();

        // Invoke the right handler for this API request
        try {
            GovernanceClassificationDefHandler governanceClassificationDefHandler = new GovernanceClassificationDefHandler(instanceHandler.getAccessServiceName(serverName),
                                                                                                                           instanceHandler.getRepositoryConnector(serverName));

            response.setClassificationDefsList(governanceClassificationDefHandler.getGovernanceClassificationDefs(userId, classification));
        }
        catch (InvalidParameterException error) {
            captureInvalidParameterException(response, error);
        }
        catch (MetadataServerException error) {
            captureMetadataServerException(response, error);
        }
        catch (ClassificationNotFoundException error) {
            captureClassificationNotFoundException(response, error);
        }
        catch (UserNotAuthorizedException error) {
            captureUserNotAuthorizedException(response,error);
        }
        catch (PropertyServerException error) {
            capturePropertyServerException(response,error);
        }
        if (log.isDebugEnabled()) {
            log.debug("Returning from method: " + methodName + " with response: " + response.toString());
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
     * @param serverName  - name of the server that the request is for
     * @param userId  - String - userId of user making request.
     * @param classificationGuid - guid of the definition to retrieve
     * @return GovernanceClassificationDef or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */

    public GovernanceClassificationDefAPIResponse getClassificationDefs(String serverName, String userId, String classificationGuid) {
        final String methodName = "getClassificationDefs";


        if (log.isDebugEnabled()) {
            log.debug("Calling method: " + methodName);
        }

        // create API response
        GovernanceClassificationDefAPIResponse response = new GovernanceClassificationDefAPIResponse();

        // Invoke the right handler for this API request
        try {

            GovernanceClassificationDefHandler tagHandler = new GovernanceClassificationDefHandler(instanceHandler.getAccessServiceName(serverName),
                                                                                                   instanceHandler.getRepositoryConnector(serverName));

            response.setGovernanceClassificationDef(tagHandler.getGovernanceClassificationDef(userId, classificationGuid));
        } catch (InvalidParameterException error) {
            captureInvalidParameterException(response, error);
        } catch (MetadataServerException error) {
            captureMetadataServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            captureUserNotAuthorizedException(response, error);
        } catch (GuidNotFoundException error) {
            captureGuidNotFoundException(response, error);
        } catch (PropertyServerException error) {
            capturePropertyServerException(response,error);
        }
        if (log.isDebugEnabled()) {
            log.debug("Returning from method: " + methodName + " with response: " + response.toString());
        }

        return response;
    }


    /**
     * Returns the list of governed asset
     * <p>
     * These include the tag associations but not the definitions of those tags
     *
     * @param serverName  - name of the server that the request is for
     * @param userId             - String - userId of user making request.
     * @param classification - this may be the qualifiedName or displayName of the connection.
     * @param type types to start query from
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
        final String methodName = "getGovernedAssets";


        if (log.isDebugEnabled()) {
            log.debug("Calling method: " + methodName);
        }

        // create API response
        GovernedAssetListAPIResponse response = new GovernedAssetListAPIResponse();

        // Invoke the right handler for this API request
        try {
            GovernedAssetHandler governedAssetHandler = new GovernedAssetHandler(instanceHandler.getAccessServiceName(serverName),
                                                                                 instanceHandler.getRepositoryConnector(serverName));

            response.setGovernedAssetList(governedAssetHandler.getGovernedAssets(userId, classification, type));
        } catch (InvalidParameterException error) {
            captureInvalidParameterException(response, error);
        } catch (MetadataServerException error) {
            captureMetadataServerException(response, error);
        } catch (ClassificationNotFoundException error) {
            captureClassificationNotFoundException(response, error);
        } catch (UserNotAuthorizedException error) {
            captureUserNotAuthorizedException(response, error);
        } catch (TypeNotFoundException error) {
            captureTypeNotFoundException(response, error);
        } catch (PropertyServerException error) {
            capturePropertyServerException(response,error);
        }
        if (log.isDebugEnabled()) {
            log.debug("Returning from method: " + methodName + " with response: " + response.toString());
        }

        return response;
    }

    /**
     * Returns a single governed asset
     * <p>
     * These include the tag associations but not the definitions of those tags
     *
     * @param serverName  - name of the server that the request is for
     * @param userId             - String - userId of user making request.
     * @param assetGuid - Guid of the asset component to retrieve
     * @return GovernedAsset or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */

    public GovernedAssetAPIResponse getGovernedAsset(String serverName,
                                                     String userId,
                                                     String assetGuid) {
        final String methodName = "getGovernedAsset";


        if (log.isDebugEnabled()) {
            log.debug("Calling method: " + methodName);
        }

        // create API response
        GovernedAssetAPIResponse response = new GovernedAssetAPIResponse();

        // Invoke the right handler for this API request
        try {
            GovernedAssetHandler governedAssetHandler = new GovernedAssetHandler(instanceHandler.getAccessServiceName(serverName),
                                                                                 instanceHandler.getRepositoryConnector(serverName));

            response.setAsset(governedAssetHandler.getGovernedAsset(userId, assetGuid));
        } catch (InvalidParameterException error) {
            captureInvalidParameterException(response, error);
        } catch (MetadataServerException error) {
            captureMetadataServerException(response, error);
        } catch (GuidNotFoundException error) {
            captureGuidNotFoundException(response, error);
        } catch (UserNotAuthorizedException error) {
            captureUserNotAuthorizedException(response, error);
        } catch (PropertyServerException error) {
            capturePropertyServerException(response,error);
        }
        if (log.isDebugEnabled()) {
            log.debug("Returning from method: " + methodName + " with response: " + response.toString());
        }

        return response;
    }

    // TODO Fixup doc headers in file

    /**
     * Set the exception information into the response.
     *
     * @param response           - REST Response
     * @param error              returned response.
     * @param exceptionClassName - class name of the exception to recreate
     */
    private void captureCheckedException(GovernanceEngineOMASAPIResponse response,
                                         GovernanceEngineCheckedExceptionBase error,
                                         String exceptionClassName) {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    private void captureInvalidParameterException(GovernanceEngineOMASAPIResponse response,
                                                  InvalidParameterException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    private void captureMetadataServerException(GovernanceEngineOMASAPIResponse response,
                                                MetadataServerException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    private void captureClassificationNotFoundException(GovernanceEngineOMASAPIResponse response,
                                                        ClassificationNotFoundException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    private void captureUserNotAuthorizedException(GovernanceEngineOMASAPIResponse response,
                                                   UserNotAuthorizedException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    private void captureTypeNotFoundException(GovernanceEngineOMASAPIResponse response,
                                              TypeNotFoundException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    private void captureGuidNotFoundException(GovernanceEngineOMASAPIResponse response,
                                              GuidNotFoundException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    private void capturePropertyServerException(GovernanceEngineOMASAPIResponse response,
                                                PropertyServerException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }

}
