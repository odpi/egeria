/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The ConformanceSuiteAuditCode is used to define the message content for the OMRS Audit Log.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum ConformanceSuiteAuditCode
{
    SERVICE_INITIALIZING("CONFORMANCE-SUITE-0001",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Conformance Service is initializing a new server instance",
             "The server platform has started up a new OMAG Server that will run the Open Metadata Conformance Suite.",
             "No action is required.  This is part of the normal operation of the service."),

    SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC("CONFORMANCE-SUITE-0002",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Conformance Suite is registering a listener with the OMRS Topic for server {0}",
             "The Open Metadata Conformance Suite is registering to receive events from the connected open metadata repositories.",
             "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INITIALIZED("CONFORMANCE-SUITE-0003",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Conformance Service has initialized a new instance for server {0}",
             "The Open Metadata Conformance Suite has completed initialization of a new instance.",
             "No action is required.  This is part of the normal operation of the service."),

    SERVICE_SHUTDOWN("CONFORMANCE-SUITE-0004",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Conformance Service is shutting down its instance for server {0}",
             "The server platform has requested shut down of an Open Metadata Conformance Suite instance.",
             "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INSTANCE_FAILURE("CONFORMANCE-SUITE-0005",
             OMRSAuditLogRecordSeverity.ERROR,
             "The Open Metadata Conformance Service is unable to initialize a new instance; error message is {0}",
             "The Open Metadata Conformance Suite detected an error during the start up of a specific server instance.  Its services are not available for the server.",
             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    WORKBENCH_INITIALIZING("CONFORMANCE-SUITE-0006",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Conformance Workbench {0} is initializing; see {1} for documentation",
             "The Open Metadata Conformance Service has started one of the workbenches from the conformance suite.",
             "No action is required.  This is part of the normal operation of the service."),

    WORKBENCH_INITIALIZED("CONFORMANCE-SUITE-0007",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Conformance Workbench {0} has initialized",
             "The Open Metadata Conformance Suite has completed initialization of a new workbench.",
             "No action is required.  This is part of the normal operation of the service."),

    WORKBENCH_WAITING_TO_START("CONFORMANCE-SUITE-0008",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Repository Conformance Workbench {0} is waiting for server {1} to join the cohort",
             "The Open Metadata Repository Conformance Workbench begins running tests once it receives a notification that the technology under test has joined the cohort.",
             "Ensure that the cohort is operating correctly and the technology under test joins the same cohort as the conformance suite."),

    WORKBENCH_FAILURE("CONFORMANCE-SUITE-0009",
             OMRSAuditLogRecordSeverity.ERROR,
             "The Open Metadata Conformance Workbench {0} is unable to run its tests; error message is {1}",
             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    WORKBENCH_COMPLETED("CONFORMANCE-SUITE-0010",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Conformance Workbench {0} has finished",
             "The Open Metadata Conformance Workbench has completed running its tests.",
             "No action is required.  This is part of the normal operation of the service."),

    WORKBENCH_SYNC_COMPLETED("CONFORMANCE-SUITE-0011",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Conformance Workbench {0} has completed its synchronous tests, further test cases may be triggered from incoming events",
             "The Open Metadata Conformance Workbench has completed running the tests it drives synchronously.  Further test activity will be triggered by incoming events from the cohort.",
             "No action is required.  This is part of the normal operation of the service."),

    TEST_CASE_INITIALIZING("CONFORMANCE-SUITE-0012",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Conformance Test Case {0} is initializing; see {1} for documentation",
             "The Open Metadata Conformance Service has started on of the workbenches from the conformance suite.",
             "No action is required.  This is part of the normal operation of the service."),

    TEST_CASE_COMPLETED("CONFORMANCE-SUITE-0013",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Conformance Test Case {0} has completed with {1} successful assertions, {2} unsuccessful assertions, {3} unexpected exceptions and {4} discovered properties.",
             "The Open Metadata Conformance Test Case has completed running its tests. Retrieve the report to find out more details.",
             "No action is required.  This is part of the normal operation of the service."),

    TEST_CASE_COMPLETED_SUCCESSFULLY("CONFORMANCE-SUITE-0014",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Conformance Test Case {0} has completed with {1} successful assertions, {2} unsuccessful assertions, {3} unexpected exceptions and {4} discovered properties.  The message on completion was: {5}",
             "The Open Metadata Conformance Test Case has completed running its tests. Retrieve the report to find out more details.",
             "No action is required.  This is part of the normal operation of the service."),

    LOCAL_CONNECTOR_IN_COHORT("CONFORMANCE-SUITE-0015",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Conformance Suite has access to the local repository with metadata collection id {0}",
             "The Open Metadata Conformance Suite is initializing.",
             "No action is required.  This is part of the normal operation of the service."),

    TUT_CONNECTED_TO_COHORT("CONFORMANCE-SUITE-0016",
             OMRSAuditLogRecordSeverity.INFO,
             "The technology under test with a server name of {0} connected to the same cohort as the Open Metadata Conformance Suite with metadata collection id {1}",
             "The Open Metadata Conformance Suite is able to start running tests.",
             "No action is required.  This is part of the normal operation of the service."),

    ANOTHER_CONNECTED_TO_COHORT("CONFORMANCE-SUITE-0017",
             OMRSAuditLogRecordSeverity.INFO,
             "Server {0} with metadata collection id {1} connected to the same cohort as the Open Metadata Conformance Suite that is configured to test a server named {2}",
             "The Open Metadata Conformance Suite will ignore this server.  However, it will also receive test metadata from the conformance suite which may not be what was intended.",
             "Verify that this server has joined the right cohort."),

    TUT_LEFT_COHORT("CONFORMANCE-SUITE-0018",
             OMRSAuditLogRecordSeverity.INFO,
             "The technology under test with server name {0} and metadata collection id {1} has left the cohort",
             "The Open Metadata Conformance Suite is unable to run any more tests.",
             "Extract the conformance report from the conformance suite to determine if the tests were successful."),

    ANOTHER_LEFT_COHORT("CONFORMANCE-SUITE-0019",
             OMRSAuditLogRecordSeverity.INFO,
             "Server {0} with metadata collection id {1} has left the cohort",
             "This server will no longer receive metadata instances from the conformance tests.",
             "No action is required.  This is part of the normal operation of the service."),

    TEST_EXECUTION_WAITING("CONFORMANCE-SUITE-0020",
            OMRSAuditLogRecordSeverity.INFO,
            "Test execution will now pause for {0} seconds",
            "The execution of tests will now pause for the specified number of seconds, based on the configuration of the workbench.",
            "No action is required.  This is part of the normal operation of the service."),

    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(ConformanceSuiteAuditCode.class);


    /**
     * The constructor for ConformanceSuiteAuditCode expects to be passed one of the enumeration rows defined in
     * ConformanceSuiteAuditCode above.   For example:
     *
     *     ConformanceSuiteAuditCode   auditCode = ConformanceSuiteAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId  unique Id for the message
     * @param severity  severity of the message
     * @param message  text for the message
     * @param systemAction  description of the action taken by the system when the condition happened
     * @param userAction  instructions for resolving the situation, if any
     */
    ConformanceSuiteAuditCode(String                     messageId,
                              OMRSAuditLogRecordSeverity severity,
                              String                     message,
                              String                     systemAction,
                              String                     userAction)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getLogMessageId()
    {
        return logMessageId;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity()
    {
        return severity;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params)
    {
        log.debug(String.format("<== Conformance Suite Audit Code.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        log.debug(String.format("==> Conformance Suite Audit Code.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }



    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction()
    {
        return userAction;
    }
}
