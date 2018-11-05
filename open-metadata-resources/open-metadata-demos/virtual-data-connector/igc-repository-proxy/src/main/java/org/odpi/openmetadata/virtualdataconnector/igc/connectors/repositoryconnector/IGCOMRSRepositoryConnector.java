/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.model.IGCColumn;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.model.IGCObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import static org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper.model.Constants.DEFAULT_PAGE_SIZE;


/**
 * The IGCOMRSRepositoryConnector is a connector to a remote IBM Information Governance Catalog (IGC) repository.
 */
public class IGCOMRSRepositoryConnector extends OMRSRepositoryConnector {

    /**
     * Default constructor used by the OCF Connector Provider.
     */
    public IGCOMRSRepositoryConnector() {
        disableSslVerification();
    }

    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set up the unique Id for this metadata collection.
     *
     * @param metadataCollectionId - String unique Id
     */
    public void setMetadataCollectionId(String metadataCollectionId) {
        this.metadataCollectionId = metadataCollectionId;

        /*
         * Initialize the metadata collection only once the connector is properly set up.
         */
        metadataCollection = new IGCOMRSMetadataCollection(this,
                super.serverName,
                repositoryHelper,
                repositoryValidator,
                metadataCollectionId);
    }

    /**
     * Free up any resources held since the connector is no longer needed.
     */
    @Override
    public void disconnect() {
        super.metadataCollection = new IGCOMRSMetadataCollection(this,
                super.serverName,
                repositoryHelper,
                repositoryValidator, metadataCollectionId);
    }


    /**
     * Returns the metadata collection object that provides an OMRS abstraction of the metadata within
     * a metadata repository.
     *
     * @return OMRSMetadataCollection - metadata information retrieved from the metadata repository.
     */
    public OMRSMetadataCollection getMetadataCollection() {

        if (metadataCollection == null) {
            throw new NullPointerException("Local metadata collection id is not set up");
        }

        return metadataCollection;
    }


    /**
     * Query IGC for more information about an asset.
     *
     * @param igcRID The techncial ID of the asset.
     * @return IGCObject
     */
    public IGCObject genericIGCQuery(String igcRID) {

        String url = this.connectionBean.getAdditionalProperties().get("igcApiGet") + igcRID;

        return getIgcObject(url);
    }

    public IGCObject getDatabaseColumns(String igcRID, Integer pageSize) {

        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        String url = this.connectionBean.getAdditionalProperties()
                .get("igcApiGet") + igcRID + "/database_columns?begin=0&pageSize=" + pageSize;

        IGCObject igcObjectMapper = getIgcObject(url);

        Integer numTotal = igcObjectMapper.getDatabaseColumns().getPaging().getNumTotal();
        if (pageSize < numTotal) {
            url = this.connectionBean.getAdditionalProperties()
                    .get("igcApiGet") + igcRID + "/database_columns?begin=0&pageSize=" + numTotal;
            return getIgcObject(url);
        }

        return igcObjectMapper;
    }

    private IGCObject getIgcObject(String url) {
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());
        String resultBody = getHttpResult(url, entity);
        return (IGCObject) getIGCObjectMapper(resultBody, IGCObject.class);
    }


    public IGCColumn getIGCColumn(String igcRID) {

        String url = this.connectionBean.getAdditionalProperties().get("igcApiGet") + igcRID;

        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());
        String resultBody = getHttpResult(url, entity);

        return (IGCColumn) getIGCObjectMapper(resultBody, IGCColumn.class);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Cache-Control", "no-cache");
        headers.set("Authorization", "Basic " + this.connectionBean.getAdditionalProperties().get("authorization"));
        headers.set("Proxy-Authorization", "Basic " + this.connectionBean.getAdditionalProperties().get("authorization"));
        headers.set("Content-Type", "application/json");

        return headers;
    }


    private String getHttpResult(String url, HttpEntity<String> entity) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return result.getBody();
    }

    private Object getIGCObjectMapper(String resultBody, Class objectClass) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(resultBody, objectClass);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}