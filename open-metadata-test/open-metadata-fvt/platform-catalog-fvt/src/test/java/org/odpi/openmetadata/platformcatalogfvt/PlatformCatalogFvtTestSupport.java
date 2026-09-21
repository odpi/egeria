/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformcatalogfvt;

import org.odpi.openmetadata.adapters.connectors.controls.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PlatformCatalogFvtTestSupport holds the naming conventions the OMAG Server Platform Cataloguer follows and
 * the helpers the tests use to read the repository back.
 * <br>
 * The naming conventions are reproduced here rather than reached for in the connector, which keeps them
 * private.  That is the point: these are the names other tools look elements up by, so a test that builds
 * the expected name independently will fail if the convention changes, instead of agreeing with whatever the
 * connector now does.
 */
final class PlatformCatalogFvtTestSupport
{
    static final int MAX_PAGE_SIZE = 500;

    /**
     * Strings that identify the elements this suite is responsible for.  Everything the cataloguer creates
     * for this suite carries one of them in a name property, which is how a previous run's debris is found
     * and removed.
     * <ul>
     *     <li>the servers are named {@code platformCatalogFvt...}, and the server name is part of every
     *     server element's qualified name, resource name and display name;</li>
     *     <li>the platform element is named after {@code platform.name} and
     *     {@code platform.organization.name}, both of which carry the hyphenated module name;</li>
     *     <li>a platform element that has been created but never refreshed still has the qualified name the
     *     cataloguer's {@code start()} gave it, which is built from the platform's URL root rather than from
     *     either of those - hence the third marker.</li>
     * </ul>
     */
    static final List<String> SUITE_MARKERS = List.of("platformCatalogFvt",
                                                      "platform-catalog-fvt",
                                                      "OMAG Server Platform::http://localhost:",
                                                      "OMAG Server Platform::http://127.0.0.1:");

    /**
     * The first marker also does duty as the single search string used where only one is wanted.
     */
    static final String SUITE_MARKER = "platformCatalogFvt";

    /**
     * The userId the cataloguer runs under, which is what it records in the createdBy of everything it
     * creates.  It comes from the content pack definition so that a change there cannot leave this suite
     * looking for the wrong name.
     */
    static final String CATALOGUER_USER_ID = IntegrationConnectorDefinition.OMAG_SERVER_PLATFORM_CATALOGUER.getConnectorUserId();

    /**
     * The deployed implementation type the cataloguer stamps on a platform element.
     */
    static final String OMAG_SERVER_PLATFORM_TYPE = EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType();

    private static final PropertyHelper propertyHelper = new PropertyHelper();

    private PlatformCatalogFvtTestSupport()
    {
    }


    /**
     * Build the qualified name a platform element carries.
     * <br>
     * It is built from the platform's address, and nothing else.  The cataloguer's {@code start()} creates
     * the local platform element with this name and its refreshes leave it alone, which is what makes it
     * usable for two things a qualified name has to be good for: telling one running platform from another,
     * and finding the element again on the next start.  The platform's configured name and organization are
     * changeable labels, so they go in the resource name and display name instead.
     *
     * @param platformURLRoot the platform's address
     * @return expected qualified name
     */
    static String expectedPlatformQualifiedName(String platformURLRoot)
    {
        return OMAG_SERVER_PLATFORM_TYPE + "::" + platformURLRoot;
    }


    /**
     * Build the qualified name that an <b>earlier release</b> of the cataloguer gave a platform element on
     * its first refresh.
     * <br>
     * Nothing produces this name any more.  It is here because upgrading from a release that did is a real
     * situation with a real ecosystem behind it, and the connector has to be able to find the element it
     * left under this name rather than catalog the platform all over again beside it.
     *
     * @param organizationName the platform's organization name - null or blank if it is not set
     * @param platformDisplayName the platform's name
     * @return the qualified name an earlier release would have written
     */
    static String legacyPlatformQualifiedName(String organizationName,
                                              String platformDisplayName)
    {
        if (isUnset(organizationName))
        {
            return OMAG_SERVER_PLATFORM_TYPE + "::" + platformDisplayName;
        }

        return OMAG_SERVER_PLATFORM_TYPE + "::" + organizationName + "::" + platformDisplayName;
    }


    /**
     * Build the resource name the cataloguer gives a platform element.
     *
     * @param organizationName the platform's organization name - null or blank if it is not set
     * @param platformDisplayName the platform's name
     * @return expected resource name
     */
    static String expectedPlatformResourceName(String organizationName,
                                               String platformDisplayName)
    {
        if (isUnset(organizationName))
        {
            return platformDisplayName;
        }

        return organizationName + "." + platformDisplayName;
    }


    /**
     * Build the qualified name the cataloguer gives a server element.  It is qualified by the platform's
     * URL root, so servers with the same name on different platforms do not collide, and by nothing else -
     * in particular not by the organization name, which is what lets the cataloguer still recognise the
     * server after somebody fills one in.
     * <br>
     * Everything the server's template creates beneath it - its API manager capability, its connection, its
     * endpoint and its secrets store pair - is named from this same string, which is why it has to be
     * unique per platform and stable over a server's life.
     *
     * @param serverType the server's type, as the platform reports it
     * @param platformURLRoot the platform's URL root
     * @param serverName the server's name
     * @return expected qualified name
     */
    static String expectedServerQualifiedName(String serverType,
                                              String platformURLRoot,
                                              String serverName)
    {
        return serverType + "::" + platformURLRoot + "::" + serverName;
    }


    /**
     * Build the resource name the cataloguer gives a server element.  Unlike the qualified name this is
     * <b>not</b> qualified by the platform, and the cataloguer matches the servers already attached to a
     * platform by it - so it is the value a test has to watch when a server's organization name changes.
     *
     * @param serverName the server's name
     * @param organizationName the server's organization name - null or blank if it is not set
     * @return expected resource name
     */
    static String expectedServerResourceName(String serverName,
                                             String organizationName)
    {
        if (isUnset(organizationName))
        {
            return serverName;
        }

        return organizationName + "." + serverName;
    }


    /**
     * The cataloguer treats a null, blank or literally "null" organization name as "not set".  The last of
     * those is not paranoia: an unset organization name reaches the connector as the string "null" by one
     * route and as an empty string by another.
     *
     * @param organizationName value to test
     * @return boolean
     */
    private static boolean isUnset(String organizationName)
    {
        return (organizationName == null) || (organizationName.isBlank()) || ("null".equals(organizationName));
    }


    /**
     * Return every SoftwareServerPlatform element this suite is responsible for.  More than one coming back
     * is itself a finding: this suite puts exactly one platform in front of the cataloguer.
     *
     * @param openMetadataStore store to read through
     * @return list - empty if the cataloguer has not created one yet
     * @throws Exception the retrieval failed
     */
    static List<OpenMetadataElement> getSuitePlatformElements(OpenMetadataStore openMetadataStore) throws Exception
    {
        return findSuiteElements(openMetadataStore, OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);
    }


    /**
     * Return every SoftwareServer element this suite is responsible for, whether or not it is still attached
     * to the platform.  Looking for them by name rather than by walking out from the platform is deliberate:
     * a server element that the cataloguer has orphaned - created a replacement for, and left behind - is
     * exactly what several of these tests are looking for, and it would be invisible from the platform.
     *
     * @param openMetadataStore store to read through
     * @return list - empty if there are none
     * @throws Exception the retrieval failed
     */
    static List<OpenMetadataElement> getSuiteServerElements(OpenMetadataStore openMetadataStore) throws Exception
    {
        return findSuiteElements(openMetadataStore, OpenMetadataType.SOFTWARE_SERVER.typeName);
    }


    /**
     * Find this suite's elements of one type.
     * <br>
     * Every element of the type is listed and then filtered here, rather than asking the repository for the
     * ones whose names contain a marker.  {@code findMetadataElementsWithString} passes the search string
     * through to the repository as a regular expression that has to match the whole property value, so the
     * plain substring these elements are identified by finds nothing - silently, which is the worst way for
     * a test helper to be wrong: every assertion about "how many were catalogued" passes vacuously at zero.
     *
     * @param openMetadataStore store to read through
     * @param typeName type to look for
     * @return list of this suite's elements of that type
     * @throws Exception the retrieval failed
     */
    private static List<OpenMetadataElement> findSuiteElements(OpenMetadataStore openMetadataStore,
                                                               String            typeName) throws Exception
    {
        List<OpenMetadataElement> results  = new ArrayList<>();

        int startFrom = 0;

        while (true)
        {
            List<OpenMetadataElement> page = openMetadataStore.findMetadataElements(typeName,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    startFrom,
                                                                                    MAX_PAGE_SIZE);

            if ((page == null) || (page.isEmpty()))
            {
                break;
            }

            for (OpenMetadataElement element : page)
            {
                /*
                 * The content packs supply template servers and platforms, which carry the Template
                 * classification.  They are not this suite's to look at.
                 */
                if ((element != null) && (! isTemplate(element)) && (isSuiteElement(element)))
                {
                    results.add(element);
                }
            }

            startFrom = startFrom + page.size();
        }

        return results;
    }


    /**
     * Is this element one that this suite's platform, or the connector cataloguing it, produced?
     *
     * @param element element to test
     * @return boolean
     */
    private static boolean isSuiteElement(OpenMetadataElement element)
    {
        String qualifiedName = getQualifiedName(element);

        if (qualifiedName == null)
        {
            return false;
        }

        /*
         * Case-insensitively, because a platform's address is one of the markers and the same address can be
         * spelled several ways - PlatformCatalogLocalServerURLFVT relies on exactly that.
         */
        String comparableName = qualifiedName.toLowerCase();

        for (String marker : SUITE_MARKERS)
        {
            if (comparableName.contains(marker.toLowerCase()))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Is this element one of the content packs' templates?
     *
     * @param element element to test
     * @return boolean
     */
    private static boolean isTemplate(OpenMetadataElement element)
    {
        if (element.getClassifications() != null)
        {
            for (var classification : element.getClassifications())
            {
                if ((classification != null) && (OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName.equals(classification.getClassificationName())))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Return every catalogued server element for one server, whatever names it is currently carrying.  The
     * match is on the qualified name, which holds the server's own name whether or not an organization name
     * has been folded in beside it, and whichever platform URL root it was catalogued from.
     * <br>
     * Finding them this way - rather than by walking out from the platform - is the point.  A server element
     * that the cataloguer has stopped recognising is still attached to the platform, but so is the
     * replacement it created; and one that has been renamed is only findable by something that does not
     * assume what it is called.
     *
     * @param openMetadataStore store to read through
     * @param serverName server to look for
     * @return list - empty if the server has not been catalogued
     * @throws Exception the retrieval failed
     */
    static List<OpenMetadataElement> getServerElementsFor(OpenMetadataStore openMetadataStore,
                                                          String            serverName) throws Exception
    {
        List<OpenMetadataElement> matches = new ArrayList<>();

        for (OpenMetadataElement serverElement : getSuiteServerElements(openMetadataStore))
        {
            String qualifiedName = getQualifiedName(serverElement);

            if ((qualifiedName != null) && (qualifiedName.endsWith(serverName)))
            {
                matches.add(serverElement);
            }
        }

        return matches;
    }


    /**
     * Return the servers currently attached to a platform by the DeployedOn relationship - the relationship
     * the cataloguer creates with {@code deployITAsset}, and the one it walks on the next refresh to work
     * out which servers it already knows about.  The server is at end 1 and the platform at end 2.
     *
     * @param openMetadataStore store to read through
     * @param platformGUID platform to look at
     * @return list - empty if nothing is attached
     * @throws Exception the retrieval failed
     */
    static List<RelatedMetadataElement> getHostedServers(OpenMetadataStore openMetadataStore,
                                                         String            platformGUID) throws Exception
    {
        List<RelatedMetadataElement> hostedServers = new ArrayList<>();

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setPageSize(MAX_PAGE_SIZE);

        RelatedMetadataElementList retrieved = openMetadataStore.getRelatedMetadataElements(platformGUID,
                                                                                           2,
                                                                                           OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName,
                                                                                           queryOptions);

        if ((retrieved != null) && (retrieved.getElementList() != null))
        {
            for (RelatedMetadataElement hostedServer : retrieved.getElementList())
            {
                if ((hostedServer != null) && (hostedServer.getElement() != null))
                {
                    hostedServers.add(hostedServer);
                }
            }
        }

        return hostedServers;
    }


    /**
     * Return the qualified name of an element.
     *
     * @param element element to read
     * @return qualified name or null
     */
    static String getQualifiedName(OpenMetadataElement element)
    {
        return getStringProperty(element, OpenMetadataProperty.QUALIFIED_NAME.name);
    }


    /**
     * Return the resource name of an element - the name the cataloguer matches a catalogued server against
     * the running one by.
     *
     * @param element element to read
     * @return resource name or null
     */
    static String getResourceName(OpenMetadataElement element)
    {
        return getStringProperty(element, OpenMetadataProperty.RESOURCE_NAME.name);
    }


    /**
     * Return the display name of an element.
     *
     * @param element element to read
     * @return display name or null
     */
    static String getDisplayName(OpenMetadataElement element)
    {
        return getStringProperty(element, OpenMetadataProperty.DISPLAY_NAME.name);
    }


    /**
     * Return the deployed implementation type of an element.
     *
     * @param element element to read
     * @return deployed implementation type or null
     */
    static String getDeployedImplementationType(OpenMetadataElement element)
    {
        return getStringProperty(element, OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);
    }


    /**
     * Return one string property of an element.
     *
     * @param element element to read
     * @param propertyName property to read
     * @return value or null
     */
    static String getStringProperty(OpenMetadataElement element,
                                    String              propertyName)
    {
        final String methodName = "getStringProperty";

        if (element == null)
        {
            return null;
        }

        return propertyHelper.getStringProperty(SUITE_MARKER,
                                                propertyName,
                                                element.getElementProperties(),
                                                methodName);
    }


    /**
     * Return an element's additional properties.  The cataloguer keeps the platform's URL root in there:
     * once updatePlatform() has rewritten the qualified name, this is the only place the address of the
     * platform instance the element describes still appears.
     *
     * @param element element to read
     * @return map - empty if there are none
     */
    static Map<String, String> getAdditionalProperties(OpenMetadataElement element)
    {
        final String methodName = "getAdditionalProperties";

        if (element == null)
        {
            return new HashMap<>();
        }

        Map<String, String> additionalProperties = propertyHelper.getStringMapFromProperty(SUITE_MARKER,
                                                                                          OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                                          element.getElementProperties(),
                                                                                          methodName);

        if (additionalProperties == null)
        {
            return new HashMap<>();
        }

        return additionalProperties;
    }


    /**
     * Return who created an instance.
     *
     * @param header instance header
     * @return userId or null
     */
    static String getCreatedBy(ElementControlHeader header)
    {
        if ((header != null) && (header.getVersions() != null))
        {
            return header.getVersions().getCreatedBy();
        }

        return null;
    }


    /**
     * Summarise a list of elements by qualified name, for an assertion message that says what was actually
     * there rather than just how many of them there were.
     *
     * @param elements elements to describe
     * @return one line per element
     */
    static String describe(List<OpenMetadataElement> elements)
    {
        StringBuilder description = new StringBuilder();

        for (OpenMetadataElement element : elements)
        {
            description.append("\n    ")
                       .append(getQualifiedName(element))
                       .append("  (resourceName=")
                       .append(getResourceName(element))
                       .append(", guid=")
                       .append(element.getElementGUID())
                       .append(")");
        }

        if (description.isEmpty())
        {
            return " (none)";
        }

        return description.toString();
    }
}
