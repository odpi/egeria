/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.GovernanceDomain;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * SecurityPackArchiveWriter creates an open metadata archive containing standard security control catalogs: the NIST
 * Cybersecurity Framework (CSF) 2.0 and NIST SP 800-53 Rev. 5.  Each catalog is a root collection with a folder for
 * each CSF function and category, or for each SP 800-53 control family.  Each CSF subcategory and SP 800-53 control
 * is a GovernanceControl, so it can be linked to a statement of applicability with the ControlApplicability
 * relationship.  SP 800-53 control enhancements are linked to the control they strengthen with GovernanceControlLink.
 * <br><br>
 * The catalogs are read from JSON resources generated from NIST's OSCAL catalogs.  Both are works of the U.S.
 * Government and in the public domain.
 */
public class SecurityPackArchiveWriter extends ContentPackBaseArchiveWriter
{
    private static final String CSF_RESOURCE        = "/security-catalogs/nist-csf-2.0.json";
    private static final String SP_800_53_RESOURCE  = "/security-catalogs/nist-sp-800-53-rev5.json";

    private static final String CSF_QUALIFIED_NAME       = "NIST CSF 2.0";
    private static final String SP_800_53_QUALIFIED_NAME = "NIST SP 800-53 Rev 5";

    private static final String CATALOG_CATEGORY = "Security Control Catalog";
    private static final List<String> AUTHORS    = List.of("National Institute of Standards and Technology (NIST)");


    /**
     * Default constructor initializes the archive.
     */
    public SecurityPackArchiveWriter()
    {
        super(ContentPackDefinition.SECURITY_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.SECURITY_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.SECURITY_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.SECURITY_CONTENT_PACK.getArchiveFileName(),
              new OpenMetadataArchive[]{new CorePackArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Implemented by subclass to add the content.
     */
    @Override
    public void getArchiveContent()
    {
        addCybersecurityFramework(readCatalog(CSF_RESOURCE));
        addSP80053(readCatalog(SP_800_53_RESOURCE));

        /*
         * Saving the GUIDs means that the guids in the archive are stable between runs of the archive writer.
         */
        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();
    }


    /**
     * Read a catalog from the resources packaged with this module.
     *
     * @param resourceName name of the resource
     * @return parsed JSON
     */
    private JsonNode readCatalog(String resourceName)
    {
        try (InputStream inputStream = SecurityPackArchiveWriter.class.getResourceAsStream(resourceName))
        {
            if (inputStream == null)
            {
                throw new IllegalStateException("Security control catalog " + resourceName + " is missing from the classpath");
            }

            return new ObjectMapper().readTree(inputStream);
        }
        catch (IOException error)
        {
            throw new UncheckedIOException(error);
        }
    }


    /**
     * Add the NIST Cybersecurity Framework 2.0: a root collection, a folder for each function and each category,
     * and a governance control for each subcategory (outcome).
     *
     * @param catalog parsed catalog
     */
    private void addCybersecurityFramework(JsonNode catalog)
    {
        String rootGUID = addCatalogRoot(CSF_QUALIFIED_NAME,
                                         "NIST Cybersecurity Framework (CSF) 2.0",
                                         "The NIST Cybersecurity Framework 2.0 describes the cybersecurity outcomes that an organization can use to understand, assess, prioritize and communicate its cybersecurity efforts.  The outcomes are organized into six functions - Govern, Identify, Protect, Detect, Respond and Recover - each divided into categories and subcategories.  Each subcategory is a governance control that can be linked to a statement of applicability or to an organization's own controls.",
                                         catalog);

        for (JsonNode function : catalog.get("functions"))
        {
            String functionGUID = addFolder(CSF_QUALIFIED_NAME + "::" + text(function, "id"),
                                            text(function, "label"),
                                            text(function, "description"),
                                            rootGUID);

            for (JsonNode category : function.get("categories"))
            {
                String categoryGUID = addFolder(CSF_QUALIFIED_NAME + "::" + text(category, "id"),
                                                text(category, "label"),
                                                text(category, "description"),
                                                functionGUID);

                for (JsonNode subcategory : category.get("subcategories"))
                {
                    String identifier = text(subcategory, "id");
                    String statement  = text(subcategory, "statement");

                    String controlGUID = addControl(CSF_QUALIFIED_NAME + "::" + identifier,
                                                    identifier,
                                                    identifier,
                                                    statement,
                                                    statement,
                                                    implementationExamples(subcategory.get("examples")));

                    archiveHelper.addMemberToCollection(categoryGUID, controlGUID, "Subcategory of " + text(category, "label"));
                }
            }
        }
    }


    /**
     * Add NIST SP 800-53 Rev. 5: a root collection, a folder for each control family, a governance control for
     * each control, and a governance control for each control enhancement, linked to the control it strengthens.
     * Withdrawn controls are not included.
     *
     * @param catalog parsed catalog
     */
    private void addSP80053(JsonNode catalog)
    {
        String rootGUID = addCatalogRoot(SP_800_53_QUALIFIED_NAME,
                                         "NIST SP 800-53 Rev. 5 Security and Privacy Controls",
                                         "NIST Special Publication 800-53 Revision 5 provides a catalog of security and privacy controls for information systems and organizations, organized into twenty control families.  Each control, and each control enhancement, is a governance control that can be linked to a statement of applicability.  Control enhancements are linked to the control they strengthen with the GovernanceControlLink relationship.  Values in square brackets are parameters that the organization defines.",
                                         catalog);

        for (JsonNode family : catalog.get("families"))
        {
            String familyId    = text(family, "id");
            String familyTitle = text(family, "title");
            String familyGUID  = addFolder(SP_800_53_QUALIFIED_NAME + "::" + familyId,
                                           familyTitle + " (" + familyId + ")",
                                           "The " + familyTitle + " family of controls from NIST SP 800-53 Rev. 5.",
                                           rootGUID);

            for (JsonNode control : family.get("controls"))
            {
                String identifier  = text(control, "id");
                String controlGUID = addControl(SP_800_53_QUALIFIED_NAME + "::" + identifier,
                                                identifier,
                                                identifier + " " + text(control, "title"),
                                                null,
                                                text(control, "statement"),
                                                text(control, "discussion"));

                archiveHelper.addMemberToCollection(familyGUID, controlGUID, "Control in the " + familyTitle + " family");

                for (JsonNode enhancement : control.get("enhancements"))
                {
                    String enhancementIdentifier = text(enhancement, "id");
                    String enhancementGUID = addControl(SP_800_53_QUALIFIED_NAME + "::" + enhancementIdentifier,
                                                        enhancementIdentifier,
                                                        enhancementIdentifier + " " + text(enhancement, "title"),
                                                        null,
                                                        text(enhancement, "statement"),
                                                        text(enhancement, "discussion"));

                    archiveHelper.addGovernanceControlLink(controlGUID, enhancementGUID);
                }
            }
        }
    }


    /**
     * Add the root collection for a catalog.
     *
     * @param qualifiedName unique name
     * @param displayName display name
     * @param description description
     * @param catalog parsed catalog, for its provenance
     * @return unique identifier
     */
    private String addCatalogRoot(String   qualifiedName,
                                  String   displayName,
                                  String   description,
                                  JsonNode catalog)
    {
        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put("title", text(catalog, "title"));
        additionalProperties.put("catalogVersion", text(catalog, "version"));
        additionalProperties.put("catalogLastModified", text(catalog, "lastModified"));
        additionalProperties.put("source", text(catalog, "source"));
        additionalProperties.put("license", text(catalog, "license"));

        Map<String, Object> extendedProperties = new HashMap<>();

        extendedProperties.put(OpenMetadataProperty.URL.name, text(catalog, "url"));

        return archiveHelper.addCollection(OpenMetadataType.ROOT_COLLECTION.typeName,
                                           null,
                                           null,
                                           null,
                                           null,
                                           null,
                                           qualifiedName,
                                           displayName,
                                           description,
                                           CATALOG_CATEGORY,
                                           additionalProperties,
                                           null,
                                           extendedProperties);
    }


    /**
     * Add a folder within a catalog.
     *
     * @param qualifiedName unique name
     * @param displayName display name
     * @param description description
     * @param parentGUID collection that the folder belongs to
     * @return unique identifier
     */
    private String addFolder(String qualifiedName,
                             String displayName,
                             String description,
                             String parentGUID)
    {
        String folderGUID = archiveHelper.addCollection(OpenMetadataType.COLLECTION_FOLDER.typeName,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        qualifiedName,
                                                        displayName,
                                                        description,
                                                        CATALOG_CATEGORY,
                                                        null,
                                                        null,
                                                        null);

        archiveHelper.addMemberToCollection(parentGUID, folderGUID, "Part of the catalog");

        return folderGUID;
    }


    /**
     * Add a governance control from a catalog.
     *
     * @param qualifiedName unique name
     * @param identifier identifier used by the catalog
     * @param displayName display name
     * @param summary short description
     * @param description the text of the control
     * @param usage supplementary guidance
     * @return unique identifier
     */
    private String addControl(String qualifiedName,
                              String identifier,
                              String displayName,
                              String summary,
                              String description,
                              String usage)
    {
        return archiveHelper.addGovernanceDefinition(OpenMetadataType.GOVERNANCE_CONTROL.typeName,
                                                     qualifiedName,
                                                     identifier,
                                                     displayName,
                                                     summary,
                                                     description,
                                                     null,
                                                     usage,
                                                     GovernanceDomain.SECURITY.getOrdinal(),
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     AUTHORS,
                                                     null,
                                                     null);
    }


    /**
     * Turn the implementation examples for a CSF subcategory into a single usage text.
     *
     * @param examples list of examples
     * @return text or null
     */
    private String implementationExamples(JsonNode examples)
    {
        if ((examples == null) || examples.isEmpty())
        {
            return null;
        }

        List<String> lines = new ArrayList<>();

        lines.add("Implementation examples:");

        for (JsonNode example : examples)
        {
            lines.add("- " + example.asText());
        }

        return String.join("\n", lines);
    }


    /**
     * Return a text field, or null if it is missing.
     *
     * @param node JSON object
     * @param fieldName name of field
     * @return string or null
     */
    private String text(JsonNode node, String fieldName)
    {
        JsonNode value = node.get(fieldName);

        if ((value == null) || value.isNull())
        {
            return null;
        }

        return value.asText();
    }


    /**
     * Main program to initiate the archive writer for the security content pack.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            SecurityPackArchiveWriter archiveWriter = new SecurityPackArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}
