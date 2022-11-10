/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;


/**
 * The OMRSRepositoryConnector defines the interface for an OMRS Repository Connector.  It is an abstract
 * class since not all the methods for OMRSMetadataCollectionManager are implemented.
 */
public abstract class OMRSRepositoryConnector extends ConnectorBase implements OMRSMetadataCollectionManager,
                                                                               AuditLoggingComponent
{
    protected OMRSRepositoryHelper    repositoryHelper    = null;
    protected OMRSRepositoryValidator repositoryValidator = null;
    protected String                  repositoryName      = null;
    protected String                  serverName          = null;
    protected String                  serverType          = null;
    protected String                  organizationName    = null;
    protected String                  serverUserId        = null;
    protected int                     maxPageSize         = 1000;

    protected String                  metadataCollectionId   = null;
    protected String                  metadataCollectionName = null;
    protected OMRSMetadataCollection  metadataCollection     = null;

    protected AuditLog                auditLog = null;



    /**
     * Default constructor nothing to do
     */
    public OMRSRepositoryConnector()
    {
    }


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;

        if (metadataCollection != null)
        {
            metadataCollection.setAuditLog(auditLog);
        }
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }


    /**
     * Set up a repository helper object for the repository connector to use.
     *
     * @param repositoryHelper helper object for building and querying TypeDefs and metadata instances.
     */
    @Override
    public void setRepositoryHelper(OMRSRepositoryHelper repositoryHelper)
    {
        this.repositoryHelper = repositoryHelper;
    }


    /**
     * Return the repository helper for this connector.
     *
     * @return helper object for building and querying TypeDefs and metadata instances.
     */
    public OMRSRepositoryHelper getRepositoryHelper()
    {
        return repositoryHelper;
    }


    /**
     * Set up a repository validator for the repository connector to use.
     *
     * @param repositoryValidator validator object to check the validity of TypeDefs and metadata instances.
     */
    @Override
    public void setRepositoryValidator(OMRSRepositoryValidator repositoryValidator)
    {
        this.repositoryValidator = repositoryValidator;
    }


    /**
     * Return the repository validator for this connector.
     *
     * @return validator object to check the validity of TypeDefs and metadata instances.
     */
    public OMRSRepositoryValidator getRepositoryValidator()
    {
        return repositoryValidator;
    }


    /**
     * Return the name of the repository where the metadata collection resides.
     *
     * @return String name
     */
    @Override
    public String  getRepositoryName()
    {
        return this.repositoryName;
    }


    /**
     * Set up the name of the repository where the metadata collection resides.
     *
     * @param repositoryName String name
     */
    @Override
    public void  setRepositoryName(String      repositoryName)
    {
        this.repositoryName = repositoryName;
    }


    /**
     * Return the name of the server where the metadata collection resides.
     *
     * @return String name
     */
    @Override
    public String getServerName() { return serverName; }


    /**
     * Set up the name of the server where the metadata collection resides.
     *
     * @param serverName String name
     */
    @Override
    public void  setServerName(String      serverName)
    {
        this.serverName = serverName;
    }


    /**
     * Return the descriptive string describing the type of the server.  This might be the
     * name of the product, or similar identifier.
     *
     * @return String name
     */
    @Override
    public String getServerType() { return serverType; }


    /**
     * Set up the descriptive string describing the type of the server.  This might be the
     * name of the product, or similar identifier.
     *
     * @param serverType String server type
     */
    @Override
    public void setServerType(String serverType)
    {
        this.serverType = serverType;
    }


    /**
     * Return the name of the organization that runs/owns the server used to access the repository.
     *
     * @return String name
     */
    @Override
    public String getOrganizationName() { return organizationName; }


    /**
     * Set up the name of the organization that runs/owns the server used to access the repository.
     *
     * @param organizationName String organization name
     */
    @Override
    public void setOrganizationName(String organizationName)
    {
        this.organizationName = organizationName;
    }


    /**
     * Return the userId that the local server should use when processing events and there is no external user
     * driving the operation.
     *
     * @return user id
     */
    @Override
    public String getServerUserId()
    {
        return serverUserId;
    }


    /**
     * Set up the userId that the local server should use when processing events and there is no external user
     * driving the operation.
     *
     * @param localServerUserId string user id
     */
    @Override
    public void setServerUserId(String localServerUserId)
    {
        this.serverUserId = localServerUserId;
    }


    /**
     * Return the unique id for this metadata collection.
     *
     * @return String unique Id
     */
    @Override
    public String getMetadataCollectionId()
    {
        return this.metadataCollectionId;
    }


    /**
     * Set up the unique id for this metadata collection.
     *
     * @param metadataCollectionId String unique Id
     */
    @Override
    public void setMetadataCollectionId(String metadataCollectionId)
    {
        this.metadataCollectionId = metadataCollectionId;
    }


    /**
     * Return the metadata collection name of this repository connector.  It defaults to the server name if not set
     * up explicitly.
     *
     * @return display name of the metadata collection.
     */
    public String getMetadataCollectionName()
    {
        if (metadataCollectionName != null)
        {
            return metadataCollectionName;
        }
        else
        {
            return serverName;
        }
    }


    /**
     * Explicitly set up the metadata collection name.
     *
     * @param metadataCollectionName display name of the metadata collection.
     */
    public void setMetadataCollectionName(String metadataCollectionName)
    {
        this.metadataCollectionName = metadataCollectionName;
    }


    /**
     * Return the maximum PageSize
     *
     * @return maximum number of elements that can be retrieved on a request.
     */
    @Override
    public int getMaxPageSize()
    {
        return this.maxPageSize;
    }


    /**
     * Set up the maximum PageSize
     *
     * @param maxPageSize maximum number of elements that can be retrieved on a request.
     */
    @Override
    public void setMaxPageSize(int    maxPageSize)
    {
        this.maxPageSize = maxPageSize;
    }


    /**
     * Throw a RepositoryErrorException if the connector is not active.
     *
     * @param methodName name of calling method
     * @throws RepositoryErrorException repository connector has not started or has been disconnected.
     */
    public void validateRepositoryIsActive(String  methodName) throws RepositoryErrorException
    {
        if (! super.isActive())
        {
            throw new RepositoryErrorException(OMRSErrorCode.REPOSITORY_NOT_AVAILABLE.getMessageDefinition(serverName, methodName),
                    this.getClass().getName(),
                    methodName);
        }
    }


    /**
     * Returns the metadata collection object that provides an OMRS abstraction of the metadata within
     * a metadata repository.
     *
     * @return OMRSMetadataInstanceStore metadata information retrieved from the metadata repository.
     ** @throws RepositoryErrorException no metadata collection
     */
    @Override
    public OMRSMetadataCollection getMetadataCollection() throws RepositoryErrorException
    {
        if (metadataCollection == null)
        {
            final String methodName = "getMetadataCollection";

            throw new RepositoryErrorException(OMRSErrorCode.NULL_METADATA_COLLECTION.getMessageDefinition(serverName),
                    this.getClass().getName(),
                    methodName);
        }

        return metadataCollection;
    }
}