package duclass.duclass.a1_sub_class;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

import duclass.duclass.a2_static_class.serverCheck;

public class AndroidSocket extends Thread {
	public SocketChannel client;
	boolean  stopThread = false;
	public static String id = "";
	volatile boolean isConnectedSocket = false; // 연결 확인 전 확인용
	volatile boolean isSendFinshed = true; //연결 해지 전 확인용
	private String strTemp;
	public static AndroidSocket socket;

	public static synchronized AndroidSocket shared() { //싱글톤 패턴

		if(socket == null) {
			socket = new AndroidSocket();
			socket.start();
		}

		return socket;
	}
	public void CloseSocket(){
		try {
			stopThread = true;

			while (!isSendFinshed){
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
//			while (!isConnectedSocket)
//				;
			try {
				socket = null;
				client.socket().close();
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (NotYetConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String GetMessage(){
		while (!HasMessage()){
			if(serverCheck.canNotGet) { //시간 내에 메시지를 받지 못하면
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Log.e("##겟메시지",""+serverCheck.canNotGet);
				serverCheck.canNotGet = false;

				return TCP_SC.CANNOTGET;
			}
		}

		Log.e("DeQue  ",TCP_SC.Queue[TCP_SC.rear % TCP_SC.QUEUESIZE]);
		String msg =  TCP_SC.Queue[TCP_SC.rear % TCP_SC.QUEUESIZE];
		if(msg.equals(null)){
			Log.e("DeQue null_ "," ");
			msg =  TCP_SC.Queue[TCP_SC.rear % TCP_SC.QUEUESIZE];
		}

		serverCheck.flag_forServer = false;
		TCP_SC.rear++;
		return msg;
	}
	public static synchronized boolean HasMessage() {
		if (TCP_SC.top == TCP_SC.rear)
			return false;
		else
			return true;
	}

	/**
	 * 메시지 큐에 넣기
	 * @param msg
	 */
	public static void InQue(String msg){
		TCP_SC.Queue[TCP_SC.top % TCP_SC.QUEUESIZE] = msg;
		TCP_SC.top++;
	}

	/**
	 * 받은 메시지 빼오기
	 * @return
	 */
	public static String DeQue(){
		while (!HasMessage()){
			if(serverCheck.canNotGet) {
				serverCheck.canNotGet = false;
				return "100";
			}
		}
		Log.e("DeQue  ",TCP_SC.Queue[TCP_SC.rear % TCP_SC.QUEUESIZE]);
		String msg =  TCP_SC.Queue[TCP_SC.rear % TCP_SC.QUEUESIZE];
		if(msg.equals(null)){
			Log.e("DeQue null_ "," ");
			msg =  TCP_SC.Queue[TCP_SC.rear % TCP_SC.QUEUESIZE];
		}

		serverCheck.flag_forServer = false;
		TCP_SC.rear++;
		return msg;
	}


	/**
	 * 소켓 연결 로직
	 */
	@Override
	public void run() {
		Selector selector = null;
		Charset cs = Charset.forName("UTF-8");

		try {
			System.out.println("Client :: started");
			client = SocketChannel.open();
			client.configureBlocking(false);


			client.connect(new InetSocketAddress(TCP_SC.serverIp, TCP_SC.port));
			selector = Selector.open();
			client.register(selector, SelectionKey.OP_READ);

			while (!Thread.interrupted() && !client.finishConnect()) {
				Thread.sleep(10);
			}

			System.out.println("Client :: connected");
			System.out.println("Client :: Thread.interrupted()  "+Thread.interrupted());
			isConnectedSocket = true;
			strTemp = "";
			while (!Thread.interrupted() && !stopThread) {

				selector.select(3000);

				Iterator<SelectionKey> iter = selector.selectedKeys()
						.iterator();
				while (!Thread.interrupted() && !stopThread && iter.hasNext()) {
					SelectionKey key = iter.next();
					ByteBuffer buffer = ByteBuffer.allocate(8189); //모바일 임으로 안정성을위해 allocate 사용


					if (key.isReadable()) {
						int len = client.read(buffer);
						if (len < 0) {
							System.out.println("Client :: server closed");
							stopThread = true;
							break;
						} else if (len == 0) {
							continue;
						}
						buffer.flip();
//						int size = buffer.remaining();
						CharBuffer cb = cs.decode(buffer);
						System.out.printf("From TCP Server : ");

						while (cb.hasRemaining()) {
							strTemp += cb.get();
						}
						System.out.println(strTemp);

						int nIsDone= strTemp.indexOf(TCP_SC._endSendDel);

						//구분 문자를 받을때 까지 받기. 이 구문으로인해 안정적인 수신이 보장된다.
						if((nIsDone+1) == strTemp.length()){
							String strs[] = strTemp.split(TCP_SC._endSendDel);
							System.out.println(strs[0]);
							InQue(strs[0]);
							Log.e("서버 Get: ", strs[0] + "    " + TCP_SC.top + " " + TCP_SC.rear + "   " + strs.length);
							strTemp = "";
						}
						buffer.compact();

					}
				}
			}
			client.socket().close();
			client.close();
			selector.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				try {
					client.socket().close();
					client.close();
					selector.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Client :: done");
		}

	}

	public synchronized void SendMessage(String text) throws IOException {
		while (!isConnectedSocket) {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Log.e("TCP_보내는 메시지: ", text);
		isSendFinshed = false;
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		Charset cs = Charset.forName("UTF-8");
		buffer = cs.encode(text + "\n");
		try {
			client.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		buffer.flip();
		buffer.compact();
		isSendFinshed = true;
	}
}