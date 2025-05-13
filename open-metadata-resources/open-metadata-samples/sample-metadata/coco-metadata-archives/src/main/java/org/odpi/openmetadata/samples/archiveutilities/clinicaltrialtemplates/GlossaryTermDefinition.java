/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.clinicaltrialtemplates;


/**
 * The GlossaryTermDefinition is used to populate the teddy bear drop foot glossary.  Because these glossary terms are
 * used in templates, two versions of each term are produced - the first is the pure term and the second is a
 * template substitute that is used in templates - to ensure that semantic assignments to the template elements are kept
 * apart from the genuine semantic assignments.
 */
public enum GlossaryTermDefinition
{
    /**
     * Unique identifier of patient.
     */
    PATIENT_IDENTIFIER("Patient Identifier",
        "Unique identifier of patient.",
        "Unique identifier for the individual that has agree to take part in this study. " +
                               " The identifier on its own is anonymous, preserving the privacy of the patient.  However, there" +
                               " is a database that ties the patient identifier to the name of the bear.",
        "PatientId",
        "Acts as an anonymous identifier for a patient in a clinical trial.",
        GlossaryCategoryDefinition.GENERIC_DATA_FIELDS),

    /**
     * Day, month and year that the patient was born.
     */
    DATE_OF_BIRTH("Patient Date of Birth",
                       "Day, month and year that the patient was born.",
                       "This is the day that the person was born.  Not official birth date if different.",
                       "PatientDateOfBirth",
                       "Acts as a standard mechanism for measuring the age of a patient in a clinical trial.",
                       GlossaryCategoryDefinition.GENERIC_DATA_FIELDS),

    /**
     * Height of patient.
     */
    HEIGHT("Patient Height",
                  "Height of patient in centimetres.",
                  "This is the height of the patient in centimetres, without shoes and to the top of the skull, ideally measured in the morning.",
                  "PatientHeight",
                  "Acts as a standard mechanism for measuring the height of a patient in a clinical trial.",
                  GlossaryCategoryDefinition.GENERIC_DATA_FIELDS),

    /**
     * Weight of patient.
     */
    WEIGHT("Patient Weight",
           "Weight of patient in kilograms.",
           "This is the weight of the patient in kilograms to 1 decimal place, without outer clothes on, ideally measured in the morning.",
           "PatientWeight",
           "Acts as a standard mechanism for measuring the weight of a patient in a clinical trial.",
           GlossaryCategoryDefinition.GENERIC_DATA_FIELDS),


    /**
     * Blood pressure of patient (systolic and diastolic).
     */
    BLOOD_PRESSURE("Patient Blood Pressure",
           "Blood pressure of patient (systolic and diastolic).",
           "Blood pressure is the pressure of circulating blood against the walls of blood vessels. Most of this pressure results from the heart pumping blood through the circulatory system. Blood pressure is expressed in terms of the systolic pressure over diastolic pressure in the cardiac cycle. It is measured in millimetres of mercury above the surrounding atmospheric pressure, or in kilopascals. The difference between the systolic and diastolic pressures is known as pulse pressure, while the average pressure during a cardiac cycle is known as mean arterial pressure.",
           "PatientBloodPressure",
           "Acts as one of the standard mechanism for measuring the health of a patient in a clinical trial.",
           GlossaryCategoryDefinition.GENERIC_DATA_FIELDS),


    /**
     * The date that the measurement was made.
     */
    MEASUREMENT_DATE("Measurement Date",
                       "The date that the patient measurement was made.",
                       "The measurements are from a discrete day so that any changes in a patient's condition" +
                             " can be measured.  The format of the date may" +
                             " vary depending on the source of the measurement.",
                       "MeasurementDate",
                       "Used to identify the data of any patient measurement during a clinical trial.",
                       GlossaryCategoryDefinition.GENERIC_DATA_FIELDS),


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
                       "Definition for use in the Teddy Bear Drop Foot demonstration study.",
                       GlossaryCategoryDefinition.TBDF_DATA_FIELDS),


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
                       "Definition for use in the Teddy Bear Drop Foot demonstration study.",
                       GlossaryCategoryDefinition.TBDF_DATA_FIELDS),
    ;

    private final String                     name;
    private final String                     summary;
    private final String                     description;
    private final String                     abbreviation;
    private final String                     usage;
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
                           String                     usage,
                           GlossaryCategoryDefinition category)
    {
        this.name         = name;
        this.summary      = summary;
        this.description  = description;
        this.abbreviation = abbreviation;
        this.usage        = usage;
        this.category     = category;
    }

    /**
     * Qualified name of the main glossary term.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "GlossaryTerm::ClinicalTrialTerminology::" + abbreviation;
    }

    /**
     * Return the qualified name for the template substitute.
     *
     * @return string
     */
    public String getTemplateSubstituteQualifiedName()
    {
        return "GlossaryTerm::ClinicalTrialTerminology::TemplateSubstitute::" + abbreviation;
    }


    /**
     * Return the name of the main term.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the name of the template substitute term.
     *
     * @return string
     */
    public String getTemplateSubstituteName() { return name; }


    /**
     * Return the summary for the main term.
     *
     * @return string
     */
    public String getSummary()
    {
        return summary;
    }

    /**
     * Return the summary for the template substitute.
     *
     * @return string
     */
    public String getTemplateSubstituteSummary() { return "Template Substitute for " + name; }


    /**
     * The usage for the main term.
     *
     * @return string
     */
    public String getUsage() { return usage; }


    /**
     * Return the usage of the template substitute term.
     *
     * @return string
     */
    public String getTemplateSubstituteUsage() { return "Only for use in templates."; }


    /**
     * Return the description for this glossary term.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the short name (abbreviation) for the glossary term.
     *
     * @return string
     */
    public String getAbbreviation()
    {
        return abbreviation;
    }


    /**
     * Return the category for the main term - may be null.
     *
     * @return category definition
     */
    public GlossaryCategoryDefinition getCategory() { return category; }


    /**
     * Return the standard category for the template substitute.
     *
     * @return category definition
     */
    public GlossaryCategoryDefinition getSubstituteCategory() { return GlossaryCategoryDefinition.TEMPLATE_SUBSTITUTES; }


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
