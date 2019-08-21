/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewSource;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class LookupBasedOnDataView implements LookupStrategy{

    private static final Logger log = LoggerFactory.getLogger(DataViewSource.class);
    private OMEntityDao omEntityDao;


    public LookupBasedOnDataView(OMEntityDao omEntityDao) {
        this.omEntityDao = omEntityDao;
    }

    @Override
    public EntityDetail lookup(Source source) {
        if (!(source instanceof DataViewColumnSource)) {
            log.error("Source is not a DataViewSourceColumn");
            return null;
        } else {
            String qualifiedName;
            if (StringUtils.isEmpty(source.getQualifiedName())) {
                qualifiedName = buildQualifiedName((DataViewColumnSource) source);
            } else {
                qualifiedName = source.getQualifiedName();
            }
            return omEntityDao.getEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, qualifiedName, false);
        }
    }

    private String buildQualifiedName(DataViewColumnSource source) {
        DataViewSource dataViewSource = source.getDataViewSource();
        return  QualifiedNameUtils.buildQualifiedNameForDataViewColumn(dataViewSource.getEndpointSource().getNetworkAddress(), dataViewSource.getId(), source.getId() );
    }
}
