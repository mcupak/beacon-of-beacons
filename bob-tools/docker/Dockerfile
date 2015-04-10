FROM jboss/wildfly:latest

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

ADD data /opt/jboss/wildfly/customization/

CMD ["/opt/jboss/wildfly/customization/execute.sh"]

