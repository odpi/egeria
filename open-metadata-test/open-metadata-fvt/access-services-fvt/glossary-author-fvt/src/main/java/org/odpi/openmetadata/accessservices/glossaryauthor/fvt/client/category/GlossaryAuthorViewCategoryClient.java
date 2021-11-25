/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryauthor.fvt.client.category;

import org.odpi.openmetadata.accessservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResponseParameterization;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
//import org.odpi.openmetadata.accessservices.glossaryview.rest.Glossary;


import java.util.*;

import static org.odpi.openmetadata.accessservices.glossaryauthor.fvt.FVTConstants.GLOSSARY_AUTHOR_BASE_URL;


public class GlossaryAuthorViewCategoryClient implements GlossaryAuthorViewCategory, ResponseParameterization<Category> {

    protected final GlossaryAuthorViewRestClient client;
    private static final String BASE_URL = GLOSSARY_AUTHOR_BASE_URL + "categories";

    protected String getMethodInfo(String methodName) {
        return methodName + " for " + resultType().getSimpleName();
    }

    public GlossaryAuthorViewCategoryClient(GlossaryAuthorViewRestClient client) {
        this.client = client;
    }


    @Override
    public Category create(String userId, Category category) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        GenericResponse<Category> response = client.postRESTCall(userId, getMethodInfo("create"), BASE_URL, getParameterizedType(), category);

        return response.head().get();
    }

    @Override
    public Category update(String userId, String guid, Category category, boolean isReplace) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        Map<String, String> params = new HashMap<>();
      //  boolean isReplace
        if (isReplace)
            params.put("isReplace", "true");
        else
            params.put("isReplace", "false");

///servers/{serverName}/open-metadata/view-services/glossary-author/users/{userId}/categories")
        GenericResponse<Category> response = client.putRESTCall(userId,
                guid,
                getMethodInfo("create"),
                BASE_URL,
                getParameterizedType(),
                category,
                params);
        return response.head().get();
    }

    @Override
    public void delete(String userId, String guid) throws PropertyServerException {
        String methodName = getMethodInfo("Restore");
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        String urlTemplate = BASE_URL;// + "/%s";

        GenericResponse<Category> response = client.delRESTCall( userId,
                type,methodName,
                urlTemplate,
                guid);

        return;
    }

    @Override
    public Category restore(String userId, String guid) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        String methodName = getMethodInfo("Restore");
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        String urlTemplate = BASE_URL + "/%s";

        GenericResponse<Category> response = client.postRESTCall( userId,
                methodName,
                urlTemplate,
                type,
                guid);

        return response.head().get();
    }

/*    @Override
    public Config getConfig(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return null;
    }*/

    @Override
    public List<Category> getCategoryChildren(String userId, String parentGuid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());
///servers/{serverName}/open-metadata/view-services/glossary-author/users/{userId}/categories/{guid}/categories
     //   String urlTemplate = BASE_URL + "/%s/categories";

        GenericResponse<Category> completeResponse =
                client.findRESTCall(userId,getMethodInfo("getCategoryChildren"),BASE_URL,
                        type, findRequest, false, true, null);

        return completeResponse.results();
    }

    @Override
    public List<Category> getCategoryChildren(String userId, String parentGuid, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());
///servers/{serverName}/open-metadata/view-services/glossary-author/users/{userId}/categories/{guid}/categories
        String urlTemplate = BASE_URL + "/%s/categories";

        GenericResponse<Category> completeResponse =
                client.findRESTCallById(userId,getMethodInfo("getCategoryChildren"),urlTemplate,
                        type, findRequest, false, true, null,parentGuid);

        return completeResponse.results();
    }

    @Override
    public List<Category> findAll(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return find(userId, new FindRequest(), false, true);
    }

    @Override
    public Category getByGUID(String userId, String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());
        GenericResponse<Category> response =
                client.getByGUIdRESTCall(userId, guid, getMethodInfo("getByGUID"), type, BASE_URL);
        return response.head().get();
    }

    @Override
    public List<Category> find(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        GenericResponse<Category> completeResponse =
        client.findRESTCall(userId,getMethodInfo("find"),BASE_URL,
                type, findRequest, exactValue, ignoreCase, null);

        return completeResponse.results();
    }


    @Override
    public List<Relationship> getAllRelationships(String userId, String guid) {
        return null;
    }

    @Override
    public List<Term> getTerms(String userId, String categoryGuid, FindRequest findRequest) {
        return null;
    }

    @Override
    public Class<? extends GenericResponse> responseType() {
        return SubjectAreaOMASAPIResponse.class;
    }
}
