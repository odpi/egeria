/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

/**
 * Meaning is a cut-down summary of a glossary term to aid the asset consumer in understanding the content
 * of an asset.
 */
public class Meaning extends ElementHeader
{
    /*
     * Attributes of a meaning object definition
     */
    protected String      name = null;
    protected String      description = null;


    /**
     * Typical Constructor
     *
     * @param parentAsset descriptor for parent asset
     * @param type details of the metadata type for this properties object
     * @param guid String unique id
     * @param url String URL
     * @param classifications enumeration of classifications
     * @param name name of the glossary term
     * @param description description of the glossary term.
     */
    public Meaning(AssetDescriptor parentAsset,
                   ElementType     type,
                   String          guid,
                   String          url,
                   Classifications classifications,
                   String          name,
                   String          description)
    {
        super(parentAsset, type, guid, url, classifications);

        this.name = name;
        this.description = description;
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset descriptor for parent asset
     * @param templateMeaning element to copy
     */
    public Meaning(AssetDescriptor parentAsset, Meaning templateMeaning)
    {
        /*
         * Save the parent asset description.
         */
        super(parentAsset, templateMeaning);

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
     * Return the description of the glossary term.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Meaning{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
                '}';
    }
}