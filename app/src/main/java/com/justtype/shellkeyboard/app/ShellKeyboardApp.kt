package com.justtype.shellkeyboard.app

import android.app.Application
import com.justtype.shellkeyboard.data.UserDictDb
import com.justtype.shellkeyboard.dict.DictDeployer

/**
 * Application class for Shell Keyboard.
 * 
 * Handles:
 * - Initial RIME deployment
 * - Database initialization
 */
class ShellKeyboardApp : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Deploy RIME assets on first launch
        val deployer = DictDeployer(this)
        if (deployer.needsDeployment()) {
            deployer.deployAll()
        }
        
        // Initialize database
        UserDictDb.getInstance(this)
    }
}
