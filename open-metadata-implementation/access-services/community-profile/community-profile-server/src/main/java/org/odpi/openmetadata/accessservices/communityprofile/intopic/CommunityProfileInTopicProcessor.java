/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.intopic;

import org.odpi.openmetadata.accessservices.communityprofile.server.CommunityProfileServicesInstance;
import org.odpi.openmetadata.accessservices.communityprofile.topics.CommunityProfileInTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommunityProfileInTopicProcessor extends CommunityProfileInTopicListener
{
    private static final Logger log = LoggerFactory.getLogger(CommunityProfileInTopicProcessor.class);

    private CommunityProfileServicesInstance   instance;

    /**
     * The constructor is given the connection to the out topic for Community Profile OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param inTopicConnector  connector to receive incoming events
     * @param instance  server instance
     */
    public CommunityProfileInTopicProcessor(OpenMetadataTopicConnector         inTopicConnector,
                                            CommunityProfileServicesInstance   instance)
    {
        super();

        this.instance = instance;
    }


    /**
     * The server is shutting down
     */
    public void shutdown()
    {

    }
}
