package it.unipd.sweng;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import lib.Service;
import lib.ServiceFilter;
import lib.interfaces.ModelInterface;
import org.controlsfx.control.CheckComboBox;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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

    public void changeToSearchScene() throws IOException
    {
        Stage stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/it.unipd.sweng/Page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MainController controller=fxmlLoader.getController();
        stage=(Stage)search.getScene().getWindow();
        IndexedCheckModel nations=nationCCB.getCheckModel();
        IndexedCheckModel tsp=tspCCB.getCheckModel();
        IndexedCheckModel types=typeCCB.getCheckModel();
        IndexedCheckModel status=statusCCB.getCheckModel();

        IndexedCheckModel[] models=new IndexedCheckModel[4];
        models[0]=nations;
        models[1]=tsp;
        models[2]=types;
        models[3]=status;
        controller.initFilters(models);
        stage.setScene(scene);
        stage.show();
    }





}
