<!-- SPDX-License-Identifier: Apache-2.0 -->

# Repository get type definition test case

Validate that it is possible to retrieve type definitions from the repository.

## Operation

This test uses the getAllTypes
(/open-metadata/repository-services/users/{userId}/types/all)
operation to retrieve all of the supported types.
It then steps through each type and issues calls to the server to 
validate that each type can be retrieved individually.

## Assertions

* **repository-get-typedef-01** <type name> type verified by repository.
  
  The verify(Attribute)TypeDef call returned true.
  
* **repository-get-typedef-02** <type name> type retrieved from repository by name.

  The get(Attribute)TypeByName call successfully returned.
  
* **repository-get-typedef-03** <type name> same type retrieved from repository by name.

  The get(Attribute)TypeByName call returned the same type as was passed on the getAllTypes.

* **repository-get-typedef-04** <type name> type retrieved from repository by guid.

  The get(Attribute)TypeByGUID call successfully returned.

* **repository-get-typedef-05** <type name> same type retrieved from repository by guid.

  The get(Attribute)TypeByGUID call returned the same type as was passed on the getAllTypes.

