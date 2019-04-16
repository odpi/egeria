#!/bin/bash
# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

token="$(cat /var/run/secrets/kubernetes.io/serviceaccount/token)"
namespace="$(cat /var/run/secrets/kubernetes.io/serviceaccount/namespace)"
cacert=/var/run/secrets/kubernetes.io/serviceaccount/ca.crt

available_pods=$(curl -s --cacert $cacert --header "Authorization: Bearer $token" https://kubernetes.default.svc/api/v1/namespaces/$namespace/endpoints/$SERVICE | jq -r '.subsets[].addresses | length')

until [ $available_pods -ge 1 ]; do
    echo waiting for $SERVICE
    sleep 2
    available_pods=$(curl -s --cacert $cacert --header "Authorization: Bearer $token" https://kubernetes.default.svc/api/v1/namespaces/$namespace/endpoints/$SERVICE | jq -r '.subsets[].addresses | length')
done
