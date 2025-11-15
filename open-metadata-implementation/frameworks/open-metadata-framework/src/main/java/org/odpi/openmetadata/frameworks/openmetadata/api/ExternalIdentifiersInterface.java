/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.api;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MetadataCorrelationHeader;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.SpecificationProperty;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ExternalIdentifiersInterface provides the interface for managing external identifiers.
 */
public interface ExternalIdentifiersInterface
{
    /**
     * Retrieve the specification for this element and a list of nested maps.  This method is used by the REST APIs.
     *
     * @param userId calling user
     * @param elementGUID element to query
     * @return map of reference data
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization issue
     */
    Map<String, List<SpecificationProperty>> getSpecification(String userId,
                                                              String elementGUID) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException;
}
