/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server.admin;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.governanceengine.server.GovernanceEngineRESTServices;
import org.odpi.openmetadata.accessservices.governanceengine.server.listeners.GovernanceEngineOMRSTopicListener;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.events.v1.OMRSEventV1;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/*
 * Mockito requires static imports
 */


/*
 * Test suite metadata & configuration
 */
@SuiteDisplayName("Governance Engine Server Admin")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)



public class GovernanceEngineAdminTest {

    @Mock
    private Logger log; // No external logging - do nothing
    @Mock
    private OMRSAuditLog auditLog; // mock the service, but
    @Mock
    private GovernanceEngineRESTServices govrestsvc; // The implementation calls a static method
    @Mock
    private GovernanceEngineOMRSTopicListener getopiclistener;
    @Mock
    private AccessServiceConfig ascfg;
    @Mock
    private OMRSTopicConnector topcon;
    @Mock
    private OMRSRepositoryConnector repcon;

    @InjectMocks
    private GovernanceEngineAdmin govadmin = new GovernanceEngineAdmin(); // Class under test

    @Captor
    private ArgumentCaptor<String> auditString;

    private static final String serverUserName = "User1";

    @Test
    @DisplayName("GovernanceEngineAdmin - check initialization")
    void testInitializeOk() {

// For this test we'll use somewhat dummy values



        try {
            govadmin.initialize(ascfg, topcon, repcon, auditLog, serverUserName);
        } catch (Exception omage) {}

        // Check we recorded an audit log enty
        // Note anyString won't match nulls, so if that parm is allowed Use Mockito.<String>any() or similar
        verify(auditLog,times(3)).logRecord(anyString(),auditString.capture(),
                any(OMRSAuditLogRecordSeverity.class), anyString(), Mockito.<String>any(),
        anyString(), anyString());

        // Check we get an initializing, omrs, initialized
        // May be a little too strict if/when we add extra audit log entries
        assertTrue(auditString.getAllValues().get(0).contains("OMAS-GOVERNANCE-ENGINE-0001"));
        assertTrue(auditString.getAllValues().get(1).contains("OMAS-GOVERNANCE-ENGINE-0002"));
        assertTrue(auditString.getAllValues().get(2).contains("OMAS-GOVERNANCE-ENGINE-0003"));

    }

    @Test
    @DisplayName("GovernanceEngineAdmin - check shutdown")
    void testShutdown() {
    }
}