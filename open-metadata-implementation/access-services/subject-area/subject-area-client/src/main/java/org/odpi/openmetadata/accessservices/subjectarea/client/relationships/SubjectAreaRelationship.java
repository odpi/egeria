/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The OMAS client library implementation of the Subject Area OMAS.
 * This interface provides relationship {@link Relationship} authoring interface for subject area experts.
 */
public class SubjectAreaRelationship implements SubjectAreaRelationshipClients {
    private static final String HAS_A = "has-as";
    private static final String RELATED_TERM = "related-terms";
    private static final String SYNONYM = "synonyms";
    private static final String ANTONYM = "antonyms";
    private static final String TRANSLATION = "translations";
    private static final String USED_IN_CONTEXT = "used-in-contexts";
    private static final String PREFERRED_TERM = "preferred-terms";
    private static final String VALID_VALUE = "valid-values";
    private static final String REPLACEMENT_TERM = "replacement-terms";
    private static final String TYPED_BY = "typed-bys";
    private static final String IS_A = "is-as";
    private static final String IS_A_TYPE_OF = "is-a-type-ofs";
    private static final String TERM_CATEGORIZATION = "term-categorizations";
    private static final String SEMANTIC_ASSIGNMENT = "semantic-assignments";
    private static final String TERM_ANCHOR = "term-anchor";
    private static final String CATEGORY_ANCHOR = "category-anchor";
    private static final String PROJECT_SCOPE = "project-scopes";
    private static final String CATEGORY_HIERARCHY_LINK = "category-hierarchy-link";

    private final Map<Class<?>, org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient> cache = new HashMap<>();
    private static final String DEFAULT_SCAN_PACKAGE = SubjectAreaRelationship.class.getPackage().getName();

    /**
     * @param packagesToScan - search packages for finding classes placed by annotation {@link SubjectAreaRelationshipClient}
     * @param subjectAreaRestClient - rest client for Subject Area OMAS REST APIs
     * */
    @SuppressWarnings("rawtypes")
    public SubjectAreaRelationship(SubjectAreaRestClient subjectAreaRestClient, String... packagesToScan) {
        Set<String> packages = new HashSet<>(Arrays.asList(packagesToScan));
        packages.add(DEFAULT_SCAN_PACKAGE);

        Reflections reflections = new Reflections(packages);
        Set<Class<?>> clientClasses = reflections.getTypesAnnotatedWith(SubjectAreaRelationshipClient.class);
        for (Class<?> declaredClass : clientClasses) {
            try {
                if (AbstractSubjectAreaRelationship.class.isAssignableFrom(declaredClass)) {
                    Constructor<?> ctor = declaredClass.getDeclaredConstructor(SubjectAreaRestClient.class);
                    ctor.setAccessible(true);
                    final AbstractSubjectAreaRelationship newInstance =
                            (AbstractSubjectAreaRelationship) ctor.newInstance(subjectAreaRestClient);
                    cache.put(newInstance.resultType(), newInstance);
                }
            } catch (NoSuchMethodException
                    | IllegalAccessException
                    | InstantiationException
                    | InvocationTargetException e) {
                throw new ExceptionInInitializerError(
                        "During initialization `SubjectArearelationship` an error has occurred - "
                                + e.getMessage()
                );
            }
        }
    }

    /**
     *  The constructor uses the current package to scan "org.odpi.openmetadata.accessservices.subjectarea.client.relationships"
     *  to search for classes placed by annotation {@link SubjectAreaRelationshipClient}.
     *
     * @param subjectAreaRestClient - rest client for Subject Area OMAS REST APIs
     */
    public SubjectAreaRelationship(SubjectAreaRestClient subjectAreaRestClient) {
        this(subjectAreaRestClient, DEFAULT_SCAN_PACKAGE);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient hasA() {
        return getClient(HasA.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient relatedTerm() {
        return getClient(RelatedTerm.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient termAnchor() {
        return getClient(TermAnchor.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient projectScope() {
        return getClient(ProjectScope.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient synonym() {
        return getClient(Synonym.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient antonym() {
        return getClient(Antonym.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient translation() {
        return getClient(Translation.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient usedInContext() {
        return getClient(UsedInContext.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient preferredTerm() {
        return getClient(PreferredTerm.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient validValue() {
        return getClient(ValidValue.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient replacementTerm() {
        return getClient(ReplacementTerm.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient typedBy() {
        return getClient(TypedBy.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient isA() {
        return getClient(IsA.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient isaTypeOf() {
        return getClient(IsATypeOf.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient termCategorization() {
        return getClient(Categorization.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient semanticAssignment() {
        return getClient(SemanticAssignment.class);
    }

    @Override
    public SubjectAreaClient<CategoryHierarchyLink> categoryHierarchyLink() {
        return getClient(CategoryHierarchyLink.class);
    }

    @Override
    public org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient categoryAnchor() {
        return getClient(CategoryAnchor.class);
    }

    @SubjectAreaRelationshipClient
    static class SubjectAreaCategoryHierarchyLinkClient extends AbstractSubjectAreaRelationship<CategoryHierarchyLink> {
        protected SubjectAreaCategoryHierarchyLinkClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, CATEGORY_HIERARCHY_LINK);
        }
    }

    @SubjectAreaRelationshipClient
    static class SubjectAreaTermAnchorClient extends AbstractSubjectAreaRelationship<TermAnchor> {
        protected SubjectAreaTermAnchorClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, TERM_ANCHOR);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaRelatedTermClient extends AbstractSubjectAreaRelationship<RelatedTerm> {
        protected SubjectAreaRelatedTermClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, RELATED_TERM);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaHasaClient extends AbstractSubjectAreaRelationship<HasA> {
        protected SubjectAreaHasaClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, HAS_A);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaSynonymClient extends AbstractSubjectAreaRelationship<Synonym> {
        protected SubjectAreaSynonymClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, SYNONYM);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaAntonymClient extends AbstractSubjectAreaRelationship<Antonym> {
        protected SubjectAreaAntonymClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, ANTONYM);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaCategoryAnchorClient extends AbstractSubjectAreaRelationship<CategoryAnchor> {
        protected SubjectAreaCategoryAnchorClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, CATEGORY_ANCHOR);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaCategorizationClient extends AbstractSubjectAreaRelationship<Categorization> {
        protected SubjectAreaCategorizationClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, TERM_CATEGORIZATION);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaTranslationClient extends AbstractSubjectAreaRelationship<Translation> {
        protected SubjectAreaTranslationClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, TRANSLATION);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaUsedInContextClient extends AbstractSubjectAreaRelationship<UsedInContext> {
        protected SubjectAreaUsedInContextClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, USED_IN_CONTEXT);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaPreferredTermClient extends AbstractSubjectAreaRelationship<PreferredTerm> {
        protected SubjectAreaPreferredTermClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, PREFERRED_TERM);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaValidValueClient extends AbstractSubjectAreaRelationship<ValidValue> {
        protected SubjectAreaValidValueClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, VALID_VALUE);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaReplacementTermClient extends AbstractSubjectAreaRelationship<ReplacementTerm> {
        protected SubjectAreaReplacementTermClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, REPLACEMENT_TERM);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaSemanticAssignmentClient extends AbstractSubjectAreaRelationship<SemanticAssignment> {
        protected SubjectAreaSemanticAssignmentClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, SEMANTIC_ASSIGNMENT);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaTypedByClient extends AbstractSubjectAreaRelationship<TypedBy> {
        protected SubjectAreaTypedByClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, TYPED_BY);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaIsaClient extends AbstractSubjectAreaRelationship<IsA> {
        protected SubjectAreaIsaClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, IS_A);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaIsaTypeOfClient extends AbstractSubjectAreaRelationship<IsATypeOf> {
        protected SubjectAreaIsaTypeOfClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, IS_A_TYPE_OF);
        }
    }
    @SubjectAreaRelationshipClient
    static class SubjectAreaProjectScopeClient extends AbstractSubjectAreaRelationship<ProjectScope> {
        protected SubjectAreaProjectScopeClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, PROJECT_SCOPE);
        }
    }

    /**
     * @param <T> - {@link Relationship} type of object
     * @param clazz - the class for which you want to get the client from cache
     *
     * @return SubjectAreaRelationshipClient or null if this client is not present
     * */
    @SuppressWarnings("unchecked")
    public <T extends Relationship> org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient getClient(Class<T> clazz) {
        if (cache.containsKey(clazz)) {
            return (org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient) cache.get(clazz);
        }
        final ExceptionMessageDefinition messageDefinition =
                SubjectAreaErrorCode.NOT_FOUND_CLIENT.getMessageDefinition(clazz.getName());
        final SubjectAreaCheckedException exc =
                new SubjectAreaCheckedException(messageDefinition, getClass().getName(), messageDefinition.getSystemAction());
        throw new IllegalArgumentException(exc);
    }
}