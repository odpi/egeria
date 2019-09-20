/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class DataEngineConnectorBase extends ConnectorBase implements DataEngineInterface {

    private static final Logger log = LoggerFactory.getLogger(DataEngineConnectorBase.class);

    public DataEngineConnectorBase() { super(); }

    /**
     * Retrieve the details about the data engine to which we are connected.
     *
     * @return DataEngineSoftwareServerCapability
     */
    public DataEngineSoftwareServerCapability getDataEngineDetails() {
        log.warn("DataEngineConnectorBase::getDataEngineDetails() is not overridden (unimplemented).");
        return null;
    }

    /**
     * Retrieve the date and time at which changes were last synchronized.
     *
     * @return Date
     */
    public Date getChangesLastSynced() {
        log.warn("DataEngineConnectorBase::getChangesLastSynced() is not overridden (unimplemented).");
        return null;
    }

    /**
     * Persist the date and time at which changes were last successfully synchronized.
     *
     * @param time the time to record for the last synchronization
     * @throws OCFRuntimeException if there is any problem persisting the date and time
     */
    public void setChangesLastSynced(Date time) throws OCFRuntimeException {
        log.warn("DataEngineConnectorBase::setChangesLastSynced(Date) is not overridden (unimplemented).");
    }

    /**
     * Retrieve a list of the changed schema types between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEngineSchemaType>}
     */
    public List<DataEngineSchemaType> getChangedSchemaTypes(Date from, Date to) {
        log.warn("DataEngineConnectorBase::getChangedSchemaTypes(Date, Date) is not overridden (unimplemented).");
        return null;
    }

    /**
     * Retrieve a list of the changed port implementations between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEnginePortImplementation>}
     */
    public List<DataEnginePortImplementation> getChangedPortImplementations(Date from, Date to) {
        log.warn("DataEngineConnectorBase::getChangedPortImplementations(Date, Date) is not overridden (unimplemented).");
        return null;
    }

    /**
     * Retrieve a list of the changed port aliases between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEnginePortAlias>}
     */
    public List<DataEnginePortAlias> getChangedPortAliases(Date from, Date to) {
        log.warn("DataEngineConnectorBase::getChangedPortAliases(Date, Date) is not overridden (unimplemented).");
        return null;
    }

    /**
     * Retrieve a list of the changed processes between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEngineProcess>}
     */
    public List<DataEngineProcess> getChangedProcesses(Date from, Date to) {
        log.warn("DataEngineConnectorBase::getChangedProcesses(Date, Date) is not overridden (unimplemented).");
        return null;
    }

    /**
     * Retrieve a list of the changed lineage mappings between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEngineLineageMappings>}
     */
    public List<DataEngineLineageMappings> getChangedLineageMappings(Date from, Date to) {
        log.warn("DataEngineConnectorBase::getChangedLineageMappings(Date, Date) is not overridden (unimplemented).");
        return null;
    }

}
