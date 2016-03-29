FROM httpd:2.4.18
MAINTAINER Miro Cupak <mirocupak@gmail.com>

# set up env vars
ENV CONFIGURE_SSL false
ENV CONFIGURE_CLIENT true
ENV CONFIGURE_BEACONIZER false
ENV SERVER_NAME localhost
ENV BECONIZER_URL https://beacon-network.org/beaconizer

ENV HTTPD_HOME /usr/local/apache2
ENV HTTPD_CONF $HTTPD_HOME/conf/httpd.conf
ENV HTTPD_SSL $HTTPD_HOME/conf
ENV HTTPD_CLIENT $HTTPD_HOME/htdocs/client

# install packages
USER root
RUN yes | apt-get update
RUN yes | apt-get install patch curl vim wget zip tree less

# copy data
ADD data $HTTPD_HOME/data

# set up httpd
WORKDIR $HTTPD_HOME/data
RUN sed -i "s/^#ServerName www.example.com:80/ServerName $SERVER_NAME/" $HTTPD_CONF
RUN sed -i "s/^#LoadModule rewrite_module/LoadModule rewrite_module/" $HTTPD_CONF
RUN sed -i "s/^#LoadModule proxy_module/LoadModule proxy_module/" $HTTPD_CONF
RUN sed -i "s/^#LoadModule proxy_http_module/LoadModule proxy_http_module/" $HTTPD_CONF
RUN if [ "$CONFIGURE_CLIENT" = true ] ; then cp -r client $HTTPD_CLIENT; fi
RUN cat base.conf >> $HTTPD_CONF
RUN if [ "$CONFIGURE_SSL" = true ] ; then sed -i "s/^#LoadModule ssl_module/LoadModule ssl_module/" $HTTPD_CONF; sed -i "s/^#LoadModule socache_shmcb_module/LoadModule socache_shmcb_module/" $HTTPD_CONF; cat ssl.conf >> $HTTPD_CONF; sed -i "s/^# RewriteRule/RewriteRule/g" $HTTPD_CONF; cp crt/*.crt $HTTPD_SSL/crt.crt; cp crt/*.key $HTTPD_SSL/key.key; cp crt/*.pem $HTTPD_SSL/chain.pem; fi
RUN sed -i "s/SERVER_NAME/$SERVER_NAME/g" $HTTPD_CONF
RUN if [ "$CONFIGURE_BEACONIZER" = true ] ; then sed -i "s/^# ProxyPass \/beaconizer/ProxyPass \/beaconizer/g" $HTTPD_CONF; sed -i "s/^# ProxyPassReverse \/beaconizer/ProxyPassReverse \/beaconizer/g" $HTTPD_CONF; sed -i "s/BEACONIZER_URL/$BEACONIZER_URL/g" $HTTPD_CONF; fi
WORKDIR $HTTPD_HOME

# expose ports
EXPOSE 80
EXPOSE 443
