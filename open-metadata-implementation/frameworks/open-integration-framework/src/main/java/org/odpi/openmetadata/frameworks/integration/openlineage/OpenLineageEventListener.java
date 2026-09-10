/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

/**
 * OpenLineageEventListener is the interface implemented by integration connectors that wish to receive OpenLineage
 * events published through the integration daemon.  Run events are the most common, and the only mandatory method
 * to implement.  Job events and dataset events (added in OpenLineage spec 2-0-x) are delivered through default
 * methods so that existing listeners are unaffected; override them to receive those events.
 */
public interface OpenLineageEventListener
{
    /**
     * Called each time an OpenLineage run event is published to the integration daemon.  The integration connector
     * is only expected to do something and return quickly.  Only the first parameter is guaranteed to be populated.
     * The rawEvent parameter is only populated if the event was received as a JSON string.
     *
     * @param event bean representation of the run event
     * @param rawEvent event in Json form from the originator - may have facets that are not known to Egeria
     */
    void processOpenLineageRunEvent(OpenLineageRunEvent event,
                                    String              rawEvent);


    /**
     * Called each time an OpenLineage job event is published to the integration daemon.  Job events describe a job
     * and its inputs/outputs independently of a run.  The default implementation ignores the event.
     *
     * @param event bean representation of the job event
     * @param rawEvent event in Json form from the originator - may have facets that are not known to Egeria
     */
    default void processOpenLineageJobEvent(OpenLineageJobEvent event,
                                            String              rawEvent)
    {
        // ignored by default
    }


    /**
     * Called each time an OpenLineage dataset event is published to the integration daemon.  Dataset events describe
     * a dataset independently of a run.  The default implementation ignores the event.
     *
     * @param event bean representation of the dataset event
     * @param rawEvent event in Json form from the originator - may have facets that are not known to Egeria
     */
    default void processOpenLineageDataSetEvent(OpenLineageDataSetEvent event,
                                                String                  rawEvent)
    {
        // ignored by default
    }
}
