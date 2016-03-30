FROM jboss/wildfly:8.2.1.Final
MAINTAINER Miro Cupak <mirocupak@gmail.com>

# prepare wildfly
ENV JBOSS_HOME /opt/jboss/wildfly
ADD data $JBOSS_HOME/customization/

USER root

# install necessary packages
RUN yes | yum update
RUN yes | yum install patch wget

# update nss and java to work around a bug with Diffie-Hellman key agreement of more than 1024 bits
RUN yes | yum upgrade nss java

# download mysql driver
RUN cd /tmp
RUN wget http://search.maven.org/remotecontent?filepath=mysql/mysql-connector-java/5.1.32/mysql-connector-java-5.1.32.jar
RUN mv *.jar $JBOSS_HOME/customization/mysql-connector-java.jar

# install certificates
RUN find $JBOSS_HOME/customization/crt/ -type f -exec keytool -import -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -noprompt -alias `basename {}` -file {} \;

# customize wildfly
USER jboss
RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent
ENV JAVA_OPTS "-Xms256m -Xmx2048m -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m -Djava.net.preferIPv4Stack=true -Djboss.modules.system.pkgs=org.jboss.byteman -Djava.awt.headless=true"

# expose ports
EXPOSE 8080
EXPOSE 9990

ENTRYPOINT ["/opt/jboss/wildfly/customization/execute.sh"]
