package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.odpi.openmetadata.accessservices.dataengine.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.dataengine.server.util.DataEngineErrorHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Date;

class ProcessHandler {
    private static final String PROCESS_PROPERTY_NAME = "name";
    private static final String QUALIFIED_NAME_PROPERTY_NAME = "qualifiedName";
    private static final String DISPLAY_NAME_PROPERTY_NAME = "displayName";
    private static final String PROCESS_TYPE_NAME = "Process";

    private OMRSMetadataCollection metadataCollection;
    private DataEngineErrorHandler errorHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private String serviceName;

    ProcessHandler(String serviceName,
                   OMRSRepositoryConnector repositoryConnector,
                   OMRSMetadataCollection metadataCollection) {

        this.serviceName = serviceName;
        if (repositoryConnector != null) {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.metadataCollection = metadataCollection;
            errorHandler = new DataEngineErrorHandler();

        }
    }

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

    private InstanceProperties createProcessProperties(String methodName, String processName, String displayName) {
        //TODO validate the other fields

        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName, null, PROCESS_PROPERTY_NAME, processName,
                methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, QUALIFIED_NAME_PROPERTY_NAME,
                processName + ":" + new Date().toString(), methodName);

        // properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);

        return properties;
    }
}
