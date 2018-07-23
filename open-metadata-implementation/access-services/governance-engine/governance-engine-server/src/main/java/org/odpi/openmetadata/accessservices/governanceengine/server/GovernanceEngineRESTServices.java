/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server;


import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.*;
import org.odpi.openmetadata.accessservices.governanceengine.server.admin.GovernanceEngineAdmin;
import org.odpi.openmetadata.accessservices.governanceengine.server.handlers.GovernanceClassificationDefinitionHandler;
import org.odpi.openmetadata.accessservices.governanceengine.server.handlers.GovernedAssetHandler;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
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
 * it is PI or SPI & perhaps masking or preventing access.
 * <p>
 * They tend to use this information in two ways
 * > At policy authoring time - where knowing what tags are available is interesting to help in that definition process
 * > At data access time - where we need to know if resource X is protected in some way due to it's metadata ie classification
 * <p>
 * Initially this OMAS api will provide information on those
 * > classifiers - we'll call them 'tags' here
 * > Managed assets - those resources - and in effect any tags associated with them. Details on the assets themselves are
 * better supported through the AssetConsumer OMAS API, and additionally the governance engine is not interested currently in HOW
 * the assets get classified - ie through the association of a classification directly to an asset, or via business terms,
 * so effectively flatten this
 * <p>
 * The result is a fairly simple object being made available to the engine, which will evolve as work is done on enhancing
 * the interaction (for example capturing information back from the engine), and as we interconnect with new governance engines
 **/

public class GovernanceEngineRESTServices {
    static private String accessServiceName = null;
    static private OMRSRepositoryConnector repositoryConnector = null;

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineRESTServices.class);

    /**
     * Provide a connector to the REST Services.
     *
     * @param accessServiceName   - name of this access service
     * @param repositoryConnector - OMRS Repository Connector to the property handlers.
     */
    static public void setRepositoryConnector(String accessServiceName,
                                              OMRSRepositoryConnector repositoryConnector) {
        GovernanceEngineRESTServices.accessServiceName = accessServiceName;
        GovernanceEngineRESTServices.repositoryConnector = repositoryConnector;
    }

    /**
     * Default constructor
     */
    public GovernanceEngineRESTServices() {
        AccessServiceDescription myDescription = AccessServiceDescription.GOVERNANCE_ENGINE_OMAS;
        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                myDescription.getAccessServiceName(),
                myDescription.getAccessServiceDescription(),
                myDescription.getAccessServiceWiki(),
                AccessServiceOperationalStatus.ENABLED,
                GovernanceEngineAdmin.class.getName());
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
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
     * @param userId             - String - userId of user making request.
     * @param rootClassification - this may be the qualifiedName or displayName of the connection.
     * @return GovernanceClassificationDefinitionList or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GovernanceClassificationDefinitionListAPIResponse getGovernanceClassificationDefinitions(String userId, List<String> rootClassification
    ) {
        final String methodName = "getGovernanceClassificationDefinitions";


        if (log.isDebugEnabled()) {
            log.debug("Calling method: " + methodName);
        }

        // create API response
        GovernanceClassificationDefinitionListAPIResponse response = new GovernanceClassificationDefinitionListAPIResponse();

        // Invoke the right handler for this API request
        try {
            this.validateInitialization(methodName);

            GovernanceClassificationDefinitionHandler governanceClassificationDefinitionHandler = new GovernanceClassificationDefinitionHandler(accessServiceName,
                    repositoryConnector);

            response.setTagList(governanceClassificationDefinitionHandler.getGovernanceClassificationDefinitions(userId, rootClassification));
        } catch (InvalidParameterException error) {
            captureInvalidParameterException(response, error);
        } catch (RootClassificationNotFoundException error) {
            captureRootClassificationNotFoundException(response, error);
        } catch (PropertyServerException error) {
            capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            captureUserNotAuthorizedException(response, error);
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
     * @param userId  - String - userId of user making request.
     * @param tagguid - guid of the definition to retrieve
     * @return GovernanceClassificationDefinition or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */

    public GovernanceClassificationDefinitionAPIResponse getClassificationDefinition(String userId, String tagguid) {
        final String methodName = "getClassificationDefinition";


        if (log.isDebugEnabled()) {
            log.debug("Calling method: " + methodName);
        }

        // create API response
        GovernanceClassificationDefinitionAPIResponse response = new GovernanceClassificationDefinitionAPIResponse();

        // Invoke the right handler for this API request
        try {
            this.validateInitialization(methodName);

            GovernanceClassificationDefinitionHandler tagHandler = new GovernanceClassificationDefinitionHandler(accessServiceName,
                    repositoryConnector);

            response.setGovernanceClassificationDefinition(tagHandler.getGovernanceClassificationDefinition(userId, tagguid));
        } catch (InvalidParameterException error) {
            captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            captureUserNotAuthorizedException(response, error);
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
     * @param userId             - String - userId of user making request.
     * @param rootClassification - this may be the qualifiedName or displayName of the connection.
     * @param rootType
     * @return GovernedAssetComponentList or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GovernedAssetComponentListAPIResponse getGovernedAssetComponents(String userId,
                                                                            List<String> rootClassification,
                                                                            List<String> rootType) {
        final String methodName = "getGovernedAssetComponents";


        if (log.isDebugEnabled()) {
            log.debug("Calling method: " + methodName);
        }

        // create API response
        GovernedAssetComponentListAPIResponse response = new GovernedAssetComponentListAPIResponse();

        // Invoke the right handler for this API request
        try {
            this.validateInitialization(methodName);

            GovernedAssetHandler governedAssetHandler = new GovernedAssetHandler(accessServiceName,
                    repositoryConnector);

            response.setGovernedAssetList(governedAssetHandler.getGovernedAssetComponents(userId, rootClassification, rootType));
        } catch (InvalidParameterException error) {
            captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            capturePropertyServerException(response, error);
        } catch (RootClassificationNotFoundException error) {
            captureRootClassificationNotFoundException(response, error);
        } catch (UserNotAuthorizedException error) {
            captureUserNotAuthorizedException(response, error);
        } catch (RootAssetTypeNotFoundException error) {
            captureRootAssetNotFoundException(response, error);
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
     * @param userId             - String - userId of user making request.
     * @param assetComponentGuid - Guid of the asset component to retrieve
     * @return GovernedAssetComponent or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */

    public GovernedAssetComponentAPIResponse getGovernedAssetComponent(String userId,
                                                                       String assetComponentGuid) {
        final String methodName = "getGovernedAssetComponent";


        if (log.isDebugEnabled()) {
            log.debug("Calling method: " + methodName);
        }

        // create API response
        GovernedAssetComponentAPIResponse response = new GovernedAssetComponentAPIResponse();

        // Invoke the right handler for this API request
        try {
            this.validateInitialization(methodName);

            GovernedAssetHandler governedAssetHandler = new GovernedAssetHandler(accessServiceName,
                    repositoryConnector);

            response.setAsset(governedAssetHandler.getGovernedAssetComponent(userId, assetComponentGuid));
        } catch (InvalidParameterException error) {
            captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            capturePropertyServerException(response, error);
        } catch (RootClassificationNotFoundException error) {
            captureRootClassificationNotFoundException(response, error);
        } catch (UserNotAuthorizedException error) {
            captureUserNotAuthorizedException(response, error);
        } catch (RootAssetTypeNotFoundException error) {
            captureRootAssetNotFoundException(response, error);
        }
        if (log.isDebugEnabled()) {
            log.debug("Returning from method: " + methodName + " with response: " + response.toString());
        }

        return response;
    }

    /**
     * Validate that this access service has been initialized before attempting to process a request.
     *
     * @param methodName - name of method called.
     * @throws PropertyServerException - not initialized
     */
    private void validateInitialization(String methodName) throws PropertyServerException {
        if (repositoryConnector == null) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.SERVICE_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
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
    private void capturePropertyServerException(GovernanceEngineOMASAPIResponse response,
                                                PropertyServerException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    private void captureRootClassificationNotFoundException(GovernanceEngineOMASAPIResponse response,
                                                            RootClassificationNotFoundException error) {
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
    private void captureRootAssetNotFoundException(GovernanceEngineOMASAPIResponse response,
                                                   RootAssetTypeNotFoundException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }
}
