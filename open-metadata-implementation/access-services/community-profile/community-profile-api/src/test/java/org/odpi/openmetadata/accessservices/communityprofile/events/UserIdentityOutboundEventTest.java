/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentity;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the UserIdentityOutboundEvent bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class UserIdentityOutboundEventTest
{
    private static CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.NEW_USER_IDENTITY_EVENT;
    private static String testUserId = "TestUserId";

    private UserIdentity bean = new UserIdentity();


    /**
     * Default constructor
     */
    public UserIdentityOutboundEventTest()
    {
        bean.setUserId(testUserId);
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private UserIdentityOutboundEvent getTestObject()
    {
        UserIdentityOutboundEvent testObject = new UserIdentityOutboundEvent();

        testObject.setEventType(eventType);
        testObject.setUserIdentity(bean);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(UserIdentityOutboundEvent resultObject)
    {
        assertTrue(resultObject.getEventType().equals(eventType));
        assertTrue(bean.equals(resultObject.getUserIdentity()));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        UserIdentityOutboundEvent nullObject = new UserIdentityOutboundEvent();

        assertTrue(nullObject.getEventType() == null);

        nullObject = new UserIdentityOutboundEvent(null);

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

        UserIdentityOutboundEvent sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        UserIdentityOutboundEvent anotherObject = getTestObject();
        anotherObject.setEventVersionId(3773L);
        assertFalse(getTestObject().equals(anotherObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        UserIdentityOutboundEvent testObject = getTestObject();

        assertTrue(testObject.hashCode() != 0);

        UserIdentityOutboundEvent differentObject = getTestObject();

        differentObject.setEventVersionId(3773L);

        assertFalse(testObject.hashCode() == differentObject.hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new UserIdentityOutboundEvent(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, UserIdentityOutboundEvent.class));
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
            validateResultObject((UserIdentityOutboundEvent) objectMapper.readValue(jsonString, CommunityProfileOutboundEvent.class));
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
            validateResultObject((UserIdentityOutboundEvent) objectMapper.readValue(jsonString, CommunityProfileEventHeader.class));
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
        assertTrue(getTestObject().toString().contains("UserIdentityOutboundEvent"));
    }
}
