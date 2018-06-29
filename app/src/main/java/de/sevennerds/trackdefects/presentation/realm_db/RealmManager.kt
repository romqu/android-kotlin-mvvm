package de.sevennerds.trackdefects.presentation.realm_db

import io.realm.Realm
import io.realm.RealmObject

object RealmManager {

    fun <T : RealmObject> insertOrUpdate(data: T) =
            Realm.getDefaultInstance().use {

                it.executeTransaction { realmT ->
                    realmT.insertOrUpdate(data)
                }

                it.close()
            }
}