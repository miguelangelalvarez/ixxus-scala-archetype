FROM hseeberger/scala-sbt

# Install Sonar scanner
RUN wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-3.3.0.1492-linux.zip && \
  unzip sonar-scanner-cli-3.3.0.1492-linux.zip && \
  mv sonar-scanner-3.3.0.1492-linux /opt/sonar-scanner && \
  rm -f sonar-scanner-cli-3.3.0.1492-linux.zip

# Install Docker repository
RUN apt-get update && \
  apt-get -y install apt-transport-https ca-certificates curl gnupg2 software-properties-common && \
  curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - && \
  add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian stretch stable"

# Install Docker
RUN apt-get update && \
  apt-get -y install docker-ce
