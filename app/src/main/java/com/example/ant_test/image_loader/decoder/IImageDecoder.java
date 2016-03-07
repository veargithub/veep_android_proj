package com.example.ant_test.image_loader.decoder;

import java.io.IOException;

import android.graphics.Bitmap;

/**
 * @Title: IImageDecoder.java
 * @Package com.example.ant_test.image_loader.decoder
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-18 下午5:26:21
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public interface IImageDecoder {
	Bitmap decode(ImageDecoderInfo info) throws IOException;
}
