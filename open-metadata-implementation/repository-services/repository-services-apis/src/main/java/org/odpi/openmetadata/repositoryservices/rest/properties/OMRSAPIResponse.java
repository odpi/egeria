/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSAPIResponse provides a common header for OMRS managed rest to the OMRS REST API.   It manages
 * information about OMRS exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = OMRSAPIPagedResponse.class, name = "OMRSRESTAPIPagedResponse"),
                @JsonSubTypes.Type(value = AttributeTypeDefListResponse.class, name = "AttributeTypeDefListResponse"),
                @JsonSubTypes.Type(value = AttributeTypeDefResponse.class, name = "AttributeTypeDefResponse"),
                @JsonSubTypes.Type(value = AuditLogReportResponse.class, name = "AuditLogReportResponse"),
                @JsonSubTypes.Type(value = AuditLogSeveritiesResponse.class, name = "AuditLogSeveritiesResponse"),
                @JsonSubTypes.Type(value = BooleanResponse.class, name = "BooleanResponse"),
                @JsonSubTypes.Type(value = CohortMembershipResponse.class, name = "CohortMembershipResponse"),
                @JsonSubTypes.Type(value = EntityDetailResponse.class, name = "EntityDetailResponse"),
                @JsonSubTypes.Type(value = EntitySummaryResponse.class, name = "EntitySummaryResponse"),
                @JsonSubTypes.Type(value = InstanceGraphResponse.class, name = "InstanceGraphResponse"),
                @JsonSubTypes.Type(value = RelationshipListResponse.class, name = "RelationshipListResponse"),
                @JsonSubTypes.Type(value = RelationshipResponse.class, name = "RelationshipResponse"),
                @JsonSubTypes.Type(value = TypeDefGalleryResponse.class, name = "TypeDefGalleryResponse"),
                @JsonSubTypes.Type(value = TypeDefListResponse.class, name = "TypeDefListResponse"),
                @JsonSubTypes.Type(value = TypeDefResponse.class, name = "TypeDefResponse"),
                @JsonSubTypes.Type(value = VoidResponse.class, name = "VoidResponse")
        })
public abstract class OMRSAPIResponse
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
    public OMRSAPIResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OMRSAPIResponse(OMRSAPIResponse template)
    {
        if (template != null)
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
     * Return the detailed class name create by the originator.
     *
     * @return class name
     */
    public String getExceptionSubclassName()
    {
        return exceptionSubclassName;
    }


    /**
     * Set up the detailed class name create by the originator.
     *
     * @param exceptionSubclassName name of class
     */
    public void setExceptionSubclassName(String exceptionSubclassName)
    {
        this.exceptionSubclassName = exceptionSubclassName;
    }


    /**
     * Return the name of any nested exception that may indicate the root cause of the exception.
     *
     * @return exception class name
     */
    public String getExceptionCausedBy()
    {
        return exceptionCausedBy;
    }


    /**
     * Set up the name of any nested exception that may indicate the root cause of the exception.
     *
     * @param exceptionCausedBy exception class name
     */
    public void setExceptionCausedBy(String exceptionCausedBy)
    {
        this.exceptionCausedBy = exceptionCausedBy;
    }

    /**
     * Return the description of the activity in progress when the exception occurred.
     *
     * @return string description
     */
    public String getActionDescription()
    {
        return actionDescription;
    }


    /**
     * Set up the description of the activity in progress when the exception occurred.
     *
     * @param actionDescription string description
     */
    public void setActionDescription(String actionDescription)
    {
        this.actionDescription = actionDescription;
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
     * Return the formal message identifier for the error message.  This is incorporated in the error message.
     * This is provided both for automated processing and to enable the error message to be reformatted
     * in a different language.
     *
     * @return string identifier
     */
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
    public void setExceptionErrorMessageParameters(String[] exceptionErrorMessageParameters)
    {
        this.exceptionErrorMessageParameters = exceptionErrorMessageParameters;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OMRSAPIResponse{" +
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
        if (!(objectToCompare instanceof OMRSAPIResponse that))
        {
            return false;
        }
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
     * Create a hash code for this element type.
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
