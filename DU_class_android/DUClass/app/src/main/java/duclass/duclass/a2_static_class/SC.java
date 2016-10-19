package duclass.duclass.a2_static_class;


public class SC {
	public static String _del = "";// ������ ������
	public static String _endDel = "";// ��̸���Ʈ ���� ������
	public static String _endSendDel = "";// ������ �� ������
	public static String key = "04250928";
	public static String isMake = "";
	public final static int  commentNum_Limt = 2;

	
	public static void SetDel(){
		SC._del="";
		SC._endDel="";
		SC._endSendDel="";

		SC._del+=(char)200;
		SC._endDel+=(char)201;
		SC._endSendDel+=(char)202;
	}


}
