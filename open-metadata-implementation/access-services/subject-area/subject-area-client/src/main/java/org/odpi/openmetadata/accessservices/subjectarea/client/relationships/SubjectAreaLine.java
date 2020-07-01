/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The OMAS client library implementation of the Subject Area OMAS.
 * This interface provides relationship {@link Line} authoring interface for subject area experts.
 * A standard set of customers is described in {@link SubjectAreaRelationship}
 */
public class SubjectAreaLine implements SubjectAreaRelationship {
    private static final String HASA = "has-as";
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

    private Map<Class<?>, SubjectAreaRelationshipClient<?>> cache = new HashMap<>();
    private static final String DEFAULT_SCAN_PACKAGE = SubjectAreaLine.class.getPackage().getName();

    /**
     * @param packagesToScan - search packages for finding classes placed by annotation {@link SubjectAreaLineClient}
     * @param subjectAreaRestClient - rest client for Subject Area OMAS REST APIs
     * */
    @SuppressWarnings("rawtypes")
    public SubjectAreaLine(SubjectAreaRestClient subjectAreaRestClient, String... packagesToScan) {
        Set<String> packages = new HashSet<>(Arrays.asList(packagesToScan));
        packages.add(DEFAULT_SCAN_PACKAGE);

        Reflections reflections = new Reflections(packages);
        Set<Class<?>> clientClasses = reflections.getTypesAnnotatedWith(SubjectAreaLineClient.class);
        for (Class<?> declaredClass : clientClasses) {
            try {
                if (AbstractSubjectAreaRelationship.class.isAssignableFrom(declaredClass)) {
                    Constructor<?> ctor = declaredClass.getDeclaredConstructor(SubjectAreaRestClient.class);
                    ctor.setAccessible(true);
                    final AbstractSubjectAreaRelationship newInstance =
                            (AbstractSubjectAreaRelationship) ctor.newInstance(subjectAreaRestClient);
                    cache.put(newInstance.type(), newInstance);
                }
            } catch (NoSuchMethodException
                    | IllegalAccessException
                    | InstantiationException
                    | InvocationTargetException e) {
                throw new ExceptionInInitializerError(
                        "During initialization SubjectAreaLine an error has occurred - "
                                + e.getMessage()
                );
            }
        }
    }

    /**
     *  The constructor uses the current package to scan "org.odpi.openmetadata.accessservices.subjectarea.client.relationships"
     *  to search for classes placed by annotation {@link SubjectAreaLineClient}.
     *
     * @param subjectAreaRestClient - rest client for Subject Area OMAS REST APIs
     */
    public SubjectAreaLine(SubjectAreaRestClient subjectAreaRestClient) {
        this(subjectAreaRestClient, DEFAULT_SCAN_PACKAGE);
    }

    @Override
    public SubjectAreaRelationshipClient<Hasa> hasa() {
        return getClient(Hasa.class);
    }

    @Override
    public SubjectAreaRelationshipClient<RelatedTerm> relatedTerm() {
        return getClient(RelatedTerm.class);
    }

    @Override
    public SubjectAreaRelationshipClient<TermAnchor> termAnchor() {
        return getClient(TermAnchor.class);
    }

    @Override
    public SubjectAreaRelationshipClient<ProjectScope> projectScope() {
        return getClient(ProjectScope.class);
    }

    @Override
    public SubjectAreaRelationshipClient<Synonym> synonym() {
        return getClient(Synonym.class);
    }

    @Override
    public SubjectAreaRelationshipClient<Antonym> antonym() {
        return getClient(Antonym.class);
    }

    @Override
    public SubjectAreaRelationshipClient<Translation> translation() {
        return getClient(Translation.class);
    }

    @Override
    public SubjectAreaRelationshipClient<UsedInContext> usedInContext() {
        return getClient(UsedInContext.class);
    }

    @Override
    public SubjectAreaRelationshipClient<PreferredTerm> preferredTerm() {
        return getClient(PreferredTerm.class);
    }

    @Override
    public SubjectAreaRelationshipClient<ValidValue> validValue() {
        return getClient(ValidValue.class);
    }

    @Override
    public SubjectAreaRelationshipClient<ReplacementTerm> replacementTerm() {
        return getClient(ReplacementTerm.class);
    }

    @Override
    public SubjectAreaRelationshipClient<TypedBy> typedBy() {
        return getClient(TypedBy.class);
    }

    @Override
    public SubjectAreaRelationshipClient<Isa> isa() {
        return getClient(Isa.class);
    }

    @Override
    public SubjectAreaRelationshipClient<IsaTypeOf> isaTypeOf() {
        return getClient(IsaTypeOf.class);
    }

    @Override
    public SubjectAreaRelationshipClient<Categorization> termCategorization() {
        return getClient(Categorization.class);
    }

    @Override
    public SubjectAreaRelationshipClient<SemanticAssignment> semanticAssignment() {
        return getClient(SemanticAssignment.class);
    }

    @Override
    public SubjectAreaRelationshipClient<CategoryAnchor> categoryAnchor() {
        return getClient(CategoryAnchor.class);
    }
    @SubjectAreaLineClient
    static class SubjectAreaTermAnchorClient extends AbstractSubjectAreaRelationship<TermAnchor> {
        protected SubjectAreaTermAnchorClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, TERM_ANCHOR);
        }

        @Override
        public Class<TermAnchor> type() {
            return TermAnchor.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaRelatedTermClient extends AbstractSubjectAreaRelationship<RelatedTerm> {
        protected SubjectAreaRelatedTermClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, RELATED_TERM);
        }

        @Override
        public Class<RelatedTerm> type() {
            return RelatedTerm.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaHasaClient extends AbstractSubjectAreaRelationship<Hasa> {
        protected SubjectAreaHasaClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, HASA);
        }

        @Override
        public Class<Hasa> type() {
            return Hasa.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaSynonymClient extends AbstractSubjectAreaRelationship<Synonym> {
        protected SubjectAreaSynonymClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, SYNONYM);
        }

        @Override
        public Class<Synonym> type() {
            return Synonym.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaAntonymClient extends AbstractSubjectAreaRelationship<Antonym> {
        protected SubjectAreaAntonymClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, ANTONYM);
        }

        @Override
        public Class<Antonym> type() {
            return Antonym.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaCategoryAnchorClient extends AbstractSubjectAreaRelationship<CategoryAnchor> {
        protected SubjectAreaCategoryAnchorClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, CATEGORY_ANCHOR);
        }

        @Override
        public Class<CategoryAnchor> type() {
            return CategoryAnchor.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaCategorizationClient extends AbstractSubjectAreaRelationship<Categorization> {
        protected SubjectAreaCategorizationClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, TERM_CATEGORIZATION);
        }

        @Override
        public Class<Categorization> type() {
            return Categorization.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaTranslationClient extends AbstractSubjectAreaRelationship<Translation> {
        protected SubjectAreaTranslationClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, TRANSLATION);
        }

        @Override
        public Class<Translation> type() {
            return Translation.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaUsedInContextClient extends AbstractSubjectAreaRelationship<UsedInContext> {
        protected SubjectAreaUsedInContextClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, USED_IN_CONTEXT);
        }

        @Override
        public Class<UsedInContext> type() {
            return UsedInContext.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaPreferredTermClient extends AbstractSubjectAreaRelationship<PreferredTerm> {
        protected SubjectAreaPreferredTermClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, PREFERRED_TERM);
        }

        @Override
        public Class<PreferredTerm> type() {
            return PreferredTerm.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaValidValueClient extends AbstractSubjectAreaRelationship<ValidValue> {
        protected SubjectAreaValidValueClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, VALID_VALUE);
        }

        @Override
        public Class<ValidValue> type() {
            return ValidValue.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaReplacementTermClient extends AbstractSubjectAreaRelationship<ReplacementTerm> {
        protected SubjectAreaReplacementTermClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, REPLACEMENT_TERM);
        }

        @Override
        public Class<ReplacementTerm> type() {
            return ReplacementTerm.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaSemanticAssignmentClient extends AbstractSubjectAreaRelationship<SemanticAssignment> {
        protected SubjectAreaSemanticAssignmentClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, SEMANTIC_ASSIGNMENT);
        }

        @Override
        public Class<SemanticAssignment> type() {
            return SemanticAssignment.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaTypedByClient extends AbstractSubjectAreaRelationship<TypedBy> {
        protected SubjectAreaTypedByClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, TYPED_BY);
        }

        @Override
        public Class<TypedBy> type() {
            return TypedBy.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaIsaClient extends AbstractSubjectAreaRelationship<Isa> {
        protected SubjectAreaIsaClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, IS_A);
        }

        @Override
        public Class<Isa> type() {
            return Isa.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaIsaTypeOfClient extends AbstractSubjectAreaRelationship<IsaTypeOf> {
        protected SubjectAreaIsaTypeOfClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, IS_A_TYPE_OF);
        }

        @Override
        public Class<IsaTypeOf> type() {
            return IsaTypeOf.class;
        }
    }
    @SubjectAreaLineClient
    static class SubjectAreaProjectScopeClient extends AbstractSubjectAreaRelationship<ProjectScope> {
        protected SubjectAreaProjectScopeClient(SubjectAreaRestClient subjectAreaRestClient) {
            super(subjectAreaRestClient, PROJECT_SCOPE);
        }

        @Override
        public Class<ProjectScope> type() {
            return ProjectScope.class;
        }
    }

    /**
     * @param <T> - {@link Line} type of object
     * @param clazz - the class for which you want to get the client from cache
     *
     * @return SubjectAreaRelationshipClient or null if this client is not present
     * */
    @SuppressWarnings("unchecked")
    public <T extends Line> SubjectAreaRelationshipClient<T> getClient(Class<T> clazz) {
        if (cache.containsKey(clazz)) {
            return (SubjectAreaRelationshipClient<T>) cache.get(clazz);
        }
        return null;
    }
}