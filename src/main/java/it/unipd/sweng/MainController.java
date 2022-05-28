package it.unipd.sweng;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.CheckComboBox;
import javafx.collections.FXCollections;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import static it.unipd.sweng.ModelSpawner.getModelInstance;
import java.util.List;

import lib.ServiceFilter;
import lib.interfaces.ModelInterface;
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
    private GridPane table;
    @FXML
    private TextArea txtArea;
    @FXML
    private TreeView selectedFilters;

    private ModelInterface model;


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
        ServiceFilter full = model.getComplementaryFilter(new ServiceFilter(null, null, null, null));

        //stato
        nationCCB.setTitle("nation");
        //nationCCB.addEventHandler(ComboBox.ON_HIDDEN,event -> {test();});

        //provider
        tspCCB.setTitle("tsp");

        //tipo di servizio
        typeCCB.setTitle("type");


        //stato del servizi
        statusCCB.setTitle("status");

        setFilters(full);
    }

    public void SearchByFilters()
    {

        /*
        prendo i le selezioni e mi creo un filtro
        uso il filtro per ottenere la lista di servizi
         */
        ObservableList nations=nationCCB.getCheckModel().getCheckedItems();
        ObservableList tsp=tspCCB.getCheckModel().getCheckedItems();
        ObservableList types=typeCCB.getCheckModel().getCheckedItems();
        ObservableList status=statusCCB.getCheckModel().getCheckedItems();

        Optional<List<String>> natList=Optional.of(nations);
        Optional<List<Integer>> tspList=Optional.of(tsp);
        Optional<List<String>> typesList=Optional.of(types);
        Optional<List<String>> statusList=Optional.of(status);

        ServiceFilter filter=new ServiceFilter(natList,tspList,typesList,statusList);

        model.getServices(filter);
        printFilters();

    }



    public void printFilters()
    {
        //ricavo le scelte selezionate
        ObservableList nations=nationCCB.getCheckModel().getCheckedItems();
        ObservableList tsp=tspCCB.getCheckModel().getCheckedItems();
        ObservableList types=typeCCB.getCheckModel().getCheckedItems();
        ObservableList status=statusCCB.getCheckModel().getCheckedItems();

        //creo radice treeview
        TreeItem root=new TreeItem("filtri");

        //cree figlio per le nazioni
        TreeItem natTree=new TreeItem("nations");
        for (Object nation:nations) {
            natTree.getChildren().add(new TreeItem(nation));
        }//se è vuoto non lo metto
        if(!nations.isEmpty())
        {
            root.getChildren().add(natTree);
        }

        //creo figlio per i provider
        TreeItem tspTree=new TreeItem("providers");
        for (Object prov:tsp) {
            tspTree.getChildren().add(new TreeItem(prov));
        }
        if(!tsp.isEmpty())
        {
            root.getChildren().add(tspTree);
        }

        //creo figlio per i tpip
        TreeItem typeTree=new TreeItem("types");
        for (Object type:types) {
            typeTree.getChildren().add(new TreeItem(type));
        }
        if(!types.isEmpty())
        {
            root.getChildren().add(typeTree);
        }

        //creo figlio per lo stato
        TreeItem statusTree=new TreeItem("status");
        for (Object state:status) {
            statusTree.getChildren().add(new TreeItem(state));
        }
        if(!status.isEmpty())
        {
            root.getChildren().add(statusTree);
        }

        selectedFilters.setRoot(root);
    }


    public void setFilters(ServiceFilter filter)
    {
        ObservableList<String> nations = FXCollections.observableArrayList(filter.countries().get());
        nationCCB.getItems().addAll(nations);

        ObservableList<Integer> tsp = FXCollections.observableArrayList(filter.providers().get());
        tspCCB.getItems().addAll(tsp);

        ObservableList<String> types = FXCollections.observableArrayList(filter.types().get());
        typeCCB.getItems().addAll(types);

        ObservableList<String> status = FXCollections.observableArrayList(filter.statuses().get());
        statusCCB.getItems().addAll(status);


    }

}









