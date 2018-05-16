package de.sevennerds.trackdefects.util

import de.sevennerds.trackdefects.data.defect_image.DefectImage
import de.sevennerds.trackdefects.data.defect_info.DefectInfo
import de.sevennerds.trackdefects.data.defect_list.DefectList
import de.sevennerds.trackdefects.data.floor.Floor
import de.sevennerds.trackdefects.data.living_unit.LivingUnit
import de.sevennerds.trackdefects.data.room.Room
import de.sevennerds.trackdefects.data.street_address.StreetAddress
import de.sevennerds.trackdefects.data.view_participant.ViewParticipant

val defectImageList = listOf(
        DefectImage(0,
                0,
                "name",
                "orig",
                0,
                1),
        DefectImage(0,
                0,
                "name1",
                "orig1",
                1,
                1))

val defectInfo = DefectInfo(
        0,
        0,
        "desc",
        "meas",
        "compin",
        "done",
        1,
        defectImageList)

val room = Room(0,
        0,
        1,
        "Wohnzimmer",
        1,
        "Hier")

val livingUnit = LivingUnit(0,
        0,
        1,
        1)

val floor = Floor(0,
        0,
        1,
        "EG")

val viewParticipantList = listOf(ViewParticipant(0,
        0,
        0,
        "Bern",
        "Trem",
        1324141,
        "a@a.de",
        "myco"))

val streetAddress = StreetAddress(0,
        0,
        0,
        "street",
        1,
        1,
        "addd",
        "d21",
        viewParticipantList = viewParticipantList)

val defectList = DefectList(0,
        0,
        "name",
        "wqdwqd", streetAddress)