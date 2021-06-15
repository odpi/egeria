/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.client;


import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ModuleTableFilter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.AnalyticsModelingOMASAPIResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.AssetsResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;


/**
 * REST client is responsible for issuing calls to the AnalyticsModeling OMAS REST APIs.
 */
public class AnalyticsModelingRestClient extends FFDCRESTClient
{
	private final String urlTemplateResource = "/servers/{0}/open-metadata/access-services/analytics-modeling/users/{1}/";
	private final String urlTemplateSynchronization= urlTemplateResource + "sync?serverCapability={2}";

    /**
     * Constructor for no authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AnalyticsModelingRestClient(String serverName, String serverPlatformURLRoot, AuditLog auditLog)
    		throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AnalyticsModelingRestClient(String serverName, String serverPlatformURLRoot)
    		throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Constructor for simple userId and password authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AnalyticsModelingRestClient(String serverName, String serverPlatformURLRoot, String userId, String password, AuditLog auditLog)
    		throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AnalyticsModelingRestClient(String serverName, String serverPlatformURLRoot, String userId, String password)
    		throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }
    
	public AnalyticsModelingOMASAPIResponse getDatabases(String userId, Integer startFrom, Integer pageSize)
	{
		String methodName = "getDatabases";
		try {
			String url = serverPlatformURLRoot + urlTemplateResource + "databases?startFrom={2}&pageSize={3}";
			return callGetRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class, url, serverName, userId, startFrom, pageSize);
		} catch (PropertyServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public AnalyticsModelingOMASAPIResponse getSchemas(String userId, String databaseGuid, Integer startFrom, Integer pageSize)
	{
		String methodName = "getSchemas";
		try {
			String url = serverPlatformURLRoot + urlTemplateResource + "{2}/schemas?startFrom={3}&pageSize={4}";
			return callGetRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class, url, serverName, userId, databaseGuid, startFrom, pageSize);
		} catch (PropertyServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public AnalyticsModelingOMASAPIResponse getTables(String userId, String databaseGuid, String catalog, String schema)
	{
		String methodName = "getTables";
		try {
			String url = serverPlatformURLRoot + urlTemplateResource + "{2}/tables?catalog={3}&schema={4}";
			return callPostRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class, url, null, serverName, userId, databaseGuid, catalog, schema);
		} catch (PropertyServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public AnalyticsModelingOMASAPIResponse getModule(String userId, String databaseGuid, String catalog, String schema, ModuleTableFilter request)
	{
		String methodName = "getModule";
		try {
			String url = serverPlatformURLRoot + urlTemplateResource + "{2}/physicalModule?catalog={3}&schema={4}";
			return callPostRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class, url, request, serverName, userId, databaseGuid, catalog, schema);
		} catch (PropertyServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
	public AssetsResponse createAssets(String user, String serverCapability, AnalyticsAsset asset)
	{
		String methodName = "createAssets";
		try {
			String url = serverPlatformURLRoot + urlTemplateSynchronization;
			return callPostRESTCall(methodName, AssetsResponse.class, url, asset, serverName, user, serverCapability);
		} catch (PropertyServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
	public AssetsResponse updateAssets(String user, String serverCapability, AnalyticsAsset asset)
	{
		String methodName = "updateAssets";
		try {
			String url = serverPlatformURLRoot + urlTemplateSynchronization;
			return callPutRESTCall(methodName, AssetsResponse.class, url, asset, serverName, user, serverCapability);
		} catch (PropertyServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
	public AssetsResponse deleteAssets(String user, String serverCapability, String identifier)
	{
		String methodName = "deleteAssets";
		try {
			String url = serverPlatformURLRoot + urlTemplateSynchronization + "&identifier={3}";
			return callDeleteRESTCall(methodName, AssetsResponse.class, url, serverName, user, serverCapability, identifier);
		} catch (PropertyServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
}
