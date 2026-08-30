/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.templatesfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TemplatedAssetConnectionFVT checks that an asset created from a template can actually be opened.
 * <br><br>
 * A template for an asset ships the connection needed to reach the digital resource behind it: the template
 * element carries a {@code ResourceConnection} to a {@code Connection}, which in turn names a connector type
 * and an endpoint.  Creating an element from that template is supposed to bring the whole cluster with it,
 * because everything the caller then does with the new asset - surveying it, cataloguing what is inside it,
 * reading it - starts by asking the asset for a connector.
 * <br><br>
 * When that link is missing the failure surfaces a long way from the cause and does not look like a
 * cataloguing problem at all.  The asset itself looks entirely normal, and nothing goes wrong until a survey
 * runs against it: {@code getConnectorForAsset()} returns null by contract when an asset has no connection,
 * the survey service casts that null to the connector class it expects, and the run dies with a
 * NullPointerException naming a class the caller has never heard of.
 * <br><br>
 * {@link ContentPackTemplateFVT} creates an element from every template already, but only checks that no
 * placeholder survived and that the provenance link is in place - a connection that was never copied leaves
 * both of those looking perfectly healthy.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class TemplatedAssetConnectionFVT
{
    /**
     * Return the content-pack templates that ship a connection of their own - the only ones for which the
     * question this test asks makes sense.
     *
     * @return templates carrying a ResourceConnection
     * @throws Exception problem reading from the repository
     */
    static List<TemplateCatalog.Template> templatesWithAConnection() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        List<TemplateCatalog.Template> withConnection = new ArrayList<>();

        for (TemplateCatalog.Template template : ContentPackTemplateFVT.contentPackTemplates())
        {
            if (! getConnectionGUIDs(openMetadataStore, template.templateGUID()).isEmpty())
            {
                withConnection.add(template);
            }
        }

        return withConnection;
    }


    @Test
    void theContentPacksShipAssetTemplatesThatCarryAConnection() throws Exception
    {
        assertFalse(templatesWithAConnection().isEmpty(),
                    "No template in the content packs carries a ResourceConnection.  Either the archives failed "
                            + "to load, or the relationship that joins an asset to its connection has been renamed "
                            + "- in which case the check below is no longer testing anything.");
    }


    /**
     * Create an element from one template that ships a connection, and check the new element has one too.
     *
     * @param template template under test
     * @throws Exception any failure - which is the finding
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("templatesWithAConnection")
    void anAssetCreatedFromATemplateCarriesItsConnection(TemplateCatalog.Template template) throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        Map<String, String> placeholderValues = TemplateCatalog.getPlaceholderValues(template);

        TemplateOptions templateOptions = new TemplateOptions();

        templateOptions.setIsOwnAnchor(true);

        String newElementGUID = null;

        try
        {
            newElementGUID = openMetadataStore.createMetadataElementFromTemplate(template.typeName(),
                                                                                 templateOptions,
                                                                                 template.templateGUID(),
                                                                                 null,
                                                                                 null,
                                                                                 placeholderValues,
                                                                                 null);

            assertNotNull(newElementGUID, "Creating an element from template " + template.templateName()
                                  + " returned no GUID");

            List<String> connectionGUIDs = getConnectionGUIDs(openMetadataStore, newElementGUID);

            assertFalse(connectionGUIDs.isEmpty(),
                        "The element created from template " + template.templateName() + " has no "
                                + OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName + ", although the template "
                                + "itself has one.  Nothing can open this asset: getConnectorForAsset() returns null "
                                + "for it, and a survey against it fails with a NullPointerException rather than a "
                                + "clean error.  Attached instead: " + describeRelationships(openMetadataStore, newElementGUID));

            /*
             * Where the template's connection names an endpoint - the address of the resource - the copy has to
             * have one too.  Whether there is an endpoint at all is the template's business: a connection to a
             * file names its path in an endpoint, while one to a table inside a database server may carry
             * everything it needs in its configuration properties.  So this compares the copy against the
             * template rather than insisting on an endpoint, which would fail the templates that legitimately
             * have none and would be testing the content packs rather than the copy.
             */
            if (hasAnEndpoint(openMetadataStore, getConnectionGUIDs(openMetadataStore, template.templateGUID())))
            {
                assertTrue(hasAnEndpoint(openMetadataStore, connectionGUIDs),
                           "The connection copied from template " + template.templateName() + " has no endpoint, "
                                   + "although the template's own connection has one - so the copy has lost the "
                                   + "address of the resource it is supposed to reach");
            }
        }
        finally
        {
            if (newElementGUID != null)
            {
                TemplatesFvtTestSupport.purgeElement(openMetadataStore, newElementGUID);
            }
        }
    }


    /**
     * Is an endpoint attached to any of these connections?
     *
     * @param openMetadataStore store to read through
     * @param connectionGUIDs connections to look at
     * @return true if at least one of them names an endpoint
     * @throws Exception problem reading from the repository
     */
    private static boolean hasAnEndpoint(OpenMetadataStore openMetadataStore,
                                         List<String>      connectionGUIDs) throws Exception
    {
        for (String connectionGUID : connectionGUIDs)
        {
            RelatedMetadataElementList endpoints = openMetadataStore.getRelatedMetadataElements(connectionGUID,
                                                                                                 1,
                                                                                                 OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName,
                                                                                                 0,
                                                                                                 0);

            if ((endpoints != null) && (endpoints.getElementList() != null) && (! endpoints.getElementList().isEmpty()))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Return the GUIDs of the connections attached to an element.
     *
     * @param openMetadataStore store to read through
     * @param elementGUID element to look at
     * @return connection GUIDs, empty if there are none
     * @throws Exception problem reading from the repository
     */
    private static List<String> getConnectionGUIDs(OpenMetadataStore openMetadataStore,
                                                   String            elementGUID) throws Exception
    {
        List<String> connectionGUIDs = new ArrayList<>();

        RelatedMetadataElementList connections = openMetadataStore.getRelatedMetadataElements(elementGUID,
                                                                                               1,
                                                                                               OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName,
                                                                                               0,
                                                                                               0);

        if ((connections != null) && (connections.getElementList() != null))
        {
            for (RelatedMetadataElement connection : connections.getElementList())
            {
                if ((connection != null) && (connection.getElement() != null))
                {
                    connectionGUIDs.add(connection.getElement().getElementGUID());
                }
            }
        }

        return connectionGUIDs;
    }


    /**
     * Describe what is attached to an element, for an assertion failure that has to explain what was found
     * instead of the connection.
     *
     * @param openMetadataStore store to read through
     * @param elementGUID element to look at
     * @return readable list of the attached relationship types and elements
     */
    private static String describeRelationships(OpenMetadataStore openMetadataStore,
                                                String            elementGUID)
    {
        try
        {
            RelatedMetadataElementList attached = openMetadataStore.getRelatedMetadataElements(elementGUID, 0, null, 0, 0);

            if ((attached == null) || (attached.getElementList() == null) || attached.getElementList().isEmpty())
            {
                return "nothing at all";
            }

            List<String> descriptions = new ArrayList<>();

            for (RelatedMetadataElement relatedElement : attached.getElementList())
            {
                if (relatedElement != null)
                {
                    OpenMetadataElement element = relatedElement.getElement();

                    descriptions.add(relatedElement.getType().getTypeName() + " -> "
                                             + ((element == null) || (element.getType() == null)
                                                        ? "?" : element.getType().getTypeName()));
                }
            }

            return descriptions.toString();
        }
        catch (Exception error)
        {
            return "could not be listed (" + error.getClass().getSimpleName() + ": " + error.getMessage() + ")";
        }
    }
}
