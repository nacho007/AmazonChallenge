package com.idd.domain.repositories

interface SavePictureRepository {
    fun <T> savePicture(param: T): Boolean
}