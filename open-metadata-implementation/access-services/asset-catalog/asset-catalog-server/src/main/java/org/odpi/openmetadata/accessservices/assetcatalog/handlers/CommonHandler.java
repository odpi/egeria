package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.REFERENCEABLE;

public class CommonHandler {

    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;

    CommonHandler(RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper) {
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
    }


    OMRSMetadataCollection getOMRSMetadataCollection() {
        return repositoryHandler.getMetadataCollection();
    }

    String getOMRSMetadataCollectionName(String userId) throws RepositoryErrorException {
        OMRSMetadataCollection metadataCollection = getOMRSMetadataCollection();
        String metadataCollectionId = metadataCollection.getMetadataCollectionId(userId);
        if (metadataCollectionId == null) {
            return null;
        }
        return repositoryHelper.getMetadataCollectionName(metadataCollectionId);
    }

    Set<String> collectSuperTypes(String userId, String typeDefName) {
        Set<String> superTypes = new HashSet<>();

        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);
        if (typeDefByName != null) {
            collectSuperTypes(userId, typeDefByName, superTypes);
        }

        return superTypes;
    }

    private void collectSuperTypes(String userId, TypeDef type, Set<String> superTypes) {
        if (type.getName().equals(REFERENCEABLE)) {
            return;
        }
        superTypes.add(type.getName());
        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, type.getSuperType().getName());
        if (typeDefByName != null) {
            collectSuperTypes(userId, typeDefByName, superTypes);
        }
    }

    /**
     * Returns the global identifier of the type using the type def name
     *
     * @param userId      user identifier that issues the call
     * @param typeDefName the type definition name
     * @return the global identifier of the type
     * @throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException - is thrown by the OMAG Service when a parameter is null or an invalid value
     */
    public String getTypeDefGUID(String userId, String typeDefName) {
        if (typeDefName == null) {
            return null;
        }

        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);
        return Optional.ofNullable(typeDefByName).map(TypeDefLink::getGUID).orElse(null);
    }
}
