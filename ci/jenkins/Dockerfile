# Jenkins LTS sobre JDK 21 con las herramientas que usan los tres pipelines.
FROM jenkins/jenkins:lts-jdk21

USER root

# psql para el pipeline de base de datos, Node.js para las pruebas de JavaScript del frontend
# y Python para sqlfluff (analisis de calidad del SQL).
RUN apt-get update \
    && apt-get install -y --no-install-recommends postgresql-client nodejs npm python3 python3-venv curl ca-certificates \
    && rm -rf /var/lib/apt/lists/*

RUN python3 -m venv /opt/sqlfluff \
    && /opt/sqlfluff/bin/pip install --no-cache-dir sqlfluff==3.2.5 \
    && ln -s /opt/sqlfluff/bin/sqlfluff /usr/local/bin/sqlfluff

# Trivy (escaneo de vulnerabilidades) con version fija y verificacion de checksum.
ARG TRIVY_VERSION=0.75.0
RUN cd /tmp \
    && curl -fsSLO "https://github.com/aquasecurity/trivy/releases/download/v${TRIVY_VERSION}/trivy_${TRIVY_VERSION}_Linux-64bit.tar.gz" \
    && curl -fsSLO "https://github.com/aquasecurity/trivy/releases/download/v${TRIVY_VERSION}/trivy_${TRIVY_VERSION}_checksums.txt" \
    && grep " trivy_${TRIVY_VERSION}_Linux-64bit.tar.gz$" "trivy_${TRIVY_VERSION}_checksums.txt" | sha256sum -c - \
    && tar -xzf "trivy_${TRIVY_VERSION}_Linux-64bit.tar.gz" -C /usr/local/bin trivy \
    && rm -f /tmp/trivy_*

USER jenkins
