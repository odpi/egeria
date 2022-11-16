/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.auditlog;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AuditLogReportingComponent describes the component issuing the audit log record.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AuditLogReportingComponent implements Serializable, ComponentDescription
{
    private static final long    serialVersionUID = 1L;

    private  int                        componentId;
    private  ComponentDevelopmentStatus componentDevelopmentStatus = ComponentDevelopmentStatus.IN_DEVELOPMENT;
    private  String                     componentName;
    private  String                     componentDescription;
    private  String                     componentWikiURL;


    /**
     * Default constructor for Jackson
     */
    public AuditLogReportingComponent()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AuditLogReportingComponent(ComponentDescription template)
    {
        if (template != null)
        {
            this.componentId = template.getComponentId();
            this.componentDevelopmentStatus = template.getComponentDevelopmentStatus();
            this.componentName = template.getComponentName();
            this.componentDescription = template.getComponentDescription();
            this.componentWikiURL = template.getComponentWikiURL();
        }
    }


    /**
     * Construct the description of the reporting component.
     *
     * @param componentId  numerical identifier for the component
     * @param componentDevelopmentStatus development status
     * @param componentName  display name for the component
     * @param componentDescription  description of the component
     * @param componentWikiURL  link to more information
     */
    public AuditLogReportingComponent(int                        componentId,
                                      ComponentDevelopmentStatus componentDevelopmentStatus,
                                      String                     componentName,
                                      String                     componentDescription,
                                      String                     componentWikiURL)
    {
        this.componentId = componentId;
        this.componentDevelopmentStatus = componentDevelopmentStatus;
        this.componentName = componentName;
        this.componentDescription = componentDescription;
        this.componentWikiURL = componentWikiURL;
    }


    /**
     * Return the numerical code for this component.
     *
     * @return int componentId
     */
    @Override
    public int getComponentId()
    {
        return componentId;
    }


    /**
     * Set up the numerical code for this component.
     *
     * @param componentId int componentId
     */
    public void setComponentId(int componentId)
    {
        this.componentId = componentId;
    }



    /**
     * Return the development status of the component.
     *
     * @return enum describing the status
     */
    @Override
    public ComponentDevelopmentStatus getComponentDevelopmentStatus()
    {
        return componentDevelopmentStatus;
    }


    /**
     * Set up the development status of the component.
     *
     * @param componentDevelopmentStatus enum describing the status
     */
    public void setComponentDevelopmentStatus(ComponentDevelopmentStatus componentDevelopmentStatus)
    {
        this.componentDevelopmentStatus = componentDevelopmentStatus;
    }


    /**
     * Return the name of the component.
     *
     * @return String component name
     */
    @Override
    public String getComponentName()
    {
        return componentName;
    }


    /**
     * Set up the name of the component.
     *
     * @param componentName String component name
     */
    public void setComponentName(String componentName)
    {
        this.componentName = componentName;
    }



    /**
     * Return the short description of the component. This is an English description.  Natural language support for
     * these values can be added to UIs using a resource bundle indexed with the component id.  This value is
     * provided as a default if the resource bundle is not available.
     *
     * @return String description
     */
    @Override
    public String getComponentDescription()
    {
        return componentDescription;
    }


    /**
     * Set up the short description of the component. This is an English description.  Natural language support for
     * these values can be added to UIs using a resource bundle indexed with the component id.  This value is
     * provided as a default if the resource bundle is not available.
     *
     * @param componentDescription String description
     */
    public void setComponentDescription(String componentDescription)
    {
        this.componentDescription = componentDescription;
    }


    /**
     * Return the URL link to the wiki page that describes this component.  This provides more information to the log reader
     * on the operation of the component.
     *
     * @return String URL
     */
    @Override
    public String getComponentWikiURL()
    {
        return componentWikiURL;
    }


    /**
     * Set up the URL link to the wiki page that describes this component.  This provides more information to the log reader
     * on the operation of the component.
     *
     * @param componentWikiURL string URL
     */
    public void setComponentWikiURL(String componentWikiURL)
    {
        this.componentWikiURL = componentWikiURL;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "AuditLogReportingComponent{" +
                "componentId=" + componentId +
                ", componentName='" + componentName + '\'' +
                ", componentDescription='" + componentDescription + '\'' +
                ", componentWikiURL='" + componentWikiURL + '\'' +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        AuditLogReportingComponent that = (AuditLogReportingComponent) objectToCompare;
        return getComponentId() == that.getComponentId() &&
                Objects.equals(getComponentName(), that.getComponentName()) &&
                Objects.equals(getComponentDescription(), that.getComponentDescription()) &&
                Objects.equals(getComponentWikiURL(), that.getComponentWikiURL());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getComponentId(), getComponentName(), getComponentDescription(), getComponentWikiURL());
    }
}
