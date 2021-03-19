/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaGraph;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaGraphClient;
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
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.io.IOException;
import java.util.*;

/**
 * FVT resource to call subject area term client API
 */
public class GraphFVT
{
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for Graph FVT";
    private static final String DEFAULT_TEST_TERM_NAME1 = "Test term 1";
    private static final String DEFAULT_TEST_TERM_NAME2 = "Test term 2";
    private static final String DEFAULT_TEST_TERM_NAME3 = "Test term 3";
    private static final String DEFAULT_TEST_CATEGORY_NAME = "Test category 1";
    private SubjectAreaGraph subjectAreaGraph = null;
    private GlossaryFVT glossaryFVT =null;
    private TermFVT termFVT = null;
    private RelationshipsFVT relationshipFVT = null;
    private CategoryFVT categoryFVT = null;
    private SubjectAreaDefinitionCategoryFVT subjectAreaFVT = null;

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
        } catch (SubjectAreaFVTCheckedException e) {
            System.out.println("ERROR: " + e.getMessage() );
        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            System.out.println("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());

        }

    }
    public GraphFVT(String url, String serverName,String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaRestClient client = new SubjectAreaRestClient(serverName, url);
        subjectAreaGraph = new SubjectAreaGraphClient(client);
        System.out.println("Create a glossary");
        glossaryFVT = new GlossaryFVT(url,serverName,userId);
        termFVT = new TermFVT(url,serverName,userId);
        categoryFVT = new CategoryFVT(url,serverName,userId);
        relationshipFVT = new RelationshipsFVT(url,serverName,userId);
        subjectAreaFVT = new SubjectAreaDefinitionCategoryFVT(url,serverName,userId);
        this.serverName=serverName;
        this.userId=userId;
    }
    public void deleteRemaining() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, SubjectAreaFVTCheckedException {
        termFVT.deleteRemainingTerms();
        // delete the subject area first so the delete of the categories do not pick them up
        subjectAreaFVT.deleteRemainingSubjectAreas();
        categoryFVT.deleteRemainingCategories();
        relationshipFVT.deleteRemaining();
        glossaryFVT.deleteRemainingGlossaries();
    }
    public static void runWith2Servers(String url) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }

    public static void runIt(String url, String serverName, String userId) throws InvalidParameterException, SubjectAreaFVTCheckedException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("GraphFVT runIt started");
        GraphFVT fvt = new GraphFVT(url, serverName, userId);
        fvt.run();
        fvt.deleteRemaining();
        System.out.println("GraphFVT runIt stopped");
    }

    public void run() throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Glossary glossary= glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);

        String glossaryGuid = glossary.getSystemAttributes().getGUID();

        Graph graph = getGraph(glossaryGuid,
                null,
                null,
                null,
                null,
                3);
        checkGraphContent(graph,1,0);
        Term term1 =termFVT.createTerm(DEFAULT_TEST_TERM_NAME1,glossaryGuid);
        graph = getGraph(glossaryGuid,
                null,
                null,
                null,
                null,
                3);
        checkGraphContent(graph,2,1);
        Term term2 =termFVT.createTerm(DEFAULT_TEST_TERM_NAME2,glossaryGuid);
        graph = getGraph(glossaryGuid,
            null,
            null,
            null,
            null,
                3);
        checkGraphContent(graph,3,2);
        graph = getGraph(term1.getSystemAttributes().getGUID(),
                null,
                null,
                null,
                null,
                1);
        checkGraphContent(graph,2,1);
        graph = getGraph(term1.getSystemAttributes().getGUID(),
                null,
                null,
                null,
                null,
                2);
        checkGraphContent(graph,3,2);
        graph = getGraph(term1.getSystemAttributes().getGUID(),
                null,
                null,
                null,
                null,
                3);
        checkGraphContent(graph,3,2);
        relationshipFVT.createSynonym(term1,term2);

        graph = getGraph(term1.getSystemAttributes().getGUID(),
                null,
                null,
                null,
                null,
                3);
        checkGraphContent(graph,3,3);

        graph = getGraph(term1.getSystemAttributes().getGUID(),
                null,
                null,
                new HashSet<>(Arrays.asList(RelationshipType.Synonym)),
                null,
                3);
        checkGraphContent(graph,2,1);

        Term term3 =termFVT.createTerm(DEFAULT_TEST_TERM_NAME3,glossaryGuid);
        relationshipFVT.createSynonym(term1,term3);
        graph = getGraph(term1.getSystemAttributes().getGUID(),
                null,
                null,
                new HashSet<>(Arrays.asList(RelationshipType.Synonym)),
                null,
                3);
        // expect 3 terms with the 2 Synonym relationships from term1.
        checkGraphContent(graph,3,2);

        // at this stage we should have a glossary, 3 terms, 3 term to glossary relationships and 2 synonym relationships
        // confirm that we have this number
        graph = getGraph(glossaryGuid,
                null,
                null,
                null,
                null,
                3);
        checkGraphContent(graph,4,5);

        graph = getGraph(term3.getSystemAttributes().getGUID(),
                null,
                null,
                new HashSet<>(Arrays.asList(RelationshipType.Synonym)),
                null,
                1);
        checkGraphContent(graph,2,1);
        graph = getGraph(glossaryGuid,
                null,
                new HashSet<>(Arrays.asList(NodeType.Glossary,NodeType.Term)),
                null,

                null,
                1);
        // expect to only pick up the TermAnchor relationships not the synonyms because we have depth 1.
        checkGraphContent(graph,4,3);

        // createCategory
        Category category  =categoryFVT.createCategory(DEFAULT_TEST_CATEGORY_NAME,glossaryGuid);
        // check it is ignored
        graph = getGraph(glossaryGuid,
                null,
                new HashSet<>(Arrays.asList(NodeType.Glossary,NodeType.Term)),
                null,
                null,
                1);
        checkGraphContent(graph,4,3);
        // check we now pickup the category as well
        graph = getGraph(glossaryGuid,
                null,
                new HashSet<>(Arrays.asList(NodeType.Glossary,NodeType.Term,NodeType.Category)),
                null,
                null,
                1);
        checkGraphContent(graph,5,4);

        Taxonomy taxonomy= glossaryFVT.getTaxonomyForInput(DEFAULT_TEST_GLOSSARY_NAME);
        Glossary createdTaxonomy= glossaryFVT.issueCreateGlossary(taxonomy);
        String taxonomyGuid = createdTaxonomy.getSystemAttributes().getGUID();
        SubjectAreaDefinition subjectAreaDefinition = subjectAreaFVT.createSubjectAreaDefinitionWithGlossaryGuid("Subject area 1",taxonomyGuid);
        graph = getGraph(taxonomyGuid,
                null,
               null,
                null,
                null,
                3);
        checkGraphContent(graph,2,1);

        checkNodesContainNodeType(graph,NodeType.Taxonomy);
        checkNodesContainNodeType(graph,NodeType.SubjectAreaDefinition);
        // delete the term, category and subject area we created.
        subjectAreaFVT.deleteSubjectAreaDefinition(subjectAreaDefinition.getSystemAttributes().getGUID());
        categoryFVT.deleteCategory(category.getSystemAttributes().getGUID());
        termFVT.deleteTerm(term1.getSystemAttributes().getGUID());
        termFVT.deleteTerm(term2.getSystemAttributes().getGUID());
        termFVT.deleteTerm(term3.getSystemAttributes().getGUID());
        glossaryFVT.deleteGlossary(taxonomyGuid);
        glossaryFVT.deleteGlossary(glossaryGuid);
    }

    private void checkNodesContainNodeType(Graph graph, NodeType nodeTypeToCheck) throws SubjectAreaFVTCheckedException {
        boolean found = false;
        if (graph == null || graph.getNodes() == null || graph.getNodes().size() == 0 ) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected to find "+nodeTypeToCheck.name() + "but there were no nodes in the graph");
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
            throw new SubjectAreaFVTCheckedException("ERROR: Expected to find "+nodeTypeToCheck.name() + "but it did not exist");
        }
    }

    private void checkGraphContent(Graph graph,int expectedNodesSize,int expectedRelationshipsSize) throws SubjectAreaFVTCheckedException {
        System.out.println("CheckGraphContent expected " +expectedNodesSize + " Nodes and "+expectedRelationshipsSize + " Relationships" );
        if (graph.getNodes().size() !=expectedNodesSize ) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + expectedNodesSize +  " nodes, got " +graph.getNodes().size());
        }
        if (expectedRelationshipsSize ==0 && (graph.getRelationships() != null) ) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 0 and graph.getRelationships() to be null ");
        } else  if (expectedRelationshipsSize !=0 && (graph.getRelationships() == null) ) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + expectedRelationshipsSize + " and graph.getRelationships() is null ");
        } else if (graph.getRelationships()!=null && graph.getRelationships().size() !=expectedRelationshipsSize ) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + expectedRelationshipsSize + " relationships, got " +graph.getRelationships().size());
        }
    }

    private Graph getGraph(String guid,
                           Date asOfTime,
                           Set<NodeType> nodeFilter,
                           Set<RelationshipType> relationshipFilter,
                           StatusFilter statusFilter,   // may need to extend this for controlled terms
                           int level) throws InvalidParameterException,
                                                 PropertyServerException,
                                                 UserNotAuthorizedException {
        return subjectAreaGraph.getGraph(
                userId,
                guid,
                asOfTime,
                nodeFilter,
                relationshipFilter,
                statusFilter,
                level);
    }
}
