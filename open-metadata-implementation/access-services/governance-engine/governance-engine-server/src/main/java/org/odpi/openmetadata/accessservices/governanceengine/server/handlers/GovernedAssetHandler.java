/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.Context;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassification;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.governanceengine.server.processor.ContextBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.DESCRIPTION;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.DISPLAY_NAME;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.GOVERNANCE_ENGINE;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.NAME;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.PATCH_LEVEL;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.QUALIFIED_NAME;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.SECURITY_LABELS;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.SECURITY_PROPERTIES;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.SECURITY_TAG;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.SOFTWARE_SERVER_CAPABILITY;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.SOFTWARE_SERVER_CAPABILITY_GUID;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.SOURCE;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.TYPE;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.VERSION;

/**
 * ConnectionHandler retrieves Connection objects from the property handlers.  It runs handlers-side in the AssetConsumer
 * OMAS and retrieves Connections through the OMRSRepositoryConnector.
 */
public class GovernedAssetHandler {

    private OMRSMetadataCollection metadataCollection;
    private OMRSRepositoryConnector repositoryConnector;
    private Map<String, String> knownTypeDefs = new HashMap<>();
    private ContextBuilder contextBuilder = new ContextBuilder();
    private OMRSRepositoryHelper repositoryHelper;

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
                this.repositoryConnector = repositoryConnector;
                this.metadataCollection = repositoryConnector.getMetadataCollection();
                this.repositoryHelper = repositoryConnector.getRepositoryHelper();
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
     * @param userId - String - userId of user making request.
     * @param type   - types to start query from.
     * @return List of Governed Access
     * @throws InvalidParameterException bad parameter
     * @throws EntityProxyOnlyException full entity not available
     * @throws TypeErrorException error in type
     * @throws FunctionNotSupportedException function not supported
     * @throws PropertyErrorException bad property parameter
     * @throws EntityNotKnownException unknown entity
     * @throws TypeDefNotKnownException unknown type
     * @throws PagingErrorException bad paging parameters
     * @throws UserNotAuthorizedException user not authorized
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException bad parameter
     * @throws RepositoryErrorException repository not available
     * @throws ClassificationErrorException invalid classification
     */
    public List<GovernedAsset> getGovernedAssets(String userId,
                                                 List<String> type) throws InvalidParameterException, EntityProxyOnlyException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, ClassificationErrorException {
        final String methodName = "getGovernedAssets";
        GovernanceEngineValidator.validateUserId(userId, methodName);

        List<EntityDetail> response = new ArrayList<>();

        if (type == null) {
            response = getEntitiesByClassification(userId, null);
        } else {
            for (String searchedType : type) {
                String typeGUID = getTypeGuidFromTypeName(userId, searchedType);
                response.addAll(getEntitiesByClassification(userId, typeGUID));
            }
        }
        return getGovernedAssets(response);
    }

    /**
     * Returns the list of governed assets with associated tags
     *
     * @param userId    - String - userId of user making request.
     * @param assetGuid - guid of the asset component.
     * @return Connection retrieved from property handlers
     * @throws InvalidParameterException - one of the parameters is null or invalid.
     */
    public GovernedAsset getGovernedAsset(String userId, String assetGuid) throws InvalidParameterException, EntityProxyOnlyException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException {
        final String methodName = "getGovernedAsset";
        final String assetParameter = "assetGuid";

        GovernanceEngineValidator.validateUserId(userId, methodName);
        GovernanceEngineValidator.validateGUID(assetGuid, assetParameter, methodName);

        EntityDetail entityDetail = getEntityDetailById(userId, assetGuid);
        if (entityDetail == null) {
            return null;
        }

        return getGovernedAsset(entityDetail);
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

    public boolean isSchemaElement(EntityDetail entityDetail) throws UserNotAuthorizedException, RepositoryErrorException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, TypeDefNotKnownException
    {
        InstanceType entityType = entityDetail.getType();
        if (entityType != null)
        {
            /*
             * Is this type a schema attribute?
             */
            if (repositoryHelper.isTypeOf(GOVERNANCE_ENGINE, entityType.getTypeDefName(), SCHEMA_ATTRIBUTE))
            {
                return true;
            }
        }

        return false;
    }

    public GovernedAsset getGovernedAsset(EntityDetail entity) throws EntityProxyOnlyException, TypeErrorException, TypeDefNotKnownException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException {
        GovernedAsset governedAsset = new GovernedAsset();

        governedAsset.setGuid(entity.getGUID());
        governedAsset.setType(entity.getType().getTypeDefName());
        governedAsset.setFullQualifiedName(getResourceValue(entity, QUALIFIED_NAME));
        governedAsset.setName(getResourceValue(entity, DISPLAY_NAME));
        governedAsset.setContext(buildContext(entity));

        if (entity.getClassifications() != null && !entity.getClassifications().isEmpty()) {
            governedAsset.setAssignedGovernanceClassification(getGovernanceClassification(entity.getClassifications()));
        }

        return governedAsset;
    }

    public SoftwareServerCapability createSoftwareServerCapability(String userId, SoftwareServerCapability softwareServerCapability) throws ClassificationErrorException, StatusNotSupportedException, UserNotAuthorizedException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException {

        InstanceProperties initialProperties = getSoftwareServerCapabilityProperties(softwareServerCapability);

        EntityDetail entityDetail = metadataCollection.addEntity(userId,
                SOFTWARE_SERVER_CAPABILITY_GUID,
                initialProperties,
                Collections.emptyList(),
                InstanceStatus.ACTIVE);

        return getSoftwareServer(entityDetail);
    }

    public SoftwareServerCapability getSoftwareServerCapabilityByGUID(String userId, String guid) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, EntityNotKnownException {

        EntityDetail entityDetail = metadataCollection.getEntityDetail(userId, guid);
        if (entityDetail.getType().getTypeDefName().equals(SOFTWARE_SERVER_CAPABILITY)) {
            return getSoftwareServer(entityDetail);
        }

        return null;
    }

    private GovernanceClassification getGovernanceClassification(List<Classification> allClassifications) {
        Optional<Classification> classification = filterGovernedClassification(allClassifications);

        return classification.map(this::getGovernanceClassification).orElse(null);

    }

    private List<EntityDetail> getEntitiesByClassification(String userId, String typeGuid) throws ClassificationErrorException, UserNotAuthorizedException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {

        return metadataCollection.findEntitiesByClassification(userId,
                typeGuid,
                SECURITY_TAG,
                null,
                null,
                0,
                null,
                null,
                null,
                null,
                0);
    }

    private GovernanceClassification getGovernanceClassification(Classification classification) {
        GovernanceClassification governanceClassification = new GovernanceClassification();

        governanceClassification.setName(classification.getName());
        InstanceProperties properties = classification.getProperties();
        if (properties != null) {
            String methodName = "getInstanceProperties";
            List<String> securityLabels = repositoryConnector.getRepositoryHelper().getStringArrayProperty(GOVERNANCE_ENGINE, SECURITY_LABELS, properties, methodName);
            governanceClassification.setSecurityLabels(securityLabels);

            Map<String, String> securityProperties = repositoryConnector.getRepositoryHelper().getStringMapFromProperty(GOVERNANCE_ENGINE, SECURITY_PROPERTIES, properties, methodName);
            governanceClassification.setSecurityProperties(securityProperties);
        }


        return governanceClassification;
    }


    private Optional<Classification> filterGovernedClassification(List<Classification> classifications) {
        return classifications.stream().filter(c -> isGovernedClassification(c.getType().getTypeDefName())).findAny();
    }

    private boolean isGovernedClassification(String classificationName) {
        return SECURITY_TAG.equals(classificationName);
    }

    private List<GovernedAsset> getGovernedAssets(List<EntityDetail> entityDetails) throws EntityProxyOnlyException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException {
        if (entityDetails == null || entityDetails.isEmpty()) {
            return Collections.emptyList();
        }

        List<GovernedAsset> governedAssets = new ArrayList<>(entityDetails.size());

        for (EntityDetail entityDetail : entityDetails) {
            governedAssets.add(getGovernedAsset(entityDetail));
        }
        return governedAssets;
    }

    private Context buildContext(EntityDetail entity) throws EntityProxyOnlyException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException {
        switch (entity.getType().getTypeDefName()) {
            case RELATIONAL_COLUMN:
                return contextBuilder.buildContextForColumn(metadataCollection, entity.getGUID());
            case RELATIONAL_TABLE:
                return contextBuilder.buildContextForTable(metadataCollection, entity.getGUID());
            default:
                return null;
        }
    }

    private String getTypeGuidFromTypeName(String typeName, String userId) throws UserNotAuthorizedException, RepositoryErrorException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, TypeDefNotKnownException {
        if (!knownTypeDefs.containsKey(typeName)) {
            final String typeDefGuid = metadataCollection.getTypeDefByName(userId, typeName).getGUID();
            knownTypeDefs.put(typeName, typeDefGuid);
            return typeDefGuid;
        } else {
            return knownTypeDefs.get(typeName);
        }
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

    private EntityDetail getEntityDetailById(String userId, String assetGuid) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, EntityNotKnownException {
        return metadataCollection.getEntityDetail(userId, assetGuid);
    }

    private SoftwareServerCapability getSoftwareServer(EntityDetail entityDetail) {
        InstanceProperties properties = entityDetail.getProperties();

        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();
        softwareServerCapability.setGUID(entityDetail.getGUID());
        softwareServerCapability.setOpenTypeGUID(entityDetail.getType().getTypeDefName());
        softwareServerCapability.setName(getStringProperty(properties, NAME, repositoryHelper));
        softwareServerCapability.setDescription(getStringProperty(properties, DESCRIPTION, repositoryHelper));
        softwareServerCapability.setType(getStringProperty(properties, TYPE, repositoryHelper));
        softwareServerCapability.setPatchLevel(getStringProperty(properties, PATCH_LEVEL, repositoryHelper));
        softwareServerCapability.setVersion(getStringProperty(properties, VERSION, repositoryHelper));
        softwareServerCapability.setSource(getStringProperty(properties, SOURCE, repositoryHelper));

        return softwareServerCapability;
    }

    private InstanceProperties getSoftwareServerCapabilityProperties(SoftwareServerCapability softwareServerCapability) {
        InstanceProperties properties = new InstanceProperties();

        addStringProperty(softwareServerCapability.getName(), NAME, properties, repositoryHelper);
        addStringProperty(softwareServerCapability.getDescription(), DESCRIPTION, properties, repositoryHelper);
        addStringProperty(softwareServerCapability.getType(), TYPE, properties, repositoryHelper);
        addStringProperty(softwareServerCapability.getVersion(), VERSION, properties, repositoryHelper);
        addStringProperty(softwareServerCapability.getPatchLevel(), PATCH_LEVEL, properties, repositoryHelper);
        addStringProperty(softwareServerCapability.getSource(), SOURCE, properties, repositoryHelper);

        return properties;
    }

    private void addStringProperty(String propertyValue, String propertyName, InstanceProperties properties, OMRSRepositoryHelper repositoryHelper) {
        String methodName = "addStringProperty";

        if (propertyValue != null) {
            repositoryHelper.addStringPropertyToInstance(GOVERNANCE_ENGINE, properties, propertyName, propertyValue, methodName);
        }
    }

    private String getStringProperty(InstanceProperties properties, String propertyName, OMRSRepositoryHelper repositoryHelper) {
        return repositoryHelper.getStringProperty(GOVERNANCE_ENGINE, propertyName, properties, "getStringProperty");
    }
}
