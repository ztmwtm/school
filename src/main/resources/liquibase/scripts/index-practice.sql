-- liquibase formatted sql

-- changeset VladimirKudriavtsev:1
CREATE INDEX students_name_index ON student (name);

-- changeset VladimirKudriavtsev:2
CREATE INDEX faculties_name_color_index ON faculty (name, color);
