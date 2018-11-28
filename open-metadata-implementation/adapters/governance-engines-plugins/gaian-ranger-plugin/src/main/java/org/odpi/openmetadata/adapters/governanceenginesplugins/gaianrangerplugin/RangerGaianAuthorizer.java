/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

import com.ibm.gaiandb.Logger;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ranger.plugin.audit.RangerDefaultAuditHandler;
import org.apache.ranger.plugin.model.RangerPolicy;
import org.apache.ranger.plugin.model.RangerServiceDef;
import org.apache.ranger.plugin.policyengine.RangerAccessRequest;
import org.apache.ranger.plugin.policyengine.RangerAccessRequestImpl;
import org.apache.ranger.plugin.policyengine.RangerAccessResult;
import org.apache.ranger.plugin.policyengine.RangerDataMaskResult;
import org.apache.ranger.plugin.service.RangerBasePlugin;

import java.util.List;
import java.util.Set;

import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.COLUMN_RESOURCE;
import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.DEFAULT_APP_ID;
import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.DEFAULT_SERVICE_TYPE;
import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.NULL_MASK_TYPE;
import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.SCHEMA_RESOURCE;
import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.SELECT_ACTION;
import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.TABLE_RESOURCE;

public class RangerGaianAuthorizer implements GaianAuthorizer {

    private static final Logger logger = new Logger("RangerGaianAuthorizer", 25);
    private static volatile RangerBasePlugin gaianPlugin = null;

    public void init() {
        logger.logDetail("==> RangerGaianPlugin.init()");
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
        logger.logDetail("<== RangerGaianPlugin.init()");
    }

    public boolean isAuthorized(QueryContext queryContext) throws GaianAuthorizationException {
        GaianResourceType resourceType = getGaianResourceType(queryContext.getResourceType());
        String accessType = queryContext.getActionType();
        logger.logDetail("Is Auth: access type is : " + accessType + " resource type is: " + resourceType + " [" + queryContext + "]");

        if (!accessType.equals(SELECT_ACTION)) {
            throw new GaianAuthorizationException("GaianAccessType is invalid!");
        }

        if (resourceType == GaianResourceType.COLUMN) {
            return isColumnAccessAllowed(queryContext, resourceType, accessType);
        } else if (resourceType == GaianResourceType.TABLE) {
            return isTableAccessAllowed(queryContext, resourceType, accessType);
        } else if (resourceType == GaianResourceType.SCHEMA) {
            return isSchemaAccessAllowed(queryContext, resourceType, accessType);
        } else {
            throw new GaianAuthorizationException("GaianResourceType is invalid!");
        }
    }


    public void applyRowFilterAndColumnMasking(QueryContext queryContext) {
        logger.logDetail("==> applyRowFilterAndColumnMasking(" + queryContext + ")");

        boolean needToTransform = false;
        if (CollectionUtils.isNotEmpty(queryContext.getColumns())) {
            for (String column : queryContext.getColumns()) {
                boolean isColumnTransformed = addCellValueTransformerAndCheckIfTransformed(queryContext, column);
                logger.logDetail("addCellValueTransformerAndCheckIfTransformed(queryContext=" + queryContext + "): " + isColumnTransformed);
                needToTransform = needToTransform || isColumnTransformed;
            }
        }
    }

    public void cleanUp() {
        logger.logDetail("==> cleanUp ");
    }

    private GaianResourceType getGaianResourceType(String resourceType) {

        switch (resourceType) {
            case SCHEMA_RESOURCE:
                return GaianResourceType.SCHEMA;
            case TABLE_RESOURCE:
                return GaianResourceType.TABLE;
            case COLUMN_RESOURCE:
                return GaianResourceType.COLUMN;
            default:
                return GaianResourceType.NONE;
        }
    }

    private boolean isColumnAccessAllowed(QueryContext queryContext, GaianResourceType resourceType, String accessType) {
        for (String col : queryContext.getColumns()) {
            RangerGaianResource resource = new RangerGaianResource(resourceType, queryContext.getSchema(), queryContext.getTableName(), col);
            boolean accessAllowed = isAccessAllowed(queryContext, accessType, resource);
            if (!accessAllowed) {
                return false;
            }
        }

        return true;
    }

    private boolean isTableAccessAllowed(QueryContext queryContext, GaianResourceType resourceType, String accessType) {
        RangerGaianResource resource = new RangerGaianResource(resourceType, queryContext.getSchema(), queryContext.getTableName());
        return isAccessAllowed(queryContext, accessType, resource);
    }

    private boolean isSchemaAccessAllowed(QueryContext queryContext, GaianResourceType resourceType, String accessType) {
        RangerGaianResource resource = new RangerGaianResource(resourceType, queryContext.getSchema());
        return isAccessAllowed(queryContext, accessType, resource);
    }

    private boolean isAccessAllowed(QueryContext queryContext, String accessType, RangerGaianResource resource) {
        RangerAccessRequest request = new RangerGaianAccessRequest(resource, accessType, queryContext.getUser(), queryContext.getUserGroups());
        RangerAccessResult result = gaianPlugin.isAccessAllowed(request);
        logger.logDetail("RangerAccessResult result: " + result);

        return result != null && result.getIsAllowed();
    }

    private boolean addCellValueTransformerAndCheckIfTransformed(QueryContext queryContext, String columnName) {

        logger.logDetail("==> addCellValueTransformerAndCheckIfTransformed(queryContext=" + queryContext + ", " + columnName + ")");
        String columnTransformer = columnName;
        List<String> columnTransformers = queryContext.getColumnTransformers();
        RangerDataMaskResult result = getRangerDataMaskResult(queryContext, columnName);
        boolean isDataMaskEnabled = isDataMaskEnabled(result);

        if (isDataMaskEnabled) {
            String transformer = getTransformer(result);
            String maskType = result.getMaskType();

            if (StringUtils.equalsIgnoreCase(maskType, RangerPolicy.MASK_TYPE_NULL)) {
                columnTransformer = NULL_MASK_TYPE;
            } else if (StringUtils.equalsIgnoreCase(maskType, RangerPolicy.MASK_TYPE_CUSTOM)) {
                columnTransformer = getCustomMaskType(columnName, result);
            } else if (StringUtils.isNotEmpty(transformer)) {
                columnTransformer = transformer.replace("{col}", columnName);
            }
        }

        columnTransformers.add(columnTransformer);
        logger.logDetail("<== addCellValueTransformerAndCheckIfTransformed(queryContext=" + queryContext + ", " + columnName + "): " + isDataMaskEnabled);

        return isDataMaskEnabled;
    }

    private String getCustomMaskType(String columnName, RangerDataMaskResult result) {
        String maskedValue = result.getMaskedValue();

        if (maskedValue == null) {
            return NULL_MASK_TYPE;
        } else {
            return maskedValue.replace("{col}", columnName);
        }
    }

    private String getTransformer(RangerDataMaskResult result) {
        RangerServiceDef.RangerDataMaskTypeDef maskTypeDef = result.getMaskTypeDef();

        if (maskTypeDef != null) {
            return maskTypeDef.getTransformer();
        }

        return null;
    }

    private RangerDataMaskResult getRangerDataMaskResult(QueryContext queryContext, String columnName) {
        GaianResourceType objectType = GaianResourceType.COLUMN;
        RangerGaianResource resource = new RangerGaianResource(objectType, queryContext.getSchema(), queryContext.getTableName(), columnName);
        String user = queryContext.getUser();
        Set<String> groups = queryContext.getUserGroups();
        RangerGaianAccessRequest request = new RangerGaianAccessRequest(resource, queryContext.getActionType(), user, groups);

        return gaianPlugin.evalDataMaskPolicies(request, new RangerDefaultAuditHandler());
    }

    private boolean isDataMaskEnabled(RangerDataMaskResult result) {
        return result != null && result.isMaskEnabled() && !StringUtils.equalsIgnoreCase(result.getMaskType(), RangerPolicy.MASK_TYPE_NONE);
    }

}

class RangerGaianPlugin extends RangerBasePlugin {

    RangerGaianPlugin() {
        super(DEFAULT_SERVICE_TYPE, DEFAULT_APP_ID);
    }
}

class RangerGaianAccessRequest extends RangerAccessRequestImpl {

    RangerGaianAccessRequest(RangerGaianResource resource, String accessType, String user, Set<String> userGroups) {
        super(resource, accessType, user, userGroups);
    }

}