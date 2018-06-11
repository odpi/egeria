<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Event Bus Connectors

Event bus connectors support different event/messaging infrastructures.
They can be plugged into the topic connectors from the access-service-connectors
and repository-service-connectors.

* **[kafka-open-connector](kafka-open-connector)** implements
an [Apache Kafka](https://kafka.apache.org/) connector for a topic that exchanges
Java Objects as JSON payloads.