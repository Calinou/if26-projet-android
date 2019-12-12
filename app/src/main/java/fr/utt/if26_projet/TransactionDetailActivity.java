package fr.utt.if26_projet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import java.util.List;

/**
 * An activity representing a single Transaction detail screen. This activity is only used on narrow
 * width devices. On tablet-size devices, item details are presented side-by-side with a list of
 * items in a {@link TransactionListActivity}.
 */
public class TransactionDetailActivity extends AppCompatActivity {

  private TransactionViewModel transactionViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transaction_detail);

    final Toolbar toolbar = findViewById(R.id.detail_toolbar);
    setSupportActionBar(toolbar);

    System.out.println("Loading TransactionDetailActivity");

    // Show the Up button in the action bar.
    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // savedInstanceState is non-null when there is fragment state
    // saved from previous configurations of this activity
    // (e.g. when rotating the screen from portrait to landscape).
    // In this case, the fragment will automatically be re-added
    // to its container so we don't need to manually add it.
    // For more information, see the Fragments API guide at:
    //
    // http://developer.android.com/guide/components/fragments.html
    //
    if (savedInstanceState == null) {
      transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
      transactionViewModel
          .getAll()
          .observe(
              this,
              new Observer<List<Transaction>>() {
                @Override
                public void onChanged(@Nullable final List<Transaction> transactions) {
                  System.out.println("Transactions updated.");
                }
              });

      // Create the detail fragment and add it to the activity
      // using a fragment transaction.
      final Bundle arguments = new Bundle();
      arguments.putString(
          TransactionDetailFragment.ARG_ITEM_ID,
          getIntent().getStringExtra(TransactionDetailFragment.ARG_ITEM_ID));
      final TransactionDetailFragment fragment = new TransactionDetailFragment();
      fragment.setArguments(arguments);
      getSupportFragmentManager()
          .beginTransaction()
          .add(R.id.transaction_detail_container, fragment)
          .commit();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      // This ID represents the Home or Up button. In the case of this
      // activity, the Up button is shown. For
      // more details, see the Navigation pattern on Android Design:
      //
      // http://developer.android.com/design/patterns/navigation.html#up-vs-back
      //
      navigateUpTo(new Intent(this, TransactionListActivity.class));

      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
