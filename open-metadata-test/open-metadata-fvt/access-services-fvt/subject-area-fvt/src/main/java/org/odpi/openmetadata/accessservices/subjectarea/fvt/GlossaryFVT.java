/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;

import java.io.IOException;

/**
 * FVT resource to call subject area glossary client API
 */
public class GlossaryFVT
{
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Testglossary1";
    private static final String DEFAULT_TEST_GLOSSARY_NAME2 = "Testglossary2";
    private static final String DEFAULT_TEST_GLOSSARY_NAME3 = "Testglossary3";
    private SubjectAreaGlossary subjectAreaGlossary = null;
    private String serverName = null;

    public GlossaryFVT(String url,String serverName) throws InvalidParameterException
    {
        subjectAreaGlossary = new SubjectAreaImpl(serverName,url).getSubjectAreaGlossary();
        this.serverName=serverName;
    }
    public static void runit(String url) throws SubjectAreaCheckedExceptionBase
    {
        GlossaryFVT fvt =new GlossaryFVT(url,FVTConstants.SERVER_NAME1);
        fvt.run();
        // check that a second server will work
        GlossaryFVT fvt2 =new GlossaryFVT(url,FVTConstants.SERVER_NAME2);
        fvt2.run();
    }
    public static void main(String args[])
    {
        try
        {
           String url = RunAllFVT.getUrl(args);
           runit(url);

        } catch (IOException e1)
        {
            System.out.println("Error getting user input");
        } catch (SubjectAreaCheckedExceptionBase e)
        {
            System.out.println("ERROR: " + e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }
    }

    public void run() throws SubjectAreaCheckedExceptionBase
    {
            System.out.println("Create a glossary");
            Glossary glossary = createGlossary(serverName+" "+DEFAULT_TEST_GLOSSARY_NAME);
            Glossary glossary2 = createGlossary(serverName+" "+DEFAULT_TEST_GLOSSARY_NAME2);
            Glossary glossaryForUpdate = new Glossary();
            glossaryForUpdate.setName(serverName+" "+DEFAULT_TEST_GLOSSARY_NAME3);

            if (glossary != null)
            {
                System.out.println("Get the glossary");
                String guid = glossary.getSystemAttributes().getGUID();
                Glossary gotGlossary = getGlossaryByGUID(guid);
                System.out.println("Update the glossary");
                Glossary updatedGlossary = updateGlossary(guid, glossaryForUpdate);
                System.out.println("Get the glossary again");
                gotGlossary = getGlossaryByGUID(guid);
                System.out.println("Delete the glossary");
                gotGlossary = deleteGlossary(guid);
                System.out.println("Purge a glossary");
                String guid2 = glossary2.getSystemAttributes().getGUID();
                purgeGlossary(guid2);
                System.out.println("Create glossary with the same name as a deleted one");
                glossary = createGlossary(serverName+" "+DEFAULT_TEST_GLOSSARY_NAME);
                System.out.println("Create the glossary with same name as an active one and expect to fail");
                try
                {
                    glossary = createGlossary(serverName+" "+DEFAULT_TEST_GLOSSARY_NAME);
                } catch (InvalidParameterException ipe)
                {
                    System.out.println("Expected failure occurred.");
                }
            }
           }

    public  Glossary createGlossary(String name) throws SubjectAreaCheckedExceptionBase
    {
        Glossary glossary = new Glossary();
        glossary.setName(name);
        Glossary newGlossary  = subjectAreaGlossary.createGlossary(serverName,FVTConstants.USERID, glossary);
        if (newGlossary != null)
        {
            System.out.println("Created Glossary " + newGlossary.getName() + " with guid " + newGlossary.getSystemAttributes().getGUID());
        }
        return newGlossary;
    }

    public  Glossary getGlossaryByGUID(String guid) throws SubjectAreaCheckedExceptionBase
    {
        Glossary glossary = subjectAreaGlossary.getGlossaryByGuid(serverName,FVTConstants.USERID, guid);
        if (glossary != null)
        {
            System.out.println("Got Glossary " + glossary.getName() + " with guid " + glossary.getSystemAttributes().getGUID() + " and status " + glossary.getSystemAttributes().getStatus());
        }
        return glossary;
    }

    public  Glossary updateGlossary(String guid, Glossary glossary) throws SubjectAreaCheckedExceptionBase
    {
        Glossary updatedGlossary = subjectAreaGlossary.updateGlossary(serverName,FVTConstants.USERID, guid, glossary);
        if (updatedGlossary != null)
        {
            System.out.println("Updated Glossary name to " + updatedGlossary.getName());
        }
        return updatedGlossary;
    }

    public Glossary deleteGlossary(String guid) throws SubjectAreaCheckedExceptionBase
    {
        Glossary deletedGlossary = subjectAreaGlossary.deleteGlossary(serverName,FVTConstants.USERID, guid);
        if (deletedGlossary != null)
        {
            System.out.println("Deleted Glossary name is " + deletedGlossary.getName());
        }
        return deletedGlossary;
    }

    public  void purgeGlossary(String guid) throws SubjectAreaCheckedExceptionBase
    {
        subjectAreaGlossary.purgeGlossary(serverName,FVTConstants.USERID, guid);
        System.out.println("Purge succeeded");
    }
}
