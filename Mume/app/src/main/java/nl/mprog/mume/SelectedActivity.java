/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

package nl.mprog.mume;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class SelectedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);
    }

    // Inflate menu facilitate help-button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }


    // Implementation of action-bar / menu functionalities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Open Help
        if (id == R.id.searchhelp_menubutton) {
            Toast.makeText(this, "Help!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // gets executed when the 'Meme it' button gets pressed
    public void startEdit(View view){
        Intent startEdit = new Intent(this, EditActivity.class);
        startActivityForResult(startEdit, 1);
    }
}
