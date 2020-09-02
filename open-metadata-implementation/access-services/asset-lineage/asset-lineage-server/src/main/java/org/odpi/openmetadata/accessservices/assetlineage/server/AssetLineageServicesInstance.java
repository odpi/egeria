/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;


import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.AssetContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ClassificationHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ProcessContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * AssetLineageServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class AssetLineageServicesInstance extends OCFOMASServiceInstance {
    private static AccessServiceDescription myDescription = AccessServiceDescription.ASSET_LINEAGE_OMAS;
    private GlossaryContextHandler glossaryContextHandler;
    private AssetContextHandler assetContextHandler;
    private ProcessContextHandler processContextHandler;
    private ClassificationHandler classificationHandler;
    private AssetLineagePublisher assetLineagePublisher;

    /**
     * Set up the handlers for this server.
     *
     * @param repositoryConnector        link to the repository responsible for servicing the REST calls.
     * @param supportedZones             list of zones that AssetLineage is allowed to serve Assets from.
     * @param lineageClassificationTypes list of lineage classification supported
     * @param localServerUserId          userId used for server initiated actions
     * @param auditLog                   destination for audit log events.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public AssetLineageServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                        List<String> supportedZones,
                                        List<String> lineageClassificationTypes,
                                        String localServerUserId, AuditLog auditLog) throws NewInstanceException {
        super(myDescription.getAccessServiceFullName(),
                repositoryConnector,
                auditLog,
                localServerUserId,
                repositoryConnector.getMaxPageSize());

        super.supportedZones = supportedZones;

        if (repositoryHandler != null) {
            assetContextHandler = new AssetContextHandler(
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler,
                    supportedZones,
                    lineageClassificationTypes);

            processContextHandler = new ProcessContextHandler(
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler,
                    assetContextHandler,
                    supportedZones,
                    lineageClassificationTypes);

            glossaryContextHandler = new GlossaryContextHandler(
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler,
                    assetContextHandler,
                    lineageClassificationTypes);

            classificationHandler = new ClassificationHandler(
                    invalidParameterHandler,
                    lineageClassificationTypes,
                    repositoryHelper);

        } else {
            String methodName = "AssetLineageServicesInstance";
            throw new NewInstanceException(AssetLineageErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                    this.getClass().getName(),
                    methodName);
        }
    }

    /**
     * Return the specialized glossary handler for Asset Lineage OMAS.
     *
     * @return glossary handler
     */
    GlossaryContextHandler getGlossaryContextHandler() {
        return glossaryContextHandler;
    }


    /**
     * Return the specialized context handler for Asset Lineage OMAS.
     *
     * @return context handler
     */
    AssetContextHandler getAssetContextHandler() {
        return assetContextHandler;
    }

    /**
     * Return the specialized process handler for Asset Lineage OMAS.
     *
     * @return process handler
     */
    ProcessContextHandler getProcessContextHandler() {
        return processContextHandler;
    }

    /**
     * Return the specialized classification handler for Asset Lineage OMAS.
     *
     * @return process handler
     */
    ClassificationHandler getClassificationHandler() {
        return classificationHandler;
    }

    public AssetLineagePublisher getAssetLineagePublisher() {
        return assetLineagePublisher;
    }

    public void setAssetLineagePublisher(AssetLineagePublisher assetLineagePublisher) {
        this.assetLineagePublisher = assetLineagePublisher;
    }
}


