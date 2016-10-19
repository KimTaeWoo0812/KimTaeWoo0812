package duclass.duclass.a11_restaurant_review;

/**
 * Created by HoChan on 2015-11-19.
 */
public class review_Adapter {

	public String num = "";
	public String id = "";
    public String nickname = "";
    public String comment = "";
    public String gradesAvr = "";
    public String date = "";

    public review_Adapter(String num, String id, String nickname, String comment, String rating, String date) {
        this.num = num;
        this.nickname = nickname;
        this.comment = comment;
        this.gradesAvr = rating;
        this.id = id;
        this.date = date;
    }

    public String getId() { return id; }
    public String getNickname() {
        return nickname;
    }
    public String getComment() {
        return comment;
    }
    public String getNum() {
        return num;
    }
    public String getGradesAvr() { return gradesAvr; }
    public String getDate() { return date; }
}
