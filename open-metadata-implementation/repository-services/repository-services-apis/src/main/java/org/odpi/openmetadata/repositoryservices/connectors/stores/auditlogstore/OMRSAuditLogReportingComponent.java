/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSAuditLogReportingComponent describes the component issuing the audit log record.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSAuditLogReportingComponent implements Serializable, ComponentDescription
{
    private static final long    serialVersionUID = 1L;

    private  int                        componentId = 0;
    private  ComponentDevelopmentStatus componentDevelopmentStatus = ComponentDevelopmentStatus.IN_DEVELOPMENT;
    private  String                     componentName = null;
    private  String                     componentDescription = null;
    private  String                     componentWikiURL = null;


    /**
     * Construct the description of the reporting component.
     *
     * @param componentId  numerical identifier for the component.
     * @param componentDevelopmentStatus status of the component's implementation
     * @param componentName  display name for the component.
     * @param componentDescription  description of the component.
     * @param componentWikiURL  link to more information.
     */
    public OMRSAuditLogReportingComponent(int                        componentId,
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
     * Construct the description of the reporting component.
     *
     * @param template  object to copy.
     */
    public OMRSAuditLogReportingComponent(ComponentDescription template)
    {
        if (template != null)
        {
            this.componentId                = template.getComponentId();
            this.componentDevelopmentStatus = template.getComponentDevelopmentStatus();
            this.componentName              = template.getComponentName();
            this.componentDescription       = template.getComponentDescription();
            this.componentWikiURL           = template.getComponentWikiURL();
        }
    }



    /**
     * Return the numerical code for this component.
     *
     * @return int componentId
     */
    public int getComponentId()
    {
        return componentId;
    }


    /**
     * Return the development status of the component.
     *
     * @return enum describing the status
     */
    public ComponentDevelopmentStatus getComponentDevelopmentStatus()
    {
        return componentDevelopmentStatus;
    }


    /**
     * Return the name of the component.  This is the name used in the audit log records.
     *
     * @return String component name
     */
    public String getComponentName()
    {
        return componentName;
    }


    /**
     * Return the short description of the component. This is an English description.  Natural language support for
     * these values can be added to UIs using a resource bundle indexed with the component id.  This value is
     * provided as a default if the resource bundle is not available.
     *
     * @return String description
     */
    public String getComponentDescription()
    {
        return componentDescription;
    }


    /**
     * URL to the wiki page that describes this component.  This provides more information to the log reader
     * on the operation of the component.
     *
     * @return String URL
     */
    public String getComponentWikiURL()
    {
        return componentWikiURL;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OMRSAuditLogReportingComponent{" +
                       "componentId=" + componentId +
                       ", componentDevelopmentStatus=" + componentDevelopmentStatus +
                       ", componentName='" + componentName + '\'' +
                       ", componentDescription='" + componentDescription + '\'' +
                       ", componentWikiURL='" + componentWikiURL + '\'' +
                       ", componentType='" + getComponentDescription() + '\'' +
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
        OMRSAuditLogReportingComponent that = (OMRSAuditLogReportingComponent) objectToCompare;
        return componentId == that.componentId &&
                       componentDevelopmentStatus == that.componentDevelopmentStatus &&
                       Objects.equals(componentName, that.componentName) &&
                       Objects.equals(componentDescription, that.componentDescription) &&
                       Objects.equals(componentWikiURL, that.componentWikiURL);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getComponentId(), getComponentDevelopmentStatus(), getComponentName(), getComponentDescription(), getComponentWikiURL());
    }
}
