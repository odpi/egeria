/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc;

public enum AssetType {

    RELATIONAL_COLUMN(4,"RelationalColumn",0),
    RELATIONAL_TABLE_TYPE(3,"RelationalTableType",1),
    RELATIONAL_TABLE(2,"RelationalTable",2),
    RELATIONAL_DB_SCHEMA_TYPE(1,"RelationalDBSchemaType",3),
    DEPLOYED_DATABASE_SCHEMA(0,"DeployedDatabaseSchema",4);

    private int elementesContained;
    private String type;
    private int indexForRelationships;

    AssetType(int elementesContained, String type, int indexForRelationships){
        this.elementesContained = elementesContained;
        this.type = type;
        this.indexForRelationships = indexForRelationships;
    }

    public int getElementesContained() {
        return elementesContained;
    }

    public String getType() {
        return type;
    }

    public int getIndexForRelationships() {
        return indexForRelationships;
    }
}
