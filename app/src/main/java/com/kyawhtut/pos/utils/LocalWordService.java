package com.kyawhtut.pos.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;

public class LocalWordService extends Service {
    private final IBinder mBinder = new MyBinder();
    private List<String> resultList = new ArrayList<>();
    private List<String> runningList = new ArrayList<>();
    private HashMap<String, Boolean> alreadyRunList = new HashMap<>();
    private int counter = 1;
    private Timer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addResultValues();
        timer();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        addResultValues();
        return mBinder;
    }

    public class MyBinder extends Binder {
        public LocalWordService getService() {
            return LocalWordService.this;
        }
    }

    public List<String> getWordList() {
        return resultList;
    }

    public List<String> getRunningList() {
        return runningList;
    }

    private void timer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        for (UsageStats usageStats : UStats.getUsageStatsList(LocalWordService.this)) {
//                            boolean isAlreadyRun = (alreadyRunList.get(usageStats.getPackageName()) == null);
                            if (usageStats.getPackageName().equals("com.mobile.legends")) {
                                Timber.d("Mobile legend -> %s %s", usageStats.getFirstTimeStamp(), usageStats.getLastTimeStamp());
//                                if (!isAlreadyRun) {
//                                    alreadyRunList.put(usageStats.getPackageName(), true);
//                                }
                            }
                        }
                        timer();
                    }
                },
                300
        );
    }

    private void processRunningList() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();

        // Display the number of running processes
        runningList.clear();
        Timber.d("Process running list %s %s", runningList, runningProcesses.size());

        // Loop through the running processes
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            // Get the process name
            runningList.add(processInfo.processName);
        }
        Timber.d("Process running list %s %s", runningList, runningProcesses.size());
    }

    private void addResultValues() {
        Random random = new Random();
        List<String> input = Arrays.asList("Linux", "Android", "iPhone", "Windows7");
        resultList.add(input.get(random.nextInt(3)) + " " + counter++);
        if (counter == Integer.MAX_VALUE) {
            counter = 0;
        }
    }
}