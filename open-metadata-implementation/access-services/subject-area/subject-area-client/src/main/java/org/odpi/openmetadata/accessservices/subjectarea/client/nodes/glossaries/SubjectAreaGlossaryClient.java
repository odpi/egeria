/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.nodes.glossaries;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.AbstractSubjectAreaNode;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryBuilder;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SubjectAreaNodeClient
public class SubjectAreaGlossaryClient<G extends Glossary> extends AbstractSubjectAreaNode<G> {
    public SubjectAreaGlossaryClient(SubjectAreaRestClient client)
    {
        super(client, SUBJECT_AREA_BASE_URL + "glossaries");
    }

    /**
     * Get the Categories owned by this glossary.
     *
     * @param userId      unique identifier for requesting user, under which the request is performed.
     * @param guid        unique identifier of the object to which the found objects should relate.
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     * @param onlyTop     when only the top categories (those categories without parents) are returned.
     * @return list of Categories
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */

    public List<Category> getCategories(String userId, String guid, FindRequest findRequest, Boolean onlyTop) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
       return getCategories(userId, guid, findRequest, onlyTop, null);
    }

    /**
     * Get the Categories owned by this glossary.
     *
     * @param userId      unique identifier for requesting user, under which the request is performed.
     * @param guid        unique identifier of the object to which the found objects should relate.
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     * @param onlyTop     when only the top categories (those categories without parents) are returned.
     * @param maximumPageSizeOnRestCall maximum page size that can be specified on a rest call
     * @return list of Categories
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */

    public List<Category> getCategories(String userId, String guid, FindRequest findRequest, Boolean onlyTop, Integer maximumPageSizeOnRestCall) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String urnTemplate = BASE_URL + "/%s/categories";
        final String methodInfo = getMethodInfo("getCategories");
        Map<String, String> params = new HashMap<>();
        params.put("onlyTop", onlyTop+"");
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());
        GenericResponse<Category> response = client.getByIdRESTCall(userId ,guid, methodInfo, type, urnTemplate, findRequest, maximumPageSizeOnRestCall, params);
        return response.results();
    }

    /**
     * Get the Terms owned by this glossary.
     *
     * @param userId unique identifier for requesting user, under which the request is performed.
     * @param guid  unique identifier of the object to which the found objects should relate.
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     * @return list of Terms
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public List<Term> getTerms(String userId, String guid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
       return getTerms(userId, guid, findRequest, null);
    }

    /**
     * Get the Terms owned by this glossary.
     *
     * @param userId unique identifier for requesting user, under which the request is performed.
     * @param guid  unique identifier of the object to which the found objects should relate.
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     * @param maximumPageSizeOnRestCall maximum page size on rest call.
     * @return list of Terms
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public List<Term> getTerms(String userId, String guid, FindRequest findRequest, Integer maximumPageSizeOnRestCall) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodInfo = getMethodInfo("getTerms");
        final String urlTemplate = BASE_URL + "/%s/terms";
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Term.class);
        ParameterizedTypeReference<GenericResponse<Term>> type = ParameterizedTypeReference.forType(resolvableType.getType());
        GenericResponse<Term> response = client.getByIdRESTCall(userId, guid, methodInfo, type, urlTemplate, findRequest, maximumPageSizeOnRestCall, null);
        return response.results();
    }
}
