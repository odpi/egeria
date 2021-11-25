/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

public class AddEntityProxyExecutor extends RepositoryExecutorBase{

    private final EntityProxy entityProxy;
    private final MaintenanceAccumulator maintenanceAccumulator;

    /**
     * Simple constructor - all methods have the userId in them
     *
     * @param userId      calling user
     * @param entityProxy entity proxy
     * @param auditLog    audit log
     * @param methodName  method name
     */
    public AddEntityProxyExecutor(String userId, EntityProxy entityProxy, AuditLog auditLog, String methodName) {
        super(userId, methodName);
        this.entityProxy = entityProxy;
        this.maintenanceAccumulator = new MaintenanceAccumulator(auditLog);
    }

    @Override
    public boolean issueRequestToRepository(String metadataCollectionId, OMRSMetadataCollection metadataCollection) {

        try {
            metadataCollection.addEntityProxy(userId, entityProxy);
            return true;
        } catch (InvalidParameterException e) {
            maintenanceAccumulator.captureException(e);
        } catch (RepositoryErrorException e) {
            maintenanceAccumulator.captureException(e);
        } catch (FunctionNotSupportedException e) {
            maintenanceAccumulator.captureException(e);
        } catch (UserNotAuthorizedException e) {
            e.printStackTrace();
        } catch (Exception e){
            maintenanceAccumulator.captureGenericException(methodName, metadataCollectionId, e);
        }

        return false;
    }
}
