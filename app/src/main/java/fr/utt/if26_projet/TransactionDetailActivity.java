package fr.utt.if26_projet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import java.util.Date;

/** An activity representing a single Transaction detail screen. */
public class TransactionDetailActivity extends AppCompatActivity {

  /** The intent argument used to specify the transaction ID. */
  static final String ARG_ITEM_ID = "item_id";

  /** The transaction to display. */
  private Transaction transaction;

  private TransactionViewModel transactionViewModel;

  private TextView kindTextView;
  private TextView amountTextView;
  private TextView dateTextView;
  private TextView accountTextView;
  private TextView categoryTextView;
  private TextView contentsTextView;
  private TextView notesTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transaction_detail);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    kindTextView = findViewById(R.id.transaction_detail_kind);
    amountTextView = findViewById(R.id.transaction_detail_amount);
    dateTextView = findViewById(R.id.transaction_detail_date);
    accountTextView = findViewById(R.id.transaction_detail_account);
    categoryTextView = findViewById(R.id.transaction_detail_category);
    contentsTextView = findViewById(R.id.transaction_detail_contents);
    notesTextView = findViewById(R.id.transaction_detail_notes);

    final Intent intent = getIntent();
    transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
    transactionViewModel
        .get(intent.getIntExtra(ARG_ITEM_ID, 0))
        .observe(
            this,
            new Observer<Transaction>() {
              @Override
              public void onChanged(@Nullable final Transaction transaction2) {
                transaction = transaction2;
                updateTransactionDetails();
              }
            });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.transaction_detail, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    } else if (item.getItemId() == R.id.action_delete) {
      final AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage(R.string.transaction_delete_confirm);
      builder.setCancelable(true);

      builder.setPositiveButton(
          R.string.yes,
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              transactionViewModel.delete(transaction);
              final Intent intent = new Intent(getBaseContext(), TransactionListActivity.class);
              startActivity(intent);
            }
          });

      builder.setNegativeButton(
          R.string.no,
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              dialog.cancel();
            }
          });

      final AlertDialog alertDialog = builder.create();
      alertDialog.show();
    }

    return super.onOptionsItemSelected(item);
  }

  /** Update the transaction details displayed in the view. */
  private void updateTransactionDetails() {
    if (transaction != null) {
      setTitle(transaction.getContents());

      kindTextView.setText(getString(transaction.getKindStringResource()));

      final SharedPreferences settings = getSharedPreferences("user", Context.MODE_PRIVATE);
      amountTextView.setText(
          transaction.getAmountString(
              settings.getBoolean(getString(R.string.setting_discreet_mode), false)));

      amountTextView.setTextColor(transaction.getAmountColor());

      final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
      dateTextView.setText(dateFormat.format(new Date(transaction.getDate() * 1000L)));

      accountTextView.setText(transaction.getAccount().getValue());
      categoryTextView.setText(transaction.getCategory().getValue());
      contentsTextView.setText(transaction.getContents());

      if (!transaction.getNotes().isEmpty()) {
        notesTextView.setText(transaction.getNotes());
      } else {
        notesTextView.setText(R.string.transaction_no_notes);
        notesTextView.setTextColor(0x80808080);
      }
    }
  }
}
