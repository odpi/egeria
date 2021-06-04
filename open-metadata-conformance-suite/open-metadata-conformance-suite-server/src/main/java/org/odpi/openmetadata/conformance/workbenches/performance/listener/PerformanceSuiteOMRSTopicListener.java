/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.performance.listener;

import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * PerformanceSuiteOMRSTopicListener receives details of each OMRS event from the cohorts that the OMAG
 * server running the Conformance suite is connected to.  It validates the content the events receives to make sure
 * the fields are filled out and are consistent with responses from the REST API.
 *
 */
public class PerformanceSuiteOMRSTopicListener implements OMRSTopicListener
{
    private static final Logger log = LoggerFactory.getLogger(PerformanceSuiteOMRSTopicListener.class);

    private PerformanceWorkPad                 workPad;

    private List<OMRSTypeDefEvent>             bufferedTypeDefEvents = new ArrayList<>();
    private int                                typeDefEventCount     = 0;

    private List<OMRSInstanceEvent>            bufferedInstanceEvents = new ArrayList<>();
    private int                                instanceEventCount     = 0;


    /**
     * The constructor is given the properties and objects it needs to verify the contents of events received from
     * the technology under test.
     *
     * @param workPad source of configuration and place to store results.
     */
    public PerformanceSuiteOMRSTopicListener(PerformanceWorkPad workPad)
    {
        this.workPad = workPad;
    }


    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    @Override
    public void processRegistryEvent(OMRSRegistryEvent event)
    {
        // Registry events are not passed to the Enterprise topic listener
    }


    /**
     * Method to pass a TypeDef event received on topic.
     *
     * @param event inbound event
     */
    @Override
    public void processTypeDefEvent(OMRSTypeDefEvent event)
    {
        // For now, do nothing (events are not (yet) part of performance test)
    }


    /**
     * Method to pass an instance event received on topic.
     *
     * @param event event to unpack
     */
    @Override
    public void processInstanceEvent(OMRSInstanceEvent  event)
    {
        // For now, do nothing (events are not (yet) part of performance test)
    }
}
