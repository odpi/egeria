/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
/**
 * This is the interface for the generic operations on data virtualization solutions
 */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.model.*;

import java.util.Date;
import java.util.List;

/**
 * Required methods expected to be implemented by any Data Engine Connector.
 */
public interface DataEngineInterface {

    /**
     * Retrieve the details about the data engine to which we are connected.
     *
     * @return DataEngineSoftwareServerCapability
     */
    DataEngineSoftwareServerCapability getDataEngineDetails();

    /**
     * Indicates whether the data engine requires polling (true) or is capable of notifying of changes on its own
     * (false).
     *
     * @return boolean
     */
    boolean requiresPolling();

    /**
     * Retrieve the date and time at which changes were last synchronized.
     *
     * @return Date
     */
    Date getChangesLastSynced();

    /**
     * Persist the date and time at which changes were last successfully synchronized.
     *
     * @param time the time to record for the last synchronization
     */
    void setChangesLastSynced(Date time);

    /**
     * Retrieve a list of the changed schema types between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEngineSchemaType>}
     */
    List<DataEngineSchemaType> getChangedSchemaTypes(Date from, Date to);

    /**
     * Retrieve a list of the changed port implementations between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEnginePortImplementation>}
     */
    List<DataEnginePortImplementation> getChangedPortImplementations(Date from, Date to);

    /**
     * Retrieve a list of the changed port aliases between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEnginePortAlias>}
     */
    List<DataEnginePortAlias> getChangedPortAliases(Date from, Date to);

    /**
     * Retrieve a list of the changed processes between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEngineProcess>}
     */
    List<DataEngineProcess> getChangedProcesses(Date from, Date to);

    /**
     * Retrieve a list of the changed lineage mappings between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<DataEngineLineageMappings>}
     */
    List<DataEngineLineageMappings> getChangedLineageMappings(Date from, Date to);

}
