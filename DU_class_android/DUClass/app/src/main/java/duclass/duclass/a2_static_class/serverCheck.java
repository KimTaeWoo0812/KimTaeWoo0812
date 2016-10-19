package duclass.duclass.a2_static_class;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.TCP_SC;


public class serverCheck {
	private static Handler mHandler;
	private static Runnable mRunnable;
	private static ProgressDialog m_loadingDialog = null;

	public volatile static boolean canNotGet = false;
	static Context baseContext;

	public static boolean flag_forServer = false;

	static long start;
	static long end;
	static final int DELAYED_TIME = 8000;

	public static void showLoading(final Context context) {
		baseContext = context;
		if (m_loadingDialog == null) {

			Log.e("test", "con: " + context);
			m_loadingDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);

			m_loadingDialog.setIndeterminate(true);
			m_loadingDialog.setCancelable(false);
			m_loadingDialog.setMessage("데이터 수신 중...");

		}
		flag_forServer = true;
		m_loadingDialog.show();
		currentActivity(context);

	}

	/**
	 * 다이얼로그 없애기
	 */
	public static void hideLoading() {

		if (m_loadingDialog != null) {
			m_loadingDialog.dismiss();
			m_loadingDialog = null;
		}
	}

	/**
	 * 다이얼로그 실행
	 * @param mContext
	 */
	public static void currentActivity(final Context mContext) {


		start = System.currentTimeMillis();
		end = System.currentTimeMillis();

		mRunnable = new Runnable() {
			@Override
			public void run() {
				end = System.currentTimeMillis();
				if(end > start + DELAYED_TIME) {
					if (m_loadingDialog != null) {
						if (flag_forServer) {
							Toast toast = Toast.makeText(mContext,
									"서버와 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT);
							toast.show();

							TCP_SC.top = TCP_SC.rear;
							canNotGet = true;
							m_loadingDialog.dismiss();
							m_loadingDialog = null;
						}
					}
				}
			}
		};

		mHandler = new Handler();
		mHandler.postDelayed(mRunnable, DELAYED_TIME);
	}
}