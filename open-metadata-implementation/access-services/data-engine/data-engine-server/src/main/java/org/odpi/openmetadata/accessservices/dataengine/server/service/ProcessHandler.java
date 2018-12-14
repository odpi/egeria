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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * ProcessHandler manages Process objects from the property server.  It runs server-side in the DataEngine
 * OMAS and creates process entities with input/output relationships through the OMRSRepositoryConnector.
 */
class ProcessHandler {
    private static final String PROCESS_PROPERTY_NAME = "name";
    private static final String QUALIFIED_NAME_PROPERTY_NAME = "qualifiedName";
    private static final String DISPLAY_NAME_PROPERTY_NAME = "displayName";
    private static final String PROCESS_TYPE_NAME = "Process";
    private static final String PROCESS_INPUT_RELATIONSHIP_TYPE_NAME = "ProcessInput";
    private static final String PROCESS_OUTPUT_RELATIONSHIP_TYPE_NAME = "ProcessOutput";

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessHandler.class);

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
    ProcessHandler(String serviceName, OMRSRepositoryConnector repositoryConnector, OMRSMetadataCollection metadataCollection) {

        this.serviceName = serviceName;
        if (repositoryConnector != null) {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.metadataCollection = metadataCollection;
            errorHandler = new DataEngineErrorHandler();

        }
    }

    /**
     * Create the process
     *
     * @param userId the name of the calling user
     * @param processName the name of the process
     * @param displayName the display name of the process
     * @return the guid of the created process
     * @throws UserNotAuthorizedException
     * @throws TypeErrorException
     * @throws ClassificationErrorException
     * @throws StatusNotSupportedException
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws PropertyErrorException
     */
    String createProcess(String userId, String processName, String displayName)
            throws UserNotAuthorizedException, TypeErrorException, ClassificationErrorException,
            StatusNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            InvalidParameterException, RepositoryErrorException, PropertyErrorException {
        //TODO validate the other fields
        final String methodName = "createProcess";

        errorHandler.validateUserId(userId, methodName);

        EntityDetail entity;
        entity = repositoryHelper.getSkeletonEntity(serviceName, "", InstanceProvenanceType.LOCAL_COHORT, userId,
                PROCESS_TYPE_NAME);


        InstanceProperties instanceProperties = createProcessProperties(methodName, processName, displayName);

        EntityDetail createdEntity = metadataCollection.addEntity(userId,
                entity.getType().getTypeDefGUID(),
                instanceProperties,
                entity.getClassifications(),
                entity.getStatus());

        return createdEntity.getGUID();
    }

    /**
     * Create an instance properties object for a process asset
     *k
     * @param methodName
     * @param processName
     * @param displayName
     * @return
     */
    private InstanceProperties createProcessProperties(String methodName, String processName, String displayName) {
        //TODO validate the other fields

        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName, null, PROCESS_PROPERTY_NAME,
                processName, methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, QUALIFIED_NAME_PROPERTY_NAME,
                processName + ":" + new Date().toString(), methodName);

        // properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);

        return properties;
    }

    /**
     * @param userId
     * @param processGuid
     * @param dataSetGuid
     * @return
     * @throws UserNotAuthorizedException
     * @throws TypeErrorException
     * @throws StatusNotSupportedException
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException
     * @throws EntityNotKnownException
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws PropertyErrorException
     */
    String addInputRelationship(String userId, String processGuid, String dataSetGuid)
            throws UserNotAuthorizedException, TypeErrorException, StatusNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, InvalidParameterException, RepositoryErrorException, PropertyErrorException {
        final String methodName = "addInputRelationship";

        return addRelationship(userId, processGuid, dataSetGuid, PROCESS_INPUT_RELATIONSHIP_TYPE_NAME, methodName);
    }


    /**
     * @param userId
     * @param processGuid
     * @param dataSetGuid
     * @return
     * @throws UserNotAuthorizedException
     * @throws TypeErrorException
     * @throws StatusNotSupportedException
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException
     * @throws EntityNotKnownException
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws PropertyErrorException
     */
    String addOutputRelationship(String userId, String processGuid, String dataSetGuid)
            throws UserNotAuthorizedException, TypeErrorException, StatusNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, InvalidParameterException, RepositoryErrorException, PropertyErrorException {
        final String methodName = "addOutputRelationship";

        return addRelationship(userId, processGuid, dataSetGuid, PROCESS_OUTPUT_RELATIONSHIP_TYPE_NAME, methodName);
    }


    private String addRelationship(String userId, String processGuid, String dataSetGuid, String relationshipType,
                                   String methodName)
            throws TypeErrorException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, EntityNotKnownException, StatusNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, UserNotAuthorizedException {

        errorHandler.validateUserId(userId, methodName);

        //TODO validate GUIDs

        Relationship relationship = repositoryHelper.getSkeletonRelationship(serviceName, "",
                InstanceProvenanceType.LOCAL_COHORT, userId, relationshipType);

        Relationship createdRelationship = metadataCollection.addRelationship(userId,
                relationship.getType().getTypeDefGUID(),
                null,
                processGuid,
                dataSetGuid,
                InstanceStatus.ACTIVE);

        return createdRelationship.getGUID();
    }

    //TODO remove when igcReplicator can replicate DataSets
    protected String createDataSet(String userId, String processName)
            throws UserNotAuthorizedException, TypeErrorException, ClassificationErrorException,
            StatusNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            InvalidParameterException, RepositoryErrorException, PropertyErrorException {

        final String methodName = "createDataSet";

        errorHandler.validateUserId(userId, methodName);

        EntityDetail entity;
        entity = repositoryHelper.getSkeletonEntity(serviceName, "", InstanceProvenanceType.LOCAL_COHORT, userId,
                "DataSet");


        InstanceProperties instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                PROCESS_PROPERTY_NAME,
                processName, methodName);

        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName, instanceProperties,
                QUALIFIED_NAME_PROPERTY_NAME,
                processName + ":" + new Date().toString(), methodName);

        EntityDetail createdEntity = metadataCollection.addEntity(userId,
                entity.getType().getTypeDefGUID(),
                instanceProperties,
                entity.getClassifications(),
                entity.getStatus());

        return createdEntity.getGUID();
    }

}
