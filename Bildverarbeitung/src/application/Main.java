package application;

import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Main extends Application {

	Image[] imagesstates = new Image[10];
	ImageView view = null;

	private int index = -1;

	public void start(Stage primaryStage) {
		try {
			Pane root = new Pane();

			view = new ImageView((Image) null);
			view.setLayoutX(50);
			view.setLayoutY(50);
			view.setPreserveRatio(true);
			root.getChildren().add(view);

			MenuBar menu_bar = new MenuBar();

			Menu image_menu = new Menu("Image");
			MenuItem menuitem_loadimage = new MenuItem("Load...");
			menuitem_loadimage.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					FileChooser chooser = new FileChooser();
					chooser.getExtensionFilters().add(new ExtensionFilter("Images Files", "*.jpg", "*.png", "*.bmp"));
					File image_file = chooser.showOpenDialog(primaryStage);
					if (image_file != null) {
						insertIntoStates(new Image(image_file.toURI().toString()), imagesstates);
						view.setImage(imagesstates[index]);
						if (imagesstates[index].getHeight() > 650)
							view.setFitHeight(650);
						else
							view.setFitHeight(imagesstates[index].getHeight());
						if (imagesstates[index].getWidth() > 1100)
							view.setFitWidth(1100);
						else
							view.setFitWidth(imagesstates[index].getWidth());
					}
				}
			});
			image_menu.getItems().add(menuitem_loadimage);

			MenuItem menuitem_undoimage = new MenuItem("Undo   STRG+Z");
			menuitem_undoimage.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if (index - 1 != -1) {
						index--;
						if (imagesstates[index].getHeight() > 650)
							view.setFitHeight(650);
						else
							view.setFitHeight(imagesstates[index].getHeight());
						if (imagesstates[index].getWidth() > 1100)
							view.setFitWidth(1100);
						else
							view.setFitWidth(imagesstates[index].getWidth());
						view.setImage(imagesstates[index]);
					}
				}
			});
			image_menu.getItems().add(menuitem_undoimage);

			MenuItem menuitem_redoimage = new MenuItem("Redo   STRG+Y");
			menuitem_redoimage.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if (imagesstates[index + 1] != null) {
						index++;
						if (imagesstates[index].getHeight() > 650)
							view.setFitHeight(650);
						else
							view.setFitHeight(imagesstates[index].getHeight());
						if (imagesstates[index].getWidth() > 1100)
							view.setFitWidth(1100);
						else
							view.setFitWidth(imagesstates[index].getWidth());
						view.setImage(imagesstates[index]);
					}
				}
			});
			image_menu.getItems().add(menuitem_redoimage);

			MenuItem menuitem_binaryfilter = new MenuItem("Binary");
			menuitem_binaryfilter.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					imagesstates = insertIntoStates(Binariesierung.display(view, imagesstates[index]), imagesstates);
				}
			});
			image_menu.getItems().add(menuitem_binaryfilter);
			menu_bar.getMenus().add(image_menu);

			Menu statistic_menu = new Menu("Statistics");
			MenuItem statistics_item = new MenuItem("Show");
			statistics_item.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent event) {
					Histogramm.display(imagesstates[index]);
				}
			});
			statistic_menu.getItems().add(statistics_item);
			menu_bar.getMenus().add(statistic_menu);

			root.getChildren().add(menu_bar);

			Menu filter_menu = new Menu("Filter");

			MenuItem aequiditen_item = new MenuItem("Aequidensiten");
			aequiditen_item.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent event) {
					imagesstates = insertIntoStates(Aequidensiten.display(view, imagesstates[index]), imagesstates);
				}
			});
			filter_menu.getItems().add(aequiditen_item);

			MenuItem rechteck_item = new MenuItem("Rechtecks Filter");
			rechteck_item.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					imagesstates = insertIntoStates(applyRectangleFilter(imagesstates[index]), imagesstates);
					view.setImage(imagesstates[index]);
				}
			});
			filter_menu.getItems().add(rechteck_item);
			menu_bar.getMenus().add(filter_menu);

			MenuItem gauss_item = new MenuItem("Gauss Filter");
			gauss_item.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					imagesstates = insertIntoStates(applyGaussFilter(imagesstates[index]), imagesstates);
					view.setImage(imagesstates[index]);
				}
			});
			filter_menu.getItems().add(gauss_item);

			MenuItem laplace_item = new MenuItem("Laplace Filter");
			laplace_item.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					imagesstates = insertIntoStates(applyLaplaceFilter(imagesstates[index]), imagesstates);
					view.setImage(imagesstates[index]);
				}
			});
			filter_menu.getItems().add(laplace_item);

			MenuItem sobel_item = new MenuItem("Sobel Filter");
			sobel_item.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					imagesstates = insertIntoStates(applySobelFilter(imagesstates[index]), imagesstates);
					view.setImage(imagesstates[index]);
				}
			});
			filter_menu.getItems().add(sobel_item);

			MenuItem dilitation_item = new MenuItem("Dilitation Filter");
			dilitation_item.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					imagesstates = insertIntoStates(applyDilatationFilter(imagesstates[index]), imagesstates);
					view.setImage(imagesstates[index]);
				}
			});
			filter_menu.getItems().add(dilitation_item);

			MenuItem erosion_item = new MenuItem("Erosion Filter");
			erosion_item.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					imagesstates = insertIntoStates(applyErosionFilter(imagesstates[index]), imagesstates);
					view.setImage(imagesstates[index]);
				}
			});
			filter_menu.getItems().add(erosion_item);

			Scene scene = new Scene(root, 1200, 700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			KeyCombination undo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
			KeyCombination redo = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);

			scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					if (undo.match(event)) {
						if (index - 1 != -1) {
							if (imagesstates[--index].getHeight() > 650)
								view.setFitHeight(650);
							else
								view.setFitHeight(imagesstates[index].getHeight());
							if (imagesstates[index].getWidth() > 1100)
								view.setFitWidth(1100);
							else
								view.setFitWidth(imagesstates[index].getWidth());
							view.setImage(imagesstates[index]);
						}
					} else if (redo.match(event)) {
						if (imagesstates[index + 1] != null) {
							if (imagesstates[++index].getHeight() > 650)
								view.setFitHeight(650);
							else
								view.setFitHeight(imagesstates[index].getHeight());
							if (imagesstates[index].getWidth() > 1100)
								view.setFitWidth(1100);
							else
								view.setFitWidth(imagesstates[index].getWidth());
							view.setImage(imagesstates[index]);
						}
					}
				}
			});
			primaryStage.setTitle("Labor01");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Image applyRectangleFilter(Image image) {
		if (null != image && !image.isBackgroundLoading()) {
			int imageWidth = (int) image.getWidth();
			int imageHeight = (int) image.getHeight();
			if (imageWidth > 0 && imageHeight > 0) {
				PixelReader pixelReader = image.getPixelReader();
				WritableImage wimage = new WritableImage(imageWidth, imageHeight);
				PixelWriter pixelWriter = wimage.getPixelWriter();
				if (null != pixelReader) {
					for (int y = 1; y < imageHeight - 1; y++) {
						for (int x = 1; x < imageWidth - 1; x++) {
							double red = 0;
							double blue = 0;
							double green = 0;
							for (int i = -1; i < 2; i++)
								for (int j = -1; j < 2; j++) {
									Color color = pixelReader.getColor(x + i, y + j);
									red += color.getRed();
									blue += color.getBlue();
									green += color.getGreen();
								}
							red /= 9;
							blue /= 9;
							green /= 9;
							pixelWriter.setColor(x, y, new Color(red, green, blue, 1.0));
						}
					}
					return wimage;
				}
				return null;
			}
			return null;
		}
		return null;
	}

	private int[][] gauss_weights = { { 1, 2, 1 }, { 2, 4, 2 }, { 1, 2, 1 } };

	private Image applyGaussFilter(Image image) {
		if (null != image && !image.isBackgroundLoading()) {
			int imageWidth = (int) image.getWidth();
			int imageHeight = (int) image.getHeight();
			if (imageWidth > 0 && imageHeight > 0) {
				PixelReader pixelReader = image.getPixelReader();
				WritableImage wimage = new WritableImage(imageWidth, imageHeight);
				PixelWriter pixelWriter = wimage.getPixelWriter();
				if (null != pixelReader) {
					for (int y = 1; y < imageHeight - 1; y++) {
						for (int x = 1; x < imageWidth - 1; x++) {
							double red = 0;
							double blue = 0;
							double green = 0;
							for (int i = -1; i < 2; i++)
								for (int j = -1; j < 2; j++) {
									Color color = pixelReader.getColor(x + i, y + j);
									red += gauss_weights[i + 1][j + 1] * color.getRed();
									blue += gauss_weights[i + 1][j + 1] * color.getBlue();
									green += gauss_weights[i + 1][j + 1] * color.getGreen();
								}
							red /= 16;
							blue /= 16;
							green /= 16;
							pixelWriter.setColor(x, y, new Color(red, green, blue, 1.0));
						}
					}
					return wimage;
				}
				return null;
			}
			return null;
		}
		return null;
	}

	private int[][] laplace_weights = { { -1, -1, -1 }, { -1, 8, -1 }, { -1, -1, -1 } };

	private Image applyLaplaceFilter(Image image) {
		if (null != image && !image.isBackgroundLoading()) {
			int imageWidth = (int) image.getWidth();
			int imageHeight = (int) image.getHeight();
			if (imageWidth > 0 && imageHeight > 0) {
				PixelReader pixelReader = image.getPixelReader();
				WritableImage wimage = new WritableImage(imageWidth, imageHeight);
				PixelWriter pixelWriter = wimage.getPixelWriter();
				if (null != pixelReader) {
					for (int y = 1; y < imageHeight - 1; y++) {
						for (int x = 1; x < imageWidth - 1; x++) {
							double red = 0;
							double blue = 0;
							double green = 0;
							for (int i = -1; i < 2; i++)
								for (int j = -1; j < 2; j++) {
									Color color = pixelReader.getColor(x + i, y + j);
									red += laplace_weights[i + 1][j + 1] * color.getRed();
									blue += laplace_weights[i + 1][j + 1] * color.getBlue();
									green += laplace_weights[i + 1][j + 1] * color.getGreen();
								}
							red = Math.min(1, Math.max(0, red));
							green = Math.min(1, Math.max(0, green));
							blue = Math.min(1, Math.max(0, blue));
							pixelWriter.setColor(x, y, new Color(red, green, blue, 1.0));
						}
					}
					return wimage;
				}
				return null;
			}
			return null;
		}
		return null;
	}

	private int[][] sobel_weights = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };

	private Image applySobelFilter(Image image) {
		if (null != image && !image.isBackgroundLoading()) {
			int imageWidth = (int) image.getWidth();
			int imageHeight = (int) image.getHeight();
			if (imageWidth > 0 && imageHeight > 0) {
				PixelReader pixelReader = image.getPixelReader();
				WritableImage wimage = new WritableImage(imageWidth, imageHeight);
				PixelWriter pixelWriter = wimage.getPixelWriter();
				if (null != pixelReader) {
					// ignore edge of the image
					for (int y = 1; y < imageHeight - 1; y++) {
						for (int x = 1; x < imageWidth - 1; x++) {
							double red = 0;
							double blue = 0;
							double green = 0;
							for (int i = -1; i < 2; i++)
								for (int j = -1; j < 2; j++) {
									Color color = pixelReader.getColor(x + i, y + j);
									red += sobel_weights[i + 1][j + 1] * color.getRed();
									blue += sobel_weights[i + 1][j + 1] * color.getBlue();
									green += sobel_weights[i + 1][j + 1] * color.getGreen();
								}
							red = Math.min(1, Math.max(0, red));
							green = Math.min(1, Math.max(0, green));
							blue = Math.min(1, Math.max(0, blue));
							pixelWriter.setColor(x, y, new Color(red, green, blue, 1.0));
						}
					}
					return wimage;
				}
				return null;
			}
			return null;
		}
		return null;
	}

	int[] dilatation_mask_x = { 0, 1, -1, 0, 1, -1, 0, 1 };
	int[] dilatation_mask_y = { -1, -1, 0, 0, 0, 1, 1, 1 };

	private Image applyDilatationFilter(Image image) {
		if (null != image && !image.isBackgroundLoading()) {
			int imageWidth = (int) image.getWidth();
			int imageHeight = (int) image.getHeight();
			if (imageWidth > 0 && imageHeight > 0) {
				PixelReader pixelReader = image.getPixelReader();
				WritableImage wimage = new WritableImage(imageWidth, imageHeight);
				PixelWriter pixelWriter = wimage.getPixelWriter();
				if (null != pixelReader) {
					for (int y = 1; y < imageHeight - 1; y++) {
						for (int x = 1; x < imageWidth - 1; x++) {
							Color final_color = pixelReader.getColor(x - 1, y - 1);
							Color color = final_color;
							double max_grauwert = 0.299 * (color.getRed() * 255) + 0.587 * (color.getGreen() * 255)
									+ 0.114 * (color.getBlue() * 255);
							for (int i = 0; i < dilatation_mask_x.length; i++)
								for (int j = 0; j < dilatation_mask_y.length; j++) {
									color = pixelReader.getColor(x + dilatation_mask_x[i], y + dilatation_mask_y[j]);
									if (max_grauwert < 0.299 * (color.getRed() * 255) + 0.587 * (color.getGreen() * 255)
											+ 0.114 * (color.getBlue() * 255)) {
										max_grauwert = 0.299 * (color.getRed() * 255) + 0.587 * (color.getGreen() * 255)
												+ 0.114 * (color.getBlue() * 255);
										final_color = color;
									}
								}
							pixelWriter.setColor(x, y, final_color);
						}
					}
					return wimage;
				}
				return null;
			}
			return null;
		}
		return null;
	}

	int[] erosion_mask_x = { 0, 1, -1, 0, 1, -1, 0, 1 };
	int[] erosion_mask_y = { -1, -1, 0, 0, 0, 1, 1, 1 };

	private Image applyErosionFilter(Image image) {
		if (null != image && !image.isBackgroundLoading()) {
			int imageWidth = (int) image.getWidth();
			int imageHeight = (int) image.getHeight();
			if (imageWidth > 0 && imageHeight > 0) {
				PixelReader pixelReader = image.getPixelReader();
				WritableImage wimage = new WritableImage(imageWidth, imageHeight);
				PixelWriter pixelWriter = wimage.getPixelWriter();
				if (null != pixelReader) {
					for (int y = 1; y < imageHeight - 1; y++) {
						for (int x = 1; x < imageWidth - 1; x++) {
							Color final_color = pixelReader.getColor(x - 1, y - 1);
							Color color = final_color;
							double min_grauwert = 0.299 * (color.getRed() * 255) + 0.587 * (color.getGreen() * 255)
									+ 0.114 * (color.getBlue() * 255);
							for (int i = 0; i < erosion_mask_x.length; i++)
								for (int j = 0; j < erosion_mask_y.length; j++) {
									color = pixelReader.getColor(x + dilatation_mask_x[i], y + dilatation_mask_y[j]);
									if (min_grauwert >= 0.299 * (color.getRed() * 255)
											+ 0.587 * (color.getGreen() * 255) + 0.114 * (color.getBlue() * 255)) {
										min_grauwert = 0.299 * (color.getRed() * 255) + 0.587 * (color.getGreen() * 255)
												+ 0.114 * (color.getBlue() * 255);
										final_color = color;
									}
								}
							pixelWriter.setColor(x, y, final_color);
						}
					}
					return wimage;
				}
				return null;
			}
			return null;
		}
		return null;
	}

	private Image[] insertIntoStates(Image image, Image[] states) {
		Image[] imagestates = states;
		if (index >= imagestates.length - 1) {
			Image[] temp_array = new Image[imagestates.length];
			for (int i = 1; i < imagestates.length; i++)
				temp_array[i - 1] = imagestates[i];
			temp_array[temp_array.length - 1] = image;
			imagestates = temp_array;
		} else {
			imagestates[++index] = image;
			for (int i = index + 1; i < imagestates.length; i++)
				imagestates[i] = null;
		}
		return imagestates;
	}

	int[][] mask_general_coords = { { -1, -1 }, { 0, -1 }, { 1, -1 }, { -1, 0 }, { 1, 0 }, { -1, 1 }, { 0, 1 },
			{ 1, 1 } };

	public static void main(String[] args) {
		launch(args);
	}
}
