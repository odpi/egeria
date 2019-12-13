/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog.exception;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;

import java.util.Objects;

/**
 * AssetCatalogException provides a checked exception for reporting errors found when using
 * the Asset Catalog OMAS services.
 * Typically these errors are either configuration or operational errors that can be fixed by an administrator
 * or power AssetConsumerInterface.  However, there may be the odd bug that surfaces here.
 * The AssetCatalogErrorCode can be used with this exception to populate it with standard messages.
 * The aim is to be able to uniquely identify the cause and remedy for the error.
 */
public class AssetCatalogException extends OCFCheckedExceptionBase {
    private final String[] assetGuid;


    public AssetCatalogException(int httpCode, String className, String actionDescription, String errorMessage,
                                 String systemAction, String userAction, String... assetGuid) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.assetGuid = assetGuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AssetCatalogException that = (AssetCatalogException) o;
        return Objects.equals(assetGuid, that.assetGuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), assetGuid);
    }

    @Override
    public String toString() {
        return "DataEngineException{" +
                "assetGuid='" + assetGuid + '\'' +
                '}';
    }

    public String[] getAssetGuid() {
        return assetGuid;
    }
}