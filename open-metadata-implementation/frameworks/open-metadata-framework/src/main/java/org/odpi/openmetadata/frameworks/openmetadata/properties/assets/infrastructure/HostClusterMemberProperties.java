/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * HostClusterMemberProperties describes the properties for the HostClusterMember relationship between a host cluster and the hosts it manages.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class HostClusterMemberProperties extends RelationshipBeanProperties
{
    private String              memberRole           = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public HostClusterMemberProperties()
    {
        super();
        super.typeName = OpenMetadataType.HOST_CLUSTER_MEMBER_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public HostClusterMemberProperties(HostClusterMemberProperties template)
    {
        super(template);

        if (template != null)
        {
            memberRole           = template.getMemberRole();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the role of the member in the host cluster.  This value is typically defined by the technology of the host cluster.
     *
     * @return string name
     */
    public String getMemberRole()
    {
        return memberRole;
    }


    /**
     * Set up the role of the member in the host cluster.  This value is typically defined by the technology of the host cluster.
     *
     * @param memberRole string name
     */
    public void setMemberRole(String memberRole)
    {
        this.memberRole = memberRole;
    }


    /**
     * Return the additional properties for the element.
     *
     * @return name-value pairs for additional values
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up the additional properties for the element.
     *
     * @param additionalProperties name-value pairs for additional values
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "HostClusterMemberProperties{" +
                "memberRole='" + memberRole + '\'' +
                ", additionalProperties=" + additionalProperties +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        HostClusterMemberProperties that = (HostClusterMemberProperties) objectToCompare;
        return Objects.equals(memberRole, that.memberRole) &&
                       Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), memberRole, additionalProperties);
    }
}
