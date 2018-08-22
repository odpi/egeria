/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.samples;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;

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

    public static void main(String args[]) {
        SubjectArea subjectArea = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a URL. Press enter to get the default ("+ DEFAULT_URL+ ".)):");
            String url = br.readLine();
            if (url.equals("")) {
                url = DEFAULT_URL;
            }
            System.out.print("Using url " +url);
            subjectArea = new SubjectAreaImpl(url);
            SubjectAreaGlossary subjectAreaGlossary = subjectArea.getSubjectAreaGlossary();
            System.out.println("Create a glossary");
            Glossary glossary = createGlossary(subjectAreaGlossary, DEFAULT_TEST_GLOSSARY_NAME);
            Glossary glossary2 = createGlossary(subjectAreaGlossary, DEFAULT_TEST_GLOSSARY_NAME2);
            Glossary glossaryForUpdate = new Glossary();
            glossaryForUpdate.setName(DEFAULT_TEST_GLOSSARY_NAME3);

            if (glossary != null) {
                System.out.println("Get the glossary");
                String guid = glossary.getSystemAttributes().getGUID();
                Glossary gotGlossary = getGlossaryByGUID(subjectAreaGlossary, guid);
                System.out.println("Update the glossary");
                Glossary updatedGlossary = updateGlossary(subjectAreaGlossary, guid, glossaryForUpdate);
                System.out.println("Get the glossary again");
                gotGlossary = getGlossaryByGUID(subjectAreaGlossary, guid);
                System.out.println("delete the glossary");
                gotGlossary = deleteGlossary(subjectAreaGlossary, guid);
                System.out.println("purge a glossary");
                String guid2 = glossary2.getSystemAttributes().getGUID();
                purgeGlossary(subjectAreaGlossary, guid2);
                System.out.println("Create glossary with the same name as a deleted one");
                glossary = createGlossary(subjectAreaGlossary, DEFAULT_TEST_GLOSSARY_NAME);
                System.out.println("Create the glossary with same name as an active one and expect to fail");
                try {
                    glossary = createGlossary(subjectAreaGlossary, DEFAULT_TEST_GLOSSARY_NAME);
                } catch (InvalidParameterException ipe) {
                    System.out.println("Expected failure occurred.");
                }
            }
        } catch (SubjectAreaCheckedExceptionBase e) {
            System.out.println(e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        } catch (IOException e) {
            System.out.println("Error getting user input");
        }
    }

    public static Glossary createGlossary(SubjectAreaGlossary subjectAreaGlossary, String name) throws SubjectAreaCheckedExceptionBase {
        Glossary glossary = new Glossary();
        glossary.setName(name);
        Glossary newGlossary = null;
        newGlossary = subjectAreaGlossary.createGlossary(USERID, glossary);
        if (newGlossary != null) {
            System.out.println("Created Glossary " + newGlossary.getName() + " with guid " + newGlossary.getSystemAttributes().getGUID());
        }
        return newGlossary;
    }

    public static Glossary getGlossaryByGUID(SubjectAreaGlossary subjectAreaGlossary, String guid) throws SubjectAreaCheckedExceptionBase {
        Glossary glossary = subjectAreaGlossary.getGlossaryByGuid(USERID, guid);
        if (glossary != null) {
            System.out.println("Got Glossary " + glossary.getName() + " with guid " + glossary.getSystemAttributes().getGUID() + " and status " + glossary.getSystemAttributes().getStatus());
        }
        return glossary;
    }

    public static Glossary updateGlossary(SubjectAreaGlossary subjectAreaGlossary, String guid, Glossary glossary) throws SubjectAreaCheckedExceptionBase {
        Glossary updatedGlossary = subjectAreaGlossary.updateGlossary(USERID, guid, glossary);
        if (updatedGlossary != null) {
            System.out.println("Updated Glossary name to " + updatedGlossary.getName());
        }
        return updatedGlossary;
    }

    public static Glossary deleteGlossary(SubjectAreaGlossary subjectAreaGlossary, String guid) throws SubjectAreaCheckedExceptionBase {
        Glossary deletedGlossary = subjectAreaGlossary.deleteGlossary(USERID, guid);
        if (deletedGlossary != null) {
            System.out.println("Deleted Glossary name is " + deletedGlossary.getName());
        }
        return deletedGlossary;
    }
    public static void purgeGlossary(SubjectAreaGlossary subjectAreaGlossary, String guid) throws SubjectAreaCheckedExceptionBase {
        subjectAreaGlossary.purgeGlossary(USERID, guid);
        System.out.println("Purge succeeded");
    }
}
