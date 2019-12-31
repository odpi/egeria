/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * The OMRSAuditLogStoreProviderBase provides a base class for the connector provider supporting OMRS
 * audit log stores.  It extends ConnectorProviderBase which does the creation of connector instances.
 * The subclasses of OMRSAuditLogStoreProviderBase must initialize ConnectorProviderBase with the Java class
 * name of the audit log connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public abstract class OMRSAuditLogStoreProviderBase extends ConnectorProviderBase
{
    public static final String  supportedSeveritiesProperty = "supportedSeverities";

    /**
     * Default Constructor
     */
    public OMRSAuditLogStoreProviderBase()
    {
        /*
         * Nothing to do
         */
    }


    /**
     * Return the list of recognized configuration properties supported by the base class for the connector.
     *
     * @return list of Audit log severities that this connector is configured to support (see OMRSAuditLogRecordSeverity).
     */
    protected List<String> getRecognizedConfigurationProperties()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(supportedSeveritiesProperty);

        return recognizedConfigurationProperties;
    }
}

