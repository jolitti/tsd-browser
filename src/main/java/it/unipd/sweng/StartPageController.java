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

import java.util.*;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

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
   private TextArea aaa;

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
        //initialising maps for code to nome conversions
        providersName=model.getCodeToProviderNames();
        nationName=model.getCountryCodeToNames();
        //creating map to go back form names to ids
        nationId = new TreeMap();
        providerid = new TreeMap();
        //creating a complete filter for initialisation
        ServiceFilter full = model.getComplementaryFilter(ServiceFilter.nullFilter);

        //initialisng state checkComboBox
        nationCCB.setTitle("nation");
        nationCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters(nationCCB);});

        //adding a listner to implement the select all function
        nationCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> nat) {
                checkall(nationCCB,nat);
            }
        });


        //initialising provider checkComboBox
        tspCCB.setTitle("tsp");
        tspCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters(tspCCB);});
        //adding a listner to implement the select all function
        tspCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> prov) {
                checkall(tspCCB,prov);
            }
        });

        //initialising service type checkComboBox
        typeCCB.setTitle("type");
        typeCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters(typeCCB);});
        //adding a listner to implement the select all function
        typeCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> ty) {
                checkall(typeCCB,ty);
            }
        });



        //initialising status checkComboBox
        statusCCB.setTitle("status");
        statusCCB.addEventHandler(ComboBox.ON_HIDDEN, event -> {getComplementaryFilters(statusCCB);});
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

    public void printFlags(ServiceFilter filter) {//TODO mettere lista nazioni complete al posto di test

        //setting gaps between flags
        flags.setHgap(30);
        flags.setVgap(15);

        //creating a button flag for each state that has a service, adding the image and size
        for(String state: filter.countries().get()) {
            FileInputStream input = null;
            try {
                input = new FileInputStream("src/main/resources/images/"+state+".png");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            ImageView img = new ImageView(new Image(input));
            img.setFitHeight(30);
            img.setFitWidth(40);
            Button nation = new Button("", img);
            nation.setMaxHeight(1);
            nation.setMaxWidth(2);
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
        {//TODO getAddedSubList da errore di out of bound exceptions ma penso sia per un bug in controlsfx DA VERIFICARE
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
        IndexedCheckModel nations=nationCCB.getCheckModel();
        IndexedCheckModel tsp=tspCCB.getCheckModel();
        IndexedCheckModel types=typeCCB.getCheckModel();
        IndexedCheckModel status=statusCCB.getCheckModel();

        IndexedCheckModel[] models=new IndexedCheckModel[4];
        models[0]=nations;
        models[1]=tsp;
        models[2]=types;
        models[3]=status;
        //launch a method in the new controller passing the models
        controller.initFilters(models);//lencia il metodono nel MainController
        //launhes the query in the new controller
        controller.searchByFilters();
        //sets and show the new scene
        stage.setScene(scene);
        stage.show();
    }

    public void getComplementaryFilters(CheckComboBox box)
    {
        ObservableList nations=copy(nationCCB.getCheckModel().getCheckedItems());
        ObservableList tsp=copy(tspCCB.getCheckModel().getCheckedItems());
        ObservableList types=copy(typeCCB.getCheckModel().getCheckedItems());
        ObservableList status=copy(statusCCB.getCheckModel().getCheckedItems());
        System.out.println(status);

        ObservableList<String> backUp=FXCollections.observableArrayList(box.getItems().subList(0, box.getItems().size()));

        ServiceFilter filter=getFilter();
        System.out.println(filter.statuses());
        nationCCB.getCheckModel().clearChecks();
        nationCCB.getItems().clear();
        tspCCB.getCheckModel().clearChecks();
        tspCCB.getItems().clear();
        typeCCB.getCheckModel().clearChecks();
        typeCCB.getItems().clear();
        statusCCB.getCheckModel().clearChecks();
        statusCCB.getItems().clear();

        setFilters(model.getComplementaryFilter(filter));
        System.out.println(filter.statuses());
        box.getItems().clear();
        for (String item:backUp
             ) {
            box.getItems().add(item);
        }

        //TODO
        System.out.println("filtri");
        System.out.println(filter.countries());
        System.out.println(filter.providers());
        System.out.println(filter.types());
        System.out.println(filter.statuses());
        System.out.println("---------");
        System.out.println(model.getComplementaryFilter(filter).countries());
        System.out.println(model.getComplementaryFilter(filter).providers());
        System.out.println(model.getComplementaryFilter(filter).types());
        System.out.println(model.getComplementaryFilter(filter).statuses());






        ObservableList[] models=new ObservableList[4];
        models[0]=nations;
        models[1]=tsp;
        models[2]=types;
        models[3]=status;
        initFilters(models);


    }


    public ObservableList<String> allCheck(CheckComboBox box) {
        ObservableList<String> list = FXCollections.observableArrayList(box.getItems().subList(1, box.getItems().size() - 1));
        return list;
    }

    public ServiceFilter getFilter()
    {
         /*
        takes the selected filters and creates a ServiceFilter
         */
        //TODO certe combinazioni di filtri generano complementari vuote capire perches
        ObservableList nations;
        ObservableList tsp;
        ObservableList types;
        ObservableList status;
        System.out.println(".......getFilter......");
        System.out.println(statusCCB.getCheckModel().getCheckedItems());
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
        System.out.println(statusList);
        System.out.println("------getFilter----");
        ServiceFilter filter = new ServiceFilter(natList, tspList, typesList, statusList);
        return filter;
    }

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
        System.out.println("checkmodels");
        System.out.println(nationCCB.getCheckModel().getCheckedItems());
        System.out.println(tspCCB.getCheckModel().getCheckedItems());
        System.out.println(typeCCB.getCheckModel().getCheckedItems());
        System.out.println(statusCCB.getCheckModel().getCheckedItems());
        //TODO togliere
        stampaTest();

    }

    public void stampaTest()
    {
        aaa.setText(nationCCB.getItems().toString()+"\n" +
                    nationCCB.getCheckModel().getCheckedItems()+"\n"+
                tspCCB.getItems().toString()+"\n"+
                tspCCB.getCheckModel().getCheckedItems()+"\n"+
                typeCCB.getItems().toString()+"\n"+
                typeCCB.getCheckModel().getCheckedItems()+"\n"+
                statusCCB.getItems().toString()+"\n"+
                statusCCB.getCheckModel().getCheckedItems()+"\n");
    }

    public ObservableList copy(ObservableList list)
    {
        ObservableList ret=FXCollections.observableArrayList();
        for (Object item: list
             ) {
            ret.add(item);
        }
        return ret;
    }





}
