/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securityfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.ADMIN_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.CONTRACTOR_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.DIGITAL_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.DISABLED_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.EMPLOYEE_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.EXTERNAL_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.UNKNOWN_USER_ID;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.UNAUTHORIZED_SERVER_ACCESS;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.UNAUTHORIZED_SERVICE_ACCESS;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.UNKNOWN_USER;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.assertRefused;

/**
 * ServerAndServiceSecurityFVT covers the two checks every request to an OMAG Server passes through before
 * any service code runs: {@code OpenMetadataServerSecurity.validateUserForServer} and
 * {@code OpenMetadataServiceSecurity.validateUserForService}.  Both are made by the multi-tenant services
 * when a request is routed to a server instance, so a user refused here is refused everything on the
 * server, whatever the request was.
 * <br><br>
 * The Open Metadata Access Security Connector answers them from two access controls: one named after the
 * <em>server</em> and one named after the <em>service</em>.  The user directory admits employees,
 * contractors and digital accounts to the server but only employees and digital accounts to the store
 * service, so that a contractor demonstrates the two checks are separate: through the first door, stopped
 * at the second.
 * <br><br>
 * The connector also insists on an active account before it makes any decision at all, and that is what
 * the last two tests cover.  They are the only tests in the suite that act as a user other than the one
 * whose token they carry: a disabled account and an unknown identity cannot obtain a token, so they reach
 * the server the way any userId does - as the path parameter of the store's REST API, which the
 * administrator's client is used to send.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ServerAndServiceSecurityFVT
{
    /**
     * Make a request that would be refused at the server or service check if the user were not admitted.
     * A search is used because it is the least demanding call on the store: it needs no element to exist,
     * and its result - possibly empty, possibly null - is not what is being tested.
     *
     * @param client client acting as the user
     * @param userId user to act as
     * @throws Exception the request was refused, or failed
     */
    private void makeARequest(EgeriaOpenMetadataStoreClient client,
                              String                        userId) throws Exception
    {
        SearchOptions searchOptions = new SearchOptions();
        searchOptions.setPageSize(SecurityFvtTestSupport.MAX_PAGE_SIZE);

        client.findMetadataElementsWithString(userId, "SecurityFVT", searchOptions);
    }


    /**
     * An employee is on both controls and gets in.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anEmployeeIsAdmittedToTheServerAndTheService() throws Exception
    {
        EgeriaOpenMetadataStoreClient client = SecurityFvtTestSupport.storeClientAs(EMPLOYEE_USER_ID);

        assertDoesNotThrow(() -> makeARequest(client, EMPLOYEE_USER_ID),
                           "An employee should be admitted to the server and the store service");
    }


    /**
     * A digital account is on both controls too, through the digitalUsers dynamic group.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aDigitalAccountIsAdmittedToTheServerAndTheService() throws Exception
    {
        EgeriaOpenMetadataStoreClient client = SecurityFvtTestSupport.storeClientAs(DIGITAL_USER_ID);

        assertDoesNotThrow(() -> makeARequest(client, DIGITAL_USER_ID),
                           "A digital account should be admitted to the server and the store service");
    }


    /**
     * An external user has a valid account and a valid token, and is refused at the server: the server's
     * control lists employees, contractors and digital accounts, and no more.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anExternalUserIsRefusedAtTheServer() throws Exception
    {
        EgeriaOpenMetadataStoreClient client = SecurityFvtTestSupport.storeClientAs(EXTERNAL_USER_ID);

        assertRefused(UNAUTHORIZED_SERVER_ACCESS,
                      EXTERNAL_USER_ID,
                      () -> makeARequest(client, EXTERNAL_USER_ID),
                      "A request to the store as an external user");
    }


    /**
     * A contractor is admitted to the server and refused the service.
     * <br><br>
     * The message identifier is what shows the contractor got past the first check: a refusal at the
     * server would be 403-002, as it is for the external user above.  The message itself does not say
     * which service refused the user - the service-access message names only the operation, and for a
     * whole-service refusal that is "any" - so the identifier is the only thing to assert on.  The
     * service is named in the audit log record the connector writes, not in the exception.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aContractorIsAdmittedToTheServerButRefusedTheService() throws Exception
    {
        EgeriaOpenMetadataStoreClient client = SecurityFvtTestSupport.storeClientAs(CONTRACTOR_USER_ID);

        assertRefused(UNAUTHORIZED_SERVICE_ACCESS,
                      CONTRACTOR_USER_ID,
                      () -> makeARequest(client, CONTRACTOR_USER_ID),
                      "A request to the store service as a contractor");
    }


    /**
     * An account that exists but is disabled is refused as unknown, before the server or service controls
     * are consulted - the connector's "active account" check comes first, and a disabled account is not
     * distinguished from a missing one in the answer it gives.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aDisabledAccountIsRefusedAsUnknown() throws Exception
    {
        EgeriaOpenMetadataStoreClient client = SecurityFvtTestSupport.storeClientAs(ADMIN_USER_ID);

        assertRefused(UNKNOWN_USER,
                      DISABLED_USER_ID,
                      () -> makeARequest(client, DISABLED_USER_ID),
                      "A request to the store as a disabled account");
    }


    /**
     * A userId that is not in the directory at all is refused as unknown.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anIdentityThatIsNotInTheDirectoryIsRefusedAsUnknown() throws Exception
    {
        EgeriaOpenMetadataStoreClient client = SecurityFvtTestSupport.storeClientAs(ADMIN_USER_ID);

        assertRefused(UNKNOWN_USER,
                      UNKNOWN_USER_ID,
                      () -> makeARequest(client, UNKNOWN_USER_ID),
                      "A request to the store as an identity that is not in the directory");
    }
}
