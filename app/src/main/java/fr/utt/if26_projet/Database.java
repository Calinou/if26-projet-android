package fr.utt.if26_projet;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@androidx.room.Database(
    entities = {Transaction.class},
    version = 1)
abstract class Database extends RoomDatabase {

  private static volatile Database INSTANCE;

  private static RoomDatabase.Callback roomDatabaseCallback =
      new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase database) {
          super.onOpen(database);
          new PopulateDbAsync(INSTANCE).execute();
        }
      };

  static Database getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (Database.class) {
        if (INSTANCE == null) {
          INSTANCE =
              Room.databaseBuilder(context.getApplicationContext(), Database.class, "database")
                  .addCallback(roomDatabaseCallback)
                  .build();
        }
      }
    }

    return INSTANCE;
  }

  abstract TransactionDao transactionDao();

  private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
    private final TransactionDao transactionDao;

    PopulateDbAsync(Database database) {
      transactionDao = database.transactionDao();
    }

    @Override
    protected Void doInBackground(final Void... params) {
      System.out.println("Populating database...");
      transactionDao.insert(
          new Transaction(0, 150000, 1500000000, 0, 0, "Salaire", "Quelques notes...", false));
      transactionDao.insert(
          new Transaction(0, -2500, 1500000000, 0, 0, "Courses", "Quelques notes...", false));
      transactionDao.insert(
          new Transaction(0, -550, 1500000000, 0, 0, "Cafés", "Quelques notes...", false));
      transactionDao.insert(
          new Transaction(0, 652, 1500000000, 0, 0, "PayPal", "Quelques notes...", false));

      return null;
    }
  }
}
