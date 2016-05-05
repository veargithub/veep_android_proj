package com.example.ant_test.wheel.wheelCustomView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ant_test.R;

import java.util.List;


/**
 * Created by 9020MT on 2014/10/11.
 */
public class TestWheelAdapter extends AbstractWheelAdapter {
    
    private List<String> items;
    protected LayoutInflater inflater;
    protected int itemResourceId;

    public TestWheelAdapter(Context context, int itemResourceId, List<String> items) {
        
        this.items = items;
        this.itemResourceId = itemResourceId;
        if (this.itemResourceId == 0) {
            this.itemResourceId = R.layout.item_wheel;
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getItemsCount() {
        if (items == null)
            return 0;
        return items.size();
    }

    private View getLayoutView(int itemResourceId, ViewGroup parent) {
        return inflater.inflate(itemResourceId, parent, false);
    }

    public String getItemText(int index) {
        if (index >= 0 && index < items.size()) {
            String item = items.get(index);
            return item;

        }
        return null;
    }


    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        if (index >= 0 && index < getItemsCount()) {
            if (convertView == null) {
                convertView = getLayoutView(itemResourceId, parent);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.adapterItemTv);
            if (textView != null) {
                String text = getItemText(index);
                if (text == null) {
                    text = "";
                }
                textView.setText(text);

                textView.setLines(1);
            }
            return convertView;
        }
        return null;
    }
}
