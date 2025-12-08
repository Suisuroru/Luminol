#!/bin/bash

local_version=$(cat api_md5.txt | head -n 1)

server_response=$(curl -s "${PUSH_WEB}/${OBJECT_NAME}/${MC_VERSION}")

# 检查服务器响应是否为空或无效
if [ -z "$server_response" ] || [ "$server_response" = "[]" ]; then
    echo "need_push_API=true" >> $GITHUB_ENV
    echo "No server versions found, setting need_push_API=true"
    exit 0
fi

# 检查本地版本是否存在于服务器版本列表中
match_found=false
for row in $(echo "${server_response}" | jq -r '.[] | @base64'); do
    _jq() {
     echo ${row} | base64 --decode | jq -r ${1}
    }

    server_version=$(_jq '.ApiVersion')

    if [ "$local_version" = "$server_version" ]; then
        match_found=true
        break
    fi
done

if [ "$match_found" = false ]; then
    echo "need_push_API=true" >> $GITHUB_ENV
    echo "Local version not found in server versions, setting need_push_API=true"
else
    echo "Local version already exists on server"
fi
