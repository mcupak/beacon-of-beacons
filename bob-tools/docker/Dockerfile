FROM jboss/wildfly:latest

ENV JBOSS_HOME /opt/jboss/wildfly

ADD data $JBOSS_HOME/customization/

USER root
RUN find $JBOSS_HOME/customization/crt/ -type f -exec keytool -import -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -noprompt -alias `basename {}` -file {} \;

USER jboss
RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

CMD ["/opt/jboss/wildfly/customization/execute.sh"]
