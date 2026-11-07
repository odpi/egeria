/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.accessconnector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.properties.users.AccessOperation;
import org.odpi.openmetadata.frameworks.connectors.properties.users.SecurityAccessControl;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountType;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * The zones whose elements a user could read are worked out in advance so that a search can leave out the
 * elements in other zones.  The list must include every zone through which the user could read some element -
 * including zones open only to owners or maintainers - and no zone without a control.
 */
public class ReadableZonesTest
{
    private static final String USER = "erinoverview";


    @Test
    public void testReadableZones() throws Exception
    {
        Map<String, SecurityAccessControl> controls = new HashMap<>();

        controls.put("open-zone",       getControl(AccessOperation.READ, "allUsers"));
        controls.put("employee-zone",   getControl(AccessOperation.READ, "employeeUsers"));
        controls.put("contractor-zone", getControl(AccessOperation.READ, "contractUsers"));
        controls.put("group-zone",      getControl(AccessOperation.READ, "governance-team"));
        controls.put("role-zone",       getControl(AccessOperation.READ, "data-steward"));
        controls.put("owner-zone",      getControl(AccessOperation.READ, "instanceOwner"));
        controls.put("default-zone",    getControl(AccessOperation.DEFAULT, "allUsers"));
        controls.put("update-only",     getControl(AccessOperation.UPDATE_PROPERTIES, "allUsers"));
        controls.put("other-group",     getControl(AccessOperation.READ, "someone-else"));

        OpenMetadataAccessSecurityConnector connector = this.getConnector(controls, true);

        Set<String> zones = new HashSet<>(connector.getReadableZonesForUser(USER, "test", "test"));

        assertEquals(zones, Set.of(USER, "open-zone", "employee-zone", "group-zone", "role-zone", "owner-zone", "default-zone"));
    }


    @Test
    public void testNoListWhenTurnedOff() throws Exception
    {
        assertNull(this.getConnector(new HashMap<>(), false).getReadableZonesForUser(USER, "test", "test"));
    }


    /**
     * The zone filter is on unless the connection's configuration properties turn it off.
     */
    @Test
    public void testFilterOnByDefault()
    {
        OpenMetadataAccessSecurityConnector connector = new OpenMetadataAccessSecurityConnector();

        assertTrue(connector.getPushDownZoneFilter(null));
        assertTrue(connector.getPushDownZoneFilter(new HashMap<>()));
        assertTrue(connector.getPushDownZoneFilter(Map.of("pushDownZoneFilter", true)));
        assertFalse(connector.getPushDownZoneFilter(Map.of("pushDownZoneFilter", false)));
        assertFalse(connector.getPushDownZoneFilter(Map.of("pushDownZoneFilter", "false")));
    }


    @Test
    public void testNoListWhenSecretsStoreCannotListControls() throws Exception
    {
        assertNull(this.getConnector(null, true).getReadableZonesForUser(USER, "test", "test"));
    }


    private SecurityAccessControl getControl(AccessOperation operation, String groupName)
    {
        SecurityAccessControl control = new SecurityAccessControl();

        control.setAssociatedSecurityList(Map.of(operation.name(), List.of(groupName)));

        return control;
    }


    /**
     * A connector with one secrets store holding the supplied controls (null for a store that cannot list them)
     * and an employee account for USER.
     */
    private OpenMetadataAccessSecurityConnector getConnector(Map<String, SecurityAccessControl> controls,
                                                             boolean                            pushDown) throws Exception
    {
        OpenMetadataUserAccount userAccount = new OpenMetadataUserAccount();

        userAccount.setUserId(USER);
        userAccount.setUserAccountType(UserAccountType.EMPLOYEE);
        userAccount.setUserAccountStatus(UserAccountStatus.AVAILABLE);
        userAccount.setSecurityGroups(List.of("governance-team"));
        userAccount.setSecurityRoles(List.of("data-steward"));

        SecretsStoreConnector secretsStore = new SecretsStoreConnector()
        {
            @Override
            protected void refreshSecrets()
            {
            }

            @Override
            protected void saveSecrets()
            {
            }

            @Override
            public String getSecret(String secretName)
            {
                return null;
            }

            @Override
            public long getRefreshTimeInterval()
            {
                return 0;
            }

            @Override
            public Map<String, SecurityAccessControl> getSecurityAccessControls()
            {
                return controls;
            }

            @Override
            public SecurityAccessControl getSecurityAccessControl(String controlName)
            {
                return (controls == null) ? null : controls.get(controlName);
            }
        };

        OpenMetadataAccessSecurityConnector connector = new OpenMetadataAccessSecurityConnector()
        {
            {
                secretsStoreConnectorMap = new HashMap<>(Map.of("test-store", secretsStore));
            }

            @Override
            public OpenMetadataUserAccount getUserAccount(String userId)
            {
                return USER.equals(userId) ? userAccount : null;
            }
        };

        connector.setAuditLog(mock(AuditLog.class));

        Field pushDownField = OpenMetadataAccessSecurityConnector.class.getDeclaredField("pushDownZoneFilter");

        pushDownField.setAccessible(true);
        pushDownField.set(connector, pushDown);

        return connector;
    }
}
