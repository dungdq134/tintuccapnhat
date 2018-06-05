package com.dungdq.tintuccapnhat.model;

public class FavoriteList {

	// private variables
	String id, title, summary, thumbnail, author, url;

	public FavoriteList() {

	}

	public FavoriteList(String id, String title, String summary,
			String thumbnail, String author, String url) {
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.thumbnail = thumbnail;
		this.author = author;
		this.url = url;
	}

	public FavoriteList(String title, String summary, String thumbnail,
			String author, String url) {
		this.title = title;
		this.summary = summary;
		this.thumbnail = thumbnail;
		this.author = author;
		this.url = url;
	}

	public String getID() {
		return this.id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getThumbnail() {
		return this.thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
