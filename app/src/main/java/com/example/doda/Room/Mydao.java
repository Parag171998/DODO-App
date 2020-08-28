package com.example.doda.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.doda.model.Drawing;
import com.example.doda.model.MapPin;

import java.util.List;

@Dao
public interface Mydao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long addDrawing(Drawing drawing);

    @Update
    public void updateDrawing(Drawing drawing);

    @Insert
    public void addPins(MapPin mapPin);

    @Query("select * from Drawing")
    public List<Drawing> getDrawings();

    @Query("select * from MapPin where drawingId = :id")
    public List<MapPin> getPins(long id);

    @Query("DELETE FROM Drawing")
    public void deleteAll();

    @Query("select * from Drawing where id = :id")
    public Drawing getDrawingById(int id);

    @Delete
    public void deleteDrawing(Drawing drawing);
}
