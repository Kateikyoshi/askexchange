FROM quay.io/keycloak/keycloak:21.1.1
RUN mkdir -p /opt/keycloak/data/import
COPY ./configs/keycloak/askexch-realm2.json /opt/keycloak/data/import/askexch-realm2.json
#RUN /opt/keycloak/bin/kc.sh import --dir /opt/keycloak/data/import --override true
#RUN /opt/keycloak/bin/kc.sh import --file=/opt/keycloak/data/import/askexch-realm.json