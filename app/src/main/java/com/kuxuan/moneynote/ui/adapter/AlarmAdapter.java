package com.kuxuan.moneynote.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.json.Time;
import com.kuxuan.moneynote.listener.DeleteListener;
import com.kuxuan.moneynote.ui.adapter.viewholder.AlarmHolder;

import java.util.List;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class AlarmAdapter extends BaseItemDraggableAdapter<Time,AlarmHolder> {
    DeleteListener mDeleteListener;
    public AlarmAdapter(@LayoutRes int layoutResId, @Nullable List<Time> data,DeleteListener mDeleteListener) {
        super(layoutResId, data);
        this.mDeleteListener = mDeleteListener;

    }

    @Override
    protected void convert(final AlarmHolder helper, Time item) {
        helper.setText(R.id.time, item.getTime());

        helper.getView(R.id.right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteListener.delete(helper.getAdapterPosition());
            }
        });

    }

}


