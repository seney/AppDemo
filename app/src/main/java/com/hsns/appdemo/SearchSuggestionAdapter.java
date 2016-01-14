package com.hsns.appdemo;

import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by seney on 1/14/16.
 */
public class SearchSuggestionAdapter extends CursorAdapter {
    private final String TAG = "SearchSuggestionAdapter";
    ArrayList<Product> mProducts;
    ArrayList<Product> mProducts1;
    //    private TextView mTextView;
//    private ImageView mImageView;
    private LayoutInflater mInflater;
    private String[] mFields = {"_id", "name"};

    public SearchSuggestionAdapter(Context context, ArrayList<Product> products) {
        super(context, null, false);
        mInflater = LayoutInflater.from(context);
        this.mProducts = this.mProducts1 = products;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_search_suggestion, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Product product = mProducts.get(cursor.getPosition());
        String name = product.getName();
        String imgUrl = product.getImgUrl();

        ViewHolder vh = (ViewHolder) view.getTag();

        vh.textView.setText(name);
//        ImageLoader mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
//        mImageLoader.get(imgUrl, ImageLoader.getImageListener(vh.imageView,
//                R.drawable.ic_menu_camera, R.drawable.ic_menu_send));

//        new URLimageDownload(vh.imageView).execute(imgUrl);
        Glide.with(context).load(imgUrl).into(vh.imageView);
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        return new SearchSuggestionCursor(constraint);
    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;

        ViewHolder(View v) {
            textView = (TextView) v.findViewById(R.id.textViewName);
            imageView = (ImageView) v.findViewById(R.id.imageViewProduct);
        }
    }

    private class SearchSuggestionCursor extends AbstractCursor {

        public SearchSuggestionCursor(CharSequence constraint) {
            /*mProducts = new ArrayList<>();
            mProducts.addAll(mProducts1);
            if (!TextUtils.isEmpty(constraint)) {
                String constraintString = constraint.toString().toLowerCase(Locale.ROOT);

                Iterator<Product> iter = mProducts.iterator();
                while (iter.hasNext()) {
                    if (!iter.next().getName().toLowerCase(Locale.ROOT).startsWith(constraintString)) {
                        iter.remove();
                    }
                }
            }*/
        }

        @Override
        public int getCount() {
            return mProducts.size();
        }

        @Override
        public String[] getColumnNames() {
            return mFields;
        }

        @Override
        public String getString(int column) {
            if (column == 1)
                return mProducts.get(mPos).getName();
            return null;
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

    private class URLimageDownload extends AsyncTask<String, Void, Bitmap> {
        ImageView imageDownloadView;

        public URLimageDownload(ImageView imageDownloadView) {
            this.imageDownloadView = imageDownloadView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmapImage = null;

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmapImage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmapImage;
        }

        protected void onPostExecute(Bitmap result) {
            imageDownloadView.setImageBitmap(result);
        }
    }
}
