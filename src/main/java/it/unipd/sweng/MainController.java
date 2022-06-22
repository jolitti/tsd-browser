package it.unipd.sweng;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.CheckComboBox;
import javafx.collections.FXCollections;
import java.io.IOException;
import java.net.URL;
import javafx.scene.text.Text;

import java.util.*;

import static it.unipd.sweng.ModelSpawner.getModelInstance;

import lib.ServiceFilter;
import lib.Service;
import lib.interfaces.ModelInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.control.IndexedCheckModel;

public class MainController implements Initializable {

    @FXML
    private CheckComboBox nationCCB;
    @FXML
    private CheckComboBox tspCCB;
    @FXML
    private CheckComboBox typeCCB;
    @FXML
    private CheckComboBox statusCCB;
    @FXML
    private TreeView selectedFilters;

    @FXML
    private GridPane serviceGP;
    @FXML
    private Button homeButton;
    private ModelInterface model;

    private Map providersName;
    private Map nationName;

    private Map nationId;
    private Map providerid;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        chiamo il model e mi faccio passare i filtro completi e creo le observable list che poi passo alle Check
        Combo box
         */

        try {
            model = getModelInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        providersName = model.getCodeToProviderNames();
        nationName = model.getCountryCodeToNames();
        //creating map to go back form names to ids
        nationId = new TreeMap();
        providerid = new TreeMap();
        ServiceFilter full = model.getComplementaryFilter(ServiceFilter.nullFilter);

        //stato
        nationCCB.setTitle("nation");
        //nationCCB.addEventHandler(ComboBox.ON_HIDDEN,event -> {setSelected();});
        nationCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> nat) {
                checkall(nationCCB, nat);
            }
        });


        //provider
        tspCCB.setTitle("tsp");
        tspCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> prov) {
                checkall(tspCCB, prov);
            }
        });

        //tipo di servizio
        typeCCB.setTitle("type");
        typeCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> ty) {
                checkall(typeCCB, ty);
            }
        });


        //stato del servizi
        statusCCB.setTitle("status");
        statusCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> stat) {
                checkall(statusCCB, stat);
            }
        });

        setFilters(full);

    }

    public void searchByFilters() {

        ServiceFilter filter=getFilter();
        //use the newly created filter to make a query to thw model
        List<Service> services = model.getServices(filter);
        //create the tree representing the filters
        printFilters();
        //print the services in the window
        printServices(services);


    }

    //method that initializes the filters usign the model form the startpage Controller
    public void initFilters(IndexedCheckModel[] models) {
        for (Object state : models[0].getCheckedItems()
        ) {
            nationCCB.getCheckModel().check(state);

        }
        for (Object provider : models[1].getCheckedItems()
        ) {
            tspCCB.getCheckModel().check(provider);

        }
        for (Object type : models[2].getCheckedItems()
        ) {
            typeCCB.getCheckModel().check(type);

        }
        for (Object status : models[3].getCheckedItems()
        ) {
            statusCCB.getCheckModel().check(status);

        }

        //execute the query started in the start page
        searchByFilters();

    }


    //method that prints the tree filters
    public void printFilters() {
        //get the selected filters form the ccb
        ObservableList nations = nationCCB.getCheckModel().getCheckedItems();
        ObservableList tsp = tspCCB.getCheckModel().getCheckedItems();
        ObservableList types = typeCCB.getCheckModel().getCheckedItems();
        ObservableList status = statusCCB.getCheckModel().getCheckedItems();

        //create tree root
        TreeItem root = new TreeItem("filtri");

        //create nation children
        TreeItem natTree = new TreeItem("nations");
        //insert the selected filters
        if (!nations.isEmpty()) {
            //if select all is selected add all the choices
            if (nations.contains("select all")) {
                for (Object nation : nationCCB.getItems()) {
                    if (nation != "select all")
                        natTree.getChildren().add(new TreeItem(nation));
                }
            } else {
                //adds the selected coiches
                for (Object nation : nations) {
                    natTree.getChildren().add(new TreeItem(nation));
                }
            }
            //add the child to the tree
            root.getChildren().add(natTree);
        }

        //does the same for the providers
        TreeItem tspTree = new TreeItem("providers");
        if (!tsp.isEmpty()) {
            if (tsp.contains("select all")) {
                for (Object provider : tspCCB.getItems()) {
                    if (provider != "select all")
                        tspTree.getChildren().add(new TreeItem(provider));
                }
            } else {
                for (Object provider : tsp) {
                    natTree.getChildren().add(new TreeItem(provider));
                }
            }
            root.getChildren().add(tspTree);
        }

        //does the same fot types
        TreeItem typeTree = new TreeItem("types");
        if (!types.isEmpty()) {
            if (types.contains("select all")) {
                for (Object type : typeCCB.getItems()) {
                    if (type != "select all")
                        typeTree.getChildren().add(new TreeItem(type));
                }
            } else {
                for (Object type : types) {
                    typeTree.getChildren().add(new TreeItem(type));
                }
            }
            root.getChildren().add(typeTree);
        }

        //does the same for status
        TreeItem statusTree = new TreeItem("status");
        if (!status.isEmpty()) {
            if (status.contains("select all")) {
                for (Object state : statusCCB.getItems()) {
                    if (state != "select all")
                        statusTree.getChildren().add(new TreeItem(state));
                }
            } else {
                for (Object state : status) {
                    statusTree.getChildren().add(new TreeItem(state));
                }
            }
            root.getChildren().add(statusTree);
        }
        //set the root of the treeview
        selectedFilters.setRoot(root);
    }


    public void printServices(List<Service> services) {
        System.out.println(services.toString());
        String oldSp = "";
        int i = 1;
        for (Service service : services) {
            serviceGP.add(new Text(nationName.get(service.countryCode()).toString()), 0, i);
            //serviceGP.add(new Text(String.valueOf(service.tspId())),1,i);//
            serviceGP.add(new Text(providersName.get(service.tspId()).toString()), 1, i);
            serviceGP.add(new Text(service.serviceName()), 2, i);
            serviceGP.add(new Text(service.type()), 3, i);
            serviceGP.add(new Text(service.currentStatus()), 4, i);
            i++;
        }


    }

    //method that sets the filters in the checkComboBoxes using a services filter as a data container
    public void setFilters(ServiceFilter filter) {
        //create a list with the data and adds it to the ccb adding a select all box
        ObservableList<String> nations = FXCollections.observableArrayList(filter.countries().get());
        nations.add(0, "select all");
        //nations need to be converted from id to name
        for (String item : nations) {
            if (item == "select all") {
                nationCCB.getItems().add("select all");
            } else {
                nationCCB.getItems().add(nationName.get(item));
                nationId.put(nationName.get(item), item);
            }
        }

        //create a list with the data and adds it to the ccb adding a select all box
        ObservableList<Integer> tsp = FXCollections.observableArrayList(filter.providers().get());
        tsp.add(0, 9999);
        //providers need to be converted from id to name
        for (int item : tsp) {
            if (item == 9999) {
                tspCCB.getItems().add("select all");
            } else {
                tspCCB.getItems().add(providersName.get(item));
                providerid.put(providersName.get(item), item);
            }
        }

        //create a list with the data and adds it to the ccb adding a select all box
        ObservableList<String> types = FXCollections.observableArrayList(filter.types().get());
        types.add(0, "select all");
        typeCCB.getItems().addAll(types);


        //create a list with the data and adds it to the ccb adding a select all box
        ObservableList<String> status = FXCollections.observableArrayList(filter.statuses().get());
        status.add(0, "select all");
        statusCCB.getItems().addAll(status);

    }

    //method used to implement the mutual exclusion of select all and a any other choice inside a filter
    public void checkall(CheckComboBox ccb, ListChangeListener.Change change) {
        //TODO getAddedSubList da errore di out of bound exceptions ma penso sia per un bug in controlsfx DA VERIFICARE
        change.next();
        if (change.getAddedSubList().contains(ccb.getItems().get(0)) && !change.getRemoved().contains(ccb.getItems().get(0))) {//getAddedSubList da errore di out of bound exceptions ma penso sia per un bug in controlsfx DA VERIFICARE
            for (int i = 1; i < ccb.getItems().size(); i++) {
                if (ccb.getCheckModel().isChecked(i)) {
                    ccb.getCheckModel().clearCheck(i);
                }
            }
        } else {
            if (ccb.getCheckModel().isChecked(0) && change.getRemovedSize() == 0) {
                ccb.getCheckModel().clearCheck(0);
            }
        }
    }


    //metohd used to implemt the return to the start page
    public void homeScene() throws IOException {
        //changes the scene to the startPage one
        Stage stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/it.unipd.sweng/StartPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = (Stage) homeButton.getScene().getWindow();
        //changes the scene to the startPage one and sets the stage
        stage.setScene(scene);
        stage.show();
    }


    //method used to return a lst with all the itmes in case of select all
    public ObservableList<String> allCheck(CheckComboBox box) {
        ObservableList<String> list = FXCollections.observableArrayList(box.getItems().subList(1, box.getItems().size() - 1));
        return list;
    }


    public ServiceFilter getFilter()
    {
         /*
        takes the selected filters and creates a ServiceFilter
         */
        //TODO gestire la conversione inversa di nation tsp da nome a id
        //TODO gestire coversione di tsp da nome a id
        ObservableList nations;
        ObservableList tsp;
        ObservableList types;
        ObservableList status;
        //creates observable list with all the checked nations
        if (nationCCB.getCheckModel().isChecked(0)) {
            nations = allCheck(nationCCB);
        } else { //if select all is checked adds all the nations of the ccb
            nations = nationCCB.getCheckModel().getCheckedItems();
        }
        //creates observable list with all the checked tsp
        if (tspCCB.getCheckModel().isChecked(0)) {
            tsp = allCheck(tspCCB);
        } else {//if select all is checked adds all the tsps of the ccb
            tsp = tspCCB.getCheckModel().getCheckedItems();
        }

        //creates observable list with all the checked types
        if (typeCCB.getCheckModel().isChecked(0)) {
            types = allCheck(typeCCB);
        } else {//if select all is checked adds all the types of the ccb
            types = typeCCB.getCheckModel().getCheckedItems();
        }

        //creates observable list with all the checked statuses
        if (statusCCB.getCheckModel().isChecked(0)) {
            status = allCheck(statusCCB);
        } else {//if select all is checked adds all the statuses of the ccb
            status = statusCCB.getCheckModel().getCheckedItems();
        }

        List n = new LinkedList();

        for (int i = 0; i < nations.size(); i++) {
            n.add(nationId.get(nations.get(i)));
        }
        List p = new LinkedList();
        for (int i = 0; i < tsp.size(); i++) {
            p.add(providerid.get(tsp.get(i)));
        }


        Optional<List<String>> natList = Optional.of(n);
        Optional<List<Integer>> tspList = Optional.of(p);
        Optional<List<String>> typesList = Optional.of(types);
        Optional<List<String>> statusList = Optional.of(status);

        ServiceFilter filter = new ServiceFilter(natList, tspList, typesList, statusList);
        return filter;
    }



}











