/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apachekafka.resource;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ffdc.ApacheKafkaAuditCode;
import org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ffdc.ApacheKafkaErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;

import java.util.Map;
import java.util.Properties;
import java.util.Set;


/**
 * ApacheKafkaAdminConnector provides access to an Apache Kafka's Admin API.
 */
public class ApacheKafkaAdminConnector extends ConnectorBase implements AuditLoggingComponent
{
    private AuditLog auditLog      = null;
    private String   targetRootURL = null;
    private String   connectorName = "Apache Kafka Admin Connector";

    private Admin    kafkaAdminClient = null;

    /**
     * Default Constructor used by the connector provider.
     */
    public ApacheKafkaAdminConnector()
    {
    }
    

    /* ==============================================================================
     * Standard methods that trigger activity.
     */


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
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";
        
        if (connectionProperties.getConnectionName() != null)
        {
            connectorName = connectionProperties.getConnectionName();
        }

        /*
         * Retrieve the configuration
         */
        EndpointProperties endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            targetRootURL = endpoint.getAddress();
        }

        if (targetRootURL == null)
        {
            throw new ConnectorCheckedException(ApacheKafkaErrorCode.NULL_URL.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * Set up the admin client.
         */
        Properties properties = new Properties();
        properties.put("bootstrap.servers", targetRootURL);
        kafkaAdminClient = Admin.create(properties);
    }


    /*
     *===========================================================================
     * Specialized methods
     */

    /**
     * Return the list of topics defined to the Apache Kafka server.
     *
     * @return set of topic names
     * @throws ConnectorCheckedException unexpected exception
     */
    public Set<String> getTopicList() throws ConnectorCheckedException
    {
        final String methodName = "getTopicList";

        try
        {
            return kafkaAdminClient.listTopics().names().get();
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  ApacheKafkaAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                 error.getClass().getName(),
                                                                                                 methodName,
                                                                                                 error.getMessage()),
                                  error);

            throw new ConnectorCheckedException(ApacheKafkaErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Return the metrics available from the Apache Kafka Server.
     *
     * @return map of metric definitions to metric values
     */
    public Map<MetricName, ? extends Metric> metrics()
    {
        return kafkaAdminClient.metrics();
    }


    /**
     * Return details about each topic
     *
     * @return topic details
     * @throws ConnectorCheckedException problem accessing event broker
     */
    public DescribeTopicsResult describeTopics() throws ConnectorCheckedException
    {
        return kafkaAdminClient.describeTopics(this.getTopicList());
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        if (kafkaAdminClient != null)
        {
            kafkaAdminClient.close();
        }

        super.disconnect();
    }
}
