/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.gaf.properties;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * TranslationDetail provides translated strings for an open metadata element for a specific language/locale.
 */
public class TranslationDetail implements Serializable
{
    private static final long   serialVersionUID = 1L;

    private String              language               = null;
    private String              locale                 = null;
    private String              displayName            = null;
    private String              description            = null;
    private Map<String, String> additionalTranslations = null;


    /**
     * Default Constructor
     */
    public TranslationDetail()
    {
    }


    /**
     * Copy/clone Constructor
     *
     * @param template object being copied
     */
    public TranslationDetail(TranslationDetail template)
    {
        if (template != null)
        {
            language = template.getLanguage();
            locale = template.getLocale();
            displayName = template.getDisplayName();
            description = template.getDescription();
            additionalTranslations = template.getAdditionalTranslations();
        }
    }


    /**
     * Return the name of the language that this translation is using.
     *
     * @return string name
     */
    public String getLanguage()
    {
        return language;
    }


    /**
     * Set up the name of the language that this translation is using.
     *
     * @param language string name
     */
    public void setLanguage(String language)
    {
        this.language = language;
    }


    /**
     * Return the locale that this translation is using.  This is more specific than the language since it covers regional difference.  It is
     * an optional value.
     *
     * @return string name
     */
    public String getLocale()
    {
        return locale;
    }


    /**
     * Set up the locale that this translation is using.  This is more specific than the language since it covers regional difference.  It is
     * an optional value.
     *
     * @param locale string name
     */
    public void setLocale(String locale)
    {
        this.locale = locale;
    }


    /**
     * Return the translation for either then "name" or "displayName" property if they are used in the attached element type.
     *
     * @return string value
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the translation for either then "name" or "displayName" property if they are used in the attached element type.
     *
     * @param displayName string value
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the translation for the "description" property if it is used in the attached element type.
     *
     * @return string value
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the translation for the "description" property if it is used in the attached element type.
     *
     * @param description string value
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the map of additional text string translations.  The name of the property is mapped to the translation.
     *
     * @return map of additional translations
     */
    public Map<String, String> getAdditionalTranslations()
    {
        return additionalTranslations;
    }


    /**
     * Set up the map of additional text string translations.  The name of the property is mapped to the translation.
     *
     * @param additionalTranslations map of additional translations
     */
    public void setAdditionalTranslations(Map<String, String> additionalTranslations)
    {
        this.additionalTranslations = additionalTranslations;
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
        if (! (objectToCompare instanceof TranslationDetail))
        {
            return false;
        }
        TranslationDetail that = (TranslationDetail) objectToCompare;
        return Objects.equals(language, that.language) &&
                       Objects.equals(locale, that.locale) &&
                       Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(additionalTranslations, that.additionalTranslations);
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(language, locale, displayName, description, additionalTranslations);
    }
}
