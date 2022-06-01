package it.unipd.sweng;




import lib.interfaces.ModelInterface;
import lib.Service;
import lib.ServiceFilter;
import java.util.*;

public class ModeTest implements ModelInterface{

    @Override
    public Map<String, String> getCountryCodeToNames() {
        return null;
    }

    @Override
    public Map<Integer, String> getCodeToProviderNames() {
        return null;
    }

    @Override
    public ServiceFilter getComplementaryFilter(ServiceFilter partial) {

        LinkedList<String> nations=new LinkedList<String>();
        nations.add("italia");
        nations.add("spagna");
        nations.add("uk");
        Optional<List<String>>  nat= Optional.of(nations);

        LinkedList<Integer> tsp=new LinkedList<Integer>();
        tsp.add(1);
        tsp.add(2);
        tsp.add(3);
        Optional<List<Integer>> t= Optional.of(tsp);

        LinkedList<String> type=new LinkedList<String>();
        type.add("time Stamp");
        type.add("pec ");
        type.add("esign");
        Optional<List<String>>  ty= Optional.of(type);

        LinkedList<String> status=new LinkedList<String>();
        status.add("granted");
        status.add("windrawn ");
        Optional<List<String>> st= Optional.of(status);


         ServiceFilter filter= new ServiceFilter(nat,t,ty,st);
         return filter;

    }

    @Override
    public List<Service> getServices(ServiceFilter filter) {

        LinkedList<Service> services =new LinkedList<Service>();

        services.add(new Service(0,0,"ita","servizio ita 1","pec","granted","tob1",null));
        services.add(new Service(0,1,"ita","servizio ita 2","esign","witdrawn","tob2",null));
        services.add(new Service(1,2,"spa","servizio spa 1","pec","granted","tob3",null));
        services.add(new Service(1,3,"spa","servizio spa 2","boh","granted","tob4",null));
        return services;

    }



}
