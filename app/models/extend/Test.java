package models.extend;

/**
 * Created by ac on 2017/3/27.
 */
public class Test {

    public static void main(String[] args) {
        SS a = new SS("11");
        a.get();
    }


}


class PP {
    String name = "PP";

    public PP(String name) {
        this.name = name;
    }

    void get() {
        System.out.println(name);
    }
}

class SS extends PP {
    String name = "SS";

    public SS(String name) {
        super(name);
    }

/*    void get() {
        System.out.println(name);
    }*/

}