#!/bin/bash
APK="app/build/outputs/apk/debug/app-debug.apk"
if [ ! -f "$APK" ]; then
  echo '{"status": "failed", "reason": "apk_missing"}' > build_report.json
  exit 1
fi

SIZE=$(wc -c < "$APK")
echo "{"status": "success", "size": $SIZE, "timestamp": "$(date)"}" > build_report.json
