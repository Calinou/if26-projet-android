package fr.utt.if26_projet;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import fr.utt.if26_projet.Transaction.Account;
import fr.utt.if26_projet.Transaction.Category;
import fr.utt.if26_projet.Transaction.Kind;
import java.text.ParseException;

public class TransactionCreateActivity extends AppCompatActivity {

  private TransactionViewModel transactionViewModel;
  private RadioGroup kindRadioGroup;
  private TextView dateTextView;
  private TextView amountTextView;
  private RadioGroup accountRadioGroup;
  private RadioGroup categoryRadioGroup;
  private TextView contentsTextView;
  private TextView notesTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transaction_create);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

    kindRadioGroup = findViewById(R.id.transaction_create_kind);
    dateTextView = findViewById(R.id.transaction_create_date);
    amountTextView = findViewById(R.id.transaction_create_amount);
    accountRadioGroup = findViewById(R.id.transaction_create_account);
    categoryRadioGroup = findViewById(R.id.transaction_create_category);
    contentsTextView = findViewById(R.id.transaction_create_contents);
    notesTextView = findViewById(R.id.transaction_create_notes);

    final Button submitButton = findViewById(R.id.transaction_create_submit);
    submitButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            // Determine the selected radio buttons
            final View kindRadioButton =
                kindRadioGroup.findViewById(kindRadioGroup.getCheckedRadioButtonId());
            final Kind kind = Kind.values()[kindRadioGroup.indexOfChild(kindRadioButton)];

            final View accountRadioButton =
                accountRadioGroup.findViewById(accountRadioGroup.getCheckedRadioButtonId());
            final Account account =
                Account.values()[accountRadioGroup.indexOfChild(accountRadioButton)];

            final View categoryRadioButton =
                categoryRadioGroup.findViewById(categoryRadioGroup.getCheckedRadioButtonId());
            final Category category =
                Category.values()[categoryRadioGroup.indexOfChild(categoryRadioButton)];

            // Handle French decimal separators in the amount
            final double amount =
                Double.parseDouble(amountTextView.getText().toString().replace(',', '.'));

            // Convert the date to an UNIX timestamp for storage purposes
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
            final long date;
            try {
              date = dateFormat.parse(dateTextView.getText().toString()).getTime() / 1000L;

              // The transaction kind isn't stored directly.
              // Instead, the transaction amount is stored as a positive or negative integer
              // depending on the transaction kind.
              final Transaction transaction =
                  new Transaction(
                      0,
                      (kind == Kind.EXPENSE ? -1 : 1) * (int) (amount * 100.0),
                      date,
                      account,
                      category,
                      contentsTextView.getText().toString(),
                      notesTextView.getText().toString(),
                      kind == Kind.TRANSFER);
              transactionViewModel.insert(transaction);

              final TextView resultTextView = findViewById(R.id.transaction_create_result);
              resultTextView.setText(R.string.transaction_added);
            } catch (ParseException e) {
              e.printStackTrace();
            }
          }
        });
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
