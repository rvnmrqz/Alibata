package com.example.cedric.alibata;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cedric.alibata.Chapters.Announcement;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ServiceNotifications extends Service {


    int lastNotifID;
    static String domain = "http://alibata-itp.esy.es/";

    static Handler handler;
    static Timer timer;
    static TimerTask timerTask;
    int tick = 0;
    int seconds;
    int maxCount = 5;
    boolean continueCount = true;

    static NotificationManager nm;
    static NotificationCompat.Builder b;

    static RequestQueue requestQueue;
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            //get the last notif id saved
            initializeSharePref();
            lastNotifID = getSharedPrefMaxNotifID();
            System.out.println("onStart: LASTNOTIFID:" + lastNotifID);
            startTimer();
        } catch (Exception e) {
            System.out.println("\nException Encountered while starting command: " + e.getMessage());

        } catch (Throwable t) {
            System.out.println("\nThrowable Encountered while starting command: " + t.getMessage());
        }

        return START_STICKY;
    }

    //SHAREDPREFERENCE
    private void initializeSharePref() {
        try {
            sharedPreferences = getSharedPreferences(MySharedPref.SHAREDPREFNAME, MODE_PRIVATE);
        } catch (Exception e) {
            Log.wtf("ServiceNotification_USER", "initializeSharePref Exception: " + e.getMessage());
        } catch (Throwable t) {
            Log.wtf("USER_NotificationService", "initializeSharePref Throwable: " + t.getMessage());
        }

    }

    private int getSharedPrefMaxNotifID() {
        try {
            int maxid = sharedPreferences.getInt(MySharedPref.LASTNOTIFID, 0);
            Log.wtf("get shared Pref MAXID", "Value : " + maxid);
            return maxid;
        } catch (Exception e) {
            Log.wtf("USER_NotificationService", "getSharedPrefMaxNotifID Exception: " + e.getMessage());
        } catch (Throwable t) {
            Log.wtf("USER_NotificationService", "getSharedPrefMaxNotifID Throwable: " + t.getMessage());
        }
        return 0;
    }

    private void setSharedPrefMaxNotifID(int value) {
        try {
            editor = sharedPreferences.edit();
            lastNotifID = value;
            editor.putInt(MySharedPref.LASTNOTIFID, value);
            editor.commit();
            Log.wtf("setSharedprefMaxid", "New Max id is saved: " + value);
        } catch (Exception e) {
            Log.wtf("ServiceNotification_USER", "setSharedPrefMaxNotifID Exception: " + e.getMessage());
        } catch (Throwable t) {
            Log.wtf("ServiceNotification_USER", "setSharedPrefMaxNotifID Throwable: " + t.getMessage());
        }
    }

    private int getUnopenedNotif() {
        try {
            return sharedPreferences.getInt(MySharedPref.NOTIFCOUNT, 0);
        } catch (Exception e) {
            Log.wtf("USER_NotificationService", "getSharedPrefMaxNotifID Exception: " + e.getMessage());
        } catch (Throwable t) {
            Log.wtf("USER_NotificationService", "getSharedPrefMaxNotifID Throwable: " + t.getMessage());
        }
        return 0;
    }

    private void setUnopenedNotif(int count){
        try {
            editor = sharedPreferences.edit();
            editor.putInt(MySharedPref.NOTIFCOUNT, count);
            editor.commit();
        } catch (Exception e) {
            Log.wtf("ServiceNotification_USER", "setSharedPrefMaxNotifID Exception: " + e.getMessage());
        } catch (Throwable t) {
            Log.wtf("ServiceNotification_USER", "setSharedPrefMaxNotifID Throwable: " + t.getMessage());
        }
    }


    // TIMER************************************
    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            handler.removeCallbacksAndMessages(null);
            timer.cancel();
            timer.purge();
            timerTask.cancel();
            timer = null;
            timerTask = null;
            handler = null;
            Log.wtf("service_doWork", "request service is stopped");
        } catch (Exception e) {
            Log.wtf("USER_NotificationService ", "onDestroy Exception " + e.getMessage());
        } catch (Throwable t) {
            Log.wtf("USER_NotificationService ", "onDestroy Throwable " + t.getMessage());
        }

    }

    public void startTimer() {
        Log.wtf("service_startTimer", "Timer started");
        try {
            initializeTimer();
            timer.scheduleAtFixedRate(timerTask, seconds, seconds);
        } catch (Exception e) {
            Log.wtf("USER_NotificationService ", "startTimer ERROR " + e.getMessage());
        } catch (Throwable t) {
            Log.wtf("USER_NotificationService ", "startTimer Throwable " + t.getMessage());
        }

    }

    private void restartCounting() {
        try {
            tick = 0;
            continueCount = true;
            Log.wtf("service_restartCounting", "Timer restarted");
        } catch (Exception e) {
            Log.wtf("USER_NotificationService ", "restartCounting ERROR " + e.getMessage());
        } catch (Throwable t) {
            Log.wtf("USER_NotificationService ", "restartCounting Throwable " + t.getMessage());
        }
    }

    private void initializeTimer() {
        try {
            seconds = 1000;
            //*********************
            //just to clear the objects
            timer = null;
            timerTask = null;
            handler = null;
            //********************
            handler = new Handler();
            timer = new Timer(false);
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (continueCount) {
                                tick++;
                                if (tick == maxCount) {
                                    if (isNetworkAvailable()) {
                                        doWork();
                                    } else {
                                        Log.wtf("tick==maxcount", "no network available, restarting");
                                        restartCounting();
                                    }
                                }
                            }
                            Log.wtf("service_timer", "Timer Tick: " + tick);
                        }
                    });
                }
            };
            Log.wtf("service_initializeTimer", "Timer initialized");
        } catch (Exception e) {
            Log.wtf("USER_NotificationService", "initializeTimer Exception: " + e.getMessage());
        } catch (Throwable t) {
            Log.wtf("USER_NotificationService ", "initializeTimer Throwable " + t.getMessage());
        }

    }

    private void stopCounting() {
        try {
            tick = 0;
            continueCount = false;
            Log.wtf("service_restartCounting", "Timer restarted");
            Log.wtf("service_stopCounting", "Timer stopped");
        } catch (Exception e) {
            Log.wtf("USER_NotificationService ", "stopCounting ERROR " + e.getMessage());
        } catch (Throwable t) {
            Log.wtf("USER_NotificationService ", "stopCounting Throwable " + t.getMessage());
        }


    }
    //*********************************************

    private void doWork() {
        try {

            String url = domain + "App/notifChecker.php";

            requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            response = response.trim();
                            System.out.println("Response: " + response);

                            //CHECK NEW NOTIFICATION
                            try {
                                String parts[] = response.split(",");

                                int newNotifID = Integer.parseInt(parts[0]);
                                int notifCount = Integer.parseInt(parts[1]);

                                if (newNotifID > 0) {
                                    System.out.println("\n\nNEW NOTIFICATION DETECTED\n\n");
                                    newAnnouncement(newNotifID, notifCount);
                                } else {
                                    System.out.println("NO NEW NOTIFICATION");
                                }
                            } catch (Exception e) {
                                System.out.println("Convertion error: " + e.getMessage());
                            }
                            restartCounting();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("onErrorResponse: " + error.getMessage());
                            restartCounting();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    System.out.println("LASTNOTIF ID: " + lastNotifID);
                    params.put("notifid", lastNotifID + "");
                    stopCounting();
                    return params;
                }
            };
            int socketTimeout = 50000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            Log.wtf("ServiceNotification_USER", "DoWork Exception: " + e.getMessage());
            restartCounting();
        } catch (Throwable t) {
            Log.wtf("USER_NotificationService ", "stopCounting Throwable " + t.getMessage());
        }

    }

    private void newAnnouncement(int newNotifId, int notifCount) {

        int unopened = getUnopenedNotif()+notifCount;
        setUnopenedNotif(unopened);
        setSharedPrefMaxNotifID(newNotifId);
        showNotification(unopened);

        //refresh listview
        if (Announcement.staticContext != null) {
            Announcement.loadNotifications();
        } else {
            //announcement activity is not opened
            if (MainActivity.staticContext != null) {
                //main activity is opened, update badge
                MainActivity.setUnopenedBadge(unopened+"");
            }
        }
    }

    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            Log.wtf("ServiceNotification_USER", "isNetworkAvailable Exception: " + e.getMessage());
        } catch (Throwable t) {
            Log.wtf("USER_NotificationService ", "isNetworkAvailable Throwable " + t.getMessage());
        }
        return false;
    }

    protected void showNotification(int notif_count) {
        try {
            System.out.println("\n\nShowNotification :" + notif_count);
            //Define sound URI

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            final Intent mainIntent = new Intent(this, Announcement.class);
            mainIntent.putExtra("notif", "notify");
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    (mainIntent), PendingIntent.FLAG_UPDATE_CURRENT);
            b = new NotificationCompat.Builder(this);
            b.setAutoCancel(true)
                    .setOngoing(false)
                    .setSound(soundUri)
                    .setSmallIcon(R.drawable.ann_ico)
                    .setWhen(System.currentTimeMillis())
                    .setTicker("New Announcement!")
                    .setContentTitle("Alibata")
                    .setContentText(notif_count + " New Announcement(s)")
                    .setContentIntent(pendingIntent);
            nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(100, b.build());
        } catch (Exception e) {
            System.out.println("showNotification Exception: " + e.getMessage());
        } catch (Throwable t) {
            System.out.println("showNotification Throwable: " + t.getMessage());
        }

    }

}
