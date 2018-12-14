/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.ReferenceMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IGCOMRSRepositoryConnector extends OMRSRepositoryConnector {

    private IGCRestClient igcRestClient;
    private String igcVersion;

    private static final Logger log = LoggerFactory.getLogger(IGCOMRSRepositoryConnector.class);

    /**
     * Default constructor used by the OCF Connector Provider.
     */
    public IGCOMRSRepositoryConnector() { }

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

        // TODO: review how to make this determination dynamically (eg. from application.properties?)
        IGCRestClient.disableSslVerification();

        // Create new REST API client (opens a new session)
        this.igcRestClient = new IGCRestClient(baseURL, auth);
        if (getMaxPageSize() > 0) {
            this.igcRestClient.setDefaultPageSize(getMaxPageSize());
        }

        // Set the version based on the IGC client's auto-determination of the IGC environment's version
        this.igcVersion = this.igcRestClient.getIgcVersion();

    }

    /**
     * Retrieve the version identifier of the IGC environment.
     *
     * @return String
     */
    public String getIGCVersion() {
        return this.igcVersion;
    }

    /**
     * Set up the unique Id for this metadata collection.
     *
     * @param metadataCollectionId - String unique Id
     */
    public void setMetadataCollectionId(String metadataCollectionId) {
        this.metadataCollectionId = metadataCollectionId;

        /*
         * Initialize the metadata collection only once the connector is properly set up.
         */
        metadataCollection = new IGCOMRSMetadataCollection(this,
                super.serverName,
                repositoryHelper,
                repositoryValidator,
                metadataCollectionId);
    }

    /**
     * Free up any resources held since the connector is no longer needed.
     */
    @Override
    public void disconnect() {

        // Close the session on the IGC REST client
        this.igcRestClient.disconnect();

        super.metadataCollection = new IGCOMRSMetadataCollection(this,
                super.serverName,
                repositoryHelper,
                repositoryValidator, metadataCollectionId);

    }


    /**
     * Returns the metadata collection object that provides an OMRS abstraction of the metadata within
     * a metadata repository.
     *
     * @return OMRSMetadataCollection - metadata information retrieved from the metadata repository.
     */
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

}
