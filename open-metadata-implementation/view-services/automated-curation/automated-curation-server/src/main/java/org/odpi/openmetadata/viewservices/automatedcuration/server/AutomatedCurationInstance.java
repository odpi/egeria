/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.automatedcuration.server;


import org.odpi.openmetadata.accessservices.assetowner.client.*;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

/**
 * AutomatedCurationInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class AutomatedCurationInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.AUTOMATED_CURATION;

    private final AssetCertificationManager assetCertificationManager;
    private final AssetLicenseManager       assetLicenseManager;
    private final ExternalReferenceManager  externalReferenceManager;
    private final ValidValuesAssetOwner     validValuesAssetOwner;
    private final FileSystemAssetOwner      fileSystemAssetOwner;
    private final CSVFileAssetOwner         csvFileAssetOwner;
    private final AvroFileAssetOwner        avroFileAssetOwner;
    private final OpenMetadataStoreClient   openMetadataStoreClient;
    private final OpenGovernanceClient      openGovernanceClient;
    private final ConnectedAssetClient      connectedAssetClient;

    /**
     * Set up the Automated Curation OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public AutomatedCurationInstance(String       serverName,
                                     AuditLog     auditLog,
                                     String       localServerUserId,
                                     int          maxPageSize,
                                     String       remoteServerName,
                                     String       remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        assetCertificationManager = new AssetCertificationManager(remoteServerName, remoteServerURL);
        assetLicenseManager       = new AssetLicenseManager(remoteServerName, remoteServerURL);
        externalReferenceManager  = new ExternalReferenceManager(remoteServerName, remoteServerURL);
        validValuesAssetOwner     = new ValidValuesAssetOwner(remoteServerName, remoteServerURL);
        fileSystemAssetOwner      = new FileSystemAssetOwner(remoteServerName, remoteServerURL, auditLog);
        csvFileAssetOwner         = new CSVFileAssetOwner(remoteServerName, remoteServerURL, auditLog);
        avroFileAssetOwner        = new AvroFileAssetOwner(remoteServerName, remoteServerURL, auditLog);
        connectedAssetClient      = new ConnectedAssetClient(remoteServerName, remoteServerURL);
        openMetadataStoreClient   = new OpenMetadataStoreClient(remoteServerName, remoteServerURL);
        openGovernanceClient      = new OpenGovernanceClient(remoteServerName, remoteServerURL);
    }


    /**
     * Return the client.  This client is from Asset Owner OMAS and is for maintaining certifications on assets.
     *
     * @return client
     */
    public AssetCertificationManager getAssetCertificationManager()
    {
        return assetCertificationManager;
    }


    /**
     * Return the client.  This client is from Asset Owner OMAS and is for maintaining licenses on assets.
     *
     * @return client
     */
    public AssetLicenseManager getAssetLicenseManager()
    {
        return assetLicenseManager;
    }


    /**
     * Return the client.  This client is from Asset Owner OMAS and is for maintaining external references for assets.
     *
     * @return client
     */
    public ExternalReferenceManager getExternalReferenceManager()
    {
        return externalReferenceManager;
    }


    /**
     * Return the client.  This client is from Asset Owner OMAS and is for maintaining valid values for assets.
     *
     * @return client
     */
    public ValidValuesAssetOwner getValidValuesAssetOwner()
    {
        return validValuesAssetOwner;
    }


    /**
     * Return the client.  This client is from Asset Owner OMAS and is for maintaining information about file system assets.
     *
     * @return client
     */
    public FileSystemAssetOwner getFileSystemAssetOwner()
    {
        return fileSystemAssetOwner;
    }


    /**
     * Return the client.  This client is from Asset Owner OMAS and is for maintaining information about CSV Files.
     *
     * @return client
     */
    public CSVFileAssetOwner getCSVFileAssetOwner()
    {
        return csvFileAssetOwner;
    }


    /**
     * Return the client.  This client is from Asset Owner OMAS and is for maintaining information about Avro Files.
     *
     * @return client
     */
    public AvroFileAssetOwner getAvroFileAssetOwner()
    {
        return avroFileAssetOwner;
    }


    /**
     * Return the connected asset client.  This client is from Open Connector Framework (OCF) and is for retrieving information about
     * assets and creating connectors.
     *
     * @return client
     */
    public ConnectedAssetClient getConnectedAssetClient()
    {
        return connectedAssetClient;
    }


    /**
     * Return the open metadata store client.  This client is from the Governance Action Framework (GAF) and is for accessing and
     * maintaining all types of metadata.
     *
     * @return client
     */
    public OpenMetadataStoreClient getOpenMetadataStoreClient()
    {
        return openMetadataStoreClient;
    }


    /**
     * Return the open governance client.  This client is from the Governance Action Framework (GAF) and is for
     * working with automation services.
     *
     * @return client
     */
    public OpenGovernanceClient getOpenGovernanceClient()
    {
        return openGovernanceClient;
    }
}
