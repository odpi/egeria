/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.outtopic;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AssetLineagePublisher is responsible for publishing events about lineage.  It is called
 * when an interesting OMRS Event is added to the Enterprise OMRS Topic.  It adds events to the Asset lineage OMAS
 * out topic.
 */
public class AssetLineagePublisher {
    private static final String personalProfileTypeName = "PersonalProfile";

    private static final Logger log = LoggerFactory.getLogger(AssetLineagePublisher.class);

    private Connection personalProfileConsumerOutTopic;
    private OMRSRepositoryHelper repositoryHelper;
    private OMRSRepositoryValidator repositoryValidator;
    private String serviceName;


    /**
     * The constructor is given the connection to the out topic for Asset lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param personalProfileConsumerOutTopic connection to the out topic
     * @param repositoryConnector             provides methods for retrieving metadata instances
     * @param repositoryHelper                provides methods for working with metadata instances
     * @param repositoryValidator             provides validation of metadata instance
     * @param serviceName                     name of this service.
     */
    public AssetLineagePublisher(Connection personalProfileConsumerOutTopic,
                                     OMRSRepositoryConnector repositoryConnector,
                                     OMRSRepositoryHelper repositoryHelper,
                                     OMRSRepositoryValidator repositoryValidator,
                                     String serviceName) {
        this.personalProfileConsumerOutTopic = personalProfileConsumerOutTopic;
        this.repositoryHelper = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.serviceName = serviceName;
    }


}


