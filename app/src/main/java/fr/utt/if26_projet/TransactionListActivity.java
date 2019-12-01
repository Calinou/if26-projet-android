package fr.utt.if26_projet;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

/**
 * An activity representing a list of Transactions. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link TransactionDetailActivity} representing item details.
 */
public class TransactionListActivity extends AppCompatActivity {

  private TransactionViewModel transactionViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transaction_list);

    final Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

    final FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
          }
        });

    final RecyclerView recyclerView = findViewById(R.id.transaction_list);
    final TransactionListAdapter adapter = new TransactionListAdapter(this);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    System.out.println("Loading TransactionListActivity");

    transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
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

  /*
  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, TransactionItem.ITEMS));
  }

  public static class SimpleItemRecyclerViewAdapter
      extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final List<TransactionItem.TransactionItem> mValues;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        TransactionItem.TransactionItem item = (TransactionItem.TransactionItem) view.getTag();
        Context context = view.getContext();
        Intent intent = new Intent(context, TransactionDetailActivity.class);
        intent.putExtra(TransactionDetailFragment.ARG_ITEM_ID, item.id);

        context.startActivity(intent);
      }
    };

    SimpleItemRecyclerViewAdapter(TransactionListActivity parent,
        List<TransactionItem.TransactionItem> items) {
      mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.transaction_list_content, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
      holder.mIdView.setText(mValues.get(position).id);
      holder.mContentView.setText(mValues.get(position).content);

      holder.itemView.setTag(mValues.get(position));
      holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
      return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

      final TextView mIdView;
      final TextView mContentView;

      ViewHolder(View view) {
        super(view);
        mIdView = (TextView) view.findViewById(R.id.id_text);
        mContentView = (TextView) view.findViewById(R.id.content);
      }
    }*/

  /*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if (requestCode == ListTransactionsActivity.NEW_TRANSACTION_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
        transactionListViewModel.insert(
            new Transaction(
                0,
                data.getIntExtra("amount", 0),
                data.getIntExtra("date", 0),
                data.getIntExtra("account", 0),
                data.getIntExtra("category", 0),
                Objects.requireNonNull(data.getStringExtra("contents")),
                data.getStringExtra("notes"),
                data.getBooleanExtra("isTransfer", false)));
      }
    }
  }
  */
}
