FROM jboss/wildfly:latest

ENV JBOSS_HOME /opt/jboss/wildfly

ADD data $JBOSS_HOME/customization/

RUN find $JBOSS_HOME/customization/crt/ -type f -exec keytool -import -trustcacerts -keystore cacerts -storepass changeit -noprompt -alias `basename {}` -file {} \;

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

CMD ["/opt/jboss/wildfly/customization/execute.sh"]

