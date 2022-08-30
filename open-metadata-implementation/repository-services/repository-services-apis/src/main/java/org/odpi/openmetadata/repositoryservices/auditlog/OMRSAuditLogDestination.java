/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.auditlog;


import org.odpi.openmetadata.frameworks.auditlog.AuditLogDestination;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecordOriginator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * OMRSAuditLogDestination provides information needed to log records to the configured audit log destinations
 * for a specific server instance.
 */
public class OMRSAuditLogDestination extends AuditLogDestination
{
    private final OMRSAuditLogRecordOriginator omrsOriginator = new OMRSAuditLogRecordOriginator();
    private       List<OMRSAuditLogStore>      auditLogStores = null;

    private static final Logger log = LoggerFactory.getLogger(OMRSAuditLogDestination.class);


    /**
     * Initialize the static values used in all log records.  These values help to pinpoint the source of messages
     * when audit log records from many servers are consolidated into centralized operational tooling.
     *
     * @param localServerName name of the local server
     * @param localServerType type of the local server
     * @param localOrganizationName name of the organization that owns the local server
     * @param auditLogStores list of destinations for the audit log records
     */
    public OMRSAuditLogDestination(String                  localServerName,
                                   String                  localServerType,
                                   String                  localOrganizationName,
                                   List<OMRSAuditLogStore> auditLogStores)
    {
        super();

        /*
         * These are the strategic values
         */
        if (super.originatorProperties == null)
        {
            super.originatorProperties = new HashMap<>();
        }
        super.originatorProperties.put(OMRSAuditLogRecordOriginator.SERVER_NAME_PROPERTY, localServerName);
        super.originatorProperties.put(OMRSAuditLogRecordOriginator.SERVER_TYPE_PROPERTY, localServerType);
        super.originatorProperties.put(OMRSAuditLogRecordOriginator.ORGANIZATION_NAME_PROPERTY, localOrganizationName);

        /*
         * These are set up for backward compatibility
         */
        this.omrsOriginator.setServerName(localServerName);
        this.omrsOriginator.setServerType(localServerType);
        this.omrsOriginator.setOrganizationName(localOrganizationName);

        if (auditLogStores != null)
        {
            this.auditLogStores = new ArrayList<>(auditLogStores);
        }
    }


    /**
     * Set up the local metadata collection id.  This is null if there is no local repository.
     *
     * @param localMetadataCollectionId String unique identifier for the metadata collection
     */
    public void setLocalMetadataCollectionId(String              localMetadataCollectionId)
    {
        super.originatorProperties.put(OMRSAuditLogRecordOriginator.METADATA_COLLECTION_ID_PROPERTY, localMetadataCollectionId);
        this.omrsOriginator.setMetadataCollectionId(localMetadataCollectionId);
    }


    /**
     * Return the originator to the audit log to help build the log record.
     *
     * @return record originator object.
     */
    OMRSAuditLogRecordOriginator getOriginator()
    {
        return omrsOriginator;
    }


    /**
     * Log an audit log record for an event, decision, error, or exception detected by the
     * open metadata services.
     *
     * @param logRecord the log record
     */
    public void addLogRecord(AuditLogRecord logRecord)
    {
        this.addLogRecord(new OMRSAuditLogRecord(logRecord));
    }


    /**
     * Log an audit log record for an event, decision, error, or exception detected by the
     * open metadata services.
     *
     * @param logRecord the log record
     */
    void addLogRecord(OMRSAuditLogRecord logRecord)
    {
        if (auditLogStores != null)
        {
            for (OMRSAuditLogStore auditLogStore : auditLogStores)
            {
                if (auditLogStore != null)
                {
                    try
                    {
                        auditLogStore.storeLogRecord(new OMRSAuditLogRecord(logRecord));
                    }
                    catch (Exception error)
                    {
                        log.error("Error: " + error + " writing audit log: " + logRecord + " to destination " + auditLogStore.getClass().getName());
                    }
                }
            }
        }
    }


    /**
     * Return information about the audit log stores configured for this server.
     *
     * @return report
     */
    OMRSAuditLogDestinationsReport getDestinationsReport()
    {
        OMRSAuditLogDestinationsReport report = null;

        if (auditLogStores != null)
        {
            report = new OMRSAuditLogDestinationsReport();

            List<OMRSAuditLogStoreReport>  storeReportList = new ArrayList<>();

            for (OMRSAuditLogStore auditLogStore : auditLogStores)
            {
                if (auditLogStore != null)
                {
                    OMRSAuditLogStoreReport auditLogStoreReport = new OMRSAuditLogStoreReport();

                    auditLogStoreReport.setDestinationName(auditLogStore.getDestinationName());
                    auditLogStoreReport.setSupportedSeverities((auditLogStore.getSupportedSeverities()));
                    auditLogStoreReport.setImplementationClass(auditLogStore.getClass().getName());

                    storeReportList.add(auditLogStoreReport);
                }
            }

            if (! storeReportList.isEmpty())
            {
                report.setLogStoreReports(storeReportList);
            }
        }

        return report;
    }
}
