/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.businesssystems;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * Define the lineage between the Coco Pharmaceutical Systems
 */
public enum SystemLevelLineage
{
    NEW_PRODUCT_IN_CATALOG(SystemDefinition.COCO_PRODUCT_PLANNING,
                           OpenMetadataType.DATA_FLOW.typeName,
                           "new product offering",
                           InformationSupplyChain.NEW_DRUG_PRODUCT_INFO_DISTRIBUTION.getQualifiedName(),
                           SystemDefinition.COCO_CRM),

    UPDATE_PRODUCT_CATALOG(SystemDefinition.COCO_PRODUCT_PLANNING,
                           OpenMetadataType.DATA_FLOW.typeName,
                           "update product offering",
                           null,
                           SystemDefinition.COCO_CRM),

    WITHDRAW_PRODUCT_FROM_CATALOG(SystemDefinition.COCO_PRODUCT_PLANNING,
                                  OpenMetadataType.DATA_FLOW.typeName,
                                  "withdraw product offering",
                                  null,
                                  SystemDefinition.COCO_CRM),

    NEW_ORDERS(SystemDefinition.COCO_CRM,
               OpenMetadataType.DATA_FLOW.typeName,
               "new orders",
               InformationSupplyChain.PERSONALIZED_TREATMENT_ORDER.getQualifiedName(),
               SystemDefinition.MANUFACTURING_PLANNING),

    NEW_SALES(SystemDefinition.COCO_CRM,
              OpenMetadataType.DATA_FLOW.typeName,
              "new sales",
              InformationSupplyChain.PERSONALIZED_TREATMENT_ORDER.getQualifiedName(),
              SystemDefinition.COCO_LEDGERS),

    AUS_MANUFACTURING_REQUEST(SystemDefinition.MANUFACTURING_PLANNING,
                              OpenMetadataType.DATA_FLOW.typeName,
                              "manufacturing request",
                              InformationSupplyChain.PERSONALIZED_TREATMENT_ORDER.getQualifiedName(),
                              SystemDefinition.AUS_MANUFACTURING_CONTROL),

    AUS_MANUFACTURING_GOODS_REQUEST(SystemDefinition.AUS_MANUFACTURING_CONTROL,
                                    OpenMetadataType.DATA_FLOW.typeName,
                                    "goods request",
                                    InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                                    SystemDefinition.AUS_INVENTORY),

    AUS_MANUFACTURING_GOODS_RECEIVED(SystemDefinition.AUS_MANUFACTURING_CONTROL,
                                     OpenMetadataType.DATA_FLOW.typeName,
                                     "goods received",
                                     InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                                     SystemDefinition.AUS_INVENTORY),

    WINCHESTER_DEPOT_ORDER_READY(SystemDefinition.AUS_MANUFACTURING_CONTROL,
                                 OpenMetadataType.DATA_FLOW.typeName,
                                 "order ready",
                                 InformationSupplyChain.NEW_DRUG_PRODUCT_INFO_DISTRIBUTION.getQualifiedName(),
                                 SystemDefinition.WINCHESTER_DEPOT_MANAGEMENT),

    KANSAS_CITY_DEPOT_ORDER_READY(SystemDefinition.AUS_MANUFACTURING_CONTROL,
                                  OpenMetadataType.DATA_FLOW.typeName,
                                  "order ready",
                                  InformationSupplyChain.NEW_DRUG_PRODUCT_INFO_DISTRIBUTION.getQualifiedName(),
                                  SystemDefinition.KANSAS_CITY_DEPOT_MANAGEMENT),

    EDMONTON_DEPOT_ORDER_READY(SystemDefinition.AUS_MANUFACTURING_CONTROL,
                               OpenMetadataType.DATA_FLOW.typeName,
                               "order ready",
                               InformationSupplyChain.NEW_DRUG_PRODUCT_INFO_DISTRIBUTION.getQualifiedName(),
                               SystemDefinition.EDMONTON_DEPOT_MANAGEMENT),

    AUS_MANUFACTURING_GOODS_READY(SystemDefinition.AUS_MANUFACTURING_CONTROL,
                                  OpenMetadataType.DATA_FLOW.typeName,
                                  "goods produced",
                                  InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                                  SystemDefinition.AUS_INVENTORY),

    AUS_MANUFACTURING_COMPLETE(SystemDefinition.AUS_MANUFACTURING_CONTROL,
                               OpenMetadataType.DATA_FLOW.typeName,
                               "manufacturing complete",
                               InformationSupplyChain.PERSONALIZED_TREATMENT_ORDER.getQualifiedName(),
                               SystemDefinition.MANUFACTURING_PLANNING),

    WINCH_MANUFACTURING_GOODS_REQUEST(SystemDefinition.WINCH_MANUFACTURING_CONTROL,
                                      OpenMetadataType.DATA_FLOW.typeName,
                                      "goods request",
                                      InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                                      SystemDefinition.COCO_INVENTORY),

    WINCH_MANUFACTURING_GOODS_RECEIVED(SystemDefinition.WINCH_MANUFACTURING_CONTROL,
                                       OpenMetadataType.DATA_FLOW.typeName,
                                       "goods received",
                                       InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                                       SystemDefinition.COCO_INVENTORY),

    WINCH_MANUFACTURING_GOODS_PRODUCED(SystemDefinition.WINCH_MANUFACTURING_CONTROL,
                                       OpenMetadataType.DATA_FLOW.typeName,
                                       "goods produced",
                                       InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                                       SystemDefinition.COCO_INVENTORY),

    WINCH_MANUFACTURING_COMPLETE(SystemDefinition.WINCH_MANUFACTURING_CONTROL,
                                 OpenMetadataType.DATA_FLOW.typeName,
                                 "manufacturing complete",
                                 InformationSupplyChain.PERSONALIZED_TREATMENT_ORDER.getQualifiedName(),
                                 SystemDefinition.MANUFACTURING_PLANNING),

    ED_MANUFACTURING_GOODS_REQUEST(SystemDefinition.ED_MANUFACTURING_CONTROL,
                                   OpenMetadataType.DATA_FLOW.typeName,
                                   "goods request",
                                   InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                                   SystemDefinition.COCO_INVENTORY),

    ED_MANUFACTURING_GOODS_RECEIVED(SystemDefinition.ED_MANUFACTURING_CONTROL,
                                    OpenMetadataType.DATA_FLOW.typeName,
                                    "goods received",
                                    InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                                    SystemDefinition.COCO_INVENTORY),

    ED_MANUFACTURING_GOODS_PRODUCED(SystemDefinition.ED_MANUFACTURING_CONTROL,
                                    OpenMetadataType.DATA_FLOW.typeName,
                                    "goods produced",
                                    InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                                    SystemDefinition.COCO_INVENTORY),

    ED_MANUFACTURING_COMPLETE(SystemDefinition.ED_MANUFACTURING_CONTROL,
                              OpenMetadataType.DATA_FLOW.typeName,
                              "manufacturing complete",
                              InformationSupplyChain.PERSONALIZED_TREATMENT_ORDER.getQualifiedName(),
                              SystemDefinition.MANUFACTURING_PLANNING),

    AUS_PROCUREMENT_REQUEST(SystemDefinition.AUS_INVENTORY,
                            OpenMetadataType.DATA_FLOW.typeName,
                            "procurement request",
                            InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                            SystemDefinition.COCO_PROCUREMENT_AUS),
    AUS_PROCUREMENT_INVOICE(SystemDefinition.COCO_PROCUREMENT_AUS,
                            OpenMetadataType.DATA_FLOW.typeName,
                            "invoice",
                            InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                            SystemDefinition.COCO_LEDGERS),
    AUS_PROCUREMENT_GOODS_IN(SystemDefinition.COCO_PROCUREMENT_AUS,
                             OpenMetadataType.DATA_FLOW.typeName,
                             "goods in",
                             InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                             SystemDefinition.AUS_INVENTORY),

    AUS_HAZ_MAT_DELIVERY(SystemDefinition.COCO_PROCUREMENT_AUS,
                         OpenMetadataType.DATA_FLOW.typeName,
                         "haz mat delivery",
                         InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                         SystemDefinition.AUS_HAZ_MAT),

    WINCH_PROCUREMENT_REQUEST(SystemDefinition.COCO_INVENTORY,
                              OpenMetadataType.DATA_FLOW.typeName,
                              "procurement request",
                              InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                              SystemDefinition.COCO_PROCUREMENT_WINCH),
    WINCH_PROCUREMENT_INVOICE(SystemDefinition.COCO_PROCUREMENT_WINCH,
                              OpenMetadataType.DATA_FLOW.typeName,
                              "invoice",
                              InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                              SystemDefinition.COCO_LEDGERS),
    WINCH_PROCUREMENT_GOODS_IN(SystemDefinition.COCO_PROCUREMENT_WINCH,
                               OpenMetadataType.DATA_FLOW.typeName,
                               "goods in",
                               InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                               SystemDefinition.COCO_INVENTORY),
    WINCH_HAZ_MAT_DELIVERY(SystemDefinition.COCO_PROCUREMENT_WINCH,
                           OpenMetadataType.DATA_FLOW.typeName,
                           "haz mat delivery",
                           InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                           SystemDefinition.HAZ_MAT),
    ED_PROCUREMENT_REQUEST(SystemDefinition.COCO_INVENTORY,
                           OpenMetadataType.DATA_FLOW.typeName,
                           "procurement request",
                           InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                           SystemDefinition.COCO_PROCUREMENT_ED),
    ED_PROCUREMENT_INVOICE(SystemDefinition.COCO_PROCUREMENT_ED,
                           OpenMetadataType.DATA_FLOW.typeName,
                           "invoice",
                           InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                           SystemDefinition.COCO_LEDGERS),
    ED_PROCUREMENT_GOODS_IN(SystemDefinition.COCO_PROCUREMENT_ED,
                            OpenMetadataType.DATA_FLOW.typeName,
                            "goods in",
                            InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                            SystemDefinition.COCO_INVENTORY),
    ED_HAZ_MAT_DELIVERY(SystemDefinition.COCO_PROCUREMENT_ED,
                           OpenMetadataType.DATA_FLOW.typeName,
                           "haz mat delivery",
                           InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                           SystemDefinition.HAZ_MAT),
    AMS_PROCUREMENT_REQUEST(SystemDefinition.COCO_INVENTORY,
                            OpenMetadataType.DATA_FLOW.typeName,
                            "procurement request",
                            InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                            SystemDefinition.COCO_PROCUREMENT_AMS),
    AMS_PROCUREMENT_INVOICE(SystemDefinition.COCO_PROCUREMENT_AMS,
                            OpenMetadataType.DATA_FLOW.typeName,
                            "invoice",
                            InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                            SystemDefinition.COCO_LEDGERS),
    AMS_PROCUREMENT_GOODS_IN(SystemDefinition.COCO_PROCUREMENT_AMS,
                             OpenMetadataType.DATA_FLOW.typeName,
                             "goods in",
                             InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                             SystemDefinition.COCO_INVENTORY),
    AMS_HAZ_MAT_DELIVERY(SystemDefinition.COCO_PROCUREMENT_AMS,
                        OpenMetadataType.DATA_FLOW.typeName,
                        "haz mat delivery",
                        InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                        SystemDefinition.HAZ_MAT),
    PLANNED_MANUFACTURED_GOODS_INVENTORY(SystemDefinition.MANUFACTURING_PLANNING,
                                         OpenMetadataType.DATA_FLOW.typeName,
                                         "planned orders",
                                         InformationSupplyChain.PHYSICAL_INVENTORY_TRACKING.getQualifiedName(),
                                         SystemDefinition.COCO_INVENTORY),

    EMPLOYEE_EXPENSES_SUS(SystemDefinition.EMPLOYEE_EXPENSES,
                          OpenMetadataType.DATA_FLOW.typeName,
                          "travel inventory",
                          InformationSupplyChain.SUSTAINABILITY_REPORTING.getQualifiedName(),
                          SystemDefinition.COCO_SUS),
    INVENTORY_SUS(SystemDefinition.COCO_INVENTORY,
                  OpenMetadataType.DATA_FLOW.typeName,
                  "goods movement",
                  InformationSupplyChain.SUSTAINABILITY_REPORTING.getQualifiedName(),
                  SystemDefinition.COCO_SUS),

    AUS_INVENTORY_SUS(SystemDefinition.AUS_INVENTORY,
                      OpenMetadataType.DATA_FLOW.typeName,
                      "goods movement",
                      InformationSupplyChain.SUSTAINABILITY_REPORTING.getQualifiedName(),
                      SystemDefinition.COCO_SUS),

    COCO_LEDGERS_SUS(SystemDefinition.COCO_LEDGERS,
                     OpenMetadataType.DATA_FLOW.typeName,
                     "spending types",
                     InformationSupplyChain.SUSTAINABILITY_REPORTING.getQualifiedName(),
                     SystemDefinition.COCO_SUS),

    HAZMAT_SUS(SystemDefinition.HAZ_MAT,
               OpenMetadataType.DATA_FLOW.typeName,
               "publish hazmat levels",
               InformationSupplyChain.SUSTAINABILITY_REPORTING.getQualifiedName(),
               SystemDefinition.COCO_SUS),

    HAZMAT_SUS_AUS(SystemDefinition.AUS_HAZ_MAT,
                   OpenMetadataType.DATA_FLOW.typeName,
                   "publish hazmat levels",
                   InformationSupplyChain.SUSTAINABILITY_REPORTING.getQualifiedName(),
                   SystemDefinition.COCO_SUS),

    DASHBOARD_QUERY(SystemDefinition.COCO_SUS,
                    OpenMetadataType.DATA_FLOW.typeName,
                    "report request",
                    InformationSupplyChain.SUSTAINABILITY_REPORTING.getQualifiedName(),
                    SystemDefinition.COCO_SUS_DASHBOARDS),

    CALCULATOR_QUERY(SystemDefinition.COCO_SUS,
                     OpenMetadataType.DATA_FLOW.typeName,
                     "raw data request",
                     InformationSupplyChain.SUSTAINABILITY_REPORTING.getQualifiedName(),
                     SystemDefinition.COCO_SUS_CALCULATORS),

    CALCULATOR_RESULTS(SystemDefinition.COCO_SUS_CALCULATORS,
                       OpenMetadataType.DATA_FLOW.typeName,
                       "results publishing",
                       InformationSupplyChain.SUSTAINABILITY_REPORTING.getQualifiedName(),
                       SystemDefinition.COCO_SUS),

    HRIM_CA_PAYROLL(SystemDefinition.COCO_HRIM,
                    OpenMetadataType.DATA_FLOW.typeName,
                    "payment authorization",
                    InformationSupplyChain.NEW_EMPLOYEE_ONBOARDING.getQualifiedName(),
                    SystemDefinition.CA_PAYROLL),

    HRIM_UK_PAYROLL(SystemDefinition.COCO_HRIM,
                    OpenMetadataType.DATA_FLOW.typeName,
                    "payment authorization",
                    InformationSupplyChain.NEW_EMPLOYEE_ONBOARDING.getQualifiedName(),
                    SystemDefinition.UK_PAYROLL),

    HRIM_NL_PAYROLL(SystemDefinition.COCO_HRIM,
                    OpenMetadataType.DATA_FLOW.typeName,
                    "payment authorization",
                    InformationSupplyChain.NEW_EMPLOYEE_ONBOARDING.getQualifiedName(),
                    SystemDefinition.NL_PAYROLL),

    HRIM_EXPENSES(SystemDefinition.COCO_HRIM,
                  OpenMetadataType.DATA_FLOW.typeName,
                  "payment authorization",
                  InformationSupplyChain.EMPLOYEE_EXPENSE_PAYMENT.getQualifiedName(),
                  SystemDefinition.EMPLOYEE_EXPENSES),

    HRIM_COCO_PAGES(SystemDefinition.COCO_HRIM,
                    OpenMetadataType.DATA_FLOW.typeName,
                    "employee directory",
                    InformationSupplyChain.NEW_EMPLOYEE_ONBOARDING.getQualifiedName(),
                    SystemDefinition.COCO_PAGES),


    ;


    private final SystemDefinition sourceSystem;
    private final String           relationshipName;
    private final String           relationshipLabel;
    private final String           iscQualifiedName;
    private final SystemDefinition destinationSystem;


    SystemLevelLineage(SystemDefinition sourceSystem,
                       String           relationshipName,
                       String           relationshipLabel,
                       String           iscQualifiedName,
                       SystemDefinition destinationSystem)
    {
        this.sourceSystem      = sourceSystem;
        this.relationshipName  = relationshipName;
        this.relationshipLabel = relationshipLabel;
        this.iscQualifiedName  = iscQualifiedName;
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

    public String getISCQualifiedName()
    {
        return iscQualifiedName;
    }

    public SystemDefinition getDestinationSystem()
    {
        return destinationSystem;
    }
}
