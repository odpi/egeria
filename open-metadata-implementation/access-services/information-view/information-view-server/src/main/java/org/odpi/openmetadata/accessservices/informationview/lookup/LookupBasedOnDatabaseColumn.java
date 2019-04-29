/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.events.DatabaseColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.EntityNotFoundException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
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
        if(log.isDebugEnabled()) {
            log.debug("Lookup based on source {0}", source);
        }
        try {
            return lookupHelper.lookupDatabaseColumn((DatabaseColumnSource) source);
        } catch (EntityNotKnownException | UserNotAuthorizedException | FunctionNotSupportedException | InvalidParameterException | RepositoryErrorException | PropertyErrorException | TypeErrorException | PagingErrorException e) {
            throw new EntityNotFoundException(InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getHttpErrorCode(),
                    EntityLookup.class.getName(),
                    InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getFormattedErrorMessage("source", source.toString()),
                    InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getSystemAction(),
                    InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getUserAction(),
                    null);
        }
    }
}
