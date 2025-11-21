@echo off
REM CToon V2 Deployment Automation for Windows

echo === CToon V2 Deployment Automation ===
echo.

REM Step 1: Clean and rebuild WAR
echo [1/4] Cleaning previous build...
call mvn clean

echo [2/4] Building fresh WAR file...
call mvn package -DskipTests -q

REM Verify WAR file exists
set WAR_FILE=target\ctoon-1.0-SNAPSHOT.war
if not exist "%WAR_FILE%" (
    echo ERROR: WAR file not found at %WAR_FILE%
    exit /b 1
)

REM Check file size
for %%A in ("%WAR_FILE%") do (
    echo [OK] WAR file created: %%~zA bytes
)

REM Step 2: Verify WAR is a valid zip (using PowerShell)
echo [3/4] Verifying WAR integrity...
powershell -NoProfile -ExecutionPolicy Bypass -Command "^
    try { ^
        [System.IO.Compression.ZipFile]::OpenRead('%WAR_FILE%') | Close-Object; ^
        Write-Host '[OK] WAR file is valid and complete' ^
    } catch { ^
        Write-Host 'ERROR: WAR file is corrupted'; ^
        exit 1 ^
    }"

REM Step 3: Build Docker image
echo [4/4] Building Docker image...
docker build -t ctoon:latest .
if errorlevel 1 (
    echo ERROR: Docker build failed
    exit /b 1
)
echo [OK] Docker image built successfully

echo.
echo === Deployment Ready ===
echo.
echo Next steps:
echo 1. Push to your registry: docker push ctoon:latest
echo 2. Or use Railway dashboard to trigger deployment from this repository
echo.
pause
