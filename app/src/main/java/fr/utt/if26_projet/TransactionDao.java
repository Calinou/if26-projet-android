package fr.utt.if26_projet;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TransactionDao {

  @Query("select * from transactions order by date asc")
  LiveData<List<Transaction>> getAll();

  @Query("select * from transactions where id = :id")
  LiveData<Transaction> get(int id);

  @Insert()
  void insert(Transaction transaction);

  @Query("delete from transactions")
  void deleteAll();
}
