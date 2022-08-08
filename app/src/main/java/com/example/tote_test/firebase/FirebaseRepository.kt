package com.example.tote_test.firebase

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

/*class AppValueEventListener (val onSuccess: (DataSnapshot) -> Unit): ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        onSuccess(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {
    }
}*/

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
                dataMap[GAMBLER_PHOTO_URL] = EMPTY
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

        //getGambler()
    }

    fun getUrlFromStorage(path: StorageReference, onSuccess: (url: String) -> Unit) {
        path.downloadUrl
            .addOnSuccessListener { onSuccess(it.toString()) }
            .addOnFailureListener { showToast(it.message.toString()) }
            //.addOnFailureListener { onFail(it.message.toString()) }
    }

    fun saveImageToStorage(uri: Uri, path: StorageReference, onSuccess: () -> Unit) {
        path.putFile(uri)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { showToast(it.message.toString()) }
            //.addOnFailureListener { onFail(it.message.toString()) }
    }

    /*
    Gamblers Repository
     */
    fun getGamblerLiveData(liveData: MutableLiveData<GamblerModel>) {
        REF_DB_ROOT.child(NODE_GAMBLERS).child(CURRENT_ID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                GAMBLER = snapshot.getValue(GamblerModel::class.java) ?: GamblerModel()
                //liveData.postValue(GAMBLER)
                liveData.value = GAMBLER
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

    fun saveGamblerToDB(dataMap: MutableMap<String, Any>, onSuccess: () -> Unit) {
        REF_DB_ROOT.child(NODE_GAMBLERS).child(CURRENT_ID).updateChildren(dataMap)
            .addOnCompleteListener {
                GAMBLER.nickname = dataMap[GAMBLER_NICKNAME].toString()
                GAMBLER.family = dataMap[GAMBLER_FAMILY].toString()
                GAMBLER.name = dataMap[GAMBLER_NAME].toString()
                GAMBLER.gender = dataMap[GAMBLER_GENDER].toString()

                onSuccess()

                /*showToast(APP_ACTIVITY.getString(R.string.data_updated))
                APP_ACTIVITY.supportFragmentManager.popBackStack()*/
            }
            .addOnFailureListener {
                showToast(it.message.toString())
                //onFail(it.message.toString())
            }
    }

    fun savePhotoUrlToDB(url: String, onSuccess: () -> Unit) {
        REF_DB_ROOT
            .child(NODE_GAMBLERS)
            .child(CURRENT_ID)
            .child(GAMBLER_PHOTO_URL)
            .setValue(url)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { showToast(it.message.toString()) }
            //.addOnFailureListener { onFail(it.message.toString()) }
    }

    fun saveStakeToDB(stake: Int, onSuccess: () -> Unit) {
        REF_DB_ROOT
            .child(NODE_GAMBLERS)
            .child(CURRENT_ID)
            .child(GAMBLER_STAKE)
            .setValue(stake)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { showToast(it.message.toString()) }
    }
}