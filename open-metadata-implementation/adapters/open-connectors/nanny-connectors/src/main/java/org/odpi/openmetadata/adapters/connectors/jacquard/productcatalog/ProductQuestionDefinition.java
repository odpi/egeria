/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * The ProductQuestionDefinition is used to populate the questions associated with the glossary.
 */
public enum ProductQuestionDefinition
{

    USER_ACCOUNT_STATUS("User Account Status",
                        "What is the status of the user accounts used to access the open metadata ecosystem?",
                        "User accounts may be in one of 4 statuses:  Active, Disabled, Credentials Expired, or Locked.  Accounts must be in active status for the user to be granted access.  The security officer is interested in those accounts that are not active since this may represent a user who is in difficulty, or an error in the security set up.",
                        "https://egeria-project.org/concepts/user-identity/",
                        ProductPerspectiveDefinition.SECURITY_OFFICER),

    USER_ACCOUNT_PROFILE("User Type Profile",
                        "What is the spread of the different types of the user accounts used to access the open metadata ecosystem?",
                        "User accounts may be for one of four types of user:  Employee, Contractor, External, or Digital.  Changes to profile of user account types may indicate a changing user base, and hence risk to the open metadata ecosystem.",
                        "https://egeria-project.org/concepts/user-identity/",
                        ProductPerspectiveDefinition.SECURITY_OFFICER),


    ;

    private final String                       displayName;
    private final String                       summary;
    private final String                       description;
    private final String                       url;
    private final ProductPerspectiveDefinition perspective;


    /**
     * The constructor creates an instance of the enum
     *
     * @param displayName  unique id for the enum
     * @param summary      name for the enum
     * @param description  description of the use of this value
     * @param url          optional url for the term
     * @param perspective  perspective that this question is associated with
     */
    ProductQuestionDefinition(String                       displayName,
                              String                       summary,
                              String                       description,
                              String                       url,
                              ProductPerspectiveDefinition perspective)
    {
        this.displayName  = displayName;
        this.summary      = summary;
        this.description  = description;
        this.url          = url;
        this.perspective  = perspective;
    }


    /**
     * Return the qualified name to use for the glossary term.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return OpenMetadataType.GLOSSARY_TERM.typeName + "::Jacquard::Question::" + displayName;
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
    public String getSummary()
    {
        return summary;
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
     * Return the perspective that this question is associated with.
     *
     * @return ProductPerspectiveDefinition
     */
    public ProductPerspectiveDefinition getPerspective()
    {
        return perspective;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProductQuestionDefinition{" + summary + '}';
    }
}
