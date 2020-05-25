/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * FVT resource to call subject area term client API FVT resources on 2 in memory servers names Server1 and
 * Server2.
 */
public class RunAllFVT
{

    public static void main(String args[])
    {
        try
        {
            String url = RunAllFVT.getUrl(args);
            GlossaryFVT.runWith2Servers(url);
            TermFVT.runWith2Servers(url);
            CategoryFVT.runWith2Servers(url);
            CategoryHierarchyFVT.runWith2Servers(url);
            RelationshipsFVT.runWith2Servers(url);
            SubjectAreaDefinitionCategoryFVT.runWith2Servers(url);
            ProjectFVT.runWith2Servers(url);
            //EffectiveDatesFVT.runWith2Servers(url);
            GraphFVT.runWith2Servers(url);
            System.out.println("FVT ran successfully");
        } catch (IOException e1)
        {
            System.out.println("Error getting user input");
        } catch (SubjectAreaCheckedException e)
        {
            System.out.println("ERROR: " + e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        } catch (SubjectAreaFVTCheckedException e) {
            System.out.println("ERROR: " + e.getMessage() );
        }
    }

    /**
     * This method gets the url that the tests will use to issue calls to the server.
     * <p>
     * If arguments are supplied then the first parameter is used as a url.
     * <p>
     * If no url is supplied then prompt the user to enter a valid url, enter means to use the default url.
     *
     * @param args arguments supplied
     * @return the url to use on the calls to the server
     * @throws IOException IO exception occured while getting input from the user.
     */
    public static String getUrl(String args[]) throws IOException
    {
        String url = null;
        if (args.length > 0)
        {
            url = args[0];
        } else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a URL. Press enter to get the default (" + FVTConstants.DEFAULT_URL + ".)):");
            url = br.readLine();
            if (url.equals(""))
            {
                url = FVTConstants.DEFAULT_URL;
            }
        }
        return url;
    }
}
