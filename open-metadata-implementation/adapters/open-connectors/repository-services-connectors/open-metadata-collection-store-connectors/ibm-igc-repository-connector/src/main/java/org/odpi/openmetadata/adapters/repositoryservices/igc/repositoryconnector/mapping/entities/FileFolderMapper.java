/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.ConnectionToAssetMapper_FileFolder;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.FolderHierarchyMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.NestedFileMapper;

public class FileFolderMapper extends ReferenceableMapper {

    public FileFolderMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "data_file_folder",
                "Data File Folder",
                "FileFolder",
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "displayName");
        addSimplePropertyMapping("short_description", "description");

        // The classes to use for mapping any relationships
        addRelationshipMapper(FolderHierarchyMapper.getInstance());
        addRelationshipMapper(NestedFileMapper.getInstance());
        addRelationshipMapper(ConnectionToAssetMapper_FileFolder.getInstance());

    }

}
