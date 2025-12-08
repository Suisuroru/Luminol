#!/bin/bash

local_version=$(cat api_md5.txt | head -n 1)

server_response=$(curl -s "${PUSH_WEB}/${OBJECT_NAME}/${MC_VERSION}")

if [ -z "$server_response" ] || [ "$server_response" = "[]" ]; then
    echo "need_push_API=true" >> $GITHUB_ENV
    echo "No server versions found, setting need_push_API=true"
    exit 0
fi

match_found=false
earliest_remote_commit=""
earliest_remote_time=""

for row in $(echo "${server_response}" | jq -r '.[] | @base64'); do
    _jq() {
     echo ${row} | base64 --decode | jq -r ${1}
    }

    server_version=$(_jq '.ApiVersion')
    commit_time=$(_jq '.CommitTime')
    commit_sha=$(_jq '.CommitSha')

    if [ "$local_version" = "$server_version" ]; then
        match_found=true
        if [ -z "$earliest_remote_time" ] || [ "$commit_time" \< "$earliest_remote_time" ]; then
            earliest_remote_time="$commit_time"
            earliest_remote_commit="$commit_sha"
        fi
    fi
done

if [ "$match_found" = false ]; then
    echo "need_push_API=true" >> $GITHUB_ENV
    echo "Local version not found in server versions, setting need_push_API=true"
else
    echo "Local version already exists on server"
fi

if [ -n "$earliest_remote_commit" ]; then
    echo "EARLIEST_REMOTE_COMMIT=$earliest_remote_commit" >> $GITHUB_ENV
    echo "Earliest remote commit SHA: $earliest_remote_commit"
else
    echo "EARLIEST_REMOTE_COMMIT=$SHA" >> $GITHUB_ENV
    echo "Using local commit SHA: $SHA"
fi

