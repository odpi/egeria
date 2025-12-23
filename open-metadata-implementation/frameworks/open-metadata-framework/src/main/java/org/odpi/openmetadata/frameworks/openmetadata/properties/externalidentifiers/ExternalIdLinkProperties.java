/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalIdLinkProperties describes the properties for the relationship between a local open metadata
 * element and one of its external identifiers.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalIdLinkProperties extends RelationshipBeanProperties
{
    private String                   usage                    = null;
    private String                   source                   = null;
    private Date                     lastSynchronized         = null;
    private Map<String, String>      mappingProperties        = null;
    private PermittedSynchronization permittedSynchronization = null;


    /**
     * Default constructor
     */
    public ExternalIdLinkProperties()
    {
        super();
        super.typeName = OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public ExternalIdLinkProperties(ExternalIdLinkProperties template)
    {
        super(template);

        if (template != null)
        {
            usage                    = template.getUsage();
            source                   = template.getSource();
            lastSynchronized         = template.getLastSynchronized();
            mappingProperties        = template.getMappingProperties();
            permittedSynchronization = template.getPermittedSynchronization();

        }
    }


    /**
     * Return a short description of how the external identifier is used.
     *
     * @return string description
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up a short description of how the external identifier is used.
     *
     * @param usage string description
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * Return the component (connector/client) that created/maintained this external identifier and its relationship
     * to the open metadata element(s).
     *
     * @return component name
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the component (connector/client) that created/maintained this external identifier and its relationship
     * to the open metadata element(s).
     *
     * @param source component name
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Return the date/time that the instance in the external system was last queried.
     *
     * @return date
     */
    public Date getLastSynchronized()
    {
        return lastSynchronized;
    }


    /**
     * Set up the date/time that the instance in the external system was last queried.
     *
     * @param lastSynchronized date
     */
    public void setLastSynchronized(Date lastSynchronized)
    {
        this.lastSynchronized = lastSynchronized;
    }


    /**
     * Return any additional properties to help with the mapping of the external identifier to open
     * metadata elements.
     *
     * @return name-value pairs
     */
    public Map<String, String> getMappingProperties()
    {
        return mappingProperties;
    }


    /**
     * Set up any additional properties to help with the mapping of the external identifier to open
     * metadata elements.
     *
     * @param mappingProperties name-value pairs
     */
    public void setMappingProperties(Map<String, String> mappingProperties)
    {
        this.mappingProperties = mappingProperties;
    }



    /**
     * Return details of the synchronization direction.
     *
     * @return enum
     */
    public PermittedSynchronization getPermittedSynchronization()
    {
        return permittedSynchronization;
    }


    /**
     * Set up details of the synchronization direction.
     *
     * @param permittedSynchronization enum
     */
    public void setPermittedSynchronization(PermittedSynchronization permittedSynchronization)
    {
        this.permittedSynchronization = permittedSynchronization;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalIdLinkProperties{" +
                "usage='" + usage + '\'' +
                ", source='" + source + '\'' +
                ", lastSynchronized=" + lastSynchronized +
                ", mappingProperties=" + mappingProperties +
                ", permittedSynchronization=" + permittedSynchronization +
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
        ExternalIdLinkProperties that = (ExternalIdLinkProperties) objectToCompare;
        return  permittedSynchronization == that.permittedSynchronization &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(source, that.source) &&
                Objects.equals(lastSynchronized, that.lastSynchronized) &&
                Objects.equals(mappingProperties, that.mappingProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), usage, source, lastSynchronized, mappingProperties, permittedSynchronization);
    }
}
