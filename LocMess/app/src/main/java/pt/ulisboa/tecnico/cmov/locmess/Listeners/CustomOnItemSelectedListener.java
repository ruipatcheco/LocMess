package pt.ulisboa.tecnico.cmov.locmess.Listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.locmess.R;

/**
 * Created by trosado on 24/03/17.
 */

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String[] items = parent.getResources().getStringArray(R.array.location_types_list);

        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
