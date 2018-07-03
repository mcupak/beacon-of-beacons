#!/usr/bin/env bash

set -ex

if [ -z ${SWAGGER_JSON_URL} ]; then
  export SWAGGER_JSON_URL=http://${SERVER_NAME}/docs/swagger.json
fi

sed -i "s/^#ServerName www.example.com:80/ServerName ${SERVER_NAME}/" ${HTTPD_CONF}
sed -i "s/^#LoadModule rewrite_module/LoadModule rewrite_module/" ${HTTPD_CONF}
sed -i "s/^#LoadModule proxy_module/LoadModule proxy_module/" ${HTTPD_CONF}
sed -i "s/^#LoadModule proxy_http_module/LoadModule proxy_http_module/" ${HTTPD_CONF}

if [ "${CONFIGURE_CLIENT}" = true ]; then
  mkdir ${HTTPD_CLIENT}
  cp -r client/* ${HTTPD_CLIENT}/
fi

cat ${HTTPD_HOME}/data/base.conf >> ${HTTPD_CONF}

if [ "${CONFIGURE_SSL}" = true ]; then
  sed -i \
    -e "s/^#Listen 12.34.56.78:80/Listen 443/" \
    -e "s/^#LoadModule ssl_module/LoadModule ssl_module/" \
    -e "s/^#LoadModule socache_shmcb_module/LoadModule socache_shmcb_module/" \
    ${HTTPD_CONF}
  cat ${HTTPD_HOME}/data/ssl.conf >> ${HTTPD_CONF}
  sed -i "s/^# RewriteRule/RewriteRule/g" ${HTTPD_CONF}
  cp crt/*.crt ${HTTPD_SSL}/crt.crt && cp crt/*.key ${HTTPD_SSL}/key.key
  cp crt/*.pem ${HTTPD_SSL}/chain.pem
fi

if [ "${CONFIGURE_SWAGGER}" = true ]; then
  cd /tmp
  wget -qO- https://github.com/swagger-api/swagger-ui/archive/v${SWAGGER_VERSION}.tar.gz | tar xz
  mv swagger-ui-${SWAGGER_VERSION}/dist ${HTTPD_DOCS}
  rm -rf swagger-ui-*
  cd -
  cp -r docs/* ${HTTPD_DOCS}/
  sed -i "s SWAGGER_JSON_URL ${SWAGGER_JSON_URL} g" ${HTTPD_DOCS}/index.html
  sed -i "s localhost:8080 ${SERVER_NAME} g" ${HTTPD_DOCS}/swagger.json
  if [ "${CONFIGURE_SSL}" = true ]; then
    sed -i "s/http/https/g" ${HTTPD_DOCS}/swagger.json
  fi
  sed -i "s/^# Header set Access-Control-/Header set Access-Control-/g" ${HTTPD_CONF}
fi

sed -i \
  -e "s/SERVER_NAME/${SERVER_NAME}/g" \
  -e "s/^# ProxyPass \/beaconizer/ProxyPass \/beaconizer/g" \
  -e "s/^# ProxyPassReverse \/beaconizer/ProxyPassReverse \/beaconizer/g" \
  -e "s BEACONIZER_URL ${BEACONIZER_URL} g" \
  ${HTTPD_CONF}

httpd-foreground "$@"
