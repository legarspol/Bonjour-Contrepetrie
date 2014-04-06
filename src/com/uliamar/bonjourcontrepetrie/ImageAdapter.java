package com.uliamar.bonjourcontrepetrie;


import java.util.ArrayList;
import java.util.List;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private List<Contre> contres;

	
	
	public void update(List<Contre> contres) {
		this.contres = contres;
		this.notifyDataSetChanged();
	}
	
	public ImageAdapter(Context context, List<Contre> contres) {
		mContext = context;
		this.contres = contres;
	}

	public int getCount() {
		return contres.size();
	}

	
	public Contre getItem(int position) {
		return contres.get(position);
	}

	
	public long getItemId(int arg0) {
		return contres.get(arg0).getId();
	}

	
	private int getPixels(int dipValue){
        Resources r = mContext.getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, 
        r.getDisplayMetrics());
        return px;
}
	
	public View getView(int position, View convertView, ViewGroup arg2)  {
		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(-1, getPixels(150)));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			//imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}
		String imageToRetrieve;
		imageToRetrieve = MainActivity.BASE_URL + getItem(position).getImg();		
		Picasso.with(mContext).load(imageToRetrieve)
				.placeholder(R.drawable.l).error(R.drawable.ic_bug).into(imageView);

		return imageView;
	}

	
}
