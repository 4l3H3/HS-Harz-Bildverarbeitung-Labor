package application;

import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Binariesierung {
	static Image binary_image = null;
	public static Image display (ImageView view, Image image) {
		Stage stage = new Stage();
		Pane root = new Pane();
		Slider slider = new Slider();
		slider.setMin(0);
		slider.setMax(255);
		slider.setValue(122);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setBlockIncrement(1);
		slider.valueProperty().addListener((observable, oldValue, newValue) -> {
			binary_image = binaryFilter(image, (int)slider.getValue());
			view.setImage(binary_image);
		});
		slider.setMinHeight(50);
		slider.setMinWidth(300);
		root.getChildren().add(slider);
		Scene scene = new Scene(root,300,100);
		stage.setScene(scene);
		stage.showAndWait();
		return binary_image;
	}
	
	private static Image binaryFilter(Image image, int schwellenwert) {
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
							if (grauwert <= schwellenwert)
								pixelWriter.setColor(x, y, Color.BLACK);
							else
								pixelWriter.setColor(x, y, Color.WHITE);
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
