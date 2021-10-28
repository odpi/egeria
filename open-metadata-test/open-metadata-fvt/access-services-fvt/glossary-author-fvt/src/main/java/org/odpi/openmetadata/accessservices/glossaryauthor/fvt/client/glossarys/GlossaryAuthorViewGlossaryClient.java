package org.odpi.openmetadata.accessservices.glossaryauthor.fvt.client.glossarys;

import org.odpi.openmetadata.accessservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;
//import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryBuilder;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryUtils;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.odpi.openmetadata.accessservices.glossaryauthor.fvt.FVTConstants.GLOSSARY_AUTHOR_BASE_URL;

public class GlossaryAuthorViewGlossaryClient implements GlossaryAuthorViewGlossary, ResponseParameterization<Glossary> {

    //public static final String GLOSSARY_AUTHOR_BASE_URL_GLOSSARY = "/servers/%s/open-metadata/view-services/glossary-author/users/%s/";
    private static final String BASE_URL = GLOSSARY_AUTHOR_BASE_URL + "glossaries";
    protected final GlossaryAuthorViewRestClient client;

    public GlossaryAuthorViewGlossaryClient(GlossaryAuthorViewRestClient client) {
        this.client = client;
    }

    public GlossaryAuthorViewGlossaryClient(String serverName, String url) throws InvalidParameterException {
        this.client = new GlossaryAuthorViewRestClient(serverName, url);
    }

    public List<Glossary> find(String userId, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        return find(userId, findRequest, false,true);
        //GenericResponse<Glossary> response = this.client.findRESTCall(userId, getMethodInfo("find"), BASE_URL, getParameterizedType(), findRequest, exactValue, ignoreCase, maximumPageSizeOnRestCall);
        //return response.results();
    }

    public List<Glossary> find(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return find(userId, findRequest, exactValue, ignoreCase, null);
    }
    public List<Glossary> find(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase, Integer maximumPageSizeOnRestCall) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        GenericResponse<Glossary> response = client.findRESTCall(userId, getMethodInfo("find"), BASE_URL, getParameterizedType(), findRequest, exactValue, ignoreCase, maximumPageSizeOnRestCall);
        return response.results();
    }

    @Override
    public Class<? extends GenericResponse> responseType() {
        return SubjectAreaOMASAPIResponse.class;
    }

   /* @Override
    public Config getConfig(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "getConfig";

        String urlTemplate = BASE_URL + "/%s";
        GenericResponse<Config> response = client.getByIdRESTCall(userId,
                                                            "current",
                                                            methodName,
                                                            getParameterizedType(),
                                                            urlTemplate);*//*,
                                                            null,
                                                        null,
                                                            null);*//*

        return response.head().get();
    }
*/
    @Override
    public Glossary create(String userId, Glossary glossary) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        GenericResponse<Glossary> response = client.postRESTCall(userId, getMethodInfo("create"), BASE_URL, getParameterizedType(), glossary);

        return response.head().get();
    }

    @Override
    public Glossary update(String userId, String guid, Glossary glossary, boolean isReplace) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        Map<String, String> params = new HashMap<>();
        if (isReplace)
            params.put("isReplace", "true");
        else
            params.put("isReplace", "false");


        GenericResponse<Glossary> response = client.putRESTCall(userId,
                                                                guid,
                                                                getMethodInfo("create"),
                                                                BASE_URL,
                                                                getParameterizedType(),
                                                                glossary,
                                                                params);
        return response.head().get();
    }

    @Override
    public Glossary getByGUID(String userId, String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        GenericResponse<Glossary> response = client.getByGUIdRESTCall( userId,
                                guid,
                                getMethodInfo("getByGUID"),
                                getParameterizedType(),
                                BASE_URL);

        return response.head().get();
    }

    @Override
    public void delete(String userId, String guid) throws PropertyServerException {

        GenericResponse<Glossary> response = client.delRESTCall(userId,
                getParameterizedType(),
                getMethodInfo("delete"),
                BASE_URL,
                guid);
        return;
                //response.head().get();
    }

    @Override
    public Glossary restore(String userId, String guid) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {

        String urnTemplate = BASE_URL +  "/%s";
        GenericResponse<Glossary> response = client.postRESTCall(userId,
                getMethodInfo("restore"),
                urnTemplate,
                getParameterizedType(),
                guid);

        return response.head().get();
    }

    @Override
    public List<Relationship> getAllRelationships(String userId, String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Relationship.class);
        String urnTemplate = BASE_URL +  "/%s/relationships";
        ParameterizedTypeReference<GenericResponse<Relationship>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        GenericResponse<Relationship> response = client.getByIdRESTCall(userId,
                                                                guid,
                                                                getMethodInfo("getAllRelationships"),
                                                                type,
                                                                urnTemplate);
        //GenericResponse<Category> response = client.getByIdRESTCall(userId ,guid, methodInfo, type, urlTemplate);

        return response.results();
    }

    @Override
    public List<Category> getCategories(String userId, String guid, FindRequest findRequest, boolean onlyTop) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        String urnTemplate = BASE_URL +  "/%s/categories";
        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        GenericResponse<Category> response = client.getByIdRESTCall(userId,
                guid,
                getMethodInfo("getCategories"),
                type,
                urnTemplate);
        //GenericResponse<Category> response = client.getByIdRESTCall(userId ,guid, methodInfo, type, urlTemplate);

        return response.results();
    }

    @Override
    public List<Term> getTerms(String userId, String guid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Term.class);
        String urnTemplate = BASE_URL +  "/%s/terms";
        ParameterizedTypeReference<GenericResponse<Term>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        GenericResponse<Term> response = client.getByIdRESTCall(userId,
                guid,
                getMethodInfo("getTerms"),
                type,
                urnTemplate);
        //GenericResponse<Category> response = client.getByIdRESTCall(userId ,guid, methodInfo, type, urlTemplate);

        return response.results();
    }

    protected String getMethodInfo(String methodName) {
        return methodName + " for " + resultType().getSimpleName();
    }
}
