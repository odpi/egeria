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
        @JsonSubTypes.Type(value = ConnectionResponse.class, name = "ConnectionResponse"),
        @JsonSubTypes.Type(value = ConnectorTypeResponse.class, name = "ConnectorTypeResponse"),
        @JsonSubTypes.Type(value = GUIDListResponse.class, name = "GUIDListResponse"),
        @JsonSubTypes.Type(value = GUIDResponse.class, name = "GUIDResponse"),
        @JsonSubTypes.Type(value = NameListResponse.class, name = "NameListResponse"),
        @JsonSubTypes.Type(value = PropertiesResponse.class, name = "PropertiesResponse"),
        @JsonSubTypes.Type(value = RegisteredOMAGServicesResponse.class, name = "RegisteredOMAGServicesResponse"),
        @JsonSubTypes.Type(value = StringResponse.class, name = "StringResponse"),
        @JsonSubTypes.Type(value = StringMapResponse.class, name = "StringMapResponse"),
        @JsonSubTypes.Type(value = VoidResponse.class, name = "VoidResponse")

})
public class FFDCResponseBase implements java.io.Serializable, FFDCResponse
{
    private static final long    serialVersionUID = 1L;

    private int                 relatedHTTPCode                 = 200;
    private String              exceptionClassName              = null;
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

    @Override
    public String getExceptionClassName()
    {
        return exceptionClassName;
    }

    @Override
    public void setExceptionClassName(String exceptionClassName)
    {
        this.exceptionClassName = exceptionClassName;
    }

    @Override
    public String getExceptionCausedBy()
    {
        return exceptionCausedBy;
    }

    @Override
    public void setExceptionCausedBy(String exceptionCausedBy)
    {
        this.exceptionCausedBy = exceptionCausedBy;
    }

    @Override
    public String getActionDescription()
    {
        return actionDescription;
    }

    @Override
    public void setActionDescription(String actionDescription)
    {
        this.actionDescription = actionDescription;
    }

    @Override
    public int getRelatedHTTPCode()
    {
        return relatedHTTPCode;
    }

    @Override
    public void setRelatedHTTPCode(int relatedHTTPCode)
    {
        this.relatedHTTPCode = relatedHTTPCode;
    }

    @Override
    public String getExceptionErrorMessage()
    {
        return exceptionErrorMessage;
    }

    @Override
    public void setExceptionErrorMessage(String exceptionErrorMessage)
    {
        this.exceptionErrorMessage = exceptionErrorMessage;
    }

    @Override
    public String getExceptionErrorMessageId()
    {
        return exceptionErrorMessageId;
    }

    @Override
    public void setExceptionErrorMessageId(String exceptionErrorMessageId)
    {
        this.exceptionErrorMessageId = exceptionErrorMessageId;
    }

    @Override
    public String[] getExceptionErrorMessageParameters()
    {
        return exceptionErrorMessageParameters;
    }

    @Override
    public void setExceptionErrorMessageParameters(String[] exceptionErrorMessageParameters)
    {
        this.exceptionErrorMessageParameters = exceptionErrorMessageParameters;
    }

    @Override
    public String getExceptionSystemAction()
    {
        return exceptionSystemAction;
    }

    @Override
    public void setExceptionSystemAction(String exceptionSystemAction)
    {
        this.exceptionSystemAction = exceptionSystemAction;
    }

    @Override
    public String getExceptionUserAction()
    {
        return exceptionUserAction;
    }

    @Override
    public void setExceptionUserAction(String exceptionUserAction)
    {
        this.exceptionUserAction = exceptionUserAction;
    }

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
        int result = Objects.hash(relatedHTTPCode, exceptionClassName, actionDescription, exceptionErrorMessage, exceptionErrorMessageId,
                                  exceptionSystemAction, exceptionUserAction, exceptionProperties);
        result = 31 * result + Arrays.hashCode(exceptionErrorMessageParameters);
        return result;
    }
}
