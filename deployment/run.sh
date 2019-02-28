#!/bin/bash

/usr/bin/docker run --rm -p 127.0.0.1:8080:8080 --name ethereum-monitor -v /opt/docker/ethereum-monitor:/config fundrequestio/ethereum-node-monitor:master