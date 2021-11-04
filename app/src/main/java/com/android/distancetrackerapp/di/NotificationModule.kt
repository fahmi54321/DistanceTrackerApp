package com.android.distancetrackerapp.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.android.distancetrackerapp.ui.MainActivity
import com.android.distancetrackerapp.R
import com.android.distancetrackerapp.utils.Constants.ACTION_NAVIGATE_TO_MAPS_FRAGMENT
import com.android.distancetrackerapp.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.android.distancetrackerapp.utils.Constants.PENDING_INTENT_REQUEST_CODE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

//todo 3 create_notification
@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {


    //todo 4 create_notification
    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
            @ApplicationContext context:Context,
            pendingIntent: PendingIntent
    ):NotificationCompat.Builder{
        return NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_run)
                .setContentIntent(pendingIntent)
    }

    //todo 5 create_notification
    @ServiceScoped
    @Provides
    fun providePendingIntent(
            @ApplicationContext context: Context,
    ):PendingIntent{
        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_REQUEST_CODE,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    //todo 6 create_notification (next TrackerService)
    @ServiceScoped
    @Provides
    fun provideNotificationManager(
            @ApplicationContext context: Context
    ):NotificationManager{
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}