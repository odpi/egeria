/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AgreementRoleProperties describes a relationship between a role and an agreement.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgreementRoleProperties extends RelationshipProperties
{
    private String roleName = null;


    /**
     * Default constructor
     */
    public AgreementRoleProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public AgreementRoleProperties(AgreementRoleProperties template)
    {
        super(template);

        if (template != null)
        {
            roleName = template.getRoleName();
        }
    }


    /**
     * Set up the name of the role from the agreement text.
     *
     * @param roleName String name
     */
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }


    /**
     * Returns the name of the role from the agreement text.
     *
     * @return String name
     */
    public String getRoleName()
    {
        return roleName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AgreementRoleProperties{" +
                "roleName='" + roleName + '\'' +
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
        if (! (objectToCompare instanceof AgreementRoleProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(roleName, that.roleName);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), roleName);
    }
}
