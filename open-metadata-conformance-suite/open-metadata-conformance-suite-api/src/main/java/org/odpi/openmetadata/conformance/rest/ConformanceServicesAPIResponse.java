/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.conformance.rest;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConformanceServicesAPIResponse provides a common header for admin services managed response to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes
        ({
                @JsonSubTypes.Type(value = TestLabReportResponse.class, name = "TestLabReportResponse"),
                @JsonSubTypes.Type(value = TestLabSummaryResponse.class, name = "TestLabSummaryResponse"),
                @JsonSubTypes.Type(value = TestCaseReportResponse.class, name = "TestCaseReportResponse"),
                @JsonSubTypes.Type(value = ProfileNameListResponse.class, name = "ProfileNameListResponse"),
                @JsonSubTypes.Type(value = ProfileReportResponse.class, name = "ProfileReportResponse"),
                @JsonSubTypes.Type(value = TestCaseListResponse.class, name = "TestCaseListResponse"),
                @JsonSubTypes.Type(value = TestCaseListReportResponse.class, name = "TestCaseListReportResponse"),
                @JsonSubTypes.Type(value = WorkbenchReportResponse.class, name = "WorkbenchReportResponse"),
                @JsonSubTypes.Type(value = VoidResponse.class, name = "VoidResponse")
        })
public abstract class ConformanceServicesAPIResponse implements java.io.Serializable
{
    private static final long    serialVersionUID = 1L;

    private int                  relatedHTTPCode = 200;
    private String               successMessage = null;
    private String               exceptionClassName = null;
    private String               exceptionErrorMessage = null;
    private String               exceptionSystemAction = null;
    private String               exceptionUserAction = null;
    private Map<String, Object>  exceptionProperties = null;

    /**
     * Default constructor
     */
    public ConformanceServicesAPIResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConformanceServicesAPIResponse(ConformanceServicesAPIResponse template)
    {
        if (template !=null)
        {
            this.relatedHTTPCode = template.getRelatedHTTPCode();
            this.exceptionClassName = template.getExceptionClassName();
            this.exceptionErrorMessage = template.getExceptionErrorMessage();
            this.exceptionSystemAction = template.getExceptionSystemAction();
            this.exceptionUserAction = template.getExceptionUserAction();
            this.exceptionProperties = template.getExceptionProperties();
        }
    }


    /**
     * Return the HTTP Code to use if forwarding response to HTTP client.
     *
     * @return integer HTTP status code
     */
    public int getRelatedHTTPCode()
    {
        return relatedHTTPCode;
    }


    /**
     * Set up the HTTP Code to use if forwarding response to HTTP client.
     *
     * @param relatedHTTPCode - integer HTTP status code
     */
    public void setRelatedHTTPCode(int relatedHTTPCode)
    {
        this.relatedHTTPCode = relatedHTTPCode;
    }


    /**
     * Return the success message (if any).
     *
     * @return string or null
     */
    public String getSuccessMessage()
    {
        return successMessage;
    }


    /**
     * Set up the success message.  This provides supplementary information about the services that
     * have been changed.
     *
     * @param successMessage string or null
     */
    public void setSuccessMessage(String successMessage)
    {
        this.successMessage = successMessage;
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


    /**
     * Return the additional properties stored by the exceptions.
     *
     * @return property map
     */
    public Map<String, Object> getExceptionProperties()
    {
        if (exceptionProperties == null)
        {
            return null;
        }
        else if (exceptionProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(exceptionProperties);
        }
    }


    /**
     * Set up the additional properties stored by the exceptions.
     *
     * @param exceptionProperties property map
     */
    public void setExceptionProperties(Map<String, Object> exceptionProperties)
    {
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
        return "ConformanceServicesAPIResponse{" +
                "relatedHTTPCode=" + relatedHTTPCode +
                ", successMessage='" + successMessage + '\'' +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                ", exceptionProperties=" + exceptionProperties +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof ConformanceServicesAPIResponse))
        {
            return false;
        }
        ConformanceServicesAPIResponse that = (ConformanceServicesAPIResponse) objectToCompare;
        return getRelatedHTTPCode() == that.getRelatedHTTPCode() &&
                Objects.equals(getSuccessMessage(), that.getSuccessMessage()) &&
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
