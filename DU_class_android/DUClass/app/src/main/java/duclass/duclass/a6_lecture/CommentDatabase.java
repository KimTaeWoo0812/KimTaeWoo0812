package duclass.duclass.a6_lecture;

/**
 * Created by HoChan on 2015-11-19.
 */
public class CommentDatabase  {
	
	public String comNum = "";
	public String id = "";
    public String nickname = "";
    public String comment = "";
    public String gradesAvr = "";
    public String like = "";
    public String unlike = "";
    public String date = "";

    public CommentDatabase(  String comNum, String comment, String id, String nickname, String rating, String like, String unlike, String date) {
        this.nickname = nickname;
        this.comment = comment;
        this.gradesAvr = rating;
        this.like = like;
        this.unlike = unlike;
        this.comNum = comNum;
        this.id = id;
        this.date = date;
    }

    public String getNickname() {
        return nickname;
    }
    public String getComment() {
        return comment;
    }
    public String getComNum() {
        return comNum;
    }
    public String getGradesAvr() { return gradesAvr; }
    public String getGood() { return like; }
    public String getBad() { return unlike; }
    public String getDate() { return date; }
}
