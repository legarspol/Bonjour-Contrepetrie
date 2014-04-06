package com.uliamar.bonjourcontrepetrie;

import android.content.Context;

import com.orm.SugarRecord;



public class Contre extends SugarRecord<Contre> {

	private String title;
	private String link;
	private String img;
	private int date;
	
	
	public Contre(Context arg0) {
		super(arg0);
	}

	public Contre(Context context, String title, String link, String img, int date) {
		super(context);
		this.title = title;
		this.link = link;
		this.img = img;
		this.date = date;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}


	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}


	/**
	 * @return the img
	 */
	public String getImg() {
		return img;
	}


	/**
	 * @param img the img to set
	 */
	public void setImg(String img) {
		this.img = img;
	}


	/**
	 * @return the date
	 */
	public int getDate() {
		return date;
	}


	/**
	 * @param date the date to set
	 */
	public void setDate(int date) {
		this.date = date;
	}

}
