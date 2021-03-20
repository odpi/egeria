/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.services;

import org.odpi.openmetadata.accessservices.subjectarea.client.configs.SubjectAreaConfigClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClients;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.glossaries.SubjectAreaGlossaryClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.viewservices.glossaryauthor.handlers.BreadCrumbHandler;
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.BreadCrumb;

import java.util.Date;
import java.util.List;

/**
 * The GlossaryAuthorViewGlossaryRESTServices provides the org.odpi.openmetadata.viewservices.glossaryauthor.services implementation of the GlossaryAuthor Open Metadata
 * View Service (OMVS). This interface provides view glossary authoring interfaces for subject area experts.
 */

public class GlossaryAuthorViewGlossaryRESTServices extends BaseGlossaryAuthorView {
    private static String className = GlossaryAuthorViewGlossaryRESTServices.class.getName();

    /**
     * Default constructor
     */
    public GlossaryAuthorViewGlossaryRESTServices() {

    }

    /**
     * Create a Glossary. There are specializations of glossaries that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Glossary in the supplied glossary.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     * <li>Taxonomy to create a Taxonomy </li>
     * <li>CanonicalGlossary to create a canonical glossary </li>
     * <li>TaxonomyAndCanonicalGlossary to create a glossary that is both a taxonomy and a canonical glosary </li>
     * <li>Glossary to create a glossary that is not a taxonomy or a canonical glossary</li>
     * </ul>
     *
     * @param serverName       name of the local UI server.
     * @param userId           user identifier
     * @param suppliedGlossary Glossary to create
     * @return response, when successful contains the created glossary.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Glossary> createGlossary(String serverName, String userId, Glossary suppliedGlossary) {
        final String methodName = "createGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied glossary - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Glossary createdGlossary = clients.glossaries().create(userId, suppliedGlossary);
            response.addResult(createdGlossary);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Get a glossary.
     *
     * @param serverName name of the local UI server.
     * @param userId     user identifier
     * @param guid       guid of the glossary to get
     * @return response which when successful contains the glossary with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Glossary> getGlossary(String serverName, String userId, String guid) {
        final String methodName = "getGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Glossary obtainedGlossary = clients.glossaries().getByGUID(userId, guid);
            response.addResult(obtainedGlossary);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Find Glossary
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param searchCriteria     String expression matching Glossary property values .
     * @param asOfTime           the glossaries returned as they were at this time. null indicates at the current time.
     * @param startingFrom             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of glossaries meeting the search Criteria
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Glossary> findGlossary(
            String serverName,
            String userId,
            Date asOfTime,
            String searchCriteria,
            Integer startingFrom,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty
                                                            ) {
        final String methodName = "findGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            if (startingFrom == null) {
                startingFrom = 0;
            }
            if (pageSize == null) {
                pageSize = invalidParameterHandler.getMaxPagingSize();
            }
            if (sequencingOrder == null) {
                sequencingOrder =SequencingOrder.ANY;
            }
            invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            FindRequest findRequest = getFindRequest(searchCriteria, asOfTime, startingFrom, pageSize, sequencingOrder.name(), sequencingProperty, invalidParameterHandler.getMaxPagingSize());
            SubjectAreaConfigClient client = instanceHandler.getSubjectAreaConfigClient(serverName, userId, methodName);
            Config subjectAreaConfig = client.getConfig(userId);
            List<Glossary> glossaries = clients.glossaries().find(userId, findRequest, subjectAreaConfig.getMaxPageSize());
            response.addAllResults(glossaries);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Get Glossary relationships
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid               guid of the glossary to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param startingFrom          the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the glossary relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Relationship> getGlossaryRelationships(
            String serverName,
            String userId,
            String guid,
            Date asOfTime,
            Integer startingFrom,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty


                                                                            ) {
        final String methodName = "getGlossaryRelationships";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Relationship> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            if (pageSize == null) {
                pageSize = invalidParameterHandler.getMaxPagingSize();
            }
            invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            FindRequest findRequest = new FindRequest();
            findRequest.setAsOfTime(asOfTime);
            findRequest.setStartingFrom(startingFrom);
            findRequest.setPageSize(pageSize);
            findRequest.setSequencingOrder(sequencingOrder);
            findRequest.setSequencingProperty(sequencingProperty);

            List<Relationship> relationships = clients.glossaries().getRelationships(userId, guid, findRequest);
            response.addAllResults(relationships);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a Glossary
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param serverName name of the local UI server.
     * @param userId     user identifier
     * @param guid       guid of the glossary to update
     * @param glossary   glossary to update
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Glossary> updateGlossary(
            String serverName,
            String userId,
            String guid,
            Glossary glossary,
            boolean isReplace
                                                              ) {
        final String methodName = "updateGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied glossary - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Glossary updatedGlossary;
            if (isReplace) {
                updatedGlossary = clients.glossaries().replace(userId, guid, glossary);
            } else {
                updatedGlossary = clients.glossaries().update(userId, guid, glossary);
            }
            response.addResult(updatedGlossary);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a Glossary instance
     * <p>
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the glossary will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param serverName name of the local UI server.
     * @param userId     user identifier
     * @param guid       guid of the glossary to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Glossary> deleteGlossary(
            String serverName,
            String userId,
            String guid,
            boolean isPurge
                                                              ) {

        final String methodName = "deleteGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied glossary - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);

            if (isPurge) {
                clients.glossaries().purge(userId, guid);
            } else {
                clients.glossaries().delete(userId, guid);
            }
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a Glossary
     * <p>
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName name of the local UI server.
     * @param userId     user identifier
     * @param guid       guid of the glossary to restore
     * @return response which when successful contains the restored glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Glossary> restoreGlossary(
            String serverName,
            String userId,
            String guid) {
        final String methodName = "restoreGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied glossary - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Glossary glossary = clients.glossaries().restore(userId, guid);
            response.addResult(glossary);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
    /**
     * Create the supplied list of Terms in the glossary, identified by the supplied guid. Each term does not need to specify a glossary.
     *
     * @param serverName       local UI server name
     * @param userId           user identifier
     * @param guid             guid of the glossary under which the Terms will be created
     * @param terms            terms to create
     * @return a response which when successful contains a list of the responses from the Term creates (successful or otherwise). The order of the responses is the same as the supplied terms order.
     *
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<SubjectAreaOMASAPIResponse<Term>> createMultipleTermsInAGlossary(String serverName, String userId, String guid, Term[] terms) {
        final String methodName = "createMultipleTermsInAGlossary";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SubjectAreaOMASAPIResponse<SubjectAreaOMASAPIResponse<Term>> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            for (Term term : terms) {
                GlossarySummary glossarySummary = new GlossarySummary();
                glossarySummary.setGuid(guid);
                term.setGlossary(glossarySummary);
                SubjectAreaOMASAPIResponse<Term> termResponse = new SubjectAreaOMASAPIResponse<Term>();
                try {
                    Term createdTerm = clients.terms().create(userId, term);
                    termResponse.addResult(createdTerm);
                } catch (Exception exception) {
                    termResponse = getResponseForException(exception, auditLog, className, methodName);
                }
                response.addResult(termResponse);
            }

        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Get BreadCrumbTrail.
     *
     * The user interface experience can start with a particular Glossary, navigate to a child Category, then to another child category then to a categories term.
     * When such a user interface navigation occurs, it is helpful for the user to be displayed a 'breadcrumb' trail of where they have been, showing how nested they are in
     * the glossary artifacts. The Get BreadcrumbTrail API returns information that allows the user interface to easy construct a trail of breadcrumbs.
     * Each breadcrumb contains
     * <ul>
     *  <li> a displayName that is intended to be shown to the user</li>
     *  <li> a guid that uniquely identifies a breadcrumb but is not intended to be shown to the user</li>
     *  <li> a types, allowing the user interface to display an appropriate icon.
     *  <li> a path that can be used to determine all the elements in the breadcrumb </li>
     * </ul>
     * @param serverName       local UI server name
     * @param userId           user identifier
     * @param guid             Glossary guid.
     * @param rootCategoryGuid Root Category guid
     * @param leafCategoryGuid Leaf Category Guid
     * @param termGuid         Term Guid.
     * @return a response which when successful contains a list of breadcrumbs corresponding to the supplied guids.
     *
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<BreadCrumb> getBreadCrumbTrail(
            String serverName,
            String userId,
            String guid,
            String rootCategoryGuid,
            String leafCategoryGuid,
            String termGuid) {
        final String methodName = "getBreadCrumbTrail";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<BreadCrumb> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            BreadCrumbHandler breadCrumbHandler = new BreadCrumbHandler(clients, userId);
            List<BreadCrumb> breadCrumbs = breadCrumbHandler.getBreadCrumbTrail(guid, rootCategoryGuid, leafCategoryGuid, termGuid);
            response.addAllResults(breadCrumbs);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Get the Categories owned by this glossary.
     *
     * @param serverName   serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       unique identifier for requesting user, under which the request is performed
     * @param guid         guid of the category to get terms
     * @param searchCriteria String expression matching child Term property values.
     * @param asOfTime     the categories returned as they were at this time. null indicates at the current time.
     * @param onlyTop      when only the top categories (those categories without parents) are returned.
     * @param startingFrom the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize     the maximum number of elements that can be returned on this request.
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of categories owned by the glossary
     *
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     * */
    public SubjectAreaOMASAPIResponse<Category> getCategories(String serverName, String userId, String guid, String searchCriteria, Date asOfTime, Boolean onlyTop, Integer startingFrom, Integer pageSize, String sequencingOrder, String sequencingProperty) {


        final String methodName = "getCategories";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        FindRequest findRequest = getFindRequest(searchCriteria, asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty, invalidParameterHandler.getMaxPagingSize());

        try {
            invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);
            findRequest.setPageSize(pageSize);
            findRequest.setStartingFrom(startingFrom);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaConfigClient client = instanceHandler.getSubjectAreaConfigClient(serverName, userId, methodName);
            Config subjectAreaConfig = client.getConfig(userId);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            List<Category> categories = ((SubjectAreaGlossaryClient)clients.glossaries()).getCategories(userId, guid, findRequest, onlyTop, subjectAreaConfig.getMaxPageSize());
            response.addAllResults(categories);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
    /**
     * Get terms that are owned by this glossary
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to get terms
     * @param searchCriteria String expression matching child Term property values.
     * @param asOfTime   the terms returned as they were at this time. null indicates at the current time.
     * @param startingFrom the starting element number for this set of results.  This is used when retrieving elements
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @param pageSize     the maximum number of elements that can be returned on this request.
     *
     * @return A list of terms owned by the glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     * */
    public SubjectAreaOMASAPIResponse<Term> getTerms(String serverName, String userId, String guid, String searchCriteria, Date asOfTime, Integer startingFrom, Integer pageSize, String sequencingOrder,String sequencingProperty) {


        final String methodName = "getTerms";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        FindRequest findRequest = getFindRequest(searchCriteria, asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty, invalidParameterHandler.getMaxPagingSize());
        try {
            invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);
            findRequest.setPageSize(pageSize);
            findRequest.setStartingFrom(startingFrom);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaConfigClient client = instanceHandler.getSubjectAreaConfigClient(serverName, userId, methodName);
            Config subjectAreaConfig = client.getConfig(userId);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            List<Term> terms = ((SubjectAreaGlossaryClient)clients.glossaries()).getTerms(userId, guid, findRequest, subjectAreaConfig.getMaxPageSize());
            response.addAllResults(terms);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

}
