package duclass.duclass.a5_search_make;


public final class SearchDatabase {
	public String num = "";
    public String major = "";
    public String title = "";
    public String professor = "";
    public String gradesAvr = "";
    public String joinPeople = "";


    public SearchDatabase(String num, String major, String title, String professor, String gradesAvr, String joinPeople) {
    	this.num = num;
    	this.major = major;
        this.title = title;
        this.professor = professor;
        this.gradesAvr = gradesAvr;
        this.joinPeople = joinPeople;
    }
    public String getNum() {
        return num;
    }
    public String getProfessor() {
        return professor;
    }
    public String getTitle() {
        return title;
    }
    public void setMajor(String major) {
        this.major = major;
    }
    public void setTitle(String lecture) {
        this.title = lecture;
    }
    public String getMajor() {
        return major;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getJoinPeople() { return joinPeople; }

    public String getGradesAvr() { return gradesAvr;}

}
