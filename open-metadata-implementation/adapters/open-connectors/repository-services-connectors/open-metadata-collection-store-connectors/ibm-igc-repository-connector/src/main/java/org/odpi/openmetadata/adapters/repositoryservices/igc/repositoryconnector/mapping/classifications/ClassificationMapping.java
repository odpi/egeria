/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestConstants;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * The base class for all mappings between OMRS Classification TypeDefs and IGC assets.
 */
public abstract class ClassificationMapping {

    private static final Logger log = LoggerFactory.getLogger(ClassificationMapping.class);

    private String igcAssetType;
    private List<String> igcRelationshipProperties;
    private String omrsClassificationType;
    private Set<String> excludeIgcAssetType;
    private List<InstanceStatus> omrsSupportedStatuses;
    private Set<String> mappedOmrsPropertyNames;

    public ClassificationMapping(String igcAssetType,
                                 String igcRelationshipProperty,
                                 String omrsClassificationType) {
        this.igcAssetType = igcAssetType;
        this.igcRelationshipProperties = new ArrayList<>();
        this.igcRelationshipProperties.add(igcRelationshipProperty);
        this.omrsClassificationType = omrsClassificationType;
        this.excludeIgcAssetType = new HashSet<>();
        this.omrsSupportedStatuses = new ArrayList<>();
        this.mappedOmrsPropertyNames = new HashSet<>();
        addSupportedStatus(InstanceStatus.ACTIVE);
        addSupportedStatus(InstanceStatus.DELETED);
    }

    /**
     * Add the provided status as one supported by this classification mapping.
     *
     * @param status a status that is supported by the mapping
     */
    public void addSupportedStatus(InstanceStatus status) { this.omrsSupportedStatuses.add(status); }

    /**
     * Retrieve the list of statuses that are supported by the classification mapping.
     *
     * @return List<InstanceStatus>
     */
    public List<InstanceStatus> getSupportedStatuses() { return this.omrsSupportedStatuses; }

    /**
     * Add the provided property name as one supported by this classification mapping.
     *
     * @param name the name of the OMRS property supported by the mapping
     */
    public void addMappedOmrsProperty(String name) { this.mappedOmrsPropertyNames.add(name); }

    /**
     * Retrieve the set of OMRS properties that are supported by the classification mapping.
     *
     * @return Set<String>
     */
    public Set<String> getMappedOmrsPropertyNames() { return this.mappedOmrsPropertyNames; }

    /**
     * Retrieve the IGC asset type to which this classification mapping applies.
     *
     * @return String
     */
    public String getIgcAssetType() { return this.igcAssetType; }

    /**
     * Retrieve the list of IGC properties used to apply this classification mapping.
     *
     * @return List<String>
     */
    public List<String> getIgcRelationshipProperties() { return this.igcRelationshipProperties; }

    /**
     * Retrieve the name of the OMRS ClassificationDef represented by this mapping.
     *
     * @return String
     */
    public String getOmrsClassificationType() { return this.omrsClassificationType; }

    /**
     * When the asset this applies to is a 'main_object', use this method to add any objects that should NOT be
     * included under that umbrella.
     *
     * @param igcAssetType the IGC asset type to exclude from 'main_object' consideration
     */
    public void addExcludedIgcAssetType(String igcAssetType) { this.excludeIgcAssetType.add(igcAssetType); }

    /**
     * Add the provided property as one of the IGC properties needed to setup this classification.
     *
     * @param property the IGC asset's property name
     */
    public void addIgcRelationshipProperty(String property) { this.igcRelationshipProperties.add(property); }

    /**
     * Implement this method to actually define the logic for the classification. (Since IGC has no actual concept
     * of classification, this is left as a method to-be-implemented depending on how the implementation desires the
     * classification to be represented within IGC.)
     *
     * @param igcomrsRepositoryConnector connectivity to the IGC repository via OMRS connector
     * @param classifications the list of classifications to which new classifications should be added
     * @param fromIgcObject the IGC object from which to determine the classifications
     * @param userId the user requesting the classifications (currently unused)
     */
    public abstract void addMappedOMRSClassifications(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                      List<Classification> classifications,
                                                      Reference fromIgcObject,
                                                      String userId);

    /**
     * Implement this method to define how IGC assets can be searched based on this classification. (Since IGC has no
     * actual concept of classification, this is left as a method to-be-implemented depending on how the implementation
     * desires the classification to be represented within IGC.)
     *
     * @param matchClassificationProperties the criteria to use when searching for the classification
     * @return IGCSearchConditionSet - the IGC search criteria to find entities based on this classification
     */
    public abstract IGCSearchConditionSet getIGCSearchCriteria(InstanceProperties matchClassificationProperties);

    /**
     * Indicates whether this classification mapping matches the provided IGC asset type: that is, this mapping
     * can be used to translate to the provided IGC asset type.
     *
     * @param igcAssetType the IGC asset type to check the mapping against
     * @return boolean
     */
    public boolean matchesAssetType(String igcAssetType) {
        String simplifiedType = Reference.getAssetTypeForSearch(igcAssetType);
        log.debug("checking for matching asset between {} and {}", this.igcAssetType, simplifiedType);
        return (
                this.igcAssetType.equals(simplifiedType)
                        || (this.igcAssetType.equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE) && !this.excludeIgcAssetType.contains(simplifiedType))
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
                classification.setCreatedBy((String)fromIgcObject.getPropertyByName(IGCRestConstants.MOD_CREATED_BY));
                classification.setCreateTime((Date)fromIgcObject.getPropertyByName(IGCRestConstants.MOD_CREATED_ON));
                classification.setUpdateTime((Date)fromIgcObject.getPropertyByName(IGCRestConstants.MOD_MODIFIED_ON));
                classification.setUpdatedBy((String)fromIgcObject.getPropertyByName(IGCRestConstants.MOD_MODIFIED_BY));
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
