# CToon V2 - Deployment Guide

## Quick Start

### Option 1: Local Deployment (Windows)
```bash
./deploy.bat
```

### Option 2: Local Deployment (macOS/Linux)
```bash
chmod +x deploy.sh
./deploy.sh
```

### Option 3: Railway Deployment
```bash
# 1. Connect your GitHub repository to Railway
# 2. Railway will automatically detect Dockerfile and build/deploy

# Or deploy manually:
railway login
railway up
```

---

## Deployment Architecture

The application uses a **multi-stage Docker build** to ensure:
- ✅ Consistent builds across environments
- ✅ No WAR file corruption issues
- ✅ Automatic integrity verification
- ✅ Smaller final image size

### Build Process
1. **Builder Stage**: Compiles code and creates WAR
2. **Verification**: Ensures WAR is valid (ZIP format)
3. **Runtime Stage**: Deploys WAR to Tomcat 9 with JDK 17

---

## Prerequisites

### For Local Docker Builds
- Docker Desktop installed and running
- Maven 3.8+ (automatically detected in Dockerfile)
- Java 11+ (for local Maven builds)

### For Railway Deployment
- GitHub repository connected to Railway
- Railway account (free tier available)
- Optional: Railway CLI installed

---

## Configuration Files

### `Dockerfile`
- Multi-stage build for WAR compilation
- Tomcat 9 with JDK 17
- Automatic health checks
- 512MB max heap size (adjustable via `JAVA_OPTS`)

### `railway.toml`
- Railway-specific configuration
- Deployment settings
- Environment variables

### `deploy.bat` / `deploy.sh`
- Automated build and verification scripts
- Checks WAR integrity before deployment
- Ready for CI/CD integration

### `.github/workflows/deploy.yml`
- GitHub Actions automation
- Builds and pushes to Docker registry
- Auto-deploys to Railway on push to `main`

---

## Troubleshooting

### Issue: `zip END header not found`
**Solution**: Use the multi-stage Dockerfile which builds WAR inside container
- ✅ Already fixed with `Dockerfile` update

### Issue: 404 Error on deployed site
**Ensure**:
1. WAR is deployed as ROOT application (not in subdirectory)
2. Check Tomcat logs: `docker logs <container-id>`
3. Verify database connection settings

### Issue: Out of Memory
**Solution**: Increase heap size in `Dockerfile` or set `JAVA_OPTS`:
```bash
docker run -e JAVA_OPTS="-Xmx1g -Xms512m" ctoon:latest
```

---

## Database Configuration

Set environment variables for database connection:
```bash
docker run \
  -e DB_HOST=your-db-host \
  -e DB_PORT=3306 \
  -e DB_NAME=ctoon \
  -e DB_USER=root \
  -e DB_PASSWORD=password \
  ctoon:latest
```

Or update `src/main/java/util/DBUtil.java` with connection details.

---

## Performance Tuning

### JVM Settings (in Dockerfile)
- `Xmx512m`: Maximum heap size (increase for large datasets)
- `Xms256m`: Initial heap size
- `-Djava.awt.headless=true`: Disable GUI for server mode

### Tomcat Settings
- Connection pool size in `web.xml`
- Thread pool configuration in `server.xml`

---

## Monitoring & Health Checks

The deployment includes automatic health checks:
- **Interval**: 30 seconds
- **Timeout**: 10 seconds
- **Start Period**: 60 seconds (time before first check)
- **Retry**: 5 failed checks before restart

View health status:
```bash
docker ps  # Shows health status in STATUS column
docker inspect <container-id>  # Detailed health info
```

---

## Rollback

To rollback to a previous version:
```bash
# Railway dashboard: Select previous deployment
# Or local Docker:
docker pull ctoon:previous-tag
docker run -p 8080:8080 ctoon:previous-tag
```

---

## Support

For deployment issues:
1. Check `Dockerfile` and `railway.toml` configuration
2. Review Tomcat logs: `docker logs <container-id>`
3. Verify WAR file integrity: `unzip -t target/ctoon-1.0-SNAPSHOT.war`
4. Test locally before pushing to production

---

# Deployment Instructions

## 1. Build the Project

Run the following command in the project root directory:

```powershell
mvn clean package
```

The generated WAR file will be located at:
```
target/ctoon-1.0-SNAPSHOT.war
```

## 2. Deploy to Tomcat

1. Copy the WAR file from `target/ctoon-1.0-SNAPSHOT.war` to your Tomcat `webapps` directory.
2. Start Tomcat. The application will be deployed automatically.

## 3. Access the Application

Open your browser and go to:
```
http://localhost:8080/ctoon-1.0-SNAPSHOT/
```

## 4. Database Setup

To populate the database, run:
```powershell
python populate_database.py
```

## 5. Project Structure Reference

```
src/main/java/controller/         # Controllers
src/main/java/dao/               # Data Access Objects
src/main/java/model/             # Models
src/main/java/util/              # Utilities
src/main/resources/              # Resources
src/main/webapp/                 # Web assets (JSP, CSS, JS, images)
src/main/webapp/WEB-INF/web.xml  # Web application descriptor
pom.xml                          # Maven build file
README.md                        # Project documentation
DEPLOYMENT.md                    # Deployment instructions
```
