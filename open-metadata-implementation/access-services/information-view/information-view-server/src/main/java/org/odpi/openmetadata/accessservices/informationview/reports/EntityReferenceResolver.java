/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.accessservices.informationview.lookup.*;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildRetrieveEntityException;

public class EntityReferenceResolver {


    private OMEntityDao omEntityDao;
    private Map<String, LookupStrategy> strategies = new HashMap<>();
    private LookupBasedOnDatabaseColumn lookupBasedOnDatabaseColumn;
    private LookupBasedOnReportColumn lookupBasedOnReportColumn;
    private LookupBasedOnDataView lookupBasedOnDataView;

    public EntityReferenceResolver(LookupHelper lookupHelper, OMEntityDao omEntityDao) {
        lookupBasedOnDatabaseColumn = new LookupBasedOnDatabaseColumn(lookupHelper);
        lookupBasedOnReportColumn = new LookupBasedOnReportColumn(omEntityDao);
        lookupBasedOnDataView = new LookupBasedOnDataView(omEntityDao);
        this.omEntityDao = omEntityDao;
        buildStrategies();
    }

    private void buildStrategies() {
        strategies.put(DatabaseColumnSource.class.getName(), lookupBasedOnDatabaseColumn);
        strategies.put(ReportColumnSource.class.getName(), lookupBasedOnReportColumn);
        strategies.put(DataViewColumnSource.class.getName(), lookupBasedOnDataView);
    }


    /**
     *
     * @param source - object used to describe the source of the report column
     * @return
     */
    public String resolveSourceGuid(Source source)  {
        if (source == null)
            return null;
        if (!StringUtils.isEmpty(source.getGuid())) {
            return source.getGuid();
        }
        if (!StringUtils.isEmpty(source.getQualifiedName())) {
            return omEntityDao.getEntity(source.getClass().getName(), source.getQualifiedName(), false).getGUID();
        }
        LookupStrategy strategy = strategies.get(source.getClass().getName());
        if (strategy != null) {
            EntityDetail entity = strategy.lookup(source);
            if (entity != null)
                return entity.getGUID();
        }
        return null;
    }


    /**
     *
     * @param businessTerm - object describing the business term
     * @return
     */
    public String getBusinessTermGuid(BusinessTerm businessTerm)  {
        if (businessTerm == null)
            return null;
        if (!StringUtils.isEmpty(businessTerm.getGuid())) {
            return businessTerm.getGuid();
        }
        if (!StringUtils.isEmpty(businessTerm.getQualifiedName())) {
            EntityDetail entity = omEntityDao.getEntity(Constants.BUSINESS_TERM, businessTerm.getQualifiedName(),
                    false);
            return entity.getGUID();
        }
        throw buildRetrieveEntityException(Constants.GUID, businessTerm.getGuid(), null, this.getClass().getName());
    }


}
