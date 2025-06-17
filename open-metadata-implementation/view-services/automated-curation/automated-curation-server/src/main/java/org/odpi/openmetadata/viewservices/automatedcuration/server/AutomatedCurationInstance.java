/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.automatedcuration.server;


import org.odpi.openmetadata.accessservices.assetowner.client.*;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.viewservices.automatedcuration.handlers.TechnologyTypeHandler;

/**
 * AutomatedCurationInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class AutomatedCurationInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.AUTOMATED_CURATION;

    private final AssetCertificationManager    assetCertificationManager;
    private final AssetLicenseManager          assetLicenseManager;
    private final ExternalReferenceManager     externalReferenceManager;
    private final ValidValuesAssetOwner        validValuesAssetOwner;
    private final FileSystemAssetOwner         fileSystemAssetOwner;
    private final CSVFileAssetOwner            csvFileAssetOwner;
    private final AvroFileAssetOwner           avroFileAssetOwner;
    private final OpenMetadataStoreClient      openMetadataStoreClient;
    private final OpenGovernanceClient         openGovernanceClient;
    private final OpenIntegrationServiceClient openIntegrationServiceClient;
    private final GovernanceConfigurationClient governanceConfigurationClient;
    private final ConnectedAssetClient         connectedAssetClient;
    private final TechnologyTypeHandler        technologyTypeHandler;

    /**
     * Set up the Automated Curation OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public AutomatedCurationInstance(String       serverName,
                                     AuditLog     auditLog,
                                     String       localServerUserId,
                                     String       localServerUserPassword,
                                     int          maxPageSize,
                                     String       remoteServerName,
                                     String       remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        if (localServerUserPassword == null)
        {
            assetCertificationManager     = new AssetCertificationManager(remoteServerName, remoteServerURL);
            assetLicenseManager           = new AssetLicenseManager(remoteServerName, remoteServerURL);
            externalReferenceManager      = new ExternalReferenceManager(remoteServerName, remoteServerURL);
            validValuesAssetOwner         = new ValidValuesAssetOwner(remoteServerName, remoteServerURL);
            fileSystemAssetOwner          = new FileSystemAssetOwner(remoteServerName, remoteServerURL, auditLog);
            csvFileAssetOwner             = new CSVFileAssetOwner(remoteServerName, remoteServerURL, auditLog);
            avroFileAssetOwner            = new AvroFileAssetOwner(remoteServerName, remoteServerURL, auditLog);
            connectedAssetClient          = new ConnectedAssetClient(remoteServerName, remoteServerURL);
            openMetadataStoreClient       = new OpenMetadataStoreClient(remoteServerName, remoteServerURL, maxPageSize);
            openGovernanceClient          = new OpenGovernanceClient(remoteServerName, remoteServerURL, maxPageSize);
            openIntegrationServiceClient  = new OpenIntegrationServiceClient(remoteServerName, remoteServerURL, maxPageSize);
            governanceConfigurationClient = new GovernanceConfigurationClient(remoteServerName, remoteServerURL, maxPageSize);
        }
        else
        {
            assetCertificationManager     = new AssetCertificationManager(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword);
            assetLicenseManager           = new AssetLicenseManager(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword);
            externalReferenceManager      = new ExternalReferenceManager(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword);
            validValuesAssetOwner         = new ValidValuesAssetOwner(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword);
            fileSystemAssetOwner          = new FileSystemAssetOwner(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, auditLog);
            csvFileAssetOwner             = new CSVFileAssetOwner(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, auditLog);
            avroFileAssetOwner            = new AvroFileAssetOwner(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, auditLog);
            connectedAssetClient          = new ConnectedAssetClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword);
            openMetadataStoreClient       = new OpenMetadataStoreClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
            openGovernanceClient          = new OpenGovernanceClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
            openIntegrationServiceClient  = new OpenIntegrationServiceClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
            governanceConfigurationClient = new GovernanceConfigurationClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
        }

        technologyTypeHandler = new TechnologyTypeHandler(externalReferenceManager, openMetadataStoreClient, serviceName, serverName);
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
     * Return the technology type handler.  This client is from the Automated Curation OMVS and is for accessing
     * technology types.
     *
     * @return client
     */
    public TechnologyTypeHandler getTechnologyTypeHandler()
    {
        return technologyTypeHandler;
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


    /**
     * Return the open integration client.  This client is from the Governance Action Framework (GAF) and is for
     * working with automation services.
     *
     * @return client
     */
    public OpenIntegrationServiceClient getOpenIntegrationServiceClient()
    {
        return openIntegrationServiceClient;
    }


    /**
     * Return the  governance configuration client.  This client is from the Governance Action Framework (GAF) and is for
     * working with automation services.
     *
     * @return client
     */
    public GovernanceConfigurationClient getGovernanceConfigurationClient()
    {
        return governanceConfigurationClient;
    }
}
