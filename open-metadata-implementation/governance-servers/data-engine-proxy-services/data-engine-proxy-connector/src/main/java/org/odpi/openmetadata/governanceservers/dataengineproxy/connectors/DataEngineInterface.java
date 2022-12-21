/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
/**
 * This is the interface for the generic operations on data virtualization solutions
 */
package org.odpi.openmetadata.governanceservers.dataengineproxy.connectors;

import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessHierarchy;
import org.odpi.openmetadata.accessservices.dataengine.model.Referenceable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.Engine;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

import java.util.Date;
import java.util.List;

/**
 * Required methods expected to be implemented by any Data Engine Connector.
 */
public interface DataEngineInterface {

    /**
     * Retrieve the details about the data engine to which we are connected.
     *
     * @return SoftwareCapability
     */
    Engine getDataEngineDetails();

    /**
     * Indicates whether the data engine requires polling (true) or is capable of notifying of changes on its own
     * (false).
     *
     * @return boolean
     */
    boolean requiresPolling();

    /**
     * Gets processing state sync key.
     *
     * @return the processing state sync key
     */
    String getProcessingStateSyncKey();

    /**
     * Retrieve the date and time at which changes were last synchronized.
     *
     * @return Date
     */
    Date getChangesLastSynced() throws ConnectorCheckedException, PropertyServerException;

    /**
     * Persist the date and time at which changes were last successfully synchronized.
     *
     * @param time the time to record for the last synchronization
     */
    void setChangesLastSynced(Date time) throws ConnectorCheckedException, PropertyServerException;

    /**
     * Retrieve the date of the oldest change since the time specified, or if there were no changes since the time
     * specified return null.
     *
     * @param time the time from which to look for the oldest change
     * @return Date
     */
    Date getOldestChangeSince(Date time) throws ConnectorCheckedException, PropertyServerException;

    /**
     * Retrieve a list of the changed schema types between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<SchemaType>}
     */
    List<SchemaType> getChangedSchemaTypes(Date from, Date to) throws ConnectorCheckedException, PropertyServerException;


    /**
     * Retrieve a list of the changed data stores between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<SchemaType>}
     */
    List<? super Referenceable> getChangedDataStores(Date from, Date to) throws ConnectorCheckedException, PropertyServerException;

    /**
     * Retrieve a list of the changed processes between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<Process>}
     */
    List<Process> getChangedProcesses(Date from, Date to) throws ConnectorCheckedException, PropertyServerException;

    /**
     * Retrieve a list of the changed process hierarchies between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<ProcessHierarchy>}
     */
    List<ProcessHierarchy> getChangedProcessHierarchies(Date from, Date to) throws ConnectorCheckedException, PropertyServerException;

    /**
     * Retrieve a list of the changed lineage mappings between the dates and times provided.
     *
     * @param from the date and time from which to look for changes (exclusive)
     * @param to the date and time up to which to look for changes (inclusive)
     * @return {@code List<LineageMapping>}
     */
    List<LineageMapping> getChangedLineageMappings(Date from, Date to) throws ConnectorCheckedException, PropertyServerException;

}
