/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.term;

import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryBuilder;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.rest.ViewServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.ViewServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResponseParameterization;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

import java.util.List;

import static org.odpi.openmetadata.viewservices.glossaryauthor.fvt.FVTConstants.*;

/**
 * The class acts as a wrapper class for calling the REST services for Glossary Author Term related services.
 */


public class GlossaryAuthorViewTermClient implements GlossaryAuthorViewTerm, ResponseParameterization<Term> {

    protected final GlossaryAuthorViewRestClient client;
    private static final String BASE_URL = GLOSSARY_AUTHOR_BASE_URL + "terms";
    private static final String GLOSSARY_AUTHOR_CONFIG_BASE_URL = ADMIN_BASE_URL + "configuration";
    private static final String GLOSSARY_AUTHOR_C_BASE_URL = ADMIN_BASE_URL + "view-services/glossary-author";
    private static final String GLOSSARY_AUTHOR_VIEWCONFIG_BASE_URL = ADMIN_BASE_URL + "view-services/configuration";

    protected String getMethodInfo(String methodName) {
        return methodName + " for " + resultType().getSimpleName();
    }

    public GlossaryAuthorViewTermClient(GlossaryAuthorViewRestClient client) {
        this.client = client;
    }


    @Override
    public Term create(String userId, Term term) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        GenericResponse<Term> response = client.postRESTCall(userId, getMethodInfo("create"), BASE_URL, getParameterizedType(), term);

        return response.head().get();
    }

    @Override
    public Term update(String userId, String guid, Term term) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        return update(userId,guid,term,false);
    }

    @Override
    public Term update(String userId, String guid, Term term, boolean isReplace) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        final String urlTemplate = BASE_URL + "/%s?isReplace=" + Boolean.toString(isReplace);
        String methodInfo = getMethodInfo("update(isReplace=" + isReplace + ")");

        GenericResponse<Term> response = client.putRESTCall(userId,
                                                            guid,
                                                            methodInfo,
                                                            urlTemplate,
                                                            getParameterizedType(),
                                                            term);
//        System.out.println(response.toString());
        return response.head().get();
    }

    @Override
    public void delete(String userId, String guid) throws PropertyServerException {
        String methodName = getMethodInfo("Restore");
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        String urlTemplate = BASE_URL;// + "/%s";

        //GenericResponse<Category> response =
        client.delRESTCall( userId,
                type,methodName,
                urlTemplate,
                guid);

        return;
    }

    @Override
    public Term restore(String userId, String guid) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        String methodName = getMethodInfo("Restore");
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Term.class);
        ParameterizedTypeReference<GenericResponse<Term>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        String urlTemplate = BASE_URL + "/%s";

        GenericResponse<Term> response = client.postRESTCall( userId,
                methodName,
                urlTemplate,
                type,
                guid);

        return response.head().get();
    }

    @Override
    public List<Category> getCategoryChildren(String userId, String parentGuid, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        String urlTemplate = BASE_URL + "/%s/categories";

        GenericResponse<Category> completeResponse =
                client.findRESTCallById(userId,getMethodInfo("getCategoryChildren"),urlTemplate,
                        type, findRequest, exactValue, ignoreCase, null,parentGuid);

        return completeResponse.results();
    }

    @Override
    public List<Term> findAll(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return find(userId, new FindRequest(), false, true);
    }

    @Override
    public Term getByGUID(String userId, String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Term.class);
        ParameterizedTypeReference<GenericResponse<Term>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        String urlTemplate = BASE_URL + "/%s";

        GenericResponse<Term> response =
                client.getByGUIdRESTCall(userId, guid, getMethodInfo("getByGUID"), type, urlTemplate);
        return response.head().get();
    }


    @Override
    public List<Term> find(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Term.class);
        ParameterizedTypeReference<GenericResponse<Term>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        GenericResponse<Term> completeResponse =
        client.findRESTCall(userId,getMethodInfo("find"),BASE_URL,
                type, findRequest, exactValue, ignoreCase, null);

        return completeResponse.results();
    }

    @Override
    public List<Category> getCategories(String userId, String termGuid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String urnTemplate = BASE_URL + "/%s/categories";
        final String methodInfo = getMethodInfo(" getCategories");
        QueryBuilder query = client.createFindQuery(methodInfo, findRequest);
        String urlTemplate = urnTemplate + query.toString();
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());
        GenericResponse<Category> response = client.getByIdRESTCall(userId ,termGuid, methodInfo, type, urlTemplate);
        return response.results();
    }

    @Override
    public OMAGServerConfig getConfig(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        OMAGServerConfigResponse completeResponse =
                client.getConfigRESTCall(userId,"current",getMethodInfo("getConfig"),OMAGServerConfigResponse.class,GLOSSARY_AUTHOR_CONFIG_BASE_URL);

        return completeResponse.getOMAGServerConfig();
    }

    @Override
    public ViewServiceConfig getGlossaryAuthViewServiceConfig(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        ViewServiceConfigResponse completeResponse =
                client.getViewServiceConfigRESTCall(userId,"current",getMethodInfo("getOmagServerName"),ViewServiceConfigResponse.class,GLOSSARY_AUTHOR_C_BASE_URL);

        return completeResponse.getConfig();
    }

    @Override
    public List<Relationship> getRelationships(String userId, String guid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Relationship.class);
        ParameterizedTypeReference<GenericResponse<Relationship>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        String urlTemplate = BASE_URL + "/%s/relationships";

        GenericResponse<Relationship> completeResponse =
                client.findRESTCallById(userId, getMethodInfo("findRelationships"), urlTemplate, type, findRequest, false, true, 0, guid);

        return completeResponse.results();
    }

    @Override
    public List<ViewServiceConfig> getViewServiceConfigs(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        ViewServicesResponse completeResponse =
                client.getViewConfigRESTCall(userId,getMethodInfo("getViewServiceConfig"),ViewServicesResponse.class,GLOSSARY_AUTHOR_VIEWCONFIG_BASE_URL);

        return completeResponse.getServices();
    }

    @Override
    public List<Relationship> getAllRelationships(String userId, String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return getRelationships( userId, guid, new FindRequest());
    }

    @Override
    public Class<? extends GenericResponse> responseType() {
        return SubjectAreaOMASAPIResponse.class;
    }
}
