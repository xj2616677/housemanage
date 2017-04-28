package com.example.admin.housemanage;

import java.io.File;

import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import util.ActivityManage;

public class MyMediaRecorderDemo extends BaseActivity implements android.media.MediaRecorder.OnErrorListener  {
	private ImageButton record = null;
	private ImageButton stop = null;
	private ImageButton browser = null;
	private TextView info = null;
	private MediaRecorder mediaRecorder = null;
	private boolean sdcardExists = false; // SD卡存在的标记
	private File recordVideoSaveFileDir = null;
	private File recordVideoSaveFile = null;
	private String recordVideoSaveFileName = null;
//	private String recDir = Contant.ORGNAME + "(所属任务：" + Contant.TASKNAME + ")";
	private boolean isRecord = false;
	private SurfaceView surface = null;

	private Chronometer chrono;
	private Camera camera;
	private String filePath = "";

	private int i = 0;

	// private int TIME = 1000;
	// private Timer timer = new Timer();;
	// private MyTimerTask task;

	// Handler handler = new Handler() {
	// public void handleMessage(Message msg) {
	// if (msg.what == 1) {
	// // tvShow.setText(Integer.toString(i++));
	// if(MyMediaRecorderDemo.this.isRecord) {
	// MyMediaRecorderDemo.this.mediaRecorder.stop() ;
	// task.cancel();
	//
	// chrono.stop();
	// chrono.setVisibility(View.GONE);
	// chrono.setBase(SystemClock.elapsedRealtime());
	// MyMediaRecorderDemo.this.mediaRecorder.release() ;
	// MyMediaRecorderDemo.this.stop.setEnabled(false) ;
	// MyMediaRecorderDemo.this.record.setEnabled(true) ;
	// MyMediaRecorderDemo.this.info.setText("录象结束，文件路径为："
	// + MyMediaRecorderDemo.this.recordVideoSaveFile);
	// MyMediaRecorderDemo.this.isRecord = false ;
	// }
	// }
	// super.handleMessage(msg);
	// };
	// };

	@Override
	protected void initView() {

		ActivityManage.getInstance().addActivity(this);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE); // 不显示标题
		super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 高亮的风格显示
		super.setContentView(R.layout.media);
		this.record = (ImageButton) super.findViewById(R.id.record);
		this.stop = (ImageButton) super.findViewById(R.id.stop);
		this.browser = (ImageButton) super.findViewById(R.id.browser);
		this.info = (TextView) super.findViewById(R.id.info);
		this.surface = (SurfaceView) super.findViewById(R.id.surface);

		this.chrono = (Chronometer) findViewById(R.id.chrono);
		chrono.setVisibility(View.GONE);
		this.surface.getHolder().setType(
				SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		this.surface.getHolder().setFixedSize(480, 800);
		Intent intent = getIntent();
		filePath = intent.getStringExtra("filePath");

		if ((this.sdcardExists = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))) {
			this.recordVideoSaveFileDir = new File(filePath); // 保存文件夹
			if (!this.recordVideoSaveFileDir.exists()) {
				this.recordVideoSaveFileDir.mkdirs(); // 创建文件夹
			}
		}
		this.record.setOnClickListener(new RecordOnClickListenerImpl());
		this.stop.setOnClickListener(new StopOnClickListenerImpl());
		this.browser.setOnClickListener(new BrowserOnClickListenerImpl());
		this.stop.setEnabled(false); // 停止录象的按钮暂时不可用
	}

	@Override
	protected void initData() {

	}

	private class RecordOnClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			i = 0;
			if (MyMediaRecorderDemo.this.sdcardExists) { // sd卡存在
				MyMediaRecorderDemo.this.recordVideoSaveFileName = MyMediaRecorderDemo.this.filePath
						.toString()
						+ File.separator
						+ "shh"
						+ System.currentTimeMillis() + ".mp4";// 文件的路径名称
				MyMediaRecorderDemo.this.recordVideoSaveFile = new File(
						MyMediaRecorderDemo.this.recordVideoSaveFileName);// 文件路径
				MyMediaRecorderDemo.this.mediaRecorder = new MediaRecorder();
				MyMediaRecorderDemo.this.mediaRecorder.reset(); // 重置
				camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);

				if (camera != null) {
					camera.setDisplayOrientation(90);// 摄像图旋转90度
					camera.unlock();
					MyMediaRecorderDemo.this.mediaRecorder.setCamera(camera);
				}
				
				MyMediaRecorderDemo.this.mediaRecorder
						.setAudioSource(MediaRecorder.AudioSource.MIC);

				MyMediaRecorderDemo.this.mediaRecorder
						.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				MyMediaRecorderDemo.this.mediaRecorder.setOrientationHint(90);
				MyMediaRecorderDemo.this.mediaRecorder
						.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
				MyMediaRecorderDemo.this.mediaRecorder
						.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
				MyMediaRecorderDemo.this.mediaRecorder
						.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				MyMediaRecorderDemo.this.mediaRecorder
						.setOutputFile(MyMediaRecorderDemo.this.recordVideoSaveFileName);
				MyMediaRecorderDemo.this.mediaRecorder.setVideoSize(320, 240);
				MyMediaRecorderDemo.this.mediaRecorder.setVideoFrameRate(10);
				MyMediaRecorderDemo.this.mediaRecorder
						.setPreviewDisplay(MyMediaRecorderDemo.this.surface
								.getHolder().getSurface());
				try {
					MyMediaRecorderDemo.this.mediaRecorder.prepare();
				} catch (Exception e) {
				}
				MyMediaRecorderDemo.this.mediaRecorder.start();
				chrono.start();
				chrono.setVisibility(View.VISIBLE);
				MyMediaRecorderDemo.this.info.setText("正在录象中...");
				MyMediaRecorderDemo.this.stop.setEnabled(true);
				MyMediaRecorderDemo.this.record.setEnabled(false);
				MyMediaRecorderDemo.this.isRecord = true;

				// task = new MyTimerTask();
				// timer.schedule(task, 1000, 1000);
			}
		}
	}

	private class StopOnClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (MyMediaRecorderDemo.this.isRecord) {
				if (mediaRecorder != null) {
		            //设置后不会崩
					mediaRecorder.setOnErrorListener(null);
					mediaRecorder.setPreviewDisplay(null);
		            try {
		            	MyMediaRecorderDemo.this.mediaRecorder.stop();
		            } catch (IllegalStateException e) {
		            } catch (RuntimeException e) {
		            } catch (Exception e) {
		            }
		        }
				// task.cancel();
				chrono.stop();
				chrono.setVisibility(View.GONE);
				chrono.setBase(SystemClock.elapsedRealtime());
				MyMediaRecorderDemo.this.mediaRecorder.release();
				MyMediaRecorderDemo.this.stop.setEnabled(false);
				MyMediaRecorderDemo.this.record.setEnabled(true);
				if (camera != null) {
					camera.release();
					camera = null;
					}
				MyMediaRecorderDemo.this.info.setText("录象结束，文件路径为："
						+ MyMediaRecorderDemo.this.recordVideoSaveFile);
				MyMediaRecorderDemo.this.isRecord = false;
			}
		}
	}

	private class BrowserOnClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (MyMediaRecorderDemo.this.isRecord) {
				// task.cancel();
				chrono.stop();
				chrono.setVisibility(View.GONE);
				chrono.setBase(SystemClock.elapsedRealtime());
				if (mediaRecorder != null) {
		            //设置后不会崩
					mediaRecorder.setOnErrorListener(null);
					mediaRecorder.setPreviewDisplay(null);
		            try {
		            	MyMediaRecorderDemo.this.mediaRecorder.stop();
		            } catch (IllegalStateException e) {
		            } catch (RuntimeException e) {
		            } catch (Exception e) {
		            }
		        }
				MyMediaRecorderDemo.this.mediaRecorder.release();
				if (camera != null) {
					camera.release();
					camera = null;
					}

			}
			MyMediaRecorderDemo.this.finish();
		}
	}

	// class MyTimerTask extends TimerTask {
	//
	// @Override
	// public void run() {
	// // 需要做的事:发送消息
	// i ++ ;
	// if(i > 30){
	// Message message = new Message();
	// message.what = 1;
	// handler.sendMessage(message);
	//
	// }
	//
	// }
	// };

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (MyMediaRecorderDemo.this.isRecord) {
				// task.cancel();
				chrono.stop();
				chrono.setVisibility(View.GONE);
				chrono.setBase(SystemClock.elapsedRealtime());
				if (mediaRecorder != null) {
		            //设置后不会崩
					mediaRecorder.setOnErrorListener(null);
					mediaRecorder.setPreviewDisplay(null);
		            try {
		            	MyMediaRecorderDemo.this.mediaRecorder.stop();
		            } catch (IllegalStateException e) {
		            } catch (RuntimeException e) {
		            } catch (Exception e) {
		            }
		        }
				MyMediaRecorderDemo.this.mediaRecorder.release();
				if (camera != null) {
					camera.release();
					camera = null;
					}

			}

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		// TODO Auto-generated method stub
		 try {
	            if (mr != null)
	                mr.reset();
	        } catch (IllegalStateException e) {
	        } catch (Exception e) {
	        }
	       
	    }
	

	

	
	
	
	
	

}