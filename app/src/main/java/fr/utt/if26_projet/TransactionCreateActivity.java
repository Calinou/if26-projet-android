package fr.utt.if26_projet;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import fr.utt.if26_projet.Transaction.Account;
import fr.utt.if26_projet.Transaction.Category;
import fr.utt.if26_projet.Transaction.Kind;
import java.text.ParseException;
import java.util.Date;

public class TransactionCreateActivity extends AppCompatActivity {

  private TransactionViewModel transactionViewModel;
  private RadioGroup kindRadioGroup;
  private TextView dateTextView;
  private TextView amountTextView;
  private RadioGroup accountRadioGroup;
  private RadioGroup categoryRadioGroup;
  private TextView contentsTextView;
  private TextView notesTextView;
  private Button submitButton;

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
    submitButton = findViewById(R.id.transaction_create_submit);

    resetForm();

    final TextWatcher textWatcher =
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Disable the Submit button if one of the required fields isn't filled
            submitButton.setEnabled(
                dateTextView.getText().length() >= 1
                    && amountTextView.getText().length() >= 1
                    && contentsTextView.getText().length() >= 1);
          }

          @Override
          public void afterTextChanged(Editable editable) {}
        };

    dateTextView.addTextChangedListener(textWatcher);
    amountTextView.addTextChangedListener(textWatcher);
    contentsTextView.addTextChangedListener(textWatcher);
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

              // Reset the form to make it easier to submit new transactions
              resetForm();
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

  /** Resets the form to its initial state. */
  public void resetForm() {
    final RadioButton kindDefaultRadioButton = findViewById(R.id.transaction_create_kind_income);
    kindDefaultRadioButton.setChecked(true);

    final RadioButton accountDefaultRadioButton =
        findViewById(R.id.transaction_create_account_cash);
    accountDefaultRadioButton.setChecked(true);

    final RadioButton categoryDefaultRadioButton =
        findViewById(R.id.transaction_create_category_food);
    categoryDefaultRadioButton.setChecked(true);

    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
    dateTextView.setText(dateFormat.format(new Date()));

    amountTextView.setText("");
    contentsTextView.setText("");
    notesTextView.setText("");
  }
}
