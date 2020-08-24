/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import java.util.Map;

public interface FFDCResponse {

    /**
     * Return the HTTP Code to use if forwarding response to HTTP client.
     *
     * @return integer HTTP status code
     */
    int getRelatedHTTPCode();

    /**
     * Return the name of the Java class name to use to recreate the exception.
     *
     * @return String name of the fully-qualified java class name
     */
    String getExceptionClassName();

    /**
     * Return the name of any nested exception that may indicate the root cause of the exception.
     *
     * @return exception class name
     */
    String getExceptionCausedBy();

    /**
     * Return the description of the action in progress when the exception occurred.
     *
     * @return string description
     */
    String getActionDescription();

    /**
     * Return the error message associated with the exception.
     *
     * @return string error message
     */
    String getExceptionErrorMessage();

    /**
     * Return the formal message identifier for the error message.  This is incorporated in the error message.
     * This is provided both for automated processing and to enable the error message to be reformatted
     * in a different language.
     *
     * @return string identifier
     */
    String getExceptionErrorMessageId();

    /**
     * Return the parameters that were inserted in the error message.
     * These are provided both for automated processing and to enable the error message to be reformatted
     * in a different language.
     *
     * @return list of strings
     */
    String[] getExceptionErrorMessageParameters();

    /**
     * Return the description of the action taken by the system as a result of the exception.
     *
     * @return - string description of the action taken
     */
    String getExceptionSystemAction();

    /**
     * Return the action that a user should take to resolve the problem.
     *
     * @return string instructions
     */
    String getExceptionUserAction();

    /**
     * Return the additional properties stored by the exceptions.
     *
     * @return property map
     */
    Map<String, Object> getExceptionProperties();

    /**
     * Set up the HTTP Code to use if forwarding response to HTTP client.
     *
     * @param relatedHTTPCode - integer HTTP status code
     */
    void setRelatedHTTPCode(int relatedHTTPCode);

    /**
     * Set up the name of the Java class name to use to recreate the exception.
     *
     * @param exceptionClassName - String name of the fully-qualified java class name
     */
    void setExceptionClassName(String exceptionClassName);

    /**
     * Set up the name of any nested exception that may indicate the root cause of the exception.
     *
     * @param exceptionCausedBy exception class name
     */
    void setExceptionCausedBy(String exceptionCausedBy);

    /**
     * Set up the description of the activity in progress when the exception occurred.
     *
     * @param actionDescription string description
     */
    void setActionDescription(String actionDescription);

    /**
     * Set up the error message associated with the exception.
     *
     * @param exceptionErrorMessage - string error message
     */
    void setExceptionErrorMessage(String exceptionErrorMessage);

    /**
     * Set up the formal message identifier for the error message.  This is incorporated in the error message.
     * This is provided both for automated processing and to enable the error message to be reformatted
     * in a different language.
     *
     * @param exceptionErrorMessageId string identifier
     */
    void setExceptionErrorMessageId(String exceptionErrorMessageId  );

    /**
     * Set up the list of parameters inserted in to the error message.
     * These are provided both for automated processing and to enable the error message to be reformatted
     * in a different language.
     *
     * @param exceptionErrorMessageParameters list of strings
     */
    void setExceptionErrorMessageParameters(String[] exceptionErrorMessageParameters);

    /**
     * Set up the description of the action taken by the system as a result of the exception.
     *
     * @param exceptionSystemAction - string description of the action taken
     */
    void setExceptionSystemAction(String exceptionSystemAction);

    /**
     * Set up the action that a user should take to resolve the problem.
     *
     * @param exceptionUserAction - string instructions
     */
    void setExceptionUserAction(String exceptionUserAction);

    /**
     * Set up the additional properties stored by the exceptions.
     *
     * @param exceptionProperties property map
     */
    void setExceptionProperties(Map<String, Object> exceptionProperties);
}
