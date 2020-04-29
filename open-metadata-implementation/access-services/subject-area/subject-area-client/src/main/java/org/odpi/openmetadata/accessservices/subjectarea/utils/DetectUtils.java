/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utils;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Graph;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;

import java.util.List;

/**
 * Utility methods take a response and look for a particular type of response.
 * For Exception response there are some detect and throw methods that throw, which look for a particular Exception and if it is found throws it.
 */
public class DetectUtils {
     private static  String className = "DetectUtils";
    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws InvalidParameterException - encoded exception from the handlers
     */
    public static void detectAndThrowInvalidParameterException(String methodName,
                                                         SubjectAreaOMASAPIResponse restResponse) throws InvalidParameterException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.InvalidParameterException)) {

            InvalidParameterExceptionResponse   invalidParameterExceptionResponse = ( InvalidParameterExceptionResponse) restResponse;
            throw new InvalidParameterException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    invalidParameterExceptionResponse.getExceptionErrorMessage(),
                    invalidParameterExceptionResponse.getExceptionSystemAction(),
                    invalidParameterExceptionResponse.getExceptionUserAction());
        }
    }

    /**
     * Throw an UnrecognizedGUIDException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws UnrecognizedGUIDException - encoded exception from the handlers
     */
    public static void detectAndThrowUnrecognizedGUIDException(String methodName,
                                                     SubjectAreaOMASAPIResponse restResponse) throws UnrecognizedGUIDException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.UnrecognizedGUIDException)) {

            UnrecognizedGUIDExceptionResponse   unrecognizedGUIDExceptionResponse = ( UnrecognizedGUIDExceptionResponse) restResponse;
            throw new UnrecognizedGUIDException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    unrecognizedGUIDExceptionResponse.getExceptionErrorMessage(),
                    unrecognizedGUIDExceptionResponse.getExceptionSystemAction(),
                    unrecognizedGUIDExceptionResponse.getExceptionUserAction(),
                    unrecognizedGUIDExceptionResponse.getGuid()
            );
        }
    }

    /**
     * Throw an ClassificationException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws ClassificationException - encoded exception from the handlers
     */
    public static void detectAndThrowClassificationException(String methodName,
                                                        SubjectAreaOMASAPIResponse restResponse) throws ClassificationException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.ClassificationException)) {

            ClassificationExceptionResponse   classificationExceptionResponse = ( ClassificationExceptionResponse) restResponse;
            throw new ClassificationException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    classificationExceptionResponse.getExceptionErrorMessage(),
            classificationExceptionResponse.getExceptionSystemAction(),
            classificationExceptionResponse.getExceptionUserAction());
        }
    }

    /**
     * Throw an EntityNotDeletedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws EntityNotDeletedException - encoded exception from the handlers
     */
    public static void detectAndThrowEntityNotDeletedException(String methodName,
                                                      SubjectAreaOMASAPIResponse restResponse) throws EntityNotDeletedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.EntityNotDeletedException)) {

            EntityNotDeletedExceptionResponse   entityNotDeletedExceptionResponse = ( EntityNotDeletedExceptionResponse) restResponse;
            throw new EntityNotDeletedException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    entityNotDeletedExceptionResponse.getExceptionErrorMessage(),
                    entityNotDeletedExceptionResponse.getExceptionSystemAction(),
                    entityNotDeletedExceptionResponse.getExceptionUserAction(),
                    entityNotDeletedExceptionResponse.getGuid()
            );
        }
    }

    /**
     * Throw an FunctionNotSupportedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws FunctionNotSupportedException - encoded exception from the handlers
     */
    public static void detectAndThrowFunctionNotSupportedException(String methodName,
                                                        SubjectAreaOMASAPIResponse restResponse) throws FunctionNotSupportedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.FunctionNotSupportedException)) {

            FunctionNotSupportedExceptionResponse   functionNotSupportedExceptionResponse = ( FunctionNotSupportedExceptionResponse) restResponse;
            throw new FunctionNotSupportedException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    functionNotSupportedExceptionResponse.getExceptionErrorMessage(),
            functionNotSupportedExceptionResponse.getExceptionSystemAction(),
            functionNotSupportedExceptionResponse.getExceptionUserAction());
        }
    }
    /**
     * Throw an GUIDNotPurgedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws GUIDNotPurgedException - encoded exception from the handlers
     */
    public static void detectAndThrowGUIDNotPurgedException(String methodName,
                                                            SubjectAreaOMASAPIResponse restResponse) throws GUIDNotPurgedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.GUIDNotPurgedException)) {

            GUIDNotPurgedExceptionResponse   gUIDNotPurgedExceptionResponse = ( GUIDNotPurgedExceptionResponse) restResponse;
            throw new GUIDNotPurgedException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    gUIDNotPurgedExceptionResponse.getExceptionErrorMessage(),
                    gUIDNotPurgedExceptionResponse.getExceptionSystemAction(),
                    gUIDNotPurgedExceptionResponse.getExceptionUserAction(),
                    gUIDNotPurgedExceptionResponse.getGuid()
            );
        }
    }

    /**
     * Throw an RelationshipNotDeletedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws RelationshipNotDeletedException - encoded exception from the handlers
     */
    public static void detectAndThrowRelationshipNotDeletedException(String methodName,
                                                            SubjectAreaOMASAPIResponse restResponse) throws RelationshipNotDeletedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.RelationshipNotDeletedException)) {

            RelationshipNotDeletedExceptionResponse   relationshipNotDeletedExceptionResponse = ( RelationshipNotDeletedExceptionResponse) restResponse;
            throw new RelationshipNotDeletedException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    relationshipNotDeletedExceptionResponse.getExceptionErrorMessage(),
                    relationshipNotDeletedExceptionResponse.getExceptionSystemAction(),
                    relationshipNotDeletedExceptionResponse.getExceptionUserAction(),
                    relationshipNotDeletedExceptionResponse.getGuid()
                    );
        }
    }

    /**
     * Throw an StatusNotSupportedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws StatusNotSupportedException - encoded exception from the handlers
     */
    public static void detectAndThrowStatusNotSupportedException(String methodName,
                                                            SubjectAreaOMASAPIResponse restResponse) throws StatusNotSupportedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.StatusNotSupportedException)) {

            StatusNotsupportedExceptionResponse   statusNotSupportedExceptionResponse = ( StatusNotsupportedExceptionResponse) restResponse;
            throw new StatusNotSupportedException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    statusNotSupportedExceptionResponse.getExceptionErrorMessage(),
                    statusNotSupportedExceptionResponse.getExceptionSystemAction(),
                    statusNotSupportedExceptionResponse.getExceptionUserAction());
        }
    }

    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws UserNotAuthorizedException - encoded exception from the handlers
     */
    public static void detectAndThrowUserNotAuthorizedException(String methodName,
                                                            SubjectAreaOMASAPIResponse restResponse) throws UserNotAuthorizedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.UserNotAuthorizedException)) {

            UserNotAuthorizedExceptionResponse   userNotAuthorizedExceptionResponse = ( UserNotAuthorizedExceptionResponse) restResponse;
            throw new UserNotAuthorizedException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    userNotAuthorizedExceptionResponse.getExceptionErrorMessage(),
                    userNotAuthorizedExceptionResponse.getExceptionSystemAction(),
                    userNotAuthorizedExceptionResponse.getExceptionUserAction(),
                    userNotAuthorizedExceptionResponse.getUserId()
                    );
        }
    }
    /**
     * Detect Void return. If we do not find one then throw an Exception
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws UnexpectedResponseException - if the response is not a glossary then throw this exception
     */
    public static void detectVoid(String methodName,
                                                   SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Void)) {
            // expected void response
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
    }

    /**
     * Detect and return a Glossary object from the supplied response. If we do not find one then throw an Exception
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return Glossary if the supplied response is a glossary response
     * @throws UnexpectedResponseException - if the response is not a glossary then throw this exception
     */
    public static Glossary detectAndReturnGlossary(String methodName,
                                                           SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Glossary glossary = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Glossary)) {
            GlossaryResponse glossaryResponse = (GlossaryResponse)restResponse;
            glossary = glossaryResponse.getGlossary();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return glossary;
    }
    public static List<Glossary> detectAndReturnGlossaries(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        List<Glossary> glossaries = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Glossaries)) {
            GlossariesResponse glossariesResponse = (GlossariesResponse)restResponse;
            glossaries = glossariesResponse.getGlossaries();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return glossaries;
    }
    /**
     * Detect and return a Project object from the supplied response. If we do not find one then throw an Exception
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return Project if the supplied response is a project response
     * @throws UnexpectedResponseException - if the response is not a project then throw this exception
     */
    public static Project detectAndReturnProject(String methodName,
                                                   SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Project project = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Project)) {
            ProjectResponse projectResponse = (ProjectResponse)restResponse;
            project = projectResponse.getProject();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return project;
    }
    /**
     * Detect and return a List of Project objects from the supplied response. If we do not find one then throw an Exception
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return Projects if the supplied response is a project response
     * @throws UnexpectedResponseException - if the response is not a project then throw this exception
     */
    public static List<Project> detectAndReturnProjects(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        List<Project> projects = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Projects)) {
            ProjectsResponse projectsResponse = (ProjectsResponse)restResponse;
            projects = projectsResponse.getProjects();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return projects;
    }
    /**
     * Detect and return a Term object from the supplied response. If we do not find one then throw an Exception
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return Term if the supplied response is a term response
     * @throws UnexpectedResponseException - if the response is not a Term then throw this exception
     */
    public static Term detectAndReturnTerm(String methodName,
                                                   SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Term term = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Term)) {
            TermResponse termResponse = (TermResponse)restResponse;
            term = termResponse.getTerm();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return term;
    }
    /**
     * Detect and return a List of Terms from the supplied response. If we do not find one then throw an Exception
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return List of Terms if the supplied response is a terms response
     * @throws UnexpectedResponseException - if the response is not a Terms then throw this exception
     */
    public static List<Term> detectAndReturnTerms(String methodName,
                                           SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
       List<Term> terms = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Terms)) {
            TermsResponse termsResponse = (TermsResponse)restResponse;
            terms = termsResponse.getTerms();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return terms;
    }
    public static Graph detectAndReturnGraph(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Graph graph = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Graph)) {
            GraphResponse GraphResponse = (GraphResponse)restResponse;
            graph = GraphResponse.getGraph();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return graph;
    }

    /*
     * Detect and return a List of relationships from the supplied response. If we do not find the expected response then throw an Exception
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return List<Line> list of Term relationships is the supplied response is Lines response
     * @throws UnexpectedResponseException - if the response is not a Term then throw this exception
     */
    public static List<Line> detectAndReturnLines(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        List<Line> lines = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Lines)) {
            LinesResponse linesResponse = (LinesResponse)restResponse;
            lines = linesResponse.getLines();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return lines;

    }
    /**
     * Detect and return a Category object from the supplied response. If we do not find one then throw an Exception
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return Category if the supplied response is a category response
     * @throws UnexpectedResponseException - if the response is not a Category then throw this exception
     */
    public static Category detectAndReturnCategory(String methodName,
                                                   SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Category category = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Category)) {
            CategoryResponse categoryResponse = (CategoryResponse)restResponse;
            category = categoryResponse.getCategory();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return category;
    }
    public static List<Category> detectAndReturnCategories(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        List<Category> categories = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Categories)) {
            CategoriesResponse categoriesResponse = (CategoriesResponse)restResponse;
            categories = categoriesResponse.getCategories();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return categories;
    }
    public static SubjectAreaDefinition detectAndReturnSubjectAreaDefinition(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        SubjectAreaDefinition subjectAreaDefinition = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.SubjectAreaDefinition)) {
            SubjectAreaDefinitionResponse subjectAreaDefinitionResponse = (SubjectAreaDefinitionResponse)restResponse;
            subjectAreaDefinition = subjectAreaDefinitionResponse.getSubjectAreaDefinition();
        } else {
           CategoryErrorResponse(methodName, restResponse);
        }
        return subjectAreaDefinition;
    }

    private static void CategoryErrorResponse(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR;
        String unexpectedResponseCategory = ResponseCategory.Unknown.name();
        if (restResponse!=null) {
            unexpectedResponseCategory = restResponse.getResponseCategory().name();
        }
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(
                unexpectedResponseCategory
        );
        throw new UnexpectedResponseException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                unexpectedResponseCategory
        );
    }

    public static Hasa detectAndReturnTermHASARelationship(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        Hasa termHASARelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermHASARelationship)) {
            TermHASARelationshipResponse termHASARelationshipResponse = (TermHASARelationshipResponse)restResponse;
            termHASARelationship = termHASARelationshipResponse.getTermHASARelationship();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return termHASARelationship;
    }

    public static RelatedTerm detectAndReturnRelatedTerm(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        RelatedTerm relatedTermRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.RelatedTerm)) {
            RelatedTermResponse relatedTermResponse = (RelatedTermResponse)restResponse;
            relatedTermRelationship = relatedTermResponse.getRelatedTerm();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return relatedTermRelationship;
    }

    public static Synonym detectAndReturnSynonym(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        Synonym synonym = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.SynonymRelationship)) {
            SynonymRelationshipResponse synonymResponse = (SynonymRelationshipResponse)restResponse;
            synonym = synonymResponse.getSynonym();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return synonym;
    }

    public static Antonym detectAndReturnAntonym(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        Antonym antonym = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.AntonymRelationship)) {
            AntonymRelationshipResponse antonymResponse = (AntonymRelationshipResponse)restResponse;
            antonym = antonymResponse.getAntonym();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return antonym;
    }
    public static Translation detectAndReturnTranslation(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        Translation translation = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TranslationRelationship)) {
            TranslationRelationshipResponse translationResponse = (TranslationRelationshipResponse)restResponse;
            translation = translationResponse.getTranslation();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return translation;
    }
    public static UsedInContext detectAndReturnUsedInContext(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        UsedInContext usedInContext = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermUsedInContextRelationship)) {
            UsedInContextRelationshipResponse usedInContextResponse = (UsedInContextRelationshipResponse)restResponse;
            usedInContext = usedInContextResponse.getUsedInContext();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return usedInContext;
    }
    public static PreferredTerm detectAndReturnPreferredTerm(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        PreferredTerm preferredTerm = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.PreferredTermRelationship)) {
            PreferredTermRelationshipResponse preferredTermResponse = (PreferredTermRelationshipResponse)restResponse;
            preferredTerm = preferredTermResponse.getPreferredTerm();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return preferredTerm;
    }
    public static ValidValue detectAndReturnValidValue(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        ValidValue validValue = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.ValidValueRelationship)) {
            ValidValueRelationshipResponse validValueResponse = (ValidValueRelationshipResponse)restResponse;
            validValue = validValueResponse.getValidValue();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return validValue;
    }
    public static ReplacementTerm detectAndReturnReplacementTerm(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        ReplacementTerm replacementTerm = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermReplacementRelationship)) {
            ReplacementRelationshipResponse replacementTermResponse = (ReplacementRelationshipResponse) restResponse;
            replacementTerm = replacementTermResponse.getTermReplacementRelationship();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return replacementTerm;
    }
    public static TypedBy detectAndReturnTermTYPEDBYRelationship(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        TypedBy termTYPEDBYRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermTYPEDBYRelationship)) {
            TermTYPEDBYRelationshipResponse termTYPEDBYRelationshipResponse = (TermTYPEDBYRelationshipResponse) restResponse;
            termTYPEDBYRelationship = termTYPEDBYRelationshipResponse.getTermTYPEDBYRelationship();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return termTYPEDBYRelationship;
    }
    public static Isa detectAndReturnISARelationship(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        Isa isa = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermISARelationship)) {
            TermISARelationshipResponse isaResponse = (TermISARelationshipResponse)restResponse;
            isa = isaResponse.getTermISARelationship();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return isa;
    }
    public static IsaTypeOf detectAndReturnTermISATypeOFRelationship(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        IsaTypeOf termISATypeOFRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermISATYPEOFRelationship)) {
            TermISATYPEOFRelationshipResponse TermISATypeOFRelationshipResponse = (TermISATYPEOFRelationshipResponse)restResponse;
            termISATypeOFRelationship = TermISATypeOFRelationshipResponse.getTermISATYPEOFRelationship();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return termISATypeOFRelationship;
    }

    public static SemanticAssignment detectAndReturnSemanticAssignmentRelationship(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        SemanticAssignment semanticAssignment = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.SemanticAssignmentRelationship)) {
           SemanticAssignementRelationshipResponse relationshipResponse = ( SemanticAssignementRelationshipResponse)restResponse;
           semanticAssignment= relationshipResponse.getSemanticAssignment();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return semanticAssignment;
    }

    public static Categorization detectAndReturnTermCategorizationRelationship(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Categorization termCategorizationRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermCategorizationRelationship)) {
            TermCategorizationRelationshipResponse relationshipResponse = (TermCategorizationRelationshipResponse) restResponse;
            termCategorizationRelationship= relationshipResponse.getTermCategorization();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return termCategorizationRelationship;
    }

    public static TermAnchor detectAndReturnTermAnchorRelationship(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        TermAnchor termAnchorRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermAnchorRelationship)) {
            TermAnchorRelationshipResponse relationshipResponse = (TermAnchorRelationshipResponse) restResponse;
            termAnchorRelationship= relationshipResponse.getTermAnchorRelationship();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return termAnchorRelationship;
    }

    public static CategoryAnchor detectAndReturnCategoryAnchorRelationship(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        CategoryAnchor categoryAnchorRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.CategoryAnchorRelationship)) {
            CategoryAnchorRelationshipResponse relationshipResponse = (CategoryAnchorRelationshipResponse) restResponse;
            categoryAnchorRelationship= relationshipResponse.getCategoryAnchorRelationship();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return categoryAnchorRelationship;
    }
    public static ProjectScope detectAndReturnProjectScope(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        ProjectScope projectScopeRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.ProjectScopeRelationship)) {
            ProjectScopeRelationshipResponse relationshipResponse = (ProjectScopeRelationshipResponse) restResponse;
            projectScopeRelationship= relationshipResponse.getProjectScope();
        } else {
            CategoryErrorResponse(methodName, restResponse);
        }
        return projectScopeRelationship;
    }

    /**
     * Convert a subject area a checked exception to a response
     * @param e Exception to comnvert
     * @return the relevant exception response
     */
    static public SubjectAreaOMASAPIResponse getResponseFromException(SubjectAreaCheckedExceptionBase e) {
        SubjectAreaOMASAPIResponse response =null;
        if (e instanceof MetadataServerUncontactableException) {
            response = new MetadataServerUncontactableExceptionResponse(e);
        } else if (e instanceof InvalidParameterException) {
            response = new InvalidParameterExceptionResponse(e);
        } else if (e instanceof UserNotAuthorizedException) {
            response = new UserNotAuthorizedExceptionResponse(e);
        } else if (e instanceof UnrecognizedGUIDException) {
            response = new UnrecognizedGUIDExceptionResponse(e);
        } else if (e instanceof ClassificationException) {
            response = new ClassificationExceptionResponse(e);
        } else if (e instanceof FunctionNotSupportedException) {
            response = new FunctionNotSupportedExceptionResponse(e);
        } else if (e instanceof UnexpectedResponseException) {
            response = new UnexpectedExceptionResponse(e);
        } else {
            // TODO catch all error
        }
        return response;
    }
}