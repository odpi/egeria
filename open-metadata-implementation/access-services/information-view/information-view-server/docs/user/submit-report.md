<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Submit Report

Submit the metadata representing a report. 
Property "id" (as unique identifier on external tool side) in combination with address from report url are used to uniquely identify a report

If no entity exists matching these properties, a new DeployedReport entity (along  with underlying entities) is created. The registrationGuid is set on entityDetails as the identifier of the tool that created the report

If there is an entity matching these properties, a check is made against the registrationGuid to amke sure only the owner tool can modify the report structure

The payload contains the structure of the report and the metadata for underlying sources.

Currently supported sources for a report are:

* database column
* report column
* data view column

The structure of the report is described by elements of type ReportElement. ReportElement can be of 2 types:
* ReportSection: used to describe the high level structure for a report.
 
  Supported properties are: 
    * name 
    * elements: other elements of type ReportSection or ReportColumn
    The nesting level of sections is not restricted
    
* ReportColumn
 
   Supported properties are: 
    * name: name for report column 
    * formula: formula applied on top of the column sources (if any)
    * sources: underlying sources for the report column 
    
    Supported sources are:
    
  * database column
  
       In order to identify the database column, following details are required: endpoint address, database name, schema name, table name and column name.
       The bean to use to identify the referenced column is DatabaseColumnSource
  * report column
        
       In order to identify the report column, following details are required: report id, full nested sections down to column level and column name.
       The bean to use to identify the referenced column is ReportColumnSource
        
       Sample 2 is an example of using ReportColumnSource as source
  * data view column
        
       The bean to use to identify the referenced column is DataViewColumnSource
       Sample 3 is an example of using DataViewColumnSource
        
    * businessTerm: business term linked to the report column; 
    
    One of the unique identifiers - guid or qualifiedName - is required to uniquely identify the business term

```

POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/information-view/users/{userId}/report

Sample 1
{
  "class": "ReportRequestBody",
  "registrationGuid": "registration-guid",
  "report":{
      "sources": [
          {
            "@id": "0",
            "class": "TableSource",
            "additionalProperties": {
              "type": "live/import"
            },
            "name": "EMPLOYEE",
            "schemaName": "HR",
            "databaseSource": {
              "class": "DatabaseSource",
              "name": "XE",
              "endpointSource": {
                "networkAddress": "host",
                "protocol": "",
                "class": "EndpointSource"
              }
            }
          }
        ],
        "id": "report_uuid",
        "createdTime": 1538988150715,
        "author": "John Martin Winston",
        "reportName": "TestReport",
        "reportPath": "/reports/employee46.xml",
        "reportUrl": "http://powerbi-server/reports/testReport",
        "lastModifier": "John Martin",
        "lastModifiedTime": 1538988150715,
        "reportElements": [
          {
            "class": "ReportSection",
            "name": "section1",
            "elements": [
              {
                "class": "ReportSection",
                "name": "section1.1",
                "elements": [
                  {
                    "class": "ReportColumn",
                    "name": "Full Name",
                    "formula": "concat",
                    "sources": [
                      {
                        "class": "DatabaseColumnSource",
                        "tableSource": "0",
                        "name": "FNAME"
                      },
                      {
                        "class": "DatabaseColumnSource",
                        "tableSource": "0",
                        "name": "LNAME"
                      }
                    ],
                    "businessTerms": [{
                                   "name": "Patient Full Name",
                                   "guid": "UUID",
                                   "qualifiedName": "businessTermQualifiedName",
                               }]
                    }
                ]
              }
            ]
          }
        ]
    }
}

```
```
Sample 2

{
	"class": "ReportRequestBody",
	"sources": [{
			"@id": "0",
			"class": "ReportSectionSource",
			"name": "section1.1",
			"parentReportSection": {
				"class": "ReportSectionSource",
				"name": "section1",
				"reportSource": {
					"class": "ReportSource",
					"reportId": "report_number_1200",
					"networkAddress": "powerbi-server"
				}
			}

		}
	],
	"id": "report_number_1201",
	"createdTime": 1538988150715,
	"author": "John Martin",
	"reportName": "Employee1201",
	"reportPath": "/reports/employee1201.xml",
	"reportUrl": "http://powerbi-server/reports/rep1201",
	"lastModifier": "John Martin",
	"lastModifiedTime": 1538988150715,
	"reportElements": [{
			"class": "ReportSection",
			"name": "section1",
			"elements": [{
					"class": "ReportSection",
					"name": "section1.1",
					"elements": [{
							"class": "ReportColumn",
							"name": "Full Name derived from report",
							"formula": "concat",
							"sources": [{
									"class": "ReportColumnSource",
									"guid": "774c8b42-f805-4c8a-a320-68dcf936f0d1"
								}
							],
							"businessTerms": [{
								"guid": "6662c0f2.e1b1ec6c.00263shk7.8vmg2e5.dt0tqp.fedhui18kd6cif3ro2ugd"
							}]
						}, {
							"class": "ReportColumn",
							"name": "Role of the employee derived from report",
							"formula": "upper",
							"sources": [{
									"class": "ReportColumnSource",
									"parentReportSection": "0",
									"name": "Role of the employee"
								}
							]
						}
					]
				}
			]
		}
	]
}

```

```
Sample 3
{
  "class": "ReportRequestBody",
  "sources": [
    {
      "@id": "0",
      "class": "DataViewSource",
      "networkAddress": "address",
      "id": "unique identifier for data view on external tool side"
    }
  ],
  "id": "report_number_1500",
  "createdTime": 1538988150715,
  "author": "John Martin tesssst",
  "reportName": "Employee based on data module",
  "reportPath": "/reports/employee1500.xml",
  "reportUrl": "http://cognos/reports/rep1500",
  "lastModifier": "Paul Martin",
  "lastModifiedTime": 1538988150715,
  "reportElements": [
    {
      "class": "ReportSection",
      "name": "Page1",
      "elements": [
        {
          "class": "ReportColumn",
          "name": "Dept name - Job Level",
          "formula": "concat",
          "sources": [
            {
              "class": "DataViewColumnSource",
              "dataViewSource": "0",
              "id": "_EMPSALARYANALYSIS.Employee_Contract_Number"
            }
          ],
          "businessTerms": [{
            "name": "Employee Contract Number",
            "guid": "6662c0f2.e1b1ec6c.00263pfur.m0g2a5b.l5676h.5imorjcftp26mv2rr93bp"
          }]
        },
        {
          "class": "ReportColumn",
          "name": "Role of the employee",
          "formula": "upper",
          "sources": [
            {
              "class": "DataViewColumnSource",
              "dataViewSource": "0",
              "id": "_EMPSALARYANALYSIS.Employee_Status"
            }
          ]
        }
      ]
    }
  ]
}

GuidResponse in case of success containing the guid of the report top level entity or error response in case of errors

```
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.




