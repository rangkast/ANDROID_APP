package com.lge.threadtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private final int Thread1 = 1;
    private final int Thread2 = 2;
    private final int Thread3 = 3;

    private static TextView textView1;
    private static TextView textView2;
    private static TextView textView3;
    private static Button btn_start;
    private static Button btn_stop;
    static String TAG = "ThreadTest";

    static int thread1_cnt = 0;
    static int thread2_cnt = 0;
    static int thread3_cnt = 0;


    private TestThread1 mTestThread1;
    private TestThread2 mTestThread2;
    private TestThread3 mTestThread3;

    private AyncTest1 ayncTest1;
    private AyncTest2 ayncTest2;
    private AyncTest3 ayncTest3;
    private AyncTest4 ayncTest4;
    private AyncTest5 ayncTest5;
    private AyncTest6 ayncTest6;
    private AyncTest7 ayncTest7;
    private ForeAsyncTask foreAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Oncreate");

        textView1 = findViewById(R.id.thread1_txt);
        textView2 = findViewById(R.id.thread2_txt);
        textView3 = findViewById(R.id.thread3_txt);
        btn_start = findViewById(R.id.thread_btn_start);
        btn_stop = findViewById(R.id.thread_btn_stop);



        Log.d(TAG, "cpu count " + Runtime.getRuntime().availableProcessors());

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Start btn click");

                mTestThread1 = new TestThread1();
                mTestThread2 = new TestThread2();
                mTestThread3 = new TestThread3();

                mTestThread1.setName("TTest1");
                mTestThread2.setName("TTest2");
                mTestThread3.setName("TTest3");

                if (mTestThread1 != null &
                        mTestThread2 != null &
                        mTestThread3 != null) {
                    if (!mTestThread1.isAlive())
                        mTestThread1.start();
                    if (!mTestThread2.isAlive())
                        mTestThread2.start();
                    if (!mTestThread3.isAlive())
                        mTestThread3.start();
                }



/*
                ayncTest2 = new AyncTest2();
                ayncTest2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  //              ayncTest2.execute();
                ayncTest1 = new AyncTest1();
                ayncTest1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                //ayncTest1.execute();
        //        ayncTest3 = new AyncTest3();
        //        ayncTest3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                foreAsyncTask = new ForeAsyncTask();
                foreAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


 */
   //             ayncTest3.execute();
                                /*
                ayncTest4 = new AyncTest4();
                ayncTest4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                ayncTest5 = new AyncTest5();
                ayncTest5.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                ayncTest6 = new AyncTest6();
                ayncTest6.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                ayncTest7 = new AyncTest7();
                ayncTest7.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                 */
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Stop btn click");
/*
                if (mTestThread1 != null &
                        mTestThread2 != null &
                        mTestThread3 != null) {

                    if (mTestThread1.isAlive())
                        mTestThread1.interrupt();
                    if (mTestThread2.isAlive())
                        mTestThread2.interrupt();
                    if (mTestThread3.isAlive())
                        mTestThread3.interrupt();
                }

*/


                ayncTest1.cancel(true);
                ayncTest2.cancel(true);
                ayncTest3.cancel(true);
                /*
                ayncTest4.cancel(true);
                ayncTest5.cancel(true);
                ayncTest6.cancel(true);
                ayncTest7.cancel(true);


 */
            }
        });

    }

    public class TestThread1 extends Thread {
        long time = 0;
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    time = System.currentTimeMillis();
                }
            });
            while (true) {
                try {
                    thread1_cnt++;

                    mHandler.sendEmptyMessageDelayed(Thread1, 0);
                    if (thread1_cnt >= 10000) {
                        Log.d(TAG,"time e1: " + (System.currentTimeMillis() - time));
                        break;
                    }
                    Thread.sleep(1);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                }
            }
        }
    }

    public class TestThread2 extends Thread {
        long time = 0;
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    time = System.currentTimeMillis();
                }
            });
            while (true) {
                try {
                    thread2_cnt++;
                    mHandler.sendEmptyMessageDelayed(Thread2, 0);
                    if (thread2_cnt >= 10000) {
                        Log.d(TAG,"time e2: " + (System.currentTimeMillis() - time));
                        break;
                    }
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                }
            }
        }
    }

    public class TestThread3 extends Thread {
        long time = 0;
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    time = System.currentTimeMillis();
                }
            });
            while (true) {
                try {
                    thread3_cnt++;
                    mHandler.sendEmptyMessageDelayed(Thread3, 0);
                    if (thread3_cnt >= 10000) {
                        Log.d(TAG,"time e3: " + (System.currentTimeMillis() - time));
                        break;
                    }
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                }
            }
        }
    }




    public class AyncTest1 extends AsyncTask {
        private Context mContext;
        long time = 0;
        public AyncTest1() {

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            while (true) {
                try {
                    thread1_cnt++;
                    mHandler.sendEmptyMessageDelayed(Thread1, 0);
                    if (thread1_cnt >= 10000) {
                        Log.d(TAG,"time e1: " + (System.currentTimeMillis() - time));
                        break;
                    }
             //       Log.d(TAG, "async1 start in background");
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            time = System.currentTimeMillis();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            if (thread1_cnt%100 == 0) {
                mHandler.sendEmptyMessageDelayed(Thread1, 0);
            }
            super.onProgressUpdate(values);
        }
    }

    public class AyncTest2 extends AsyncTask {
        private Context mContext;
        long time = 0;
        public AyncTest2() {}

        @Override
        protected Object doInBackground(Object[] objects) {

            while (true) {
                try {
                    thread2_cnt++;
                    mHandler.sendEmptyMessageDelayed(Thread2, 0);
   //                 onProgressUpdate(thread2_cnt);
                    if (thread2_cnt >= 10000) {
                        Log.d(TAG,"time e2: " + (System.currentTimeMillis() - time));
                        break;
                    }
        //            Log.d(TAG, "async2 start in background");
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                }
            }

            return null;
        }
        protected void onProgressUpdate(Integer... progress) {
            if (thread2_cnt%100 == 0) {
                mHandler.sendEmptyMessageDelayed(Thread2, 0);
            }
        }
        @Override
        protected void onPreExecute() {
            time = System.currentTimeMillis();
            super.onPreExecute();
        }
        /*
        @Override
        protected void onProgressUpdate(Object[] values) {
            if (thread2_cnt%100 == 0) {
                mHandler.sendEmptyMessageDelayed(Thread2, 0);
            }
            super.onProgressUpdate(values);
        }

         */
    }

    public class AyncTest3 extends AsyncTask {
        private Context mContext;
        long time = 0;
        public AyncTest3() {}

        @Override
        protected Object doInBackground(Object[] objects) {

            while (true) {
                try {
                    thread3_cnt++;
                    mHandler.sendEmptyMessageDelayed(Thread3, 0);
                    if (thread3_cnt >= 10000) {
                        Log.d(TAG,"time e3: " + (System.currentTimeMillis() - time));
                        break;
                    }
      //              Log.d(TAG, "async3 start in background");
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            time = System.currentTimeMillis();
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Object[] values) {
            if (thread3_cnt%100 == 0) {
                mHandler.sendEmptyMessageDelayed(Thread3, 0);
            }
            super.onProgressUpdate(values);
        }
    }

    public class ForeAsyncTask extends PriorityAsyncTask<String, Integer, Boolean> {
        long time = 0;
        @Override
        protected void onPreExecute() {
            time = System.currentTimeMillis();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            while (true) {
                try {
                    thread3_cnt++;
                    mHandler.sendEmptyMessageDelayed(Thread3, 0);
                    if (thread3_cnt >= 10000) {
                        Log.d(TAG,"time e3: " + (System.currentTimeMillis() - time));
                        break;
                    }
                    //              Log.d(TAG, "async3 start in background");
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                }
            }
            return null;
        }
    }



    public class AyncTest4 extends AsyncTask {
        private Context mContext;

        public AyncTest4() {}

        @Override
        protected Object doInBackground(Object[] objects) {

            while (true) {
                try {
                    thread3_cnt++;
                    mHandler.sendEmptyMessageDelayed(Thread3, 0);
                    Log.d(TAG, "async4 start in background");
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
    public class AyncTest5 extends AsyncTask {
        private Context mContext;

        public AyncTest5() {}

        @Override
        protected Object doInBackground(Object[] objects) {

            while (true) {
                try {
                    thread3_cnt++;
                    mHandler.sendEmptyMessageDelayed(Thread3, 0);
                    Log.d(TAG, "async5 start in background");
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
    public class AyncTest6 extends AsyncTask {
        private Context mContext;

        public AyncTest6() {}

        @Override
        protected Object doInBackground(Object[] objects) {

            while (true) {
                try {
                    thread3_cnt++;
                    mHandler.sendEmptyMessageDelayed(Thread3, 0);
                    Log.d(TAG, "async6 start in background");
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
    public class AyncTest7 extends AsyncTask {
        private Context mContext;

        public AyncTest7() {}

        @Override
        protected Object doInBackground(Object[] objects) {

            while (true) {
                try {
                    thread3_cnt++;
                    mHandler.sendEmptyMessageDelayed(Thread3, 0);
                    Log.d(TAG, "async7 start in background");
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }




    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            //      Log.d(TAG, "mHandler " + msg.what);
            switch (msg.what) {
                case Thread1:
     //               Log.d(TAG, "Handler Thread:1 " + thread1_cnt);
                    textView1.setText(Integer.toString(thread1_cnt));
                    break;
                case Thread2:
      //              Log.d(TAG, "Handler Thread:2 " + thread2_cnt);
                    textView2.setText(Integer.toString(thread2_cnt));
                    break;
                case Thread3:
     //               Log.d(TAG, "Handler Thread:3 " + thread3_cnt);
                    textView3.setText(Integer.toString(thread3_cnt));
                    break;

            }
            super.handleMessage(msg);
        }
    };
}