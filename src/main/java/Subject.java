/**
 * Created by Mello on 30.05.2014.
 */
public class Subject {
    String name;
    static int idSubject;

    public Subject(String name) {
        this.name = name;
        idSubject++;
    }
}
