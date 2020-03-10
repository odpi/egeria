/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;


import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.AssetContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ClassificationHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ProcessContextHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * AssetLineageServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class AssetLineageServicesInstance extends OCFOMASServiceInstance {
    private static AccessServiceDescription myDescription = AccessServiceDescription.ASSET_LINEAGE_OMAS;
    private GlossaryHandler glossaryHandler;
    private AssetContextHandler assetContextHandler;
    private ProcessContextHandler processContextHandler;
    private ClassificationHandler classificationHandler;

    /**
     * Set up the handlers for this server.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones      list of zones that AssetLineage is allowed to serve Assets from.
     * @param lineageClassificationTypes
     * @param localServerUserId   userId used for server initiated actions
     * @param auditLog            destination for audit log events.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public AssetLineageServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                        List<String> supportedZones,
                                        List<String> lineageClassificationTypes,
                                        String localServerUserId, OMRSAuditLog auditLog) throws NewInstanceException {
        super(myDescription.getAccessServiceFullName(),
                repositoryConnector,
                auditLog,
                localServerUserId,
                repositoryConnector.getMaxPageSize());

        final String methodName = "AssetLineageServicesInstance";

        super.supportedZones = supportedZones;

        if (repositoryHandler != null) {
            glossaryHandler = new GlossaryHandler(
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler);

            assetContextHandler = new AssetContextHandler(
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler,
                    supportedZones);

            processContextHandler = new ProcessContextHandler(
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler,
                    supportedZones);

            classificationHandler = new ClassificationHandler(
                    invalidParameterHandler,
                    lineageClassificationTypes
            );

        } else {
            AssetLineageErrorCode errorCode = AssetLineageErrorCode.OMRS_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    /**
     * Return the specialized glossary handler for Asset Lineage OMAS.
     *
     * @return glossary handler
     */
    GlossaryHandler getGlossaryHandler() {
        return glossaryHandler;
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

}


