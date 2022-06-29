package it.unipd.sweng;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import lib.Service;
import lib.ServiceFilter;
import lib.interfaces.ModelInterface;
import org.controlsfx.control.CheckComboBox;

import java.awt.*;
import java.util.*;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.control.IndexedCheckModel;


import static it.unipd.sweng.ModelSpawner.getModelInstance;

public class StartPageController implements Initializable {

    @FXML
    private CheckComboBox nationCCB;
    @FXML
    private CheckComboBox tspCCB;
    @FXML
    private CheckComboBox typeCCB;
    @FXML
    private CheckComboBox statusCCB;

   @FXML
   private Button search;

   @FXML
   private FlowPane flags;

   @FXML


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
        //initialising maps for code to nome conversions
        providersName=model.getCodeToProviderNames();
        nationName=model.getCountryCodeToNames();
        //creating map to go back form names to ids
        nationId = new TreeMap();
        providerid = new TreeMap();
        //creating a complete filter for initialisation
        ServiceFilter full = model.getComplementaryFilter(ServiceFilter.nullFilter);
        fullFilter=full;

        //initialisng state checkComboBox
        nationCCB.setTitle("NATION");
        nationCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters();});

        //adding a listner to implement the select all function
        nationCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> nat) {
                checkall(nationCCB,nat);
            }
        });


        //initialising provider checkComboBox
        tspCCB.setTitle("PROVIDER");
        tspCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters();});
        //adding a listner to implement the select all function
        tspCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> prov) {
                checkall(tspCCB,prov);
            }
        });

        //initialising service type checkComboBox
        typeCCB.setTitle("TYPE");
        typeCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters();});
        //adding a listner to implement the select all function
        typeCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> ty) {
                checkall(typeCCB,ty);
            }
        });



        //initialising status checkComboBox
        statusCCB.setTitle("STATUS");
        statusCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters();});
        //adding a listner to implement the select all function
        statusCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> stat) {
                checkall(statusCCB,stat);
            }
        });

        //call to set filter function with full filter parameter, use for first valorization of filters
        setFilters(full);

        //call to print flags to create the falg button for quick query
        printFlags(full);

    }

    public void printFlags(ServiceFilter filter) {

        //setting gaps between flags
        flags.setHgap(30);
        flags.setVgap(30);

        //creating a button flag for each state that has a service, adding the image and size
        for(String state: filter.countries().get()) {
            FileInputStream input = null;
            try {
                input = new FileInputStream("src/main/resources/images/"+state+".png");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            ImageView img = new ImageView(new Image(input));
            img.setFitHeight(45);
            img.setFitWidth(60);
            Button nation = new Button("", img);
            nation.setMinSize(64,49);
            nation.setMaxSize(64,49);

            flags.getChildren().add(nation);
            flags.getChildren().add(new Pane());

            //adding a event handler fot the click action on the flag
            nation.setOnAction(event-> {
                try {
                    flagButton(state);     // the method receives the state of the flag
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    //method launched after a flag is clicked, launches a query with only the state
    public void flagButton(String state) throws IOException {
        //set all the filters to empty excepts the nations that contains the flag nation
        nationCCB.getCheckModel().clearChecks();
        tspCCB.getCheckModel().clearChecks();
        typeCCB.getCheckModel().clearChecks();
        statusCCB.getCheckModel().clearChecks();
        nationCCB.getItems().add(nationName.get(state));
        nationCCB.getCheckModel().check(nationName.get(state));

        //makes the query
        changeToSearchScene();

    }



    //method that sets the filters in the checkComboBoxes using a services filter as a data container
    public void setFilters(ServiceFilter filter)
    {
        //create a list with the data and adds it to the ccb adding a select all box

        ObservableList<String> nations = FXCollections.observableArrayList(filter.countries().get());
        nations.add(0,"select all");
        //nations need to be converted from id to name
        for (String item:nations) {
            if(item=="select all")
            {
                nationCCB.getItems().add("select all");
            }
            else {
                nationCCB.getItems().add(nationName.get(item));
                nationId.put(nationName.get(item), item);
            }
        }

        //create a list with the data and adds it to the ccb adding a select all box
        ObservableList<String> tsp = FXCollections.observableArrayList(filter.providers().get());
        tsp.add(0,"select all");
        //providers need to be converted from id to name
        for (Object item:tsp) {
            if(item=="select all")
            {
                tspCCB.getItems().add("select all");
            }
            else {
                tspCCB.getItems().add(providersName.get(item));
                providerid.put(providersName.get(item), item);
            }
        }

        //create a list with the data and adds it to the ccb adding a select all box
        ObservableList<String> types = FXCollections.observableArrayList(filter.types().get());
        types.add(0,"select all");
        typeCCB.getItems().addAll(types);


        //create a list with the data and adds it to the ccb adding a select all box
        ObservableList<String> status = FXCollections.observableArrayList(filter.statuses().get());
        status.add(0,"select all");
        statusCCB.getItems().addAll(status);

    }

    //method used to implement the mutual exclusion of select all and a any other choice inside a filter
    public void checkall(CheckComboBox ccb, ListChangeListener.Change change)
    {
        change.next();
        if(change.getAddedSubList().contains(ccb.getItems().get(0)) && !change.getRemoved().contains(ccb.getItems().get(0)))
        {
            for(int i=1;i<ccb.getItems().size();i++)
            {
                if(ccb.getCheckModel().isChecked(i))
                {
                    ccb.getCheckModel().clearCheck(i);
                }
            }
        }
        else
        {
            if(ccb.getCheckModel().isChecked(0)&& change.getRemovedSize()==0)
            {
                ccb.getCheckModel().clearCheck(0);
            }
        }
    }

    //method used to launch the initials query, and change scene to the result page
    public void changeToSearchScene() throws IOException
    {
        //creates a new scene
        Stage stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/it.unipd.sweng/page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MainController controller=fxmlLoader.getController();
        stage=(Stage)search.getScene().getWindow();

        //creates a model array to pass in the new controller, so the ccb can maintain their checkModel
        ObservableList nations=copy(nationCCB.getCheckModel().getCheckedItems());
        ObservableList tsp=copy(tspCCB.getCheckModel().getCheckedItems());
        ObservableList types=copy(typeCCB.getCheckModel().getCheckedItems());
        ObservableList status=copy(statusCCB.getCheckModel().getCheckedItems());

        ObservableList[] models=new ObservableList[4];
        models[0]=nations;
        models[1]=tsp;
        models[2]=types;
        models[3]=status;
        initFilters(models);
        //launch a method in the new controller passing the models
        controller.initFilters(models);//lencia il metodono nel MainController
        controller.getComplementaryFilters();
        //launhes the query in the new controller
        controller.searchByFilters();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        stage.setMinWidth(screenSize.width/2);
        stage.setMinHeight(screenSize.height/2);
        stage.setMaxHeight(screenSize.height);
        stage.setMaxWidth(screenSize.width);

        //sets and show the new scene
        stage.setScene(scene);
        stage.show();
    }



    //method used to implement the filters complementairty
    public void getComplementaryFilters()
    {
        //save a copy of the ChecModels
        ObservableList nations=copy(nationCCB.getCheckModel().getCheckedItems());
        ObservableList tsp=copy(tspCCB.getCheckModel().getCheckedItems());
        ObservableList types=copy(typeCCB.getCheckModel().getCheckedItems());
        ObservableList status=copy(statusCCB.getCheckModel().getCheckedItems());
        //System.out.println(status);
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
        //System.out.println(filter.statuses());
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
        //System.out.println(filter.statuses());
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


    /*ObservableList toNationId(ObservableList nat)
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

    //return all the items in the ccb, to use in case of select all
    public ObservableList<String> allCheck(CheckComboBox box) {
        ObservableList<String> list = FXCollections.observableArrayList(box.getItems().subList(1, box.getItems().size()));
        return list;
    }


    //returns the items selected in the ccb
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


    //sets the check in the ccbs
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

        /*System.out.println("checkmodels");
        System.out.println(nationCCB.getCheckModel().getCheckedItems());
        System.out.println(tspCCB.getCheckModel().getCheckedItems());
        System.out.println(typeCCB.getCheckModel().getCheckedItems());
        System.out.println(statusCCB.getCheckModel().getCheckedItems());
        //TODO togliere

         */
       // stampaTest();

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





}
