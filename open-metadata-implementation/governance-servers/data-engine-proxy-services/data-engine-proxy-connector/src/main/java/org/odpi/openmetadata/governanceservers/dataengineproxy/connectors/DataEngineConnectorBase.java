/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.connectors;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.governanceservers.dataengineproxy.connectors.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Base implementation of a Data Engine Connector, implementing all of the required methods any Data Engine Connector
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
     * Retrieve the details about the data engine to which we are connected.
     *
     * @return DataEngineSoftwareServerCapability
     */
    public abstract DataEngineSoftwareServerCapability getDataEngineDetails();

    /**
     * Indicates whether the data engine requires polling (true) or is capable of notifying of changes on its own
     * (false).
     *
     * @return boolean
     */
    public boolean requiresPolling() { return true; }

    /**
     * Retrieve the date and time at which changes were last synchronized.
     *
     * @return Date
     */
    public Date getChangesLastSynced() {
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
    public void setChangesLastSynced(Date time) {
        if (requiresPolling()) {
            log.warn("DataEngineConnectorBase::setChangesLastSynced(Date) is not overridden (unimplemented), yet the connector requires polling.");
        }
    }

    /**
     * Retrieve a list of the changed schema types between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEngineSchemaType>}
     */
    public List<DataEngineSchemaType> getChangedSchemaTypes(Date from, Date to) {
        log.debug("DataEngineConnectorBase::getChangedSchemaTypes(Date, Date) is not overridden (unimplemented).");
        return Collections.emptyList();
    }

    /**
     * Retrieve a list of the changed port implementations between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEnginePortImplementation>}
     */
    public List<DataEnginePortImplementation> getChangedPortImplementations(Date from, Date to) {
        log.debug("DataEngineConnectorBase::getChangedPortImplementations(Date, Date) is not overridden (unimplemented).");
        return Collections.emptyList();
    }

    /**
     * Retrieve a list of the changed port aliases between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEnginePortAlias>}
     */
    public List<DataEnginePortAlias> getChangedPortAliases(Date from, Date to) {
        log.debug("DataEngineConnectorBase::getChangedPortAliases(Date, Date) is not overridden (unimplemented).");
        return Collections.emptyList();
    }

    /**
     * Retrieve a list of the changed processes between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEngineProcess>}
     */
    public List<DataEngineProcess> getChangedProcesses(Date from, Date to) {
        log.debug("DataEngineConnectorBase::getChangedProcesses(Date, Date) is not overridden (unimplemented).");
        return Collections.emptyList();
    }

    /**
     * Retrieve a list of the changed lineage mappings between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEngineLineageMappings>}
     */
    public List<DataEngineLineageMappings> getChangedLineageMappings(Date from, Date to) {
        log.debug("DataEngineConnectorBase::getChangedLineageMappings(Date, Date) is not overridden (unimplemented).");
        return Collections.emptyList();
    }

}
