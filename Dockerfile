FROM mariadb:10.6.11

ENV MYSQL_DATABASE=oh
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_USER=isf
ENV MYSQL_PASSWORD=isf123

COPY sql/step_01_create_structure.sql /docker-entrypoint-initdb.d/
COPY sql/step_02_dump_menu.sql /docker-entrypoint-initdb.d/
COPY sql/step_03_dump_default_data_en.sql /docker-entrypoint-initdb.d/
#COPY sql/step_04_all_following_steps.sql /docker-entrypoint-initdb.d/
COPY sql/step_05_update_menu_i18n.sql /docker-entrypoint-initdb.d/
COPY sql/step_06_opd_extended.sql /docker-entrypoint-initdb.d/
COPY sql/step_07_modifiche_matiri.sql /docker-entrypoint-initdb.d/
COPY sql/step_08_int_restore_del_labrestype.sql /docker-entrypoint-initdb.d/
COPY sql/step_09_update_agetype.sql /docker-entrypoint-initdb.d/
COPY sql/step_1* /docker-entrypoint-initdb.d/
COPY sql/step_2* /docker-entrypoint-initdb.d/
COPY sql/step_3* /docker-entrypoint-initdb.d/
COPY sql/step_4* /docker-entrypoint-initdb.d/
COPY sql/step_5* /docker-entrypoint-initdb.d/
COPY sql/step_6* /docker-entrypoint-initdb.d/
COPY sql/step_7* /docker-entrypoint-initdb.d/
COPY sql/step_8* /docker-entrypoint-initdb.d/
COPY sql/step_9* /docker-entrypoint-initdb.d/
COPY sql/data_en/* data_en/
#COPY sql/data_es/* data_es/
#COPY sql/data_it/* data_it/
#COPY sql/data_pt/* data_pt/
#COPY sql/data_fr/* data_fr/
