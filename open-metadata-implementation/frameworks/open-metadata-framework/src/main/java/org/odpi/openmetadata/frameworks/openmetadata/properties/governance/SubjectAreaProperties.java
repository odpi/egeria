/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A subject area defines a group of definitions for governing assets related to a specific topic.  The subject area definition defines
 * how the assets related to the topic should be managed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SubjectAreaProperties extends CollectionProperties
{
    private String scope            = null;
    private String usage            = null;
    private int    domainIdentifier = 0;

    /**
     * Default constructor
     */
    public SubjectAreaProperties()
    {
        super();
        super.typeName = OpenMetadataType.SUBJECT_AREA.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SubjectAreaProperties(SubjectAreaProperties template)
    {
        super(template);

        if (template != null)
        {
            this.scope             = template.getScope();
            this.usage             = template.getUsage();
            this.domainIdentifier  = template.getDomainIdentifier();
        }
    }


    /**
     * Return the organizational scope that this governance definition applies to.
     *
     * @return String
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the organizational scope that this definition applies to.
     *
     * @param scope String
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Return details of the usage of this definition.
     *
     * @return text
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up the details of the usage of this definition.
     *
     * @param usage text
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * Return the identifier of the governance domain that this definition belongs to (0=all).
     *
     * @return int
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Set up the identifier of the governance domain that this definition belongs to (0=all).
     *
     * @param domainIdentifier int
     */
    public void setDomainIdentifier(int domainIdentifier)
    {
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SubjectAreaProperties{" +
                "scope='" + getScope() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", domainIdentifier=" + getDomainIdentifier() +
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
        SubjectAreaProperties that = (SubjectAreaProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(usage, that.usage);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), scope, usage, domainIdentifier);
    }
}
