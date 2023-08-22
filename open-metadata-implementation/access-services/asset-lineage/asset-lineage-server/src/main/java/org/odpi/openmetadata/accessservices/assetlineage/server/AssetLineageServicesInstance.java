/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;


import org.odpi.openmetadata.accessservices.assetlineage.converters.GenericStubConverter;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.AssetContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ClassificationHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.HandlerHelper;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ProcessContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.model.GenericStub;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.connector.AssetLineageOutTopicClientProvider;
import org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageTypesValidator;
import org.odpi.openmetadata.accessservices.assetlineage.util.ClockService;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.time.Clock;
import java.util.List;

/**
 * AssetLineageServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class AssetLineageServicesInstance extends OMASServiceInstance {
    private static final AccessServiceDescription description = AccessServiceDescription.ASSET_LINEAGE_OMAS;
    private final GlossaryContextHandler glossaryContextHandler;
    private final AssetContextHandler assetContextHandler;
    private final ProcessContextHandler processContextHandler;
    private final ClassificationHandler classificationHandler;
    private final HandlerHelper handlerHelper;

    private AssetLineagePublisher assetLineagePublisher;

    /**
     * Set up the handlers for this server.
     *
     * @param repositoryConnector        link to the repository responsible for servicing the REST calls.
     * @param supportedZones             list of zones that AssetLineage is allowed to serve Assets from.
     * @param localServerUserId          userId used for server initiated actions
     * @param auditLog                   destination for audit log events.
     * @param outTopicEventBusConnection the out topic connector
     * @param assetLineageTypesValidator service for validating types
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public AssetLineageServicesInstance(OMRSRepositoryConnector repositoryConnector, List<String> supportedZones, String localServerUserId,
                                        AuditLog auditLog, Connection outTopicEventBusConnection,
                                        AssetLineageTypesValidator assetLineageTypesValidator) throws NewInstanceException {
        super(description.getAccessServiceFullName(), repositoryConnector, null, null, null, auditLog,
                localServerUserId, repositoryConnector.getMaxPageSize(), null, null,
                AssetLineageOutTopicClientProvider.class.getName(), outTopicEventBusConnection);

        super.supportedZones = supportedZones;

        if (repositoryHandler == null) {
            String methodName = "AssetLineageServicesInstance";
            throw new NewInstanceException(AssetLineageErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(serverName),
                    this.getClass().getName(), methodName);
        }

        OpenMetadataAPIGenericHandler<GenericStub> genericHandler =
                new OpenMetadataAPIGenericHandler<>(new GenericStubConverter<>(repositoryHelper, serviceName, serverName),
                        GenericStub.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                        localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);

        Converter converter = new Converter(repositoryHelper);
        ClockService clockService = new ClockService(Clock.systemUTC());
        handlerHelper = new HandlerHelper(invalidParameterHandler, repositoryHelper, genericHandler, converter,
                assetLineageTypesValidator, clockService);
        assetContextHandler = new AssetContextHandler(genericHandler, handlerHelper, supportedZones, clockService);
        processContextHandler = new ProcessContextHandler(assetContextHandler, handlerHelper, supportedZones);
        glossaryContextHandler = new GlossaryContextHandler(invalidParameterHandler, assetContextHandler, handlerHelper);
        classificationHandler = new ClassificationHandler(invalidParameterHandler, handlerHelper);
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

    /**
     * Return the handler helper for Asset Lineage OMAS.
     *
     * @return process handler
     */
    HandlerHelper getHandlerHelper() {
        return handlerHelper;
    }

    public AssetLineagePublisher getAssetLineagePublisher() {
        return assetLineagePublisher;
    }

    public void setAssetLineagePublisher(AssetLineagePublisher assetLineagePublisher) {
        this.assetLineagePublisher = assetLineagePublisher;
    }
}


