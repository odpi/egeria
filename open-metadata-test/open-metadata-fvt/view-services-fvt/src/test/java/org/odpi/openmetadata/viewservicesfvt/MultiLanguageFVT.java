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
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.translations.TranslationDetailProperties;
import org.odpi.openmetadata.frameworkservices.omf.rest.TranslationDetailResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.TranslationListResponse;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MultiLanguageFVT covers every request of the Multi Language OMVS: a translation is set for an asset, read back
 * by language/locale and in the list of the element's translations, replaced, and then cleared.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MultiLanguageFVT
{
    private static final ViewServiceClient multiLanguage = new ViewServiceClient(ViewServiceDescription.MULTI_LANGUAGE);
    private static final ViewServiceClient assetMaker    = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);

    private static final String LANGUAGE = "Spanish";
    private static final String LOCALE   = "es-ES";

    private static String elementGUID = null;


    /**
     * The element to translate.
     */
    @Test
    @Order(1)
    void createElementToTranslate()
    {
        DataSetProperties properties = new DataSetProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DataSet", "multi-language"));
        properties.setDisplayName("Customer data set");
        properties.setDescription("A data set with a Spanish translation.");

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        elementGUID = assetMaker.create("/assets", requestBody);
    }


    /**
     * setTranslation creates the translation; both retrieve requests return it.
     */
    @Test
    @Order(2)
    void setAndReadTranslation()
    {
        multiLanguage.call("/elements/" + elementGUID + "/translations", translation("Conjunto de datos de clientes"));

        TranslationDetailProperties translation = getTranslation();

        assertNotNull(translation, "getTranslation returned nothing after setTranslation");
        assertEquals("Conjunto de datos de clientes", translation.getDisplayName());
        assertEquals(LANGUAGE, translation.getLanguage());
        assertEquals(LOCALE, translation.getLocale());
        assertEquals("es", translation.getLanguageCode());
        assertEquals(Map.of("summary", "Datos de clientes"), translation.getAdditionalTranslations());

        List<TranslationDetailProperties> translations = getTranslations();

        assertEquals(1, translations.size(), "getTranslations should return the one translation: " + translations);
        assertEquals("Conjunto de datos de clientes", translations.get(0).getDisplayName());
    }


    /**
     * A second setTranslation for the same language/locale replaces the first rather than adding another.
     */
    @Test
    @Order(3)
    void replaceTranslation()
    {
        multiLanguage.call("/elements/" + elementGUID + "/translations", translation("Datos de clientes"));

        TranslationDetailProperties translation = getTranslation();

        assertNotNull(translation);
        assertEquals("Datos de clientes", translation.getDisplayName());
        assertEquals(1, getTranslations().size(), "setTranslation for the same language/locale should update, not add");
    }


    /**
     * clearTranslation removes it.
     */
    @Test
    @Order(4)
    void clearTranslation()
    {
        multiLanguage.call("/elements/" + elementGUID + "/translations/remove?language=" + LANGUAGE + "&locale=" + LOCALE, null);

        assertNull(getTranslation(), "getTranslation still returns the translation after clearTranslation");
        assertTrue(getTranslations().isEmpty(), "getTranslations still returns translations after clearTranslation");
    }


    @AfterAll
    static void deleteElement()
    {
        if (elementGUID != null)
        {
            assetMaker.call("/assets/" + elementGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + elementGUID + "/retrieve");
        }
    }


    private static NewElementRequestBody translation(String displayName)
    {
        TranslationDetailProperties properties = new TranslationDetailProperties();

        properties.setLanguage(LANGUAGE);
        properties.setLanguageCode("es");
        properties.setLocale(LOCALE);
        properties.setDisplayName(displayName);
        properties.setDescription("Un conjunto de datos con una traducción al español.");
        properties.setAdditionalTranslations(Map.of("summary", "Datos de clientes"));

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setProperties(properties);

        return requestBody;
    }


    private static TranslationDetailProperties getTranslation()
    {
        return multiLanguage.get("/elements/" + elementGUID + "/translations/retrieve?language=" + LANGUAGE + "&locale=" + LOCALE,
                                 TranslationDetailResponse.class).getElement();
    }


    private static List<TranslationDetailProperties> getTranslations()
    {
        List<TranslationDetailProperties> translations = multiLanguage.get("/elements/" + elementGUID + "/translations?startFrom=0&pageSize=10",
                                                                           TranslationListResponse.class).getElements();

        return (translations == null) ? List.of() : translations;
    }
}
