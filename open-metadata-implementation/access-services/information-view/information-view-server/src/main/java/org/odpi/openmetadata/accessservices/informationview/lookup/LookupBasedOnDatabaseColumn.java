/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.events.DatabaseColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LookupBasedOnDatabaseColumn implements LookupStrategy {


    private static final Logger log = LoggerFactory.getLogger(LookupBasedOnDatabaseColumn.class);
    private LookupHelper lookupHelper;

    public LookupBasedOnDatabaseColumn(LookupHelper lookupHelper) {
        this.lookupHelper = lookupHelper;
    }

    @Override
    public EntityDetail lookup(Source source) {
        log.debug("Lookup based on source {0}", source);
        try {
            return lookupHelper.lookupDatabaseColumn((DatabaseColumnSource) source);
        } catch (EntityNotKnownException | UserNotAuthorizedException | FunctionNotSupportedException | InvalidParameterException | RepositoryErrorException | PropertyErrorException | TypeErrorException | PagingErrorException e) {
            throw new RuntimeException("Exception retrieving the entity based on source details", e);
        }
    }
}
