FROM jboss/wildfly:8.2.1.Final

ENV JBOSS_HOME /opt/jboss/wildfly

ADD data $JBOSS_HOME/customization/

USER root
RUN find $JBOSS_HOME/customization/crt/ -type f -exec keytool -import -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -noprompt -alias `basename {}` -file {} \;
RUN yes | yum install patch

USER jboss
RUN patch $JBOSS_HOME/bin/standalone.conf < $JBOSS_HOME/customization/opts.patch
RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

CMD ["/opt/jboss/wildfly/customization/execute.sh"]
