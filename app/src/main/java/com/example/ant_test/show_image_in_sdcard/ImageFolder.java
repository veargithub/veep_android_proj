package com.example.ant_test.show_image_in_sdcard;

import java.util.ArrayList;
import java.util.List;

public class ImageFolder {
	public String folderName;
	public String firstImagePath;
	public int totalCount;
	public int selectedCount;
	public List<String> images;
	
	public ImageFolder() {
		this.folderName = null;
		this.firstImagePath = null;
		this.totalCount = 0;
		this.selectedCount = 0;
		this.images = new ArrayList<String>();
	}
}
