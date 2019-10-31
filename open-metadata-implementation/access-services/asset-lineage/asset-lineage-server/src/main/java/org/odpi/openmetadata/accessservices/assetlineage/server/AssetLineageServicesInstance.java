/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;


import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.CommonHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ProcessHandler;
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
    private ContextHandler contextHandler;
    private CommonHandler commonHandler;
    private ProcessHandler processHandler;

    /**
     * Set up the handlers for this server.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones      list of zones that AssetLineage is allowed to serve Assets from.
     * @param auditLog            destination for audit log events.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public AssetLineageServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                        List<String> supportedZones,
                                        OMRSAuditLog auditLog) throws NewInstanceException {
        super(myDescription.getAccessServiceName() + " OMAS",
                repositoryConnector,
                auditLog);

        final String methodName = "new ServiceInstance";

        super.supportedZones = supportedZones;

        if (repositoryHandler != null)  {
            glossaryHandler = new GlossaryHandler(serviceName,
                    serverName,
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler);

            contextHandler = new ContextHandler(serviceName,
                    serverName,
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler);

            commonHandler = new CommonHandler(serviceName,
                    serverName,
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler);

            processHandler = new ProcessHandler(serviceName,
                    serverName,
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler);

        }else {
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
    GlossaryHandler getGlossaryHandler()
    {
        return glossaryHandler;
    }


    /**
     * Return the specialized context handler for Asset Lineage OMAS.
     *
     * @return context handler
     */
    ContextHandler getContextHandler()
    {
        return contextHandler;
    }

    /**
     * Return the specialized common handler for Asset Lineage OMAS.
     *
     * @return common handler
     */
    CommonHandler getCommonHandler()
    {
        return commonHandler;
    }

    /**
     * Return the specialized process handler for Asset Lineage OMAS.
     *
     * @return process handler
     */
    ProcessHandler getProcessHandler()
    {
        return processHandler;
    }

}


