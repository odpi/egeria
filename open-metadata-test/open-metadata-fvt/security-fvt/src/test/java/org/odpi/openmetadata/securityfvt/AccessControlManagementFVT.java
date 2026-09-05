/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securityfvt;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataSecurityAccessControl;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.ALL_USERS_GROUP;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.DIGITAL_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.DISABLED_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.EMPLOYEE_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.INVESTIGATOR_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.OPEN_ZONE;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.OPERATOR_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.SERVER_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.STEWARDS_GROUP;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.UNAUTHORIZED_PLATFORM_ACCESS;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.assertRefused;

/**
 * AccessControlManagementFVT covers the management half of {@code OpenMetadataUserSecurity} as it is
 * reached through the platform: reading and writing access controls, and reading user accounts.  auth-fvt
 * covers creating and deleting accounts, so that is not repeated here.
 * <br><br>
 * These calls are guarded by the platform's operator check, and they are the only way to change the
 * security connector's decisions without editing the user directory by hand.  The round-trip test writes
 * a control, reads it back through the platform and reads it again from the file, because the platform
 * verifier returns null for a control it could not read - so "get returns what set stored" is the
 * assertion that shows the write reached the connector at all.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AccessControlManagementFVT
{
    /**
     * An operator can create an access control, read it back, and delete it, and the control is persisted
     * to the user directory in between.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anOperatorCanCreateReadAndDeleteAnAccessControl() throws Exception
    {
        final String controlName = "secfvt-runtime-zone";

        PlatformServicesClient client = SecurityFvtTestSupport.platformClientAs(OPERATOR_USER_ID);

        assertNull(client.getSecurityAccessControl(controlName),
                   "A control that has not been created should not be returned");

        OpenMetadataSecurityAccessControl control = new OpenMetadataSecurityAccessControl();

        control.setControlName(controlName);
        control.setControlDisplayName("Runtime Zone");
        control.setControlTypeName(OpenMetadataType.GOVERNANCE_ZONE.typeName);
        control.setDescription("Created by security-fvt at runtime.");
        control.setAssociatedSecurityList(Map.of("DEFAULT", List.of(STEWARDS_GROUP)));

        client.setSecurityAccessControl(control);

        try
        {
            OpenMetadataSecurityAccessControl stored = client.getSecurityAccessControl(controlName);

            assertNotNull(stored, "A control that was just set should be returned");
            assertEquals(controlName, stored.getControlName(), "The control returned should be the one that was set");
            assertEquals(OpenMetadataType.GOVERNANCE_ZONE.typeName, stored.getControlTypeName(),
                         "The control type should have been stored");
            assertNotNull(stored.getAssociatedSecurityList(), "The security lists should have been stored");
            assertEquals(List.of(STEWARDS_GROUP), stored.getAssociatedSecurityList().get("DEFAULT"),
                         "The DEFAULT list should have been stored as set");

            JsonNode persisted = SecurityFvtTestSupport.readStoredAccessControl(controlName);

            assertNotNull(persisted, "The control should have been written to the user directory file");
            assertEquals(STEWARDS_GROUP, persisted.path("associatedSecurityList").path("DEFAULT").path(0).asText(),
                         "The file should hold the DEFAULT list that was set.  Entry: " + persisted);
        }
        finally
        {
            client.deleteSecurityAccessControl(controlName);
        }

        assertNull(client.getSecurityAccessControl(controlName),
                   "A control that has been deleted should no longer be returned");
        assertNull(SecurityFvtTestSupport.readStoredAccessControl(controlName),
                   "A control that has been deleted should no longer be in the user directory file");
    }


    /**
     * A control that was defined in the user directory is returned with each of its operation lists.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anExistingControlIsReturnedWithItsOperationLists() throws Exception
    {
        PlatformServicesClient client = SecurityFvtTestSupport.platformClientAs(OPERATOR_USER_ID);

        OpenMetadataSecurityAccessControl control = client.getSecurityAccessControl(OPEN_ZONE);

        assertNotNull(control, "The open zone's control should be returned");
        assertEquals(OpenMetadataType.GOVERNANCE_ZONE.typeName, control.getControlTypeName(),
                     "The open zone should be a governance zone control");
        assertNotNull(control.getAssociatedSecurityList(), "The open zone should have security lists");
        assertEquals(List.of(ALL_USERS_GROUP), control.getAssociatedSecurityList().get("READ"),
                     "The open zone's READ list should be as defined");
        assertEquals(List.of(STEWARDS_GROUP), control.getAssociatedSecurityList().get("DEFAULT"),
                     "The open zone's DEFAULT list should be as defined");
    }


    /**
     * Managing access controls is an operator's job, so the investigator is refused even a read.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anInvestigatorCannotReadAccessControls() throws Exception
    {
        PlatformServicesClient client = SecurityFvtTestSupport.platformClientAs(INVESTIGATOR_USER_ID);

        assertRefused(UNAUTHORIZED_PLATFORM_ACCESS,
                      INVESTIGATOR_USER_ID,
                      () -> client.getSecurityAccessControl(OPEN_ZONE),
                      "Reading an access control as the investigator");
    }


    /**
     * The user list can be filtered by account status and type, which is what the dynamic groups are
     * built from.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void theUserListIsFilteredByStatusAndType() throws Exception
    {
        PlatformServicesClient client = SecurityFvtTestSupport.platformClientAs(OPERATOR_USER_ID);

        List<String> digitalUsers = client.getUserList(UserAccountStatus.AVAILABLE, UserAccountType.DIGITAL);

        assertNotNull(digitalUsers, "The list of available digital accounts should be returned");
        assertTrue(digitalUsers.contains(DIGITAL_USER_ID), "The digital account should be listed as such.  List: " + digitalUsers);
        assertTrue(digitalUsers.contains(SERVER_USER_ID), "The server's account should be listed as digital.  List: " + digitalUsers);
        assertFalse(digitalUsers.contains(EMPLOYEE_USER_ID), "The employee should not be listed as digital.  List: " + digitalUsers);

        List<String> disabledUsers = client.getUserList(UserAccountStatus.DISABLED, null);

        assertNotNull(disabledUsers, "The list of disabled accounts should be returned");
        assertEquals(List.of(DISABLED_USER_ID), disabledUsers, "Only the disabled account should be listed as disabled");
    }


    /**
     * A user account read through the platform carries its type and status but not its secrets.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aUserAccountIsReturnedWithoutItsSecrets() throws Exception
    {
        PlatformServicesClient client = SecurityFvtTestSupport.platformClientAs(OPERATOR_USER_ID);

        OpenMetadataUserAccount account = client.getUserAccount(EMPLOYEE_USER_ID);

        assertNotNull(account, "The employee's account should be returned");
        assertEquals(EMPLOYEE_USER_ID, account.getUserId(), "The account returned should be the employee's");
        assertEquals(UserAccountType.EMPLOYEE, account.getUserAccountType(), "The account type should be returned");
        assertEquals(UserAccountStatus.AVAILABLE, account.getUserAccountStatus(), "The account status should be returned");
        assertNull(account.getSecrets(), "An account read through the platform should not carry its secrets");
    }
}
