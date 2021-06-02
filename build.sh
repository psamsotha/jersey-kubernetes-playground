#!/bin/bash

./mvnw clean && ./mvnw package && docker build --tag psamsotha/jersey-kubernetes .
