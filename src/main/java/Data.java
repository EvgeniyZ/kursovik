import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

public class Data {
    static final int STUDENTS_COUNT = 2;
    static final int SUBJECTS_COUNT = 4;
//    // this will load the MySQL driver, each DB has its own driver
//    Class.forName("com.mysql.jdbc.Driver");

    public int[] subjectsInitialize(int count) {
        int[] subjects = new int[count];
        for (int i = 0; i < count; i++)
            subjects[i] = i+1;
        return subjects;
    }

    public static void main(String[] args) {
        Data data = new Data();
        int[] zakharovSubject = data.subjectsInitialize(2);
        int[] perminovSubject = data.subjectsInitialize(3);
        Student zakharovStudent = new Student("E.I.", "Zakharov", zakharovSubject);
        Student perminovStudent = new Student("E.V.", "Perminov", perminovSubject);
        Student[] studentsList = new Student[STUDENTS_COUNT];
        studentsList[0] = zakharovStudent;
        studentsList[1] = perminovStudent;
        Subject[] subjectsList = new Subject[SUBJECTS_COUNT];
        subjectsList[0] = new Subject("Maths");
        subjectsList[1] = new Subject("History");
        subjectsList[2] = new Subject("Chemisty");
        subjectsList[3] = new Subject("Physics");
        Map<Student, int[]> students = new HashMap<Student, int[]>();
        for (int i = 0; i < STUDENTS_COUNT; i++)
            students.put(studentsList[i], studentsList[i].subjects);
        Connection connection = null;
        //URL к базе состоит из протокола:подпротокола://[хоста]:[порта_СУБД]/[БД] и других_сведений
        String url = "jdbc:mysql://localhost:3306/students";
        //Имя пользователя БД
        String name = "root";
        //Пароль
        String password = "swat3456";
        try {
            //Загружаем драйвер
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Драйвер подключен");
            //Создаём соединение
            connection = DriverManager.getConnection(url, name, password);
            System.out.println("Соединение установлено");
            //Для использования SQL запросов существуют 3 типа объектов:
            //1.Statement: используется для простых случаев без параметров
            /*Statement statement = null;

            statement = connection.createStatement();
//            Очищаем базу данных
//            statement.executeUpdate("DELETE FROM students.group");
           // Вставить запись
            statement.executeUpdate("USE STUDENTS");
            statement.executeUpdate("INSERT INTO studentslist(initials, lastname) values" + "(" + zakharovStudent.initials + "," + zakharovStudent.subjects + ")"
                                                                                  );


            // TODO Вставка нескольких строк через BATCH
            // TODO ПРОБЛЕМА С PRIMARY KEYS! ID PROBLEM
            //Выполним запрос
            ResultSet result1 = statement.executeQuery(
                    "SELECT * FROM studentslist");
            //result это указатель на первую строку с выборки
            //чтобы вывести данные мы будем использовать
            //метод next() , с помощью которого переходим к следующему элементу
            System.out.println("Выводим statement");
            while (result1.next())
            {
                System.out.println("Номер в выборке #" + result1.getRow()
                        + "\t Номер в базе #" + result1.getInt("idgroup") + "\t Имя студента " + result1.getString("firstname")
                        + "\t Фамилия студента" + result1.getString("lastname"));
            }
            // Удалить запись
            while (result1.getString("lastname") == "Petrov")
                result1.deleteRow();*/


            //Обновить запись
            // statement.executeUpdate("UPDATE studentslist");
            //2.PreparedStatement: предварительно компилирует запросы,
            //которые могут содержать входные параметры
            PreparedStatement preparedStatement = null;

            //TODO ПРОДУМАТЬ КАК НОРМАЛЬНО ДОБАВЛЯТЬ СТУДЕНТОВ, ПРЕДМЕТЫ И ИХ СВЯЗЬ. СТУДЕНТ 1 УЧИТ ИСТОРИЮ ХИМИЮ БИОЛОГИЮ И Т.Д.
            // TODO через BATCH работает!

            preparedStatement = connection.prepareStatement("USE STUDENTS");
            for (Student student : studentsList) {
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO studentslist(initials, lastname) values" + "(?, ?)");
                preparedStatement.setString(1, student.initials);
                preparedStatement.setString(2, student.lastname);
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
            }
            for (Subject subject : subjectsList) {
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO subjects(name) values" + "(?)");
                preparedStatement.setString(1, subject.name);
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
            }
            for (Student key : students.keySet()) {
                preparedStatement = connection.prepareStatement("INSERT INTO journal(student_id, subject_id) values" + "(?, ?)");
                for (int i = 0; i < students.get(key).length; i++) {
                    preparedStatement.setInt(1, key.idStudent);
                    preparedStatement.setInt(2, students.get(key)[i]);
                    preparedStatement.addBatch();
                    preparedStatement.executeBatch();
                }
            }
/*
            //3.CallableStatement: используется для вызова хранимых функций,
            // которые могут содержать входные и выходные параметры
            CallableStatement callableStatement = null;
            //Вызываем функцию myFunc (хранится в БД)
            callableStatement = connection.prepareCall(
                    " { call myfunc(?,?) } ");
            //Задаём входные параметры
            callableStatement.setString(1, "Dima");
            callableStatement.setString(2, "Alex");
            //Выполняем запрос
            ResultSet result3 = callableStatement.executeQuery();
            //Если CallableStatement возвращает несколько объектов ResultSet,
            //то нужно выводить данные в цикле с помощью метода next
            //у меня функция возвращает один объект
            result3.next();
            System.out.println(result3.getString("MESSAGE"));
            //если функция вставляет или обновляет, то используется метод executeUpdate()*/

        } catch (Exception ex) {
            //выводим наиболее значимые сообщения
            //ex.getMessage();
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.getErrorCode();
                }
            }
        }
    }
}
