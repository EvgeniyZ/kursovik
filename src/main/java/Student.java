/**
 * Created by Mello on 30.05.2014.
 */
public class Student {
    String initials;
    String lastname;
    int[] subjects;
    static int idStudent;

    public Student(String initials, String lastname, int[] subjects) {
        this.initials = initials;
        this.lastname = lastname;
        this.subjects = subjects;
        idStudent++;
    }
}
