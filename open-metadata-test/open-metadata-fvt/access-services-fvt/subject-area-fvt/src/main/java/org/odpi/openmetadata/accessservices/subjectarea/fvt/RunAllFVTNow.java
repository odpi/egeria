/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * FVT resource to call subject area term client API FVT resources
 *
 * This runs the FVT without the effectivity times. This FVT allows serverName and userId to be input,
 * to allow the FVT to run on any server with any userid.
 */
public class RunAllFVTNow
{
    public static void main(String args[])
    {
        try
        {
            String url = RunAllFVTNow.getUrl(args);
            String serverName = getServerName(args);
            String userId = getUserId(args);

            GlossaryFVT.runIt(url,serverName,userId);
            TermFVT.runIt(url,serverName,userId);
            CategoryFVT.runIt(url,serverName,userId);
            CategoryHierarchyFVT.runIt(url,serverName,userId);
            RelationshipsFVT.runIt(url,serverName,userId);
            SubjectAreaDefinitionCategoryFVT.runIt(url,serverName,userId);
            GraphFVT.runIt(url,serverName,userId);
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
    public static String getServerName(String args[]) throws IOException
    {
        String name = null;
        if (args.length > 1)
        {
            name = args[1];
        } else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a OMAG Server Name. Press enter to get the default (" + FVTConstants.SERVER_NAME1 + ")) :");
            name = br.readLine();
            if (name.equals(""))
            {
                name = FVTConstants.SERVER_NAME1;
            }

        }
        return name;
    }
    /**
     * This method gets the userId that the sample will use to issue calls to the server.
     * <p>
     * If arguments are supplied then the third parameter is used as a userId.
     * <p>
     * If no userId is supplied then prompt the user to enter a valid userId, enter means to use the default userId.
     *
     * @param args arguments supplied
     * @return the url to use on the calls to the server
     * @throws IOException IO exception occurred while getting input from the user.
     */
    public static String getUserId(String args[]) throws IOException
    {
        String userId = null;
        if (args.length > 2)
        {
            userId = args[2];
        } else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a userId. Press enter to get the default (" + FVTConstants.USERID + ")) :");
            userId = br.readLine();
            if (userId.equals(""))
            {
                userId = FVTConstants.USERID;
            }

        }
        return userId;
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
            System.out.print("Enter a URL. Press enter to get the default (" + FVTConstants.DEFAULT_URL + ".)) :");
            url = br.readLine();
            if (url.equals(""))
            {
                url = FVTConstants.DEFAULT_URL;
            }
        }
        return url;
    }
}
