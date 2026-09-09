/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolAuthoritativeDefinition;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDescription;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolStatus;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolSupportChannel;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolTeam;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolTeamMember;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AttributedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContactDetailsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.SearchKeywordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * BitolGeneratorBase holds the logic shared by the generators that build Open Data Contract Standard (ODCS) and Open
 * Data Product Standard (ODPS) documents from open metadata: the reverse of BitolMapperBase.  It reads the
 * fundamentals, tags, custom properties, authoritative definitions, support channels and team from an element and its
 * related elements.  Values that the cataloguers kept in additional properties under the "bitol." prefix are read
 * back so that a document catalogued by Egeria regenerates faithfully; elements created directly in Egeria produce a
 * document from their native properties.
 */
public abstract class BitolGeneratorBase
{
    protected final ConnectorContextBase context;
    protected final PropertyHelper       propertyHelper = new PropertyHelper();


    /**
     * Constructor.
     *
     * @param context connector context providing access to the open metadata clients
     */
    protected BitolGeneratorBase(ConnectorContextBase context)
    {
        this.context = context;
    }


    /*
     * ==========================================================================================================
     * Fundamentals
     */

    /**
     * Fill the fundamentals of a document from the properties of the element that represents it.  The identifier is
     * the element's identifier property, or its GUID when there is none, so that a round trip preserves the original
     * document identifier while an element created in Egeria still gets a stable id.
     *
     * @param element element representing the document
     * @param document document to fill
     */
    protected void fillFundamentals(OpenMetadataRootElement element,
                                    BitolDocument           document)
    {
        if (element.getProperties() instanceof AuthoredReferenceableProperties properties)
        {
            document.setId((properties.getIdentifier() != null) ? properties.getIdentifier() : element.getElementHeader().getGUID());
            document.setName((properties.getDisplayName() != null) ? properties.getDisplayName() : properties.getQualifiedName());
            document.setVersion(properties.getVersionIdentifier());
            document.setStatus(getBitolStatus(properties.getContentStatus(), properties.getUserDefinedContentStatus()));

            Map<String, String> additionalProperties = properties.getAdditionalProperties();

            document.setDomain(getBitolValue(additionalProperties, "domain"));
            document.setTenant(getBitolValue(additionalProperties, "tenant"));

            String storedApiVersion = getBitolValue(additionalProperties, "apiVersion");

            if (storedApiVersion != null)
            {
                document.setApiVersion(storedApiVersion);
            }

            BitolDescription description = new BitolDescription();

            description.setPurpose(properties.getDescription());
            description.setLimitations(getBitolValue(additionalProperties, "limitations"));
            description.setUsage(getBitolValue(additionalProperties, "usage"));

            if ((description.getPurpose() != null) || (description.getLimitations() != null) || (description.getUsage() != null))
            {
                document.setDescription(description);
            }

            document.setCustomProperties(getCustomProperties(additionalProperties));
        }
        else
        {
            document.setId(element.getElementHeader().getGUID());
        }

        document.setTags(getTags(element));
        document.setAuthoritativeDefinitions(getAuthoritativeDefinitions(element));

        if (document.getTenant() == null)
        {
            document.setTenant(getTenant(element));
        }
    }


    /**
     * Map a content status onto a Bitol status.
     *
     * @param contentStatus status of the element (may be null)
     * @param userDefinedStatus value used when the status is OTHER
     * @return status string or null
     */
    static String getBitolStatus(ContentStatus contentStatus,
                                 String        userDefinedStatus)
    {
        if (contentStatus == null)
        {
            return null;
        }

        return switch (contentStatus)
        {
            case DRAFT, PREPARED -> BitolStatus.DRAFT.getValue();
            case PROPOSED, APPROVED -> BitolStatus.PROPOSED.getValue();
            case ACTIVE -> BitolStatus.ACTIVE.getValue();
            case DEPRECATED -> BitolStatus.DEPRECATED.getValue();
            case OBSOLETE, REJECTED -> BitolStatus.RETIRED.getValue();
            case OTHER -> userDefinedStatus;
        };
    }


    /**
     * Return a value stored by the cataloguers under the bitol prefix.
     *
     * @param additionalProperties additional properties of the element (may be null)
     * @param name name without the prefix
     * @return value or null
     */
    protected static String getBitolValue(Map<String, String> additionalProperties,
                                          String              name)
    {
        if (additionalProperties != null)
        {
            return additionalProperties.get(BitolMapperBase.ADDITIONAL_PROPERTY_PREFIX + name);
        }

        return null;
    }


    /**
     * Return the additional properties that are not bitol bookkeeping as custom properties.
     *
     * @param additionalProperties additional properties of the element (may be null)
     * @return list of custom properties or null if there are none
     */
    protected static List<BitolCustomProperty> getCustomProperties(Map<String, String> additionalProperties)
    {
        if (additionalProperties == null)
        {
            return null;
        }

        List<BitolCustomProperty> customProperties = new ArrayList<>();

        for (Map.Entry<String, String> entry : new TreeMap<>(additionalProperties).entrySet())
        {
            if (! entry.getKey().startsWith(BitolMapperBase.ADDITIONAL_PROPERTY_PREFIX))
            {
                BitolCustomProperty customProperty = new BitolCustomProperty();

                customProperty.setProperty(entry.getKey());
                customProperty.setValue(entry.getValue());

                customProperties.add(customProperty);
            }
        }

        return customProperties.isEmpty() ? null : customProperties;
    }


    /*
     * ==========================================================================================================
     * Shared sections
     */

    /**
     * Return the search keywords of an element as tags.
     *
     * @param element element with its related elements
     * @return list of tags or null
     */
    protected List<String> getTags(AttributedMetadataElement element)
    {
        if (element.getSearchKeywords() == null)
        {
            return null;
        }

        List<String> tags = new ArrayList<>();

        for (RelatedMetadataElementSummary keyword : element.getSearchKeywords())
        {
            if ((keyword != null) && (keyword.getRelatedElement() != null) &&
                (keyword.getRelatedElement().getProperties() instanceof SearchKeywordProperties properties) &&
                (properties.getDisplayName() != null))
            {
                tags.add(properties.getDisplayName());
            }
        }

        return tags.isEmpty() ? null : tags;
    }


    /**
     * Return the external references of an element as authoritative definitions.
     *
     * @param element element with its related elements
     * @return list of definitions or null
     */
    protected List<BitolAuthoritativeDefinition> getAuthoritativeDefinitions(AttributedMetadataElement element)
    {
        if (element.getExternalReferences() == null)
        {
            return null;
        }

        List<BitolAuthoritativeDefinition> definitions = new ArrayList<>();

        for (RelatedMetadataElementSummary reference : element.getExternalReferences())
        {
            if ((reference != null) && (reference.getRelatedElement() != null) &&
                (reference.getRelatedElement().getProperties() instanceof ExternalReferenceProperties properties))
            {
                BitolAuthoritativeDefinition definition = new BitolAuthoritativeDefinition();

                definition.setUrl((properties.getURL() != null) ? properties.getURL() : properties.getDisplayName());
                definition.setType(properties.getReferenceTitle());
                definition.setDescription(properties.getDescription());

                if (reference.getRelationshipProperties() instanceof LabeledRelationshipProperties linkProperties)
                {
                    if (linkProperties.getLabel() != null)
                    {
                        definition.setType(linkProperties.getLabel());
                    }
                    if (linkProperties.getDescription() != null)
                    {
                        definition.setDescription(linkProperties.getDescription());
                    }
                }

                if (definition.getType() == null)
                {
                    definition.setType("reference");
                }

                if (definition.getUrl() != null)
                {
                    definitions.add(definition);
                }
            }
        }

        return definitions.isEmpty() ? null : definitions;
    }


    /**
     * Return the contact details of an element as support channels.
     *
     * @param element element with its related elements
     * @return list of channels or null
     */
    protected List<BitolSupportChannel> getSupportChannels(AttributedMetadataElement element)
    {
        if (element.getContactDetails() == null)
        {
            return null;
        }

        List<BitolSupportChannel> channels = new ArrayList<>();

        for (RelatedMetadataElementSummary contact : element.getContactDetails())
        {
            if ((contact != null) && (contact.getRelatedElement() != null) &&
                (contact.getRelatedElement().getProperties() instanceof ContactDetailsProperties properties))
            {
                BitolSupportChannel channel = new BitolSupportChannel();

                channel.setChannel(properties.getDisplayName());
                channel.setDescription(properties.getDescription());
                channel.setScope(properties.getContactType());
                channel.setTool(properties.getContactMethodService());
                channel.setUrl(properties.getContactMethodValue());
                channel.setInvitationUrl(getBitolValue(properties.getAdditionalProperties(), "invitationUrl"));
                channel.setCustomProperties(getCustomProperties(properties.getAdditionalProperties()));

                if (channel.getChannel() != null)
                {
                    channels.add(channel);
                }
            }
        }

        return channels.isEmpty() ? null : channels;
    }


    /**
     * Return the organization that the element is scoped by as the tenant.
     *
     * @param element element with its related elements
     * @return organization name or null
     */
    protected String getTenant(AttributedMetadataElement element)
    {
        if (element.getRelevantToScopes() != null)
        {
            for (RelatedMetadataElementSummary scope : element.getRelevantToScopes())
            {
                if ((scope != null) && (scope.getRelatedElement() != null) &&
                    (propertyHelper.isTypeOf(scope.getRelatedElement().getElementHeader(), OpenMetadataType.ORGANIZATION.typeName)) &&
                    (scope.getRelatedElement().getProperties() instanceof ReferenceableProperties properties))
                {
                    return properties.getDisplayName();
                }
            }
        }

        return null;
    }


    /**
     * Return the team and person roles scoped to the element as a Bitol team.
     *
     * @param element element with its related elements
     * @return team or null if there is neither a team nor any roles
     */
    protected BitolTeam getTeam(AttributedMetadataElement element)
    {
        if (element.getScopedElements() == null)
        {
            return null;
        }

        BitolTeam             team    = new BitolTeam();
        List<BitolTeamMember> members = new ArrayList<>();

        for (RelatedMetadataElementSummary scoped : element.getScopedElements())
        {
            if ((scoped != null) && (scoped.getRelatedElement() != null))
            {
                MetadataElementSummary scopedElement = scoped.getRelatedElement();

                if ((propertyHelper.isTypeOf(scopedElement.getElementHeader(), OpenMetadataType.TEAM.typeName)) &&
                    (scopedElement.getProperties() instanceof TeamProperties teamProperties))
                {
                    team.setName(teamProperties.getDisplayName());
                    team.setDescription(teamProperties.getDescription());
                    team.setCustomProperties(getCustomProperties(teamProperties.getAdditionalProperties()));
                }
                else if ((propertyHelper.isTypeOf(scopedElement.getElementHeader(), OpenMetadataType.PERSON_ROLE.typeName)) &&
                         (scopedElement.getProperties() instanceof PersonRoleProperties roleProperties))
                {
                    BitolTeamMember     member               = new BitolTeamMember();
                    Map<String, String> additionalProperties = roleProperties.getAdditionalProperties();

                    member.setUsername((getBitolValue(additionalProperties, "username") != null) ? getBitolValue(additionalProperties, "username") : roleProperties.getIdentifier());
                    member.setName(getBitolValue(additionalProperties, "name"));
                    member.setRole(roleProperties.getDisplayName());
                    member.setDescription(roleProperties.getDescription());
                    member.setDateIn(getBitolValue(additionalProperties, "dateIn"));
                    member.setDateOut(getBitolValue(additionalProperties, "dateOut"));
                    member.setReplacedByUsername(getBitolValue(additionalProperties, "replacedByUsername"));
                    member.setCustomProperties(getCustomProperties(additionalProperties));

                    if (member.getUsername() != null)
                    {
                        members.add(member);
                    }
                }
            }
        }

        if (! members.isEmpty())
        {
            team.setMembers(members);
        }

        if ((team.getName() == null) && (team.getMembers() == null))
        {
            return null;
        }

        return team;
    }


    /**
     * Return the display name of a summarized element.
     *
     * @param summary element summary
     * @return display name, or qualified name, or null
     */
    protected String getDisplayName(MetadataElementSummary summary)
    {
        if ((summary != null) && (summary.getProperties() instanceof ReferenceableProperties properties))
        {
            return (properties.getDisplayName() != null) ? properties.getDisplayName() : properties.getQualifiedName();
        }

        return null;
    }

    /**
     * Return the unique identifier of an element for use in an error message.
     *
     * @param element element (may be null)
     * @return guid or "null"
     */
    protected static String getGUID(OpenMetadataRootElement element)
    {
        if ((element != null) && (element.getElementHeader() != null))
        {
            return element.getElementHeader().getGUID();
        }

        return "null";
    }


    /**
     * Return the type name of an element for use in an error message.
     *
     * @param element element (may be null)
     * @return type name or "null"
     */
    protected static String getTypeName(OpenMetadataRootElement element)
    {
        if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getType() != null))
        {
            return element.getElementHeader().getType().getTypeName();
        }

        return "null";
    }
}
