package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Adapters;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Profile;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.R;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.Model;

/**
 * Created by brigadinhos on 12/05/2017.
 */


public class ProfileChoiceAdapter extends ArrayAdapter<Model> {

    private class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
        protected RadioGroup radioGroup;
    }


    private final List<Model> list;
    private final Activity context;
    boolean checkAll_flag = false;
    boolean checkItem_flag = false;

    public ProfileChoiceAdapter(Activity context, List<Model> list) {
        super(context, R.layout.row, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.row, null);

            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.label);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.check);
            viewHolder.checkbox.setTag(position);
            viewHolder.radioGroup = (RadioGroup) convertView.findViewById(R.id.radioSex);
            viewHolder.radioGroup.setTag(position);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Profile profile = list.get(position).getProfile();

        viewHolder.text.setText(profile.getKey() + " -> " + profile.getValue());
        viewHolder.checkbox.setChecked(list.get(position).isSelected());
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox checkbox = (CheckBox) v;
                int getPosition = (Integer) checkbox.getTag();
                list.get(getPosition).setSelected(checkbox.isChecked());
                finalViewHolder.radioGroup.getChildAt(0).setEnabled(checkbox.isChecked());
                finalViewHolder.radioGroup.getChildAt(1).setEnabled(checkbox.isChecked());
                Log.d("SIZE", String.valueOf(finalViewHolder.radioGroup.getChildCount()));

            }
        });

        viewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean isWhite = (checkedId == R.id.cc);
                int getPosition = (Integer) group.getTag();
                list.get(getPosition).setWhite(isWhite);
            }
        });

        return convertView;
    }
}