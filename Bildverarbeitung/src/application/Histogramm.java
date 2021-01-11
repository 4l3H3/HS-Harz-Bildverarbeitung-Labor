package application;

import java.util.Arrays;

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Histogramm {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void display(Image image) {
		Stage primaryStage = new Stage();
		Pane root = new Pane();

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
		barChart.setCategoryGap(0);
		barChart.setBarGap(0.5);
		int [] values = ermittleGrauwerte(image);
		xAxis.setLabel("Value");
		yAxis.setLabel("Anzahl Pixel");

		XYChart.Series series1 = new XYChart.Series();
		series1.setName("Grau Werte");

		for (int i = 0; i < 256; i++) {
			series1.getData().add(new XYChart.Data<String, Integer>(Integer.toString(i), values[i]));
		}

		barChart.getData().addAll(series1);
		root.getChildren().addAll(barChart);

		Label max_gray_label = new Label("Max. Grauwert: " + getMaxGreyValue(values));
		max_gray_label.setLayoutX(50);
		max_gray_label.setLayoutY(400);
		root.getChildren().add(max_gray_label);

		Label min_gray_label = new Label("Min. Grauwert: " + getMinGreyValue(values));
		min_gray_label.setLayoutX(50);
		min_gray_label.setLayoutY(420);
		root.getChildren().add(min_gray_label);

		double mittelwert = getMidGreyValue(values);
		Label mid_gray_label = new Label("Mittlerer Grauwert: " + mittelwert);
		mid_gray_label.setLayoutX(50);
		mid_gray_label.setLayoutY(440);
		root.getChildren().add(mid_gray_label);
		
		double varianz = varianz(values, mittelwert);
		Label varianz_label = new Label("Varianz: " + varianz);
		varianz_label.setLayoutX(300);
		varianz_label.setLayoutY(400);
		root.getChildren().add(varianz_label);
		
		
		Label standartabweichung_label = new Label("Standartabweichung: " + Math.sqrt(varianz));
		standartabweichung_label.setLayoutX(300);
		standartabweichung_label.setLayoutY(420);
		root.getChildren().add(standartabweichung_label);

		Scene scene = new Scene(root, 600, 500);
		primaryStage.setTitle("Histogramm");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	private static int getMaxGreyValue(int[] grey_values) {
		int max_grey = grey_values.length - 1;
		for (int i = grey_values.length - 1; grey_values[i] == 0 && i > 1; i--)
			max_grey = i - 1;
		return max_grey;
	}

	private static int getMinGreyValue(int[] grey_values) {
		int min_grey = 0;
		for (int i = 0; grey_values[i] == 0 && i < grey_values.length - 1; i++)
			min_grey = i + 1;
		return min_grey;
	}

	private static double getMidGreyValue(int[] grey_values) {
		double ergebnis = 0;
		int anzahl = 0;
		for (int i = 0; i < grey_values.length; i++) {
			ergebnis += grey_values[i] * i;
			anzahl += grey_values[i];
		}
		return ergebnis / anzahl;
	}
	
	private static double varianz (int [] grey_values, double mittelwert) {
		double ergebnis = 0;
		int anzahl = 0;
		for (int i = 0; i < grey_values.length; i++) {
			ergebnis += grey_values[i] * Math.pow((i - mittelwert), 2);
			anzahl += grey_values[i];
		}
		return ergebnis / anzahl;
	}
	
	private static int[] ermittleGrauwerte(Image image) {
		int [] histogramm = new int[256];
		Arrays.fill(histogramm, 0);
		if (null != image && !image.isBackgroundLoading()) {
			int imageWidth = (int) image.getWidth();
			int imageHeight = (int) image.getHeight();
			if (imageWidth > 0 && imageHeight > 0) {
				PixelReader pixelReader = image.getPixelReader();
				if (null != pixelReader) {
					for (int y = 0; y < imageHeight; y++) {
						for (int x = 0; x < imageWidth; x++) {
							Color color = pixelReader.getColor(x, y);
							double grauwert = 0.299 * (color.getRed() * 255) + 0.587 * (color.getGreen() * 255)
									+ 0.114 * (color.getBlue() * 255);
							histogramm[(int) grauwert] += 1;
						}
					}
				}
			}
		}
		return histogramm;
	}
}
