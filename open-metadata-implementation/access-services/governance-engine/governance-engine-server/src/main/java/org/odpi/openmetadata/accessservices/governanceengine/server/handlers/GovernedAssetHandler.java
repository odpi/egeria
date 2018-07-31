/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;


import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassificationDef;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

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

    private OMRSMetadataCollection metadataCollection=null;
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
    public GovernedAssetHandler(String serviceName,
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
     * Returns the list of governed assets with associated tags
     *
     * @param userId             - String - userId of user making request.
     * @param rootClassification - this may be the qualifiedName or displayName of the connection.
     * @return Connection retrieved from property handlers
     * @throws InvalidParameterException           - one of the parameters is null or invalid.
     * @throws ClassificationNotFoundException - there is no connection defined for this name.
     * @throws TypeNotFoundException      - there is no connection defined for this name.
     * @throws PropertyServerException             - there is a problem retrieving information from the property (metadata) handlers.
     * @throws UserNotAuthorizedException          - the requesting user is not authorized to issue this request.
     */
    public List<GovernedAsset> getGovernedAssets(String userId,
                                                 List<String> rootClassification,
                                                 List<String> rootAssetType) throws InvalidParameterException,
            ClassificationNotFoundException,
            TypeNotFoundException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "getGovernedAssets";
        final String rootClassificationParameter = "rootClassification";
        final String rootAssetTypeParameter = "rootAssetType";

        List<GovernedAsset> assetsToReturn = null;

        List<GovernanceClassificationDef> interestingClassifications = null;


        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateClassification(rootClassification, rootClassificationParameter, methodName);
        errorHandler.validateType(rootClassification, rootAssetTypeParameter, methodName);

        //TODO: This will retrieve all assets for now - does not support starting from a root classification, nor 
        // selection on type

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

                String assetTypeToSearchFor = null;

                // Now let's get the entities

                //TODO Need to iterate through all possible asset types - for now we will take first in the list
                if (rootAssetType.size() >= 1)
                     assetTypeToSearchFor = new String(rootAssetType.get(0));

                // TODO Pagination & sort order is needed in these APIs - for now do not specify limits
                //TODO We may want to restrict to active entities only
                List<EntityDetail> entities = null;
                // TODO Exceptions need handling before this method is usable!
                try {
                    entities = metadataCollection.findEntitiesByClassification(userId, assetTypeToSearchFor,
                            td.getName() ,
                            null, null, 0,
                            null,
                            null, null, null, 0);
                }
                //TODO Handle exceptions
                catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
                } catch (TypeErrorException e) {
                } catch (RepositoryErrorException e) {
                } catch (ClassificationErrorException e) {
                } catch (PropertyErrorException e) {
                } catch (PagingErrorException e) {
                } catch (FunctionNotSupportedException e) {
                } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
                }

                // Now for each entity we need to get it's fq name, and/or follow through to the schema
                entities.forEach(entityDetail -> {
                    String resourceName = getGovernanceResourceNameFromEntity(entityDetail);
                });
            });

            // for the interested classifications, get all entities assigned that classification

            // Return the data

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            e.printStackTrace();
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            e.printStackTrace();
        } catch (RepositoryErrorException e) {
            e.printStackTrace();
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
     * @throws InvalidParameterException           - one of the parameters is null or invalid.
     * @throws ClassificationNotFoundException - there is no connection defined for this name.
     * @throws TypeNotFoundException      - there is no connection defined for this name.
     * @throws PropertyServerException             - there is a problem retrieving information from the property (metadata) handlers.
     * @throws UserNotAuthorizedException          - the requesting user is not authorized to issue this request.
     */
    public GovernedAsset getGovernedAsset(String userId,
                                          String assetGuid
    ) throws InvalidParameterException,
            ClassificationNotFoundException,
            TypeNotFoundException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "getGovernedAsset";

        final String assetParm = "assetGuid";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(assetGuid, assetParm, methodName);


        return null;

    }

    private String getPropertyValue(InstanceProperties instanceProperties, String propertyName) {

        PrimitivePropertyValue value = (PrimitivePropertyValue) instanceProperties.getPropertyValue(propertyName);
        if (value != null) {
            return (String) value.getPrimitiveValue();
        }

        return null;
    }

}
