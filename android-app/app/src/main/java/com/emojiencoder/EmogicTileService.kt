package com.emojiencoder

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
            startFloatingBubble()
            qsTile.state = Tile.STATE_ACTIVE
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
