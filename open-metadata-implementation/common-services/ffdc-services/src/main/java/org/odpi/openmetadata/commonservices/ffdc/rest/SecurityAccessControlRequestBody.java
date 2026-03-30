/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataSecurityAccessControl;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityAccessControlRequestBody passes information to set up a security access control with the security connector to protect
 * requests to the platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecurityAccessControlRequestBody
{
    private OpenMetadataSecurityAccessControl securityAccessControl = null;


    /**
     * Default constructor
     */
    public SecurityAccessControlRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template source
     */
    public SecurityAccessControlRequestBody(SecurityAccessControlRequestBody template)
    {
        if (template != null)
        {
            securityAccessControl = template.getSecurityAccessControl();
        }
    }


    /**
     * Return the security access control.
     *
     * @return security access control
     */
    public OpenMetadataSecurityAccessControl getSecurityAccessControl()
    {
        return securityAccessControl;
    }


    /**
     * Set up the security access control.
     *
     * @param securityAccessControl security access control
     */
    public void setSecurityAccessControl(OpenMetadataSecurityAccessControl securityAccessControl)
    {
        this.securityAccessControl = securityAccessControl;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "SecurityAccessControlRequestBody{" +
                "securityAccessControl=" + securityAccessControl +
                '}';
    }

    
    /**
     * Compare objects
     *
     * @param objectToCompare object
     * @return boolean
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
        SecurityAccessControlRequestBody that = (SecurityAccessControlRequestBody) objectToCompare;
        return Objects.equals(getSecurityAccessControl(), that.getSecurityAccessControl());
    }


    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSecurityAccessControl());
    }
}
