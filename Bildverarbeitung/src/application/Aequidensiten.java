package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Aequidensiten {
	static Image binary_image = null;
	public static Image display (ImageView view, Image image) {
		binary_image = applyAequidensitenFilter(image);
		view.setImage(binary_image);
		return binary_image;
	}
	
	private static Image applyAequidensitenFilter(Image image) {
		if (image != null && !image.isBackgroundLoading()) {
			int imageWidth = (int) image.getWidth();
			int imageHeight = (int) image.getHeight();
			if (imageWidth > 0 && imageHeight > 0) {
				PixelReader pixelReader = image.getPixelReader();
				WritableImage wimage = new WritableImage(imageWidth, imageHeight);
				PixelWriter pixelWriter = wimage.getPixelWriter();
				if (null != pixelReader && null != pixelWriter) {
					for (int y = 0; y < imageHeight; y++) {
						for (int x = 0; x < imageWidth; x++) {
							Color color = pixelReader.getColor(x, y);
							double grauwert = 0.299 * (color.getRed() * 255) + 0.587 * (color.getGreen() * 255)
									+ 0.114 * (color.getBlue() * 255);
							if (grauwert < 55)
								pixelWriter.setColor(x, y, new Color(30.0 / 255, 30.0 / 255, 30.0 / 255, 1.0));
							else if (55 <= grauwert && grauwert < 155)
								pixelWriter.setColor(x, y, new Color(100.0 / 255, 100.0 / 255, 100.0 / 255, 1.0));
							else
								pixelWriter.setColor(x, y, new Color(255.0 / 255, 255.0 / 255, 255.0 / 255, 1.0));
						}
					}
				}
				return wimage;
			}
			return null;
		}
		return null;
	}
}