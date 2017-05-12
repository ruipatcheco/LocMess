package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Duarte on 12/05/2017.
 */

public class MyAdapter extends ArrayAdapter<Model> {

    private class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
        protected RadioGroup radioGroup;
    }

    private final List<Model> list;
    private final Activity context;
    boolean checkAll_flag = false;
    boolean checkItem_flag = false;

    public MyAdapter(Activity context, List<Model> list) {
        super(context, R.layout.profile_choice_for_messages, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.profile_choice_for_messages, null);

            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.profile_choice_label);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.profile_choice_checkbox);
            viewHolder.checkbox.setTag(position);
            viewHolder.radioGroup = (RadioGroup) convertView.findViewById(R.id.radioProfileGroup);
            viewHolder.radioGroup.setTag(position);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ViewHolder viewHolderCopy = viewHolder;

        viewHolder.text.setText(list.get(position).getName());
        viewHolder.checkbox.setChecked(list.get(position).isSelected());
        viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox checkbox = (CheckBox) v;
                int getPosition = (Integer) checkbox.getTag();
                //list.get(getPosition).setSelected(checkbox.isChecked());
                //Toast.makeText(context, viewHolderCopy.radioGroup.getChildCount(), Toast.LENGTH_LONG).show();
            }
        });

        viewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean isCcOrIsTo = (checkedId == R.id.radio_whitelist);
                int getPosition = (Integer) group.getTag();
                list.get(getPosition).setBlacklist(isCcOrIsTo);
                //Toast.makeText(context, getPosition, Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }
}
