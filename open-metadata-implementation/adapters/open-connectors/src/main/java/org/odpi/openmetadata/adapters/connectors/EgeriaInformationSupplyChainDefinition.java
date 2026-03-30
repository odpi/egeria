/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors;

import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ScopeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;

/**
 * A description of the predefined information supply chains.  There are two formats - one for templates and
 * the other for normal information supply chains.
 */
public enum EgeriaInformationSupplyChainDefinition
{
    /**
     * Standard template
     */
    INFORMATION_SUPPLY_CHAIN_TEMPLATE("ba3ab0dd-3ec5-4ec5-9db9-f3dc56e3a732",
                                      PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                                      PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                      ScopeDefinition.TEMPLATE_PLACEHOLDER,
                                      null,
                                      null,
                                      null,
                                      false,
                                      null,
                                      "Standard information supply chain template",
                                      "Create a new type of information supply chain"),

    /**
     * Open Metadata Highway
     */
    OPEN_METADATA_HIGHWAY("af7c22ee-0718-4a12-9cf3-ceb8c179cb55",
                    "Open Metadata Highway Information Supply Chain",
                    "Identifies the data flows related to synchronizing open metadata between tools.",
                    ScopeDefinition.WITHIN_SOLUTION,
                    new String[]{ "To ensure the best, and consistent metadata is available to all connected tools." },
                    null,
                    null,
                    false,
                    null),

    /**
     * Dynamic Configuration
     */
    DYNAMIC_CONFIGURATION("07f7699b-d6a7-4722-a919-02d016cf161f",
                          "Dynamic Configuration Information Supply Chain",
                          "Identifies the data flows related to the dynamic configuration of the governance servers using open metadata definitions.",
                          ScopeDefinition.WITHIN_SOLUTION,
                          new String[]{ "To ensure the governance servers are able to pick up new configuration as soon as possible." },
                          null,
                          null,
                          false,
                          null),

    /**
     * Open Metadata Observability
     */
    OPEN_METADATA_OBSERVABILITY("20e59f79-d2e7-4ae1-b995-e9b350f8acdd",
                          "Open Metadata Observability Information Supply Chain",
                          "Identifies the data flows related to the capture of observability data from the Egeria operations.",
                          ScopeDefinition.WITHIN_SOLUTION,
                          new String[]{ "To ensure the Egeria servers and platforms are operating properly." },
                          null,
                          null,
                          false,
                          null),

    /**
     * Security Access Information Supply Chain
     */
    SECURITY("207380ef-a133-4939-b403-027574b797d2",
             "Security Access Information Supply Chain",
             "Identifies the data flows related to access control information such as user accounts, security lists, and security access controls.",
             ScopeDefinition.WITHIN_SOLUTION,
             new String[]{ "To ensure that access to open metadata is controlled." },
             EgeriaRoleDefinition.SECURITY_MANAGER.getGUID(),
             null,
             false,
             null),
    ;


    private final String                                 guid;
    private final String                                 displayName;
    private final String                                 description;
    private final ScopeDefinition                        scope;
    private final String[]                               dataProcessingPurposes;
    private final String                                 ownerGUID;
    private final EgeriaInformationSupplyChainDefinition owningSupplyChain;
    private final boolean                                isOwningInformationSupplyChainAnchor;
    private final EgeriaInformationSupplyChainDefinition anchorScope;

    private boolean isTemplate          = false;
    private String  templateName        = null;
    private String  templateDescription = null;


    /**
     * Construct an enum instance (non-template).
     *
     * @param guid unique identifier
     * @param displayName display name of information supply chain
     * @param description description of information supply chain
     * @param scope scope of information supply chain
     * @param dataProcessingPurposes purposes of information supply chain
     * @param ownerGUID identifier of owner
     * @param owningSupplyChain the parent information supply chain
     * @param isOwningInformationSupplyChainAnchor should the parent supply chain (if any) bee this information supply chain's anchor?
     * @param anchorScope anchor scope for this information supply chain
     */
    EgeriaInformationSupplyChainDefinition(String                 guid,
                                           String                 displayName,
                                           String                 description,
                                           ScopeDefinition        scope,
                                           String[]               dataProcessingPurposes,
                                           String                 ownerGUID,
                                           EgeriaInformationSupplyChainDefinition owningSupplyChain,
                                           boolean                isOwningInformationSupplyChainAnchor,
                                           EgeriaInformationSupplyChainDefinition anchorScope)
    {
        this.guid                                 = guid;
        this.displayName                          = displayName;
        this.description                          = description;
        this.scope                                = scope;
        this.dataProcessingPurposes               = dataProcessingPurposes;
        this.ownerGUID                            = ownerGUID;
        this.owningSupplyChain                    = owningSupplyChain;
        this.isOwningInformationSupplyChainAnchor = isOwningInformationSupplyChainAnchor;
        this.anchorScope                          = anchorScope;
    }


    /**
     * Construct an enum instance (template).
     *
     * @param guid unique identifier
     * @param displayName display name of information supply chain
     * @param description description of information supply chain
     * @param scope scope of information supply chain
     * @param dataProcessingPurposes purposes of information supply chain
     * @param ownerGUID identifier of owner
     * @param owningSupplyChain the parent information supply chain
     * @param isOwningInformationSupplyChainAnchor should the parent supply chain (if any) bee this information supply chain's anchor?
     * @param anchorScope anchor scope for this information supply chain
     * @param templateName is this a template? What is it called?
     * @param templateDescription describe how this template is used
     */
    EgeriaInformationSupplyChainDefinition(String                 guid,
                                           String                 displayName,
                                           String                 description,
                                           ScopeDefinition        scope,
                                           String[] dataProcessingPurposes,
                                           String                 ownerGUID,
                                           EgeriaInformationSupplyChainDefinition owningSupplyChain,
                                           boolean                isOwningInformationSupplyChainAnchor,
                                           EgeriaInformationSupplyChainDefinition anchorScope,
                                           String                 templateName,
                                           String                 templateDescription)
    {
        this.guid                                 = guid;
        this.displayName                          = displayName;
        this.description                          = description;
        this.scope                  = scope;
        this.dataProcessingPurposes = dataProcessingPurposes;
        this.ownerGUID              = ownerGUID;
        this.owningSupplyChain                    = owningSupplyChain;
        this.isOwningInformationSupplyChainAnchor = isOwningInformationSupplyChainAnchor;
        this.anchorScope                          = anchorScope;
        this.isTemplate                           = true;
        this.templateName                         = templateName;
        this.templateDescription                  = templateDescription;
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
     * Return the display name of the information supply chain.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the solution information supply chain.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the scope of the information supply chain.
     *
     * @return string
     */
    public ScopeDefinition getScope()
    {
        return scope;
    }


    /**
     * Return the purposes of the information supply chain.
     *
     * @return list of strings
     */
    public List<String> getDataProcessingPurposes()
    {
        if (dataProcessingPurposes == null)
        {
            return null;

        }

        return Arrays.asList(dataProcessingPurposes);
    }



    /**
     * Return the identifier of the owner.
     *
     * @return string
     */
    public String getOwner()
    {
        return ownerGUID;
    }


    /**
     * Return the type name of the element describing the owner.
     *
     * @return string
     */
    public String getOwnerTypeName()
    {
        return OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName;
    }

    /**
     * Return the property name used to identify the owner.
     *
     * @return string
     */
    public String getOwnerPropertyName()
    {
        return OpenMetadataProperty.GUID.name;
    }


    /**
     * Return the supply chain that this segment is a part of.
     *
     * @return information supply chain
     */
    public String getOwningSupplyChain()
    {
        if (owningSupplyChain != null)
        {
            return owningSupplyChain.getGUID();
        }

        return null;
    }


    /**
     * Should this information supply chain be anchored to its parent?
     *
     * @return boolean
     */
    public boolean isOwningInformationSupplyChainAnchor()
    {
        return isOwningInformationSupplyChainAnchor;
    }

    public String getAnchorScopeGUID()
    {
        if (anchorScope != null)
        {
            return anchorScope.getGUID();
        }
        else
        {
            return guid;
        }
    }

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
     * Return the unique name of the information supply chain.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "InformationSupplyChain::" + displayName;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "InformationSupplyChain{" + displayName + '}';
    }
}
