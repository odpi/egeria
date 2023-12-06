/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.glossaryview.rest.ExternalGlossaryLink;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetail;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class GlossaryViewOmasBase {

    protected static final String USER_ID = "test";
    protected static final String SERVER_NAME = "omas";
    protected static final String GUID_PARAM_NAME = "guid";

    protected static final String GLOSSARY_TYPE_NAME = "Glossary";
    protected static final String GLOSSARY_TYPE_GUID = "glossary-type-guid";
    protected static final String CATEGORY_TYPE_NAME = "GlossaryCategory";
    protected static final String CATEGORY_TYPE_GUID = "category-type-guid";
    protected static final String TERM_TYPE_NAME = "GlossaryTerm";
    protected static final String TERM_TYPE_GUID = "term-type-guid";
    protected static final String EXTERNAL_GLOSSARY_LINK_TYPE_NAME = "ExternalGlossaryLink";
    protected static final String EXTERNAL_GLOSSARY_LINK_TYPE_GUID = "external-glossary-link-type-guid";

    protected static final String CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME = "CategoryHierarchyLink";
    protected static final String CATEGORY_HIERARCHY_LINK_RELATIONSHIP_GUID = "category-hierarchy-link-relationship-type-guid";
    protected static final String EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_NAME = "ExternallySourcedGlossary";
    protected static final String EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_GUID = "externally-sourced-glossary-relationship-type-guid";
    protected static final String CATEGORY_ANCHOR_RELATIONSHIP_NAME = "CategoryAnchor";
    protected static final String CATEGORY_ANCHOR_RELATIONSHIP_GUID = "category-anchor-relationship-type-guid";
    protected static final String TERM_ANCHOR_RELATIONSHIP_NAME = "TermAnchor";
    protected static final String TERM_ANCHOR_RELATIONSHIP_GUID = "term-anchor-relationship-type-guid";
    protected static final String TERM_CATEGORIZATION_RELATIONSHIP_NAME = "TermCategorization";
    protected static final String TERM_CATEGORIZATION_RELATIONSHIP_GUID = "term-categorization-relationship-type-guid";
    protected static final String LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_NAME = "LibraryCategoryReference";
    protected static final String LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_GUID = "library-category-reference-relationship-guid";
    protected static final String LIBRARY_TERM_REFERENCE_RELATIONSHIP_NAME = "LibraryTermReference";
    protected static final String LIBRARY_TERM_REFERENCE_RELATIONSHIP_GUID = "library-term-reference-relationship-guid";
    protected static final String RELATED_TERM_RELATIONSHIP_NAME = "RelatedTerm";
    protected static final String RELATED_TERM_RELATIONSHIP_GUID = "related-term-relationship-guid";
    protected static final String SYNONYM_RELATIONSHIP_NAME = "Synonym";
    protected static final String SYNONYM_RELATIONSHIP_GUID = "synonym-relationship-guid";
    protected static final String ANTONYM_RELATIONSHIP_NAME = "Antonym";
    protected static final String ANTONYM_RELATIONSHIP_GUID = "antonym-relationship-guid";
    protected static final String PREFERRED_TERM_RELATIONSHIP_NAME = "PreferredTerm";
    protected static final String PREFERRED_TERM_RELATIONSHIP_GUID = "preferred-term-relationship-guid";
    protected static final String REPLACEMENT_TERM_RELATIONSHIP_NAME = "ReplacementTerm";
    protected static final String REPLACEMENT_TERM_RELATIONSHIP_GUID = "replacement-term-relationship-guid";
    protected static final String TRANSLATION_RELATIONSHIP_NAME = "Translation";
    protected static final String TRANSLATION_RELATIONSHIP_GUID = "translation-relationship-guid";
    protected static final String IS_A_RELATIONSHIP_NAME = "ISARelationship";
    protected static final String IS_A_RELATIONSHIP_GUID = "is-a-relationship-guid";
    protected static final String VALID_VALUE_RELATIONSHIP_NAME = "ValidValue";
    protected static final String VALID_VALUE_RELATIONSHIP_GUID = "valid-value-relationship-guid";
    protected static final String USED_IN_CONTEXT_RELATIONSHIP_NAME = "UsedInContext";
    protected static final String USED_IN_CONTEXT_RELATIONSHIP_GUID = "used-in-context-relationship-guid";
    protected static final String SEMANTIC_ASSIGNMENT_RELATIONSHIP_NAME = "SemanticAssignment";
    protected static final String SEMANTIC_ASSIGNMENT_RELATIONSHIP_GUID = "semantic-assignment-relationship-guid";
    protected static final String TERM_HAS_A_RELATIONSHIP_NAME = "TermHASARelationship";
    protected static final String TERM_HAS_A_RELATIONSHIP_GUID = "term-has-a-relationship-guid";
    protected static final String TERM_IS_A_TYPE_OF_RELATIONSHIP_NAME = "TermISATypeOfRelationship";
    protected static final String TERM_IS_A_TYPE_OF_RELATIONSHIP_GUID = "term-is-a-type-of-relationship-guid";
    protected static final String TERM_TYPED_BY_RELATIONSHIP_NAME = "TermTYPEDBYRelationship";
    protected static final String TERM_TYPED_BY_RELATIONSHIP_GUID = "term-typed-by-relationship-guid";

    protected Predicate<GlossaryViewEntityDetail> isEffective = e -> {
        if(e.getEffectiveFromTime() == null || e.getEffectiveToTime() == null){
            return true;
        }
        long effectiveFromTime = e.getEffectiveFromTime().getTime();
        long effectiveToTime = e.getEffectiveToTime().getTime();
        long now = Calendar.getInstance().getTimeInMillis();
        return effectiveFromTime <= now && now <= effectiveToTime;
    };

    protected List<EntityDetail> glossaries = new ArrayList<>();
    protected List<EntityDetail> categories = new ArrayList<>();
    protected List<EntityDetail> terms = new ArrayList<>();
    protected EntityDetail externalGlossaryLink;

    @Mock
    protected GlossaryViewInstanceHandler instanceHandler;
    @Mock
    protected OpenMetadataAPIGenericHandler<GlossaryViewEntityDetail> entitiesHandler;
    @Mock
    protected OMRSRepositoryConnector repositoryConnector;
    @Mock
    protected OMRSRepositoryHelper repositoryHelper;

    @Mock
    protected TypeDef glossaryTypeDef;
    @Mock
    protected TypeDef categoryTypeDef;
    @Mock
    protected TypeDef termTypeDef;

    @Mock
    protected TypeDef categoryHierarchyLinkRelationshipTypeDef;
    @Mock
    protected TypeDef externallySourcedGlossaryRelationshipTypeDef;
    @Mock
    protected TypeDef categoryAnchorRelationshipTypeDef;
    @Mock
    protected TypeDef termAnchorRelationshipTypeDef;
    @Mock
    protected TypeDef termCategorizationRelationshipTypeDef;
    @Mock
    protected TypeDef libraryCategoryReferenceRelationshipTypeDef;
    @Mock
    protected TypeDef libraryTermReferenceRelationshipTypeDef;
    @Mock
    protected TypeDef relatedTermRelationshipTypeDef;
    @Mock
    protected TypeDef synonymRelationshipTypeDef;
    @Mock
    protected TypeDef antonymRelationshipTypeDef;
    @Mock
    protected TypeDef preferredTermRelationshipTypeDef;
    @Mock
    protected TypeDef replacementTermRelationshipTypeDef;
    @Mock
    protected TypeDef translationRelationshipTypeDef;
    @Mock
    protected TypeDef isARelationshipTypeDef;
    @Mock
    protected TypeDef validValueRelationshipTypeDef;
    @Mock
    protected TypeDef usedInContextRelationshipTypeDef;
    @Mock
    protected TypeDef semanticAssignmentRelationshipTypeDef;
    @Mock
    protected TypeDef termHasARelationshipTypeDef;
    @Mock
    protected TypeDef termIsATypeOfRelationshipTypeDef;
    @Mock
    protected TypeDef termTypedByRelationshipTypeDef;

    public void before(GlossaryViewOMAS underTest) throws Exception{
        MockitoAnnotations.openMocks(this);
        when(instanceHandler.getRepositoryConnector(eq(USER_ID), eq(SERVER_NAME), anyString())).thenReturn(repositoryConnector);
        when(instanceHandler.getEntitiesHandler(eq(USER_ID), eq(SERVER_NAME), anyString())).thenReturn(entitiesHandler);
        when(repositoryConnector.getRepositoryHelper()).thenReturn(repositoryHelper);

        when(repositoryHelper.getTypeDefByName(anyString(), eq(GLOSSARY_TYPE_NAME))).thenReturn(glossaryTypeDef);
        when(glossaryTypeDef.getGUID()).thenReturn(GLOSSARY_TYPE_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(CATEGORY_TYPE_NAME))).thenReturn(categoryTypeDef);
        when(categoryTypeDef.getGUID()).thenReturn(CATEGORY_TYPE_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(TERM_TYPE_NAME))).thenReturn(termTypeDef);
        when(termTypeDef.getGUID()).thenReturn(TERM_TYPE_GUID);

        when(repositoryHelper.getTypeDefByName(anyString(), eq(CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME))).thenReturn(categoryHierarchyLinkRelationshipTypeDef);
        when(categoryHierarchyLinkRelationshipTypeDef.getGUID()).thenReturn(CATEGORY_HIERARCHY_LINK_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_NAME))).thenReturn(externallySourcedGlossaryRelationshipTypeDef);
        when(externallySourcedGlossaryRelationshipTypeDef.getGUID()).thenReturn(EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(CATEGORY_ANCHOR_RELATIONSHIP_NAME))).thenReturn(categoryAnchorRelationshipTypeDef);
        when(categoryAnchorRelationshipTypeDef.getGUID()).thenReturn(CATEGORY_ANCHOR_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(TERM_ANCHOR_RELATIONSHIP_NAME))).thenReturn(termAnchorRelationshipTypeDef);
        when(termAnchorRelationshipTypeDef.getGUID()).thenReturn(TERM_ANCHOR_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(TERM_CATEGORIZATION_RELATIONSHIP_NAME))).thenReturn(termCategorizationRelationshipTypeDef);
        when(termCategorizationRelationshipTypeDef.getGUID()).thenReturn(TERM_CATEGORIZATION_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_NAME))).thenReturn(libraryCategoryReferenceRelationshipTypeDef);
        when(libraryCategoryReferenceRelationshipTypeDef.getGUID()).thenReturn(LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(LIBRARY_TERM_REFERENCE_RELATIONSHIP_NAME))).thenReturn(libraryTermReferenceRelationshipTypeDef);
        when(libraryTermReferenceRelationshipTypeDef.getGUID()).thenReturn(LIBRARY_TERM_REFERENCE_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(RELATED_TERM_RELATIONSHIP_NAME))).thenReturn(relatedTermRelationshipTypeDef);
        when(relatedTermRelationshipTypeDef.getGUID()).thenReturn(RELATED_TERM_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(SYNONYM_RELATIONSHIP_NAME))).thenReturn(synonymRelationshipTypeDef);
        when(synonymRelationshipTypeDef.getGUID()).thenReturn(SYNONYM_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(ANTONYM_RELATIONSHIP_NAME))).thenReturn(antonymRelationshipTypeDef);
        when(antonymRelationshipTypeDef.getGUID()).thenReturn(ANTONYM_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(PREFERRED_TERM_RELATIONSHIP_NAME))).thenReturn(preferredTermRelationshipTypeDef);
        when(preferredTermRelationshipTypeDef.getGUID()).thenReturn(PREFERRED_TERM_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(REPLACEMENT_TERM_RELATIONSHIP_NAME))).thenReturn(replacementTermRelationshipTypeDef);
        when(replacementTermRelationshipTypeDef.getGUID()).thenReturn(REPLACEMENT_TERM_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(TRANSLATION_RELATIONSHIP_NAME))).thenReturn(translationRelationshipTypeDef);
        when(translationRelationshipTypeDef.getGUID()).thenReturn(TRANSLATION_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(IS_A_RELATIONSHIP_NAME))).thenReturn(isARelationshipTypeDef);
        when(isARelationshipTypeDef.getGUID()).thenReturn(IS_A_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(VALID_VALUE_RELATIONSHIP_NAME))).thenReturn(validValueRelationshipTypeDef);
        when(validValueRelationshipTypeDef.getGUID()).thenReturn(VALID_VALUE_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(USED_IN_CONTEXT_RELATIONSHIP_NAME))).thenReturn(usedInContextRelationshipTypeDef);
        when(usedInContextRelationshipTypeDef.getGUID()).thenReturn(USED_IN_CONTEXT_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(SEMANTIC_ASSIGNMENT_RELATIONSHIP_NAME))).thenReturn(semanticAssignmentRelationshipTypeDef);
        when(semanticAssignmentRelationshipTypeDef.getGUID()).thenReturn(SEMANTIC_ASSIGNMENT_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(TERM_HAS_A_RELATIONSHIP_NAME))).thenReturn(termHasARelationshipTypeDef);
        when(termHasARelationshipTypeDef.getGUID()).thenReturn(TERM_HAS_A_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(TERM_IS_A_TYPE_OF_RELATIONSHIP_NAME))).thenReturn(termIsATypeOfRelationshipTypeDef);
        when(termIsATypeOfRelationshipTypeDef.getGUID()).thenReturn(TERM_IS_A_TYPE_OF_RELATIONSHIP_GUID);
        when(repositoryHelper.getTypeDefByName(anyString(), eq(TERM_TYPED_BY_RELATIONSHIP_NAME))).thenReturn(termTypedByRelationshipTypeDef);
        when(termTypedByRelationshipTypeDef.getGUID()).thenReturn(TERM_TYPED_BY_RELATIONSHIP_GUID);

        Field instanceHandlerField = ReflectionUtils.findField(OMRSClient.class, "instanceHandler");
        instanceHandlerField.setAccessible(true);
        ReflectionUtils.setField(instanceHandlerField, underTest, instanceHandler);
        instanceHandlerField.setAccessible(false);

        glossaries.add(createGlossary("glossary-1"));
        glossaries.add(createGlossary("glossary-2"));
        glossaries.add(createGlossary("glossary-3"));

        categories.add(createCategory("category-1"));
        categories.add(createCategory("category-2"));
        categories.add(createCategory("category-3"));

        terms.add(createTerm("term-1"));
        terms.add(createTerm("term-2"));
        terms.add(createTerm("term-3"));
        terms.add(createTerm("term-4"));
        terms.add(createTerm("term-5"));

        externalGlossaryLink = createExternalGlossaryLink("external-glossary-link-1");
    }


    private EntityDetail createGlossary(String guid){
        return createEntityDetail(guid, createGlossaryType(), createGlossaryProperties(guid), createGlossaryClassifications());
    }

    private EntityDetail createCategory(String guid){
        return createEntityDetail(guid, createCategoryType(), createCategoryProperties(guid), createCategoryClassifications());
    }

    private EntityDetail createTerm(String guid){
        return createEntityDetail(guid, createTermType(), createTermProperties(guid), null);
    }

    private EntityDetail createExternalGlossaryLink(String guid){
        return createEntityDetail(guid, createExternalGlossaryLinkType(), createExternalGlossaryLinkProperties(guid), null);
    }

    private EntityDetail createEntityDetail(String guid, InstanceType instanceType, InstanceProperties instanceProperties,
                                            List<Classification> classifications){
        EntityDetail entityDetail = new EntityDetail();
        entityDetail.setCreatedBy("odpi-egeria");
        entityDetail.setGUID(guid);
        entityDetail.setType(instanceType);
        entityDetail.setStatus(InstanceStatus.ACTIVE);
        entityDetail.setProperties(instanceProperties);
        entityDetail.setClassifications(classifications);

        return entityDetail;
    }

    private InstanceType createGlossaryType(){
        InstanceType type = new InstanceType();
        type.setTypeDefName(GLOSSARY_TYPE_NAME);
        type.setTypeDefGUID(GLOSSARY_TYPE_GUID);

        return type;
    }

    private InstanceType createCategoryType(){
        InstanceType type = new InstanceType();
        type.setTypeDefName(CATEGORY_TYPE_NAME);
        type.setTypeDefGUID(CATEGORY_TYPE_GUID);

        return type;
    }

    private InstanceType createTermType(){
        InstanceType type = new InstanceType();
        type.setTypeDefName(TERM_TYPE_NAME);
        type.setTypeDefGUID(TERM_TYPE_GUID);

        return type;
    }

    private InstanceType createExternalGlossaryLinkType(){
        InstanceType type = new InstanceType();
        type.setTypeDefName(EXTERNAL_GLOSSARY_LINK_TYPE_NAME);
        type.setTypeDefGUID(EXTERNAL_GLOSSARY_LINK_TYPE_GUID);

        return type;
    }

    private InstanceProperties createGlossaryProperties(String guid){
        Calendar from = Calendar.getInstance();
        from.set(Calendar.HOUR_OF_DAY, from.get(Calendar.HOUR_OF_DAY) - 1);
        Date effectiveFromTime = from.getTime();

        Calendar to = Calendar.getInstance();
        to.set(Calendar.HOUR_OF_DAY, to.get(Calendar.HOUR_OF_DAY) + 1);
        Date effectiveToTime = to.getTime();

        InstanceProperties properties = new InstanceProperties();
        properties.setEffectiveFromTime(effectiveFromTime);
        properties.setEffectiveToTime(effectiveToTime);
        properties.setProperty("qualifiedName", createPrimitiveStringPropertyValue("Qualified Name of " + guid));
        properties.setProperty("displayName", createPrimitiveStringPropertyValue("Display Name of " + guid));
        properties.setProperty("language", createPrimitiveStringPropertyValue("Language of " + guid));
        properties.setProperty("description", createPrimitiveStringPropertyValue("Description of " + guid));
        properties.setProperty("usage", createPrimitiveStringPropertyValue("Usage of " + guid));

        return properties;
    }

    private InstanceProperties createCategoryProperties(String guid){
        Calendar from = Calendar.getInstance();
        from.set(Calendar.HOUR_OF_DAY, from.get(Calendar.HOUR_OF_DAY) - 1);
        Date effectiveFromTime = from.getTime();

        Calendar to = Calendar.getInstance();
        to.set(Calendar.HOUR_OF_DAY, to.get(Calendar.HOUR_OF_DAY) + 1);
        Date effectiveToTime = to.getTime();

        InstanceProperties properties = new InstanceProperties();
        properties.setEffectiveFromTime(effectiveFromTime);
        properties.setEffectiveToTime(effectiveToTime);
        properties.setProperty("qualifiedName", createPrimitiveStringPropertyValue("Qualified Name of " + guid));
        properties.setProperty("displayName", createPrimitiveStringPropertyValue("Display Name of " + guid));
        properties.setProperty("description", createPrimitiveStringPropertyValue("Description of " + guid));

        return properties;
    }

    private InstanceProperties createTermProperties(String guid){
        Calendar from = Calendar.getInstance();
        from.set(Calendar.HOUR_OF_DAY, from.get(Calendar.HOUR_OF_DAY) - 1);
        Date effectiveFromTime = from.getTime();

        Calendar to = Calendar.getInstance();
        to.set(Calendar.HOUR_OF_DAY, to.get(Calendar.HOUR_OF_DAY) + 1);
        Date effectiveToTime = to.getTime();

        if(guid.equalsIgnoreCase("term-5")){
            from.set(Calendar.HOUR_OF_DAY, from.get(Calendar.HOUR_OF_DAY) + 2);
            to.set(Calendar.HOUR_OF_DAY, to.get(Calendar.HOUR_OF_DAY) + 4);
        }

        InstanceProperties properties = new InstanceProperties();
        properties.setEffectiveFromTime(effectiveFromTime);
        properties.setEffectiveToTime(effectiveToTime);
        properties.setProperty("qualifiedName", createPrimitiveStringPropertyValue("Qualified Name of " + guid));
        properties.setProperty("displayName", createPrimitiveStringPropertyValue("Display Name of " + guid));
        properties.setProperty("summary", createPrimitiveStringPropertyValue("Summary of " + guid));
        properties.setProperty("description", createPrimitiveStringPropertyValue("Description of " + guid));
        properties.setProperty("examples", createPrimitiveStringPropertyValue("Examples of " + guid));
        properties.setProperty("abbreviation", createPrimitiveStringPropertyValue("Abbreviation of " + guid));
        properties.setProperty("usage", createPrimitiveStringPropertyValue("Usage of " + guid));

        return properties;
    }

    private InstanceProperties createExternalGlossaryLinkProperties(String guid){
        Calendar from = Calendar.getInstance();
        from.set(Calendar.HOUR_OF_DAY, from.get(Calendar.HOUR_OF_DAY) - 1);
        Date effectiveFromTime = from.getTime();

        Calendar to = Calendar.getInstance();
        to.set(Calendar.HOUR_OF_DAY, to.get(Calendar.HOUR_OF_DAY) + 1);
        Date effectiveToTime = to.getTime();

        InstanceProperties properties = new InstanceProperties();
        properties.setEffectiveFromTime(effectiveFromTime);
        properties.setEffectiveToTime(effectiveToTime);
        properties.setProperty("qualifiedName", createPrimitiveStringPropertyValue("Qualified Name of " + guid));

        return properties;
    }

    private List<Classification> createGlossaryClassifications(){
        List<Classification> classifications = new ArrayList<>();
        InstanceType type = new InstanceType();
        type.setTypeDefName(GLOSSARY_TYPE_NAME);

        InstanceProperties properties = new InstanceProperties();
        properties.setProperty("organizingPrinciple", createPrimitiveStringPropertyValue("Organizing Principle"));
        Classification classification = new Classification();
        classification.setName("Taxonomy");
        classification.setProperties(properties);
        classification.setType(type);
        classification.setStatus(InstanceStatus.ACTIVE);

        classifications.add(classification);

        properties = new InstanceProperties();
        properties.setProperty("scope", createPrimitiveStringPropertyValue("Scope"));
        classification = new Classification();
        classification.setName("CanonicalVocabulary");
        classification.setProperties(properties);
        classification.setType(type);
        classification.setStatus(InstanceStatus.ACTIVE);

        classifications.add(classification);

        return classifications;
    }

    private List<Classification> createCategoryClassifications(){
        List<Classification> classifications = new ArrayList<>();
        InstanceType type = new InstanceType();
        type.setTypeDefName(CATEGORY_TYPE_NAME);

        InstanceProperties properties = new InstanceProperties();
        properties.setProperty("name", createPrimitiveStringPropertyValue("Name"));
        Classification classification = new Classification();
        classification.setName("SubjectArea");
        classification.setProperties(properties);
        classification.setType(type);
        classification.setStatus(InstanceStatus.ACTIVE);

        classifications.add(classification);

        return classifications;
    }

    private PrimitivePropertyValue createPrimitiveStringPropertyValue(String value) {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();
        propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        propertyValue.setPrimitiveValue(value);
        propertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
        propertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        return propertyValue;
    }

    protected void assertExternalGlossaryLinkProperties(EntityDetail expected, ExternalGlossaryLink actual){
        assertEquals(expected.getGUID(), actual.getGuid());
        assertEquals(expected.getProperties().getPropertyValue("qualifiedName").valueAsString(), actual.getQualifiedName());
    }

    protected void assertExceptionDataInResponse(PropertyServerException expected, GlossaryViewEntityDetailResponse actual){
        assertEquals(expected.getReportedHTTPCode(), actual.getRelatedHTTPCode());
        assertEquals(expected.getReportingClassName(), actual.getExceptionClassName());
        assertEquals(expected.getReportingActionDescription(), actual.getActionDescription());
        assertEquals(expected.getReportedErrorMessage(), actual.getExceptionErrorMessage());
        assertEquals(expected.getReportedSystemAction(), actual.getExceptionSystemAction());
        assertEquals(expected.getReportedUserAction(), actual.getExceptionUserAction());
    }

}
