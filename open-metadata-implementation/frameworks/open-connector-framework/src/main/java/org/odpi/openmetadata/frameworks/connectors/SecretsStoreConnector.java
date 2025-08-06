/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.users.NamedList;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccount;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.Map;

/**
 * SecretsStoreConnector is the interface for a connector that is able to retrieve secrets (passwords, certificates, ...) from a secure location.
 * The secrets store connector is embedded in a connector that needs one or more secrets to perform its tasks.  Both connectors are initialised
 * together by the ConnectorBroker.  The secrets store connector is called by the surrounding connector to extract the needed secrets.
 * When the ConnectorBroker detects that there is a secrets store connector embedded in another connector, it attempts to retrieve the standard
 * secrets for the corresponding connection object (if they are null):
 *
 * <ul>
 *     <li>userId</li>
 *     <li>clearPassword</li>
 *     <li>encryptedPassword</li>
 * </ul>
 *
 * This means that even if the outer connector is written to expect these secrets in its connection object, they do not need to be stored
 * in the connection object (ie in the configuration document or in the metadata store) but will be placed in the right fields by the
 * ConnectorBroker.
 * If the name(s) of the secret(s) needed by the connector must be configured, they can be stored in the secureProperties.
 */
public abstract class SecretsStoreConnector extends ConnectorBase implements AuditLoggingComponent
{
    protected String   secretsCollectionName = null;
    private   Date     secretsTimeout        = new Date();


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
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        secretsCollectionName = super.getStringConfigurationProperty(SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName(),
                                                                     connectionBean.getConfigurationProperties());

        if (secretsCollectionName == null)
        {
            throwMissingConfigurationProperty(this.getClass().getName(),
                                              "secrets",
                                              SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName(),
                                              methodName);
        }

        checkSecretsStillValid();
    }


    /**
     * Called by subclass to determine if the secrets should be refreshed
     */
    public void checkSecretsStillValid() throws ConnectorCheckedException
    {
        if ((secretsTimeout != null) && (! secretsTimeout.after(new Date())))
        {
            refreshSecrets();
            resetRefreshTime();
        }
    }


    /**
     * Request that the subclass refreshes its secrets.
     */
    protected abstract void refreshSecrets() throws ConnectorCheckedException;


    /**
     * Reset the next refresh time
     *
     * @throws ConnectorCheckedException problem with the store
     */
    protected void resetRefreshTime() throws ConnectorCheckedException
    {
        long refreshTimeInterval = getRefreshTimeInterval();

        if (refreshTimeInterval != 0L)
        {
            long newRefreshTime = new Date().getTime() + (refreshTimeInterval * 60 * 1000);
            secretsTimeout = new Date(newRefreshTime);
        }
    }


    /**
     * Retrieve a secret from the secrets store.
     *
     * @param secretName name of the secret.
     * @return secret
     * @throws ConnectorCheckedException there is a problem with the connector
     */
    abstract public String getSecret(String secretName) throws ConnectorCheckedException;


    /**
     * Retrieve the refresh time from the secrets store.
     *
     * @return how long the secrets can be cached - 0 means indefinitely
     * @throws ConnectorCheckedException there is a problem with the connector
     */
    abstract public long   getRefreshTimeInterval() throws ConnectorCheckedException;


    /**
     * Retrieve the requested user definitions stored in the secrets collection.
     *
     * @param userId userId for the lookup
     * @return associated user details or null
     * @throws ConnectorCheckedException there is a problem with the connector
     */
    public UserAccount getUser(String userId) throws ConnectorCheckedException
    {
        return null;
    }


    /**
     * Retrieve any user definitions stored in the secrets collection.
     *
     * @return map of userIds to user details
     * @throws ConnectorCheckedException there is a problem with the connector
     */
    public Map<String, UserAccount> getUsers() throws ConnectorCheckedException
    {
        return null;
    }


    /**
     * Look up a particular named list in the collection.
     *
     * @param listName name of a list
     * @return corresponding named list or null
     * @throws ConnectorCheckedException there is a problem with the connector
     */
    public NamedList getNamedList(String listName) throws  ConnectorCheckedException
    {
        return null;
    }


    /**
     * Return all of the known named lists in this collection
     *
     * @return map of named lists in this collection
     * @throws ConnectorCheckedException there is a problem with the connector
     */
    public Map<String, NamedList> getNamedLists() throws ConnectorCheckedException
    {
        return null;
    }
}
