package controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Guest;

public class PickGuestPopupController {

	private ObservableList<Guest> guests;
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private DBParser dbParser = new DBParser();

	@FXML
	private Button popupGuestSearchButton;

	@FXML
	private Button closeGuestsPopUpButton;

	@FXML
	private TableView<model.Guest> guestsResultTable;

	@FXML
	private TextField popupGuestSearch;

	@FXML
	private TableColumn<model.Guest, String> popFirstNameCol;

	@FXML
	private TableColumn<model.Guest, String> popLastNameCol;

	@FXML
	private TableColumn<model.Guest, String> popPassportCol;

	@FXML
	private TableColumn<model.Guest, String> popTelephoneCol;

	@FXML
	void closeGuestsPopUp(MouseEvent event) {
		((Node) (event.getSource())).getScene().getWindow().hide();
	}

	/**
	 * Clear the TableView and return the result of the search. Temporarily
	 * using firstname for testing.
	 */
	@FXML
	void popupGuestSearch(ActionEvent event) {
		executor.submit(() -> {
			guestsResultTable.getItems().clear();
			String firstname = popupGuestSearch.getText();
			guests = FXCollections.observableArrayList(dbParser.searchGuests(firstname, "", "", "", "", ""));
			guestsResultTable.setItems(guests);
		});
	}

	/**
	 * Get the Guest when double clicking on row.
	 */
	@FXML
	private void getGuestData(MouseEvent event) {
		if (event.getClickCount() == 2) {
			Guest guest = guestsResultTable.getSelectionModel().getSelectedItem();
			System.out.println(guest);
		}
	}
	
	/**
	 * Fire Search-button event when clicking ENTER in TextField.
	 */
    @FXML
    void onEnterClick(ActionEvent event) {
    	popupGuestSearchButton.fire();
    }

	/**
	 * Load Guest(s) upon opening of the window.
	 */
	@FXML
	void initialize() {
		popFirstNameCol.setCellValueFactory(new PropertyValueFactory<model.Guest, String>("firstName"));
		popLastNameCol.setCellValueFactory(new PropertyValueFactory<model.Guest, String>("lastName"));
		popPassportCol.setCellValueFactory(new PropertyValueFactory<model.Guest, String>("passportNumber"));
		popTelephoneCol.setCellValueFactory(new PropertyValueFactory<model.Guest, String>("telephoneNumber"));

		executor.submit(() -> {
			guests = FXCollections.observableArrayList(dbParser.getAllGuests());
			guestsResultTable.setItems(guests);

		});
	}
}