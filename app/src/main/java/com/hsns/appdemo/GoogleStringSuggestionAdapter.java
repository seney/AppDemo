package com.hsns.appdemo;

import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SENEY SEAN on 1/15/16.
 */
public class GoogleStringSuggestionAdapter extends CursorAdapter {


    private final String[] mFields = {"_id", "name"};
    private final LayoutInflater mInflater;
    private final ArrayList<String> mStrings;

    public GoogleStringSuggestionAdapter(Context context, ArrayList<String> strings) {
        super(context, null, false);
        mInflater = LayoutInflater.from(context);
        this.mStrings = strings;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_search_suggestion, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String string = mStrings.get(cursor.getPosition());
        ViewHolder vh = (ViewHolder) view.getTag();
        vh.textView.setText(string);
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        return new GoogleStringSuggestionCursor(constraint);
    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;

        ViewHolder(View v) {
            textView = (TextView) v.findViewById(R.id.textViewName);
            imageView = (ImageView) v.findViewById(R.id.imageViewProduct);
        }
    }

    private class GoogleStringSuggestionCursor extends AbstractCursor {
        public GoogleStringSuggestionCursor(CharSequence constraint) {

        }

        @Override
        public int getCount() {
            return mStrings.size();
        }

        @Override
        public String[] getColumnNames() {
            return mFields;
        }

        @Override
        public String getString(int column) {
            if (column == 1)
                return mStrings.get(mPos);
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public short getShort(int column) {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public int getInt(int column) {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public long getLong(int column) {
            if (column == 0) {
                return mPos;
            }
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public float getFloat(int column) {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public double getDouble(int column) {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public boolean isNull(int column) {
            return false;
        }
    }
}
