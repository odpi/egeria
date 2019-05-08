/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.governanceservers.openlineage.util;

public final class Constants {

    private Constants() {
    }

    //Asset Types
    public static final String ASSET = "Asset";
    public static final String GLOSSARY_TERM = "GlossaryTerm";

    //Relationships Type
    public static final String SCHEMA_ATTRIBUTE_TYPE = "SchemaAttributeType";
    public static final String ATTRIBUTE_FOR_SCHEMA = "AttributeForSchema";
    public static final String COMPLEX_SCHEMA_TYPE = "ComplexSchemaType";
    public static final String ASSET_SCHEMA_TYPE = "AssetSchemaType";
    public static final String CONNECTION_TO_ASSET = "ConnectionToAsset";
    public static final String DATA_CONTENT_FOR_DATA_SET = "DataContentForDataSet";
    public static final String SEMANTIC_ASSIGNMENT = "SemanticAssignment";

    //Instance Properties fields
    public static final String TYPE = "dataType";
    public static final String QUALIFIED_NAME = "qualifiedName";
    public static final String NAME = "name";

    //File names
    public static final String GRAPHML = "lineageGraph.graphml";
}