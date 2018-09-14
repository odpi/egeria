/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;


import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassificationUsage;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
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
     * @throws MetadataServerException - there is a problem retrieving information from the metadata server
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
     * @param type           - types to start query from.
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



        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateClassification(classification, classificationParameter, methodName);
        errorHandler.validateType(classification, typeParameter, methodName);

        //TODO: Add tighter query using type and classification (TBD)

        //TODO Refactoring of common code with GovernanceClassificationDefs

        if (type==null)
        {
            addToAssetListByType(assetsToReturn,null,classification,userId);
        }
        else
        {
            type.forEach((typesearch) -> {
                // TODO Mapping of types between OMRS and Ranger should be abstracted
                // TODO Mapping of alpha name is fragile - temporary for initial debug
                addToAssetListByType(assetsToReturn,typesearch,classification,userId);
            });
        }

        return assetsToReturn;
    }

    private void addToAssetListByType(List<GovernedAsset> assetsToReturn, String type,
                                      List<String>classification, String userId ) {

        // We know the type, let's do this by classification now

        if (classification == null) {
            addToAssetListByClassification(assetsToReturn, type,null, userId);
        } else {
            classification.forEach((classificationsearch) -> {
                addToAssetListByClassification(assetsToReturn, type,classificationsearch, userId);
            });
        }

        return;
    }

    private void addToAssetListByClassification(List<GovernedAsset> assetsToReturn,
                                                String type,String classification, String userId) {


        List<EntityDetail> entities =null;

        String typeGuid = getTypeGuidFromTypeName(type,userId);


            try {
                entities = metadataCollection.findEntitiesByClassification(userId, typeGuid,
                        classification,
                        null, null, 0,
                        null,
                        null, null, null, 0);

            } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
                e.printStackTrace();
            } catch (TypeErrorException e) {
                e.printStackTrace();
            } catch (RepositoryErrorException e) {
                e.printStackTrace();
            } catch (ClassificationErrorException e) {
                e.printStackTrace();
            } catch (PropertyErrorException e) {
                e.printStackTrace();
            } catch (PagingErrorException e) {
                e.printStackTrace();
            } catch (FunctionNotSupportedException e) {
                e.printStackTrace();
            } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
                e.printStackTrace();
            }

            // We have the asset -- add to return
        if (entities!=null) {
            entities.forEach((entity) -> {
                GovernedAsset entry=addEntityIfDoesntExist(assetsToReturn,entity);
                addClassificationInfoToEntry(entry,entity,classification);
            });

        }
    }

    private void addClassificationInfoToEntry(GovernedAsset entry, EntityDetail entity,
                                              String classification) {
        // Just add this classification info - the current methods are convoluted, but useful to prove out concept
        // and understand the API. Also consideration to be made to using the metadata collection helpers/extending
        // TODO Refactor

        // Get the current list of assigned classification
         List<GovernanceClassificationUsage> usageList = entry.getAssignedGovernanceClassifications();

        // Add the new assignment locally (in case of copying)
        GovernanceClassificationUsage usage = new GovernanceClassificationUsage();

        // Only work on the classification passed to this method (inefficient)
        // Demonstration using a set rather than list may work a lot better - skip for now to avoid method sig changes

        Classification entityClassification = entity.getClassifications().stream()
                .filter(c -> classification.equals(c.getName()))
                .findAny()
                .orElse(null);

        // Now found it in the of assigned classifications for this entity
        if (entityClassification!=null) {

            // So now let's add into the assigned classifications list
            usage.setName(classification);

            // And now let's pull in the properties
            Map<String,String> m = new HashMap<>();
            Map<String, InstancePropertyValue> ip=entityClassification.getProperties().getInstanceProperties();

            //mapping them to our map
            ip.entrySet().stream().forEach(props -> {
                // TODO Mapping of types between OMRS and Ranger should be abstracted
                // TODO Mapping of alpha name is fragile - temporary for initial debug
                m.put(props.getKey(),props.getValue().toString());
            });

            // And set them back
            usage.setAttributeValues(m);
            usageList.add(usage);
            // Now push it back to the list
            entry.setAssignedGovernanceClassifications(usageList);
        }
    }

    private GovernedAsset addEntityIfDoesntExist(List<GovernedAsset> assetsToReturn, EntityDetail entity) {

        GovernedAsset ga = new GovernedAsset();
        ga.setGuid(entity.getGUID());
        ga.setType(entity.getType().getTypeDefName());

        //TODO get name of entity - use this as a key
        ga.setFqName(getGovernanceResourceNameFromEntity(entity));
        // See if we can find this asset in the list - if not we'll add
        GovernedAsset existingEntry = assetsToReturn.stream()
                .filter(govasset -> ga.getGuid().equals(govasset.getGuid()))
                .findAny()
                .orElse(null);
        if (existingEntry==null) {
            assetsToReturn.add(ga);
            existingEntry=ga;
        }
        return(existingEntry);


    }

    private String getTypeGuidFromTypeName(String type, String userId) {

        String guid = new String();

        // TODO Decided how to handle exceptions. For now we'll ensure an empty String is returned
        try {
            guid = metadataCollection.getTypeDefByName(userId, type).getGUID();
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
        } catch (RepositoryErrorException e) {
        } catch (TypeDefNotKnownException e) {
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
        }

        return(guid);
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
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws GuidNotFoundException      - specified guid is not found
     * @throws MetadataServerException    - there is a problem retrieving information from the metadata server.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
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
