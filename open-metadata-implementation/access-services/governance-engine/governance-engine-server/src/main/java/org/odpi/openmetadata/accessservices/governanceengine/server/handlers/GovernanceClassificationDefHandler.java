/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.GuidNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
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
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.serverName = repositoryConnector.getServerName();
            try {
                this.metadataCollection = repositoryConnector.getMetadataCollection();
            } catch (RepositoryErrorException e) {
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
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws MetadataServerException    - there is a problem retrieving information from the metadata server
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<GovernanceClassificationDef> getGovernanceClassificationDefs(String userId, List<String> classification)
            throws InvalidParameterException, MetadataServerException, UserNotAuthorizedException {
        final String methodName = "getGovernanceClassificationDefs";
        final String nameParameter = "name";

        validator.validateUserId(userId, methodName);
        validator.validateClassification(classification, nameParameter, methodName);

        List<GovernanceClassificationDef> defsToReturn = new ArrayList<>();
        try {
            List<TypeDef> typeDefsByCategory = metadataCollection.findTypeDefsByCategory(userId, TypeDefCategory.CLASSIFICATION_DEF);
            if (typeDefsByCategory != null) {
                typeDefsByCategory.forEach(typeDef -> {
                    GovernanceClassificationDef governanceClassificationDef = getGovernanceClassificationDef(typeDef);
                    defsToReturn.add(governanceClassificationDef);
                });
            }

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.USER_NOT_AUTHORIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(userId, methodName);

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

    private GovernanceClassificationDef getGovernanceClassificationDef(TypeDef typeDef) {
        GovernanceClassificationDef governanceClassificationDef = new GovernanceClassificationDef();

        governanceClassificationDef.setName(typeDef.getName());
        Map<String, String> propertyMap = getProperties(typeDef);
        governanceClassificationDef.setAttributeDefinitions(propertyMap);

        return governanceClassificationDef;
    }

    private Map<String, String> getProperties(TypeDef typeDef) {
        Map<String, String> propertyMap = new HashMap<>();
        List<TypeDefAttribute> properties = typeDef.getPropertiesDefinition();
        if (properties != null) {
            properties.forEach(attribute -> propertyMap.put(attribute.getAttributeName(), attribute.getAttributeType().getName()));
        }
        return propertyMap;
    }

    /**
     * Returns a single tag definitions.
     * <p>
     * NOTE: Currently am returning the same type (list) to keep API return consistent
     *
     * @param userId  - String - userId of user making request.
     * @param tagGuid - tag guid
     * @return Connection retrieved from property handlers
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws MetadataServerException    - there is a problem retrieving information from the property (metadata)
     *                                    handlers.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     * @throws GuidNotFoundException      - the requesting user is not authorized to issue this request.
     */
    public GovernanceClassificationDef getGovernanceClassificationDef(String userId, String tagGuid) throws InvalidParameterException, MetadataServerException, UserNotAuthorizedException, GuidNotFoundException {
        final String methodName = "getGovernanceClassificationDef";
        validator.validateUserId(userId, methodName);
        validator.validateGUID(tagGuid, "tagGuid", methodName);

        try {
            TypeDef typeDef = metadataCollection.getTypeDefByGUID(userId, tagGuid);
            return getGovernanceClassificationDef(typeDef);
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
    }
}
