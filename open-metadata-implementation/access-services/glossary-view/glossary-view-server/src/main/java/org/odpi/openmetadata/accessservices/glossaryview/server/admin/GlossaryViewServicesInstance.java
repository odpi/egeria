/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.admin;

import org.odpi.openmetadata.accessservices.glossaryview.exception.GlossaryViewErrorCode;
import org.odpi.openmetadata.accessservices.glossaryview.exception.GlossaryViewOMASNewInstanceException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

/**
 * Stores references to OMRS objects for a specific server. It is also responsible for registering this OMAS in the
 * Access Service Registry, and itself in the instance map
 */
public class GlossaryViewServicesInstance {

    private final OMRSRepositoryConnector repositoryConnector;
    private OMRSMetadataCollection metadataCollection;
    private final String serverName;

    /**
     * Set up the local repository connector that will service the REST Calls, registers this OMAS in the
     * Access Service Registry and in the instance map
     *
     * @param repositoryConnector responsible for servicing the REST calls.
     *
     * @throws GlossaryViewOMASNewInstanceException if a problem occurred during initialization
     */
    public GlossaryViewServicesInstance(OMRSRepositoryConnector repositoryConnector) throws GlossaryViewOMASNewInstanceException {
        final String methodName = "new GlossaryViewServiceInstance";

        if (!valid(repositoryConnector)) {
            throw buildGlossaryViewOMASNewInstanceException(GlossaryViewErrorCode.OMRS_NOT_INITIALIZED, methodName);
        }

        this.repositoryConnector = repositoryConnector;
        this.serverName = repositoryConnector.getServerName();

        try {
            this.metadataCollection = repositoryConnector.getMetadataCollection();
        }catch(RepositoryErrorException ree){
            // TODO remove this try/catch when getMetadataCollection() no longer throws an exception
            // execution should not reach this point
            // check explanation from TODO within method valid()
        }

        GlossaryViewServicesInstanceMap.setNewInstanceForJVM(serverName, this);
    }

    /*
    * Validates incoming repository. Not supposed to throw exceptions
    *
    * @return true if valid
    */
    private boolean valid(OMRSRepositoryConnector repositoryConnector){
        if(repositoryConnector == null){
            return false;
        }
        if(repositoryConnector.getServerName() == null){
            return false;
        }
        try {
            if (repositoryConnector.getMetadataCollection() == null) {
                return false;
            }
        }catch(RepositoryErrorException ree){
            return false;
            // TODO remove this try/catch when getMetadataCollection() no longer throws an exception
            // the getter will throw an exception if the field is null; in my opinion the responsibility on how to
            // treat the stored value within a field lays with the caller, not the provider(getter)
            // the value should've been validated before stored
        }
        return true;
    }

    /*
    * @return GlossaryViewOMASNewInstanceException
    */
    private GlossaryViewOMASNewInstanceException buildGlossaryViewOMASNewInstanceException(GlossaryViewErrorCode errorCode, String methodName){
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

        return new GlossaryViewOMASNewInstanceException(errorCode.getHttpErrorCode(), GlossaryViewServicesInstance.class.getName(),
                methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
    }

    /**
     * @return serverName
     */
    public String getServerName()  {
        return serverName;

    }

    /**
     * @return metadataCollection
     */
    public OMRSMetadataCollection getMetadataCollection() {
        return metadataCollection;
    }

    /**
     * @return repositoryConnector
     */
    public OMRSRepositoryConnector getRepositoryConnector() {
        return repositoryConnector;
    }

    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        GlossaryViewServicesInstanceMap.removeInstanceForJVM(serverName);
    }

}
