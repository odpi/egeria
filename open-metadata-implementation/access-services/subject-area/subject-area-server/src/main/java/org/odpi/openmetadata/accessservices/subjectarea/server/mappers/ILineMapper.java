/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Interface for mapping between a Subject Area OMAS Line to an OMRS Relationship
 */
public interface ILineMapper
{
    /**
     * Map from an OMRS Relationship to a Subject Area OMAS Line
     * @param relationship OMRS Lines
     * @return Subject Area OMAS Line
     * @throws InvalidParameterException a supplied parameter was null or invalid.
     */
    Line mapRelationshipToLine(Relationship relationship) throws InvalidParameterException;

    /**
     * Map from a Subject Area OMAS Line to an OMRS Relationship
     * @param line a Subject Area OMAS Line
     * @return  an OMRS Relationship
     * @throws InvalidParameterException a supplied parameter was null or invalid.
     */
     Relationship mapLineToRelationship(Line line)  throws InvalidParameterException;

    /**
     * Get the type name
     * @return type name
     */
     String getTypeName();
}
