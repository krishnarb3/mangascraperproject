package com.dev.pro.noob.rb.mangaproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by RB on 19-07-2015.
 */
public class AutoStartService extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        context.startService(new Intent(context, Checkforlatestmanga.class));
    }
}
