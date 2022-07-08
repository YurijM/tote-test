package com.example.tote_test.utils

import com.example.tote_test.MainActivity
import com.example.tote_test.firebase.FirebaseRepository
import com.example.tote_test.models.Gambler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference

lateinit var APP_ACTIVITY: MainActivity

lateinit var REPOSITORY: FirebaseRepository
lateinit var AUTH: FirebaseAuth
lateinit var REF_DB_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var REF_GAMBLER: DatabaseReference

const val NODE_GAMBLERS = "gamblers"

lateinit var GAMBLER: Gambler
lateinit var CURRENT_ID: String
lateinit var EMAIL: String
lateinit var PASSWORD: String
var IS_ADMIN: Boolean = false

const val KEY_LOG = "logTote"
const val YEAR_START = 2021
const val EMPTY = "empty"
