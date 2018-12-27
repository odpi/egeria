/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.odpi.openmetadata.accessservices.dataengine.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.dataengine.server.util.DataEngineErrorHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Date;
import java.util.List;


/**
 * ProcessHandler manages Process objects from the property server.  It runs server-side in the DataEngine
 * OMAS and creates process entities with input/output relationships through the OMRSRepositoryConnector.
 */
class ProcessHandler {
    private static final String PROCESS_PROPERTY_NAME = "name";
    private static final String QUALIFIED_NAME_PROPERTY_NAME = "qualifiedName";
    private static final String PROCESS_TYPE_NAME = "Process";
    private static final String PROCESS_INPUT_RELATIONSHIP_TYPE_NAME = "ProcessInput";
    private static final String PROCESS_OUTPUT_RELATIONSHIP_TYPE_NAME = "ProcessOutput";
    private static final String OWNER_PROPERTY_NAME = "owner";
    private static final String LATEST_CHANGE_PROPERTY_NAME = "latestChange";
    private static final String DESCRIPTION_PROPERTY_NAME = "description";

    private OMRSMetadataCollection metadataCollection;
    private DataEngineErrorHandler errorHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private String serviceName;

    /**
     * Construct the process handler with a link to the property server's connector, a link to the metadata collection
     * and this access service's official name.
     *
     * @param serviceName         name of this service
     * @param repositoryConnector connector to the property server.
     */
    ProcessHandler(String serviceName, OMRSRepositoryConnector repositoryConnector,
                   OMRSMetadataCollection metadataCollection) {

        this.serviceName = serviceName;
        if (repositoryConnector != null) {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.metadataCollection = metadataCollection;
        }
        errorHandler = new DataEngineErrorHandler();
    }


    /**
     * Create the process
     *
     * @param userId          the name of the calling user
     * @param processName     the name of the process
     * @param description     the description of the process
     * @param latestChange    the description for the latest change done for the asset
     * @param zoneMembership  the zone membership of the process
     * @param displayName     the display name of the process
     * @param parentProcessId the parent process Guid, null if no parent present
     * @return the guid of the created process
     * @throws UserNotAuthorizedException                                                         the requesting user is not authorized to issue this request.
     * @throws TypeErrorException                                                                 unknown or invalid type
     * @throws ClassificationErrorException                                                       the requested classification is either not known or not valid for the entity.
     * @throws StatusNotSupportedException                                                        status not supported
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the requesting user
     *                                                                                            is not authorized to issue this request.
     * @throws InvalidParameterException                                                          one of the parameters is null or invalid.
     * @throws RepositoryErrorException                                                           no metadata collection
     * @throws PropertyErrorException                                                             there is a problem with one of the other parameters.
     */
    String createProcess(String userId, String processName, String description, String latestChange,
                         String zoneMembership, String displayName, String parentProcessId)
            throws UserNotAuthorizedException, TypeErrorException, ClassificationErrorException,
            StatusNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            InvalidParameterException, RepositoryErrorException, PropertyErrorException {

        final String methodName = "createProcess";

        errorHandler.validateUserId(userId, methodName);

        EntityDetail entity = repositoryHelper.getSkeletonEntity(serviceName, "",
                InstanceProvenanceType.LOCAL_COHORT, userId, PROCESS_TYPE_NAME);


        InstanceProperties instanceProperties = createProcessProperties(userId, processName, description, latestChange,
                zoneMembership, displayName, parentProcessId);

        EntityDetail createdEntity = metadataCollection.addEntity(userId, entity.getType().getTypeDefGUID(),
                instanceProperties, entity.getClassifications(), entity.getStatus());

        return createdEntity.getGUID();
    }

    /**
     * Create an instance properties object for a process asset
     *
     * @param processName     the name of the process
     * @param description     the description of the process
     * @param latestChange    the description for the latest change done for the asset
     * @param zoneMembership  the zone membership of the process
     * @param displayName     the display name of the process
     * @param parentProcessId the parent process Guid, null if no parent present
     * @return instance properties object
     */
    private InstanceProperties createProcessProperties(String userId, String processName, String description,
                                                       String latestChange, String zoneMembership, String displayName,
                                                       String parentProcessId) {

        final String methodName = "createProcessProperties";

        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName, null, PROCESS_PROPERTY_NAME,
                processName, methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, QUALIFIED_NAME_PROPERTY_NAME,
                processName + ":" + new Date().toString(), methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, DESCRIPTION_PROPERTY_NAME,
                description, methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, LATEST_CHANGE_PROPERTY_NAME,
                latestChange, methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OWNER_PROPERTY_NAME,
                userId, methodName);

        return properties;
    }

    /**
     * Create ProcessInput relationships between a Process asset and the corresponding input DataSets
     *
     * @param userId      the name of the calling user
     * @param processGuid the unique identifier of the process
     * @param inputs      list of unique identifiers for DataSets to be linked to the process
     * @throws UserNotAuthorizedException                                                         the requesting user is not authorized to issue this request.
     * @throws TypeErrorException                                                                 unknown or invalid type
     * @throws StatusNotSupportedException                                                        status not supported
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the requesting user
     *                                                                                            is not authorized to issue this request.
     * @throws InvalidParameterException                                                          one of the parameters is null or invalid.
     * @throws RepositoryErrorException                                                           no metadata collection
     * @throws PropertyErrorException                                                             there is a problem with one of the other parameters.
     * @throws EntityNotKnownException                                                            the entity instance is not known in the metadata collection.
     */
    void addInputRelationships(String userId, String processGuid, List<String> inputs)
            throws UserNotAuthorizedException, TypeErrorException, StatusNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException,
            InvalidParameterException, RepositoryErrorException, PropertyErrorException {

        final String methodName = "addInputRelationship";

        if (inputs == null) {
            return;
        }
        for (String dataSetGuid : inputs) {
            addRelationship(userId, processGuid, dataSetGuid, PROCESS_INPUT_RELATIONSHIP_TYPE_NAME, methodName);
        }
    }

    /**
     * Create ProcessOutput relationships between a Process asset and the corresponding output DataSets
     *
     * @param userId      the name of the calling user
     * @param processGuid the unique identifier of the process
     * @param outputs     list of unique identifiers for DataSets to be linked to the process
     * @throws UserNotAuthorizedException                                                         the requesting user is not authorized to issue this request.
     * @throws TypeErrorException                                                                 unknown or invalid type
     * @throws StatusNotSupportedException                                                        status not supported
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the requesting user
     *                                                                                            is not authorized to issue this request.
     * @throws InvalidParameterException                                                          one of the parameters is null or invalid.
     * @throws RepositoryErrorException                                                           no metadata collection
     * @throws PropertyErrorException                                                             there is a problem with one of the other parameters.
     * @throws EntityNotKnownException                                                            the entity instance is not known in the metadata collection.
     */
    void addOutputRelationships(String userId, String processGuid, List<String> outputs)
            throws UserNotAuthorizedException, TypeErrorException, StatusNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException,
            InvalidParameterException, RepositoryErrorException, PropertyErrorException {

        final String methodName = "addOutputRelationship";
        if (outputs == null) {
            return;
        }

        for (String dataSetGuid : outputs) {
            addRelationship(userId, processGuid, dataSetGuid, PROCESS_OUTPUT_RELATIONSHIP_TYPE_NAME, methodName);
        }
    }

    private void addRelationship(String userId, String processGuid, String dataSetGuid, String relationshipType,
                                 String methodName)
            throws TypeErrorException, InvalidParameterException, RepositoryErrorException, PropertyErrorException,
            EntityNotKnownException, StatusNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, UserNotAuthorizedException {

        errorHandler.validateUserId(userId, methodName);

        //TODO add validation for GUIDs

        Relationship relationship = repositoryHelper.getSkeletonRelationship(serviceName, "",
                InstanceProvenanceType.LOCAL_COHORT, userId, relationshipType);

        metadataCollection.addRelationship(userId, relationship.getType().getTypeDefGUID(), null,
                processGuid, dataSetGuid, InstanceStatus.ACTIVE);
    }

    //TODO remove when igcReplicator can replicate DataSets
    String createDataSet(String userId, String processName)
            throws UserNotAuthorizedException, TypeErrorException, ClassificationErrorException,
            StatusNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            InvalidParameterException, RepositoryErrorException, PropertyErrorException {

        final String methodName = "createDataSet";

        errorHandler.validateUserId(userId, methodName);

        EntityDetail entity = repositoryHelper.getSkeletonEntity(serviceName, "",
                InstanceProvenanceType.LOCAL_COHORT, userId, "DataSet");

        InstanceProperties instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                PROCESS_PROPERTY_NAME, processName, methodName);

        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName, instanceProperties,
                QUALIFIED_NAME_PROPERTY_NAME, processName + ":" + new Date().toString(), methodName);

        EntityDetail createdEntity = metadataCollection.addEntity(userId, entity.getType().getTypeDefGUID(),
                instanceProperties, entity.getClassifications(), entity.getStatus());

        return createdEntity.getGUID();
    }
}
