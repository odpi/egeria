/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.listener;

import org.odpi.openmetadata.accessservices.dataplatform.outtopic.DataPlatformOutTopicPublisher;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * DataPlatformOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes NEW_ENTITY_EVENTs to the publisher.
 */
public class DataPlatformOMRSTopicListener extends OMRSTopicListenerBase
{
    private static final Logger log = LoggerFactory.getLogger(DataPlatformOMRSTopicListener.class);

    private OMRSRepositoryHelper          repositoryHelper;
    private List<String>                  supportedZones;
    private DataPlatformOutTopicPublisher eventPublisher;


    /**
     * Initialize the topic listener.
     *
     * @param serviceName this is the full name of the service - used for error logging in base class
     * @param eventPublisher this is the out topic publisher.
     * @param supportedZones list of zones for the visible assets
     * @param repositoryHelper repository helper
     * @param auditLog logging destination
     */
    public DataPlatformOMRSTopicListener(String                        serviceName,
                                         DataPlatformOutTopicPublisher eventPublisher,
                                         List<String>                  supportedZones,
                                         OMRSRepositoryHelper          repositoryHelper,
                                         AuditLog                      auditLog)
    {
        super(serviceName, auditLog);

        this.repositoryHelper = repositoryHelper;
        this.supportedZones = supportedZones;

        this.eventPublisher = eventPublisher;
    }

    /**
     * Unpack and deliver an instance event to the InstanceEventProcessor
     *
     * @param instanceEvent event to unpack
     */
    public void processInstanceEvent(OMRSInstanceEvent  instanceEvent)
    {

    }



    /**
     * Determines whether an Asset is in the supported zones.
     *
     * @param assetZones list of zones for the asset.
     * @return boolean flag
     */
    private boolean inTheZone(List<String>   assetZones)
    {
        if (supportedZones == null)
        {
            /*
             * If supported zones is null then all zones are supported.
             */
            return true;
        }
        else if (assetZones == null)
        {
            /*
             * If there are no zones set up in the asset then it is a member of all zones.
             */
            return true;
        }
        else
        {
            for (String supportedZoneName : supportedZones)
            {
                for (String assetZoneName : assetZones)
                {
                    if (supportedZoneName.equals(assetZoneName))
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
