/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.cassandra;

import com.datastax.driver.core.*;

/**
 * The interface of Cassandra store listener.
 */
public interface CassandraStoreListener extends SchemaChangeListener{

    @Override
    default void onKeyspaceAdded(KeyspaceMetadata keyspaceMetadata) {

    }

    @Override
    default void onKeyspaceRemoved(KeyspaceMetadata keyspaceMetadata) {

    }

    @Override
    default void onKeyspaceChanged(KeyspaceMetadata keyspaceMetadata, KeyspaceMetadata keyspaceMetadata1) {

    }

    @Override
    default void onTableAdded(TableMetadata tableMetadata) {

    }

    @Override
    default void onTableRemoved(TableMetadata tableMetadata) {

    }

    @Override
    default void onTableChanged(TableMetadata tableMetadata, TableMetadata tableMetadata1) {

    }

    @Override
    default void onUserTypeAdded(UserType userType) {

    }

    @Override
    default void onUserTypeRemoved(UserType userType) {

    }

    @Override
    default void onUserTypeChanged(UserType userType, UserType userType1) {

    }

    @Override
    default void onFunctionAdded(FunctionMetadata functionMetadata) {

    }

    @Override
    default void onFunctionRemoved(FunctionMetadata functionMetadata) {

    }

    @Override
    default void onFunctionChanged(FunctionMetadata functionMetadata, FunctionMetadata functionMetadata1) {

    }

    @Override
    default void onAggregateAdded(AggregateMetadata aggregateMetadata) {

    }

    @Override
    default void onAggregateRemoved(AggregateMetadata aggregateMetadata) {

    }

    @Override
    default void onAggregateChanged(AggregateMetadata aggregateMetadata, AggregateMetadata aggregateMetadata1) {

    }

    @Override
    default void onMaterializedViewAdded(MaterializedViewMetadata materializedViewMetadata) {

    }

    @Override
    default void onMaterializedViewRemoved(MaterializedViewMetadata materializedViewMetadata) {

    }

    @Override
    default void onMaterializedViewChanged(MaterializedViewMetadata materializedViewMetadata, MaterializedViewMetadata materializedViewMetadata1) {

    }

    @Override
    default void onRegister(Cluster cluster) {

    }

    @Override
    default void onUnregister(Cluster cluster) {

    }
}
