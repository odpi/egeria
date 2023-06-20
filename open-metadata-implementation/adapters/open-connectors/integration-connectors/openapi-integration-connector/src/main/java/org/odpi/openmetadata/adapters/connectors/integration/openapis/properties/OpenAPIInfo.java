/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenAPIInfo describes the top level info section of the Open API Specification
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPIInfo
{
    private String                title       = null;
    private String                description = null;
    private OpenAPIContactDetails contact     = null;
    private OpenAPILicense        license     = null;
    private String                version     = null;


    public OpenAPIInfo()
    {
    }


    public String getTitle()
    {
        return title;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public OpenAPIContactDetails getContact()
    {
        return contact;
    }


    public void setContact(OpenAPIContactDetails contact)
    {
        this.contact = contact;
    }


    public OpenAPILicense getLicense()
    {
        return license;
    }


    public void setLicense(OpenAPILicense license)
    {
        this.license = license;
    }


    public String getVersion()
    {
        return version;
    }


    public void setVersion(String version)
    {
        this.version = version;
    }


    @Override
    public String toString()
    {
        return "OpenAPIInfo{" +
                       "title='" + title + '\'' +
                       ", description='" + description + '\'' +
                       ", contact=" + contact +
                       ", license=" + license +
                       ", version='" + version + '\'' +
                       '}';
    }
}
