/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataEngineOMASAPIResponse provides a common header for Data Engine OMAS managed rest to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = GUIDResponse.class, name = "GUIDResponse"),
        })
public abstract class DataEngineOMASAPIResponse implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int relatedHTTPCode = 200;
    private String exceptionClassName = null;
    private String exceptionErrorMessage = null;
    private String exceptionSystemAction = null;
    private String exceptionUserAction = null;
    private Map<String, Object> exceptionProperties = null;

    /**
     * Default constructor
     */
    DataEngineOMASAPIResponse() {
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    DataEngineOMASAPIResponse(DataEngineOMASAPIResponse template) {
        if (template != null) {
            this.relatedHTTPCode = template.getRelatedHTTPCode();
            this.exceptionClassName = template.getExceptionClassName();
            this.exceptionErrorMessage = template.getExceptionErrorMessage();
            this.exceptionSystemAction = template.getExceptionSystemAction();
            this.exceptionUserAction = template.getExceptionUserAction();
            this.exceptionProperties = template.getExceptionProperties();
        }
    }

    public int getRelatedHTTPCode() {
        return relatedHTTPCode;
    }

    public String getExceptionClassName() {
        return exceptionClassName;
    }

    public String getExceptionErrorMessage() {
        return exceptionErrorMessage;
    }

    public String getExceptionSystemAction() {
        return exceptionSystemAction;
    }

    public String getExceptionUserAction() {
        return exceptionUserAction;
    }

    public Map<String, Object> getExceptionProperties() {
        return exceptionProperties;
    }
    public void setRelatedHTTPCode(int relatedHTTPCode) {
        this.relatedHTTPCode = relatedHTTPCode;
    }

    public void setExceptionClassName(String exceptionClassName) {
        this.exceptionClassName = exceptionClassName;
    }

    public void setExceptionErrorMessage(String exceptionErrorMessage) {
        this.exceptionErrorMessage = exceptionErrorMessage;
    }

    public void setExceptionSystemAction(String exceptionSystemAction) {
        this.exceptionSystemAction = exceptionSystemAction;
    }

    public void setExceptionUserAction(String exceptionUserAction) {
        this.exceptionUserAction = exceptionUserAction;
    }

    public void setExceptionProperties(Map<String, Object> exceptionProperties) {
        this.exceptionProperties = exceptionProperties;
    }

    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */

    @Override
    public String toString()
    {
        return "DataEngineOMASAPIResponse{" +
                "relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                ", exceptionProperties=" + exceptionProperties +
                '}';
    }

    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DataEngineOMASAPIResponse that = (DataEngineOMASAPIResponse) objectToCompare;

        return getRelatedHTTPCode() == that.getRelatedHTTPCode() &&
                Objects.equals(getExceptionClassName(), that.getExceptionClassName()) &&
                Objects.equals(getExceptionErrorMessage(), that.getExceptionErrorMessage()) &&
                Objects.equals(getExceptionSystemAction(), that.getExceptionSystemAction()) &&
                Objects.equals(getExceptionUserAction(), that.getExceptionUserAction()) &&
                Objects.equals(getExceptionProperties(), that.getExceptionProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getRelatedHTTPCode(),
                getExceptionClassName(),
                getExceptionErrorMessage(),
                getExceptionSystemAction(),
                getExceptionUserAction(),
                getExceptionProperties());
    }
}
