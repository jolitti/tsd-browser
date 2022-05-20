package lib.interfaces;

import lib.Service;
import lib.ServiceFilter;

import java.util.List;
import java.util.Map;

public interface ModelInterface {

    public Map<String,String> getCountryCodeToNames(); // Es: "FR" to "France"
    public Map<Integer,String> getCodeToProviderNames(); // Integer provider code to actual name

    public ServiceFilter getComplementaryFilter(ServiceFilter partial);
    public List<Service> getServices(ServiceFilter filter);

}
