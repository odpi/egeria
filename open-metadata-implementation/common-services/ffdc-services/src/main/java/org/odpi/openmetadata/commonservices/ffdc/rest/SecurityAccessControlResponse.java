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
 * OpenMetadataSecurityAccessControl is the response structure used to resturn a security access control to the caller.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityAccessControlResponse extends FFDCResponseBase
{
    private OpenMetadataSecurityAccessControl securityAccessControl = null;

    /**
     * Default constructor
     */
    public SecurityAccessControlResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SecurityAccessControlResponse(SecurityAccessControlResponse template)
    {
        super(template);

        if (template != null)
        {
            this.securityAccessControl = template.getSecurityAccessControl();
        }
    }


    /**
     * Return the security access control object.
     *
     * @return security access control
     */
    public OpenMetadataSecurityAccessControl getSecurityAccessControl()
    {
        return securityAccessControl;
    }


    /**
     * Set up the security access control object.
     *
     * @param securityAccessControl - security access control
     */
    public void setSecurityAccessControl(OpenMetadataSecurityAccessControl securityAccessControl)
    {
        this.securityAccessControl = securityAccessControl;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SecurityAccessControlResponse{" +
                "securityAccessControl=" + securityAccessControl +
                "} " + super.toString();
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
        if (!(objectToCompare instanceof SecurityAccessControlResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getSecurityAccessControl(), that.getSecurityAccessControl());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        if (securityAccessControl == null)
        {
            return super.hashCode();
        }
        else
        {
            return securityAccessControl.hashCode();
        }
    }
}
