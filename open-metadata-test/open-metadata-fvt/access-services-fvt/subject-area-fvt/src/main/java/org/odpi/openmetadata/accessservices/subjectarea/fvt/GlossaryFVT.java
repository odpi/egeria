/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
        FVTUtils.validateNode(glossary);
        Glossary glossary2 = createGlossary(serverName+" "+DEFAULT_TEST_GLOSSARY_NAME2);
        FVTUtils.validateNode(glossary2);
        Glossary glossaryForUpdate = new Glossary();
        glossaryForUpdate.setName(serverName+" "+DEFAULT_TEST_GLOSSARY_NAME3);
        System.out.println("Get the glossary");
        String guid = glossary.getSystemAttributes().getGUID();
        Glossary gotGlossary = getGlossaryByGUID(guid);
        System.out.println("Update the glossary");
        Glossary updatedGlossary = updateGlossary(guid, glossaryForUpdate);
        FVTUtils.validateNode(updatedGlossary);
        System.out.println("Get the glossary again");
        gotGlossary = getGlossaryByGUID(guid);
        FVTUtils.validateNode(gotGlossary);
        System.out.println("Delete the glossary");
        gotGlossary = deleteGlossary(guid);
        FVTUtils.validateNode(gotGlossary);
        System.out.println("restore the glossary");
        gotGlossary = restoreGlossary(guid);
        FVTUtils.validateNode(gotGlossary);
        System.out.println("Delete the glossary again");
        gotGlossary = deleteGlossary(guid);
        FVTUtils.validateNode(gotGlossary);
        //TODO - delete a deletedGlossary should fail
        System.out.println("Purge a glossary");
        purgeGlossary(guid);
        System.out.println("Create glossary with the same name as a deleted one");
        glossary = createGlossary(serverName + " " + DEFAULT_TEST_GLOSSARY_NAME);
        FVTUtils.validateNode(glossary);

        System.out.println("create glossaries to find");
        Glossary glossaryForFind1 = getGlossaryForInput("abc");
        glossaryForFind1.setQualifiedName("yyy");
        glossaryForFind1 = issueCreateGlossary(glossaryForFind1);
        FVTUtils.validateNode(glossaryForFind1);
        Glossary glossaryForFind2 = createGlossary("yyy");
        FVTUtils.validateNode(glossaryForFind2);
        Glossary glossaryForFind3 = createGlossary("zzz");
        FVTUtils.validateNode(glossaryForFind3);
        Glossary glossaryForFind4 = createGlossary("This is a Glossary with spaces in name");
        FVTUtils.validateNode(glossaryForFind4);

        List<Glossary> results = findGlossaries("zzz");
        if (results.size() !=1 ) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected 1 back on the find got " +results.size(), "", "");
        }
        results = findGlossaries("yyy");
        if (results.size() !=2 ) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected 2 back on the find got " +results.size(), "", "");
        }
        //soft delete a glossary and check it is not found
        Glossary deleted4 = deleteGlossary(glossaryForFind2.getSystemAttributes().getGUID());
        FVTUtils.validateNode(deleted4);
        results = findGlossaries("yyy");
        if (results.size() !=1 ) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected 1 back on the find got " +results.size(), "", "");
        }

        // search for a glossary with a name with spaces in
        results = findGlossaries("This is a Glossary with spaces in name");
        if (results.size() !=1 ) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected 1 back on the find got " +results.size(), "", "");
        }

    }

    public  Glossary createGlossary(String glossaryName) throws SubjectAreaCheckedExceptionBase
    {
        Glossary glossary = getGlossaryForInput(glossaryName);
        return issueCreateGlossary(glossary);
    }

    private Glossary issueCreateGlossary(Glossary glossary) throws MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException, UnrecognizedGUIDException {
        Glossary newGlossary = subjectAreaGlossary.createGlossary(serverName,FVTConstants.USERID, glossary);
        if (newGlossary != null)
        {
            System.out.println("Created Glossary " + newGlossary.getName() + " with guid " + newGlossary.getSystemAttributes().getGUID());
        }
        return newGlossary;
    }

    private Glossary getGlossaryForInput(String glossaryName) {
        Glossary glossary = new Glossary();
        glossary.setName(glossaryName);
        return glossary;
    }
    public  Glossary createPastToGlossary(String name) throws SubjectAreaCheckedExceptionBase
    {
        Glossary glossary = new Glossary();
        glossary.setName(name);
        long now = new Date().getTime();
        // expire the glossary 10 milliseconds ago
        glossary.setEffectiveToTime(new Date(now-10));
        Glossary newGlossary  = subjectAreaGlossary.createGlossary(serverName,FVTConstants.USERID, glossary);
        FVTUtils.validateNode(newGlossary);
        System.out.println("Created Glossary " + newGlossary.getName() + " with guid " + newGlossary.getSystemAttributes().getGUID());

        return newGlossary;
    }
    public  Glossary createPastFromGlossary(String name) throws SubjectAreaCheckedExceptionBase
    {
        Glossary glossary = new Glossary();
        glossary.setName(name);
        long now = new Date().getTime();
        // expire the glossary 10 milliseconds ago
        glossary.setEffectiveFromTime(new Date(now-10));
       return  subjectAreaGlossary.createGlossary(serverName,FVTConstants.USERID, glossary);
    }
    public  Glossary createInvalidEffectiveDateGlossary(String name) throws SubjectAreaCheckedExceptionBase
    {
        Glossary glossary = new Glossary();
        glossary.setName(name);
        long now = new Date().getTime();
        // expire the glossary 10 milliseconds ago
        glossary.setEffectiveFromTime(new Date(now - 10));
        glossary.setEffectiveToTime(new Date(now - 11));
        return  subjectAreaGlossary.createGlossary(serverName, FVTConstants.USERID, glossary);
    }

    public  Glossary createFutureGlossary(String name) throws SubjectAreaCheckedExceptionBase
    {
        Glossary glossary = new Glossary();
        glossary.setName(name);
        long now = new Date().getTime();
        // make the glossary effective in a days time for day
        glossary.setEffectiveFromTime(new Date(now+1000*60*60*24));
        glossary.setEffectiveToTime(new Date(now+2000*60*60*24));
        Glossary newGlossary  = subjectAreaGlossary.createGlossary(serverName,FVTConstants.USERID, glossary);
        FVTUtils.validateNode(newGlossary);
        System.out.println("Created Glossary " + newGlossary.getName() + " with guid " + newGlossary.getSystemAttributes().getGUID());
        return newGlossary;
    }
    public List<Glossary> findGlossaries(String criteria) throws SubjectAreaCheckedExceptionBase
    {
        List<Glossary> glossaries = subjectAreaGlossary.findGlossary(
                serverName,
                FVTConstants.USERID,
                criteria,
                null,
                0,
                0,
                null,
                null);
        return glossaries;
    }

    public  Glossary getGlossaryByGUID(String guid) throws SubjectAreaCheckedExceptionBase {
        Glossary glossary = subjectAreaGlossary.getGlossaryByGuid(serverName, FVTConstants.USERID, guid);
        FVTUtils.validateNode(glossary);
        System.out.println("Got Glossary " + glossary.getName() + " with guid " + glossary.getSystemAttributes().getGUID() + " and status " + glossary.getSystemAttributes().getStatus());

        return glossary;
    }
    public  Glossary updateGlossary(String guid, Glossary glossary) throws SubjectAreaCheckedExceptionBase
    {
        Glossary updatedGlossary = subjectAreaGlossary.updateGlossary(serverName,FVTConstants.USERID, guid, glossary);
        FVTUtils.validateNode(updatedGlossary);
        System.out.println("Updated Glossary name to " + updatedGlossary.getName());
        return updatedGlossary;
    }

    public Glossary deleteGlossary(String guid) throws SubjectAreaCheckedExceptionBase
    {
        Glossary deletedGlossary = subjectAreaGlossary.deleteGlossary(serverName,FVTConstants.USERID, guid);
        FVTUtils.validateNode(deletedGlossary);
        System.out.println("Deleted Glossary name is " + deletedGlossary.getName());
        return deletedGlossary;
    }
    public Glossary restoreGlossary(String guid) throws SubjectAreaCheckedExceptionBase
    {
        Glossary restoredGlossary = subjectAreaGlossary.restoreGlossary(serverName,FVTConstants.USERID, guid);
        FVTUtils.validateNode(restoredGlossary);
        System.out.println("Restored Glossary name is " + restoredGlossary.getName());
        return restoredGlossary;
    }

    public  void purgeGlossary(String guid) throws SubjectAreaCheckedExceptionBase
    {
        subjectAreaGlossary.purgeGlossary(serverName,FVTConstants.USERID, guid);
        System.out.println("Purge succeeded");
    }
    public List<Line> getGlossaryRelationships(Glossary glossary) throws UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, FunctionNotSupportedException, MetadataServerUncontactableException {
        return subjectAreaGlossary.getGlossaryRelationships(serverName,FVTConstants.USERID,
                glossary.getSystemAttributes().getGUID(),
                null,
                0,
                0,
                null,
                null);
    }
}
