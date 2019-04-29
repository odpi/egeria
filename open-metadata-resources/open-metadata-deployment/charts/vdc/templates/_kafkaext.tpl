{{/* <!-- SPDX-License-Identifier: Apache-2.0 --> */}}
{{/* Copyright Contributors to the Egeria project. */}}{{/* vim: set filetype=mustache: */}}

{{/* Fragment to add kafka user/pass into container spec using secret */}}
{{ define "vdc.template.kafkaext.secret" }}
{{- if .Values.kafka.external.secret -}}
- name: KAFKA_USER
  valueFrom:
    secretKeyRef:
      name: {{ .Values.kafka.external.secret }}
      key: username
- name: KAFKA_PASS
  valueFrom:
    secretKeyRef:
      name: {{ .Values.kafka.external.secret }}
      key: password
{{- end -}}
{{- end -}}



{{ define "vdc.template.kafkaext.eventbus" -}}
--data '{
"producer":
{
"bootstrap.servers":"{{-  required "Require kafka.external.brokers" .Values.kafka.external.brokers -}}"
{{- if and (required "Required kafka.external.provider" .Values.kafka.external.provider) ( eq  .Values.kafka.external.provider "IBMEventStreams" ) -}}
,
"security.protocol":"SASL_SSL",
"ssl.protocol":"TLSv1.2",
"ssl.enabled.protocols":"TLSv1.2",
"ssl.endpoint.identification.algorithm":"HTTPS",
"sasl.jaas.config":"org.apache.kafka.common.security.plain.PlainLoginModule required username='${KAFKA_USER}' password='${KAFKA_PASS}';",
"sasl.mechanism":"PLAIN"
{{- end -}}
},
"consumer":
{
"bootstrap.servers":"{{ .Values.kafka.external.brokers }}"
{{- if and (required "Required kafka.external.provider" .Values.kafka.external.provider) ( eq  .Values.kafka.external.provider "IBMEventStreams" ) -}},
"security.protocol":"SASL_SSL",
"ssl.protocol":"TLSv1.2",
"ssl.enabled.protocols":"TLSv1.2",
"ssl.endpoint.identification.algorithm":"HTTPS",
"auto.offset.reset":"latest",
"sasl.jaas.config":"org.apache.kafka.common.security.plain.PlainLoginModule required username='${KAFKA_USER}' password='${KAFKA_PASS}';",
"sasl.mechanism":"PLAIN"
{{- end -}}
}
}'
{{- end -}}
