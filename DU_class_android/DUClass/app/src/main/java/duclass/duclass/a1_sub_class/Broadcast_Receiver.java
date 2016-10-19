package duclass.duclass.a1_sub_class;//package main.a1_sub_class;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import java.io.IOException;
//
//import main.a2_static_class.SC;
//import main.a2_static_class.CUserInfo;
//
//public class Broadcast_Receiver extends BroadcastReceiver {
//
//	String action;
//	private AndroidSocket socket;
//	//private static Dialog m_loadingDialog = null; // ȭ�鿡 ������ dialog ��ü
//	@Override
//    public void onReceive(Context context, Intent intent) {
//
//        action=intent.getAction();
//
//        Log.d("TestReceiver","action : " + action);
//
//
//
//		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
//		{
//			//Toast.makeText(context, "��Ʈ��ũ ���� ����!!\n ���� ������ ��",Toast.LENGTH_SHORT).show();
//
//			if (CUserInfo.id.compareTo("") != 0) {
//				// showLoading(context);
////				try {
////					AndroidSocket.CloseSocket();
////				} catch (Exception e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////				AndroidSocket._shared = null;
//
//				socket = AndroidSocket.shared();
//
//				try {
//					Thread.sleep(90);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				new SendMsgTask().execute();
//
//				// hideLoading();
//				//Toast.makeText(context, "������ ����!", Toast.LENGTH_SHORT).show();
//				/*
//				 *
//				 * ��Ʈ��ũ ��ȭ�� ���涧���� �������� ���´�.
//				 */
//			}
//       }
//
//    }
//	public class SendMsgTask extends AsyncTask<String, Void, String> {
//		String msg = "";
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			msg = "CHANGE_IP"+ SC._del+ CUserInfo.id+SC._del;
//
//			try {
//				socket.SendMessage(msg);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//
//		}
//
//	}
//}
