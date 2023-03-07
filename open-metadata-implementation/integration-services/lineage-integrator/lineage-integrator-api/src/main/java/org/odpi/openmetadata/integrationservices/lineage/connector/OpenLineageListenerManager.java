/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.connector;


import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageRunEvent;

public interface OpenLineageListenerManager
{
    /**
     * The listener is implemented by the integration connector.  Once it is registered with the context, its processOpenLineageRunEvent()
     * method is called each time an open lineage event is published to the Lineage Integrator OMIS.
     *
     * @param listener listener to call
     */
    void registerListener(OpenLineageEventListener listener);


    /**
     * Called each time an open lineage run event is published to the Lineage Integrator OMIS.  The integration connector is able to
     * work with the formatted event using the Egeria beans or reformat the open lineage run event using the supplied open lineage backend beans
     * or another set of beans.
     *
     * @param rawEvent json payload received for the event
     */
    void publishOpenLineageRunEvent(String rawEvent);


    /**
     * Called each time an open lineage run event is published to the Lineage Integrator OMIS.  The integration connector is able to
     * work with the formatted event using the Egeria beans or reformat the open lineage run event using the supplied open lineage backend beans
     * or another set of beans.
     *
     * @param event bean for the event
     */
    void publishOpenLineageRunEvent(OpenLineageRunEvent event);
}
