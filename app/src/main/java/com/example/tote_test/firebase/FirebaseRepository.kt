package com.example.tote_test.firebase

import com.example.tote_test.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FirebaseRepository {
    init {
        AUTH = FirebaseAuth.getInstance()
    }

    fun connectionToDatabase(onSuccess: () -> Unit) {
        if (AppPreferences.getIsAuth()) {
            initDB()
            onSuccess()
        } else {
            AUTH.signInWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnSuccessListener {
                    initDB()
                    onSuccess()
                }
                .addOnFailureListener {
                    fixError(it.message.toString())
                }
        }
    }

    fun signOut() {
        AppPreferences.setIsAuth(false)
        AUTH.signOut()
    }

    private fun initDB() {
        REF_DB_ROOT = FirebaseDatabase.getInstance().reference
        REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference

        CURRENT_ID = AUTH.currentUser?.uid.toString()
        REF_GAMBLER = REF_DB_ROOT.child(NODE_GAMBLERS).child(CURRENT_ID)
    }
}