package com.example.ant_test.image_loader.decoder;

import java.io.InputStream;

import com.example.ant_test.image_loader.downloader.IImageDownloader;
import com.example.ant_test.image_loader.helper.ImageSize;
import com.example.ant_test.image_loader.helper.SampleType;
import com.example.ant_test.image_loader.helper.ViewScaleType;

/**
 * @Title: ImageDecoderInfo.java
 * @Package com.example.ant_test.image_loader.decoder
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-28 上午11:14:33
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ImageDecoderInfo {
	//private final String imageKey;
//	private final IImageDownloader downloader;
	private final String imageUri;
	private final InputStream imageStream;
	private final ImageSize targetSize;
	private final ViewScaleType targetScaleType;//crop or fit inside
	private final SampleType sampleType;
	
	public ImageDecoderInfo(/*String imageKey, IImageDownloader downloader,*/ String imageUri, 
			InputStream imageStream, ImageSize targetScale, ViewScaleType targetScaleType, SampleType sampleType) {
		super();
//		this.imageKey = imageKey;
//		this.downloader = downloader;
		this.imageUri = imageUri;
		this.imageStream = imageStream;
		this.targetSize = targetScale;
		this.targetScaleType = targetScaleType;
		this.sampleType = sampleType;
		
	}

	public InputStream getImageStream() {
		return imageStream;
	}

	public ImageSize getTargetSize() {
		return targetSize;
	}

	public ViewScaleType getTargetScaleType() {
		return targetScaleType;
	}

	public SampleType getSampleType() {
		return sampleType;
	}

//	public String getImageKey() {
//		return imageKey;
//	}

//	public IImageDownloader getDownloader() {
//		return downloader;
//	}
//
	public String getImageUri() {
		return imageUri;
	}
	
	
}
