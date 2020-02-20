/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BooleanResponse.class, name = "BooleanResponse"),
        @JsonSubTypes.Type(value = CountResponse.class, name = "CountResponse"),
        @JsonSubTypes.Type(value = GUIDListResponse.class, name = "GUIDListResponse"),
        @JsonSubTypes.Type(value = GUIDResponse.class, name = "GUIDResponse"),
        @JsonSubTypes.Type(value = NameListResponse.class, name = "NameListResponse"),
        @JsonSubTypes.Type(value = RegisteredOMAGServicesResponse.class, name = "RegisteredOMAGServicesResponse"),
        @JsonSubTypes.Type(value = VoidResponse.class, name = "VoidResponse")

})
public class FFDCResponseBase implements java.io.Serializable
{
    private static final long    serialVersionUID = 1L;

    private int                 relatedHTTPCode       = 200;
    private String              exceptionClassName    = null;
    private String              exceptionErrorMessage = null;
    private String              exceptionSystemAction = null;
    private String              exceptionUserAction   = null;
    private Map<String, Object> exceptionProperties   = null;

    /**
     * Default constructor
     */
    public FFDCResponseBase()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FFDCResponseBase(FFDCResponseBase  template)
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
        return "FFDCResponseBase{" +
                "relatedHTTPCode=" + relatedHTTPCode +
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
        if (!(objectToCompare instanceof FFDCResponseBase))
        {
            return false;
        }
        FFDCResponseBase that = (FFDCResponseBase) objectToCompare;
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
