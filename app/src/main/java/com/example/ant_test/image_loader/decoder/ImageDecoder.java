package com.example.ant_test.image_loader.decoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.ant_test.image_loader.helper.ImageSize;
import com.example.ant_test.image_loader.helper.SampleType;
import com.example.ant_test.image_loader.util.ImageSizeUtil;
import com.example.ant_test.image_loader.util.IoUtils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

/**
 * @Title: ImageDecoder.java
 * @Package com.example.ant_test.image_loader.decoder
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-28 上午11:15:02
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ImageDecoder implements IImageDecoder{

	@Override
	public Bitmap decode(ImageDecoderInfo info) throws IOException{
		Bitmap decodedBitmap = null;
		//InputStream imageStream = getImageStream(info);
		InputStream imageStream = info.getImageStream();
		if (imageStream != null) {
			byte[] imageBytes = readStream(imageStream);
			try {
				Options opt = calculateDecodeOptions(info,/* imageStream,*/ imageBytes);
				//resetStream(imageStream, info);
				//decodedBitmap = BitmapFactory.decodeStream(imageStream, null, opt);
				decodedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, opt);
			} finally {
				IoUtils.closeStream(imageStream);
			}
		}
		if (decodedBitmap == null) {
			Log.d("", "can not decode image:" + info.getImageUri());
		}
		return decodedBitmap;
	}

	private Options calculateDecodeOptions(ImageDecoderInfo info,/* InputStream inputStream,*/ byte[] imageBytes) {
		ImageSize srcSize = getSrcImageSize(/*inputStream,*/ imageBytes);
		SampleType sampleType = info.getSampleType();
		int scale;
		if (sampleType == SampleType.SAMPLE_TYPE_NONE) {
			scale = 1;
		} else {
			ImageSize targetSize = info.getTargetSize();
			boolean powerOf2 = info.getSampleType() == SampleType.SAMPLE_TYPE_POWER_OF_TWO;
			scale = ImageSizeUtil.computeImageSampleSize(srcSize, targetSize, 
					info.getTargetScaleType(), powerOf2);
		}
		Options decodingOptions = new Options();//TODO 写死，如果有需要，这个options应该是外部传进来的
		decodingOptions.inSampleSize = scale;
		return decodingOptions;
	}
	
	private ImageSize getSrcImageSize(/*InputStream inputStream,*/ byte[] imageBytes) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		//BitmapFactory.decodeStream(inputStream, null, options);
		BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
		return new ImageSize(options.outWidth, options.outHeight);
	}
	
//	protected InputStream resetStream(InputStream imageStream, ImageDecoderInfo decodingInfo) throws IOException {
//		try {
//			imageStream.reset();
//		} catch (IOException e) {
//			Log.d("", "reset stream exception");
//			IoUtils.closeStream(imageStream);
//			imageStream = getImageStream(decodingInfo);
//		}
//		return imageStream;
//	}
//	
//	protected InputStream getImageStream(ImageDecoderInfo decodingInfo) throws IOException {
//		return decodingInfo.getDownloader().getStream(decodingInfo.getImageUri());
//	}
	
	private byte[] readStream(InputStream inputStream) throws IOException {        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();        
        byte[] buffer = new byte[1024];        
        int len = 0;        
        while( (len=inputStream.read(buffer)) != -1){        
            outputStream.write(buffer, 0, len);        
        }        
        outputStream.close();        
        inputStream.close();        
        return outputStream.toByteArray();        
    }  
}
