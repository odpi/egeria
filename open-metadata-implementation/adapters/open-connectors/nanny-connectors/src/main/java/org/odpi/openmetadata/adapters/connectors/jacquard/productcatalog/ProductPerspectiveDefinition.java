/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * The ProductPerspectiveDefinition is used to populate the perspectives associated with the digital products.
 */
public enum ProductPerspectiveDefinition
{

    SECURITY_OFFICER("Security Officer",
         "SECURITY-OFFICER-PERSPECTIVE",
         "An actor responsible for ensuring the security of the organization's resources.",
         "https://egeria-project.org/practices/coco-pharmaceuticals/personas/ivor-padlock/"),
    ;

    private final String displayName;
    private final String identifier;
    private final String description;
    private final String url;


    /**
     * The constructor creates an instance of the enum
     *
     * @param displayName  name for the enum
     * @param identifier      identifier for the enum
     * @param description  description of the use of this value
     * @param url          optional url for the term
     */
    ProductPerspectiveDefinition(String displayName,
                                 String identifier,
                                 String description,
                                 String url)
    {
        this.displayName = displayName;
        this.identifier  = identifier;
        this.description = description;
        this.url          = url;
    }


    /**
     * Return the qualified name to use for the glossary term.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return OpenMetadataType.PERSPECTIVE.typeName + "::" + displayName;
    }


    /**
     * Return the display name for this term.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the summary for this term.
     *
     * @return string
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Return the description for this term.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the URL for this term.
     *
     * @return string
     */
    public String getURL()
    {
        return url;
    }


    /**
     * Return the folder for this term.
     *
     * @return ProductFolderDefinition
     */
    public ProductFolderDefinition getFolder()
    {
        return ProductFolderDefinition.GLOSSARY_QUESTIONS;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProductQuestionDefinition{" + identifier + '}';
    }
}
