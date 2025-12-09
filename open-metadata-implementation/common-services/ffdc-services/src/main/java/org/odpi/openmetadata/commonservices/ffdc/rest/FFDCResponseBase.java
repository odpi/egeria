/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * FFDCResponseBase provides the base class for REST API responses.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BooleanResponse.class, name = "BooleanResponse"),
        @JsonSubTypes.Type(value = OCFConnectionResponse.class, name = "OCFConnectionResponse"),
        @JsonSubTypes.Type(value = OCFConnectorTypeResponse.class, name = "OCFConnectorTypeResponse"),
        @JsonSubTypes.Type(value = GUIDListResponse.class, name = "GUIDListResponse"),
        @JsonSubTypes.Type(value = GUIDResponse.class, name = "GUIDResponse"),
        @JsonSubTypes.Type(value = NameListResponse.class, name = "NameListResponse"),
        @JsonSubTypes.Type(value = PropertiesResponse.class, name = "PropertiesResponse"),
        @JsonSubTypes.Type(value = RegisteredOMAGServicesResponse.class, name = "RegisteredOMAGServicesResponse"),
        @JsonSubTypes.Type(value = StringResponse.class, name = "StringResponse"),
        @JsonSubTypes.Type(value = StringMapResponse.class, name = "StringMapResponse"),
        @JsonSubTypes.Type(value = VoidResponse.class, name = "VoidResponse")

})
public class FFDCResponseBase implements FFDCResponse
{
    private int                 relatedHTTPCode                 = 200;
    private String              exceptionClassName              = null;
    private String              exceptionSubclassName           = null;
    private String              exceptionCausedBy               = null;
    private String              actionDescription               = null;
    private String              exceptionErrorMessage           = null;
    private String              exceptionErrorMessageId         = null;
    private String[]            exceptionErrorMessageParameters = null;
    private String              exceptionSystemAction           = null;
    private String              exceptionUserAction             = null;
    private Map<String, Object> exceptionProperties             = null;


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
            this.exceptionSubclassName = template.getExceptionSubclassName();
            this.exceptionCausedBy = template.getExceptionCausedBy();
            this.actionDescription = template.getActionDescription();
            this.exceptionErrorMessage = template.getExceptionErrorMessage();
            this.exceptionErrorMessageId = template.getExceptionErrorMessageId();
            this.exceptionErrorMessageParameters = template.getExceptionErrorMessageParameters();
            this.exceptionSystemAction = template.getExceptionSystemAction();
            this.exceptionUserAction = template.getExceptionUserAction();
            this.exceptionProperties = template.getExceptionProperties();
        }
    }


    /**
     * Return the name of the Java class name to use to recreate the exception.
     *
     * @return String name of the fully-qualified java class name
     */
    @Override
    public String getExceptionClassName()
    {
        return exceptionClassName;
    }


    /**
     * Set up the name of the Java class name to use to recreate the exception.
     *
     * @param exceptionClassName - String name of the fully-qualified java class name
     */
    @Override
    public void setExceptionClassName(String exceptionClassName)
    {
        this.exceptionClassName = exceptionClassName;
    }


    /**
     * Return the detailed class name create by the originator.
     *
     * @return class name
     */
    @Override
    public String getExceptionSubclassName()
    {
        return exceptionSubclassName;
    }


    /**
     * Set up the detailed class name create by the originator.
     *
     * @param exceptionSubclassName name of class
     */
    @Override
    public void setExceptionSubclassName(String exceptionSubclassName)
    {
        this.exceptionSubclassName = exceptionSubclassName;
    }


    /**
     * Return the name of any nested exception that may indicate the root cause of the exception.
     *
     * @return exception class name
     */
    @Override
    public String getExceptionCausedBy()
    {
        return exceptionCausedBy;
    }


    /**
     * Set up the name of any nested exception that may indicate the root cause of the exception.
     *
     * @param exceptionCausedBy exception class name
     */
    @Override
    public void setExceptionCausedBy(String exceptionCausedBy)
    {
        this.exceptionCausedBy = exceptionCausedBy;
    }


    /**
     * Return the description of the action in progress when the exception occurred.
     *
     * @return string description
     */
    @Override
    public String getActionDescription()
    {
        return actionDescription;
    }


    /**
     * Set up the description of the activity in progress when the exception occurred.
     *
     * @param actionDescription string description
     */
    @Override
    public void setActionDescription(String actionDescription)
    {
        this.actionDescription = actionDescription;
    }


    /**
     * Return the HTTP Code to use if forwarding response to HTTP client.
     *
     * @return integer HTTP status code
     */
    @Override
    public int getRelatedHTTPCode()
    {
        return relatedHTTPCode;
    }


    /**
     * Set up the HTTP Code to use if forwarding response to HTTP client.
     *
     * @param relatedHTTPCode - integer HTTP status code
     */
    @Override
    public void setRelatedHTTPCode(int relatedHTTPCode)
    {
        this.relatedHTTPCode = relatedHTTPCode;
    }


    /**
     * Return the error message associated with the exception.
     *
     * @return string error message
     */
    @Override
    public String getExceptionErrorMessage()
    {
        return exceptionErrorMessage;
    }


    /**
     * Set up the error message associated with the exception.
     *
     * @param exceptionErrorMessage - string error message
     */
    @Override
    public void setExceptionErrorMessage(String exceptionErrorMessage)
    {
        this.exceptionErrorMessage = exceptionErrorMessage;
    }


    /**
     * Return the formal message identifier for the error message.  This is incorporated in the error message.
     * This is provided both for automated processing and to enable the error message to be reformatted
     * in a different language.
     *
     * @return string identifier
     */
    @Override
    public String getExceptionErrorMessageId()
    {
        return exceptionErrorMessageId;
    }


    /**
     * Set up the formal message identifier for the error message.  This is incorporated in the error message.
     * This is provided both for automated processing and to enable the error message to be reformatted
     * in a different language.
     *
     * @param exceptionErrorMessageId string identifier
     */
    @Override
    public void setExceptionErrorMessageId(String exceptionErrorMessageId)
    {
        this.exceptionErrorMessageId = exceptionErrorMessageId;
    }


    /**
     * Return the parameters that were inserted in the error message.
     * These are provided both for automated processing and to enable the error message to be reformatted
     * in a different language.
     *
     * @return array of strings
     */
    @Override
    public String[] getExceptionErrorMessageParameters()
    {
        return exceptionErrorMessageParameters;
    }


    /**
     * Set up the list of parameters inserted in to the error message.
     * These are provided both for automated processing and to enable the error message to be reformatted
     * in a different language.
     *
     * @param exceptionErrorMessageParameters list of strings
     */
    @Override
    public void setExceptionErrorMessageParameters(String[] exceptionErrorMessageParameters)
    {
        this.exceptionErrorMessageParameters = exceptionErrorMessageParameters;
    }


    /**
     * Return the description of the action taken by the system as a result of the exception.
     *
     * @return - string description of the action taken
     */
    @Override
    public String getExceptionSystemAction()
    {
        return exceptionSystemAction;
    }


    /**
     * Set up the description of the action taken by the system as a result of the exception.
     *
     * @param exceptionSystemAction - string description of the action taken
     */
    @Override
    public void setExceptionSystemAction(String exceptionSystemAction)
    {
        this.exceptionSystemAction = exceptionSystemAction;
    }


    /**
     * Return the action that a user should take to resolve the problem.
     *
     * @return string instructions
     */
    @Override
    public String getExceptionUserAction()
    {
        return exceptionUserAction;
    }


    /**
     * Set up the action that a user should take to resolve the problem.
     *
     * @param exceptionUserAction - string instructions
     */
    @Override
    public void setExceptionUserAction(String exceptionUserAction)
    {
        this.exceptionUserAction = exceptionUserAction;
    }


    /**
     * Return the additional properties stored by the exceptions.
     *
     * @return property map
     */
    @Override
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
    @Override
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
                ", exceptionSubclassName='" + exceptionSubclassName + '\'' +
                ", exceptionCausedBy='" + exceptionCausedBy + '\'' +
                ", actionDescription='" + actionDescription + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionErrorMessageId='" + exceptionErrorMessageId + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(exceptionErrorMessageParameters) +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        FFDCResponseBase that = (FFDCResponseBase) objectToCompare;
        return relatedHTTPCode == that.relatedHTTPCode &&
                Objects.equals(exceptionClassName, that.exceptionClassName) &&
                Objects.equals(exceptionSubclassName, that.exceptionSubclassName) &&
                Objects.equals(actionDescription, that.actionDescription) &&
                Objects.equals(exceptionErrorMessage, that.exceptionErrorMessage) &&
                Objects.equals(exceptionErrorMessageId, that.exceptionErrorMessageId) &&
                Arrays.equals(exceptionErrorMessageParameters, that.exceptionErrorMessageParameters) &&
                Objects.equals(exceptionSystemAction, that.exceptionSystemAction) &&
                Objects.equals(exceptionUserAction, that.exceptionUserAction) &&
                Objects.equals(exceptionProperties, that.exceptionProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        int result = Objects.hash(relatedHTTPCode, exceptionClassName, exceptionSubclassName, exceptionCausedBy, actionDescription, exceptionErrorMessage, exceptionErrorMessageId, exceptionSystemAction, exceptionUserAction, exceptionProperties);
        result = 31 * result + Arrays.hashCode(exceptionErrorMessageParameters);
        return result;
    }
}
