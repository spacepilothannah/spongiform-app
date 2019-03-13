package com.github.spacepilothannah.spongiform.support

import com.github.spacepilothannah.spongiform.data.DataManager
import com.github.spacepilothannah.spongiform.data.DataManagerFactory

fun MockDataManagerFactory(dm : DataManager) : DataManagerFactory {
    return object : DataManagerFactory {
        override fun createDataManager(): DataManager {
            return dm
        }
    }
}