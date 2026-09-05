/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securityfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.ADMIN_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.EMPLOYEE_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.INVESTIGATOR_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.METADATA_STORE_NAME;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.OPERATOR_USER_ID;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.UNAUTHORIZED_PLATFORM_ACCESS;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.UNAUTHORIZED_SERVICE_OPERATION_ACCESS;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.assertRefused;

/**
 * PlatformSecurityFVT covers {@code OpenMetadataPlatformSecurity} and the server-level part of
 * {@code OpenMetadataServerSecurity}: the three platform roles - administrator, operator and investigator -
 * and what each of them may and may not do to the servers on the platform.
 * <br><br>
 * The decisions come from three access controls in the user directory, at the names the connector looks
 * for by default:
 * <ul>
 *     <li>{@code admin-services} - who may create a new server configuration, and who counts as a server
 *     administrator when a server's own security connector is asked;</li>
 *     <li>{@code platform-services} - who may change an existing configuration and start or stop
 *     servers, and who counts as a server operator;</li>
 *     <li>{@code server-operations} - who may look.</li>
 * </ul>
 * The tests pair each permitted action with the same action refused to a user one role down, because a
 * connector that permitted everything would pass every "can" test on its own.
 * <br><br>
 * Two verifiers take part.  The platform's verifier is asked first, on every administration call.  Once a
 * server has a security connection in its configuration document, the server's own verifier is asked as
 * well, for the same role the platform asked for: investigator to read the document, administrator to
 * change it, operator for the read that starts the server.  Its refusals carry the service-operation
 * identifier, 403-006, and name the administration service, the operation and the server, where the
 * platform's carry 403-001; the tests assert the identifier so that they show which verifier refused.
 * <br><br>
 * Every refusal is checked for the user it names, both in the exception's userId and in its message.  The
 * administration API's client used to rebuild a refusal without the userId the server sent - its exception
 * handler passed null where the common client handler reads the value from the response - which the first
 * run of this class found and which has been fixed; the message check is kept alongside as the module's
 * own statement of who was refused.
 * <br><br>
 * Nothing here shuts a server down, even to show that it is refused.  A refusal that did not happen would
 * take the store down for every test that followed, and the test that found the defect would then be lost
 * among the ones that merely suffered from it.  Activation of an already-running server is used instead: it
 * is refused at the same check, and for an authorized user it fails harmlessly for a different reason.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class PlatformSecurityFVT
{
    /**
     * Assert that a refusal received through the administration client names the user in its message.
     *
     * @param error the refusal
     * @param userId user expected to be named
     */
    private static void assertMessageNamesUser(UserNotAuthorizedException error,
                                               String                     userId)
    {
        assertTrue(error.getReportedErrorMessage().contains("User " + userId + " "),
                   "The refusal should name the user refused.  Message: " + error.getReportedErrorMessage());
    }


    /**
     * The administrator holds the admin-services control and so may create a configuration document for
     * a server that does not yet exist.
     * <br><br>
     * This is also the path the extension took to configure this suite's own server, so a failure here
     * would have stopped the run before it reached this test; it is kept so that the permitted case sits
     * next to the refused ones below.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anAdministratorCanCreateANewServerConfiguration() throws Exception
    {
        final String serverName = "secFvtAdminScratchServer";

        OMAGServerConfigurationClient client = SecurityFvtTestSupport.configurationClientAs(ADMIN_USER_ID, serverName);

        try
        {
            client.setServerUserId(ADMIN_USER_ID);

            assertEquals(serverName, client.getOMAGServerConfig().getLocalServerName(),
                         "The administrator's new configuration document should be stored and readable");
        }
        finally
        {
            client.clearOMAGServerConfig();
        }
    }


    /**
     * An operator may start and stop servers but is not on the admin-services control, so may not create
     * a server.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anOperatorCannotCreateANewServerConfiguration() throws Exception
    {
        OMAGServerConfigurationClient client = SecurityFvtTestSupport.configurationClientAs(OPERATOR_USER_ID, "secFvtOperatorScratchServer");

        UserNotAuthorizedException error = assertRefused(UNAUTHORIZED_PLATFORM_ACCESS,
                                                         OPERATOR_USER_ID,
                                                         () -> client.setServerUserId(OPERATOR_USER_ID),
                                                         "Creating a server configuration as the operator");

        assertMessageNamesUser(error, OPERATOR_USER_ID);
    }


    /**
     * An investigator may not create a server either, and the refusal should name the platform so that
     * the user knows which platform's administrator to ask.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anInvestigatorCannotCreateANewServerConfiguration() throws Exception
    {
        OMAGServerConfigurationClient client = SecurityFvtTestSupport.configurationClientAs(INVESTIGATOR_USER_ID, "secFvtInvestigatorScratchServer");

        UserNotAuthorizedException error = assertRefused(UNAUTHORIZED_PLATFORM_ACCESS,
                                                         INVESTIGATOR_USER_ID,
                                                         () -> client.setServerUserId(INVESTIGATOR_USER_ID),
                                                         "Creating a server configuration as the investigator");

        assertMessageNamesUser(error, INVESTIGATOR_USER_ID);
        assertTrue(error.getReportedErrorMessage().contains(SecurityFvtTestSupport.PLATFORM_NAME),
                   "The refusal should name the platform.  Message: " + error.getReportedErrorMessage());
    }


    /**
     * An investigator may list the active servers, and may read the configuration document of a server
     * that has no security connector of its own.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anInvestigatorCanLookAtThePlatformAndAServerWithNoConnectorOfItsOwn() throws Exception
    {
        PlatformServicesClient platformClient = SecurityFvtTestSupport.platformClientAs(INVESTIGATOR_USER_ID);

        assertTrue(platformClient.getActiveServers().contains(METADATA_STORE_NAME),
                   "The investigator should be able to list the active servers");

        final String serverName = "secFvtPlainScratchServer";

        OMAGServerConfigurationClient adminClient = SecurityFvtTestSupport.configurationClientAs(ADMIN_USER_ID, serverName);

        try
        {
            adminClient.setServerUserId(ADMIN_USER_ID);

            OMAGServerConfigurationClient investigatorClient = SecurityFvtTestSupport.configurationClientAs(INVESTIGATOR_USER_ID, serverName);

            assertEquals(serverName, investigatorClient.getOMAGServerConfig().getLocalServerName(),
                         "The investigator should be able to read the configuration of a server with no security connector");
        }
        finally
        {
            adminClient.clearOMAGServerConfig();
        }
    }


    /**
     * Once a server has its own security connector, reading its configuration document is also put to
     * that connector - and as the same investigator check the platform made, so the investigator who
     * passed the platform's check passes the store's too.  The operator, who holds the investigator role
     * as well, can read it likewise.
     * <br><br>
     * The first run of this suite found the store's connector being asked for an <em>operator</em> on this
     * read, which refused the investigator a document the platform had just said they could see.  The
     * admin store now asks both verifiers for the same role.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void readingAServersConfigurationIsPutToTheServersOwnConnectorAsAnInvestigator() throws Exception
    {
        OMAGServerConfigurationClient investigatorClient = SecurityFvtTestSupport.configurationClientAs(INVESTIGATOR_USER_ID, METADATA_STORE_NAME);

        assertEquals(METADATA_STORE_NAME, investigatorClient.getOMAGServerConfig().getLocalServerName(),
                     "The investigator should be able to read the configuration of a server with its own security connector");

        OMAGServerConfigurationClient operatorClient = SecurityFvtTestSupport.configurationClientAs(OPERATOR_USER_ID, METADATA_STORE_NAME);

        assertNotNull(operatorClient.getOMAGServerConfig(),
                      "The operator should be able to read the store's configuration");
    }


    /**
     * An investigator may not change an existing configuration document, and may not start a server.
     * Both are governed by the platform-services control, which the investigator is not on.
     * <br><br>
     * The two refusals come from different verifiers.  A change to the document is refused by the
     * platform's verifier, before anything is loaded.  An activation first loads the document for start-up,
     * and that load puts the read to the server's own connector as a server operator check - starting a
     * server is an operator's job whichever verifier is asked - so it is the store's connector that refuses
     * the investigator, with the service-access identifier, and the platform's operator check is never
     * reached.  Either way the investigator does not start the server.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anInvestigatorCannotChangeOrOperateAServer() throws Exception
    {
        OMAGServerConfigurationClient configurationClient = SecurityFvtTestSupport.configurationClientAs(INVESTIGATOR_USER_ID, METADATA_STORE_NAME);

        /*
         * The same value the server already has, so that nothing changes if the check is broken.
         */
        UserNotAuthorizedException error = assertRefused(UNAUTHORIZED_PLATFORM_ACCESS,
                                                         INVESTIGATOR_USER_ID,
                                                         () -> configurationClient.setMaxPageSize(SecurityFvtTestSupport.MAX_PAGE_SIZE),
                                                         "Changing a server's configuration as the investigator");

        assertMessageNamesUser(error, INVESTIGATOR_USER_ID);

        PlatformServicesClient platformClient = SecurityFvtTestSupport.platformClientAs(INVESTIGATOR_USER_ID);

        error = assertRefused(UNAUTHORIZED_SERVICE_OPERATION_ACCESS,
                              INVESTIGATOR_USER_ID,
                              () -> platformClient.activateWithStoredConfig(METADATA_STORE_NAME),
                              "Activating a server as the investigator");

        assertTrue(error.getReportedErrorMessage().contains("operations"),
                   "The refusal should be the server's operator check.  Message: " + error.getReportedErrorMessage());
        assertTrue(error.getReportedErrorMessage().contains(METADATA_STORE_NAME),
                   "The refusal should name the server.  Message: " + error.getReportedErrorMessage());
    }


    /**
     * The operator passes the platform's operator check but is then refused by the <em>server's</em>
     * security connector, which asks whether the user is a server administrator - and the operator is not
     * on the admin-services control.
     * <br><br>
     * This is the clearest case of the two verifiers being asked in turn for a single request: the
     * platform verifier says the operator may change configurations in general, and the server verifier -
     * the connector named in the document being changed - says this user is not an administrator of this
     * server.  The message identifier distinguishes the two.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anOperatorIsRefusedByTheServersOwnConnectorWhenChangingItsConfiguration() throws Exception
    {
        OMAGServerConfigurationClient configurationClient = SecurityFvtTestSupport.configurationClientAs(OPERATOR_USER_ID, METADATA_STORE_NAME);

        UserNotAuthorizedException error = assertRefused(UNAUTHORIZED_SERVICE_OPERATION_ACCESS,
                                                         OPERATOR_USER_ID,
                                                         () -> configurationClient.setMaxPageSize(SecurityFvtTestSupport.MAX_PAGE_SIZE),
                                                         "Changing the store's configuration as the operator");

        assertMessageNamesUser(error, OPERATOR_USER_ID);
        assertTrue(error.getReportedErrorMessage().contains("configuration"),
                   "The refusal should say it was the configuration request that was refused.  Message: " + error.getReportedErrorMessage());
        assertTrue(error.getReportedErrorMessage().contains(METADATA_STORE_NAME),
                   "The refusal should name the server.  Message: " + error.getReportedErrorMessage());
    }


    /**
     * The operator may look at the platform and at a running server's active configuration.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anOperatorCanLookAtARunningServer() throws Exception
    {
        PlatformServicesClient platformClient = SecurityFvtTestSupport.platformClientAs(OPERATOR_USER_ID);

        assertTrue(platformClient.getActiveServers().contains(METADATA_STORE_NAME),
                   "The operator should be able to list the active servers");
        assertNotNull(platformClient.getActiveConfiguration(METADATA_STORE_NAME),
                      "The operator should be able to read a running server's active configuration");
    }


    /**
     * A valid account with none of the platform roles is refused even the investigator's view.
     * <br><br>
     * The employee can log on, and is admitted to the metadata store by the tests in
     * {@link ServerAndServiceSecurityFVT} - so this shows that platform access is a separate decision
     * from having an account, and from being allowed into a server.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anAccountWithNoPlatformRoleIsRefusedTheInvestigatorsView() throws Exception
    {
        PlatformServicesClient platformClient = SecurityFvtTestSupport.platformClientAs(EMPLOYEE_USER_ID);

        assertRefused(UNAUTHORIZED_PLATFORM_ACCESS,
                      EMPLOYEE_USER_ID,
                      platformClient::getActiveServers,
                      "Listing the active servers as an account with no platform role");
    }
}
