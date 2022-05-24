package lib.interfaces;

import lib.Service;
import lib.ServiceFilter;

import java.util.List;
import java.util.Map;

public interface ModelInterface {

    public Map<String,String> getCountryCodeToNames(); // Es: "FR" to "France"
    public Map<Integer,String> getCodeToProviderNames(); // Integer provider code to actual name

    /**
     * Given a partially filled ServiceFilter, return a filter where the null fields
     * are completed with every parameter where adding just that to the filter is sure to
     * still maintain a valid one
     * @param partial the partially filled filter
     * @return
     */
    public ServiceFilter getComplementaryFilter(ServiceFilter partial);

    /**
     * Get a list of all the Services that satisfy the given filter
     * @param filter the filter (partial or complete) that services must satisfy
     * @return list of Services that conform to filter
     */
    public List<Service> getServices(ServiceFilter filter);

}