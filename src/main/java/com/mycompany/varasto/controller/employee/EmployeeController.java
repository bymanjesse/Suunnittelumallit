/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.varasto.controller.employee;

/**
 *
 * @author aleks
 */
import com.mycompany.varasto.entity.Employee;
import com.mycompany.varasto.interfaces.EmployeeInterface;
import static com.mycompany.varasto.interfaces.EmployeeInterface.EMPLOYEELIST;
import com.mycompany.varasto.model.EmployeeModel;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class EmployeeController implements Initializable, EmployeeInterface {
    // fxml muutujat
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Long> idColumn;
    @FXML
    private TableColumn<Employee, String> firstnameColumn, lastnameColumn, usernameColumn,
            passwordColumn, phoneColumn, addressColumn;
    @FXML
    private TextField searchField;
    @FXML
    private Button editButton, deleteButton;
    private EmployeeModel model;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button menu;
    @FXML
    private VBox drawer;
    private ResourceBundle rb;
    @Override
    // ladataan ikkuna
    // lue categoryControllien kommentit ovat samanliaset
    // mutta muutettu vain Category employees ja joitan muita pieniä muutoksia GUI hin
    // 
    public void initialize(URL location, ResourceBundle rb) {
        model = new EmployeeModel();
        this.rb = rb;

        drawerAction();
        loadData();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        employeeTable.setItems(EMPLOYEELIST);

        filterData();

        editButton
                .disableProperty()
                .bind(Bindings.isEmpty(employeeTable.getSelectionModel().getSelectedItems()));
        deleteButton
                .disableProperty()
                .bind(Bindings.isEmpty(employeeTable.getSelectionModel().getSelectedItems()));
    }

    private void filterData() {
        FilteredList<Employee> searchedData = new FilteredList<>(EMPLOYEELIST, e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                searchedData.setPredicate(employee -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (employee.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (employee.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (employee.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (employee.getUserName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });

            SortedList<Employee> sortedData = new SortedList<>(searchedData);
            sortedData.comparatorProperty().bind(employeeTable.comparatorProperty());
            employeeTable.setItems(sortedData);
        });
    }

    private void loadData() {

        if (!EMPLOYEELIST.isEmpty()) {
            EMPLOYEELIST.clear();
        }
        EMPLOYEELIST.addAll(model.getEmployees());
    }
    // menu napin handlaus kommentoitu cetegory luokassa ovat samanlaiset
    // kaikki alla samaa kuin category controllerissa mutta muutettu employee
    private void drawerAction() {

        TranslateTransition openNav = new TranslateTransition(new Duration(350), drawer);
        openNav.setToX(0);
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), drawer);
        menu.setOnAction((ActionEvent evt) -> {
            if (drawer.getTranslateX() != 0) {
                openNav.play();
                menu.getStyleClass().remove("menu-button");
                menu.getStyleClass().add("open-menu");
            } else {
                closeNav.setToX(-(drawer.getWidth()));
                closeNav.play();
                menu.getStyleClass().remove("open-menu");
                menu.getStyleClass().add("menu-button");
            }
        });
    }


    @FXML
    public void adminAction(ActionEvent event) throws Exception {
        windows("/fxml/Admin.fxml", rb.getString("administrator"), event, rb);
    }

    @FXML
    public void productAction(ActionEvent event) throws Exception {

        windows("/fxml/Product.fxml", rb.getString("product"), event, rb);
    }

    @FXML
    public void categoryAction(ActionEvent event) throws Exception {

        windows("/fxml/Category.fxml", rb.getString("category"), event, rb);
    }

    @FXML
    public void purchaseAction(ActionEvent event) throws Exception {

        windows("/fxml/Purchase.fxml", rb.getString("purchase"), event, rb);
    }

    @FXML
    public void salesAction(ActionEvent event) throws Exception {

        windows("/fxml/Sales.fxml", rb.getString("sales"), event, rb);
    }

    @FXML
    public void reportAction(ActionEvent event) throws Exception {
        windows("/fxml/Report.fxml", rb.getString("report"), event, rb);
    }

    @FXML
    public void supplierAction(ActionEvent event) throws Exception {
        windows("/fxml/Supplier.fxml", rb.getString("supplier"), event, rb);
    }

    
    @FXML
    public void logoutAction(ActionEvent event) throws Exception {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"), rb);
        Stage stage = new Stage();
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        Scene scene = new Scene(root);
        stage.setTitle("Inventory:: Version 1.0");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

    }

    private void windows(String path, String title, ActionEvent event, ResourceBundle rb) throws Exception {

        double width = ((Node) event.getSource()).getScene().getWidth();
        double height = ((Node) event.getSource()).getScene().getHeight();

        Parent root = FXMLLoader.load(getClass().getResource(path), rb);
        Scene scene = new Scene(root, width, height);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public void addAction(ActionEvent event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/employee/Add.fxml"), rb);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Employee");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void editAction(ActionEvent event) throws Exception {

        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        int selectedEmployeeId = employeeTable.getSelectionModel().getSelectedIndex();
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/fxml/employee/Edit.fxml")), rb);
        EditController controller = new EditController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Details");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        controller.setEmployee(selectedEmployee, selectedEmployeeId);
        employeeTable.getSelectionModel().clearSelection();

    }

    @FXML
    public void deleteAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove");
        alert.setHeaderText("Remove Employee");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

            model.deleteEmployee(selectedEmployee);
            EMPLOYEELIST.remove(selectedEmployee);
        }

        employeeTable.getSelectionModel().clearSelection();
    }
}
