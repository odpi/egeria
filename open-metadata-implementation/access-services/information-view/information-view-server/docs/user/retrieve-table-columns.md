<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Retrieve columns

Retrieve columns of a table using pagination

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/information-view/users/{userId}/tables/{tableGUID}/columns?pageSize=10&startFrom=0
```

`TableColumnsResponse` response with list of columns of table from startFrom to startFrom + pageSize
`VoidResponse` with error message in case of error.

Sample:

```json
{
    "class": "TableColumnsResponse",
    "relatedHTTPCode": 200,
    "tableColumns": [
        {
            "guid": "b1c497ce.60641b50.0v9mgsb2t.faunv1i.4jppgp.vcpqhhv5aub9uk5thtugp",
            "qualifiedName": "(host_(engine))={host}::(database)=EMPLSANL::(database_schema)=DB2INST1::(database_table)=EMPSALARYANALYSIS::(database_column)=STATE",
            "name": "STATE",
            "position": 20,
            "businessTerms": [{
                "guid": "6662c0f2.e1b1ec6c.00263phfe.hdcsrms.095jod.69a74ckl4hrfodcsgeu2d",
                "name": "State",
                "description": "",
                "abbreviation": "",
                "usage": "",
                "summary": "",
                "examples": "",
                "qualifiedName": "(category)=Coco Pharmaceuticals::(category)=Terms::(term)=State"
            }],
            "isNullable": false,
            "isUnique": false,
            "isPrimaryKey": false
        },
        {
            "guid": "b1c497ce.60641b50.0v9mgsb2t.fadq4s6.5vn3hu.ehqs5908s76ig6cn8ho6g",
            "qualifiedName": "(host_(engine))={host}::(database)=EMPLSANL::(database_schema)=DB2INST1::(database_table)=EMPSALARYANALYSIS::(database_column)=EMPSTATUS",
            "name": "EMPSTATUS",
            "position": 4,
            "businessTerms": [{
                "guid": "6662c0f2.e1b1ec6c.00263pfrb.66t5p8c.jk5353.69k7s3u2do49odaavci58",
                "name": "Employee Status",
                "description": "",
                "abbreviation": "",
                "usage": "",
                "summary": "Employee Status represents current various status that an employee could have. Possible values: 1=Student; 2=Temporary-Assignment; 3=Board-Advisor; 4=Sabbatical; 5=Executive; 6=Part-Time-Perm; 7=Full-Time-Perm; 8=Left; 9=Retired; 10=DIS",
                "examples": "",
                "qualifiedName": "(category)=Coco Pharmaceuticals::(category)=Terms::(term)=Employee Status"
            }],
            "isNullable": false,
            "isUnique": false,
            "isPrimaryKey": false
        },
        {
            "guid": "b1c497ce.60641b50.0v9mgsb2t.fadoft6.jdu07h.62mt3l35mh6qmt0lcufbv",
            "qualifiedName": "(host_(engine))={host}::(database)=EMPLSANL::(database_schema)=DB2INST1::(database_table)=EMPSALARYANALYSIS::(database_column)=TAXP",
            "name": "TAXP",
            "position": 22,
            "businessTerms": [{
                "guid": "6662c0f2.e1b1ec6c.00263phg8.gdtncls.audklp.e6or1vj80e3j7mgaqignr",
                "name": "Tax State",
                "description": "",
                "abbreviation": "",
                "usage": "",
                "summary": "",
                "examples": "",
                "qualifiedName": "(category)=Coco Pharmaceuticals::(category)=Terms::(term)=Tax State"
            }],
            "isNullable": false,
            "isUnique": false,
            "isPrimaryKey": false
        },
        {
            "guid": "b1c497ce.60641b50.0v9mgsb2t.faul2ce.qfu265.bepvic8n0615h13llbi6h",
            "qualifiedName": "(host_(engine))={host}::(database)=EMPLSANL::(database_schema)=DB2INST1::(database_table)=EMPSALARYANALYSIS::(database_column)=HDR",
            "name": "HDR",
            "position": 1,
            "isNullable": false,
            "isUnique": false,
            "isPrimaryKey": false
        }
    ]
}
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.