/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidTypeDefException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefConflictException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

/**
 * The IGCOMRSMetadataCollection represents a remote IBM Information Governance Catalog (IGC)
 * metadata repository.  IGC supports its own native REST APIs.
 * Requests to this metadata collection are translated to the IGC REST API calls and the results are
 * transformed to OMRS objects before returning to the caller.
 */
public class IGCOMRSMetadataCollection extends OMRSMetadataCollectionBase
{

    //TODO Implement
    /**
     * Default constructor.
     *
     * @param parentConnector - connector that this metadata collection supports.  The connector has the information
     *                        to call the metadata repository.
     * @param metadataCollectionId  - unique identifier for the repository.
     */
    public IGCOMRSMetadataCollection(IGCOMRSRepositoryConnector parentConnector,
                                     String                            repositoryName,
                                     OMRSRepositoryHelper repositoryHelper,
                                     OMRSRepositoryValidator repositoryValidator,
                                     String                     metadataCollectionId)
    {
        /*
         * The metadata collection Id is the unique Id for the metadata collection.  It is managed by the super class.
         */
        super(metadataCollectionId);
        this.metadataCollectionId = metadataCollectionId;

        /*
         * Save parentConnector since this has the connection information.
         */
        this.parentConnector = parentConnector;
        this.repositoryValidator = repositoryValidator;
        this.repositoryHelper = repositoryHelper;
        this.repositoryName = repositoryName;
        /*
         * This is a temporary implementation to allow the structural implementation of the connectors to
         * be committed before the metadata collection implementation is complete.
         */

//        throw new NotImplementedRuntimeException("IGCOMRSMetadataCollection", "constructor", "ATLAS-1774");
    }

    @Override
    public boolean verifyAttributeTypeDef(String userId, AttributeTypeDef attributeTypeDef) throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefConflictException, InvalidTypeDefException, UserNotAuthorizedException {
        final String  methodName           = "verifyAttributeTypeDef";
        final String  typeDefParameterName = "attributeTypeDef";

        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateAttributeTypeDef(repositoryName, typeDefParameterName, attributeTypeDef, methodName);

        return true;//TODO implement this!!
    }

    @Override
    public boolean verifyTypeDef(String userId, TypeDef typeDef) throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefConflictException, InvalidTypeDefException, UserNotAuthorizedException {
        final String  methodName           = "verifyTypeDef";
        final String  typeDefParameterName = "typeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDef(repositoryName, typeDefParameterName, typeDef, methodName);

        return true;//TODO implement this!!
    }
}