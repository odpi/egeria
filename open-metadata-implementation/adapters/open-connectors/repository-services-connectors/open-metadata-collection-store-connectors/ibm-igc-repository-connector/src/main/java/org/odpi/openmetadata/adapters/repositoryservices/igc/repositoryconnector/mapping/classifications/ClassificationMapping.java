/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class ClassificationMapping {

    private static final Logger log = LoggerFactory.getLogger(ClassificationMapping.class);

    private String igcAssetType;
    private List<String> igcRelationshipProperties;
    private String omrsClassificationType;

    public ClassificationMapping(String igcAssetType,
                                 String igcRelationshipProperty,
                                 String omrsClassificationType) {
        this.igcAssetType = igcAssetType;
        this.igcRelationshipProperties = new ArrayList<>();
        this.igcRelationshipProperties.add(igcRelationshipProperty);
        this.omrsClassificationType = omrsClassificationType;
    }

    public String getIgcAssetType() { return this.igcAssetType; }
    public List<String> getIgcRelationshipProperties() { return this.igcRelationshipProperties; }
    public String getOmrsClassificationType() { return this.omrsClassificationType; }

    public void addIgcRelationshipProperty(String property) { this.igcRelationshipProperties.add(property); }

    public abstract void addMappedOMRSClassifications(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                      List<Classification> classifications,
                                                      Reference fromIgcObject,
                                                      String userId);

    /**
     * Indicates whether this classification mapping matches the provided IGC asset type: that is, this mapping
     * can be used to translate to the provided IGC asset type.
     *
     * @param igcAssetType the IGC asset type to check the mapping against
     * @return boolean
     */
    public boolean matchesAssetType(String igcAssetType) {
        log.debug("checking for matching asset between {} and {}", this.igcAssetType, Reference.getAssetTypeForSearch(igcAssetType));
        return (
                this.igcAssetType.equals(igcAssetType)
                        || this.igcAssetType.equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)
                        || this.igcAssetType.equals(Reference.getAssetTypeForSearch(igcAssetType))
        );
    }

    /**
     * Retrieve a Classification instance based on the provided information.
     *
     * @param omrsClassificationType the type name of the OMRS classification
     * @param omrsEntityType the OMRS type of entity the OMRS classification is being linked against
     * @param classificationProperties the properties to setup on the classification
     * @return Classification
     * @throws RepositoryErrorException
     */
    protected static Classification getMappedClassification(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                            String omrsClassificationType,
                                                            String omrsEntityType,
                                                            InstanceProperties classificationProperties,
                                                            Reference fromIgcObject,
                                                            String userId) throws RepositoryErrorException {

        final String methodName = "getMappedClassification";

        Classification classification = null;

        try {
            // Try to instantiate a new classification from the repository connector
            classification = igcomrsRepositoryConnector.getRepositoryHelper().getNewClassification(
                    igcomrsRepositoryConnector.getRepositoryName(),
                    userId,
                    omrsClassificationType,
                    omrsEntityType,
                    ClassificationOrigin.ASSIGNED,
                    null,
                    classificationProperties
            );
            // If modification details are available on the IGC object, add these to the classification,
            // including setting its version number based on the last update time
            if (fromIgcObject.hasModificationDetails()) {
                classification.setCreatedBy((String)fromIgcObject.getPropertyByName(Reference.MOD_CREATED_BY));
                classification.setCreateTime((Date)fromIgcObject.getPropertyByName(Reference.MOD_CREATED_ON));
                classification.setUpdateTime((Date)fromIgcObject.getPropertyByName(Reference.MOD_MODIFIED_ON));
                classification.setUpdatedBy((String)fromIgcObject.getPropertyByName(Reference.MOD_MODIFIED_BY));
                if (classification.getUpdateTime() != null) {
                    classification.setVersion(classification.getUpdateTime().getTime());
                }
            }
        } catch (TypeErrorException e) {
            log.error("Unable to create a new classification.", e);
            OMRSErrorCode errorCode = OMRSErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                    omrsClassificationType,
                    omrsEntityType);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    ClassificationMapping.class.getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return classification;

    }

}
