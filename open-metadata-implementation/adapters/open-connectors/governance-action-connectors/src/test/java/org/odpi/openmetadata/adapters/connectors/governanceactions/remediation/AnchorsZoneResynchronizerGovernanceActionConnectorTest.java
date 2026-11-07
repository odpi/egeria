/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.mockito.ArgumentCaptor;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

/**
 * AnchorsZoneResynchronizerGovernanceActionConnectorTest runs the Anchors Zone Resynchronizer over a mocked
 * governance context and checks what it asks the metadata store to check, and the guards it produces.
 */
public class AnchorsZoneResynchronizerGovernanceActionConnectorTest
{
    private GovernanceActionContext governanceContext;
    private OpenMetadataStore       openMetadataStore;


    /**
     * A mocked governance context with no action targets.
     *
     * @throws Exception not expected
     */
    @BeforeMethod public void setUpContext() throws Exception
    {
        governanceContext = mock(GovernanceActionContext.class);
        openMetadataStore = mock(OpenMetadataStore.class);

        when(governanceContext.getOpenMetadataStore()).thenReturn(openMetadataStore);
        when(openMetadataStore.getMetadataSourceOptions()).thenReturn(new MetadataSourceOptions());
    }


    /**
     * Without action targets the whole repository is checked, including lineage and duplicate elements.
     *
     * @throws Exception not expected
     */
    @Test public void testWholeRepositoryWhenNoTargets() throws Exception
    {
        when(openMetadataStore.resynchronizeAnchorsZoneMembership(isNull(), any())).thenReturn(3L);

        this.runService();

        ArgumentCaptor<MetadataSourceOptions> options = ArgumentCaptor.forClass(MetadataSourceOptions.class);

        verify(openMetadataStore).resynchronizeAnchorsZoneMembership(isNull(), options.capture());
        assertTrue(options.getValue().getForLineage(), "Elements with a Memento classification should be checked");
        assertTrue(options.getValue().getForDuplicateProcessing(), "Duplicates should be checked one by one");

        this.verifyCompletion(AnchorsZoneResynchronizerGuard.ZONES_CORRECTED);
    }


    /**
     * Each action target is checked, and the corrections are added up.  None means the zones were up to date.
     *
     * @throws Exception not expected
     */
    @Test public void testEachTargetChecked() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("guid-1"), this.actionTarget("guid-2")));
        when(openMetadataStore.resynchronizeAnchorsZoneMembership(anyString(), any())).thenReturn(0L);

        this.runService();

        verify(openMetadataStore).resynchronizeAnchorsZoneMembership(eq("guid-1"), any());
        verify(openMetadataStore).resynchronizeAnchorsZoneMembership(eq("guid-2"), any());
        verify(openMetadataStore, never()).resynchronizeAnchorsZoneMembership(isNull(), any());

        this.verifyCompletion(AnchorsZoneResynchronizerGuard.ZONES_UP_TO_DATE);
    }


    /**
     * When the server reports that some elements could not be corrected, the service completes with the failure
     * guard rather than claiming the zones are up to date.
     *
     * @throws Exception not expected
     */
    @Test public void testIncompleteCheckRecorded() throws Exception
    {
        when(openMetadataStore.resynchronizeAnchorsZoneMembership(isNull(), any())).thenThrow(this.incompleteCheck());

        this.runService();

        this.verifyCompletion(AnchorsZoneResynchronizerGuard.RESYNC_FAILED);
    }


    /**
     * A target that can not be corrected does not stop the others being checked, and the service completes with
     * the failure guard even though other targets were corrected.
     *
     * @throws Exception not expected
     */
    @Test public void testFailedTargetDoesNotStopOthers() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("guid-1"), this.actionTarget("guid-2")));
        when(openMetadataStore.resynchronizeAnchorsZoneMembership(eq("guid-1"), any())).thenThrow(this.incompleteCheck());
        when(openMetadataStore.resynchronizeAnchorsZoneMembership(eq("guid-2"), any())).thenReturn(2L);

        this.runService();

        verify(openMetadataStore).resynchronizeAnchorsZoneMembership(eq("guid-2"), any());

        this.verifyCompletion(AnchorsZoneResynchronizerGuard.RESYNC_FAILED);
    }


    /**
     * An unexpected error is recorded with the failure guard, and reported to the engine.
     *
     * @throws Exception not expected
     */
    @Test public void testUnexpectedFailureRecorded() throws Exception
    {
        when(openMetadataStore.resynchronizeAnchorsZoneMembership(isNull(), any())).thenThrow(new IllegalStateException("repository unavailable"));

        assertThrows(ConnectorCheckedException.class, this::runService);

        verify(governanceContext).recordCompletionStatus(eq(AnchorsZoneResynchronizerGuard.RESYNC_FAILED.getCompletionStatus()),
                                                         eq(List.of(AnchorsZoneResynchronizerGuard.RESYNC_FAILED.getName())),
                                                         isNull(),
                                                         isNull(),
                                                         anyString());
    }


    private PropertyServerException incompleteCheck()
    {
        return new PropertyServerException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition("test", "test", "1 element not corrected"),
                                           "test",
                                           "test");
    }


    private void runService() throws Exception
    {
        AnchorsZoneResynchronizerGovernanceActionConnector connector = new AnchorsZoneResynchronizerGovernanceActionConnector();

        connector.initialize("test-instance", new Connection());
        connector.setGovernanceContext(governanceContext);
        connector.start();
    }


    private ActionTargetElement actionTarget(String guid)
    {
        OpenMetadataElement element = new OpenMetadataElement();

        element.setElementGUID(guid);

        ActionTargetElement actionTarget = new ActionTargetElement();

        actionTarget.setTargetElement(element);

        return actionTarget;
    }


    private void verifyCompletion(AnchorsZoneResynchronizerGuard guard) throws Exception
    {
        verify(governanceContext).recordCompletionStatus(eq(guard.getCompletionStatus()),
                                                         eq(List.of(guard.getName())),
                                                         isNull(),
                                                         isNull(),
                                                         any(AuditLogMessageDefinition.class));
    }
}
