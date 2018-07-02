package de.sevennerds.trackdefects.presentation.realm_db

import io.realm.Realm
import io.realm.RealmObject

object RealmManager {

    fun <T : RealmObject> insertOrUpdate(data: T) {
        val realm = Realm.getDefaultInstance()

        //realm.isClosed

        realm.executeTransaction { realmT ->
            realmT.insertOrUpdate(data)
        }

        realm.close()
    }
}