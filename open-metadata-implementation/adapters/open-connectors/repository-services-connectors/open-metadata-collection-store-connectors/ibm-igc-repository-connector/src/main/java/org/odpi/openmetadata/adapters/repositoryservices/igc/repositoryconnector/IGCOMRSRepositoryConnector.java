/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCVersionEnum;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.model.OMRSStub;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class IGCOMRSRepositoryConnector extends OMRSRepositoryConnector {

    private static final Logger log = LoggerFactory.getLogger(IGCOMRSRepositoryConnector.class);

    private IGCRestClient igcRestClient;
    private IGCVersionEnum igcVersion;

    private boolean successfulInit;

    /**
     * Default constructor used by the OCF Connector Provider.
     */
    public IGCOMRSRepositoryConnector() {
        // Nothing to do...
    }

    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connectionProperties   POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String               connectorInstanceId,
                           ConnectionProperties connectionProperties)
    {
        super.initialize(connectorInstanceId, connectionProperties);

        // Retrieve connection details
        String baseURL = (String) this.connectionBean.getAdditionalProperties().get("igcBaseURL");
        String auth = (String) this.connectionBean.getAdditionalProperties().get("igcAuthorization");

        // Create new REST API client (opens a new session)
        this.igcRestClient = new IGCRestClient(baseURL, auth);
        if (getMaxPageSize() > 0) {
            this.igcRestClient.setDefaultPageSize(getMaxPageSize());
        }

        // Set the version based on the IGC client's auto-determination of the IGC environment's version
        this.igcVersion = this.igcRestClient.getIgcVersion();

        try {
            boolean success = upsertOMRSBundleZip();
            this.igcRestClient.registerPOJO(OMRSStub.class);
            successfulInit = success;
        } catch (RepositoryErrorException e) {
            log.error("Unable to create necessary OMRS objects -- failing.", e);
            successfulInit = false;
        }

    }

    /**
     * Retrieve the version identifier of the IGC environment.
     *
     * @return IGCVersionEnum
     */
    public IGCVersionEnum getIGCVersion() {
        return this.igcVersion;
    }

    /**
     * Set up the unique Id for this metadata collection.
     *
     * @param metadataCollectionId - String unique Id
     */
    @Override
    public void setMetadataCollectionId(String metadataCollectionId) {
        this.metadataCollectionId = metadataCollectionId;

        /*
         * Initialize the metadata collection only once the connector is properly set up.
         * (Meaning we will NOT initialise the collection if the connector failed to setup properly.)
         */
        if (successfulInit) {
            metadataCollection = new IGCOMRSMetadataCollection(this,
                    serverName,
                    repositoryHelper,
                    repositoryValidator,
                    metadataCollectionId);
        }
    }

    /**
     * Free up any resources held since the connector is no longer needed.
     */
    @Override
    public void disconnect() {

        // Close the session on the IGC REST client
        this.igcRestClient.disconnect();

    }


    /**
     * Returns the metadata collection object that provides an OMRS abstraction of the metadata within
     * a metadata repository.
     *
     * @return OMRSMetadataCollection - metadata information retrieved from the metadata repository.
     */
    @Override
    public OMRSMetadataCollection getMetadataCollection() {

        if (metadataCollection == null) {
            throw new NullPointerException("Local metadata collection id is not set up");
        }

        return metadataCollection;
    }


    /**
     * Access the IGC REST API client directly.
     *
     * @return IGCRestClient
     */
    public IGCRestClient getIGCRestClient() { return this.igcRestClient; }

    /**
     * Generates a zip file for the OMRS OpenIGC bundle, needed to enable change tracking for the event mapper.
     *
     * @return boolean true on success, or if the bundle already exists and false otherwise
     */
    private boolean upsertOMRSBundleZip() throws RepositoryErrorException {

        final String methodName = "upsertOMRSBundleZip";

        List<String> existingBundles = this.igcRestClient.getOpenIgcBundles();
        boolean bAlreadyExists = (existingBundles.contains("OMRS"));

        ClassPathResource bundleResource = new ClassPathResource("/bundleOMRS");
        try {
            File bundle = this.igcRestClient.createOpenIgcBundleFile(bundleResource.getFile());
            if (bundle != null) {
                bAlreadyExists = this.igcRestClient.upsertOpenIgcBundle("OMRS", bundle.getAbsolutePath());
            } else if (!bAlreadyExists) {
                OMRSErrorCode errorCode = OMRSErrorCode.REPOSITORY_LOGIC_ERROR;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                        repositoryName,
                        methodName,
                        "Unable to generate OMRS bundle.");
                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
        } catch (IOException e) {
            if (!bAlreadyExists) {
                OMRSErrorCode errorCode = OMRSErrorCode.REPOSITORY_LOGIC_ERROR;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                        repositoryName,
                        methodName,
                        "Unable to open bundle resource for OMRS.");
                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
        }

        return bAlreadyExists;

    }

}
