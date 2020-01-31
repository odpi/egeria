/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.ffdc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * OCFCheckedExceptionBase provides a checked exception for reporting errors found when using OCF connectors.
 * Typically these errors are either configuration or operational errors that can be fixed by an administrator
 * or power user.  However, there may be the odd bug that surfaces here. The OCFErrorCode can be used with
 * this exception to populate it with standard messages.  Otherwise messages defined uniquely for a
 * ConnectorProvider/Connector implementation can be used.  The aim is to be able to uniquely identify the cause
 * and remedy for the error.
 */
public abstract class OCFCheckedExceptionBase extends Exception
{
    private static final long    serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(OCFCheckedExceptionBase.class);

    /*
     * These default values are only seen if this exception is initialized using one of its superclass constructors.
     */
    private int                 reportedHTTPCode;
    private String              reportingClassName;
    private String              reportingActionDescription;
    private String              reportedErrorMessage;
    private String              reportedSystemAction;
    private String              reportedUserAction;
    private Throwable           reportedCaughtException = null;
    private Map<String, Object> relatedProperties = null;


    /**
     * This is the typical constructor used for creating an OCFCheckedException.
     *
     * @param httpCode   http response code to use if this exception flows over a REST call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     */
    public OCFCheckedExceptionBase(int    httpCode,
                                   String className,
                                   String actionDescription,
                                   String errorMessage,
                                   String systemAction,
                                   String userAction)
    {
        super(errorMessage);

        this.reportedHTTPCode = httpCode;
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;

        this.validateCoreProperties();
    }


    /**
     * This is the typical constructor used for creating an OCFCheckedException.
     *
     * @param httpCode   http response code to use if this exception flows over a REST call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public OCFCheckedExceptionBase(int                 httpCode,
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

        this.validateCoreProperties();
    }


    /**
     * This is the constructor used for creating an OCFCheckedException when an unexpected error has been caught.
     *
     * @param httpCode   http response code to use if this exception flows over a REST call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   previous error causing this exception
     */
    public OCFCheckedExceptionBase(int       httpCode,
                                   String    className,
                                   String    actionDescription,
                                   String    errorMessage,
                                   String    systemAction,
                                   String    userAction,
                                   Throwable caughtError)
    {
        super(errorMessage, caughtError);

        this.reportedHTTPCode = httpCode;
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.reportedCaughtException = caughtError;

        this.validateCoreProperties();
    }


    /**
     * This is the constructor used for creating an OCFCheckedException when an unexpected error has been caught.
     *
     * @param httpCode   http response code to use if this exception flows over a REST call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public OCFCheckedExceptionBase(int                 httpCode,
                                   String              className,
                                   String              actionDescription,
                                   String              errorMessage,
                                   String              systemAction,
                                   String              userAction,
                                   Throwable           caughtError,
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

        this.validateCoreProperties();
    }


    /**
     * This is the copy/clone constructor used for creating an OCFCheckedException.
     *
     * @param errorMessage message for the exception
     * @param template   object to copy
     */
    public OCFCheckedExceptionBase(String                  errorMessage,
                                   OCFCheckedExceptionBase template)
    {
        super(errorMessage, template);

        if (template != null)
        {
            this.reportedHTTPCode = template.getReportedHTTPCode();
            this.reportingClassName = template.getReportingClassName();
            this.reportingActionDescription = template.getReportingActionDescription();
            this.reportedErrorMessage = errorMessage;
            this.reportedSystemAction = template.getReportedSystemAction();
            this.reportedUserAction = template.getReportedUserAction();
        }

        this.validateCoreProperties();
    }


    /**
     * This is the copy/clone constructor used for creating an OCFCheckedException.
     *
     * @param template   object to copy
     */
    public OCFCheckedExceptionBase(OCFCheckedExceptionBase template)
    {
        super(template);

        if (template != null)
        {
            this.reportedHTTPCode = template.getReportedHTTPCode();
            this.reportingClassName = template.getReportingClassName();
            this.reportingActionDescription = template.getReportingActionDescription();
            this.reportedErrorMessage = template.getErrorMessage();
            this.reportedSystemAction = template.getReportedSystemAction();
            this.reportedUserAction = template.getReportedUserAction();
        }

        this.validateCoreProperties();
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

        if (reportingClassName == null)
        {
            log.error("Null class name passed to an exception");
        }

        if (reportedErrorMessage == null)
        {
            log.error("Null error message passed to an exception");
        }

        if (reportingActionDescription == null)
        {
            log.error("Null action description passed to an exception");
        }

        if (reportedSystemAction == null)
        {
            log.error("Null system action passed to an exception");
        }

        if (reportedUserAction == null)
        {
            log.error("Null user action passed to an exception");
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
     *
     * @return reportedErrorMessage
     */
    public String getErrorMessage()
    {
        return reportedErrorMessage;
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
     * newly created and not the result of a previous exception.
     *
     * @return reportedCaughtException
     */
    public Throwable getReportedCaughtException() { return reportedCaughtException; }


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
        if (!(objectToCompare instanceof OCFCheckedExceptionBase))
        {
            return false;
        }
        OCFCheckedExceptionBase that = (OCFCheckedExceptionBase) objectToCompare;
        return getReportedHTTPCode() == that.getReportedHTTPCode() &&
                Objects.equals(getReportingClassName(), that.getReportingClassName()) &&
                Objects.equals(getReportingActionDescription(), that.getReportingActionDescription()) &&
                Objects.equals(reportedErrorMessage, that.reportedErrorMessage) &&
                Objects.equals(getReportedSystemAction(), that.getReportedSystemAction()) &&
                Objects.equals(getReportedUserAction(), that.getReportedUserAction()) &&
                Objects.equals(getRelatedProperties(), that.getRelatedProperties()) &&
                Objects.equals(getReportedCaughtException(), that.getReportedCaughtException());
    }


    /**
     * Provide a common implementation of hashCode for all OCF Exception objects.
     *
     * @return integer hash code based on the values in the attributes
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getReportedHTTPCode(),
                            getReportingClassName(),
                            getReportingActionDescription(),
                            reportedErrorMessage,
                            getReportedSystemAction(),
                            getReportedUserAction(),
                            getRelatedProperties(),
                            getReportedCaughtException());
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OCFCheckedExceptionBase{" +
                ", reportedHTTPCode=" + reportedHTTPCode +
                ", reportingClassName='" + reportingClassName + '\'' +
                ", reportingActionDescription='" + reportingActionDescription + '\'' +
                ", reportedErrorMessage='" + reportedErrorMessage + '\'' +
                ", reportedSystemAction='" + reportedSystemAction + '\'' +
                ", reportedUserAction='" + reportedUserAction + '\'' +
                ", reportedCaughtException=" + reportedCaughtException +
                ", relatedProperties=" + relatedProperties +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}