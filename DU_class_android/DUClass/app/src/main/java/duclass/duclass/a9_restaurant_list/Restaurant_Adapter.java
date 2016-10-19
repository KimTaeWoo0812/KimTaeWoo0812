package duclass.duclass.a9_restaurant_list;

/**
 * Created by kimtaewoo on 2016-08-17.
 */
public class Restaurant_Adapter {
    public String num = "";
    public String name = "";
    public String grade = "";
    public String rank="";

    public Restaurant_Adapter(String num, String name, String grade, int rank) {
        this.num = num;
        this.name = name;
        this.grade = grade;
        this.rank = Integer.toString(rank);
    }
    public String getNum() {
        return num;
    }
    public String getName() {
        return name;
    }
    public String getGrade() {
        return grade;
    }

    public String getRank() {
        return rank;
    }
}
