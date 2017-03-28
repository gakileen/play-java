package models.extend;

/**
 * Created by acmac on 16/3/22.
 */
public class Son extends Parent {

    private String pri = "son_pri";
    protected String pro = "son_pro";
    public String pub = "son_pub";

    Son(String pro, String pub) {
        super(pro, pub);

        System.out.println("===Son start");


        System.out.println("===Son end");
    }

    public void get() {
        System.out.println("---son_get_start");

        System.out.println(pri);
        System.out.println(pro);
        System.out.println(pub);


        System.out.println("---son_get_end");
    }

    public static void main(String[] args) {
/*        Parent p = new Parent("1", "2");
        p.hello();
        System.out.println("***" + p.pro);
        System.out.println("***" + p.pub);*/

/*        Son s = new Son("1", "2");
        s.hello();
        System.out.println("***" + s.pro);
        System.out.println("***" + s.pub);*/

/*        Parent s = new Son("1", "2");
        s.hello();
        System.out.println("***" + s.pro);
        System.out.println("***" + s.pub);*/

/*        Son s = new Son("1", "2");
        Parent p = (Parent) s;
        p.hello();
        System.out.println("***" + p.pro);
        System.out.println("***" + p.pub);*/


    }

}
