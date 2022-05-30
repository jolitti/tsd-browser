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
      nationCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
          @Override
          public void onChanged(ListChangeListener.Change<? extends String> nat) {
              checkall(nationCCB,nat);
          }
      });


        //provider
        tspCCB.setTitle("tsp");
        tspCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> prov) {
                checkall(tspCCB,prov);
            }
        });

        //tipo di servizio
        typeCCB.setTitle("type");
        typeCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> ty) {
                checkall(typeCCB,ty);
            }
        });



        //stato del servizi
        statusCCB.setTitle("status");
        statusCCB.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> stat) {
                checkall(statusCCB,stat);
            }
        });

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
        nations.add(0,"select all");
        nationCCB.getItems().addAll(nations);

        ObservableList<Integer> tsp = FXCollections.observableArrayList(filter.providers().get());
        tsp.add(0,9999);// TODO 9999 usato come "selectall" perche per ora sono int, da cambiare appena ho la mappa
        tspCCB.getItems().addAll(tsp);

        ObservableList<String> types = FXCollections.observableArrayList(filter.types().get());
        types.add(0,"select all");
        typeCCB.getItems().addAll(types);

        ObservableList<String> status = FXCollections.observableArrayList(filter.statuses().get());
        status.add(0,"select all");
        statusCCB.getItems().addAll(status);



    }

    public void test()
    {
        System.out.println("www");
    }

    public void checkall(CheckComboBox ccb, ListChangeListener.Change change)
    {
        change.next();
       if(change.getAddedSubList().contains(ccb.getItems().get(0)) && !change.getRemoved().contains(ccb.getItems().get(0)))
       {//getAddedSubList da errore di out of bound exceptions ma penso sia per un bug in controlsfx DA VERIFICARE 
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

}









