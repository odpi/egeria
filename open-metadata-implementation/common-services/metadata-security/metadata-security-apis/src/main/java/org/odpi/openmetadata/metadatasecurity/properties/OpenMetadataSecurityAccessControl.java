/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.users.SecurityAccessControl;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataSecurityAccessControl extends UserAccount with name.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataSecurityAccessControl extends SecurityAccessControl
{
    private String controlName = null;

    /**
     * Default constructor
     */
    public OpenMetadataSecurityAccessControl()
    {
        super();
    }


    /**
     * Copy constructor
     *
     * @param controlName name of control
     * @param securityAccessControl super class properties
     */
    public OpenMetadataSecurityAccessControl(String                 controlName,
                                             SecurityAccessControl  securityAccessControl)
    {
        super(securityAccessControl);

        this.controlName = controlName;
    }


    /**
     * Return the name that identifies the control.
     *
     * @return string identifier
     */
    public String getControlName()
    {
        return controlName;
    }


    /**
     * Set up the name that identifies the control.
     *
     * @param controlName string identifier
     */
    public void setControlName(String controlName)
    {
        this.controlName = controlName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenMetadataSecurityAccessControl{" +
                "controlName='" + controlName + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        OpenMetadataSecurityAccessControl that = (OpenMetadataSecurityAccessControl) objectToCompare;
        return Objects.equals(controlName, that.controlName);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), controlName);
    }
}
