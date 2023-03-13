/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.graph;

import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.StatusFilter;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.NeighborhoodHistoricalFindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Graph;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.RelationshipType;
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
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.GraphStatistics;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.viewservices.glossaryauthor.fvt.FVTConstants.ADMIN_BASE_URL;
import static org.odpi.openmetadata.viewservices.glossaryauthor.fvt.FVTConstants.GLOSSARY_AUTHOR_BASE_URL;
/**
 * The class acts as a wrapper class for calling the REST services for Glossary Author Graph related services.
 */

public class GlossaryAuthorViewGraphClient implements GlossaryAuthorViewGraph, ResponseParameterization<Graph> {

    protected final GlossaryAuthorViewRestClient client;
    private static final String BASE_URL = GLOSSARY_AUTHOR_BASE_URL + "graph";
    private static final String BASE_URL_GPSTATS = GLOSSARY_AUTHOR_BASE_URL + "graph-counts";

    private static final String GLOSSARY_AUTHOR_CONFIG_BASE_URL = ADMIN_BASE_URL + "configuration";
    private static final String GLOSSARY_AUTHOR_C_BASE_URL = ADMIN_BASE_URL + "view-services/glossary-author";


    private static final String GLOSSARY_AUTHOR_VIEWCONFIG_BASE_URL = ADMIN_BASE_URL + "view-services/configuration";

    protected String getMethodInfo(String methodName) {
        return methodName + " for " + resultType().getSimpleName();
    }

    public GlossaryAuthorViewGraphClient(GlossaryAuthorViewRestClient client) {
        this.client = client;
    }


    @Override
    public Graph create(String userId, Graph graph) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        GenericResponse<Graph> response = client.postRESTCall(userId, getMethodInfo("create"), BASE_URL, getParameterizedType(), graph);

        return response.head().get();
    }

    @Override
    public Graph update(String userId, String guid, Graph graph, boolean isReplace) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        final String urlTemplate = BASE_URL + "/%s?isReplace=" + Boolean.toString(isReplace);
        String methodInfo = getMethodInfo("update(isReplace=" + isReplace + ")");

        GenericResponse<Graph> response = client.putRESTCall(userId,
                                                            guid,
                                                            methodInfo,
                                                            urlTemplate,
                                                            getParameterizedType(),
                                                            graph);

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
    public Graph restore(String userId, String guid) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        String methodName = getMethodInfo("Restore");
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Graph.class);
        ParameterizedTypeReference<GenericResponse<Graph>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        String urlTemplate = BASE_URL + "/%s";

        GenericResponse<Graph> response = client.postRESTCall( userId,
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
    public List<Graph> findAll(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return find(userId, new FindRequest(), false, true);
    }

    @Override
    public Graph getByGUID(String userId, String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Graph.class);
        ParameterizedTypeReference<GenericResponse<Graph>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        String urlTemplate = BASE_URL + "/%s";

        GenericResponse<Graph> response =
                client.getByGUIdRESTCall(userId, guid, getMethodInfo("getByGUID"), type, urlTemplate);
        return response.head().get();
    }


    @Override
    public List<Graph> find(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Graph.class);
        ParameterizedTypeReference<GenericResponse<Graph>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        GenericResponse<Graph> completeResponse =
        client.findRESTCall(userId,getMethodInfo("find"),BASE_URL,
                type, findRequest, exactValue, ignoreCase, null);

        return completeResponse.results();
    }

    @Override
    public List<Category> getCategories(String userId, String graphGuid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String urnTemplate = BASE_URL + "/%s/categories";
        final String methodInfo = getMethodInfo(" getCategories");
        QueryBuilder query = client.createFindQuery(methodInfo, findRequest);
        String urlTemplate = urnTemplate + query.toString();
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Category.class);
        ParameterizedTypeReference<GenericResponse<Category>> type = ParameterizedTypeReference.forType(resolvableType.getType());
        GenericResponse<Category> response = client.getByIdRESTCall(userId ,graphGuid, methodInfo, type, urlTemplate);
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
    public GraphStatistics getGraphStatistics(String userId,
                              String guid,
                              Date asOfTime,
                              Set<NodeType> nodeFilter,
                              Set<RelationshipType> relationshipFilter,
                              StatusFilter statusFilter)
                                throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException
    {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, GraphStatistics.class);
        ParameterizedTypeReference<GenericResponse<GraphStatistics>> type = ParameterizedTypeReference.forType(resolvableType.getType());


        NeighborhoodHistoricalFindRequest request = new NeighborhoodHistoricalFindRequest();
        request.setAsOfTime(asOfTime);
        request.setNodeFilter(nodeFilter);
        request.setRelationshipFilter(relationshipFilter);
//        request.setLevel(level);
        request.setStatusFilter(statusFilter);
//       return getGraph(userId, guid, request);
        final String methodName = "getGraphStatistics";;

        String urlTemplate = BASE_URL_GPSTATS + "/%s" + createGraphQuery(request).toString();
        GenericResponse<GraphStatistics> response = client.getByIdRESTCall(userId, guid, methodName, type, urlTemplate);
        return response.head().get();
    }

    @Override
    public Graph getGraph(String userId,
                          String guid,
                          Date asOfTime,
                          Set<NodeType> nodeFilter,
                          Set<RelationshipType> relationshipFilter,
                          StatusFilter statusFilter)
                          throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException
    {
        NeighborhoodHistoricalFindRequest request = new NeighborhoodHistoricalFindRequest();
        request.setAsOfTime(asOfTime);
        request.setNodeFilter(nodeFilter);
        request.setRelationshipFilter(relationshipFilter);
        request.setStatusFilter(statusFilter);
        final String methodName = "getGraph";;

        String urlTemplate = BASE_URL + "/%s" + createGraphQuery(request).toString();
        GenericResponse<Graph> response = client.getByIdRESTCall(userId, guid, methodName, getParameterizedType(), urlTemplate);
        return response.head().get();
    }


    public QueryBuilder createGraphQuery(NeighborhoodHistoricalFindRequest request) {
        QueryBuilder queryBuilder = new QueryBuilder();
        String nodeFilter = request.getNodeFilter().stream().map(NodeType::name).collect(Collectors.joining(","));
        String relationshipFilter = request.getRelationshipFilter().stream().map(RelationshipType::name).collect(Collectors.joining(","));

        if (!nodeFilter.isEmpty())
            queryBuilder.addParam("nodeFilter", nodeFilter);

        if(!relationshipFilter.isEmpty())
            queryBuilder.addParam("relationshipFilter", relationshipFilter);

        return queryBuilder
                .addParam("asOfTime", request.getAsOfTime())
                .addParam("statusFilter", request.getStatusFilter().name());
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
}
