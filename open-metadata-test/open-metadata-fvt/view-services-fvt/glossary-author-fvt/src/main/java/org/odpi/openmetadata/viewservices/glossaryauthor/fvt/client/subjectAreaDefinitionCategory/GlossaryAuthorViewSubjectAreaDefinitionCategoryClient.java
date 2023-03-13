/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.subjectAreaDefinitionCategory;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;
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
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

import java.util.List;

import static org.odpi.openmetadata.viewservices.glossaryauthor.fvt.FVTConstants.ADMIN_BASE_URL;
import static org.odpi.openmetadata.viewservices.glossaryauthor.fvt.FVTConstants.GLOSSARY_AUTHOR_BASE_URL;

/**
 * The class acts as a wrapper class for calling the REST services for Glossary Author SubjectAreaDefinition related services.
 */


public class GlossaryAuthorViewSubjectAreaDefinitionCategoryClient implements GlossaryAuthorViewSubjectAreaDefinitionCategory, ResponseParameterization<SubjectAreaDefinition> {

    protected final GlossaryAuthorViewRestClient client;
    private static final String BASE_URL = GLOSSARY_AUTHOR_BASE_URL + "categories";
    private static final String GLOSSARY_AUTHOR_CONFIG_BASE_URL = ADMIN_BASE_URL + "configuration";
    private static final String GLOSSARY_AUTHOR_C_BASE_URL = ADMIN_BASE_URL + "view-services/glossary-author";
    private static final String GLOSSARY_AUTHOR_VIEWCONFIG_BASE_URL = ADMIN_BASE_URL + "view-services/configuration";

    protected String getMethodInfo(String methodName) {
        return methodName + " for " + resultType().getSimpleName();
    }

    public GlossaryAuthorViewSubjectAreaDefinitionCategoryClient(GlossaryAuthorViewRestClient client) {
        this.client = client;
    }


    @Override
    public SubjectAreaDefinition create(String userId, SubjectAreaDefinition subjectAreaDefinition) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        GenericResponse<SubjectAreaDefinition> response = client.postRESTCall(userId, getMethodInfo("create"), BASE_URL, getParameterizedType(), subjectAreaDefinition);

        return response.head().get();
    }

    @Override
    public SubjectAreaDefinition update(String userId, String guid, SubjectAreaDefinition subjectAreaDefinition, boolean isReplace) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        final String urlTemplate = BASE_URL + "/%s?isReplace=" + Boolean.toString(isReplace);
        String methodInfo = getMethodInfo("update(isReplace=" + isReplace + ")");

        GenericResponse<SubjectAreaDefinition> response = client.putRESTCall(userId,
                                                            guid,
                                                            methodInfo,
                                                            urlTemplate,
                                                            getParameterizedType(),
                                                            subjectAreaDefinition);

        return response.head().get();
    }

    @Override
    public void delete(String userId, String guid) throws PropertyServerException {
        String methodName = getMethodInfo("Delete");
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
    public SubjectAreaDefinition restore(String userId, String guid) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        String methodName = getMethodInfo("Restore");
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, SubjectAreaDefinition.class);
        ParameterizedTypeReference<GenericResponse<SubjectAreaDefinition>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        String urlTemplate = BASE_URL + "/%s";

        GenericResponse<SubjectAreaDefinition> response = client.postRESTCall( userId,
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
    public List<SubjectAreaDefinition> findAll(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return find(userId, new FindRequest(), false, true);
    }

    @Override
    public SubjectAreaDefinition getByGUID(String userId, String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, SubjectAreaDefinition.class);
        ParameterizedTypeReference<GenericResponse<SubjectAreaDefinition>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        String urlTemplate = BASE_URL + "/%s";

        GenericResponse<SubjectAreaDefinition> response =
                client.getByGUIdRESTCall(userId, guid, getMethodInfo("getByGUID"), type, urlTemplate);
        return response.head().get();
    }

    @Override
    public List<SubjectAreaDefinition> find(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, SubjectAreaDefinition.class);
        ParameterizedTypeReference<GenericResponse<SubjectAreaDefinition>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        GenericResponse<SubjectAreaDefinition> completeResponse =
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
    public List<ViewServiceConfig> getViewServiceConfigs(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        ViewServicesResponse completeResponse =
                client.getViewConfigRESTCall(userId,getMethodInfo("getViewServiceConfig"),ViewServicesResponse.class,GLOSSARY_AUTHOR_VIEWCONFIG_BASE_URL);

        return completeResponse.getServices();
    }

    @Override
    public Class<? extends GenericResponse> responseType() {
        return SubjectAreaOMASAPIResponse.class;
    }

    public Synonym createSynonym(String userId, Synonym synonym) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Synonym.class);
        ParameterizedTypeReference<GenericResponse<Synonym>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        String urlTemplate = BASE_URL + "/synonyms";

        GenericResponse<Synonym> response = client.postRESTCall(userId, getMethodInfo("create"), urlTemplate, type, synonym);
        return response.head().get();
    }

    public Synonym getSynonym(String userId, String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Synonym.class);
        ParameterizedTypeReference<GenericResponse<Synonym>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        String urlTemplate = BASE_URL + "/synonyms/%s";

        GenericResponse<Synonym> response =
                client.getByGUIdRESTCall(userId, guid, getMethodInfo("getByGUID"), type, urlTemplate);
        return response.head().get();
    }

    public Synonym updateSynonym(String userId, String guid, Synonym updateSynonym, boolean isReplace) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        final String urlTemplate = BASE_URL + "/synonyms/%s?isReplace=" + Boolean.toString(isReplace);
        String methodInfo = getMethodInfo("update(isReplace=" + isReplace + ")");

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Synonym.class);
        ParameterizedTypeReference<GenericResponse<Synonym>> type = ParameterizedTypeReference.forType(resolvableType.getType());


        GenericResponse<Synonym> response = client.putRESTCall(userId,
                guid,
                methodInfo,
                urlTemplate,
                type,
                updateSynonym);

        return response.head().get();
    }

    public<T> T createRel(String userId, T t,ParameterizedTypeReference<GenericResponse<T>> type, String relType) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {

        String urlTemplate = BASE_URL + "/" + relType; //synonyms";

        GenericResponse<T> response = client.postRESTCall(userId, getMethodInfo("create"), urlTemplate, type, t);
        return response.head().get();
    }

    public <T> T getRel(String userId, String guid,ParameterizedTypeReference<GenericResponse<T>> type, String relType) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        String urlTemplate = BASE_URL + "/" + relType + "/%s";

        GenericResponse<T> response =
                client.getByGUIdRESTCall(userId, guid, getMethodInfo("getByGUID"), type, urlTemplate);
        return response.head().get();
    }

    public <T> T updateRel(String userId, String guid, T t, ParameterizedTypeReference<GenericResponse<T>> type, String relType, boolean isReplace) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        final String urlTemplate = BASE_URL + "/" + relType + "/%s?isReplace=" + Boolean.toString(isReplace);
        String methodInfo = getMethodInfo("update(isReplace=" + isReplace + ")");

        GenericResponse<T> response = client.putRESTCall(userId,
                guid,
                methodInfo,
                urlTemplate,
                type,
                t);

        return response.head().get();
    }
    public <T> T replaceRel(String userId, String guid, T t, ParameterizedTypeReference<GenericResponse<T>> type, String relType, boolean isReplace) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        final String urlTemplate = BASE_URL + "/" + relType + "/%s?isReplace=" + Boolean.toString(isReplace);
        String methodInfo = getMethodInfo("update(isReplace=" + isReplace + ")");

        GenericResponse<T> response = client.putRESTCall(userId,
                guid,
                methodInfo,
                urlTemplate,
                type,
                t);

        return response.head().get();
    }


    public <T> void deleteRel(String userId, String guid, ParameterizedTypeReference<GenericResponse<T>> type, String relType) throws PropertyServerException {
        String methodName = getMethodInfo("Delete");

        String urlTemplate = BASE_URL + "/" + relType;// + "/%s";//BASE_URL;// + "/%s";

        //GenericResponse<T> response =
        client.delRESTCall( userId,
                type,methodName,
                urlTemplate,
                guid);

        return;
    }

    public <T> T restoreRel(String userId, String guid, ParameterizedTypeReference<GenericResponse<T>> type, String relType) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        String methodName = getMethodInfo("Restore");

        String urlTemplate = BASE_URL + "/" + relType +  "/%s";

        GenericResponse<T> response = client.postRESTCall( userId,
                methodName,
                urlTemplate,
                type,
                guid);

        return response.head().get();
    }
}
