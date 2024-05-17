FROM postgres:14.3	
ENV POSTGRES_PASSWORD postgres
ENV POSTGRES_DB DB_STUDENT
ENV POSTGRES_USER postgres

ARG SOURCE_SQL=./SQL/schema.sql  
WORKDIR /
ADD ${SOURCE_SQL} /docker-entrypoint-initdb.d/schema.sql
