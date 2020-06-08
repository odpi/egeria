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
    //private static String className = "DetectUtils";

    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws InvalidParameterException - encoded exception from the handlers
     */
    public static void detectAndThrowInvalidParameterException(SubjectAreaOMASAPIResponse restResponse) throws InvalidParameterException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.InvalidParameterException)) {

            InvalidParameterExceptionResponse response = (InvalidParameterExceptionResponse) restResponse;
            SubjectAreaErrorCode subjectAreaErrorCode = SubjectAreaErrorCode.valueOf(response.getMessageId());

            throw new InvalidParameterException(subjectAreaErrorCode.getMessageDefinition(),
                                                response.getExceptionClassName(),
                                                response.getActionDescription(),
                                                response.getInvalidPropertyName(),
                                                response.getInvalidPropertyValue());
        }
    }

    /**
     * Throw an UnrecognizedGUIDException if it is encoded in the REST response.
     *
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws UnrecognizedGUIDException - encoded exception from the handlers
     */
    public static void detectAndThrowUnrecognizedGUIDException(SubjectAreaOMASAPIResponse restResponse) throws UnrecognizedGUIDException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.UnrecognizedGUIDException)) {

            UnrecognizedGUIDExceptionResponse response = (UnrecognizedGUIDExceptionResponse) restResponse;

            SubjectAreaErrorCode subjectAreaErrorCode = SubjectAreaErrorCode.valueOf(response.getMessageId());

            throw new UnrecognizedGUIDException(subjectAreaErrorCode.getMessageDefinition(),
                                                response.getExceptionClassName(),
                                                response.getActionDescription(),
                                                response.getGuid());

        }
    }

    /**
     * Throw an ClassificationException if it is encoded in the REST response.
     *
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws ClassificationException - encoded exception from the handlers
     */
    public static void detectAndThrowClassificationException(SubjectAreaOMASAPIResponse restResponse) throws ClassificationException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.ClassificationException)) {

            ClassificationExceptionResponse response = (ClassificationExceptionResponse) restResponse;

            SubjectAreaErrorCode subjectAreaErrorCode = SubjectAreaErrorCode.valueOf(response.getMessageId());

            throw new ClassificationException(subjectAreaErrorCode.getMessageDefinition(),
                                              response.getExceptionClassName(),
                                              response.getActionDescription()
            );
        }
    }

    /**
     * Throw an EntityNotDeletedException if it is encoded in the REST response.
     *
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws EntityNotDeletedException - encoded exception from the handlers
     */
    public static void detectAndThrowEntityNotDeletedException(SubjectAreaOMASAPIResponse restResponse) throws EntityNotDeletedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.EntityNotDeletedException)) {

            EntityNotDeletedExceptionResponse response = (EntityNotDeletedExceptionResponse) restResponse;

            SubjectAreaErrorCode subjectAreaErrorCode = SubjectAreaErrorCode.valueOf(response.getMessageId());

            throw new EntityNotDeletedException(subjectAreaErrorCode.getMessageDefinition(),
                                                response.getExceptionClassName(),
                                                response.getActionDescription(),
                                                response.getGuid());

        }
    }

    /**
     * Throw an FunctionNotSupportedException if it is encoded in the REST response.
     *
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws FunctionNotSupportedException - encoded exception from the handlers
     */
    public static void detectAndThrowFunctionNotSupportedException(SubjectAreaOMASAPIResponse restResponse) throws FunctionNotSupportedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.FunctionNotSupportedException)) {

            FunctionNotSupportedExceptionResponse response = (FunctionNotSupportedExceptionResponse) restResponse;
            SubjectAreaErrorCode subjectAreaErrorCode = SubjectAreaErrorCode.valueOf(response.getMessageId());

            throw new FunctionNotSupportedException(subjectAreaErrorCode.getMessageDefinition(),
                                                    response.getExceptionClassName(),
                                                    response.getActionDescription());
        }
    }

    /**
     * Throw an EntityNotPurgedException if it is encoded in the REST response.
     *
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws EntityNotPurgedException - encoded exception from the handlers
     */
    public static void detectAndThrowEntityNotPurgedException(SubjectAreaOMASAPIResponse restResponse) throws EntityNotPurgedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.EntityNotPurgedException)) {

            EntityNotPurgedExceptionResponse response = (EntityNotPurgedExceptionResponse) restResponse;
            SubjectAreaErrorCode subjectAreaErrorCode = SubjectAreaErrorCode.valueOf(response.getMessageId());

            throw new EntityNotPurgedException(subjectAreaErrorCode.getMessageDefinition(),
                                               response.getExceptionClassName(),
                                               response.getActionDescription(),
                                               response.getGuid());

        }
    }

    /**
     * Throw an RelationshipNotDeletedException if it is encoded in the REST response.
     *
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws RelationshipNotDeletedException - encoded exception from the handlers
     */
    public static void detectAndThrowRelationshipNotDeletedException(SubjectAreaOMASAPIResponse restResponse) throws RelationshipNotDeletedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.RelationshipNotDeletedException)) {

            RelationshipNotDeletedExceptionResponse response = (RelationshipNotDeletedExceptionResponse) restResponse;
            SubjectAreaErrorCode subjectAreaErrorCode = SubjectAreaErrorCode.valueOf(response.getMessageId());

            throw new RelationshipNotDeletedException(subjectAreaErrorCode.getMessageDefinition(),
                                                      response.getExceptionClassName(),
                                                      response.getActionDescription(),
                                                      response.getGuid());
        }
    }

    /**
     * Throw an RelationshipNotPurgedException if it is encoded in the REST response.
     *
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws RelationshipNotPurgedException - encoded exception from the handlers
     */
    public static void detectAndThrowRelationshipNotPurgedException(SubjectAreaOMASAPIResponse restResponse) throws RelationshipNotPurgedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.RelationshipNotPurgedException)) {

            RelationshipNotPurgedExceptionResponse response = (RelationshipNotPurgedExceptionResponse) restResponse;
            SubjectAreaErrorCode subjectAreaErrorCode = SubjectAreaErrorCode.valueOf(response.getMessageId());

            throw new RelationshipNotPurgedException(subjectAreaErrorCode.getMessageDefinition(),
                                               response.getExceptionClassName(),
                                               response.getActionDescription(),
                                               response.getGuid());

        }
    }

    /**
     * Throw an StatusNotSupportedException if it is encoded in the REST response.
     *
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws StatusNotSupportedException - encoded exception from the handlers
     */
    public static void detectAndThrowStatusNotSupportedException(SubjectAreaOMASAPIResponse restResponse) throws StatusNotSupportedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.StatusNotSupportedException)) {

            StatusNotsupportedExceptionResponse response = (StatusNotsupportedExceptionResponse) restResponse;
            SubjectAreaErrorCode subjectAreaErrorCode = SubjectAreaErrorCode.valueOf(response.getMessageId());
            throw new StatusNotSupportedException(subjectAreaErrorCode.getMessageDefinition(),
                                                  response.getExceptionClassName(),
                                                  response.getActionDescription());
        }
    }

    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws UserNotAuthorizedException - encoded exception from the handlers
     */
    public static void detectAndThrowUserNotAuthorizedException(SubjectAreaOMASAPIResponse restResponse) throws UserNotAuthorizedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.UserNotAuthorizedException)) {

            UserNotAuthorizedExceptionResponse response = (UserNotAuthorizedExceptionResponse) restResponse;

            SubjectAreaErrorCode subjectAreaErrorCode = SubjectAreaErrorCode.valueOf(response.getMessageId());
            throw new UserNotAuthorizedException(subjectAreaErrorCode.getMessageDefinition(),
                                                 response.getExceptionClassName(),
                                                 response.getActionDescription(),
                                                 response.getUserId());

        }
    }

    /**
     * Detect Void return. If we do not find one then throw an Exception
     *
     * @param className name of the class.
     * @param methodName method being performed.
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws UnexpectedResponseException - if the response is not a glossary then throw this exception
     */
    public static void detectVoid(
            String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Void)) {
            // expected void response
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
    }

    /**
     * Detect and return a Glossary object from the supplied response. If we do not find one then throw an Exception
     *
     * @param className name of the class.
     * @param methodName method being performed.
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return Glossary if the supplied response is a glossary response
     * @throws UnexpectedResponseException - if the response is not a glossary then throw this exception
     */
    public static Glossary detectAndReturnGlossary(
            String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Glossary glossary = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Glossary)) {
            GlossaryResponse glossaryResponse = (GlossaryResponse) restResponse;
            glossary = glossaryResponse.getGlossary();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return glossary;
    }

    public static List<Glossary> detectAndReturnGlossaries(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        List<Glossary> glossaries = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Glossaries)) {
            GlossariesResponse glossariesResponse = (GlossariesResponse) restResponse;
            glossaries = glossariesResponse.getGlossaries();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return glossaries;
    }

    /**
     * Detect and return a Project object from the supplied response. If we do not find one then throw an Exception
     *
     * @param className name of the class.
     * @param methodName method being performed.
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return Project if the supplied response is a project response
     * @throws UnexpectedResponseException - if the response is not a project then throw this exception
     */
    public static Project detectAndReturnProject(
            String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Project project = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Project)) {
            ProjectResponse projectResponse = (ProjectResponse) restResponse;
            project = projectResponse.getProject();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return project;
    }

    /**
     * Detect and return a List of Project objects from the supplied response. If we do not find one then throw an Exception
     *
     * @param className name of the class.
     * @param methodName method being performed.
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return Projects if the supplied response is a project response
     * @throws UnexpectedResponseException - if the response is not a project then throw this exception
     */
    public static List<Project> detectAndReturnProjects(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        List<Project> projects = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Projects)) {
            ProjectsResponse projectsResponse = (ProjectsResponse) restResponse;
            projects = projectsResponse.getProjects();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return projects;
    }

    /**
     * Detect and return a Term object from the supplied response. If we do not find one then throw an Exception
     *
     * @param className name of the class.
     * @param methodName method being performed.
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return Term if the supplied response is a term response
     * @throws UnexpectedResponseException - if the response is not a Term then throw this exception
     */
    public static Term detectAndReturnTerm(
            String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Term term = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Term)) {
            TermResponse termResponse = (TermResponse) restResponse;
            term = termResponse.getTerm();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return term;
    }

    /**
     * Detect and return a List of Terms from the supplied response. If we do not find one then throw an Exception
     *
     * @param className name of the class.
     * @param methodName method being performed.
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return List of Terms if the supplied response is a terms response
     * @throws UnexpectedResponseException - if the response is not a Terms then throw this exception
     */
    public static List<Term> detectAndReturnTerms(
            String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        List<Term> terms = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Terms)) {
            TermsResponse termsResponse = (TermsResponse) restResponse;
            terms = termsResponse.getTerms();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return terms;
    }

    public static Graph detectAndReturnGraph(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Graph graph = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Graph)) {
            GraphResponse GraphResponse = (GraphResponse) restResponse;
            graph = GraphResponse.getGraph();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
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
    public static List<Line> detectAndReturnLines(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        List<Line> lines = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Lines)) {
            LinesResponse linesResponse = (LinesResponse) restResponse;
            lines = linesResponse.getLines();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return lines;

    }

    /**
     * Detect and return a Category object from the supplied response. If we do not find one then throw an Exception
     *
     * @param className name of the class.
     * @param methodName method being performed.
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return Category if the supplied response is a category response
     * @throws UnexpectedResponseException - if the response is not a Category then throw this exception
     */
    public static Category detectAndReturnCategory(
            String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Category category = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Category)) {
            CategoryResponse categoryResponse = (CategoryResponse) restResponse;
            category = categoryResponse.getCategory();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return category;
    }

    public static List<Category> detectAndReturnCategories(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        List<Category> categories = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Categories)) {
            CategoriesResponse categoriesResponse = (CategoriesResponse) restResponse;
            categories = categoriesResponse.getCategories();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return categories;
    }

    public static SubjectAreaDefinition detectAndReturnSubjectAreaDefinition(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        SubjectAreaDefinition subjectAreaDefinition = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.SubjectAreaDefinition)) {
            SubjectAreaDefinitionResponse subjectAreaDefinitionResponse = (SubjectAreaDefinitionResponse) restResponse;
            subjectAreaDefinition = subjectAreaDefinitionResponse.getSubjectAreaDefinition();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return subjectAreaDefinition;
    }

    /**
     * We were got an unexpected response
     *
     * @param className class issuing the rest call
     * @param actionDescription description of the action
     * @param restResponse      rest response
     * @throws UnexpectedResponseException UnexpectedResponse exception
     */
    private static void CategoryErrorResponse( String className, String actionDescription, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR;
        String unexpectedResponseCategory = ResponseCategory.Unknown.name();
        if (restResponse != null) {
            unexpectedResponseCategory = restResponse.getResponseCategory().name();
        }

        throw new UnexpectedResponseException(errorCode.getMessageDefinition(),
                                              className,
                                              actionDescription,
                                              unexpectedResponseCategory);
    }

    public static Hasa detectAndReturnTermHASARelationship(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Hasa termHASARelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermHASARelationship)) {
            TermHASARelationshipResponse termHASARelationshipResponse = (TermHASARelationshipResponse) restResponse;
            termHASARelationship = termHASARelationshipResponse.getTermHASARelationship();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return termHASARelationship;
    }

    public static RelatedTerm detectAndReturnRelatedTerm(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        RelatedTerm relatedTermRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.RelatedTerm)) {
            RelatedTermResponse relatedTermResponse = (RelatedTermResponse) restResponse;
            relatedTermRelationship = relatedTermResponse.getRelatedTerm();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return relatedTermRelationship;
    }

    public static Synonym detectAndReturnSynonym(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Synonym synonym = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.SynonymRelationship)) {
            SynonymRelationshipResponse synonymResponse = (SynonymRelationshipResponse) restResponse;
            synonym = synonymResponse.getSynonym();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return synonym;
    }

    public static Antonym detectAndReturnAntonym(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Antonym antonym = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.AntonymRelationship)) {
            AntonymRelationshipResponse antonymResponse = (AntonymRelationshipResponse) restResponse;
            antonym = antonymResponse.getAntonym();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return antonym;
    }

    public static Translation detectAndReturnTranslation(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Translation translation = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TranslationRelationship)) {
            TranslationRelationshipResponse translationResponse = (TranslationRelationshipResponse) restResponse;
            translation = translationResponse.getTranslation();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return translation;
    }

    public static UsedInContext detectAndReturnUsedInContext(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        UsedInContext usedInContext = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermUsedInContextRelationship)) {
            UsedInContextRelationshipResponse usedInContextResponse = (UsedInContextRelationshipResponse) restResponse;
            usedInContext = usedInContextResponse.getUsedInContext();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return usedInContext;
    }

    public static PreferredTerm detectAndReturnPreferredTerm(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        PreferredTerm preferredTerm = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.PreferredTermRelationship)) {
            PreferredTermRelationshipResponse preferredTermResponse = (PreferredTermRelationshipResponse) restResponse;
            preferredTerm = preferredTermResponse.getPreferredTerm();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return preferredTerm;
    }

    public static ValidValue detectAndReturnValidValue(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        ValidValue validValue = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.ValidValueRelationship)) {
            ValidValueRelationshipResponse validValueResponse = (ValidValueRelationshipResponse) restResponse;
            validValue = validValueResponse.getValidValue();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return validValue;
    }

    public static ReplacementTerm detectAndReturnReplacementTerm(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        ReplacementTerm replacementTerm = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermReplacementRelationship)) {
            ReplacementRelationshipResponse replacementTermResponse = (ReplacementRelationshipResponse) restResponse;
            replacementTerm = replacementTermResponse.getTermReplacementRelationship();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return replacementTerm;
    }

    public static TypedBy detectAndReturnTermTYPEDBYRelationship(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        TypedBy termTYPEDBYRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermTYPEDBYRelationship)) {
            TermTYPEDBYRelationshipResponse termTYPEDBYRelationshipResponse = (TermTYPEDBYRelationshipResponse) restResponse;
            termTYPEDBYRelationship = termTYPEDBYRelationshipResponse.getTermTYPEDBYRelationship();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return termTYPEDBYRelationship;
    }

    public static Isa detectAndReturnISARelationship(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Isa isa = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermISARelationship)) {
            TermISARelationshipResponse isaResponse = (TermISARelationshipResponse) restResponse;
            isa = isaResponse.getTermISARelationship();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return isa;
    }

    public static IsaTypeOf detectAndReturnTermISATypeOFRelationship(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        IsaTypeOf termISATypeOFRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermISATYPEOFRelationship)) {
            TermISATYPEOFRelationshipResponse TermISATypeOFRelationshipResponse = (TermISATYPEOFRelationshipResponse) restResponse;
            termISATypeOFRelationship = TermISATypeOFRelationshipResponse.getTermISATYPEOFRelationship();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return termISATypeOFRelationship;
    }

    public static SemanticAssignment detectAndReturnSemanticAssignmentRelationship(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        SemanticAssignment semanticAssignment = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.SemanticAssignmentRelationship)) {
            SemanticAssignementRelationshipResponse relationshipResponse = (SemanticAssignementRelationshipResponse) restResponse;
            semanticAssignment = relationshipResponse.getSemanticAssignment();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return semanticAssignment;
    }

    public static Categorization detectAndReturnTermCategorizationRelationship(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Categorization termCategorizationRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermCategorizationRelationship)) {
            TermCategorizationRelationshipResponse relationshipResponse = (TermCategorizationRelationshipResponse) restResponse;
            termCategorizationRelationship = relationshipResponse.getTermCategorization();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return termCategorizationRelationship;
    }

    public static TermAnchor detectAndReturnTermAnchorRelationship(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        TermAnchor termAnchorRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermAnchorRelationship)) {
            TermAnchorRelationshipResponse relationshipResponse = (TermAnchorRelationshipResponse) restResponse;
            termAnchorRelationship = relationshipResponse.getTermAnchorRelationship();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return termAnchorRelationship;
    }

    public static CategoryAnchor detectAndReturnCategoryAnchorRelationship(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        CategoryAnchor categoryAnchorRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.CategoryAnchorRelationship)) {
            CategoryAnchorRelationshipResponse relationshipResponse = (CategoryAnchorRelationshipResponse) restResponse;
            categoryAnchorRelationship = relationshipResponse.getCategoryAnchorRelationship();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return categoryAnchorRelationship;
    }

    public static ProjectScope detectAndReturnProjectScope(String className, String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        ProjectScope projectScopeRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.ProjectScopeRelationship)) {
            ProjectScopeRelationshipResponse relationshipResponse = (ProjectScopeRelationshipResponse) restResponse;
            projectScopeRelationship = relationshipResponse.getProjectScope();
        } else {
            CategoryErrorResponse(className, methodName, restResponse);
        }
        return projectScopeRelationship;
    }

    /**
     * Convert a subject area a checked exception to a response
     *
     * @param e Exception to comnvert
     * @return the relevant exception response
     */
    static public SubjectAreaOMASAPIResponse getResponseFromException(SubjectAreaCheckedException e) {
        SubjectAreaOMASAPIResponse response = null;
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
        } else if (e instanceof EntityNotDeletedException) {
            response = new EntityNotDeletedExceptionResponse(e);
        } else if (e instanceof EntityNotPurgedException) {
            response = new EntityNotPurgedExceptionResponse(e);
        } else if (e instanceof RelationshipNotDeletedException) {
            response = new RelationshipNotDeletedExceptionResponse(e);
        } else if (e instanceof RelationshipNotPurgedException) {
            response = new RelationshipNotPurgedExceptionResponse(e);
        } else {
            // TODO catch all error
        }
        return response;
    }
}