package duclass.duclass.a10_restaurant_menu;

/**
 * Created by kimtaewoo on 2016-08-19.
 */
public class menu_for_personnel_Adapter {
    public String num = "";
    public String name = "";

    public menu_for_personnel_Adapter(String num, String name) {
        this.num = num;
        this.name = name;
    }
    public String getNum() {
        return num;
    }
    public String getName() {
        return name;
    }
}
