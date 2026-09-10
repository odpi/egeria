/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

/**
 * OpenLineageListenerManager is the interface used by integration connectors to register for, and publish,
 * OpenLineage events within an integration daemon.
 */
public interface OpenLineageListenerManager
{
    /**
     * The listener is implemented by the integration connector to receive OpenLineage events.
     * Only one listener can be registered by a connector.
     *
     * @param listener listener to register
     */
    void registerListener(OpenLineageEventListener listener);


    /**
     * Called each time an OpenLineage event is published to the integration daemon as a JSON string.  The event is
     * parsed into the appropriate bean (run event, job event or dataset event, determined by the properties present)
     * and delivered to each of the registered listeners.  If the event cannot be parsed it is delivered to the run
     * event listeners with a null bean and the raw event only.
     *
     * @param rawEvent event in Json form from the originator - may have facets that are not known to Egeria
     */
    void publishOpenLineageRunEvent(String rawEvent);


    /**
     * Called each time an OpenLineage run event is published to the integration daemon as a bean.  The event is
     * serialized into JSON and delivered, along with the bean, to each of the registered listeners.
     *
     * @param event bean representation of the event
     */
    void publishOpenLineageRunEvent(OpenLineageRunEvent event);


    /**
     * Called each time an OpenLineage job event is published to the integration daemon as a bean.  The event is
     * serialized into JSON and delivered, along with the bean, to each of the registered listeners.
     *
     * @param event bean representation of the event
     */
    void publishOpenLineageJobEvent(OpenLineageJobEvent event);


    /**
     * Called each time an OpenLineage dataset event is published to the integration daemon as a bean.  The event is
     * serialized into JSON and delivered, along with the bean, to each of the registered listeners.
     *
     * @param event bean representation of the event
     */
    void publishOpenLineageDataSetEvent(OpenLineageDataSetEvent event);
}
