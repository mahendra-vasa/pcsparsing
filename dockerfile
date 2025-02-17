# Generated by IBM Migration Artifact Bundler
# Mon Feb 03 11:13:26 UTC 2025

FROM icr.io/appcafe/open-liberty:kernel-slim-java8-openj9-ubi
#FROM public.ecr.aws/docker/library/open-liberty:24.0.0.12-kernel-slim-java8-openj9

#RUN apt-get update && \
#    apt-get install -y maven unzip

WORKDIR /project
COPY . .

#RUN mvn -X initialize process-resources verify => to get dependencies from maven
#RUN mvn clean install
#RUN mvn -version

RUN mkdir -p /config/dropins && \    
    cp ./src/main/liberty/config/* /config && \
    cp ./target/*.*ar /config/dropins/  

ARG TLS=true

RUN mkdir -p /opt/ol/wlp/usr/shared/config/lib/global

#COPY --chown=1001:0 ./src/main/liberty/config/* /config/
#COPY --chown=1001:0 ./target/*.*ar /config/dropins/
#COPY --chown=1001:0 ./src/main/liberty/lib/* /opt/ol/wlp/usr/shared/config/lib/global

# This script will add the requested XML snippets to enable Liberty features and grow image to be fit-for-purpose using featureUtility.
# Only available in 'kernel-slim'. The 'full' tag already includes all features for convenience.
RUN features.sh

# This script will add the requested server configurations, apply any interim fixes and populate caches to optimize runtime
RUN configure.sh

# Upgrade to production license if URL to JAR provided
ARG LICENSE_JAR_URL
RUN \
   if [ $LICENSE_JAR_URL ]; then \
     wget $LICENSE_JAR_URL -O /tmp/license.jar \
     && java -jar /tmp/license.jar -acceptLicense /opt/ibm \
     && rm /tmp/license.jar; \
   fi
