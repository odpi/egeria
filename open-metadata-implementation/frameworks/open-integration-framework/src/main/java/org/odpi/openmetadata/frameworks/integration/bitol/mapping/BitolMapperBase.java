/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolAuthoritativeDefinition;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDescription;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolStatus;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolSupportChannel;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolSupportTool;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolTeam;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolTeamMember;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContactMethodType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContactDetailsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContactThroughProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.OrganizationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleAppointmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.SearchKeywordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ScopedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BitolMapperBase holds the mapping logic that is shared by the Open Data Contract Standard (ODCS) and Open Data
 * Product Standard (ODPS) mappers: the fundamentals of a document (identity, status, description, tags), custom
 * properties, authoritative definitions, support channels and teams.  The mappers work through the connector context
 * clients so that they can run inside an integration connector (through the IntegrationContext) or inside a view
 * service that builds a ConnectorContextBase for the calling user.
 * <br><br>
 * Every element created from a document has a deterministic qualifiedName built from the document's identifier and
 * version, so that republishing a document updates the existing elements rather than duplicating them, and each
 * version of a document is a distinct element.  The document identifier is also recorded as an ExternalId shared by
 * all versions.
 */
public abstract class BitolMapperBase
{
    /**
     * Prefix used in additionalProperties for values that have no home in the open metadata types.
     */
    public static final String ADDITIONAL_PROPERTY_PREFIX = "bitol.";

    /**
     * Separator used in qualified names.
     */
    public static final String SEPARATOR = "::";

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    protected final ConnectorContextBase context;


    /**
     * Constructor.
     *
     * @param context connector context providing access to the open metadata clients
     */
    protected BitolMapperBase(ConnectorContextBase context)
    {
        this.context = context;
    }


    /*
     * ==========================================================================================================
     * Naming
     */

    /**
     * Build the qualified name for the element that represents a document version.
     *
     * @param kind kind of document
     * @param id document identifier
     * @param version document version (may be null)
     * @return qualified name
     */
    public static String getDocumentQualifiedName(String kind,
                                                  String id,
                                                  String version)
    {
        if ((version == null) || (version.isBlank()))
        {
            return kind + SEPARATOR + id;
        }

        return kind + SEPARATOR + id + SEPARATOR + version;
    }


    /**
     * Build the qualified name for the ExternalId that carries a document identifier.  It is shared by all versions.
     *
     * @param kind kind of document
     * @param id document identifier
     * @return qualified name
     */
    public static String getExternalIdQualifiedName(String kind,
                                                    String id)
    {
        return OpenMetadataType.EXTERNAL_ID.typeName + SEPARATOR + kind + SEPARATOR + id;
    }


    /*
     * ==========================================================================================================
     * Look-ups
     */

    /**
     * Return the unique identifier of the element with the supplied qualified name, or null if there is none.
     *
     * @param qualifiedName qualified name to look for
     * @return guid or null
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    protected String findElementGUID(String qualifiedName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        OpenMetadataElement element = context.getOpenMetadataStore().getMetadataElementByUniqueName(qualifiedName,
                                                                                                    OpenMetadataProperty.QUALIFIED_NAME.name);

        if (element != null)
        {
            return element.getElementGUID();
        }

        return null;
    }


    /**
     * Return the elements identified by a document identifier, found through the ExternalId that all versions of
     * the document share.
     *
     * @param kind kind of document
     * @param id document identifier
     * @return list of elements (empty if none)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    protected List<OpenMetadataRootElement> findElementsByDocumentId(String kind,
                                                                     String id) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "findElementsByDocumentId";

        String externalIdGUID = findElementGUID(getExternalIdQualifiedName(kind, id));

        if (externalIdGUID != null)
        {
            /*
             * The ExternalIdLink relationship has the identified element at end 1 and the ExternalId at end 2.
             */
            List<OpenMetadataRootElement> elements = context.getClassificationExplorerClient().getRelatedElements(externalIdGUID,
                                                                                                                 OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName,
                                                                                                                 2,
                                                                                                                 null,
                                                                                                                 null,
                                                                                                                 context.getClassificationExplorerClient().getQueryOptions(),
                                                                                                                 methodName);

            if (elements != null)
            {
                return elements;
            }
        }

        return List.of();
    }


    /**
     * Return the element with the highest version identifier from the supplied list.  Versions are compared as
     * dot-separated numbers where possible, otherwise as strings.
     *
     * @param elements candidate elements
     * @return chosen element or null if the list is empty
     */
    protected OpenMetadataRootElement getLatestVersion(List<OpenMetadataRootElement> elements)
    {
        OpenMetadataRootElement latest        = null;
        String                  latestVersion = null;

        for (OpenMetadataRootElement element : elements)
        {
            if (element != null)
            {
                String version = null;

                if (element.getProperties() instanceof ReferenceableProperties referenceableProperties)
                {
                    version = referenceableProperties.getVersionIdentifier();
                }

                if ((latest == null) || (compareVersions(version, latestVersion) > 0))
                {
                    latest        = element;
                    latestVersion = version;
                }
            }
        }

        return latest;
    }


    /**
     * Compare two version strings such as "1.2.0" and "1.10.0".
     *
     * @param version1 first version (may be null)
     * @param version2 second version (may be null)
     * @return negative, zero or positive as version1 is lower than, equal to or higher than version2
     */
    static int compareVersions(String version1,
                               String version2)
    {
        if (version1 == null)
        {
            return (version2 == null) ? 0 : -1;
        }
        if (version2 == null)
        {
            return 1;
        }

        String[] parts1 = version1.replaceFirst("^[vV]", "").split("[.\\-]");
        String[] parts2 = version2.replaceFirst("^[vV]", "").split("[.\\-]");

        for (int i = 0; i < Math.max(parts1.length, parts2.length); i++)
        {
            String part1 = (i < parts1.length) ? parts1[i] : "0";
            String part2 = (i < parts2.length) ? parts2[i] : "0";

            int result;

            try
            {
                result = Integer.compare(Integer.parseInt(part1), Integer.parseInt(part2));
            }
            catch (NumberFormatException notNumeric)
            {
                result = part1.compareTo(part2);
            }

            if (result != 0)
            {
                return result;
            }
        }

        return 0;
    }


    /*
     * ==========================================================================================================
     * Fundamentals
     */

    /**
     * Copy the fundamentals of a document into the properties of the element that represents it.
     *
     * @param document document
     * @param properties properties to fill
     * @param qualifiedName qualified name for the element
     */
    protected void fillFundamentals(BitolDocument                   document,
                                    AuthoredReferenceableProperties properties,
                                    String                          qualifiedName)
    {
        properties.setQualifiedName(qualifiedName);
        properties.setIdentifier(document.getId());
        properties.setVersionIdentifier(document.getVersion());
        properties.setDisplayName((document.getName() != null) ? document.getName() : document.getId());

        if (document.getDescription() != null)
        {
            properties.setDescription(document.getDescription().getPurpose());
        }

        setContentStatus(document.getStatus(), properties);

        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "apiVersion", document.getApiVersion());
        additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "kind", document.getKind());

        if (document.getDomain() != null)
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "domain", document.getDomain());
        }
        if (document.getTenant() != null)
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "tenant", document.getTenant());
        }

        addDescriptionDetails(document.getDescription(), additionalProperties);
        addCustomProperties(document.getCustomProperties(), additionalProperties);

        properties.setAdditionalProperties(additionalProperties);
    }


    /**
     * Map a Bitol status onto the content status of an element.  Values outside the standard set are recorded as
     * OTHER with the original value in userDefinedContentStatus.
     *
     * @param status status from the document
     * @param properties properties to update
     */
    protected void setContentStatus(String                          status,
                                    AuthoredReferenceableProperties properties)
    {
        BitolStatus bitolStatus = BitolStatus.fromValue(status);

        if (bitolStatus != null)
        {
            switch (bitolStatus)
            {
                case PROPOSED   -> properties.setContentStatus(ContentStatus.PROPOSED);
                case DRAFT      -> properties.setContentStatus(ContentStatus.DRAFT);
                case ACTIVE     -> properties.setContentStatus(ContentStatus.ACTIVE);
                case DEPRECATED -> properties.setContentStatus(ContentStatus.DEPRECATED);
                case RETIRED    -> properties.setContentStatus(ContentStatus.OBSOLETE);
            }
        }
        else if (status != null)
        {
            properties.setContentStatus(ContentStatus.OTHER);
            properties.setUserDefinedContentStatus(status);
        }
    }


    /**
     * Return whether the document has been retired, in which case its element is deleted rather than maintained.
     *
     * @param document document
     * @return boolean
     */
    protected boolean isRetired(BitolDocument document)
    {
        return BitolStatus.fromValue(document.getStatus()) == BitolStatus.RETIRED;
    }


    /**
     * Copy the limitations and usage of a description into additional properties.
     *
     * @param description description structure (may be null)
     * @param additionalProperties map to add to
     */
    protected void addDescriptionDetails(BitolDescription    description,
                                         Map<String, String> additionalProperties)
    {
        if (description != null)
        {
            if (description.getLimitations() != null)
            {
                additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "limitations", description.getLimitations());
            }
            if (description.getUsage() != null)
            {
                additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "usage", description.getUsage());
            }

            addCustomProperties(description.getCustomProperties(), additionalProperties);
        }
    }


    /**
     * Copy custom properties into additional properties.  Values that are not simple strings are rendered with
     * toString so that lists and structures are still visible.
     *
     * @param customProperties custom properties from the document (may be null)
     * @param additionalProperties map to add to
     */
    protected void addCustomProperties(List<BitolCustomProperty> customProperties,
                                       Map<String, String>       additionalProperties)
    {
        if (customProperties != null)
        {
            for (BitolCustomProperty customProperty : customProperties)
            {
                if ((customProperty != null) && (customProperty.getProperty() != null))
                {
                    additionalProperties.put(customProperty.getProperty(),
                                             (customProperty.getValue() == null) ? "" : customProperty.getValue().toString());
                }
            }
        }
    }


    /**
     * Return a value from a document as a string for additionalProperties.
     *
     * @param value value (may be null)
     * @return string or null
     */
    protected String toStringValue(Object value)
    {
        return (value == null) ? null : value.toString();
    }


    /*
     * ==========================================================================================================
     * Shared sections
     */

    /**
     * Record the document identifier as an ExternalId linked to the element.  The ExternalId is shared by all
     * versions of the document, so it is created once and linked to each version's element.
     *
     * @param kind kind of document
     * @param id document identifier
     * @param elementGUID element representing this version
     * @param source description of where the document came from
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    protected void linkExternalId(String kind,
                                  String id,
                                  String elementGUID,
                                  String source) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        String externalIdQualifiedName = getExternalIdQualifiedName(kind, id);
        String externalIdGUID          = findElementGUID(externalIdQualifiedName);

        ExternalIdLinkProperties linkProperties = new ExternalIdLinkProperties();

        linkProperties.setSource(source);
        linkProperties.setLastSynchronized(new Date());

        if (externalIdGUID == null)
        {
            ExternalIdProperties externalIdProperties = new ExternalIdProperties();

            externalIdProperties.setQualifiedName(externalIdQualifiedName);
            externalIdProperties.setIdentifier(id);
            externalIdProperties.setKeyPattern(KeyPattern.STABLE_KEY);
            externalIdProperties.setExternalInstanceTypeName(kind);

            context.getExternalIdClient().createExternalId(elementGUID, null, linkProperties, externalIdProperties);
        }
        else
        {
            context.getExternalIdClient().linkExternalIdToElement(elementGUID,
                                                                  externalIdGUID,
                                                                  context.getExternalIdClient().getMakeAnchorOptions(false),
                                                                  linkProperties);
        }
    }


    /**
     * Attach the document's tags to the element as search keywords.
     *
     * @param tags tags from the document (may be null)
     * @param elementGUID element to tag
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    protected void addSearchKeywords(List<String> tags,
                                     String       elementGUID) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        if (tags != null)
        {
            for (String tag : tags)
            {
                if ((tag != null) && (! tag.isBlank()))
                {
                    SearchKeywordProperties properties = new SearchKeywordProperties();

                    properties.setDisplayName(tag);

                    context.getSearchKeywordClient().addSearchKeywordToElement(elementGUID,
                                                                               context.getSearchKeywordClient().getMetadataSourceOptions(),
                                                                               null,
                                                                               properties);
                }
            }
        }
    }


    /**
     * Link the element to the organization named as the document's tenant, creating the organization profile if it
     * is not already known.  The element is scoped by the organization.
     *
     * @param tenant tenant from the document (may be null)
     * @param elementGUID element to link
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    protected void linkTenantOrganization(String tenant,
                                          String elementGUID) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        if ((tenant != null) && (! tenant.isBlank()))
        {
            String qualifiedName    = OpenMetadataType.ORGANIZATION.typeName + SEPARATOR + tenant;
            String organizationGUID = findElementGUID(qualifiedName);

            if (organizationGUID == null)
            {
                OrganizationProperties properties = new OrganizationProperties();

                properties.setQualifiedName(qualifiedName);
                properties.setDisplayName(tenant);

                organizationGUID = context.getActorProfileClient(OpenMetadataType.ORGANIZATION.typeName).createActorProfile(new NewElementOptions(context.getActorProfileClient().getMetadataSourceOptions()),
                                                                                                                             null,
                                                                                                                             properties,
                                                                                                                             null);
            }

            context.getClassificationExplorerClient().addScopeToElement(elementGUID,
                                                                        organizationGUID,
                                                                        context.getClassificationExplorerClient().getMakeAnchorOptions(false),
                                                                        new ScopedByProperties());
        }
    }


    /**
     * Link the element to external references for the document's authoritative definitions, creating each reference
     * if its URL is not already known.
     *
     * @param authoritativeDefinitions definitions from the document (may be null)
     * @param elementGUID element to link
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    protected void linkAuthoritativeDefinitions(List<BitolAuthoritativeDefinition> authoritativeDefinitions,
                                                String                             elementGUID) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        if (authoritativeDefinitions != null)
        {
            for (BitolAuthoritativeDefinition definition : authoritativeDefinitions)
            {
                if ((definition != null) && (definition.getUrl() != null))
                {
                    String qualifiedName        = OpenMetadataType.EXTERNAL_REFERENCE.typeName + SEPARATOR + definition.getUrl();
                    String externalReferenceGUID = findElementGUID(qualifiedName);

                    if (externalReferenceGUID == null)
                    {
                        ExternalReferenceProperties properties = new ExternalReferenceProperties();

                        properties.setQualifiedName(qualifiedName);
                        properties.setDisplayName(definition.getUrl());
                        properties.setReferenceTitle(definition.getType());
                        properties.setDescription(definition.getDescription());
                        properties.setURL(definition.getUrl());

                        externalReferenceGUID = context.getExternalReferenceClient().createExternalReference(new NewElementOptions(context.getExternalReferenceClient().getMetadataSourceOptions()),
                                                                                                             null,
                                                                                                             properties,
                                                                                                             null);
                    }

                    ExternalReferenceLinkProperties linkProperties = new ExternalReferenceLinkProperties();

                    linkProperties.setLabel(definition.getType());
                    linkProperties.setDescription(definition.getDescription());

                    context.getExternalReferenceClient().linkExternalReference(elementGUID,
                                                                               externalReferenceGUID,
                                                                               context.getExternalReferenceClient().getMakeAnchorOptions(false),
                                                                               linkProperties);
                }
            }
        }
    }


    /**
     * Create contact details for each of the document's support channels and link them to the element.  The contact
     * details are anchored to the element so they are removed with it.
     *
     * @param supportChannels channels from the document (may be null)
     * @param elementGUID element to link
     * @param elementQualifiedName qualified name of the element (used to build the contact details' names)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    protected void linkSupportChannels(List<BitolSupportChannel> supportChannels,
                                       String                    elementGUID,
                                       String                    elementQualifiedName) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        if (supportChannels != null)
        {
            for (BitolSupportChannel channel : supportChannels)
            {
                if ((channel != null) && (channel.getChannel() != null))
                {
                    String qualifiedName     = elementQualifiedName + SEPARATOR + "Support" + SEPARATOR + channel.getChannel();
                    String contactDetailsGUID = findElementGUID(qualifiedName);

                    ContactDetailsProperties properties = new ContactDetailsProperties();

                    properties.setQualifiedName(qualifiedName);
                    properties.setDisplayName(channel.getChannel());
                    properties.setDescription(channel.getDescription());
                    properties.setContactType(channel.getScope());
                    properties.setContactMethodType(getContactMethodType(channel.getTool()));
                    properties.setContactMethodService(channel.getTool());
                    properties.setContactMethodValue(channel.getUrl());

                    Map<String, String> additionalProperties = new HashMap<>();

                    if (channel.getInvitationUrl() != null)
                    {
                        additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "invitationUrl", channel.getInvitationUrl());
                    }
                    addCustomProperties(channel.getCustomProperties(), additionalProperties);
                    properties.setAdditionalProperties(additionalProperties);

                    if (contactDetailsGUID == null)
                    {
                        NewElementOptions newElementOptions = new NewElementOptions(context.getContactDetailsClient().getMetadataSourceOptions());

                        newElementOptions.setAnchorGUID(elementGUID);
                        newElementOptions.setIsOwnAnchor(false);
                        newElementOptions.setParentGUID(elementGUID);
                        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName);
                        newElementOptions.setParentAtEnd1(true);

                        contactDetailsGUID = context.getContactDetailsClient().createContactDetails(newElementOptions,
                                                                                                    null,
                                                                                                    properties,
                                                                                                    new ContactThroughProperties());
                    }
                    else
                    {
                        context.getContactDetailsClient().updateContactDetails(contactDetailsGUID,
                                                                               context.getContactDetailsClient().getUpdateOptions(false),
                                                                               properties);
                    }

                    linkAuthoritativeDefinitions(channel.getAuthoritativeDefinitions(), contactDetailsGUID);
                }
            }
        }
    }


    /**
     * Map a Bitol support tool onto a contact method type.
     *
     * @param tool tool from the document (may be null)
     * @return enum
     */
    protected ContactMethodType getContactMethodType(String tool)
    {
        BitolSupportTool supportTool = BitolSupportTool.fromValue(tool);

        if (supportTool == null)
        {
            return ContactMethodType.OTHER;
        }

        return switch (supportTool)
        {
            case EMAIL -> ContactMethodType.EMAIL;
            case SLACK, TEAMS, DISCORD, GOOGLE_CHAT -> ContactMethodType.CHAT;
            case TICKET, OTHER -> ContactMethodType.OTHER;
        };
    }


    /**
     * Create the team and the roles for the team members described in the document.  The team is a Team profile
     * named from the document; each member becomes a PersonRole scoped to the element (ScopedBy).  Where the member's
     * username is known to Egeria as a user identity with a profile, the profile is appointed to the role; otherwise
     * the username and name are recorded on the role's additional properties and reported as a warning.
     *
     * @param team team from the document (may be null)
     * @param elementGUID element that the roles are scoped to
     * @param elementQualifiedName qualified name of the element (used to build the roles' names)
     * @param result result to record warnings in
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    protected void linkTeam(BitolTeam          team,
                            String             elementGUID,
                            String             elementQualifiedName,
                            BitolMappingResult result) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        if (team == null)
        {
            return;
        }

        String teamGUID = null;

        if ((team.getName() != null) && (! team.getName().isBlank()))
        {
            String teamQualifiedName = OpenMetadataType.TEAM.typeName + SEPARATOR + team.getName();

            teamGUID = findElementGUID(teamQualifiedName);

            if (teamGUID == null)
            {
                TeamProperties properties = new TeamProperties();

                properties.setQualifiedName(teamQualifiedName);
                properties.setDisplayName(team.getName());
                properties.setDescription(team.getDescription());

                Map<String, String> additionalProperties = new HashMap<>();
                addCustomProperties(team.getCustomProperties(), additionalProperties);
                properties.setAdditionalProperties(additionalProperties);

                teamGUID = context.getActorProfileClient(OpenMetadataType.TEAM.typeName).createActorProfile(new NewElementOptions(context.getActorProfileClient().getMetadataSourceOptions()),
                                                                                                             null,
                                                                                                             properties,
                                                                                                             null);

                addSearchKeywords(team.getTags(), teamGUID);
                linkAuthoritativeDefinitions(team.getAuthoritativeDefinitions(), teamGUID);
            }

            /*
             * The team's work is scoped to the element (for example the data product).
             */
            context.getClassificationExplorerClient().addScopeToElement(teamGUID,
                                                                        elementGUID,
                                                                        context.getClassificationExplorerClient().getMakeAnchorOptions(false),
                                                                        new ScopedByProperties());
        }

        if (team.getMembers() != null)
        {
            for (BitolTeamMember member : team.getMembers())
            {
                if ((member != null) && (member.getUsername() != null))
                {
                    linkTeamMember(member, teamGUID, elementGUID, elementQualifiedName, result);
                }
            }
        }
    }


    /**
     * Create the role for one team member and appoint the member's profile if it can be found.
     *
     * @param member member from the document
     * @param teamGUID team profile (may be null)
     * @param elementGUID element that the role is scoped to
     * @param elementQualifiedName qualified name of the element
     * @param result result to record warnings in
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void linkTeamMember(BitolTeamMember    member,
                                String             teamGUID,
                                String             elementGUID,
                                String             elementQualifiedName,
                                BitolMappingResult result) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        String roleName          = (member.getRole() != null) ? member.getRole() : "Team member";
        String roleQualifiedName = elementQualifiedName + SEPARATOR + "Role" + SEPARATOR + member.getUsername();
        String roleGUID          = findElementGUID(roleQualifiedName);

        PersonRoleProperties properties = new PersonRoleProperties();

        properties.setQualifiedName(roleQualifiedName);
        properties.setDisplayName(roleName);
        properties.setDescription(member.getDescription());
        properties.setScope(elementQualifiedName);
        properties.setIdentifier(member.getUsername());

        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "username", member.getUsername());
        if (member.getName() != null)
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "name", member.getName());
        }
        if (member.getDateIn() != null)
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "dateIn", member.getDateIn());
        }
        if (member.getDateOut() != null)
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "dateOut", member.getDateOut());
        }
        if (member.getReplacedByUsername() != null)
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "replacedByUsername", member.getReplacedByUsername());
        }
        addCustomProperties(member.getCustomProperties(), additionalProperties);
        properties.setAdditionalProperties(additionalProperties);

        if (roleGUID == null)
        {
            NewElementOptions newElementOptions = new NewElementOptions(context.getActorRoleClient().getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(elementGUID);
            newElementOptions.setIsOwnAnchor(false);

            roleGUID = context.getActorRoleClient(OpenMetadataType.PERSON_ROLE.typeName).createActorRole(newElementOptions, null, properties, null);

            context.getClassificationExplorerClient().addScopeToElement(roleGUID,
                                                                        elementGUID,
                                                                        context.getClassificationExplorerClient().getMakeAnchorOptions(false),
                                                                        new ScopedByProperties());
        }
        else
        {
            context.getActorRoleClient().updateActorRole(roleGUID, context.getActorRoleClient().getUpdateOptions(false), properties);
        }

        addSearchKeywords(member.getTags(), roleGUID);
        linkAuthoritativeDefinitions(member.getAuthoritativeDefinitions(), roleGUID);

        /*
         * Appoint the person if their user identity is known.
         */
        OpenMetadataRootElement profile = null;

        try
        {
            profile = context.getActorProfileClient().getActorProfileByUserId(member.getUsername(), context.getActorProfileClient().getGetOptions());
        }
        catch (InvalidParameterException notKnown)
        {
            /*
             * Not known - recorded below.
             */
        }

        if ((profile != null) && (profile.getElementHeader() != null))
        {
            PersonRoleAppointmentProperties appointment = new PersonRoleAppointmentProperties();

            appointment.setEffectiveFrom(parseDate(member.getDateIn()));
            appointment.setEffectiveTo(parseDate(member.getDateOut()));

            context.getActorRoleClient().linkPersonRoleToProfile(roleGUID,
                                                                 profile.getElementHeader().getGUID(),
                                                                 context.getActorRoleClient().getMakeAnchorOptions(false),
                                                                 appointment);
        }
        else
        {
            result.addWarning("Team member " + member.getUsername() + " (" + roleName + ") has no user identity with a profile in open metadata; the role " + roleQualifiedName + " records the username but is not appointed");
        }

        if (teamGUID != null)
        {
            /*
             * The role belongs to the team as well as being scoped to the element.
             */
            context.getClassificationExplorerClient().addScopeToElement(roleGUID,
                                                                        teamGUID,
                                                                        context.getClassificationExplorerClient().getMakeAnchorOptions(false),
                                                                        new ScopedByProperties());
        }
    }


    /**
     * Parse an ISO date (yyyy-MM-dd) from the document.
     *
     * @param value date string (may be null)
     * @return date or null if missing or malformed
     */
    protected Date parseDate(String value)
    {
        if (value != null)
        {
            try
            {
                return new SimpleDateFormat(DATE_FORMAT).parse(value);
            }
            catch (ParseException error)
            {
                return null;
            }
        }

        return null;
    }
}
