/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The XTDBOMRSRepositoryConnectorProvider is the connector provider for the XTDBOMRSRepositoryConnector.
 * It extends OMRSRepositoryConnectorProviderBase which in turn extends the OCF ConnectorProviderBase.
 * ConnectorProviderBase supports the creation of connector instances.
 * <p>
 * The XTDBOMRSRepositoryConnectorProvider must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 * <br><br>
 * The configurationProperties can contain the following keys:
 * <ul>
 *     <li><code>xtdbConfig</code>: a XTDB configuration document that follows the JSON structure documented on
 *         XTDB's site: https://xtdb.com/reference/1.19.0-beta1/configuration.html</li>
 *     <li><code>syncIndex</code>: a boolean indicating whether writes should be done synchronously (true, default) to
 *         ensure the index is up-to-date before returning, or asynchronously (false) to ensure that the transaction is
 *         recorded but that the index can be eventually consistent (faster writes)</li>
 *     <li><code>luceneRegexes</code>: a boolean indicating whether any unquoted regexes (those not surrounded by
 *         <code>\Q</code> and <code>\E</code>) should be treated as Lucene regexes (true) or not (false). Technically
 *         the search interfaces are meant to take Java regexes; however, if usage of the connector is only expected to
 *         pass fairly simple regexes that are also supported by Lucene, enabling this should significantly improve the
 *         performance of queries against text data that involves regexes that are unquoted. (Regexes that are quoted
 *         will be handled appropriately irrespective of this setting.)  Note that this will have no impact if Lucene
 *         itself is not configured.</li>
 * </ul><br>
 * For example:
 * <code>
 * {
 *   "class": "Connection",
 *   "connectorType": {
 *     "class": "ConnectorType",
 *     "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnectorProvider"
 *   },
 *   "configurationProperties": {
 *     "xtdbConfig": {
 *       "xtdb/index-store": {
 *         "kv-store": {
 *           "xtdb/module": "xtdb.rocksdb/-&gt;kv-store",
 *           "db-dir": "data/servers/xtdb/rdb-index"
 *         }
 *       },
 *       "xtdb/document-store": {
 *         "kv-store": {
 *           "xtdb/module": "xtdb.rocksdb/-&gt;kv-store",
 *           "db-dir": "data/servers/xtdb/rdb-docs"
 *         }
 *       },
 *       "xtdb/tx-log": {
 *         "xtdb/module": "xtdb.kafka/-&gt;tx-log",
 *         "kafka-config": {
 *           "bootstrap-servers": "localhost:9092"
 *         },
 *         "tx-topic-opts": {
 *           "topic-name": "xtdb-tx-log"
 *         },
 *         "poll-wait-duration": "PT1S"
 *       }
 *     },
 *     "syncIndex": false,
 *     "luceneRegexes": true
 *   }
 * }
 * </code>
 */
public class XTDBOMRSRepositoryConnectorProvider extends OMRSRepositoryConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int connectorComponentId = 87;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID = "ba99618d-fd21-475b-8eba-87051aea026e";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:OMRSRepositoryConnector:XTDB";
    private static final String connectorDisplayName = "XTDB-based OMRS Repository Connector";
    private static final String connectorDescription = "Plugin open metadata repository connector that maps open metadata calls to an XTDB-based metadata repository.";
    private static final String connectorWikiPage = "https://egeria-project.org/egeria-docs/connectors/repository/xtdb/";

    /*
     * Class of the connector.
     */
    private static final String connectorClass = "org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector";

    public static final String XTDB_CONFIG = "xtdbConfig";
    public static final String XTDB_CONFIG_EDN = "xtdbConfigEDN";
    public static final String SYNCHRONOUS_INDEX = "syncIndex";
    public static final String LUCENE_REGEXES = "luceneRegexes";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public XTDBOMRSRepositoryConnectorProvider() {

        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClass);

        /*
         * Set up the connector type that should be included in a connection used to configure this connector.
         */
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> configProperties = new ArrayList<>();
        configProperties.add(XTDB_CONFIG);
        configProperties.add(XTDB_CONFIG_EDN);
        configProperties.add(SYNCHRONOUS_INDEX);
        configProperties.add(LUCENE_REGEXES);
        connectorType.setRecognizedConfigurationProperties(configProperties);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);

    }

}
