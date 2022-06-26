package it.unipd.sweng;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
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

    private ServiceFilter fullFilter;


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
        fullFilter=full;

        //stato
        nationCCB.setTitle("nation");
        nationCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters();});
        nationCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> nat) {
                checkall(nationCCB, nat);
            }
        });


        //provider
        tspCCB.setTitle("tsp");
        tspCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters();});

        tspCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> prov) {
                checkall(tspCCB, prov);
            }
        });

        //tipo di servizio
        typeCCB.setTitle("type");
        typeCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters();});

        typeCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> ty) {
                checkall(typeCCB, ty);
            }
        });


        //stato del servizi
        statusCCB.setTitle("status");
        statusCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters();});

        statusCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> stat) {
                checkall(statusCCB, stat);
            }
        });

        setFilters(full);

    }

    public void searchByFilters() {

        ServiceFilter filter = getFilter();
        //use the newly created filter to make a query to thw model
        List<Service> services = model.getServices(filter);
        //create the tree representing the filters
        printFilters();
        //print the services in the window
        printServices(services);


    }

    //method that initializes the filters usign the model form the startpage Controller
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
        //set the root of the treeview
        selectedFilters.setRoot(root);
    }


    public void printServices(List<Service> services) {


        serviceGP.getChildren().clear();
        serviceGP.add(new Text("nation"),0,0);
        serviceGP.add(new Text("provider"),1,0);
        serviceGP.add(new Text("name"),2,0);
        serviceGP.add(new Text("type"),3,0);
        serviceGP.add(new Text("status"),4,0);
        String oldSp = "";
        int i = 1;
        for (Service service : services) {
            serviceGP.add(new ScrollPane(new Text(nationName.get(service.countryCode()).toString())), 0, i);
            serviceGP.add(new ScrollPane(new Text(providersName.get(service.tspId()).toString())), 1, i);
            serviceGP.add(new ScrollPane(new Text(service.serviceName())), 2, i);
            serviceGP.add(new ScrollPane(new Text(service.type())), 3, i);
            serviceGP.add(new ScrollPane(new Text(service.currentStatus())), 4, i);
            i++;
        }

        TextFlow text=new TextFlow();


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
        stage.setMinWidth(1000);
        stage.setMinHeight(750);
        stage.show();
    }


    public void getComplementaryFilters()
    {
        //save a copy of the ChecModels
        ObservableList nations=copy(nationCCB.getCheckModel().getCheckedItems());
        ObservableList tsp=copy(tspCCB.getCheckModel().getCheckedItems());
        ObservableList types=copy(typeCCB.getCheckModel().getCheckedItems());
        ObservableList status=copy(statusCCB.getCheckModel().getCheckedItems());
        System.out.println(status);
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
        //resets all the ccb
        System.out.println(filter.statuses());
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
        System.out.println(filter.statuses());
        /*
        //TODO
        System.out.println("filtri");
        System.out.println(filter.countries());
        System.out.println(filter.providers());
        System.out.println(filter.types());
        System.out.println(filter.statuses());
        System.out.println("---------");
        System.out.println(finFilter.countries());
        System.out.println(finFilter.providers());
        System.out.println(finFilter.types());
        System.out.println(finFilter.statuses());

         */

        //sets the cehcks
        ObservableList[] models=new ObservableList[4];
        models[0]=nations;
        models[1]=tsp;
        models[2]=types;
        models[3]=status;
        initFilters(models);


    }


    //method used to return a lst with all the itmes in case of select all
    public ObservableList<String> allCheck(CheckComboBox box) {
        ObservableList<String> list = FXCollections.observableArrayList(box.getItems().subList(1, box.getItems().size() - 1));
        return list;
    }


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

        List n = new LinkedList();

        for (int i = 0; i < nations.size(); i++) {
            n.add(nationId.get(nations.get(i)));
        }
        List p = new LinkedList();
        for (int i = 0; i < tsp.size(); i++) {
            p.add(providerid.get(tsp.get(i)));
        }


        Optional<List<String>> natList;
        Optional<List<String>> tspList;
        Optional<List<String>> typesList;
        Optional<List<String>> statusList;

        //initializes the optionals, if the list is empty creates assing  Optional.empty
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

        //creates and return the filter
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

   /* ObservableList toNationId(ObservableList nat)
    {
        ObservableList ret=FXCollections.observableArrayList();
        for (Object n:nat)
        {
            if(n!="select all")
                ret.add(nationId.get(n));
        }
        return ret;
    }

    ObservableList toTspId(ObservableList tsp)
    {
        ObservableList ret=FXCollections.observableArrayList();
        for (Object t:tsp)
        {
            if(t!="select all")
                ret.add(providerid.get(t));
        }
        return ret;
    }

    */


    //makes the intersection of the 4 Optional contained in the filters
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

    //intersects 2 lists
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