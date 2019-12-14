package fr.utt.if26_projet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

/** An activity representing a list of Transactions. */
public class TransactionListActivity extends AppCompatActivity {

  private TransactionListAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transaction_list);
    setTitle(R.string.title_transaction_list);

    final Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

    final FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            final Intent intent = new Intent(view.getContext(), TransactionCreateActivity.class);
            startActivity(intent);
          }
        });

    final SharedPreferences settings = getSharedPreferences("user", Context.MODE_PRIVATE);
    adapter = new TransactionListAdapter(this);
    adapter.setDiscreetMode(settings.getBoolean(getString(R.string.setting_discreet_mode), false));

    final RecyclerView recyclerView = findViewById(R.id.transaction_list);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    System.out.println("Loading TransactionListActivity");

    final TransactionViewModel transactionViewModel =
        new ViewModelProvider(this).get(TransactionViewModel.class);
    transactionViewModel
        .getAll()
        .observe(
            this,
            new Observer<List<Transaction>>() {
              @Override
              public void onChanged(@Nullable final List<Transaction> transactions) {
                System.out.println("Updating list of transactions.");
                // Update the cached copy of the transactions in the adapter
                adapter.setTransactions(transactions);
              }
            });
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    // Recreate the activity to take a possible discreet mode change into account
    recreate();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.action_settings) {
      final Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
    }

    return super.onOptionsItemSelected(item);
  }
}
