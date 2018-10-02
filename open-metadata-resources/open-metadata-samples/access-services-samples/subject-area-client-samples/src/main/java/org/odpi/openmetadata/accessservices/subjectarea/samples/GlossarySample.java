/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.samples;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Sample to call subject area glossary client API
 */
public class GlossarySample {
    private static final String USERID = " Fred";
    private static final String DEFAULT_URL = "http://localhost:8080/open-metadata/access-services/subject-area";
    private static final String DEFAULT_TEST_GLOSSARY_NAME  = "Testglossary1";
    private static final String DEFAULT_TEST_GLOSSARY_NAME2 = "Testglossary2";
    private static final String DEFAULT_TEST_GLOSSARY_NAME3 = "Testglossary3";
    static SubjectAreaGlossary subjectAreaGlossary = null;


    public static void main(String args[]) {
        try {

            String url =RunAllSamples.getUrl(args);
            System.out.print("Using url " +url);
            initialiseGlossarySample(url);
            System.out.println("Create a glossary");
            Glossary glossary = createGlossary( DEFAULT_TEST_GLOSSARY_NAME);
            Glossary glossary2 = createGlossary( DEFAULT_TEST_GLOSSARY_NAME2);
            Glossary glossaryForUpdate = new Glossary();
            glossaryForUpdate.setName(DEFAULT_TEST_GLOSSARY_NAME3);

            if (glossary != null) {
                System.out.println("Get the glossary");
                String guid = glossary.getSystemAttributes().getGUID();
                Glossary gotGlossary = getGlossaryByGUID(guid);
                System.out.println("Update the glossary");
                Glossary updatedGlossary = updateGlossary(guid, glossaryForUpdate);
                System.out.println("Get the glossary again");
                gotGlossary = getGlossaryByGUID( guid);
                System.out.println("Delete the glossary");
                gotGlossary = deleteGlossary( guid);
                System.out.println("Purge a glossary");
                String guid2 = glossary2.getSystemAttributes().getGUID();
                purgeGlossary(guid2);
                System.out.println("Create glossary with the same name as a deleted one");
                glossary = createGlossary( DEFAULT_TEST_GLOSSARY_NAME);
                System.out.println("Create the glossary with same name as an active one and expect to fail");
                try {
                    glossary = createGlossary( DEFAULT_TEST_GLOSSARY_NAME);
                } catch (InvalidParameterException ipe) {
                    System.out.println("Expected failure occurred.");
                }
            }
        } catch (SubjectAreaCheckedExceptionBase e) {
            System.out.println("ERROR: " +e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        } catch (IOException e) {
            System.out.println("Error getting user input");
        }
    }

    public static Glossary createGlossary(String name) throws SubjectAreaCheckedExceptionBase {
        Glossary glossary = new Glossary();
        glossary.setName(name);
        Glossary newGlossary = null;
        newGlossary = subjectAreaGlossary.createGlossary(USERID, glossary);
        if (newGlossary != null) {
            System.out.println("Created Glossary " + newGlossary.getName() + " with guid " + newGlossary.getSystemAttributes().getGUID());
        }
        return newGlossary;
    }
    public static Glossary createTaxonomy(String name) throws SubjectAreaCheckedExceptionBase {
        Glossary glossary = new Glossary();
        glossary.setName(name);
        glossary.setNodeType(NodeType.Taxonomy);
        Glossary newGlossary = null;
        newGlossary = subjectAreaGlossary.createGlossary(USERID, glossary);
        if (newGlossary != null) {
            System.out.println("Created Glossary " + newGlossary.getName() + " with guid " + newGlossary.getSystemAttributes().getGUID());
        }
        return newGlossary;
    }

    public static Glossary getGlossaryByGUID( String guid) throws SubjectAreaCheckedExceptionBase {
        Glossary glossary = subjectAreaGlossary.getGlossaryByGuid(USERID, guid);
        if (glossary != null) {
            System.out.println("Got Glossary " + glossary.getName() + " with guid " + glossary.getSystemAttributes().getGUID() + " and status " + glossary.getSystemAttributes().getStatus());
        }
        return glossary;
    }

    public static Glossary updateGlossary( String guid, Glossary glossary) throws SubjectAreaCheckedExceptionBase {
        Glossary updatedGlossary = subjectAreaGlossary.updateGlossary(USERID, guid, glossary);
        if (updatedGlossary != null) {
            System.out.println("Updated Glossary name to " + updatedGlossary.getName());
        }
        return updatedGlossary;
    }

    public static Glossary deleteGlossary(String guid) throws SubjectAreaCheckedExceptionBase {
        Glossary deletedGlossary = subjectAreaGlossary.deleteGlossary(USERID, guid);
        if (deletedGlossary != null) {
            System.out.println("Deleted Glossary name is " + deletedGlossary.getName());
        }
        return deletedGlossary;
    }
    public static void purgeGlossary( String guid) throws SubjectAreaCheckedExceptionBase {
        subjectAreaGlossary.purgeGlossary(USERID, guid);
        System.out.println("Purge succeeded");
    }

    /**
     * Call this to initialise the glossary sample
     * @param url
     * @throws InvalidParameterException
     */
    public static void initialiseGlossarySample(String url) throws InvalidParameterException {
        subjectAreaGlossary = new SubjectAreaImpl(url).getSubjectAreaGlossary();
    }
}
