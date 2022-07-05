/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.fvt;

import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.graph.GlossaryAuthorViewGraphClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Taxonomy;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Graph;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.RelationshipType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.GraphStatistics;
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.NodeRelationshipStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * FVT resource to call Glossary Author View Graph API
 */
public class GraphFVT
{
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for Graph FVT";
    private static final String DEFAULT_TEST_TERM_NAME1 = "Test term 1";
    private static final String DEFAULT_TEST_TERM_NAME2 = "Test term 2";
    private static final String DEFAULT_TEST_TERM_NAME3 = "Test term 3";
    private static final String DEFAULT_TEST_CATEGORY_NAME = "Test category 1";
    private GlossaryAuthorViewGraphClient glossaryAuthorViewGraphClient = null;
    private GlossaryFVT glossaryFVT =null;
    private TermFVT termFVT = null;
    private RelationshipsFVT relationshipFVT = null;
    private CategoryFVT categoryFVT = null;
    private SubjectAreaDefinitionCategoryFVT subjectAreaFVT = null;
    private static Logger log = LoggerFactory.getLogger(GraphFVT.class);


    private String serverName = null;
    private String userId = null;
    public static void main(String args[])
    {
        try
        {
            String url = RunAllFVTOn2Servers.getUrl(args);
            runWith2Servers(url);
        } catch (IOException e1)
        {
            System.out.println("Error getting user input");
        } catch (GlossaryAuthorFVTCheckedException e) {
            log.error("ERROR: " + e.getMessage() );
        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            log.error("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());

        }

    }
    public GraphFVT(String url, String serverName,String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        GlossaryAuthorViewRestClient client = new GlossaryAuthorViewRestClient(serverName, url);
        glossaryAuthorViewGraphClient = new GlossaryAuthorViewGraphClient(client);
        if (log.isDebugEnabled()) {
            log.debug("Create a glossary");
        }
        glossaryFVT = new GlossaryFVT(url,serverName,userId);
        termFVT = new TermFVT(url,serverName,userId);
        categoryFVT = new CategoryFVT(url,serverName,userId);
        relationshipFVT = new RelationshipsFVT(url,serverName,userId);
        subjectAreaFVT = new SubjectAreaDefinitionCategoryFVT(url, serverName,userId);
        this.serverName=serverName;
        this.userId=userId;
    }
    public void deleteRemaining() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, GlossaryAuthorFVTCheckedException {
        termFVT.deleteRemainingTerms();
        // delete the subject area first so the delete of the categories do not pick them up
        subjectAreaFVT.deleteRemainingSubjectAreas();
        categoryFVT.deleteRemainingCategories();
        relationshipFVT.deleteRemaining();
        glossaryFVT.deleteRemainingGlossaries();
    }

    private String retrieveOmagServerName(String viewServiceName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        List<ViewServiceConfig> viewServiceConfigs = glossaryAuthorViewGraphClient.getViewServiceConfigs(userId);

        for (ViewServiceConfig vsc: viewServiceConfigs){
            if (vsc.getViewServiceName().equals(viewServiceName)) {
                if (log.isDebugEnabled()) {
                    log.debug("OMAG Server URL " + String.valueOf(vsc.getOMAGServerPlatformRootURL()));
                }
                return  String.valueOf(vsc.getOMAGServerName());
            }
        }
        return null;
    }
    public static void runWith2Servers(String url) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }

    public static void runIt(String url, String serverName, String userId) throws InvalidParameterException, GlossaryAuthorFVTCheckedException, PropertyServerException, UserNotAuthorizedException {
        try
        {
            System.out.println("GraphFVT runIt started");
            GraphFVT fvt = new GraphFVT(url, serverName, userId);
            fvt.run();
            fvt.deleteRemaining();
            System.out.println("GraphFVT runIt stopped");
        }
        catch (Exception error) {
            log.error("The FVT Encountered an Exception", error);
            throw error;
        }
    }

    public void run() throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Glossary glossary= glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);

        String glossaryGuid = glossary.getSystemAttributes().getGUID();

        Graph graph = getGraph(glossaryGuid,
                null,
                null,
                null,
                null);
        checkGraphContent(graph,1,0);

        Term term1 =termFVT.createTerm(DEFAULT_TEST_TERM_NAME1,glossaryGuid);
        graph = getGraph(glossaryGuid,
                null,
                null,
                null,
                null);
        checkGraphContent(graph,2,1);

        Term term2 =termFVT.createTerm(DEFAULT_TEST_TERM_NAME2,glossaryGuid);
        graph = getGraph(glossaryGuid,
            null,
            null,
            null,
            null);
        checkGraphContent(graph,3,2);


        graph = getGraph(term1.getSystemAttributes().getGUID(),
                null,
                null,
                null,
                null);
        checkGraphContent(graph,2,1);

        graph = getGraph(term2.getSystemAttributes().getGUID(),
                null,
                null,
                null,
                null);
        checkGraphContent(graph,2,1);

        relationshipFVT.createSynonym(term1,term2);

        graph = getGraph(term1.getSystemAttributes().getGUID(),
                null,
                null,
                null,
                null);
        checkGraphContent(graph,3,2);

        graph = getGraph(term1.getSystemAttributes().getGUID(),
                null,
                null,
                new HashSet<>(Arrays.asList(RelationshipType.Synonym)),
                null);
        checkGraphContent(graph,2,1);

        Term term3 =termFVT.createTerm(DEFAULT_TEST_TERM_NAME3,glossaryGuid);
        relationshipFVT.createSynonym(term1,term3);
        graph = getGraph(term1.getSystemAttributes().getGUID(),
                null,
                null,
                new HashSet<>(Arrays.asList(RelationshipType.Synonym)),
                null);
        // expect 3 terms with the 2 Synonym relationships from term1.
        checkGraphContent(graph,3,2);


        // at this stage we should have a glossary, 3 terms, 3 term to glossary relationships and 2 synonym relationships
        // confirm that we have this number
        graph = getGraph(glossaryGuid,
                null,
                null,
                null,
                null);
        checkGraphContent(graph,4,3);


        graph = getGraph(term3.getSystemAttributes().getGUID(),
                null,
                null,
                new HashSet<>(Arrays.asList(RelationshipType.Synonym)),
                null);
        checkGraphContent(graph,2,1);

        graph = getGraph(glossaryGuid,
                null,
                new HashSet<>(Arrays.asList(NodeType.Glossary,NodeType.Term)),
                null,
                null);

        // expect to only pick up the TermAnchor relationships not the synonyms.
        checkGraphContent(graph,4,3);

        // createCategory
        Category category  =categoryFVT.createCategory(DEFAULT_TEST_CATEGORY_NAME,glossaryGuid);
        // check it is ignored
        graph = getGraph(glossaryGuid,
                null,
                new HashSet<>(Arrays.asList(NodeType.Glossary,NodeType.Term)),
                null,
                null);
        checkGraphContent(graph,4,3);

        // check we now pickup the category as well
        graph = getGraph(glossaryGuid,
                null,
                new HashSet<>(Arrays.asList(NodeType.Glossary,NodeType.Term,NodeType.Category)),
                null,
                null);
        checkGraphContent(graph,5,4);

        Taxonomy taxonomy= glossaryFVT.getTaxonomyForInput(DEFAULT_TEST_GLOSSARY_NAME);
        Glossary createdTaxonomy= glossaryFVT.issueCreateGlossary(taxonomy);
        String taxonomyGuid = createdTaxonomy.getSystemAttributes().getGUID();
        SubjectAreaDefinition subjectAreaDefinition = subjectAreaFVT.createSubjectAreaDefinitionWithGlossaryGuid("Subject area 1",taxonomyGuid);
        graph = getGraph(taxonomyGuid,
                null,
               null,
                null,
                null);
        checkGraphContent(graph,2,1);

        checkNodesContainNodeType(graph,NodeType.Taxonomy);
        checkNodesContainNodeType(graph,NodeType.SubjectAreaDefinition);


    // Glossary Statistics
        GraphStatistics graphST = getGraphStats(taxonomyGuid,
                null,
                null,
                null,
                null);
        //check for nodeOrRelationshipTypeName as Taxonamy
        checkGraphStats1(graphST,"Taxonomy");


        graphST = getGraphStats(glossaryGuid,
                null,
                null,
                null,
                null);
        //check for nodeCounts and Relationship counts for term
        checkGraphStats2(graphST,"Term",3,"TermAnchor",3);

        graphST = getGraphStats(term3.getSystemAttributes().getGUID(),
                null,
                null,
                null,
                null);
        //check for node Term count & Relationship Synonym count
        checkGraphStats2(graphST,"Term",2,"Synonym",1);

        graphST = getGraphStats(term1.getSystemAttributes().getGUID(),
                null,
                null,
                null,
                null);
        //check for node Term count & Relationship Synonym count
        checkGraphStats2(graphST,"Term",3,"Synonym",2);

        graphST = getGraphStats(subjectAreaDefinition.getSystemAttributes().getGUID(),
                null,
                null,
                null,
                null);
        //check for getNodeCounts() and getRelationshipCounts()
        checkGraphStats(graphST,2,1);


        // delete the term, category and subject area we created.
        subjectAreaFVT.deleteSubjectAreaDefinition(subjectAreaDefinition.getSystemAttributes().getGUID());
        categoryFVT.deleteCategory(category.getSystemAttributes().getGUID());
        termFVT.deleteTerm(term1.getSystemAttributes().getGUID());
        termFVT.deleteTerm(term2.getSystemAttributes().getGUID());
        termFVT.deleteTerm(term3.getSystemAttributes().getGUID());
        glossaryFVT.deleteGlossary(taxonomyGuid);
        glossaryFVT.deleteGlossary(glossaryGuid);
    }

    private void checkGraphStats1(GraphStatistics graphST, String nodeToCheck) throws GlossaryAuthorFVTCheckedException {
            Map<String, NodeRelationshipStats> nodeInfos = graphST.getNodeCounts();

             if (!nodeInfos.containsKey(nodeToCheck)){
                throw new GlossaryAuthorFVTCheckedException("ERROR: Expected to find "+ nodeToCheck + " but it did not exist");
            }
    }
    private void checkGraphStats2(GraphStatistics graphST,String nodeTypeName, int  nodecount, String relTypeName, int relationshipCount) throws GlossaryAuthorFVTCheckedException {
        Map<String, NodeRelationshipStats> nodeInfos = graphST.getNodeCounts();
        Map<String, NodeRelationshipStats> relaInfos = graphST.getRelationshipCounts();
        NodeRelationshipStats nrStats;


        for (Map.Entry<String, NodeRelationshipStats> mapEntry: nodeInfos.entrySet()){
            if (mapEntry.getKey().equals(nodeTypeName)){
                nrStats = mapEntry.getValue();
                if (nrStats.getCount() != nodecount)
                    throw new GlossaryAuthorFVTCheckedException("ERROR: Expected to find "+ nodecount + " nodes but found " + nrStats.getCount());
/*                System.out.println(mapEntry.getValue());
                System.out.println(nrStats.getCount());
                System.out.println(nrStats.getNodeOrRelationshipTypeName());
*/
            }
        }
        for (Map.Entry<String, NodeRelationshipStats> mapEntry: relaInfos.entrySet()){
            if (mapEntry.getKey().equals(relTypeName)){
                nrStats = mapEntry.getValue();
                if (nrStats.getCount() != relationshipCount)
                    throw new GlossaryAuthorFVTCheckedException("ERROR: Expected to find "+ relationshipCount + " relationships but found " + nrStats.getCount());
                if (log.isDebugEnabled()) {
                    log.debug(mapEntry.getValue().toString());
                    log.debug(nrStats.getCount().toString());
                    log.debug(nrStats.getNodeOrRelationshipTypeName());
                }
            }
        }
    }

    private void checkGraphStats(GraphStatistics graphST, int nodeCount, int relCount) throws GlossaryAuthorFVTCheckedException {
        Map<String, NodeRelationshipStats> nodeInfos = graphST.getNodeCounts();
        Map<String, NodeRelationshipStats> relaInfos = graphST.getRelationshipCounts();
        if (!(nodeInfos.size() == nodeCount)){
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected to find "+ nodeCount + " node but it found " + nodeInfos.size());
        }

        if (!(relaInfos.size() == relCount)){
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected to find "+ relCount + " relationships but it found " + nodeInfos.size());
        }
    }

    private void checkNodesContainNodeType(Graph graph, NodeType nodeTypeToCheck) throws GlossaryAuthorFVTCheckedException {
        boolean found = false;
        if (graph == null || graph.getNodes() == null || graph.getNodes().size() == 0 ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected to find "+nodeTypeToCheck.name() + "but there were no nodes in the graph");
        }
        Map<String, Node> nodes =graph.getNodes();
        Set<String> guids = nodes.keySet();
        for (String guid:guids) {
            Node node = nodes.get(guid);
            if (node.getNodeType() == nodeTypeToCheck) {
                found = true;
            }
        }
        if (!found) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected to find "+nodeTypeToCheck.name() + "but it did not exist");
        }
    }

    private void checkGraphContent(Graph graph,int expectedNodesSize,int expectedRelationshipsSize) throws GlossaryAuthorFVTCheckedException {
        if (log.isDebugEnabled()) {
            log.debug("CheckGraphContent expected " +expectedNodesSize + " Nodes and "+expectedRelationshipsSize + " Relationships" );
        }
        if (graph.getNodes().size() !=expectedNodesSize ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected " + expectedNodesSize +  " nodes, got " +graph.getNodes().size());
        }
        if (expectedRelationshipsSize ==0 && (graph.getRelationships() != null) ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 0 and graph.getRelationships() to be null ");
        } else  if (expectedRelationshipsSize !=0 && (graph.getRelationships() == null) ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected " + expectedRelationshipsSize + " and graph.getRelationships() is null ");
        } else if (graph.getRelationships()!=null && graph.getRelationships().size() !=expectedRelationshipsSize ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected " + expectedRelationshipsSize + " relationships, got " +graph.getRelationships().size());
        }
    }

    private Graph getGraph(String guid,
                           Date asOfTime,
                           Set<NodeType> nodeFilter,
                           Set<RelationshipType> relationshipFilter,
                           StatusFilter statusFilter)   // may need to extend this for controlled terms
                           //int level)
                                            throws InvalidParameterException,
                                                 PropertyServerException,
                                                 UserNotAuthorizedException {

        return glossaryAuthorViewGraphClient.getGraph(
                userId,
                guid,
                asOfTime,
                nodeFilter,
                relationshipFilter,
                statusFilter);
//                level);
    }


    private GraphStatistics getGraphStats(String guid,
                           Date asOfTime,
                           Set<NodeType> nodeFilter,
                           Set<RelationshipType> relationshipFilter,
                           StatusFilter statusFilter)   // may need to extend this for controlled terms
                            throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {

        return glossaryAuthorViewGraphClient.getGraphStatistics(
                userId,
                guid,
                asOfTime,
                nodeFilter,
                relationshipFilter,
                statusFilter);
//                level);
    }
}
