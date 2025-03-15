/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.omrstopic;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;


/**
 * OMRSTopicListenerWrapper is a class that wraps a real OMRSTopicListener when it registers with the
 * OMRSTopicConnector.  Its sole purpose is to catch exceptions from the real OMRSTopicListener and create
 * diagnostics.  The listeners are called in parallel with no mechanism for the connector to properly
 * manage errors from the listener so this wrapper has been installed.  If the real OMRSTopicListener
 * has been implemented properly then no errors should be handled by this wrapper class
 */
public class OMRSTopicListenerWrapper implements OMRSTopicListener
{
    private final String THREAD_NAME_DESCRIPTION = " OMRSTopicListener";

    private OMRSTopicListener                fullListener;
    private OMRSTopicRepositoryEventListener repositoryListener;
    private final AuditLog                   auditLog;
    private String                           serviceName = "<Unknown Service>";


    /**
     * Save the real listener and other error handling information.
     *
     * @param fullListener this is the topic listener that was registered.
     * @param serviceName this is the name of the service that owns the topic listener.
     * @param auditLog this is the log destination
     */
    OMRSTopicListenerWrapper(OMRSTopicListener fullListener,
                             String            serviceName,
                             AuditLog          auditLog)
    {
        this.fullListener = fullListener;
        this.repositoryListener = fullListener;
        this.serviceName = serviceName;
        this.auditLog = auditLog;
    }


    /**
     * Save the real listener and other error handling information.
     *
     * @param repositoryEventListener this is the topic listener that was registered.
     * @param serviceName this is the name of the service that owns the topic listener.
     * @param auditLog this is the log destination
     */
    OMRSTopicListenerWrapper(OMRSTopicRepositoryEventListener repositoryEventListener,
                             String                           serviceName,
                             AuditLog                         auditLog)
    {
        this.repositoryListener = repositoryEventListener;
        this.serviceName = serviceName;
        this.auditLog = auditLog;
    }


    /**
     * Save the real listener and other error handling information.
     *
     * @param fullListener this is the topic listener that was registered.
     * @param auditLog this is the log destination
     */
    @Deprecated
    OMRSTopicListenerWrapper(OMRSTopicListener fullListener,
                             AuditLog          auditLog)
    {
        this.fullListener = fullListener;
        this.auditLog = auditLog;
    }


    /**
     * Log an audit log message to record an unexpected exception.  We should never see this message.
     * It indicates a logic error in the service that threw the exception.
     *
     * @param error exception
     * @param methodName calling activity
     */
    private void logUnhandledException(Exception  error,
                                       String     methodName)
    {
        auditLog.logException(methodName,
                              OMRSAuditCode.UNHANDLED_EXCEPTION_FROM_SERVICE_LISTENER.getMessageDefinition(serviceName,
                                                                                                           error.getClass().getName(),
                                                                                                           error.getMessage()),
                              error);
    }


    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    public void processRegistryEvent(OMRSRegistryEvent event)
    {
        final String methodName = "processRegistryEvent";

        String currentThreadName = Thread.currentThread().getName();

        Thread.currentThread().setName(serviceName + THREAD_NAME_DESCRIPTION);

        try
        {
            fullListener.processRegistryEvent(event);
        }
        catch (Exception  error)
        {
            logUnhandledException(error, methodName);
        }

        Thread.currentThread().setName(currentThreadName);
    }


    /**
     * Method to pass a TypeDef event received on topic.
     *
     * @param event inbound event
     */
    public void processTypeDefEvent(OMRSTypeDefEvent event)
    {
        final String methodName = "processTypeDefEvent";

        String currentThreadName = Thread.currentThread().getName();

        Thread.currentThread().setName(serviceName + THREAD_NAME_DESCRIPTION);

        try
        {
            repositoryListener.processTypeDefEvent(event);
        }
        catch (Exception  error)
        {
            logUnhandledException(error, methodName);
        }

        Thread.currentThread().setName(currentThreadName);
    }


    /**
     * Method to pass an Instance event received on topic.
     *
     * @param event inbound event
     */
    public void processInstanceEvent(OMRSInstanceEvent event)
    {
        final String methodName = "processInstanceEvent";

        String currentThreadName = Thread.currentThread().getName();

        Thread.currentThread().setName(serviceName + THREAD_NAME_DESCRIPTION);

        try
        {
            repositoryListener.processInstanceEvent(event);
        }
        catch (Exception  error)
        {
            logUnhandledException(error, methodName);
        }

        Thread.currentThread().setName(currentThreadName);
    }
}
