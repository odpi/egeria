/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;


import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.ClassificationNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernanceClassificationDef;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernanceClassificationDefAPIResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

/**
 * ConnectionHandler retrieves Connection objects from the property handlers.  It runs handlers-side in the AssetConsumer
 * OMAS and retrieves Connections through the OMRSRepositoryConnector.
 */
public class GovernanceClassificationDefinitionHandler {
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
    public GovernanceClassificationDefinitionHandler(String serviceName,
                                                     OMRSRepositoryConnector repositoryConnector) {
        this.serviceName = serviceName;
        if (repositoryConnector != null) {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.serverName = repositoryConnector.getServerName();
            errorHandler = new Validator(repositoryConnector);
        }
    }


    /**
     * Returns the list of tag definitions.
     *
     * @param userId             - String - userId of user making request.
     * @param rootClassification - this may be the qualifiedName or displayName of the connection.
     * @return Connection retrieved from property handlers
     * @throws InvalidParameterException           - one of the parameters is null or invalid.
     * @throws ClassificationNotFoundException - there is no connection defined for this name.
     * @throws PropertyServerException             - there is a problem retrieving information from the property (metadata) handlers.
     * @throws UserNotAuthorizedException          - the requesting user is not authorized to issue this request.
     */
    public List<GovernanceClassificationDef> getGovernanceClassificationDefinitions(String userId,
                                                                                    List<String> rootClassification) throws InvalidParameterException,
            ClassificationNotFoundException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "getGovernanceClassificationDefinitions";
        final String nameParameter = "name";

        //TODO: Should use common code for this validation in client and server

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateRootClassification(rootClassification, nameParameter, methodName);

        //OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);


//TODO Implement query code
        return null;
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
     * @throws PropertyServerException    - there is a problem retrieving information from the property (metadata) handlers.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GovernanceClassificationDef getGovernanceClassificationDefinition(String userId,
                                                                             String tagguid) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "getGovernanceClassificationDef";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(tagguid, "tagguid", methodName);

        //OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        //TODO Implement query
        return null;
    }

    GovernanceClassificationDefAPIResponse getTagsFromRepository(String name, OMRSMetadataCollection metadataCollection, EntityDetail entity) {
        //TODO: Needs implementing & signature changed as this needs to reformat the set - we can't return NULL...
        return null;
    }
}
