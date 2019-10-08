/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.metadataextractor.cassandra;


import com.datastax.oss.driver.api.core.metadata.schema.*;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.odpi.openmetadata.dataplatformservices.api.DataPlatformMetadataExtractor;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformDeployedDatabaseSchema;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformSoftwareServerCapability;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformTabularColumn;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformTabularSchema;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;

import java.util.Date;

public interface CassandraMetadataExtractor extends SchemaChangeListener, DataPlatformMetadataExtractor {

    @Override
    default void onKeyspaceCreated(@NonNull KeyspaceMetadata keyspaceMetadata) {

    }

    @Override
    default void onKeyspaceDropped(@NonNull KeyspaceMetadata keyspaceMetadata) {

    }

    @Override
    default void onKeyspaceUpdated(@NonNull KeyspaceMetadata keyspaceMetadata, @NonNull KeyspaceMetadata keyspaceMetadata1) {

    }

    @Override
    default void onTableCreated(@NonNull TableMetadata tableMetadata) {

    }

    @Override
    default void onTableDropped(@NonNull TableMetadata tableMetadata) {

    }

    @Override
    default void onTableUpdated(@NonNull TableMetadata tableMetadata, @NonNull TableMetadata tableMetadata1) {

    }

    @Override
    default void onUserDefinedTypeCreated(@NonNull UserDefinedType userDefinedType) {

    }

    @Override
    default void onUserDefinedTypeDropped(@NonNull UserDefinedType userDefinedType) {

    }

    @Override
    default void onUserDefinedTypeUpdated(@NonNull UserDefinedType userDefinedType, @NonNull UserDefinedType userDefinedType1) {

    }

    @Override
    default void onFunctionCreated(@NonNull FunctionMetadata functionMetadata) {

    }

    @Override
    default void onFunctionDropped(@NonNull FunctionMetadata functionMetadata) {

    }

    @Override
    default void onFunctionUpdated(@NonNull FunctionMetadata functionMetadata, @NonNull FunctionMetadata functionMetadata1) {

    }

    @Override
    default void onAggregateCreated(@NonNull AggregateMetadata aggregateMetadata) {

    }

    @Override
    default void onAggregateDropped(@NonNull AggregateMetadata aggregateMetadata) {

    }

    @Override
    default void onAggregateUpdated(@NonNull AggregateMetadata aggregateMetadata, @NonNull AggregateMetadata aggregateMetadata1) {

    }

    @Override
    default void onViewCreated(@NonNull ViewMetadata viewMetadata) {

    }

    @Override
    default void onViewDropped(@NonNull ViewMetadata viewMetadata) {

    }

    @Override
    default void onViewUpdated(@NonNull ViewMetadata viewMetadata, @NonNull ViewMetadata viewMetadata1) {

    }

    @Override
    default void close() throws Exception {

    }

    @Override
    default DataPlatformSoftwareServerCapability getDataPlatformSoftwareServerCapability() {
        return null;
    }

    @Override
    default DataPlatformDeployedDatabaseSchema getDataPlatformDeployedDatabaseSchema() {

        return null;
    }

    @Override
    default DataPlatformTabularSchema getDataPlatformTabularSchema() {
        return null;
    }

    @Override
    default DataPlatformTabularColumn getDataPlatformTabularColumn() {
        return null;
    }

    @Override
    default Date getChangesLastSynced() {
        return null;
    }

    @Override
    default void setChangesLastSynced(Date time) throws OCFRuntimeException {

    }
}
