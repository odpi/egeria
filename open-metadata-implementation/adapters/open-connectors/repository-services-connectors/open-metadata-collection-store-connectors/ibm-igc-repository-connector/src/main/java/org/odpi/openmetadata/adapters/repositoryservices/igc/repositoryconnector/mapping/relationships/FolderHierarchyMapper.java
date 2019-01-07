/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

public class FolderHierarchyMapper extends RelationshipMapping {

    private static class Singleton {
        private static final FolderHierarchyMapper INSTANCE = new FolderHierarchyMapper();
    }
    public static FolderHierarchyMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private FolderHierarchyMapper() {
        super(
                "data_file_folder",
                "data_file_folder",
                "data_file_folders",
                "parent_folder",
                "FolderHierarchy",
                "parentFolder",
                "nestedFolder"
        );
    }

}
