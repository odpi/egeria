/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;


import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernedAssetComponent;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

/**
 * ConnectionHandler retrieves Connection objects from the property handlers.  It runs handlers-side in the AssetConsumer
 * OMAS and retrieves Connections through the OMRSRepositoryConnector.
 */
public class GovernedAssetHandler {
    //TODO: Figure out what's needed for handler method

    private static final String connectionTypeGUID = "114e9f8f-5ff3-4c32-bd37-a7eb42712253";

    private String serviceName;
    private OMRSRepositoryHelper repositoryHelper = null;
    private String serverName = null;
    private Validator errorHandler = null;

    /**
     * Construct the connection handler with a link to the property handlers's connector and this access service's
     * official name.
     *
     * @param serviceName         - name of this service
     * @param repositoryConnector - connector to the property handlers.
     */
    public GovernedAssetHandler(String serviceName,
                                OMRSRepositoryConnector repositoryConnector) {
        this.serviceName = serviceName;
        if (repositoryConnector != null) {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.serverName = repositoryConnector.getServerName();
            errorHandler = new Validator(repositoryConnector);
        }
    }


    /**
     * Returns the list of governed assets with associated tags
     *
     * @param userId             - String - userId of user making request.
     * @param rootClassification - this may be the qualifiedName or displayName of the connection.
     * @return Connection retrieved from property handlers
     * @throws InvalidParameterException           - one of the parameters is null or invalid.
     * @throws RootClassificationNotFoundException - there is no connection defined for this name.
     * @throws RootAssetTypeNotFoundException      - there is no connection defined for this name.
     * @throws PropertyServerException             - there is a problem retrieving information from the property (metadata) handlers.
     * @throws UserNotAuthorizedException          - the requesting user is not authorized to issue this request.
     */
    public List<GovernedAssetComponent> getGovernedAssetComponents(String userId,
                                                                   List<String> rootClassification,
                                                                   List<String> rootAssetType) throws InvalidParameterException,
            RootClassificationNotFoundException,
            RootAssetTypeNotFoundException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "getGovernedAssetComponents";
        final String rootClassificationParameter = "rootClassification";
        final String rootAssetTypeParameter = "rootAssetType";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateRootClassification(rootClassification, rootClassificationParameter, methodName);
        errorHandler.validateRootAssetType(rootClassification, rootAssetTypeParameter, methodName);

        //OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        //TODO: This will retrieve all classifications for now - does not support starting from a root classification

        /*
         * --->
         *
         try {

            // This is method specific -- here we would want to retrieve relevant tags...
            InstanceProperties properties = null;

            //TODO: This will retrieve all classifications for now - does not support starting from a root classification
            //properties = repositoryHelper.addStringPropertyToInstance(serviceName,
            //                                                          properties,
            //                                                          qualifiedNamePropertyName,
            //                                                          name,
            //                                                          methodName);


            //TODO: what guid/calls do we need here?

            List<EntityDetail> tags = metadataCollection.findEntitiesByProperty(userId,
                    connectionTypeGUID,
                    properties,
                    MatchCriteria.ANY,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    2);
            // Empty results are fine - there are no tags to retrieve
            //return this.getTagsFromRepository(userId, metadataCollection, tags.get(0));

            return null; //TODO: Fix this

            // TODO : Fill out exceptions that need working

        } catch (Exception e) {
        }
        /* catch (org.apache.atlas.omrs.ffdc.exception.UserNotAuthorizedException error) {
            errorHandler.handleUnauthorizedUser(userId,
                    methodName,
                    serverName,
                    serviceName);
        } catch (Throwable error) {
            errorHandler.handleRepositoryError(error,
                    methodName,
                    serverName,
                    serviceName);
        }
        */
        return null;

    }


    /**
     * Returns the list of governed assets with associated tags
     *
     * @param userId    - String - userId of user making request.
     * @param assetGuid - guid of the asset component.
     * @return Connection retrieved from property handlers
     * @throws InvalidParameterException           - one of the parameters is null or invalid.
     * @throws RootClassificationNotFoundException - there is no connection defined for this name.
     * @throws RootAssetTypeNotFoundException      - there is no connection defined for this name.
     * @throws PropertyServerException             - there is a problem retrieving information from the property (metadata) handlers.
     * @throws UserNotAuthorizedException          - the requesting user is not authorized to issue this request.
     */
    public GovernedAssetComponent getGovernedAssetComponent(String userId,
                                                            String assetGuid
    ) throws InvalidParameterException,
            RootClassificationNotFoundException,
            RootAssetTypeNotFoundException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "getGovernedAssetComponent";

        final String assetParm = "assetGuid";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(assetGuid, assetParm, methodName);

        //OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        //TODO: This will retrieve all classifications for now - does not support starting from a root classification

        /*
         * --->
         *
         try {

            // This is method specific -- here we would want to retrieve relevant tags...
            InstanceProperties properties = null;

            //TODO: This will retrieve all classifications for now - does not support starting from a root classification
            //properties = repositoryHelper.addStringPropertyToInstance(serviceName,
            //                                                          properties,
            //                                                          qualifiedNamePropertyName,
            //                                                          name,
            //                                                          methodName);


            //TODO: what guid/calls do we need here?

            List<EntityDetail> tags = metadataCollection.findEntitiesByProperty(userId,
                    connectionTypeGUID,
                    properties,
                    MatchCriteria.ANY,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    2);
            // Empty results are fine - there are no tags to retrieve
            //return this.getTagsFromRepository(userId, metadataCollection, tags.get(0));

            return null; //TODO: Fix this

            // TODO : Fill out exceptions that need working

        } catch (Exception e) {
        }
        /* catch (org.apache.atlas.omrs.ffdc.exception.UserNotAuthorizedException error) {
            errorHandler.handleUnauthorizedUser(userId,
                    methodName,
                    serverName,
                    serviceName);
        } catch (Throwable error) {
            errorHandler.handleRepositoryError(error,
                    methodName,
                    serverName,
                    serviceName);
        }
        */
        return null;

    }

}
