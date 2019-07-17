/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.samples;


import org.apache.log4j.varia.NullAppender;
import org.odpi.openmetadata.accessservices.assetowner.client.CSVFileAssetOwner;

/**
 * CreateCSVFileAssetSample creates a simple asset definition in the open metadata repositories for
 * a file.
 */
public class CreateCSVFileAssetSample
{
    private String  fileName;
    private String  serverName;
    private String  serverURLRoot;
    private String  clientUserId;


    /**
     * Set up the parameters for the sample.
     *
     * @param fileName name of the file
     * @param serverName server to call
     * @param serverURLRoot location of server
     * @param clientUserId userId to access the server
     */
    public CreateCSVFileAssetSample(String  fileName,
                                    String  serverName,
                                    String  serverURLRoot,
                                    String  clientUserId)
    {
        this.fileName = fileName;
        this.serverName = serverName;
        this.serverURLRoot = serverURLRoot;
        this.clientUserId = clientUserId;
    }


    /**
     * This runs the sample
     */
    void run()
    {
        try
        {
            CSVFileAssetOwner client = new CSVFileAssetOwner(serverName, serverURLRoot);

            client.addCSVFileToCatalog(clientUserId,
                                       fileName,
                                       "This is a new file asset created by the CreateCSVFileAssetSample.",
                                       fileName);
        }
        catch (Throwable error)
        {
            System.out.println("The asset metadata can not be created.  Error message is: " + error.getMessage());
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
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());

        if ((args == null) || (args.length < 1))
        {
            System.out.println("Please specify the file name in the first parameter");
            System.exit(-1);
        }

        String  fileName = args[0];
        String  serverName = "cocoMDS1";
        String  serverURLRoot = "http://localhost:8080";
        String  clientUserId = "garygeeke";


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
        System.out.println("CSV File Asset Creation   ");
        System.out.println("===============================");
        System.out.println("Running against server: " + serverName + " at " + serverURLRoot);
        System.out.println("Using userId: " + clientUserId);
        System.out.println();

        try
        {
            CreateCSVFileAssetSample   sample = new CreateCSVFileAssetSample(fileName, serverName, serverURLRoot, clientUserId);

            sample.run();
        }
        catch (Throwable  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}
