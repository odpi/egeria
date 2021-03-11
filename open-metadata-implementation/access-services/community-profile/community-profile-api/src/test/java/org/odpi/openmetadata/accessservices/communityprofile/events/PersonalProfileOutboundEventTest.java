/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentity;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the PersonalProfileOutboundEvent bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class PersonalProfileOutboundEventTest
{
    private static CommunityProfileOutboundEventType eventType = CommunityProfileOutboundEventType.NEW_PERSONAL_PROFILE_EVENT;
    private static String testFullName = "TestFullName";
    private static String testJobTitle = "TestJobTitle";
    private static String testUserId = "TestUserId";

    private PersonalProfile bean = new PersonalProfile();


    /**
     * Default constructor
     */
    public PersonalProfileOutboundEventTest()
    {
        List<UserIdentity>  users = new ArrayList<>();

        UserIdentity userBean = new UserIdentity();
        userBean.setUserId(testUserId);
        users.add(userBean);

        bean.setFullName(testFullName);
        bean.setJobTitle(testJobTitle);
        bean.setAssociatedUserIds(users);
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private PersonalProfileOutboundEvent getTestObject()
    {
        PersonalProfileOutboundEvent testObject = new PersonalProfileOutboundEvent();

        testObject.setEventType(eventType);
        testObject.setPersonalProfile(bean);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(PersonalProfileOutboundEvent resultObject)
    {
        assertTrue(resultObject.getEventType().equals(eventType));
        assertTrue(bean.equals(resultObject.getPersonalProfile()));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        PersonalProfileOutboundEvent nullObject = new PersonalProfileOutboundEvent();

        assertTrue(nullObject.getEventType() == null);

        nullObject = new PersonalProfileOutboundEvent(null);

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

        PersonalProfileOutboundEvent sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        PersonalProfileOutboundEvent anotherObject = getTestObject();
        anotherObject.setEventVersionId(3773L);
        assertFalse(getTestObject().equals(anotherObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        PersonalProfileOutboundEvent testObject = getTestObject();

        assertTrue(testObject.hashCode() != 0);

        PersonalProfileOutboundEvent differentObject = getTestObject();

        differentObject.setEventVersionId(3773L);

        assertFalse(testObject.hashCode() == differentObject.hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new PersonalProfileOutboundEvent(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, PersonalProfileOutboundEvent.class));
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
            validateResultObject((PersonalProfileOutboundEvent) objectMapper.readValue(jsonString, CommunityProfileOutboundEvent.class));
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
            validateResultObject((PersonalProfileOutboundEvent) objectMapper.readValue(jsonString, CommunityProfileEventHeader.class));
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
        assertTrue(getTestObject().toString().contains("PersonalProfileOutboundEvent"));
    }
}
