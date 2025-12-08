#!/bin/bash

cd "../"

find . -maxdepth 2 -name "*-api" -type d | while read apiModule; do
    find "$apiModule/src/main/java" -type f -name "*.java" -exec md5sum {} \;
done | md5sum | cut -d' ' -f1 > api_md5.txt

api_md5_value=$(cat api_md5.txt)
echo "api_md5=$api_md5_value" >> $GITHUB_ENV
