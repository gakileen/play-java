package models.extend;

/**
 * Created by acmac on 16/3/22.
 */
public class Parent {

    private String pri = "parent_pri";
    protected String pro = "parent_pro";
    public String pub = "parent_pub";

    Parent(String pro, String pub) {

        System.out.println("===Parent start");

        this.pro = pro;
        this.pub = pub;

        System.out.println("===Parent end");
    }

    public void get() {
        System.out.println("---parent_get_start");

        System.out.println(pri);
        System.out.println(pro);
        System.out.println(pub);


        System.out.println("---parent_get_end");
    }

    public void hello() {
        get();
    }

}
