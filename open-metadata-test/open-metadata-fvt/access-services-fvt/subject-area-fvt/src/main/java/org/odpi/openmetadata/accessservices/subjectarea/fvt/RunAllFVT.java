/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * FVT resource to call subject area term client API FVT resources
 */
public class RunAllFVT
{

    public static final String DEFAULT_URL = "http://localhost:8080/open-metadata/access-services/subject-area";

    public static void main(String args[])
    {
        SubjectArea subjectArea = null;
        try
        {
            String url = RunAllFVT.getUrl(args);
            String[] argsForSamples = new String[1];
            argsForSamples[0] = url;
            GlossaryFVT.main(argsForSamples);
            TermFVT.main(argsForSamples);
            CategoryFVT.main(argsForSamples);
            CategoryHierarchyFVT.main(argsForSamples);
            System.out.println("Samples all run");
        } catch (IOException e)
        {
            System.out.println("Error getting user input");
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
            System.out.print("Enter a URL. Press enter to get the default (" + DEFAULT_URL + ".)):");
            url = br.readLine();
            if (url.equals(""))
            {
                url = DEFAULT_URL;
            }
        }
        return url;
    }
}
