package it.unipd.sweng;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.controlsfx.control.CheckComboBox;
import javafx.collections.FXCollections;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import static it.unipd.sweng.ModelSpawner.getModelInstance;

import lib.ServiceFilter;
import lib.Service;
import lib.interfaces.ModelInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ResultPageController implements Initializable {

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
    private TableView serviceGP;
    @FXML
    private Button homeButton;
    private ModelInterface model;

    private Map providersName;
    private Map nationName;

    private Map nationId;
    private Map providerid;

    private ServiceFilter fullFilter;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //gets an  instance of the model and uses it to initialise the nodes
        try {
            model = getModelInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //get from the model 2 maps to conver ids into names
        providersName = model.getCodeToProviderNames();
        nationName = model.getCountryCodeToNames();
        //creating 2 maps to go back form names to ids
        nationId = new TreeMap();
        providerid = new TreeMap();
        //ask the model for the complete filters and saves a copy
        ServiceFilter full = model.getComplementaryFilter(ServiceFilter.nullFilter);
        fullFilter=full;

        //initialisation of the nation CCB
        nationCCB.setTitle("NATION");
        nationCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters();});
        nationCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> nat) {
                checkall(nationCCB, nat);
            }
        });

        //initialisation of the provider CCB
        tspCCB.setTitle("PROVIDER");
        tspCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters();});

        tspCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> prov) {
                checkall(tspCCB, prov);
            }
        });

        //initialisation of the type CCB
        typeCCB.setTitle("TYPE");
        typeCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters();});

        typeCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> ty) {
                checkall(typeCCB, ty);
            }
        });

        //initialisation of the status CCB
        statusCCB.setTitle("STATUS");
        statusCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters();});

        statusCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> stat) {
                checkall(statusCCB, stat);
            }
        });

        //set the data in the CCBs
        setFilters(full);

    }

    public void searchByFilters() {
        //gets the filter form the CCBs
        ServiceFilter filter = getFilter();
        //use the newly created filter to make a query to the model
        List<Service> services = model.getServices(filter);
        //create the tree representing the filters
        printFilters();
        //print the services in the window
        printServices(services);

    }

    /*method that initializes the filters using the checkModels form the startPage Controller,
    check the same items that where checked in the start page
     */
    public void initFilters(ObservableList[] models) {
        for (Object state : models[0]
        ) {
            if(nationCCB.getItems().contains(state))
                nationCCB.getCheckModel().check(state);

        }
        for (Object provider : models[1]
        ) {
            if(tspCCB.getItems().contains(provider))
                tspCCB.getCheckModel().check(provider);

        }
        for (Object type : models[2]
        ) {
            if(typeCCB.getItems().contains(type))
                typeCCB.getCheckModel().check(type);

        }
        for (Object status : models[3]
        ) {
            if(statusCCB.getItems().contains(status)) {
                statusCCB.getCheckModel().check(status);
            }
        }
    }

    //method that prints the selected filters in the threeView
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
                //adds the selected cohiches
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
                    tspTree.getChildren().add(new TreeItem(provider));
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
        //set the root of the treeView
        selectedFilters.setRoot(root);
    }

    //method used to print the services in the tableView
    public void printServices(List<Service> services) {
        //clears the previous results
        serviceGP.getColumns().clear();
        serviceGP.getItems().clear();

        //creates column for the nation, uses a lambda expression to specify the value to print, and sets the width
        TableColumn<PrintableService,String> natioColumn=new TableColumn<>("NATION");
        natioColumn.setCellValueFactory(s-> new SimpleStringProperty(s.getValue().country()));
        natioColumn.setMinWidth(150);

        //creates column for the provider, uses a lambda expression to specify the value to print, and sets the width
        TableColumn<PrintableService,String> providerColumn=new TableColumn<>("PROVIDER");
        providerColumn.setCellValueFactory(s-> new SimpleStringProperty(s.getValue().provider()));
        providerColumn.setMinWidth(150);

        //creates column for the name, uses a lambda expression to specify the value to print, and sets the width
        TableColumn<PrintableService,String> nameColumn=new TableColumn<>("NAME");
        nameColumn.setCellValueFactory(s-> new SimpleStringProperty(s.getValue().name()));
        nameColumn.setMinWidth(150);

        //creates column for the type, uses a lambda expression to specify the value to print, and sets the width
        TableColumn<PrintableService,String> typeColumn=new TableColumn<>("TYPE");
        typeColumn.setCellValueFactory(s-> new SimpleStringProperty(s.getValue().type()));
        typeColumn.setMinWidth(150);

        //creates column for the status, uses a lambda expression to specify the value to print, and sets the width
        TableColumn<PrintableService,String> statusColumn=new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(s-> new SimpleStringProperty(s.getValue().status()));
        statusColumn.setMinWidth(150);

        //add the columns to the tableView
        serviceGP.getColumns().add(natioColumn);
        serviceGP.getColumns().add(providerColumn);
        serviceGP.getColumns().add(nameColumn);
        serviceGP.getColumns().add(typeColumn);
        serviceGP.getColumns().add(statusColumn);

        //adds all the services
        for (Service service:services) {
            //creates a printable service containing the data and adds it to the tableView
            PrintableService aux=new PrintableService((String) providersName.get(service.tspId()),(String) nationName.get(service.countryCode()),service.serviceName(),qServiceToString(service.qServiceTypes()),service.currentStatus() );
            serviceGP.getItems().add(aux);
        }
    }

    //method use convert the qServiceType field from an array to a string
    public String qServiceToString(String[] q)
    {
        String ret="";
        for (String s:q
             ) {
            ret=" "+ret+s+"  ";
        }

        return ret;
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
        ObservableList<String> tsp = FXCollections.observableArrayList(filter.providers().get());
        tsp.add(0, "select all");
        //providers need to be converted from id to name
        for (Object item : tsp) {
            if (item == "select all") {
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
        try {
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
        catch ( IndexOutOfBoundsException e) {}
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
        stage.setMinWidth(1000);
        stage.setMinHeight(750);
        stage.show();
    }

    //method use to obtain the complementary of the selected filters
    public void getComplementaryFilters()
    {
        //save a copy of the ChecModels
        ObservableList nations=copy(nationCCB.getCheckModel().getCheckedItems());
        ObservableList tsp=copy(tspCCB.getCheckModel().getCheckedItems());
        ObservableList types=copy(typeCCB.getCheckModel().getCheckedItems());
        ObservableList status=copy(statusCCB.getCheckModel().getCheckedItems());

        //gets the selected items from the ccb
        ServiceFilter filter=getFilter();

        //create a filter for every checkBox
        ServiceFilter natFilter=new ServiceFilter(filter.countries(), Optional.empty(), Optional.empty(), Optional.empty());
        ServiceFilter tspFilter=new ServiceFilter(Optional.empty(), filter.providers(), Optional.empty(), Optional.empty());
        ServiceFilter typeFilter=new ServiceFilter(Optional.empty(), Optional.empty(), filter.types(), Optional.empty());
        ServiceFilter statusFilter=new ServiceFilter(Optional.empty(), Optional.empty(), Optional.empty(), filter.statuses());

        //gets the complemtary of every ccb
        ServiceFilter nationsResult=model.getComplementaryFilter(natFilter);
        ServiceFilter tspResult=model.getComplementaryFilter(tspFilter);
        ServiceFilter typeResult=model.getComplementaryFilter(typeFilter);
        ServiceFilter statusResult=model.getComplementaryFilter(statusFilter);

        //intersect all the results
        List auxNat=intersection(fullFilter.countries(),tspResult.countries(),typeResult.countries(),statusResult.countries());
        List auxTsp=intersection(nationsResult.providers(),fullFilter.providers(),typeResult.providers(),statusResult.providers());
        List auxType=intersection(nationsResult.types(),tspResult.types(),fullFilter.types(),statusResult.types());
        List auxStat=intersection(nationsResult.statuses(),tspResult.statuses(),typeResult.statuses(),fullFilter.statuses());

        //creates the final filter
        ServiceFilter finFilter=new ServiceFilter(Optional.of(auxNat),Optional.of(auxTsp),Optional.of(auxType),Optional.of(auxStat));

        //resets all the CCBs and their checkModels
        nationCCB.getCheckModel().clearChecks();
        nationCCB.getItems().clear();
        tspCCB.getCheckModel().clearChecks();
        tspCCB.getItems().clear();
        typeCCB.getCheckModel().clearChecks();
        typeCCB.getItems().clear();
        statusCCB.getCheckModel().clearChecks();
        statusCCB.getItems().clear();

        //sets the filter
        setFilters(finFilter);

        //creates an array of the checkModels
        ObservableList[] models=new ObservableList[4];
        models[0]=nations;
        models[1]=tsp;
        models[2]=types;
        models[3]=status;

        //sets the checks
        initFilters(models);

    }

    //method used to return a lst with all the items in case of select all
    public ObservableList<String> allCheck(CheckComboBox box) {
        ObservableList<String> list = FXCollections.observableArrayList(box.getItems().subList(1, box.getItems().size() ));
        return list;
    }

    //methos used to create a filter containing the selected items from the CCBs
    public ServiceFilter getFilter()
    {
        //takes the selected filters and creates a ServiceFilter
        ObservableList nations;
        ObservableList tsp;
        ObservableList types;
        ObservableList status;

        //creates observable list with all the checked nations
        if (nationCCB.getCheckModel().isChecked(0)) {
            nations = allCheck(nationCCB);
        } else { //if select all is checked adds all the nations of the ccb
            nations = copy(nationCCB.getCheckModel().getCheckedItems());
        }
        //creates observable list with all the checked tsp
        if (tspCCB.getCheckModel().isChecked(0)) {
            tsp = allCheck(tspCCB);
        } else {//if select all is checked adds all the tsps of the ccb
            tsp = copy(tspCCB.getCheckModel().getCheckedItems());
        }

        //creates observable list with all the checked types
        if (typeCCB.getCheckModel().isChecked(0)) {
            types = allCheck(typeCCB);
        } else {//if select all is checked adds all the types of the ccb
            types = copy(typeCCB.getCheckModel().getCheckedItems());
        }

        //creates observable list with all the checked statuses
        if (statusCCB.getCheckModel().isChecked(0)) {
            status = allCheck(statusCCB);
        } else {//if select all is checked adds all the statuses of the ccb
            status = copy(statusCCB.getCheckModel().getCheckedItems());
        }

        //converts the nation names into ids
        List n = new LinkedList();
        for (int i = 0; i < nations.size(); i++) {
            n.add(nationId.get(nations.get(i)));
        }

        //converts the provider names into ids
        List p = new LinkedList();
        for (int i = 0; i < tsp.size(); i++) {
            p.add(providerid.get(tsp.get(i)));
        }

        //creates the optional for the filters
        Optional<List<String>> natList;
        Optional<List<String>> tspList;
        Optional<List<String>> typesList;
        Optional<List<String>> statusList;

        //initializes the optionals, if the list is empty creates assigns Optional.empty
        if(n.isEmpty())
        {
            natList=Optional.empty();
        }
        else
        {
            natList = Optional.of(n);
        }

        if(p.isEmpty())
        {
            tspList=Optional.empty();
        }
        else
        {
            tspList = Optional.of(p);
        }

        if(types.isEmpty())
        {
            typesList = Optional.empty();
        }
        else
        {
            typesList = Optional.of(types);
        }

        if(status.isEmpty())
        {
            statusList = Optional.empty();
        }
        else
        {
            statusList = Optional.of(status);
        }

        //creates and returns the filter
        ServiceFilter filter = new ServiceFilter(natList, tspList, typesList, statusList);
        return filter;
    }

    //method used to create a deep copy of Observable lists
    public  ObservableList copy(ObservableList list)
    {
        ObservableList ret=FXCollections.observableArrayList();
        for (Object item: list
        ) {
            ret.add(item);
        }
        return ret;
    }

    //Method used to intersect 4 Optional
    public List intersection(Optional o1,Optional o2,Optional o3,Optional o4)
    {
        List l1=new ArrayList();
        List l2=new ArrayList();
        List l3=new ArrayList();
        List l4=new ArrayList();

        if(o1.isPresent())
        {
            l1=(List) o1.get();
        }
        if(o2.isPresent())
        {
            l2=(List) o2.get();
        }
        if(o3.isPresent())
        {
            l3=(List) o3.get();
        }
        if(o4.isPresent())
        {
            l4=(List) o4.get();
        }

        List a1=inter(l1,l2);
        List a2=inter(l3,l4);

        return inter(a1,a2);
    }

    //metod used to intersect to lists
    public List inter(List l1,List l2)
    {
        List a1=new ArrayList<>();
        for (Object item:l1
        ) {
            if(l2.contains(item))
            {
                a1.add(item);
            }
        }
        return a1;
    }

}