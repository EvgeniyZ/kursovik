USE students;
SELECT * FROM list;
ALTER TABLE list ADD FOREIGN KEY (subjectID) references students.subjects (id);