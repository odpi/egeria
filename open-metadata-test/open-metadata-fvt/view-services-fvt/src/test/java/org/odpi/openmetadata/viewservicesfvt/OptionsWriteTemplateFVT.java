/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.rest.NewOpenMetadataElementRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.RelatedMetadataElementListResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.automatedCuration;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.classification;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.createAsset;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.metadataExpert;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.named;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.raw;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.record;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.string;

/**
 * OptionsWriteTemplateFVT checks TemplateOptions and the TemplateRequestBody: effectiveFrom, effectiveTo,
 * deepCopy, templateSubstitute, allowRetrieve, metadataElementSubtypeName, replacementProperties and
 * placeholderPropertyValues.
 * <br>
 * The requests go to the Automated Curation's createElementFromTemplate (/catalog-templates/new-element), which
 * passes every one of these options to the metadata access store.  The template is a data file whose qualified
 * name and description hold a placeholder, with a schema type anchored to it (linked by a Schema relationship)
 * whose qualified name holds the same placeholder.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class OptionsWriteTemplateFVT
{
    private static final String PLACEHOLDER = "~{fileName}~";
    private static final long   DAY         = 24L * 60L * 60L * 1000L;

    private static final List<String> created = new ArrayList<>();

    private static String templateGUID              = null;
    private static String templateQualifiedName     = null;
    private static String templateSchemaGUID        = null;
    private static String templateSchemaQualifiedName = null;


    /**
     * Create the template and its anchored schema type.
     */
    @BeforeAll
    static void createTemplate()
    {
        DataFileProperties properties = named(new DataFileProperties(), "DataFile", "template::" + PLACEHOLDER);

        properties.setDescription("Copy of the template for " + PLACEHOLDER);
        properties.setFileType("csv");

        templateQualifiedName = properties.getQualifiedName();
        templateGUID          = createAsset(properties, created);

        NewOpenMetadataElementRequestBody schemaRequestBody = new NewOpenMetadataElementRequestBody();

        schemaRequestBody.setIsOwnAnchor(false);
        schemaRequestBody.setAnchorGUID(templateGUID);
        schemaRequestBody.setParentGUID(templateGUID);
        schemaRequestBody.setParentRelationshipTypeName(OpenMetadataType.SCHEMA_RELATIONSHIP.typeName);
        schemaRequestBody.setParentAtEnd1(true);

        templateSchemaGUID          = OptionsWriteSupport.createGenericElement(schemaRequestBody,
                                                                               OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName,
                                                                               "template-schema::" + PLACEHOLDER,
                                                                               created);
        templateSchemaQualifiedName = string(raw(templateSchemaGUID), OpenMetadataProperty.QUALIFIED_NAME.name);

        assertEquals(List.of(templateSchemaGUID), schemaGUIDs(templateGUID), "the template's schema is not linked to it");
    }


    /**
     * placeholderPropertyValues replace the placeholders in the template's properties - in the new element and in
     * the anchored elements copied with it.  replacementProperties override the template's values; the template's
     * other values are kept.  The default deepCopy=true copies the anchored schema type: the new element has its
     * own schema type, anchored to it.  The default templateSubstitute=false adds no TemplateSubstitute
     * classification.
     */
    @Test
    void placeholdersReplacementPropertiesAndDeepCopy()
    {
        DataFileProperties replacementProperties = new DataFileProperties();

        replacementProperties.setDisplayName("Replaced display name");

        TemplateRequestBody requestBody = templateRequest("deep-copy");

        requestBody.setReplacementProperties(replacementProperties);

        String copy = record(automatedCuration.create("/catalog-templates/new-element", requestBody), created);

        assertNotEquals(templateGUID, copy);

        OpenMetadataElement element = raw(copy);

        assertEquals(templateQualifiedName.replace(PLACEHOLDER, "deep-copy"), string(element, OpenMetadataProperty.QUALIFIED_NAME.name),
                     "the placeholder in the qualified name was not replaced");
        assertEquals("Copy of the template for deep-copy", string(element, OpenMetadataProperty.DESCRIPTION.name),
                     "the placeholder in the description was not replaced");
        assertEquals("Replaced display name", string(element, OpenMetadataProperty.DISPLAY_NAME.name),
                     "replacementProperties did not override the template's value");
        assertEquals("csv", string(element, OpenMetadataProperty.FILE_TYPE.name),
                     "a template value that replacementProperties does not mention was not kept");
        assertNull(classification(element, OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName),
                   "templateSubstitute=false added a TemplateSubstitute classification");

        List<String> schemas = schemaGUIDs(copy);

        assertEquals(1, schemas.size(), "deepCopy=true did not copy the anchored schema type");
        assertNotEquals(templateSchemaGUID, schemas.get(0), "deepCopy=true linked the template's own schema type instead of copying it");

        OpenMetadataElement schema = raw(schemas.get(0));

        created.add(schemas.get(0));

        assertEquals(templateSchemaQualifiedName.replace(PLACEHOLDER, "deep-copy"), string(schema, OpenMetadataProperty.QUALIFIED_NAME.name),
                     "the placeholder in the copied schema type was not replaced");
        assertEquals(copy, string(classification(schema, OpenMetadataType.ANCHORS_CLASSIFICATION.typeName).getClassificationProperties(),
                                  OpenMetadataProperty.ANCHOR_GUID.name),
                     "the copied schema type is not anchored to the new element");

        assertEquals(List.of(templateSchemaGUID), schemaGUIDs(templateGUID), "copying the template changed the template's schema");
    }


    /**
     * deepCopy=false copies the template element alone: the new element has no schema type.
     */
    @Test
    void deepCopyFalseCopiesOnlyTheElement()
    {
        TemplateRequestBody requestBody = templateRequest("shallow-copy");

        requestBody.setDeepCopy(false);

        String copy = record(automatedCuration.create("/catalog-templates/new-element", requestBody), created);

        assertEquals(templateQualifiedName.replace(PLACEHOLDER, "shallow-copy"), string(raw(copy), OpenMetadataProperty.QUALIFIED_NAME.name));
        assertTrue(schemaGUIDs(copy).isEmpty(), "deepCopy=false copied the anchored schema type");
    }


    /**
     * templateSubstitute=true classifies the new element as a TemplateSubstitute.
     */
    @Test
    void templateSubstitute()
    {
        TemplateRequestBody requestBody = templateRequest("substitute");

        requestBody.setIsTemplateSubstitute(true);

        String copy = record(automatedCuration.create("/catalog-templates/new-element", requestBody), created);

        assertNotNull(classification(raw(copy), OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName),
                      "templateSubstitute=true did not add the TemplateSubstitute classification");
    }


    /**
     * allowRetrieve=true (the default) returns the existing element when the new element's qualified name is
     * already in use.  allowRetrieve=false must create a new element, which the unique qualified name prevents,
     * so the request is refused rather than returning the existing element.
     */
    @Test
    void allowRetrieve()
    {
        String first = record(automatedCuration.create("/catalog-templates/new-element", templateRequest("retrieve")), created);
        String again = automatedCuration.create("/catalog-templates/new-element", templateRequest("retrieve"));

        assertEquals(first, again, "allowRetrieve=true did not return the existing element with the same qualified name");

        TemplateRequestBody noRetrieve = templateRequest("retrieve");

        noRetrieve.setAllowRetrieve(false);

        GUIDResponse response = automatedCuration.postUnchecked("/catalog-templates/new-element", noRetrieve, GUIDResponse.class);

        assertNotNull(response);

        if (response.getRelatedHTTPCode() == 200)
        {
            record(response.getGUID(), created);
        }

        assertNotEquals(200, response.getRelatedHTTPCode(),
                        "allowRetrieve=false created a second element with a qualified name that is already in use (" + response.getGUID() + ")");
    }


    /**
     * effectiveFrom and effectiveTo set the effectivity dates of the new element.
     */
    @Test
    void effectiveFromAndEffectiveTo()
    {
        Date from = new Date(new Date().getTime() - DAY);
        Date to   = new Date(new Date().getTime() + (100 * DAY));

        TemplateRequestBody requestBody = templateRequest("effectivity");

        requestBody.setEffectiveFrom(from);
        requestBody.setEffectiveTo(to);

        String copy = record(automatedCuration.create("/catalog-templates/new-element", requestBody), created);

        OpenMetadataElement element = raw(copy);

        assertNotNull(element.getEffectiveFromTime(), "effectiveFrom was not set on the new element");
        assertNotNull(element.getEffectiveToTime(), "effectiveTo was not set on the new element");
        assertEquals(from.getTime(), element.getEffectiveFromTime().getTime());
        assertEquals(to.getTime(), element.getEffectiveToTime().getTime());
    }


    /**
     * metadataElementSubtypeName creates an element of a subtype of the template's type: a CSVFile from a DataFile
     * template.
     */
    @Test
    void metadataElementSubtypeName()
    {
        TemplateRequestBody requestBody = templateRequest("subtype");

        requestBody.setMetadataElementSubtypeName(OpenMetadataType.CSV_FILE.typeName);

        String copy = record(automatedCuration.create("/catalog-templates/new-element", requestBody), created);

        assertEquals(OpenMetadataType.CSV_FILE.typeName, raw(copy).getType().getTypeName(),
                     "metadataElementSubtypeName did not set the type of the new element");
    }


    private static TemplateRequestBody templateRequest(String fileName)
    {
        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(templateGUID);
        requestBody.setIsOwnAnchor(true);
        requestBody.setPlaceholderPropertyValues(Map.of("fileName", fileName));

        return requestBody;
    }


    private static List<String> schemaGUIDs(String assetGUID)
    {
        RelatedMetadataElementListResponse response = metadataExpert.post("/related-elements/" + assetGUID + "/type/" + OpenMetadataType.SCHEMA_RELATIONSHIP.typeName,
                                                                          new ResultsRequestBody(),
                                                                          RelatedMetadataElementListResponse.class);
        List<String> guids = new ArrayList<>();

        if ((response.getRelatedElementList() != null) && (response.getRelatedElementList().getElementList() != null))
        {
            for (RelatedMetadataElement relatedMetadataElement : response.getRelatedElementList().getElementList())
            {
                guids.add(relatedMetadataElement.getElement().getElementGUID());
            }
        }

        return guids;
    }


    @AfterAll
    static void cleanUp()
    {
        OptionsWriteSupport.purgeAll(created);
    }
}
