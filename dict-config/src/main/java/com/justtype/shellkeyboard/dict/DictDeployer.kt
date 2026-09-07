package com.justtype.shellkeyboard.dict

import android.content.Context
import java.io.File

class DictDeployer(private val context: Context) {

    companion object {
        const val RIME_CONFIG_DIR = "rime"
        const val SCHEMAS_DIR = "schemas"
        const val DICTIONARIES_DIR = "dictionaries"
    }

    fun deployAll() {
        val rimeDir = getRimeDir()
        deploySchemas(rimeDir)
        deployDictionaries(rimeDir)
        deployDefaultConfig(rimeDir)
    }

    private fun getRimeDir(): File {
        val dir = File(context.filesDir, RIME_CONFIG_DIR)
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    private fun deploySchemas(rimeDir: File) {
        val schemasDir = File(rimeDir, SCHEMAS_DIR)
        if (!schemasDir.exists()) schemasDir.mkdirs()
        try {
            val assets = context.assets.list("rime/schemas") ?: return
            for (asset in assets)
                copyAsset("rime/schemas/", File(schemasDir, asset))
        } catch (e: Exception) {
            // Assets may not exist yet
        }
    }

    private fun deployDictionaries(rimeDir: File) {
        val dictDir = File(rimeDir, DICTIONARIES_DIR)
        if (!dictDir.exists()) dictDir.mkdirs()
        try {
            val assets = context.assets.list("rime/dictionaries") ?: return
            for (asset in assets)
                copyAsset("rime/dictionaries/", File(dictDir, asset))
        } catch (e: Exception) {
            // Assets may not exist yet
        }
    }

    private fun deployDefaultConfig(rimeDir: File) {
        try {
            val assets = context.assets.list("rime") ?: return
            for (asset in assets) {
                if (asset.endsWith(".yaml")) {
                    copyAsset("rime/", File(rimeDir, asset))
                }
            }
        } catch (e: Exception) {
            // Assets may not exist yet
        }
    }

    private fun copyAsset(assetPath: String, dest: File) {
        try {
            context.assets.open(assetPath).use { input ->
                dest.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            // Ignore copy errors
        }
    }

    fun needsDeployment(): Boolean {
        val rimeDir = File(context.filesDir, RIME_CONFIG_DIR)
        return !rimeDir.exists()
    }
}
