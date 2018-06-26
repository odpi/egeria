/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.common;


import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.EnterpriseOMRSConnectorProvider;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.EnterpriseOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.ArrayList;

public class OMRSMetadataCollectionHelper {

    static OMRSMetadataCollection oMRSMetadataCollection =null;

    public OMRSMetadataCollectionHelper() {
        if (oMRSMetadataCollection !=null) {
            ConnectorType connectortype = new ConnectorType(null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    EnterpriseOMRSConnectorProvider.class.toString());

            Connection connection = new Connection(null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                     connectortype,
                    null,
                    null);

            ConnectorBroker connectorBroker = new ConnectorBroker();
            EnterpriseOMRSRepositoryConnector connector = null;
            try {
                connector = (EnterpriseOMRSRepositoryConnector) connectorBroker.getConnector(connection);
                // check for null so unit tests will run without null pointering
                oMRSMetadataCollection = connector.getMetadataCollection();
            } catch (ConnectionCheckedException e) {
                e.printStackTrace();
            } catch (ConnectorCheckedException e) {
                e.printStackTrace();
            } catch (RepositoryErrorException e) {
                e.printStackTrace();
            }
        }
    }

    public EntityDetail callOMRSGetEntityByGuid(String userId, String entityGUID)
            throws UserNotAuthorizedException,
            RepositoryErrorException,
            InvalidParameterException,
            EntityNotKnownException,
            ConnectorCheckedException,
            ConnectionCheckedException,
            EntityProxyOnlyException {
        return oMRSMetadataCollection.getEntityDetail(userId,entityGUID);
    }
    public EntityDetail classifyEntity(String               userId,
                                       String               entityGUID,
                                       String classificationName,
                                       InstanceProperties instanceProperties
    ) throws UserNotAuthorizedException,
            RepositoryErrorException,
            InvalidParameterException,
            EntityNotKnownException,
            ConnectorCheckedException,
            ConnectionCheckedException,
            ClassificationErrorException,
            PropertyErrorException {
        return oMRSMetadataCollection.classifyEntity(userId,entityGUID,classificationName,instanceProperties);
    }
    public EntityDetail deClassifyEntity(String               userId,
                                         String               entityGUID,
                                         String classificationName
    ) throws UserNotAuthorizedException,
            RepositoryErrorException,
            InvalidParameterException,
            EntityNotKnownException,
            ConnectorCheckedException,
            ConnectionCheckedException,
            ClassificationErrorException,
            PropertyErrorException {

        return oMRSMetadataCollection.declassifyEntity(userId,entityGUID,classificationName);
    }

    public EntityDetail addEntity(String userid, EntityDetail entityDetail)
            throws ClassificationErrorException,
            StatusNotSupportedException,
            UserNotAuthorizedException,
            InvalidParameterException,
            RepositoryErrorException,
            PropertyErrorException,
            TypeErrorException {
        InstanceProperties instanceProperties = entityDetail.getProperties();
        return oMRSMetadataCollection.addEntity(userid,entityDetail.getType().getTypeDefGUID(),instanceProperties,new ArrayList(), InstanceStatus.ACTIVE);


    }
    public EntityDetail updateEntity(String userid, EntityDetail entityDetail)
            throws ClassificationErrorException,
            StatusNotSupportedException,
            UserNotAuthorizedException,
            InvalidParameterException,
            RepositoryErrorException,
            PropertyErrorException,
            TypeErrorException,
            EntityNotKnownException {
        InstanceProperties instanceProperties = entityDetail.getProperties();
        return oMRSMetadataCollection.updateEntityProperties(userid,entityDetail.getGUID(),instanceProperties);
    }
    public void  deleteEntity(String userid,String typeDefName, String typeDefGuid, String guid)
            throws ClassificationErrorException,
            StatusNotSupportedException,
            UserNotAuthorizedException,
            InvalidParameterException,
            RepositoryErrorException,
            PropertyErrorException,
            TypeErrorException,
            EntityNotKnownException,
            FunctionNotSupportedException {
        oMRSMetadataCollection.deleteEntity(userid,typeDefName,typeDefGuid,guid);
    }

    public String getTypeGuid(String userid, String typeName)
            throws ClassificationErrorException,
            StatusNotSupportedException,
            UserNotAuthorizedException,
            InvalidParameterException,
            RepositoryErrorException,
            PropertyErrorException,
            TypeErrorException,
            TypeDefNotKnownException {
        TypeDef typeDef = oMRSMetadataCollection.getTypeDefByName(userid,typeName);
        return typeDef.getGUID().toString();
    }
}
