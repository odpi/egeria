/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Meaning is a cut-down summary of a glossary term to aid the asset consumer in understanding the content
 * of an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Meaning extends Referenceable
{
    /*
     * Attributes of a meaning object definition
     */
    private String      name = null;
    private String      description = null;


    /**
     * Default Constructor
     */
    public Meaning()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateMeaning - element to copy
     */
    public Meaning(Meaning templateMeaning)
    {
        super(templateMeaning);

        if (templateMeaning != null)
        {
            /*
             * Copy the values from the supplied meaning object.
             */
            name = templateMeaning.getName();
            description = templateMeaning.getDescription();
        }
    }


    /**
     * Return the glossary term name.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the glossary term.
     *
     * @param name - String
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description of the glossary term.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the glossary term.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
}