#!/bin/bash

# Clone GitHub repository
echo "Cloning repository..."
git clone "https://x-access-token:${PAT_SECRET}@github.com/ORGANIZATION/REPO_NAME.git"
echo "Switching to project directory..."
cd VersionReleaseSystem

echo "Installing required dependencies..."
pip install requests cryptography

echo "Running client_send_stage1.py..."
python client_send_stage1.py \
  --token "${PUSH_TOKEN}" \
  --web "${PUSH_WEB}/version-data" \
  --object "${PROJECT_NAME}" \
  --major "${MC_VERSION}"

echo "Python script completed!"

if [ -f "responseStage1.txt" ]; then
    SUBVERSION=$(python -c "import json; data = json.load(open('responseStage1.txt')); print(data.get('SubVersion', ''))")

    echo "SUBVERSION=$SUBVERSION" >> $GITHUB_ENV

    echo "SubVersion value: $SUBVERSION"
    echo "Set GitHub environment variable SUBVERSION successfully!"
else
    echo "Error: responseStage1.txt not found!"
    exit 1
fi
