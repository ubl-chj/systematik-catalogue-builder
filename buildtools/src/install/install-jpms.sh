#!/usr/bin/env bash
./gradlew --version
./gradlew --stacktrace --warning-mode=all scb.templates:build
./gradlew --stacktrace --warning-mode=all scb.creator:build