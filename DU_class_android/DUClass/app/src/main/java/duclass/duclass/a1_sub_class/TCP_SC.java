package duclass.duclass.a1_sub_class;

/*
 * 사용법: 여기서 소켓클래스 싱글톤으로 연다.
 * 맨 처음 SetData()로 소켓 열고, 초기화 하고
 * GetMsg()로 받고
 * SendMsg()로 준다.
 * 끝
 */
public class TCP_SC {
//	public static final String serverIp = "192.168.0.6";
//	public static final String serverIp = "180.189.95.198";
	public static final String serverIp = "52.78.112.67";
	public static final int port = 5003;
	public static String _del="";//데이터 구분자
	public static String _endDel="";//어레이리스트 종료 구분자
	public static String _endSendDel="";//데이터 끝 구분자
	public static boolean stopThread = true;
	public static boolean isPrint = true;
	//public static SocketChannel cli;

	public static final int QUEUESIZE = 500;
	public volatile static String Queue[] = new String[QUEUESIZE];
	public volatile static int top = 0;
	public volatile static int rear = 0;
	public final static String CANNOTGET = "999999";

	public static void SetData(){

		_del= "";
		_endDel= "";
		_endSendDel= "";
		_del+=(char)200;
		_endDel+=(char)201;
		_endSendDel+=(char)202;
	}
	public static void Print0(Object str){
		if(isPrint)
			System.out.println(str);
	}

	/** 큐가 비었을 경우 받을때까지 무한루프 돈다.
	 * 이부분 타임아웃 걸어야댐
	 * @return
	 */

}
