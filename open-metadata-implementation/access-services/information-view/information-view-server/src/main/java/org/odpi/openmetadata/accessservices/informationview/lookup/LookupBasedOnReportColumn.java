/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.ReportColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.ReportSectionSource;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class LookupBasedOnReportColumn implements LookupStrategy {

    private static final Logger log = LoggerFactory.getLogger(ReportColumnSource.class);
    public static final String SEPARATOR = "::";
    private OMEntityDao omEntityDao;


    public LookupBasedOnReportColumn(OMEntityDao omEntityDao) {
        this.omEntityDao = omEntityDao;
    }

    @Override
    public EntityDetail lookup(Source source) {
        if (!(source instanceof ReportColumnSource)) {
            log.error("Source is not a ReportColumnSource");
            return null;
        } else {
            String qualifiedName;
            if (!StringUtils.isEmpty(source.getQualifiedName())) {
                qualifiedName = buildQualifiedName((ReportColumnSource) source);
            } else {
                qualifiedName = source.getQualifiedName();
            }
            return omEntityDao.getEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, qualifiedName, false);
        }
    }

    private String buildQualifiedName(ReportColumnSource source) {
        ReportSectionSource parentReportSection = source.getParentReportSection();
        StringBuilder builder = new StringBuilder();
        while (parentReportSection != null) {
            builder.insert(0, parentReportSection.getName() + SEPARATOR);
            if (parentReportSection.getParentReportSection() == null) {
                builder.insert(0, parentReportSection.getReportSource().getEndpointSource().getNetworkAddress() + SEPARATOR + parentReportSection.getReportSource().getReportId() + SEPARATOR);
            }
            parentReportSection = parentReportSection.getParentReportSection();
        }

        builder.append(source.getName());
        return builder.toString();
    }
}
