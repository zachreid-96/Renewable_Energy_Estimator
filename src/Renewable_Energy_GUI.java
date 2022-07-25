
import java.util.*;
import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Renewable_Energy_GUI extends Application {

	static boolean state = true;

	static ArrayList<CheckBox> windTurbines = new ArrayList<CheckBox>();
	static ArrayList<CheckBox> solarFarmTypes = new ArrayList<CheckBox>();
	static ArrayList<CheckBox> windFarmTypes = new ArrayList<CheckBox>();
	static ArrayList<CheckBox> solarPanels = new ArrayList<CheckBox>();

	static String wattChoice = " kW";
	
	static boolean turbinesNearSolarFarms = true;
	
	static int height = 500;
	static int width = 1000;
	
	//static String background_Color = "-fx-background-color: ";
	static String text_Font = "-fx-font: normal 13px ";
	//static String text_Color = "-fx-text-fill: ";

	public static void main(String[] args) {
		
		if (System.getProperty("os.name").contains("Linux")) {
			height = 500;
			width = 1000;
			//background_Color += "#a0a0a0";
			text_Font += "'Times New Roman'";
			//text_Color += "#000000";
			
		} else if (System.getProperty("os.name").contains("Windows")) {
			height = 500;
			width = 1000;
			//background_Color += "#a0a0a0";
			text_Font += "'Tahoma'";
			//text_Color += "#000000";
			
		}
		
		launch(args);

	} // End main

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Renewable Energy Estimator");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(25);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		// Solar Panel Farm Layout Options

		Text solarFarmLayouts = new Text("Solar Farm Layouts:");
		solarFarmLayouts.setStyle(text_Font);
		//solarFarmLayouts.setStyle(text_Color);

		CheckBox microFarm = new CheckBox("Micro Farm");
		microFarm.setStyle(text_Font);
		//microFarm.setStyle(text_Color);
		
		CheckBox industrialFarm = new CheckBox("Industrial Farm");
		industrialFarm.setStyle(text_Font);

		VBox solarFarmTypesVBOX = new VBox(10, microFarm, industrialFarm);

		VBox.setMargin(microFarm, new Insets(0, 0, 0, 10));
		VBox.setMargin(industrialFarm, new Insets(0, 0, 0, 10));

		// Creating Peak Sun Hours Slider

		Text sliderTextPSH = new Text("Average Peak Sun Hours\n(recommended > 3)");
		sliderTextPSH.setStyle(text_Font);

		Slider sliderPSH = new Slider(1, 6, 1);
		sliderPSH.setShowTickLabels(true);
		sliderPSH.setMajorTickUnit(1);
		sliderPSH.setMinorTickCount(0);
		sliderPSH.setBlockIncrement(1);
		sliderPSH.setSnapToTicks(true);
		sliderPSH.setValue(3);

		// Creating Solar Panel Options

		Text solarPanelOptions = new Text("Solar Panel Types:");
		solarPanelOptions.setStyle(text_Font);;

		CheckBox monoPERC_60x120_345 = new CheckBox("Mono PERC 60 Cell - 345W");
		monoPERC_60x120_345.setStyle(text_Font);
		
		CheckBox monoPERC_66x132_390 = new CheckBox("Mono PERC 66 Cell - 390W");
		monoPERC_66x132_390.setStyle(text_Font);
		
		CheckBox monoPERC_72x144_415 = new CheckBox("Mono PERC 72 Cell - 415W");
		monoPERC_72x144_415.setStyle(text_Font);

		VBox solarPanelVBOX = new VBox(10, monoPERC_60x120_345, monoPERC_66x132_390, monoPERC_72x144_415);

		VBox.setMargin(monoPERC_60x120_345, new Insets(0, 0, 0, 10));
		VBox.setMargin(monoPERC_66x132_390, new Insets(0, 0, 0, 10));
		VBox.setMargin(monoPERC_72x144_415, new Insets(0, 0, 0, 10));
		
		// Creating Peak Sun Hours Slider

		Text sliderTextWind = new Text("Average Wind Speed (m/s)\n(recommended > 3)");
		sliderTextWind.setStyle(text_Font);

		Slider sliderWindSpeed = new Slider(0, 7, 1);
		sliderWindSpeed.setShowTickLabels(true);
		sliderWindSpeed.setMajorTickUnit(1);
		sliderWindSpeed.setMinorTickCount(0);
		sliderWindSpeed.setBlockIncrement(1);
		sliderWindSpeed.setSnapToTicks(true);
		sliderWindSpeed.setValue(3);
		
		// Creating Wind Farm Options
		
		Text windFarmLayouts = new Text("Wind Farm Layouts:");
		windFarmLayouts.setStyle(text_Font);
		//solarFarmLayouts.setStyle(text_Color);

		CheckBox smallWindFarm = new CheckBox("Small (10-150 Turbines)");
		smallWindFarm.setStyle(text_Font);
		//microFarm.setStyle(text_Color);
		
		CheckBox largeWindFarm = new CheckBox("Large (100-250 Turbines)");
		largeWindFarm.setStyle(text_Font);

		VBox windFarmTypesVBOX = new VBox(10, smallWindFarm, largeWindFarm);

		VBox.setMargin(smallWindFarm, new Insets(0, 0, 0, 10));
		VBox.setMargin(largeWindFarm, new Insets(0, 0, 0, 10));
		
		// Creating Land Wind Turbine Options

		Text landWindTurbineOptions = new Text("Land Wind Turbine Options:");
		landWindTurbineOptions.setStyle(text_Font);

		CheckBox windTurbine_265 = new CheckBox("Wind Turbine (265 ft.)");
		windTurbine_265.setStyle(text_Font);
		
		CheckBox windTurbine_295 = new CheckBox("Wind Turbine (295 ft.)");
		windTurbine_295.setStyle(text_Font);

		VBox landTurbineVBOX = new VBox(10, windTurbine_265, windTurbine_295);

		VBox.setMargin(windTurbine_265, new Insets(0, 0, 0, 10));
		VBox.setMargin(windTurbine_295, new Insets(0, 0, 0, 10));

		// Creating OffShore Wind Turbine Options

		Text offShoreWindTurbineOptions = new Text("Offshore Wind Turbine Options:");
		offShoreWindTurbineOptions.setStyle(text_Font);;

		CheckBox windTurbine_328 = new CheckBox("Wind Turbine (328 ft.)");
		windTurbine_328.setStyle(text_Font);
		windTurbine_328.setDisable(true);

		VBox seaTurbineCheckBoxes = new VBox(10, windTurbine_328);
		VBox.setMargin(windTurbine_328, new Insets(0, 0, 0, 10));

		// Creating Desired KiloWatt TextField Entry
		
		Text desiredKW = new Text("Desired KiloWatt(s):");
		desiredKW.setStyle(text_Font);;
		
		TextField kiloWatt_USER = new TextField("0");
		Label kiloWatt_Entered = new Label();
		
		Label defaultEntry = new Label("kiloWatts");
		
		kiloWatt_Entered.setText(getConversion(kiloWatt_USER.getText()));
		
		EventHandler<ActionEvent> updateEnteredWatts = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				kiloWatt_Entered.setText(getConversion(kiloWatt_USER.getText()));
				// kiloWatt_Entered.setText(kiloWatt_USER.getText());
			}
		};

		kiloWatt_USER.setOnAction(updateEnteredWatts);

		// Creating KiloWatt DropDown Selection

		ComboBox<String> wattageChoice = new ComboBox<String>();

		wattageChoice.getItems().add("Watts");
		wattageChoice.getItems().add("kiloWatts");
		wattageChoice.getItems().add("MegaWatts");
		wattageChoice.getItems().add("GigaWatts");

		wattageChoice.setValue("kiloWatts");
		
		wattageChoice.setStyle(text_Font);

		// Adding Listeners to Wind Turbine CheckBoxes

		wattageChoice.setOnAction((t) -> {
			
			int selectedIndex = wattageChoice.getSelectionModel().getSelectedIndex();
			
			switch (selectedIndex) {
			case 0:
				wattChoice = " W";
				break;
			case 1:
				wattChoice = " kW";
				break;
			case 2:
				wattChoice = " MW";
				break;
			case 3:
				wattChoice = " GW";
				break;
			}
			
			kiloWatt_Entered.setText(getConversion(kiloWatt_USER.getText()));
			
		});
		
		// Adding Listeners to Solar Farm Layout CheckBoxes

		smallWindFarm.selectedProperty()
				.addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
					alterWindFarmTypes(smallWindFarm);
				});
		largeWindFarm.selectedProperty()
				.addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
					alterWindFarmTypes(largeWindFarm);
				});
		
		// Adding Listeners to Wind Turbine CheckBoxes

		windTurbine_265.selectedProperty()
				.addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
					alterWindTurbines(windTurbine_265);
				});
		windTurbine_295.selectedProperty()
				.addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
					alterWindTurbines(windTurbine_295);
				});
		windTurbine_328.selectedProperty()
				.addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
					alterWindTurbines(windTurbine_328);
				});

		// Adding Listeners to Solar Farm Layout CheckBoxes

		microFarm.selectedProperty()
				.addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
					alterSolarFarmTypes(microFarm);
				});
		industrialFarm.selectedProperty()
				.addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
					alterSolarFarmTypes(industrialFarm);
				});

		// Adding Listeners to Solar Panel CheckBoxes

		monoPERC_60x120_345.selectedProperty()
				.addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
					alterSolarPanels(monoPERC_60x120_345);
				});
		monoPERC_66x132_390.selectedProperty()
				.addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
					alterSolarPanels(monoPERC_66x132_390);
				});
		monoPERC_72x144_415.selectedProperty()
				.addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
					alterSolarPanels(monoPERC_72x144_415);
				});
		
		// Creating Number of Tests Slider

		Text sliderTestNumberText = new Text("Number of Tests to Run: 1");
		sliderTestNumberText.setStyle(text_Font);

		Slider sliderTestNumber = new Slider(1, 100, 1);
		sliderTestNumber.setShowTickLabels(true);
		sliderTestNumber.setMajorTickUnit(100);
		//sliderTestNumber.setMinorTickCount(5);
		sliderTestNumber.setBlockIncrement(1);
		sliderTestNumber.setSnapToTicks(false);
		sliderTestNumber.setValue(1);
		
		sliderTestNumber.valueProperty().addListener((ov, oldValue, newValue) -> {
			sliderTestNumberText.setText("Number of Tests to Run: " + (int) sliderTestNumber.getValue());
		});
		
		Button runTest = new Button("Run");
		runTest.setStyle(text_Font);

		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				
				long tempHours = 0L;
				
				try {
					tempHours = Long.parseLong(kiloWatt_USER.getText());
				} catch (NumberFormatException z) {
					System.out.println(z.getMessage());
				} finally {
					// Default Value (used Hours from 2021)
					// Only if something goes wrong
					if (tempHours == 0L) {
						tempHours = 4120000000000L;
					}
				}
				Renewable_Energy.setHours(tempHours);
				checkTest(sliderPSH.getValue(), sliderWindSpeed.getValue(), (int) sliderTestNumber.getValue());
			}
		};

		runTest.setOnAction(event);
		
		// Adding Solar Farm Types to grid

		grid.add(solarFarmLayouts, 0, 0, 2, 1);
		grid.add(solarFarmTypesVBOX, 0, 1);

		// Adding Solar Panel Options to grid

		grid.add(solarPanelOptions, 0, 2, 2, 1);
		grid.add(solarPanelVBOX, 0, 3);

		// Adding Peak Solar Hours (PSH) Slider to grid

		grid.add(sliderTextPSH, 0, 4);
		grid.add(sliderPSH, 0, 5);

		// Adding Wind Speed Slider to grid

		grid.add(sliderTextWind, 2, 4);
		grid.add(sliderWindSpeed, 2, 5, 2, 1);
		
		// Adding Wind Farm Layouts to grid
		
		grid.add(windFarmLayouts, 2, 0, 2, 1);
		grid.add(windFarmTypesVBOX, 2, 1);

		// Adding Land Wind Turbine Options to grid

		grid.add(landWindTurbineOptions, 2, 2, 2, 1);
		grid.add(landTurbineVBOX, 2, 3);

		// Adding OffShore Wind Turbine Options to grid (CURRENTLY UNIMPLEMENTED)
		
		//grid.add(offShoreWindTurbineOptions, 2, 2, 2, 1);
		//grid.add(seaTurbineChechBoxes, 2, 3);
		
		// Adding kiloWatt_USER to grid
		
		grid.add(desiredKW, 6, 0, 2, 1);
		grid.add(kiloWatt_USER, 6, 1);
		grid.add(defaultEntry, 7, 1);

		// Adding Watt Choice Drop Down Box to grid
		
		grid.add(kiloWatt_Entered, 6, 2);
		grid.add(wattageChoice, 7, 2);
		
		// Adding Test Number Slider to grid

		grid.add(sliderTestNumberText, 6, 3, 2, 1);
		grid.add(sliderTestNumber, 6, 4, 2, 1);

		// Adding Run Button to grid

		grid.add(runTest, 6, 5);
		
		// Styling Grid and Other Components
		
		//grid.setStyle(background_Color);
		
		// Creating Scene

		Scene scene = new Scene(grid, width, height);
		primaryStage.setScene(scene);
		primaryStage.show();

	} // End start

	private static String getConversion(String s) {
		
		double watts = Double.parseDouble(s);
		
		switch (wattChoice) {
		case " W":
			watts = watts * 1000;
			break;
		case " MW":
			watts = watts / 1000;
			break;
		case " GW":
			watts = watts / 1000000;
			break;
		}
		
		String wattString = String.format("%,.2f", watts) + wattChoice;

		return wattString;
	} // End getConversion

	// Adds/Removes Wind Turbines from Active List

	private static void alterWindTurbines(CheckBox box) {

		if (box.isSelected()) {
			//System.out.println("HERE");
			windTurbines.add(box);
		} else {
			windTurbines.remove(windTurbines.indexOf(box));
		}
	} // End alterWindTurbines

	// Adds/Removes Solar Panel Options from Active List

	private static void alterSolarPanels(CheckBox box) {

		if (box.isSelected()) {
			solarPanels.add(box);
		} else {
			solarPanels.remove(solarPanels.indexOf(box));
		}
	} // End alterSolarPanels

	// Adds/Removes Solar Farm Layouts from Active List

	private static void alterSolarFarmTypes(CheckBox box) {

		if (box.isSelected()) {
			solarFarmTypes.add(box);
		} else {
			solarFarmTypes.remove(solarFarmTypes.indexOf(box));
		}
	} // End alterFarmTypes
	
	private static void alterWindFarmTypes(CheckBox box) {

		if (box.isSelected()) {
			windFarmTypes.add(box);
		} else {
			windFarmTypes.remove(windFarmTypes.indexOf(box));
		}
	} // End alterFarmTypes

	private static void checkTest(double valuePSH, double valueWindSpeed, int numTests) {
		
		if ((solarFarmTypes.size() == 0 && solarPanels.size() > 0)
				|| (solarFarmTypes.size() > 0 && solarPanels.size() == 0)) {
			
			Alert alert_MismatchedInputs = new Alert(AlertType.ERROR, "Must include Solar Panel Farm and Solar Panel Type!", ButtonType.OK);
			alert_MismatchedInputs.setTitle("Mismatched Inputs!");
			alert_MismatchedInputs.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert_MismatchedInputs.show();
			
		} else if ((windFarmTypes.size() == 0 && windTurbines.size() > 0)
				|| (windFarmTypes.size() > 0 && windTurbines.size() == 0)) {
			
			Alert alert_MismatchedInputs = new Alert(AlertType.ERROR, "Must include Wind Farm and Turbine Type!", ButtonType.OK);
			alert_MismatchedInputs.setTitle("Mismatched Inputs!");
			alert_MismatchedInputs.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert_MismatchedInputs.show();
			
		} else if ((solarFarmTypes.size() > 0 && solarPanels.size() > 0)
				&& (windFarmTypes.size() > 0 && windTurbines.size() > 0)) {
			
			System.out.println("HERE - logic 1");
			runTest(valuePSH, valueWindSpeed, numTests);
			
		} else if ((solarFarmTypes.size() > 0 && solarPanels.size() > 0)
				|| (windFarmTypes.size() > 0 && windTurbines.size() > 0)) {
			
			System.out.println("HERE - logic 2");
			runTest(valuePSH, valueWindSpeed, numTests);
			
		} // End Run If/Else Block
	} // End checkTest
	
	private static void runTest(double valuePSH, double valueWindSpeed, int numTests) {
		
		for (CheckBox box : windFarmTypes) {
			Renewable_Energy.initializeEnergySources(box.toString().split("'")[1]);
			//System.out.println(box.toString().split("'")[1]);
		}

		for (CheckBox box : windTurbines) {
			Renewable_Energy.initializeEnergySources(box.toString().split("'")[1]);
			//System.out.println(box.toString().split("'")[1]);
		}
		
		for (CheckBox box : solarFarmTypes) {
			Renewable_Energy.initializeEnergySources(box.toString().split("'")[1]);
			//System.out.println(box.toString().split("'")[1]);
		}

		for (CheckBox box : solarPanels) {
			Renewable_Energy.initializeEnergySources(box.toString().split("'")[1]);
			//System.out.println(box.toString().split("'")[1]);
		}
		
		String message = "DONE";
		
		Renewable_Energy.cleanDirectory();
		
		// Running ONLY ONE (1) Test
		
		if (numTests == 1) {
			
			Renewable_Energy.readStates();
			
			Renewable_Energy.populateUseableStates(valuePSH, valueWindSpeed);
			
			Renewable_Energy.getStats(null);
			
			Renewable_Energy.clearStats();
			
			Renewable_Energy.clear();
			
			message = "Done Running (x" + numTests + ") Test!\n";
			message += "Tests found in .../Testing_Data/test.txt";
			
		} else if (numTests > 1) { // Running MORE than ONE (1) Test
			
			Renewable_Energy.readStates();
			
			Renewable_Energy.populateUseableStates(valuePSH, valueWindSpeed);
			
			for (Integer i = 0; i < numTests; i++) {
				
				Renewable_Energy.getStats(null);
			
				Renewable_Energy.clearStats();
			}
			
			Renewable_Energy.writeAverages(numTests);
			
			Renewable_Energy.clear();
			
			message = "Done Running (x" + numTests + ") Tests!\n";
			message += "Tests found in .../test_results.txt\n";
			message += "Tests found in .../Testing_Data/test_##.txt";
		}
		
		Alert alert_TestsDone = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
		alert_TestsDone.setTitle("Done!");
		alert_TestsDone.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert_TestsDone.show();
		
	}
	
} // End class Renewable_Energy_GUI
