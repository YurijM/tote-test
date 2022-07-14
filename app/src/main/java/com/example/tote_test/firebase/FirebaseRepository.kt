package com.example.tote_test.firebase

import androidx.lifecycle.MutableLiveData
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class FirebaseRepository {
    init {
        AUTH = FirebaseAuth.getInstance()
    }

    fun signup(onSuccess: () -> Unit, onFail: (String) -> Unit) {
        AUTH.createUserWithEmailAndPassword(EMAIL, PASSWORD)
            .addOnSuccessListener {
                CURRENT_ID = AUTH.currentUser?.uid.toString()

                val dataMap = mutableMapOf<String, Any>()
                dataMap[CHILD_ID] = CURRENT_ID
                dataMap[GAMBLER_NICKNAME] = ""
                dataMap[GAMBLER_EMAIL] = EMAIL
                dataMap[GAMBLER_FAMILY] = ""
                dataMap[GAMBLER_NAME] = ""
                dataMap[GAMBLER_GENDER] = ""
                dataMap[GAMBLER_PHOTO_URI] = EMPTY
                dataMap[GAMBLER_STAKE] = 0
                dataMap[GAMBLER_POINTS] = 0.00
                dataMap[GAMBLER_PREV_PLACE] = 0
                dataMap[GAMBLER_PLACE] = 1
                dataMap[GAMBLER_IS_ADMIN] = false

                REF_DB_ROOT.child(NODE_GAMBLERS).child(CURRENT_ID).updateChildren(dataMap)
                    .addOnCompleteListener { onSuccess() }
                    .addOnFailureListener { showToast(it.message.toString()) }
            }
            .addOnFailureListener {
                onFail(it.message.toString())
            }
    }


    fun signIn(onSuccess: () -> Unit) {
        if (AppPreferences.getIsAuth()) {
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

    fun initDB() {
        REF_DB_ROOT = FirebaseDatabase.getInstance().reference
        REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference

        CURRENT_ID = AUTH.currentUser?.uid.toString()
    }

    /*
    Gamblers Repository
     */
    fun getGamblerLiveData(liveData: MutableLiveData<GamblerModel>) {
        REF_DB_ROOT.child(NODE_GAMBLERS).child(CURRENT_ID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                GAMBLER = snapshot.getValue(GamblerModel::class.java) ?: GamblerModel()
                liveData.postValue(GAMBLER)
            }

            override fun onCancelled(error: DatabaseError) {
                fixError("FirebaseRepository-getGambler-onCancelled: ${error.message}")
            }
        })
    }

    fun getGambler() {
        REF_DB_ROOT.child(NODE_GAMBLERS).child(CURRENT_ID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                GAMBLER = snapshot.getValue(GamblerModel::class.java) ?: GamblerModel()
            }

            override fun onCancelled(error: DatabaseError) {
                fixError("FirebaseRepository-getGambler-onCancelled: ${error.message}")
            }
        })
    }
}