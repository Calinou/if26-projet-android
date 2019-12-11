package fr.utt.if26_projet;

import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TransactionListAdapter
    extends RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder> {

  class TransactionViewHolder extends RecyclerView.ViewHolder {
    private final TextView amountTextView;
    private final TextView dateTextView;
    private final TextView contentsTextView;
    private final TextView accountTextView;

    private TransactionViewHolder(View itemView) {
      super(itemView);

      amountTextView = itemView.findViewById(R.id.transaction_item_amount);
      dateTextView = itemView.findViewById(R.id.transaction_item_date);
      contentsTextView = itemView.findViewById(R.id.transaction_item_contents);
      accountTextView = itemView.findViewById(R.id.transaction_item_account);
    }
  }

  private final LayoutInflater inflater;

  /** The cached copy of the transactions. */
  private List<Transaction> transactions;

  //  private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
  //    @Override
  //    public void onClick(View view) {
  //      Transaction transaction = (TransactionItem.TransactionItem) view.getTag();
  //      Context context = view.getContext();
  //      Intent intent = new Intent(context, TransactionDetailActivity.class);
  //      intent.putExtra(TransactionDetailFragment.ARG_ITEM_ID, transaction.getId());
  //
  //      context.startActivity(intent);
  //    }
  //  };

  TransactionListAdapter(Context context) {
    inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = inflater.inflate(R.layout.transaction_item, parent, false);

    return new TransactionViewHolder(itemView);
  }

  @RequiresApi(api = VERSION_CODES.N)
  @Override
  public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
    if (transactions != null) {
      final Transaction current = transactions.get(position);

      holder.amountTextView.setText(current.getAmountString());
      holder.amountTextView.setTextColor(current.getAmountColor());

      // String.valueOf() is used as we should set the value to a string, not an integer.
      // Otherwise, Android will interpret the integer as a resource ID (and will crash as it
      // doesn't exist).
      holder.dateTextView.setText(String.valueOf(current.getDate()));
      holder.contentsTextView.setText(current.getContents());
      holder.accountTextView.setText(String.valueOf(current.getAccount()));
    } else {
      // Data isn't ready yet
      holder.amountTextView.setText("...");
      holder.dateTextView.setText("...");
      holder.contentsTextView.setText("...");
      holder.accountTextView.setText("...");
    }
  }

  void setTransactions(List<Transaction> transactions) {
    this.transactions = transactions;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    if (transactions != null) {
      return transactions.size();
    }

    return 0;
  }
}
