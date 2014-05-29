import javax.swing.*;
import java.sql.*;
import java.util.logging.*;

public class Data {

//    // this will load the MySQL driver, each DB has its own driver
//    Class.forName("com.mysql.jdbc.Driver");


    public static void main(String[] args) {

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
            Statement statement = null;

            statement = connection.createStatement();
//            Очищаем базу данных
            statement.executeUpdate("DELETE FROM students.group");
           // Вставить запись
            statement.executeUpdate("INSERT INTO students.group(idgroup, firstname, lastname) values(1,'Anastasya', 'Lubich')");
            statement.executeUpdate("INSERT INTO students.group(idgroup, firstname, lastname) values(2,'Evgeny', 'Perminov')");
            statement.executeUpdate("INSERT INTO students.group(idgroup, firstname, lastname) values(3,'Evgeny', 'Zakharov')");
            statement.executeUpdate("INSERT INTO students.group(idgroup, firstname, lastname) values(4,'Nikita', 'Ermolenko')");

            // TODO Вставка нескольких строк через BATCH
            // TODO SQL INJECTION

            //Выполним запрос
            ResultSet result1 = statement.executeQuery(
                    "SELECT * FROM students.group");
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
            while (result1.getString("firstname") == "Nikita")
                result1.deleteRow();


            //Обновить запись
            statement.executeUpdate("UPDATE students.group SET root = 'admin' where id = 1");
            /*//2.PreparedStatement: предварительно компилирует запросы,
            //которые могут содержать входные параметры
            PreparedStatement preparedStatement = null;
            // ? - место вставки нашего значеня
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users where id > ? and id < ?");
            //Устанавливаем в нужную позицию значения определённого типа
            preparedStatement.setInt(1, 2);
            preparedStatement.setInt(2, 10);
            //выполняем запрос
            ResultSet result2 = preparedStatement.executeQuery();

            System.out.println("Выводим PreparedStatement");
            while (result2.next()) {
                System.out.println("Номер в выборке #" + result2.getRow()
                        + "\t Номер в базе #" + result2.getInt("id")
                        + "\t" + result2.getString("username"));
            }

            preparedStatement = connection.prepareStatement(
                    "INSERT INTO users(username) values(?)");
            preparedStatement.setString(1, "user_name");
            //метод принимает значение без параметров
            //темже способом можно сделать и UPDATE
            preparedStatement.executeUpdate();



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
