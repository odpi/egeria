/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.accessconnector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityErrorCode;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

/**
 * Publishing an element moves it to the zones in the publishZones property of the caller's account, and
 * withdrawing it moves it to the zones in the defaultZones property.  A property that is present says what the
 * zones are - including that there are none.  A property that is absent says nothing, and the connector refuses
 * the request rather than guess: taking it to mean "no zones" would open the element to everyone.
 */
public class PublishWithdrawZonesTest
{
    private static final String       USER          = "erinoverview";
    private static final List<String> CURRENT_ZONES = List.of("quarantine");


    /**
     * The publish zones replace the element's current zones; they are not added to them.
     */
    @Test
    public void testPublishZonesAreReturnedInsteadOfTheCurrentZones() throws Exception
    {
        Map<String, Object> otherProperties = new HashMap<>();

        otherProperties.put("publishZones", List.of("trusted", "data-lake"));

        assertEquals(this.getConnector(otherProperties).getPublishZonesForUser(CURRENT_ZONES, "Asset", "test", USER),
                     List.of("trusted", "data-lake"));
    }


    /**
     * The zones for a withdrawn element are exactly the default zones.  Keeping the current zones as well would
     * leave the element as visible as it was while published.
     */
    @Test
    public void testWithdrawZonesAreTheDefaultZonesAlone() throws Exception
    {
        Map<String, Object> otherProperties = new HashMap<>();

        otherProperties.put("defaultZones", List.of("quarantine"));

        assertEquals(this.getConnector(otherProperties).getWithdrawZonesForUser(List.of("trusted"), "Asset", "test", USER),
                     List.of("quarantine"));
    }


    /**
     * The default zones are still added to the zones requested when an element is created, and an account
     * with no default zones still creates elements in just the zones requested.
     */
    @Test
    public void testDefaultZonesAreStillAddedOnCreate() throws Exception
    {
        Map<String, Object> otherProperties = new HashMap<>();

        otherProperties.put("defaultZones", List.of("quarantine"));

        List<String> zones = this.getConnector(otherProperties).getDefaultZonesForUser(List.of("requested"), "Asset", "test", USER);

        assertNotNull(zones);
        assertEquals(zones.size(), 2);
        assertTrue(zones.containsAll(List.of("quarantine", "requested")));

        assertEquals(this.getConnector(null).getDefaultZonesForUser(List.of("requested"), "Asset", "test", USER), List.of("requested"));
    }


    /**
     * An empty list is a definition: the element is to have no zones.
     */
    @Test
    public void testEmptyListMeansNoZones() throws Exception
    {
        Map<String, Object> otherProperties = new HashMap<>();

        otherProperties.put("publishZones", new ArrayList<String>());
        otherProperties.put("defaultZones", new ArrayList<String>());

        OpenMetadataAccessSecurityConnector connector = this.getConnector(otherProperties);

        assertEquals(connector.getPublishZonesForUser(CURRENT_ZONES, "Asset", "test", USER), List.of());
        assertEquals(connector.getWithdrawZonesForUser(CURRENT_ZONES, "Asset", "test", USER), List.of());
    }


    /**
     * A property with no value - which is how YAML reads "publishZones:" - is a definition too.
     */
    @Test
    public void testNullValueMeansNoZones() throws Exception
    {
        Map<String, Object> otherProperties = new HashMap<>();

        otherProperties.put("publishZones", null);
        otherProperties.put("defaultZones", null);

        OpenMetadataAccessSecurityConnector connector = this.getConnector(otherProperties);

        assertEquals(connector.getPublishZonesForUser(CURRENT_ZONES, "Asset", "test", USER), List.of());
        assertEquals(connector.getWithdrawZonesForUser(CURRENT_ZONES, "Asset", "test", USER), List.of());
    }


    /**
     * Blank and repeated zone names are left out, and the order of the rest is kept.
     */
    @Test
    public void testZoneListIsTidied() throws Exception
    {
        Map<String, Object> otherProperties = new HashMap<>();

        otherProperties.put("publishZones", Arrays.asList("trusted", "", null, "data-lake", "trusted"));

        assertEquals(this.getConnector(otherProperties).getPublishZonesForUser(null, "Asset", "test", USER),
                     List.of("trusted", "data-lake"));
    }


    /**
     * An account with other properties, but not publishZones, does not say what publishing means.
     */
    @Test
    public void testPublishIsRefusedWhenPublishZonesAreNotDefined() throws Exception
    {
        Map<String, Object> otherProperties = new HashMap<>();

        otherProperties.put("defaultZones", List.of("quarantine"));

        OpenMetadataAccessSecurityConnector connector = this.getConnector(otherProperties);

        UserNotAuthorizedException error = expectThrows(UserNotAuthorizedException.class,
                                                        () -> connector.getPublishZonesForUser(CURRENT_ZONES, "Asset", "test", USER));

        assertEquals(error.getReportedErrorMessageId(), OpenMetadataSecurityErrorCode.UNDEFINED_ZONES_FOR_USER.getMessageDefinition().getMessageId());
        assertEquals(error.getUserId(), USER);
        assertTrue(error.getReportedErrorMessage().contains("publishZones"), error.getReportedErrorMessage());
        assertTrue(error.getReportedErrorMessage().contains("publish an element of type Asset"), error.getReportedErrorMessage());
    }


    /**
     * An account with other properties, but not defaultZones, does not say what withdrawing means.
     */
    @Test
    public void testWithdrawIsRefusedWhenDefaultZonesAreNotDefined() throws Exception
    {
        Map<String, Object> otherProperties = new HashMap<>();

        otherProperties.put("publishZones", List.of("trusted"));

        OpenMetadataAccessSecurityConnector connector = this.getConnector(otherProperties);

        UserNotAuthorizedException error = expectThrows(UserNotAuthorizedException.class,
                                                        () -> connector.getWithdrawZonesForUser(CURRENT_ZONES, "Asset", "test", USER));

        assertEquals(error.getReportedErrorMessageId(), OpenMetadataSecurityErrorCode.UNDEFINED_ZONES_FOR_USER.getMessageDefinition().getMessageId());
        assertTrue(error.getReportedErrorMessage().contains("defaultZones"), error.getReportedErrorMessage());
        assertTrue(error.getReportedErrorMessage().contains("withdraw an element of type Asset"), error.getReportedErrorMessage());
    }


    /**
     * An account with no other properties at all is refused both requests.
     */
    @Test
    public void testBothAreRefusedWhenTheAccountHasNoOtherProperties() throws Exception
    {
        OpenMetadataAccessSecurityConnector connector = this.getConnector(null);

        expectThrows(UserNotAuthorizedException.class, () -> connector.getPublishZonesForUser(CURRENT_ZONES, "Asset", "test", USER));
        expectThrows(UserNotAuthorizedException.class, () -> connector.getWithdrawZonesForUser(CURRENT_ZONES, "Asset", "test", USER));
    }


    /**
     * A user with no account is refused as an unknown user, as for every other request.
     */
    @Test
    public void testUnknownUserIsRefused() throws Exception
    {
        OpenMetadataAccessSecurityConnector connector = this.getConnector(null);

        UserNotAuthorizedException error = expectThrows(UserNotAuthorizedException.class,
                                                        () -> connector.getPublishZonesForUser(CURRENT_ZONES, "Asset", "test", "nobody"));

        assertEquals(error.getReportedErrorMessageId(), OpenMetadataSecurityErrorCode.UNKNOWN_USER.getMessageDefinition().getMessageId());
    }


    /**
     * A connector with an employee account for USER that has the supplied other properties.
     */
    private OpenMetadataAccessSecurityConnector getConnector(Map<String, Object> otherProperties)
    {
        OpenMetadataUserAccount userAccount = new OpenMetadataUserAccount();

        userAccount.setUserId(USER);
        userAccount.setUserAccountType(UserAccountType.EMPLOYEE);
        userAccount.setUserAccountStatus(UserAccountStatus.AVAILABLE);
        userAccount.setOtherProperties(otherProperties);

        OpenMetadataAccessSecurityConnector connector = new OpenMetadataAccessSecurityConnector()
        {
            @Override
            public OpenMetadataUserAccount getUserAccount(String userId)
            {
                return USER.equals(userId) ? userAccount : null;
            }
        };

        connector.setAuditLog(mock(AuditLog.class));

        return connector;
    }
}
