/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.ScopeDefinition;


/**
 * The ExceptionTypeDefinition is used to feed the definition of the standard Exception Types for
 * Egeria's connectors.
 */
public enum ExceptionTypeDefinition
{
    /**
     * Missing User Account Exception
     */
    MISSING_USER_ACCOUNT("40b941d8-fcba-49e8-9e9d-85090e6e817c",
                         "MISSING-USER-ACCOUNT",
                         "Missing User Account Exception",
                         "A UserIdentity element stored in open metadata does not have a corresponding UserAccount defined for the user.",
                         "This exception is raised by OMAGServerPlatformCataloguer when a UserIdentity element is found without a corresponding UserAccount in the platform's security connector.",
                         "This exception provides information about missing user accounts in the platform's security connector.  It indicates that the user is not connecting to the platform and therefore cannot access resources or participate in the open metadata ecosystem.",
                         ScopeDefinition.WITHIN_ORGANIZATION,
                         false,
                         null,
                         null),

    /**
     * Missing User Identity Exception
     */
    MISSING_USER_IDENTITY("7998ac6d-b9db-4005-8d7c-9724484931c5",
                         "MISSING-USER-IDENTITY",
                         "Missing User Identity Exception",
                         "The platform's user directory has an active user account with no corresponding UserIdentity element stored in open metadata.",
                         "This exception is raised by OMAGServerPlatformCataloguer when a UserIdentity element is not found for one of the user account's in the platform's security connector.",
                         "This exception provides information about unexpected user accounts in the platform's security connector.  This user account could be used to connect to the platform and access the open metadata ecosystem.",
                         ScopeDefinition.WITHIN_ORGANIZATION,
                         false,
                         null,
                         null),

    /**
     * Missing User Identity Exception
     */
    CLEAR_TEXT_PASSWORD("769e2f89-a940-4f32-abbc-a427c78c14ca",
                          "CLEAR-TEXT-PASSWORD",
                          "Clear Text Password Exception",
                          "The platform's user directory has an active user account with a clear text password.",
                          "This exception is raised by OMAGServerPlatformCataloguer when a user account has a clear text password defined.",
                          "This exception provides information about unsecured user accounts in the platform's security connector.  The credentials for this user account should be reset as soon as possible.  Update the user account to CREDENTIALS_EXPIRED if the user account is active, so the password will be updated on the next login, or disable the account.",
                          ScopeDefinition.WITHIN_ORGANIZATION,
                          false,
                          null,
                          null),
    ;


    private final String          guid;
    private final String          identifier;
    private final String          displayName;
    private final String          summary;
    private final String          description;
    private final String          details;
    private final ScopeDefinition scope;
    private final boolean         isTemplate;
    private final String          templateName;
    private final String          templateDescription;

    /**
     * The constructor creates an instance of the enum
     *
     * @param guid            unique id for the enum
     * @param identifier          identifier of the enum
     * @param displayName          title of the enum
     * @param summary        short description for the enum
     * @param description   description of how to use this value
     * @param details qualifying details
     * @param scope usage scope
     */
    ExceptionTypeDefinition(String          guid,
                            String          identifier,
                            String          displayName,
                            String          summary,
                            String          description,
                            String          details,
                            ScopeDefinition scope,
                            boolean         isTemplate,
                            String          templateName,
                            String          templateDescription)
    {
        this.guid                = guid;
        this.identifier          = identifier;
        this.displayName         = displayName;
        this.summary             = summary;
        this.description         = description;
        this.details             = details;
        this.scope               = scope;
        this.isTemplate          = isTemplate;
        this.templateName        = templateName;
        this.templateDescription = templateDescription;
    }

    /**
     * Return the unique name of the certification type.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "ExceptionType:: " + identifier + ":: " + displayName;
    }


    /**
     * Return the GUID for the element.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the identifier for the certification type.
     *
     * @return string
     */
    public String getIdentifier()
    {
        return identifier;
    }

    /**
     * Return the title of the certification type.
     *
     * @return string
     */
    public String getDisplayName() { return displayName; }

    /**
     * Return the summary of the certification type.
     *
     * @return string
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Return the description of the certification type.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Return the details of how the certification type works.
     *
     * @return string
     */
    public String getDetails() { return details; }


    /**
     * Return the scope of usage for this information supply chain.
     *
     * @return string
     */
    public ScopeDefinition getScope() {return scope; }

    /**
     * Return whether this is a template or not.
     *
     * @return boolean
     */
    public boolean isTemplate()
    {
        return isTemplate;
    }


    /**
     * Return the template name.
     *
     * @return string
     */
    public String getTemplateName()
    {
        return templateName;
    }


    /**
     * Return the template description.
     *
     * @return string
     */
    public String getTemplateDescription()
    {
        return templateDescription;
    }



    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ExceptionTypeDefinition{" + summary + '}';
    }
}
