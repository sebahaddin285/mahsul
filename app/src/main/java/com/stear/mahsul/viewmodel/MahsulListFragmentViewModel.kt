package com.stear.mahsul.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView.ViewCacheExtension
import com.stear.mahsul.model.Mahsul
import com.stear.mahsul.model.Users
import com.stear.mahsul.repository.Repository
import com.stear.mahsul.view.MahsulListFragment

class MahsulListFragmentViewModel(val repository: Repository) : ViewModel() {

    val mahsulList : MutableLiveData<List<Mahsul>> = MutableLiveData()

    fun getMahsul(){
        repository.getMahsul().addSnapshotListener(){snap, error ->
            if (snap != null && !snap.isEmpty) {
                val document = snap.documents
                val commingList : ArrayList<Mahsul> = ArrayList()
                commingList.clear()
                document.forEach() {
                    val mahsul = Mahsul(
                        it.get("uId").toString(),
                        it.get("titleText").toString(),
                        it.get("destinationText").toString(),
                        it.get("turText").toString(),
                        it.get("priceText").toString(),
                        it.get("photoUrl").toString(),
                        it.get("eMail").toString()
                    )
                    commingList.add(mahsul)
                }
                mahsulList.value = commingList
            }


        }
    }




}