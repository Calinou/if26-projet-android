package fr.utt.if26_projet;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build.VERSION_CODES;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Date;
import java.util.List;

public class TransactionListAdapter
    extends RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder> {

  class TransactionViewHolder extends RecyclerView.ViewHolder {
    private final LinearLayout transactionItemView;
    private final TextView amountTextView;
    private final TextView dateTextView;
    private final TextView categoryTextView;
    private final TextView contentsTextView;
    private final TextView accountTextView;

    private TransactionViewHolder(View itemView) {
      super(itemView);

      transactionItemView = itemView.findViewById(R.id.transaction_item);
      amountTextView = itemView.findViewById(R.id.transaction_item_amount);
      dateTextView = itemView.findViewById(R.id.transaction_item_date);
      categoryTextView = itemView.findViewById(R.id.transaction_item_category);
      contentsTextView = itemView.findViewById(R.id.transaction_item_contents);
      accountTextView = itemView.findViewById(R.id.transaction_item_account);
    }
  }

  private final LayoutInflater inflater;

  /** The cached copy of the transactions. */
  private List<Transaction> transactions;

  /** If `true`, discreet mode is enabled. */
  private boolean discreetMode = false;

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

      // Alternate row background colors for better readability
      if (position % 2 == 0) {
        holder.transactionItemView.setBackgroundColor(0x10808080);
      } else {
        // This must be done to reset the background color properly.
        // Otherwise, the color may not alternate correctly.
        holder.transactionItemView.setBackgroundColor(0x00000000);
      }

      holder.amountTextView.setText(current.getAmountString(discreetMode));
      holder.amountTextView.setTextColor(current.getAmountColor());

      final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
      holder.dateTextView.setText(dateFormat.format(new Date(current.getDate() * 1000L)));

      holder.categoryTextView.setText(current.getCategory().getValue());
      holder.contentsTextView.setText(current.getContents());
      holder.accountTextView.setText(current.getAccount().getValue());
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

  void setDiscreetMode(boolean discreetMode) {
    this.discreetMode = discreetMode;
  }
}
