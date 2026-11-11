/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.mockito.ArgumentCaptor;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.opengovernance.WatchdogGovernanceListener;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyActionTarget;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertThrows;

/**
 * WaitForStewardGovernanceActionConnectorTest runs the Wait For Steward Watchdog Action Service over a mocked
 * watchdog context.  The events are passed to the listener that the service registers, and the tests check
 * when the service completes and the guard it produces.
 */
public class WaitForStewardGovernanceActionConnectorTest
{
    private static final String TO_DO_GUID       = "to-do-guid";
    private static final String TO_DO_TWO_GUID   = "to-do-two-guid";
    private static final String OTHER_TO_DO_GUID = "other-to-do-guid";
    private static final String STEWARD_GUID     = "steward-guid";

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private WatchdogContext   watchdogContext;
    private OpenMetadataStore openMetadataStore;


    /**
     * A mocked watchdog context with no action targets.
     *
     * @throws Exception not expected
     */
    @BeforeMethod public void setUpContext() throws Exception
    {
        watchdogContext   = mock(WatchdogContext.class);
        openMetadataStore = mock(OpenMetadataStore.class);

        when(watchdogContext.getOpenMetadataStore()).thenReturn(openMetadataStore);
    }


    /**
     * With no to dos in the action targets there is nothing to wait for, so the service completes straight away
     * without registering a listener.
     *
     * @throws Exception not expected
     */
    @Test public void testNoToDos() throws Exception
    {
        when(watchdogContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(ActionTarget.STEWARD.getName(), STEWARD_GUID, OpenMetadataType.PERSON.typeName)));

        this.runService();

        verify(watchdogContext, never()).registerListener(any(), any(), any(), any());
        this.verifyCompletion(WaitForStewardGuard.NO_TO_DOS);
    }


    /**
     * A to do that was completed before the service started is found when the service checks the status of
     * its to dos.  The listener is limited to the one to do, and is removed when the service completes.
     *
     * @throws Exception not expected
     */
    @Test public void testToDoAlreadyCompleted() throws Exception
    {
        when(watchdogContext.getActionTargetElements()).thenReturn(List.of(this.toDoActionTarget(TO_DO_GUID)));
        when(openMetadataStore.getMetadataElementByGUID(TO_DO_GUID)).thenReturn(this.toDo(TO_DO_GUID, ActivityStatus.COMPLETED));

        this.runService();

        verify(watchdogContext).registerListener(any(),
                                                 eq(List.of(OpenMetadataEventType.ELEMENT_UPDATED, OpenMetadataEventType.ELEMENT_DELETED)),
                                                 eq(List.of(OpenMetadataType.TO_DO.typeName)),
                                                 eq(TO_DO_GUID));
        this.verifyCompletion(WaitForStewardGuard.TO_DOS_COMPLETED);
        verify(watchdogContext).disconnectListener();
    }


    /**
     * The service waits until every to do has been completed.  The to dos are the action target named
     * stewardAction and any action target whose element is a ToDo or one of its subtypes.  Updates that do not
     * finish a to do, and events for other to dos, are ignored, as are events after the service has completed.
     *
     * @throws Exception not expected
     */
    @Test public void testWaitsForEveryToDo() throws Exception
    {
        ActionTargetElement subtypeToDo = this.actionTarget(ActionTarget.NEW_ASSET.getName(), TO_DO_TWO_GUID, "SpecialToDo");
        subtypeToDo.getTargetElement().getType().setSuperTypeNames(List.of(OpenMetadataType.TO_DO.typeName));

        when(watchdogContext.getActionTargetElements()).thenReturn(List.of(this.toDoActionTarget(TO_DO_GUID),
                                                                           subtypeToDo,
                                                                           this.actionTarget(ActionTarget.STEWARD.getName(), STEWARD_GUID, OpenMetadataType.PERSON.typeName)));
        when(openMetadataStore.getMetadataElementByGUID(TO_DO_GUID)).thenReturn(this.toDo(TO_DO_GUID, ActivityStatus.REQUESTED));
        when(openMetadataStore.getMetadataElementByGUID(TO_DO_TWO_GUID)).thenReturn(this.toDo(TO_DO_TWO_GUID, ActivityStatus.IN_PROGRESS));

        WatchdogGovernanceListener listener = this.runServiceAndGetListener(null);

        listener.processEvent(this.updateEvent(OTHER_TO_DO_GUID, ActivityStatus.COMPLETED));
        listener.processEvent(this.updateEvent(TO_DO_GUID, ActivityStatus.IN_PROGRESS));
        listener.processEvent(this.updateEvent(TO_DO_GUID, ActivityStatus.COMPLETED));
        this.verifyNotCompleted();

        listener.processEvent(this.updateEvent(TO_DO_TWO_GUID, ActivityStatus.COMPLETED));
        this.verifyCompletion(WaitForStewardGuard.TO_DOS_COMPLETED);

        listener.processEvent(this.deleteEvent(TO_DO_GUID));
        this.verifyCompletion(WaitForStewardGuard.TO_DOS_COMPLETED);
    }


    /**
     * A to do that finishes with any status other than COMPLETED means that the to dos were not completed.
     *
     * @throws Exception not expected
     */
    @Test public void testToDoAbandoned() throws Exception
    {
        when(watchdogContext.getActionTargetElements()).thenReturn(List.of(this.toDoActionTarget(TO_DO_GUID),
                                                                           this.toDoActionTarget(TO_DO_TWO_GUID)));
        when(openMetadataStore.getMetadataElementByGUID(TO_DO_GUID)).thenReturn(this.toDo(TO_DO_GUID, ActivityStatus.COMPLETED));
        when(openMetadataStore.getMetadataElementByGUID(TO_DO_TWO_GUID)).thenReturn(this.toDo(TO_DO_TWO_GUID, ActivityStatus.WAITING));

        WatchdogGovernanceListener listener = this.runServiceAndGetListener(null);

        this.verifyNotCompleted();

        listener.processEvent(this.updateEvent(TO_DO_TWO_GUID, ActivityStatus.ABANDONED));

        this.verifyCompletion(WaitForStewardGuard.TO_DOS_NOT_COMPLETED);
    }


    /**
     * A to do that is deleted while the service is waiting has finished without being completed.
     *
     * @throws Exception not expected
     */
    @Test public void testToDoDeletedWhileWaiting() throws Exception
    {
        when(watchdogContext.getActionTargetElements()).thenReturn(List.of(this.toDoActionTarget(TO_DO_GUID)));
        when(openMetadataStore.getMetadataElementByGUID(TO_DO_GUID)).thenReturn(this.toDo(TO_DO_GUID, ActivityStatus.REQUESTED));

        WatchdogGovernanceListener listener = this.runServiceAndGetListener(TO_DO_GUID);

        listener.processEvent(this.deleteEvent(TO_DO_GUID));

        this.verifyCompletion(WaitForStewardGuard.TO_DOS_NOT_COMPLETED);
    }


    /**
     * A to do that was deleted before the service started can no longer be retrieved, so it has finished
     * without being completed.
     *
     * @throws Exception not expected
     */
    @Test public void testToDoDeletedBeforeStart() throws Exception
    {
        when(watchdogContext.getActionTargetElements()).thenReturn(List.of(this.toDoActionTarget(TO_DO_GUID)));
        doThrow(mock(InvalidParameterException.class)).when(openMetadataStore).getMetadataElementByGUID(TO_DO_GUID);

        this.runService();

        this.verifyCompletion(WaitForStewardGuard.TO_DOS_NOT_COMPLETED);
    }


    /**
     * A failure to register the listener is reported with the wait-failed guard and an exception.
     *
     * @throws Exception not expected
     */
    @Test public void testListenerNotRegistered() throws Exception
    {
        when(watchdogContext.getActionTargetElements()).thenReturn(List.of(this.toDoActionTarget(TO_DO_GUID)));
        doThrow(mock(InvalidParameterException.class)).when(watchdogContext).registerListener(any(), any(), any(), any());

        assertThrows(GovernanceServiceException.class, this::runService);

        this.verifyCompletion(WaitForStewardGuard.WAIT_FAILED);
    }


    /**
     * A failure to read a to do is reported with the wait-failed guard and an exception.
     *
     * @throws Exception not expected
     */
    @Test public void testToDoNotRetrieved() throws Exception
    {
        when(watchdogContext.getActionTargetElements()).thenReturn(List.of(this.toDoActionTarget(TO_DO_GUID)));
        doThrow(mock(PropertyServerException.class)).when(openMetadataStore).getMetadataElementByGUID(TO_DO_GUID);

        assertThrows(GovernanceServiceException.class, this::runService);

        this.verifyCompletion(WaitForStewardGuard.WAIT_FAILED);
    }


    /**
     * Run the service.
     *
     * @throws Exception not expected
     */
    private void runService() throws Exception
    {
        WaitForStewardGovernanceActionConnector connector = new WaitForStewardGovernanceActionConnector();

        connector.initialize("test-instance", new Connection());
        connector.setWatchdogContext(watchdogContext);
        connector.start();
    }


    /**
     * Run the service and return the listener that it registered.
     *
     * @param specificInstance the instance that the listener is expected to be restricted to
     * @return listener
     * @throws Exception not expected
     */
    private WatchdogGovernanceListener runServiceAndGetListener(String specificInstance) throws Exception
    {
        this.runService();

        ArgumentCaptor<WatchdogGovernanceListener> listener = ArgumentCaptor.forClass(WatchdogGovernanceListener.class);

        verify(watchdogContext).registerListener(listener.capture(), any(), any(), eq(specificInstance));

        return listener.getValue();
    }


    /**
     * Check that the service completed once with the requested guard and a message describing the outcome.
     *
     * @param guard expected guard
     * @throws Exception not expected
     */
    private void verifyCompletion(WaitForStewardGuard guard) throws Exception
    {
        verify(watchdogContext).recordCompletionStatus(eq(guard.getCompletionStatus()),
                                                       eq(List.of(guard.getName())),
                                                       isNull(),
                                                       isNull(),
                                                       notNull(AuditLogMessageDefinition.class));
    }


    /**
     * Check that the service is still waiting.
     *
     * @throws Exception not expected
     */
    private void verifyNotCompleted() throws Exception
    {
        verify(watchdogContext, never()).recordCompletionStatus(any(), any(), any(), any(), nullable(AuditLogMessageDefinition.class));
        verify(watchdogContext, never()).disconnectListener();
    }


    /**
     * Return an action target for a to do, named stewardAction.
     *
     * @param toDoGUID unique identifier of the to do
     * @return action target
     */
    private ActionTargetElement toDoActionTarget(String toDoGUID)
    {
        return this.actionTarget(SurveyActionTarget.TO_DO_ELEMENT.getName(), toDoGUID, OpenMetadataType.TO_DO.typeName);
    }


    /**
     * Return an action target.
     *
     * @param name name of the action target
     * @param guid unique identifier of the element
     * @param typeName type of the element
     * @return action target
     */
    private ActionTargetElement actionTarget(String name,
                                             String guid,
                                             String typeName)
    {
        ActionTargetElement actionTarget = new ActionTargetElement();

        actionTarget.setActionTargetName(name);
        actionTarget.setActionTargetGUID(guid);
        actionTarget.setTargetElement(this.element(guid, typeName, null));

        return actionTarget;
    }


    /**
     * Return a to do with the requested activity status.
     *
     * @param toDoGUID unique identifier of the to do
     * @param activityStatus status of the to do
     * @return element
     */
    private OpenMetadataElement toDo(String         toDoGUID,
                                     ActivityStatus activityStatus)
    {
        return this.element(toDoGUID, OpenMetadataType.TO_DO.typeName, this.toDoProperties(activityStatus));
    }


    /**
     * Return a metadata element.
     *
     * @param guid unique identifier of the element
     * @param typeName type of the element
     * @param properties properties of the element
     * @return element
     */
    private OpenMetadataElement element(String            guid,
                                        String            typeName,
                                        ElementProperties properties)
    {
        ElementType elementType = new ElementType();
        elementType.setTypeName(typeName);

        OpenMetadataElement element = new OpenMetadataElement();

        element.setElementGUID(guid);
        element.setType(elementType);
        element.setElementProperties(properties);

        return element;
    }


    /**
     * Return the properties of a to do with the requested activity status.
     *
     * @param activityStatus status of the to do
     * @return properties
     */
    private ElementProperties toDoProperties(ActivityStatus activityStatus)
    {
        return propertyHelper.addEnumProperty(null,
                                              OpenMetadataProperty.ACTIVITY_STATUS.name,
                                              ActivityStatus.getOpenTypeName(),
                                              activityStatus.name());
    }


    /**
     * Return an event reporting that a to do has been updated.
     *
     * @param toDoGUID unique identifier of the to do
     * @param activityStatus new status of the to do
     * @return event
     */
    private OpenMetadataOutTopicEvent updateEvent(String         toDoGUID,
                                                  ActivityStatus activityStatus)
    {
        OpenMetadataOutTopicEvent event = this.event(OpenMetadataEventType.ELEMENT_UPDATED, toDoGUID);

        event.setElementProperties(this.toDoProperties(activityStatus));

        return event;
    }


    /**
     * Return an event reporting that a to do has been deleted.
     *
     * @param toDoGUID unique identifier of the to do
     * @return event
     */
    private OpenMetadataOutTopicEvent deleteEvent(String toDoGUID)
    {
        return this.event(OpenMetadataEventType.ELEMENT_DELETED, toDoGUID);
    }


    /**
     * Return an event about a to do.
     *
     * @param eventType type of event
     * @param toDoGUID unique identifier of the to do
     * @return event
     */
    private OpenMetadataOutTopicEvent event(OpenMetadataEventType eventType,
                                            String                toDoGUID)
    {
        ElementType elementType = new ElementType();
        elementType.setTypeName(OpenMetadataType.TO_DO.typeName);

        ElementHeader elementHeader = new ElementHeader();
        elementHeader.setGUID(toDoGUID);
        elementHeader.setType(elementType);

        OpenMetadataOutTopicEvent event = new OpenMetadataOutTopicEvent();

        event.setEventType(eventType);
        event.setElementHeader(elementHeader);

        return event;
    }
}
