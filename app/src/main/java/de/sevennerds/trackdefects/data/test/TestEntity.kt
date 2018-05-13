package de.sevennerds.trackdefects.data.test

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "test_table")
data class TestEntity @JvmOverloads constructor(@PrimaryKey(autoGenerate = true) val id: Long,
                                                @ColumnInfo(name = "name") val name: String,
                                                @Ignore val intList: List<Int> = ArrayList()) {
}

