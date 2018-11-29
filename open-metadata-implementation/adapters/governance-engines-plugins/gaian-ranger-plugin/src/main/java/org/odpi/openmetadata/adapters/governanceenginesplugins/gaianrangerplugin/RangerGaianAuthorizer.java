/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.ranger.plugin.audit.RangerDefaultAuditHandler;
import org.apache.ranger.plugin.model.RangerPolicy;
import org.apache.ranger.plugin.model.RangerServiceDef;
import org.apache.ranger.plugin.policyengine.RangerAccessRequest;
import org.apache.ranger.plugin.policyengine.RangerAccessRequestImpl;
import org.apache.ranger.plugin.policyengine.RangerAccessResult;
import org.apache.ranger.plugin.policyengine.RangerDataMaskResult;
import org.apache.ranger.plugin.service.RangerBasePlugin;
import org.apache.ranger.plugin.util.RangerPerfTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;


public class RangerGaianAuthorizer implements GaianAuthorizer {
    private static final Logger LOG = LoggerFactory.getLogger(RangerGaianAuthorizer.class);
    private static final Log PERF_GAIANAUTH_REQUEST_LOG = RangerPerfTracer.getPerfLogger("gaianauth.request");
    private static boolean isDebugEnabled = LOG.isDebugEnabled();
    private static volatile RangerBasePlugin gaianPlugin = null;

    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("==> RangerGaianPlugin.init()");
        }

        RangerBasePlugin plugin = gaianPlugin;

        if (plugin == null) {
            synchronized (RangerGaianPlugin.class) {
                plugin = gaianPlugin;

                if (plugin == null) {
                    plugin = new RangerGaianPlugin();
                    plugin.init();
                    plugin.setResultProcessor(new RangerDefaultAuditHandler());
                    gaianPlugin = plugin;

                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("<== RangerGaianPlugin.init()");
        }
    }

    public boolean isAuthorized(QueryContext queryContext) throws GaianAuthorizationException{
        boolean isAuthorized = true;
        GaianResourceType resourceType = getGaianResourceType(queryContext.getResourceType());
        String accessType = queryContext.getActionType();
        if (!accessType.equals("SELECT")) {
            throw new GaianAuthorizationException("GaianAccessType is invalid!");
        }
        if (isDebugEnabled) {
            LOG.debug("==> isAuthorized( " + queryContext + " )");
        }
        RangerPerfTracer perf = null;

        if(RangerPerfTracer.isPerfTraceEnabled(PERF_GAIANAUTH_REQUEST_LOG)) {
            perf = RangerPerfTracer.getPerfTracer(PERF_GAIANAUTH_REQUEST_LOG, "RangerGaianAuthorizer.isAuthorized(queryContext=" + queryContext + ")");
        }

        if (resourceType == GaianResourceType.COLUMN) {

            for (String col : queryContext.getColumns()) {
                RangerGaianResource resource = new RangerGaianResource(resourceType, queryContext.getSchema(),
                        queryContext.getTableName(), col);


                RangerAccessRequest request = new RangerGaianAccessRequest(resource, accessType, queryContext.getUser(), queryContext.getUserGroups());

                RangerAccessResult result = gaianPlugin.isAccessAllowed(request);

                if (result == null || !result.getIsAllowed()) {
                    isAuthorized = false;
                    break;
                }
            }
        } else if (resourceType == GaianResourceType.TABLE) {
            RangerGaianResource resource = new RangerGaianResource(resourceType, queryContext.getSchema(),
                    queryContext.getTableName());


            RangerAccessRequest request = new RangerGaianAccessRequest(resource, accessType, queryContext.getUser(), queryContext.getUserGroups());

            RangerAccessResult result = gaianPlugin.isAccessAllowed(request);

            if (result == null || !result.getIsAllowed()) {
                isAuthorized = false;
            }
        } else if (resourceType == GaianResourceType.SCHEMA) {
            RangerGaianResource resource = new RangerGaianResource(resourceType, queryContext.getSchema());


            RangerAccessRequest request = new RangerGaianAccessRequest(resource, accessType, queryContext.getUser(), queryContext.getUserGroups());

            RangerAccessResult result = gaianPlugin.isAccessAllowed(request);

            if (result == null || !result.getIsAllowed()) {
                isAuthorized = false;
            }
        } else {
            throw new GaianAuthorizationException("GaianResourceType is invalid!");
        }


        RangerPerfTracer.log(perf);

        if (isDebugEnabled) {
            LOG.debug("<== isAuthorized Returning value :: " + isAuthorized);
        }


        return isAuthorized;
    }

    public GaianResourceType getGaianResourceType(String resourceType) {
        if (resourceType.equals("SCHEMA")) {
            return GaianResourceType.SCHEMA;
        } else if (resourceType.equals("TABLE")) {
            return GaianResourceType.TABLE;
        } else if (resourceType.equals("COLUMN")) {
            return GaianResourceType.COLUMN;
        } else {
            return GaianResourceType.NONE;
        }
    }

    public void applyRowFilterAndColumnMasking(QueryContext queryContext) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("==> applyRowFilterAndColumnMasking(" + queryContext + ")");
        }

        RangerPerfTracer perf = null;

        if(RangerPerfTracer.isPerfTraceEnabled(PERF_GAIANAUTH_REQUEST_LOG)) {
            perf = RangerPerfTracer.getPerfTracer(PERF_GAIANAUTH_REQUEST_LOG, "RangerGaianAuthorizer.applyRowFilterAndColumnMasking()");
        }

        boolean needToTransform = false;
        if (CollectionUtils.isNotEmpty(queryContext.getColumns())) {

            for (String column : queryContext.getColumns()) {
                boolean isColumnTransformed = addCellValueTransformerAndCheckIfTransformed(queryContext, column);

                if(LOG.isDebugEnabled()) {
                    LOG.debug("addCellValueTransformerAndCheckIfTransformed(queryContext=" + queryContext + "): " + isColumnTransformed);
                }

                needToTransform = needToTransform || isColumnTransformed;
            }

        }

    }

    private boolean addCellValueTransformerAndCheckIfTransformed(QueryContext queryContext, String columnName) {

        if(LOG.isDebugEnabled()) {
            LOG.debug("==> addCellValueTransformerAndCheckIfTransformed(queryContext=" + queryContext + ", " + columnName + ")");
        }

        boolean ret = false;
        String columnTransformer = columnName;

        List<String> columnTransformers = queryContext.getColumnTransformers();

        String                  user           = queryContext.getUser();
        Set<String>             groups         = queryContext.getUserGroups();
        GaianResourceType          objectType     = GaianResourceType.COLUMN;
        RangerGaianResource      resource       = new RangerGaianResource(objectType, queryContext.getSchema(), queryContext.getTableName(), columnName);
        RangerGaianAccessRequest request        = new RangerGaianAccessRequest(resource, queryContext.getActionType(), user, groups);

        RangerDataMaskResult result = gaianPlugin.evalDataMaskPolicies(request, new RangerDefaultAuditHandler());

        ret = isDataMaskEnabled(result);

        if(ret) {
            String maskType = result.getMaskType();
            RangerServiceDef.RangerDataMaskTypeDef maskTypeDef = result.getMaskTypeDef();
            String transformer = null;
            if (maskTypeDef != null) {
                transformer = maskTypeDef.getTransformer();
            }

            if (StringUtils.equalsIgnoreCase(maskType, RangerPolicy.MASK_TYPE_NULL)) {
                columnTransformer = "NULL";
            } else if (StringUtils.equalsIgnoreCase(maskType, RangerPolicy.MASK_TYPE_CUSTOM)) {
                String maskedValue = result.getMaskedValue();

                if (maskedValue == null) {
                    columnTransformer = "NULL";
                } else {
                    columnTransformer = maskedValue.replace("{col}", columnName);
                }

            } else if (StringUtils.isNotEmpty(transformer)) {
                columnTransformer = transformer.replace("{col}", columnName);
            }

            /*
            String maskCondition = result.getMaskCondition();

            if(StringUtils.isNotEmpty(maskCondition)) {
                ret = "if(" + maskCondition + ", " + ret + ", " + columnName + ")";
            }
            */

        }

        columnTransformers.add(columnTransformer);

        if(LOG.isDebugEnabled()) {
            LOG.debug("<== addCellValueTransformerAndCheckIfTransformed(queryContext=" + queryContext + ", " + columnName + "): " + ret);
        }

        return ret;
    }

    private boolean isDataMaskEnabled(RangerDataMaskResult result) {
        return result != null && result.isMaskEnabled() && !StringUtils.equalsIgnoreCase(result.getMaskType(), RangerPolicy.MASK_TYPE_NONE);
    }

    public void cleanUp() {
        if (isDebugEnabled) {
            LOG.debug("==> cleanUp ");
        }
    }

}

enum GaianResourceType { NONE, SCHEMA, TABLE, COLUMN };

class RangerGaianPlugin extends RangerBasePlugin {
    RangerGaianPlugin() {
        super("gaian", "gaian");
    }
}

class RangerGaianAccessRequest extends RangerAccessRequestImpl {

    public RangerGaianAccessRequest(RangerGaianResource resource, String accessType, String user,
                                    Set<String> userGroups) {
        super(resource, accessType, user, userGroups);
    }

}
