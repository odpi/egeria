/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.SubjectArea;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UnexpectedExceptionResponse is the response that wraps a UnexpectedException
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UnexpectedExceptionResponse extends SubjectAreaOMASAPIResponse
{
    protected String    exceptionClassName = null;
    protected String    exceptionErrorMessage = null;
    protected String    exceptionSystemAction = null;
    protected String    exceptionUserAction = null;

    /**
     * Default constructor
     */
    public UnexpectedExceptionResponse()
    {
        this.setResponseCategory(ResponseCategory.UnexpectedException);
    }
    public UnexpectedExceptionResponse(String methodName, String errorMessage)
    {
        this.setResponseCategory(ResponseCategory.UnexpectedException);
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_DOES_NOT_EXIST;
        this.exceptionErrorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName,errorMessage);
        this.exceptionSystemAction = errorCode.getSystemAction();
        this.exceptionUserAction = errorCode.getUserAction();

    }
    public UnexpectedExceptionResponse(SubjectAreaCheckedExceptionBase e)
    {
        this();
        this.exceptionClassName = e.getReportingClassName();
        this.exceptionErrorMessage = e.getErrorMessage();
        this.exceptionSystemAction = e.getReportedSystemAction();
        this.exceptionUserAction = e.getReportedUserAction();
    }

    @Override
    public String toString()
    {
        return "UnexpectedExceptionResponse{" +
                super.toString() +
                "relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                "category=" + this.responseCategory +
                '}';
    }

    /**
     * Return the name of the Java class name to use to recreate the exception.
     *
     * @return String name of the fully-qualified java class name
     */
    public String getExceptionClassName()
    {
        return exceptionClassName;
    }

    /**
     * Set up the name of the Java class name to use to recreate the exception.
     *
     * @param exceptionClassName - String name of the fully-qualified java class name
     */
    public void setExceptionClassName(String exceptionClassName)
    {
        this.exceptionClassName = exceptionClassName;
    }

    /**
     * Return the error message associated with the exception.
     *
     * @return string error message
     */
    public String getExceptionErrorMessage()
    {
        return exceptionErrorMessage;
    }

    /**
     * Set up the error message associated with the exception.
     *
     * @param exceptionErrorMessage - string error message
     */
    public void setExceptionErrorMessage(String exceptionErrorMessage)
    {
        this.exceptionErrorMessage = exceptionErrorMessage;
    }

    /**
     * Return the description of the action taken by the system as a result of the exception.
     *
     * @return - string description of the action taken
     */
    public String getExceptionSystemAction()
    {
        return exceptionSystemAction;
    }

    /**
     * Set up the description of the action taken by the system as a result of the exception.
     *
     * @param exceptionSystemAction - string description of the action taken
     */
    public void setExceptionSystemAction(String exceptionSystemAction)
    {
        this.exceptionSystemAction = exceptionSystemAction;
    }

    /**
     * Return the action that a user should take to resolve the problem.
     *
     * @return string instructions
     */
    public String getExceptionUserAction()
    {
        return exceptionUserAction;
    }

    /**
     * Set up the action that a user should take to resolve the problem.
     *
     * @param exceptionUserAction - string instructions
     */
    public void setExceptionUserAction(String exceptionUserAction)
    {
        this.exceptionUserAction = exceptionUserAction;
    }
}
