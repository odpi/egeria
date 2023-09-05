/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.samples.definition;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.http.HttpHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *  SubjectAreaDefinition Sample provides a client program that calls the Subject Area OMAS to create a category hierarchy
 *  that matches the subject area definitions defined by Coco Pharmaceuticals.
 */
public class SubjectAreaDefinitionSample
{
    /*
     * Subject areas for Coco Pharmaceuticals sample.
     */
    private static final String ORGANIZATION = "Organization";
    private static final String   HOSPITAL = "Hospital";
    private static final String   SUPPLIER = "Supplier";
    private static final String PERSON = "Person";
    private static final String   PATIENT = "Patient";
    private static final String   CLINICIAN = "Clinician";
    private static final String   EMPLOYEE = "Employee";
    private static final String   COLLABORATOR = "Collaborator";
    private static final String CLINICAL = "Clinical";
    private static final String   SYMPTOM = "Symptom";
    private static final String   MEASUREMENT = "Measurement";
    private static final String   PRESCRIPTION = "Prescription";
    private static final String   OUTCOME = "Outcome";
    private static final String TREATMENT = "Treatment";
    private static final String   PRODUCT = "Product";
    private static final String   ORDER = "Order";
    private static final String   RECIPE = "Recipe";
    private static final String SERVICE_QUALITY = "Service Quality";
    private static final String   CONTRACT = "Contract";
    private static final String   STOCK = "Stock";
    private static final String   DISTRIBUTION = "Distribution";
    private static final String   INVOICE = "Invoice";

    private static final String GLOSSARY_NAME        = "Coco Pharmaceuticals Common Data Definitions";
    private static final String GLOSSARY_DESCRIPTION = "Coco Pharmaceuticals semantic definitions for personalized medicine.";
    private static final String DEFAULT_SERVER_NAME  = "simple-metadata-store";
    private static final String DEFAULT_USERID       = "erinoverview";
    private static final String DEFAULT_URL          = "https://localhost:9443";

    private final String              serverURLRoot;
    private final String              clientUserId;
    private final String              serverName;
    private SubjectAreaNodeClient<Glossary>              subjectAreaGlossary  = null;
    private SubjectAreaNodeClient<SubjectAreaDefinition> subjectAreaCategory  =null;


    /**
     * Constructor
     *
     * @param serverURLRoot server platform to connect to.
     * @param serverName server to connect to.
     * @param clientUserId user id to use to access metadata.
     */
    private SubjectAreaDefinitionSample(String serverURLRoot, String serverName, String clientUserId)
    {
        this.serverURLRoot = serverURLRoot;
        this.serverName = serverName;
        this.clientUserId = clientUserId;
    }

    /**
     * Run the sample
     * @throws SubjectAreaCheckedException error
     */
    private void run() throws SubjectAreaCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        SubjectArea subjectArea = new SubjectAreaImpl(this.serverName, this.serverURLRoot);
        subjectAreaGlossary = subjectArea.nodeClients().glossaries();
        subjectAreaCategory = subjectArea.nodeClients().categories();

        System.out.println("----------------------------");
        System.out.println("Creating the Coco Pharmaceutical Glossary for Subject Area Definitions : ");

        Glossary glossary = createGlossary(clientUserId, GLOSSARY_NAME, GLOSSARY_DESCRIPTION);

        /*
         * create Organisation subject areas
         */
        String glossaryGuid =  glossary.getSystemAttributes().getGUID();
        SubjectAreaDefinition organisation = createTopSubjectAreaDefinition(ORGANIZATION,glossaryGuid);
        createChildSubjectAreaDefinition(HOSPITAL,organisation,glossaryGuid);
        createChildSubjectAreaDefinition(SUPPLIER,organisation,glossaryGuid);

        /*
         * create Person subject areas
         */
        SubjectAreaDefinition person = createTopSubjectAreaDefinition(PERSON,glossaryGuid);
        createChildSubjectAreaDefinition(PATIENT,person,glossaryGuid);
        createChildSubjectAreaDefinition(CLINICIAN,person,glossaryGuid);
        createChildSubjectAreaDefinition(EMPLOYEE,person,glossaryGuid);
        createChildSubjectAreaDefinition(COLLABORATOR,person,glossaryGuid);

        /*
         * create Clinical subject areas
         */
        SubjectAreaDefinition clinical = createTopSubjectAreaDefinition(CLINICAL,glossaryGuid);
        createChildSubjectAreaDefinition(SYMPTOM,clinical,glossaryGuid);
        createChildSubjectAreaDefinition(MEASUREMENT,clinical,glossaryGuid);
        createChildSubjectAreaDefinition(PRESCRIPTION,clinical,glossaryGuid);
        createChildSubjectAreaDefinition(OUTCOME,clinical,glossaryGuid);

        /*
         * create Treatment subject areas
         */
        SubjectAreaDefinition treatment = createTopSubjectAreaDefinition(TREATMENT,glossaryGuid);
        createChildSubjectAreaDefinition(PRODUCT,treatment,glossaryGuid);
        createChildSubjectAreaDefinition(ORDER,treatment,glossaryGuid);
        createChildSubjectAreaDefinition(RECIPE,treatment,glossaryGuid);

        /*
         * create Service Quality subject areas
         */
        SubjectAreaDefinition serviceQuality = createTopSubjectAreaDefinition(SERVICE_QUALITY,glossaryGuid);
        createChildSubjectAreaDefinition(CONTRACT,serviceQuality,glossaryGuid);
        createChildSubjectAreaDefinition(STOCK,serviceQuality,glossaryGuid);
        createChildSubjectAreaDefinition(DISTRIBUTION,serviceQuality,glossaryGuid);
        createChildSubjectAreaDefinition(INVOICE,serviceQuality,glossaryGuid);
    }


    /**
     * Create a top level subject area definition - this means a Subject Area Definition that does not have a superCategory.
     * @param name name of the Subject Area Definition to create
     * @param glossaryGuid guid of the glossary to associate the Subject Area with.
     * @return SubjectAreaDefinition the created Subject Area Definition
     */
    private SubjectAreaDefinition createTopSubjectAreaDefinition( String name, String glossaryGuid) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        System.out.println("----------------------------");
        System.out.println("Creating a top level Subject Area Definition called " + name);
        SubjectAreaDefinition subjectAreaDefinition = new SubjectAreaDefinition();
        subjectAreaDefinition.setName(name);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        subjectAreaDefinition.setGlossary(glossarySummary);
        return subjectAreaCategory.create(clientUserId, subjectAreaDefinition);
    }


    /**
     * Create a child Subject Area Definition - this means a Subject Area Definition that has a superCategory.
     *
     * @param name         name of the Subject Area Definition to create
     * @param parent       parent Category
     * @param glossaryGuid guid of the glossary to associate the Subject Area with.
     */
    private void createChildSubjectAreaDefinition(String                name,
                                                  SubjectAreaDefinition parent,
                                                  String                glossaryGuid) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        SubjectAreaDefinition subjectAreaDefinition = new SubjectAreaDefinition();
        subjectAreaDefinition.setName(name);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        subjectAreaDefinition.setGlossary(glossarySummary);
        CategorySummary parentCategorysummary = new CategorySummary();
        parentCategorysummary.setGuid(parent.getSystemAttributes().getGUID());
        subjectAreaDefinition.setParentCategory(parentCategorysummary);
        SubjectAreaDefinition newSubjectAreaDefinition = subjectAreaCategory.create(clientUserId, subjectAreaDefinition);
        if (newSubjectAreaDefinition != null)
        {
            System.out.println("Created Subject Area Definition " + newSubjectAreaDefinition.getName() + " with guid " + newSubjectAreaDefinition.getSystemAttributes().getGUID() + ", parent SubjectArea Definition is " + parent.getName());
        }
    }


    /**
     * Create a Glossary
     *
     * @param userId user id
     * @param glossaryName name of the glossary to create
     * @param glossaryDescription description of the Glossary
     * @return created glossary
     */
    private Glossary createGlossary(String userId,
                                    String glossaryName,
                                    String glossaryDescription) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        Glossary glossary = new Glossary();
        glossary.setName(glossaryName);
        glossary.setDescription(glossaryDescription);
        return subjectAreaGlossary.create(userId, glossary);
    }


    /**
     * The main program takes the URL root for the server.
     *
     * @param args URL root for the OMAG server platform; OMAG server name; userId
     * @throws IOException IO Exception
     */
    public static void main(String[] args) throws IOException
    {
        String  serverURLRoot = SubjectAreaDefinitionSample.getUrl(args);
        String  serverName = SubjectAreaDefinitionSample.getServerName(args);
        String  clientUserId = SubjectAreaDefinitionSample.getUserId(args);

        System.out.println("===============================");
        System.out.println("Subject Area Definition Sample ");
        System.out.println("===============================");
        System.out.println("Running against OMAG server platform: " + serverURLRoot);
        System.out.println("OMAG server platform: " + serverName);
        System.out.println("Using userId: " + clientUserId);

        HttpHelper.noStrictSSL();

        try
        {
            SubjectAreaDefinitionSample  sample = new SubjectAreaDefinitionSample(serverURLRoot,
                                                                                  serverName,
                                                                                  clientUserId);

            sample.run();
        }
        catch (Throwable  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }


    /**
     * This method gets the url that the sample will use to issue calls to the server.
     * <p>
     * If arguments are supplied then the first parameter is used as the url.
     * <p>
     * If no url is supplied then prompt the user to enter a valid url, enter means to use the default url.
     *
     * @param args arguments supplied
     * @return the url to use on the calls to the server
     * @throws IOException IO exception occurred while getting input from the user.
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
            System.out.print("Enter the OMAG Server platform URL. Press enter to get the default (" + DEFAULT_URL + ")):");
            url = br.readLine();
            if (url.equals(""))
            {
                url = DEFAULT_URL;
            }

        }
        return url;
    }


    /**
     * This method gets the server name that the sample will use to issue calls to the server.
     * <p>
     * If arguments are supplied then the second parameter is used as a server name.
     * <p>
     * If no name is supplied then prompt the user to enter a valid server name, enter means to use the default server name.
     *
     * @param args arguments supplied
     * @return the url to use on the calls to the server
     * @throws IOException IO exception occurred while getting input from the user.
     */
    public static String getServerName(String args[]) throws IOException
    {
        String name = null;
        if (args.length > 1)
        {
            name = args[1];
        } else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a OMAG Server Name. Press enter to get the default (" + DEFAULT_SERVER_NAME + ")):");
            name = br.readLine();
            if (name.equals(""))
            {
                name = DEFAULT_SERVER_NAME;
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
        if (args.length > 1)
        {
            userId = args[1];
        } else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a userId. Press enter to get the default (" + DEFAULT_USERID + ")):");
            userId = br.readLine();
            if (userId.equals(""))
            {
                userId = DEFAULT_USERID;
            }

        }
        return userId;
    }

}
