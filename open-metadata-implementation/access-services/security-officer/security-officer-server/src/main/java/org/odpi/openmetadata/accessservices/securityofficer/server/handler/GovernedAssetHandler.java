/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.server.handler;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.Context;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernanceClassification;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernedAsset;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.securityofficer.server.processors.GovernedAssetContextBuilder;
import org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * ConnectionHandler retrieves Connection objects from the property handlers.  It runs handlers-side in the AssetConsumer
 * OMAS and retrieves Connections through the OMRSRepositoryConnector.
 */
public class GovernedAssetHandler {

    private final String serviceName;
    private final String serverName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final RepositoryErrorHandler errorHandler;
    private OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();
    private List<String> supportedZones;
    private GovernedAssetContextBuilder contextBuilder;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param errorHandler            provides utilities for manipulating the repository services
     * @param supportedZones          setting of the supported zones for the handler
     */
    public GovernedAssetHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper, RepositoryErrorHandler errorHandler,
                                List<String> supportedZones) {

        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.errorHandler = errorHandler;
        this.supportedZones = supportedZones;
        contextBuilder = new GovernedAssetContextBuilder(serverName, repositoryHandler, repositoryHelper);
    }

    public void setSecurityVerifier(OpenMetadataServerSecurityVerifier securityVerifier) {
        this.securityVerifier = securityVerifier;
    }

    /**
     * Returns the list of governed assets with associated tags
     *
     * @param userId      - String - userId of user making request.
     * @param entityTypes - types to start query offset.
     * @return List of Governed Access
     */
    public List<GovernedAsset> getGovernedAssets(String userId, List<String> entityTypes, Integer offset, Integer pageSize)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {

        String methodName = "getGovernedAssets";
        invalidParameterHandler.validateUserId(userId, methodName);

        List<EntityDetail> response = new ArrayList<>();

        if (CollectionUtils.isEmpty(entityTypes)) {
            response = repositoryHandler.getEntitiesForClassificationType(userId, null, Constants.SECURITY_TAG, false, false, offset, pageSize, new Date(), methodName);
        } else {
            for (String typeName : entityTypes) {
                TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeName);
                if (typeDefByName != null && typeDefByName.getGUID() != null) {
                    response.addAll(repositoryHandler.getEntitiesForClassificationType(userId, typeDefByName.getGUID(), Constants.SECURITY_TAG, false, false, offset, pageSize, new Date(), methodName));
                }
            }
        }

        return convertGovernedAssets(userId, response);
    }

    public GovernedAsset getGovernedAsset(String userId, String assedID)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getGovernedAsset";
        invalidParameterHandler.validateUserId(userId, methodName);

        EntityDetail entityDetailsByGUID = getEntityDetailsByGUID(userId, assedID, null);

        if (containsGovernedClassification(entityDetailsByGUID)) {
            return convertGovernedAsset(userId, entityDetailsByGUID);
        }

        return null;
    }

    public boolean containsGovernedClassification(EntityDetail entityDetail) {
        if (CollectionUtils.isEmpty(entityDetail.getClassifications())) {
            return false;
        }

        for (Classification classification : entityDetail.getClassifications()) {
            if (classification.getType() != null &&
                    classification.getType().getTypeDefName() != null &&
                    isGovernedClassification(classification.getType().getTypeDefName())) {
                return true;
            }
        }

        return false;
    }

    public boolean isSchemaElement(InstanceType entityType) {
        if (entityType == null) {
            return false;
        }

        return repositoryHelper.isTypeOf(serverName, entityType.getTypeDefName(), Constants.SCHEMA_ATTRIBUTE);
    }

    public String createSoftwareServerCapability(String userId, SoftwareServerCapability softwareServerCapability) throws UserNotAuthorizedException, PropertyServerException, org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        String methodName = "createSoftwareServerCapability";
        invalidParameterHandler.validateUserId(userId, methodName);

        InstanceProperties initialProperties = getSoftwareServerCapabilityProperties(softwareServerCapability);

        return repositoryHandler.createEntity(userId,
                                              Constants.SOFTWARE_SERVER_CAPABILITY_GUID,
                                              Constants.SOFTWARE_SERVER_CAPABILITY,
                                              null,
                                              null,
                                              initialProperties,
                                              null,
                                              InstanceStatus.ACTIVE,
                                              methodName);

    }

    public SoftwareServerCapability getSoftwareServerCapabilityByGUID(String userId, String guid)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getSoftwareServerCapabilityByGUID";
        invalidParameterHandler.validateUserId(userId, methodName);

        EntityDetail entityDetailsByGUID = getEntityDetailsByGUID(userId, guid, Constants.SOFTWARE_SERVER_CAPABILITY);
        if (entityDetailsByGUID == null) {
            return null;
        }

        return convertSoftwareServerCapability(entityDetailsByGUID);
    }

    public GovernedAsset convertGovernedAsset(String userID, EntityDetail entity)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "convertGovernedAsset";
        GovernedAsset governedAsset = new GovernedAsset();

        governedAsset.setGuid(entity.getGUID());
        governedAsset.setType(entity.getType().getTypeDefName());
        governedAsset.setFullQualifiedName(repositoryHelper.getStringProperty(serverName, Constants.QUALIFIED_NAME, entity.getProperties(), methodName));
        governedAsset.setName(repositoryHelper.getStringProperty(serverName, Constants.DISPLAY_NAME, entity.getProperties(), methodName));
        governedAsset.setContext(buildContext(userID, entity));

        if (entity.getClassifications() != null && !entity.getClassifications().isEmpty()) {
            governedAsset.setAssignedGovernanceClassification(getGovernanceClassification(entity.getClassifications()));
        }

        return governedAsset;
    }

    private GovernanceClassification getGovernanceClassification(List<Classification> allClassifications) {
        Optional<Classification> classification = filterGovernedClassification(allClassifications);

        return classification.map(this::getGovernanceClassification).orElse(null);

    }

    private GovernanceClassification getGovernanceClassification(Classification classification) {
        String methodName = "getInstanceProperties";

        GovernanceClassification governanceClassification = new GovernanceClassification();
        governanceClassification.setName(classification.getName());

        InstanceProperties properties = classification.getProperties();
        if (properties != null) {
            governanceClassification.setSecurityLabels(
                    repositoryHelper.getStringArrayProperty(serverName, Constants.SECURITY_LABELS, properties, methodName));

            governanceClassification.setSecurityProperties(
                    repositoryHelper.getStringMapFromProperty(serverName, Constants.SECURITY_PROPERTIES, properties, methodName));
        }

        return governanceClassification;
    }


    private Optional<Classification> filterGovernedClassification(List<Classification> classifications) {
        return classifications.stream().filter(c -> isGovernedClassification(c.getType().getTypeDefName())).findAny();
    }

    private boolean isGovernedClassification(String classificationName) {
        return Constants.SECURITY_TAG.equals(classificationName);
    }

    private List<GovernedAsset> convertGovernedAssets(String userID, List<EntityDetail> entityDetails)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        if (CollectionUtils.isEmpty(entityDetails)) {
            return Collections.emptyList();
        }

        List<GovernedAsset> result = new ArrayList<>();
        for (EntityDetail entityDetail : entityDetails) {
            result.add(convertGovernedAsset(userID, entityDetail));
        }

        return result;
    }

    private Context buildContext(String userID, EntityDetail entity)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        switch (entity.getType().getTypeDefName()) {
            case Constants.RELATIONAL_COLUMN:
                return contextBuilder.buildContextForColumn(userID, entity.getGUID());
            case Constants.RELATIONAL_TABLE:
                return contextBuilder.buildContextForTable(userID, entity.getGUID());
            default:
                return null;
        }
    }

    private EntityDetail getEntityDetailsByGUID(String userId, String guid, String entityType)
            throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException {
        String methodName = "getEntityDetailsByGUID";

        return repositoryHandler.getEntityByGUID(userId, guid, "guid", entityType, false, false, new Date(), methodName);
    }

    private SoftwareServerCapability convertSoftwareServerCapability(EntityDetail entityDetail) {
        InstanceProperties properties = entityDetail.getProperties();

        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();
        softwareServerCapability.setGUID(entityDetail.getGUID());
        softwareServerCapability.setOpenTypeGUID(entityDetail.getType().getTypeDefName());
        softwareServerCapability.setName(getStringProperty(properties, Constants.NAME, repositoryHelper));
        softwareServerCapability.setDescription(getStringProperty(properties, Constants.DESCRIPTION, repositoryHelper));
        softwareServerCapability.setType(getStringProperty(properties, Constants.TYPE, repositoryHelper));
        softwareServerCapability.setPatchLevel(getStringProperty(properties, Constants.PATCH_LEVEL, repositoryHelper));
        softwareServerCapability.setVersion(getStringProperty(properties, Constants.VERSION, repositoryHelper));
        softwareServerCapability.setSource(getStringProperty(properties, Constants.SOURCE, repositoryHelper));

        return softwareServerCapability;
    }

    private InstanceProperties getSoftwareServerCapabilityProperties(SoftwareServerCapability softwareServerCapability) {
        InstanceProperties properties = new InstanceProperties();

        addStringProperty(softwareServerCapability.getName(), Constants.NAME, properties, repositoryHelper);
        addStringProperty(softwareServerCapability.getDescription(), Constants.DESCRIPTION, properties, repositoryHelper);
        addStringProperty(softwareServerCapability.getType(), Constants.TYPE, properties, repositoryHelper);
        addStringProperty(softwareServerCapability.getVersion(), Constants.VERSION, properties, repositoryHelper);
        addStringProperty(softwareServerCapability.getPatchLevel(), Constants.PATCH_LEVEL, properties, repositoryHelper);
        addStringProperty(softwareServerCapability.getSource(), Constants.SOURCE, properties, repositoryHelper);

        return properties;
    }

    private void addStringProperty(String propertyValue, String propertyName, InstanceProperties properties, OMRSRepositoryHelper repositoryHelper) {
        String methodName = "addStringProperty";

        if (propertyValue != null) {
            repositoryHelper.addStringPropertyToInstance(serverName, properties, propertyName, propertyValue, methodName);
        }
    }

    private String getStringProperty(InstanceProperties properties, String propertyName, OMRSRepositoryHelper repositoryHelper) {
        return repositoryHelper.getStringProperty(serverName, propertyName, properties, "getStringProperty");
    }
}
