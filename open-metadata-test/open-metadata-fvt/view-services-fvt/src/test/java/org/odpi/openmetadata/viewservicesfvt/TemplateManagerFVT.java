/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.CatalogTemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateSubstituteProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.assertLinked;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.assertNotLinked;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.named;

/**
 * TemplateManagerFVT covers every request of the Template Manager OMVS: setting and clearing the Template and
 * TemplateSubstitute classifications, and attaching and detaching the SourcedFrom and CatalogTemplate
 * relationships.  The elements are data file assets made through Asset Maker.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TemplateManagerFVT
{
    private static final ViewServiceClient templateManager = new ViewServiceClient(ViewServiceDescription.TEMPLATE_MANAGER);

    private static String templateGUID = null;
    private static String elementGUID  = null;

    private static final List<String> createdGUIDs = new ArrayList<>();


    @Test
    @Order(1)
    void createElements()
    {
        templateGUID = AssetMakerFvtSupport.createAsset(named(new DataFileProperties(), "DataFile", "template-file"));
        createdGUIDs.add(templateGUID);

        elementGUID = AssetMakerFvtSupport.createAsset(named(new DataFileProperties(), "DataFile", "file-from-template"));
        createdGUIDs.add(elementGUID);
    }


    /**
     * The Template classification is set with its properties and removed.
     */
    @Test
    @Order(2)
    void templateClassification()
    {
        TemplateProperties properties = new TemplateProperties();

        properties.setDisplayName("FVT data file template");
        properties.setVersionIdentifier("1.0");

        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(properties);

        templateManager.call("/elements/" + templateGUID + "/template", requestBody);

        ElementHeader header = AssetMakerFvtSupport.retrieve(templateGUID).getElementHeader();

        assertNotNull(header.getTemplate(), "the Template classification is missing");
        assertTrue(header.getTemplate().getClassificationProperties() instanceof TemplateProperties templateProperties &&
                           "FVT data file template".equals(templateProperties.getDisplayName()) &&
                           "1.0".equals(templateProperties.getVersionIdentifier()),
                   "the Template classification's properties are wrong: " + header.getTemplate().getClassificationProperties());

        templateManager.call("/elements/" + templateGUID + "/template/remove", new DeleteClassificationRequestBody());

        assertNull(AssetMakerFvtSupport.retrieve(templateGUID).getElementHeader().getTemplate(), "the Template classification was not removed");
    }


    /**
     * The TemplateSubstitute classification is set and removed.
     */
    @Test
    @Order(3)
    void templateSubstituteClassification()
    {
        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(new TemplateSubstituteProperties());

        templateManager.call("/elements/" + templateGUID + "/template-substitute", requestBody);

        assertNotNull(AssetMakerFvtSupport.retrieve(templateGUID).getElementHeader().getTemplateSubstitute(),
                      "the TemplateSubstitute classification is missing");

        templateManager.call("/elements/" + templateGUID + "/template-substitute/remove", new DeleteClassificationRequestBody());

        assertNull(AssetMakerFvtSupport.retrieve(templateGUID).getElementHeader().getTemplateSubstitute(),
                   "the TemplateSubstitute classification was not removed");
    }


    /**
     * SourcedFrom links an element to the template it was made from.
     */
    @Test
    @Order(4)
    void sourcedFrom()
    {
        String path = "/elements/" + elementGUID + "/sourced-from/" + templateGUID;

        templateManager.call(path + "/attach", new NewRelationshipRequestBody());

        assertLinked(OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName, elementGUID, templateGUID);
        assertEquals(templateGUID,
                     AssetMakerFvtSupport.retrieve(elementGUID).getSourcedFromTemplate().getRelatedElement().getElementHeader().getGUID(),
                     "the element does not report the template it was sourced from");

        templateManager.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName, elementGUID, templateGUID);
    }


    /**
     * CatalogTemplate links an element to a template to use when cataloguing, with a label.
     */
    @Test
    @Order(5)
    void catalogTemplate()
    {
        CatalogTemplateProperties properties = new CatalogTemplateProperties();

        properties.setLabel("fvt-catalog-template");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        String path = "/elements/" + elementGUID + "/catalog-template/" + templateGUID;

        templateManager.call(path + "/attach", requestBody);

        assertLinked(OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName, elementGUID, templateGUID);
        assertTrue(AssetMakerFvtSupport.relationshipPropertiesJSON(elementGUID, templateGUID).contains("fvt-catalog-template"),
                   "the catalog template's label is missing");

        templateManager.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName, elementGUID, templateGUID);
    }


    @Test
    @Order(6)
    void deleteElements()
    {
        for (String guid : List.copyOf(createdGUIDs))
        {
            AssetMakerFvtSupport.delete(guid);
            createdGUIDs.remove(guid);
        }
    }


    @AfterAll
    static void cleanUp()
    {
        for (String guid : createdGUIDs)
        {
            AssetMakerFvtSupport.assetMaker.postUnchecked("/assets/" + guid + "/delete", new DeleteElementRequestBody(), VoidResponse.class);
        }
    }
}
