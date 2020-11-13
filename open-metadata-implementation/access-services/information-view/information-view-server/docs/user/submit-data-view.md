<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Submit a data view

Create a data view as a view on top of other sources.

```

POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/information-view/users/{userId}/dataview
```
```json
{
   "author": "owner-test",
   "class": "DataViewRequestBody",
   "registrationGuid": "registrationGuid",
   "dataView":{
   	"elements": [
      {
         "class": "DataViewTable",
         "comment": null,
         "description": null,
         "elements": [
            {
               "class": "DataViewColumn",
               "columnGuid": "d0845613-749d-42ac-8f5c-c9c45f565687",
               "comment": null,
               "dataType": null,
               "description": null,
               "formula": "COUNTRYCODE",
               "hidden": null,
               "id": "EuroConversion.CountryCode",
               "identifier": "CountryCode",
               "label": "CountryCode",
               "name": "CountryCode",
               "nativeClass": "QueryItem",
               "regularAggregate": "none",
               "usage": "identifier",
               "businessTerms":[{"guid":"6662c0f2.e1b1ec6c.00263pgul.jam2g2s.qlr9u8.b9ei1dc60fgnhvs533pvs"}]
            },
            {
               "class": "DataViewColumn",
               "columnGuid": "9544f066-488a-448c-9537-65999f1dafab",
               "comment": null,
               "dataType": null,
               "description": null,
               "formula": "EUROMONTH",
               "hidden": null,
               "id": "EuroConversion._EuroMounth",
               "identifier": "EuroMounth",
               "label": "EuroMounth",
               "name": "EuroMounth",
               "nativeClass": "QueryItem",
               "regularAggregate": "none",
               "usage": "identifier"
            }
         ],
         "id": "test._employee",
         "label": "EuroConversion",
         "name": "EuroConversion",
         "nativeClass": "QuerySubject"
      }
   ],
   "id": "i999911123",
   "lastModifiedTime": 1547838663347,
   "lastModifier": "owner",
   "name": "test-module-test",
   "nativeClass": "Module",
   "networkAddress": null
   }
}
```


GuidResponse in case of success containing the guid of the view top level entity or error response in case of errors



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.




