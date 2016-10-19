package duclass.duclass.a12_chatting;

/**
 * Created by kimtaewoo on 2016-10-01.
 */
public class chatting_Adapter {

    public String id = "";
    public String nickname = "";
    public String comment = "";
    public String date = "";
    public boolean isitme=false;

    public chatting_Adapter(String id, String nickname, String comment, String date, boolean isitme) {
        this.id = id;
        this.nickname = nickname;
        this.comment = comment;
        this.date = date;
        this.isitme = isitme;
    }

    public String getId(){
        return id;
    }
    public String getNickname() {
        return nickname;
    }
    public String getComment() {
        return comment;
    }
    public String getDate() { return date; }
    public boolean getIsitme(){ return isitme; }
}
