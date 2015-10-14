FROM jboss/wildfly:8.2.1.Final

# prepare wildfly
ENV JBOSS_HOME /opt/jboss/wildfly
ADD data $JBOSS_HOME/customization/

USER root

# install necessary packages
RUN yes | yum install patch

# update nss and java to work around a bug with Diffie-Hellman key agreement of more than 1024 bits
RUN yes | yum upgrade nss java

# install certificates
RUN find $JBOSS_HOME/customization/crt/ -type f -exec keytool -import -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -noprompt -alias `basename {}` -file {} \;

# customize wildfly
USER jboss
RUN patch $JBOSS_HOME/bin/standalone.conf < $JBOSS_HOME/customization/opts.patch
RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

CMD ["/opt/jboss/wildfly/customization/execute.sh"]
