/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetcatalog.connectors.outtopic;

import org.odpi.openmetadata.accessservices.assetcatalog.api.AssetCatalogEventInterface;
import org.odpi.openmetadata.accessservices.assetcatalog.api.AssetCatalogEventListener;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogEvent;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListenerConnectorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * AssetCatalogOutTopicClientConnector is the java base class implementation of
 * the client side connector that receives events from the Asset Catalog OMAS's OutTopic.
 */
public class AssetCatalogOutTopicClientConnector extends OpenMetadataTopicListenerConnectorBase implements AssetCatalogEventInterface, AssetCatalogEventListener {
    private static final Logger log = LoggerFactory.getLogger(AssetCatalogOutTopicClientConnector.class);

    private final List<AssetCatalogEventListener> internalEventListeners = new ArrayList<>();


    /**
     * Register a listener object that will be passed each of the events published by
     * the Asset Catalog OMAS.
     *
     * @param userId   calling user
     * @param listener listener object
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    @Override
    public synchronized void registerListener(String userId,
                                              AssetCatalogEventListener listener) throws InvalidParameterException {
        final String methodName = "registerListener";
        final String parameterName = "listener";

        if (listener == null) {
            throw new InvalidParameterException(AssetCatalogErrorCode.NULL_LISTENER.getMessageDefinition(userId, methodName),
                    this.getClass().getName(),
                    methodName,
                    parameterName);
        }

        internalEventListeners.add(listener);
    }


    /**
     * Method to pass an event received on topic.
     *
     * @param event inbound event
     */
    @Override
    public synchronized void processEvent(String event) {
        if (event != null) {
            try {
                AssetCatalogEvent eventObject = super.getEventBean(event, AssetCatalogEvent.class);
                for (AssetCatalogEventListener listener : internalEventListeners) {
                    try {
                        listener.processEvent(eventObject);
                    } catch (Exception error) {
                        log.error("Listener: {} is unable to process event: {}", listener.getClass().getName(), event, error);
                    }
                }
            } catch (Exception error) {
                log.error("Unable to read event: {}", event, error);
            }
        }
    }


    @Override
    public void processEvent(AssetCatalogEvent event) {
        log.debug("process event {}", this.getClass().getName());
    }
}
