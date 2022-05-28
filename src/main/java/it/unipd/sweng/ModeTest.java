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
        type.add("madonna ");
        type.add("cristo");
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

        System.out.println(filter.countries().toString());
        System.out.println(filter.providers().toString());
        System.out.println(filter.types().toString());
        System.out.println(filter.statuses().toString());

        return  null;
    }



}
