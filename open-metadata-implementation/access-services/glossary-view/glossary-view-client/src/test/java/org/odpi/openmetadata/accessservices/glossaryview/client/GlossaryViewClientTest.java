/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.glossaryview.exception.GlossaryViewOmasException;
import org.odpi.openmetadata.accessservices.glossaryview.rest.ExternalGlossaryLink;
import org.odpi.openmetadata.accessservices.glossaryview.rest.Glossary;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryCategory;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryTerm;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetail;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

public class GlossaryViewClientTest {

    private static final String GLOSSARY = "Glossary";
    private static final String CATEGORY = "GlossaryCategory";
    private static final String TERM = "GlossaryTerm";
    private static final String EXTERNAL_GLOSSARY_LINK = "ExternalGlossaryLink";

    private static final String SERVER_NAME = "omas";
    private static final String SERVER_PLATFORM_URL = "test://test.glossary.view.client";
    private static final String USER_ID = "test";
    private static final String USER_PASSWORD = "password";

    private GlossaryViewEntityDetailResponse response;
    private List<GlossaryViewEntityDetail> glossaries = new ArrayList<>();
    private List<GlossaryViewEntityDetail> categories = new ArrayList<>();
    private List<GlossaryViewEntityDetail> terms = new ArrayList<>();
    private GlossaryViewEntityDetail externalGlossaryLink;

    private GlossaryViewClient underTest;

    @Mock
    private RESTClientConnector connector;

    @Before
    public void before() throws Exception{
        MockitoAnnotations.initMocks(this);

        underTest = new GlossaryViewClient(SERVER_NAME, SERVER_PLATFORM_URL, USER_ID, USER_PASSWORD);

        Field connectorField = ReflectionUtils.findField(GlossaryViewClient.class, "clientConnector");
        connectorField.setAccessible(true);
        ReflectionUtils.setField(connectorField, underTest, connector);
        connectorField.setAccessible(false);

        response = new GlossaryViewEntityDetailResponse();

        glossaries.add(createGlossaryViewEntityDetail(GLOSSARY, "glossary-01"));
        glossaries.add(createGlossaryViewEntityDetail(GLOSSARY, "glossary-02"));
        glossaries.add(createGlossaryViewEntityDetail(GLOSSARY, "glossary-03"));

        categories.add(createGlossaryViewEntityDetail(CATEGORY, "category-01"));
        categories.add(createGlossaryViewEntityDetail(CATEGORY, "category-02"));
        categories.add(createGlossaryViewEntityDetail(CATEGORY, "category-03"));
        categories.add(createGlossaryViewEntityDetail(CATEGORY, "category-04"));
        categories.add(createGlossaryViewEntityDetail(CATEGORY, "category-05"));

        terms.add(createGlossaryViewEntityDetail(TERM, "term-01"));
        terms.add(createGlossaryViewEntityDetail(TERM, "term-02"));
        terms.add(createGlossaryViewEntityDetail(TERM, "term-03"));
        terms.add(createGlossaryViewEntityDetail(TERM, "term-04"));
        terms.add(createGlossaryViewEntityDetail(TERM, "term-05"));
        terms.add(createGlossaryViewEntityDetail(TERM, "term-06"));
        terms.add(createGlossaryViewEntityDetail(TERM, "term-07"));

        externalGlossaryLink = createGlossaryViewEntityDetail(EXTERNAL_GLOSSARY_LINK, "external-glossary-link-01");
    }

    private GlossaryViewEntityDetail createGlossaryViewEntityDetail(String typeDefName, String guid){
        GlossaryViewEntityDetail glossaryViewEntityDetail = createConcreteObject(typeDefName);
        glossaryViewEntityDetail.setTypeDefName(typeDefName);
        glossaryViewEntityDetail.setGuid(guid);
        glossaryViewEntityDetail.setStatus("ACTIVE");

        return glossaryViewEntityDetail;
    }

    private GlossaryViewEntityDetail createConcreteObject(String typeDefName){
        GlossaryViewEntityDetail glossaryViewEntityDetail;
        switch (typeDefName) {
            case GLOSSARY:
                glossaryViewEntityDetail = new Glossary();
                break;
            case CATEGORY:
                glossaryViewEntityDetail = new GlossaryCategory();
                break;
            case TERM:
                glossaryViewEntityDetail = new GlossaryTerm();
                break;
            case EXTERNAL_GLOSSARY_LINK:
                glossaryViewEntityDetail = new ExternalGlossaryLink();
                break;
            default:
                glossaryViewEntityDetail = new GlossaryViewEntityDetail();
        }
        return glossaryViewEntityDetail;

    }

    @Test
    public void getGlossary() throws Exception{
        response.addEntityDetail(glossaries.get(0));

        when(connector.callGetRESTCall(eq("getGlossary"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID), eq(glossaries.get(0).getGuid()))).thenReturn(response);

        Glossary glossary = underTest.getGlossary(USER_ID, glossaries.get(0).getGuid());

        assertEquals(glossaries.get(0).getGuid(), glossary.getGuid());
    }

    @Test
    public void getCategory() throws Exception{
        response.addEntityDetail(categories.get(0));

        when(connector.callGetRESTCall(eq("getCategory"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID), eq(categories.get(0).getGuid()))).thenReturn(response);

        GlossaryCategory category = underTest.getCategory(USER_ID, categories.get(0).getGuid());

        assertEquals(categories.get(0).getGuid(), category.getGuid());
    }

    @Test
    public void getCategories() throws Exception{
        response.addEntityDetails(Arrays.asList(categories.get(0), categories.get(1), categories.get(2)));

        when(connector.callGetRESTCall(eq("getCategories"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID),eq(glossaries.get(0).getGuid()), anyInt(), anyInt())).thenReturn(response);

        List<GlossaryCategory> categories = underTest.getCategories(USER_ID, glossaries.get(0).getGuid(), 0, 10);

        assertEquals(3, categories.size());
        assertEquals(this.categories.get(0).getGuid(), categories.get(0).getGuid());
        assertEquals(this.categories.get(1).getGuid(), categories.get(1).getGuid());
        assertEquals(this.categories.get(2).getGuid(), categories.get(2).getGuid());
    }

    @Test
    public void getAllCategories() throws Exception{
        response.addEntityDetails(Arrays.asList(categories.get(0), categories.get(1), categories.get(2)));

        when(connector.callGetRESTCall(eq("getAllCategories"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID), anyInt(), anyInt())).thenReturn(response);

        List<GlossaryCategory> categories = underTest.getAllCategories(USER_ID, 0, 10);

        assertEquals(3, categories.size());
    }

    @Test
    public void getAllGlossaries() throws Exception{
        response.addEntityDetails(glossaries);

        when(connector.callGetRESTCall(eq("getAllGlossaries"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID), anyInt(), anyInt())).thenReturn(response);

        List<Glossary> glossaries = underTest.getAllGlossaries(USER_ID,0, 10);

        assertEquals(3, glossaries.size());
        assertEquals(this.glossaries.get(0).getGuid(), glossaries.get(0).getGuid());
        assertEquals(this.glossaries.get(1).getGuid(), glossaries.get(1).getGuid());
        assertEquals(this.glossaries.get(2).getGuid(), glossaries.get(2).getGuid());
    }

    @Test
    public void getAllTerms() throws Exception{
        response.addEntityDetails(terms);

        when(connector.callGetRESTCall(eq("getAllGlossaryTerms"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID), anyInt(), anyInt())).thenReturn(response);

        List<GlossaryTerm> terms = underTest.getAllGlossaryTerms(USER_ID,0, 10);

        assertEquals(7, terms.size());
    }

    @Test
    public void getCategoryHomeGlossary() throws Exception{
        response.addEntityDetail(glossaries.get(0));

        when(connector.callGetRESTCall(eq("getCategoryHomeGlossary"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID),eq(categories.get(0).getGuid()))).thenReturn(response);

        Glossary glossary = underTest.getCategoryHomeGlossary(USER_ID, categories.get(0).getGuid());

        assertEquals(glossaries.get(0).getGuid(), glossary.getGuid());
    }

    @Test
    public void getTermHomeGlossary() throws Exception{
        response.addEntityDetail(glossaries.get(1));

        when(connector.callGetRESTCall(eq("getTermHomeGlossary"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID),eq(terms.get(3).getGuid()))).thenReturn(response);

        Glossary glossary = underTest.getTermHomeGlossary(USER_ID, terms.get(3).getGuid());

        assertEquals(glossaries.get(1).getGuid(), glossary.getGuid());
    }

    @Test
    public void getExternalGlossaryLinksOfGlossary() throws Exception{
        response.addEntityDetail(externalGlossaryLink);

        when(connector.callGetRESTCall(eq("getExternalGlossaryLinksOfGlossary"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID),eq(glossaries.get(0).getGuid()), isNull(), isNull())).thenReturn(response);

        List<ExternalGlossaryLink> externalGlossaryLinks = underTest.getExternalGlossaryLinksOfGlossary(USER_ID, glossaries.get(0).getGuid(), null, null);

        assertEquals(1, externalGlossaryLinks.size());
        assertEquals(externalGlossaryLink.getGuid(), externalGlossaryLinks.get(0).getGuid());
    }

    @Test
    public void getExternalGlossaryLinksOfCategory() throws Exception{
        response.addEntityDetail(externalGlossaryLink);

        when(connector.callGetRESTCall(eq("getExternalGlossaryLinksOfCategory"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID),eq(categories.get(0).getGuid()), isNull(), isNull())).thenReturn(response);

        List<ExternalGlossaryLink> externalGlossaryLinks = underTest.getExternalGlossaryLinksOfCategory(USER_ID, categories.get(0).getGuid(), null, null);

        assertEquals(1, externalGlossaryLinks.size());
        assertEquals(externalGlossaryLink.getGuid(), externalGlossaryLinks.get(0).getGuid());
    }

    @Test
    public void getExternalGlossaryLinksOfTerm() throws Exception{
        response.addEntityDetail(externalGlossaryLink);

        when(connector.callGetRESTCall(eq("getExternalGlossaryLinksOfTerm"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID),eq(terms.get(0).getGuid()), isNull(), isNull())).thenReturn(response);

        List<ExternalGlossaryLink> externalGlossaryLinks = underTest.getExternalGlossaryLinksOfTerm(USER_ID, terms.get(0).getGuid(), null, null);

        assertEquals(1, externalGlossaryLinks.size());
        assertEquals(externalGlossaryLink.getGuid(), externalGlossaryLinks.get(0).getGuid());
    }

    @Test
    public void getSubcategories() throws Exception{
        response.addEntityDetails(Arrays.asList(categories.get(3), categories.get(4)));

        when(connector.callGetRESTCall(eq("getSubcategories"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID),eq(categories.get(0).getGuid()), isNull(), isNull())).thenReturn(response);

        List<GlossaryCategory> subcategories = underTest.getSubcategories(USER_ID, categories.get(0).getGuid(), null, null);

        assertEquals(2, subcategories.size());
        assertEquals(categories.get(3).getGuid(), subcategories.get(0).getGuid());
        assertEquals(categories.get(4).getGuid(), subcategories.get(1).getGuid());
    }

    @Test(expected = GlossaryViewOmasException.class)
    public void getTermsOfGlossary_throwException() throws Exception{
        response.setRelatedHTTPCode(501);
        response.setActionDescription("Action description");
        response.setExceptionSystemAction("Exception system action");
        response.setExceptionErrorMessage("Exception error message");
        response.setExceptionUserAction("Exception user action");
        response.setExceptionClassName(GlossaryViewOmasException.class.getName());

        when(connector.callGetRESTCall(eq("getTermsOfGlossary"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID),eq(glossaries.get(0).getGuid()), isNull(), isNull())).thenReturn(response);

        underTest.getTermsOfGlossary(USER_ID, glossaries.get(0).getGuid(), null, null);
    }

    @Test(expected = GlossaryViewOmasException.class)
    public void getRelatedTerms_throwExceptionWithParameterName() throws Exception{
        response.setRelatedHTTPCode(501);
        response.setActionDescription("Action description");
        response.setExceptionSystemAction("Exception system action");
        response.setExceptionErrorMessage("Exception error message");
        response.setExceptionUserAction("Exception user action");
        response.setExceptionClassName(GlossaryViewOmasException.class.getName());

        Map<String, Object> exceptionProperties = new HashMap<>();
        exceptionProperties.put("parameterName", "CocoPharm");
        response.setExceptionProperties(exceptionProperties);

        when(connector.callGetRESTCall(eq("getRelatedTerms"), eq(GlossaryViewEntityDetailResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID),eq(terms.get(0).getGuid()), isNull(), isNull())).thenReturn(response);

        underTest.getRelatedTerms(USER_ID, terms.get(0).getGuid(), null, null);
    }

}
