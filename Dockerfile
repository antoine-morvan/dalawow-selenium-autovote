FROM selenium/standalone-firefox:latest

# Make sure all is up to date and Maven is present, as well as Xvfb
RUN \
  sudo apt update && \
  sudo apt -y upgrade && \
  sudo apt -y install maven xvfb openjdk-8-jdk

# Prepare working dir
RUN \
  sudo mkdir -p /var/autovote && \
  sudo chmod 777 /var/autovote

# Copy and build the autovote Java app
COPY dalawow-selenium-autovote /var/autovote/
COPY entry-point.sh /var/autovote/

WORKDIR /var/autovote/
RUN \
  mvn clean package && \
  chmod a+x /var/autovote/docker-entrypoint.sh

ENTRYPOINT ["/var/autovote/docker-entrypoint.sh"]
CMD['--help']
