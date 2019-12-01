package fr.utt.if26_projet;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;

class TransactionRepository {

  private TransactionDao transactionDao;
  private LiveData<List<Transaction>> transactions;

  TransactionRepository(Application application) {
    Database database = Database.getDatabase(application);
    transactionDao = database.transactionDao();
    transactions = transactionDao.getAll();
  }

  LiveData<List<Transaction>> getAll() {
    return transactions;
  }

  LiveData<Transaction> get(int id) {
    System.out.println("Getting transaction " + id + ".");
    return transactionDao.get(id);
  }

  void insert(Transaction transaction) {
    new InsertAsyncTask(transactionDao).execute(transaction);
  }

  private static class InsertAsyncTask extends AsyncTask<Transaction, Void, Void> {

    private TransactionDao TransactionDao;

    InsertAsyncTask(TransactionDao TransactionDao) {
      this.TransactionDao = TransactionDao;
    }

    @Override
    protected Void doInBackground(final Transaction... params) {
      TransactionDao.insert(params[0]);

      return null;
    }
  }
}
