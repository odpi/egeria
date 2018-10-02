/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.samples;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Sample to call subject area term client API
 */
public class RunAllSamples {

    public static final String DEFAULT_URL = "http://localhost:8080/open-metadata/access-services/subject-area";

    public static void main(String args[]) {
        SubjectArea subjectArea = null;
        try {
            String url =RunAllSamples.getUrl(args);
            String[] argsForSamples = new String[1];
            argsForSamples[0] = url;
            GlossarySample.main(argsForSamples);
            TermSample.main(argsForSamples);
            CategorySample.main(argsForSamples);
            CategoryHierarchySample.main(argsForSamples);
            System.out.println("Samples all run");
        } catch (IOException e) {
            System.out.println("Error getting user input");
        }

    }

    /**
     * This method gets the url that the samples will use to issue calles to the server.
     * <p>
     * If arguments are supplied then the first parameter is used as a url.
     * <p>
     * If no url is supplied then prompt the user to enter a valid url, enter means to use the default url.
     *
     * @param args arguments supplied
     * @return the url to use on the calls to the server
     * @throws IOException IO exception occured while getting input from the user.
     */
    public static String getUrl(String args[]) throws IOException {
        String url =null;
        if (args.length>0) {
            url = args[0];
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a URL. Press enter to get the default ("+ DEFAULT_URL+ ".)):");
            url = br.readLine();
            if (url.equals("")) {
                url = DEFAULT_URL;
            }
        }
        return url;
    }
}
