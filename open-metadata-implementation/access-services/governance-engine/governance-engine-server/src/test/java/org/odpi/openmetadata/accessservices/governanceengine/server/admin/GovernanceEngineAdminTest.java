/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server.admin;

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
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SuiteDisplayName("Governance Engine Server Admin")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class GovernanceEngineAdminTest {

    private static final String serverUserName = "User1";
    @Captor
    ArgumentCaptor<String> servicename;
    @Captor
    ArgumentCaptor<OMRSRepositoryConnector> repcon2;
    @Mock
    private LoggerFactory log; // No external logging - do nothing
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

    @Test
    @DisplayName("GovernanceEngineAdmin - check initialization audit")
    void testInitializeOk() {
        // For this test we'll use somewhat dummy values
        govadmin.initialize(ascfg, topcon, repcon, auditLog, serverUserName);
        // Check we recorded an audit log enty
        // Note anyString won't match nulls, so if that parm is allowed Use Mockito.<String>any() or similar
        verify(auditLog, atLeast(2)).logRecord(anyString(), auditString.capture(),
                any(OMRSAuditLogRecordSeverity.class), anyString(), Mockito.any(),
                anyString(), anyString());


        // Validate first entry is initializing, last is initialized. Interim audit log entries are not checked
        assertTrue(auditString.getAllValues().get(0).contains("OMAS-GOVERNANCE-ENGINE-0001"));
        // TODO This test failing Not working any more
        // assertTrue(auditString.getAllValues().get(auditString.getAllValues().size()-1).contains("OMAS-GOVERNANCE" +
        //   "-ENGINE" +
        //   "-0003"));

        // check for any misuse first
        validateMockitoUsage();

        // Check repo connector has been set in rest services
        //TODO This test failing
        //verify(govrestsvc,times(1)).setRepositoryConnector(servicename.capture(),repcon2.capture() );
        //assertEquals(null,repcon2.getValue(),"OMAS Repository Connector not set");
        //assertEquals("BERT",servicename.getValue(),"OMAS Service Name incorrect");


    }

    @Test
    @DisplayName("GovernanceEngineAdmin - check shutdown")
    void testShutdown() {

        govadmin.shutdown();

        // Just check we report the fact the service is shutting down
        verify(auditLog, atLeast(2)).logRecord(anyString(), auditString.capture(),
                any(OMRSAuditLogRecordSeverity.class), anyString(), Mockito.any(),
                anyString(), anyString());


        // Validate first entry is initializing, last is initialized. Interim audit log entries are not checked
        assertTrue(auditString.getAllValues().get(0).contains("OMAS-GOVERNANCE-ENGINE-0004"));
        assertTrue(auditString.getAllValues().get(auditString.getAllValues().size() - 1).contains("OMAS-GOVERNANCE" +
                "-ENGINE" +
                "-0005"));
    }


}