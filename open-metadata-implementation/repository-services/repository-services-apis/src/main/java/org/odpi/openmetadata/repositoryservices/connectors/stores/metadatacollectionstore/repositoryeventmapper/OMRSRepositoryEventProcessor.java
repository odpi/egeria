/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper;


import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessor;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEventProcessor;

/**
 * OMRSRepositoryEventProcessor describes the interface of a component that can process both TypeDef and Instance
 * events from an open metadata repository.
 */
public interface OMRSRepositoryEventProcessor extends OMRSTypeDefEventProcessor, OMRSInstanceEventProcessor
{
}
