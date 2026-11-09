/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

/**
 * Verify that a caller's subscription to a topic is scoped by the topic, so deployments that share an event bus
 * and a content pack do not share subscriptions (for Apache Kafka, consumer groups).
 */
public class TopicSubscriberIdTest
{
    private static final String CALLER_ID = "ab72825d-d96f-4ff6-96d1-155c1a3d5133";


    private static Connection topicConnection(String topicName)
    {
        Connection connection = new Connection();
        Endpoint   endpoint   = new Endpoint();

        endpoint.setNetworkAddress(topicName);
        connection.setEndpoint(endpoint);

        return connection;
    }


    /**
     * The same caller on the out topics of two metadata stores subscribes under two identities.
     */
    @Test
    public void testDifferentTopicsGiveDifferentIdentities()
    {
        String quickstart = AccessServerServiceInstance.getTopicSubscriberId(CALLER_ID, topicConnection("egeria.omag.server.qs-metadata-store.omas.openmetadatastore.outTopic"));
        String fvt        = AccessServerServiceInstance.getTopicSubscriberId(CALLER_ID, topicConnection("egeria.omag.openlineage-fvt.server.openlineageFvtMetadataStore.omas.openmetadatastore.outTopic"));

        assertNotEquals(quickstart, fvt);
        assertEquals(quickstart, CALLER_ID + "@egeria.omag.server.qs-metadata-store.omas.openmetadatastore.outTopic");
    }


    /**
     * Callers with the same identifier on the same topic share one identity, so they divide the topic's events.
     */
    @Test
    public void testSameCallerAndTopicShareIdentity()
    {
        String topicName = "egeria.omag.server.qs-metadata-store.omas.opengovernanceservice.outTopic";

        assertEquals(AccessServerServiceInstance.getTopicSubscriberId("EgeriaGovernance", topicConnection(topicName)),
                     AccessServerServiceInstance.getTopicSubscriberId("EgeriaGovernance", topicConnection(topicName)));
    }


    /**
     * Without a topic name, the caller identifier is used unchanged.
     */
    @Test
    public void testNoTopicName()
    {
        assertEquals(AccessServerServiceInstance.getTopicSubscriberId(CALLER_ID, null), CALLER_ID);
        assertEquals(AccessServerServiceInstance.getTopicSubscriberId(CALLER_ID, new Connection()), CALLER_ID);
        assertEquals(AccessServerServiceInstance.getTopicSubscriberId(CALLER_ID, topicConnection(" ")), CALLER_ID);
    }
}
