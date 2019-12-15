package fr.utt.if26_projet;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@androidx.room.Database(
    entities = {Transaction.class},
    version = 1)
@TypeConverters({Converters.class})
abstract class Database extends RoomDatabase {

  private static volatile Database INSTANCE;

  static Database getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (Database.class) {
        if (INSTANCE == null) {
          INSTANCE =
              Room.databaseBuilder(context.getApplicationContext(), Database.class, "database")
                  .build();
        }
      }
    }

    return INSTANCE;
  }

  abstract TransactionDao transactionDao();
}
