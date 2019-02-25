/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

import com.ibm.gaiandb.Logger;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ranger.authorization.hadoop.config.RangerConfiguration;
import org.apache.ranger.plugin.audit.RangerDefaultAuditHandler;
import org.apache.ranger.plugin.model.RangerPolicy;
import org.apache.ranger.plugin.model.RangerServiceDef;
import org.apache.ranger.plugin.policyengine.RangerAccessRequest;
import org.apache.ranger.plugin.policyengine.RangerAccessRequestImpl;
import org.apache.ranger.plugin.policyengine.RangerAccessResult;
import org.apache.ranger.plugin.service.RangerBasePlugin;

import java.util.List;
import java.util.Set;

import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.*;

public class RangerGaianAuthorizer implements GaianAuthorizer {

    private static final Logger logger = new Logger("RangerGaianAuthorizer", 25);
    private static volatile RangerBasePlugin gaianPlugin;
    private RangerServerProperties rangerServerProperties = loadRangerServerProperties();

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

    public RangerServerProperties getRangerServerProperties(){
        return rangerServerProperties;
    }

    private RangerServerProperties loadRangerServerProperties() {
        RangerServerProperties serverProperties = new RangerServerProperties();

        if(RangerConfiguration.getInstance() != null){
            String rangerURL = RangerConfiguration.getInstance().get("ranger.plugin.gaian.policy.rest.url");
            if(rangerURL != null){
                serverProperties.setServerURL(rangerURL);
            }

            String rangerAuthorization = RangerConfiguration.getInstance().get("ranger.plugin.gaian.policy.rest.authorization");
            if(rangerAuthorization != null) {
                serverProperties.setServerAuthorization(rangerAuthorization);
            }
        }

        return serverProperties;
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
        RangerAccessResult result = getRangerDataMaskResult(queryContext, columnName);
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

    private String getCustomMaskType(String columnName, RangerAccessResult result) {
        String maskedValue = result.getMaskedValue();

        if (maskedValue == null) {
            return NULL_MASK_TYPE;
        } else {
            return maskedValue.replace("{col}", columnName);
        }
    }

    private String getTransformer(RangerAccessResult result) {
        RangerServiceDef.RangerDataMaskTypeDef maskTypeDef = result.getMaskTypeDef();

        if (maskTypeDef != null) {
            return maskTypeDef.getTransformer();
        }

        return null;
    }

    private RangerAccessResult getRangerDataMaskResult(QueryContext queryContext, String columnName) {
        GaianResourceType objectType = GaianResourceType.COLUMN;
        RangerGaianResource resource = new RangerGaianResource(objectType, queryContext.getSchema(), queryContext.getTableName(), columnName);
        String user = queryContext.getUser();
        Set<String> groups = queryContext.getUserGroups();
        RangerGaianAccessRequest request = new RangerGaianAccessRequest(resource, queryContext.getActionType(), user, groups);

        return gaianPlugin.evalDataMaskPolicies(request, new RangerDefaultAuditHandler());
    }

    private boolean isDataMaskEnabled(RangerAccessResult result) {
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
