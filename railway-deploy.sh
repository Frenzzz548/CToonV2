#!/usr/bin/env bash
# Railway deployment script - runs inside Railway container
set -e

echo "=== CToon V2 Railway Deployment ==="
echo ""

# Log system info
echo "[INFO] System Information:"
java -version 2>&1 | head -1
mvn --version | head -1
echo ""

# Step 1: Verify environment
echo "[1/4] Verifying environment..."
if [ ! -f "pom.xml" ]; then
    echo "ERROR: pom.xml not found in $(pwd)"
    exit 1
fi
echo "✓ Source code verified"

# Step 2: Build WAR
echo "[2/4] Building WAR file..."
mvn clean package -DskipTests -q

WAR_FILE="target/ctoon-1.0-SNAPSHOT.war"
if [ ! -f "$WAR_FILE" ]; then
    echo "ERROR: WAR file not created"
    exit 1
fi

WAR_SIZE=$(stat -c%s "$WAR_FILE")
echo "✓ WAR file created: $((WAR_SIZE / 1024 / 1024)) MB"

# Step 3: Verify WAR integrity
echo "[3/4] Verifying WAR integrity..."
if unzip -t "$WAR_FILE" > /dev/null 2>&1; then
    echo "✓ WAR file is valid"
else
    echo "ERROR: WAR file is corrupted"
    exit 1
fi

# Step 4: Prepare for Tomcat
echo "[4/4] Preparing Tomcat deployment..."
if [ -d "$CATALINA_HOME" ]; then
    echo "✓ CATALINA_HOME detected: $CATALINA_HOME"
    cp "$WAR_FILE" "$CATALINA_HOME/webapps/ROOT.war"
    echo "✓ WAR deployed to Tomcat"
else
    echo "✓ Tomcat will handle deployment via Docker"
fi

echo ""
echo "=== Deployment Complete ==="
echo "Application will start on port 8080"
