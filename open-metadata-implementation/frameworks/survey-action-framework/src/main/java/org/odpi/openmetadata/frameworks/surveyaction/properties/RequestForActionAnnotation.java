/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RequestForActionAnnotation is used to record an issue that has been discovered.  It is typically used when the
 * survey action service is running quality rules and data values are discovered that are not correct.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequestForActionAnnotation extends DataFieldAnnotation
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String              surveyActivity  = null;
    private String              actionRequested = null;
    private Map<String, String> actionProperties  = null;


    /**
     * Default constructor
     */
    public RequestForActionAnnotation()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RequestForActionAnnotation(RequestForActionAnnotation template)
    {
        super(template);

        if (template != null)
        {
            surveyActivity  = template.getSurveyActivity();
            actionRequested = template.getActionRequested();
            actionProperties  = template.getActionProperties();
        }
    }


    /**
     * Return the unique name of the discovery activity.  It should be possible for the stewardship processes to know what
     * discovery processing was running for this value.
     *
     * @return string name
     */
    public String getSurveyActivity()
    {
        return surveyActivity;
    }


    /**
     * Set up the unique name of the discovery activity.  It should be possible for the stewardship processes to know what
     * discovery processing was running for this value.
     *
     * @param surveyActivity string name
     */
    public void setSurveyActivity(String surveyActivity)
    {
        this.surveyActivity = surveyActivity;
    }


    /**
     * Return the identifier of the type of action that needs to be run. It should be possible for the stewardship processes to know what
     * to run from this value.
     *
     * @return string name
     */
    public String getActionRequested()
    {
        return actionRequested;
    }


    /**
     * Set up the identifier of the type of action that needs to be run. It should be possible for the stewardship processes to know what
     * to run from this value.
     *
     * @param actionRequested string name
     */
    public void setActionRequested(String actionRequested)
    {
        this.actionRequested = actionRequested;
    }


    /**
     * Return the properties that will guide the governance action.
     *
     * @return map of property names to property values
     */
    public Map<String, String> getActionProperties()
    {
        if (actionProperties == null)
        {
            return null;
        }
        else if (actionProperties.isEmpty())
        {
            return null;
        }

        return actionProperties;
    }


    /**
     * Set up the properties that will guide the remediation action.
     *
     * @param actionProperties map of property names to property values
     */
    public void setActionProperties(Map<String, String> actionProperties)
    {
        this.actionProperties = actionProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RequestForActionAnnotation{" +
                "surveyActivity='" + surveyActivity + '\'' +
                ", actionRequested='" + actionRequested + '\'' +
                ", actionProperties=" + actionProperties +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        RequestForActionAnnotation that = (RequestForActionAnnotation) objectToCompare;
        return Objects.equals(surveyActivity, that.surveyActivity) &&
                Objects.equals(actionRequested, that.actionRequested) &&
                Objects.equals(actionProperties, that.actionProperties);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), surveyActivity, actionRequested, actionProperties);
    }
}
