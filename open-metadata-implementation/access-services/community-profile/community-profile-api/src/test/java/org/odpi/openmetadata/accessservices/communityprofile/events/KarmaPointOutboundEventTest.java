/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the KarmaPointOutboundEvent bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class KarmaPointOutboundEventTest
{
    private static CommunityProfileOutboundEventType eventType  = CommunityProfileOutboundEventType.NEW_PERSONAL_PROFILE_EVENT;
    private static PersonalProfile                   testPP     = new PersonalProfile();
    private static String                            testPPGUID = "TestPersonalProfileGUID";
    private static String                            testUserId = "TestUserId";
    private static long                              plateau    = 9;
    private static long                              points     = 40;


    /**
     * Default constructor
     */
    public KarmaPointOutboundEventTest()
    {
        testPP.setGUID(testPPGUID);
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private KarmaPointOutboundEvent getTestObject()
    {
        KarmaPointOutboundEvent testObject = new KarmaPointOutboundEvent();

        testObject.setEventType(eventType);
        testObject.setUserId(testUserId);
        testObject.setPersonalProfile(testPP);
        testObject.setPlateau(plateau);
        testObject.setPointsTotal(points);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(KarmaPointOutboundEvent resultObject)
    {
        assertTrue(resultObject.getEventType().equals(eventType));
        assertTrue(resultObject.getUserId().equals(testUserId));
        assertTrue(resultObject.getPersonalProfile().equals(testPP));
        assertTrue(resultObject.getPlateau() == plateau);
        assertTrue(resultObject.getPointsTotal() == points);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        KarmaPointOutboundEvent nullObject = new KarmaPointOutboundEvent();

        assertTrue(nullObject.getEventType() == null);

        nullObject = new KarmaPointOutboundEvent(null);

        assertTrue(nullObject.getEventType() == null);
    }




    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("DummyString"));
        assertTrue(getTestObject().equals(getTestObject()));

        CommunityProfileOutboundEvent sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        CommunityProfileOutboundEvent anotherObject = getTestObject();
        anotherObject.setEventVersionId(3773L);
        assertFalse(getTestObject().equals(anotherObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        CommunityProfileOutboundEvent testObject = getTestObject();

        assertTrue(testObject.hashCode() != 0);

        CommunityProfileOutboundEvent differentObject = getTestObject();

        differentObject.setEventVersionId(3773L);

        assertFalse(testObject.hashCode() == differentObject.hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new KarmaPointOutboundEvent(getTestObject()));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        /*
         * This class
         */
        try
        {
            jsonString = objectMapper.writeValueAsString(getTestObject());
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject(objectMapper.readValue(jsonString, KarmaPointOutboundEvent.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        PersonalProfileOutboundEvent superObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(superObject);
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((KarmaPointOutboundEvent) objectMapper.readValue(jsonString, PersonalProfileOutboundEvent.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        CommunityProfileOutboundEvent superSuperObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(superSuperObject);
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((KarmaPointOutboundEvent) objectMapper.readValue(jsonString, CommunityProfileOutboundEvent.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        CommunityProfileEventHeader superSuperSuperObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(superSuperSuperObject);
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((KarmaPointOutboundEvent) objectMapper.readValue(jsonString, CommunityProfileEventHeader.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("KarmaPointOutboundEvent"));
    }
}
