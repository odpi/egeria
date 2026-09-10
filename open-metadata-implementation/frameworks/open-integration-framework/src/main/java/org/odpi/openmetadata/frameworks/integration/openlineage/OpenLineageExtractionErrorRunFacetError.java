/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a single error that occurred while OpenLineage was extracting lineage for a task in the run.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageExtractionErrorRunFacetError
{
    private String errorMessage = null;
    private String stackTrace = null;
    private String task = null;
    private Long   taskNumber = null;


    /**
     * Default constructor
     */
    public OpenLineageExtractionErrorRunFacetError()
    {
    }


    /**
     * Return the text representation of the extraction error.
     *
     * @return string
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }


    /**
     * Set up the text representation of the extraction error.
     *
     * @param errorMessage string
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }


    /**
     * Return the stack trace of the extraction error.
     *
     * @return string
     */
    public String getStackTrace()
    {
        return stackTrace;
    }


    /**
     * Set up the stack trace of the extraction error.
     *
     * @param stackTrace string
     */
    public void setStackTrace(String stackTrace)
    {
        this.stackTrace = stackTrace;
    }


    /**
     * Return the text representation of the task that failed, for example the SQL statement.
     *
     * @return string
     */
    public String getTask()
    {
        return task;
    }


    /**
     * Set up the text representation of the task that failed, for example the SQL statement.
     *
     * @param task string
     */
    public void setTask(String task)
    {
        this.task = task;
    }


    /**
     * Return the order of the task in the run, if applicable.
     *
     * @return long
     */
    public Long getTaskNumber()
    {
        return taskNumber;
    }


    /**
     * Set up the order of the task in the run, if applicable.
     *
     * @param taskNumber long
     */
    public void setTaskNumber(Long taskNumber)
    {
        this.taskNumber = taskNumber;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageExtractionErrorRunFacetError{" +
                       "errorMessage='" + errorMessage + '\'' +
                       ", stackTrace='" + stackTrace + '\'' +
                       ", task='" + task + '\'' +
                       ", taskNumber=" + taskNumber +
                       '}';
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
        OpenLineageExtractionErrorRunFacetError that = (OpenLineageExtractionErrorRunFacetError) objectToCompare;
        return Objects.equals(errorMessage, that.errorMessage) &&
                       Objects.equals(stackTrace, that.stackTrace) &&
                       Objects.equals(task, that.task) &&
                       Objects.equals(taskNumber, that.taskNumber);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(errorMessage, stackTrace, task, taskNumber);
    }
}
