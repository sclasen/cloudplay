FROM ubuntu
MAINTAINER Scott Clasen "scott@heroku.com"

RUN echo deb http://archive.ubuntu.com/ubuntu precise universe >> /etc/apt/sources.list
RUN echo deb http://pkg.jenkins-ci.org/debian binary/ > /etc/apt/sources.list.d/jenkins.list
RUN echo "deb http://toolbelt.heroku.com/ubuntu ./" > /etc/apt/sources.list.d/heroku.list
RUN apt-get update

RUN apt-get install -q -y curl telnet wget
RUN wget -O- https://toolbelt.heroku.com/apt/release.key | apt-key add -
RUN wget -q -O - http://pkg.jenkins-ci.org/debian/jenkins-ci.org.key | apt-key add -

RUN apt-get install -q -y --force-yes heroku-toolbelt
RUN heroku plugins:install https://github.com/ddollar/heroku-config
RUN heroku plugins:install https://github.com/heroku/heroku-pipeline

RUN apt-get install -q -y openjdk-7-jre-headless

RUN apt-get install jenkins -y --force-yes

RUN service jenkins stop

RUN mkdir /jenkins
ENV JENKINS_HOME /jenkins

RUN rm /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/java.security
ADD usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/java.security /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/java.security

EXPOSE 8080
CMD ["java", "-Dcom.heroku.jenkins.features='ANVIL,CISAURUS'", "-jar", "/usr/share/jenkins/jenkins.war"]
