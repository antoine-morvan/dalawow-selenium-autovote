FROM selenium/standalone-firefox:3.141.59

# Make sure all is up to date and Maven is present, as well as Xvfb
RUN \
  sudo apt update && \
  sudo apt -y upgrade && \
  sudo apt -y install maven xvfb openjdk-8-jdk bsdmainutils bash

# Prepare working dir
RUN \
  sudo mkdir -p /var/autovote && \
  sudo chmod 777 /var/autovote

# Copy and build the autovote Java app
COPY dalawow-selenium-autovote /var/autovote/

WORKDIR /var/autovote/
RUN mvn clean package

COPY entry-point.sh /var/autovote/
RUN sudo chmod a+x /var/autovote/entry-point.sh

ENTRYPOINT ["./entry-point.sh"]
#CMD ["--help"]
