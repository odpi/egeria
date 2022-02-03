/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.samples.subjectareas;

import org.odpi.openmetadata.accessservices.governanceprogram.client.SubjectAreaManager;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.SubjectAreaProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.http.HttpHelper;

import java.util.HashMap;
import java.util.Map;


/**
 * CreateSubjectAreasSample illustrates the use of the Governance Program OMAS API to create subject areas
 * for Coco Pharmaceuticals.
 */
public class CreateSubjectAreasSample
{
    private String  serverName;
    private String  serverURLRoot;
    private String  clientUserId;

    private Map<String, String> subjectAreaMap = new HashMap<>();

    private SubjectAreaManager client = null;

    /**
     * Set up the parameters for the sample.
     *
     * @param serverName server to call
     * @param serverURLRoot location of server
     * @param clientUserId userId to access the server
     */
    public CreateSubjectAreasSample(String  serverName,
                                    String  serverURLRoot,
                                    String  clientUserId)
    {
        this.serverName = serverName;
        this.serverURLRoot = serverURLRoot;
        this.clientUserId = clientUserId;
    }


    /**
     * Set up a new subject area definition
     *
     * @param subjectAreaDefinitionName qualified name
     * @param parentName optional parent subject area
     * @param displayName display name
     * @param description longer description
     * @param scope how broadly is this used
     * @param usage how is this subject area used
     * @param domainIdentifier which governance domain does this subject area belong to
     * @throws InvalidParameterException bad parameters passed to client
     * @throws UserNotAuthorizedException userId is not allowed to create zones
     * @throws PropertyServerException service is not running - or is in trouble
     */
    private void createSubjectAreaDefinition(String     subjectAreaDefinitionName,
                                             String     parentName,
                                             String     displayName,
                                             String     description,
                                             String     scope,
                                             String     usage,
                                             int        domainIdentifier) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        System.out.println("------------------------------------------------------------------------");
        System.out.println(subjectAreaDefinitionName);
        System.out.println("------------------------------------------------------------------------");
        System.out.println(" ==> qualifiedName: " + subjectAreaDefinitionName);
        System.out.println(" ==> displayName:   " + displayName);
        System.out.println(" ==> description:   " + description);
        System.out.println(" ==> scope:         " + scope);
        System.out.println(" ==> usage:         " + usage);
        System.out.println(" ==> domain:        " + domainIdentifier);
        System.out.println(" ");

        SubjectAreaProperties properties = new SubjectAreaProperties();

        properties.setQualifiedName(subjectAreaDefinitionName);
        properties.setDisplayName(displayName);
        properties.setDescription(description);
        properties.setScope(scope);
        properties.setUsage(usage);
        properties.setDomainIdentifier(domainIdentifier);

        String subjectAreaGUID = client.createSubjectArea(clientUserId, properties);

        if (parentName != null)
        {
            client.linkSubjectAreasInHierarchy(clientUserId, subjectAreaMap.get(parentName), subjectAreaGUID);
        }

        subjectAreaMap.put(subjectAreaDefinitionName, subjectAreaGUID);
    }


    /**
     * This runs the sample
     */
    public void run()
    {
        try
        {
            client = new SubjectAreaManager(serverName, serverURLRoot);

            SubjectAreaSampleDefinitions[] subjectAreaDefinitions = SubjectAreaSampleDefinitions.values();

            for (SubjectAreaSampleDefinitions definition : subjectAreaDefinitions)
            {
                createSubjectAreaDefinition(definition.getSubjectAreaName(),
                                            definition.getParentName(),
                                            definition.getDisplayName(),
                                            definition.getDescription(),
                                            definition.getScope(),
                                            definition.getUsage(),
                                            definition.getDomain());
            }


        }
        catch (Exception error)
        {
            System.out.println("There was an exception when calling the SubjectAreaManager client.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Main program that controls the operation of the sample.  The parameters are passed space separated.
     * The file name must be passed as parameter 1.  The other parameters are used to override the
     * sample's default values.
     *
     * @param args 1. file name 2. server name, 3. URL root for the server, 4. client userId
     */
    public static void main(String[] args)
    {
        String  serverName = "cocoMDS2";
        String  serverURLRoot = "https://localhost:9443";
        String  clientUserId = "erinoverview";


        if (args.length > 1)
        {
            serverName = args[1];
        }

        if (args.length > 2)
        {
            serverURLRoot = args[2];
        }

        if (args.length > 3)
        {
            clientUserId = args[3];
        }

        System.out.println("===============================");
        System.out.println("Create Subject Area Definitions Sample   ");
        System.out.println("===============================");
        System.out.println("Running against server: " + serverName + " at " + serverURLRoot);
        System.out.println("Using userId: " + clientUserId);
        System.out.println();

        HttpHelper.noStrictSSLIfConfigured();


        try
        {
            CreateSubjectAreasSample sample = new CreateSubjectAreasSample(serverName, serverURLRoot, clientUserId);

            sample.run();
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}
