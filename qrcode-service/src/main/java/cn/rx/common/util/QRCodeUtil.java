package cn.rx.common.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

/**
 * 二维码编码/解码工具类
 * http://blog.csdn.net/johnsonvily/article/details/11212619
 */

public class QRCodeUtil {

	private static Logger logger = LoggerFactory.getLogger(QRCodeUtil.class);
	
	private int size;
	
	public QRCodeUtil(int size) {
		this.size = size;
	}

	/**
	 * 生成二维码图片
	 * @param content
	 * @param imgPath
	 */
	public boolean encoderQRCode(String content, String imgPath) {
		logger.info("imgPath: " + imgPath);

		boolean flag = false;
		try {
			File file = new File(imgPath);
			QRCodeWriter writer = new QRCodeWriter();
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			// 二维码容错率，分四个等级：H、L 、M、 Q
	        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);  
	        // 设置二维码空白边框的大小 1-4，1是最小 4是默认的国标
	        hints.put(EncodeHintType.MARGIN, 2);
	        // 指定编码格式  
	        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			
			BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints);
//			matrix.setRegion(30, 30, 30, 30);
			MatrixToImageWriter.writeToFile(matrix, "png", file);
			flag = true;
		} catch (Exception e) {
			logger.error("", e);
		}
		return flag;
	}
	
	/**
	 * 二维码解码
	 * @param imgPath
	 * @return String
	 */
	public String decoderQRCode(String imgPath) {
		File imageFile = new File(imgPath);
		BufferedImage bufImg = null;
		String decodedData = null;
		try {
			QRCodeReader reader = new QRCodeReader();
			bufImg = ImageIO.read(imageFile);
			LuminanceSource source = new BufferedImageLuminanceSource(bufImg);
			Binarizer binarizer = new HybridBinarizer(source );
			BinaryBitmap imageBinaryBitmap = new BinaryBitmap(binarizer);
			Result result = reader.decode(imageBinaryBitmap);
			decodedData = result.toString();
		} catch (Exception e) {
			logger.error("", e);
		}
		return decodedData;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		QRCodeUtil qrCoder = new QRCodeUtil(300);
		// encode
		String content = "http://zhaoxianling.com/m/camp/y4PPdfa35ocfC2UXWURX";
		String imgPath = "E:/qr_zxing.png";
		long start = System.currentTimeMillis();
		System.out.println("========encoder start...");
		System.out.println("========content length: " + content.length());
		qrCoder.encoderQRCode(content, imgPath);
		System.out.println("========encoder success!!!");
		long end = System.currentTimeMillis();
		System.out.println("花费时间：" + (end - start) + " ms");
		
		//decode
		start = System.currentTimeMillis();
		System.out.println("========decoder start...");
		String decoderContent = qrCoder.decoderQRCode(imgPath);
		System.out.println("解析结果：" + decoderContent);
		System.out.println("========decoder success!!!");
		end = System.currentTimeMillis();
		System.out.println("花费时间：" + (end - start) + " ms");
	}

}
