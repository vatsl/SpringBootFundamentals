DROP TABLE if exists larku.Student_Scheduledclass;

DROP TABLE if exists larku.ScheduledClass;

DROP TABLE if exists larku.Course;

DROP TABLE if exists larku.Student;


CREATE TABLE Student
(
    id          INT(11)      NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(20),
    status      VARCHAR(20),
    CONSTRAINT student_pk PRIMARY KEY (id)
);

CREATE TABLE Course
(
    id      INT    NOT NULL AUTO_INCREMENT,
    code    VARCHAR(20),
    credits DOUBLE NOT NULL,
    title   VARCHAR(255),
    CONSTRAINT course_pk PRIMARY KEY (id)
);

CREATE TABLE ScheduledClass
(
    id        INT NOT NULL AUTO_INCREMENT,
    enddate   VARCHAR(20),
    startdate VARCHAR(20),
    course_id INTEGER,
    CONSTRAINT class_pk PRIMARY KEY (id)
);

CREATE TABLE larku.Student_Scheduledclass
(
    students_id INTEGER NOT NULL,
    classes_id  INTEGER NOT NULL
);

CREATE UNIQUE INDEX SQL150211090953900 ON larku.Course (id ASC);

CREATE INDEX SQL150211090954080 ON larku.Student_Scheduledclass (classes_id ASC);

CREATE INDEX SQL150211090953990 ON larku.ScheduledClass (course_id ASC);

CREATE UNIQUE INDEX SQL150211090953920 ON larku.ScheduledClass (id ASC);

CREATE UNIQUE INDEX SQL150211090953950 ON larku.Student (id ASC);

CREATE INDEX SQL150211090954040 ON larku.Student_Scheduledclass (students_id ASC);

-- ALTER TABLE larku.COURSE ADD CONSTRAINT SQL150211090953900 PRIMARY KEY (ID);

-- ALTER TABLE larku.SCHEDULEDCLASS ADD CONSTRAINT SQL150211090953920 PRIMARY KEY (ID);

-- ALTER TABLE larku.STUDENT ADD CONSTRAINT SQL150211090953950 PRIMARY KEY (ID);

ALTER TABLE larku.Student_Scheduledclass
    ADD CONSTRAINT FK318CA38F83AAB40 FOREIGN KEY (classes_id)
        REFERENCES larku.ScheduledClass (id);

ALTER TABLE larku.Student_Scheduledclass
    ADD CONSTRAINT FK318CA38F5E4D1BDC FOREIGN KEY (students_id)
        REFERENCES larku.Student (id);

ALTER TABLE larku.Student_Scheduledclass
    ADD CONSTRAINT NEW_UNIQUE UNIQUE (students_id, classes_id);

ALTER TABLE larku.ScheduledClass
    ADD CONSTRAINT FKE64C28EBD735B77B FOREIGN KEY (course_id)
        REFERENCES larku.Course (id);
	


	