/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *InvalidParameterExceptionResponse is the response that wraps a InvalidParameterException
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InvalidParameterExceptionResponse extends SubjectAreaOMASAPIResponse
{
    protected String    invalidPropertyName = null;
    protected String    invalidPropertyValue = null;

    public InvalidParameterExceptionResponse(SubjectAreaCheckedException e)
    {
        super(e);
        this.setResponseCategory(ResponseCategory.InvalidParameterException);
        InvalidParameterException ipe = (InvalidParameterException)e;
        this.invalidPropertyName = ipe.getInvalidPropertyName();
        this.invalidPropertyValue = ipe.getInvalidPropertyValue();
    }

    @Override
    public String toString()
    {
        return "InvalidParameterExceptionResponse{" +
                super.toString() +
                ", invalidPropertyName='" + invalidPropertyName + '\'' +
                ", invalidPropertyValue='" + invalidPropertyValue +
                '}';
    }

    /**
     * Get the invalid property name
     *
     * @return the invalid property name
     */
    public String getInvalidPropertyName() {
        return invalidPropertyName;
    }
    /**
     * Set the invalid property name
     *
     * @param  invalidPropertyName String the invalid property name
     */
    public void setInvalidPropertyName(String invalidPropertyName) {
        this.invalidPropertyName = invalidPropertyName;
    }
    /**
     * Get the invalid property value
     *
     * @return the invalid property value
     */
    public String getInvalidPropertyValue() {
        return invalidPropertyValue;
    }
    /**
     * Set the invalid property value
     *
     * @param  invalidPropertyValue String the invalid property value
     */
    public void setInvalidPropertyValue(String invalidPropertyValue) {
        this.invalidPropertyValue = invalidPropertyValue;
    }
}
