/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;


import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.ClassificationNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassificationDef;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassificationDefAPIResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ConnectionHandler retrieves Connection objects from the property handlers.  It runs handlers-side in the AssetConsumer
 * OMAS and retrieves Connections through the OMRSRepositoryConnector.
 */
public class GovernanceClassificationDefHandler {
    private  OMRSMetadataCollection metadataCollection=null;

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
     */
    public GovernanceClassificationDefHandler(String serviceName,
                                              OMRSRepositoryConnector repositoryConnector) {
        this.serviceName = serviceName;
        if (repositoryConnector != null) {
            try {
                this.repositoryHelper = repositoryConnector.getRepositoryHelper();
                this.serverName = repositoryConnector.getServerName();
                this.metadataCollection = repositoryConnector.getMetadataCollection();
            } catch (RepositoryErrorException re) {
                //TODO Log appropriate error if we have a bad repository connection to metadata
            } ;
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
        final String methodName = "getGovernanceClassificationDefs";
        final String nameParameter = "name";
        List<GovernanceClassificationDef> defsToReturn=null;

        //TODO: Should use api code for this validation in client and server
        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateClassification(rootClassification, nameParameter, methodName);

        // Search for all classification types that match our criteria
        try {
            List<TypeDef> typeDefsByCategory = metadataCollection.findTypeDefsByCategory(userId, TypeDefCategory.CLASSIFICATION_DEF);
            // We just need the name and parameter
            //TODO Needs to restrict classifications by parent - for now returns ALL classifications
            typeDefsByCategory.forEach((td) -> {
                //TODO federation: resolve what to do if we have duplicate names (with different definitions)
                GovernanceClassificationDef gcd = new GovernanceClassificationDef();
                gcd.setName(td.getName());
                Map<String,String> gdca = new HashMap<>();
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

        }

        //TODO Figure out exception handling
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception
         .InvalidParameterException e) {
                    } catch (RepositoryErrorException e) {
                    } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
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
     * @throws PropertyServerException    - there is a problem retrieving information from the property (metadata) handlers.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GovernanceClassificationDef getGovernanceClassificationDefinition(String userId,
                                                                             String tagguid) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {

        GovernanceClassificationDef defToReturn=null;
        TypeDef typeDef;


        final String methodName = "getGovernanceClassificationDef";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(tagguid, "tagguid", methodName);

        // Search for all classification types that match our criteria
        // getTypedefByCategory(CLASSIFICATION_DEF)
        try {
            TypeDef td = metadataCollection.getTypeDefByGUID(userId, tagguid);
                GovernanceClassificationDef gcd = new GovernanceClassificationDef();
                gcd.setName(td.getName());
                Map<String,String> gdca = new HashMap<>();
                //Get the parms
                td.getPropertiesDefinition().forEach((tda) -> {
                    // TODO Mapping of types between OMRS and Ranger should be abstracted
                    // TODO Mapping of alpha name is fragile - temporary for initial debug
                    gdca.put(tda.getAttributeName(), tda.getAttributeType().getName());
                });
                gcd.setAttributeDefinitions(gdca);
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e1) {
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e1) {
        } catch (RepositoryErrorException e1) {
        } catch (TypeDefNotKnownException e1) {
        }
        //TODO Figure out exception handling
              return defToReturn;
    }

    GovernanceClassificationDefAPIResponse getTagsFromRepository(String name, OMRSMetadataCollection metadataCollection, EntityDetail entity) {
        //TODO: Needs implementing & signature changed as this needs to reformat the set - we can't return NULL...
        return null;
    }
}
