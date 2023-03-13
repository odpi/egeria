/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.graph;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.StatusFilter;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Graph;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.RelationshipType;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.GraphStatistics;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface GlossaryAuthorViewGraph {

    /**
     * Create a Graph.
     * <p>
     * The result is the Graph object
     *
     * @param userId       userId under which the request is performed
     * @param graph     Graph object to be created
     *
     * @return The Graph
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Graph create(String userId, Graph graph) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;


    /**
     * Get a Graph.
     * <p>
     * The result is the requested Graph object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Graph object to be retrieved
     *
     * @return The requested Graph
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Graph getByGUID(String userId, String guid) throws PropertyServerException,UserNotAuthorizedException, InvalidParameterException ;

    /**
     * Update a Graph.
     * <p>
     * The result is the updated Glossary object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Glossary object to be updated
     * @param graph     Glossary object with updated values
     * @param isReplace    If the object is to be replaced
     *
     * @return The updated Graph
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Graph update(String userId, String guid, Graph graph, boolean isReplace) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

    /**
     * Delete a Graph.
     * <p>
     * The result Void object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Graph object to be retrieved
     *
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    void delete(String userId, String guid) throws PropertyServerException;

    /**
     * Restore a soft-deleted Graph.
     * <p>
     * The result is the restored Graph object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Graph object to be restored
     *
     * @return The restored Graph
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Graph restore(String userId, String guid) throws PropertyServerException, UserNotAuthorizedException,InvalidParameterException  ;


    /**
     * Extract children within a Category
     *
     * @param userId calling user
     * @param parentGuid Category GUID
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     * @param exactValue  exactValue - when false values with trailing characters will match.
     * @param ignoreCase  ignore the case when matching.
     *
     * @return list of  Categories
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Category> getCategoryChildren(String userId, String parentGuid, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;

    /**
     * Find Category
     *
     * @param userId calling user
     *
     * @return Categories belonging to Userid
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Graph> findAll(String userId) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;
    /**
     * Find graphs
     *
     * @param userId calling user
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     * @param exactValue  exactValue - when false values with trailing characters will match.
     * @param ignoreCase  ignore the case when matching.
     *
     * @return list of  Categories
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Graph> find(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;

    /**
     * Find Category for a graph
     *
     * @param userId calling user
     * @param graphGuid graph id
     * @param findRequest nformation object for find calls. This include pageSize to limit the number of elements returned.
     *
     *
     * @return Categories belonging to graph
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */

    List<Category> getCategories(String userId, String graphGuid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Get config for server
     *
     * @param userId calling user
     *
     * @return Config for view server
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    OMAGServerConfig getConfig(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Get list of view service config on the server
     *
     * @param userId calling user
     *
     * @return Config for view server
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<ViewServiceConfig> getViewServiceConfigs(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Get service config for a particular view Service
     *
     * @param userId calling user
     *
     * @return Config for view server
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    ViewServiceConfig getGlossaryAuthViewServiceConfig(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Get graph for a particular node guid
     *
     * @param userId calling user
     * @param guid                  the starting point of the query.
     * @param nodeFilter            Set of node names to include in the query results.  Null means include
     *                              all entities found, irrespective of their type.
     * @param relationshipFilter    Set of relationship names to include in the query results.  Null means include
     *                              all relationships found, irrespective of their type.
     * @param asOfTime              Requests a historical query of the relationships for the entity.  Null means return the
     *                              present values.
     * @param statusFilter          By default only active instances are returned. Specify ALL to see all instance in any status.
     *
     * @return A graph of nodeTypes.
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Graph getGraph(String userId, String guid, Date asOfTime, Set<NodeType> nodeFilter, Set<RelationshipType> relationshipFilter, StatusFilter statusFilter) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Get graphStats for a particular  guid
     *
     * @param userId calling user
     * @param guid                  Guid for a node.
     * @param asOfTime              Requests a historical query of the relationships for the entity.  Null means return the
     *                              present values.
     * @param nodeFilter            Set of node names to include in the query results.  Null means include
     *                              all entities found, irrespective of their type.
     * @param relationshipFilter    Set of relationship names to include in the query results.  Null means include
     *                              all relationships found, irrespective of their type.

     * @param statusFilter          By default only active instances are returned. Specify ALL to see all instance in any status.
     *
     * @return A graph statistics of nodeTypes.
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */

    GraphStatistics getGraphStatistics(String userId, String guid, Date asOfTime, Set<NodeType> nodeFilter, Set<RelationshipType> relationshipFilter, StatusFilter statusFilter) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;
}

