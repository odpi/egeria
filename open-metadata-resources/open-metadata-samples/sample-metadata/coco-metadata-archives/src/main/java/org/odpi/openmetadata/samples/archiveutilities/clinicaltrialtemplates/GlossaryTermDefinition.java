/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.clinicaltrialtemplates;


/**
 * The GlossaryTermDefinition is used to populate the teddy bear drop foot glossary.
 */
public enum GlossaryTermDefinition
{
    /**
     * Unique identifier of patient.
     */
    PATIENT_IDENTIFIER("Patient Identifier",
        "Unique identifier of patient.",
        "Unique identifier for the teddy bear that has agree to take part in this study. " +
                               " The identifier on its own is anonymous, preserving the privacy of the patient.  However, there" +
                               " is a database that ties the patient identifier to the name of the bear.",
        "PatientId",
        GlossaryCategoryDefinition.DATA_FIELDS),


    /**
     * The date that the measurement was made.
     */
    MEASUREMENT_DATE("Measurement Date",
                       "The date that the measurement was made.",
                       "The measurements are from a discrete day so that any changes in a patient's condition" +
                             " can be measured.  The format of the date may" +
                             " vary depending on the source of the measurement.",
                       "MeasurementDate",
                       GlossaryCategoryDefinition.DATA_FIELDS),


    /**
     * The number of degrees of rotation of the left foot measured from vertical.
     */
    ANGLE_LEFT("Left Hip Rotation Angle",
                       "The number of degrees of rotation of the left foot measured from vertical.",
                       "As the stuffing around the hip weakens, the foot on the attached leg rotates," +
                       " typically outwards when the teddy bear is sitting.  This measurement is an integer" +
                       " measuring the number of degrees of rotation of the left foot measured from vertical." +
                       " Positive values measure that the foot is rotating outwards.",
                       "AngleLeft",
                       GlossaryCategoryDefinition.DATA_FIELDS),


    /**
     * The number of degrees of rotation of the right foot measured from vertical.
     */
    ANGLE_RIGHT("Right Hip Rotation Angle",
                       "The number of degrees of rotation of the right foot measured from vertical.",
                       "As the stuffing around the hip weakens, the foot on the attached leg rotates," +
                        " typically outwards when the teddy bear is sitting.  This measurement is an integer" +
                        " measuring the number of degrees of rotation of the right foot measured from vertical." +
                        " Positive values measure that the foot is rotating outwards.",
                       "AngleRight",
                       GlossaryCategoryDefinition.DATA_FIELDS),
    ;

    private final String                     name;
    private final String                     summary;
    private final String                     description;
    private final String                     abbreviation;
    private final GlossaryCategoryDefinition category;


    /**
     * The constructor creates an instance of the enum
     *
     * @param name   unique id for the enum
     * @param summary   name for the enum
     * @param description   description of the use of this value
     * @param abbreviation optional abbreviation
     * @param category optional category for the term
     */
    GlossaryTermDefinition(String                     name,
                           String                     summary,
                           String                     description,
                           String                     abbreviation,
                           GlossaryCategoryDefinition category)
    {
        this.name = name;
        this.summary = summary;
        this.description = description;
        this.abbreviation = abbreviation;
        this.category = category;
    }

    public String getQualifiedName()
    {
        return "GlossaryTerm:TeddyBearDropFootTerminology:" + name;
    }
    public String getTemplateSubstituteQualifiedName()
    {
        return "GlossaryTerm:TeddyBearDropFootTerminology:TemplateSubstitute:" + abbreviation;
    }


    public String getName()
    {
        return name;
    }


    public String getTemplateSubstituteName() { return name; }

    public String getSummary()
    {
        return summary;
    }


    public String getUsage() { return "Definition for use in the Teddy Bear Drop Foot demonstration study."; }


    public String getTemplateSubstituteUsage() { return "Only for use in templates."; }


    public String getDescription()
    {
        return description;
    }


    public String getTemplateSubstituteSummary() { return "Template Substitute for " + name; }

    public String getAbbreviation()
    {
        return abbreviation;
    }


    public GlossaryCategoryDefinition getCategory() { return category; }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "GlossaryTermDefinition{" + summary + '}';
    }
}
