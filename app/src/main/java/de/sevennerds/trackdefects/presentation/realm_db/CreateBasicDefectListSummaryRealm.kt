package de.sevennerds.trackdefects.presentation.realm_db

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class CreateBasicDefectListSummaryRealm(
        @PrimaryKey var id: Long = 0L,
        var groundPlanPictureName: String = "",
        var streetAddressRealm: StreetAddressRealm? = null,
        var viewParticipantRealmList: RealmList<ViewParticipantRealm?> = RealmList()
) : RealmObject()


open class StreetAddressRealm(@PrimaryKey var id: Long = 0L,
                              var streetName: String = "",
                              var streetNumber: String = "",
                              var streetAdditional: String = "") : RealmObject()

open class ViewParticipantRealm(@PrimaryKey var id: Long = 0L,
                                var name: String = "",
                                var phoneNumber: String = "",
                                var email: String = "") : RealmObject()