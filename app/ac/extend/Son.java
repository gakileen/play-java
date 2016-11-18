package ac.extend;

/**
 * Created by acmac on 16/3/22.
 */
public class Son extends Parent {

    private String pri = "priSon";
    protected String pro = "proSon";
    public String pub = "pubSon";

    public void get() {
        System.out.println("Son start");

        System.out.println(pri);
        System.out.println(pro);
        System.out.println(pub);


        System.out.println("Son end");
    }

    public static void main(String[] args) {
/*        Parent r = new Parent();
        r.get();*/

        Parent s = new Son();
        s.hello();

    }

}
