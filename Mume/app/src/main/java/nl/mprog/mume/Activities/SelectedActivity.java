/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Shows one artrecord (image and metadata). Has a button to start editing the image */

package nl.mprog.mume.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import nl.mprog.mume.Classes.Artrecord;
import nl.mprog.mume.Dialogs.HelpDialog;
import nl.mprog.mume.R;

public class SelectedActivity extends AppCompatActivity {

    private TextView artisname_holder;
    private TextView title_holder;
    private TextView dating_holder;
    private TextView materials_holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);

        artisname_holder = (TextView) findViewById(R.id.artistnameholder_textview);
        title_holder = (TextView) findViewById(R.id.titleholder_textview);
        dating_holder = (TextView) findViewById(R.id.datingholder_textview);
        materials_holder = (TextView) findViewById(R.id.materialsholder_textview);

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("objectid");
        Artrecord obj = gson.fromJson(strObj, Artrecord.class);
        Log.e("SELECTED ACT", obj.getPrincipalmaker());

        artisname_holder.setText(obj.getPrincipalmaker());
        title_holder.setText(obj.getTitle());
        dating_holder.setText(obj.getDating());
        materials_holder.setText(obj.getMaterials());

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

        // Open Help-dialog once the button gets pressed
        if (id == R.id.searchhelp_menubutton) {

            DialogFragment newFragment = new HelpDialog();
            newFragment.show(getFragmentManager(), "help");
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    // gets executed when the 'Meme it' button gets pressed
    public void startEdit(View view){
        // open the EditActivity to start editing the image
        Intent startEdit = new Intent(this, EditActivity.class);
        startActivityForResult(startEdit, 1);
    }

}

