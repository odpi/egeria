/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.glossarys;

import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;
//import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryParams;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResponseParameterization;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
//import org.odpi.openmetadata.accessservices.glossaryview.rest.Glossary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.odpi.openmetadata.viewservices.glossaryauthor.fvt.FVTConstants.GLOSSARY_AUTHOR_BASE_URL;
/**
 * The class acts as a wrapper class for calling the REST services for Glossary Author Glossary related services.
 */
public class GlossaryAuthorViewGlossaryClient implements GlossaryAuthorViewGlossary, ResponseParameterization<Glossary> {

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

        String urlTemplate = BASE_URL + "/%s";

        GenericResponse<Glossary> response = client.getByGUIdRESTCall( userId,
                                guid,
                                getMethodInfo("getByGUID"),
                                getParameterizedType(),
                                urlTemplate);

        return response.head().get();
    }

    @Override
    public void delete(String userId, String guid) throws PropertyServerException {

        //GenericResponse<Glossary> response =
        client.delRESTCall(userId,
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
        return response.results();
    }
    //find with conditions
    @Override
    public List<Category> getCategories(String userId, String guid, FindRequest findRequest, boolean onlyTop) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        String urnTemplate = BASE_URL +  "/" + guid + "/categories";

        Map<String, String> params = new HashMap<>();
        params.put("exactValue", "false");
        params.put("ignoreCase", "true");
        params.put("onlyTop", String.valueOf(onlyTop));


        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());
        GenericResponse<Category> response = client.getByIdRESTCall(userId,
                guid,
                getMethodInfo("getCategories"),
                type,
                urnTemplate,
                findRequest,
                0,
                params);

        return response.results();

    }

    //find
    @Override
    public List<Category> getCategories(String userId, String guid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        String urnTemplate = BASE_URL +  "/" + guid + "/categories";
        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());
        GenericResponse<Category> response = client.findRESTCall(userId,
                                                                getMethodInfo("getCategories"),
                                                                urnTemplate,
                                                                type,
                                                                findRequest,
                                                                false,
                                                                true,
                                                                0);

        return response.results();
    }

    @Override
    public List<Term> getTerms(String userId, String guid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException{
        return getTerms(userId, guid, findRequest, false, true,null);
    }

    @Override
    public List<Term> createTerms(String userId, String guid, Term[] termArray) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Term.class);
        String urnTemplate = BASE_URL +  "/%s/terms";

        ResolvableType resolvableType1 = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, resolvableType);
//        System.out.println(resolvableType1.getType().toString());
        ParameterizedTypeReference<SubjectAreaOMASAPIResponse<SubjectAreaOMASAPIResponse<Term>>> type = ParameterizedTypeReference.forType(resolvableType1.getType());

        SubjectAreaOMASAPIResponse<SubjectAreaOMASAPIResponse<Term>> completeResponse =
        client.postRESTCallArr(userId, getMethodInfo("createMultipleTerms"), urnTemplate, guid,type,termArray);

        List<SubjectAreaOMASAPIResponse<Term>> temp =  completeResponse.results();
        List<Term> termList = new ArrayList<>();
        for (GenericResponse<Term> resp: temp){
            if (resp.results().size() > 0) {
                termList.add(resp.results().get(0));
            }
        }
//        System.out.println(completeResponse.toString());
        return termList;
    }


    public List<Term> getTerms(String userId, String guid, FindRequest findRequest,boolean exactValue ,boolean ignoreCase, Integer maximumPageSizeOnRestCall) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Term.class);
        String urnTemplate = BASE_URL +  "/%s/terms";
        ParameterizedTypeReference<GenericResponse<Term>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        QueryParams queryParams = new QueryParams()
                .setExactValue(exactValue)
                .setIgnoreCase(ignoreCase);

        GenericResponse<Term> response = client.getByIdRESTCall(userId,
                guid,
                getMethodInfo("getTerms"),
                type,
                urnTemplate,
                findRequest,
                maximumPageSizeOnRestCall,
                queryParams);

        return response.results();
    }

    protected String getMethodInfo(String methodName) {
        return methodName + " for " + resultType().getSimpleName();
    }
}
