/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalIdentifierProperties describes the  properties used to pass information about an external identifier.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalIdentifierProperties
{
    private SynchronizationDirection synchronizationDirection   = null;
    private String                   synchronizationDescription = null;
    private String                   externalIdentifier         = null;
    private String                   externalIdentifierName     = null;
    private String                   externalIdentifierUsage    = null;
    private String                   externalIdentifierSource   = null;
    private KeyPattern               keyPattern                 = null;
    private Map<String, String>      mappingProperties          = null;

    /**
     * Default constructor
     */
    public ExternalIdentifierProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public ExternalIdentifierProperties(ExternalIdentifierProperties template)
    {
        if (template != null)
        {
            synchronizationDirection = template.getSynchronizationDirection();
            synchronizationDescription = template.getSynchronizationDescription();
            externalIdentifier = template.getExternalIdentifier();
            externalIdentifierName = template.getExternalIdentifierName();
            externalIdentifierUsage = template.getExternalIdentifierUsage();
            externalIdentifierSource = template.getExternalIdentifierSource();
            keyPattern = template.getKeyPattern();
            mappingProperties = template.getMappingProperties();
        }
    }


    /**
     * Return details of the synchronization direction.
     *
     * @return enum
     */
    public SynchronizationDirection getSynchronizationDirection()
    {
        return synchronizationDirection;
    }


    /**
     * Set up details of the synchronization direction.
     *
     * @param synchronizationDirection enum
     */
    public void setSynchronizationDirection(SynchronizationDirection synchronizationDirection)
    {
        this.synchronizationDirection = synchronizationDirection;
    }


    /**
     * Return optional short description of the asset manager.
     *
     * @return string summary
     */
    public String getSynchronizationDescription()
    {
        return synchronizationDescription;
    }


    /**
     * Set up optional short description of the asset manager.
     *
     * @param synchronizationDescription string summary
     */
    public void setSynchronizationDescription(String synchronizationDescription)
    {
        this.synchronizationDescription = synchronizationDescription;
    }


    /**
     * Return the unique identifier used in the external asset manager for this element.
     *
     * @return string identifier
     */
    public String getExternalIdentifier()
    {
        return externalIdentifier;
    }


    /**
     * Set up the unique identifier used in the external asset manager for this element.
     *
     * @param externalIdentifier string identifier
     */
    public void setExternalIdentifier(String externalIdentifier)
    {
        this.externalIdentifier = externalIdentifier;
    }


    /**
     * Return a short description of the external identifier (such as style or property name).
     *
     * @return string summary
     */
    public String getExternalIdentifierName()
    {
        return externalIdentifierName;
    }


    /**
     * Set up a short description of the external identifier (such as style or property name).
     *
     * @param externalIdentifierName string summary
     */
    public void setExternalIdentifierName(String externalIdentifierName)
    {
        this.externalIdentifierName = externalIdentifierName;
    }


    /**
     * Return a short description of how the external identifier is used.
     *
     * @return string description
     */
    public String getExternalIdentifierUsage()
    {
        return externalIdentifierUsage;
    }


    /**
     * Set up a short description of how the external identifier is used.
     *
     * @param externalIdentifierUsage string description
     */
    public void setExternalIdentifierUsage(String externalIdentifierUsage)
    {
        this.externalIdentifierUsage = externalIdentifierUsage;
    }


    /**
     * Return the component (connector/client) that created/maintained this external identifier and its relationship
     * to the open metadata element(s).
     *
     * @return component name
     */
    public String getExternalIdentifierSource()
    {
        return externalIdentifierSource;
    }


    /**
     * Set up the component (connector/client) that created/maintained this external identifier and its relationship
     * to the open metadata element(s).
     *
     * @param externalIdentifierSource component name
     */
    public void setExternalIdentifierSource(String externalIdentifierSource)
    {
        this.externalIdentifierSource = externalIdentifierSource;
    }


    /**
     * Set up the key pattern used in the asset manager for the external identifier.
     *
     * @param keyPattern String name
     */
    public void setKeyPattern(KeyPattern keyPattern)
    {
        this.keyPattern = keyPattern;
    }


    /**
     * Returns the key pattern used in the asset manager  for the external identifier.
     *
     * @return String name
     */
    public KeyPattern getKeyPattern()
    {
        return keyPattern;
    }


    /**
     * Return any additional properties to help with the mapping of the external identifier to open
     * metadata elements.
     *
     * @return name-value pairs
     */
    public Map<String, String> getMappingProperties()
    {
        if (mappingProperties == null)
        {
            return null;
        }
        else if (mappingProperties.isEmpty())
        {
            return null;
        }

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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalIdentifierProperties{" +
                       "synchronizationDirection=" + synchronizationDirection +
                       ", synchronizationDescription='" + synchronizationDescription + '\'' +
                       ", externalIdentifier='" + externalIdentifier + '\'' +
                       ", externalIdentifierName='" + externalIdentifierName + '\'' +
                       ", externalIdentifierUsage='" + externalIdentifierUsage + '\'' +
                       ", externalIdentifierSource='" + externalIdentifierSource + '\'' +
                       ", keyPattern=" + keyPattern +
                       ", mappingProperties=" + mappingProperties +
                       '}';
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        ExternalIdentifierProperties that = (ExternalIdentifierProperties) objectToCompare;
        return getSynchronizationDirection() == that.getSynchronizationDirection() &&
                       Objects.equals(getSynchronizationDescription(), that.getSynchronizationDescription()) &&
                       Objects.equals(getExternalIdentifier(), that.getExternalIdentifier()) &&
                       Objects.equals(getExternalIdentifierName(), that.getExternalIdentifierName()) &&
                       Objects.equals(getExternalIdentifierUsage(), that.getExternalIdentifierUsage()) &&
                       Objects.equals(getExternalIdentifierSource(), that.getExternalIdentifierSource()) &&
                       getKeyPattern() == that.getKeyPattern() &&
                       Objects.equals(getMappingProperties(), that.getMappingProperties());
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(synchronizationDirection, synchronizationDescription, externalIdentifier, externalIdentifierName, externalIdentifierUsage,
                            externalIdentifierSource, keyPattern, mappingProperties);
    }
}
