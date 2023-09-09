package com.yelmanov.domain

enum class Regions(val regionNumber: String, val regionName: String) {
    SE1("1", "se1-lulea"),
    SE2("2", "se2-sundsvall"),
    SE3("3", "se3-stockholm"),
    SE4("4", "/se4-malmo");

   fun getByNumber(regionNumber: String):Regions{
        when(regionNumber){
            "1" -> return SE1
            "2" -> return SE2
            "3" -> return SE3
            "4" -> return SE4
            else -> throw IllegalArgumentException("Region is not exists")
        }
    }
}