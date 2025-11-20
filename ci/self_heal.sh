#!/bin/bash
LOG=$1
echo "Analyzing $LOG..."

# Fix 1: Missing Wrapper
if grep -q "Task 'wrapper' not found" "$LOG"; then
  echo "Fixing: Missing Wrapper"
  gradle wrapper --gradle-version 8.2.2
fi

# Fix 2: SDK License
if grep -q "license" "$LOG"; then
  echo "Fixing: SDK License"
  mkdir -p "$ANDROID_SDK_ROOT/licenses"
  echo -e "
8933bad161af4178b1185d1a37fbf41ea5269c55" > "$ANDROID_SDK_ROOT/licenses/android-sdk-license"
fi

# Fix 3: Namespace/SDK Version Mismatch (Common in upgrades)
if grep -q "namespace" "$LOG"; then
    echo "Fixing: Gradle Namespace Config"
    sed -i 's/compileSdk 34/compileSdk 33/g' app/build.gradle
fi
