/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceAuditHeader;
import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify which versions of an instance InMemoryOMRSMetadataStore.checkInclusiveDate() considers to have
 * been in effect during a requested window.
 * <br>
 * The window is [fromTime, toTime) - fromTime inclusive, toTime exclusive - and a version is in effect
 * over [versionStartTime, versionEndTime).  Every test here is about a boundary, because the interior
 * cases are obvious and were never wrong.  What was wrong, twice, was the instant at each end: a version
 * beginning exactly at toTime was treated as already in effect, and a version superseded exactly at
 * fromTime was treated as still in effect.
 * <br>
 * These are not academic.  A caller taking a timestamp immediately before creating an instance lands in
 * the same millisecond as the create often enough that the conformance suite failed on it - against this
 * store, where a create takes microseconds, rather than against a database, where a round trip pushes the
 * two apart.
 */
public class CheckInclusiveDateTest
{
    private static final long ONE_SECOND = 1000L;
    private static final Date VERSION_START = new Date(1700000000000L);
    private static final Date VERSION_END   = new Date(VERSION_START.getTime() + (60 * ONE_SECOND));


    /**
     * Return a version that began at the given time, as an updated instance would report it.
     *
     * @param startTime when this version came into effect
     * @return instance header
     */
    private InstanceAuditHeader versionStartingAt(Date startTime)
    {
        EntityDetail entity = new EntityDetail();

        entity.setCreateTime(new Date(startTime.getTime() - (10 * ONE_SECOND)));
        entity.setUpdateTime(startTime);

        return entity;
    }


    /**
     * A version that began at exactly the instant the window closes was not yet in effect during it -
     * toTime is exclusive.
     */
    @Test
    public void versionBeginningExactlyAtToTimeIsNotYetInEffect()
    {
        assertFalse(InMemoryOMRSMetadataStore.checkInclusiveDate(null,
                                                                 VERSION_START,
                                                                 versionStartingAt(VERSION_START),
                                                                 null),
                    "A version that began at exactly toTime had not come into existence during the window");
    }


    /**
     * A version that began after the window closed was not in effect during it.
     */
    @Test
    public void versionBeginningAfterToTimeIsNotInEffect()
    {
        Date toTime = new Date(VERSION_START.getTime() - ONE_SECOND);

        assertFalse(InMemoryOMRSMetadataStore.checkInclusiveDate(null,
                                                                 toTime,
                                                                 versionStartingAt(VERSION_START),
                                                                 null),
                    "A version that began after the window closed was not in effect during it");
    }


    /**
     * A version that began before the window closed was in effect during it - the ordinary case.
     */
    @Test
    public void versionBeginningBeforeToTimeIsInEffect()
    {
        Date toTime = new Date(VERSION_START.getTime() + ONE_SECOND);

        assertTrue(InMemoryOMRSMetadataStore.checkInclusiveDate(null,
                                                                toTime,
                                                                versionStartingAt(VERSION_START),
                                                                null),
                   "A version that began before the window closed was in effect during it");
    }


    /**
     * A version that has never been updated starts when the instance was created.
     */
    @Test
    public void createTimeIsUsedWhenTheVersionHasNeverBeenUpdated()
    {
        EntityDetail neverUpdated = new EntityDetail();

        neverUpdated.setCreateTime(VERSION_START);
        neverUpdated.setUpdateTime(null);

        assertFalse(InMemoryOMRSMetadataStore.checkInclusiveDate(null, VERSION_START, neverUpdated, null),
                    "A never-updated instance is in effect from its create time, so the same boundary applies");

        assertTrue(InMemoryOMRSMetadataStore.checkInclusiveDate(null,
                                                               new Date(VERSION_START.getTime() + ONE_SECOND),
                                                               neverUpdated,
                                                               null),
                   "A never-updated instance is in effect once the window extends past its create time");
    }


    /**
     * A version superseded at exactly the instant the window opens was already gone - fromTime is
     * inclusive, and the version that replaced it is the one in effect from then on.
     */
    @Test
    public void versionSupersededExactlyAtFromTimeIsAlreadyGone()
    {
        assertFalse(InMemoryOMRSMetadataStore.checkInclusiveDate(VERSION_END,
                                                                 null,
                                                                 versionStartingAt(VERSION_START),
                                                                 VERSION_END),
                    "A version superseded at exactly fromTime was no longer in effect when the window opened");
    }


    /**
     * A version still in effect after the window opens overlaps it.
     */
    @Test
    public void versionSupersededAfterFromTimeIsInEffect()
    {
        Date fromTime = new Date(VERSION_END.getTime() - ONE_SECOND);

        assertTrue(InMemoryOMRSMetadataStore.checkInclusiveDate(fromTime,
                                                                null,
                                                                versionStartingAt(VERSION_START),
                                                                VERSION_END),
                   "A version superseded after the window opened was in effect during part of it");
    }


    /**
     * A version superseded before the window opened was not in effect during it.
     */
    @Test
    public void versionSupersededBeforeFromTimeIsNotInEffect()
    {
        Date fromTime = new Date(VERSION_END.getTime() + ONE_SECOND);

        assertFalse(InMemoryOMRSMetadataStore.checkInclusiveDate(fromTime,
                                                                 null,
                                                                 versionStartingAt(VERSION_START),
                                                                 VERSION_END),
                    "A version superseded before the window opened was not in effect during it");
    }


    /**
     * The current version has not been superseded, so it is in effect for the rest of the window whatever
     * fromTime says.
     */
    @Test
    public void currentVersionIsInEffectFromWheneverTheWindowOpens()
    {
        Date fromTime = new Date(VERSION_START.getTime() + (600 * ONE_SECOND));

        assertTrue(InMemoryOMRSMetadataStore.checkInclusiveDate(fromTime,
                                                                null,
                                                                versionStartingAt(VERSION_START),
                                                                null),
                   "A version that has never been superseded is still in effect");
    }


    /**
     * An unbounded window covers every version.
     */
    @Test
    public void unboundedWindowCoversEveryVersion()
    {
        assertTrue(InMemoryOMRSMetadataStore.checkInclusiveDate(null,
                                                                null,
                                                                versionStartingAt(VERSION_START),
                                                                VERSION_END),
                   "A window with no bounds covers a superseded version");

        assertTrue(InMemoryOMRSMetadataStore.checkInclusiveDate(null,
                                                                null,
                                                                versionStartingAt(VERSION_START),
                                                                null),
                   "A window with no bounds covers the current version");
    }
}
