/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.model.IGCColumn;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.model.IGCObject;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.model.Terms;
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

    //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
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

    /**
     * Returns the metadata collection object that provides an OMRS abstraction of the metadata within
     * a metadata repository.
     *
     * @return OMRSMetadataCollection - metadata information retrieved from the metadata repository.
     */
    public Terms genericIGCPostQuery(String igcRID) {
        String url = this.connectionBean.getAdditionalProperties().get("igcApiSearch") + igcRID;
        HttpHeaders headers = getHttpHeaders();

        String requestBody = "{\n" +
                "  \"pageSize\": \"100\",\n" +
                "  \"properties\": [\"modified_on\",\"name\", \"position\", \"assigned_to_terms\", \"data_type\", \"length\", \"database_table_or_view.assigned_to_terms\"],\n" +
                "  \"types\": [\"database_column\"],\n" +
                "  \"where\":\n" +
                "  {\n" +
                "    \"operator\": \"and\",\n" +
                "    \"conditions\": [\n" +
                "      {\n" +
                "        \"property\": \"database_table_or_view\",\n" +
                "        \"operator\": \"=\",\n" +
                "        \"value\": \"" + igcRID + "\"\n" +
                "      }\n" +
                "    ]\n" +
                "   }\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        String resultBody = postHttpResult(url, entity);
        return (Terms) getIGCObjectMapper(resultBody, Terms.class);
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

    private String postHttpResult(String url, HttpEntity<String> entity) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
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