package com.emojiencoder

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class EmogicTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        super.onClick()
        
        val isActive = qsTile.state == Tile.STATE_ACTIVE
        
        if (isActive) {
            stopFloatingBubble()
            qsTile.state = Tile.STATE_INACTIVE
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (android.provider.Settings.canDrawOverlays(this)) {
                    startFloatingBubble()
                    qsTile.state = Tile.STATE_ACTIVE
                } else {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    val pendingIntent = PendingIntent.getActivity(
                        this, 0, intent, 
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    startActivityAndCollapse(pendingIntent)
                    return
                }
            } else {
                startFloatingBubble()
                qsTile.state = Tile.STATE_ACTIVE
            }
        }
        
        updateTile()
    }

    private fun updateTile() {
        qsTile?.apply {
            val isActive = state == Tile.STATE_ACTIVE
            label = if (isActive) "EMOGIC ON" else "EMOGIC OFF"
            contentDescription = if (isActive) "Tap to disable EMOGIC bubble" else "Tap to enable EMOGIC bubble"
            state = if (isActive) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            updateTile()
        }
    }

    private fun startFloatingBubble() {
        val intent = Intent(this, FloatingBubbleService::class.java)
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopFloatingBubble() {
        val intent = Intent(this, FloatingBubbleService::class.java)
        stopService(intent)
    }
}
