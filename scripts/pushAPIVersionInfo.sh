#!/bin/bash

API_VERSION=$(cat api_md5.txt)

echo "Running client_send_stage2.py..."
python client_send_stage2.py   --token "${PUSH_TOKEN}" \
                               --web "${PUSH_WEB}/api-version" \
                               --api "$API_VERSION" \
                               --sha "${SHA}"

echo "Operation completed!"
