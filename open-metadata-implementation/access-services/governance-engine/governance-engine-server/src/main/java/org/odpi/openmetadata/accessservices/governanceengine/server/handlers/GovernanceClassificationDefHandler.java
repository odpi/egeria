/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ConnectionHandler retrieves Connection objects from the property handlers.  It runs handlers-side in the AssetConsumer
 * OMAS and retrieves Connections through the OMRSRepositoryConnector.
 */
public class GovernanceClassificationDefHandler {
    private OMRSMetadataCollection metadataCollection = null;
    private String serviceName;
    private OMRSRepositoryHelper repositoryHelper = null;
    private String serverName = null;
    private static GovernanceEngineValidator validator;

    /**
     * Construct the  handler with a link to the repository connector
     *
     * @param serviceName         - name of this service
     * @param repositoryConnector - connector to the metadata repository connector
     * @throws MetadataServerException problem communicating with the repository
     */
    public GovernanceClassificationDefHandler(String serviceName,
                                              OMRSRepositoryConnector repositoryConnector) throws MetadataServerException {
        final String methodName = "GovernanceClassificationDefHandler";
        this.serviceName = serviceName;

        if (repositoryConnector != null) {
            try {
                this.repositoryHelper = repositoryConnector.getRepositoryHelper();
                this.serverName = repositoryConnector.getServerName();
                this.metadataCollection = repositoryConnector.getMetadataCollection();
            } catch (Throwable error) {
                GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.NO_METADATA_COLLECTION;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

                throw new MetadataServerException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
        }

    }


    /**
     * Returns the list of tag definitions.
     *
     * @param userId         - String - userId of user making request.
     * @param classification - classifications to start query from
     * @return List of Classification Definitions
     * @throws InvalidParameterException       - one of the parameters is null or invalid.
     * @throws ClassificationNotFoundException - cannot find all the classifications specified.
     * @throws MetadataServerException         - there is a problem retrieving information from the metadata server
     * @throws UserNotAuthorizedException      - the requesting user is not authorized to issue this request.
     */
    public List<GovernanceClassificationDef> getGovernanceClassificationDefs(String userId,
                                                                             List<String> classification) throws InvalidParameterException,
            MetadataServerException, ClassificationNotFoundException, UserNotAuthorizedException {

        final String methodName = "getGovernanceClassificationDefs";
        final String nameParameter = "name";

        List<GovernanceClassificationDef> defsToReturn = new ArrayList<>();

        //TODO: use common api code for this validation in client and server
        validator.validateUserId(userId, methodName);
        validator.validateClassification(classification, nameParameter, methodName);

        // Search for all classification types that match our criteria
        try {
            List<TypeDef> typeDefsByCategory = metadataCollection.findTypeDefsByCategory(userId, TypeDefCategory.CLASSIFICATION_DEF);
            //TODO Needs to restrict classifications by parent - for now returns ALL classifications
            typeDefsByCategory.forEach((td) -> {
                //TODO federation: resolve what to do if we have duplicate names (with different definitions)
                GovernanceClassificationDef gcd = new GovernanceClassificationDef();
                gcd.setName(td.getName());
                Map<String, String> gdca = new HashMap<>();
                //Get the parms
                td.getPropertiesDefinition().forEach((tda) -> {
                    // TODO Mapping of types between OMRS and Ranger should be abstracted
                    // TODO Mapping of alpha name is fragile - temporary for initial debug. Could just map primitive
                    // types
                    gdca.put(tda.getAttributeName(), tda.getAttributeType().getName());
                });
                gcd.setAttributeDefinitions(gdca);
                defsToReturn.add(gcd);
            });

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.USER_NOT_AUTHORIZED;
            String errorMessage =
                    errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(userId, methodName);

            throw new UserNotAuthorizedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException | RepositoryErrorException e) {

            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.METADATA_QUERY_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new MetadataServerException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return defsToReturn;
    }

    /**
     * Returns a single tag definitions.
     * <p>
     * NOTE: Currently am returning the same type (list) to keep API return consistent
     *
     * @param userId  - String - userId of user making request.
     * @param tagguid - tag guid
     * @return Connection retrieved from property handlers
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws MetadataServerException    - there is a problem retrieving information from the property (metadata)
     *                                    handlers.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     * @throws GuidNotFoundException      - the requesting user is not authorized to issue this request.
     */
    public GovernanceClassificationDef getGovernanceClassificationDef(String userId,
                                                                      String tagguid) throws InvalidParameterException,
            MetadataServerException,
            UserNotAuthorizedException, GuidNotFoundException {

        GovernanceClassificationDef defToReturn = new GovernanceClassificationDef();


        final String methodName = "getGovernanceClassificationDef";

        validator.validateUserId(userId, methodName);
        validator.validateGUID(tagguid, "tagguid", methodName);

        // Search for all classification types that match our criteria
        // getTypedefByCategory(CLASSIFICATION_DEF)
        try {
            TypeDef td = metadataCollection.getTypeDefByGUID(userId, tagguid);
            GovernanceClassificationDef gcd = new GovernanceClassificationDef();
            gcd.setName(td.getName());
            Map<String, String> gdca = new HashMap<>();
            //Get the parms
            td.getPropertiesDefinition().forEach((tda) -> {
                // TODO Mapping of types between OMRS and Ranger should be abstracted
                // TODO Mapping of alpha name is fragile - temporary for initial debug
                gdca.put(tda.getAttributeName(), tda.getAttributeType().getName());
            });
            gcd.setAttributeDefinitions(gdca);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.USER_NOT_AUTHORIZED;
            String errorMessage =
                    errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(userId, methodName);

            throw new UserNotAuthorizedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException | RepositoryErrorException e) {

            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.METADATA_QUERY_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new MetadataServerException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } catch (TypeDefNotKnownException e) {

            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.GUID_NOT_FOUND_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new GuidNotFoundException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
        return defToReturn;
    }
}
