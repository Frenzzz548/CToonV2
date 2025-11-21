@echo off
REM Complete automated deployment to Railway
REM Prerequisites: Git and Docker installed

echo === CToon V2 Complete Deployment ===
echo.

REM Step 1: Verify Git
git --version > nul 2>&1
if errorlevel 1 (
    echo ERROR: Git is not installed or not in PATH
    exit /b 1
)
echo [OK] Git verified

REM Step 2: Clean build
echo.
echo [1/5] Cleaning previous build...
call mvn clean > nul 2>&1

REM Step 3: Build WAR
echo [2/5] Building WAR file...
call mvn package -DskipTests -q

if not exist "target\ctoon-1.0-SNAPSHOT.war" (
    echo ERROR: WAR file failed to build
    exit /b 1
)
echo [OK] WAR file created

REM Step 4: Commit changes
echo [3/5] Committing to Git...
git add -A > nul 2>&1
git commit -m "Automated build: WAR file generated" > nul 2>&1 || echo [INFO] No changes to commit
echo [OK] Changes committed

REM Step 5: Push to GitHub
echo [4/5] Pushing to GitHub (triggers Railway deployment)...
git push origin main
if errorlevel 1 (
    echo WARNING: Push failed - check your Git configuration
    echo           Manual push may be required
) else (
    echo [OK] Pushed to GitHub
)

REM Step 6: Instructions
echo [5/5] Deployment Instructions
echo.
echo === Next Steps ===
echo 1. Visit your Railway dashboard: https://railway.app/dashboard
echo 2. Check deployment status
echo 3. View live logs during build
echo 4. Application will be available at your Railway URL
echo.
echo === Manual Docker Build (Optional) ===
echo docker build -t ctoon:latest .
echo docker push your-registry/ctoon:latest
echo.
pause
