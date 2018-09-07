/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;


import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassificationDef;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.RELATIONAL_TABLE;

/**
 * ConnectionHandler retrieves Connection objects from the property handlers.  It runs handlers-side in the AssetConsumer
 * OMAS and retrieves Connections through the OMRSRepositoryConnector.
 */
public class GovernedAssetHandler {

    private OMRSMetadataCollection metadataCollection = null;
    private String serviceName;
    private OMRSRepositoryHelper repositoryHelper = null;
    private String serverName = null;
    private static GovernanceEngineValidator errorHandler;

    /**
     * Construct the connection handler with a link to the property handlers's connector and this access service's
     * official name.
     *
     * @param serviceName         - name of this service
     * @param repositoryConnector - connector to the property handlers.
     * @throws MetadataServerException         - there is a problem retrieving information from the metadata server
     */
    public GovernedAssetHandler(String serviceName,
                                OMRSRepositoryConnector repositoryConnector) throws MetadataServerException {
        final String methodName = "GovernedAssetHandler";
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
     * Returns the list of governed assets with associated tags
     *
     * @param userId         - String - userId of user making request.
     * @param classification - classifications to start query from .
     * @param type - types to start query from.
     * @return List of Governed Access
     * @throws InvalidParameterException       - one of the parameters is null or invalid.
     * @throws ClassificationNotFoundException - cannot find all the classifications specified
     * @throws TypeNotFoundException           - cannot find all the types specified
     * @throws MetadataServerException         - there is a problem retrieving information from the metadata server
     * @throws UserNotAuthorizedException      - the requesting user is not authorized to issue this request.
     */
    public List<GovernedAsset> getGovernedAssets(String userId,
                                                 List<String> classification,
                                                 List<String> type) throws InvalidParameterException,
            UserNotAuthorizedException, MetadataServerException, ClassificationNotFoundException, TypeNotFoundException {
        final String methodName = "getGovernedAssets";
        final String classificationParameter = "classification";
        final String typeParameter = "type";
        List<GovernedAsset> assetsToReturn = new ArrayList<>();
        List<GovernanceClassificationDef> interestingClassifications = new ArrayList<>();

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateClassification(classification, classificationParameter, methodName);
        errorHandler.validateType(classification, typeParameter, methodName);

        //TODO: Add tighter query using type and classification (TBD)

        //TODO Refactoring of common code with GovernanceClassificationDefs
        // Get all classifications, to determine which we are interested in
        try {
            List<TypeDef> typeDefsByCategory = metadataCollection.findTypeDefsByCategory(userId, TypeDefCategory.CLASSIFICATION_DEF);
            // We just need the name and parameter
            //TODO Needs to restrict classifications by parent - for now returns ALL classifications
            typeDefsByCategory.forEach((td) -> {
                //TODO federation: resolve what to do if we have duplicate names (with different definitions)
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
                interestingClassifications.add(gcd);

                //TODO Need to iterate through all possible asset types - for now we will take first in the list
                String assetTypeToSearchFor="";
                if (type.size() >= 1)
                    assetTypeToSearchFor = new String(type.get(0));

                // TODO Pagination & sort order is needed in these APIs - for now do not specify limits
                //TODO restrict to active entities only
                List<EntityDetail> entities = new ArrayList<>();

                // Java Streams can't deal with checked exceptions
                try {
                    entities = metadataCollection.findEntitiesByClassification(userId, assetTypeToSearchFor,
                            td.getName(),
                            null, null, 0,
                            null,
                            null, null, null, 0);
                } catch (Exception e) {
                    // We could only throw a RuntimeException here - consider using
                    // wrapping, or unroll the iterator.
                    // for now will catch and just return empty set (not good enough!)
                                        // TODO Manage exceptions in Java Streams
                }


                // Now for each entity we need to get it's fq name, and/or follow through to the schema
                entities.forEach(entityDetail -> {
                    String resourceName = getGovernanceResourceNameFromEntity(entityDetail);
                });
            });

            // for the interested classifications, get all entities assigned that classification

            // Return the data

        }
        // TODO - Common catch pattern
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

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
        return assetsToReturn;
    }

    private String getGovernanceResourceNameFromEntity(EntityDetail entityDetail) {
        // We have an entity, but need to map this to 'something' that a governance engine like
        // ranger understands. Some of this is managed by 'tagsync' For OMAS APIs

        //TODO WOrk through this in debugger/UML model
        String resourceName = null;
        //TODO WOrk through this in debugger/UML model
        String typeDefName = entityDetail.getType().getTypeDefName();

        switch (typeDefName) {
            case RELATIONAL_COLUMN:
                resourceName = "MyColumn <placeholder>";
                break;
            case RELATIONAL_TABLE:
                resourceName = "MyTable <placeholder>";
                break;
            default:
                resourceName = "MyResourceWithAnotherName <placeholder>";
                break;
        }

        return resourceName;

    }

    /**
     * Returns the list of governed assets with associated tags
     *
     * @param userId    - String - userId of user making request.
     * @param assetGuid - guid of the asset component.
     * @return Connection retrieved from property handlers
     * @throws InvalidParameterException       - one of the parameters is null or invalid.
     * @throws GuidNotFoundException           - specified guid is not found
     * @throws MetadataServerException         - there is a problem retrieving information from the metadata server.
     * @throws UserNotAuthorizedException      - the requesting user is not authorized to issue this request.
     */
    public GovernedAsset getGovernedAsset(String userId,
                                          String assetGuid
    ) throws InvalidParameterException,
            MetadataServerException,
            UserNotAuthorizedException, GuidNotFoundException {

        final String methodName = "getGovernedAsset";
        final String assetParm = "assetGuid";

        GovernedAsset defToReturn = new GovernedAsset();

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(assetGuid, assetParm, methodName);

/*        try {
            // do some things here
            int i=0;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

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

        }*/
        return defToReturn;
    }

    private String getPropertyValue(InstanceProperties instanceProperties, String propertyName) {

        PrimitivePropertyValue value = (PrimitivePropertyValue) instanceProperties.getPropertyValue(propertyName);
        if (value != null) {
            return (String) value.getPrimitiveValue();
        }

        return null;
    }

}
