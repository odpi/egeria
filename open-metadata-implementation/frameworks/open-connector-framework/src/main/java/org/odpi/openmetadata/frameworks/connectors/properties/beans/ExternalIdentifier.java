/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalIdentifier stores information about an identifier for the asset that is used in an external system.
 * This is used for correlating information about the asset across different systems.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalIdentifier extends Referenceable
{
    /*
     * Attributes of an external identifier
     */
    protected String        identifier                     = null;
    protected String        description                    = null;
    protected String        usage                          = null;
    protected String        source                         = null;
    protected KeyPattern    keyPattern                     = null;
    protected String        externalInstanceCreatedBy      = null;
    protected Date          externalInstanceCreationTime   = null;
    protected String        externalInstanceLastUpdatedBy  = null;
    protected Date          externalInstanceLastUpdateTime = null;
    protected long          externalInstanceVersion        = 0L;
    protected Referenceable scope                          = null;
    protected String        scopeDescription               = null;


    /**
     * Default constructor
     */
    public ExternalIdentifier()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template  element to copy
     */
    public ExternalIdentifier(ExternalIdentifier template)
    {

        super(template);

        if (template != null)
        {
            /*
             * Copy the values from the supplied template.
             */
            identifier = template.getIdentifier();
            description = template.getDescription();
            usage = template.getUsage();
            source = template.getSource();
            keyPattern = template.getKeyPattern();
            externalInstanceCreatedBy = template.getExternalInstanceCreatedBy();
            externalInstanceCreationTime = template.getExternalInstanceCreationTime();
            externalInstanceLastUpdatedBy = template.getExternalInstanceLastUpdatedBy();
            externalInstanceLastUpdateTime = template.getExternalInstanceLastUpdateTime();
            externalInstanceVersion = template.getExternalInstanceVersion();

            Referenceable  templateScope = template.getScope();
            if (templateScope != null)
            {
                scope = new Referenceable(templateScope);
            }

            scopeDescription = template.getScopeDescription();
        }
    }


    /**
     * Return the external identifier for this asset.
     *
     * @return String identifier
     */
    public String getIdentifier() { return identifier; }


    /**
     * Set up the external identifier for this asset.
     *
     * @param identifier String identifier
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Return the description of the external identifier.
     *
     * @return String text
     */
    public String getDescription() { return description; }


    /**
     * Set up the description of the external identifier.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return a description of how, where and when this external identifier is used.
     *
     * @return String usage description
     */
    public String getUsage() { return usage; }


    /**
     * Set up a description of how, where and when this external identifier is used.
     *
     * @param usage String usage description
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * Return details of the source system where this external identifier comes from.
     *
     * @return String server identifier
     */
    public String getSource() { return source; }


    /**
     * Set up details of the source system where this external identifier comes from.
     *
     * @param source String server identifier
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Return the key pattern that is used with this external identifier.
     *
     * @return KeyPattern enum
     */
    public KeyPattern getKeyPattern() { return keyPattern; }


    /**
     * Set up the key pattern that is used with this external identifier.
     *
     * @param keyPattern KeyPattern enum
     */
    public void setKeyPattern(KeyPattern keyPattern)
    {
        this.keyPattern = keyPattern;
    }



    /**
     * Return the username of the person or process that created the instance in the external system.
     *
     * @return name
     */
    public String getExternalInstanceCreatedBy()
    {
        return externalInstanceCreatedBy;
    }


    /**
     * Set up the username of the person or process that created the instance in the external system.
     *
     * @param externalInstanceCreatedBy name
     */
    public void setExternalInstanceCreatedBy(String externalInstanceCreatedBy)
    {
        this.externalInstanceCreatedBy = externalInstanceCreatedBy;
    }


    /**
     * Return the date/time when the instance in the external system was created.
     *
     * @return date
     */
    public Date getExternalInstanceCreationTime()
    {
        return externalInstanceCreationTime;
    }


    /**
     * Set up the date/time when the instance in the external system was created.
     *
     * @param externalInstanceCreationTime date
     */
    public void setExternalInstanceCreationTime(Date externalInstanceCreationTime)
    {
        this.externalInstanceCreationTime = externalInstanceCreationTime;
    }


    /**
     * Return the username of the person or process that last updated the instance in the external system.
     *
     * @return name
     */
    public String getExternalInstanceLastUpdatedBy()
    {
        return externalInstanceLastUpdatedBy;
    }


    /**
     * Set up the username of the person or process that last updated the instance in the external system.
     *
     * @param externalInstanceLastUpdatedBy name
     */
    public void setExternalInstanceLastUpdatedBy(String externalInstanceLastUpdatedBy)
    {
        this.externalInstanceLastUpdatedBy = externalInstanceLastUpdatedBy;
    }


    /**
     * Return the date/time that the instance in the external system was last updated.
     *
     * @return date
     */
    public Date getExternalInstanceLastUpdateTime()
    {
        return externalInstanceLastUpdateTime;
    }


    /**
     * Set up the date/time that the instance in the external system was last updated.
     *
     * @param externalInstanceLastUpdateTime date
     */
    public void setExternalInstanceLastUpdateTime(Date externalInstanceLastUpdateTime)
    {
        this.externalInstanceLastUpdateTime = externalInstanceLastUpdateTime;
    }


    /**
     * Return the latest version of the element in the external system.
     *
     * @return long
     */
    public long getExternalInstanceVersion()
    {
        return externalInstanceVersion;
    }


    /**
     * Set up the latest version of the element in the external system.
     *
     * @param externalInstanceVersion long
     */
    public void setExternalInstanceVersion(long externalInstanceVersion)
    {
        this.externalInstanceVersion = externalInstanceVersion;
    }


    /**
     * Return the scope of this external identifier.  This depends on the key pattern.  It may be a server definition,
     * a reference data set or glossary term.
     *
     * @return Referenceable scope
     */
    public Referenceable getScope()
    {
        if (scope == null)
        {
            return null;
        }
        else
        {
            return new Referenceable(scope);
        }
    }


    /**
     * Set up the scope of this external identifier.  This depends on the key pattern.  It may be a server definition,
     * a reference data set or glossary term.
     *
     * @param scope Referenceable object defining the scope of this external identifier.
     */
    public void setScope(Referenceable scope)
    {
        this.scope = scope;
    }


    /**
     * Return the text description of the scope for this external identifier.
     *
     * @return String scope description
     */
    public String getScopeDescription() { return scopeDescription; }


    /**
     * Set up the text description of the scope for this external identifier.
     *
     * @param scopeDescription String scope description
     */
    public void setScopeDescription(String scopeDescription)
    {
        this.scopeDescription = scopeDescription;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalIdentifier{" +
                       "identifier='" + identifier + '\'' +
                       ", description='" + description + '\'' +
                       ", usage='" + usage + '\'' +
                       ", source='" + source + '\'' +
                       ", keyPattern=" + keyPattern +
                       ", externalInstanceCreatedBy='" + externalInstanceCreatedBy + '\'' +
                       ", externalInstanceCreationTime=" + externalInstanceCreationTime +
                       ", externalInstanceLastUpdatedBy='" + externalInstanceLastUpdatedBy + '\'' +
                       ", externalInstanceLastUpdateTime='" + externalInstanceLastUpdateTime + '\'' +
                       ", externalInstanceVersion=" + externalInstanceVersion +
                       ", scope=" + scope +
                       ", scopeDescription='" + scopeDescription + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", URL='" + getURL() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ExternalIdentifier that = (ExternalIdentifier) objectToCompare;
        return Objects.equals(getIdentifier(), that.getIdentifier()) &&
                       Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getUsage(), that.getUsage()) &&
                       Objects.equals(getSource(), that.getSource()) &&
                       getKeyPattern() == that.getKeyPattern() &&
                       Objects.equals(getScope(), that.getScope()) &&
                       Objects.equals(getScopeDescription(), that.getScopeDescription());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getIdentifier(), getDescription(), getUsage(), getSource(), getKeyPattern(), getScope(),
                            getScopeDescription());
    }
}