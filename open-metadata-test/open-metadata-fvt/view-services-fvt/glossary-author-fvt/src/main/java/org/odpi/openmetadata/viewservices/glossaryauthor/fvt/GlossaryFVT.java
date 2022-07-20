/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.fvt;

import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.glossarys.GlossaryAuthorViewGlossaryClient;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.term.GlossaryAuthorViewTermClient;
//import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.glossaries.GlossaryAuthorViewGlossaryClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
//import org.odpi.openmetadata.accessservices.glossaryview.rest.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Taxonomy;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * FVT resource to call Glossary author glossary API.
 *
 */
public class GlossaryFVT {
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Testglossary1";
    private static final String DEFAULT_TEST_GLOSSARY_NAME2 = "Testglossary2";
    private static final String DEFAULT_TEST_GLOSSARY_NAME3 = "Testglossary3";
    private static final String DEFAULT_TEST_TERM_LIST = "Test term Number ";
    private GlossaryAuthorViewGlossaryClient glossaryAuthorViewGlossaryClient = null;
    private GlossaryAuthorViewTermClient glossaryAuthorViewTermClient = null;
    private String serverName = null;
    private String userId = null;
    private int existingGlossaryCount = 0;
    private static Logger log = LoggerFactory.getLogger(GlossaryFVT.class);

    /*
     * Keep track of all the created guids in this set, by adding create and restore guids and removing when deleting.
     * At the end of the test it will delete any remaining guids.
     *
     * Note this FVT is called by other FVTs. Who ever constructs the FVT should run deleteRemainingSubjectAreas.
     */
    private Set<String> createdGlossariesSet;

    public GlossaryFVT(String url, String serverName, String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        GlossaryAuthorViewRestClient client = new GlossaryAuthorViewRestClient(serverName, url);
        this.glossaryAuthorViewGlossaryClient = new GlossaryAuthorViewGlossaryClient(client);
        this.glossaryAuthorViewTermClient = new GlossaryAuthorViewTermClient(client);
        this.serverName = serverName;
        this.userId = userId;
        createdGlossariesSet = new HashSet<>();
        existingGlossaryCount = findGlossaries("").size();
        if (log.isDebugEnabled()) {
            log.debug("existingGlossaryCount " + existingGlossaryCount);
        }
    }

    public static void runWith2Servers(String url) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, GlossaryAuthorFVTCheckedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }

    public static void main(String[] args) {
        try {
            String url = RunAllFVTOn2Servers.getUrl(args);
            runWith2Servers(url);
        } catch (IOException e1) {
            System.out.println("Error getting user input");
        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            log.error("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        } catch (GlossaryAuthorFVTCheckedException e) {
            log.error("ERROR: " + e.getMessage() );
        }
    }

    public static void runIt(String url, String serverName, String userId) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, GlossaryAuthorFVTCheckedException {
        try
        {
            System.out.println("GlossaryFVT runIt started ");
            GlossaryFVT fvt = new GlossaryFVT(url, serverName, userId);
            fvt.run();
            fvt.deleteRemainingGlossaries();
            System.out.println("GlossaryFVT runIt finished");
        }
        catch (Exception error) {
            log.error("The FVT Encountered an Exception", error);
            throw error;
        }
    }

    public static int getGlossaryCount(String url, String serverName, String userId) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, GlossaryAuthorFVTCheckedException {
        GlossaryFVT fvt = new GlossaryFVT(url, serverName, userId);
        return fvt.findGlossaries("").size();
    }

    public void run() throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        List<Glossary> initialGlossaryState = findGlossaries(null);
        int initialGlossaryCount = 0;
        if (initialGlossaryState != null && initialGlossaryState.size() > 0) {
            initialGlossaryCount = initialGlossaryState.size();
        }

        if (log.isDebugEnabled()) {
            log.debug("Create a glossary");
        }
        Glossary glossary = createGlossary(serverName + " " + DEFAULT_TEST_GLOSSARY_NAME);
        FVTUtils.validateNode(glossary);
        Glossary glossary2 = createGlossary(serverName + " " + DEFAULT_TEST_GLOSSARY_NAME2);
        FVTUtils.validateNode(glossary2);

        List<Glossary> results = findGlossaries(null);
        if (results.size() != initialGlossaryCount + 2) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected " + initialGlossaryCount + 2 + " back on the find got " + results.size());
        }
        Glossary glossaryForUpdate = new Glossary();
        glossaryForUpdate.setName(serverName + " " + DEFAULT_TEST_GLOSSARY_NAME3);
        if (log.isDebugEnabled()) {
            log.debug("Get the glossary");
        }
        String guid = glossary.getSystemAttributes().getGUID();
        Glossary gotGlossary = getGlossaryByGUID(guid);
        if (log.isDebugEnabled()) {
            log.debug("Update the glossary");
        }
        Glossary updatedGlossary = updateGlossary(guid, glossaryForUpdate);
        FVTUtils.validateNode(updatedGlossary);
        if (log.isDebugEnabled()) {
            log.debug("Get the glossary again");
        }
        gotGlossary = getGlossaryByGUID(guid);
        FVTUtils.validateNode(gotGlossary);
        if (log.isDebugEnabled()) {
            log.debug("Delete the glossary");
        }
        deleteGlossary(guid);
        //FVTUtils.validateNode(gotGlossary);
        if (log.isDebugEnabled()) {
            log.debug("restore the glossary");
        }
        gotGlossary = restoreGlossary(guid);
        FVTUtils.validateNode(gotGlossary);
        if (log.isDebugEnabled()) {
            log.debug("Delete the glossary again");
        }
        deleteGlossary(guid);
        //FVTUtils.validateNode(gotGlossary);
        if (log.isDebugEnabled()) {
            log.debug("Create glossary with the same name as a deleted one");
        }
        glossary = createGlossary(serverName + " " + DEFAULT_TEST_GLOSSARY_NAME);
        FVTUtils.validateNode(glossary);

        if (log.isDebugEnabled()) {
            log.debug("create glossaries to find");
        }
        Glossary glossaryForFind1 = getGlossaryForInput("qrs");
        glossaryForFind1.setQualifiedName("yyy");
        glossaryForFind1 = issueCreateGlossary(glossaryForFind1);
        FVTUtils.validateNode(glossaryForFind1);
        Glossary glossaryForFind2 = createGlossary("yyy");
        FVTUtils.validateNode(glossaryForFind2);
        Glossary glossaryForFind3 = createGlossary("zzz");
        FVTUtils.validateNode(glossaryForFind3);
        Glossary glossaryForFind4 = createGlossary("This is a Glossary with spaces in name");
        FVTUtils.validateNode(glossaryForFind4);

        results = findGlossaries("zzz");
        if (results.size() != 1) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 back on the find got " + results.size());
        }
        results = findGlossaries("yyy");
        if (results.size() != 2) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 2 back on the find got " + results.size());
        }
        //soft delete a glossary and check it is not found
        deleteGlossary(glossaryForFind2.getSystemAttributes().getGUID());
        results = findGlossaries("yyy");
        if (results.size() != 1) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 back on the find got " + results.size());
        }

        // search for a glossary with a name with spaces in
        results = findGlossaries("This is a Glossary with spaces in name");
        if (results.size() != 1) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 back on the find got " + results.size());
        }
        // make sure there is a glossary with the name
        createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        Glossary glossaryForUniqueQFN2= createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        if (glossaryForUniqueQFN2 == null || glossaryForUniqueQFN2.getQualifiedName().length() == 0) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected qualified name to be set");
        }
        //test Multiple terms
        String glossaryForUniqueQFN2Guid = glossaryForUniqueQFN2.getSystemAttributes().getGUID();
        List<Term> multipleTermList = new ArrayList<Term>();

        for (int i = 1;i <5; i++) {
            multipleTermList.add(getTermForInput(DEFAULT_TEST_TERM_LIST + String.valueOf(i), glossaryForUniqueQFN2Guid));
            //   DEFAULT_TEST_TERM_LIST
        }
        Term[] termArray = new Term[multipleTermList.size()];
        termArray = multipleTermList.toArray(termArray);

/*        List<Term> createdTermList = new ArrayList<Term>();
        createdTermList =*/
        createMultipleTerms(userId, glossaryForUniqueQFN2Guid, termArray); // multipleTermList.toArray());

        //System.out.println("*****^&*&*& " + createdTermList.toString() + " *****^&*&*& ");

        FindRequest findRequest = new FindRequest();
        findRequest.setSearchCriteria("");
        List<Term> termList = getTerms(glossaryForUniqueQFN2Guid, findRequest);
        if (termList.size() != 4) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 4 back on getGlossaryTerms for multipleCreate got " + results.size());
        }
        deleteTermsOnGlossary(userId,glossaryForUniqueQFN2Guid);
    }

    private void deleteTermsOnGlossary(String userId, String glossaryForUniqueQFN2Guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        FindRequest findRequest = new FindRequest();
        findRequest.setSearchCriteria("");
        List<Term> termList = getTerms(glossaryForUniqueQFN2Guid, findRequest);
        for (Term term: termList) {
            glossaryAuthorViewTermClient.delete(userId, term.getSystemAttributes().getGUID());
        }
    }


    public Term getTermForInput(String termName, String glossaryGuid) {
        Term term = new Term();
        term.setName(termName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term.setGlossary(glossarySummary);
        return term;
    }


    void deleteRemainingGlossaries() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        Iterator<String> iter =  createdGlossariesSet.iterator();
        while (iter.hasNext()) {
            String guid = iter.next();
            iter.remove();
            deleteGlossary(guid);
        }
        List<Glossary> glossaries = findGlossaries("");
        if (glossaries.size() != existingGlossaryCount) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected " +existingGlossaryCount + " glossaries, got " + glossaries.size());
        }
    }

    public Glossary createGlossary(String glossaryName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Glossary glossary = getGlossaryForInput(glossaryName);
        return issueCreateGlossary(glossary);
    }

    public Glossary issueCreateGlossary(Glossary glossary) throws  InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        Glossary newGlossary = glossaryAuthorViewGlossaryClient.create(this.userId, glossary);

        if (newGlossary != null) {
            String guid = newGlossary.getSystemAttributes().getGUID();
            if (log.isDebugEnabled()) {
                log.debug("Created Glossary " + newGlossary.getName() + " with userId " + guid);
            }
            createdGlossariesSet.add(guid);
        }
        return newGlossary;
    }

    public Glossary getGlossaryForInput(String glossaryName) {
        Glossary glossary = new Glossary();
        glossary.setName(glossaryName);
        return glossary;
    }

    public Taxonomy getTaxonomyForInput(String glossaryName) {
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setName(glossaryName);
        return taxonomy;
    }

    public Glossary createPastToGlossary(String name) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Glossary glossary = new Glossary();
        glossary.setName(name);
        long now = new Date().getTime();
        // expire the glossary 10 milliseconds ago
        glossary.setEffectiveToTime(new Date(now - 10).getTime());
        Glossary newGlossary = issueCreateGlossary(glossary);
        FVTUtils.validateNode(newGlossary);
        if (log.isDebugEnabled()) {
            log.debug("Created Glossary " + newGlossary.getName() + " with GUID " + newGlossary.getSystemAttributes().getGUID());
        }

        return newGlossary;
    }

    public Glossary createPastFromGlossary(String name) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Glossary glossary = new Glossary();
        glossary.setName(name);
        long now = new Date().getTime();
        // expire the glossary 10 milliseconds ago
        glossary.setEffectiveFromTime(new Date(now - 10).getTime());
        return issueCreateGlossary(glossary);
    }

    public Glossary createInvalidEffectiveDateGlossary(String name) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Glossary glossary = new Glossary();
        glossary.setName(name);
        long now = new Date().getTime();
        // expire the glossary 10 milliseconds ago
        glossary.setEffectiveFromTime(new Date(now - 10).getTime());
        glossary.setEffectiveToTime(new Date(now - 11).getTime());
        return issueCreateGlossary(glossary);
    }

    public Glossary createFutureGlossary(String name) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        Glossary glossary = new Glossary();
        glossary.setName(name);
        long now = new Date().getTime();
        // make the glossary effective in a days time for day
        glossary.setEffectiveFromTime(new Date(now + 1000 * 60 * 60 * 24).getTime());
        glossary.setEffectiveToTime(new Date(now + 2000 * 60 * 60 * 24).getTime());
        Glossary newGlossary = issueCreateGlossary(glossary);
        FVTUtils.validateNode(newGlossary);
        if (log.isDebugEnabled()) {
            log.debug("Created Glossary " + newGlossary.getName() + " with userId " + newGlossary.getSystemAttributes().getGUID());
        }
        return newGlossary;
    }

    public List<Glossary> findGlossaries(String criteria) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        FindRequest findRequest = new FindRequest();
        findRequest.setSearchCriteria(criteria);
        List<Glossary> glossaries = glossaryAuthorViewGlossaryClient.find(this.userId, findRequest);
        return glossaries;
    }

    public Glossary getGlossaryByGUID(String guid) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Glossary glossary = glossaryAuthorViewGlossaryClient.getByGUID(this.userId, guid);
        FVTUtils.validateNode(glossary);
        if (log.isDebugEnabled()) {
            log.debug("Got Glossary " + glossary.getName() + " with userId " + glossary.getSystemAttributes().getGUID() + " and status " + glossary.getSystemAttributes().getStatus());
        }
        return glossary;
    }

    public Glossary updateGlossary(String guid, Glossary glossary) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        boolean isReplace = true;
        Glossary updatedGlossary = glossaryAuthorViewGlossaryClient.update(this.userId, guid, glossary, isReplace);
        FVTUtils.validateNode(updatedGlossary);
        if (log.isDebugEnabled()) {
            log.debug("Updated Glossary name to " + updatedGlossary.getName());
        }
        return updatedGlossary;
    }

    public void deleteGlossary(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
            glossaryAuthorViewGlossaryClient.delete(this.userId, guid);
            createdGlossariesSet.remove(guid);
            if (log.isDebugEnabled()) {
                log.debug("Delete succeeded");
            }
    }

    public Glossary restoreGlossary(String guid) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Glossary restoredGlossary = glossaryAuthorViewGlossaryClient.restore(this.userId, guid);
        FVTUtils.validateNode(restoredGlossary);
        createdGlossariesSet.add(restoredGlossary.getSystemAttributes().getGUID());
        if (log.isDebugEnabled()) {
            log.debug("Restored Glossary name is " + restoredGlossary.getName());
        }
        return restoredGlossary;
    }

    public List<Relationship> getGlossaryRelationships(Glossary glossary) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return glossaryAuthorViewGlossaryClient.getAllRelationships(this.userId, glossary.getSystemAttributes().getGUID());
    }

    public List<Category> getCategories(String glossaryGuid, FindRequest findRequest, boolean onlyTop) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        return glossaryAuthorViewGlossaryClient.getCategories(userId, glossaryGuid, findRequest, onlyTop);
    }

    public List<Term> getTerms(String glossaryGuid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return glossaryAuthorViewGlossaryClient.getTerms(userId, glossaryGuid, findRequest);
    }

    public List<Term> createMultipleTerms(String userId, String guid, Term[] termArray) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
         return glossaryAuthorViewGlossaryClient.createTerms(userId,guid,termArray);
    }

}
