/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.wso2mi.resource;

import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.factory.RESTClientFactory;
import org.odpi.openmetadata.adapters.connectors.wso2mi.ffdc.WSO2MIAuditCode;
import org.odpi.openmetadata.adapters.connectors.wso2mi.ffdc.WSO2MIErrorCode;
import org.odpi.openmetadata.adapters.connectors.wso2mi.properties.APIInfo;
import org.odpi.openmetadata.adapters.connectors.wso2mi.resource.rest.ListAPIsResponse;
import org.odpi.openmetadata.adapters.connectors.wso2mi.resource.rest.LoginResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Collections;
import java.util.List;

/**
 * WSO2MIResourceConnector is the resource connector for a WSO2 Micro Integrator instance.  It talks to the
 * Micro Integrator's <a href="https://mi.docs.wso2.com/">Management API</a> (default port 9164, base path
 * {@code /management}).
 *
 * <p>Authentication is a two step exchange, unlike the JDBC- and OAuth2-based connectors in this family:</p>
 * <ol>
 *     <li>{@code GET /management/login} with HTTP Basic Auth ({@code username:password}) returns
 *     {@code { "AccessToken": "..." }}.</li>
 *     <li>That token is presented as a {@code Bearer} token on every subsequent Management API call.</li>
 * </ol>
 *
 * <p><b>Open design point (odpi/egeria#9245):</b> the exact wiring of this login/session-token exchange into
 * Egeria's {@code RESTClientFactory} / secrets-store machinery is still being agreed with the Egeria
 * maintainers.  The current implementation obtains the base client from the factory (which applies the
 * configured Basic credentials) for the login call, then holds the resulting bearer token on this
 * connector.  This will be revised to match whatever session-token pattern Egeria settles on.</p>
 *
 * <p>v1 scope is deliberately minimal: authenticate, then list the deployed APIs.  Proxy Services,
 * Sequences, Endpoints, Inbound Endpoints, Connectors, Message Stores/Processors, Templates and Tasks are
 * follow-up increments.</p>
 */
public class WSO2MIResourceConnector extends ConnectorBase implements AuditLoggingComponent
{
    private AuditLog auditLog      = null;
    private String   connectorName = "WSO2 Micro Integrator Connector";

    private String targetRootURL   = null;
    private String wso2InstanceName = "WSO2 Micro Integrator";

    private RESTClientConnector clientConnector = null;
    private String             bearerToken     = null;


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
     * Set up a new instance name (must be called before {@link #start()}).
     *
     * @param wso2InstanceName new instance name
     */
    public void setWSO2InstanceName(String wso2InstanceName)
    {
        this.wso2InstanceName = wso2InstanceName;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException connector disconnected
     */
    @Override
    public void start() throws ConnectorCheckedException,
                               UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        if (connectionBean.getDisplayName() != null)
        {
            connectorName = connectionBean.getDisplayName();
        }

        Endpoint endpoint = connectionBean.getEndpoint();

        if (endpoint != null)
        {
            targetRootURL = endpoint.getNetworkAddress();
        }

        if (targetRootURL == null)
        {
            throw new ConnectorCheckedException(WSO2MIErrorCode.NULL_URL.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        try
        {
            RESTClientFactory factory = new RESTClientFactory(wso2InstanceName,
                                                             targetRootURL,
                                                             secretsStoreConnectorMap,
                                                             auditLog);

            this.clientConnector = factory.getClientConnector();

            /*
             * Authenticate and prove the endpoint is reachable.
             */
            this.login();
            this.listAPIs();
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      WSO2MIAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                               error.getClass().getName(),
                                                                                               methodName,
                                                                                               error.getMessage()),
                                      error);
            }

            throw new ConnectorCheckedException(WSO2MIErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),
                                                                                                         methodName,
                                                                                                         error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /*
     *===========================================================================
     * Authentication
     */

    /**
     * Exchange the configured Basic credentials for a short-lived bearer token via
     * {@code GET /management/login}, and hold it for subsequent calls.
     *
     * @throws PropertyServerException the login call failed or returned no token
     */
    public void login() throws PropertyServerException
    {
        final String methodName  = "login";
        final String urlTemplate = targetRootURL + "/login";

        LoginResponse response = callGetRESTCallNoParams(methodName, LoginResponse.class, urlTemplate);

        if ((response == null) || (response.getAccessToken() == null))
        {
            throw new PropertyServerException(WSO2MIErrorCode.NO_ACCESS_TOKEN.getMessageDefinition(connectorName, targetRootURL),
                                              this.getClass().getName(),
                                              methodName);
        }

        this.bearerToken = response.getAccessToken();

        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                WSO2MIAuditCode.AUTHENTICATED.getMessageDefinition(connectorName, targetRootURL));
        }
    }


    /**
     * Return the bearer token obtained from the last {@link #login()} call, or null if not yet authenticated.
     *
     * @return string token
     */
    public String getBearerToken()
    {
        return bearerToken;
    }


    /*
     *===========================================================================
     * Specialized methods - APIs
     */

    /**
     * List the REST APIs currently deployed on the Micro Integrator.
     *
     * @return list of {@link APIInfo}; an empty list if none are deployed
     * @throws PropertyServerException problem with the call
     */
    public List<APIInfo> listAPIs() throws PropertyServerException
    {
        final String methodName  = "listAPIs";
        final String urlTemplate = targetRootURL + "/apis";

        ListAPIsResponse response = callGetRESTCallNoParams(methodName, ListAPIsResponse.class, urlTemplate);

        if ((response != null) && (response.getList() != null))
        {
            return response.getList();
        }

        return Collections.emptyList();
    }


    /**
     * Return the detail for a single deployed API by name ({@code GET /management/apis?apiName={name}}).
     *
     * @param apiName name of the API
     * @return {@link APIInfo}, or null if the Micro Integrator has no API with that name
     * @throws PropertyServerException problem with the call
     */
    public APIInfo getAPI(String apiName) throws PropertyServerException
    {
        final String methodName  = "getAPI";
        final String urlTemplate = targetRootURL + "/apis?apiName={0}";

        return callGetRESTCall(methodName, APIInfo.class, urlTemplate, apiName);
    }


    /*
     *===========================================================================
     * REST call wrappers
     */

    /**
     * Issue a GET REST call that returns a response object, with no URL parameters.
     *
     * @param <T>         return type
     * @param methodName  name of the method being called
     * @param returnClass class of the response object
     * @param urlTemplate the full URL for the REST API call
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack
     */
    private <T> T callGetRESTCallNoParams(String   methodName,
                                          Class<T> returnClass,
                                          String   urlTemplate) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate);
        }
        catch (Exception error)
        {
            throw new PropertyServerException(WSO2MIErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              error);
        }
    }


    /**
     * Issue a GET REST call that returns a response object, with URL parameters.
     *
     * @param <T>         return type
     * @param methodName  name of the method being called
     * @param returnClass class of the response object
     * @param urlTemplate template of the URL for the REST API call with {@code {0}}-style place-holders
     * @param params      parameters slotted into the url template
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack
     */
    private <T> T callGetRESTCall(String   methodName,
                                  Class<T> returnClass,
                                  String   urlTemplate,
                                  Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate, params);
        }
        catch (Exception error)
        {
            throw new PropertyServerException(WSO2MIErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              error);
        }
    }
}
