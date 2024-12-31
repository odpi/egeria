/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.businesssystems;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * Define the lineage between the Coco Pharmaceutical Systems
 */
public enum SystemLevelLineage
{
    PRODUCT_CATALOG(SystemDefinition.COCO_PRODUCT_PLANNING,
                    OpenMetadataType.DATA_FLOW.typeName,
                    "product offerings",
                    SystemDefinition.COCO_CRM),

    NEW_ORDERS(SystemDefinition.COCO_CRM,
               OpenMetadataType.DATA_FLOW.typeName,
               "new orders",
               SystemDefinition.MANUFACTURING_PLANNING),

    NEW_SALES(SystemDefinition.COCO_CRM,
               OpenMetadataType.DATA_FLOW.typeName,
               "new sales",
               SystemDefinition.COCO_LEDGERS),

    AUS_MANUFACTURING_REQUEST(SystemDefinition.MANUFACTURING_PLANNING,
                              OpenMetadataType.DATA_FLOW.typeName,
                              "manufacturing request",
                              SystemDefinition.AUS_MANUFACTURING_CONTROL),

    AUS_MANUFACTURING_GOODS_REQUEST(SystemDefinition.AUS_MANUFACTURING_CONTROL,
                            OpenMetadataType.DATA_FLOW.typeName,
                            "goods request",
                            SystemDefinition.AUS_INVENTORY),

    AUS_MANUFACTURING_GOODS_RECEIVED(SystemDefinition.AUS_MANUFACTURING_CONTROL,
                                    OpenMetadataType.DATA_FLOW.typeName,
                                    "goods received",
                                    SystemDefinition.AUS_INVENTORY),

    AUS_MANUFACTURING_GOODS_PRODUCED(SystemDefinition.AUS_MANUFACTURING_CONTROL,
                                     OpenMetadataType.DATA_FLOW.typeName,
                                     "goods produced",
                                     SystemDefinition.AUS_INVENTORY),

    AUS_MANUFACTURING_COMPLETE(SystemDefinition.AUS_MANUFACTURING_CONTROL,
                               OpenMetadataType.DATA_FLOW.typeName,
                               "manufacturing complete",
                               SystemDefinition.MANUFACTURING_PLANNING),

    WINCH_MANUFACTURING_GOODS_REQUEST(SystemDefinition.WINCH_MANUFACTURING_CONTROL,
                                    OpenMetadataType.DATA_FLOW.typeName,
                                    "goods request",
                                    SystemDefinition.COCO_INVENTORY),

    WINCH_MANUFACTURING_GOODS_RECEIVED(SystemDefinition.WINCH_MANUFACTURING_CONTROL,
                                     OpenMetadataType.DATA_FLOW.typeName,
                                     "goods received",
                                     SystemDefinition.COCO_INVENTORY),

    WINCH_MANUFACTURING_GOODS_PRODUCED(SystemDefinition.WINCH_MANUFACTURING_CONTROL,
                                     OpenMetadataType.DATA_FLOW.typeName,
                                     "goods produced",
                                     SystemDefinition.COCO_INVENTORY),

    WINCH_MANUFACTURING_COMPLETE(SystemDefinition.WINCH_MANUFACTURING_CONTROL,
                               OpenMetadataType.DATA_FLOW.typeName,
                               "manufacturing complete",
                               SystemDefinition.MANUFACTURING_PLANNING),

    ED_MANUFACTURING_GOODS_REQUEST(SystemDefinition.ED_MANUFACTURING_CONTROL,
                                      OpenMetadataType.DATA_FLOW.typeName,
                                      "goods request",
                                      SystemDefinition.COCO_INVENTORY),

    ED_MANUFACTURING_GOODS_RECEIVED(SystemDefinition.ED_MANUFACTURING_CONTROL,
                                       OpenMetadataType.DATA_FLOW.typeName,
                                       "goods received",
                                       SystemDefinition.COCO_INVENTORY),

    ED_MANUFACTURING_GOODS_PRODUCED(SystemDefinition.ED_MANUFACTURING_CONTROL,
                                       OpenMetadataType.DATA_FLOW.typeName,
                                       "goods produced",
                                       SystemDefinition.COCO_INVENTORY),

    ED_MANUFACTURING_COMPLETE(SystemDefinition.ED_MANUFACTURING_CONTROL,
                                 OpenMetadataType.DATA_FLOW.typeName,
                                 "manufacturing complete",
                                 SystemDefinition.MANUFACTURING_PLANNING),

    AUS_PROCUREMENT_REQUEST(SystemDefinition.AUS_INVENTORY,
                              OpenMetadataType.DATA_FLOW.typeName,
                              "procurement request",
                              SystemDefinition.COCO_PROCUREMENT_AUS),
    AUS_PROCUREMENT_INVOICE(SystemDefinition.COCO_PROCUREMENT_AUS,
                             OpenMetadataType.DATA_FLOW.typeName,
                             "invoice",
                             SystemDefinition.COCO_LEDGERS),
    AUS_PROCUREMENT_GOODS_IN(SystemDefinition.COCO_PROCUREMENT_AUS,
                            OpenMetadataType.DATA_FLOW.typeName,
                            "goods in",
                            SystemDefinition.AUS_INVENTORY),


    WINCH_PROCUREMENT_REQUEST(SystemDefinition.COCO_INVENTORY,
                            OpenMetadataType.DATA_FLOW.typeName,
                            "procurement request",
                            SystemDefinition.COCO_PROCUREMENT_WINCH),
    WINCH_PROCUREMENT_INVOICE(SystemDefinition.COCO_PROCUREMENT_WINCH,
                            OpenMetadataType.DATA_FLOW.typeName,
                            "invoice",
                            SystemDefinition.COCO_LEDGERS),
    WINCH_PROCUREMENT_GOODS_IN(SystemDefinition.COCO_PROCUREMENT_WINCH,
                             OpenMetadataType.DATA_FLOW.typeName,
                             "goods in",
                             SystemDefinition.COCO_INVENTORY),

    ED_PROCUREMENT_REQUEST(SystemDefinition.COCO_INVENTORY,
                              OpenMetadataType.DATA_FLOW.typeName,
                              "procurement request",
                              SystemDefinition.COCO_PROCUREMENT_ED),
    ED_PROCUREMENT_INVOICE(SystemDefinition.COCO_PROCUREMENT_ED,
                              OpenMetadataType.DATA_FLOW.typeName,
                              "invoice",
                              SystemDefinition.COCO_LEDGERS),
    ED_PROCUREMENT_GOODS_IN(SystemDefinition.COCO_PROCUREMENT_ED,
                               OpenMetadataType.DATA_FLOW.typeName,
                               "goods in",
                               SystemDefinition.COCO_INVENTORY),

    AMS_PROCUREMENT_REQUEST(SystemDefinition.COCO_INVENTORY,
                           OpenMetadataType.DATA_FLOW.typeName,
                           "procurement request",
                           SystemDefinition.COCO_PROCUREMENT_AMS),
    AMS_PROCUREMENT_INVOICE(SystemDefinition.COCO_PROCUREMENT_AMS,
                           OpenMetadataType.DATA_FLOW.typeName,
                           "invoice",
                           SystemDefinition.COCO_LEDGERS),
    AMS_PROCUREMENT_GOODS_IN(SystemDefinition.COCO_PROCUREMENT_AMS,
                            OpenMetadataType.DATA_FLOW.typeName,
                            "goods in",
                            SystemDefinition.COCO_INVENTORY),


    PLANNED_MANUFACTURED_GOODS_INVENTORY(SystemDefinition.MANUFACTURING_PLANNING,
               OpenMetadataType.DATA_FLOW.typeName,
               "planned orders",
               SystemDefinition.COCO_INVENTORY),

    EMPLOYEE_EXPENSES_SUS(SystemDefinition.EMPLOYEE_EXPENSES,
                  OpenMetadataType.DATA_FLOW.typeName,
                  "travel inventory",
                  SystemDefinition.COCO_SUS),
    INVENTORY_SUS(SystemDefinition.COCO_INVENTORY,
                  OpenMetadataType.DATA_FLOW.typeName,
                  "goods movement",
                  SystemDefinition.COCO_SUS),

    AUS_INVENTORY_SUS(SystemDefinition.AUS_INVENTORY,
                  OpenMetadataType.DATA_FLOW.typeName,
                  "goods movement",
                  SystemDefinition.COCO_SUS),

    COCO_LEDGERS_SUS(SystemDefinition.COCO_LEDGERS,
                  OpenMetadataType.DATA_FLOW.typeName,
                  "spending types",
                  SystemDefinition.COCO_SUS),

    HAZMAT_SUS(SystemDefinition.HAZ_MAT,
               OpenMetadataType.DATA_FLOW.typeName,
               "publish hazmat",
               SystemDefinition.COCO_SUS),

    HAZMAT_SUS_AUS(SystemDefinition.AUS_HAZ_MAT,
               OpenMetadataType.DATA_FLOW.typeName,
               "publish hazmat",
               SystemDefinition.COCO_SUS),

    DASHBOARD_QUERY(SystemDefinition.COCO_SUS,
                    OpenMetadataType.DATA_FLOW.typeName,
                    "data request",
                    SystemDefinition.COCO_SUS_DASHBOARDS),
    CALCULATOR_QUERY(SystemDefinition.COCO_SUS,
                    OpenMetadataType.DATA_FLOW.typeName,
                    "data request",
                    SystemDefinition.COCO_SUS_CALCULATORS),

    CALCULATOR_RESULTS(SystemDefinition.COCO_SUS_CALCULATORS,
                       OpenMetadataType.DATA_FLOW.typeName,
                       "results publishing",
                       SystemDefinition.COCO_SUS),

    HRIM_CA_PAYROLL(SystemDefinition.COCO_HRIM,
                       OpenMetadataType.DATA_FLOW.typeName,
                       "payment authorization",
                       SystemDefinition.CA_PAYROLL),

    HRIM_UK_PAYROLL(SystemDefinition.COCO_HRIM,
                    OpenMetadataType.DATA_FLOW.typeName,
                    "payment authorization",
                    SystemDefinition.UK_PAYROLL),

    HRIM_NL_PAYROLL(SystemDefinition.COCO_HRIM,
                    OpenMetadataType.DATA_FLOW.typeName,
                    "payment authorization",
                    SystemDefinition.NL_PAYROLL),

    HRIM_EXPENSES(SystemDefinition.COCO_HRIM,
                    OpenMetadataType.DATA_FLOW.typeName,
                    "payment authorization",
                    SystemDefinition.EMPLOYEE_EXPENSES),

    HRIM_COCO_PAGES(SystemDefinition.COCO_HRIM,
                  OpenMetadataType.DATA_FLOW.typeName,
                  "employee directory",
                  SystemDefinition.COCO_PAGES),


    ;


    private SystemDefinition sourceSystem;
    private String           relationshipName;
    private String           relationshipLabel;
    private SystemDefinition destinationSystem;


    SystemLevelLineage(SystemDefinition sourceSystem,
                       String           relationshipName,
                       String relationshipLabel,
                       SystemDefinition destinationSystem)
    {
        this.sourceSystem            = sourceSystem;
        this.relationshipName  = relationshipName;
        this.relationshipLabel = relationshipLabel;
        this.destinationSystem = destinationSystem;
    }


    public SystemDefinition getSourceSystem()
    {
        return sourceSystem;
    }

    public String getRelationshipName()
    {
        return relationshipName;
    }

    public String getRelationshipLabel()
    {
        return relationshipLabel;
    }

    public SystemDefinition getDestinationSystem()
    {
        return destinationSystem;
    }
}
