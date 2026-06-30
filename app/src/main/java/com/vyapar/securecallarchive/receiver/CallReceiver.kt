package com.vyapar.securecallarchive.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import timber.log.Timber

class CallReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        when (intent.action) {
            TelephonyManager.ACTION_PHONE_STATE_CHANGED -> {
                handlePhoneStateChange(context, intent)
            }
            Intent.ACTION_NEW_OUTGOING_CALL -> {
                handleOutgoingCall(context, intent)
            }
        }
    }

    private fun handlePhoneStateChange(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        when (state) {
            TelephonyManager.EXTRA_STATE_IDLE -> {
                Timber.d("Call ended")
                // TODO: Stop recording
            }
            TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                Timber.d("Call answered")
                // TODO: Start recording
            }
            TelephonyManager.EXTRA_STATE_RINGING -> {
                Timber.d("Incoming call from: $incomingNumber")
                // TODO: Prepare to record incoming call
            }
        }
    }

    private fun handleOutgoingCall(context: Context, intent: Intent) {
        val phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
        Timber.d("Outgoing call to: $phoneNumber")
        // TODO: Prepare to record outgoing call
    }
}
