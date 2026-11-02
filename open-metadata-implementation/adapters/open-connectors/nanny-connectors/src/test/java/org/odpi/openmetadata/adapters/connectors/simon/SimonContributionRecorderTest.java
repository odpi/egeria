/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.simon;

import org.odpi.openmetadata.adapters.connectors.simon.controls.SimonConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementVersions;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;


/**
 * Verify how the Simon contribution recorder turns the open metadata events into karma points.  These tests
 * drive the event processing directly, so they need no integration context.
 */
public class SimonContributionRecorderTest
{
    /**
     * Build an event as the metadata access server would.
     *
     * @param eventType type of event
     * @param createdBy user that created the element
     * @param updatedBy user that last updated the element - null if it has not been updated
     * @return event
     */
    private OpenMetadataOutTopicEvent getEvent(OpenMetadataEventType eventType,
                                               String                createdBy,
                                               String                updatedBy)
    {
        ElementVersions versions = new ElementVersions();

        versions.setCreatedBy(createdBy);
        versions.setUpdatedBy(updatedBy);

        ElementHeader elementHeader = new ElementHeader();

        elementHeader.setVersions(versions);

        OpenMetadataOutTopicEvent event = new OpenMetadataOutTopicEvent();

        event.setEventType(eventType);
        event.setElementHeader(elementHeader);

        return event;
    }


    /**
     * Each change earns a karma point for the user that made it: the last updater, or the creator if the element
     * has not been updated.  Refresh events report no change so earn nothing.
     */
    @Test public void testAwarding()
    {
        SimonContributionRecorderConnector connector = new SimonContributionRecorderConnector();

        connector.processEvent(getEvent(OpenMetadataEventType.NEW_ELEMENT_CREATED, "erinoverview", null));
        connector.processEvent(getEvent(OpenMetadataEventType.ELEMENT_UPDATED, "erinoverview", "peterprofile"));
        connector.processEvent(getEvent(OpenMetadataEventType.ELEMENT_CLASSIFIED, "erinoverview", "peterprofile"));
        connector.processEvent(getEvent(OpenMetadataEventType.ELEMENT_DELETED, "peterprofile", "erinoverview"));
        connector.processEvent(getEvent(OpenMetadataEventType.REFRESH_ELEMENT_EVENT, "erinoverview", "erinoverview"));
        connector.processEvent(getEvent(OpenMetadataEventType.UNKNOWN_EVENT, "erinoverview", "erinoverview"));
        connector.processEvent(getEvent(OpenMetadataEventType.ELEMENT_UPDATED, null, null));
        connector.processEvent(new OpenMetadataOutTopicEvent());
        connector.processEvent(null);

        Map<String, Long> pending = connector.getPendingKarmaPoints();

        assertEquals(pending.size(), 2);
        assertEquals(pending.get("erinoverview"), Long.valueOf(2L));
        assertEquals(pending.get("peterprofile"), Long.valueOf(2L));
    }


    /**
     * The users whose userIds match the ignoredUserIdPatterns configuration property are not awarded karma points.
     * The content pack's default patterns match the non-personal accounts and the governance engines.
     */
    @Test public void testIgnoredUserPatterns()
    {
        SimonContributionRecorderConnector connector = new SimonContributionRecorderConnector();

        connector.setIgnoredUserIdPatterns(SimonConfigurationProperty.DEFAULT_IGNORED_USER_ID_PATTERNS, "simonnpa");
        connector.processEvent(getEvent(OpenMetadataEventType.ELEMENT_UPDATED, "erinoverview", "simonnpa"));
        connector.processEvent(getEvent(OpenMetadataEventType.ELEMENT_UPDATED, "erinoverview", "pepysnpa"));
        connector.processEvent(getEvent(OpenMetadataEventType.NEW_ELEMENT_CREATED, "assetonboardingengine", null));

        assertTrue(connector.getPendingKarmaPoints().isEmpty());

        /*
         * The patterns match the whole userId, so a userId that only contains npa or engine is awarded karma points.
         */
        connector.processEvent(getEvent(OpenMetadataEventType.NEW_ELEMENT_CREATED, "npauser", null));
        connector.processEvent(getEvent(OpenMetadataEventType.NEW_ELEMENT_CREATED, "engineer", null));

        assertEquals(connector.getPendingKarmaPoints().size(), 2);
    }


    /**
     * The connector's own changes are ignored even when no pattern matches its userId, or the property is not
     * set - otherwise each update of a contribution record would earn another karma point.  Its userId is matched
     * exactly, so regular expression characters in it have no effect.
     */
    @Test public void testOwnChangesAlwaysIgnored()
    {
        SimonContributionRecorderConnector connector = new SimonContributionRecorderConnector();

        connector.setIgnoredUserIdPatterns(null, "simon.recorder");
        connector.processEvent(getEvent(OpenMetadataEventType.ELEMENT_UPDATED, "erinoverview", "simon.recorder"));

        assertTrue(connector.getPendingKarmaPoints().isEmpty());

        connector.processEvent(getEvent(OpenMetadataEventType.ELEMENT_UPDATED, "erinoverview", "simonXrecorder"));

        assertEquals(connector.getPendingKarmaPoints().get("simonXrecorder"), Long.valueOf(1L));

        connector = new SimonContributionRecorderConnector();
        connector.setIgnoredUserIdPatterns(List.of(".*engine"), "simonnpa");
        connector.processEvent(getEvent(OpenMetadataEventType.ELEMENT_UPDATED, "erinoverview", "simonnpa"));

        assertTrue(connector.getPendingKarmaPoints().isEmpty());
    }


    /**
     * A pattern that is not a valid regular expression is skipped, and the others still apply.
     */
    @Test public void testInvalidPatternSkipped()
    {
        SimonContributionRecorderConnector connector = new SimonContributionRecorderConnector();

        connector.setIgnoredUserIdPatterns(List.of("[npa", ".*engine"), "simonnpa");
        connector.processEvent(getEvent(OpenMetadataEventType.NEW_ELEMENT_CREATED, "assetonboardingengine", null));

        assertTrue(connector.getPendingKarmaPoints().isEmpty());

        connector.processEvent(getEvent(OpenMetadataEventType.NEW_ELEMENT_CREATED, "daemonnpa", null));

        assertEquals(connector.getPendingKarmaPoints().get("daemonnpa"), Long.valueOf(1L));
    }
}
