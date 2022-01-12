import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class SerializeTest {
    @Test
    public void testSave(){
        File file = new File("C:\\Users\\WIN10\\Desktop\\test\\a.dat");
        try {
            T t = new T();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(t);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testRead(){
        File file = new File("C:\\Users\\WIN10\\Desktop\\test\\a.dat");
        try {
            //T t = new T();
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            T t1= (T) ois.readObject();
            System.out.println(t1.m+"  ====== "+t1.n);
            Assertions.assertEquals(4,t1.m);
            Assertions.assertEquals(8,t1.n);

            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
class T implements Serializable{
    int m = 4;
    //transient 关键字
    transient int n = 8;
}