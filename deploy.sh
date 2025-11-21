#!/bin/bash
set -e

echo "=== CToon V2 Deployment Automation ==="
echo ""

# Step 1: Clean and rebuild WAR
echo "[1/5] Cleaning previous build..."
mvn clean

echo "[2/5] Building fresh WAR file..."
mvn package -DskipTests -q

# Verify WAR file exists and is valid
WAR_FILE="target/ctoon-1.0-SNAPSHOT.war"
if [ ! -f "$WAR_FILE" ]; then
    echo "ERROR: WAR file not found at $WAR_FILE"
    exit 1
fi

WAR_SIZE=$(stat -f%z "$WAR_FILE" 2>/dev/null || stat -c%s "$WAR_FILE" 2>/dev/null)
echo "✓ WAR file created: $WAR_SIZE bytes"

# Step 2: Verify WAR is a valid zip
echo "[3/5] Verifying WAR integrity..."
unzip -t "$WAR_FILE" > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✓ WAR file is valid and complete"
else
    echo "ERROR: WAR file is corrupted"
    exit 1
fi

# Step 3: Build Docker image
echo "[4/5] Building Docker image..."
docker build -t ctoon:latest .
if [ $? -ne 0 ]; then
    echo "ERROR: Docker build failed"
    exit 1
fi
echo "✓ Docker image built successfully"

# Step 4: Push to Railway (if Railway CLI is installed)
if command -v railway &> /dev/null; then
    echo "[5/5] Deploying to Railway..."
    railway up
    echo "✓ Deployment complete!"
else
    echo "[5/5] Railway CLI not found. Skipping Railway deployment."
    echo ""
    echo "To deploy manually:"
    echo "  1. Ensure Docker is running"
    echo "  2. Run: docker push ctoon:latest (after configuring registry)"
    echo "  3. Or use Railway dashboard to connect this repository"
fi

echo ""
echo "=== Deployment Ready ==="
