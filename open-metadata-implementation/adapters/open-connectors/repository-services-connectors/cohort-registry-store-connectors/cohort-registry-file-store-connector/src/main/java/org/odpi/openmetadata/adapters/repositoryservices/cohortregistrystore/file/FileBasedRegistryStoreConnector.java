/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file.ffdc.FileBasedRegistryStoreConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.OMRSCohortRegistryStoreConnectorBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.CohortMembership;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * FileBasedRegistryStoreConnector uses JSON to store details of the membership of the open metadata repository
 * cohort on behalf of the OMRSCohortRegistry.
 */
public class FileBasedRegistryStoreConnector extends OMRSCohortRegistryStoreConnectorBase
{
    /*
     * This is the name of the cohort registry file that is used if there is no file name in the connection.
     */
    private static final String defaultFilename = "cohort.registry";

    /*
     * Variables used in writing to the file.
     */
    private String           registryStoreName       = defaultFilename;

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(FileBasedRegistryStoreConnector.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final ObjectReader OBJECT_READER = OBJECT_MAPPER.reader();

    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer();

    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId - unique id for the connector instance - useful for messages etc
     * @param connectionDetails - POJO for the configuration used to create the connector.
     * @throws ConnectorCheckedException  a problem within the connector.
     */
    @Override
    public void initialize(String     connectorInstanceId,
                           Connection connectionDetails) throws ConnectorCheckedException
    {
        super.initialize(connectorInstanceId, connectionDetails);

        Endpoint endpoint = connectionDetails.getEndpoint();

        if (endpoint != null)
        {
            registryStoreName = endpoint.getNetworkAddress();

            if (registryStoreName == null)
            {
                registryStoreName = defaultFilename;
            }
        }
    }


    /**
     * Save the local registration to the cohort registry store.  This provides details of the local repository's
     * registration with the metadata repository cohort.
     * Any previous local registration information is overwritten.
     *
     * @param localRegistration - details of the local repository's registration with the metadata cohort.
     */
    @Override
    public synchronized void saveLocalRegistration(MemberRegistration localRegistration)
    {
        if (localRegistration != null)
        {
            CohortMembership registryStoreProperties = this.retrieveRegistryStoreProperties();

            registryStoreProperties.setLocalRegistration(localRegistration);

            this.writeRegistryStoreProperties(registryStoreProperties);
        }
        else
        {
            if (auditLog != null)
            {
                String actionDescription = "Saving Local Registration to Registry Store";

                auditLog.logMessage(actionDescription, FileBasedRegistryStoreConnectorAuditCode.NULL_MEMBER_REGISTRATION.getMessageDefinition(registryStoreName));
            }

            log.debug("Null local registration passed to saveLocalRegistration :(");
        }
    }


    /**
     * Retrieve details of the local registration from the cohort registry store.  A null may be returned if the
     * local registration information has not been saved (typically because this is a new server instance).
     *
     * @return MemberRegistration object containing details for the local repository's registration with the
     * metadata cohort (may be null if no registration has taken place).
     */
    @Override
    public synchronized MemberRegistration retrieveLocalRegistration()
    {
        CohortMembership registryStoreProperties = this.retrieveRegistryStoreProperties();

        MemberRegistration localRegistration = registryStoreProperties.getLocalRegistration();

        if (log.isDebugEnabled())
        {
            if (localRegistration == null)
            {
                log.debug("Null local registration returned from retrieveLocalRegistration");
            }
            else
            {
                log.debug("Local Registration details: " +
                                  "metadataCollectionId: " + localRegistration.getMetadataCollectionId() +
                                  "; displayName: " + localRegistration.getServerName() +
                                  "; serverType: " + localRegistration.getServerType() +
                                  "; organizationName: " + localRegistration.getOrganizationName() +
                                  "; registrationTime " + localRegistration.getRegistrationTime());
            }
        }

        return localRegistration;
    }


    /**
     * Remove details of the local registration from the cohort registry store.  This is used when the local
     * repository unregisters from the open metadata repository cohort.
     *
     * There is a side-effect that all the remote registrations are removed to since the local repository is
     * no longer a member of this cohort.
     */
    @Override
    public synchronized void removeLocalRegistration()
    {
        log.debug("Removing local repository from  cohort registry store.");

        CohortMembership registryStoreProperties = this.retrieveRegistryStoreProperties();

        registryStoreProperties.setLocalRegistration(null);

        this.writeRegistryStoreProperties(registryStoreProperties);
    }


    /**
     * Return the remote members from the store as a map from metadata collection id to member registration.
     * Any value of the remote member may change from registration to registration except the
     * metadata collection id.
     *
     * @param remoteMembersList values from the store
     * @return remote member map
     */
    private Map<String, MemberRegistration> getRemoteMemberMap(List<MemberRegistration> remoteMembersList)
    {
        Map<String, MemberRegistration>  remoteMemberMap = new HashMap<>();

        if ((remoteMembersList != null) && (! remoteMembersList.isEmpty()))
        {
            for (MemberRegistration remoteMember : remoteMembersList)
            {
                if ((remoteMember != null) && (remoteMember.getMetadataCollectionId() != null))
                {
                    remoteMemberMap.put(remoteMember.getMetadataCollectionId(), remoteMember);
                }
            }
        }

        return remoteMemberMap;
    }


    /**
     * Save details of a remote registration.  This contains details of one of the other repositories in the
     * metadata repository cohort.
     *
     * @param remoteRegistration - details of a remote repository in the metadata repository cohort.
     */
    @Override
    public synchronized void saveRemoteRegistration(MemberRegistration  remoteRegistration)
    {
        if ((remoteRegistration != null) && (remoteRegistration.getMetadataCollectionId() != null))
        {
            /*
             * Retrieve the current properties from the file is necessary.
             */
            CohortMembership registryStoreProperties = this.retrieveRegistryStoreProperties();

            /*
             * It is possible that the remote repository already has an entry in the cohort registry and if this is
             * the case, it will be overwritten.  Otherwise, the new remote properties are added.
             * The map is used to ensure duplicates can not be stored.
             */
            Map<String, MemberRegistration>  remoteMemberMap = this.getRemoteMemberMap(registryStoreProperties.getRemoteRegistrations());

            /*
             * Now add the new member to the map.  This will overwrite any previous registration.
             */
            remoteMemberMap.put(remoteRegistration.getMetadataCollectionId(), remoteRegistration);

            registryStoreProperties.setRemoteRegistrations(new ArrayList<>(remoteMemberMap.values()));

            /*
             * Write out the new cohort registry content.
             */
            this.writeRegistryStoreProperties(registryStoreProperties);
        }
        else
        {
            if (auditLog != null)
            {
                String actionDescription = "Saving a Remote Registration to Cohort Registry Store";

                auditLog.logMessage(actionDescription, FileBasedRegistryStoreConnectorAuditCode.NULL_MEMBER_REGISTRATION.getMessageDefinition(registryStoreName));
            }

            log.debug("Null remote registration passed to saveRemoteRegistration :(");
        }
    }


    /**
     * Return a list of all the remote metadata repositories registered in the metadata repository cohort.
     *
     * @return Remote registrations list
     */
    @Override
    public synchronized List<MemberRegistration> retrieveRemoteRegistrations()
    {
        /*
         * Ensure the current properties are retrieved from the registry.
         */
        CohortMembership registryStoreProperties = this.retrieveRegistryStoreProperties();
        Map<String, MemberRegistration>  remoteMemberMap = this.getRemoteMemberMap(registryStoreProperties.getRemoteRegistrations());

        if (remoteMemberMap.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(remoteMemberMap.values());
        }
    }


    /**
     * Return the registration information for a specific metadata repository, identified by its metadataCollectionId.
     * If the metadataCollectionId is not recognized then null is returned.
     *
     * @param metadataCollectionId - unique identifier for the repository
     * @return MemberRegistration object containing details of the remote metadata repository. (null if not found)
     */
    @Override
    public synchronized MemberRegistration retrieveRemoteRegistration(String    metadataCollectionId)
    {
        MemberRegistration    remoteRegistration = null;

        if (metadataCollectionId != null)
        {
            /*
             * Ensure the current properties are retrieved from the registry.
             */
            CohortMembership registryStoreProperties = this.retrieveRegistryStoreProperties();
            Map<String, MemberRegistration>  remoteMemberMap = this.getRemoteMemberMap(registryStoreProperties.getRemoteRegistrations());

            remoteRegistration = remoteMemberMap.get(metadataCollectionId);
        }
        else
        {
            if (auditLog != null)
            {
                String actionDescription = "Retrieving Remote Registration from Cohort Registry Store";

                auditLog.logMessage(actionDescription, FileBasedRegistryStoreConnectorAuditCode.NULL_MEMBER_REGISTRATION.getMessageDefinition(registryStoreName));
            }

            log.debug("Null metadataCollectionId passed to retrieveRemoteRegistration :(");
        }

        if (remoteRegistration == null)
        {
            log.debug("No remote registrations");
        }

        return remoteRegistration;
    }


    /**
     * Remove details of the requested remote repository's registration from the store.
     *
     * @param metadataCollectionId - unique identifier for the repository
     */
    @Override
    public synchronized void removeRemoteRegistration(String    metadataCollectionId)
    {
        if (metadataCollectionId != null)
        {
            /*
             * Ensure the current properties are retrieved from the registry.
             */
            CohortMembership registryStoreProperties = this.retrieveRegistryStoreProperties();
            Map<String, MemberRegistration> remoteMemberMap = this.getRemoteMemberMap(registryStoreProperties.getRemoteRegistrations());

            /*
             * Remove the requested properties
             */
            MemberRegistration removedMember = remoteMemberMap.remove(metadataCollectionId);

            if (removedMember != null)
            {
                registryStoreProperties.setRemoteRegistrations(new ArrayList<>(remoteMemberMap.values()));
                writeRegistryStoreProperties(registryStoreProperties);
            }
            else
            {
                log.debug("No remote registration");

                if (auditLog != null)
                {
                    String actionDescription = "Removing Remote Registration from Cohort Registry Store";

                    auditLog.logMessage(actionDescription, FileBasedRegistryStoreConnectorAuditCode.MISSING_MEMBER_REGISTRATION.getMessageDefinition(metadataCollectionId, registryStoreName));
                }

                log.debug("MetadataCollectionId : " + metadataCollectionId + " passed to removeRemoteRegistration not found :(");
            }
        }
        else
        {
            if (auditLog != null)
            {
                String actionDescription = "Removing Remote Registration from Cohort Registry Store";

                auditLog.logMessage(actionDescription, FileBasedRegistryStoreConnectorAuditCode.NULL_MEMBER_REGISTRATION.getMessageDefinition(registryStoreName));
            }

            log.debug("Null metadataCollectionId passed to removeRemoteRegistration :(");
        }
    }


    /**
     * Remove the local and remote registrations from the cohort registry store since the local server has
     * unregistered from the cohort.
     */
    @Override
    public void clearAllRegistrations()
    {
        writeRegistryStoreProperties(null);
    }


    /**
     * Close the config file
     */
    @Override
    public void disconnect()
    {
        try
        {
            super.disconnect();
        }
        catch (Exception  exec)
        {
            log.debug("Ignoring unexpected exception " + exec.getClass().getSimpleName() + " with message " + exec.getMessage());
        }

        log.debug("Closing Cohort Registry Store.");
    }


    /**
     * Refresh the registry store properties with the current values in the file base registry store.
     *
     * @return CohortRegistryProperties object containing the currently stored properties.
     */
    private CohortMembership retrieveRegistryStoreProperties()
    {
        File               registryStoreFile = new File(registryStoreName);
        CohortMembership   newRegistryStoreProperties = null;

        try
        {
            log.debug("Retrieving cohort registry store properties");

            String registryStoreFileContents = FileUtils.readFileToString(registryStoreFile, "UTF-8");

            newRegistryStoreProperties = OBJECT_READER.readValue(registryStoreFileContents, CohortMembership.class);
        }
        catch (IOException   ioException)
        {
            /*
             * The registry file is not found, create a new one ...
             */
            if (auditLog != null)
            {
                String actionDescription = "Retrieving Cohort Registry Store Properties";

                auditLog.logMessage(actionDescription, FileBasedRegistryStoreConnectorAuditCode.CREATE_REGISTRY_FILE.getMessageDefinition(registryStoreName));
            }

            log.debug("New Cohort Registry Store", ioException);
        }

        if (newRegistryStoreProperties == null)
        {
            newRegistryStoreProperties = new CohortMembership();
        }

        return newRegistryStoreProperties;
    }


    /**
     * Test the uniqueness of a single cohort member
     *
     * @param testMember member to test
     * @param metadataCollectionIdTestMap test map
     * @param serverNameTestMap test map
     * @param endpointAddressTestMap test map
     */
    private void  mapMember(MemberRegistration               testMember,
                            Map<String, MemberRegistration>  metadataCollectionIdTestMap,
                            Map<String, MemberRegistration>  serverNameTestMap,
                            Map<String, MemberRegistration>  endpointAddressTestMap)
    {
        if (auditLog != null)
        {
            String actionDescription = "saveRegistryStore";

            if (testMember != null)
            {
                if ((testMember.getMetadataCollectionId() == null) || (testMember.getMetadataCollectionId().length() == 0))
                {
                    auditLog.logMessage(actionDescription, FileBasedRegistryStoreConnectorAuditCode.NULL_REGISTERED_MC_ID.getMessageDefinition(testMember.getServerName()));
                }
                else
                {
                    MemberRegistration duplicateMember = metadataCollectionIdTestMap.put(testMember.getMetadataCollectionId(), testMember);

                    if (duplicateMember != null)
                    {
                        auditLog.logMessage(actionDescription,
                                            FileBasedRegistryStoreConnectorAuditCode.DUPLICATE_REGISTERED_MC_ID.getMessageDefinition(testMember.getMetadataCollectionId(),
                                                                                                          testMember.getServerName(),
                                                                                                          duplicateMember.getServerName()),
                                           testMember.toString() + " " + duplicateMember.toString());
                    }
                }


                if ((testMember.getServerName() == null) || (testMember.getServerName().length() == 0))
                {
                    auditLog.logMessage(actionDescription,
                                        FileBasedRegistryStoreConnectorAuditCode.NULL_REGISTERED_SERVER_NAME.getMessageDefinition(testMember.getMetadataCollectionId()),
                                        testMember.toString());
                }
                else
                {
                    MemberRegistration duplicateMember = serverNameTestMap.put(testMember.getServerName(), testMember);
                    if (duplicateMember != null)
                    {
                        auditLog.logMessage(actionDescription,
                                            FileBasedRegistryStoreConnectorAuditCode.DUPLICATE_REGISTERED_SERVER_NAME.getMessageDefinition(testMember.getServerName(),
                                                                                                                testMember.getMetadataCollectionId(),
                                                                                                                duplicateMember.getMetadataCollectionId()),
                                           testMember.toString() + " " + duplicateMember.toString());
                    }
                }

                Connection repositoryConnection = testMember.getRepositoryConnection();

                if (repositoryConnection != null)
                {
                    Endpoint endpoint   = repositoryConnection.getEndpoint();
                    String   serverAddress = null;

                    if (endpoint != null)
                    {
                        serverAddress = endpoint.getNetworkAddress();
                    }

                    if (serverAddress != null)
                    {
                        MemberRegistration duplicateMember = endpointAddressTestMap.put(serverAddress, testMember);

                        if (duplicateMember != null)
                        {
                            auditLog.logMessage(actionDescription,
                                                FileBasedRegistryStoreConnectorAuditCode.DUPLICATE_REGISTERED_SERVER_ADDR.getMessageDefinition(testMember.getServerName(),
                                                                                testMember.getMetadataCollectionId(),
                                                                                serverAddress,
                                                                                duplicateMember.getServerName(),
                                                                                duplicateMember.getMetadataCollectionId()),
                                               testMember.toString() + " " + duplicateMember.toString());
                        }
                    }
                    else
                    {
                        auditLog.logMessage(actionDescription,
                                            FileBasedRegistryStoreConnectorAuditCode.NULL_REGISTERED_SERVER_NAME.getMessageDefinition(testMember.getServerName(),
                                                                                                           testMember.getMetadataCollectionId()),
                                            testMember.toString());
                    }
                }
                else
                {
                    auditLog.logMessage(actionDescription,
                                        FileBasedRegistryStoreConnectorAuditCode.NULL_REGISTERED_SERVER_CONNECTION.getMessageDefinition(testMember.getServerName(),
                                                                                                             testMember.getMetadataCollectionId()),
                                       testMember.toString());
                }
            }
            else
            {
                auditLog.logMessage(actionDescription, FileBasedRegistryStoreConnectorAuditCode.NULL_MEMBER_REGISTRATION.getMessageDefinition());
            }
        }
    }


    /**
     * This method performs a number of checks to ensure each member of the cohort is unique
     * in terms of its metadata collection id, server name and endpoint address.
     *
     * @param newRegistryStoreProperties values that are about to written the registry store
     */
    private void validateRegistryStore(CohortMembership   newRegistryStoreProperties)
    {
        if (newRegistryStoreProperties != null)
        {
            Map<String, MemberRegistration> metadataCollectionIdTestMap = new HashMap<>();
            Map<String, MemberRegistration> serverNameTestMap           = new HashMap<>();
            Map<String, MemberRegistration> endpointAddressTestMap      = new HashMap<>();

            /*
             * It is ok for the local registration to be null
             */
            if (newRegistryStoreProperties.getLocalRegistration() != null)
            {
                /*
                 * Add the local member to each of the test maps
                 */
                mapMember(newRegistryStoreProperties.getLocalRegistration(),
                          metadataCollectionIdTestMap,
                          serverNameTestMap,
                          endpointAddressTestMap);
            }

            if (newRegistryStoreProperties.getRemoteRegistrations() != null)
            {
                /*
                 * The remote members should not be null/empty
                 */
                for (MemberRegistration  remoteMember : newRegistryStoreProperties.getRemoteRegistrations())
                {
                    mapMember(remoteMember,
                              metadataCollectionIdTestMap,
                              serverNameTestMap,
                              endpointAddressTestMap);
                }
            }
        }
    }


    /**
     * Writes the supplied registry store properties to the registry store.
     *
     * @param newRegistryStoreProperties - contents of the registry store
     */
    private void writeRegistryStoreProperties(CohortMembership   newRegistryStoreProperties)
    {
        File    registryStoreFile = new File(registryStoreName);

        this.validateRegistryStore(newRegistryStoreProperties);

        try
        {
            log.debug("Writing cohort registry store properties" + newRegistryStoreProperties);

            if (newRegistryStoreProperties == null)
            {
                registryStoreFile.delete();
            }
            else
            {

                String registryStoreFileContents = OBJECT_WRITER.writeValueAsString(newRegistryStoreProperties);

                FileUtils.writeStringToFile(registryStoreFile, registryStoreFileContents, (String)null,false);
            }
        }
        catch (IOException   ioException)
        {
            if (auditLog != null)
            {
                String actionDescription = "Writing Cohort Registry Store Properties";

                auditLog.logException(actionDescription,
                                      FileBasedRegistryStoreConnectorAuditCode.UNUSABLE_REGISTRY_FILE.getMessageDefinition(registryStoreName),
                                      ioException);
            }

            log.debug("Unusable Cohort Registry Store :(", ioException);
        }
    }


    /**
     * Flush all changes and close the registry store.
     */
    @Override
    public void close()
    {
        this.disconnect();
    }
}
