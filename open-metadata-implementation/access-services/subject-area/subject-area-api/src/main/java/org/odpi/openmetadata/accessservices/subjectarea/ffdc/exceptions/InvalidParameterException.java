/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import java.io.Serial;
import java.util.Map;

/**
 * The InvalidParameterException is thrown by the Subject Area OMAS when a parameter is null or an invalid
 * value.
 */
public class InvalidParameterException extends SubjectAreaCheckedException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * invalid property name
     *
     * @return invalid property name
     */
    public String getInvalidPropertyName() {
        return invalidPropertyName;
    }

    /**
     * invalid property value
     *
     * @return in valid property value
     */
    public String getInvalidPropertyValue() {
        return invalidPropertyValue;
    }

    private String invalidPropertyName;
    private String invalidPropertyValue;
    private Map<String, Object> relatedProperties;

    /**
     * This is the typical constructor used for creating an InvalidParameterException
     *
     * @param messageDefinition    content of the message
     * @param className            name of class reporting error
     * @param actionDescription    description of function it was performing when error detected
     * @param invalidPropertyName  invalidPropertyName if there is one
     * @param invalidPropertyValue invalidPropertyValue if there is one
     */
    public InvalidParameterException(ExceptionMessageDefinition messageDefinition,
                                     String className,
                                     String actionDescription,
                                     String invalidPropertyName,
                                     String invalidPropertyValue) {
        super(messageDefinition, className, actionDescription);

        this.invalidPropertyName = invalidPropertyName;
        this.invalidPropertyValue = invalidPropertyValue;
    }


    /**
     * This is the constructor used for creating an InvalidParameterException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition    content of the message
     * @param className            name of class reporting error
     * @param actionDescription    description of function it was performing when error detected
     * @param caughtError          previous error causing this exception
     * @param invalidPropertyName  invalidPropertyName if there is one
     * @param invalidPropertyValue invalidPropertyValue if there is one
     */
    public InvalidParameterException(ExceptionMessageDefinition messageDefinition,
                                     String className,
                                     String actionDescription,
                                     Exception caughtError,
                                     String invalidPropertyName,
                                     String invalidPropertyValue) {
        super(messageDefinition, className, actionDescription, caughtError);
        this.invalidPropertyName = invalidPropertyName;
        this.invalidPropertyValue = invalidPropertyValue;
        messageDefinition.setMessageParameters(invalidPropertyName, invalidPropertyValue);
    }

    public Map<String, Object> getRelatedProperties() {
        return relatedProperties;
    }

    public void setRelatedProperties(Map<String, Object> relatedProperties) {
        this.relatedProperties = relatedProperties;
    }
}
