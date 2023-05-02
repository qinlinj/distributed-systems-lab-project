# Use Tomcat 10, which supports Servlets 5
FROM tomcat:10.1.0-M5-jdk16-openjdk-slim-bullseye

# This limits the amount of memory used so that free-tier Heroku limits are not exceeded
ENV JAVA_OPTS="-Xmx300m"

# Expose port 8080 when running on localhost
EXPOSE 8080

# Copy in our ROOT.war to the right place in the container
COPY ROOT.war /usr/local/tomcat/webapps/

# LOCALHOST:  Run catalina in the container
# Should map localhost:8080 to this app
CMD ["catalina.sh", "run"]
