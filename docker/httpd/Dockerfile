FROM httpd:2.4.23
MAINTAINER Miro Cupak <mirocupak@gmail.com>

# set up env vars
# change env vars in this block to configure
ENV CONFIGURE_SSL=false
ENV CONFIGURE_CLIENT=true
ENV CONFIGURE_SWAGGER=false
ENV SERVER_NAME=localhost

# you should not need to change these env vars
ENV HTTPD_HOME=/usr/local/apache2
ENV HTTPD_CONF=$HTTPD_HOME/conf/httpd.conf
ENV HTTPD_SSL=$HTTPD_HOME/conf
ENV HTTPD_CLIENT=$HTTPD_HOME/htdocs/client
ENV HTTPD_DOCS=$HTTPD_CLIENT/docs
ENV BEACONIZER_URL=http://beaconizer:8080/beacon
ENV SWAGGER_VERSION=2.1.4

# install packages
USER root
RUN yes | apt-get update
RUN yes | apt-get install wget

# copy data
ADD data $HTTPD_HOME/data
ADD entrypoint.sh ${HTTPD_HOME}/entrypoint.sh

WORKDIR $HTTPD_HOME

ENTRYPOINT ["entrypoint.sh"]
