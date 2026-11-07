package com.example.showpointrma.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SerijaDao {

    // livedata: room ce sam javiti UI-ju svaki put kad se tabela promeni
    @Query("SELECT * FROM serija ORDER BY naziv")
    LiveData<List<SerijaEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SerijaEntity> serije);

    @Query("DELETE FROM serija")
    void deleteAll();

}
