package com.example.android.weeklytips;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.weeklytips.services.MyJobService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLog = (TextView) findViewById(R.id.log);
        mLog.setMovementMethod(new ScrollingMovementMethod());
    }

    /**
     * Run some code. If the TextView only displays the intro message, clear it first.
     */
    public void runCode(View view) {
        JobScheduler jobScheduler =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = new JobInfo.Builder(1001,
                new ComponentName(this, MyJobService.class))
                .setMinimumLatency(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
        if (jobScheduler != null) {
            jobScheduler.schedule(jobInfo);
        }
        Log.i("JobScheduler", "runCode: job is scheduled");
    }

    /**
     * Clear the output TextView
     * @param view The button the user clicked
     */
    public void clearLog(View view) {
        mLog.setText("");
    }

    /**
     * Logs a message - called initially by runCode()
     *
     * @param message The message to display
     */
    private void log(String message) {
        Log.i(TAG, message);
        mLog.append(message + "\n");
        adjustScroll();
    }

    /**
     * Adjusts scroll vertically to ensure last line is displayed
     */
    private void adjustScroll() {
        final int scrollAmount = mLog.getLayout()
                .getLineTop(mLog.getLineCount()) - mLog.getHeight();
        if (scrollAmount > 0)
            mLog.scrollTo(0, scrollAmount);
        else
            mLog.scrollTo(0, 0);
    }

}
