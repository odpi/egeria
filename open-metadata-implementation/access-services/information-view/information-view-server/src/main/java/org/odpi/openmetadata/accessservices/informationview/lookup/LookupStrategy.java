/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

public interface LookupStrategy {

    EntityDetail lookup(Source source) throws UserNotAuthorizedException, FunctionNotSupportedException,
                                              InvalidParameterException, RepositoryErrorException,
                                              PropertyErrorException, TypeErrorException, PagingErrorException;
}
