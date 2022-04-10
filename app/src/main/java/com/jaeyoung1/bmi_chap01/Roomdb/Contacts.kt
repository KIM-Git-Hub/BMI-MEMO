package com.jaeyoung1.bmi_chap01.Roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_contacts")
data class Contacts(
    var weight: String?,
    var bmiNum: String?,
    var bmiString: String?,
    var memoText: String?,
    var year : String?,
    var month : String?,
    var day : String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
