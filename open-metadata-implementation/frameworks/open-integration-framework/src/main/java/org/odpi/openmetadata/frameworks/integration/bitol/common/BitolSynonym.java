/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents an alternative name for a data product, schema object or schema property, recorded so that catalogs, AI tools and natural language interfaces can resolve business vocabulary to the underlying element (ODCS v3.2.0 and ODPS v1.1.0, RFC 0041).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BitolSynonym
{
    private String                    id = null;
    private String                    synonym = null;
    private String                    description = null;
    private String                    locale = null;
    private String                    source = null;
    private String                    status = null;
    private List<BitolCustomProperty> customProperties = null;


    /**
     * Default constructor
     */
    public BitolSynonym()
    {
    }


    /**
     * Return the stable identifier of the synonym.
     *
     * @return String
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of the synonym.
     *
     * @param id String
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the synonymous term.
     *
     * @return String
     */
    public String getSynonym()
    {
        return synonym;
    }


    /**
     * Set up the synonymous term.
     *
     * @param synonym String
     */
    public void setSynonym(String synonym)
    {
        this.synonym = synonym;
    }


    /**
     * Return a short note about when or why this synonym is used.
     *
     * @return String
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up a short note about when or why this synonym is used.
     *
     * @param description String
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the BCP 47 language tag when the synonym is language-specific.
     *
     * @return String
     */
    public String getLocale()
    {
        return locale;
    }


    /**
     * Set up the BCP 47 language tag when the synonym is language-specific.
     *
     * @param locale String
     */
    public void setLocale(String locale)
    {
        this.locale = locale;
    }


    /**
     * Return the origin of the synonym, for example glossary or legacy-system.
     *
     * @return String
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the origin of the synonym, for example glossary or legacy-system.
     *
     * @param source String
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Return the lifecycle status of the synonym, for example active or deprecated.
     *
     * @return String
     */
    public String getStatus()
    {
        return status;
    }


    /**
     * Set up the lifecycle status of the synonym, for example active or deprecated.
     *
     * @param status String
     */
    public void setStatus(String status)
    {
        this.status = status;
    }


    /**
     * Return the custom properties of the synonym.
     *
     * @return List<BitolCustomProperty>
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the custom properties of the synonym.
     *
     * @param customProperties List<BitolCustomProperty>
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BitolSynonym{" +
                       "id=" + id +
                       ", synonym=" + synonym +
                       ", description=" + description +
                       ", locale=" + locale +
                       ", source=" + source +
                       ", status=" + status +
                       ", customProperties=" + customProperties +
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
        BitolSynonym that = (BitolSynonym) objectToCompare;
        return Objects.equals(id, that.id) &&
                       Objects.equals(synonym, that.synonym) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(locale, that.locale) &&
                       Objects.equals(source, that.source) &&
                       Objects.equals(status, that.status) &&
                       Objects.equals(customProperties, that.customProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, synonym, description, locale, source, status, customProperties);
    }
}
