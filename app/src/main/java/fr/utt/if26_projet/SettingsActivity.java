package fr.utt.if26_projet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    setTitle(R.string.action_settings);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    final Button discreetModeButton = findViewById(R.id.settings_discreet_mode);
    discreetModeButton.setOnClickListener(
        new OnClickListener() {
          @Override
          public void onClick(View view) {
            final Intent intent = new Intent(view.getContext(), SettingsDiscreetModeActivity.class);
            startActivity(intent);
          }
        });

    final Button aboutButton = findViewById(R.id.settings_about);
    aboutButton.setOnClickListener(
        new OnClickListener() {
          @Override
          public void onClick(View view) {
            final Intent intent = new Intent(view.getContext(), SettingsAboutActivity.class);
            startActivity(intent);
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
