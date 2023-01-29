/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.repository.listener;

import org.odpi.openmetadata.conformance.tests.repository.types.*;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * ConformanceSuiteOMRSTopicListener receives details of each OMRS event from the cohorts that the OMAG
 * server running the Conformance suite is connected to.  It validates the content the events receives to make sure
 * the fields are filled out and are consistent with responses from the REST API.
 *
 */
public class ConformanceSuiteOMRSTopicListener implements OMRSTopicListener
{
    private static final Logger log = LoggerFactory.getLogger(ConformanceSuiteOMRSTopicListener.class);

    private final RepositoryConformanceWorkPad       workPad;

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
    public ConformanceSuiteOMRSTopicListener(RepositoryConformanceWorkPad   workPad)
    {
        this.workPad = workPad;
    }


    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    public void processRegistryEvent(OMRSRegistryEvent event)
    {
        // Registry events are not passed to the Enterprise topic listener
    }


    /**
     * Method to pass a TypeDef event received on topic.
     *
     * @param event inbound event
     */
    public void processTypeDefEvent(OMRSTypeDefEvent event)
    {
        log.debug("Processing type event: " + event.toString());

        if (workPad.getTutRepositoryConnector() == null)
        {
            bufferedTypeDefEvents.add(event);
        }
        else
        {
            if (!bufferedTypeDefEvents.isEmpty())
            {
                /*
                 * Process the events that arrived before the cohort registration completed.
                 */
                for (OMRSTypeDefEvent  typeDefEvent: bufferedTypeDefEvents)
                {
                    testTypeDefEvent(typeDefEvent);
                }

                bufferedTypeDefEvents = new ArrayList<>();
            }

            testTypeDefEvent(event);
        }
    }


    /**
     * Test a specific TypeDef event.
     *
     * @param event event to test
     */
    private  void testTypeDefEvent(OMRSTypeDefEvent event)
    {
        typeDefEventCount ++;

        /*
         * All events have an initial test.  If an additional technology is connected to the cohort
         * then their events will be validated and any errors may affect the technology under test's results.
         * This is why the tester is requested not to have other servers in the cohort while testing is underway.
         */
        TestValidTypeDefEvent testValidTypeDefEvent = new TestValidTypeDefEvent(workPad,
                                                                                typeDefEventCount,
                                                                                event);

        testValidTypeDefEvent.executeTest();

        String           serverName           = testValidTypeDefEvent.getServerName();
        String           metadataCollectionId = testValidTypeDefEvent.getMetadataCollectionId();

        /*
         * New tests are initiated if this event comes from the technology under test.
         */
        if ((serverName != null) &&
            (serverName.equals(workPad.getTutServerName())) &&
            (metadataCollectionId != null) &&
            (metadataCollectionId.equals(workPad.getTutMetadataCollectionId())))
        {
            AttributeTypeDef attributeTypeDef = testValidTypeDefEvent.getExtractedAttributeTypeDef();
            TypeDef          typeDef          = testValidTypeDefEvent.getExtractedTypeDef();

            if (attributeTypeDef != null)
            {
                TestSupportedAttributeTypeDef testSupportedAttributeTypeDef = new TestSupportedAttributeTypeDef(workPad,
                                                                                                                attributeTypeDef,
                                                                                                                "event-" + typeDefEventCount,
                                                                                                                RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_NOTIFICATIONS.getProfileId(),
                                                                                                                RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_NOTIFICATIONS.getRequirementId());
                testSupportedAttributeTypeDef.executeTest();

                TestConsistentAttributeTypeDef testConsistentAttributeTypeDef =
                        new TestConsistentAttributeTypeDef(workPad,
                                                           attributeTypeDef,
                                                           typeDefEventCount);

                testConsistentAttributeTypeDef.executeTest();

            }
            else if (typeDef != null)
            {
                TestSupportedTypeDef testSupportedTypeDef = new TestSupportedTypeDef(workPad,
                                                                                     typeDef,
                                                                                     "event-" + typeDefEventCount,
                                                                                     RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_NOTIFICATIONS.getProfileId(),
                                                                                     RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_NOTIFICATIONS.getRequirementId());
                testSupportedTypeDef.executeTest();

                TestConsistentTypeDef testConsistentTypeDef = new TestConsistentTypeDef(workPad,
                                                                                        typeDef,
                                                                                        typeDefEventCount);

                testConsistentTypeDef.executeTest();
            }
        }
    }


    /**
     * Method to pass an instance event received on topic.
     *
     * @param event event to unpack
     */
    public void processInstanceEvent(OMRSInstanceEvent  event)
    {
        log.debug("Processing instance event: " + event);

        if (workPad.getTutRepositoryConnector() == null)
        {
            bufferedInstanceEvents.add(event);
        }
        else
        {
            if (!bufferedInstanceEvents.isEmpty())
            {
                /*
                 * Process the events that arrived before the cohort registration completed.
                 */
                for (OMRSInstanceEvent  instanceEvent: bufferedInstanceEvents)
                {
                    testInstanceEvent(instanceEvent);
                }

                bufferedInstanceEvents = new ArrayList<>();
            }

            testInstanceEvent(event);
        }
    }


    /**
     * Test a specific event.
     *
     * @param event event to test
     */
    private  void testInstanceEvent(OMRSInstanceEvent event)
    {

    }
}
