package fr.utt.if26_projet;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

  private TransactionRepository transactionRepository;
  private LiveData<List<Transaction>> transactions;

  public TransactionViewModel(Application application) {
    super(application);
    transactionRepository = new TransactionRepository(application);
    transactions = transactionRepository.getAll();
  }

  LiveData<List<Transaction>> getAll() {
    return transactions;
  }

  LiveData<Transaction> get(int id) {
    return transactionRepository.get(id);
  }

  public void insert(Transaction transaction) {
    transactionRepository.insert(transaction);
  }
}
