/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.samples;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaCategory;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *  Category Sample provides a client program that calls the Subject Area OMAS to create sample Subject Area Catalog.
 */
public class SubjectAreaCatalogSample
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

    private static final String GLOSSARY_NAME        = "Coco Pharmaceuticals Subject Area Glossary";
    private static final String GLOSSARY_DESCRIPTION = "Coco Pharmaceuticals Core Subject Areas for Personalized Medicine.";
    private static final String DEFAULT_SERVER_NAME  = "cocoMDS1";
    private static final String DEFAULT_USERID       = "erinoverview";
    private static final String DEFAULT_URL          = "http://localhost:8080";

    private String              serverURLRoot;
    private String              clientUserId;
    private String              serverName;
    private SubjectAreaGlossary subjectAreaGlossary  =null;
    private SubjectAreaCategory subjectAreaCategory  =null;


    /**
     * Constructor
     *
     * @param serverURLRoot server platform to connect to.
     * @param serverName server to connect to.
     * @param clientUserId user id to use to access metadata.
     */
    private SubjectAreaCatalogSample(String serverURLRoot, String serverName, String clientUserId)
    {
        this.serverURLRoot = serverURLRoot;
        this.serverName = serverName;
        this.clientUserId = clientUserId;
    }

    /**
     * Run the sample
     * @throws SubjectAreaCheckedExceptionBase error
     */
    private void run() throws SubjectAreaCheckedExceptionBase
    {
        SubjectArea subjectArea = new SubjectAreaImpl(this.serverName, this.serverURLRoot);
        subjectAreaGlossary = subjectArea.getSubjectAreaGlossary();
        subjectAreaCategory = subjectArea.getSubjectAreaCategory();

        System.out.println("----------------------------");
        System.out.println("Creating the Coco Pharmaceutical Glossary for Subject Area Definitions : ");

        Glossary glossary = createGlossary(clientUserId, GLOSSARY_NAME, GLOSSARY_DESCRIPTION);

        /*
         * create Organisation subject areas
         */
        String glossaryGuid =  glossary.getSystemAttributes().getGUID();
        Category organisation = createTopCategory(ORGANIZATION,glossaryGuid);
        Category hospital= createChildCategory(HOSPITAL,organisation,glossaryGuid);
        Category supplier= createChildCategory(SUPPLIER,organisation,glossaryGuid);

        /*
         * create Person subject areas
         */
        Category person = createTopCategory(PERSON,glossaryGuid);
        Category patient= createChildCategory(PATIENT,person,glossaryGuid);
        Category clinician= createChildCategory(CLINICIAN,person,glossaryGuid);
        Category employee= createChildCategory(EMPLOYEE,person,glossaryGuid);
        Category collaborator= createChildCategory(COLLABORATOR,person,glossaryGuid);

        /*
         * create Clinical subject areas
         */
        Category clinical = createTopCategory(CLINICAL,glossaryGuid);
        Category symptom= createChildCategory(SYMPTOM,clinical,glossaryGuid);
        Category measurement= createChildCategory(MEASUREMENT,clinical,glossaryGuid);
        Category prescription= createChildCategory(PRESCRIPTION,clinical,glossaryGuid);
        Category outcome= createChildCategory(OUTCOME,clinical,glossaryGuid);

        /*
         * create Treatment subject areas
         */
        Category treatment = createTopCategory(TREATMENT,glossaryGuid);
        Category product= createChildCategory(PRODUCT,treatment,glossaryGuid);
        Category order= createChildCategory(ORDER,treatment,glossaryGuid);
        Category recipe= createChildCategory(RECIPE,treatment,glossaryGuid);

        /*
         * create Service Quality subject areas
         */
        Category serviceQuality = createTopCategory(SERVICE_QUALITY,glossaryGuid);
        Category contract= createChildCategory(CONTRACT,serviceQuality,glossaryGuid);
        Category stock= createChildCategory(STOCK,serviceQuality,glossaryGuid);
        Category distribution= createChildCategory(DISTRIBUTION,serviceQuality,glossaryGuid);
        Category invoice= createChildCategory(INVOICE,serviceQuality,glossaryGuid);
    }


    /**
     * Create a top level subject area definition - this means a Subject Area Category that does not have a superCategory.
     * @param name name of the Subject Area Category to create
     * @param glossaryGuid guid of the glossary to associate the Subject Area with.
     * @return Category the created Subject Area
     * @throws  SubjectAreaCheckedExceptionBase error
     */
    private Category createTopCategory( String name, String glossaryGuid)  throws SubjectAreaCheckedExceptionBase
    {
        System.out.println("----------------------------");
        System.out.println("Creating a top level Subject Area Category called " + name);
        Category subjectAreaDefinition = new Category();
        subjectAreaDefinition.setName(name);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        subjectAreaDefinition.setGlossary(glossarySummary);
        return subjectAreaCategory.createCategory(clientUserId, subjectAreaDefinition);
    }


    /**
     * Create a child Subject Area Definition - this means a Subject Area Definition that has a superCategory.
     * @param name name of the Subject Area Category to create
     * @param parent parent Category
     * @param glossaryGuid guid of the glossary to associate the Subject Area with.
     * @return Category the created Subject Area
     * @throws SubjectAreaCheckedExceptionBase error
     */
    private Category createChildCategory(String name, Category parent, String glossaryGuid) throws SubjectAreaCheckedExceptionBase
    {
        Category subjectAreaDefinition = new Category();
        subjectAreaDefinition.setName(name);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        subjectAreaDefinition.setGlossary(glossarySummary);
        CategorySummary parentCategorysummary = new CategorySummary();
        parentCategorysummary.setGuid(parent.getSystemAttributes().getGUID());
        subjectAreaDefinition.setParentCategory(parentCategorysummary);
        Category newCategory = subjectAreaCategory.createCategory(clientUserId,
                subjectAreaDefinition);
        if (newCategory != null)
        {
            System.out.println("Created Subject Area Category " + newCategory.getName() + " with guid " + newCategory.getSystemAttributes().getGUID() + ", parent SubjectArea Category is " + parent.getName());
        }
        return newCategory;
    }


    /**
     * Create a Glossary
     *
     * @param userId user id
     * @param glossaryName name of the glossary to create
     * @param glossaryDescription description of the Glossary
     * @return created glossary
     * @throws SubjectAreaCheckedExceptionBase error
     */
    private Glossary createGlossary(String userId, String glossaryName, String glossaryDescription) throws SubjectAreaCheckedExceptionBase
    {
        Glossary glossary = new Glossary();
        glossary.setName(glossaryName);
        glossary.setDescription(glossaryDescription);
        return subjectAreaGlossary.createGlossary(userId, glossary);
    }


    /**
     * The main program takes the URL root for the server.
     *
     * @param args URL root for the OMAG server platform; OMAG server name; userId
     * @throws IOException IO Exception
     */
    public static void main(String[] args) throws IOException
    {
        String  serverURLRoot = SubjectAreaCatalogSample.getUrl(args);
        String  serverName = SubjectAreaCatalogSample.getServerName(args);
        String  clientUserId = SubjectAreaCatalogSample.getUserId(args);

        System.out.println("===============================");
        System.out.println("Subject Area Catalog Sample ");
        System.out.println("===============================");
        System.out.println("Running against OMAG server platform: " + serverURLRoot);
        System.out.println("OMAG server platform: " + serverName);
        System.out.println("Using userId: " + clientUserId);
        try
        {
            SubjectAreaCatalogSample sample = new SubjectAreaCatalogSample(serverURLRoot,
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
     * If arguments are supplied then the first parameter is used as a url.
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
