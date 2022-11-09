/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.connectors;

import org.odpi.openmetadata.accessservices.dataengine.model.*;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Base implementation of a Data Engine Connector, implementing all the required methods any Data Engine Connector
 * is expected to provide (via the DataEngineInterface). It is an abstract class as on its own it does absolutely
 * nothing, and therefore a Data Engine Connector needs to extend it and override at least one of the methods to
 * actually do something.
 */
public abstract class DataEngineConnectorBase extends ConnectorBase implements DataEngineInterface {

    private static final Logger log = LoggerFactory.getLogger(DataEngineConnectorBase.class);

    /**
     * Default constructor
     */
    public DataEngineConnectorBase() { super(); }

    /**
     * Indicates whether the data engine requires polling (true) or is capable of notifying of changes on its own
     * (false).
     *
     * @return boolean
     */
    @Override
    public boolean requiresPolling() { return true; }

    @Override
    public String getProcessingStateSyncKey() {
        log.warn("DataEngineConnectorBase::getProcessingStateSyncKey() is not overridden (unimplemented).");
        return null;
    }

    /**
     * Retrieve the date and time at which changes were last synchronized.
     *
     * @return Date
     */
    @Override
    public Date getChangesLastSynced() throws ConnectorCheckedException, PropertyServerException {
        if (requiresPolling()) {
            log.warn("DataEngineConnectorBase::getChangesLastSynced() is not overridden (unimplemented), yet the connector requires polling.");
        }
        return null;
    }

    /**
     * Persist the date and time at which changes were last successfully synchronized.
     *
     * @param time the time to record for the last synchronization
     */
    @Override
    public void setChangesLastSynced(Date time) throws ConnectorCheckedException, PropertyServerException {
        if (requiresPolling()) {
            log.warn("DataEngineConnectorBase::setChangesLastSynced(Date) is not overridden (unimplemented), yet the connector requires polling.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getOldestChangeSince(Date time) throws ConnectorCheckedException, PropertyServerException {
        if (requiresPolling()) {
            log.warn("DataEngineConnectorBase::getOldestChangeSince(Date) is not overridden (unimplemented), yet the connector requires polling.");
        }
        return null;
    }

    /**
     * Retrieve a list of the changed schema types between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<SchemaType>}
     */
    @Override
    public List<SchemaType> getChangedSchemaTypes(Date from, Date to) throws ConnectorCheckedException, PropertyServerException {
        if (requiresPolling()) {
            log.debug("DataEngineConnectorBase::getChangedSchemaTypes(Date, Date) is not overridden (unimplemented).");
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve a list of the changed data stores between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<SchemaType>}
     */
    @Override
    public List<? super Referenceable> getChangedDataStores(Date from, Date to) throws ConnectorCheckedException, PropertyServerException {
        if (requiresPolling()) {
            log.debug("DataEngineConnectorBase::getChangedDataStores(Date, Date) is not overridden (unimplemented).");
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve a list of the changed processes between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<Process>}
     */
    @Override
    public List<Process> getChangedProcesses(Date from, Date to) throws ConnectorCheckedException, PropertyServerException {
        if (requiresPolling()) {
            log.debug("DataEngineConnectorBase::getChangedProcesses(Date, Date) is not overridden (unimplemented).");
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve a list of the changed process hierarchies between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<ProcessHierarchy>}
     */
    @Override
    public List<ProcessHierarchy> getChangedProcessHierarchies(Date from, Date to) throws ConnectorCheckedException, PropertyServerException{
        if (requiresPolling()) {
            log.debug("DataEngineConnectorBase::getChangedProcessHierarchies(Date, Date) is not overridden (unimplemented).");
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve a list of the changed lineage mappings between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<LineageMapping>}
     */
    @Override
    public List<LineageMapping> getChangedLineageMappings(Date from, Date to) throws ConnectorCheckedException, PropertyServerException {
        if (requiresPolling()) {
            log.debug("DataEngineConnectorBase::getChangedLineageMappings(Date, Date) is not overridden (unimplemented).");
        }
        return Collections.emptyList();
    }

}
