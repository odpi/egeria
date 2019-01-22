/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.Context;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassification;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.accessservices.governanceengine.server.processor.ContextBuilder;
import org.odpi.openmetadata.accessservices.governanceengine.server.util.PropertyUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.CONFIDENCE;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.CONFIDENTIALITY;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.CRITICALITY;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.DISPLAY_NAME;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.GLOSSARY_TERM;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.QUALIFIED_NAME;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.RETENTION;

/**
 * ConnectionHandler retrieves Connection objects from the property handlers.  It runs handlers-side in the AssetConsumer
 * OMAS and retrieves Connections through the OMRSRepositoryConnector.
 */
public class GovernedAssetHandler {

    private static final Logger log = LoggerFactory.getLogger(GovernedAssetHandler.class);
    private OMRSMetadataCollection metadataCollection;

    private Map<String, String> knownTypeDefs = new HashMap<>();
    private Set<String> governedClassifications = setGovernedClassifications();
    private ContextBuilder contextBuilder = new ContextBuilder();

    /**
     * Construct the connection handler with a link to the property handlers's connector and this access service's
     * official name.
     *
     * @param repositoryConnector - connector to the property handlers.
     * @throws MetadataServerException - there is a problem retrieving information from the metadata server
     */
    public GovernedAssetHandler(OMRSRepositoryConnector repositoryConnector) throws MetadataServerException {
        final String methodName = "GovernedAssetHandler";

        if (repositoryConnector != null) {
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
     * Returns the list of governed assets with associated tags
     *
     * @param userId         - String - userId of user making request.
     * @param classification - classifications to start query from .
     * @param type           - types to start query from.
     * @return List of Governed Access
     */
    public List<GovernedAsset> getGovernedAssets(String userId,
                                                 List<String> classification,
                                                 List<String> type) throws InvalidParameterException, EntityProxyOnlyException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException {
        final String methodName = "getGovernedAssets";
        final String classificationParameter = "classification";
        final String typeParameter = "type";

        GovernanceEngineValidator.validateUserId(userId, methodName);
        GovernanceEngineValidator.validateClassification(classification, classificationParameter, methodName);
        GovernanceEngineValidator.validateType(classification, typeParameter, methodName);

        if (classification == null) {
            List<String> classificationTypeDef = getClassificationsDef(userId);
            if (classificationTypeDef != null) {
                classification = new ArrayList<>(classificationTypeDef);
            }
        }

        List<GovernedAsset> response = new ArrayList<>();
        if (type == null) {
            return addToAssetListByType(null, classification, userId);
        } else {
            for (String searchedType : type) {
                List<GovernedAsset> assetsByType = addToAssetListByType(searchedType, classification, userId);
                if (assetsByType != null) {
                    response.addAll(assetsByType);
                }
            }
        }
        return response;
    }

    /**
     * Returns the list of governed assets with associated tags
     *
     * @param userId    - String - userId of user making request.
     * @param assetGuid - guid of the asset component.
     * @return Connection retrieved from property handlers
     * @throws InvalidParameterException - one of the parameters is null or invalid.
     */
    public GovernedAsset getGovernedAsset(String userId, String assetGuid) throws InvalidParameterException {
        final String methodName = "getGovernedAsset";
        final String assetParm = "assetGuid";

        GovernanceEngineValidator.validateUserId(userId, methodName);
        GovernanceEngineValidator.validateGUID(assetGuid, assetParm, methodName);

        return new GovernedAsset();
    }

    public boolean containsGovernedClassification(EntityDetail entityDetail) {
        if (entityDetail.getClassifications() == null || entityDetail.getClassifications().isEmpty()) {
            return false;
        }

        for (Classification classification : entityDetail.getClassifications()) {
            if (isGovernedClassification(classification.getType().getTypeDefName())) {
                return true;
            }
        }

        return false;
    }

    public List<GovernanceClassification> getGovernanceClassifications(List<Classification> allClassifications) {
        List<Classification> governedClassifications = filterGovernedClassifications(allClassifications);

        List<GovernanceClassification> classifications = new ArrayList<>(governedClassifications.size());

        for (Classification classification : governedClassifications) {
            classifications.add(getGovernanceClassification(classification));
        }
        return classifications;
    }

    private Set<String> setGovernedClassifications() {
        Set<String> classifications = new HashSet<>(4);
        classifications.add(CONFIDENTIALITY);
        classifications.add(CONFIDENCE);
        classifications.add(CRITICALITY);
        classifications.add(RETENTION);
        return classifications;
    }

    private List<GovernedAsset> addToAssetListByType(String type, List<String> classification, String userId) throws EntityProxyOnlyException, TypeErrorException, TypeDefNotKnownException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException {
        if (classification == null) {
            return Collections.emptyList();
        }

        List<GovernedAsset> response = new ArrayList<>();
        for (String searchedClassifications : classification) {
            List<GovernedAsset> assetsToReturn = addToAssetListByClassification(type, searchedClassifications, userId);
            response.addAll(assetsToReturn);
        }

        return response;
    }

    private List<String> getClassificationsDef(String userId) {
        try {
            List<TypeDef> classificationsDef = metadataCollection.findTypeDefsByCategory(userId, TypeDefCategory.CLASSIFICATION_DEF);
            return classificationsDef.stream().map(TypeDefLink::getName).collect(Collectors.toList());
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException | org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException | RepositoryErrorException e) {
            log.error(e.getErrorMessage());
        }

        return Collections.emptyList();
    }

    private List<GovernedAsset> addToAssetListByClassification(String type, String classification, String userId) throws EntityProxyOnlyException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException {
        String typeGuid = getTypeGuidFromTypeName(type, userId);
        List<EntityDetail> entities = getEntitiesByClassification(classification, userId, typeGuid);

        List<GovernedAsset> assetsToReturn = new ArrayList<>();
        if (entities != null) {
            for (EntityDetail entity : entities) {
                GovernedAsset entry = existingGovernedAsset(assetsToReturn, entity);

                if (entry == null) {
                    entry = getGovernedAsset(entity);
                    assetsToReturn.add(entry);
                }

                addClassificationInfoToEntry(entry, entity);
            }
        }
        return assetsToReturn;
    }

    private GovernedAsset existingGovernedAsset(List<GovernedAsset> assetsToReturn, EntityDetail entity) {
        Optional<GovernedAsset> asset = assetsToReturn.stream().filter(s -> entity.getGUID().equals(s.getGuid())).findFirst();
        return asset.orElse(null);
    }

    private List<EntityDetail> getEntitiesByClassification(String classification, String userId, String typeGuid) {
        try {
            return metadataCollection.findEntitiesByClassification(userId,
                    typeGuid,
                    classification,
                    null,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    0);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException | org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException | FunctionNotSupportedException | PagingErrorException | PropertyErrorException | ClassificationErrorException | RepositoryErrorException | TypeErrorException e) {
            log.error(e.getErrorMessage());
        }
        return Collections.emptyList();
    }


    private void addClassificationInfoToEntry(GovernedAsset entry, EntityDetail governedAsset) {

        final List<Classification> classifications = filterGovernedClassifications(governedAsset.getClassifications());
        if (classifications == null || classifications.isEmpty()) {
            return;
        }

        List<GovernanceClassification> governanceClassifications = getGovernanceClassifications(entry, classifications);
        if (governanceClassifications.isEmpty()) {
            return;
        }

        List<GovernanceClassification> usageList = entry.getAssignedGovernanceClassifications();
        if (usageList == null) {
            usageList = new ArrayList<>();
        }
        usageList.addAll(governanceClassifications);
        entry.setAssignedGovernanceClassifications(usageList);
    }

    private List<GovernanceClassification> getGovernanceClassifications(GovernedAsset entry, List<Classification> classifications) {
        List<GovernanceClassification> governanceClassifications = new ArrayList<>();
        for (Classification classification : classifications) {
            if (containsGovernedClassification(entry, classification)) {
                continue;
            }
            GovernanceClassification governanceClassification = getGovernanceClassification(classification);
            governanceClassifications.add(governanceClassification);
        }
        return governanceClassifications;
    }

    private GovernanceClassification getGovernanceClassification(Classification classification) {
        GovernanceClassification governanceClassification = new GovernanceClassification();

        governanceClassification.setName(classification.getName());
        Map<String, String> attributes = getInstanceProperties(classification);
        governanceClassification.setAttributes(attributes);

        return governanceClassification;
    }

    private boolean containsGovernedClassification(GovernedAsset entry, Classification classification) {
        if (entry.getAssignedGovernanceClassifications() == null || entry.getAssignedGovernanceClassifications().isEmpty()) {
            return false;
        }
        final long count = entry.getAssignedGovernanceClassifications().stream().filter(c -> c.getName().equals(classification.getType().getTypeDefName())).count();
        return count != 0;
    }

    private Map<String, String> getInstanceProperties(Classification classification) {
        Map<String, String> attributes = new HashMap<>();

        InstanceProperties properties = classification.getProperties();
        if (properties != null) {
            Map<String, InstancePropertyValue> instanceProperties = properties.getInstanceProperties();
            if (instanceProperties != null) {
                instanceProperties.forEach((key, value) -> attributes.put(key, PropertyUtils.getStringForPropertyValue(value)));
            }
        }
        return attributes;
    }

    private List<Classification> filterGovernedClassifications(List<Classification> classifications) {
        return classifications.stream()
                .filter(c -> isGovernedClassification(c.getType().getTypeDefName()))
                .collect(Collectors.toList());
    }

    private boolean isGovernedClassification(String classificationName) {
        return governedClassifications.contains(classificationName);
    }


    public GovernedAsset getGovernedAsset(EntityDetail entity) throws EntityProxyOnlyException, TypeErrorException, TypeDefNotKnownException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException {
        GovernedAsset governedAsset = new GovernedAsset();

        governedAsset.setGuid(entity.getGUID());
        governedAsset.setType(entity.getType().getTypeDefName());
        governedAsset.setFullQualifiedName(getResourceValue(entity, QUALIFIED_NAME));
        governedAsset.setName(getResourceValue(entity, DISPLAY_NAME));
        governedAsset.setContexts(buildDatabaseContext(entity));

        return governedAsset;
    }

    private List<Context> buildDatabaseContext(EntityDetail entity) throws EntityProxyOnlyException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException {
        switch (entity.getType().getTypeDefName()) {
            case RELATIONAL_COLUMN:
                return contextBuilder.buildContextForColumn(metadataCollection, entity.getGUID());
            case RELATIONAL_TABLE:
                return contextBuilder.buildContextForTable(metadataCollection, entity.getGUID());
            case GLOSSARY_TERM:
                return contextBuilder.buildContextForGlossaryTerm(metadataCollection, entity.getGUID());
            default:
                return new ArrayList<>();
        }
    }

    private String getTypeGuidFromTypeName(String typeName, String userId) {

        try {
            if (knownTypeDefs.containsKey(typeName)) {
                return knownTypeDefs.get(typeName);
            } else {
                final String typeDefGuid = metadataCollection.getTypeDefByName(userId, typeName).getGUID();
                knownTypeDefs.put(typeName, typeDefGuid);
                return typeDefGuid;
            }
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException | org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException | TypeDefNotKnownException | RepositoryErrorException e) {
            log.error(e.getErrorMessage());
        }

        return null;
    }

    private String getResourceValue(EntityDetail entityDetail, String propertyName) {
        InstanceProperties instanceProperties = entityDetail.getProperties();

        if (instanceProperties.getPropertyValue(propertyName) instanceof PrimitivePropertyValue) {
            PrimitivePropertyValue value = (PrimitivePropertyValue) instanceProperties.getPropertyValue(propertyName);
            if (value != null && value.getPrimitiveValue() instanceof String) {
                return (String) value.getPrimitiveValue();
            }
        }
        return null;
    }
}
