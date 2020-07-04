package com.abh16am.ucekbuzz.models
import androidx.annotation.IntDef

/**
 * Created by Shahbaz Hashmi on 26/04/19.
 */
class RowModel {

    companion object {

        @IntDef(COUNTRY, STATE, CITY)
        @Retention(AnnotationRetention.SOURCE)
        annotation class RowType

        const val COUNTRY = 1
        const val STATE = 2
        const val CITY = 3
    }

    @RowType var type : Int

    lateinit var semester : Semester

    lateinit var subject : Subject

    lateinit var year : Year

    var isExpanded : Boolean

    constructor(@RowType type : Int, semester : Semester, isExpanded : Boolean = false){
        this.type = type
        this.semester = semester
        this.isExpanded = isExpanded
    }

    constructor(@RowType type : Int, subject : Subject, isExpanded : Boolean = false){
        this.type = type
        this.subject = subject
        this.isExpanded = isExpanded
    }

    constructor(@RowType type : Int, year: Year, isExpanded : Boolean = false){
        this.type = type
        this.year = year
        this.isExpanded = isExpanded
    }

}