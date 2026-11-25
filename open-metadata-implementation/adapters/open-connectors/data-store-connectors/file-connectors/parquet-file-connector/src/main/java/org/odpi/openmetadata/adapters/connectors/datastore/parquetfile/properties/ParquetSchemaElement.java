/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * ParquetSchemaElement describes a field in the schema of a Parquet file.  A field is either a column that holds
 * values (called a leaf) or a group of nested fields.
 */
public class ParquetSchemaElement
{
    /*
     * The names of the groups used by the standard, and the legacy, representations of lists and maps.
     */
    private static final String LIST_TYPE          = "LIST";
    private static final String MAP_TYPE           = "MAP";
    private static final String MAP_KEY_VALUE_TYPE = "MAP_KEY_VALUE";
    private static final String REPEATED           = "REPEATED";
    private static final String LEGACY_ARRAY_NAME  = "array";
    private static final String LEGACY_TUPLE_NAME  = "_tuple";

    private String                           name            = null;
    private String                           pathName        = null;
    private String                           physicalType    = null;
    private Integer                          typeLength      = null;
    private String                           repetition      = null;
    private String                           convertedType   = null;
    private String                           logicalType     = null;
    private String                           logicalTypeName = null;
    private Integer                          scale           = null;
    private Integer                          precision       = null;
    private Integer                          fieldId         = null;
    private String                           timeUnit        = null;
    private Boolean                          adjustedToUTC   = null;
    private final List<ParquetSchemaElement> children        = new ArrayList<>();


    /**
     * Default constructor.
     */
    public ParquetSchemaElement()
    {
    }


    /**
     * Return the name of the field.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the field.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the names of each of the fields from the top-level field to this field, separated by dots.
     * This is the name used to match the field to the values stored for it.
     *
     * @return string
     */
    public String getPathName()
    {
        return pathName;
    }


    /**
     * Set up the dot-separated path name.
     *
     * @param pathName string
     */
    public void setPathName(String pathName)
    {
        this.pathName = pathName;
    }


    /**
     * Return the type used to store the values (BOOLEAN, INT32, INT64, INT96, FLOAT, DOUBLE, BYTE_ARRAY or
     * FIXED_LEN_BYTE_ARRAY).  It is null for a group.
     *
     * @return string
     */
    public String getPhysicalType()
    {
        return physicalType;
    }


    /**
     * Set up the type used to store the values.
     *
     * @param physicalType string
     */
    public void setPhysicalType(String physicalType)
    {
        this.physicalType = physicalType;
    }


    /**
     * Return the number of bytes in each value if the physical type is FIXED_LEN_BYTE_ARRAY.
     *
     * @return number of bytes or null
     */
    public Integer getTypeLength()
    {
        return typeLength;
    }


    /**
     * Set up the number of bytes in each value.
     *
     * @param typeLength number of bytes or null
     */
    public void setTypeLength(Integer typeLength)
    {
        this.typeLength = typeLength;
    }


    /**
     * Return whether the field is REQUIRED, OPTIONAL or REPEATED.
     *
     * @return string
     */
    public String getRepetition()
    {
        return repetition;
    }


    /**
     * Set up whether the field is REQUIRED, OPTIONAL or REPEATED.
     *
     * @param repetition string
     */
    public void setRepetition(String repetition)
    {
        this.repetition = repetition;
    }


    /**
     * Return whether a value can be missing for this field.
     *
     * @return boolean
     */
    public boolean isNullable()
    {
        return ! "REQUIRED".equals(repetition);
    }


    /**
     * Return the original style of annotation that describes how to interpret the values (such as UTF8 or
     * TIMESTAMP_MILLIS).  This is the only annotation in files written to version 1 of the format.  Newer
     * writers add it alongside the logical type, when there is an equivalent, so older readers still work.
     *
     * @return string or null
     */
    public String getConvertedType()
    {
        return convertedType;
    }


    /**
     * Set up the converted type.
     *
     * @param convertedType string or null
     */
    public void setConvertedType(String convertedType)
    {
        this.convertedType = convertedType;
    }


    /**
     * Return the description of the logical type annotation introduced in version 2 of the format, including
     * its parameters, such as TIMESTAMP(MICROS, UTC).
     *
     * @return string or null
     */
    public String getLogicalType()
    {
        return logicalType;
    }


    /**
     * Set up the description of the logical type.
     *
     * @param logicalType string or null
     */
    public void setLogicalType(String logicalType)
    {
        this.logicalType = logicalType;
    }


    /**
     * Return the name of the logical type annotation without its parameters, such as TIMESTAMP.
     *
     * @return string or null
     */
    public String getLogicalTypeName()
    {
        return logicalTypeName;
    }


    /**
     * Set up the name of the logical type.
     *
     * @param logicalTypeName string or null
     */
    public void setLogicalTypeName(String logicalTypeName)
    {
        this.logicalTypeName = logicalTypeName;
    }


    /**
     * Return the number of digits after the decimal point for a decimal.
     *
     * @return number or null
     */
    public Integer getScale()
    {
        return scale;
    }


    /**
     * Set up the number of digits after the decimal point for a decimal.
     *
     * @param scale number or null
     */
    public void setScale(Integer scale)
    {
        this.scale = scale;
    }


    /**
     * Return the total number of digits in a decimal.
     *
     * @return number or null
     */
    public Integer getPrecision()
    {
        return precision;
    }


    /**
     * Set up the total number of digits in a decimal.
     *
     * @param precision number or null
     */
    public void setPrecision(Integer precision)
    {
        this.precision = precision;
    }


    /**
     * Return the identifier assigned to the field by the writer.  Table formats such as Apache Iceberg use this
     * to track a column when it is renamed.
     *
     * @return number or null
     */
    public Integer getFieldId()
    {
        return fieldId;
    }


    /**
     * Set up the identifier assigned to the field by the writer.
     *
     * @param fieldId number or null
     */
    public void setFieldId(Integer fieldId)
    {
        this.fieldId = fieldId;
    }


    /**
     * Return the unit (MILLIS, MICROS or NANOS) that a time or timestamp is stored in.
     *
     * @return string or null
     */
    public String getTimeUnit()
    {
        return timeUnit;
    }


    /**
     * Set up the unit that a time or timestamp is stored in.
     *
     * @param timeUnit string or null
     */
    public void setTimeUnit(String timeUnit)
    {
        this.timeUnit = timeUnit;
    }


    /**
     * Return whether a time or timestamp is an instant on the UTC timeline (true) or a local time, with no
     * time zone, (false).
     *
     * @return boolean or null
     */
    public Boolean getAdjustedToUTC()
    {
        return adjustedToUTC;
    }


    /**
     * Set up whether a time or timestamp is an instant on the UTC timeline or a local time.
     *
     * @param adjustedToUTC boolean or null
     */
    public void setAdjustedToUTC(Boolean adjustedToUTC)
    {
        this.adjustedToUTC = adjustedToUTC;
    }


    /**
     * Return the fields nested in this field.  The list is empty for a leaf.
     *
     * @return list
     */
    public List<ParquetSchemaElement> getChildren()
    {
        return children;
    }


    /**
     * Return whether this field holds values (true) or is a group of other fields (false).
     *
     * @return boolean
     */
    public boolean isLeaf()
    {
        return physicalType != null;
    }


    /**
     * Return whether this field is an unsigned integer.
     *
     * @return boolean
     */
    public boolean isUnsigned()
    {
        if (convertedType != null && convertedType.startsWith("UINT_"))
        {
            return true;
        }

        return (logicalType != null) && (logicalType.startsWith("INTEGER(")) && (logicalType.endsWith("unsigned)"));
    }


    /**
     * Return the name of the annotation that describes how to interpret the field, whichever style of annotation
     * the writer used.
     *
     * @return logical type name, or the converted type, or null
     */
    public String getAnnotation()
    {
        if (logicalTypeName != null)
        {
            return logicalTypeName;
        }

        return convertedType;
    }


    /**
     * Return a description of the type of data stored in this field, using the names of types that are
     * familiar from programming languages and databases rather than the names of the Parquet encodings.
     * A nested field is described using array, map and struct.
     *
     * @return string
     */
    public String getDataType()
    {
        if (isLeaf())
        {
            String leafDataType = this.getLeafDataType();

            if (REPEATED.equals(repetition))
            {
                /*
                 * A repeated leaf, outside a list group, is how the protobuf style of writer represents a list.
                 */
                return "array<" + leafDataType + ">";
            }

            return leafDataType;
        }

        String annotation   = this.getAnnotation();
        String groupDataType;

        if ((LIST_TYPE.equals(annotation)) && (children.size() == 1))
        {
            groupDataType = "array<" + this.getListElement(children.get(0)).getElementDataType() + ">";
        }
        else if ((MAP_TYPE.equals(annotation) || MAP_KEY_VALUE_TYPE.equals(annotation)) &&
                 (children.size() == 1) && (children.get(0).getChildren().size() == 2))
        {
            ParquetSchemaElement keyValue = children.get(0);

            groupDataType = "map<" + keyValue.getChildren().get(0).getDataType() + "," + keyValue.getChildren().get(1).getDataType() + ">";
        }
        else
        {
            groupDataType = this.getStructDataType();
        }

        if (REPEATED.equals(repetition))
        {
            return "array<" + groupDataType + ">";
        }

        return groupDataType;
    }


    /**
     * Return the description of this field as the element of a list.  This ignores the fact that it is repeated
     * because that is what makes it a list.
     *
     * @return string
     */
    private String getElementDataType()
    {
        if (isLeaf())
        {
            return this.getLeafDataType();
        }

        if (REPEATED.equals(repetition))
        {
            return this.getStructDataType();
        }

        return this.getDataType();
    }


    /**
     * Return the field that describes the elements in a list.  The standard layout is a group, annotated as a LIST,
     * that contains a repeated group, that contains the element.  However, older writers used a variety of
     * layouts and the rules to recognize them are defined in the Parquet specification.
     *
     * @param repeatedField the single child of the list group
     * @return field describing the element
     */
    private ParquetSchemaElement getListElement(ParquetSchemaElement repeatedField)
    {
        if (repeatedField.isLeaf())
        {
            return repeatedField;
        }

        if (repeatedField.getChildren().size() != 1)
        {
            return repeatedField;
        }

        if ((LEGACY_ARRAY_NAME.equals(repeatedField.getName())) || ((name + LEGACY_TUPLE_NAME).equals(repeatedField.getName())))
        {
            return repeatedField;
        }

        return repeatedField.getChildren().get(0);
    }


    /**
     * Return the description of a group of fields.
     *
     * @return string
     */
    private String getStructDataType()
    {
        StringBuilder stringBuilder = new StringBuilder("struct<");
        boolean       firstChild    = true;

        for (ParquetSchemaElement child : children)
        {
            if (! firstChild)
            {
                stringBuilder.append(",");
            }

            stringBuilder.append(child.getName()).append(":").append(child.getDataType());
            firstChild = false;
        }

        stringBuilder.append(">");

        return stringBuilder.toString();
    }


    /**
     * Return the description of the type of data stored in a leaf field.
     *
     * @return string
     */
    private String getLeafDataType()
    {
        String annotation = this.getAnnotation();

        if (annotation != null)
        {
            switch (annotation)
            {
                case "STRING", "UTF8", "ENUM" -> { return "string"; }
                case "JSON"                   -> { return "json"; }
                case "BSON"                   -> { return "bson"; }
                case "UUID"                   -> { return "uuid"; }
                case "DATE"                   -> { return "date"; }
                case "FLOAT16"                -> { return "float"; }
                case "INTERVAL"               -> { return "interval"; }
                case "VARIANT"                -> { return "variant"; }
                case "GEOMETRY"               -> { return "geometry"; }
                case "GEOGRAPHY"              -> { return "geography"; }
                case "TIME", "TIME_MILLIS", "TIME_MICROS" -> { return "time"; }
                case "TIMESTAMP", "TIMESTAMP_MILLIS", "TIMESTAMP_MICROS" -> { return "timestamp"; }
                case "INT_8"                  -> { return "byte"; }
                case "INT_16"                 -> { return "short"; }
                case "UINT_8"                 -> { return "unsigned byte"; }
                case "UINT_16"                -> { return "unsigned short"; }
                case "UINT_32"                -> { return "unsigned int"; }
                case "UINT_64"                -> { return "unsigned long"; }
                case "DECIMAL"                ->
                {
                    if (precision != null)
                    {
                        return "decimal(" + precision + "," + ((scale == null) ? 0 : scale) + ")";
                    }

                    return "decimal";
                }
                case "INTEGER" ->
                {
                    /*
                     * The logical type description is INTEGER(bitWidth, signed|unsigned).
                     */
                    String prefix = this.isUnsigned() ? "unsigned " : "";

                    if (logicalType.startsWith("INTEGER(8,"))
                    {
                        return prefix + "byte";
                    }
                    if (logicalType.startsWith("INTEGER(16,"))
                    {
                        return prefix + "short";
                    }
                    if (logicalType.startsWith("INTEGER(32,"))
                    {
                        return prefix + "int";
                    }

                    return prefix + "long";
                }
                default -> { }
            }
        }

        switch (physicalType)
        {
            case "BOOLEAN" -> { return "boolean"; }
            case "INT32"   -> { return "int"; }
            case "INT64"   -> { return "long"; }
            case "INT96"   ->
            {
                /*
                 * INT96 was only ever used, by Apache Impala, Hive and early versions of Spark, to store a timestamp.
                 */
                return "timestamp";
            }
            case "FLOAT"   -> { return "float"; }
            case "DOUBLE"  -> { return "double"; }
            default        -> { return "binary"; }
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ParquetSchemaElement{" +
                "name='" + name + '\'' +
                ", pathName='" + pathName + '\'' +
                ", physicalType='" + physicalType + '\'' +
                ", typeLength=" + typeLength +
                ", repetition='" + repetition + '\'' +
                ", convertedType='" + convertedType + '\'' +
                ", logicalType='" + logicalType + '\'' +
                ", scale=" + scale +
                ", precision=" + precision +
                ", fieldId=" + fieldId +
                ", dataType='" + getDataType() + '\'' +
                ", children=" + children +
                '}';
    }
}
