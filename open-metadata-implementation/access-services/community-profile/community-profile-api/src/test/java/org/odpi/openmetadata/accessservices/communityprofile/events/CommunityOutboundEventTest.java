/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.Community;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentity;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the CommunityOutboundEvent bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class CommunityOutboundEventTest
{
    private static CommunityProfileOutboundEventType eventType    = CommunityProfileOutboundEventType.NEW_PERSONAL_PROFILE_EVENT;
    private static String                            testMission  = "TestMission";
    private static String                            testUserId   = "TestUserId";

    private Community bean = new Community();


    /**
     * Default constructor
     */
    public CommunityOutboundEventTest()
    {
        List<UserIdentity>  users = new ArrayList<>();

        UserIdentity userBean = new UserIdentity();
        userBean.setUserId(testUserId);
        users.add(userBean);

        bean.setMission(testMission);
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private CommunityOutboundEvent getTestObject()
    {
        CommunityOutboundEvent testObject = new CommunityOutboundEvent();

        testObject.setEventType(eventType);
        testObject.setCommunity(bean);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(CommunityOutboundEvent resultObject)
    {
        assertTrue(resultObject.getEventType().equals(eventType));
        assertTrue(bean.equals(resultObject.getCommunity()));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        CommunityOutboundEvent nullObject = new CommunityOutboundEvent();

        assertTrue(nullObject.getEventType() == null);

        nullObject = new CommunityOutboundEvent(null);

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

        CommunityOutboundEvent sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        CommunityOutboundEvent anotherObject = getTestObject();
        anotherObject.setEventVersionId(3773L);
        assertFalse(getTestObject().equals(anotherObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        CommunityOutboundEvent testObject = getTestObject();

        assertTrue(testObject.hashCode() != 0);

        CommunityOutboundEvent differentObject = getTestObject();

        differentObject.setEventVersionId(3773L);

        assertFalse(testObject.hashCode() == differentObject.hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new CommunityOutboundEvent(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, CommunityOutboundEvent.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        CommunityProfileEventHeader superObject = getTestObject();

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
            validateResultObject((CommunityOutboundEvent) objectMapper.readValue(jsonString, CommunityProfileOutboundEvent.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        CommunityProfileEventHeader superSuperObject = getTestObject();

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
            validateResultObject((CommunityOutboundEvent) objectMapper.readValue(jsonString, CommunityProfileEventHeader.class));
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
        assertTrue(getTestObject().toString().contains("CommunityOutboundEvent"));
    }
}
