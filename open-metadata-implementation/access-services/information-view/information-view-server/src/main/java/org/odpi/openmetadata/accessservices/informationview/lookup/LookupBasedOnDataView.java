/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class LookupBasedOnDataView implements LookupStrategy{

    private static final Logger log = LoggerFactory.getLogger(DataViewSource.class);
    public static final String SEPARATOR = "::";
    private OMEntityDao omEntityDao;


    public LookupBasedOnDataView(OMEntityDao omEntityDao) {
        this.omEntityDao = omEntityDao;
    }

    @Override
    public EntityDetail lookup(Source source) throws UserNotAuthorizedException,
                                                     FunctionNotSupportedException,
                                                     InvalidParameterException,
                                                     RepositoryErrorException,
                                                     PropertyErrorException,
                                                     TypeErrorException,
                                                     PagingErrorException {
        if (!(source instanceof DataViewColumnSource)) {
            log.error("Source is not a DataViewSourceColumn");
            return null;
        } else {
            String qualifiedName = buildQualifiedName((DataViewColumnSource) source);
            return omEntityDao.getEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, qualifiedName);
        }
    }

    private String buildQualifiedName(DataViewColumnSource source) {
        DataViewSource dataViewSource = source.getDataViewSource();
        StringBuilder builder = new StringBuilder();
        if(!StringUtils.isEmpty(dataViewSource.getNetworkAddress())){
            builder = builder.append(dataViewSource.getNetworkAddress() + SEPARATOR);
        }

        builder.append(dataViewSource.getName());
        builder.append(SEPARATOR);
        builder.append(source.getId());

        return builder.toString();
    }
}
