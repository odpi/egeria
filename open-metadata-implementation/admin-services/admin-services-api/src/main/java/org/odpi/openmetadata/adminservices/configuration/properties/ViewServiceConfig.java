/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceRegistration;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ViewServiceConfig provides the configuration for a single Open Metadata View Service (OMVS).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SolutionViewServiceConfig.class, name = "SolutionViewServiceConfig"),
        @JsonSubTypes.Type(value = IntegrationViewServiceConfig.class, name = "IntegrationViewServiceConfig")
})
public class ViewServiceConfig extends OMAGServerClientConfig
{
    private static final long    serialVersionUID = 1L;

    private int                            viewServiceId                = 0;
    private String                         viewServiceAdminClass        = null;
    private String                         viewServiceName              = null;
    private String                         viewServiceFullName          = null;
    private String                         viewServiceURLMarker         = null;
    private String                         viewServiceDescription       = null;
    private String                         viewServiceWiki              = null;
    private ServiceOperationalStatus       viewServiceOperationalStatus = null;
    private Map<String, Object>            viewServiceOptions           = null;


    /**
     * Default constructor for use with Jackson libraries
     */
    public ViewServiceConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ViewServiceConfig(ViewServiceConfig template)
    {
        super(template);

        if (template != null)
        {
            viewServiceId = template.getViewServiceId();
            viewServiceAdminClass = template.getViewServiceAdminClass();
            viewServiceFullName = template.getViewServiceFullName();
            viewServiceName = template.getViewServiceName();
            viewServiceURLMarker = template.getViewServiceURLMarker();
            viewServiceDescription = template.getViewServiceDescription();
            viewServiceWiki = template.getViewServiceWiki();
            viewServiceOperationalStatus = template.getViewServiceOperationalStatus();
            viewServiceOptions = template.getViewServiceOptions();
        }
    }



    /**
     * Set up the default values for a view service using a view service description.
     *
     * @param viewRegistration fixed properties about the view service
     */
    public ViewServiceConfig(ViewServiceRegistration viewRegistration)
    {
        this.viewServiceId = viewRegistration.getViewServiceCode();
        this.viewServiceName = viewRegistration.getViewServiceName();
        this.viewServiceFullName = viewRegistration.getViewServiceFullName();
        this.viewServiceURLMarker = viewRegistration.getViewServiceURLMarker();
        this.viewServiceAdminClass = viewRegistration.getViewServiceAdminClassName();
        this.viewServiceDescription = viewRegistration.getViewServiceDescription();
        this.viewServiceWiki = viewRegistration.getViewServiceWiki();
        this.viewServiceOperationalStatus = viewRegistration.getViewServiceOperationalStatus();
    }


    /**
     * Return the code number (ordinal) for this view service.
     *
     * @return int ordinal
     */
    public int getViewServiceId()
    {
        return viewServiceId;
    }


    /**
     * Set up the code number (ordinal) for this view service.
     *
     * @param viewId int ordinal
     */
    public void setViewServiceId(int viewId)
    {
        this.viewServiceId = viewId;
    }


    /**
     * Return the Java class name of the admin-services interface for this view service.
     *
     * @return String class name implementing the
     * ViewServiceAdmin interface.
     */
    public String getViewServiceAdminClass()
    {
        return viewServiceAdminClass;
    }


    /**
     * Set up the Java class name of the admin services interface for this view service.
     *
     * @param viewServiceAdminClass String class name implementing the
     * ViewServiceAdmin interface.
     */
    public void setViewServiceAdminClass(String viewServiceAdminClass)
    {
        this.viewServiceAdminClass = viewServiceAdminClass;
    }


    /**
     * Return the full name of the view service.
     *
     * @return String name
     */
    public String getViewServiceFullName()
    {
        return viewServiceFullName;
    }


    /**
     * Set up the full name of the view service.
     *
     * @param viewServiceFullName String name
     */
    public void setViewServiceFullName(String viewServiceFullName)
    {
        this.viewServiceFullName = viewServiceFullName;
    }


    /**
     * Return the name of the view service.
     *
     * @return String name
     */
    public String getViewServiceName()
    {
        return viewServiceName;
    }


    /**
     * Set up the name of the view service.
     *
     * @param viewServiceName String name
     */
    public void setViewServiceName(String viewServiceName)
    {
        this.viewServiceName = viewServiceName;
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @return String viewServiceURLMarker
     */
    public String getViewServiceURLMarker()
    {
        return viewServiceURLMarker;
    }


    /**
     * Set up the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @param viewServiceURLMarker url fragment
     */
    public void setViewServiceURLMarker(String viewServiceURLMarker)
    {
        this.viewServiceURLMarker = viewServiceURLMarker;
    }


    /**
     * Return the short description of the view service.  The default value is in English but this can be changed.
     *
     * @return String viewServiceDescription
     */
    public String getViewServiceDescription()
    {
        return viewServiceDescription;
    }


    /**
     * Set up the short description of the view service.
     *
     * @param viewServiceDescription String description
     */
    public void setViewServiceDescription(String viewServiceDescription)
    {
        this.viewServiceDescription = viewServiceDescription;
    }


    /**
     * Return the wiki page link for the view service.  The default value points to a page on the Atlas
     * confluence wiki.
     *
     * @return viewServiceWiki String url
     */
    public String getViewServiceWiki()
    {
        return viewServiceWiki;
    }


    /**
     * Set up the wiki page link for the view service.
     *
     * @param viewServiceWiki String url
     */
    public void setViewServiceWiki(String viewServiceWiki)
    {
        this.viewServiceWiki = viewServiceWiki;
    }


    /**
     * Return the status of this view service.
     *
     * @return viewServiceOperationalStatus enum
     */
    public ServiceOperationalStatus getViewServiceOperationalStatus()
    {
        return viewServiceOperationalStatus;
    }


    /**
     * Set up the status of the view service.
     *
     * @param viewServiceOperationalStatus ViewServiceOperationalStatus enum
     */
    public void setViewServiceOperationalStatus(ServiceOperationalStatus viewServiceOperationalStatus)
    {
        this.viewServiceOperationalStatus = viewServiceOperationalStatus;
    }

    /**
     * Return the options for this view service. These are properties that are specific to the view service.
     *
     * @return Map from String to String
     */
    public Map<String, Object> getViewServiceOptions()
    {
        if (viewServiceOptions == null)
        {
            return null;
        }
        else if (viewServiceOptions.isEmpty())
        {
            return null;
        }
        else
        {
            return viewServiceOptions;
        }
    }


    /**
     * Set up the options for this view service.  These are properties that are specific to the view service.
     *
     * @param viewServiceOptions Map from String to String
     */
    public void setViewServiceOptions(Map<String, Object> viewServiceOptions)
    {
        this.viewServiceOptions = viewServiceOptions;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "ViewServiceConfig{" +
                "viewServiceId=" + viewServiceId +
                ", viewServiceAdminClass='" + viewServiceAdminClass + '\'' +
                ", viewServiceName='" + viewServiceName + '\'' +
                ", viewServiceFullName='" + viewServiceFullName + '\'' +
                ", viewServiceURLMarker='" + viewServiceURLMarker + '\'' +
                ", viewServiceDescription='" + viewServiceDescription + '\'' +
                ", viewServiceWiki='" + viewServiceWiki + '\'' +
                ", viewServiceOperationalStatus=" + viewServiceOperationalStatus +
                ", viewServiceOptions=" + viewServiceOptions +
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
        ViewServiceConfig that = (ViewServiceConfig) objectToCompare;
        return getViewServiceId() == that.getViewServiceId() &&
                       Objects.equals(getViewServiceAdminClass(), that.getViewServiceAdminClass()) &&
                       Objects.equals(getViewServiceName(), that.getViewServiceName()) &&
                       Objects.equals(getViewServiceFullName(), that.getViewServiceFullName()) &&
                       Objects.equals(getViewServiceURLMarker(), that.getViewServiceURLMarker()) &&
                       Objects.equals(getViewServiceDescription(), that.getViewServiceDescription()) &&
                       Objects.equals(getViewServiceWiki(), that.getViewServiceWiki()) &&
                       getViewServiceOperationalStatus() == that.getViewServiceOperationalStatus() &&
                       Objects.equals(getViewServiceOptions(), that.getViewServiceOptions());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getViewServiceId(), getViewServiceAdminClass(), getViewServiceName(), getViewServiceFullName(), getViewServiceURLMarker(),
                            getViewServiceDescription(), getViewServiceWiki(), getViewServiceOperationalStatus(), getViewServiceOptions());
    }
}
