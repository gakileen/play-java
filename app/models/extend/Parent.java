package models.extend;

/**
 * Created by acmac on 16/3/22.
 */
public class Parent {

    private String pri = "priParent";
    protected String pro = "proParent";
    public String pub = "pubParent";

    public void get() {
        System.out.println("Parent start");

        System.out.println(pri);
        System.out.println(pro);
        System.out.println(pub);


        System.out.println("Parent end");
    }

    public void hello() {
        get();
    }

}
