/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.ffdc.exception;

import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ConformanceSuiteRuntimeException is used for all logic errors detected by the Open Metadata Conformance Suite.
 * It is used in conjunction with the ConformanceSuiteErrorCode to provide first failure data capture for these errors.
 */
public class ConformanceSuiteRuntimeException extends RuntimeException
{
    @Serial
    private static final long   serialVersionUID = 1L;
    private static final Logger log              = LoggerFactory.getLogger(ConformanceSuiteRuntimeException.class);

    private static final MessageFormatter messageFormatter = new MessageFormatter();

    private final int                 reportedHTTPCode;
    private final String              reportingClassName;
    private final String              reportingActionDescription;
    private final String              reportedErrorMessage;
    private       String              reportedErrorMessageId;
    private       String[]            reportedErrorMessageParameters;
    private final String              reportedSystemAction;
    private final String              reportedUserAction;
    private       Throwable           reportedCaughtException          = null;
    private       String              reportedCaughtExceptionClassName = null;
    private       Map<String, Object> relatedProperties                = null;


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     */
    public ConformanceSuiteRuntimeException(ExceptionMessageDefinition messageDefinition,
                                            String                     className,
                                            String                     actionDescription)
    {
        this(messageDefinition, className, actionDescription, (Map<String, Object>)null);
    }


    /**
     * This is the typical constructor used for creating an exception.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public ConformanceSuiteRuntimeException(ExceptionMessageDefinition messageDefinition,
                                            String                     className,
                                            String                     actionDescription,
                                            Map<String, Object>        relatedProperties)
    {
        super(messageFormatter.getFormattedMessage(messageDefinition));

        this.reportedHTTPCode = messageDefinition.getHttpErrorCode();
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = messageFormatter.getFormattedMessage(messageDefinition);
        this.reportedErrorMessageId = messageDefinition.getMessageId();
        this.reportedErrorMessageParameters = messageDefinition.getMessageParams();
        this.reportedSystemAction = messageDefinition.getSystemAction();
        this.reportedUserAction = messageDefinition.getUserAction();
        this.relatedProperties = relatedProperties;

        this.validateCoreProperties();

        log.debug("{}, {}, {}", messageDefinition, className, actionDescription);
    }


    /**
     * This is the constructor used for creating an exception when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     */
    public ConformanceSuiteRuntimeException(ExceptionMessageDefinition messageDefinition,
                                            String                     className,
                                            String                     actionDescription,
                                            Throwable                  caughtError)
    {
        this(messageDefinition, className, actionDescription, caughtError, null);
    }


    /**
     * This is the constructor used for creating an exception when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public ConformanceSuiteRuntimeException(ExceptionMessageDefinition messageDefinition,
                                            String                     className,
                                            String                     actionDescription,
                                            Throwable                  caughtError,
                                            Map<String, Object>        relatedProperties)
    {
        super(messageFormatter.getFormattedMessage(messageDefinition), caughtError);

        this.reportedHTTPCode = messageDefinition.getHttpErrorCode();
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = messageFormatter.getFormattedMessage(messageDefinition);
        this.reportedErrorMessageId = messageDefinition.getMessageId();
        this.reportedErrorMessageParameters = messageDefinition.getMessageParams();
        this.reportedSystemAction = messageDefinition.getSystemAction();
        this.reportedUserAction = messageDefinition.getUserAction();
        this.reportedCaughtException = caughtError;
        this.reportedCaughtExceptionClassName = caughtError.getClass().getName();
        this.relatedProperties = relatedProperties;

        this.validateCoreProperties();

        log.debug("{}, {}, {}, {}", messageDefinition, className, actionDescription, caughtError);
    }

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode           http response code to use if this exception flows over a rest call
     * @param className          name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage       description of error
     * @param systemAction       actions of the system as a result of the error
     * @param userAction         instructions for correcting the error
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public ConformanceSuiteRuntimeException(int                 httpCode,
                                            String              className,
                                            String              actionDescription,
                                            String              errorMessage,
                                            String              systemAction,
                                            String              userAction,
                                            Map<String, Object> relatedProperties)
    {
        super(errorMessage);
        this.reportedHTTPCode = httpCode;
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.relatedProperties = relatedProperties;
    }


    /**
     * This is the constructor used for creating an exception when an unexpected error has been
     * caught.
     *
     * @param httpCode           http response code to use if this exception flows over a rest call
     * @param className          name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage       description of error
     * @param systemAction       actions of the system as a result of the error
     * @param userAction         instructions for correcting the error
     * @param caughtError        previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public ConformanceSuiteRuntimeException(int                 httpCode,
                                            String              className,
                                            String              actionDescription,
                                            String              errorMessage,
                                            String              systemAction,
                                            String              userAction,
                                            Exception           caughtError,
                                            Map<String, Object> relatedProperties)
    {
        super(errorMessage, caughtError);
        this.reportedHTTPCode = httpCode;
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.reportedCaughtException = caughtError;
        this.relatedProperties = relatedProperties;
    }


    /**
     * Check that essential details of the exception are populated.
     */
    private void validateCoreProperties()
    {
        if (reportedHTTPCode == 0)
        {
            log.error("Zero HTTP code passed to an exception");
        }

        if (reportedErrorMessage == null)
        {
            log.error("Null error message passed to an exception");
        }

        if (reportedErrorMessageId == null)
        {
            log.error("Null error message Identifier passed to an exception");
        }

        if (reportedSystemAction == null)
        {
            log.error("Null system action passed to an exception");
        }

        if (reportedUserAction == null)
        {
            log.error("Null user action passed to an exception");
        }

        if (reportingActionDescription == null)
        {
            log.error("Null action description passed to an exception");
        }

        if (reportingClassName == null)
        {
            log.error("Null class name passed to an exception");
        }
    }


    /**
     * Return the HTTP response code to use with this exception.
     *
     * @return reportedHTTPCode
     */
    public int getReportedHTTPCode()
    {
        return reportedHTTPCode;
    }


    /**
     * The class that created this exception.
     *
     * @return reportingClassName
     */
    public String getReportingClassName()
    {
        return reportingClassName;
    }


    /**
     * The type of request that the class was performing when the condition occurred that resulted in this
     * exception.
     *
     * @return reportingActionDescription
     */
    public String getReportingActionDescription()
    {
        return reportingActionDescription;
    }


    /**
     * A formatted short description of the cause of the condition that resulted in this exception.
     * It includes the message id and is formatted with the message parameters.  The message is defined in En_US.
     * The method is deprecated because it is inconsistent in its naming compared with other methods.
     *
     * @return string message
     */
    @Deprecated
    public String getErrorMessage()
    {
        return reportedErrorMessage;
    }

    /**
     * A formatted short description of the cause of the condition that resulted in this exception.
     * It includes the message id and is formatted with the message parameters.  The message is defined in En_US.
     *
     * @return string message
     */
    public String getReportedErrorMessage()
    {
        return reportedErrorMessage;
    }


    /**
     * Return the formal message identifier for the error message.  This is incorporated in the error message.
     * This is provided both for automated processing and to enable the error message to be reformatted
     * in a different language.
     *
     * @return string message id
     */
    public String getReportedErrorMessageId()
    {
        return reportedErrorMessageId;
    }


    /**
     * Return the parameters that were inserted in the error message.
     * These are provided both for automated processing and to enable the error message to be reformatted
     * in a different language.
     *
     * @return list of parameter values
     */
    public String[] getReportedErrorMessageParameters()
    {
        return reportedErrorMessageParameters;
    }


    /**
     * A description of the action that the system took as a result of the error condition.
     *
     * @return reportedSystemAction
     */
    public String getReportedSystemAction()
    {
        return reportedSystemAction;
    }


    /**
     * A description of the action necessary to correct the error.
     *
     * @return reportedUserAction
     */
    public String getReportedUserAction()
    {
        return reportedUserAction;
    }


    /**
     * An exception that was caught and wrapped by this exception.  If a null is returned, then this exception is
     * either newly created and not the result of a previous exception or the exception occurred in a remote
     * server.  If the second situation is true then reportedCaughtExceptionClassName is set.
     *
     * @return reportedCaughtException Throwable object
     */
    public Throwable getReportedCaughtException() { return reportedCaughtException; }


    /**
     * An exception that was caught and wrapped by this exception.  If a null is returned, then this exception is
     * the result of a newly detected error and not caused by another exception.
     *
     * @return full class name of the original exception
     */
    public String getReportedCaughtExceptionClassName()
    {
        return reportedCaughtExceptionClassName;
    }


    /**
     * Return any additional properties that were added to the exception to aid diagnosis.
     *
     * @return property map
     */
    public Map<String, Object> getRelatedProperties()
    {
        if (relatedProperties == null)
        {
            return null;
        }
        else if (relatedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(relatedProperties);
        }
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        ConformanceSuiteRuntimeException that = (ConformanceSuiteRuntimeException) objectToCompare;
        return reportedHTTPCode == that.reportedHTTPCode &&
                       Objects.equals(reportingClassName, that.reportingClassName) &&
                       Objects.equals(reportingActionDescription, that.reportingActionDescription) &&
                       Objects.equals(reportedErrorMessage, that.reportedErrorMessage) &&
                       Objects.equals(reportedErrorMessageId, that.reportedErrorMessageId) &&
                       Arrays.equals(reportedErrorMessageParameters, that.reportedErrorMessageParameters) &&
                       Objects.equals(reportedSystemAction, that.reportedSystemAction) &&
                       Objects.equals(reportedUserAction, that.reportedUserAction) &&
                       Objects.equals(reportedCaughtException, that.reportedCaughtException) &&
                       Objects.equals(relatedProperties, that.relatedProperties);
    }

    /**
     * Provide a common implementation of hashCode for all OCF Exception objects.
     *
     * @return integer hash code based on the values in the attributes
     */
    @Override
    public int hashCode()
    {
        int result = Objects.hash(reportedHTTPCode, reportingClassName, reportingActionDescription, reportedErrorMessage, reportedErrorMessageId,
                                  reportedSystemAction, reportedUserAction, reportedCaughtException, relatedProperties);
        result = 31 * result + Arrays.hashCode(reportedErrorMessageParameters);
        return result;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConformanceSuiteRuntimeException{" +
                       "reportedHTTPCode=" + reportedHTTPCode +
                       ", reportingClassName='" + reportingClassName + '\'' +
                       ", reportingActionDescription='" + reportingActionDescription + '\'' +
                       ", reportedErrorMessage='" + reportedErrorMessage + '\'' +
                       ", reportedErrorMessageId='" + reportedErrorMessageId + '\'' +
                       ", reportedErrorMessageParameters=" + Arrays.toString(reportedErrorMessageParameters) +
                       ", reportedSystemAction='" + reportedSystemAction + '\'' +
                       ", reportedUserAction='" + reportedUserAction + '\'' +
                       ", reportedCaughtException=" + reportedCaughtException +
                       ", reportedCaughtExceptionClassName='" + reportedCaughtExceptionClassName + '\'' +
                       ", relatedProperties=" + relatedProperties +
                       '}';
    }
}
